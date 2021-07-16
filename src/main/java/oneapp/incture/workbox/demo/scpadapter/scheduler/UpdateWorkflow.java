package oneapp.incture.workbox.demo.scpadapter.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.scpadapter.util.ExternalService;
import oneapp.incture.workbox.demo.scpadapter.util.OAuth;
import oneapp.incture.workbox.demo.scpadapter.util.RestUtil;
import oneapp.incture.workbox.demo.scpadapter.util.WorkflowContextData;

@Service("UpdateWorkflow")
public class UpdateWorkflow {

	@Autowired
	private ExternalService externalService;

	@Autowired
	OAuth oAuth;

	@Autowired
	PropertiesConstants getProperty;

	String[] tokens = null;

	@PostConstruct
	public void executeAfterConstructor() {
		tokens = oAuth.getToken();
	}


	public List<String> getRecipientUser(String taskId, String type, JSONObject resource) {
		List<String> resultList = new ArrayList<>();
		JSONObject resources = resource;
		try {
			if (type.equals(PMCConstant.FORWARD_TASK)) {
				String requestUrl = getProperty.getValue("REQUEST_URL_INST") + "task-instances/" + taskId;
				Object responseObject = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null,
						"GET", "application/json", false, null, null, null, tokens[0], tokens[1]).getResponseObject();
				JSONObject jsonObject = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
				resources = (JSONObject) jsonObject;
				System.err.println("[WBP-Dev] Resources: "+resources);
			}
			JSONArray jsonArray = resource.getJSONArray("recipientUsers");
			for (int i = 0; i < jsonArray.length(); i++)
				resultList.add(jsonArray.optString(i));
			String currentProcessor = resource.optString("processor");

			if (!ServicesUtil.isEmpty(currentProcessor))
				resultList.add(currentProcessor);
		} catch (Exception e) {
			System.err.println("[PMC][SubstitutionRuleFacade][getRecipientUser][error]"+ e.getMessage());
		}
		return resultList;

	}

	public List<String> getRecipientUserOfGroup() {
		List<String> groupUser = new ArrayList<>();
		String requestUrl = "https://aiiha1kww.accounts.ondemand.com/service/scim/Users?filter=groups%20eq%20%22workbox%22";
		Object responseObject = RestUtil.callRestService(requestUrl, null, null, "GET", "application/scim+json", false,
				null, "T000006", "Workbox@123", null, null).getResponseObject();
		JSONObject jsonObject = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
		if (jsonObject != null) {
			JSONArray resources = jsonObject.getJSONArray("Resources");
			JSONObject resource;
			for (Object obj : resources) {
				try {
					resource = (JSONObject) obj;
					groupUser.add(resource.optString("id"));
				} catch (Exception e) {
					System.err.println("[PMC][SubstitutionRuleFacade][getRecipientUserOfGroup][error]"
							+ e.getMessage());
				}
			}
		}
		return groupUser;
	}

	public String getPayloadForSubstitution(List<String> recipientUser) {
		String listToString = "";
		if (!ServicesUtil.isEmpty(recipientUser) && !recipientUser.isEmpty()) {
			listToString = "{ \"recipientUsers\":";
			listToString += "\"";
			for (String str : recipientUser) {
				listToString += str + ",";
			}
			listToString = listToString.substring(0, listToString.length() - 1);
			listToString += "\"";
			listToString += ",\"processor\":\"\"}";
		} else if (ServicesUtil.isEmpty(recipientUser)) // for RU of group
			listToString = "{ \"recipientUsers\":\"\"}";
		return listToString;
	}

	public String updateRecipientUserInWorkflow(String taskId, String payload) {
		try {
			String requestUrl = getProperty.getValue("REQUEST_URL_INST") + "task-instances/" + taskId;

			RestResponse restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, payload,
					"PATCH", "application/json", false, "Fetch", null, null, tokens[0], tokens[1]);
			if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >= 200)
					&& (restResponse.getResponseCode() <= 207)) {
				return PMCConstant.SUCCESS;
			}
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][PMC][SubstitutionRuleFacade][updateRecipientUserInWorkflow][error]" + e.getMessage());

		}
		return PMCConstant.SUCCESS;

	}

	public ResponseMessage adminClaim(List<String> instanceList, String processor) {
		ResponseMessage responseMessage = null;
		for (String instance : instanceList) {
			String payload = "{\"processor\":\"" + processor + "\"}";
			responseMessage = externalService.updateTaskDefinition(instance, payload, tokens[0], tokens[1],
					PMCConstant.ACTION_TYPE_CLAIM);
		}
		return responseMessage;
	}

	public ResponseMessage adminRelease(List<String> instanceList) {
		ResponseMessage responseMessage = null;
		for (String instance : instanceList) {
			String payload = "{\"processor\":\"\"}";
			responseMessage = externalService.updateTaskDefinition(instance, payload, tokens[0], tokens[1],
					PMCConstant.ACTION_TYPE_RELEASE);
		}
		return responseMessage;
	}

	public ResponseMessage adminForward(List<String> instanceList, String userId) {
		ResponseMessage responseMessage = null;
		for (String instance : instanceList) {
			String payload = "{\"processor\":\"" + userId + "\"}";
			responseMessage = externalService.updateTaskDefinition(instance, payload, tokens[0], tokens[1],
					PMCConstant.ACTION_TYPE_FORWARD);
		}
		return responseMessage;
	}

	public String createWorkFlowInstance(String payload) {
		try {
			String requestUrl = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances";

			RestResponse restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, payload,
					"POST", "application/json", false, null, null, null, tokens[0], tokens[1]);

			System.err.println("restResponse For Instance Creation: " + restResponse);
			if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >= 200)
					&& (restResponse.getResponseCode() <= 207)) {
				return PMCConstant.SUCCESS;
			} else
				return PMCConstant.FAILURE;
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][PMC][SubstitutionRuleFacade][updateRecipientUserInWorkflow][error]" + e.getMessage());

			return PMCConstant.FAILURE;
		}

	}

//	public static void main(String[] args) {
//		new UpdateWorkflow().createWorkFlowInstanceMap(null);
//	}

	public String createWorkFlowInstanceMap(Map<String, String> payload) {
		try {
			
			String requestUrl = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances";

			for (Entry<String, String> data : payload.entrySet()) {
				String id = data.getKey();
				String sampleData = data.getValue();
				for (String context : sampleData.split("split")) {
					JSONObject obj = new JSONObject(context);
					String workflowData = "{\"definitionId\":\"" + id + "\" ,\"context\":" + obj.toString() + " }";

					System.err.println("UpdateWorkflow.createWorkFlowInstanceMap() body : " + workflowData);

					RestResponse restResponse = RestUtil.callRestService(requestUrl, null, workflowData, "POST",
							"application/json", false, "Fetch", "P000092", "Incture@12345", null, null);

					System.err.println("restResponse For Instance Creation: " + restResponse.getResponseObject());

					if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >= 200)
							&& (restResponse.getResponseCode() <= 207)) {
						System.out.println("UpdateWorkflow.createWorkFlowInstanceMap() workflow started : " + id);

					} else
						System.out.println("UpdateWorkflow.createWorkFlowInstanceMap() workflow not started : " + id);
					break;
				}

			}
		} catch (Exception e) {
			System.err.println("[PMC][SubstitutionRuleFacade][updateRecipientUserInWorkflow][error]" + e.getMessage());

			return PMCConstant.FAILURE;
		}

		return PMCConstant.SUCCESS;
	}

	private Map<String, String> prepareSampleData() {

		Map<String, String> map = new HashMap<>();
		map.put("leaveapprovalmanagement", WorkflowContextData.leaveApproval);
		map.put("budgetaryapprovalprocess", WorkflowContextData.budgetaryApproval);
		map.put("campaignmanagementworkflow", WorkflowContextData.campaignApproval);
		map.put("purchaseorderapproval", WorkflowContextData.purchaseOrderApproval);
		map.put("travelandexpenseapproval", WorkflowContextData.travelAndExpense);

		return map;
	}

	public String createWorkFlowInstance(JSONObject payload) {
		try {
			String requestUrl = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances";

			RestResponse restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI,
					payload.toString(), "POST", "application/json", true, "Fetch", null, null, null, null);
			System.out.println("UpdateWorkflow.createWorkFlowInstance()");
			System.err.println(
					"UpdateWorkflow.createWorkFlowInstance() : restResponse For Instance Creation: " + restResponse);
			if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >= 200)
					&& (restResponse.getResponseCode() <= 207)) {
				return PMCConstant.SUCCESS;
			} else
				return PMCConstant.FAILURE;
		} catch (Exception e) {
			System.err.println("[WBP-Dev]UpdateWorkflow.createWorkFlowInstance() [error] : " + e.getMessage());

			return PMCConstant.FAILURE;
		}

	}

}
