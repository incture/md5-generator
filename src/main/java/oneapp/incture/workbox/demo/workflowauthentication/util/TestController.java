package oneapp.incture.workbox.demo.workflowauthentication.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.cloud.security.xsuaa.token.XsuaaToken;

@RestController
public class TestController {

	@Autowired
	private WorkflowUtil workflowUtil;

	@Autowired
	private DestinationAccessHelper destinationAccessHelper;

	@GetMapping("/v1/say")
	public String sayHello() {
		return "Hello World";
	}

	@GetMapping("/v1/sayHello/{id}")
	public Map<String, String> sayHello1(@AuthenticationPrincipal Token token, @PathVariable String id) {

		System.err.println("Got the Xsuaa token: {}" + token.getAppToken());
		System.err.println(token.toString());

		Map<String, String> result = new HashMap<>();
		result.put("grant type", token.getGrantType());
		result.put("client id", token.getClientId());
		result.put("subaccount id", token.getSubaccountId());
		result.put("zone id", token.getZoneId());
		result.put("logon name", token.getLogonName());
		result.put("family name", token.getFamilyName());
		result.put("given name", token.getGivenName());
		result.put("email", token.getEmail());
		result.put("authorities", String.valueOf(token.getAuthorities()));
		result.put("scopes", String.valueOf(token.getScopes()));
		result.put("PathParam", id);

		return result;
	}

	@GetMapping("/v1/triggerwf")
	public String testWorkflowTest(@AuthenticationPrincipal XsuaaToken token)
			throws ClientProtocolException, JSONException, IOException {

		workflowUtil.triggerWorkflow("{\"definitionId\":\"testworkflowapp\",\"context\":{\"name\":\"testing 1 today\"}}",
				token, "https://api.workflow-sap.cfapps.us10.hana.ondemand.com/workflow-service");
		return "Success";
	}

	@GetMapping("/v1/approveTask")
	public String approveTask(@AuthenticationPrincipal XsuaaToken token, @RequestParam String taskId)
			throws ClientProtocolException, JSONException, IOException {

		JSONObject response = new JSONObject();
		JSONObject context = new JSONObject();
		response.put(WorkflowConstants.STATUS, "completed");
		context.put("status", "Approved");
		context.put("approverComments", "Hey hi");
		context.put("isApproved", Boolean.TRUE);
		response.put(WorkflowConstants.CONTEXT, context);

		workflowUtil.actionOnTask(response.toString(), token,
				"https://api.workflow-sap.cfapps.us10.hana.ondemand.com/workflow-service", taskId);
		return "Success";
	}

	@GetMapping("/v1/sso")
	public String get(@AuthenticationPrincipal XsuaaToken token)
			throws ClientProtocolException, JSONException, IOException {
		return destinationAccessHelper.readDestinationDestination("workflow_destination", "", token,null);
	}

	@GetMapping("/v1/testBind")
	public String testBind(@AuthenticationPrincipal XsuaaToken token)
			throws JsonMappingException, JsonProcessingException {
		return destinationAccessHelper.accessToken();
	}

	@GetMapping("/v1/getTaks")
	public String getTaskIntance(@AuthenticationPrincipal XsuaaToken token)
			throws ClientProtocolException, JSONException, IOException {
		return workflowUtil.getTaskInstances(token,
				"https://api.workflow-sap.cfapps.us10.hana.ondemand.com/workflow-service");

	}

}
