package oneapp.incture.workbox.demo.sapAriba.util;

import org.json.JSONObject;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

public class AribaOAuthTokenGenerator {

	public static String[] getToken() {
		String res[] = new String[2];

		String oAuthTokenUrl = "https://api.mn1.ariba.com/v2/oauth/token";
		String oAuthClientId = "340f7127-5bf4-4a7d-977d-911d726073e9";
		String oAuthClientSecret = "PYrihnl2bASbp1ZhELS7T7YyXv0DQKur";
		String grantType = "openapi_2lo";
		String contentType = "application/x-www-form-urlencoded";

		String body = "grant_type=openapi_2lo";

		RestResponse restResponse2 = SCPRestUtil.callRestService(oAuthTokenUrl, null, body, "POST", contentType, false,
				null, oAuthClientId, oAuthClientSecret, null, null);

		System.err.println("AribaOAuthTokenGenerator.getToken() token response : " + restResponse2);
		if (!ServicesUtil.isEmpty(restResponse2) && !ServicesUtil.isEmpty(restResponse2.getResponseObject())) {
			
			JSONObject result = (JSONObject) restResponse2.getResponseObject();
//			System.out.println("AribaOAuthTokenGenerator.getToken() result : "+result);
//			System.out.println("AribaOAuthTokenGenerator.getToken() token : " + result.getString("access_token"));
//			System.out.println("AribaOAuthTokenGenerator.getToken() token type : " + result.getString("token_type"));
			res[0] = result.getString("access_token");
			res[1] = result.getString("token_type");

		}

		return res;

	}

	public static void main(String[] args) {
		System.out.println("AribaOAuthTokenGenerator.main() token : "+getToken()[0]+ " token type : "+getToken()[1]);
	}

}
