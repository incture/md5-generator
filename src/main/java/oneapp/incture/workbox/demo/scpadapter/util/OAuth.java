package oneapp.incture.workbox.demo.scpadapter.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstantsStatic;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Component
public class OAuth {

	private static final Logger logger = LoggerFactory.getLogger(OAuth.class);

	public String[] getToken() {
		String[] result = new String[2];
		try {

			// String requestUrlDev =
			// "https://oauthasservices-kbniwmq1aj.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";
			// String requestUrlQa =
			// "https://oauthasservices-a8b98c03e.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";
			// https://oauthasservices-a8b98c03e.hana.ondemand.com/oauth2/api/v1/token
			String requestUrl = PropertiesConstantsStatic.NETWORK_REQUEST_URL;
			String userId = PropertiesConstantsStatic.NETWORK_USER_ID;
			String password = PropertiesConstantsStatic.NETWORK_PASSWORD;
			// e5422b86-1f6f-33a6-a6b6-2bd5cabde2a1 , Workbox@123----> Incture
			// workbox demo
			// 00b2004f-5b09-3656-a808-19c174dc2f6d,123456-----> Incture workbox
			// QA

			Object responseObject = RestUtil.callRestService(requestUrl, null, null, "POST", "application/json", false,
					null, userId, password, null, null).getResponseObject();
			JSONObject resources = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
			// JSONObject resources = (JSONObject) object;
			if (resources != null) {
				result[0] = resources.optString("access_token");
				result[1] = resources.optString("token_type");
			}
		} catch (Exception e) {
			logger.error("[PMC][SubstitutionRuleFacade][getPrevRecipient][error]" + e.getMessage());
		}
		return result;
	}

//	public static void main(String[] args) {
//		OAuth oAuth = new OAuth();
//		for (String str : oAuth.getToken()) {
//			System.out.println(str);
//		}
//	}

}
