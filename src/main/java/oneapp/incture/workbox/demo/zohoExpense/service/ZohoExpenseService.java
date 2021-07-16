package oneapp.incture.workbox.demo.zohoExpense.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import oneapp.incture.workbox.demo.zohoExpense.dao.ZohoExpensecustomAttributeDao;
import oneapp.incture.workbox.demo.zohoExpense.dto.ExpenseDto;
import oneapp.incture.workbox.demo.zohoExpense.util.AcessTokenZohoExpense;
import oneapp.incture.workbox.demo.zohoExpense.util.RestUserZohoExpense;
import oneapp.incture.workbox.demo.zohoExpense.util.ZohoExpenseConstants;



@Service
//@Transactional
public class ZohoExpenseService implements ZohoExpenseServiceLocal {
	@Autowired
	RestUserZohoExpense restUserZoho;

	@Autowired
	AcessTokenZohoExpense accessToken;

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
	private ZohoExpensecustomAttributeDao zohoExpenseCustomAttributeDao;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public ResponseMessage setAll() {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Something went wrong");
		responseMessage.setStatus("Failure");
		responseMessage.setStatusCode("1");

		try {
			List<TaskEventsDto> taskEventsDtos = new ArrayList<>();
			List<TaskOwnersDto> taskOwnersDtos = new ArrayList<>();
			List<CustomAttributeValue> customAttributeValues = new ArrayList<>();

			String accessTokenString = accessToken.getAcessToken();

			Map<String, UserIDPMappingDto> userIDPSFMapping = fetchUsers();
			System.err.println("[WBP-Dev][WORKBOX- Salesforce][getAllUserDetails]");
			System.err.println(userIDPSFMapping);

			System.err.println("[WBP-Dev][ZOHOExpense][setAll] AcessToken : " + accessTokenString);
			RestResponse processReponse = restUserZoho.callGetService(
					ZohoExpenseConstants.baseUrl + "reports/expensedetails", accessTokenString,
					new HashMap<String, String>());
			JSONObject jsonObject = new JSONObject((String) processReponse.getResponseObject());
			JSONArray jsonArray = jsonObject.getJSONArray("expenses");
			List<ProcessEventsDto> processEventsDtos = setProcess(jsonArray, userIDPSFMapping);

			List<String> attributes = zohoExpenseCustomAttributeDao.getCustomAttributesByProcess("ExpenseReport");

			for (ProcessEventsDto processEventsDto : processEventsDtos) {
				RestResponse processReponsetask = restUserZoho.callGetService(ZohoExpenseConstants.baseUrl
						+ "expensereports/" + processEventsDto.getProcessId() + "/approvalhistory", accessTokenString,
						new HashMap<String, String>());
				JSONObject jsonObjecttask = new JSONObject((String) processReponsetask.getResponseObject());
				JSONObject jsonobj = jsonObjecttask.getJSONObject("expense_report");
				JSONArray jsontask = jsonobj.getJSONArray("approval_path");

				JSONArray history = jsonObjecttask.getJSONArray("approval_history");
				int created = history.length() - 3;
				int completed = history.length() - 4;

				int count = 1;
				for (Object object : jsontask) {
					TaskEventsDto taskEventsDto = new TaskEventsDto();
					String status = ((JSONObject) object).optString("status_formatted");
					if (!status.equalsIgnoreCase("Draft")) {
						String eventId = (processEventsDto.getProcessId() + "-" + count);
						taskEventsDto.setEventId(eventId);
						taskEventsDto.setProcessId(processEventsDto.getProcessId());
						taskEventsDto.setRequestId(eventId);
						taskEventsDto.setOrigin("ZohoExpenses");
						taskEventsDto.setPriority("MEDIUM");
						taskEventsDto.setName("ExpenseReport");
						taskEventsDto.setProcessName("ExpenseReport");
						taskEventsDto.setCreatedBy(userIDPSFMapping
								.get(((JSONObject) jsonobj).getString("created_by_id")).getUserFirstName() + " "
								+ userIDPSFMapping.get(((JSONObject) jsonobj).getString("created_by_id"))
										.getUserLastName());
						taskEventsDto.setDescription(
								"Expense Report Submitted by " + processEventsDto.getStartedByDisplayName());
						taskEventsDto.setSubject(
								"Expense Report Submitted by " + processEventsDto.getStartedByDisplayName());

						if (created >= 0) {
							taskEventsDto.setCreatedAt(ServicesUtil.convertFromStringToDateZohoExpense(
									((JSONObject) history.getJSONObject(created)).getString("date") + " "
											+ ((JSONObject) history.getJSONObject(created)).getString("time")));

							System.err.println(((JSONObject) history.getJSONObject(created)).getString("date") + " "
									+ ((JSONObject) history.getJSONObject(created)).getString("time"));

							System.err.println(taskEventsDto.getCreatedAt().getTime());

						}
						created--;
						if (completed >= 0) {
							taskEventsDto.setCompletedAt(ServicesUtil.convertFromStringToDateZohoExpense(
									((JSONObject) history.getJSONObject(completed)).getString("date") + " "
											+ ((JSONObject) history.getJSONObject(completed)).getString("time")));
						}
						completed--;

						taskEventsDto.setCompletionDeadLine(
								new Date(taskEventsDto.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 3)));
						taskEventsDto.setSlaDueDate(
								new Date(taskEventsDto.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 3)));
						taskEventsDto.setUpdatedAt(new Date(taskEventsDto.getCreatedAt().getTime()));

						if (ZohoExpenseConstants.SUBMITTED.equalsIgnoreCase(status)) {
							taskEventsDto.setStatus("READY");
							taskEventsDto.setCurrentProcessor(null);
							taskEventsDto.setCurrentProcessorDisplayName(null);
						} else if (ZohoExpenseConstants.APPROVED.equalsIgnoreCase(status)
								|| ZohoExpenseConstants.REJECTED.equalsIgnoreCase(status)) {
							taskEventsDto.setStatus("COMPLETED");
							taskEventsDto.setCurrentProcessor(
									userIDPSFMapping.get(((JSONObject) object).getString("user_id")).getUserId());
							taskEventsDto.setCurrentProcessorDisplayName(
									userIDPSFMapping.get(((JSONObject) object).getString("user_id")).getUserFirstName()
											+ " " + userIDPSFMapping.get(((JSONObject) object).getString("user_id"))
													.getUserLastName());
						}
						List<CustomAttributeValue> customAttributeValue = setCustomAttributeValues(eventId, jsonobj,
								attributes);
						customAttributeValues.addAll(customAttributeValue);
						taskOwnersDtos.add(taskOwners(eventId, object, userIDPSFMapping));
						taskEventsDtos.add(taskEventsDto);
						count++;
					}

				}
			}

			Gson g = new Gson();
			System.err.println("payload:" + g.toJson(customAttributeValues));
			processEventsDao.saveOrUpdateProcesses(processEventsDtos);
			taskEventsDao.saveOrUpdateTasks(taskEventsDtos);
			taskOwnersDao.saveOrUpdateOwners(taskOwnersDtos);
			customAttributeDao.addCustomAttributeValue(customAttributeValues);
			responseMessage.setMessage("Data Added Successfully");
			responseMessage.setStatus("Success");
			responseMessage.setStatusCode("200");
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ZOHOExpense][setAll]Error" + e);
		}

		return responseMessage;
	}

	private List<CustomAttributeValue> setCustomAttributeValues(String eventId, JSONObject jsonobj,
			List<String> attributes) {
		List<CustomAttributeValue> customAttributeValues = new ArrayList<>();
		for (String attribute : attributes) {

			try {
				if (!"expense".equalsIgnoreCase(attribute)) {
					CustomAttributeValue customAttributeValue = new CustomAttributeValue();
					customAttributeValue.setAttributeValue(((JSONObject) jsonobj).get(attribute).toString());
					customAttributeValue.setTaskId(eventId);
					customAttributeValue.setKey(attribute);
					customAttributeValue.setProcessName("ExpenseReport");
					customAttributeValues.add(customAttributeValue);
				} else {
					ExpenseDto expenseDto = null;
					List<ExpenseDto> expenseDtos = new ArrayList<>();
					JSONArray jArray = (JSONArray) jsonobj.get("expenses");
					for (Object obj : jArray) {
						JSONObject jObj = (JSONObject) obj;
						expenseDto = new ExpenseDto();
						expenseDto.setDate(jObj.opt("date").toString());
						expenseDto.setCategoryName(jObj.opt("category_name").toString());
						expenseDto.setAmount(
								jObj.opt("amount").toString() + " " + jObj.opt("rcy_currency_code").toString());
						expenseDtos.add(expenseDto);
					}
					Gson g = new Gson();
					CustomAttributeValue customAttributeValue = new CustomAttributeValue();
					customAttributeValue.setAttributeValue(g.toJson(expenseDtos));
					customAttributeValue.setTaskId(eventId);
					customAttributeValue.setKey(attribute);
					customAttributeValue.setProcessName("ExpenseReport");
					customAttributeValues.add(customAttributeValue);
				}

			} catch (Exception e) {
				System.err.println("[WBP-Dev][teams][Attributes] missing" + e);
				continue;
			}
		}
		return customAttributeValues;

	}

	private Map<String, UserIDPMappingDto> fetchUsers() {
		UserIDPMappingDto userDetails = null;

		String userofSF = "Select user_login_name,user_email,user_first_name,user_id,user_last_name,Zoho_expense_id"
				+ " from user_idp_mapping where Zoho_expense_id is not null";
		Query sfUsersQuery = this.getSession().createSQLQuery(userofSF);
		List<Object[]> userList = sfUsersQuery.list();

		Map<String, UserIDPMappingDto> userMapping = new HashMap<>();
		for (Object[] obj : userList) {

			userDetails = new UserIDPMappingDto();
			userDetails.setUserLoginName((String) obj[0]);
			userDetails.setUserEmail((String) obj[1]);
			userDetails.setUserFirstName((String) obj[2]);
			userDetails.setUserId((String) obj[3]);
			userDetails.setUserLastName((String) obj[4]);
			userMapping.put((String) obj[5], userDetails);
		}
		return userMapping;
	}

	private TaskOwnersDto taskOwners(String eventId, Object object, Map<String, UserIDPMappingDto> userIDPSFMapping) {
		TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
		taskOwnersDto.setEventId(eventId);
		taskOwnersDto.setTaskOwnerDisplayName(
				userIDPSFMapping.get(((JSONObject) object).getString("user_id")).getUserFirstName() + " "
						+ userIDPSFMapping.get(((JSONObject) object).getString("user_id")).getUserLastName());
		taskOwnersDto.setTaskOwner(userIDPSFMapping.get(((JSONObject) object).getString("user_id")).getUserId());
		taskOwnersDto.setOwnerEmail(userIDPSFMapping.get(((JSONObject) object).getString("user_id")).getUserEmail());
		return taskOwnersDto;
	}

	public List<ProcessEventsDto> setProcess(JSONArray processArray, Map<String, UserIDPMappingDto> userIDPSFMapping) {
		List<ProcessEventsDto> processEventsDtos = new ArrayList<>();
		Map<String, Integer> processId = new HashMap<String, Integer>();

		for (Object object : processArray) {

			if (!processId.containsKey(((JSONObject) object).getString("report_id"))
					&& !ServicesUtil.isEmpty(((JSONObject) object).getString("report_id"))) {
				ProcessEventsDto processEventsDto = new ProcessEventsDto();
				processEventsDto.setProcessId(((JSONObject) object).getString("report_id"));
				processEventsDto.setName("ExpenseReport");
				processEventsDto.setProcessDisplayName("ExpenseReport");
				processEventsDto.setSubject("ZohoExpenses");
				String status = ((JSONObject) object).getString("status")
						.equalsIgnoreCase(ZohoExpenseConstants.SUBMITTED) ? "RUNNING" : "COMPLETED";
				processEventsDto.setStatus(status);
				processEventsDto.setRequestId(((JSONObject) object).getString("report_id"));
				processEventsDto
						.setStartedBy(userIDPSFMapping.get(((JSONObject) object).getString("user_id")).getUserId());
				processEventsDto.setStartedAt(ServicesUtil
						.convertFromStringToDateSubstitution(((JSONObject) object).getString("created_time")));
				processEventsDto.setStartedByDisplayName(
						userIDPSFMapping.get(((JSONObject) object).getString("user_id")).getUserFirstName() + " "
								+ userIDPSFMapping.get(((JSONObject) object).getString("user_id")).getUserLastName());
				processEventsDtos.add(processEventsDto);

			}
			processId.put(((JSONObject) object).getString("report_id"), 1);

		}

		return processEventsDtos;
	}

	@SuppressWarnings("unchecked")
	public Map<String, UserIDPMappingDto> fetchUsers(int type) {
		System.err.println("In fetch users");
		UserIDPMappingDto userDetails = null;
		String userofSF = "Select user_login_name,user_email,user_first_name,user_id,user_last_name,Zoho_expense_id,refresh_token_zoho_expense"
				+ " from user_idp_mapping where Zoho_expense_id is not null";
		Query sfUsersQuery = this.getSession().createSQLQuery(userofSF);
		List<Object[]> userList = sfUsersQuery.list();
		System.err.println(userList);

		Map<String, UserIDPMappingDto> userMapping = new HashMap<>();
		 for (Object[] obj : userList) {
		 userDetails = new UserIDPMappingDto();
		 userDetails.setUserEmail((String) obj[1]);
		 userDetails.setUserFirstName((String) obj[2]);
		 userDetails.setUserId((String) obj[3]);
		 userDetails.setUserLastName((String) obj[4]);
		 userDetails.setUserLoginName((String) obj[0]);
		 userDetails.setZohoId((String) obj[5]);
		 userDetails.setRefreshToken((String) obj[6]);
		
		 // 0 for zohoId as key 1 for puser as ka key
		 int index = type == 0 ? 5 : 3;
		 userMapping.put((String) obj[index], userDetails);
		
		 }

		System.err.println("[WBP-Dev][ZOHO][ZohoServiceImpl][] fetchUsers : " + userMapping);
		return userMapping;
	}

}
