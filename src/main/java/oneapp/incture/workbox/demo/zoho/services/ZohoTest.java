package oneapp.incture.workbox.demo.zoho.services;

import static org.hamcrest.CoreMatchers.equalToObject;
import static org.hamcrest.CoreMatchers.nullValue;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.naming.java.javaURLContextFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import net.objecthunter.exp4j.ExpressionBuilder;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TableContentDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ValueListDto;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.sapAriba.util.SapAribaConstant;
import oneapp.incture.workbox.demo.zoho.util.AccessTokenZoho;
import oneapp.incture.workbox.demo.zoho.util.RestUserZoho;
import oneapp.incture.workbox.demo.zoho.util.ZohoConstants;


class Obj{
	Boolean IsActive;
	String dataType;
	String processName;

	public Boolean getIsActive() {
		return IsActive;
	}

	public void setIsActive(Boolean isActive) {
		IsActive = isActive;
	}
	

	public Obj(Boolean isActive, String dataType, String processName) {
		super();
		IsActive = isActive;
		this.dataType = dataType;
		this.processName = processName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Override
	public String toString() {
		return "Obj [IsActive=" + IsActive + ", dataType=" + dataType + ", processName=" + processName + "]";
	}

	public Obj(Boolean isActive) {
		super();
		IsActive = isActive;
	}

	
	
	
}

@Service
public class ZohoTest {

	// @Autowired
	AccessTokenZoho accessToken = new AccessTokenZoho();
	RestUserZoho restUserZoho = new RestUserZoho();

	@Autowired
	SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	
	public List<CustomAttributeTemplateDto> getTaskCustomAttributesByTemplateId(String templateId) {

		List<CustomAttributeTemplateDto> cusAttrList = null;
		CustomAttributeTemplateDto taskCusAttr = null;
		List<ValueListDto> valueList = null;
		List<TableContentDto> tableContentDtos = null;
		try {
			String attributeDetail = "select cat.PROCESS_NAME,cat.KEY,cat.LABEL,cat.IS_ACTIVE,cat.IS_MAND,cat.IS_EDITABLE "
					+ ",cat.DATA_TYPE,cat.ATTR_DES,cat.origin,cat.attribute_path,cat.dependant_on , cat.IS_RUN_TIME from"
					+ " CUSTOM_ATTR_TEMPLATE as cat " + "where cat.PROCESS_NAME = '" + templateId + "'"
					+ " and (cat.IS_DELETED= 0) and (cat.IS_VISIBLE=1) ";
			Query attrDetailQuery = getSession().createSQLQuery(attributeDetail);

			List<Object[]> custAttr = attrDetailQuery.list();
			if (!custAttr.isEmpty()) {
				cusAttrList = new ArrayList<>();
				for (Object[] obj : custAttr) {
					taskCusAttr = new CustomAttributeTemplateDto();
					taskCusAttr.setProcessName(obj[0].toString());
					taskCusAttr.setKey(obj[1].toString());
					taskCusAttr.setLabel(obj[2].toString());

					if (!ServicesUtil.isEmpty(obj[3]))
						taskCusAttr.setIsActive(ServicesUtil.asBoolean(obj[3]));
					if (!ServicesUtil.isEmpty(obj[4]))
						taskCusAttr.setIsMandatory(ServicesUtil.asBoolean(obj[4]));
					if (!ServicesUtil.isEmpty(obj[5]))
						taskCusAttr.setIsEditable(ServicesUtil.asBoolean(obj[5]));
					taskCusAttr.setDataType(obj[6].toString());
					if (!ServicesUtil.isEmpty(obj[7]))
						taskCusAttr.setAttrDes(obj[7].toString());
					valueList = new ArrayList<ValueListDto>();
					taskCusAttr.setValueList(valueList);
					if (!ServicesUtil.isEmpty(obj[11]))
						taskCusAttr.setIsRunTime(ServicesUtil.asBoolean(obj[11]));
					else
						taskCusAttr.setIsRunTime(false);
					taskCusAttr.setDataTypeKey(obj[6].equals(TaskCreationConstant.DROPDOWN) && taskCusAttr.getIsRunTime() ? 1 : 0);
					taskCusAttr.setOrigin(ServicesUtil.isEmpty(obj[8]) ? "" : obj[8].toString());
					taskCusAttr.setAttributePath(ServicesUtil.isEmpty(obj[9]) ? null : obj[9].toString());
					taskCusAttr.setDependantOn(ServicesUtil.isEmpty(obj[10]) ? null : obj[10].toString());

					taskCusAttr.setValue("");

					if (taskCusAttr.getDataType().equals(TaskCreationConstant.TABLE)) {
						TableContentDto tableContentDto = new TableContentDto();
						tableContentDtos = new ArrayList<>();
						tableContentDto.setTableAttributes(getTaskCustomAttributesByTemplateId(taskCusAttr.getKey()));
						tableContentDtos.add(tableContentDto);
						taskCusAttr.setTableContents(tableContentDtos);
						//System.err.println(new Gson().toJson(taskCusAttr));
					}

					cusAttrList.add(taskCusAttr);
				}
				System.err.println("Task level custom attributes : " + new Gson().toJson(cusAttrList));
				return cusAttrList;
			} else
				return new ArrayList<>();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WOKRBOX][ATTRIBUTE FETCHING] ERROR: " + e.getMessage());
			return new ArrayList<>();
		}

	}
	
	
	public static void testExp()  {
		
		List<Obj> lists = new ArrayList<>();
		lists.add(new Obj(true , "INPUT1" , "CourseRegistration"));
		lists.add(new Obj(false , "INPUT2" , "STANDARD"));
		lists.add(new Obj(true , "INPUT3" , "CourseRegistration"));
		lists.add(new Obj(true , "INPUT4" , "CourseRegistration"));
		
		lists = lists.stream().filter(a -> a.getIsActive().equals(true)).limit(3).collect(Collectors.toList());
		
		System.err.println(lists);
		
//		strings.add("sdf");
//		strings.add("sdf");
//		strings.add("sdf");
//		strings.add("sdf");
		
		
		
//		String expString = "${ig49h8dbh84a} '+' ${db9589jie1ege} '+' ${8i89a9h48c1j} '+' ${75i6g2b5e4b} '+' ${2a276hgihbbc4} ";
//		Map<String, String> sMap = new HashMap<>();
//		sMap.put("ig49h8dbh84a" ,"1");
//		sMap.put("db9589jie1ege" ,"");
//		sMap.put("8i89a9h48c1j" ,"1");
//		sMap.put("75i6g2b5e4b" ,"10");
//		sMap.put("2a276hgihbbc4" ,"");
//		
//		int flag = 0;
//		String key = "";
//		String finalExpString = ""; 
//		for(int i=0; i < expString.length();i++) {
//			char ch = expString.charAt(i);
//			if(ch == '{') {
//				flag = 1;
//			}
//				
//			if(flag == 1 & ch != '{' && ch != '}') {
//				key += ch;
//			}
//			if(ch== '}') {
//				System.err.println("Key: " + key);
//				String value = sMap.get(key);
//				if(value.equals(""))
//					value = "0";
//				System.err.println("Value : " + value);
//				finalExpString += value;
//				flag = 0;
//				key = "";
//				
//			}
//			
//			if(ch == '\'') {
//				char next = expString.charAt(i+1);
//				if(next != ' ') {
//					System.err.println("Found");
//					finalExpString += next;
//				}
//				
//			}
//		}
//		int j = 0;
//		while(j < expString.length()) {
//			char ch = expString.charAt(j);
//			char next = expString.charAt(j+1);
//			if(ch == '{') {
//				while(ch != '}') {		
//					key += ch;
//					j++;
//					ch = expString.charAt(j);
//				}
//				String value = sMap.get(key.substring(1 ,key.length()));
//				if(value.equals(""))
//					value = "0";
//				finalExpString += value;
//				key = "";
//			}
//			if(ch == '\'' && next != ' ') {
//				finalExpString += next;
//			}
//			j++;
//		}
//		
//		System.err.println("FInal exp : " + finalExpString);
//		System.err.println(String.valueOf(new ExpressionBuilder(finalExpString).build().evaluate()));
		
	}

	public static void main(String[] args) throws Exception {
		
//		List<String> strings = new ArrayList<>();
//		for(int i =0; i < 10; i++) {
//			testExp(strings);
//		}
		testExp();
//		System.err.println(strings);

//		String accesToken = new Test().getTokenForAriba();
//		RestResponse response = new Test().callGetService(
//				"https://eu.openapi.ariba.com/api/sourcing-approval/v2/prod/usergroups/BS PROD MANAGER/members?realm=foulath-T"
//						.replace(" ", "%20"),
//				accesToken, new HashMap<>());
//		new Test().

//		System.err.println(new Test().convertNumberToWord("1.0"));

		// System.err.println(new Test().setBalenceLeave("Casual Leave"));

		// new Test().setAll("leave");
//		 new Test().getData();
//		Date date = new SimpleDateFormat("dd-MMM-yyyy HH:mm:s").parse("18-Dec-2020 18:33:33");
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
//		Date date = simpleDateFormat.format(date);
//		System.err.println(date);
		// System.err.println(new Date(1608317737000l));
		// System.err.println(new Date(1608317737000l));
	}

	public RestResponse callGetService(String requestUrl, String accessToken, Map<String, String> parameters) {

		RestResponse restResponse = null;
		HttpResponse response = null;
		HttpClient client = null;

		try {
			client = new DefaultHttpClient();
			URIBuilder uriBuilder = new URIBuilder(requestUrl);
			restResponse = new RestResponse();

			// Setting parameters in the URL
			for (Entry<String, String> parameter : parameters.entrySet()) {
				System.err.println(parameter.getKey() + " " + parameter.getValue());
				uriBuilder.setParameter(parameter.getKey(), parameter.getValue());
			}

			// Calling the get service
			System.err.println(uriBuilder.build());
			HttpGet request = new HttpGet(uriBuilder.build());
			request.addHeader("Authorization", "Bearer " + accessToken);
			request.addHeader("apiKey", SapAribaConstant.API_KEY);
			request.addHeader("Content-Type", SapAribaConstant.JSON);
			response = client.execute(request);
			System.err.println(response);

			HttpEntity entity = response.getEntity();
			String data = EntityUtils.toString(entity);
			restResponse.setResponseObject(data);
			restResponse.setResponseCode(response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			System.err.println("[WBP-Dev]RestUserZoho.callGetService() : " + e.getMessage());
		}
		return restResponse;
	}

	private String getTokenForAriba() {

		try {
			String base64 = SapAribaConstant.OAUTH_CLIENT_ID + ":" + SapAribaConstant.OAUTH_SECRET;

			String tokenResponse = null;

			DefaultHttpClient client = new DefaultHttpClient();
			HttpParams params = client.getParams();
			HttpClientParams.setCookiePolicy(params, CookiePolicy.RFC_2109);
			params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 30000);

			HttpPost oauthPost = new HttpPost(SapAribaConstant.OAUTH_URL);

			List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();

			parametersBody.add(new BasicNameValuePair("grant_type", SapAribaConstant.GRANT_TYPE));

			oauthPost.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));
			oauthPost.setHeader("Content-Type", SapAribaConstant.CONTENT_TYPE);
			oauthPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(base64.getBytes()));
			HttpResponse response = client.execute(oauthPost);
			int code = response.getStatusLine().getStatusCode();

			System.err.println("[WORKBOX- SapAriba][ResponseCode][Approve]" + code);

			if (code == 200)
				tokenResponse = EntityUtils.toString(response.getEntity());
			else {
				return SapAribaConstant.FAILURE;
			}

			JSONObject myObject = new JSONObject(tokenResponse);
			String accessToken = myObject.getString("access_token");
			System.err.println(accessToken);
			return accessToken;
		} catch (Exception e) {
			System.out.println("ParseSapAriba.enclosing_method()" + e);
		}
		return null;
	}

	public String setBalenceLeave(String leaveTypeName) {
		String AccessTokenString = accessToken.getAcessToken();
		System.err.println(AccessTokenString);

		Map<String, String> parameters = new HashMap<>();
		parameters.put("userId", "41885000000186005");
		RestResponse processReponse = restUserZoho.callGetService(ZohoConstants.baseUrl + "leave/getLeaveTypeDetails",
				AccessTokenString, parameters);
		JSONObject leaveTypeObject = new JSONObject((String) processReponse.getResponseObject());
		JSONArray leaveTypeArray = (leaveTypeObject.getJSONObject("response").getJSONArray("result"));
		String leaveBalence = "0";

		for (Object object : leaveTypeArray) {
			String name = ((JSONObject) object).getString("Name");
			if (name.equals(leaveTypeName)) {
				leaveBalence = (((JSONObject) object).getString("BalanceCount"));
				break;
			}
		}
		return leaveBalence;
	}

	public String convertNumberToWord(String number) {
		System.err.println(number);
		String[] numberDecimals = number.split("[.]");
		Map<String, String> decimals = new HashMap<>();
		decimals.put("5", "Half");
		decimals.put("25", "Quarter");
		decimals.put("0", "");
		String result = numberDecimals[0];

		if (Integer.parseInt(numberDecimals[1]) != 0) {
			result += " And ";
		}

		result += decimals.get(numberDecimals[1]) + " Day";

		if (Integer.parseInt(numberDecimals[0]) > 1
				|| (Integer.parseInt(numberDecimals[0]) == 1 && Integer.parseInt(numberDecimals[1]) != 0)) {
			result += "s";
		}

		return result;
	}

	public ResponseMessage serverStatus(String serverUrl) {

		ResponseMessage resp = new ResponseMessage();
		resp.setMessage("FAILURE");
		System.err.println(resp);
		try {
//			InetAddress address = InetAddress.getByName(serverUrl);
//			System.err.println(address);
//			System.out.println("Name: " + address.getHostName());
//			System.out.println("Addr: " + address.getHostAddress());
//			System.out.println("Reach: " + address.isReachable(3000));
//			resp.setMessage("SUCCESS");

			String hostname = "https://productbnqthp5nvp.hana.ondemand.com/workbox-product/workbox/detailPage/dynamicDetails?taskId=41885000000203001-3";
			URL url = new URL(hostname);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			System.err.println(con.getResponseCode());

		} catch (Exception e) {
			System.err.println(e);
		}
		return resp;

	}

	public void printAcessToken() {
		String AccessTokenString = accessToken.getAcessToken();
		System.err.println(AccessTokenString);
	}

	public void test() {

		String AccessTokenString = accessToken.getAcessToken();
		System.err.println(AccessTokenString);

		RestResponse processReponse = restUserZoho.callGetService(ZohoConstants.baseUrl + "forms/leave/getRecords",
				AccessTokenString, new HashMap<String, String>());
		JSONObject jsonObject = new JSONObject((String) processReponse.getResponseObject());
		JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("result");
		List<ProcessEventsDto> processEventsDtos = new ArrayList<>();
		Map<String, UserIDPMappingDto> userList = makeUsers();
		// System.err.println(jsonArray.key);

		for (Object object : jsonArray) {
			String recordId = ((JSONObject) object).keys().next();
			System.err.println(((JSONObject) object).getJSONArray(recordId).getJSONObject(0));

			ProcessEventsDto processEventsDto = new ProcessEventsDto();
			JSONObject processObject = ((JSONObject) object).getJSONArray(recordId).getJSONObject(0);
			processEventsDto.setProcessId(recordId);
			processEventsDto.setRequestId(recordId);
			processEventsDto.setName("Leave");
			processEventsDto.setSubject("Zoho");
			processEventsDto.setStatus("RUNNING");
			if (processObject.getString("ApprovalStatus").equals("Approved")
					|| processObject.getString("ApprovalStatus").equals("Rejected")) {
				processEventsDto.setStatus("COMPLETED");
			}
			processEventsDto.setStartedBy(userList.get(processObject.getString("Employee_ID.ID")).getUserId());
			processEventsDto
					.setStartedAt(ServicesUtil.convertFromStringToDateZoho(processObject.getString("AddedTime")));
			processEventsDto.setStartedByUser(userList.get(processObject.getString("Employee_ID.ID")).getUserId());
			processEventsDto
					.setStartedByDisplayName(userList.get(processObject.getString("Employee_ID.ID")).getUserFirstName()
							+ " " + userList.get(processObject.getString("Employee_ID.ID")).getUserLastName());
			processEventsDto.setProcessDisplayName("Leave");
			processEventsDto.setProcessDefinitionId(recordId);
			processEventsDtos.add(processEventsDto);

		}

		System.err.println(new Gson().toJson(processEventsDtos));

	}

	public void callMethod() {
		// String accesTokenString = accessToken.getAcessToken();
		// Map<String, String> parameters = new HashMap<>();
		// parameters.put("approvalStatus", "pending");
		// System.err.println(accesTokenString);
		// RestResponse response =
		// (restUserZoho.callGetService("https://people.zoho.in/people/api/forms/P_ApplyLeaveView/records",
		// accesTokenString, parameters));
		//
		// JSONArray jsonArray = new JSONArray((String
		// )response.getResponseObject());
		// System.err.println(jsonArray);

		// String AccessTokenString = accessToken.getAcessToken();
		// Map<String, String> parameters = new HashMap<String, String>();
		// RestResponse response =
		// restUserZoho.callGetService(ZohoConstants.baseUrl +
		// "forms/P_ApplyLeaveView/records", AccessTokenString, parameters);
		// System.err.println(response.getResponseObject());
		// JSONArray processArray = new
		// JSONArray((String)response.getResponseObject());
		// List<ProcessEventsDto> processEventsDtos = new ArrayList<>();
		//
		// for(Object process : processArray){
		// ProcessEventsDto processEventsDto = new ProcessEventsDto();
		// JSONObject processObject = (JSONObject) process;
		// processEventsDto.setProcessId(processObject.getString("recordId"));
		// processEventsDto.setRequestId(processObject.getString("recordId"));
		// processEventsDto.setName("Leave");
		// processEventsDto.setSubject("Zoho");
		// processEventsDto.setStatus("RUNNING");
		// if(processObject.getString("ApprovalStatus").equals("Approved")){
		// processEventsDto.setStatus("COMPLETED");
		// }
		// processEventsDto.setStartedBy(processObject.getString("ownerName"));
		// Date createdAt = new
		// Date(Long.parseLong(processObject.getString("createdTime")));
		// processEventsDto.setStartedAt(createdAt);
		// processEventsDto.setStartedByUser(processObject.getString("ownerID"));
		// processEventsDto.setStartedByDisplayName(processObject.getString("ownerName"));
		// processEventsDto.setProcessDisplayName("Leave");
		// processEventsDto.setProcessDefinitionId(processObject.getString("recordId"));
		//
		// processEventsDtos.add(processEventsDto);
		//
		//
		// }
		//
		// System.err.println(processEventsDtos);

		String AccessTokenString = accessToken.getAcessToken();
		System.err.println(AccessTokenString);
		RestResponse processReponse = restUserZoho.callGetService(
				ZohoConstants.baseUrl + "forms/P_ApplyLeaveView/records", AccessTokenString,
				new HashMap<String, String>());
		JSONArray processArray = new JSONArray((String) processReponse.getResponseObject());
		List<TaskEventsDto> taskEventsDtos = new ArrayList<>();
		// Map<String, UserIDPMappingDto> userList = fetchUsers(0);

		for (Object leaveObject : processArray) {

			String recordId = ((JSONObject) leaveObject).getString("recordId");
			Date createdAt = new Date(Long.parseLong(((JSONObject) leaveObject).getString("createdTime")));
			Date modifiedTime = new Date(Long.parseLong(((JSONObject) leaveObject).getString("modifiedTime")));
			String ownerId = ((JSONObject) leaveObject).getString("ownerID");
			Map<String, String> taskParameters = new HashMap<>();
			taskParameters.put("recordId", recordId);
			taskParameters.put("formLinkName", "leave");
			RestResponse taskResponse = restUserZoho.callGetService(ZohoConstants.baseUrl + "getApprovalDetails",
					AccessTokenString, taskParameters);
			JSONObject approvalObject = new JSONObject((String) taskResponse.getResponseObject());
			approvalObject = approvalObject.getJSONObject("response").getJSONArray("result").getJSONObject(0);
			JSONArray processedApprovalObjectArray = approvalObject.getJSONArray("processed");
			// System.err.println(approvalObject);
			int pendingApprovalLevel;
			if (approvalObject.getInt("currentApprovalStatus") == -1) {
				pendingApprovalLevel = approvalObject.getInt("pendingApprovalLevel");
				JSONObject currentApprovalObject = approvalObject.getJSONObject("current");
				// System.err.println(currentApprovalObject);

				TaskEventsDto taskEventsDto = new TaskEventsDto();
				taskEventsDto.setEventId(recordId + String.valueOf(pendingApprovalLevel));
				taskEventsDto.setRequestId(recordId);
				taskEventsDto.setProcessId(recordId);
				taskEventsDto.setSubject("Zoho");
				taskEventsDto.setDescription("Approval");
				taskEventsDto.setName("Approval");
				taskEventsDto.setStatus("READY");
				taskEventsDto.setCreatedAt(createdAt);
				taskEventsDto.setCompletionDeadLine(new Date(createdAt.getTime() + (1000 * 60 * 60 * 24 * 5)));
				taskEventsDto.setOrigin("Zoho");
				taskEventsDto.setSlaDueDate(new Date(createdAt.getTime() + (1000 * 60 * 60 * 24 * 5)));
				taskEventsDto.setPriority("MEDIUM");
				taskEventsDto.setOwnersName(currentApprovalObject.getString("name")); // needs
																						// to
																						// be
																						// changed
																						// using
																						// user_idp_mapping
				taskEventsDto.setProcessName("Approval");
				taskEventsDto.setUpdatedAt(modifiedTime);
				taskEventsDto.setCreatedBy(ownerId);
				taskEventsDtos.add(taskEventsDto);
			} else {
				pendingApprovalLevel = processedApprovalObjectArray.length() * 2;
			}

			int taskIdIndex = pendingApprovalLevel - processedApprovalObjectArray.length() + 1;
			// System.err.println(taskIdIndex);
			for (Object processedApprovalObject : processedApprovalObjectArray) {
				TaskEventsDto taskEventsDto = new TaskEventsDto();
				JSONObject approvarObject = ((JSONObject) processedApprovalObject).optJSONObject("approverDtls");
				// System.err.println(approvarObject);
				if (approvarObject != null) {
					approvarObject = ((JSONObject) processedApprovalObject).getJSONObject("approverDtls");
				}
				// System.err.println("in for loop");
				// System.err.println(approvarObject);
				taskEventsDto.setEventId(recordId + String.valueOf(taskIdIndex));
				taskEventsDto.setRequestId(recordId);
				taskEventsDto.setProcessId(recordId);
				taskEventsDto.setSubject("Zoho");
				taskEventsDto.setDescription("Approval");
				taskEventsDto.setName("Approval");
				taskEventsDto.setStatus("COMPLETED");
				taskEventsDto.setCreatedAt(createdAt);
				taskEventsDto.setCompletionDeadLine(new Date(createdAt.getTime() + (1000 * 60 * 60 * 24 * 5)));
				taskEventsDto.setOrigin("Zoho");
				taskEventsDto.setSlaDueDate(new Date(createdAt.getTime() + (1000 * 60 * 60 * 24 * 5)));
				taskEventsDto.setPriority("MEDIUM");
				taskEventsDto.setOwnersName(((JSONObject) processedApprovalObject).getString("name")); // needs
																										// to
																										// be
																										// changed
																										// using
																										// user_idp_mapping
				taskEventsDto.setProcessName("Approval");
				taskEventsDto.setUpdatedAt(modifiedTime);
				taskEventsDto.setCreatedBy(ownerId);
				taskEventsDtos.add(taskEventsDto);
				taskEventsDto.setCurrentProcessor(approvarObject.getString("erecno"));
				taskEventsDto.setCurrentProcessorDisplayName(approvarObject.getString("name"));
				taskIdIndex++;
			}
		}

		// String AccessTokenString = accessToken.getAcessToken();
		// System.err.println(AccessTokenString);
		// RestResponse processReponse =
		// restUserZoho.callGetService(ZohoConstants.baseUrl +
		// "forms/P_ApplyLeaveView/records",
		// AccessTokenString, new HashMap<String, String>());
		// JSONArray processArray = new JSONArray((String)
		// processReponse.getResponseObject());
		// List<TaskEventsDto> taskEventsDtos = new ArrayList<>();
		// Map<String, UserIDPMappingDto> userList = fetchUsers(0);
		List<TaskOwnersDto> taskOwnersDtos = new ArrayList<>();

		// for (Object leaveObject : processArray) {
		//
		// String recordId = ((JSONObject) leaveObject).getString("recordId");
		// Date createdAt = new Date(Long.parseLong(((JSONObject)
		// leaveObject).getString("createdTime")));
		// Date modifiedTime = new Date(Long.parseLong(((JSONObject)
		// leaveObject).getString("modifiedTime")));
		// String ownerId = ((JSONObject) leaveObject).getString("ownerID");
		// Map<String, String> taskParameters = new HashMap<>();
		// taskParameters.put("recordId", recordId);
		// taskParameters.put("formLinkName", "leave");
		// RestResponse taskResponse =
		// restUserZoho.callGetService(ZohoConstants.baseUrl +
		// "getApprovalDetails",
		// AccessTokenString, taskParameters);
		// JSONObject approvalObject = new
		// JSONObject((String)taskResponse.getResponseObject());
		// approvalObject =
		// approvalObject.getJSONObject("response").getJSONArray("result").getJSONObject(0);
		// System.err.println(approvalObject);
		// int pendingApprovalLevel;
		// if(approvalObject.getInt("currentApprovalStatus") == -1){
		// pendingApprovalLevel = approvalObject.getInt("pendingApprovalLevel");
		// JSONObject currentApprovalObject =
		// approvalObject.getJSONObject("current");
		//
		// TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
		// taskOwnersDto.setEventId(recordId +
		// String.valueOf(pendingApprovalLevel));
		// taskOwnersDto.setTaskOwner(currentApprovalObject.getString("name"));
		// taskOwnersDto.setTaskOwnerDisplayName(currentApprovalObject.getString("name"));
		// taskOwnersDto.setOwnerEmail(currentApprovalObject.getString("email"));
		//
		// taskOwnersDtos.add(taskOwnersDto);
		//
		//// TaskEventsDto taskEventsDto = new TaskEventsDto();
		//// taskEventsDto.setEventId(recordId +
		// String.valueOf(pendingApprovalLevel));
		//// taskEventsDto.setRequestId(recordId);
		//// taskEventsDto.setProcessId(recordId);
		//// taskEventsDto.setSubject("Zoho");
		//// taskEventsDto.setDescription("Approval");
		//// taskEventsDto.setName("Approval");
		//// taskEventsDto.setStatus("READY");
		//// taskEventsDto.setCreatedAt(createdAt);
		//// taskEventsDto.setCompletionDeadLine(new Date(createdAt.getTime() +
		// (1000 * 60 * 60 * 24 * 5)));
		//// taskEventsDto.setOrigin("Zoho");
		//// taskEventsDto.setSlaDueDate(new Date(createdAt.getTime() + (1000 *
		// 60 * 60 * 24 * 5)));
		//// taskEventsDto.setPriority("MEDIUM");
		//// taskEventsDto.setOwnersName(currentApprovalObject.getString("name"));
		// // needs to be changed using user_idp_mapping
		//// taskEventsDto.setProcessName("Approval");
		//// taskEventsDto.setUpdatedAt(modifiedTime);
		//// taskEventsDto.setCreatedBy(ownerId);
		//// taskEventsDtos.add(taskEventsDto);
		// }
		// }

		// System.err.println(taskOwnersDtos);
		// System.err.println(new Gson().toJson(taskEventsDtos));

	}

	public void getData() {

		try {
			HttpGet httpGet = new HttpGet("https://people.zoho.in/people/api/forms/leave/views");
			String accessToken = "1000.55d86f3c146cd9473e834711fb4f3ec2.b039f5be7f0d5f1cb9a0578aa46dc5b6";
			httpGet.setHeader("Authorization", "Bearer " + accessToken);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(httpGet);
			String jsonString = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(jsonString);
			// System.err.println(jsonObject);
		} catch (Exception e) {
			System.err.println("Error");
		}

	}

	public String getUserbyZohoEmail(String email) {

		Map<String, String> userParameters = new HashMap<>();
		userParameters.put("searchColumn", "EMPLOYEEMAILALIAS");
		userParameters.put("searchValue", email);
		String AccessTokenString = accessToken.getAcessToken();
		// System.err.println(AccessTokenString);
		RestResponse taskResponse = restUserZoho.callGetService(ZohoConstants.baseUrl + "forms/P_EmployeeView/records",
				AccessTokenString, userParameters);
		JSONObject userObject = new JSONArray((String) taskResponse.getResponseObject()).getJSONObject(0);
		return userObject.getString("ownerID");
	}

	public List<TaskEventsDto> setTask(ProcessEventsDto processEventsDto, JSONObject approvalObject,
			Map<String, UserIDPMappingDto> userList) {

		JSONArray processedApprovalObjectArray = approvalObject.getJSONArray("processed");
		List<TaskEventsDto> taskEventsDtos = new ArrayList<>();

		int pendingApprovalLevel = 1;
		String processOwnerId = approvalObject.getJSONObject("recordowner").getString("erecno");
		String taskOwnerId = null;
		String currentProcessorId = null;
		Date completedAt = processEventsDto.getStartedAt();

		for (Object processedApprovalObject : processedApprovalObjectArray) {

			TaskEventsDto taskEventsDto = new TaskEventsDto();
			currentProcessorId = ((JSONObject) processedApprovalObject).getString("erecno");
//			taskOwnerId = ((JSONObject) processedApprovalObject).getString("erecno");
//			if (((JSONObject) processedApprovalObject).optJSONObject("approverDtls") != null) {
//				taskOwnerId = ((JSONObject) processedApprovalObject).optJSONObject("approverDtls").getString("erecno");
//			}

			taskEventsDto.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
			taskEventsDto.setRequestId(processEventsDto.getProcessId());
			taskEventsDto.setProcessId(processEventsDto.getProcessId());
			taskEventsDto.setSubject("Zoho-Leave approval request");
			taskEventsDto.setDescription("Approval");
			taskEventsDto.setName("Approval");
			taskEventsDto.setStatus("COMPLETED");
			taskEventsDto.setCompletionDeadLine(
					new Date((processEventsDto.getStartedAt().getTime() + (1000 * 60 * 60 * 24 * 5))));
			taskEventsDto.setOrigin("Zoho");
			taskEventsDto
					.setSlaDueDate(new Date(processEventsDto.getStartedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
			taskEventsDto.setPriority("MEDIUM");
//			taskEventsDto.setOwnersName(
//					userList.get(taskOwnerId).getUserFirstName() + " " + userList.get(taskOwnerId).getUserLastName());
			taskEventsDto.setProcessName("Approval");
			taskEventsDto.setUpdatedAt(processEventsDto.getStartedAt());
			taskEventsDto.setCreatedBy(userList.get(processOwnerId).getUserFirstName() + " "
					+ userList.get(processOwnerId).getUserLastName());
			taskEventsDto.setCurrentProcessor(userList.get(currentProcessorId).getUserId());
			taskEventsDto.setCurrentProcessorDisplayName(userList.get(currentProcessorId).getUserFirstName() + " "
					+ userList.get(currentProcessorId).getUserLastName());
			taskEventsDto.setCompletedAt(ServicesUtil
					.convertCompletedDateZoho(((JSONObject) processedApprovalObject).getString("approvedTime")));
			taskEventsDto.setCreatedAt(completedAt);
			taskEventsDtos.add(taskEventsDto);

			System.err.println(completedAt);
			completedAt = ServicesUtil
					.convertCompletedDateZoho(((JSONObject) processedApprovalObject).getString("approvedTime"));
			System.err.println(completedAt);
			pendingApprovalLevel++;
		}

		if (approvalObject.getInt("currentApprovalStatus") == -1) {
			// pendingApprovalLevel =
			// approvalObject.getInt("pendingApprovalLevel");
			JSONObject currentApprovalObject = approvalObject.getJSONObject("current");
			// ownerId = ((JSONObject)
			// currentApprovalObject).getString("erecno");
			// System.err.println(currentApprovalObject);
			// taskOwnerId = getUserbyZohoEmail(currentApprovalObject.getString("email"));
			TaskEventsDto taskEventsDto = new TaskEventsDto();
			taskEventsDto.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
			taskEventsDto.setRequestId(processEventsDto.getProcessId());
			taskEventsDto.setProcessId(processEventsDto.getProcessId());
			taskEventsDto.setSubject("Zoho-Leave approval request");
			taskEventsDto.setDescription("Approval");
			taskEventsDto.setName("Approval");
			taskEventsDto.setStatus("READY");

			if (currentApprovalObject.optJSONObject("forward") != null) {
				taskEventsDto.setStatus("RESERVED");
				currentProcessorId = currentApprovalObject.getJSONObject("forward").getJSONObject("target")
						.getString("erecno");
				taskEventsDto.setCurrentProcessor(userList.get(currentProcessorId).getUserId());
				taskEventsDto.setCurrentProcessorDisplayName(userList.get(currentProcessorId).getUserFirstName() + " "
						+ userList.get(currentProcessorId).getUserLastName());
			}

			taskEventsDto.setCreatedAt(completedAt);
			taskEventsDto.setCompletionDeadLine(
					new Date((processEventsDto.getStartedAt().getTime() + (1000 * 60 * 60 * 24 * 5))));
			taskEventsDto.setOrigin("Zoho");
			taskEventsDto
					.setSlaDueDate(new Date(processEventsDto.getStartedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
			taskEventsDto.setPriority("MEDIUM");
			// taskEventsDto.setOwnersName(
			// userList.get(taskOwnerId).getUserFirstName() + " " +
			// userList.get(taskOwnerId).getUserLastName());
			taskEventsDto.setProcessName("Approval");
			taskEventsDto.setUpdatedAt(processEventsDto.getStartedAt());
			taskEventsDto.setCreatedBy(userList.get(processOwnerId).getUserFirstName() + " "
					+ userList.get(processOwnerId).getUserLastName());
			taskEventsDtos.add(taskEventsDto);
		}
		System.err.println(pendingApprovalLevel);

		// System.err.println(taskEventsDtos);
		// System.err.println(new Gson().toJson(taskEventsDtos));
		return taskEventsDtos;

	}

	public List<TaskOwnersDto> setOwner(ProcessEventsDto processEventsDto, JSONObject approvalObject,
			Map<String, UserIDPMappingDto> userList) {

		System.err.println("In setOwner");
		JSONArray processedApprovalObjectArray = approvalObject.getJSONArray("processed");
		List<TaskOwnersDto> taskOwnersDtos = new ArrayList<>();
		int pendingApprovalLevel = 1;

		for (Object processedApprovalObject : processedApprovalObjectArray) {

//			String currentProcessorId = ((JSONObject) processedApprovalObject).getString("erecno");
//			if (((JSONObject) processedApprovalObject).optJSONObject("approverDtls") != null) {
//				currentProcessorId = ((JSONObject) processedApprovalObject).optJSONObject("approverDtls")
//						.getString("erecno");
//			}
//			TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
//			taskOwnersDto.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
//			taskOwnersDto.setTaskOwner(userList.get(currentProcessorId).getUserId());
//			taskOwnersDto.setTaskOwnerDisplayName(userList.get(currentProcessorId).getUserFirstName() + " "
//					+ userList.get(currentProcessorId).getUserLastName());
//			taskOwnersDto.setOwnerEmail(userList.get(currentProcessorId).getUserEmail());
//			taskOwnersDtos.add(taskOwnersDto);

			// setting owners based on the role
			if (((JSONObject) processedApprovalObject).optJSONObject("approverDtls") != null) {
				String[] userEmails = ((JSONObject) processedApprovalObject).getJSONObject("approverDtls")
						.getString("email").split(",");
				for (String userEmail : userEmails) {
					String userRecordId = getUserbyZohoEmail(userEmail);

					TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
					taskOwnersDto
							.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
					taskOwnersDto.setTaskOwner(userList.get(userRecordId).getUserId());
					taskOwnersDto.setTaskOwnerDisplayName(userList.get(userRecordId).getUserFirstName() + " "
							+ userList.get(userRecordId).getUserLastName());
					taskOwnersDto.setOwnerEmail(userList.get(userRecordId).getUserEmail());
					taskOwnersDtos.add(taskOwnersDto);
				}
			} else {
				String userEmail = ((JSONObject) processedApprovalObject).getString("email");
				String userRecordId = getUserbyZohoEmail(userEmail);

				TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
				taskOwnersDto.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
				taskOwnersDto.setTaskOwner(userList.get(userRecordId).getUserId());
				taskOwnersDto.setTaskOwnerDisplayName(userList.get(userRecordId).getUserFirstName() + " "
						+ userList.get(userRecordId).getUserLastName());
				taskOwnersDto.setOwnerEmail(userList.get(userRecordId).getUserEmail());
				taskOwnersDtos.add(taskOwnersDto);
			}

			pendingApprovalLevel++;
		}

		if (approvalObject.getInt("currentApprovalStatus") == -1) {

			JSONObject currentApprovalObject = approvalObject.getJSONObject("current");

			String[] userEmails = currentApprovalObject.getString("email").split(",");
			for (String userEmail : userEmails) {
				String userRecordId = getUserbyZohoEmail(userEmail);

				TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
				taskOwnersDto.setEventId(processEventsDto.getProcessId() + "-" + String.valueOf(pendingApprovalLevel));
				taskOwnersDto.setTaskOwner(userList.get(userRecordId).getUserId());
				taskOwnersDto.setTaskOwnerDisplayName(userList.get(userRecordId).getUserFirstName() + " "
						+ userList.get(userRecordId).getUserLastName());
				taskOwnersDto.setOwnerEmail(userList.get(userRecordId).getUserEmail());
				taskOwnersDtos.add(taskOwnersDto);
			}

			pendingApprovalLevel++;

		}

		return taskOwnersDtos;

	}

	public ResponseMessage setAll(String processName) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Something went wrong");
		responseMessage.setStatus("Failure");
		responseMessage.setStatusCode("1");

		try {

			String accessTokenString = accessToken.getAcessToken();
			System.err.println(accessTokenString);
			// Map<String, UserIDPMappingDto> userList = fetchUsers(0);
			Map<String, UserIDPMappingDto> userList = makeUsers();
			// RestResponse response = restUserZoho.callGetService(
			// ZohoConstants.baseUrl + "forms/P_ApplyLeaveView/records",
			// accessTokenString,
			// new HashMap<String, String>());
			// JSONArray processArray = new JSONArray((String)
			// response.getResponseObject());
			// List<ProcessEventsDto> processEventsDtos = setProcess(userList,
			// accessTokenString, processArray);
			int processCount = 0;

			RestResponse processReponse = restUserZoho.callGetService(
					ZohoConstants.baseUrl + "forms/" + processName.toLowerCase() + "/getRecords", accessTokenString,
					new HashMap<String, String>());
			JSONObject processObject = new JSONObject((String) processReponse.getResponseObject());
			JSONArray processArray = processObject.getJSONObject("response").getJSONArray("result");
			List<ProcessEventsDto> processEventsDtos = setProcess(userList, accessTokenString, processArray);

			System.err.println(new Gson().toJson(processEventsDtos));

			for (ProcessEventsDto processEventsDto : processEventsDtos) {

				Map<String, String> taskParameters = new HashMap<>();
				taskParameters.put("recordId", processEventsDto.getProcessId());
				taskParameters.put("formLinkName", "leave");
				RestResponse taskResponse = restUserZoho.callGetService(ZohoConstants.baseUrl + "getApprovalDetails",
						accessTokenString, taskParameters);
				JSONObject approvalObject = new JSONObject((String) taskResponse.getResponseObject());
				approvalObject = approvalObject.getJSONObject("response").getJSONArray("result").getJSONObject(0);
				List<TaskEventsDto> taskEventsDtos = setTask(processEventsDto, approvalObject, userList);
				List<TaskOwnersDto> taskOwnersDtos = setOwner(processEventsDto, approvalObject, userList);
				List<CustomAttributeValue> customAttributeValues = setCustomAttributeValues(
						processArray.get(processCount), approvalObject, userList);
				processCount++;
				System.err.println(new Gson().toJson(taskEventsDtos));
				System.err.println(new Gson().toJson(taskOwnersDtos));
				System.err.println(new Gson().toJson(customAttributeValues));

				responseMessage.setMessage("Data Added Successfully");
				responseMessage.setStatus("Success");
				responseMessage.setStatusCode("200");
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ZOHO][setAll]Error" + e);
		}

		return responseMessage;
	}

	public List<CustomAttributeValue> setCustomAttributeValues(Object object, JSONObject approvalObject,
			Map<String, UserIDPMappingDto> userList) {

		String recordId = ((JSONObject) object).keys().next();
		JSONObject processObject = ((JSONObject) object).getJSONArray(recordId).getJSONObject(0);

		// JSONArray processArray = new JSONArray((String)
		// processObject.getResponseObject());
		processObject = (JSONObject) processObject;
		List<CustomAttributeValue> customAttributeValues = new ArrayList<>();
		String fromDate = ((JSONObject) processObject).getString("From");
		String toDate = ((JSONObject) processObject).getString("To");
		String leaveType = ((JSONObject) processObject).getString("Leavetype");
		String reasonForLeave = ((JSONObject) processObject).getString("Reasonforleave");
		JSONArray processedApprovalObjectArray = approvalObject.getJSONArray("processed");

		int pendingApprovalLevel = 1;
		CustomAttributeValue customAttributeValue = null;

		for (Object processedApprovalObject : processedApprovalObjectArray) {

			String comment = ((JSONObject) processedApprovalObject).getString("comment");
			customAttributeValue = new CustomAttributeValue();
			customAttributeValue.setKey("comment");
			customAttributeValue.setAttributeValue(comment);
			customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
			customAttributeValue.setProcessName("Leave");
			customAttributeValues.add(customAttributeValue);

			customAttributeValue = new CustomAttributeValue();
			customAttributeValue.setKey("From");
			customAttributeValue.setAttributeValue(fromDate);
			customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
			customAttributeValue.setProcessName("Leave");
			customAttributeValues.add(customAttributeValue);

			customAttributeValue = new CustomAttributeValue();
			customAttributeValue.setKey("To");
			customAttributeValue.setAttributeValue(toDate);
			customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
			customAttributeValue.setProcessName("Leave");
			customAttributeValues.add(customAttributeValue);

			customAttributeValue = new CustomAttributeValue();
			customAttributeValue.setKey("Leave Type");
			customAttributeValue.setAttributeValue(leaveType);
			customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
			customAttributeValue.setProcessName("Leave");
			customAttributeValues.add(customAttributeValue);

			customAttributeValue = new CustomAttributeValue();
			customAttributeValue.setKey("Reason For Leave");
			customAttributeValue.setAttributeValue(reasonForLeave);
			customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
			customAttributeValue.setProcessName("Leave");
			customAttributeValues.add(customAttributeValue);

			pendingApprovalLevel++;
		}

		if (approvalObject.getInt("currentApprovalStatus") == -1) {

			customAttributeValue = new CustomAttributeValue();
			customAttributeValue.setKey("From");
			customAttributeValue.setAttributeValue(fromDate);
			customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
			customAttributeValue.setProcessName("Leave");
			customAttributeValues.add(customAttributeValue);

			customAttributeValue = new CustomAttributeValue();
			customAttributeValue.setKey("To");
			customAttributeValue.setAttributeValue(toDate);
			customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
			customAttributeValue.setProcessName("Leave");
			customAttributeValues.add(customAttributeValue);

			customAttributeValue = new CustomAttributeValue();
			customAttributeValue.setKey("Leave Type");
			customAttributeValue.setAttributeValue(leaveType);
			customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
			customAttributeValue.setProcessName("Leave");
			customAttributeValues.add(customAttributeValue);

			customAttributeValue = new CustomAttributeValue();
			customAttributeValue.setKey("Reason For Leave");
			customAttributeValue.setAttributeValue(reasonForLeave);
			customAttributeValue.setTaskId(recordId + "-" + String.valueOf(pendingApprovalLevel));
			customAttributeValue.setProcessName("Leave");
			customAttributeValues.add(customAttributeValue);

		}

		return customAttributeValues;
	}

	public List<ProcessEventsDto> setProcess(Map<String, UserIDPMappingDto> userList, String accessTokenString,
			JSONArray processArray) {

		System.err.println("In setProcess function");
		List<ProcessEventsDto> processEventsDtos = new ArrayList<>();
		for (Object process : processArray) {
			String recordId = ((JSONObject) process).keys().next();
			System.err.println(recordId);

			ProcessEventsDto processEventsDto = new ProcessEventsDto();
			JSONObject processObject = ((JSONObject) process).getJSONArray(recordId).getJSONObject(0);
			System.err.println(processObject);
			processEventsDto.setProcessId(recordId);
			processEventsDto.setRequestId(recordId);
			processEventsDto.setName("Leave");
			processEventsDto.setSubject("Zoho-Leave approval request");
			processEventsDto.setStatus("RUNNING");
			if (processObject.getString("ApprovalStatus").equals("Approved")
					|| processObject.getString("ApprovalStatus").equals("Rejected")) {
				processEventsDto.setStatus("COMPLETED");
			}
			processEventsDto.setStartedBy(userList.get(processObject.getString("Employee_ID.ID")).getUserId());
			processEventsDto
					.setStartedAt(ServicesUtil.convertFromStringToDateZoho(processObject.getString("AddedTime")));
			processEventsDto.setStartedByUser(userList.get(processObject.getString("Employee_ID.ID")).getUserId());
			processEventsDto
					.setStartedByDisplayName(userList.get(processObject.getString("Employee_ID.ID")).getUserFirstName()
							+ " " + userList.get(processObject.getString("Employee_ID.ID")).getUserLastName());
			processEventsDto.setProcessDisplayName("Leave");
			processEventsDto.setProcessDefinitionId(recordId);
			processEventsDtos.add(processEventsDto);

		}

		return processEventsDtos;
	}

	public Map<String, UserIDPMappingDto> makeUsers() {

		Map<String, UserIDPMappingDto> userList = new HashMap<>();
		UserIDPMappingDto userIDPMappingDto = new UserIDPMappingDto();
		userIDPMappingDto.setUserFirstName("Preetham");
		userIDPMappingDto.setUserLastName("KR");
		userIDPMappingDto.setUserEmail("Preetham.R@incture.com");
		userIDPMappingDto.setUserLoginName("P000188");
		userIDPMappingDto.setUserId("P000188");
		userIDPMappingDto.setZohoId("43277000000190005");
		userList.put("43277000000190005", userIDPMappingDto);

		UserIDPMappingDto userIDPMappingDto1 = new UserIDPMappingDto();
		userIDPMappingDto1.setUserFirstName("Shailesh");
		userIDPMappingDto1.setUserLastName("Shetty");
		userIDPMappingDto1.setUserEmail("shailesh.shetty@incture.com");
		userIDPMappingDto1.setUserLoginName("P000006");
		userIDPMappingDto1.setUserId("P000006");
		userIDPMappingDto1.setZohoId("43277000000191737");
		userList.put("43277000000191737", userIDPMappingDto1);

		UserIDPMappingDto userIDPMappingDto2 = new UserIDPMappingDto();
		userIDPMappingDto2.setUserFirstName("Shruti");
		userIDPMappingDto2.setUserLastName("Patra");
		userIDPMappingDto2.setUserEmail("shruti.patra@incture.com");
		userIDPMappingDto2.setUserLoginName("P000057");
		userIDPMappingDto2.setUserId("P000057");
		userIDPMappingDto2.setZohoId("43277000000191853");
		userList.put("43277000000191853", userIDPMappingDto2);

		return userList;

	}

	public ResponseMessage acceptOrRejectRequest() {

		String accessTokenString = accessToken.getAcessToken();
		System.err.println(accessTokenString);
		Map<String, String> actionParameters = new HashMap<>();
		actionParameters.put("status", "1");
		actionParameters.put("remarks", "done");
		actionParameters.put("responseVersion", "1");
		actionParameters.put("pkid", "41885000000201001");

		RestResponse response1 = restUserZoho.callGetService(ZohoConstants.baseUrl + "approveRecord", accessTokenString,
				actionParameters);

		System.err.println(response1);

		return null;

	}

}
