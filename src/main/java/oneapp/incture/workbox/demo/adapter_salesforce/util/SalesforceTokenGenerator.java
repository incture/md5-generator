package oneapp.incture.workbox.demo.adapter_salesforce.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@SuppressWarnings("deprecation")
@Component
public class SalesforceTokenGenerator {

	@SuppressWarnings({ "resource" })
	public String accesstokenUsingUserCredentials(String username, String password)  throws IOException{

		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpClientParams.setCookiePolicy(params, CookiePolicy.RFC_2109);
		params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 30000);

		HttpPost oauthPost = new HttpPost(SalesforceConstant.SALESFORCE_TOKEN_URL);

		List<BasicNameValuePair> parametersBody = new ArrayList<>();

		parametersBody.add(new BasicNameValuePair("grant_type", SalesforceConstant.GRANT_TYPE));

		parametersBody.add(new BasicNameValuePair("username", username));

		parametersBody.add(new BasicNameValuePair("password", password));

		parametersBody.add(new BasicNameValuePair("client_id", SalesforceConstant.CLIENT_ID));

		parametersBody.add(new BasicNameValuePair("client_secret", SalesforceConstant.CLIENT_SECRET));

		oauthPost.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

		HttpResponse response = client.execute(oauthPost);

		int code = response.getStatusLine().getStatusCode();

		System.err.println("[WBP-Dev][WORKBOX- Salesforce][ResponseCode][Approve]" + code);

		String accessToken = "";
		
		if(code == 200){
			String tokenResponse = EntityUtils.toString(response.getEntity());
			JSONObject myObject = new JSONObject(tokenResponse);
			accessToken =myObject.getString("access_token");
		}
		else{
			return SalesforceConstant.FAILURE;
		}

		return accessToken;
	}


}
