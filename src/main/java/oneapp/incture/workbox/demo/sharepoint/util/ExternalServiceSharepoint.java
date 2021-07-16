package oneapp.incture.workbox.demo.sharepoint.util;

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

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

//@Component
@Service("ExternalServiceSharePoint")
public class ExternalServiceSharepoint {

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

		if (!ServicesUtil.isEmpty(requestUrl) && !ServicesUtil.isEmpty(tokenType) && !ServicesUtil.isEmpty(tokenNo)) {
			requestUrl += "&$top=1000&$inlinecount=allpages";
			restResponse = RestUtilSharePoint.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					"application/json", true, null, null, null, tokenType, tokenNo);
		}
		if(restResponse != null){
			responseObject = restResponse.getResponseObject();
			httpResponse = restResponse.getHttpResponse();
		}
		if(httpResponse != null){
			for (Header header : httpResponse.getAllHeaders()) {
				if ("X-Total-Count".equalsIgnoreCase(header.getName())) {
					taskInstancesCount = Integer.parseInt(header.getValue());
				}
			}
		}
		JSONArray jsonArray = ServicesUtil.isEmpty(responseObject) ? null : (JSONArray) responseObject;
		if(jsonArray != null && taskInstancesCount > jsonArray.length()){
				int skip = 1000;
				for (int k = 1; k < taskInstancesCount / skip; k++) {
					requestUrl += "&$skip=" + (skip * k);
					restResponse = RestUtilSharePoint.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null,
							"GET", "application/json", true, null, null, null, null, null);
					responseObject = restResponse.getResponseObject();
					JSONArray jsonArraySkip = ServicesUtil.isEmpty(responseObject) ? null : (JSONArray) responseObject;
					jsonArray = mergeJsonArray(jsonArray, jsonArraySkip);
				}
		}
		return jsonArray;
	}

	private JSONArray fetchInstances(String requestUrl) {
		int taskInstancesCount = -1;
		Object responseObject = null;
		HttpResponse httpResponse = null;
		RestResponse restResponse = null;

		if (!ServicesUtil.isEmpty(requestUrl)) {
			requestUrl += "&$top=1000&$inlinecount=allpages";
			restResponse = RestUtilSharePoint.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null, "GET",
					"application/json", true, null, null, null, null, null);
		}
		if(restResponse != null){
			responseObject = restResponse.getResponseObject();
			httpResponse = restResponse.getHttpResponse();
		}
		if(httpResponse != null){
			for (Header header : httpResponse.getAllHeaders()) {
				if ("X-Total-Count".equalsIgnoreCase(header.getName())) {
					taskInstancesCount = Integer.parseInt(header.getValue());
				}
			}
		}
		JSONArray jsonArray = ServicesUtil.isEmpty(responseObject) ? null : (JSONArray) responseObject;
		if(jsonArray != null && taskInstancesCount > jsonArray.length()){
				int skip = 1000;
				for (int k = 1; k < taskInstancesCount / skip; k++) {
					requestUrl += "&$skip=" + (skip * k);
					restResponse = RestUtilSharePoint.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null,
							"GET", "application/json", true, null, null, null, null, null);
					responseObject = restResponse.getResponseObject();
					JSONArray jsonArraySkip = ServicesUtil.isEmpty(responseObject) ? null : (JSONArray) responseObject;
					jsonArray = mergeJsonArray(jsonArray, jsonArraySkip);
				}
		}
		return jsonArray;
	}

	private JSONArray mergeJsonArray(JSONArray jsonArray, JSONArray jsonArraySkip) {
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

	public Map<String, String> fetchAllTaskEvents(List<String> taskIds) {
		Map<String, String> taskProcessMap = null;
		String taskInstanceURL = getProperty.getValue("REQUEST_URL_INST")
				+ "task-instances?status=READY&status=RESERVED";
		JSONArray jsonArray = fetchInstances(taskInstanceURL);
		if (!ServicesUtil.isEmpty(jsonArray) && jsonArray.length() > 0 && !ServicesUtil.isEmpty(taskIds)
				&& !taskIds.isEmpty()) {
			taskProcessMap = new HashMap<>();
			for (Object taskJsonObject : jsonArray) {
				JSONObject task = (JSONObject) taskJsonObject;
				String taskId = task.optString("id");
				if (!ServicesUtil.isEmpty(task)) {
					taskId = taskId.trim();
					if (taskIds.contains(taskId)) {
						TaskEventsDto event = parseJSONObjectToTaskDto(task);
						if (!ServicesUtil.isEmpty(event) && !ServicesUtil.isEmpty(event.getEventId()))
							taskProcessMap.put(event.getEventId(), event.getProcessId());
					}
				}
			}
		}
		return taskProcessMap;
	}

	public List<ProcessEventsDto> fetchAllProcessEvents(Map<String, String> taskProcessMap) {
		Map<String, JSONObject> processMap = new HashMap<>();
		List<ProcessEventsDto> processEventsList = null;
		ProcessEventsDto processDto = null;
		String processInstanceURL = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances?";
		
		JSONArray jsonArray = fetchInstances(processInstanceURL);
		
		if (!ServicesUtil.isEmpty(jsonArray) && jsonArray.length() > 0 && !ServicesUtil.isEmpty(taskProcessMap)
				&& taskProcessMap.size() > 0) {
			processEventsList = new ArrayList<>();
			for (Object processJsonObject : jsonArray) {
				JSONObject process = (JSONObject) processJsonObject;
				
				processMap.put(process.optString("id"), process);

			}

			for (Entry<String, String> entry : taskProcessMap.entrySet()) {
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
			Object responseObject = RestUtilSharePoint.callRestService(processInstanceURL,
					PMCConstant.SAML_HEADER_KEY_TC, null, "GET", "application/json", true, null, null, null, null, null)
					.getResponseObject();
			JSONObject jsonObject = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
			if (jsonObject != null && jsonObject.toString().contains("RequestId")) {
				return jsonObject.optString("RequestId");
			}
		}
		return null;
	}

	public Map<String, String> fetchKeyValue(String processId, List<String> keys) {
		Map<String, String> map = new HashMap<>();
		if (!ServicesUtil.isEmpty(processId)) {
			String taskInstanceURL = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances/" + processId + "/context";
			System.err.println(taskInstanceURL);
			Object responseObject = RestUtilSharePoint.callRestService(taskInstanceURL, PMCConstant.SAML_HEADER_KEY_TC,
					null, "GET", "application/json", true, null, null, null, null, null).getResponseObject();
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
		String message = "";
		if (PMCConstant.ACTION_TYPE_CLAIM.equalsIgnoreCase(actionType) 
				|| PMCConstant.ACTION_TYPE_RELEASE.equalsIgnoreCase(actionType)
				|| PMCConstant.ACTION_TYPE_FORWARD.equalsIgnoreCase(actionType)) {
			message = "Task " + actionType;
		}
		try {
			RestUtilSharePoint.callRestService(getProperty.getValue("REQUEST_URL_INST") + "/task-instances/" + taskId,
					PMCConstant.SAML_HEADER_KEY_TI, payload, PMCConstant.HTTP_METHOD_PATCH,
					PMCConstant.APPLICATION_JSON, false, "Fetch", null, null, token, tokenType);
			if (PMCConstant.ACTION_TYPE_CLAIM.equals(actionType)) {
				message = "Task " + PMCConstant.CLAIM_SUCCESS;
			} else if (PMCConstant.ACTION_TYPE_RELEASE.equals(actionType)) {
				message = "Task " + PMCConstant.RELEASE_SUCCESS;
			} else if (PMCConstant.ACTION_TYPE_FORWARD.equals(actionType)) {
				message = "Task " + PMCConstant.FORWARD_SUCCESS;
			}
			responseMessage = new ResponseMessage(PMCConstant.STATUS_SUCCESS, PMCConstant.CODE_SUCCESS, message);
		} catch (Exception ex) {
			System.err.println("[WBP-Dev]Exception while updating Task : " + ex.getMessage());
			if (PMCConstant.ACTION_TYPE_CLAIM.equals(actionType)) {
				message = "Task " + PMCConstant.CLAIM_FAILURE;
			} else if (PMCConstant.ACTION_TYPE_RELEASE.equals(actionType)) {
				message = "Task " + PMCConstant.RELEASE_FAILURE;
			} else if (PMCConstant.ACTION_TYPE_FORWARD.equals(actionType)) {
				message = "Task " + PMCConstant.FORWARD_FAILURE;
			}
			responseMessage = new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE, message);
		}
		return responseMessage;
	}
}
