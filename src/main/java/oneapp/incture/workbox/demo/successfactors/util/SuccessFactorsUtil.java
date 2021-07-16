package oneapp.incture.workbox.demo.successfactors.util;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dao.UserDetailsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDoPK;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.RestUtil;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Component
public class SuccessFactorsUtil {

	@Autowired
	TaskEventsDao eventsDao;

	@Autowired
	TaskOwnersDao ownersDao;

	@Autowired
	SFEventsScheduler sfEvents;

	@Autowired
	UserDetailsDao userDetails;

	public JSONArray getTaskList() {

		RestResponse restResponse = RestUtil.callRestService(
				"https://apisalesdemo8.successfactors.com/odata/v2/FormHeader?%24filter=formTemplateId%20eq%20182&$format=json",
				"", null, PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, false, null, "HQ90@SFPART020012",
				"India@123", null, null);
		System.err.println(restResponse);
		JSONObject responseObject = (JSONObject) restResponse.getResponseObject();
		return responseObject.optJSONObject("d").optJSONArray("results");
	}

	public JSONArray getSuccessFactorTaskList(String userId) {
		/*
		 * // Fetching bearer token
		 * 
		 * RestResponse restResponse1 =
		 * RestUtil.callRestService(SuccessFactorsConstant.GET_SF_BEARER_TOKEN_URL +
		 * userId, "", null, PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON,
		 * false, null, null, null, null, null);
		 * 
		 * System.err.
		 * println("[WBP-Dev]SuccessFactorsUtil.getSuccessFactorTaskList() getting token response"
		 * + restResponse1); String bearerToken =
		 * restResponse1.getResponseObject().toString(); System.err.
		 * println("[WBP-Dev]SuccessFactorsUtil.getSuccessFactorTaskList() bearerToken"
		 * + bearerToken);
		 * 
		 * RestResponse restResponse =
		 * RestUtil.callRestService(SuccessFactorsConstant.GET_SF_TASKS_URL, "", null,
		 * PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, false, null, null,
		 * null, bearerToken, "Bearer");
		 * 
		 * System.err.
		 * println("[WBP-Dev]SuccessFactorsUtil.getSuccessFactorTaskList() fetching task response"
		 * + restResponse);
		 * 
		 * JSONObject responseObject = (JSONObject) restResponse.getResponseObject();
		 * JSONArray responseArray = null; if
		 * (responseObject.optJSONObject("d").optJSONArray("results").length() > 0) {
		 * responseArray =
		 * responseObject.optJSONObject("d").optJSONArray("results").getJSONObject(0)
		 * .optJSONObject("todos").optJSONArray("results");
		 * 
		 * }
		 * 
		 * return responseArray;
		 */
		
		
		
		RestResponse restResponse = RestUtil.callRestService(SuccessFactorsConstant.GET_SF_TASKS_URL, "", null,
				PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, false, null, "sfadmin@SFPART042431", "August@123", null,
				null);

		System.err.println("[WBP-Dev]SuccessFactorsUtil.getSuccessFactorTaskList() fetching task response" + restResponse);

		JSONObject responseObject = (JSONObject) restResponse.getResponseObject();
		JSONArray responseArray = null;
		if (responseObject.optJSONObject("d").optJSONArray("results").length() > 0) {
			responseArray = responseObject.optJSONObject("d").optJSONArray("results").getJSONObject(0)
					.optJSONObject("todos").optJSONArray("results");

		}

		return responseArray;
	}

	public JSONArray getSuccessFactorCustomAttributes(String taskId, String userId) {
		System.err.println(
				"SuccessFactorsUtil.getSuccessFactorCustomAttributes() taskId-" + taskId + " userId-" + userId);
		// Fetching bearer token
//		RestResponse restResponse1 = RestUtil.callRestService(SuccessFactorsConstant.GET_SF_BEARER_TOKEN_URL + userId, "", null,
//				PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, false, null, null, null, null, null);
//		String bearerToken = restResponse1.getResponseObject().toString();
//
//		RestResponse restResponse = RestUtil.callRestService(
//				SuccessFactorsConstant.GET_SF_CUSTATTR_URL + taskId + "%27" + SuccessFactorsConstant.GET_SF_CUSTATTR_FILTER_URL, "", null,
//				PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, false, null, null, null, bearerToken,
//				"Bearer");
		
		RestResponse restResponse = RestUtil.callRestService(
				SuccessFactorsConstant.GET_SF_CUSTATTR_URL + taskId + "%27" + SuccessFactorsConstant.GET_SF_CUSTATTR_FILTER_URL, "", null,
				PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, false, null, "sfadmin@SFPART042431", "August@123", null,null);

		JSONObject responseObject = (JSONObject) restResponse.getResponseObject();

		JSONArray responseArray = responseObject.optJSONObject("d").optJSONArray("results");

		return responseArray;
	}

	public  JSONArray getSuccessFactorTaskLeaveList(String workflowRequestId) {


		String toDoUrl = "https://apisalesdemo4.successfactors.com:443/odata/v2/EmployeeTime?$format=json&"
				+ "$filter=workflowRequestId%20eq%20'"+workflowRequestId+"'&$select=timeTypeNav/externalName_en_US,createdByNav/"
				+ "firstName,createdByNav/lastName&$expand=createdByNav,timeTypeNav&$format=JSON";

		RestResponse restResponse = RestUtil.callRestService(toDoUrl, "", null, PMCConstant.HTTP_METHOD_GET,
				PMCConstant.APPLICATION_JSON, false, null, "sfadmin@SFPART042431", "August@123", null , null);

		System.err.println("SuccessFactorsUtil.getSuccessFactorTaskList() fetching task response" + restResponse);

		JSONObject responseObject = (JSONObject) restResponse.getResponseObject();
		JSONArray responseArray = null;
		if (responseObject.optJSONObject("d").optJSONArray("results").length() > 0) {
			responseArray = responseObject.optJSONObject("d").optJSONArray("results");

		}

		return responseArray;
	}

	public JSONObject getSuccessFactorTimeSheetList(String userId) {

		String toDoUrl = "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/time-sheet/getTimesheetPendingTask?managerId="
				+ userId;

		RestResponse restResponse = RestUtil.callRestService(toDoUrl, "", null, PMCConstant.HTTP_METHOD_GET,
				PMCConstant.APPLICATION_JSON, false, null, null, null, null, null);

		System.err.println("SuccessFactorsUtil.getSuccessFactorTaskList() fetching task response" + restResponse);

		JSONObject responseObject = (JSONObject) restResponse.getResponseObject();

		return responseObject;

	}

	public  JSONObject getSuccessFactorPendingTaskList(String userId) {

		String toDoUrl = "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/TravelRequest/getPendingList?userId="+ userId;

		RestResponse restResponse = RestUtil.callRestService(toDoUrl, "", null, PMCConstant.HTTP_METHOD_GET,
				PMCConstant.APPLICATION_JSON, false, null, null, null, null, null);

		System.err.println("SuccessFactorsUtil.getSuccessFactorTaskList() fetching task response" + restResponse);

		JSONObject responseObject = (JSONObject) restResponse.getResponseObject();

		return responseObject;
	}

	public String getPayloadForExpense(String expenseId, String status, String updatedDate, String updatedBy, String wfTaskId, String updatedByName, String comment) {
		String listToString = "{ \"expenseId\":";
		if (!ServicesUtil.isEmpty(expenseId)) {
			listToString += "\"";
			listToString += expenseId;
		}

		listToString += "\"";
		listToString += " ,\"status\":";
		listToString += "\"";
		listToString += status;

		listToString += "\"";
		listToString += " ,\"updatedDate\":";
		listToString += "\"";
		listToString += updatedDate;

		listToString += "\"";
		listToString += " ,\"updatedBy\":";
		listToString += "\"";
		listToString += updatedBy;

		listToString += "\"";
		listToString += " ,\"wfTaskId\":";
		listToString += "\"";
		listToString += wfTaskId;

		listToString += "\"";
		listToString += " ,\"updatedByName\":";
		listToString += "\"";
		listToString += updatedByName;

		listToString += "\"";
		listToString += " ,\"comment\":";
		listToString += "\"";
		listToString += comment;
		listToString += "\"";
		listToString += "}";

		return listToString;
	}

	public String getPayloadForTravel(String requestId, String status, String approvedDate, String approvedBy, String workflowId, String comment) {
		String listToString = "{ \"requestId\":";
		if (!ServicesUtil.isEmpty(requestId)) {
			listToString += "\"";
			listToString += requestId;
		}

		listToString += "\"";
		listToString += " ,\"status\":";
		listToString += "\"";
		listToString += status;

		listToString += "\"";
		listToString += " ,\"approvedDate\":";
		listToString += "\"";
		listToString += approvedDate;

		listToString += "\"";
		listToString += " ,\"approvedBy\":";
		listToString += "\"";
		listToString += approvedBy;

		listToString += "\"";
		listToString += " ,\"workflowId\":";
		listToString += "\"";
		listToString += workflowId;

		listToString += "\"";
		listToString += " ,\"comment\":";
		listToString += "\"";
		listToString += comment;
		listToString += "\"";
		listToString += "}";

		return listToString;
	}

	public String getPayloadForTimeSheet(String timesheetId, String status, String updatedDate, String updatedBy, String wfTaskId, String updatedByName, String comment) {
		String listToString = "{ \"timesheetId\":";
		if (!ServicesUtil.isEmpty(timesheetId)) {
			listToString += "\"";
			listToString += timesheetId;
		}

		listToString += "\"";
		listToString += " ,\"status\":";
		listToString += "\"";
		listToString += status;

		listToString += "\"";
		listToString += " ,\"auditId\":";
		listToString += "\"";
		listToString += "1";

		listToString += "\"";
		listToString += " ,\"updatedDate\":";
		listToString += "\"";
		listToString += updatedDate;

		listToString += "\"";
		listToString += " ,\"updatedBy\":";
		listToString += "\"";
		listToString += updatedBy;

		listToString += "\"";
		listToString += " ,\"wfTaskId\":";
		listToString += "\"";
		listToString += wfTaskId;

		listToString += "\"";
		listToString += " ,\"updatedByName\":";
		listToString += "\"";
		listToString += updatedByName;

		listToString += "\"";
		listToString += " ,\"comment\":";
		listToString += "\"";
		listToString += comment;
		listToString += "\"";
		listToString += "}";

		return listToString;
	}

	public ResponseMessage completeSuccessFactorTask(String action, String taskId, String subject, String userId, String processType, String comment) {

		System.err.println("[WBP-Dev]SuccessFactorsUtil.completeSuccessFactorTask() userId before Mapping "+userId);

		Map<String, List<String>> usersMap = userDetails.getAllSuccessFactorsUserDetails();

		if (usersMap.containsKey(userId)) {

			userId = usersMap.get(userId).get(1);

		}

		System.err.println("[WBP-Dev]SuccessFactorsUtil.completeSuccessFactorTask() userId After Mapping "+userId);


		String url = "";
		String status = "";
		JSONObject responseObject;

		if("Travel".equalsIgnoreCase(processType))
		{
			if ("Reject".equalsIgnoreCase(action)) {
				url = SuccessFactorsConstant.REJECT_SF_TRAVEL_TASK_URL;
				status = "REJECTED";

			} else if ("Approve".equalsIgnoreCase(action)) {
				url = SuccessFactorsConstant.APPROVE_SF_TRAVEL_TASK_URL;
				status = "APPROVED";
				System.out.println(status);
			}

			String payLoad = getPayloadForTravel(taskId, status, ServicesUtil.getUtcTimeWithoutT(), userId, "", comment);
			RestResponse restResponse = RestUtil.callRestService(
					url, "", payLoad,
					PMCConstant.HTTP_METHOD_POST, PMCConstant.APPLICATION_JSON, false, null, null, null, null, null);

			responseObject = (JSONObject) restResponse.getResponseObject();
			System.out.println(responseObject);

		}

		else if("Expense".equalsIgnoreCase(processType))
		{
			if ("Reject".equalsIgnoreCase(action)) {
				url = SuccessFactorsConstant.REJECT_SF_EXPENSE_TASK_URL;
				status = "REJECTED";

			} else if ("Approve".equalsIgnoreCase(action)) {
				url = SuccessFactorsConstant.APPROVE_SF_EXPENSE_TASK_URL;
				status = "APPROVED";
			}

			String payLoad = getPayloadForExpense(taskId, status, ServicesUtil.getUtcTime(), userId, "", "", comment);
			RestResponse restResponse = RestUtil.callRestService(
					url, "", payLoad,
					PMCConstant.HTTP_METHOD_POST, PMCConstant.APPLICATION_JSON, false, null, null, null, null, null);


			responseObject = (JSONObject) restResponse.getResponseObject();
		}

		else if("TimeSheet".equalsIgnoreCase(processType))
		{
			if ("Reject".equalsIgnoreCase(action)) {
				url = SuccessFactorsConstant.REJECT_SF_TS_TASK_URL;
				status = "REJECTED";

			} else if ("Approve".equalsIgnoreCase(action)) {
				url = SuccessFactorsConstant.APPROVE_SF_TS_TASK_URL;
				status = "APPROVED";
			}

			String payLoad = getPayloadForTimeSheet(taskId, status, ServicesUtil.getUtcTime(), userId, "", "", comment);
			RestResponse restResponse = RestUtil.callRestService(
					url, "", payLoad,
					PMCConstant.HTTP_METHOD_POST, PMCConstant.APPLICATION_JSON, false, null, null, null, null, null);

			responseObject = (JSONObject) restResponse.getResponseObject();
			System.err.println("TimeSheet Response :  " +responseObject);
		}

		else{

			if ("Reject".equalsIgnoreCase(action)) {
				url = SuccessFactorsConstant.REJECT_SF_TASK_URL + taskId + "L"+SuccessFactorsConstant.COMMENT_TAG+comment;

			} else if ("Approve".equalsIgnoreCase(action)) {
				url = SuccessFactorsConstant.APPROVE_SF_TASK_URL + taskId + "L"+SuccessFactorsConstant.COMMENT_TAG+comment;
			}
//			RestResponse restResponse1 = RestUtil.callRestService(SuccessFactorsConstant.GET_SF_BEARER_TOKEN_URL + userId, "", null,
//					PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, false, null, null, null, null, null);
//
//			System.err.println("[WBP-Dev]SuccessFactorsUtil.completeSuccessFactorTask() getting token response" + restResponse1);
//			String bearerToken = restResponse1.getResponseObject().toString();
//			System.err.println("[WBP-Dev]SuccessFactorsUtil.completeSuccessFactorTask() bearerToken" + bearerToken);

//			RestResponse restResponse = RestUtil.callRestService(url, "", null, PMCConstant.HTTP_METHOD_POST,
//					PMCConstant.APPLICATION_JSON, false, null, null, null, bearerToken, "Bearer");
			RestResponse restResponse = RestUtil.callRestService(url, "", null, PMCConstant.HTTP_METHOD_POST,
					PMCConstant.APPLICATION_JSON, false, null, "sfadmin@SFPART042431", "August@123", null, null);
//			responseObject = (JSONObject) restResponse.getResponseObject();
			if (restResponse.getResponseObject().toString().contains("success") || restResponse.getResponseObject().toString().contains("Save SuccessFully")|| restResponse.getResponseObject().toString().contains("Success")) {
				eventsDao.updateTaskEventToCompleted(taskId);
				return new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS, "Task Completed Successfully");
			}else {
				System.err.println("[WBP-Dev]SuccessFactorsUtil.completeSuccessFactorTask() responseObject" + restResponse);
				return new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE, "Task Completion failed");
			}
		}

		System.err.println("[WBP-Dev]SuccessFactorsUtil.completeSuccessFactorTask() responseObject" + responseObject);
		eventsDao.updateTaskEventToCompleted(taskId);
		return new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS, "Task Completed Successfully");
//		if (restResponse.getResponseObject().toString().contains("success") || responseObject.toString().contains("Save SuccessFully")|| responseObject.toString().contains("Success")) {
//			eventsDao.updateTaskEventToCompleted(taskId);
//			return new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS, "Task Completed Successfully");
//		}else {
//			
//			return new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE, "Task Completion failed");
//		}
	}

	public JSONArray getProcessList() {
		RestResponse restResponse = RestUtil.callRestService(
				"https://apisalesdemo8.successfactors.com/odata/v2/FormTemplate?$format=json", "", null,
				PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, false, null, "HQ90@SFPART020012",
				"India@123", null, null);
		JSONObject responseObject = (JSONObject) restResponse.getResponseObject();
		return responseObject.optJSONObject("d").optJSONArray("results");
	}

	public ResponseMessage completeSfTask(String action, String taskId, String subject) {
		String url = "";
		String userId = "HQ75@SFPART020012";
		try {
			if ("Reject".equalsIgnoreCase(action)) {
				url = "https://apisalesdemo8.successfactors.com/odata/v2/rejectForm?formDataId=" + taskId
						+ "L&comment=Rejected";
				switch (subject) {
				case "Manager Approval":
					userId = "HQ75@SFPART020012";
					break;
				case "Reviewer Approval":
					userId = "HQ76@SFPART020012";
					break;
				case "Performance Review Signing":
					userId = "HQ74@SFPART020012";
					break;
				default: 
					userId = "HQ75@SFPART020012";
					break;
				}
			} else if ("Approve".equalsIgnoreCase(action)) {
				switch (subject) {
				case "Manager Approval":
					url = "https://apisalesdemo8.successfactors.com/odata/v2/sendToNextStep?formDataId=" + taskId
					+ "L&comment=Approved&innerStepUserId=HQ75&nextIStepEntryUser=HQ76";
					userId = "HQ75@SFPART020012";
					break;
				case "Reviewer Approval":
					url = "https://apisalesdemo8.successfactors.com/odata/v2/sendToNextStep?formDataId=" + taskId
					+ "L&comment=Approved&innerStepUserId=HQ76&nextIStepEntryUser=HQ74";
					userId = "HQ76@SFPART020012";
					break;
				case "Performance Review Signing":
					url = "https://apisalesdemo8.successfactors.com/odata/v2/signForm?formDataId=602L&comment=Signed";
					userId = "HQ74@SFPART020012";
					break;
				default: 
					url = "";
					userId = "HQ75@SFPART020012";
					break;
				}
			}


			RestResponse restResponse = RestUtil.callRestService(url, "", null, PMCConstant.HTTP_METHOD_GET,
					PMCConstant.APPLICATION_JSON, false, null, userId, "India@123", null, null);

			if(restResponse.getResponseCode() == PMCConstant.UNAUTHORIZED){
				return new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE,
						"Unauthorized Request");
			}

			JSONObject responseObject = (JSONObject) restResponse.getResponseObject();

			if (responseObject.toString().contains("Success")) {
				switch (action) {
				case "Approve":
					ownersDao.changeSFOwnerStatus(
							new TaskOwnersDo(new TaskOwnersDoPK(taskId, getOwner(subject)), true, true));
					break;
				case "Reject":
					ownersDao.removeSFOwner(new TaskOwnersDo(new TaskOwnersDoPK(taskId, getOwner(subject)), true, true));
					break;
				default: 
					break;
				}
				return new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS, "Task Completed Successfully");
			} else {
				return new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE,
						"Task Not Completed Successfully");
			}
		}catch(Exception e){
			System.err.println("[WBP-Dev]SuccessFactorsUtil.completeSfTask() exception :"+e.getMessage());
			return new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE,
					"Task Not Completed Successfully");
		}
	}

	private String getOwner(String name) {
		String owner = "";
		switch (name) {
		case "Requestor Task":
			owner = "P000035";
			break;
		case "Manager Approval":
			owner = "P000036";
			break;
		case "Reviewer Approval":
			owner = "P000006";
			break;
		case "Performance Review Signing":
			owner = "P000015";
			break;
		default:
			owner = "P000057";
			break;
		}
		return owner;
	}

}
