package oneapp.incture.workbox.demo.sapAriba.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Component
public class RestUserSapAriba {

	public RestResponse callRestService(String requestUrl, String accessToken) {

		RestResponse restResponse = null;
		JSONObject obj = null;
		Object returnJson = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		JSONArray array = null;

		try{

			if(requestUrl != null){
				restResponse = new RestResponse();

				httpRequestBase = new HttpGet(requestUrl);
				httpRequestBase.addHeader("Authorization","Bearer " + accessToken);
				httpRequestBase.addHeader("apiKey",SapAribaConstant.API_KEY);
				httpRequestBase.addHeader("Content-Type",SapAribaConstant.JSON);
				httpResponse = httpClient.execute(httpRequestBase);

				String jsonRespose = EntityUtils.toString(httpResponse.getEntity());
				System.err.println(jsonRespose);
				try{
					if (!ServicesUtil.isEmpty(jsonRespose)) {
						returnJson = new Object[2];
						if (jsonRespose.charAt(0) == '{') {
							obj = new JSONObject(jsonRespose);
							returnJson = obj;
						}else if (jsonRespose.charAt(0) == '[') {
							array = new JSONArray(jsonRespose);
							returnJson = array;
						}

						restResponse.setResponseObject(returnJson);

					}
					
					restResponse.setHttpResponse(httpResponse);
					restResponse.setResponseCode(httpResponse.getStatusLine().getStatusCode());
					httpClient.close();

				}catch (JSONException e) {

					System.err.println("[Workbox]JSONException : " + e + "JSON Object : " + jsonRespose);
				}
			}


		}catch (Exception e) {
			System.err.println("RestUserSalesforce.getAllUsersInSalesforce() : "+e);
		}

		return restResponse;
	}



}
