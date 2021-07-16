package oneapp.incture.workbox.demo.zohoExpense.service;

import java.util.HashMap;

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.security.um.user.User;

import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.zohoExpense.dao.ZohoExpenseTaskEventsDao;
import oneapp.incture.workbox.demo.zohoExpense.util.AcessTokenZohoExpense;
import oneapp.incture.workbox.demo.zohoExpense.util.RestUserZohoExpense;
import oneapp.incture.workbox.demo.zohoExpense.util.ZohoExpenseConstants;


@Service
//@Transactional
public class ZohoExpenseActionFacade {

	@Autowired
	ZohoExpenseServiceLocal zohoService;

	@Autowired
	AcessTokenZohoExpense accessToken;

	@Autowired
	RestUserZohoExpense restUserZoho;
	
	@Autowired
	ZohoExpenseTaskEventsDao zohoExpenseTaskEventsDao;

	public ResponseMessage acceptOrRejectRequest(ActionDto actionDto, ActionDtoChild actionDtoChild,
			String actionType) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(ZohoExpenseConstants.FAILURE);
		responseMessage.setStatus(ZohoExpenseConstants.FAILURE);
		responseMessage.setStatusCode("1");

		try {
			User user = UserManagementUtil.getLoggedInUser();
			String puserId = user.getName().toUpperCase();
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] puserId : " + puserId);

			String refreshTokenString = zohoService.fetchUsers(1).get(puserId).getRefreshToken();
			String accessTokenString = accessToken.getAcessTokenByRefreshToken(refreshTokenString);
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] accessTokenString : " + accessTokenString);

			String taskId = actionDtoChild.getInstanceId().split("-")[0];
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] taskId : " + taskId);

			RestResponse response = restUserZoho.callPostService(
					ZohoExpenseConstants.baseUrl + "expensereports/" + taskId + "/" + actionType, accessTokenString,
					new HashMap<String, String>());
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] response : " + response);
			int statusCode = response.getResponseCode();

			JSONObject resultJson = new JSONObject((String) response.getResponseObject());
			if (resultJson.getInt("code") == 0) {
				responseMessage.setMessage(ZohoExpenseConstants.REJECT_MESSAGE);
				if (actionType.equals("approve")) {
					responseMessage.setMessage(ZohoExpenseConstants.APPROVE_MESSAGE);
				}
				// updating task events and task owners
				zohoExpenseTaskEventsDao.completeTaskStatus(actionDtoChild.getInstanceId());

			} else {
				responseMessage.setMessage(" : " + resultJson.getString("message"));
				;
			}

			responseMessage.setStatus(PMCConstant.SUCCESS);
			responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][ZOHO][acceptOrRejectRequest] error : " + e.getMessage());
		}
		return responseMessage;
	}

}
