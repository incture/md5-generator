package oneapp.incture.workbox.demo.workflowauthentication.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.util.Base64;
import com.sap.cloud.security.xsuaa.token.XsuaaToken;

import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.jdbc.CfJdbcEnv;

@Component
public class DestinationAccessHelper {

	// @Value("${vcap.services.destination-service.credentials.uri}")
	private String uri;

	// @Value("${vcap.services.destination-service.credentials.url}")
	private String tokenUrl;

	// @Value("${vcap.services.destination-service.credentials.clientid}")
	private String clientId;

	// @Value("${vcap.services.destination-service.credentials.clientsecret}")
	private String clientSecret;

	public String accessToken() throws JsonMappingException, JsonProcessingException {

		CfJdbcEnv cfJdbcEnv = new CfJdbcEnv();
		CfCredentials cfCredentials = cfJdbcEnv.findCredentialsByTag("destination");

		System.err.println("Got desc connection");

		Map<String, Object> map = cfCredentials.getMap();

		uri = (String) map.get("uri");
		tokenUrl = (String) map.get("url");
		clientId = (String) map.get("clientid");
		clientSecret = (String) map.get("clientsecret");

		System.err.println("Destination datails" + map);

		String url = tokenUrl + "/oauth/token?grant_type=client_credentials";
		RestTemplate template = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		Base64 encode = Base64.encode(clientId + ":" + clientSecret);
		headers.add("Authorization", "Basic " + encode.toString());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = template.postForEntity(url, entity, String.class);
		System.err.println("res" + response);
		return new ObjectMapper().readTree(response.getBody()).get("access_token").asText();
	}

	public String readDestinationDestination(String destinationName, String body, XsuaaToken token,CloseableHttpClient httpClient)
			throws ClientProtocolException, IOException {
		System.err.println("DestinationAccessHelper.readDestinationDestination()");
		HttpHeaders headers = new HttpHeaders();
		String accessToken = accessToken();
		System.err.println("DestinationAccessHelper.readDestinationDestination() "+accessToken);
		headers.add("X-user-token", token.getTokenValue());
		headers.add("Authorization", "Bearer " + accessToken);
		// RestTemplate template = new RestTemplate();
		// HttpEntity<?> entity = new HttpEntity<>(headers);
		String url = uri + "/destination-configuration/v1/destinations/" + destinationName;

		HttpResponse httpResponse = null;
		String jsonString = null;
		HttpRequestBase httpRequestBase = null;
		if(httpClient==null) {
			httpClient = HttpClientBuilder.create().build();
		}

		httpRequestBase = new HttpGet(url);
		httpRequestBase.addHeader("X-user-token", token.getTokenValue());
		httpRequestBase.addHeader("Authorization", "Bearer " + accessToken);
		// httpRequestBase.addHeader(WorkflowConstants.AUTHORIZATION, token);

		httpResponse = httpClient.execute(httpRequestBase);

		jsonString = EntityUtils.toString(httpResponse.getEntity());

		System.err.println("Poil res1 :" + jsonString);

		return jsonString;
	}
//	public ResponseEntity<Destination> readDestinationDestination(String destinationName, String body, XsuaaToken token)
//			throws JsonMappingException, JsonProcessingException {
//		HttpHeaders headers = new HttpHeaders();
//		String accessToken = accessToken();
//		headers.add("X-user-token", token.getTokenValue());
//		headers.add("Authorization", "Bearer " + accessToken);
//		RestTemplate template = new RestTemplate();
//		HttpEntity<?> entity = new HttpEntity<>(headers);
//		String url = uri + "/destination-configuration/v1/destinations/" + destinationName;
//		return template.exchange(url, HttpMethod.GET, entity, Destination.class);
//	}

}
