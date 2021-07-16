package oneapp.incture.workbox.demo.manageGroup.util;

import java.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

public class IASRestUtil {
	
	private IASRestUtil() {
	    throw new IllegalAccessError("IASRestUtil class");
	  }
	
	public static RestResponse callIASRestService(String url, String userName, String password, Integer startIndex, String groupId) {
		
		RestResponse restResponse = null;
		JSONObject obj = null;
		Object returnJson = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		
		try{

			if(url != null){
				restResponse = new RestResponse();
				if(ServicesUtil.isEmpty(groupId))
					url = url+"/service/scim/Groups?startIndex="+startIndex;
				else
					url = url+"/service/scim/Groups/"+groupId;
				httpRequestBase = new HttpGet(url);
				String userpass = userName + ":" + password;
				String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
				httpRequestBase.addHeader("Authorization", basicAuth);
				httpResponse = httpClient.execute(httpRequestBase);

				String jsonRespose = EntityUtils.toString(httpResponse.getEntity());
				System.err.println(jsonRespose);
				
				try{
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

				}catch (JSONException e) {

					System.err.println("[WBP-Dev][Workbox]JSONException : " + e + "JSON Object : " + jsonRespose);
				}
			}


		}catch (Exception e) {
			System.err.println("[WBP-Dev]RestUserSalesforce.getAllUsersInSalesforce() : "+e);
		}

		return restResponse;
	}

}
