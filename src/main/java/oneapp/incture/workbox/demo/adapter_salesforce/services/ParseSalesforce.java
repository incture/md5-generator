package oneapp.incture.workbox.demo.adapter_salesforce.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_salesforce.dao.CustomAttributesDao;
import oneapp.incture.workbox.demo.adapter_salesforce.dao.TaskEventDao;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.ParseResponse;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.UserDto;
import oneapp.incture.workbox.demo.adapter_salesforce.util.RestUserSalesforce;
import oneapp.incture.workbox.demo.adapter_salesforce.util.SalesforceConstant;
import oneapp.incture.workbox.demo.adapter_salesforce.util.SalesforceTokenGenerator;

@Repository
public class ParseSalesforce {

	private String accessToken;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private TaskEventDao taskEventsDao;
	
	@Autowired
	private CustomAttributesDao customAttributeDao;
	
	@Autowired
	SalesforceTokenGenerator salesforceTokenGenerator;
	
	@Autowired
	RestUserSalesforce restUserSalesforce;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public ParseResponse parseDetails() {
		ParseResponse parseResponse = null;
		long start = System.currentTimeMillis();

		List<UserDto> users = null;
		List<TaskEventsDto> tasks = null;
		List<ProcessEventsDto> processes = null;
		List<TaskOwnersDto> owners = null;
		List<CustomAttributeValue> customAttributeValues = null;
		JSONObject processObject = null;
		JSONArray processList = null;
		JSONObject taskObject = null;
		JSONArray taskList = null;
		

		try{
	
			Map<String,UserIDPMappingDto> userIDPSFMapping = fetchUsers();
			System.err.println("[WBP-Dev][WORKBOX- Salesforce][getAllUserDetails]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			
			List<String> existingProcessesList = taskEventsDao.getprocessIdByOrigin("Salesforce");
			StringBuilder existingProcesses = new StringBuilder();
			
			for (String str : existingProcessesList) {
				existingProcesses.append("'"+str+"',");
			}
			if(ServicesUtil.isEmpty(existingProcesses))
				existingProcesses.append("'',");
			System.err.println("[WBP-Dev][WORKBOX- Salesforce][API]"+SalesforceConstant.REST_URL_SALESFORCE+
					SalesforceConstant.FETCH_WHOLE_PROCESS_INSTANCE+existingProcesses.substring(0, existingProcesses.length()-1)+")");
			
			Object getAllData = fetchProcessInstances(SalesforceConstant.REST_URL_SALESFORCE+
					SalesforceConstant.FETCH_WHOLE_PROCESS_INSTANCE+existingProcesses.substring(0, existingProcesses.length()-1)+")");
			
			System.err.println("[WBP-Dev][WORKBOX- Salesforce][getProcessDetails]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
		
			if(((JSONObject)getAllData).getInt("totalSize") < 1)
				return parseResponse;
				
			processList = ((JSONObject)getAllData).getJSONArray("records");
			
			processes = new ArrayList<>();
			tasks = new ArrayList<>();
			owners = new ArrayList<>();
			customAttributeValues = new ArrayList<>();
			for (Object object : processList) {
				
				start = System.currentTimeMillis();
				
				processObject = (JSONObject) object;
				
				ProcessEventsDto process = null;
				
				process = setProcess(processObject,userIDPSFMapping);
				System.err.println("[WBP-Dev][WORKBOX- Salesforce][process]"+ (System.currentTimeMillis() - start));
				
				processes.add(process);
				
				taskList = ((JSONObject)processObject).getJSONObject("StepsAndWorkitems").getJSONArray("records");
				
				List<String> taskId = new ArrayList<>();
				for (Object obj2 : taskList) {
					
					taskObject = (JSONObject) obj2;
					
					if("Started".equals(taskObject.getString("StepStatus")))
						continue;
					
					TaskEventsDto task = null;
					TaskOwnersDto owner = new TaskOwnersDto();
					
					task = setTask(taskObject,userIDPSFMapping,processObject);					
					
					owner.setIsProcessed(taskObject.getString("StepStatus").equals(SalesforceConstant.PENDING)?false:true);
					owner.setEventId(taskObject.getString("Id"));
					owner.setOwnerEmail(userIDPSFMapping.get(taskObject.getString("ActorId")).getUserEmail());
					owner.setTaskOwner(userIDPSFMapping.get(taskObject.getString("ActorId")).getUserId());
					owner.setTaskOwnerDisplayName(userIDPSFMapping.get(taskObject.getString("ActorId")).getUserFirstName()+" "+
													userIDPSFMapping.get(taskObject.getString("ActorId")).getUserLastName());
					
					tasks.add(task);
					owners.add(owner);
					
					taskId.add(taskObject.getString("Id"));
					
				}
				
				customAttributeValues.addAll(customAttributeGenerator(
						processObject.getString("TargetObjectId"),taskId));
			}
			
			System.err.println("[WBP-Dev][WORKBOX- Salesforce][final setting to Dtos]" + (System.currentTimeMillis() - start));
			
		}
		catch(Exception e){
			System.err.println("[WBP-Dev][Salesforce][Parse]Error"+e);
		}

		parseResponse = new ParseResponse(tasks, processes, owners, 0, customAttributeValues, users);

		return parseResponse;
	}

	private TaskEventsDto setTask(JSONObject taskObject, Map<String, UserIDPMappingDto> userIDPSFMapping, JSONObject processObject) {
		TaskEventsDto task = new TaskEventsDto();
		
		task.setCompletedAt(null);
		task.setCreatedAt(ServicesUtil.isEmpty(taskObject.optString("CreatedDate")) ? null
				: ServicesUtil.convertAdminFromStringToDate(
						taskObject.optString("CreatedDate")));
		task.setCompletionDeadLine(
				new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
		task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
		task.setCurrentProcessor(userIDPSFMapping.get(taskObject.getString("ActorId")).getUserId());
		task.setCurrentProcessorDisplayName(userIDPSFMapping.get(taskObject.getString("ActorId")).getUserFirstName()+" "+
				userIDPSFMapping.get(taskObject.getString("ActorId")).getUserLastName());
		task.setDescription("Approval of Campaign for "+
				userIDPSFMapping.get(taskObject.getString("CreatedById")).getUserFirstName()+" "+
				userIDPSFMapping.get(taskObject.getString("CreatedById")).getUserLastName());
		task.setName("Campaign Manager");
		task.setOrigin("Salesforce");
		task.setPriority("MEDIUM");
		task.setProcessName("Campaign");
		task.setSubject("Campaign Manager approval for "+
				userIDPSFMapping.get(taskObject.getString("CreatedById")).getUserFirstName()+" "+
				userIDPSFMapping.get(taskObject.getString("CreatedById")).getUserLastName());
		task.setEventId(taskObject.getString("Id"));
		task.setProcessId(processObject.getString("Id"));
		if("Pending".equals(taskObject.getString("StepStatus")))
			task.setStatus("RESERVED");
		else if("Approved".equals(taskObject.getString("StepStatus")) || "Rejected".equals(taskObject.getString("StepStatus")))
			task.setStatus("COMPLETED");
		task.setUpdatedAt(ServicesUtil.isEmpty(taskObject.optString("CreatedDate")) ? null
				: ServicesUtil.convertAdminFromStringToDate(
						taskObject.optString("CreatedDate")));
		return task;
	}

	private ProcessEventsDto setProcess(JSONObject processObject, Map<String, UserIDPMappingDto> userIDPSFMapping) {
		ProcessEventsDto process = new ProcessEventsDto();
		
		process.setCompletedAt(ServicesUtil.isEmpty(processObject.optString("CompletedDate")) ? null
				: ServicesUtil.convertAdminFromStringToDate(
						processObject.optString("CompletedDate")));
		process.setName("Campaign");
		process.setRequestId(processObject.getString("TargetObjectId"));
		process.setProcessDefinitionId(processObject.getString("ProcessDefinitionId"));
		process.setProcessDisplayName("Campaign");
		process.setProcessId(processObject.getString("Id"));
		process.setStartedBy(userIDPSFMapping.get(processObject.getString("CreatedById")).getUserId());
		process.setStartedAt(ServicesUtil.isEmpty(processObject.optString("CreatedDate")) ? null
				: ServicesUtil.convertAdminFromStringToDate(
						processObject.optString("CreatedDate")));
		process.setStartedByDisplayName(userIDPSFMapping.get(processObject.getString("CreatedById")).getUserFirstName()+" "
										+userIDPSFMapping.get(processObject.getString("CreatedById")).getUserLastName());
		String status = processObject.getString("Status");
		if(SalesforceConstant.PENDING.equals(status))
			process.setStatus("RUNNIING");
		else if(SalesforceConstant.APPROVED.equals(status) || SalesforceConstant.REJECTED.equals(status))
			process.setStatus("COMPLETED");
		process.setSubject("Campaign");
		
		return process;
	}

	private List<CustomAttributeValue> customAttributeGenerator(String objectId, List<String> taskId) {
		
		CustomAttributeValue attributeValue = null;
		
		Object getCustomAttributes = fetchCustomAttributes(SalesforceConstant.REST_URL_SALESFORCE+
				SalesforceConstant.FETCH_CUSTOM_ATTRIBUTES+objectId);
		
		List<String> attributes = customAttributeDao.getCustomAttributesByProcess("Campaign");
		JSONObject attribute = (JSONObject)getCustomAttributes;
		List<CustomAttributeValue> customAttributeValues = new ArrayList<>();
		
		System.err.println("[WBP-Dev][Salesforce][attributes]"+attribute);
		for (String str : taskId) {
			
			for (String att : attributes) {
				attributeValue = new CustomAttributeValue();
				
				try{
					attributeValue.setAttributeValue(attribute.get(att).toString());
				}catch(Exception e){
					System.err.println("[WBP-Dev][SalesForce][Attributes] missing"+e);
					continue;
				}
				attributeValue.setTaskId(str);
				attributeValue.setKey(att);
				attributeValue.setProcessName("Campaign");
				
				customAttributeValues.add(attributeValue);
			}
		}
		
		return customAttributeValues;
	}

	@SuppressWarnings("unchecked")
	private Map<String,UserIDPMappingDto> fetchUsers() {
		UserIDPMappingDto userDetails = null;
		
		String userofSF = "Select user_login_name,user_email,user_first_name,user_id,user_last_name,salesforce_id"
				+ " from user_idp_mapping where salesforce_id is not null";
		Query sfUsersQuery = this.getSession().createSQLQuery(userofSF);
		List<Object[]> userList = sfUsersQuery.list();
		
		Map<String, UserIDPMappingDto> userMapping = new HashMap<>();
		for (Object[] obj : userList) {
			
			userDetails = new UserIDPMappingDto();
			userDetails.setUserEmail((String)obj[1]);
			userDetails.setUserFirstName((String)obj[2]);
			userDetails.setUserId((String)obj[3]);
			userDetails.setUserLastName((String)obj[4]);
			userDetails.setUserLoginName((String)obj[0]);
			
			userMapping.put((String)obj[5], userDetails);
		}
		return userMapping;
	}

	private Object fetchProcessInstances(String requestUrl) {
		
		Object responseObject = null;

		
		try{
			
			accessToken = salesforceTokenGenerator.accesstokenUsingUserCredentials(SalesforceConstant.USERNAME,SalesforceConstant.PASSWORD);
			
			RestResponse restResponse = null;

			if (!ServicesUtil.isEmpty(requestUrl)) {
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
	
private Object fetchCustomAttributes(String requestUrl) {
		
		Object responseObject = null;

		
		try{
			
			accessToken = salesforceTokenGenerator.accesstokenUsingUserCredentials(SalesforceConstant.USERNAME,SalesforceConstant.PASSWORD);
			
			RestResponse restResponse = null;

			if (!ServicesUtil.isEmpty(requestUrl)) {
				restResponse = restUserSalesforce.callRestService(requestUrl,accessToken);
			}
			
			if(restResponse != null)
				responseObject = restResponse.getResponseObject();

			
			System.err.println(responseObject);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][Salesforce fetchUsers][error]" + e.getLocalizedMessage());
		}
		
		return responseObject;

	}



}
