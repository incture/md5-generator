package oneapp.incture.workbox.demo.sharepointFileUpload;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Joiner;


@Service
public class SharePointService {

    @Autowired
    private SharePointServiceCached serviceCached;

    @Autowired
    private RestTemplate restTemplate;

    public byte[] performHttpRequest(HttpMethod method, String path) throws Exception {
        Long executionDateTime = serviceCached.parseExecutionDateTime(new Date());
        String securityToken = serviceCached.receiveSecurityToken(executionDateTime);
        List<String> cookies = serviceCached.getSignInCookies(securityToken);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", Joiner.on(';').join(cookies));
        RequestEntity<String> requestEntity = new RequestEntity<>(headers, method, new URI(path));
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(requestEntity, byte[].class);
        byte[] responseBody = responseEntity.getBody();
        System.err.println(responseBody);
        return responseBody;
    }

    public String performHttpRequest(String path, String json, boolean isUpdate, boolean isWithDigest) throws Exception {
        Long executionDateTime = serviceCached.parseExecutionDateTime(new Date());
        String securityToken = serviceCached.receiveSecurityToken(executionDateTime);
        securityToken = securityToken.substring(2);
        List<String> cookies = serviceCached.getSignInCookies(securityToken);
        String formDigestValue = serviceCached.getFormDigestValue(cookies);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", Joiner.on(';').join(cookies));
        headers.add("Content-type", "application/json;odata=verbose");
        if (isWithDigest) {
            headers.add("X-RequestDigest", formDigestValue);
        }
        if (isUpdate) {
            headers.add("X-HTTP-Method", "MERGE");
            headers.add("IF-MATCH", "*");
        }
        RequestEntity<String> requestEntity = new RequestEntity<>(json, headers, HttpMethod.POST, new URI(path));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        System.err.println(responseBody);
        return responseBody;
    }

    public  String attachFile(String path, byte[] file) throws Exception {
        Long executionDateTime = serviceCached.parseExecutionDateTime(new Date());
        String securityToken = serviceCached.receiveSecurityToken(executionDateTime);
        List<String> cookies = serviceCached.getSignInCookies(securityToken);
        String formDigestValue = serviceCached.getFormDigestValue(cookies);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", Joiner.on(';').join(cookies));
        headers.add("X-RequestDigest", formDigestValue);
        headers.add("content-length", String.valueOf(file.length));
        ResponseEntity<String> responseEntity = null;
        RequestEntity<byte[]> requestEntity = new RequestEntity<>(file, headers, HttpMethod.POST, new URI(path));
		System.err.println("Got till here #2");
		responseEntity = restTemplate.exchange(requestEntity, String.class);
        String responseBody = responseEntity.getBody();
        System.err.println("Response Body of SharePoint: " + responseBody);
        return responseBody;
    }

}
