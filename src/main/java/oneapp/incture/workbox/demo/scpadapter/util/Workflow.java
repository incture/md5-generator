package oneapp.incture.workbox.demo.scpadapter.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.transaction.Transactional;

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
import com.google.gson.reflect.TypeToken;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystForm;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystFormData;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystLineItemsAndForms;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystsLineItems;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstantsStatic;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.scpadapter.scheduler.UpdateWorkflow;

@Component("scpWorkflow")
public class Workflow {

	@Autowired
	PropertiesConstants getProperty;
	
	@Autowired
	private UpdateWorkflow updateWorkflow;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	TaskEventsDao taskEvents;

	private static String[] getToken() {
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

			Object responseObject = RestUtil.callRestService(requestUrl, null, null, "POST", "application/json", false,
					null, userId, password, null, null).getResponseObject();
			System.err.println("Workflow.getToken() response object : "+responseObject);
			JSONObject resources = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
			System.err.println("Workflow.getToken() resources : "+resources);
			if (resources != null) {
				result[0] = resources.optString("access_token");
				result[1] = resources.optString("token_type");
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][getPrevRecipient][error]" + e.getMessage());
		}
		return result;
	}

	public RestResponse getContextData(String taskId) {
		String token[] = getToken();
		String url = getProperty.getValue("REQUEST_URL_INST") + "task-instances/" + taskId + "/context";
		RestResponse response = null;
		try {
			response = RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
					false, null, null, null, token[0], token[1]);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workflow][getContextData()] " + e.getMessage());
		}
		return response;
	}
	
	public RestResponse getWorkflowContextData(String processId) {
		String token[] = getToken();
		String url = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances/" + processId + "/context";
		RestResponse response = null;
		try {
			response = RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json",
					false, null, null, null, token[0], token[1]);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workflow][getContextData()] " + e.getMessage());
		}
		return response;
	}
	
	public RestResponse updateCustomAttributes(String payload, String processId){
		String token[] = getToken();
		RestResponse response = null;
		JSONObject jsonPayload = new JSONObject(payload);
		System.err.println("Workflow.updateCustomAttributes() JSON payload : "+payload);
		String url = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances/" + processId + "/context";
		try{
			response = RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI, jsonPayload.toString(), "PATCH", 
					"application/json", false, null, null, null, token[0], token[1]);
			
			System.err.println("Workflow.updateCustomAttributes() rest response : "+response.getResponseObject()+" with response code : "+response.getResponseCode());;
		}catch(Exception e){
			System.err.println("Workflow.enclosing_method() exception "+e.getMessage());
		}
		return response;
	}
	
//	////@Transactional
	public RestResponse updateContextData(String payload, String processId, String processName){
		String token[] = getToken();
		RestResponse response = null;
		JSONObject jsonPayload = new JSONObject(payload);
		System.err.println("Workflow.updateContextData() JSON payload : "+payload);
		String url = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances/" + processId + "/context";
		try{
			response = RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI, jsonPayload.toString(), "PATCH", 
					"application/json", false, null, null, null, token[0], token[1]);
			
			System.err.println("Workflow.updateContextData() rest response : "+response.getResponseObject()+" with response code : "+response.getResponseCode());
			
			if(response.getResponseCode() >= 201 && response.getResponseCode() <= 207){
				
				
				
			}
			
			try {
				if (processName.equalsIgnoreCase("ic_manager_approval_process")) {

					// get existing forms
					AnalystLineItemsAndForms lineItemsAndForms = taskEvents.getManagerLineItemsAndForms(processId);
					
					List<AnalystFormData> formsExisting=lineItemsAndForms.getForms();
					
					List<AnalystFormData> formsNew=convertToFormObject(jsonPayload.getJSONArray("forms"));
					
					List<AnalystFormData> formUpdate=getUpdatedForms(formsNew,formsExisting);
					JSONArray jsonPayload3 = new JSONArray(formUpdate);
					
					String query = "UPDATE CUSTOM_ATTR_VALUES SET ATTR_VALUE = '"
							+ jsonPayload3.toString() + "' "
							+ "WHERE KEY = 'forms' AND TASK_ID IN (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '"
							+ processId + "')";

					sessionFactory.getCurrentSession().createSQLQuery(query).executeUpdate();
					
				} else {

					String query = "UPDATE CUSTOM_ATTR_VALUES SET ATTR_VALUE = '"
							+ jsonPayload.getJSONArray("forms").toString() + "' "
							+ "WHERE KEY = 'forms' AND TASK_ID IN (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '"
							+ processId + "')";

					sessionFactory.getCurrentSession().createSQLQuery(query).executeUpdate();
				}

			}catch(Exception e){
				
				System.err.println("Workflow.updateContextData() error : "+e);
			}

			
			if(processName.equalsIgnoreCase("ic_manager_approval_process")){

				List<AnalystFormData> forms = new ArrayList<AnalystFormData>();
				List<AnalystForm> analystFormList = null;
				
				JsonParser parser = new JsonParser();
				JsonElement jsonElement = parser.parse(payload);
				JsonObject jObj = jsonElement.getAsJsonObject();
				JsonArray jsonArray = jObj.getAsJsonArray("forms");
				System.err.println("Workflow.updateContextData() json array : "+jsonArray);
				
				for (Object jsonObj : jsonArray) {
					analystFormList = new ArrayList<AnalystForm>();
					JsonParser jsonParser = new JsonParser();
					JsonElement element = jsonParser.parse(jsonObj.toString());
					JsonObject jsonObject = element.getAsJsonObject();
					Set<String> keys = jsonObject.keySet();
					String formId = "";
					String formStatus = "";
					String lineItemsOfForm = "";
					List<AnalystsLineItems> parsedLineItemsTemp=new ArrayList<>();
					for (String key : keys) {

						if (key.equalsIgnoreCase("formData")) {
							JsonArray array = jsonObject.getAsJsonArray(key);
//							System.out.println("TaskEventsDao.main() array : " + array);
							for (Object object : array) {
								Gson gson = new Gson();
								AnalystForm form = gson.fromJson(object.toString(), AnalystForm.class);
								analystFormList.add(form);
							}
						} else if (key.equalsIgnoreCase("formId")) {
							formId = jsonObject.get("formId").getAsString();
//							System.err.println("TaskEventsDao.main() form id " + s);

						} else if (key.equalsIgnoreCase("formStatus")) {
							formStatus = jsonObject.get("formStatus").getAsString();
//							System.err.println("TaskEventsDao.main() form id " + s);
						}
						else if (key.equalsIgnoreCase("lineItems")) {
							lineItemsOfForm = jsonObject.get("lineItems").getAsJsonArray().toString();
							System.err.println("TaskEventsDao.main() form id " + lineItemsOfForm);
							parsedLineItemsTemp=parseLineItems(lineItemsOfForm);
						}	

					}
					if(formStatus.equalsIgnoreCase("RETURNED")){
						System.out.println("Workflow.main() inside if");
					forms.add(new AnalystFormData(analystFormList, formId, formStatus,parsedLineItemsTemp));
					}
				}
				
				System.err.println("Workflow.updateContextData() froms : "+forms);
				
				
			
				
				RestResponse resp = getWorkflowContextData(processId);
				System.err.println("Workflow.updateContextData() workflow context data : "+resp.getResponseObject()+" with code : "+resp.getResponseCode());
				JSONObject context = (JSONObject) resp.getResponseObject();
				
				//get the rejected forms
				
				for(AnalystFormData form:forms){
					String result = triggerAnalystApprovalProcess(form,context,processId);
					
					if(result.equalsIgnoreCase(PMCConstant.SUCCESS)){
						response.setResponseCode(200);
					}
					else{
						response.setResponseCode(500);
					}
				}
				
				
				
				
			}
		}catch(Exception e){
			System.err.println("Workflow.updateContextData() exception "+e.getMessage());
		}
		
		return response;
	}
	
	
	
	public RestResponse updateWorkflowContextData(String payload, String processId){
		String token[] = getToken();
		RestResponse response = null;
		JSONObject jsonPayload = new JSONObject(payload);
		System.err.println("Workflow.updateContextData() JSON payload : "+payload);
		String url = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances/" + processId + "/context";
		try{
			response = RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI, jsonPayload.toString(), "PATCH", 
					"application/json", false, null, null, null, token[0], token[1]);
			
			System.err.println("Workflow.updateContextData() rest response : "+response.getResponseObject()+" with response code : "+response.getResponseCode());
			
			if(response.getResponseCode() >= 201 && response.getResponseCode() <= 207){
				
				
				
			}
		}catch(Exception e){
			System.err.println("Workflow.updateContextData() exception "+e.getMessage());
		}
		
		return response;
		
		}
	
	private static  List<AnalystFormData> getUpdatedForms(List<AnalystFormData> formsNew, List<AnalystFormData> formsExisting) {
		
		int index=-1;
		AnalystFormData newform=new AnalystFormData();
		for(AnalystFormData f:formsNew){
			
			for(AnalystFormData ff:formsExisting){
				index++;
				if(f.getFormId().equalsIgnoreCase(ff.getFormId())){
//					System.err.println("Workflow.getUpdatedForms()");
					newform=f;
					break;
				}
				
			}
			break;
		}
		
//		System.out.println("Workflow.getUpdatedForms()"+newform);
//		System.out.println("Workflow.getUpdatedForms() index : "+index);
//		System.err.println("Workflow.getUpdatedForms() before "+formsExisting);
		if(index!=-1){
			formsExisting.add(index,newform);
			formsExisting.remove(index+1);
		}
//		System.err.println("Workflow.getUpdatedForms() after"+formsExisting);
		return formsExisting;
	}
	
//	public static void main(String[] args) {
////		String s="{\"forms\":[{\"formData\":[{\"valueHelp\":[],\"value\":\"Venky 1\",\"key\":\"What measures have been put into place to avoid further correction?\"},{\"valueHelp\":[\"Process\",\"Training\",\"Individual\",\"Tools\"],\"value\":\"Tools\",\"key\":\"What was the main reason for the system correction?\"},{\"valueHelp\":[],\"value\":\"Venky 1\",\"key\":\"Please provide details of any additional training or action against an individual or group taken as a result of the correction\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"What was the issue raised in JOS meeting?\"},{\"valueHelp\":[],\"value\":\"Venky 1\",\"key\":\"Analyst Comment\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"Was there a corrective action raised\"},{\"valueHelp\":[],\"value\":\"sdfsdf\",\"key\":\"Manager Comment\"}],\"formId\":\"J-4088\",\"formStatus\":\"APPROVED\",\"lineItems\":[{\"documentId\":null,\"materialDocumentNumber\":\"5067\",\"lineItemNumber\":\"0001\",\"date\":\"20200131\",\"totalCost\":\"$45.15\",\"materialNumber\":\"ASPCA-01241-01\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"movementType\":\"Z91\",\"quantity\":\"15\",\"analyst\":\"P000092\",\"formId\":\"J-4088\",\"attachments\":null}]}]}";
////		JSONObject jsonPayload = new JSONObject(s);
////		System.out.println("Workflow.main()"+convertToFormObject(jsonPayload.getJSONArray("forms")));
////		
////		String ss="{\"lineItems\":[{\"documentId\":null,\"materialDocumentNumber\":\"5067\",\"lineItemNumber\":\"0001\",\"date\":\"20200131\",\"totalCost\":\"$45.15\",\"materialNumber\":\"ASPCA-01241-01\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"movementType\":\"Z91\",\"quantity\":\"15\",\"analyst\":\"P000092\",\"formId\":\"J-6358\",\"attachments\":null},{\"documentId\":null,\"materialDocumentNumber\":\"5068\",\"lineItemNumber\":\"0002\",\"date\":\"20200131\",\"totalCost\":\"$45.15\",\"materialNumber\":\"ASPCA-01241-01\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"movementType\":\"Z92\",\"quantity\":\"15\",\"analyst\":\"P000092\",\"formId\":\"J-2727\",\"attachments\":null},{\"documentId\":null,\"materialDocumentNumber\":\"5069\",\"lineItemNumber\":\"0003\",\"date\":\"20200131\",\"totalCost\":\"$45.15\",\"materialNumber\":\"ASPCA-01241-01\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"movementType\":\"Z91\",\"quantity\":\"15\",\"analyst\":\"P000006\",\"formId\":\"J-4088\",\"attachments\":null}],\"forms\":[{\"formData\":[{\"valueHelp\":[],\"value\":\"Venky 1\",\"key\":\"What measures have been put into place to avoid further correction?\"},{\"valueHelp\":[\"Process\",\"Training\",\"Individual\",\"Tools\"],\"value\":\"Tools\",\"key\":\"What was the main reason for the system correction?\"},{\"valueHelp\":[],\"value\":\"Venky 1\",\"key\":\"Please provide details of any additional training or action against an individual or group taken as a result of the correction\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"What was the issue raised in JOS meeting?\"},{\"valueHelp\":[],\"value\":\"Venky 1\",\"key\":\"Analyst Comment\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"Was there a corrective action raised\"},{\"valueHelp\":[],\"value\":\"\",\"key\":\"Manager Comment\"}],\"formId\":\"J-6358\",\"formStatus\":\"SUBMITTED\",\"lineItems\":[{\"documentId\":null,\"materialDocumentNumber\":\"5067\",\"lineItemNumber\":\"0001\",\"date\":\"20200131\",\"totalCost\":\"$45.15\",\"materialNumber\":\"ASPCA-01241-01\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"movementType\":\"Z91\",\"quantity\":\"15\",\"analyst\":\"P000092\",\"formId\":\"J-6358\",\"attachments\":null}]},{\"formData\":[{\"valueHelp\":[],\"value\":\"Venky 2\",\"key\":\"What measures have been put into place to avoid further correction?\"},{\"valueHelp\":[\"Process\",\"Training\",\"Individual\",\"Tools\"],\"value\":\"Training\",\"key\":\"What was the main reason for the system correction?\"},{\"valueHelp\":[],\"value\":\"Venky 2\",\"key\":\"Please provide details of any additional training or action against an individual or group taken as a result of the correction\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"What was the issue raised in JOS meeting?\"},{\"valueHelp\":[],\"value\":\"Venky 2\",\"key\":\"Analyst Comment\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"Was there a corrective action raised\"},{\"valueHelp\":[],\"value\":\"\",\"key\":\"Manager Comment\"}],\"formId\":\"J-2727\",\"formStatus\":\"SUBMITTED\",\"lineItems\":[{\"documentId\":null,\"materialDocumentNumber\":\"5068\",\"lineItemNumber\":\"0002\",\"date\":\"20200131\",\"totalCost\":\"$45.15\",\"materialNumber\":\"ASPCA-01241-01\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"movementType\":\"Z92\",\"quantity\":\"15\",\"analyst\":\"P000092\",\"formId\":\"J-2727\",\"attachments\":null}]},{\"formData\":[{\"valueHelp\":[],\"value\":\"shailesh 1\",\"key\":\"What measures have been put into place to avoid further correction?\"},{\"valueHelp\":[\"Process\",\"Training\",\"Individual\",\"Tools\"],\"value\":\"Individual\",\"key\":\"What was the main reason for the system correction?\"},{\"valueHelp\":[],\"value\":\"shailesh 1\",\"key\":\"Please provide details of any additional training or action against an individual or group taken as a result of the correction\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"What was the issue raised in JOS meeting?\"},{\"valueHelp\":[],\"value\":\"shailesh 1\",\"key\":\"Analyst Comment\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"Was there a corrective action raised\"},{\"valueHelp\":[],\"value\":\"\",\"key\":\"Manager Comment\"}],\"formId\":\"J-4088\",\"formStatus\":\"SUBMITTED\",\"lineItems\":[{\"documentId\":null,\"materialDocumentNumber\":\"5069\",\"lineItemNumber\":\"0003\",\"date\":\"20200131\",\"totalCost\":\"$45.15\",\"materialNumber\":\"ASPCA-01241-01\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"movementType\":\"Z91\",\"quantity\":\"15\",\"analyst\":\"P000006\",\"formId\":\"J-4088\",\"attachments\":null}]}],\"error\":null}";
////		
////		JSONObject jsonPayload1 = new JSONObject(ss);
////		System.out.println("Workflow.main() 2 "+convertToFormObject(jsonPayload1.getJSONArray("forms")));
////		
////		System.out.println(getUpdatedForms(convertToFormObject(jsonPayload.getJSONArray("forms")),convertToFormObject(jsonPayload1.getJSONArray("forms"))));
////
////	JSONArray jsonPayload3 = new JSONArray(getUpdatedForms(convertToFormObject(jsonPayload.getJSONArray("forms")),convertToFormObject(jsonPayload1.getJSONArray("forms"))));
//////	jsonPayload3.accumulate("forms", getUpdatedForms(convertToFormObject(jsonPayload.getJSONArray("forms")),convertToFormObject(jsonPayload1.getJSONArray("forms"))));
//////System.out.println("Workflow.main() json "+jsonPayload3);
////
////		
////	System.out.println("Workflow.main() res \n "+jsonPayload3.toString());
////	
////	}
//	String payload="{\"forms\":[{\"formData\":[{\"valueHelp\":[],\"value\":\"dsd\",\"key\":\"What measures have been put into place to avoid further correction?\"},{\"valueHelp\":[\"Process\",\"Training\",\"Individual\",\"Tools\"],\"value\":\"Process\",\"key\":\"What was the main reason for the system correction?\"},{\"valueHelp\":[],\"value\":\"dsd\",\"key\":\"Please provide details of any additional training or action against an individual or group taken as a result of the correction\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"Yes\",\"key\":\"What was the issue raised in JOS meeting?\"},{\"valueHelp\":[],\"value\":\"dsd\",\"key\":\"Analyst Comment\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"Was there a corrective action raised\"},{\"valueHelp\":[],\"value\":\"need more info\",\"key\":\"Manager Comment\"}],\"formId\":\"J-5706\",\"formStatus\":\"RETURNED\",\"lineItems\":[{\"documentId\":null,\"materialDocumentNumber\":\"5067\",\"lineItemNumber\":\"0001\",\"date\":\"20200131\",\"totalCost\":\"$45.15\",\"materialNumber\":\"ASPCA-01241-01\",\"plant\":\"MY01\",\"workCell\":\"ARISTA\",\"movementType\":\"Z91\",\"quantity\":\"15\",\"analyst\":\"P000092\",\"formId\":\"J-5706\",\"attachments\":null}]}]}";
//		
//		List<AnalystFormData> forms = new ArrayList<AnalystFormData>();
//		List<AnalystForm> analystFormList = null;
//		
//		JsonParser parser = new JsonParser();
//		JsonElement jsonElement = parser.parse(payload);
//		JsonObject jObj = jsonElement.getAsJsonObject();
//		JsonArray jsonArray = jObj.getAsJsonArray("forms");
//		System.err.println("Workflow.updateContextData() json array : "+jsonArray);
//		
//		for (Object jsonObj : jsonArray) {
//			analystFormList = new ArrayList<AnalystForm>();
//			JsonParser jsonParser = new JsonParser();
//			JsonElement element = jsonParser.parse(jsonObj.toString());
//			JsonObject jsonObject = element.getAsJsonObject();
//			Set<String> keys = jsonObject.keySet();
//			String formId = "";
//			String formStatus = "";
//			String lineItemsOfForm = "";
//			List<AnalystsLineItems> parsedLineItemsTemp=new ArrayList<>();
//			for (String key : keys) {
//
//				if (key.equalsIgnoreCase("formData")) {
//					JsonArray array = jsonObject.getAsJsonArray(key);
////					System.out.println("TaskEventsDao.main() array : " + array);
//					for (Object object : array) {
//						Gson gson = new Gson();
//						AnalystForm form = gson.fromJson(object.toString(), AnalystForm.class);
//						analystFormList.add(form);
//					}
//				} else if (key.equalsIgnoreCase("formId")) {
//					formId = jsonObject.get("formId").getAsString();
////					System.err.println("TaskEventsDao.main() form id " + s);
//
//				} else if (key.equalsIgnoreCase("formStatus")) {
//					formStatus = jsonObject.get("formStatus").getAsString();
//					System.err.println("TaskEventsDao.main() form id " + formStatus);
//				}
//				else if (key.equalsIgnoreCase("lineItems")) {
//					lineItemsOfForm = jsonObject.get("lineItems").getAsJsonArray().toString();
//					System.err.println("TaskEventsDao.main() form id " + lineItemsOfForm);
//					parsedLineItemsTemp=parseLineItems(lineItemsOfForm);
//				}	
//
//			}
//			if(formStatus.equalsIgnoreCase("RETURNED")){
//				System.out.println("Workflow.main() inside if");
//			forms.add(new AnalystFormData(analystFormList, formId, formStatus,parsedLineItemsTemp));
//			}
//		}
//		
//		System.err.println("Workflow.updateContextData() froms : "+forms);
//	
//}

	private  static List<AnalystFormData> convertToFormObject(JSONArray jsonArray2) {
		
		
		
		List<AnalystForm> analystFormList = null;
		List<AnalystFormData> forms = new ArrayList<AnalystFormData>();
		
		for(Object jsonObj : jsonArray2){
			analystFormList = new ArrayList<AnalystForm>();
			JsonParser jsonParser = new JsonParser();
			JsonElement element = jsonParser.parse(jsonObj.toString());
			JsonObject jsonObject = element.getAsJsonObject();
			Set<String> keys=jsonObject.keySet();
			String s="";
			String s1="";
			for (String key : keys) {
				
				if (key.equalsIgnoreCase("formData")) {
					JsonArray array = jsonObject.getAsJsonArray(key);
					//System.out.println("TaskEventsDao.main() array : " + array);
					for (Object object : array) {
						Gson gson = new Gson();
						AnalystForm form = gson.fromJson(object.toString(), AnalystForm.class);
						analystFormList.add(form);
					}
				} else if(key.equalsIgnoreCase("formId")){
					s=jsonObject.get("formId").getAsString();
					//System.err.println("TaskEventsDao.main() form id "+s);
  
				}else if (key.equalsIgnoreCase("formStatus")){
					s1=jsonObject.get("formStatus").getAsString();
					//System.err.println("TaskEventsDao.main() form id "+s);
				}

			}
			forms.add(new AnalystFormData(analystFormList,s,s1));
			}
		//forms.add(new AnalystFormData(analystFormList));
	
		
		
		return forms;
	}

//	////@Transactional
	public RestResponse updateContextDataLineItems(String payload, String processId){
		String token[] = getToken();
		RestResponse response = null;
		JSONObject jsonPayload = new JSONObject(payload);
		System.err.println("Workflow.updateContextData() JSON payload : "+payload);
		String url = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances/" + processId + "/context";
		try{
			response = RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI, jsonPayload.toString(), "PATCH", 
					"application/json", false, null, null, null, token[0], token[1]);
			
			System.err.println("Workflow.updateContextData() rest response : "+response.getResponseObject()+" with response code : "+response.getResponseCode());
			
			if(response.getResponseCode() >= 201 && response.getResponseCode() <= 207){
				
				
				//process check if the process is IC manager process 
				//check for the manager commnet in the form 
				//contains any of these keys Reject , denied
				
				//process id can we get context data of that process 
				//line items and forms based on the index
				
				//trigger analyst task
				
				
			}
			
		}catch(Exception e){
			System.err.println("Workflow.updateContextData() exception "+e.getMessage());
			
			
		}
		
		try{
		String query = "UPDATE CUSTOM_ATTR_VALUES SET ATTR_VALUE = '"+jsonPayload.getJSONArray("lineItems").toString()+"' "
				+ "WHERE KEY = 'lineItems' AND TASK_ID IN (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '"+processId+"')";
		sessionFactory.getCurrentSession().createSQLQuery(query).executeUpdate();
		
		}catch(Exception e){
			System.err.println("Workflow.updateContextDataLineItems() error : "+e);
		}
		return response;
		}
	
	static List<AnalystsLineItems> parseLineItems(String payload){
		List<AnalystsLineItems> lineItems = new ArrayList<AnalystsLineItems>();
		System.err.println("Workflow.parseLineItems() line items payload in string : "+payload);
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(payload);
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		
		Type lineItemType = new TypeToken<ArrayList<AnalystsLineItems>>(){}.getType();
		lineItems = gson.fromJson(payload, lineItemType);
		System.err.println("Workflow.parseLineItems() line items : "+lineItems);
		
		return lineItems;
	}
	
	public String triggerAnalystApprovalProcess(AnalystFormData form,JSONObject context, String processId){
		 
	
		
		
		String result = PMCConstant.SUCCESS;
		
		String analyst=getAnalyst(form.getLineItems());
		
			
			JSONObject payload = new JSONObject();
			JSONObject mainContext = new JSONObject();
			
			
			payload.accumulate("caseId", context.get("caseId"));
			payload.accumulate("currentStatus", context.get("currentStatus"));
			payload.accumulate("role", "Analyst");
			payload.accumulate("urgency",context.get("urgency"));
			payload.accumulate("application", context.get("application"));
			payload.accumulate("icManager", context.get("icManager"));
			payload.accumulate("analyst", analyst);
		
			List<AnalystsLineItems> rejectList = form.getLineItems();
			payload.accumulate("lineItems", rejectList);
			payload.accumulate("icManagerProcessId", processId);
			int randomNum=new Random().nextInt(10000);
			
			payload.accumulate("subCaseId", context.get("caseId")+"-MVL-"+randomNum);
			
			
			List<AnalystFormData> formList = new ArrayList<AnalystFormData>();
			form.setLineItems(null);
			formList.add(form);
			payload.accumulate("forms", formList);
			
			System.err.println("Workflow.triggerAnalystApprovalProcess() payload : "+payload);
			
			mainContext.accumulate("definitionId", "analyst_appproval_process");
			mainContext.accumulate("context", payload);
			System.err.println("Workflow.triggerAnalystApprovalProcess() main context : "+mainContext);
			String workflowResponse = updateWorkflow.createWorkFlowInstance(mainContext.toString());
			
			if(workflowResponse.equalsIgnoreCase(PMCConstant.FAILURE)){
				result = workflowResponse;
			}
			
		
		return result;
	}
	
	private String getAnalyst(List<AnalystsLineItems> lineItems) {
		String analyst="P000092";
		for(AnalystsLineItems line:lineItems){
			analyst=line.getAnalyst();
		}
		return analyst;
	}

	public List<Integer> findIndexOfAnalyst(List<AnalystsLineItems> rejectList, String analyst){
		List<Integer> list = new ArrayList<Integer>();
		
		for(int i=0; i<rejectList.size(); i++){
			AnalystsLineItems obj = rejectList.get(i);
			if(obj.getAnalyst().equalsIgnoreCase(analyst))
				list.add(i);
		}
		return list;
	}
}
