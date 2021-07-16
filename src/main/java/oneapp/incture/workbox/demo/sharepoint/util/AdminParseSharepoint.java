package oneapp.incture.workbox.demo.sharepoint.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dao.UserDetailsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserDetailsDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDoPK;
import oneapp.incture.workbox.demo.adapter_base.mapping.AdminParseResponseObject;
import oneapp.incture.workbox.demo.adapter_base.mapping.Task;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Component
public class AdminParseSharepoint {

	@SuppressWarnings("unused")
	private Map<String, String> groupMappings;

	@Autowired
	UserDetailsDao userDetails;

	private Map<String, UserDetailsDto> usersMap;

	@Autowired
	CustomAttributeDao customDao;

	@Autowired
	TaskEventsDao taskEventsDao;

	@Autowired
	TaskOwnersDao taskOwnersDao;

	@Autowired
	ProcessConfigDao processConfigDao;

	@Autowired
	ProcessEventsDao processEventsDao;

	int taskCount = 0;
	int processCount = 0;
	private int callCount = 0;

//	public static void main(String[] args) throws Exception {
//
//		AdminParseSharepoint adminParse = new AdminParseSharepoint();
//		System.err.println(adminParse.parseDetail());
//
//	}

	public AdminParseResponse parseDetail()  {
		long start = System.currentTimeMillis();
		long startProcess = System.currentTimeMillis();

		Map<String, List<String>> usersListMap = null;

		System.err.println("[WBP-Dev][WORKBOX- Sharepoint][getAllUserDetails]" + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();

		List<TaskEventsDto> tasks = null;
		List<ProcessEventsDto> processes = null;
		List<TaskOwnersDto> owners = null;
		List<CustomAttributeValue> customAttributeValues = new ArrayList<>();
		Map<String, String> userMapping = null;
		JSONObject processObject = null;

		ProcessEventsDto process = null;
		JSONArray processList = null;

		try {

			Object processResponse = fetchInstances(
					SharePointConstant.REQUEST_URL_SHAREPOINT + SharePointConstant.SHAREPOINT_JSON_Format);

			//System.err.println("FetchInstance object : " + new Gson().toJson(processResponse) );
			System.err.println("[WBP-Dev]\n\n[WORKBOX- Sharepoint][fetchInstances][object]" + processResponse.toString()
			+ "[time taken]" + +(System.currentTimeMillis() - start));

			if (!ServicesUtil.isEmpty(processResponse)) {
				if (processResponse instanceof JSONObject) {
					processList = ((JSONObject) processResponse).getJSONArray("value");
					System.err.println("[WBP-Dev]\n\n[WORKBOX- Sharepoint]2nd part" + processList.toString()
					+ "[time taken]" + +(System.currentTimeMillis() - start));

				}
				tasks = new ArrayList<>();
				processes = new ArrayList<>();
				owners = new ArrayList<TaskOwnersDto>();
				List<String> processIdListDB = null;
				List<TaskOwnersDo> taskOwnersDB = null;
				Map<String, TaskEventsDo> tasksDB = null;
				try {
					userMapping = userDetails.getAllUserMaping();
					usersListMap = userDetails.getAllSharepointUserDetails();
					processIdListDB = new ArrayList<>();
					taskOwnersDB = taskOwnersDao.getAllTaskOwnersDo();
					tasksDB = taskEventsDao.getAllOpenTasksDo();

				} catch (Exception e) {
					System.err.println("[WBP-Dev]error fetching Process Ids from DB" + e);
				}

				startProcess = System.currentTimeMillis();

//				System.err.println(
//						"[WBP-Dev][WORKBOX- Sharepoint][fetchInstances][object] process list : " + processList);
				if (!ServicesUtil.isEmpty(processList)) {

					for (Object object : processList) {
						start = System.currentTimeMillis();
						processObject = (JSONObject) object;
//						System.err.println(
//								"[WORKBOX- Sharepoint][fetchInstances][object] process json object : " + processObject);
						JSONObject listObj = processObject.getJSONObject("list");
						String template = listObj.optString("template");

						String processId = processObject.optString("id");
						String processName = processObject.optString("displayName");
						System.err.println(processName);
						if (processIdListDB != null) {
							System.err.println("[WBP-Dev][WORKBOX- Sharepoint][processid] & List" + processId + ","
									+ processIdListDB.toString() + "[processIdListDB.contains(processId)]"
									+ processIdListDB.contains(processId));
						}
						if ("tasksWithTimelineAndHierarchy".equalsIgnoreCase(template)
								&& SharePointConstant.PROCESS_NAME_DOC_APPROVAL.equals(processName)) {
							if (processIdListDB == null || ServicesUtil.isEmpty(processIdListDB)
									|| !processIdListDB.contains(processId)) {

								process = new ProcessEventsDto();
								process.setProcessId(processId);
								process.setName(processName);
								process.setSubject(processObject.optString("description")); // Descriptoin
								process.setStatus("RUNNING");
								process.setRequestId(
										processObject.optString("eTag").replaceAll("\"", "").split(",")[0]); // etag
								process.setStartedAt(
										ServicesUtil.isEmpty(processObject.optString("createdDateTime")) ? null
												: ServicesUtil.convertAdminFromStringToDate(
														processObject.optString("createdDateTime")));
								process.setCompletedAt(ServicesUtil.isEmpty("") ? null
										: ServicesUtil.convertAdminFromStringToDate(
												processObject.optString("lastModifiedDateTime"))); // Empty
								JSONObject createdBy = processObject.optJSONObject("createdBy"); //
								JSONObject user = createdBy.getJSONObject("user");
								process.setStartedBy(user.optString("displayName"));

								System.err.println(process);
								processes.add(process);
							}
							System.err.println("[WBP-Dev][WORKBOX- Sharepoint][process][" + (processCount++) + "]"
									+ (System.currentTimeMillis() - start));

							TaskAndOwnerDto dto = null;
							if (process != null)
								dto = taskGenerator(process.getProcessId(), process.getName(), usersListMap, userMapping,
										tasksDB);
								String startedBy = dto.getTasks().get(0).getCreatedBy();
								process.setStartedByDisplayName(usersListMap.get(startedBy).get(0) + " " + usersListMap.get(startedBy).get(1));;

							if (dto != null) {
								tasks.addAll(dto.getTasks());
								owners.addAll(dto.getOwners());
								customAttributeValues.addAll(dto.getCustomAttributeValues());
							}
						}
					}
					System.err.println("[WBP-Dev][WORKBOX- Sharepoint][Parsing All process (processList)]"
							+ (System.currentTimeMillis() - startProcess));

				}
				System.err.println("[WBP-Dev][WORKBOX- Sharepoint][Count][ProcessCount]" + processes.size()
				+ " [taskCount]" + tasks.size() + " [taskOwner]" + owners.size() + "[Custom attributes]"
				+ customAttributeValues.size());

			}

			else {
				System.err.println("[WBP-Dev]AdminParseSharepoint.parseDetail() NO data found : " + processResponse);
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev]AdminParseSharepoint.parseDetail() error " + e);

		}

		System.err.println("[WBP-Dev]AdminParseSharepoint.parseDetail() all process instances  : " + processes);
		System.err.println("[WBP-Dev]AdminParseSharepoint.parseDetail() all Task instances  : " + tasks);
		System.err.println("[WBP-Dev]AdminParseSharepoint.parseDetail() all onwer instances  : " + owners);

		return new AdminParseResponse(tasks, processes, owners, null, 0, customAttributeValues);
	}

	private TaskAndOwnerDto taskGenerator(String gettId, String processName, Map<String, List<String>> usersMap,
			Map<String, String> userMapping, Map<String, TaskEventsDo> taskEventDB)
					throws Exception {

		TaskAndOwnerDto dto = new TaskAndOwnerDto();
		JSONObject taskObject;
		JSONArray taskList = null;
		String taskDetails = null;
		TaskEventsDto task = null;
		TaskOwnersDto owner = null;
		CustomAttributeValue customAttributes = null;
		List<TaskEventsDto> taskLists = new ArrayList<>();
		List<TaskOwnersDto> owners = new ArrayList<>();
		List<CustomAttributeValue> customAttributeValues = new ArrayList<>();
		Long start = System.currentTimeMillis();
		Long startTask = System.currentTimeMillis();
		Object detailresponseObject = null;
		RestResponse detailresponse = null;

		try {
			
			Object TaskResponse = fetchInstances(
					SharePointConstant.REQUEST_URL_SHAREPOINT + "/" + gettId + SharePointConstant.SHAREPOINT_ITEMS);
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][fetchInstances : TASK]"
					+ (System.currentTimeMillis() - start) + " [TaskResponse]" + TaskResponse.toString());

			if (!ServicesUtil.isEmpty(TaskResponse)) {
				if (TaskResponse instanceof JSONObject) {
					taskList = ((JSONObject) TaskResponse).getJSONArray("value");
				}

				for (Object object : taskList) {
					taskObject = (JSONObject) object;
					task = new TaskEventsDto();
					// set EventId
					task.setEventId(taskObject.optString("eTag").replaceAll("\"", "").split(",")[0]);
					// set status
					JSONObject fields = taskObject.getJSONObject("fields");
					String taskStatus = fields.optString("Status");
					if ("Not Started".equalsIgnoreCase(taskStatus) || "Waiting for someone else".equalsIgnoreCase(taskStatus)) {
						task.setStatus("READY");
					} else if ("In Progress".equalsIgnoreCase(taskStatus)) {
						task.setStatus("INPROGRESS");// Fields->Status
					} else if ("Completed".equalsIgnoreCase(taskStatus) || "Approved".equalsIgnoreCase(taskStatus)) {
						task.setStatus("COMPLETED");
					}else {
						task.setStatus(taskStatus);
					}
					// set processor
					String userId = userMapping.get(((JSONObject)fields.optJSONArray("AssignedTo").get(0)).optString("LookupId"));
					task.setCurrentProcessor(!ServicesUtil.isEmpty(userId) ? userId : "P000057");

					// new Task
					if (ServicesUtil.isEmpty(taskEventDB) || !taskEventDB.containsKey(taskObject.optString("id"))) {

						customAttributes = new CustomAttributeValue();

						task.setOrigin("Sharepoint");
						task.setEventId(taskObject.optString("eTag").replaceAll("\"", "").split(",")[0]);
						task.setProcessId(gettId); // eTag
						task.setProcessName(processName);

						task.setCreatedAt(ServicesUtil.isEmpty(fields.optString("Created")) ? null
								: ServicesUtil.convertAdminFromStringToDate(fields.optString("Created"))); // createdDateTime
						task.setDescription(fields.optString("Title")); // Fields->Title
						task.setCreatedBy(userMapping.get(fields.optString("AuthorLookupId")));
						

						if (!ServicesUtil.isEmpty(task.getCurrentProcessor())
								&& !SharePointConstant.NULL_STRING.equalsIgnoreCase(task.getCurrentProcessor())
								&& usersMap.containsKey(task.getCurrentProcessor())) {
							task.setCurrentProcessorDisplayName(usersMap.get(task.getCurrentProcessor()).get(0) + " " + usersMap.get(task.getCurrentProcessor()).get(1));
						}
						task.setSubject(fields.optString("Title")); // Fields ->

						task.setName("Document approval process");

						if (fields.optString("Priority").contains("Normal")) {
							task.setPriority("MEDIUM");
						} else {
							task.setPriority(fields.optString("Priority"));
						}
						task.setCompletedAt(ServicesUtil.isEmpty(fields.optString("completedAt")) ? null
								: ServicesUtil.convertFromStringToDate(fields.optString("completedAt"))); // completedAt

						if (ServicesUtil.isEmpty(fields.optString("DueDate"))) {
							task.setCompletionDeadLine(
									new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
							task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
						} else {
							task.setCompletionDeadLine(
									ServicesUtil.convertAdminFromStringToDate(fields.optString("DueDate")));
							task.setSlaDueDate(ServicesUtil.convertAdminFromStringToDate(fields.optString("DueDate"))); // due
							// date
						}

						owner = new TaskOwnersDto();
						owner.setEventId(task.getEventId());
						owner.setTaskOwner(task.getCurrentProcessor());
						owner.setIsProcessed(true);

						if (usersMap.containsKey(owner.getTaskOwner())) {

							owner.setOwnerEmail(usersMap.get(owner.getTaskOwner()).get(0));
							owner.setTaskOwnerDisplayName(usersMap.get(owner.getTaskOwner()).get(1));

						}

						customAttributes.setTaskId(task.getEventId());
						customAttributes.setProcessName(processName);
						customAttributes.setKey("URL");
						taskDetails=fields.getString("RelatedItems");
						JSONArray jsonArray = new JSONArray(taskDetails);
						Integer itemId=((JSONObject) jsonArray.get(0)).getInt("ItemId");
						
						
						try {
							
							String requestURL=SharePointConstant.REQUEST_URL_SHAREPOINT + "/" + SharePointConstant.SHAREPOINT_DIRECTORY + "/items/"+itemId;
							if (!ServicesUtil.isEmpty(requestURL)) {
								String token = TokenGenerator.getAccessTokenFromUserCredentials();
								detailresponse = RestUtilSharePoint.callRestService(requestURL, SharePointConstant.SAML_HEADER_SHAREPOINT,
										null, "GET", "application/json", false, null, null, null, token, "Bearer");
							}
							if (detailresponse != null)
								detailresponseObject = detailresponse.getResponseObject();
						} catch (Exception e) {
							// TODO: handle exception
						}
						System.err.println("[WBP-Dev][WORKBOX- Sharepoint][fetchInstances : TASK]"
								+ (System.currentTimeMillis() - start) + " [TaskResponse]" + detailresponseObject.toString());
						if (TaskResponse instanceof JSONObject) {
							taskDetails = ((JSONObject) detailresponseObject).getString("webUrl");
							
						}	
						task.setDescription(fields.optString("Title")+" - "+((JSONObject) detailresponseObject).getJSONObject("fields").getString("LinkFilename")); // Fields->Title
						//JSONObject WorkflowLink = fields.optJSONObject("WorkflowLink") != null ? fields.getJSONObject("WorkflowLink") : new JSONObject();
						customAttributes.setAttributeValue(taskDetails);

						owners.add(owner);
						taskLists.add(task);
						customAttributeValues.add(customAttributes);
					} else {

						// check status and processor

						if (!ServicesUtil.isEmpty(taskEventDB)) {
							String status = taskEventDB.get(task.getEventId()).getStatus();
							String currentProcessor = taskEventDB.get(task.getEventId()).getCurrentProcessor();

							if (status.equals(task.getStatus())) {
								if (!currentProcessor.equals(task.getCurrentProcessor())) {
									// perform change for only current processor
									owner = new TaskOwnersDto();
									owner.setEventId(task.getEventId());
									owner.setTaskOwner(task.getCurrentProcessor());
									owner.setIsProcessed(true);

									if (usersMap.containsKey(owner.getTaskOwner())) {

										owner.setOwnerEmail(usersMap.get(owner.getTaskOwner()).get(0));
										owner.setTaskOwnerDisplayName(usersMap.get(owner.getTaskOwner()).get(1));

									}
									owners.add(owner);

								}

							} else {
								// status changed but not current processor
								if (currentProcessor.equals(task.getCurrentProcessor())) {

									task.setOrigin("Sharepoint");
									task.setProcessId(gettId); // eTag
									task.setProcessName(processName);

									task.setCreatedAt(ServicesUtil.isEmpty(fields.optString("Created")) ? null
											: ServicesUtil.convertAdminFromStringToDate(fields.optString("Created"))); // createdDateTime
									task.setDescription(fields.optString("Title")); // Fields->Title

									System.err.println(
											"[WBP-Dev][WORKBOX- Sharepoint]" + fields.optString("AssignedToLookupId")
											+ "[userId]" + task.getCurrentProcessor());

									if (!ServicesUtil.isEmpty(task.getCurrentProcessor())
											&& !SharePointConstant.NULL_STRING
											.equalsIgnoreCase(task.getCurrentProcessor())
											&& usersMap.containsKey(task.getCurrentProcessor())) {

											task.setCurrentProcessorDisplayName(
													usersMap.get(task.getCurrentProcessor()).get(1));

									}
									task.setSubject(fields.optString("Title")); // Fields
									// ->

									task.setName("Document approval process");

									if (fields.optString("Priority").contains("Normal")) {
										task.setPriority("MEDIUM");
									} else {
										task.setPriority(fields.optString("Priority"));
									}
									task.setCompletedAt(ServicesUtil.isEmpty(fields.optString("completedAt")) ? null
											: ServicesUtil.convertFromStringToDate(fields.optString("completedAt"))); // completedAt

									if (ServicesUtil.isEmpty(fields.optString("DueDate"))) {
										task.setCompletionDeadLine(
												new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
										task.setSlaDueDate(
												new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
									} else {
										task.setCompletionDeadLine(
												ServicesUtil.convertAdminFromStringToDate(fields.optString("DueDate")));
										task.setSlaDueDate(
												ServicesUtil.convertAdminFromStringToDate(fields.optString("DueDate"))); // due
										// date
									}

									taskLists.add(task);

								}
								// if status and current processor both changed
								else {
									task.setOrigin("Sharepoint");
									task.setProcessId(gettId); // eTag
									task.setProcessName(processName);

									task.setCreatedAt(ServicesUtil.isEmpty(fields.optString("Created")) ? null
											: ServicesUtil.convertAdminFromStringToDate(fields.optString("Created"))); // createdDateTime
									task.setDescription(fields.optString("Title")); // Fields->Title

									System.err.println(
											"[WBP-Dev][WORKBOX- Sharepoint]" + fields.optString("AssignedToLookupId")
											+ "[userId]" + task.getCurrentProcessor());

									if (!ServicesUtil.isEmpty(task.getCurrentProcessor())
											&& !SharePointConstant.NULL_STRING
											.equalsIgnoreCase(task.getCurrentProcessor())
											&& usersMap.containsKey(task.getCurrentProcessor())) {
											task.setCurrentProcessorDisplayName(
													usersMap.get(task.getCurrentProcessor()).get(1));

									}
									task.setSubject(fields.optString("Title")); // Fields
									// ->

									task.setName("Document approval process");

									if (fields.optString("Priority").contains("Normal")) {
										task.setPriority("MEDIUM");
									} else {
										task.setPriority(fields.optString("Priority"));
									}
									task.setCompletedAt(ServicesUtil.isEmpty(fields.optString("completedAt")) ? null
											: ServicesUtil.convertFromStringToDate(fields.optString("completedAt"))); // completedAt

									if (ServicesUtil.isEmpty(fields.optString("DueDate"))) {
										task.setCompletionDeadLine(
												new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
										task.setSlaDueDate(
												new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
									} else {
										task.setCompletionDeadLine(
												ServicesUtil.convertAdminFromStringToDate(fields.optString("DueDate")));
										task.setSlaDueDate(
												ServicesUtil.convertAdminFromStringToDate(fields.optString("DueDate"))); // due
										// date
									}

									owner = new TaskOwnersDto();
									owner.setEventId(task.getEventId());
									owner.setTaskOwner(task.getCurrentProcessor());
									owner.setIsProcessed(true);

									if (usersMap.containsKey(owner.getTaskOwner())) {

										owner.setOwnerEmail(usersMap.get(owner.getTaskOwner()).get(0));
										owner.setTaskOwnerDisplayName(usersMap.get(owner.getTaskOwner()).get(1));

									}
									owners.add(owner);

								}
							}

						}
					}
				}

			}
			System.err
			.println("[WBP-Dev][WORKBOX- Sharepoint][ParseAllTask]" + (System.currentTimeMillis() - startTask));

		} catch (Exception e) {
			System.err.println("[WBP-Dev]AdminParseSharepoint.taskGenerator() error " + e);

		}

		dto.setTasks(taskLists);
		dto.setOwners(owners);
		dto.setCustomAttributeValues(customAttributeValues);
		return dto;
	}

	@SuppressWarnings("unused")
	private static Object[] fetchTaskInstances(String requestUrl)  {
		Object[] ret = null;
		return ret;
	}

	private Object fetchInstances(String requestUrl)  {
		Object responseObject = null;

		try {
			System.err.println("[WBP-Dev]Service Call Counter : " + callCount);
			callCount++;

			RestResponse restResponse = null;
			
			String token = TokenGenerator.getAccessTokenFromUserCredentials();
			System.err.println(requestUrl);
			if (!ServicesUtil.isEmpty(requestUrl)) {
				requestUrl += "&$top=1000&$inlinecount=allpages";
				restResponse = RestUtilSharePoint.callRestService(requestUrl, SharePointConstant.SAML_HEADER_SHAREPOINT,
						null, "GET", "application/json", false, null, null, null, token, "Bearer");
			}

			System.err.println("[WBP-Dev]AdminParseSharepoint.fetchInstances()" + restResponse);
			if (restResponse != null)
				responseObject = restResponse.getResponseObject();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Sharepoint test][error]" + e.getLocalizedMessage());
		}
		return responseObject;
	}

	@SuppressWarnings("unused")
	private static JSONArray mergeJsonArray(JSONArray jsonArray, JSONArray jsonArraySkip) {
		List<Object> object = ServicesUtil.isEmpty(jsonArray) ? null : jsonArray.toList();
		List<Object> objectSkip = ServicesUtil.isEmpty(jsonArraySkip) ? null : jsonArraySkip.toList();
		if (ServicesUtil.isEmpty(object)) {
			if (ServicesUtil.isEmpty(objectSkip)) {
				return null;
			} else {
				return new JSONArray(objectSkip);
			}
		} else {
			if (object != null && !ServicesUtil.isEmpty(objectSkip)) {
				object.addAll(objectSkip);
			}
			return new JSONArray(object);
		}
	}

	private static List<String> getRecipientUserOfGroup(String groupName) {
		List<String> groupUser = new ArrayList<>();
		String requestUrl = "https://aiiha1kww.accounts.ondemand.com/service/scim/Users?filter=groups%20eq%20%22"
				+ groupName + "%22";
		Object responseObject = RestUtilSharePoint.callRestService(requestUrl, null, null, "GET",
				"application/scim+json", false, null, "T000006", "Workbox@123", null, null).getResponseObject();
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
		return null;
	}

	@SuppressWarnings("unused")
	private Map<String, String> fetchCustomData(String taskEventId) {
		Map<String, String> contextData = null;
		RestResponse restResponse = RestUtilSharePoint.callRestService(
				SharePointConstant.REQUEST_URL_INST + "task-instances/" + taskEventId + "/context", null, null,
				SharePointConstant.HTTP_METHOD_GET, SharePointConstant.APPLICATION_JSON, true, null, null, null, null,
				null);
		if (!ServicesUtil.isEmpty(restResponse) && !ServicesUtil.isEmpty(restResponse.getResponseObject())
				&& restResponse.getResponseObject().toString().startsWith("{")) {
				contextData = new HashMap<>();
				JSONObject context = (JSONObject) restResponse.getResponseObject();
				Iterator<String> keys = context.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					contextData.put(key, context.optString(key));
				}
		}
		return contextData;
	}

	@SuppressWarnings("unused")
	private Map<String, List<CustomAttributeTemplate>> getCustomTemplateMap() throws NoResultFault {
		List<ProcessConfigDto> pcEntry = processConfigDao.getAllProcessConfigEntry();
		List<CustomAttributeTemplate> customTemplates = null;
		Map<String, List<CustomAttributeTemplate>> customTemplateMap = null;
		if (!ServicesUtil.isEmpty(pcEntry) && !pcEntry.isEmpty()) {
			customTemplateMap = new HashMap<>();
			for (ProcessConfigDto pcDto : pcEntry) {
				customTemplates = customDao.getCustomAttributeTemplates(pcDto.getProcessName(), null, null);
				customTemplateMap.put(pcDto.getProcessName(), customTemplates);
			}
		}
		return customTemplateMap;
	}

	@SuppressWarnings("unchecked")
	public AdminParseResponseObject parseAPI() {

		TaskEventsDo task = null;

		TaskOwnersDo owner = null;

		ProcessEventsDo process = null;

		System.err.println("[WBP-Dev][parseAPI]Fetch Start : " + System.currentTimeMillis());

		List<Task> taskList = getInstances(
				SharePointConstant.REQUEST_URL_INST
				+ "task-instances?status=READY&status=RESERVED&status=CANCELED&status=COMPLETED&$expand=attributes",
				Task.class);
		List<oneapp.incture.workbox.demo.adapter_base.mapping.Process> processList = getInstances(
				SharePointConstant.REQUEST_URL_INST
				+ "workflow-instances?status=RUNNING&status=ERRONEOUS&status=CANCELED&status=COMPLETED",
				oneapp.incture.workbox.demo.adapter_base.mapping.Process.class);

		System.err.println("[WBP-Dev][parseAPI]Fetch End : " + System.currentTimeMillis());

		List<TaskEventsDo> tasks = new ArrayList<>();
		List<TaskOwnersDo> owners = new ArrayList<>();
		List<ProcessEventsDo> processes = new ArrayList<>();

		UserDetailsDto ownerDetails = null;

		System.err.println("[WBP-Dev][parseAPI]Parse Start : " + System.currentTimeMillis());

		if (!ServicesUtil.isEmpty(taskList) && !taskList.isEmpty()) {
			for (Task parseTask : taskList) {
				if (!ServicesUtil.isEmpty(parseTask)) {

					task = new TaskEventsDo();

					task.setEventId(parseTask.getId());
					task.setProcessId(parseTask.getWorkflowInstanceId());
					task.setProcessName(parseTask.getWorkflowDefinitionId());
					task.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(parseTask.getCreatedAt()));
					task.setDescription(parseTask.getDescription());
					task.setCurrentProcessor(parseTask.getProcessor());

					if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
						UserDetailsDto processorDetails = getUserDetails(new UserDetailsDto(task.getCurrentProcessor(), null));
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

					if ("leaveapprovalmanagement".equalsIgnoreCase(task.getProcessName()))
						task.setOrigin("BPM");
					else if ("travelandexpenseapproval".equalsIgnoreCase(task.getProcessName()))
						task.setOrigin("SCP");
					else if ("performanceappraisal".equalsIgnoreCase(task.getProcessName()))
						task.setOrigin("SuccessFactors");
					else if ("purchaseorderapproval".equalsIgnoreCase(task.getProcessName())
							|| "poapprovalprocess".equalsIgnoreCase(task.getProcessName()))
						task.setOrigin("ECC");
					else
						task.setOrigin("SCP");

					System.err.println(
							"Task Origin Hardcoded to: " + task.getOrigin() + ", task Type: " + task.getProcessName());
					tasks.add(task);

					if (!ServicesUtil.isEmpty(task.getCurrentProcessor())) {
						owner = new TaskOwnersDo();
						owner.setTaskOwnersDoPK(new TaskOwnersDoPK(task.getEventId(), task.getCurrentProcessor()));
						owner.setIsProcessed(true);
						if (!ServicesUtil.isEmpty(owner) && !ServicesUtil.isEmpty(owner.getTaskOwnersDoPK()))
							ownerDetails = getUserDetails(
									new UserDetailsDto(owner.getTaskOwnersDoPK().getTaskOwner(), null));
						if (!ServicesUtil.isEmpty(ownerDetails)) {
							owner.setOwnerEmail(ownerDetails.getEmailId());
							owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName());
						}
						owners.add(owner);
					}

					List<String> recepients = parseTask.getRecipientUsers();
					List<String> recepientGroups = parseTask.getRecipientGroups();
					for (String group : recepientGroups) {
						recepients.addAll(getRecipientUserOfGroup((String) group));
					}

					for (String recepient : recepients) {
						owner = new TaskOwnersDo();
						owner.setTaskOwnersDoPK(new TaskOwnersDoPK(task.getEventId(), recepient));
						owner.setIsProcessed(false);
						if (!ServicesUtil.isEmpty(owner) && !ServicesUtil.isEmpty(owner.getTaskOwnersDoPK()))
							ownerDetails = getUserDetails(
									new UserDetailsDto(owner.getTaskOwnersDoPK().getTaskOwner(), null));
						if (!ServicesUtil.isEmpty(ownerDetails)) {
							owner.setOwnerEmail(ownerDetails.getEmailId());
							owner.setTaskOwnerDisplayName(ownerDetails.getDisplayName());
						}
						owners.add(owner);
					}

				}
			}
		}

		if (!ServicesUtil.isEmpty(processList) && !processList.isEmpty()) {
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

		System.err.println("[WBP-Dev][parseAPI]Parse End : " + System.currentTimeMillis());

		emptyGroupsMapping();
		emptyUsersMap();

		return new AdminParseResponseObject(tasks, processes, owners, null, 0);
	}

	@SuppressWarnings({ "rawtypes", "unused" })
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

		/*
		 * String token = getToken();
		 * 
		 * if (!ServicesUtil.isEmpty(requestUrl)) { requestUrl +=
		 * "&$top=1000&$inlinecount=allpages";
		 * 
		 * restResponse = RestUtil.invokeRestService(requestUrl,
		 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
		 * true, null, null, null, null, null, null);
		 * 
		 * restResponse = RestUtil.callRestService(requestUrl,
		 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
		 * true, null, null, null, token[0], token[1]); }
		 */

		/*
		 * responseObject = restResponse.getResponseObject(); httpResponse =
		 * restResponse.getHttpResponse();
		 * 
		 * if (!ServicesUtil.isEmpty(httpResponse)) { for (Header header :
		 * httpResponse.getAllHeaders()) { if
		 * (header.getName().equalsIgnoreCase("X-Total-Count")) {
		 * taskInstancesCount = Integer.parseInt(header.getValue()); } } } else
		 * if (!ServicesUtil.isEmpty(restResponse.getUrlConnection()) &&
		 * restResponse.getResponseCode() == HttpURLConnection.HTTP_OK) {
		 * taskInstancesCount =
		 * Integer.parseInt(restResponse.getUrlConnection().getHeaderField(
		 * "X-Total-Count")); } if (clazz.equals(Task.class)) { //
		 * System.err.println("[WBP-Dev]AdminParse.getInstances() tasks :" + //
		 * responseObject.toString()); taskArray =
		 * ServicesUtil.isEmpty(responseObject) ? null :
		 * JsonIterator.deserialize(responseObject.toString(), new
		 * TypeLiteral<List<Task>>() { }); //
		 * System.err.println("[WBP-Dev]AdminParse.getInstances() taskArray :" +
		 * // taskArray); if (!ServicesUtil.isEmpty(taskArray)) taskArraySize =
		 * taskArray.size(); if (taskInstancesCount > taskArraySize) { int skip
		 * = 1000; for (int k = 1; k < taskInstancesCount / skip; k++) {
		 * requestUrl += "&$skip=" + (skip * k);
		 * 
		 * restResponse = RestUtil.invokeRestService(requestUrl,
		 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
		 * true, null, null, null, null, null, null);
		 * 
		 * 
		 * restResponse = RestUtil.callRestService(requestUrl,
		 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
		 * true, null, null, null, token[0], token[1]); responseObject =
		 * restResponse.getResponseObject();
		 * 
		 * taskArraySkip = ServicesUtil.isEmpty(responseObject) ? null :
		 * JsonIterator.deserialize(responseObject.toString(), new
		 * TypeLiteral<List<Task>>() { }); if (!ServicesUtil.isEmpty(taskArray)
		 * && taskArraySize > 0) { taskArray.addAll(taskArraySkip); } else {
		 * taskArray = taskArraySkip; } } } return taskArray; } else if
		 * (clazz.equals(oneapp.incture.workbox.adapter_base.mapping.Process.
		 * class)) {
		 * 
		 * // System.err.println("[WBP-Dev]AdminParse.getInstances() process :"
		 * + // responseObject.toString()); processArray =
		 * ServicesUtil.isEmpty(responseObject) ? null :
		 * JsonIterator.deserialize(responseObject.toString(), new
		 * TypeLiteral<List<oneapp.incture.workbox.adapter_base.mapping.Process>
		 * >() { }); // System.err.
		 * println("[WBP-Dev]AdminParse.getInstances() processArray :" + //
		 * processArray); if (!ServicesUtil.isEmpty(processArray))
		 * processArraySize = processArray.size(); if (taskInstancesCount >
		 * processArraySize) { int skip = 1000; for (int k = 1; k <
		 * taskInstancesCount / skip; k++) { requestUrl += "&$skip=" + (skip *
		 * k);
		 * 
		 * restResponse = RestUtil.invokeRestService(requestUrl,
		 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
		 * true, null, null, null, null, null, null);
		 * 
		 * 
		 * restResponse = RestUtil.callRestService(requestUrl,
		 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
		 * true, null, null, null, token[0], token[1]);
		 * 
		 * 
		 * responseObject = restResponse.getResponseObject(); processArraySkip =
		 * ServicesUtil.isEmpty(responseObject) ? null :
		 * JsonIterator.deserialize(responseObject.toString(), new
		 * TypeLiteral<List<oneapp.incture.workbox.adapter_base.mapping.Process>
		 * >() { }); if (!ServicesUtil.isEmpty(processArray) && processArraySize
		 * > 0) { processArray.addAll(processArraySkip); } else { processArray =
		 * processArraySkip; } } } return processArray;
		 * 
		 * }
		 */
		return null;

	}

	private UserDetailsDto getUserDetails(UserDetailsDto userDetail) {
		UserDetailsDto user = new UserDetailsDto();
		if (ServicesUtil.isEmpty(usersMap)) {
			refreshUserDetailsMap();
			user = getUserDetails(userDetail);
		} else {
			user = usersMap.get(userDetail.getUserId());
		}
		return user;
	}

	private Map<String, UserDetailsDto> refreshUserDetailsMap() {
		Map<String, UserDetailsDto> userDetailsMap = new HashMap<>();
		UserDetailsDto userDetail = null;
		List<Object[]> resultList = this.userDetails.getUserDetailResponse();
		if (!ServicesUtil.isEmpty(resultList) && !resultList.isEmpty()) {
			for (Object[] object : resultList) {
				userDetail = new UserDetailsDto();
				userDetail.setUserId(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
				userDetail.setEmailId(ServicesUtil.isEmpty(object[3]) ? null : (String) object[3]);
				userDetail.setDisplayName(ServicesUtil.isEmpty(object[4]) ? null : (String) object[4]);
				userDetailsMap.put(userDetail.getUserId(), userDetail);
			}
		}
		usersMap = userDetailsMap;
		return userDetailsMap;
	}

	private void emptyGroupsMapping() {
		groupMappings = null;
	}

	private void emptyUsersMap() {
		usersMap = null;
	}

}
