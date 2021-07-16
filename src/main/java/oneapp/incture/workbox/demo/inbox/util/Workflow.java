package oneapp.incture.workbox.demo.inbox.util;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstantsStatic;
import oneapp.incture.workbox.demo.adapter_base.util.RestUtil;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;


@Component
public class Workflow {

	@Autowired
	PropertiesConstants getProperty;

	private static String[] getToken() {
		String[] result = new String[2];
		try {
			//String requestUrl = "https://oauthasservices-kbniwmq1aj.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";

			String requestUrl = PropertiesConstantsStatic.NETWORK_REQUEST_URL;
			String userId=PropertiesConstantsStatic.NETWORK_USER_ID;
			String password=PropertiesConstantsStatic.NETWORK_PASSWORD;
			//e5422b86-1f6f-33a6-a6b6-2bd5cabde2a1 , Workbox@123---> Incture workbox demo
			//00b2004f-5b09-3656-a808-19c174dc2f6d, 123456---------->Incture workbox qa

			Object responseObject = RestUtil.callRestService(requestUrl, null, null, "POST", "application/json",
					false, null, userId, password, null, null).getResponseObject();
			JSONObject resources = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
			if(resources != null){
				result[0] = resources.optString("access_token");
				result[1] = resources.optString("token_type");
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][getPrevRecipient][error]" + e.getMessage());
		}
		return result;
	}

	public RestResponse getContextData(String taskId) {
		String token[] = getToken();
		String url = getProperty.getValue("REQUEST_URL_INST")+"task-instances/"+taskId+"/context";
		RestResponse response = null;
		try {
			response = RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json", false, null, null, null, token[0], token[1]);
		}catch(Exception e) {
			System.err.println("[WBP-Dev][Workflow][getContextData()] "+e.getMessage());
		}
		return response;
	}
}
