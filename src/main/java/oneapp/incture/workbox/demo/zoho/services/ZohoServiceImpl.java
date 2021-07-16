package oneapp.incture.workbox.demo.zoho.services;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.zoho.dao.ZohoTaskEventsDao;
import oneapp.incture.workbox.demo.zoho.dao.ZohocustomAttributeDao;
import oneapp.incture.workbox.demo.zoho.util.AccessTokenZoho;
import oneapp.incture.workbox.demo.zoho.util.RestUserZoho;
import oneapp.incture.workbox.demo.zoho.util.ZohoConstants;



@Service
//@Transactional
public class ZohoServiceImpl implements ZohoService {

	@Autowired
	RestUserZoho restUserZoho;

	@Autowired
	AccessTokenZoho accessToken;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private ProcessEventsDao processEventsDao;

	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private TaskOwnersDao taskOwnersDao;

	@Autowired
	private CustomAttributeDao customAttributeDao;

	@Autowired
	private ZohocustomAttributeDao zohocustomAttributeDao;

	@Autowired
	private ZohoTaskEventsDao zohoTaskEventsDao;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	// For setting all the Dtos
	public ResponseMessage setAll(String processName) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Something went wrong");
		responseMessage.setStatus("Failure");
		responseMessage.setStatusCode("1");

		try {

			String accessTokenString = accessToken.getAcessToken();
			System.err.println("[WBP-Dev][ZOHO][setAll] AcessToken : " + accessTokenString);

			// fetching the userDetails with zohoId as key
			Map<String, UserIDPMappingDto> userList = fetchUsers(0);
			int processCount = 0; // for getting each process items

			RestResponse processReponse = restUserZoho.callGetService(
					ZohoConstants.baseUrl + "forms/" + processName + "/getRecords", accessTokenString,
					new HashMap<String, String>());
			JSONObject processObject = new JSONObject((String) processReponse.getResponseObject());
			JSONArray processArray = processObject.getJSONObject("response").getJSONArray("result");

			// getting the custom attributes of the process
			List<String> attributes = zohocustomAttributeDao.getCustomAttributesByProcess(processName);
			System.err.println("[WBP-Dev][ZOHO][setAll] TaskEventDtos : " + attributes);

			// setting the proecssDto
			List<ProcessEventsDto> processEventsDtos = setProcess(userList, accessTokenString, processArray,
					processName);
			for (ProcessEventsDto processEventsDto : processEventsDtos) {

				// getting all the leave types and balance of the process
				Map<String, String> parameters = new HashMap<>();
				Map<String, UserIDPMappingDto> userListWithPuserId = fetchUsers(1);
				parameters.put("userId", userListWithPuserId.get(processEventsDto.getStartedByUser()).getZohoId());
				RestResponse leaveTypeResponse = restUserZoho.callGetService(
						ZohoConstants.baseUrl + "leave/getLeaveTypeDetails", accessTokenString, parameters);
				JSONObject leaveTypeObject = new JSONObject((String) leaveTypeResponse.getResponseObject());
				JSONArray leaveTypeArray = (leaveTypeObject.getJSONObject("response").getJSONArray("result"));

				// setting parameters for the getting approval details
				Map<String, String> taskParameters = new HashMap<>();
				taskParameters.put("recordId", processEventsDto.getProcessId());
				taskParameters.put("formLinkName", processName.toLowerCase());
				RestResponse taskResponse = restUserZoho.callGetService(ZohoConstants.baseUrl + "getApprovalDetails",
						accessTokenString, taskParameters);

				JSONObject approvalObject = new JSONObject((String) taskResponse.getResponseObject());
				approvalObject = approvalObject.getJSONObject("response").getJSONArray("result").getJSONObject(0);

				// setting task , owners and custom attributes of the process
				List<TaskEventsDto> taskEventsDtos = setTask(processEventsDto, approvalObject, userList,
						accessTokenString, processName);
				List<TaskOwnersDto> taskOwnersDtos = setOwner(processEventsDto, approvalObject, userList,
						accessTokenString);
				List<CustomAttributeValue> customAttributeValues = setCustomAttributeValues(
						processArray.get(processCount), approvalObject, userList, processName, attributes,
						leaveTypeArray);
				processCount++;

				// saving the Dtos in the db
				processEventsDao.saveOrUpdateProcesses(processEventsDtos);
				taskEventsDao.saveOrUpdateTasks(taskEventsDtos);
				taskOwnersDao.saveOrUpdateOwners(taskOwnersDtos);
				customAttributeDao.addCustomAttributeValue(customAttributeValues);

				System.err.println("[WBP-Dev][ZOHO][setAll] TaskEventDtos : " + new Gson().toJson(taskEventsDtos));
				System.err.println("[WBP-Dev][ZOHO][setAll] TaskOwnerDtos : " + new Gson().toJson(taskOwnersDtos));
				System.err.println(
						"[WBP-Dev][ZOHO][setAll] CustomAttributeValues : " + new Gson().toJson(customAttributeValues));
			}

			responseMessage.setMessage("Data Added Successfully");
			responseMessage.setStatus("Success");
			responseMessage.setStatusCode("200");

		} catch (Exception e) {
			System.err.println("[WBP-Dev][ZOHO][setAll]Error" + e);
		}

		return responseMessage;
	}

	public List<ProcessEventsDto> setProcess(Map<String, UserIDPMappingDto> userList, String accessTokenString,
			JSONArray processArray, String processName) {

		List<ProcessEventsDto> processEventsDtos = new ArrayList<>();

		for (Object process : processArray) {
			String recordId = ((JSONObject) process).keys().next();

			ProcessEventsDto processEventsDto = new ProcessEventsDto();
			JSONObject processObject = ((JSONObject) process).getJSONArray(recordId).getJSONObject(0);
			// setting values in Dto
			processEventsDto.setProcessId(recordId);
			processEventsDto.setRequestId(recordId);
			processEventsDto.setName(processName);
			processEventsDto.setSubject("Zoho - " + processName + " approval request");
			processEventsDto.setStatus("RUNNING");
			if (processObject.getString("ApprovalStatus").equals("Approved")
					|| processObject.getString("ApprovalStatus").equals("Rejected")) {
				processEventsDto.setStatus("COMPLETED");
			} else if (processObject.getString("ApprovalStatus").equals("Cancelled")) {
				processEventsDto.setStatus("CANCELLED");
			}
			processEventsDto.setStartedBy(userList.get(processObject.getString("Employee_ID.ID")).getUserId());
			processEventsDto
					.setStartedAt(ServicesUtil.convertFromStringToDateZoho(processObject.getString("AddedTime")));
			processEventsDto.setStartedByUser(userList.get(processObject.getString("Employee_ID.ID")).getUserId());
			processEventsDto
					.setStartedByDisplayName(userList.get(processObject.getString("Employee_ID.ID")).getUserFirstName()
							+ " " + userList.get(processObject.getString("Employee_ID.ID")).getUserLastName());
			processEventsDto.setProcessDisplayName(processName);
			processEventsDto.setProcessDefinitionId(recordId);
			processEventsDtos.add(processEventsDto);

		}

		return processEventsDtos;
	}

	public List<TaskEventsDto> setTask(ProcessEventsDto processEventsDto, JSONObject approvalObject,
			Map<String, UserIDPMappingDto> userList, String accesTokenString, String processName) {

		JSONArray processedApprovalObjectArray = approvalObject.getJSONArray("processed");
		List<TaskEventsDto> taskEventsDtos = new ArrayList<>();

		int pendingApprovalLevel = 1; // for unique Id
		String processOwnerId = approvalObject.getJSONObject("recordowner").getString("erecno");
		String currentProcessorId = null;

		Date completedAt = processEventsDto.getStartedAt();

		for (Object processedApprovalObject : processedApprovalObjectArray) {

			TaskEventsDto taskEventsDto = new TaskEventsDto();
			currentProcessorId = ((JSONObject) processedApprovalObject).getString("erecno");
//			taskOwnerId = ((JSONObject) processedApprovalObject).getString("erecno");
//			if (((JSONObject) processedApprovalObject).optJSONObject("approverDtls") != null) {
//				taskOwnerId = ((JSONObject) processedApprovalObject).optJSONObject("approverDtls").getString("erecno");
//			}

			taskEventsDto.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
			taskEventsDto.setRequestId(processEventsDto.getProcessId());
			taskEventsDto.setProcessId(processEventsDto.getProcessId());
			taskEventsDto.setSubject("Zoho - " + processName + "  approval request");
			taskEventsDto.setDescription(
					processName + " approval request from " + processEventsDto.getStartedByDisplayName());
			taskEventsDto.setName(processName);
			taskEventsDto.setStatus("COMPLETED");
			// taskEventsDto.setCreatedAt(processEventsDto.getStartedAt());
			taskEventsDto.setCompletionDeadLine(
					new Date((processEventsDto.getStartedAt().getTime() + (1000 * 60 * 60 * 24 * 5))));
			taskEventsDto.setOrigin("Zoho");
			taskEventsDto
					.setSlaDueDate(new Date(processEventsDto.getStartedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
			taskEventsDto.setPriority("MEDIUM");
//			taskEventsDto.setOwnersName(
//					userList.get(taskOwnerId).getUserFirstName() + " " + userList.get(taskOwnerId).getUserLastName());
			taskEventsDto.setProcessName(processName);
			taskEventsDto.setUpdatedAt(processEventsDto.getStartedAt());
			taskEventsDto.setCreatedBy(userList.get(processOwnerId).getUserFirstName() + " "
					+ userList.get(processOwnerId).getUserLastName());
			taskEventsDto.setCurrentProcessor(userList.get(currentProcessorId).getUserId());
			taskEventsDto.setCurrentProcessorDisplayName(userList.get(currentProcessorId).getUserFirstName() + " "
					+ userList.get(currentProcessorId).getUserLastName());
			taskEventsDto.setCompletedAt(ServicesUtil
					.convertCompletedDateZoho(((JSONObject) processedApprovalObject).getString("approvedTime")));
			taskEventsDto.setCreatedAt(completedAt);
			taskEventsDtos.add(taskEventsDto);

			completedAt = ServicesUtil
					.convertCompletedDateZoho(((JSONObject) processedApprovalObject).getString("approvedTime"));
			pendingApprovalLevel++;

		}

		if (approvalObject.getInt("currentApprovalStatus") == -1) {

			JSONObject currentApprovalObject = approvalObject.getJSONObject("current");
			//taskOwnerId = getUserbyZohoEmail(currentApprovalObject.getString("email"), accesTokenString);

			TaskEventsDto taskEventsDto = new TaskEventsDto();
			taskEventsDto.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
			taskEventsDto.setRequestId(processEventsDto.getProcessId());
			taskEventsDto.setProcessId(processEventsDto.getProcessId());
			taskEventsDto.setSubject("Zoho : " + processName + " approval request");
			taskEventsDto.setDescription(
					"Zoho : " + processName + " approval request from " + processEventsDto.getStartedByDisplayName());
			taskEventsDto.setName(processName);
			taskEventsDto.setStatus("READY");
			
			
			if(currentApprovalObject.optJSONObject("forward") != null) {
				taskEventsDto.setStatus("RESERVED");
				currentProcessorId = currentApprovalObject.getJSONObject("forward").getJSONObject("target").getString("erecno");
				taskEventsDto.setCurrentProcessor(userList.get(currentProcessorId).getUserId());
				taskEventsDto.setCurrentProcessorDisplayName(userList.get(currentProcessorId).getUserFirstName() + " "
						+ userList.get(currentProcessorId).getUserLastName());
			}
			
			taskEventsDto.setCreatedAt(completedAt);
			taskEventsDto.setCompletionDeadLine(
					new Date((processEventsDto.getStartedAt().getTime() + (1000 * 60 * 60 * 24 * 5))));
			taskEventsDto.setOrigin("Zoho");
			taskEventsDto
					.setSlaDueDate(new Date(processEventsDto.getStartedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
			taskEventsDto.setPriority("MEDIUM");
//			taskEventsDto.setOwnersName(
//					userList.get(taskOwnerId).getUserFirstName() + " " + userList.get(taskOwnerId).getUserLastName());
			taskEventsDto.setProcessName(processName);
			taskEventsDto.setUpdatedAt(processEventsDto.getStartedAt());
			taskEventsDto.setCreatedBy(userList.get(processOwnerId).getUserFirstName() + " "
					+ userList.get(processOwnerId).getUserLastName());
			taskEventsDtos.add(taskEventsDto);
		}

		// checking for cancelled requests
		if (approvalObject.getInt("currentApprovalStatus") == 2) {
			zohoTaskEventsDao.updateTaskStatus(processEventsDto.getProcessId());
		}

		return taskEventsDtos;

	}

	public List<TaskOwnersDto> setOwner(ProcessEventsDto processEventsDto, JSONObject approvalObject,
			Map<String, UserIDPMappingDto> userList, String accessTokenString) {

		System.err.println("In setOwner");
		JSONArray processedApprovalObjectArray = approvalObject.getJSONArray("processed");
		List<TaskOwnersDto> taskOwnersDtos = new ArrayList<>();
		int pendingApprovalLevel = 1;

		for (Object processedApprovalObject : processedApprovalObjectArray) {

			if (((JSONObject) processedApprovalObject).optJSONObject("approverDtls") != null) {
				String[] userEmails = ((JSONObject) processedApprovalObject).getJSONObject("approverDtls")
						.getString("email").split(",");
				for (String userEmail : userEmails) {
					String userRecordId = getUserbyZohoEmail(userEmail, accessTokenString);

					TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
					taskOwnersDto
							.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
					taskOwnersDto.setTaskOwner(userList.get(userRecordId).getUserId());
					taskOwnersDto.setTaskOwnerDisplayName(userList.get(userRecordId).getUserFirstName() + " "
							+ userList.get(userRecordId).getUserLastName());
					taskOwnersDto.setOwnerEmail(userList.get(userRecordId).getUserEmail());
					taskOwnersDtos.add(taskOwnersDto);
				}
			} else {
				String userEmail = ((JSONObject) processedApprovalObject).getString("email");
				String userRecordId = getUserbyZohoEmail(userEmail, accessTokenString);

				TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
				taskOwnersDto.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
				taskOwnersDto.setTaskOwner(userList.get(userRecordId).getUserId());
				taskOwnersDto.setTaskOwnerDisplayName(userList.get(userRecordId).getUserFirstName() + " "
						+ userList.get(userRecordId).getUserLastName());
				taskOwnersDto.setOwnerEmail(userList.get(userRecordId).getUserEmail());
				taskOwnersDtos.add(taskOwnersDto);
			}

			pendingApprovalLevel++;
		}

		if (approvalObject.getInt("currentApprovalStatus") == -1) {

			JSONObject currentApprovalObject = approvalObject.getJSONObject("current");

			String[] userEmails = currentApprovalObject.getString("email").split(",");
			for (String userEmail : userEmails) {
				String userRecordId = getUserbyZohoEmail(userEmail, accessTokenString);

				TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
				taskOwnersDto.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
				taskOwnersDto.setTaskOwner(userList.get(userRecordId).getUserId());
				taskOwnersDto.setTaskOwnerDisplayName(userList.get(userRecordId).getUserFirstName() + " "
						+ userList.get(userRecordId).getUserLastName());
				taskOwnersDto.setOwnerEmail(userList.get(userRecordId).getUserEmail());
				taskOwnersDtos.add(taskOwnersDto);
			}

			pendingApprovalLevel++;

		}

		return taskOwnersDtos;

	}

	@SuppressWarnings("unchecked")
	public Map<String, UserIDPMappingDto> fetchUsers(int type) {
		System.err.println("In fetch users");
		String userofSF = "Select user_login_name,user_email,user_first_name,user_id,user_last_name,zoho_id,refresh_token"
				+ " from user_idp_mapping where zoho_id is not null";
		Query sfUsersQuery = this.getSession().createSQLQuery(userofSF);
		List<Object[]> userList = sfUsersQuery.list();
		System.err.println(userList);

		Map<String, UserIDPMappingDto> userMapping = new HashMap<>();
		userList.stream().map(obj -> Arrays.asList(obj).toArray(new String[0])).forEach(resultList -> {
			UserIDPMappingDto userDetails = new UserIDPMappingDto();
			userDetails.setUserEmail(resultList[1]);
			userDetails.setUserFirstName(resultList[2]);
			userDetails.setUserId(resultList[3]);
			userDetails.setUserLastName(resultList[4]);
			userDetails.setUserLoginName(resultList[0]);
			userDetails.setZohoId(resultList[5]);
			userDetails.setRefreshToken(resultList[6]);
			int index = type == 0 ? 3 : 5; 
			userMapping.put( resultList[index], userDetails);
		});
		
		
//		for (Object[] obj : userList) {
//			userDetails = new UserIDPMappingDto();
//			userDetails.setUserEmail((String) obj[1]);
//			userDetails.setUserFirstName((String) obj[2]);
//			userDetails.setUserId((String) obj[3]);
//			userDetails.setUserLastName((String) obj[4]);
//			userDetails.setUserLoginName((String) obj[0]);
//			userDetails.setZohoId((String) obj[5]);
//			userDetails.setRefreshToken((String) obj[6]);
//
//			// 0 for zohoId as key 1 for puser as ka key
//			
//			String string = (type > 0) ? "hello" : "hai";
//			
//			
//			int index = type == 0 ? 3 : 5; 
//			userMapping.put((String) obj[index], userDetails);
//			
//			if (type == 0)
//				userMapping.put((String) obj[5], userDetails);
//			else
//				userMapping.put((String) obj[3], userDetails);
//		}
		System.err.println("[WBP-Dev][ZOHO][ZohoServiceImpl][] fetchUsers : " + userMapping);
		return userMapping;
	}

	public List<CustomAttributeValue> setCustomAttributeValues(Object object, JSONObject approvalObject,
			Map<String, UserIDPMappingDto> userLis, String processName, List<String> attributes,
			JSONArray leaveTypeArray) {

		String recordId = ((JSONObject) object).keys().next();
		JSONObject processObject = ((JSONObject) object).getJSONArray(recordId).getJSONObject(0);

		// processObject = (JSONObject) processObject;
		List<CustomAttributeValue> customAttributeValues = new ArrayList<>();
		JSONArray processedApprovalObjectArray = approvalObject.getJSONArray("processed");

		int pendingApprovalLevel = 1;
		CustomAttributeValue customAttributeValue = null;

		for (Object processedApprovalObject : processedApprovalObjectArray) {

			// adding custom attributes dynamically
			for (String attribute : attributes) {

				try {
					customAttributeValue = new CustomAttributeValue();
					customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
					customAttributeValue.setKey(attribute);
					customAttributeValue.setProcessName(processName);

					// checking for DaysTaken attribute
					if (attribute.equals("Daystaken") && processName.equals("leave")) {
						String daysTaken = convertNumberToWord(((JSONObject) processObject).getString(attribute));
						customAttributeValue.setAttributeValue(daysTaken);
					} else if (attribute.equals("BalanceCount") && processName.equals("leave")) {
						String balanceCount = setLeaveBalance(leaveTypeArray,
								((JSONObject) processObject).getString("Leavetype"));
						customAttributeValue.setAttributeValue(balanceCount + " Day(s)");
					} else {
						customAttributeValue.setAttributeValue(((JSONObject) processObject).getString(attribute));
					}

					customAttributeValues.add(customAttributeValue);

				} catch (Exception e) {
					System.err.println("[WBP-Dev][ZOHO][Attributes] missing" + e);
					continue;
				}
			}

			pendingApprovalLevel++;
		}

		if (approvalObject.getInt("currentApprovalStatus") == -1) {

			for (String attribute : attributes) {

				try {
					customAttributeValue = new CustomAttributeValue();
					customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
					customAttributeValue.setKey(attribute);
					customAttributeValue.setProcessName(processName);

					// checking for DaysTaken attribute
					if (attribute.equals("Daystaken") && processName.equals("leave")) {
						String daysTaken = convertNumberToWord(((JSONObject) processObject).getString(attribute));
						customAttributeValue.setAttributeValue(daysTaken);
					} else if (attribute.equals("BalanceCount") && processName.equals("leave")) {
						String balanceCount = setLeaveBalance(leaveTypeArray,
								((JSONObject) processObject).getString("Leavetype"));
						customAttributeValue.setAttributeValue(balanceCount + " Day(s)");
					} else {
						customAttributeValue.setAttributeValue(((JSONObject) processObject).getString(attribute));
					}

					customAttributeValues.add(customAttributeValue);

				} catch (Exception e) {
					System.err.println("[WBP-Dev][teams][Attributes] missing" + e);
					continue;
				}
			}

		}

		return customAttributeValues;
	}

	// For fetching zohoUserId from email
	public String getUserbyZohoEmail(String email, String accessTokenString) {

		Map<String, String> userParameters = new HashMap<>();
		userParameters.put("searchColumn", "EMPLOYEEMAILALIAS");
		userParameters.put("searchValue", email);
		RestResponse taskResponse = restUserZoho.callGetService(ZohoConstants.baseUrl + "forms/P_EmployeeView/records",
				accessTokenString, userParameters);
		JSONObject userObject = new JSONArray((String) taskResponse.getResponseObject()).getJSONObject(0);
		return userObject.getString("ownerID");
	}

	public List<String> getCustomAttributeValues(String processName) {
		return zohocustomAttributeDao.getCustomAttributesByProcess(processName);
	}

	public String convertNumberToWord(String number) {
		System.err.println(number);
		String[] numberDecimals = number.split("[.]");
		Map<String, String> decimals = new HashMap<>();
		decimals.put("5", "half");
		decimals.put("25", "quarter");
		decimals.put("0", "");
		String result = numberDecimals[0];

		if (Integer.parseInt(numberDecimals[1]) != 0) {
			result += " and ";
		}

		result += decimals.get(numberDecimals[1]) + " day";

		if (Integer.parseInt(numberDecimals[0]) > 1
				|| (Integer.parseInt(numberDecimals[0]) == 1 && Integer.parseInt(numberDecimals[1]) != 0)) {
			result += "s";
		}

		return result;
	}

	public String setLeaveBalance(JSONArray leaveTypeArray, String leaveTypeName) {
		String AccessTokenString = accessToken.getAcessToken();
		System.err.println(AccessTokenString);
		String leaveBalance = "0";

		for (Object object : leaveTypeArray) {
			String name = ((JSONObject) object).getString("Name");
			if (name.equals(leaveTypeName)) {
				leaveBalance = (((JSONObject) object).getString("BalanceCount"));
				break;
			}
		}
		return leaveBalance;
	}

}
