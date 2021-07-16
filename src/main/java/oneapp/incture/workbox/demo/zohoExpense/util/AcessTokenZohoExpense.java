package oneapp.incture.workbox.demo.zohoExpense.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class AcessTokenZohoExpense {

	public String getAcessTokenByRefreshToken(String refreshToken) {

		String accessToken = "";

		try {
			URIBuilder signignBuilder = new URIBuilder(ZohoExpenseConstants.authUrl);
			signignBuilder.setParameter("refresh_token", refreshToken);
			signignBuilder.setParameter("client_id", ZohoExpenseConstants.clientId);
			signignBuilder.setParameter("client_secret", ZohoExpenseConstants.clientSecret);
			signignBuilder.setParameter("grant_type", ZohoExpenseConstants.grantType);

			HttpPost Signingpost = new HttpPost(signignBuilder.build());
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(Signingpost);

			String jsonString = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(jsonString);
			accessToken = jsonObject.getString("access_token");

		} catch (Exception e) {
			System.err.println("[WBP-Dev]AcessToken.getAcessToken() error " + e);
			return ZohoExpenseConstants.FAILURE;
		}

		return accessToken;

	}
	
	public String getAcessToken() {

		String accessToken = "";

		try {
			
			//setting the paramater in the URL
			URIBuilder signignBuilder = new URIBuilder(ZohoExpenseConstants.authUrl);
			signignBuilder.setParameter("refresh_token", ZohoExpenseConstants.refreshToken);
			signignBuilder.setParameter("client_id", ZohoExpenseConstants.clientId);
			signignBuilder.setParameter("client_secret", ZohoExpenseConstants.clientSecret);
			signignBuilder.setParameter("grant_type", ZohoExpenseConstants.grantType);

			HttpPost Signingpost = new HttpPost(signignBuilder.build());
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(Signingpost);

			String jsonString = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(jsonString);
			accessToken = jsonObject.getString("access_token");

		} catch (Exception e) {
			System.err.println("[WBP-Dev]AcessToken.getAcessToken() error " + e);
			return ZohoExpenseConstants.FAILURE;
		}

		return accessToken;

	}

}
