package oneapp.incture.workbox.demo.scpadapter.util;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jsoniter.JsonIterator;
import com.jsoniter.spi.TypeLiteral;
import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.UserDetailsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserDetailsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValuesTableDo;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDoPK;
import oneapp.incture.workbox.demo.adapter_base.mapping.AdminParseResponseObject;
import oneapp.incture.workbox.demo.adapter_base.mapping.Attribute;
import oneapp.incture.workbox.demo.adapter_base.mapping.Task;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstantsStatic;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Component
public class AdminParse {

	private Map<String, UserDetailsDto> usersMap;
	@SuppressWarnings("unused")
	private Map<String, String> groupMappings;

	@Autowired
	UserDetailsDao userDetails;

	@Autowired
	CustomAttributeDao customDao;

	@Autowired
	ProcessConfigDao processConfigDao;

	/*
	 * @Autowired PushNotificationService notificationService;
	 */

	@Autowired
	PropertiesConstants getProperty;

	@Autowired
	SessionFactory sessionFactory;
	

	private int callCount = 0;

	public class AdminParseResponse {

		public AdminParseResponse(List<TaskEventsDto> tasks, List<ProcessEventsDto> processes,
				List<TaskOwnersDto> owners, List<WorkBoxDto> workbox, int resultCount) {
			super();
			this.tasks = tasks;
			this.processes = processes;
			this.owners = owners;
			this.workbox = workbox;
			this.resultCount = resultCount;
		}

		private List<TaskEventsDto> tasks;
		private List<ProcessEventsDto> processes;
		private List<TaskOwnersDto> owners;
		private List<WorkBoxDto> workbox;
		private int resultCount;

		public List<TaskEventsDto> getTasks() {
			return tasks;
		}

		public void setTasks(List<TaskEventsDto> tasks) {
			this.tasks = tasks;
		}

		public List<ProcessEventsDto> getProcesses() {
			return processes;
		}

		public void setProcesses(List<ProcessEventsDto> processes) {
			this.processes = processes;
		}

		public List<TaskOwnersDto> getOwners() {
			return owners;
		}

		public void setOwners(List<TaskOwnersDto> owners) {
			this.owners = owners;
		}

		public List<WorkBoxDto> getWorkbox() {
			return workbox;
		}

		public void setWorkbox(List<WorkBoxDto> workbox) {
			this.workbox = workbox;
		}

		public int getResultCount() {
			return resultCount;
		}

		public void setResultCount(int resultCount) {
			this.resultCount = resultCount;
		}

		@Override
		public String toString() {
			return "AdminParseResponse [tasks=" + tasks + ", processes=" + processes + ", owners=" + owners
					+ ", workbox=" + workbox + ", resultCount=" + resultCount + "]";
		}

	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * ApplicationContext applicationContext = new
	 * AnnotationConfigApplicationContext(SpringConfiguration.class); AdminParse
	 * adminParse = applicationContext.getBean(AdminParse.class);
	 * System.out.println(adminParse.parseDetail());
	 * ((ConfigurableApplicationContext) applicationContext).close();
	 * 
	 * // System.out.println(new Date(new Date().getTime() + (1000 * 60 * 60 *
	 * 24 * 5)));
	 * 
	 * }
	 */

	public AdminParseResponse parseDetail() {
		System.err.println("[WBP-Dev]Start : " + System.currentTimeMillis());

		List<TaskEventsDto> tasks = null;
		List<ProcessEventsDto> processes = null;
		List<TaskOwnersDto> owners = null;

		JSONObject taskObject = null;
		JSONObject processObject = null;

		TaskEventsDto task = null;
		ProcessEventsDto process = null;
		TaskOwnersDto owner = null;

		JSONArray taskList = fetchInstances(
				getProperty.getValue("REQUEST_URL_INST") + "task-instances?status=READY&status=RESERVED");
		JSONArray processList = fetchInstances(getProperty.getValue("REQUEST_URL_INST") + "workflow-instances?");

		if (!ServicesUtil.isEmpty(taskList) && !ServicesUtil.isEmpty(processList) && taskList.length() > 0
				&& processList.length() > 0) {
			tasks = new ArrayList<TaskEventsDto>();
			processes = new ArrayList<ProcessEventsDto>();
			owners = new ArrayList<TaskOwnersDto>();

			for (Object object : processList) {
				processObject = (JSONObject) object;
				process = new ProcessEventsDto();

				process.setProcessId(processObject.optString("id"));
				process.setName(processObject.optString("definitionId"));
				process.setSubject(processObject.optString("subject"));
				process.setStatus(processObject.optString("status"));
				// process.setRequestId(fetchRequestId(process.getProcessId()));
				process.setRequestId(processObject.optString("businessKey"));
				process.setStartedAt(ServicesUtil.isEmpty(processObject.optString("startedAt")) ? null
						: ServicesUtil.convertAdminFromStringToDate(processObject.optString("startedAt")));
				process.setCompletedAt(ServicesUtil.isEmpty(processObject.optString("completedAt")) ? null
						: ServicesUtil.convertAdminFromStringToDate(processObject.optString("completedAt")));
				process.setStartedBy(processObject.optString("startedBy"));
				UserDetailsDto uDetails = userDetails.getUserDetails(new UserDetailsDto(process.getStartedBy(), null));
				if (!ServicesUtil.isEmpty(uDetails)) {
					process.setStartedByDisplayName(uDetails.getDisplayName());
				}

				processes.add(process);
			}

			for (Object object : taskList) {
				taskObject = (JSONObject) object;

				task = new TaskEventsDto();

				task.setEventId(taskObject.optString("id"));
				task.setProcessId(taskObject.optString("workflowInstanceId"));

				task.setProcessName(taskObject.optString("workflowDefinitionId"));

				task.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(taskObject.optString("createdAt")));
				task.setDescription(taskObject.optString("description"));
				task.setCurrentProcessor(taskObject.optString("processor"));

				if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
					UserDetailsDto processorDetails = userDetails
							.getUserDetails(new UserDetailsDto(task.getCurrentProcessor(), null));
					if (!ServicesUtil.isEmpty(processorDetails)) {
						task.setCurrentProcessorDisplayName(processorDetails.getDisplayName());
					}
				}

				task.setSubject(taskObject.optString("subject"));
				task.setStatus(taskObject.optString("status"));
				task.setName(taskObject.optString("definitionId"));
				task.setPriority(taskObject.optString("priority"));
				task.setCompletedAt(ServicesUtil.isEmpty(taskObject.optString("completedAt")) ? null
						: ServicesUtil.convertFromStringToDate(taskObject.optString("completedAt")));

				if (ServicesUtil.isEmpty(taskObject.optString("dueDate"))) {
					task.setCompletionDeadLine(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
					task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
				} else {
					task.setCompletionDeadLine(
							ServicesUtil.convertAdminFromStringToDate(taskObject.optString("dueDate")));
					task.setSlaDueDate(ServicesUtil.convertAdminFromStringToDate(taskObject.optString("dueDate")));
				}

				tasks.add(task);

				if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
					owner = new TaskOwnersDto();
					owner.setEventId(task.getEventId());
					owner.setTaskOwner(task.getCurrentProcessor());
					owner.setIsProcessed(true);
					UserDetailsDto ownerDetails = userDetails
							.getUserDetails(new UserDetailsDto(owner.getTaskOwner(), null));
					if (!ServicesUtil.isEmpty(ownerDetails)) {
						owner.setOwnerEmail(ownerDetails.getEmailId());
						owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName());
					}
					owners.add(owner);
				}

				JSONArray userArray = taskObject.optJSONArray("recipientUsers");
				List<String> recepients = new ArrayList<String>();
				if (!ServicesUtil.isEmpty(userArray) && userArray.length() > 0) {
					for (Object user : userArray) {
						recepients.add((String) user);
					}
				}
				Map<String, String> groupMap = new HashMap<>();
				JSONArray groupArray = taskObject.optJSONArray("recipientGroups");
				for (Object group : groupArray) {
					List<String> recepientsForGroups = getRecipientUserOfGroup((String) group);
					recepients.addAll(recepientsForGroups);
					for (String user : recepientsForGroups) {
						groupMap.put(user, groupMap.containsKey(user) ? (groupMap.get(user) + "," + group.toString())
								: group.toString());
					}
				}

				for (String recepient : recepients) {
					owner = new TaskOwnersDto();
					owner.setEventId(task.getEventId());
					owner.setTaskOwner(recepient);
					owner.setIsProcessed(false);
					owner.setGroupId(groupMap.get(recepient));
					owner.setGroupOwner(groupMap.get(recepient));
					UserDetailsDto ownerDetails = userDetails
							.getUserDetails(new UserDetailsDto(owner.getTaskOwner(), null));
					if (!ServicesUtil.isEmpty(ownerDetails)) {
						owner.setOwnerEmail(ownerDetails.getEmailId());
						owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName());
					}
					owners.add(owner);
				}

				// System.out.println(task.getEventId() + " : " + recepients);
			}

			// System.out.println(processes.size());
			// System.out.println(tasks.size());
			// System.out.println(owners.size());
		}
		System.err.println("[WBP-Dev]End : " + System.currentTimeMillis());
		return new AdminParseResponse(tasks, processes, owners, null, 0);
	}

	public AdminParseResponse parseAdminTasks(String relativeQueryParams) {

		System.err.println("[WBP-Dev]Start : " + System.currentTimeMillis());
		List<WorkBoxDto> workboxes = null;

		JSONObject taskObject = null;

		WorkBoxDto workbox = null;
		Object[] objects = fetchTaskInstances(
				getProperty.getValue("REQUEST_URL_INST") + "task-instances?" + relativeQueryParams);
		JSONArray taskList = (JSONArray) objects[0];
		int resultCount = Integer.valueOf((String) objects[1]);

		if (!ServicesUtil.isEmpty(taskList) && taskList.length() > 0) {
			workboxes = new ArrayList<WorkBoxDto>();

			for (Object object : taskList) {
				taskObject = (JSONObject) object;

				workbox = new WorkBoxDto();

				SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");

				workbox.setTaskId(taskObject.optString("id"));
				workbox.setProcessId(taskObject.optString("workflowInstanceId"));
				workbox.setProcessName(taskObject.optString("workflowDefinitionId"));
				workbox.setCreatedAt(simpleDateFormat1
						.format(ServicesUtil.convertFromStringToDate(taskObject.optString("createdAt"))));
				workbox.setTaskDescription(taskObject.optString("description"));
				workbox.setStatus(taskObject.optString("status"));
				workbox.setSubject(taskObject.optString("subject"));
				workbox.setStartedBy(taskObject.optString("createdBy"));
				workbox.setSlaDisplayDate(ServicesUtil.isEmpty(taskObject.optString("dueDate")) ? null
						: simpleDateFormat1.format(ServicesUtil.resultAsDate(taskObject.optString("dueDate"))));
				if (!ServicesUtil.isEmpty(taskObject.optString("dueDate"))
						&& !ServicesUtil.isEmpty(taskObject.optString("createdAt"))) {
					Calendar created = ServicesUtil.timeStampToCalAdmin(taskObject.optString("createdAt"));
					Calendar slaDate = ServicesUtil.timeStampToCalAdmin(taskObject.optString("dueDate"));
					String timeLeftString = ServicesUtil.getSLATimeLeft(slaDate);
					if (timeLeftString.equalsIgnoreCase("Breach")) {
						workbox.setBreached(true);
					} else {
						workbox.setBreached(false);
						workbox.setTimeLeftDisplayString(timeLeftString);
						workbox.setTimePercentCompleted(ServicesUtil.getPercntTimeCompleted(created, slaDate));
					}
				}

				workboxes.add(workbox);
			}

		}
		System.err.println("[WBP-Dev]End : " + System.currentTimeMillis());
		return new AdminParseResponse(null, null, null, workboxes, resultCount);
	}

	public static String[] getToken() {
		String[] result = new String[2];
		try {
			// String requestUrl =
			// "https://oauthasservices-kbniwmq1aj.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";

			String requestUrl = PropertiesConstantsStatic.NETWORK_REQUEST_URL;
			String userId = PropertiesConstantsStatic.NETWORK_USER_ID;
			String password = PropertiesConstantsStatic.NETWORK_PASSWORD;
			// e5422b86-1f6f-33a6-a6b6-2bd5cabde2a1 , Workbox@123---> Incture
			// workbox demo
			// 00b2004f-5b09-3656-a808-19c174dc2f6d, 123456---------->Incture
			// workbox qa

			String tokenUrl = "https://hrapps.authentication.eu10.hana.ondemand.com/oauth/token";

			String clientId="sb-clone-7142ea58-df10-4a14-aac8-349f85a5334d!b19391|workflow!b10150";
			String clientSecret="b8b7e265-ebb7-4336-9c1b-ece1a50d11de$oN5jKPYAW_Npj9dYTatNg_5uklKEmMyT6md7aDj-U7M=";
			String body = "grant_type=password&username=shruti.patra@incture.com&password=Incture@123&client_id=sb-clone-d6948b2e-75f4-4342-bcc2-3cb5ceec57aa!b19391|workflow!b10150&response_type=toke&client_secret=b34313ff-e7bf-4ce5-96e5-6917fc5a4089$K-_IZvpC-0GxU8CSzAP4amMFEb_OuYNKHWP9-IY3Tqk=";
			
			body = "grant_type=client_credentials&client_id="+clientId+"&client_secret="+clientSecret+"&response_type=token";
			
			String coentType = "application/x-www-form-urlencoded";
			
			Object responseObject = SCPRestUtil
					.callRestService(tokenUrl, null, body, "POST", coentType, false, null, null, null, null, null)
					.getResponseObject();


			Gson g = new Gson(); 
		System.err.println("AdminParse.getToken() response : "+g.toJson(responseObject));
			JSONObject resources = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
			result[0] = resources.optString("access_token");
			result[1] = resources.optString("token_type");
		} catch (Exception e) {
			System.err.println("[PMC][SubstitutionRuleFacade][getPrevRecipient][error]" + e.getMessage());
		}
		return result;
	}

	private static Object[] fetchTaskInstances(String requestUrl) {
		Object[] ret = null;
		System.out.println(requestUrl);
		String[] token = getToken();
		RestResponse restResponse = null;
		requestUrl += "&$inlinecount=allpages";
		restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
				"application/json", false, null, null, null, token[0], token[1]);
		ret = new Object[2];
		ret[0] = (JSONArray) restResponse.getResponseObject();
		if (!ServicesUtil.isEmpty(restResponse.getHttpResponse())) {
			for (Header header : restResponse.getHttpResponse().getAllHeaders()) {
				if ("X-Total-Count".equalsIgnoreCase(header.getName())) {
					ret[1] = header.getValue();
				}
			}
		}
		return ret;
	}

	private JSONArray fetchInstances(String requestUrl) {

		System.err.println("[WBP-Dev]Service Call Counter : " + callCount);
		callCount++;

		int taskInstancesCount = -1;
		Object responseObject = null;
		HttpResponse httpResponse = null;
		RestResponse restResponse = null;
		JSONArray jsonArray = null;
		JSONArray jsonArraySkip = null;

		String[] token = getToken();

		if (!ServicesUtil.isEmpty(requestUrl)) {
			requestUrl += "&$top=1000&$inlinecount=allpages";
			restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					"application/json", false, null, null, null, token[0], token[1]);
		}
		if (restResponse != null) {
			responseObject = restResponse.getResponseObject();
			httpResponse = restResponse.getHttpResponse();
			for (Header header : httpResponse.getAllHeaders()) {
				if (header.getName().equalsIgnoreCase("X-Total-Count")) {
					taskInstancesCount = Integer.parseInt(header.getValue());
				}
			}
			jsonArray = ServicesUtil.isEmpty(responseObject) ? null : (JSONArray) responseObject;
			if (jsonArray != null && taskInstancesCount > jsonArray.length()) {
				int skip = 1000;
				for (int k = 1; k < taskInstancesCount / skip; k++) {
					requestUrl += "&$skip=" + (skip * k);
					restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
							"application/json", false, null, null, null, token[0], token[1]);
					responseObject = restResponse.getResponseObject();
					jsonArraySkip = ServicesUtil.isEmpty(responseObject) ? null : (JSONArray) responseObject;
					jsonArray = mergeJsonArray(jsonArray, jsonArraySkip);
				}
			}
		}
		return jsonArray;
	}

	private static JSONArray mergeJsonArray(JSONArray jsonArray, JSONArray jsonArraySkip) {
		List<Object> object = ServicesUtil.isEmpty(jsonArray) ? null : jsonArray.toList();
		List<Object> objectSkip = ServicesUtil.isEmpty(jsonArraySkip) ? null : jsonArraySkip.toList();
		if (object == null || ServicesUtil.isEmpty(object)) {
			if (ServicesUtil.isEmpty(objectSkip)) {
				return null;
			} else {
				return new JSONArray(objectSkip);
			}
		} else {
			if (!ServicesUtil.isEmpty(objectSkip)) {
				object.addAll(objectSkip);
			}
			return new JSONArray(object);
		}
	}

	private static List<String> getRecipientUserOfGroup(String groupName) {
		List<String> groupUser = new ArrayList<String>();
		String requestUrl = PropertiesConstantsStatic.IAS_SERVICES + "/scim/Users?filter=groups%20eq%20%22" + groupName
				+ "%22";
		Object responseObject = RestUtil
				.callRestService(requestUrl, null, null, "GET", "application/scim+json", false, null,
						PropertiesConstantsStatic.IAS_USER, PropertiesConstantsStatic.IAS_USER_PASS, null, null)
				.getResponseObject();
		JSONObject jsonObject = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
		if (jsonObject != null) {
			JSONArray resources = jsonObject.getJSONArray("Resources");
			JSONObject resource;
			for (Object obj : resources) {
				try {
					resource = (JSONObject) obj;
					groupUser.add(resource.optString("id"));
				} catch (Exception e) {
					System.err.println("[WBP-Dev][getRecipientUserOfGroup][error]" + e.getMessage());
				}
			}
		}
		return groupUser;
	}

	@SuppressWarnings("unused")
	private String fetchRequestId(String processId) {
		String[] token = getToken();
		if (!ServicesUtil.isEmpty(processId)) {
			String processInstanceURL = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances/" + processId
					+ "/context";
			Object responseObject = RestUtil.callRestService(processInstanceURL, PMCConstant.SAML_HEADER_KEY_TC, null,
					"GET", "application/json", true, null, null, null, token[0], token[1]).getResponseObject();
			JSONObject jsonObject = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
			if (jsonObject != null && !ServicesUtil.isEmpty(jsonObject)
					&& jsonObject.toString().contains("RequestId")) {
				return jsonObject.optString("RequestId");
			}
		}
		return null;
	}

	public AdminParseResponse parseCompleteDetail() {
		System.err.println("[WBP-Dev]Start : " + System.currentTimeMillis());

		List<TaskEventsDto> tasks = null;
		List<ProcessEventsDto> processes = null;
		List<TaskOwnersDto> owners = null;

		JSONObject taskObject = null;
		JSONObject processObject = null;

		TaskEventsDto task = null;
		ProcessEventsDto process = null;
		// TaskOwnersDto owner = null;

		JSONArray taskList = fetchInstances(
				getProperty.getValue("REQUEST_URL_INST") + "task-instances?status=COMPLETED");
		JSONArray processList = fetchInstances(getProperty.getValue("REQUEST_URL_INST") + "workflow-instances?");

		if (!ServicesUtil.isEmpty(taskList) && !ServicesUtil.isEmpty(processList) && taskList.length() > 0
				&& processList.length() > 0) {
			tasks = new ArrayList<TaskEventsDto>();
			processes = new ArrayList<ProcessEventsDto>();
			owners = new ArrayList<TaskOwnersDto>();

			for (Object object : processList) {
				processObject = (JSONObject) object;
				process = new ProcessEventsDto();

				process.setProcessId(processObject.optString("id"));
				process.setName(processObject.optString("definitionId"));
				process.setSubject(processObject.optString("subject"));
				process.setStatus(processObject.optString("status"));
				// process.setRequestId(fetchRequestId(process.getProcessId()));
				process.setRequestId(processObject.optString("businessKey"));
				process.setStartedAt(ServicesUtil.isEmpty(processObject.optString("startedAt")) ? null
						: ServicesUtil.convertAdminFromStringToDate(processObject.optString("startedAt")));
				process.setCompletedAt(ServicesUtil.isEmpty(processObject.optString("completedAt")) ? null
						: ServicesUtil.convertAdminFromStringToDate(processObject.optString("completedAt")));
				process.setStartedBy(processObject.optString("startedBy"));
				UserDetailsDto uDetails = userDetails.getUserDetails(new UserDetailsDto(process.getStartedBy(), null));
				if (!ServicesUtil.isEmpty(uDetails)) {
					process.setStartedByDisplayName(uDetails.getDisplayName());
				}

				processes.add(process);
			}

			for (Object object : taskList) {
				taskObject = (JSONObject) object;

				task = new TaskEventsDto();

				task.setEventId(taskObject.optString("id"));
				task.setProcessId(taskObject.optString("workflowInstanceId"));

				task.setProcessName(taskObject.optString("workflowDefinitionId"));

				task.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(taskObject.optString("createdAt")));
				task.setDescription(taskObject.optString("description"));
				task.setCurrentProcessor(taskObject.optString("processor"));

				if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
					UserDetailsDto processorDetails = userDetails
							.getUserDetails(new UserDetailsDto(task.getCurrentProcessor(), null));
					if (!ServicesUtil.isEmpty(processorDetails)) {
						task.setCurrentProcessorDisplayName(processorDetails.getDisplayName());
					}
				}

				task.setSubject(taskObject.optString("subject"));
				task.setStatus(taskObject.optString("status"));
				task.setName(taskObject.optString("definitionId"));
				task.setPriority(taskObject.optString("priority"));
				task.setCompletedAt(ServicesUtil.isEmpty(taskObject.optString("completedAt")) ? null
						: ServicesUtil.convertAdminFromStringToDate(taskObject.optString("completedAt")));

				if (ServicesUtil.isEmpty(taskObject.optString("dueDate"))) {
					task.setCompletionDeadLine(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
					task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
				} else {
					task.setCompletionDeadLine(
							ServicesUtil.convertAdminFromStringToDate(taskObject.optString("dueDate")));
					task.setSlaDueDate(ServicesUtil.convertAdminFromStringToDate(taskObject.optString("dueDate")));
				}

				task.setOrigin("SCP");
				tasks.add(task);

				// if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
				// owner = new TaskOwnersDto();
				// owner.setEventId(task.getEventId());
				// owner.setTaskOwner(task.getCurrentProcessor());
				// owner.setIsProcessed(true);
				// UserDetailsDto ownerDetails = userDetails.getUserDetails(new
				// UserDetailsDto(owner.getTaskOwner(), null));
				// if(!ServicesUtil.isEmpty(ownerDetails)) {
				// owner.setOwnerEmail(ownerDetails.getEmailId());
				// owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName());
				// }
				// owners.add(owner);
				// }

				// JSONArray userArray =
				// taskObject.optJSONArray("recipientUsers");
				// List<String> recepients = new ArrayList<String>();
				// if (!ServicesUtil.isEmpty(userArray) && userArray.length() >
				// 0) {
				// for (Object user : userArray) {
				// recepients.add((String) user);
				// }
				// }
				// JSONArray groupArray =
				// taskObject.optJSONArray("recipientGroups");
				// for (Object group : groupArray) {
				// recepients.addAll(getRecipientUserOfGroup((String) group));
				// }

				// for (String recepient : recepients) {
				// owner = new TaskOwnersDto();
				// owner.setEventId(task.getEventId());
				// owner.setTaskOwner(recepient);
				// owner.setIsProcessed(false);
				// UserDetailsDto ownerDetails = userDetails.getUserDetails(new
				// UserDetailsDto(owner.getTaskOwner(), null));
				// if(!ServicesUtil.isEmpty(ownerDetails)) {
				// owner.setOwnerEmail(ownerDetails.getEmailId());
				// owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName());
				// }
				// owners.add(owner);
				// }

				// System.out.println(task.getEventId() + " : " + recepients);
			}

			// System.out.println(processes.size());
			// System.out.println(tasks.size());
			// System.out.println(owners.size());
		}
		System.err.println("[WBP-Dev]End : " + System.currentTimeMillis());
		return new AdminParseResponse(tasks, processes, owners, null, 0);
	}

	public ResponseMessage updateCustomAttributes(List<TaskEventsDto> tasks) {
		Map<String, List<CustomAttributeTemplate>> customTemplateMap = null;
		try {
			customTemplateMap = getCustomTemplateMap();
		} catch (NoResultFault e) {
			e.printStackTrace();
		}

		System.err.println("[WBP-Dev]AdminParse.updateCustomAttributes() Template map : " + customTemplateMap);
		List<CustomAttributeValue> customValues = new ArrayList<CustomAttributeValue>();
		for (TaskEventsDto task : tasks) {
			Map<String, String> customData = fetchCustomData(task.getEventId());
			// System.err.println("[WBP-Dev]TaskId : "+task.getEventId());
			// for(Entry<String, String> entry : customData.entrySet()) {
			// System.err.println("[WBP-Dev]Map : Key : "+ entry.getKey() + "
			// Value :
			// "+entry.getValue());
			// }

			if (customTemplateMap != null && !ServicesUtil.isEmpty(customTemplateMap)) {
				List<CustomAttributeTemplate> templateList = customTemplateMap.get(task.getProcessName());
				if (!ServicesUtil.isEmpty(templateList) && templateList.size() > 0
						&& !ServicesUtil.isEmpty(customData)) {
					for (CustomAttributeTemplate customTemplate : templateList) {
						customValues.add(new CustomAttributeValue(task.getEventId(), task.getProcessName(),
								customTemplate.getKey(), customData.get(customTemplate.getKey())));
					}
				}
			}
		}
		try {
			System.err
					.println("[WBP-Dev]AdminParse.updateCustomAttributes() custom attribute values : " + customValues);
			// System.err.println("[WBP-Dev]Custom Values To Insert : " +
			// customValues);
			customDao.addCustomAttributeValue(customValues);
			return new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS,
					"Custom Values Inserted Successfully");
		} catch (Exception ex) {
			System.err.println(
					" AdminParse.updateCustomAttributes() Exception while inserting Custom Value Data : " + ex);
			
			return new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE,
					"Custom Values Not Inserted Successfully, Message : " + ex.getMessage());
		}
	}

	private Map<String, String> fetchCustomData(String taskEventId) {
		// String[] tokens = getToken();
		JSONObject context = null;
		Map<String, String> contextData = null;
		RestResponse restResponse = RestUtil.callRestService(
				getProperty.getValue("REQUEST_URL_INST") + "task-instances/" + taskEventId + "/context", null, null,
				PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, true, null, null, null, null, null);
		if (!ServicesUtil.isEmpty(restResponse) && !ServicesUtil.isEmpty(restResponse.getResponseObject())) {
			if (restResponse.getResponseObject().toString().startsWith("{")) {
				contextData = new HashMap<String, String>();
				context = (JSONObject) restResponse.getResponseObject();
				Iterator<String> keys = context.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					contextData.put(key, context.optString(key));
				}
			}
		}
		return contextData;
	}

	private Map<String, List<CustomAttributeTemplate>> getCustomTemplateMap() throws NoResultFault {
		List<ProcessConfigDto> pcEntry = processConfigDao.getAllProcessConfigEntry();
		List<CustomAttributeTemplate> customTemplates = null;
		Map<String, List<CustomAttributeTemplate>> customTemplateMap = null;
		if (!ServicesUtil.isEmpty(pcEntry) && pcEntry.size() > 0) {
			customTemplateMap = new HashMap<>();
			for (ProcessConfigDto pcDto : pcEntry) {
				customTemplates = customDao.getCustomAttributeTemplates(pcDto.getProcessName(), null, null);
				customTemplateMap.put(pcDto.getProcessName(), customTemplates);
			}
		}
		return customTemplateMap;
	}

	@SuppressWarnings("unchecked")
	public AdminParseResponseObject parseAPI(boolean updateSCP) {

		List<TaskEventsDo> tasks = null;
		TaskEventsDo task = null;
		List<CustomAttributeValue> customAttrValueList = new ArrayList<CustomAttributeValue>();
		List<String> taskIdsList = getExistingTaskIds();

		List<TaskOwnersDo> owners = null;
		TaskOwnersDo owner = null;

		List<ProcessEventsDo> processes = null;
		ProcessEventsDo process = null;
       
		List<CustomAttributeValuesTableDo> custValues=new ArrayList<>();
		String requestURL = "";
		System.err.println("[WBP-Dev][parseSCP] getting task instances start : " + System.currentTimeMillis());

		if (!updateSCP)
			requestURL = getProperty.getValue("REQUEST_URL_INST")
					+ "task-instances?status=READY&status=RESERVED&status=CANCELED&status=COMPLETED"
					+ "&lastChangedFrom=" + ServicesUtil.getUtcTimeWithDelay(8000) + "&$expand=attributes";
		else {
			requestURL = getProperty.getValue("REQUEST_URL_INST")
					+ "task-instances?status=READY&status=RESERVED&status=CANCELED&status=COMPLETED&$expand=attributes";
			System.err.println("[WBP-Dev][parseSCP] AdminParse.parseAPI()" + updateSCP);
		}

		System.err.println("[WBP-Dev][parseSCP] AdminParse.parseAPI()" + requestURL);

		List<Task> taskList = getInstances(requestURL, Task.class);

		System.err.println("[WBP-Dev][parseSCP] getting task instances stop : " + System.currentTimeMillis());

		if (!updateSCP)
			requestURL = getProperty.getValue("REQUEST_URL_INST")
					+ "workflow-instances?status=RUNNING&status=ERRONEOUS&status=CANCELED&status=COMPLETED"
					+ "&completedFrom=" + ServicesUtil.getUtcTimeWithDelay(8000);
		else
			requestURL = getProperty.getValue("REQUEST_URL_INST")
					+ "workflow-instances?status=RUNNING&status=ERRONEOUS&status=CANCELED&status=COMPLETED";

		System.err.println("[WBP-Dev][parseSCP] AdminParse.parseAPI()" + requestURL);

		List<oneapp.incture.workbox.demo.adapter_base.mapping.Process> processList = getInstances(requestURL,
				oneapp.incture.workbox.demo.adapter_base.mapping.Process.class);

		System.err.println("[WBP-Dev][parseSCP] getting process instances stop : " + System.currentTimeMillis());

		// System.err.println("[WBP-Dev]AdminParse.parseAPI() Task List :" +
		// taskList);
		// System.err.println("[WBP-Dev]AdminParse.parseAPI() process List :" +
		// processList);

		tasks = new ArrayList<TaskEventsDo>();
		owners = new ArrayList<TaskOwnersDo>();
		processes = new ArrayList<ProcessEventsDo>();

		/*
		 * PushNotificationDto notificationDto = null; List<String> users = new
		 * ArrayList<>(); Boolean notificationFlag = false;
		 */
		System.err.println("[WBP-Dev][parseSCP] Parse Start : " + System.currentTimeMillis());

		System.err.println("[WBP-Dev][parseSCP] task parsing Start : " + System.currentTimeMillis());

		if (!ServicesUtil.isEmpty(taskList) && taskList.size() > 0) {
			System.err.println("[WBP-Dev][parseSCP]Parse taskList size : " + taskList.size());

			for (Task parseTask : taskList) {
				if (!ServicesUtil.isEmpty(parseTask)) {

					task = new TaskEventsDo();

					task.setEventId(parseTask.getId());
					task.setProcessId(parseTask.getWorkflowInstanceId());
					task.setProcessName(parseTask.getWorkflowDefinitionId());
					task.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(parseTask.getCreatedAt()));
					task.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(parseTask.getLastChangedAt()));
					task.setDescription(parseTask.getDescription());

					task.setCurrentProcessor(parseTask.getProcessor());

					if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
//						System.err.println("[WBP-Dev]here :" + parseTask.getStatus() + parseTask.getSubject()
//								+ parseTask.getDescription());
						UserDetailsDto processorDetails = getUserDetails(
								new UserDetailsDto(task.getCurrentProcessor(), null));
						if (!ServicesUtil.isEmpty(processorDetails)) {
							task.setCurrentProcessorDisplayName(processorDetails.getDisplayName());
						}
					}

					task.setSubject(parseTask.getSubject());
					task.setStatus(parseTask.getStatus());
					task.setName(parseTask.getDefinitionId());
					task.setPriority(parseTask.getPriority());
					task.setCompletedAt(ServicesUtil.isEmpty(parseTask.getCompletedAt()) ? null
							: ServicesUtil.convertAdminFromStringToDate(parseTask.getCompletedAt()));

					if (ServicesUtil.isEmpty(parseTask.getDueDate())) {
						task.setCompletionDeadLine(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
						task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
					} else {
						task.setCompletionDeadLine(ServicesUtil.convertAdminFromStringToDate(parseTask.getDueDate()));
						task.setSlaDueDate(ServicesUtil.convertAdminFromStringToDate(parseTask.getDueDate()));
					}

					if (task.getProcessName().equalsIgnoreCase("leaveapprovalmanagement"))
						task.setOrigin("BPM");
					else if (task.getProcessName().equalsIgnoreCase("travelandexpenseapproval"))
						task.setOrigin("SCP");
					else if (task.getProcessName().equalsIgnoreCase("approvalforaward"))
						task.setOrigin("Ariba");
					else if (task.getProcessName().equalsIgnoreCase("campaignmanagementworkflow"))
						task.setOrigin("Salesforce");
					else if (task.getProcessName().equalsIgnoreCase("documentapproval"))
						task.setOrigin("Sharepoint");
					else if (task.getProcessName().equalsIgnoreCase("performanceappraisal"))
						task.setOrigin("SuccessFactors");
					else if (task.getProcessName().equalsIgnoreCase("purchaseorderapproval")
							|| task.getProcessName().equalsIgnoreCase("poapprovalprocess")
							|| task.getProcessName().equalsIgnoreCase("budgetaryapprovalprocess"))
						task.setOrigin("ECC");
					else
						task.setOrigin("SCP");

					// System.err.println("[WBP-Dev]Task Origin Hardcoded to:
					// "+task.getOrigin()+", task Type:
					// "+task.getProcessName());

					// check for the notification
					/* long currentTime = System.currentTimeMillis() - 5000; */// scheduler
																				// is
																				// running
																				// for
																				// every
																				// 3000
																				// millisecs

					if (!ServicesUtil.isEmpty(parseTask.getAttributes())) {
						if (!taskIdsList.contains(parseTask.getId())) {

							// System.err.println("AdminParse.parseAPI()
							// Attributes : "+parseTask.getAttributes());
							for (Attribute attribute : parseTask.getAttributes()) {
								CustomAttributeValue customAttrValue = new CustomAttributeValue();
								customAttrValue.setKey(attribute.getId());
								customAttrValue.setProcessName(parseTask.getWorkflowDefinitionId());
								customAttrValue.setTaskId(parseTask.getId());
								customAttrValue.setAttributeValue(attribute.getValue());

								customAttrValueList.add(customAttrValue);
//								if (parseTask.getWorkflowDefinitionId().equalsIgnoreCase("analyst_appproval_process") || parseTask.getWorkflowDefinitionId().equalsIgnoreCase("inventoryparentworkflow") || parseTask.getWorkflowDefinitionId().equalsIgnoreCase("ic_manager_approval_process")) {
									try {
										CustomAttributeValuesTableDo newTemp=new CustomAttributeValuesTableDo();
									
										newTemp.setKey(attribute.getId());
										newTemp.setProcessName(parseTask.getWorkflowDefinitionId());
										newTemp.setAttributeValue(attribute.getValue());
										newTemp.setTaskId(parseTask.getId());

										if("lineItems".equalsIgnoreCase(newTemp.getKey())  || "forms".equalsIgnoreCase(newTemp.getKey())){
											custValues.addAll(prepareCustomAttributeValues(newTemp));
											System.err.println("WBProduct-Dev customValues"+custValues);
										}else{
										
										custValues.add(newTemp);
										}
									} catch (Exception e) {
										System.err.println("AdminParse.parseAPI() customValues"+e);
									}
//								}
							}
						}
					}

					/*
					 * if (task.getCreatedAt().getTime() >= currentTime) {
					 * System.err.
					 * println("[WBP-Dev]AdminParse.parseAPI() new task found in the scheduler : "
					 * + task);
					 * 
					 * notificationDto = new PushNotificationDto();
					 * 
					 * notificationDto.setData(task.getSubject());
					 * notificationDto.setAlert(task.getProcessName());
					 * 
					 * notificationFlag = true; }
					 */

					// Only to check vendorregistrationprocess tasks
					/*
					 * if(task.getProcessName().equalsIgnoreCase(
					 * "vendorregistrationprocess")){ System.err.
					 * println("AdminParse.parseAPI() task id of vendorregistrationprocess :"
					 * +task.getEventId()); }
					 */
					tasks.add(task);

					// if (!ServicesUtil.isEmpty(parseTask.getAttributes()) &&
					// parseTask.getAttributes().size() > 0) {
					// processDetail = new ProcessDetail();
					// processDetail.setProcessId(task.getProcessId());
					// for (Attribute attribute : parseTask.getAttributes()) {
					// setProcessDetail(processDetail, attribute);
					// }
					// }
					
					UserDetailsDto ownerDetails = new UserDetailsDto();
					if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
						owner = new TaskOwnersDo();
						owner.setTaskOwnersDoPK(new TaskOwnersDoPK(task.getEventId(), task.getCurrentProcessor()));
						owner.setIsProcessed(true);
						if (!ServicesUtil.isEmpty(owner) && !ServicesUtil.isEmpty(owner.getTaskOwnersDoPK()))
							ownerDetails = getUserDetails(
									new UserDetailsDto(owner.getTaskOwnersDoPK().getTaskOwner(), null));
						if (!ServicesUtil.isEmpty(ownerDetails)) {
							owner.setOwnerEmail(ownerDetails.getEmailId() != null ? ownerDetails.getEmailId() : null);
							owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName() != null
									? ownerDetails.getDisplayName() : ownerDetails.getUserId());
						}
						// for Notification
						/*
						 * if (notificationFlag) {
						 * users.add(task.getCurrentProcessor()); }
						 */
						owners.add(owner);
					}

					List<String> recepients = parseTask.getRecipientUsers();
					List<String> recepientGroups = parseTask.getRecipientGroups();
					for (String group : recepientGroups) {
						recepients.addAll(getRecipientUserOfGroup((String) group));
					}

					for (String recepient : recepients) {
						if (!recepient.equalsIgnoreCase(task.getCurrentProcessor())) {
							owner = new TaskOwnersDo();
							owner.setTaskOwnersDoPK(new TaskOwnersDoPK(task.getEventId(), recepient));
							owner.setIsProcessed(false);
							if (!ServicesUtil.isEmpty(owner) && !ServicesUtil.isEmpty(owner.getTaskOwnersDoPK()))
								ownerDetails = getUserDetails(
										new UserDetailsDto(owner.getTaskOwnersDoPK().getTaskOwner(), null));
							if (!ServicesUtil.isEmpty(ownerDetails)) {
								owner.setOwnerEmail(
										ownerDetails.getEmailId() != null ? ownerDetails.getEmailId() : null);
								owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName() != null
										? ownerDetails.getDisplayName() : ownerDetails.getUserId());
							}

							// for Notification
							/*
							 * if (notificationFlag) { users.add(recepient); }
							 */
							owners.add(owner);
						}
					}
				}
			}
		}

		System.err.println("[WBP-Dev][parseSCP] task parsing stop : " + System.currentTimeMillis());
		System.err.println("[WBP-Dev][parseSCP] process parsing stop : " + System.currentTimeMillis());

		if (!ServicesUtil.isEmpty(processList) && processList.size() > 0) {
			System.err.println("[WBP-Dev][parseSCP]Parse processList size : " + processList.size());

			for (oneapp.incture.workbox.demo.adapter_base.mapping.Process parseProcess : processList) {
				if (!ServicesUtil.isEmpty(parseProcess)) {

					process = new ProcessEventsDo();

					process.setProcessId(parseProcess.getId());

					process.setName(parseProcess.getDefinitionId());
					process.setSubject(parseProcess.getSubject());
					process.setStatus(parseProcess.getStatus());

					process.setRequestId(parseProcess.getBusinessKey());
					process.setStartedAt(ServicesUtil.isEmpty(parseProcess.getStartedAt()) ? null
							: ServicesUtil.convertAdminFromStringToDate(parseProcess.getStartedAt()));
					process.setCompletedAt(ServicesUtil.isEmpty(parseProcess.getCompletedAt()) ? null
							: ServicesUtil.convertAdminFromStringToDate(parseProcess.getCompletedAt()));
					process.setStartedBy(parseProcess.getStartedBy());
					UserDetailsDto uDetails = getUserDetails(new UserDetailsDto(process.getStartedBy(), null));
					if (!ServicesUtil.isEmpty(uDetails)) {
						process.setStartedByDisplayName(uDetails.getDisplayName());
					}

					processes.add(process);

				}
			}
		}

		System.err.println("[WBP-Dev][parseSCP] process parsing stop : " + System.currentTimeMillis());

		// Send notification to recipient users
		/*
		 * if (notificationFlag) { notificationDto.setUsers(users);
		 * notificationService.notifyUser(notificationDto); }
		 */

		System.err.println("[WBP-Dev][parseSCP]Parse End : " + System.currentTimeMillis());

		emptyGroupsMapping();
		emptyUsersMap();
		// notificationFlag = false;

		// System.err.println("[WBP-Dev]AdminParse.parseAPI() parsed tasks list
		// :
		// "+tasks);
		// System.err.println("[WBP-Dev]AdminParse.parseAPI() parsed process
		// list :
		// "+processes);
		System.err.println("[WBP-Dev]AdminParse.parseAPI() parsed owners list : " + owners);

		return new AdminParseResponseObject(tasks, processes, owners, null, 0, customAttrValueList,custValues);
	}

	private List<CustomAttributeValuesTableDo> prepareCustomAttributeValues(CustomAttributeValuesTableDo newTemp) {
		
		List<CustomAttributeValuesTableDo> values=new ArrayList<>();
		try{
			
			JsonParser parser = new JsonParser();
			String lines = newTemp.getAttributeValue();
			JsonElement jsonElement = parser.parse(lines);
			JsonArray jsonArray = jsonElement.getAsJsonArray();

			int index=1;
			for (Object jsonObj : jsonArray) {
				JsonParser jsonParser = new JsonParser();
				
				JsonElement element = jsonParser.parse(jsonObj.toString());
				JsonObject jsonObject = element.getAsJsonObject();
				Set<String> keys = jsonObject.keySet();
				for (String key : keys) {
					
					if(key.equalsIgnoreCase("formData")){
						JsonArray array = jsonObject.getAsJsonArray(key);
					
						int findex=index*100;
						for (Object object : array) {
							JsonElement element2 = jsonParser.parse(object.toString());
							JsonObject formJsonObje = element2.getAsJsonObject();
							Set<String> keys2 = formJsonObje.keySet();
							for(String k:keys2){
								CustomAttributeValuesTableDo doValue=new CustomAttributeValuesTableDo();
								
								if("valueHelp".equalsIgnoreCase(k))
								doValue.setAttributeValue(formJsonObje.getAsJsonArray(k).toString());
								else
									doValue.setAttributeValue(formJsonObje.get(k).getAsString());
								
								doValue.setKey(k);
								doValue.setTaskId(newTemp.getTaskId());
								doValue.setProcessName(newTemp.getProcessName());
								doValue.setIndex(findex);
								values.add(doValue);
								
							}
							findex++;
						}
					}else{
						CustomAttributeValuesTableDo doValue=new CustomAttributeValuesTableDo();
						if(!"lineItems".equalsIgnoreCase(key)){
					doValue.setAttributeValue(jsonObject.get(key).getAsString());
					doValue.setKey(key);
					doValue.setTaskId(newTemp.getTaskId());
					doValue.setProcessName(newTemp.getProcessName());
					doValue.setIndex(index);
					values.add(doValue);
						}
					}
				}
				index++;
			}
		
			
		}catch(Exception e){
			System.err.println("AdminParse.prepareCustomAttributeValues() error : "+e);
			e.printStackTrace();
		}
		
		return values;
	}
	
//	public static void main(String[] args) {
//		String value="[{\"lineItems\":[],\"formId\":\"J-3024\",\"formStatus\":\"SUBMITTED\",\"formData\":[{\"valueHelp\":[],\"value\":\"dfdsf\",\"key\":\"What measures have been put into place to avoid further correction?\"},{\"valueHelp\":[\"Process\",\"Training\",\"Individual\",\"Tools\"],\"value\":\"Process\",\"key\":\"What was the main reason for the system correction?\"},{\"valueHelp\":[],\"value\":\"sdfdsf\",\"key\":\"Please provide details of any additional training or action against an individual or group taken as a result of the correction\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"Yes\",\"key\":\"What was the issue raised in JOS meeting?\"},{\"valueHelp\":[],\"value\":\"sdfdsf\",\"key\":\"Analyst Comment\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"Was there a corrective action raised\"},{\"valueHelp\":[],\"value\":\"\",\"key\":\"Manager Comment\"}]}]";
//				
//		CustomAttributeValuesTableDo tt=new CustomAttributeValuesTableDo();
//		tt.setKey("lineItems");
//		tt.setProcessName("process");
//		tt.setTaskId("task124");
//		tt.setAttributeValue(value);
//		Gson g = new Gson();
//		System.out.println("AdminParse.main()"+g.toJson(prepareCustomAttributeValues(tt)) );
//	}

	@SuppressWarnings("unchecked")
	public AdminParseResponseObject parseSCPAPI() {

		List<TaskEventsDo> tasks = null;
		TaskEventsDo task = null;
		List<CustomAttributeValue> customAttrValueList = new ArrayList<CustomAttributeValue>();
		List<String> taskIdsList = getExistingTaskIds();

		List<TaskOwnersDo> owners = null;
		TaskOwnersDo owner = null;

		List<ProcessEventsDo> processes = null;
		ProcessEventsDo process = null;
		Date date = new Date();
		date.setTime(date.getTime() + 5000);
		String lastSynced = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);

		System.err.println("[WBP-Dev][parseAPI]Fetch Start : " + System.currentTimeMillis());

		List<Task> taskList = getWorkflowInstances(getProperty.getValue("REQUEST_URL_INST")
				+ "task-instances?status=READY&status=RESERVED&$expand=attributes", Task.class, null);
		List<Task> taskListComplete = getWorkflowInstances(getProperty.getValue("REQUEST_URL_INST")
				+ "task-instances?status=CANCELED&status=COMPLETED&$expand=attributes", Task.class, lastSynced);
		List<oneapp.incture.workbox.demo.adapter_base.mapping.Process> processList = getWorkflowInstances(
				getProperty.getValue("REQUEST_URL_INST")
						+ "workflow-instances?status=RUNNING&status=ERRONEOUS&status=CANCELED&status=COMPLETED",
						oneapp.incture.workbox.demo.adapter_base.mapping.Process.class, null);

		if (!ServicesUtil.isEmpty(taskList)) {
			taskList.addAll(taskListComplete);
		} else {
			taskList = taskListComplete;
		}

		// System.err.println("[WBP-Dev]AdminParse.parseAPI() Task List :" +
		// taskList);
		// System.err.println("[WBP-Dev]AdminParse.parseAPI() process List :" +
		// processList);

		System.err.println("[WBP-Dev][parseAPI]Fetch End : " + System.currentTimeMillis());

		tasks = new ArrayList<TaskEventsDo>();
		owners = new ArrayList<TaskOwnersDo>();
		processes = new ArrayList<ProcessEventsDo>();

		/*
		 * PushNotificationDto notificationDto = null; List<String> users = new
		 * ArrayList<>(); Boolean notificationFlag = false;
		 */

		System.err.println("[WBP-Dev][parseAPI]Parse Start : " + System.currentTimeMillis());

		if (!ServicesUtil.isEmpty(taskList) && taskList.size() > 0) {
			System.err.println("[WBP-Dev][parseAPI]Parse taskList size : " + taskList.size());

			for (Task parseTask : taskList) {
				if (!ServicesUtil.isEmpty(parseTask)) {

					task = new TaskEventsDo();

					task.setEventId(parseTask.getId());
					task.setProcessId(parseTask.getWorkflowInstanceId());
					task.setProcessName(parseTask.getWorkflowDefinitionId());
					task.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(parseTask.getCreatedAt()));
					task.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(parseTask.getLastChangedAt()));
					task.setDescription(parseTask.getDescription());

					task.setCurrentProcessor(parseTask.getProcessor());

					if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
//						System.err.println("[WBP-Dev]here :" + parseTask.getStatus() + parseTask.getSubject()
//								+ parseTask.getDescription());
						UserDetailsDto processorDetails = getUserDetails(
								new UserDetailsDto(task.getCurrentProcessor(), null));
						if (!ServicesUtil.isEmpty(processorDetails)) {
							task.setCurrentProcessorDisplayName(processorDetails.getDisplayName());
						}
					}

					task.setSubject(parseTask.getSubject());
					task.setStatus(parseTask.getStatus());
					task.setName(parseTask.getDefinitionId());
					task.setPriority(parseTask.getPriority());
					task.setCompletedAt(ServicesUtil.isEmpty(parseTask.getCompletedAt()) ? null
							: ServicesUtil.convertAdminFromStringToDate(parseTask.getCompletedAt()));

					if (ServicesUtil.isEmpty(parseTask.getDueDate())) {
						task.setCompletionDeadLine(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
						task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
					} else {
						task.setCompletionDeadLine(ServicesUtil.convertAdminFromStringToDate(parseTask.getDueDate()));
						task.setSlaDueDate(ServicesUtil.convertAdminFromStringToDate(parseTask.getDueDate()));
					}

					if (task.getProcessName().equalsIgnoreCase("leaveapprovalmanagement"))
						task.setOrigin("BPM");
					else if (task.getProcessName().equalsIgnoreCase("travelandexpenseapproval"))
						task.setOrigin("SCP");
					else if (task.getProcessName().equalsIgnoreCase("performanceappraisal"))
						task.setOrigin("SuccessFactors");
					else if (task.getProcessName().equalsIgnoreCase("purchaseorderapproval")
							|| task.getProcessName().equalsIgnoreCase("poapprovalprocess")
							|| task.getProcessName().equalsIgnoreCase("budgetaryapprovalprocess"))
						task.setOrigin("ECC");
					else
						task.setOrigin("SCP");

					// System.err.println("[WBP-Dev]Task Origin Hardcoded to:
					// "+task.getOrigin()+", task Type:
					// "+task.getProcessName());

					// check for the notification
					/* long currentTime = System.currentTimeMillis() - 5000; */// scheduler
																				// is
																				// running
																				// for
																				// every
																				// 3000
																				// millisecs

					if (!ServicesUtil.isEmpty(parseTask.getAttributes())) {
						if (!taskIdsList.contains(parseTask.getId())) {

							// System.err.println("AdminParse.parseAPI()
							// Attributes : "+parseTask.getAttributes());
							for (Attribute attribute : parseTask.getAttributes()) {
								CustomAttributeValue customAttrValue = new CustomAttributeValue();
								customAttrValue.setKey(attribute.getId());
								customAttrValue.setProcessName(parseTask.getWorkflowDefinitionId());
								customAttrValue.setTaskId(parseTask.getId());
								customAttrValue.setAttributeValue(attribute.getValue());

								customAttrValueList.add(customAttrValue);
							}
						}
					}

					/*
					 * if (task.getCreatedAt().getTime() >= currentTime) {
					 * System.err.
					 * println("[WBP-Dev]AdminParse.parseAPI() new task found in the scheduler : "
					 * + task);
					 * 
					 * notificationDto = new PushNotificationDto();
					 * 
					 * notificationDto.setData(task.getSubject());
					 * notificationDto.setAlert(task.getProcessName());
					 * 
					 * notificationFlag = true; }
					 */

					// Only to check vendorregistrationprocess tasks
					/*
					 * if(task.getProcessName().equalsIgnoreCase(
					 * "vendorregistrationprocess")){ System.err.
					 * println("AdminParse.parseAPI() task id of vendorregistrationprocess :"
					 * +task.getEventId()); }
					 */
					tasks.add(task);

					// if (!ServicesUtil.isEmpty(parseTask.getAttributes()) &&
					// parseTask.getAttributes().size() > 0) {
					// processDetail = new ProcessDetail();
					// processDetail.setProcessId(task.getProcessId());
					// for (Attribute attribute : parseTask.getAttributes()) {
					// setProcessDetail(processDetail, attribute);
					// }
					// }

					UserDetailsDto ownerDetails = new UserDetailsDto();
					if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
						owner = new TaskOwnersDo();
						owner.setTaskOwnersDoPK(new TaskOwnersDoPK(task.getEventId(), task.getCurrentProcessor()));
						owner.setIsProcessed(true);
						if (!ServicesUtil.isEmpty(owner) && !ServicesUtil.isEmpty(owner.getTaskOwnersDoPK()))
							ownerDetails = getUserDetails(
									new UserDetailsDto(owner.getTaskOwnersDoPK().getTaskOwner(), null));
						if (!ServicesUtil.isEmpty(ownerDetails)) {
							owner.setOwnerEmail(ownerDetails.getEmailId() != null ? ownerDetails.getEmailId() : null);
							owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName() != null
									? ownerDetails.getDisplayName() : ownerDetails.getUserId());
						}
						// for Notification
						/*
						 * if (notificationFlag) {
						 * users.add(task.getCurrentProcessor()); }
						 */
						owners.add(owner);
					}

					List<String> recepients = parseTask.getRecipientUsers();
					List<String> recepientGroups = parseTask.getRecipientGroups();
					for (String group : recepientGroups) {
						recepients.addAll(getRecipientUserOfGroup((String) group));
					}

					for (String recepient : recepients) {
						if (!recepient.equalsIgnoreCase(task.getCurrentProcessor())) {
							owner = new TaskOwnersDo();
							owner.setTaskOwnersDoPK(new TaskOwnersDoPK(task.getEventId(), recepient));
							owner.setIsProcessed(false);
							if (!ServicesUtil.isEmpty(owner) && !ServicesUtil.isEmpty(owner.getTaskOwnersDoPK()))
								ownerDetails = getUserDetails(
										new UserDetailsDto(owner.getTaskOwnersDoPK().getTaskOwner(), null));
							if (!ServicesUtil.isEmpty(ownerDetails)) {
								owner.setOwnerEmail(
										ownerDetails.getEmailId() != null ? ownerDetails.getEmailId() : null);
								owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName() != null
										? ownerDetails.getDisplayName() : ownerDetails.getUserId());
							}

							// for Notification
							/*
							 * if (notificationFlag) { users.add(recepient); }
							 */
							owners.add(owner);
						}
					}
				}
			}
		}

		if (!ServicesUtil.isEmpty(processList) && processList.size() > 0) {
			System.err.println("[WBP-Dev][parseAPI]Parse processList size : " + processList.size());

			for (oneapp.incture.workbox.demo.adapter_base.mapping.Process parseProcess : processList) {
				if (!ServicesUtil.isEmpty(parseProcess)) {

					process = new ProcessEventsDo();

					process.setProcessId(parseProcess.getId());

					process.setName(parseProcess.getDefinitionId());
					process.setSubject(parseProcess.getSubject());
					process.setStatus(parseProcess.getStatus());

					process.setRequestId(parseProcess.getBusinessKey());
					process.setStartedAt(ServicesUtil.isEmpty(parseProcess.getStartedAt()) ? null
							: ServicesUtil.convertAdminFromStringToDate(parseProcess.getStartedAt()));
					process.setCompletedAt(ServicesUtil.isEmpty(parseProcess.getCompletedAt()) ? null
							: ServicesUtil.convertAdminFromStringToDate(parseProcess.getCompletedAt()));
					process.setStartedBy(parseProcess.getStartedBy());
					UserDetailsDto uDetails = getUserDetails(new UserDetailsDto(process.getStartedBy(), null));
					if (!ServicesUtil.isEmpty(uDetails)) {
						process.setStartedByDisplayName(uDetails.getDisplayName());
					}

					processes.add(process);

				}
			}
		}

		// Send notification to recipient users
		/*
		 * if (notificationFlag) { notificationDto.setUsers(users);
		 * notificationService.notifyUser(notificationDto); }
		 */

		System.err.println("[WBP-Dev][parseAPI]Parse End : " + System.currentTimeMillis());

		emptyGroupsMapping();
		emptyUsersMap();
		// notificationFlag = false;

		// System.err.println("[WBP-Dev]AdminParse.parseAPI() parsed tasks list
		// :
		// "+tasks);
		// System.err.println("[WBP-Dev]AdminParse.parseAPI() parsed process
		// list :
		// "+processes);
		System.err.println("[WBP-Dev]AdminParse.parseAPI() parsed owners list : " + owners);

		return new AdminParseResponseObject(tasks, processes, owners, null, 0, customAttrValueList);
	}

	@SuppressWarnings("unchecked")
	public List<String> getExistingTaskIds() {
		List<String> taskIdsList = new ArrayList<String>();
		Session session = null;
		String query = "";
		try {
			query = "SELECT DISTINCT TASK_ID FROM CUSTOM_ATTR_VALUES";
			session = sessionFactory.openSession();
			if (session != null) {
				List<Object> objList = session.createSQLQuery(query).list();

				for (Object obj : objList) {
					taskIdsList.add(obj.toString());
				}
			}

		} catch (Exception e) {
			System.err.println("AdminParse.getExistingTaskIds() exception : " + e.getMessage());
		}
		if (session != null)
			session.close();
		return taskIdsList;
	}

	@SuppressWarnings("rawtypes")
	private List getInstances(String requestUrl, Class clazz) {

		int taskInstancesCount = -1;
		Object responseObject = null;
		HttpResponse httpResponse = null;
		RestResponse restResponse = null;
		List<Task> taskArray = null;
		List<Task> taskArraySkip = null;

		List<oneapp.incture.workbox.demo.adapter_base.mapping.Process> processArray = null;
		List<oneapp.incture.workbox.demo.adapter_base.mapping.Process> processArraySkip = null;

		int taskArraySize = 0;
		int processArraySize = 0;

		String[] token = getToken();

		if (!ServicesUtil.isEmpty(requestUrl)) {
			requestUrl += "&$top=1000&$inlinecount=allpages";

			System.err.println("[WBP-Dev][parseSCP]  AdminParse.getInstances() request url" + requestUrl);

			/*
			 * restResponse = RestUtil.invokeRestService(requestUrl,
			 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
			 * true, null, null, null, null, null, null);
			 */
//			restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
//					"application/json", false, null, null, null, token[1], token[1]+" "+token[0]);
			
			restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					"application/json", false, null, null, null, token[0], token[1]);
		}

		responseObject = restResponse.getResponseObject();
		// System.err.println("[WBP-Dev]Response Object : " + responseObject);
		httpResponse = restResponse.getHttpResponse();
		if (!ServicesUtil.isEmpty(httpResponse)) {
			for (Header header : httpResponse.getAllHeaders()) {
				if (header.getName().equalsIgnoreCase("X-Total-Count")) {
					taskInstancesCount = Integer.parseInt(header.getValue());
				}
			}
		} else if (!ServicesUtil.isEmpty(restResponse.getUrlConnection())
				&& restResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
			taskInstancesCount = Integer.parseInt(restResponse.getUrlConnection().getHeaderField("X-Total-Count"));
		}

		System.err.println("[WBP-Dev][parseSCP] AdminParse.getInstances() X-Total-Count" + taskInstancesCount);

		if (clazz.equals(Task.class)) {
			// System.err.println("[WBP-Dev]AdminParse.getInstances() tasks :" +
			// responseObject.toString());
			taskArray = ServicesUtil.isEmpty(responseObject) ? null
					: JsonIterator.deserialize(responseObject.toString(), new TypeLiteral<List<Task>>() {
					});
			// System.err.println("[WBP-Dev]AdminParse.getInstances() taskArray
			// :" +
			// taskArray);
			if (taskArray != null && !ServicesUtil.isEmpty(taskArray))
				taskArraySize = taskArray.size();
			if (taskInstancesCount > taskArraySize) {
				int skip = 1000;
				for (int k = 1; k <= taskInstancesCount / skip; k++) {
					requestUrl += "&$skip=" + (skip * k);
					System.err
							.println("[WBP-Dev][parseSCP]  [WBP-Dev]  AdminParse.getInstances() skip url" + requestUrl);

					/*
					 * restResponse = RestUtil.invokeRestService(requestUrl,
					 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					 * "application/json", true, null, null, null, null, null,
					 * null);
					 */
					restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
							"application/json", true, null, null, null, token[0], token[1]);
					responseObject = restResponse.getResponseObject();
					//System.err.println("test:"+new Gson().toJson(responseObject));
					taskArraySkip = ServicesUtil.isEmpty(responseObject) ? null
							: JsonIterator.deserialize(responseObject.toString(), new TypeLiteral<List<Task>>() {
							});
					if (taskArray != null && !ServicesUtil.isEmpty(taskArray) && taskArraySize > 0) {
						taskArray.addAll(taskArraySkip);
					} else {
						taskArray = taskArraySkip;
					}
				}
			}
			return taskArray;
		} else if (clazz.equals(oneapp.incture.workbox.demo.adapter_base.mapping.Process.class)) {

			// System.err.println("[WBP-Dev]AdminParse.getInstances() process :"
			// +
			// responseObject.toString());
			processArray = ServicesUtil.isEmpty(responseObject) ? null
					: JsonIterator.deserialize(responseObject.toString(),
							new TypeLiteral<List<oneapp.incture.workbox.demo.adapter_base.mapping.Process>>() {
							});
			// System.err.println("[WBP-Dev]AdminParse.getInstances()
			// processArray :" +
			// processArray);
			if (processArray != null && !ServicesUtil.isEmpty(processArray))
				processArraySize = processArray.size();
			if (taskInstancesCount > processArraySize) {
				int skip = 1000;
				for (int k = 1; k < taskInstancesCount / skip; k++) {
					requestUrl += "&$skip=" + (skip * k);
					/*
					 * restResponse = RestUtil.invokeRestService(requestUrl,
					 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					 * "application/json", true, null, null, null, null, null,
					 * null);
					 */
					restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
							"application/json", true, null, null, null, token[0], token[1]);

					responseObject = restResponse.getResponseObject();
					System.err.println("test:"+new Gson().toJson(responseObject));
					processArraySkip = ServicesUtil.isEmpty(responseObject) ? null
							: JsonIterator.deserialize(responseObject.toString(),
									new TypeLiteral<List<oneapp.incture.workbox.demo.adapter_base.mapping.Process>>() {
									});
					if (processArray != null && !ServicesUtil.isEmpty(processArray) && processArraySize > 0) {
						processArray.addAll(processArraySkip);
					} else {
						processArray = processArraySkip;
					}
				}
			}
			return processArray;
		}
		return null;

	}

	@SuppressWarnings("rawtypes")
	private List getWorkflowInstances(String requestUrl, Class clazz, String lastSynced) {

		int taskInstancesCount = -1;
		Object responseObject = null;
		HttpResponse httpResponse = null;
		RestResponse restResponse = null;
		List<Task> taskArray = null;
		List<Task> taskArraySkip = null;

		List<oneapp.incture.workbox.demo.adapter_base.mapping.Process> processArray = null;
		List<oneapp.incture.workbox.demo.adapter_base.mapping.Process> processArraySkip = null;

		int taskArraySize = 0;
		int processArraySize = 0;

		String[] token = getToken();

		if (!ServicesUtil.isEmpty(requestUrl)) {

			if (!ServicesUtil.isEmpty(lastSynced)) {
				requestUrl += "&lastChangedFrom=" + lastSynced;
			}
			requestUrl += "&$top=1000&$inlinecount=allpages";

			System.err.println("  [WBP-Dev]  AdminParse.getInstances() request url" + requestUrl);

			/*
			 * restResponse = RestUtil.invokeRestService(requestUrl,
			 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
			 * true, null, null, null, null, null, null);
			 */
			restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					"application/json", true, null, null, null, token[0], token[1]);
		}

		responseObject = restResponse.getResponseObject();
		// System.err.println("[WBP-Dev]Response Object : " + responseObject);
		httpResponse = restResponse.getHttpResponse();
		if (!ServicesUtil.isEmpty(httpResponse)) {
			for (Header header : httpResponse.getAllHeaders()) {
				if (header.getName().equalsIgnoreCase("X-Total-Count")) {
					taskInstancesCount = Integer.parseInt(header.getValue());
				}
			}
		} else if (!ServicesUtil.isEmpty(restResponse.getUrlConnection())
				&& restResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
			taskInstancesCount = Integer.parseInt(restResponse.getUrlConnection().getHeaderField("X-Total-Count"));
		}

		System.err.println("AdminParse.getInstances() X-Total-Count" + taskInstancesCount);

		if (clazz.equals(Task.class)) {
			// System.err.println("[WBP-Dev]AdminParse.getInstances() tasks :" +
			// responseObject.toString());
			taskArray = ServicesUtil.isEmpty(responseObject) ? null
					: JsonIterator.deserialize(responseObject.toString(), new TypeLiteral<List<Task>>() {
					});
			// System.err.println("[WBP-Dev]AdminParse.getInstances() taskArray
			// :" +
			// taskArray);
			if (taskArray != null && !ServicesUtil.isEmpty(taskArray))
				taskArraySize = taskArray.size();
			if (taskArray != null && taskInstancesCount > taskArraySize) {
				int skip = 1000;
				for (int k = 1; k <= taskInstancesCount / skip; k++) {
					requestUrl += "&$skip=" + (skip * k);
					System.err.println("  [WBP-Dev]  AdminParse.getInstances() skip url" + requestUrl);

					/*
					 * restResponse = RestUtil.invokeRestService(requestUrl,
					 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					 * "application/json", true, null, null, null, null, null,
					 * null);
					 */
					restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
							"application/json", true, null, null, null, token[0], token[1]);
					responseObject = restResponse.getResponseObject();
					taskArraySkip = ServicesUtil.isEmpty(responseObject) ? null
							: JsonIterator.deserialize(responseObject.toString(), new TypeLiteral<List<Task>>() {
							});
					if (taskArray != null && !ServicesUtil.isEmpty(taskArray) && taskArraySize > 0) {
						taskArray.addAll(taskArraySkip);
					} else {
						taskArray = taskArraySkip;
					}
				}
			}
			return taskArray;
		} else if (clazz.equals(oneapp.incture.workbox.demo.adapter_base.mapping.Process.class)) {

			// System.err.println("[WBP-Dev]AdminParse.getInstances() process :"
			// +
			// responseObject.toString());
			processArray = ServicesUtil.isEmpty(responseObject) ? null
					: JsonIterator.deserialize(responseObject.toString(),
							new TypeLiteral<List<oneapp.incture.workbox.demo.adapter_base.mapping.Process>>() {
							});
			// System.err.println("[WBP-Dev]AdminParse.getInstances()
			// processArray :" +
			// processArray);
			if (!ServicesUtil.isEmpty(processArray))
				processArraySize = processArray.size();
			if (taskInstancesCount > processArraySize) {
				int skip = 1000;
				for (int k = 1; k < taskInstancesCount / skip; k++) {
					requestUrl += "&$skip=" + (skip * k);
					/*
					 * restResponse = RestUtil.invokeRestService(requestUrl,
					 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					 * "application/json", true, null, null, null, null, null,
					 * null);
					 */
					restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
							"application/json", true, null, null, null, token[0], token[1]);

					responseObject = restResponse.getResponseObject();
					processArraySkip = ServicesUtil.isEmpty(responseObject) ? null
							: JsonIterator.deserialize(responseObject.toString(),
									new TypeLiteral<List<oneapp.incture.workbox.demo.adapter_base.mapping.Process>>() {
									});
					if (!ServicesUtil.isEmpty(processArray) && processArraySize > 0) {
						processArray.addAll(processArraySkip);
					} else {
						processArray = processArraySkip;
					}
				}
			}
			return processArray;
		}
		return null;

	}

	private UserDetailsDto getUserDetails(UserDetailsDto userDetail) {
		UserDetailsDto user = null;
		if (ServicesUtil.isEmpty(usersMap)) {
			refreshUserDetailsMap();
			user = getUserDetails(userDetail);
		} else {
			user = usersMap.get(userDetail.getUserId());
		}
		return user;
	}

	private Map<String, UserDetailsDto> refreshUserDetailsMap() {
		Map<String, UserDetailsDto> userDetailsMap = new HashMap<String, UserDetailsDto>();
		UserDetailsDto userDetails = null;
		List<Object[]> resultList = this.userDetails.getUserDetailResponse();
		if (!ServicesUtil.isEmpty(resultList) && resultList.size() > 0) {
			for (Object[] object : resultList) {
				userDetails = new UserDetailsDto();
				userDetails.setUserId(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
				userDetails.setEmailId(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
				userDetails.setDisplayName(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);
				userDetailsMap.put(userDetails.getUserId(), userDetails);
			}
		}
		System.err.println(userDetailsMap);
		usersMap = userDetailsMap;
		return userDetailsMap;
	}

	private void emptyGroupsMapping() {
		groupMappings = null;
	}

	private void emptyUsersMap() {
		usersMap = null;
	}

	public Object getContextDetail(String taskId) {
		HttpResponse httpResponse = null;
		Object responseObject = null;
		RestResponse restResponse = null;
		String requestUrl = "";
		String[] token = getToken();
		try{
		requestUrl = getProperty.getValue("REQUEST_URL_INST")
		+ "task-instances/"+taskId+"/context";
		
		restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
				"application/json", false, null, null, null, token[0], token[1]);
		System.err.println("restResponse"+restResponse);
		responseObject = restResponse.getResponseObject();
		
		}catch (Exception e) {
			System.err.println("getContextDetail error"+e);
			
		}
		
		return responseObject;
	}

//	public static void main(String[] args) {
//
//		System.out.println("AdminParse.main()" + ServicesUtil.getUtcTimeWithDelay(8000));
//
//	}

}
