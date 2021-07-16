package oneapp.incture.workbox.demo.adapter_salesforce.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.ApproveRequestDto;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.RestResponse;

@Component
public class RestUserSalesforce {

	public RestResponse callRestService(String requestUrl, String accessToken) {

		RestResponse restResponse = null;
		JSONObject obj = null;
		Object returnJson = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;

		try{

			if(requestUrl != null){
				restResponse = new RestResponse();

				httpRequestBase = new HttpGet(requestUrl);
				httpRequestBase.addHeader("Authorization","Bearer " + accessToken);
				httpResponse = httpClient.execute(httpRequestBase);

				String jsonRespose = EntityUtils.toString(httpResponse.getEntity());

				if (!ServicesUtil.isEmpty(jsonRespose)) {
					returnJson = new Object[2];
					if (jsonRespose.charAt(0) == '{') {
						obj = new JSONObject(jsonRespose);
						returnJson = obj;
					}

					restResponse.setResponseObject(returnJson);

				}

				restResponse.setHttpResponse(httpResponse);
				restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
				httpClient.close();

			}


		}catch (Exception e) {
			System.err.println("[WBP-Dev]RestUserSalesforce.getAllUsersInSalesforce() : "+e);
		}

		return restResponse;
	}

	public RestResponse callPostRestService(String requestUrl, String accessToken, ApproveRequestDto requestDto) {

		RestResponse restResponse = null;
		JSONArray obj = null;
		Object returnJson = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpRequestBase = null;
		HttpResponse httpResponse = null;

		try{

			if(requestUrl != null){
				restResponse = new RestResponse();

				httpRequestBase = new HttpPost(requestUrl);
				httpRequestBase.addHeader("Authorization","Bearer " + accessToken);

				ObjectMapper mapper = new ObjectMapper();
				String simpleJSON = mapper.writeValueAsString(requestDto);
				StringEntity params =new StringEntity(simpleJSON);
				httpRequestBase.addHeader("content-type","application/json");
				httpRequestBase.setEntity(params);
				httpResponse = httpClient.execute(httpRequestBase);

				String jsonRespose = EntityUtils.toString(httpResponse.getEntity());

				System.err.println("[WBP-Dev][Salesforce]"+httpResponse.getStatusLine());

				if (!ServicesUtil.isEmpty(jsonRespose)) {
					returnJson = new Object[2];
					if (jsonRespose.charAt(0) == '[') {
						obj = new JSONArray(jsonRespose);
						returnJson = obj;
					}

					restResponse.setResponseObject(returnJson);

				}

				restResponse.setHttpResponse(httpResponse);
				restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
				httpClient.close();

			}


		}catch (Exception e) {
			System.err.println("[WBP-Dev]RestUserSalesforce.getAllUsersInSalesforce() : "+e.getMessage());
		}

		return restResponse;
	}




}
