package oneapp.incture.workbox.demo.adapter_salesforce.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.ApproveRequestDto;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.RestResponse;

@Component
public class ApproveTaskInSalesforce {
	
	private String accessToken;
	
	@Autowired
	SalesforceTokenGenerator salesforceTokenGenerator;
	
	@Autowired
	RestUserSalesforce restUserSalesforce;
	
	public Object approveTask(ApproveRequestDto requestDto) {
		
		Object approvalResponse = approveTask(requestDto,SalesforceConstant.REST_URL_SALESFORCE+
				SalesforceConstant.REST_URL_FOR_APPROVAL);

		Object res = (JSONObject)((JSONArray)approvalResponse).get(0);
		
		return res;
	}

	public Object getWorkItem(String requestUrl) {

		Object responseObject = null;


		try{

			accessToken = salesforceTokenGenerator.accesstokenUsingUserCredentials(
					SalesforceConstant.USERNAME,SalesforceConstant.PASSWORD);

			RestResponse restResponse = null;

			if (!ServicesUtil.isEmpty(requestUrl)) {
				System.err.println("[WBP-Dev][WorkItem][URL]"+requestUrl);
				restResponse = restUserSalesforce.callRestService(requestUrl,accessToken);
			}

			if(restResponse != null)
				responseObject = restResponse.getResponseObject();

			System.err.println("[WBP-Dev][Salesforce][ProcessInstance]"+responseObject);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][Salesforce fetchUsers][error]" + e.getLocalizedMessage());
		}

		return responseObject;
	}

	private Object approveTask(ApproveRequestDto requestDto, String requestUrl) {

		Object responseObject = null;

		try{
		
			RestResponse restResponse = null;
			accessToken = salesforceTokenGenerator.accesstokenUsingUserCredentials(
					SalesforceConstant.USERNAME2,SalesforceConstant.PASSWORD2);

			if (!ServicesUtil.isEmpty(requestUrl)) {
				restResponse = restUserSalesforce.callPostRestService(requestUrl,accessToken,requestDto);
			}
			if(restResponse != null)
				responseObject = restResponse.getResponseObject();

			System.err.println("[WBP-Dev][Salesforce][ProcessInstance]"+responseObject);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][Salesforce fetchUsers][error]" + e.getLocalizedMessage());
		}

		return responseObject;
		
	}

}
