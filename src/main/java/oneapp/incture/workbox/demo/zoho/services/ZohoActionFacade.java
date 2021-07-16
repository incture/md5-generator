package oneapp.incture.workbox.demo.zoho.services;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sap.security.um.user.User;

import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.zoho.dao.ZohoTaskEventsDao;
import oneapp.incture.workbox.demo.zoho.util.AccessTokenZoho;
import oneapp.incture.workbox.demo.zoho.util.RestUserZoho;
import oneapp.incture.workbox.demo.zoho.util.ZohoConstants;



@Component
public class ZohoActionFacade {

	@Autowired
	AccessTokenZoho accessToken;

	@Autowired
	RestUserZoho restUserZoho;
	
	@Autowired
	ZohoService zohoService;
	
	@Autowired
	private ZohoTaskEventsDao zohoTaskEventsDao;

	public ResponseMessage acceptOrRejectRequest(ActionDto actionDto, ActionDtoChild actionDtoChild , String actionType) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(ZohoConstants.FAILURE);
		responseMessage.setStatus(ZohoConstants.FAILURE);
		responseMessage.setStatusCode("1");

		try {
			
			//Getting loggedIn Users
			User user = UserManagementUtil.getLoggedInUser();
			String puserId = user.getName().toUpperCase();
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] puserId : " + puserId);
			
			//Getting the refresh token from the db
			String refreshTokenString = zohoService.fetchUsers(1).get(puserId).getRefreshToken();
			String accessTokenString = accessToken.getAcessTokenByRefreshToken(refreshTokenString);
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] accessTokenString : " + accessTokenString);
			
			//Getting the processId from taskId
			String taskId = actionDtoChild.getInstanceId().split("-")[0];
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] taskId : " + taskId);
			
			
			//setting parameters for the action request
			Map<String, String> actionParameters = new HashMap<>();
			actionParameters.put("status", actionType);
			actionParameters.put("remarks", actionDtoChild.getComment());
			actionParameters.put("responseVersion", "1");
			actionParameters.put("pkid", taskId);

			RestResponse response = restUserZoho.callGetService(ZohoConstants.baseUrl + "approveRecord",
					accessTokenString, actionParameters);
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] response : " + response);
			int statusCode = response.getResponseCode();
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] StatusCode : " + statusCode);
//			if (statusCode == 200) {
//				responseMessage.setMessage(ZohoConstants.APPROVE_MESSAGE);
//				responseMessage.setStatus(ZohoConstants.STATUS_SUCCESS);
//				responseMessage.setStatusCode("200");
//				
//				//updating task events and task owners
//				zohoTaskEventsDao.completeTaskStatus(actionDtoChild.getInstanceId());
//			}
			JSONObject resultJson = new JSONObject((String)response.getResponseObject()).getJSONObject("response");
			if (resultJson.getInt("status") == 0) {
				responseMessage.setMessage(ZohoConstants.REJECT_MESSAGE);
				if(actionType.equals("1")) {
					responseMessage.setMessage(ZohoConstants.APPROVE_MESSAGE);
				}					
				//updating task events and task owners
				zohoTaskEventsDao.completeTaskStatus(actionDtoChild.getInstanceId());
				
			}
			else {
				responseMessage.setMessage(" : " + resultJson.getJSONObject("errors").getString("message"));;
			}
			
			responseMessage.setStatus(ZohoConstants.STATUS_SUCCESS);
			responseMessage.setStatusCode("200");
			
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest]Error" + e);
		}
		
		return responseMessage;
	}
	
	public ResponseMessage forwardRequest(ActionDto actionDto, ActionDtoChild actionDtoChild) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(ZohoConstants.FAILURE);
		responseMessage.setStatus(ZohoConstants.FAILURE);
		responseMessage.setStatusCode("1");

		try {
			
			//Getting loggedIn Users
			User user = UserManagementUtil.getLoggedInUser();
			String puserId = user.getName().toUpperCase();
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] puserId : " + puserId);
			
			//Getting the refresh token from the db
			Map<String, UserIDPMappingDto> userList = zohoService.fetchUsers(1);
			String refreshTokenString = userList.get(puserId).getRefreshToken();
			String accessTokenString = accessToken.getAcessTokenByRefreshToken(refreshTokenString);
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] accessTokenString : " + accessTokenString);
			
			//Getting the processId from taskId
			String taskId = actionDtoChild.getInstanceId().split("-")[0];
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] taskId : " + taskId);
			
			
			
			
			//setting parameters for the action request
			Map<String, String> actionParameters = new HashMap<>();
			actionParameters.put("recordId", taskId);
			actionParameters.put("comment", "forwarded");
			actionParameters.put("formLinkName", "leave");
			actionParameters.put("forwardErecno", userList.get(actionDtoChild.getSendToUser()).getZohoId());

			RestResponse response = restUserZoho.callGetService(ZohoConstants.baseUrl + "forwardApproval",
					accessTokenString, actionParameters);
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] response : " + response);
			int statusCode = response.getResponseCode();
			JSONObject resultJson = new JSONObject((String) response.getResponseObject()).getJSONObject("response");
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] resultJson : " + resultJson);
			if (resultJson.getInt("status") == 0) {
				responseMessage.setMessage(ZohoConstants.FORWARD_MESSAGE);
				responseMessage.setStatus(ZohoConstants.STATUS_SUCCESS);
				responseMessage.setStatusCode("200");
				
				//updating the status of the task as completed
//				Map<String, UserIDPMappingDto> userList = zohoService.fetchUsers(1);
				zohoTaskEventsDao.updateAssignee(actionDtoChild.getInstanceId() , actionDtoChild.getSendToUser(), userList);
				
			}
			else {
				responseMessage.setMessage(" : " + resultJson.getJSONObject("errors").getString("message"));;
				responseMessage.setStatus(ZohoConstants.STATUS_SUCCESS);
				responseMessage.setStatusCode("200");
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest]Error" + e);
		}
		
		return responseMessage;
	}

}
