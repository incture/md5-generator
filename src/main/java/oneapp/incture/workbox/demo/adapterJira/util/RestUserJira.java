package oneapp.incture.workbox.demo.adapterJira.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;


@Component
public class RestUserJira {

	public RestResponse callPostService(String requestUrl, String payload) {
		RestResponse restResponse = null;
		StringEntity stringEntity = null;
		HttpResponse response = null;
		HttpClient client = null;

		try {
			if (requestUrl != null) {
				System.err.println("[WBP-Dev]RestUserJira.callPostService()");
				restResponse = new RestResponse();
				HttpPost signingpost = new HttpPost(requestUrl);
				signingpost.setHeader("Authorization",
						"Basic U2FuamFuYS5BU0BpbmN0dXJlLmNvbTpCbFljd2dESHJQbGEwemg4cmJuU0I3RDM=");
				signingpost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
				stringEntity = new StringEntity(payload, ContentType.APPLICATION_JSON);
				signingpost.setEntity(stringEntity);
				client = new DefaultHttpClient();
				response = client.execute(signingpost);
				System.err.println("[WBP-Dev]RestUserJira.callPostService() response : " + response);
				restResponse.setHttpResponse(response);
				restResponse.setResponseCode(response.getStatusLine().getStatusCode());

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]RestUserJira.callPostService() : " + e.getMessage());
		}

		return restResponse;
	}

	public RestResponse callPutService(String requestUrl, String payload) {
		RestResponse restResponse = null;
		StringEntity stringEntity = null;
		HttpResponse response = null;
		HttpClient client = null;

		try {
			if (requestUrl != null) {
				System.err.println("[WBP-Dev]RestUserJira.callPutService()");
				restResponse = new RestResponse();
				HttpPut signingpost = new HttpPut(requestUrl);
				signingpost.setHeader("Authorization",
						"Basic U2FuamFuYS5BU0BpbmN0dXJlLmNvbTpCbFljd2dESHJQbGEwemg4cmJuU0I3RDM=");
				signingpost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
				stringEntity = new StringEntity(payload, ContentType.APPLICATION_JSON);
				signingpost.setEntity(stringEntity);
				client = new DefaultHttpClient();
				response = client.execute(signingpost);
				System.err.println("[WBP-Dev]RestUserJira.callPutService() response : " + response);
				restResponse.setHttpResponse(response);
				restResponse.setResponseCode(response.getStatusLine().getStatusCode());

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]RestUserJira.callPutService() : " + e.getMessage());
		}

		return restResponse;
	}
	
	public RestResponse callGetResponse(String requestUrl) {
		
		RestResponse restResponse = null;
		HttpResponse response = null;
		HttpClient client = null;
		
		try {
			client = new DefaultHttpClient();
			restResponse = new RestResponse();
	        HttpGet request = new HttpGet(requestUrl);
	    	request.setHeader("Authorization","Basic U2FuamFuYS5BU0BpbmN0dXJlLmNvbTpCbFljd2dESHJQbGEwemg4cmJuU0I3RDM=");
	    	request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
	    	
	    	response=client.execute(request);
	    	System.err.println(response);
	    	restResponse.setHttpResponse(response);
			HttpEntity entity = response.getEntity();
			System.err.println(entity);
	    	String tokenResponse = EntityUtils.toString(entity);
	    	System.err.println(tokenResponse);
	    	JSONObject myObject=new JSONObject(tokenResponse);
	    	System.err.println(myObject);
	    	restResponse.setResponseObject(myObject);
		}
		catch (Exception e) {
			System.err.println("[WBP-Dev]RestUserJira.callGetService() : " + e.getMessage());
		}
		return restResponse;
	}
	
	public RestResponse callGetResponseArray(String requestUrl) {
		
		RestResponse restResponse = null;
		HttpResponse response = null;
		HttpClient client = null;
		
		try {
			client = new DefaultHttpClient();
			restResponse = new RestResponse();
	        HttpGet request = new HttpGet(requestUrl);
	    	request.setHeader("Authorization","Basic U2FuamFuYS5BU0BpbmN0dXJlLmNvbTpCbFljd2dESHJQbGEwemg4cmJuU0I3RDM=");
	    	request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
	    	
	    	response=client.execute(request);
	    	System.err.println(response);
	    	restResponse.setHttpResponse(response);
			HttpEntity entity = response.getEntity();
			System.err.println(entity);
	    	String tokenResponse = EntityUtils.toString(entity);
	    	System.err.println(tokenResponse);
//	    	JSONObject myObject=new JSONObject(tokenResponse);
//	    	JSONArray jsonArray = new JSONArray(tokenResponse);
//	    	System.err.println(jsonArray);
	    	restResponse.setResponseObject(tokenResponse);
		}
		catch (Exception e) {
			System.err.println("[WBP-Dev]RestUserJira.callGetService() : " + e.getMessage());
		}
		return restResponse;
	}
	
	public JSONObject getAPI() {
		RestResponse response = callGetResponse(JiraConstants.baseUrl + "/search?jql=project=WP");
		JSONObject myObject = (JSONObject) response.getResponseObject();
		return myObject;
	}
	
}
