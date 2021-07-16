package oneapp.incture.workbox.demo.docusign.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;


@Component
public class RestCall {
	
	public RestResponse callGetService(String requestUrl,String accessToken){
		RestResponse restResponse = new RestResponse();
		JSONObject jsonObject = null;
		HttpResponse response = null;
		int code = 500;
		try{
			HttpGet get =new HttpGet(requestUrl);
			get.setHeader("Accept", "application/json");
			get.setHeader("Authorization", "Bearer " + accessToken);
	
			HttpClient httpClient = new DefaultHttpClient();
			response = httpClient.execute(get);
	
			code = response.getStatusLine().getStatusCode();
			System.err.println(response);
			if(code==200){
				String responsejson = EntityUtils.toString(response.getEntity());
				jsonObject = new JSONObject(responsejson);
				restResponse.setResponseObject(jsonObject);
				restResponse.setHttpResponse(response);
				restResponse.setResponseCode(code);
				
				return restResponse;
			}
			
		}catch (ClientProtocolException e) {
			System.err.println("[WBP-Dev][Docusign][EnvelopeService][callGetService][ClientProtocolException]Error");
		}catch (IOException e) {
			System.err.println("[WBP-Dev][Docusign][EnvelopeService][callGetService][EntityIOException]Error");
		}
		restResponse.setResponseCode(code);
		restResponse.setHttpResponse(response);
		return restResponse;
	}
	
	
	public RestResponse callPostService(String requesturl,String accessToken,String payload){
		int code=500;
		RestResponse restResponse = new RestResponse();
		HttpResponse response = null;
		try{
			//URIBuilder signignBuilder = new URIBuilder(DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes/"+envelopeId+"/views/recipient");
			//HttpPost Signingpost = new HttpPost(DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes/"+envelopeId+"/views/recipient");
			HttpPost post = new HttpPost(requesturl);
			post.setHeader("Accept","application/json");
			post.setHeader("Authorization","Bearer "+accessToken);
			
			StringEntity entity = new StringEntity(payload,ContentType.APPLICATION_JSON);
			

			
			post.setEntity(entity);
			
			
			HttpClient client = new DefaultHttpClient();
			response = client.execute(post);
			code = response.getStatusLine().getStatusCode();
			System.err.println(response);
			JSONObject jsonObject =new JSONObject(EntityUtils.toString(response.getEntity()));
			restResponse.setHttpResponse(response);
			restResponse.setResponseCode(code);
			restResponse.setResponseObject(jsonObject);
			return restResponse;
		}catch (ClientProtocolException e) {
			System.err.println("[WBP-Dev][Docusign][EnvelopeService][getSigningUrlPOST][ClientProtocolException]Error");
		}catch (IOException e) {
			System.err.println("[WBP-Dev][Docusign][EnvelopeService][getSigningUrlPOST][EntityIOException]Error");
		}
		restResponse.setHttpResponse(response);
		restResponse.setResponseCode(code);
		restResponse.setResponseObject(response);
		return restResponse;
	}
	
	

}
