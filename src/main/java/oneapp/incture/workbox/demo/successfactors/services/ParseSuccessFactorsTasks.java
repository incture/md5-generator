package oneapp.incture.workbox.demo.successfactors.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dao.UserDetailsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkboxMasterDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.successfactors.dto.TimesheetEntryDetailDto;
import oneapp.incture.workbox.demo.successfactors.dto.TimesheetResponseDto;
import oneapp.incture.workbox.demo.successfactors.util.SuccessFactorsUtil;

//import oneapp.incture.workbox.config.SpringConfiguration;

@Component
public class ParseSuccessFactorsTasks {

	@Autowired
	SuccessFactorsUtil successFactorsUtil;

	@Autowired
	private ProcessEventsDao processEvents;

	@Autowired
	private TaskEventsDao taskEvents;

	@Autowired
	private TaskOwnersDao taskOwners;
	
	@Autowired
	private CustomAttributeDao attributeDao;

	@Autowired
	UserDetailsDao userDetails;

	public ResponseMessage persistSFTasks() {
		WorkboxMasterDto masterDto = parseSFTasks();
		if (!ServicesUtil.isEmpty(masterDto)) {
			try {
				processEvents.saveOrUpdateProcesses(masterDto.getProcesses());
				taskEvents.saveOrUpdateTasks(masterDto.getTasks());
				taskOwners.saveOrUpdateOwners(masterDto.getOwners());

			} catch (Exception ex) {
				System.err.println("[WBP-Dev]Exception while persisting SF Tasks : " + ex.getMessage());
				return new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE,
						"Exception while persisting SF Tasks : " + ex.getMessage());
			}
		}
		return new ResponseMessage(PMCConstant.STATUS_SUCCESS, PMCConstant.CODE_SUCCESS,
				"Persisting SF Tasks Successful");
	}

	public WorkboxMasterDto parseSFTasks() {
		JSONArray taskList = successFactorsUtil.getTaskList();
		JSONObject taskObject = null;
		List<ProcessEventsDto> processes = null;
		List<TaskEventsDto> tasks = null;
		List<TaskOwnersDto> owners = null;
		ProcessEventsDto process = null;
		TaskEventsDto task = null;
		TaskOwnersDto owner = null;

		if (!ServicesUtil.isEmpty(taskList) && taskList.length() > 0) {
			tasks = new ArrayList<>();
			processes = new ArrayList<>();
			owners = new ArrayList<>();

			for (Object object : taskList) {
				taskObject = (JSONObject) object;

				process = new ProcessEventsDto();
				task = new TaskEventsDto();
				owner = new TaskOwnersDto();

				task.setEventId(taskObject.optString("formDataId"));
				task.setCreatedAt(ServicesUtil.convertSuccessFactorsDate(taskObject.optString("dateAssigned")));
				task.setName(taskObject.optString("formTemplateType"));
				task.setSubject(taskObject.optString("currentStep"));
				task.setDescription(taskObject.optString("formTitle"));
				task.setOrigin("SuccessFactors");
				task.setProcessId(taskObject.optString("formTemplateId") + "::" + taskObject.optString("formDataId"));

				task.setStatus("READY");

				process.setName("performance_evaluation");
				process.setProcessId(
						taskObject.optString("formTemplateId") + "::" + taskObject.optString("formDataId"));
				process.setStatus("RUNNING");
				process.setStartedAt(ServicesUtil.convertSuccessFactorsDate(taskObject.optString("creationDate")));
				process.setProcessDefinitionId(taskObject.optString("formTemplateId"));
				process.setStartedBy("P000057");
				process.setStartedByDisplayName("Shruti Patra");

				owner.setEventId(taskObject.optString("formDataId"));
				owner.setTaskOwner(getOwner(task.getSubject()));
				owner.setTaskOwnerDisplayName(getOwnerName(task.getSubject()));
				owner.setOwnerEmail(getOwnerEmail(task.getSubject()));
				owner.setEnRoute(false);

				processes.add(process);
				tasks.add(task);
				owners.add(owner);
			}
		}
		return new WorkboxMasterDto(processes, tasks, owners, null);
	}

	public WorkboxMasterDto parseSuccessFactorTasks(String userId) {

		System.err.println("ParseSuccessFactorsTasks.parseSuccessFactorTasks() userId" + userId);
		String pId = "";
		String name = "";

		 Map<String, List<String>> usersMap = userDetails.getAllSuccessFactorsUserDetails();
		if (!ServicesUtil.isEmpty(usersMap) && !ServicesUtil.isEmpty(usersMap.get(userId))) {
			pId = userId;
			userId = usersMap.get(userId).get(1);
		} else {
			pId = "69121";
			userId = "69121";
		}

		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSuccessFactorTasks() userId" + userId);

		Long start = System.currentTimeMillis();
		JSONArray toDoList = successFactorsUtil.getSuccessFactorTaskList(userId);
		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() toDoList results " + toDoList);
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF LEAVE TASK FETCH TIME: ----> "+(System.currentTimeMillis()-start));
		
		start = System.currentTimeMillis();
		JSONObject taskObject = null;
		List<ProcessEventsDto> processes = null;
		List<TaskEventsDto> tasks = null;
		List<TaskOwnersDto> owners = null;
		List<CustomAttributeValue> customAttrValues = null;
		ProcessEventsDto process = null;
		TaskEventsDto task = null;
		TaskOwnersDto owner = null;
		CustomAttributeValue attributeValue = null;

		if (!ServicesUtil.isEmpty(toDoList) && toDoList.length() > 0) {
			tasks = new ArrayList<>();
			processes = new ArrayList<>();
			owners = new ArrayList<>();

			customAttrValues = new ArrayList<CustomAttributeValue>();
			for (Object object : toDoList) {
				taskObject = (JSONObject) object;

				if (!ServicesUtil.isEmpty(taskObject.optString("entries"))) {
					JSONArray taskList = taskObject.getJSONObject("entries").getJSONArray("results");

					for (Object taskItem : taskList) {
						JSONObject taskdata = (JSONObject) taskItem;
						task = new TaskEventsDto();
						owner = new TaskOwnersDto();
						attributeValue = new CustomAttributeValue();
						process = new ProcessEventsDto();

						process.setName("leave_approval");
					
						if ("Active".equalsIgnoreCase(taskObject.optString("statusLabel"))) {
							process.setStatus("RUNNING");
						} else if ("Completed".equalsIgnoreCase(taskObject.optString("statusLabel"))) {
							process.setStatus("COMPLETED");
						}
						
						process.setProcessId("LE-"+taskdata.optString("subjectId"));
						process.setRequestId(generateProcessId("LE"));
						
						task.setEventId(taskdata.optString("subjectId"));
						task.setCreatedAt(
								ServicesUtil.convertMillisecondsToDate(taskObject.optString("completedDate")));
						task.setName(taskdata.optString("subjectFullName"));
						task.setSubject(taskdata.optString("subjectFullName"));
						task.setDescription(taskdata.optString("subjectFullName"));
						task.setOrigin("SuccessFactor");
						task.setProcessId("LE-"+taskdata.optString("subjectId"));
						task.setProcessName("leave_approval");

						task.setStatus("Active".equalsIgnoreCase(taskdata.optString("statusLabel")) ? "READY" : "");
						task.setCurrentProcessor(userId);

						task.setCurrentProcessorDisplayName(userId);

						System.out.println("Test123.parseSucessFactorTasks() task:" + task);
						owner.setEventId(taskdata.optString("subjectId"));
						owner.setTaskOwner(pId);
						owner.setTaskOwnerDisplayName("");
						owner.setOwnerEmail("");
						owner.setEnRoute(false);
						if (usersMap.containsKey(owner.getTaskOwner())) {

							owner.setOwnerEmail(usersMap.get(owner.getTaskOwner()).get(0));
							owner.setTaskOwnerDisplayName(usersMap.get(owner.getTaskOwner()).get(1));

						}
						
					

						JSONArray customAttr = successFactorsUtil
								.getSuccessFactorCustomAttributes(taskdata.optString("subjectId"), userId);
						
						JSONArray requesterDetails = successFactorsUtil
								.getSuccessFactorTaskLeaveList(taskdata.optString("subjectId"));
						
						name = requesterDetails.getJSONObject(0)
						.optJSONObject("createdByNav").optString("firstName") + requesterDetails.getJSONObject(0)
						.optJSONObject("createdByNav").optString("lastName");
						
						process.setStartedByDisplayName(name);
						
						if (!ServicesUtil.isEmpty(customAttr)) {
							JSONObject custom = (JSONObject) customAttr.get(0);
							attributeValue.setProcessName("leave_approval");
							attributeValue.setTaskId(taskdata.optString("subjectId"));

							attributeValue.setAttributeValue(
									ServicesUtil.convertMillisecondsToDate(custom.optString("startDate")).toString());
							attributeValue.setKey("startDate");

							attributeValue.setAttributeValue(
									ServicesUtil.convertMillisecondsToDate(custom.optString("startDate")).toString());
							attributeValue.setKey("endDate");

							attributeValue.setAttributeValue(custom.optString("editable"));
							attributeValue.setKey("editable");

							attributeValue.setAttributeValue(custom.optString("quantityInDays"));
							attributeValue.setKey("quantityInDays");

							attributeValue.setAttributeValue(custom.optString("quantityInHours"));
							attributeValue.setKey("quantityInHours");

							attributeValue.setAttributeValue(custom.optString("timeType"));
							attributeValue.setKey("timeType");
							
							task.setCreatedAt(ServicesUtil
									.convertMillisecondsToDate(custom.optString("createdDateTime")));
							
							task.setCompletionDeadLine(ServicesUtil
									.convertMillisecondsToDate(custom.optString("startDate")));
							
							process.setStartedBy(custom.optString("userId"));
						}
						
						owners.add(owner);
						tasks.add(task);
						customAttrValues.add(attributeValue);
						processes.add(process);

					}
				}
				
				

			}
		}
		
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF LEAVE TASK PARSING TIME: ----> "+(System.currentTimeMillis()-start));
		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() processes" + processes);
		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() tasks" + tasks);

		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() owners" + owners);

		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() custom attribute" + customAttrValues);

		WorkboxMasterDto masterDto = new WorkboxMasterDto(processes, tasks, owners, customAttrValues);
		start = System.currentTimeMillis();
		System.err.println(
				"ParseSuccessFactorsTasks.parseSuccessFactorTasks() response " + persistSuccessFactorsTasks(masterDto));
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF LEAVE TASK DB TIME: ----> "+(System.currentTimeMillis()-start));
		return masterDto;
	}
	
	
	public WorkboxMasterDto parseSuccessFactorTasks() {

		Long start = System.currentTimeMillis();
		String userId = "sfadmin";
		String pId = "preetham.r@incture.com";
		String name = "";
		 Map<String, List<String>> usersMap = userDetails.getAllSuccessFactorsUserDetails();
			if (!ServicesUtil.isEmpty(usersMap) && !ServicesUtil.isEmpty(usersMap.get(userId))) {
				pId = userId;
				userId = usersMap.get(userId).get(1);
			} else {
				pId = "preetham.r@incture.com";
				userId = "sfadmin";
			}
		JSONArray toDoList = successFactorsUtil.getSuccessFactorTaskList(userId);
		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() toDoList results " + toDoList);
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF LEAVE TASK FETCH TIME: ----> "+(System.currentTimeMillis()-start));
		
		start = System.currentTimeMillis();
		JSONObject taskObject = null;
		List<ProcessEventsDto> processes = null;
		List<TaskEventsDto> tasks = null;
		List<TaskOwnersDto> owners = null;
		List<CustomAttributeValue> customAttrValues = null;
		ProcessEventsDto process = null;
		TaskEventsDto task = null;
		TaskOwnersDto owner = null;
		CustomAttributeValue attributeValue = null;

		if (!ServicesUtil.isEmpty(toDoList) && toDoList.length() > 0) {
			tasks = new ArrayList<>();
			processes = new ArrayList<>();
			owners = new ArrayList<>();

			customAttrValues = new ArrayList<CustomAttributeValue>();
			for (Object object : toDoList) {
				taskObject = (JSONObject) object;

				if (!ServicesUtil.isEmpty(taskObject.optString("entries"))) {
					JSONArray taskList = taskObject.getJSONObject("entries").getJSONArray("results");

					for (Object taskItem : taskList) {
						JSONObject taskdata = (JSONObject) taskItem;
						task = new TaskEventsDto();
						owner = new TaskOwnersDto();
						
						process = new ProcessEventsDto();

						process.setName("leave_approval");
					
						if ("Active".equalsIgnoreCase(taskObject.optString("statusLabel"))) {
							process.setStatus("RUNNING");
						} else if ("Completed".equalsIgnoreCase(taskObject.optString("statusLabel"))) {
							process.setStatus("COMPLETED");
						}
						
						process.setProcessId("LE-"+taskdata.optString("subjectId"));
						process.setRequestId(generateProcessId("LE"));
						
						task.setEventId(taskdata.optString("subjectId"));
						task.setCreatedAt(
								ServicesUtil.convertMillisecondsToDate(taskObject.optString("completedDate")));
						task.setName(taskdata.optString("subjectFullName"));
						task.setSubject(taskdata.optString("subjectFullName"));
						task.setDescription(taskdata.optString("subjectFullName"));
						task.setOrigin("SuccessFactor");
						task.setProcessId("LE-"+taskdata.optString("subjectId"));
						task.setProcessName("leave_approval");

						task.setStatus("Active".equalsIgnoreCase(taskdata.optString("statusLabel")) ? "READY" : "");
						task.setCurrentProcessor("");

						task.setCurrentProcessorDisplayName("");

						System.out.println("Test123.parseSucessFactorTasks() task:" + task);
						owner.setEventId(taskdata.optString("subjectId"));
						owner.setTaskOwner(pId);
						owner.setTaskOwnerDisplayName("");
						owner.setOwnerEmail("");
						owner.setEnRoute(false);
						if (usersMap.containsKey(owner.getTaskOwner())) {

							owner.setOwnerEmail(usersMap.get(owner.getTaskOwner()).get(2));
							owner.setTaskOwnerDisplayName(usersMap.get(owner.getTaskOwner()).get(1));

						}
						
					

						JSONArray customAttr = successFactorsUtil
								.getSuccessFactorCustomAttributes(taskdata.optString("subjectId"), userId);
						
						JSONArray requesterDetails = successFactorsUtil
								.getSuccessFactorTaskLeaveList(taskdata.optString("subjectId"));
						
						name = requesterDetails.getJSONObject(0)
						.optJSONObject("createdByNav").optString("firstName") + requesterDetails.getJSONObject(0)
						.optJSONObject("createdByNav").optString("lastName");
						
						process.setStartedByDisplayName(name);
						
						if (!ServicesUtil.isEmpty(customAttr)) {
							JSONObject custom = (JSONObject) customAttr.get(0);
							attributeValue = new CustomAttributeValue();
							attributeValue.setProcessName("leave_approval");
							attributeValue.setTaskId(taskdata.optString("subjectId"));

							attributeValue.setAttributeValue(
									ServicesUtil.convertMillisecondsToDate(custom.optString("startDate")).toString());
							attributeValue.setKey("startDate");

							customAttrValues.add(attributeValue);
							
							attributeValue = new CustomAttributeValue();
							attributeValue.setProcessName("leave_approval");
							attributeValue.setTaskId(taskdata.optString("subjectId"));
							attributeValue.setAttributeValue(
									ServicesUtil.convertMillisecondsToDate(custom.optString("startDate")).toString());
							attributeValue.setKey("endDate");
							customAttrValues.add(attributeValue);
							
							attributeValue = new CustomAttributeValue();
							attributeValue.setProcessName("leave_approval");
							attributeValue.setTaskId(taskdata.optString("subjectId"));
							attributeValue.setAttributeValue(custom.optString("editable"));
							attributeValue.setKey("editable");
							customAttrValues.add(attributeValue);

							attributeValue = new CustomAttributeValue();
							attributeValue.setProcessName("leave_approval");
							attributeValue.setTaskId(taskdata.optString("subjectId"));
							attributeValue.setAttributeValue(custom.optString("quantityInDays"));
							attributeValue.setKey("quantityInDays");
							customAttrValues.add(attributeValue);

							attributeValue = new CustomAttributeValue();
							attributeValue.setProcessName("leave_approval");
							attributeValue.setTaskId(taskdata.optString("subjectId"));
							attributeValue.setAttributeValue(custom.optString("quantityInHours"));
							attributeValue.setKey("quantityInHours");
							customAttrValues.add(attributeValue);

							attributeValue = new CustomAttributeValue();
							attributeValue.setProcessName("leave_approval");
							attributeValue.setTaskId(taskdata.optString("subjectId"));
							attributeValue.setAttributeValue(custom.optString("timeType"));
							attributeValue.setKey("timeType");
							customAttrValues.add(attributeValue);
							
							task.setCreatedAt(ServicesUtil
									.convertMillisecondsToDate(custom.optString("createdDateTime")));
							
							task.setCompletionDeadLine(ServicesUtil
									.convertMillisecondsToDate(custom.optString("startDate")));
							
							process.setStartedBy(custom.optString("userId"));
						}
						
						owners.add(owner);
						tasks.add(task);
//						customAttrValues.add(attributeValue);
						processes.add(process);

					}
				}
				
				

			}
		}
		Gson g = new Gson();
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF LEAVE TASK PARSING TIME: ----> "+(System.currentTimeMillis()-start));
		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() processes" + g.toJson(processes));
		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() tasks" + g.toJson(tasks));

		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() owners" + g.toJson(owners));

		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSucessFactorTasks() custom attribute" + g.toJson(customAttrValues));

		WorkboxMasterDto masterDto = new WorkboxMasterDto(processes, tasks, owners, customAttrValues);
		start = System.currentTimeMillis();
		System.err.println(
				"ParseSuccessFactorsTasks.parseSuccessFactorTasks() response " + persistSuccessFactorsTasks(masterDto));
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF LEAVE TASK DB TIME: ----> "+(System.currentTimeMillis()-start));
		return masterDto;
	}

	public ResponseMessage persistSuccessFactorsTasks(String userId) {
		WorkboxMasterDto masterDto = parseSuccessFactorTasks(userId);

		if (!ServicesUtil.isEmpty(masterDto)) {
			try {
				processEvents.saveOrUpdateProcesses(masterDto.getProcesses());
				taskEvents.saveOrUpdateTasks(masterDto.getTasks());
				taskOwners.saveOrUpdateOwners(masterDto.getOwners());
			} catch (Exception ex) {
				System.err.println("[WBP-Dev]Exception while persisting SF Tasks : " + ex.getMessage());
				return new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE,
						"Exception while persisting SF Tasks : " + ex.getMessage());
			}
		}
		return new ResponseMessage(PMCConstant.STATUS_SUCCESS, PMCConstant.CODE_SUCCESS,
				"Persisting SF Tasks Successful");
	}

	public TimesheetResponseDto parseSuccessFactorTSEntryDetails(String userId, String timesheetId)
			throws ParseException {
	
		 Map<String, List<String>> usersMap = userDetails.getAllSuccessFactorsUserDetails();
		if (!ServicesUtil.isEmpty(usersMap) && !ServicesUtil.isEmpty(usersMap.get(userId))) {
			userId = usersMap.get(userId).get(1);
		} else {
			userId = "69121";
		}

		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSuccessFactorTasks() userId" + userId);

		System.err.println("ParseSuccessFactorsTasks.parseSuccessFactorTSEntryDetails() userId  " + userId);

		JSONObject responseObject = successFactorsUtil.getSuccessFactorTimeSheetList(userId);
		System.out.println(responseObject);

		TimesheetResponseDto response = new TimesheetResponseDto();

		List<TimesheetEntryDetailDto> processes = new ArrayList<>();

		TimesheetEntryDetailDto process = null;

		JSONObject taskObject = null;
		JSONObject taskObject3 = null;
		JSONObject taskObject2 = null;

		if (responseObject.optJSONArray("timesheetRespDtos").length() > 0) {

			JSONArray toDoList = (JSONArray) responseObject.get("timesheetRespDtos");
			if (!ServicesUtil.isEmpty(toDoList) && toDoList.length() > 0) {

				System.err.println("ParseSuccessFactorsTasks.parseSuccessFactorTSEntryDetails() 1111  ");

				for (Object object : toDoList) {

					taskObject = (JSONObject) object;
					taskObject3 = taskObject.optJSONObject("timesheetDetailsDto");

					System.err.println("ParseSuccessFactorsTasks.parseSuccessFactorTSEntryDetails() 2222  ");

					if (taskObject3.optString("timesheetId").equalsIgnoreCase(timesheetId)) {

						System.err.println("ParseSuccessFactorsTasks.parseSuccessFactorTSEntryDetails() 3333  ");

						JSONArray toDoList2 = (JSONArray) taskObject.get("entryDetailsDtos");

						for (Object object2 : toDoList2) {

							taskObject2 = (JSONObject) object2;

							process = new TimesheetEntryDetailDto();
							process.setActivityType(taskObject2.optString("activityType"));
							process.setHours(taskObject2.optString("hours"));
							process.setProjectCode(taskObject2.optString("projectCode"));
							process.setProjectName(taskObject2.optString("projectName"));
							process.setRequestedOn(taskObject2.optString("requestedOn"));
							process.setTimesheetEntryDate(taskObject2.optString("timesheetEntryDate"));
							process.setTimesheetEntryId(taskObject2.optString("timesheetEntryId"));
							process.setTimeType(taskObject2.optString("timeType"));

							processes.add(process);
						}

						System.err.println("ParseSuccessFactorsTasks.parseSuccessFactorTSEntryDetails() 4444  ");
						break;
					}
				}
			}
		}

		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() processes" + processes);

		response.setTimesheetEntryDetailDto(processes);

		return response;

	}

	public WorkboxMasterDto parseSuccessFactorTimeSheetTasks(String userId) throws ParseException {

		System.err.println("ParseSuccessFactorsTasks.parseSuccessFactorTasks() userId" + userId);
		String pId = "";
		
		Map<String, List<String>> usersMap = userDetails.getAllSuccessFactorsUserDetails();
		if (!ServicesUtil.isEmpty(usersMap) && !ServicesUtil.isEmpty(usersMap.get(userId))) {
			pId = userId;
			userId = usersMap.get(userId).get(1);
		} else {
			pId = "69121";
			userId = "69121";
		}

		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSuccessFactorTasks() userId" + userId);

		Long start = System.currentTimeMillis();
		JSONObject responseObject = successFactorsUtil.getSuccessFactorTimeSheetList(userId);
		System.out.println(responseObject);
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF TimeSheet TASK FETCH TIME: ----> "+(System.currentTimeMillis()-start));

		
		start = System.currentTimeMillis();
		List<ProcessEventsDto> processes = new ArrayList<>();
		List<TaskEventsDto> tasks = new ArrayList<>();
		List<TaskOwnersDto> owners = new ArrayList<>();
		List<CustomAttributeValue> customAttrValues = new ArrayList<>();
		
		List<String> eventId = new ArrayList<String>();

		ProcessEventsDto process = null;
		TaskEventsDto task = null;
		TaskOwnersDto owner = null;
		CustomAttributeValue attributeValue = null;

		JSONObject taskObject = null;
		JSONObject taskObject2 = null;
		JSONObject taskObject3 = null;

		if (responseObject.optJSONArray("timesheetRespDtos").length() > 0) {

			JSONArray toDoList = (JSONArray) responseObject.get("timesheetRespDtos");
			if (!ServicesUtil.isEmpty(toDoList) && toDoList.length() > 0) {

				for (Object object : toDoList) {

					taskObject = (JSONObject) object;
					taskObject3 = taskObject.optJSONObject("timesheetDetailsDto");

					if ("PENDING".equalsIgnoreCase(taskObject3.optString("status"))) {

						JSONArray toDoList2 = (JSONArray) taskObject.get("entryDetailsDtos");

						taskObject2 = toDoList2.getJSONObject(2);

						String processId = "TS-"+taskObject3.optString("timesheetId");
						String requestId = generateProcessId("TS");

						process = new ProcessEventsDto();

						process.setName("TimeSheet");
						process.setProcessId(processId);
						process.setStatus("READY");
						process.setStartedByDisplayName(taskObject3.optString("requesterName"));
						process.setStartedBy(taskObject3.optString("requestedFor"));
						process.setRequestId(requestId);
						processes.add(process);

						if ("READY".equalsIgnoreCase(process.getStatus())) {

							task = new TaskEventsDto();
							owner = new TaskOwnersDto();

							task.setRequestId(taskObject3.optString("timesheetId"));
							task.setEventId(taskObject3.optString("timesheetId"));
							eventId.add(taskObject3.optString("timesheetId"));

							task.setName("TimeSheet Request");
							task.setSubject("TimeSheet Request");
							task.setDescription("Timesheet Request: Week " + taskObject3.optString("weekNumber"));
							task.setOrigin("SuccessFactor");
							task.setProcessId(processId);
							task.setProcessName("TimeSheet");
							task.setStatus("READY");
							task.setCurrentProcessor(userId);

							task.setCurrentProcessorDisplayName(userId);

							task.setCreatedAt(
									ServicesUtil.convertMillisecondsToDate2(taskObject2.optString("requestedOn")));

							task.setCompletionDeadLine(
									ServicesUtil.getNextDates1(taskObject2.optString("requestedOn"), 3));

							System.out.println("Test123.parseSucessFactorTasks() task:" + task);
							owner.setEventId(taskObject3.optString("timesheetId"));
							owner.setTaskOwner(pId);
							owner.setTaskOwnerDisplayName(taskObject3.optString("requesterName"));
							owner.setOwnerEmail("");
							owner.setEnRoute(false);
							if (usersMap.containsKey(owner.getTaskOwner())) {

								owner.setOwnerEmail(usersMap.get(owner.getTaskOwner()).get(0));
								owner.setTaskOwnerDisplayName(usersMap.get(owner.getTaskOwner()).get(1));

							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("stringTotalHours"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue
										.setAttributeValue((taskObject3.optString("stringTotalHours")).toString());
								attributeValue.setKey("totalHours");
								customAttrValues.add(attributeValue);
							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("stringOvertime"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue(taskObject3.optString("stringOvertime"));
								attributeValue.setKey("overtime");
								customAttrValues.add(attributeValue);

							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("stringRegularTime"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue(taskObject3.optString("stringRegularTime"));
								attributeValue.setKey("regularTime");
								customAttrValues.add(attributeValue);

							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("weekNumber"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue(taskObject3.optString("weekNumber"));
								attributeValue.setKey("weekNumber");
								customAttrValues.add(attributeValue);

							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("startDate"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue((taskObject3.optString("startDate")).toString());
								attributeValue.setKey("startDate");
								customAttrValues.add(attributeValue);
							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("endDate"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue((taskObject3.optString("endDate")).toString());
								attributeValue.setKey("endDate");
								customAttrValues.add(attributeValue);
							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("requesterName"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue((taskObject3.optString("requesterName")).toString());
								attributeValue.setKey("requesterName");
								customAttrValues.add(attributeValue);
							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("stringHoliday"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue((taskObject3.optString("stringHoliday")).toString());
								attributeValue.setKey("stringHoliday");
								customAttrValues.add(attributeValue);
							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("stringVacation"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue((taskObject3.optString("stringVacation")).toString());
								attributeValue.setKey("stringVacation");
								customAttrValues.add(attributeValue);
							}

							if (!ServicesUtil.isEmpty(taskObject3.optString("requestedByManager"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue
										.setAttributeValue((taskObject3.optString("requestedByManager")).toString());
								attributeValue.setKey("requestedByManager");
								customAttrValues.add(attributeValue);
							}

							if (!ServicesUtil.isEmpty(taskObject.optString("requesterForName"))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue((taskObject.optString("requesterForName")).toString());
								attributeValue.setKey("requesterForName");
								customAttrValues.add(attributeValue);
							}

							if (!ServicesUtil.isEmpty((taskObject3.optString("requestedFor")))) {
								attributeValue = new CustomAttributeValue();
								attributeValue.setProcessName("TimeSheet");
								attributeValue.setTaskId(taskObject3.optString("timesheetId"));
								attributeValue.setAttributeValue((taskObject3.optString("requestedFor")).toString());
								attributeValue.setKey("userId");
								customAttrValues.add(attributeValue);
							}

						}

						owners.add(owner);
						tasks.add(task);
					}

				}
			}

		}
		
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF TimeSheet TASK PARSING TIME: ----> "+(System.currentTimeMillis()-start));
		taskEvents.deleteTimeSheetTasks(userId,eventId);

		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() processes" + processes);
		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() tasks" + tasks);

		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() owners" + owners);

		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() custom attribute" + customAttrValues);

		WorkboxMasterDto masterDto = new WorkboxMasterDto(processes, tasks, owners, customAttrValues);
		start = System.currentTimeMillis();
		System.err.println(
				"ParseSuccessFactorsTasks.parseSuccessFactorTasks() response " + persistSuccessFactorsTasks(masterDto));
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF TimeSheet TASK DB TIME: ----> "+(System.currentTimeMillis()-start));
		return masterDto;

	}


	public static String generateProcessId(String action) {

		Random rn = new Random();
		StringBuilder processId = new StringBuilder(action);
		int randRequest1 = (int) (1000 + rn.nextInt() * (9999 - 1000) + 1);
		processId.append(randRequest1);
		return processId.toString();
	}


	public WorkboxMasterDto parseSuccessFactorPendingTasks(String userId) throws ParseException {

		System.err.println("ParseSuccessFactorsTasks.parseSuccessFactorTasks() userId" + userId);
		String pId = "";
		Map<String, List<String>> usersMap = null;

		usersMap = userDetails.getAllSuccessFactorsUserDetails();
		if (!ServicesUtil.isEmpty(usersMap) && !ServicesUtil.isEmpty(usersMap.get(userId))) {
			pId = userId;
			userId = usersMap.get(userId).get(1);
		} else {
			pId = "69121";
			userId = "69121";
		}

		System.err.println("[WBP-Dev]ParseSuccessFactorsTasks.parseSuccessFactorTasks() userId" + userId);

		Long start = System.currentTimeMillis();
		JSONObject responseObject = successFactorsUtil.getSuccessFactorPendingTaskList(userId);
		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() toDoList results " + responseObject);
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF Travel and Expense TASK FETCH TIME: ----> "+(System.currentTimeMillis()-start));

		start = System.currentTimeMillis();
		JSONObject taskObject = null;
		List<ProcessEventsDto> processes = new ArrayList<ProcessEventsDto>();
		List<TaskEventsDto> tasks = new ArrayList<TaskEventsDto>();
		List<TaskOwnersDto> owners = new ArrayList<TaskOwnersDto>();
		List<CustomAttributeValue> customAttrValues = new ArrayList<CustomAttributeValue>();
		List<String> eventId = new ArrayList<String>();
		ProcessEventsDto process = null;
		TaskEventsDto task = null;
		TaskOwnersDto owner = null;
		CustomAttributeValue attributeValue = null;

		try {

			if (responseObject.optJSONArray("travelList").length() > 0) {

				JSONArray toDoList = (JSONArray) responseObject.get("travelList");

				if (!ServicesUtil.isEmpty(toDoList) && toDoList.length() > 0) {

					for (Object object : toDoList) {
						taskObject = (JSONObject) object;
						if ("PENDING".equalsIgnoreCase(taskObject.optString("status"))) {

							String processId = "";
							String requestId = generateProcessId("TR");

							process = new ProcessEventsDto();

							process.setName("Travel");
							
							process.setStatus("READY");
							process.setRequestId(requestId);
							
							if ("READY".equalsIgnoreCase(process.getStatus())) {

								JSONObject taskdata = (JSONObject) object;
								task = new TaskEventsDto();
								owner = new TaskOwnersDto();
								attributeValue = new CustomAttributeValue();

								processId = "TR-"+taskdata.optString("requestId");
								process.setProcessId(processId);
								
								task.setRequestId(taskdata.optString("requestId"));
								task.setEventId(taskdata.optString("requestId"));
								eventId.add(taskdata.optString("requestId"));

								task.setName(taskdata.optString("activity"));
								task.setSubject(taskdata.optString("description"));
								task.setDescription(taskdata.optString("description"));
								task.setOrigin("SuccessFactor");
								task.setProcessId(processId);
								task.setProcessName("Travel");
								task.setStatus("READY");
								task.setCurrentProcessor(userId);

								task.setCurrentProcessorDisplayName(userId);

								System.err.println("created......."
										+ ServicesUtil.convertMillisecondsToDate1(taskdata.optString("cretedOn")));

								task.setCreatedAt(
										ServicesUtil.convertMillisecondsToDate1(taskdata.optString("cretedOn")));

								System.out.println("created......."
										+ ServicesUtil.convertMillisecondsToDate1(taskdata.optString("cretedOn")));

								task.setCompletionDeadLine(
										ServicesUtil.getNextDates(taskdata.optString("cretedOn"), 3));

								System.out.println("Completed......."
										+ ServicesUtil.getNextDates(taskdata.optString("cretedOn"), 3));

								System.out.println("Test123.parseSucessFactorTasks() task:" + task);
								owner.setEventId(taskdata.optString("requestId"));
								owner.setTaskOwner(pId);
								owner.setTaskOwnerDisplayName(taskdata.optString("managerName"));
								owner.setOwnerEmail("");
								owner.setEnRoute(false);
								if (usersMap.containsKey(owner.getTaskOwner())) {

									owner.setOwnerEmail(usersMap.get(owner.getTaskOwner()).get(0));
									owner.setTaskOwnerDisplayName(usersMap.get(owner.getTaskOwner()).get(1));

								}

								// JSONArray customAttr = successFactorsUtil
								// .getSuccessFactorCustomAttributes(taskdata.optString("subjectId"),
								// userId);

								// JSONArray customAttr =
								// responseArray.getJSONArray(1);
								if (!ServicesUtil.isEmpty(toDoList.toList()) && toDoList.toList().size() > 0) {

									JSONObject custom = (JSONObject) object;

									if (!ServicesUtil.isEmpty(custom.optString("requestId"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Travel");
										attributeValue.setTaskId(custom.optString("requestId"));
										attributeValue.setAttributeValue((custom.optString("requestId")).toString());
										attributeValue.setKey("travelRequestId");
										customAttrValues.add(attributeValue);
									}

									if (!ServicesUtil.isEmpty(custom.optString("startDate"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Travel");
										attributeValue.setTaskId(custom.optString("requestId"));
										attributeValue.setAttributeValue((custom.optString("startDate")).toString());
										attributeValue.setKey("startDate");
										customAttrValues.add(attributeValue);
									}

									if (!ServicesUtil.isEmpty(custom.optString("endDate"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Travel");
										attributeValue.setTaskId(custom.optString("requestId"));
										attributeValue.setAttributeValue((custom.optString("endDate")).toString());
										attributeValue.setKey("endDate");
										customAttrValues.add(attributeValue);
									}

									if (!ServicesUtil.isEmpty(custom.optString("cretedOn"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Travel");
										attributeValue.setTaskId(custom.optString("requestId"));
										attributeValue.setAttributeValue(custom.optString("cretedOn"));
										attributeValue.setKey("cretedOn");
										customAttrValues.add(attributeValue);

									}

									if (!ServicesUtil.isEmpty(custom.optString("userId"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Travel");
										attributeValue.setTaskId(custom.optString("requestId"));
										attributeValue.setAttributeValue(custom.optString("userId"));
										attributeValue.setKey("userId");
										customAttrValues.add(attributeValue);

									}
									if (!ServicesUtil.isEmpty(custom.optString("userName"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Travel");
										attributeValue.setTaskId(custom.optString("requestId"));
										attributeValue.setAttributeValue(custom.optString("userName"));
										attributeValue.setKey("userName");
										customAttrValues.add(attributeValue);

									}
									if (!ServicesUtil.isEmpty(custom.optString("description"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Travel");
										attributeValue.setTaskId(custom.optString("requestId"));
										attributeValue.setAttributeValue(custom.optString("description"));
										attributeValue.setKey("description");
										customAttrValues.add(attributeValue);

									}
									process.setStartedByDisplayName(custom.optString("userName"));
									process.setStartedBy(custom.optString("userId"));
									


								}
								
								processes.add(process);
								owners.add(owner);
								tasks.add(task);

							}
							// }
						}

					}

				}
			}
		} catch (NullPointerException e) {
			System.out.print("NullPointerException Caught");
		}

		try {

			if (responseObject.optJSONArray("expenseList").length() > 0) {

				JSONArray customAttr = (JSONArray) responseObject.get("expenseList");

				if (!ServicesUtil.isEmpty(customAttr) && customAttr.length() > 0) {

					for (Object object : customAttr) {
						taskObject = (JSONObject) object;
						if ("PENDING".equalsIgnoreCase(taskObject.optString("status"))) {

							String requestId = generateProcessId("ER");
							String processId = "";

							process = new ProcessEventsDto();

							process.setName("Expense");
							process.setStatus("READY");
							process.setRequestId(requestId);

							
							if ("READY".equalsIgnoreCase(process.getStatus())) {
								// if
								// (!ServicesUtil.isEmpty(taskObject.optString("entries")))
								// {
								// JSONArray taskList =
								// taskObject.getJSONObject("entries").getJSONArray("results");

								// for (Object taskItem : toDoList) {
								JSONObject taskdata = (JSONObject) object;
								task = new TaskEventsDto();
								owner = new TaskOwnersDto();
								attributeValue = new CustomAttributeValue();

								processId = "ER-"+taskdata.optString("expenseId");
								process.setProcessId(processId);
								
								task.setRequestId(taskdata.optString("expenseId"));
								task.setEventId(taskdata.optString("expenseId"));
								eventId.add(taskdata.optString("expenseId"));

								task.setName(taskdata.optString("description"));
								task.setSubject(taskdata.optString("description"));
								task.setDescription(taskdata.optString("description"));
								task.setOrigin("SuccessFactor");
								task.setProcessId(processId);
								task.setProcessName("Expense");
								task.setStatus("READY");
								task.setCurrentProcessor(userId);

								task.setCurrentProcessorDisplayName(userId);

								task.setCreatedAt(
										ServicesUtil.convertMillisecondsToDate2(taskdata.optString("requestedDate")));

								task.setCompletionDeadLine(
										ServicesUtil.getNextDates1(taskdata.optString("requestedDate"), 3));

								System.out.println("Test123.parseSucessFactorTasks() task:" + task);
								owner.setEventId(taskdata.optString("expenseId"));
								owner.setTaskOwner(pId);
								owner.setTaskOwnerDisplayName(taskdata.optString("requesterName"));
								owner.setOwnerEmail("");
								owner.setEnRoute(false);
								if (usersMap.containsKey(owner.getTaskOwner())) {

									owner.setOwnerEmail(usersMap.get(owner.getTaskOwner()).get(0));
									owner.setTaskOwnerDisplayName(usersMap.get(owner.getTaskOwner()).get(1));

								}

								if (!ServicesUtil.isEmpty(customAttr.toList()) && customAttr.toList().size() > 0) {

									JSONObject custom = (JSONObject) object;

									if (!ServicesUtil.isEmpty(custom.optString("travelReqId"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue((custom.optString("travelReqId")).toString());
										attributeValue.setKey("travelRequestId");
										customAttrValues.add(attributeValue);
									}

									if (!ServicesUtil.isEmpty(custom.optString("startDate"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue((custom.optString("startDate")).toString());
										attributeValue.setKey("startDate");
										customAttrValues.add(attributeValue);
									}

									if (!ServicesUtil.isEmpty(custom.optString("endDate"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue((custom.optString("endDate")).toString());
										attributeValue.setKey("endDate");
										customAttrValues.add(attributeValue);
									}

									if (!ServicesUtil.isEmpty(custom.optString("requestedDate"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue(custom.optString("requestedDate"));
										attributeValue.setKey("requestedDate");
										customAttrValues.add(attributeValue);

									}

									if (!ServicesUtil.isEmpty(custom.optString("requestedBy"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue(custom.optString("requestedBy"));
										attributeValue.setKey("userId");
										customAttrValues.add(attributeValue);

									}

									if (!ServicesUtil.isEmpty(custom.optString("requesterName"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue(custom.optString("requesterName"));
										attributeValue.setKey("requesterName");
										customAttrValues.add(attributeValue);

									}

									if (!ServicesUtil.isEmpty(custom.optString("totalHomeCurrencyAmount"))) {
										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue(custom.optString("totalHomeCurrencyAmount"));
										attributeValue.setKey("totalHomeCurrencyAmount");
										customAttrValues.add(attributeValue);

									}

									if (!ServicesUtil.isEmpty(custom.optString("totalClaimAmount"))) {

										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue(custom.optString("totalClaimAmount"));
										attributeValue.setKey("totalClaimAmount");
										customAttrValues.add(attributeValue);
									}

									if (!ServicesUtil.isEmpty(custom.optString("approvedAmount"))) {

										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue(custom.optString("approvedAmount"));
										attributeValue.setKey("approvedAmount");
										customAttrValues.add(attributeValue);
									}

									if (!ServicesUtil.isEmpty(custom.optString("baseCurrencyCode"))) {

										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue(custom.optString("baseCurrencyCode"));
										attributeValue.setKey("baseCurrencyCode");
										customAttrValues.add(attributeValue);
									}

									if (!ServicesUtil.isEmpty(custom.optString("targetCurrencyCode"))) {

										attributeValue = new CustomAttributeValue();
										attributeValue.setProcessName("Expense");
										attributeValue.setTaskId(custom.optString("expenseId"));
										attributeValue.setAttributeValue(custom.optString("targetCurrencyCode"));
										attributeValue.setKey("targetCurrencyCode");
										customAttrValues.add(attributeValue);
									}

									process.setStartedByDisplayName(custom.optString("requesterName"));
									process.setStartedBy(custom.optString("requestedBy"));
								}
								
								// task.setCreatedAt(null);

							}
							
							processes.add(process);
							owners.add(owner);
							tasks.add(task);

						}
						// }
					}

				}

			}
		} catch (NullPointerException e) {
			System.out.print("NullPointerException Caught");
		}
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF Travel and Expense TASK PARSING TIME: ----> "+(System.currentTimeMillis()-start));
		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() processes" + processes);
		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() tasks" + tasks);

		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() owners" + owners);

		System.err.println("ParseSuccessFactorsTasks.parseSucessFactorTasks() custom attribute" + customAttrValues);

		WorkboxMasterDto masterDto = new WorkboxMasterDto(processes, tasks, owners, customAttrValues);
		start = System.currentTimeMillis();
		System.err.println(
				"ParseSuccessFactorsTasks.parseSuccessFactorTasks() response " + persistSuccessFactorsTasks(masterDto));
		System.err.println("AdlsaWbECCAdapter.parseDataFromECC() SF Travel and Expense TASK DB TIME: ----> "+(System.currentTimeMillis()-start));
		return masterDto;
	}

	public ResponseMessage persistSuccessFactorsTasks(WorkboxMasterDto masterDto) {
		// WorkboxMasterDto masterDto = parseSuccessFactorTasks(userId);

		if (!ServicesUtil.isEmpty(masterDto)) {
			try {
				processEvents.saveOrUpdateProcesses(masterDto.getProcesses());
				taskEvents.saveOrUpdateTasks(masterDto.getTasks());
				taskOwners.saveOrUpdateOwners(masterDto.getOwners());
				attributeDao.addCustomAttributeValue(masterDto.getAttrDtos());
				
			} catch (Exception ex) {
				System.err.println("Exception while persisting SF Tasks : " + ex.getMessage());
				return new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE,
						"Exception while persisting SF Tasks : " + ex.getMessage());
			}
		}
		return new ResponseMessage(PMCConstant.STATUS_SUCCESS, PMCConstant.CODE_SUCCESS,
				"Persisting SF Tasks Successful");
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

	private String getOwnerName(String name) {
		String ownerName = "";
		switch (name) {
		case "Requestor Task":
			ownerName = "Sai Tharun";
			break;
		case "Manager Approval":
			ownerName = "Kiruthika A";
			break;
		case "Reviewer Approval":
			ownerName = "Shailesh Shetty";
			break;
		case "Performance Review Signing":
			ownerName = "Amit Dey";
			break;
		default:
			ownerName = "Shruti Patra";
			break;
		}
		return ownerName;
	}

	private String getOwnerEmail(String name) {
		String ownerEmail = "";
		switch (name) {
		case "Requestor Task":
			ownerEmail = "sai.tharun@incture.com";
			break;
		case "Manager Approval":
			ownerEmail = "kiruthika.a@incture.com";
			break;
		case "Reviewer Approval":
			ownerEmail = "shailesh.shetty@incture.com";
			break;
		case "Performance Review Signing":
			ownerEmail = "amit.dey@incture.com";
			break;
		default:
			ownerEmail = "shruti.patra@incture.com";
			break;
		}
		return ownerEmail;
	}

	public List<WorkBoxDto> parseTasks(List<String> formTemplateId) {
		JSONArray taskList = successFactorsUtil.getTaskList();
		JSONObject taskObject = null;
		WorkBoxDto task = null;
		List<WorkBoxDto> tasks = null;
		if (!ServicesUtil.isEmpty(taskList)) {
			tasks = new ArrayList<WorkBoxDto>();
			for (Object object : taskList) {
				taskObject = (JSONObject) object;
				task = new WorkBoxDto();

				task.setTaskId(taskObject.optString("formDataId"));
				task.setCreatedAt(ServicesUtil.convertFromDateToString(
						ServicesUtil.convertSuccessFactorsDate(taskObject.optString("dateAssigned"))));
				task.setStartedBy(taskObject.optString("formOriginator"));
				task.setTaskDescription(taskObject.optString("formTitle"));
				task.setSubject(taskObject.optString("currentStep"));
				task.setStatus(getStatus(taskObject.optInt("formDataStatus")));
				task.setProcessName(taskObject.optString("formTemplateId"));
				task.setName(taskObject.optString("formTemplateType"));

				if (!ServicesUtil.isEmpty(formTemplateId) && formTemplateId.size() > 0) {
					if (formTemplateId.contains(task.getProcessName())) {
						tasks.add(task);
					}
				} else {
					tasks.add(task);
				}
			}
		}
		return tasks;
	}

	private String getStatus(int dataStatus) {
		String status = null;
		switch (dataStatus) {
		case 1:
			status = "READY";
			break;
		case 2:
			status = "RESERVED";
			break;
		default:
			status = "READY";
			break;
		}
		return status;
	}

	public List<ProcessEventsDto> parseProcesses() {
		return null;
	}

	public List<TaskOwnersDto> parseOwners() {
		return null;
	}

//	public static void main(String[] args) {
//
//		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
//		ParseSuccessFactorsTasks successFactorsTasks = applicationContext.getBean(ParseSuccessFactorsTasks.class);
//		// List<WorkBoxDto> tasks =
//		// successFactorsTasks.parseTasks(Arrays.asList("141"));
//		// System.out.println(tasks);
//		WorkboxMasterDto masterDto = successFactorsTasks.parseSFTasks();
//		System.out.println(masterDto);
//		((ConfigurableApplicationContext) applicationContext).close();
//
//	}

}
