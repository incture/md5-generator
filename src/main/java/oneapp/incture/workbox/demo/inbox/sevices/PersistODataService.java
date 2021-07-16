package oneapp.incture.workbox.demo.inbox.sevices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.EhCacheImplementation;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.RestUtil;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.TaskCacheInstance;
import oneapp.incture.workbox.demo.inbox.util.FieldConstants;
import oneapp.incture.workbox.demo.scpadapter.util.ExternalService;

@Service("PersistODataService")
public class PersistODataService {

	@Autowired
	TaskEventsDao taskEventsDao;

	@Autowired
	ProcessEventsDao processEventsDao;

	@Autowired
	TaskOwnersDao taskOwnersDao;

	@Autowired
	ExternalService externalService;

	@Autowired
	CustomAttributeDao customAttributes;

	@Autowired
	PropertiesConstants getProperty;

	public ResponseMessage persistToDb(TaskOwnersDto owner) {
		List<TaskEventsDto> tasks = getTaskList();

		tasks = checkTasksCache(tasks, owner);

		List<String> taskIds = new ArrayList<String>();
		ResponseMessage response = null;
		for (TaskEventsDto task : tasks) {
			try {
				taskIds.add(task.getEventId());
				long start = System.currentTimeMillis();
				if (!ServicesUtil.isEmpty(task.getCurrentProcessor())
						&& owner.getTaskOwner().equalsIgnoreCase(task.getCurrentProcessor())) {
					owner.setIsProcessed(true);
					owner.setEventId(task.getEventId());
					taskOwnersDao.saveOrUpdateTaskOwner(owner);
					// System.err.println("[WBP-Dev]TaskownerPersisted Current
					// Processor:" + persistedOwner);

				} else {
					owner.setEventId(task.getEventId());
					owner.setIsProcessed(false);
					taskOwnersDao.saveOrUpdateTaskOwner(owner);
					// System.err.println("[WBP-Dev]TaskownerPersisted Not a current
					// Processor:" + persistedOwner);
				}

				// Custom Attribute Values Persisting

				persistCustomAttrValues(task.getProcessId(), task.getProcessName(), task.getEventId());

				TaskEventsDto persistedTask = taskEventsDao.saveOrUpdateTask(task);
				long end = System.currentTimeMillis();
				System.err.println("[WBP-Dev]Task Persisted :" + persistedTask);
				System.err.println("[WBP-Dev]time taken to persist one task :" + (end - start));
			} catch (Exception e) {
				System.err.println("[WBP-Dev]Fail to persist" + e.toString());
				response = new ResponseMessage("Fail", "500", "Fail to persist");
			}
		}
		persistProcess(taskIds);
		response = new ResponseMessage("Success", "200", "Tasks And Owners Persisted");
		return response;
	}

	/* Cache Implementation */
	private List<TaskEventsDto> checkTasksCache(List<TaskEventsDto> tasks, TaskOwnersDto owner) {
		List<TaskEventsDto> deleteTaskInstances = new ArrayList<TaskEventsDto>();
		EhCacheImplementation taskCacheImplementation = new EhCacheImplementation("taskInstanceCache");
		EhCacheImplementation processCacheImplementation = new EhCacheImplementation("processInstanceCache");

		List<String> keys = taskCacheImplementation.getKeys();

		if (!ServicesUtil.isEmpty(tasks) && tasks.size() > 0) {
			if (keys == null || keys.size() <= 0) {
				List<TaskEventsDto> taskDBEvents = taskEventsDao.getAllOpenTasks();
				if (!ServicesUtil.isEmpty(taskDBEvents) && taskDBEvents.size() > 0) {
					for (TaskEventsDto tiValue : taskDBEvents) {
						taskCacheImplementation.putInCache(tiValue.getEventId(), new TaskCacheInstance(
								tiValue.getEventId(), tiValue.getStatus(), tiValue.getCurrentProcessor()));
					}
				}
				keys = taskCacheImplementation.getKeys();
			}

			for (TaskEventsDto tiValue : tasks) {

				if (keys.contains(tiValue.getEventId())) {
					TaskCacheInstance fromCache = taskCacheImplementation.retrieveFromCache(tiValue.getEventId());
					if ((fromCache.getStatus().equals(tiValue.getStatus()))) {
						if (fromCache.getStatus().equals("RESERVED")) {
							if (this.eq(fromCache.getProcessor(), (tiValue.getCurrentProcessor()))) {
								deleteTaskInstances.add(tiValue);
							} else {
								taskCacheImplementation.removeFromCache(tiValue.getEventId());
								taskCacheImplementation.putInCache(tiValue.getEventId(), new TaskCacheInstance(
										tiValue.getEventId(), tiValue.getStatus(), tiValue.getCurrentProcessor()));
							}
						} else {
							deleteTaskInstances.add(tiValue);
							owner.setEventId(tiValue.getEventId());
							taskOwnersDao.saveOrUpdateTaskOwner(owner);
						}
					} else {
						switch (compareStatus(fromCache.getStatus(), tiValue.getStatus())) {
						case (1):
						case (3):
							taskCacheImplementation.removeFromCache(tiValue.getEventId());
							taskCacheImplementation.putInCache(tiValue.getEventId(), new TaskCacheInstance(
									tiValue.getEventId(), tiValue.getStatus(), tiValue.getCurrentProcessor()));
							break;
						case (2):
						case (4):
							taskCacheImplementation.removeFromCache(tiValue.getEventId());
							if (!processExists(tiValue.getProcessId())) {
								processCacheImplementation.removeFromCache(tiValue.getProcessId());
							}
							break;
						default:
							break;
						}
					}
				} else if (tiValue.getStatus().equals("COMPLETED")) {
					deleteTaskInstances.add(tiValue);
				} else {
					taskCacheImplementation.putInCache(tiValue.getEventId(), new TaskCacheInstance(tiValue.getEventId(),
							tiValue.getStatus(), tiValue.getCurrentProcessor()));
				}
			}
			tasks.removeAll(deleteTaskInstances);
		}
		return tasks;
	}

	public boolean persistCustomAttrValues(String processId, String processName, String eventId) {
		System.out.println(
				"CustomAttrbvalues:ProcessId:" + processId + " processName:" + processName + " taskID: " + eventId);
		if (!ServicesUtil.isEmpty(processName)) {
			List<String> keys = customAttributes.getKeysFromTemplate(processName);
			// fatching values from process Id

			Map<String, String> map = externalService.fetchKeyValue(processId, keys);
			System.err.println(map);
			for (String key : map.keySet()) {
				CustomAttributeValue customAttributeValue = new CustomAttributeValue();
				customAttributeValue.setProcessName(processName);
				customAttributeValue.setTaskId(eventId);
				customAttributeValue.setKey(key);
				customAttributeValue.setAttributeValue(map.get(key));
				System.err.println("[WBP-Dev]Custom Attribute Value to be Persited:" + customAttributeValue);

				customAttributes.addCustomAttributeValue(customAttributeValue);
				System.err.println("[WBP-Dev]Custom Attribute Value Persited:" + customAttributeValue);

			}
			return true;

		} else
			return false;
	}

	private boolean processExists(String processId) {
		ProcessEventsDto processEventsDto = new ProcessEventsDto();
		try {
			processEventsDto = getProcessById(processId);
		} catch (Exception ex) {
			System.err.println("[WBP-Dev]Exception while parsing response : " + ex.getMessage());
		}
		if ("RUNNING".equals(processEventsDto.getStatus())) {
			return true;
		}
		return false;
	}

	private static int compareStatus(String cacheStatus, String apiStatus) {
		if (cacheStatus.equals("READY")) {
			if (apiStatus.equals("RESERVED")) {
				return 1;
			} else if (apiStatus.equals("COMPLETED")) {
				return 2;
			}
		} else if (cacheStatus.equals("RESERVED")) {
			if (apiStatus.equals("READY")) {
				return 3;
			} else if (apiStatus.equals("COMPLETED")) {
				return 4;
			}
		}
		return 0;
	}

	private boolean eq(String st1, String st2) {
		boolean ret = false;
		if (st1 == null) {
			if (st2 == null) {
				ret = true;
			}
		} else if (st1.equals(st2)) {
			ret = true;
		}
		return ret;
	}

	public ProcessEventsDto getProcessById(String WorkflowinstanceId) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String url = getProperty.getValue("REQUEST_BASE_URL_TC") + "/workflow-instances?id=" + WorkflowinstanceId;
		ProcessEventsDto dto = null;
		Object responseObject = RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TC, null, "GET",
				"application/json", true, null, null, null, null, null).getResponseObject();
		JSONArray json = ServicesUtil.isEmpty(responseObject) ? null : (JSONArray) responseObject;
		if (json == null)
			return null;
		try {
			dto = new ProcessEventsDto();
			org.json.JSONObject process = json.getJSONObject(0);
			System.err.println("[WBP-Dev]process :" + process);
			dto.setProcessId(process.optString("id"));

			dto.setProcessDisplayName(process.optString("definitionId"));
			// dto.setDefinitionversion(process.optString("definitionVersion"));
			dto.setSubject(process.optString("subject"));
			dto.setStatus(process.optString("status"));
			dto.setRequestId(process.optString("businessKey"));
			if (!ServicesUtil.isEmpty(process.optString("startedAt")))
				dto.setStartedAt(dateFormat.parse(process.optString("startedAt")));
			if (!ServicesUtil.isEmpty(process.optString("completedAt")))
				dto.setCompletedAt(dateFormat.parse(process.optString("completedAt")));
			dto.setStartedBy(process.optString("startedBy"));
		} catch (Exception ex) {
			System.err.println("[WBP-Dev]Exception while fetching process : " + ex.getMessage());
		}
		return dto;

	}

	private void persistProcess(List<String> taskIds) {
		Long start = System.currentTimeMillis();
		List<ProcessEventsDto> processList = getPersistableProcessList(taskIds);
		if (!ServicesUtil.isEmpty(processList) && processList.size() > 0) {
			for (ProcessEventsDto process : processList) {
				processEventsDao.updateTaskEventWithProcessID(process);
				processEventsDao.saveOrUpdateProcessList(process);
			}
		}
		Long end = System.currentTimeMillis();
		System.err.println("[WBP-Dev]Time taken for demon thread : " + (end - start));
	}

	private List<ProcessEventsDto> getPersistableProcessList(List<String> taskIds) {
		if (!ServicesUtil.isEmpty(taskIds) && taskIds.size() > 0)
			return externalService.fetchAllProcessEvents(externalService.fetchAllTaskEvents(taskIds));
		return null;
	}

	public List<TaskEventsDto> getTaskList() {
		String jsonresponse = pullDataFromSCP();
		if (jsonresponse != null)
			return parse(jsonresponse);
		else
			return null;
	}

	private List<TaskEventsDto> parse(String jsonResponse) {

		System.err.println("[WBP-Dev]\nInitializing custom parser...");

		List<TaskEventsDto> taskEventsList = new ArrayList<>();

		JSONParser jsonParser = new JSONParser();
		try {

			System.err.println("[WBP-Dev]\nParsing...");

			// Parse String into JSONObject
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) jsonParser.parse(jsonResponse);
			// Fetch master
			org.json.simple.JSONObject master = (org.json.simple.JSONObject) jsonObject.get("d");
			// Get results array
			org.json.simple.JSONArray resultsArray = (org.json.simple.JSONArray) master.get("results");

			// Iterate each JSONObject items --Tasks
			for (Object entry : resultsArray) {

				try {
					// Generate a unique process Id for each task
					// This is only for object mapping between Task_Event and
					// Process_Event
					// String uniqueProcessId = UUID.randomUUID().toString();

					JSONObject taskEntry = (JSONObject) entry;
					System.err.println("[WBP-Dev]TaskEntry:" + entry);
					// Extract task events
					TaskEventsDto taskEventResult = getTaskEventEntry(taskEntry, "ProcessID");

					/*
					 * // Extract Task Owner TaskOwnersDto taskOwnersDto =
					 * getTaskOwnerEntry(taskEntry);
					 * 
					 * // Extract Process Event ProcessEventsDto
					 * processEventsDto = getProcessEventEntry(taskEntry,
					 * uniqueProcessId);
					 */
					taskEventsList.add(taskEventResult);
					// taskOwnersList.add(taskOwnersDto);
					// processEventsList.add(processEventsDto);
				} catch (Exception ex) {
					System.err.println("[WBP-Dev][ECC_ADAPTER][ODataConsumerService][parse][error] " + ex.getMessage());
				}
			}

			// Set the values
			/*
			 * masterDto.setmTaskEventsDtos(taskEventsList);
			 * masterDto.setmTaskOwnersDtos(taskOwnersList);
			 * masterDto.setmProcessEventsDtos(processEventsList);
			 */

			System.out.println("\nParsing complete.");

		} catch (ParseException e) {
			System.err.println("[WBP-Dev][ECC_ADAPTER][ODataConsumerService][parse][error] " + e.getMessage());
		}

		return taskEventsList;
	}

	private TaskEventsDto getTaskEventEntry(JSONObject taskEntry, String processId) {

		TaskEventsDto dto = new TaskEventsDto();

		// Fetch task instance Id
		if (taskEntry.containsKey(FieldConstants.INSTANCE_ID)) {
			dto.setEventId(((String) taskEntry.get(FieldConstants.INSTANCE_ID)));
		}
		// Fetch task process Id
		if (taskEntry.containsKey(FieldConstants.TASK_DEFINITION_ID)) {
			// dto.setProcessId((String)
			// taskEntry.get(FieldConstants.TASK_DEFINITION_ID));
			// dto.setProcessId(processId);
			dto.setProcessDefinitionId((String) taskEntry.get(FieldConstants.TASK_DEFINITION_ID));
		}
		// Fetch task status
		if (taskEntry.containsKey(FieldConstants.STATUS)) {
			dto.setStatus((String) taskEntry.get(FieldConstants.STATUS));
		}
		// Fetch task priority
		if (taskEntry.containsKey(FieldConstants.PRIORITY)) {
			dto.setPriority((String) taskEntry.get(FieldConstants.PRIORITY));
		}
		// Fetch task description
		if (taskEntry.containsKey(FieldConstants.DESCRIPTION)) {

			JSONObject description = (JSONObject) taskEntry.get(FieldConstants.DESCRIPTION);
			if (description.containsKey(FieldConstants.DESCRIPTION)) {
				dto.setDescription((String) description.get(FieldConstants.DESCRIPTION));
			}
		}
		// Fetch task process name
		if (taskEntry.containsKey(FieldConstants.TASK_DEFINITION_NAME)) {
			dto.setDescription((String) taskEntry.get(FieldConstants.TASK_DEFINITION_NAME));
		}
		// Fetch task name
		if (taskEntry.containsKey(FieldConstants.TASK_TITLE)) {
			if (taskEntry.get(FieldConstants.TASK_TITLE).toString().length() > 30)
				dto.setName(((String) taskEntry.get(FieldConstants.TASK_TITLE)).substring(0, 30));
			else
				dto.setName(((String) taskEntry.get(FieldConstants.TASK_TITLE)));
			dto.setSubject(((String) taskEntry.get(FieldConstants.TASK_DEFINITION_NAME)).trim() + "Task");
		}
		// Fetch task createdOn in long format
		if (taskEntry.containsKey(FieldConstants.CREATED_ON)) {
			String s = (String) taskEntry.get(FieldConstants.CREATED_ON);
			long time = Long.parseLong(s.substring(s.indexOf("(") + 1, s.indexOf(")")));
			dto.setCreatedAt(new Date(time));
		}
		// Fetch task processor
		if (taskEntry.containsKey(FieldConstants.PROCESSOR)) {
			dto.setCurrentProcessor((String) taskEntry.get(FieldConstants.PROCESSOR));
		}
		// Fetch task processor name
		if (taskEntry.containsKey(FieldConstants.PROCESSOR_NAME)) {
			dto.setCurrentProcessorDisplayName((String) taskEntry.get(FieldConstants.PROCESSOR_NAME));
		}
		// Fetch task ExpiryDate
		if (taskEntry.containsKey(FieldConstants.COMPLETION_DEADLINE)) {
			String s = (String) taskEntry.get(FieldConstants.COMPLETION_DEADLINE);
			if (s != null && !s.isEmpty()) {
				long time = Long.parseLong(s.substring(s.indexOf("(") + 1, s.indexOf(")")));
				dto.setCompletionDeadLine(new Date(time));
			}
		}
		// Fetch task ForwardingUser
		if (taskEntry.containsKey(FieldConstants.FORWARDING_USER)) {
			dto.setForwardedBy((String) taskEntry.get(FieldConstants.FORWARDING_USER));
		}

		dto.setTaskType("Human");
		dto.setOrigin("SCP");
		System.err.println("[WBP-Dev]dto response :" + dto);
		return dto;
	}

	private String pullDataFromSCP() {
		String url = getProperty.getValue("REQUEST_BASE_URL_TC") + PMCConstant.TASK_COLLECTION_RELATIVE_URL;
		Object responseObject = RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TC, null, "GET",
				"application/json", true, null, null, null, null, null).getResponseObject();
		org.json.JSONObject responseObj = ServicesUtil.isEmpty(responseObject) ? null
				: (org.json.JSONObject) responseObject;

		if (responseObj == null)
			return null;
		System.err.println("[WBP-Dev]ODATA RESPONSE:" + responseObj.toString());
		return responseObj.toString();

	}

}
