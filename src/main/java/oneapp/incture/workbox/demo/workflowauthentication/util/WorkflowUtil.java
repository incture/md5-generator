package oneapp.incture.workbox.demo.workflowauthentication.util;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.cloud.security.xsuaa.token.XsuaaToken;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;


@Service
public class WorkflowUtil {

	@Autowired
	private DestinationAccessHelper destinationAccessHelper;

	public JSONObject triggerWorkflow(String input, XsuaaToken token, String destinationUrl)
			throws ClientProtocolException, IOException, JSONException {

		// String res =
		// destinationAccessHelper.readDestinationDestination("workflow_destination",
		// "", token);
		String res = destinationAccessHelper.readDestinationDestination("workflow_desc", "", token,null);
		System.err.println("Poli check :" + res);

		JSONObject resObj = new JSONObject(res);

		System.err.println("check :" + resObj.optJSONArray("authTokens"));
		System.err.println("check :" + resObj.optJSONArray("authTokens").toString());
		System.err.println("start");

		HttpResponse httpResponse = null;
		String jsonString = null;
		JSONObject responseObj = null;

		HttpRequestBase httpRequestBase = null;
		StringEntity data = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		// String xCsrfToken = getWorkflowToken(token, destinationUrl, httpClient,
		// resObj);

		httpRequestBase = new HttpPost(destinationUrl + WorkflowConstants.WORKFLOW_TRIGGER_URL);

		data = new StringEntity(input);
		data.setContentType(WorkflowConstants.CONTENT_TYPE);

		((HttpPost) httpRequestBase).setEntity(data);
		// httpRequestBase.addHeader(WorkflowConstants.X_CSRF_TOKEN, xCsrfToken);
		httpRequestBase.addHeader(WorkflowConstants.ACCEPT, WorkflowConstants.CONTENT_TYPE);
		// httpRequestBase.addHeader(WorkflowConstants.AUTHORIZATION,
		// resObj.optJSONArray("authTokens").toString());

		JSONArray auth = resObj.optJSONArray("authTokens");
		int len = auth.length();

		for (int i = 0; i < len; i++) {
			JSONObject obj = auth.getJSONObject(i);
			JSONObject headObj = obj.optJSONObject("http_header");
			httpRequestBase.addHeader(headObj.optString("key"), headObj.optString("value"));
		}

		httpResponse = httpClient.execute(httpRequestBase);

		jsonString = EntityUtils.toString(httpResponse.getEntity());

		System.err.println("Poil res1 :" + httpResponse.getStatusLine().getStatusCode());

		if (httpResponse.getStatusLine().getStatusCode() == 400) {
			System.err.println("WorkflowInvoker | triggerWorkflow | Error :" + input);
		}

		responseObj = new JSONObject(jsonString);

		System.err.println("Poil res :" + responseObj);

		httpClient.close();
		return responseObj;

	}

	public RestResponse actionOnTask(String input, XsuaaToken token, String destinationUrl, String taskInstanceId)
			throws ClientProtocolException, IOException, JSONException {

					
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		String res = destinationAccessHelper.readDestinationDestination("cp_poc_workflow_destination", "", token,httpClient);
		System.err.println("Poli check :" + res);

		JSONObject resObj = new JSONObject(res);

		HttpResponse httpResponse = null;
		RestResponse response = new RestResponse();
		HttpRequestBase httpRequestBase = null;
		StringEntity data = null;
		

		// String xCsrfToken = getWorkflowToken(authentication, destinationUrl,
		// httpClient);
		if(taskInstanceId!=null) {
		httpRequestBase = new HttpPatch(destinationUrl + taskInstanceId);
		System.err.println(destinationUrl+taskInstanceId);
		}
		else {
			httpRequestBase = new HttpPatch(destinationUrl);
			System.err.println(destinationUrl);
			
		}
		if(input!=null) {
			data = new StringEntity(input, "UTF-8");
			data.setContentType(WorkflowConstants.CONTENT_TYPE);
	
			((HttpPatch) httpRequestBase).setEntity(data);
		}
		// httpRequestBase.addHeader(WorkflowConstants.X_CSRF_TOKEN, xCsrfToken);
		httpRequestBase.addHeader(WorkflowConstants.ACCEPT, WorkflowConstants.CONTENT_TYPE);
		// httpRequestBase.addHeader(WorkflowConstants.AUTHORIZATION, authentication);

		JSONArray auth = resObj.optJSONArray("authTokens");
		int len = auth.length();

		for (int i = 0; i < len; i++) {
			JSONObject obj = auth.getJSONObject(i);
			JSONObject headObj = obj.optJSONObject("http_header");
			httpRequestBase.addHeader(headObj.optString("key"), headObj.optString("value"));
			System.err.println("Key :   "+headObj.optString("key")+"value"  +headObj.optString("value"));
		}
		
		System.err.println(httpRequestBase.getAllHeaders());
		httpResponse = httpClient.execute(httpRequestBase);
		System.err.println(httpResponse.getStatusLine().getStatusCode());

		if (httpResponse.getStatusLine().getStatusCode() == 400) {
			System.err.println("WorkflowInvoker | approveTask | Error :" + input);
		}

		httpClient.close();
		response.setHttpResponse(httpResponse);
		response.setResponseCode(httpResponse.getStatusLine().getStatusCode());
		
		return response;
		
	}

	public String getWorkflowToken(XsuaaToken xsuaaToken, String destinationUrl, CloseableHttpClient httpClient,
			JSONObject resObj) throws ClientProtocolException, IOException {

		String xCsrfToken = "";
		HttpResponse httpResponse = null;
		HttpRequestBase httpRequestBase = null;

		httpRequestBase = new HttpGet(destinationUrl + WorkflowConstants.XSCRF_TOKEN_URL);

		httpRequestBase.setHeader(WorkflowConstants.X_CSRF_TOKEN, WorkflowConstants.FETCH);
		httpRequestBase.addHeader(WorkflowConstants.AUTHORIZATION, resObj.optJSONArray("authTokens").toString());

		httpResponse = httpClient.execute(httpRequestBase);

		System.err.println("Poil Token :" + httpResponse.getStatusLine().getStatusCode());

		Header[] headers = httpResponse.getAllHeaders();

		System.err.println("Poil Headers :" + headers);

		for (Header header : headers) {
			if (header.getName().equalsIgnoreCase(WorkflowConstants.X_CSRF_TOKEN)) {
				xCsrfToken = header.getValue();
				break;
			}
		}

		return xCsrfToken;
	}

	public String getTaskInstances(XsuaaToken token, String destinationUrl)
			throws ClientProtocolException, IOException, JSONException {

		// String res =
		// destinationAccessHelper.readDestinationDestination("workflow_destination",
		// "", token);
		String res = destinationAccessHelper.readDestinationDestination("workflow_desc", "", token,null);
		System.err.println("Poli check :" + res);

		JSONObject resObj = new JSONObject(res);

		System.err.println("check :" + resObj.optJSONArray("authTokens"));
		System.err.println("check :" + resObj.optJSONArray("authTokens").toString());
		System.err.println("start");

		HttpResponse httpResponse = null;
		String jsonString = null;
		JSONObject responseObj = null;

		HttpRequestBase httpRequestBase = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		// String xCsrfToken = getWorkflowToken(token, destinationUrl, httpClient,
		// resObj);

		httpRequestBase = new HttpGet(destinationUrl + "/rest/v1/workflow-instances");

		// httpRequestBase.addHeader(WorkflowConstants.X_CSRF_TOKEN, xCsrfToken);
		httpRequestBase.addHeader(WorkflowConstants.ACCEPT, WorkflowConstants.CONTENT_TYPE);
		// httpRequestBase.addHeader(WorkflowConstants.AUTHORIZATION,
		// resObj.optJSONArray("authTokens").toString());

		JSONArray auth = resObj.optJSONArray("authTokens");
		int len = auth.length();

		for (int i = 0; i < len; i++) {
			JSONObject obj = auth.getJSONObject(i);
			JSONObject headObj = obj.optJSONObject("http_header");
			httpRequestBase.addHeader(headObj.optString("key"), headObj.optString("value"));
		}

		httpResponse = httpClient.execute(httpRequestBase);

		jsonString = EntityUtils.toString(httpResponse.getEntity());

		System.err.println("Poil res1 :" + httpResponse.getStatusLine().getStatusCode());

		responseObj = new JSONObject(jsonString);

		System.err.println("Poil res :" + responseObj);

		httpClient.close();
		return responseObj.toString();

	}

}
