package oneapp.incture.workbox.demo.sharepointFileUpload;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthenticationException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.xml.transform.StringSource;
import org.springframework.xml.xpath.XPathExpression;
import org.w3c.dom.Document;

import com.google.common.base.Joiner;


@Service
public class SharePointServiceCached {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SharePointProperties sharePointProperties;

    @Autowired
    private XPathExpression xPathExpression;

    @Cacheable("CACHE_NAME_TOKEN")
    public String receiveSecurityToken(Long executionDateTime) throws Exception {
        RequestEntity<String> requestEntity = new RequestEntity<String>(buildSecurityTokenRequestEnvelope(), HttpMethod.POST, 
        		new URI(sharePointProperties.getEndpointToken() + "/extSTS.srf"));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        DOMResult result = new DOMResult();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new StringSource(responseEntity.getBody()), result);
        Document definitionDocument = (Document) result.getNode();

        String securityToken = xPathExpression.evaluateAsString(definitionDocument);
        if (StringUtils.isBlank(securityToken)) {
            throw new AuthenticationException("Unable to authenticate: empty token");
        }
        System.err.println("Microsoft Online respond with Token: " + securityToken);
        return securityToken;
    }

    private String buildSecurityTokenRequestEnvelope() {
    	System.err.println("SharePoint Properties, userName: " + sharePointProperties.getUsername() + 
    			", password: " + sharePointProperties.getPassword() + ", Endpoint Token: " + 
    			sharePointProperties.getEndpointToken() + ", Endpoint Domain: " + sharePointProperties.getEndpointDomain());
        String envelopeTemplate = "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" "
        		+ "xmlns:a=\"http://www.w3.org/2005/08/addressing\" "
        		+ "xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"> "
        		+ "<s:Header>  <a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</a:Action>  "
        		+ "<a:ReplyTo> <a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>  </a:ReplyTo> "
        		+ "<a:To s:mustUnderstand=\"1\">https://login.microsoftonline.com/extSTS.srf</a:To> <o:Security s:mustUnderstand=\"1\"  "
        		+ "xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"> "
        		+ "<o:UsernameToken>  <o:Username>%s</o:Username>  <o:Password>%s</o:Password> </o:UsernameToken>  "
        		+ "</o:Security> </s:Header><s:Body><t:RequestSecurityToken xmlns:t=\"http://schemas.xmlsoap.org/ws/2005/02/trust\">"
        		+ "<wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\"><a:EndpointReference><a:Address>" 
        		+ sharePointProperties.getEndpointDomain() + "</a:Address></a:EndpointReference>"
        		+ "</wsp:AppliesTo><t:KeyType>http://schemas.xmlsoap.org/ws/2005/05/identity/NoProofKey</t:KeyType>  "
        		+ "<t:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</t:RequestType> "
        		+ "<t:TokenType>urn:oasis:names:tc:SAML:1.0:assertion</t:TokenType></t:RequestSecurityToken></s:Body></s:Envelope>";
        return String.format(envelopeTemplate, sharePointProperties.getUsername(), sharePointProperties.getPassword());
    }

    @Cacheable("CACHE_NAME_COOKIE")
    public List<String> getSignInCookies(String securityToken) throws Exception {
        RequestEntity<String> requestEntity = new RequestEntity<String>(securityToken, HttpMethod.POST, new URI(
				 "https://incturet.sharepoint.com" //+  sharePointProperties.getEndpointDomain() 
        		+ "/_forms/default.aspx?wa=wsignin1.0"));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        HttpHeaders headers = responseEntity.getHeaders();
        List<String> cookies = headers.get("Set-Cookie");
        System.err.println("Got till here #1");
        if (CollectionUtils.isEmpty(cookies)) {
            throw new AuthenticationException("Unable to sign in: no cookies returned in response");
        }
        System.err.println("SharePoint respond with cookies: " + Joiner.on(", ").join(cookies));
        return cookies;
    }

    public String getFormDigestValue(List<String> cookies) throws IOException, URISyntaxException, TransformerException, JSONException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", Joiner.on(';').join(cookies));
        headers.add("Accept", "application/json;odata=verbose");
        headers.add("X-ClientService-ClientTag", "SDK-JAVA");
        RequestEntity<String> requestEntity = new RequestEntity<String>(headers, HttpMethod.POST, new URI(
        		sharePointProperties.getEndpointDomain() + "/_api/contextinfo"));
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        JSONObject json = new JSONObject(responseEntity.getBody());
        return json.getJSONObject("d")
                .getJSONObject("GetContextWebInformation")
                .getString("FormDigestValue");
    }

    public Long parseExecutionDateTime(Date dateTime) {
        if (dateTime == null)
            return null;
        final Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

}