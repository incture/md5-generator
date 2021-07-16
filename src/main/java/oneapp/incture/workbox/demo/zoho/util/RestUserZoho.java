package oneapp.incture.workbox.demo.zoho.util;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;



@Component
public class RestUserZoho {

	public RestResponse callGetService(String requestUrl, String accessToken, Map<String, String> parameters) {

		RestResponse restResponse = null;
		HttpResponse response = null;
		HttpClient client = null;
		

		try {
			client = new DefaultHttpClient();
			URIBuilder uriBuilder = new URIBuilder(requestUrl);
			restResponse = new RestResponse();
			
			//Setting parameters in the URL
			for (Entry<String, String> parameter : parameters.entrySet()) {
				System.err.println(parameter.getKey() +  " " + parameter.getValue());
				uriBuilder.setParameter(parameter.getKey(), parameter.getValue());		
			}
			
			//Calling the get service
			System.err.println(uriBuilder.build());
			HttpGet request = new HttpGet(uriBuilder.build());
			request.setHeader("Authorization", "Bearer " + accessToken);
			request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			response = client.execute(request);
			System.err.println(response);
			
			
			HttpEntity entity = response.getEntity();
			String data = EntityUtils.toString(entity);
			restResponse.setResponseObject(data);
			restResponse.setResponseCode(response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			System.err.println("[WBP-Dev]RestUserZoho.callGetService() : " + e.getMessage());
		}
		return restResponse;
	}
	


}
