package oneapp.incture.workbox.demo.scpadapter.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

//@Component
@Service("scpExternalService")
public class ExternalService {

	@Autowired
	private TaskOwnersDao taskOwnersDao;

	@Autowired
	PropertiesConstants getProperty;

	public JSONArray getTaskList(String requestUrl, String tokenType, String tokenNo) {
		return fetchInstancesWithOAuth(requestUrl, tokenType, tokenNo);
	}

	private JSONArray fetchInstancesWithOAuth(String requestUrl, String tokenType, String tokenNo) {
		int taskInstancesCount = -1;
		Object responseObject = null;
		HttpResponse httpResponse = null;
		RestResponse restResponse = null;
		JSONArray jsonArray = null;
		JSONArray jsonArraySkip = null;

		if (!ServicesUtil.isEmpty(requestUrl) && !ServicesUtil.isEmpty(tokenType) && !ServicesUtil.isEmpty(tokenNo)) {
			requestUrl += "&$top=1000&$inlinecount=allpages";
			restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					"application/json", true, null, null, null, tokenType, tokenNo);
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
							"application/json", true, null, null, null, null, null);
					if (restResponse != null) {
						responseObject = restResponse.getResponseObject();
						jsonArraySkip = ServicesUtil.isEmpty(responseObject) ? null : (JSONArray) responseObject;
						jsonArray = mergeJsonArray(jsonArray, jsonArraySkip);
					}
				}
			}
		}
		return jsonArray;
	}

	private JSONArray fetchInstances(String requestUrl) {
		int taskInstancesCount = -1;
		Object responseObject = null;
		HttpResponse httpResponse = null;
		RestResponse restResponse = null;
		JSONArray jsonArray = null;
		JSONArray jsonArraySkip = null;

		if (!ServicesUtil.isEmpty(requestUrl)) {
			requestUrl += "&$top=1000&$inlinecount=allpages";
			restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					"application/json", true, null, null, null, null, null);
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
							"application/json", true, null, null, null, null, null);
					if (restResponse != null) {
						responseObject = restResponse.getResponseObject();
						jsonArraySkip = ServicesUtil.isEmpty(responseObject) ? null : (JSONArray) responseObject;
						jsonArray = mergeJsonArray(jsonArray, jsonArraySkip);
					}
				}
			}
		}
		return jsonArray;
	}

	private JSONArray mergeJsonArray(JSONArray jsonArray, JSONArray jsonArraySkip) {
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

	public Map<String, String> fetchAllTaskEvents(List<String> taskIds) {
		JSONObject task = null;
		TaskEventsDto event = null;
		Map<String, String> taskProcessMap = null;
		String taskInstanceURL = getProperty.getValue("REQUEST_URL_INST")
				+ "task-instances?status=READY&status=RESERVED";
		// String taskInstanceURL =
		// "https://bpmworkflowruntimed7943e608-d998e5467.us2.hana.ondemand.com/workflow-service/rest/v1/task-instances?status=READY&status=RESERVED";
		JSONArray jsonArray = fetchInstances(taskInstanceURL);
		if (!ServicesUtil.isEmpty(jsonArray) && jsonArray.length() > 0 && !ServicesUtil.isEmpty(taskIds)
				&& taskIds.size() > 0) {
			taskProcessMap = new HashMap<String, String>();
			for (Object taskJsonObject : jsonArray) {
				task = (JSONObject) taskJsonObject;
				String taskId = task.optString("id");
				if (!ServicesUtil.isEmpty(task)) {
					taskId = taskId.trim();
					if (taskIds.contains(taskId)) {
						event = parseJSONObjectToTaskDto(task);
						if (!ServicesUtil.isEmpty(event) && !ServicesUtil.isEmpty(event.getEventId()))
							taskProcessMap.put(event.getEventId(), event.getProcessId());
					}
				}
			}
		}
		return taskProcessMap;
	}

	public List<ProcessEventsDto> fetchAllProcessEvents(Map<String, String> taskProcessMap) {
		JSONObject process = null;
		Map<String, JSONObject> processMap = new HashMap<String, JSONObject>();
		List<ProcessEventsDto> processEventsList = null;
		ProcessEventsDto processDto = null;
		String processInstanceURL = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances?";
		// String processInstanceURL =
		// "https://bpmworkflowruntimed7943e608-d998e5467.us2.hana.ondemand.com/workflow-service/rest/v1/workflow-instances";

		// Object responseObject =
		// SCPRestUtil.callRestService(processInstanceURL,
		// PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
		// true, null, null, null, null, null).getResponseObject();
		// JSONArray jsonArray = ServicesUtil.isEmpty(responseObject) ? null :
		// (JSONArray) responseObject;
		JSONArray jsonArray = fetchInstances(processInstanceURL);
		// System.err.println("[WBP-Dev]jsonArray : "+jsonArray);
		// JSONArray jsonArray = new
		// JSONArray(JSONUtil.readJSONFile("src/main/resources/json/process.json",
		// null));
		if (!ServicesUtil.isEmpty(jsonArray) && jsonArray.length() > 0 && !ServicesUtil.isEmpty(taskProcessMap)
				&& taskProcessMap.size() > 0) {
			processEventsList = new ArrayList<ProcessEventsDto>();
			for (Object processJsonObject : jsonArray) {
				process = (JSONObject) processJsonObject;
				// System.err.println("[WBP-Dev]process : "+process);
				processMap.put(process.optString("id"), process);
				/*
				 * for(TaskProcessDto taskProcess : processEventIds) {
				 * System.err.println("[WBP-Dev]taskProcess : "+taskProcess); if
				 * (!ServicesUtil.isEmpty(definitionId)) { definitionId =
				 * definitionId // .replaceAll("\\", "") // .replaceAll("\r",
				 * "") // .replaceAll("\n", "") .replaceAll("[^\\x00-\\x7F]",
				 * "") ; } if
				 * (taskProcess.getProcessDefinitionId().equalsIgnoreCase(
				 * process.optString("definitionId"))) {
				 * System.err.println("[WBP-Dev]Matched : "+process + " ::: " +
				 * taskProcess); processDto =
				 * parseJSONObjectToProcessDto(process);
				 * processDto.settId(taskProcess.getTaskId()); System.err.
				 * println("[WBP-Dev]task matched and added to response : "
				 * +processDto); processEventsList.add(processDto); } }
				 */
			}

			for (Entry<String, String> entry : taskProcessMap.entrySet()) {
				// System.err.println("[WBP-Dev]entry : "+entry.getKey() + " ::
				// "+entry.getValue());
				if (processMap.keySet().contains(entry.getValue())) {
					processDto = parseJSONObjectToProcessDto(processMap.get(entry.getValue()));
					processDto.settId(entry.getKey());
					processDto.setRequestId(fetchRequestId(processDto.getProcessId()));
					System.err.println("[WBP-Dev]Matched processMap : " + processDto);
					processEventsList.add(processDto);
				}
			}
		}
		return processEventsList;
	}

	private String fetchRequestId(String processId) {
		if (!ServicesUtil.isEmpty(processId)) {
			String processInstanceURL = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances/" + processId
					+ "/context";
			Object responseObject = RestUtil.callRestService(processInstanceURL, PMCConstant.SAML_HEADER_KEY_TC, null,
					"GET", "application/json", true, null, null, null, null, null).getResponseObject();
			JSONObject jsonObject = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
			if (jsonObject != null && !ServicesUtil.isEmpty(jsonObject)
					&& jsonObject.toString().contains("RequestId")) {
				return jsonObject.optString("RequestId");
			}
		}
		return null;
	}

	public Map<String, String> fetchKeyValue(String processId, List<String> keys) {
		Map<String, String> map = new HashMap<String, String>();
		if (!ServicesUtil.isEmpty(processId)) {
			String taskInstanceURL = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances/" + processId
					+ "/context";
			System.err.println(taskInstanceURL);
			Object responseObject = RestUtil.callRestService(taskInstanceURL, PMCConstant.SAML_HEADER_KEY_TC, null,
					"GET", "application/json", true, null, null, null, null, null).getResponseObject();
			JSONObject jsonObject = new JSONObject(responseObject.toString());
			System.err.println(jsonObject);
			for (String key : keys) {

				if (!ServicesUtil.isEmpty(jsonObject) && jsonObject.toString().contains(key)) {

					map.put(key, jsonObject.optString(key));

				}
			}
		}
		return map;
	}

	private TaskEventsDto parseJSONObjectToTaskDto(JSONObject taskJsonObject) {
		TaskEventsDto taskEventsDto = new TaskEventsDto();
		taskEventsDto.setEventId(taskJsonObject.optString("id"));
		taskEventsDto.setProcessId(taskJsonObject.optString("workflowInstanceId"));
		return taskEventsDto;
	}

	private ProcessEventsDto parseJSONObjectToProcessDto(JSONObject process) {
		ProcessEventsDto processEventsDto = new ProcessEventsDto();
		processEventsDto.setProcessId(process.optString("id"));
		// processEventsDto.setProcessDefinitionId(process.optString("definitionId"));
		processEventsDto.setName(process.optString("definitionId"));
		processEventsDto.setSubject(process.optString("subject"));
		processEventsDto.setStatus(process.optString("status"));
		processEventsDto.setStartedAt(ServicesUtil.convertFromStringToDate(process.optString("startedAt")));
		processEventsDto.setStartedBy(process.optString("startedBy"));
		return processEventsDto;
	}

	public ResponseMessage updateTaskDefinition(String taskId, String payload, String token, String tokenType,
			String actionType) {
		ResponseMessage responseMessage = null;
		RestResponse restResponse = null;
		String message = "";
		if (PMCConstant.ACTION_TYPE_CLAIM.equalsIgnoreCase(actionType)) {
			message = "Task " + actionType;
		} else if (PMCConstant.ACTION_TYPE_RELEASE.equalsIgnoreCase(actionType)) {
			message = "Task " + actionType;
		} else if (PMCConstant.ACTION_TYPE_FORWARD.equalsIgnoreCase(actionType)) {
			message = "Task " + actionType;
		}
		try {
			restResponse = RestUtil.callRestService(
					getProperty.getValue("REQUEST_URL_INST") + "/task-instances/" + taskId,
					PMCConstant.SAML_HEADER_KEY_TI, payload, PMCConstant.HTTP_METHOD_PATCH,
					PMCConstant.APPLICATION_JSON, false, "Fetch", null, null, token, tokenType);
			if (restResponse != null) {
				if (restResponse != null && !ServicesUtil.isEmpty(restResponse)
						&& (restResponse.getResponseCode() >= 200) && (restResponse.getResponseCode() <= 207)) {

					if (PMCConstant.ACTION_TYPE_CLAIM.equals(actionType)) {
						message = "Task " + PMCConstant.CLAIM_SUCCESS;
					} else if (PMCConstant.ACTION_TYPE_RELEASE.equals(actionType)) {
						taskOwnersDao.deleteUser(taskId, null);
						message = "Task " + PMCConstant.RELEASE_SUCCESS;
					} else if (PMCConstant.ACTION_TYPE_FORWARD.equals(actionType)) {
						message = "Task " + PMCConstant.FORWARD_SUCCESS;
					}
					responseMessage = new ResponseMessage(PMCConstant.STATUS_SUCCESS, PMCConstant.CODE_SUCCESS,
							message);
				} else {
					if (PMCConstant.ACTION_TYPE_CLAIM.equals(actionType)) {
						message = "Task " + PMCConstant.CLAIM_FAILURE;
					} else if (PMCConstant.ACTION_TYPE_RELEASE.equals(actionType)) {
						message = "Task " + PMCConstant.RELEASE_FAILURE;
					} else if (PMCConstant.ACTION_TYPE_FORWARD.equals(actionType)) {
						message = "Task " + PMCConstant.FORWARD_FAILURE;
					}
					System.err.println("[WBP-Dev]updateTaskDefinition failed " + actionType + " "
							+ "failed with response code- " + restResponse.getResponseCode());
					responseMessage = new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE,
							message + " " + restResponse.getResponseObject());

				}
			}
		} catch (Exception ex) {
			System.err.println("[WBP-Dev]Exception while updating Task : " + ex.getMessage());
			if (PMCConstant.ACTION_TYPE_CLAIM.equals(actionType)) {
				message = "Task " + PMCConstant.CLAIM_FAILURE;
			} else if (PMCConstant.ACTION_TYPE_RELEASE.equals(actionType)) {
				message = "Task " + PMCConstant.RELEASE_FAILURE;
			} else if (PMCConstant.ACTION_TYPE_FORWARD.equals(actionType)) {
				message = "Task " + PMCConstant.FORWARD_FAILURE;
			}
			if (restResponse != null)
				System.err.println("[WBP-Dev]updateTaskDefinition failed " + actionType + " "
						+ "failed with response code- " + restResponse.getResponseCode());

			responseMessage = new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE, message);
		}
		return responseMessage;
	}
}
