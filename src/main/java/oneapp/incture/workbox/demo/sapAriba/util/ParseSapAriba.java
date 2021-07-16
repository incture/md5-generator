package oneapp.incture.workbox.demo.sapAriba.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.sapAriba.dao.CustomAttributeValueDao;
import oneapp.incture.workbox.demo.sapAriba.dto.AribaResposeDto;
import oneapp.incture.workbox.demo.sapAriba.dto.AwardDetails;
import oneapp.incture.workbox.demo.sapAriba.dto.Awards;
import oneapp.incture.workbox.demo.sapAriba.dto.HeaderDto;
import oneapp.incture.workbox.demo.sapAriba.dto.ItemValues;
import oneapp.incture.workbox.demo.sapAriba.dto.ParseResponse;
import oneapp.incture.workbox.demo.sapAriba.dto.Report;
import oneapp.incture.workbox.demo.sapAriba.dto.SupplierValues;
import oneapp.incture.workbox.demo.sapAriba.dto.SuppliersDetail;
import oneapp.incture.workbox.demo.sapAriba.dto.ValueDto;

@SuppressWarnings("deprecation")
@Component
public class ParseSapAriba {

	@Autowired
	RestUserSapAriba restUserSapAriba;

	@Autowired
	TaskEventsDao taskEventsDao;

	@Autowired
	CustomAttributeValueDao customAttrDao;

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public ParseResponse parseDetails() {
		List<TaskEventsDto> tasks = null;
		List<ProcessEventsDto> processes = null;
		List<TaskOwnersDto> owners = null;
		List<CustomAttributeValue> customAttributeValues = null;
		JSONObject processObject = null;
		JSONArray processList = null;
		JSONObject processDetail = null;
		JSONObject taskObject = null;
		JSONArray taskList = null;
		String processId = null;
		ParseResponse parseResponse = null;
		Map<String,String> documentMap = new HashMap<>();
		CustomAttributeValue customAttributeValue = null;
		String region = "";
		String commodities = "";
		String finantialSite = "";
		String attrValue = "";
		try{
			long startTime=System.currentTimeMillis();
			String accessToken = getAccessCode();
			System.err.println("ParseSapAriba.parseDetails() time taken to get token : "+(System.currentTimeMillis()-startTime));
			startTime=System.currentTimeMillis();
			Map<String,UserIDPMappingDto> userIDPSFMapping = fetchUsers();
			System.err.println("ParseSapAriba.parseDetails() time taken to get users from idp mapping table : "+(System.currentTimeMillis()-startTime));
			startTime=System.currentTimeMillis();
			RestResponse restResponse  = restUserSapAriba.callRestService(SapAribaConstant.PENDING_APPROVAL_URL, accessToken);
			System.err.println("ParseSapAriba.parseDetails() time taken to get pending processes : "+(System.currentTimeMillis()-startTime));
			processList =(JSONArray)(restResponse.getResponseObject());
			processes = new ArrayList<>();
			tasks = new ArrayList<>();
			owners = new ArrayList<>();
			customAttributeValues = new ArrayList<>();
			Set<String> processIds = new HashSet<String>();
			
			long parserstart=System.currentTimeMillis();
			
			for (Object obj : processList) {
				processObject = (JSONObject) obj;


//				if((processObject.optString("description").contains("Approval for Publishing RFP"))){

					System.err.println(processObject.optString("description"));
					String processName = processObject.optString("description");
					processId = processObject.optString("uniqueName");
					//					processId = "TSK157049337";
					if(processIds.contains(processId))
						continue;
					else
						processIds.add(processId);

					/*RestResponse taskResponse = restUserSapAriba.callRestService(
							SapAribaConstant.TASK_DEATIL_URL+processId+"?realm=aldahra-T", accessToken);*/
					
					startTime=System.currentTimeMillis();
					RestResponse taskResponse = restUserSapAriba.callRestService(
							SapAribaConstant.TASK_DEATIL_URL+processId+"?realm=foulath-T", accessToken);
					System.err.println("ParseSapAriba.parseDetails() time taken to get one task details in parser : "+(System.currentTimeMillis()-startTime));
					ProcessEventsDto process = new ProcessEventsDto();
					processDetail = (JSONObject) taskResponse.getResponseObject();
					JSONObject taskOwner = processDetail.optJSONObject("owner");
					
					
					if(taskOwner.optJSONObject("user") != null) 
                        taskOwner = taskOwner.optJSONObject("user");
                    
                    else 
                        taskOwner = taskOwner.optJSONObject("group");
					
					process.setName(processName);
					process.setRequestId(processId);// need to know
					process.setProcessDefinitionId(processId);//need to know
					process.setProcessDisplayName(processDetail.getString("title"));
					process.setProcessId(processId);
					process.setSubject(processDetail.getString("workspaceTitle"));

					if(userIDPSFMapping.containsKey(taskOwner.getString("uniqueName"))){
						process.setStartedBy(userIDPSFMapping.get(taskOwner.getString("uniqueName")).getUserId());
						process.setStartedByDisplayName(userIDPSFMapping.get(taskOwner.getString("uniqueName")).getUserFirstName()+" "
								+userIDPSFMapping.get(taskOwner.getString("uniqueName")).getUserLastName());
					}else{
						process.setStartedBy(taskOwner.getString("uniqueName"));
						process.setStartedByDisplayName(taskOwner.getString("name"));
					}
					process.setStartedAt(new Date(Long.valueOf(processDetail.getLong("beginDate"))));

					String status = processDetail.getString("status");
					if(SapAribaConstant.PENDING.equals(status))
						process.setStatus("RUNNIING");

					processes.add(process);

					String docId = processDetail.getJSONObject("document").getString("id");

//					JSONArray cusValue = processDetail.getJSONObject("document").getJSONObject("content").getJSONArray("regions");
//					region = ((JSONObject) cusValue.get(0)).optString("name")+" "
//							+((JSONObject) cusValue.get(0)).optString("uniqueName");
//
//					cusValue = processDetail.getJSONObject("document").getJSONObject("content").getJSONArray("commodities");
//					commodities = ((JSONObject) cusValue.get(0)).optString("name")+" "
//							+((JSONObject) cusValue.get(0)).optString("uniqueName");
//
//					JSONArray cusValue = processDetail.getJSONObject("document").getJSONObject("content").getJSONArray("customFields");
//					for (Object res : cusValue) {
//						JSONObject value = (JSONObject) res;
//						if(SapAribaConstant.FINANTIAL_SITE.equals(value.optString("fieldName")))
//						{
//							finantialSite = value.optString("fieldValue");
//							break;
//						}
//					}

					documentMap.put(processId, docId);

					taskList = processDetail.optJSONArray("approvalRequests");

					for (Object objTask : taskList) {
						taskObject = (JSONObject) objTask;

						TaskEventsDto task = new TaskEventsDto();
						//						if(taskObject.getString("approvalState").equals("Pending"))
						//							continue;

						if(taskObject.getString("approvalState").equals("Approved")||taskObject.getString("approvalState").equals("Denied"))
							task.setCompletedAt(new Date(Long.valueOf(taskObject.getLong("approvedDate"))));

						if(!taskObject.getString("approvalState").equals("Pending"))
						{
							task.setCreatedAt(new Date(Long.valueOf(taskObject.getLong("activationDate"))));

							task.setCompletionDeadLine(
									new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
							task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
						}

						task.setCurrentProcessor("");
						task.setCurrentProcessorDisplayName("");

						if(taskObject.getString("approvalState").equals("Approved"))
						{
							JSONObject approvedByOwner = taskObject.getJSONObject("approvedBy");
							approvedByOwner =  approvedByOwner.getJSONObject("user");
							if(userIDPSFMapping.containsKey(approvedByOwner.getString("uniqueName"))){
								task.setCurrentProcessor(userIDPSFMapping.get(approvedByOwner.getString("uniqueName")).getUserId());
								task.setCurrentProcessorDisplayName(userIDPSFMapping.get(approvedByOwner.getString("uniqueName")).getUserFirstName()+" "
										+userIDPSFMapping.get(approvedByOwner.getString("uniqueName")).getUserLastName());
							}else if(approvedByOwner.getString("uniqueName").equals("allwyn.augustine%40incture.com")){
								task.setCurrentProcessor("P000006");
								task.setCurrentProcessorDisplayName("Shailesh Shetty");
							}
							else{
								task.setCurrentProcessor(approvedByOwner.getString("uniqueName"));
								task.setCurrentProcessorDisplayName(approvedByOwner.getString("name"));
							}
						}

						task.setDescription(processDetail.getString("title")+" - Created By "+taskOwner.getString("name"));
						task.setName("Approval");
						task.setOrigin("ARIBA");
						task.setPriority("MEDIUM");
						task.setProcessName(process.getName());
						task.setSubject(process.getSubject());
						task.setProcessId(process.getProcessId());
						task.setEventId(taskObject.getString("id"));
						if(taskObject.getString("approvalState").equals("Ready for approval"))
							task.setStatus("READY");
						else if(taskObject.getString("approvalState").equals("Approved"))
							task.setStatus("COMPLETED");
						else if(taskObject.getString("approvalState").equals("Denied"))
							task.setStatus("COMPLETED");
						else if(taskObject.getString("approvalState").equals("Pending"))
							task.setStatus("NOTSTARTED");

						if(!taskObject.getString("approvalState").equals("Pending"))
							task.setUpdatedAt(new Date(Long.valueOf(taskObject.getLong("activationDate"))));//need to know

						tasks.add(task);


						JSONArray ownerList = taskObject.optJSONArray("approvers");
						for (Object approvers : ownerList) {
							JSONObject approver = (JSONObject)approvers;
							if(approver.getString("type").equals("group")){

								JSONObject ownerDetail = approver.optJSONObject("group");
								long groupTime=System.currentTimeMillis();
								RestResponse groupDetail = restUserSapAriba.callRestService(
										SapAribaConstant.GROUP_DETAIL_URL+ownerDetail.getString("uniqueName").replace(" ", "%20")
										+"/members?realm=foulath-T", accessToken);
								System.err.println("ParseSapAriba.parseDetails() time taken to get group details : "+(System.currentTimeMillis()-groupTime));
								JSONArray groupList = (JSONArray)groupDetail.getResponseObject();
								for (Object grp : groupList) {
									JSONObject userDetail = (JSONObject)grp;

									TaskOwnersDto owner = new TaskOwnersDto();
									owner.setIsProcessed(false);
									owner.setEventId(task.getEventId());
									if(userIDPSFMapping.containsKey(userDetail.getString("uniqueName"))){
										owner.setOwnerEmail(userIDPSFMapping.get(userDetail.getString("uniqueName")).getUserEmail());
										owner.setTaskOwner(userIDPSFMapping.get(userDetail.getString("uniqueName")).getUserId());
										owner.setTaskOwnerDisplayName(userIDPSFMapping.get(userDetail.getString("uniqueName")).getUserFirstName()+" "+
												userIDPSFMapping.get(userDetail.getString("uniqueName")).getUserLastName());
									}else if("allwyn.augustine%40incture.com".equals(userDetail.getString("uniqueName"))){
										owner.setOwnerEmail("shailesh.shetty@incture.com");
										owner.setTaskOwner("P000006");
										owner.setTaskOwnerDisplayName("Shailesh Shetty");
									}
									else{
										owner.setOwnerEmail(userDetail.getString("emailAddress"));
										owner.setTaskOwner(userDetail.getString("uniqueName"));
										owner.setTaskOwnerDisplayName(userDetail.getString("name"));
									}
									owners.add(owner);
								}

							}else if(approver.getString("type").equals("user")){
								TaskOwnersDto owner = new TaskOwnersDto();
								JSONObject ownerDetail = approver.optJSONObject("user");
								owner.setIsProcessed(false);
								owner.setEventId(task.getEventId());
								if(userIDPSFMapping.containsKey(ownerDetail.getString("uniqueName"))){
									owner.setOwnerEmail(userIDPSFMapping.get(ownerDetail.getString("uniqueName")).getUserEmail());
									owner.setTaskOwner(userIDPSFMapping.get(ownerDetail.getString("uniqueName")).getUserId());
									owner.setTaskOwnerDisplayName(userIDPSFMapping.get(ownerDetail.getString("uniqueName")).getUserFirstName()+" "+
											userIDPSFMapping.get(ownerDetail.getString("uniqueName")).getUserLastName());
								}else if("allwyn.augustine%40incture.com".equals(ownerDetail.getString("uniqueName"))){
									owner.setOwnerEmail("shailesh.shetty@incture.com");
									owner.setTaskOwner("P000006");
									owner.setTaskOwnerDisplayName("Shailesh Shetty");
								}
								else{
									owner.setOwnerEmail(ownerDetail.getString("emailAddress"));
									owner.setTaskOwner(ownerDetail.getString("uniqueName"));
									owner.setTaskOwnerDisplayName(ownerDetail.getString("name"));
								}
								owners.add(owner);

							}

						}

						
						// getting the custom attributes of the process
                        List<String> attributes = customAttrDao.getCustomAttributesByProcess(processName);
                        JSONObject cusValue = processDetail.getJSONObject("document").getJSONObject("content");
                        
                        for(String attribute : attributes) {
                            customAttributeValue = new CustomAttributeValue();
                            customAttributeValue.setProcessName(process.getName());
                            customAttributeValue.setTaskId(task.getEventId());
                            if(attribute.equals("arb_CompanyCode"))
    							attrValue = ((JSONObject)cusValue.getJSONArray("customFields").get(0)).getString("fieldValue");
                            else if(attribute.equals("arb_PurchasingOrganization"))
                            	attrValue = ((JSONObject)cusValue.getJSONArray("customFields").get(1)).getString("fieldValue");
                            else if(attribute.equals("arb_PurchasingGroup"))
                            	attrValue = ((JSONObject)cusValue.getJSONArray("customFields").get(2)).getString("fieldValue");
                            else {
								attrValue = ((JSONObject) processDetail).getString("workspaceTitle");
							}
                            customAttributeValue.setKey(attribute);
                            customAttributeValue.setAttributeValue(attrValue);
                            customAttributeValues.add(customAttributeValue);
                        }

//                        JSONArray cusValue = processDetail.getJSONObject("document").getJSONObject("content").getJSONArray("customFields");
//    					for (Object res : cusValue) {
//    						if(customAttributeValue.getKey().equals("arb_CompanyCode"))
//    							companyCode = ((JSONObject) cusValue.get(0)).optString("fieldValue");
//    							
                        
                        
//						customAttributeValue = new CustomAttributeValue();
//
//						customAttributeValue.setProcessName(process.getName());
//						customAttributeValue.setTaskId(task.getEventId());
//						customAttributeValue.setKey("region");
//						customAttributeValue.setAttributeValue(region);
//						customAttributeValues.add(customAttributeValue);
//
//						customAttributeValue = new CustomAttributeValue();
//
//						customAttributeValue.setProcessName(process.getName());
//						customAttributeValue.setTaskId(task.getEventId());
//						customAttributeValue.setKey("commodities");
//						customAttributeValue.setAttributeValue(commodities);
//						customAttributeValues.add(customAttributeValue);
//
//						customAttributeValue = new CustomAttributeValue();
//
//						customAttributeValue.setProcessName(process.getName());
//						customAttributeValue.setTaskId(task.getEventId());
//						customAttributeValue.setKey("cus_FinancialSites");
//						customAttributeValue.setAttributeValue(finantialSite);
//						customAttributeValues.add(customAttributeValue);
					}
//				}

			}
			
			populateDetails(documentMap,accessToken);
			System.err.println("ParseSapAriba.parseDetails() time taken to parse the tasks : "+(System.currentTimeMillis()-parserstart));
			parseResponse = new ParseResponse(tasks, processes, owners, 0, customAttributeValues);
		
		}catch(Exception e){
			System.err.println("ParseSapAriba.parseDetails()"+e.getMessage());
			e.printStackTrace();
		}
		return parseResponse;
	}

	@Async
	public void populateDetails(Map<String, String> documentMap, String accessToken) {
		List<CustomAttributeValue> customAttributeValues = null;
		CustomAttributeValue attributeValue = null;
		ItemValues itemValues = null;
		JSONArray terms = null;
		Map<String,ItemValues> items = null;//new HashMap<String, ItemValues>();
		Map<String,String> suppliersMapping = null;//new HashMap<>();
		try{
			List<String> existingProcessesList = taskEventsDao.getprocessIdByOrigin("ARIBA");
			customAttributeValues = new ArrayList<>();
			for (Map.Entry<String, String> process : documentMap.entrySet()) {
				items = new HashMap<String, ItemValues>();
				suppliersMapping = new HashMap<>();
				if(existingProcessesList.contains(process.getKey())){
					attributeValue = new CustomAttributeValue();
					attributeValue.setAttributeValue(process.getValue());
					attributeValue.setKey(SapAribaConstant.DOCUMENT_ID);
					attributeValue.setProcessName("Approval For Award for RFQ".replace(" ", ""));
					attributeValue.setTaskId(process.getKey());
					customAttributeValues.add(attributeValue);


					RestResponse itemResponse = restUserSapAriba.callRestService(
							SapAribaConstant.ITEM_DEATIL_URL+process.getValue()+"?realm=foulath-T&$select=items", accessToken);
					JSONObject itemDetail =(JSONObject)(itemResponse.getResponseObject());
					JSONArray itemDetailsList = itemDetail.getJSONArray("items");
					for (Object itemObj : itemDetailsList) {
						JSONObject item = (JSONObject)itemObj;

							if(item.optString("itemType").equals("Line Item")){
								itemValues = new ItemValues();
								itemValues.setHistoricPrize("");
								itemValues.setItemName(item.optString("title"));
								itemValues.setCompetitiveTermFieldId(item.optString("competitiveTermFieldId"));
								terms = item.getJSONArray("terms");
								for (Object term : terms) {
									if(((JSONObject)term).optString("fieldId").equals("PRICE"))
									{
										try{
											itemValues.setInitialPrize(((JSONObject)term).getJSONObject("value").optString("amount"));
										}catch (Exception e) {
											System.err.println("[ARIBA Content]no initial prize");
										}
									}
									if(((JSONObject)term).optString("fieldId").equals("QUANTITY"))
									{
										try{
											itemValues.setQuantity(((JSONObject)term).getJSONObject("value").optString("quantity"));
											itemValues.setQuantity(itemValues.getQuantity()+" "+((JSONObject)term).getJSONObject("value").optString("uom"));
										}catch (Exception e) {
											System.err.println("[ARIBA Content]no initial QUANTITY");
										}
									}
								}
								items.put(item.optString("itemId"), itemValues);
							}
					}

					RestResponse suppleirResponse = restUserSapAriba.callRestService(
							SapAribaConstant.ITEM_DEATIL_URL+process.getValue()+"?realm=foulath-T&$select=invitedSuppliers", accessToken);

					JSONArray supplierList = ((JSONObject)(suppleirResponse.getResponseObject())).getJSONArray("invitedSuppliers");
					for (Object supplierObj : supplierList) {
						JSONObject supplier = (JSONObject)supplierObj;
						supplier = supplier.getJSONObject("mainContact");
						suppliersMapping.put(supplier.optString("uniqueName"), supplier.optString("orgName"));
					}

					Gson g = new Gson();
					System.err.println(g.toJson(items));

					attributeValue = new CustomAttributeValue();
					attributeValue.setAttributeValue(g.toJson(items));
					attributeValue.setKey(SapAribaConstant.ITEMS);
					attributeValue.setProcessName("Approval For Award for RFQ".replace(" ", ""));
					attributeValue.setTaskId(process.getKey());
					customAttributeValues.add(attributeValue);

					attributeValue = new CustomAttributeValue();
					attributeValue.setAttributeValue(g.toJson(suppliersMapping));
					attributeValue.setKey(SapAribaConstant.SUPPLIERS);
					attributeValue.setProcessName("Approval For Award for RFQ".replace(" ", ""));
					attributeValue.setTaskId(process.getKey());
					customAttributeValues.add(attributeValue);

				}
			}
			Gson g1 = new Gson();
			System.err.println(g1.toJson(customAttributeValues));
			customAttrDao.addCustomAttributeValue(customAttributeValues);
			System.err.println("[WORKBOX- SAP Ariba][addCustomAttributeValue]" );

		}catch (Exception e) {
			System.err.println("ParseSapAriba.populateDetails()"+e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private Map<String, UserIDPMappingDto> fetchUsers() {
		Map<String, UserIDPMappingDto> userMapping = null;
		UserIDPMappingDto userDetails = null;

		String userofSF = "Select user_login_name,user_email,user_first_name,user_id,user_last_name,ariba_id"
				+ " from user_idp_mapping where ariba_id is not null";
		Query sfUsersQuery = this.getSession().createSQLQuery(userofSF);
		List<Object[]> userList = sfUsersQuery.list();

		userMapping = new HashMap<String, UserIDPMappingDto>();
		for (Object[] obj : userList) {

			userDetails = new UserIDPMappingDto();
			userDetails.setUserEmail((String)obj[1]);
			userDetails.setUserFirstName((String)obj[2]);
			userDetails.setUserId((String)obj[3]);
			userDetails.setUserLastName((String)obj[4]);
			userDetails.setUserLoginName((String)obj[0]);

			userMapping.put((String)obj[5], userDetails);
		}
		return userMapping;

	}

	@SuppressWarnings("unchecked")
	public String getAribaUserId(String userId) {

		String aribaId = "Ariba Id not Found";
		try {
			String userofSF = "Select ariba_id,user_email,user_first_name,user_id,user_last_name"
					+ " from user_idp_mapping where ariba_id is not null and user_login_name='" + userId + "'";
			Query sfUsersQuery = this.getSession().createSQLQuery(userofSF);
			List<Object[]> userList = sfUsersQuery.list();

			if (!ServicesUtil.isEmpty(userList)) {
				Object[] user = userList.get(0);
				aribaId = (String) user[0];
				System.err.println("ParseSapAriba.getAribaUserId() User Id : " + userId + " Ariba ID : " + aribaId);
			}
		} catch (Exception e) {
			System.err.println("ParseSapAriba.getAribaUserId() error  : " + e);
		}
		return aribaId;

	}
	@SuppressWarnings("resource")
	private String getAccessCode()
	{

		try{
			String base64 = SapAribaConstant.OAUTH_CLIENT_ID+":"+SapAribaConstant.OAUTH_SECRET;

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
			oauthPost.setHeader("Authorization","Basic "+Base64.getEncoder().encodeToString(base64.getBytes()));
			HttpResponse response = client.execute(oauthPost);
			int code = response.getStatusLine().getStatusCode();

			System.err.println("[WORKBOX- SapAriba][ResponseCode][Approve]" + code);

			if(code == 200)
				tokenResponse = EntityUtils.toString(response.getEntity());
			else{
				return SapAribaConstant.FAILURE;
			}

			JSONObject myObject = new JSONObject(tokenResponse);
			String accessToken =myObject.getString("access_token");
			System.err.println(accessToken);
			return accessToken;
		}catch (Exception e) {
			System.out.println("ParseSapAriba.enclosing_method()"+e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public AribaResposeDto getDetails(String taskId) {
		AribaResposeDto aribaResposeDto = new AribaResposeDto();
		SuppliersDetail supplierDetail = null;
		Report report = null;
		HeaderDto headerdto = null;
		ValueDto valueDto = null;
		List<HeaderDto> headerdtos = null;
		List<ValueDto> valueDtos = null;
		Map<String,ItemValues> items = new HashMap<String, ItemValues>();
		Map<String,String> suppliersMapping = new HashMap<>();
		Map<String,List<SupplierValues>> itemResponseMap = new HashMap<>();
		List<SupplierValues> supplierValues = null;
		List<AwardDetails> awardDetails = null;
		AwardDetails details = null;
		String quantityUOM = null;
		String currency = null;
		ObjectMapper mapper = new ObjectMapper();
		String documentId = null;
		Double saving = null;
		Double extendedPrice = null;
		Double quantity = null;
		Double historicExtendedPrice = null;
		Double historicPrice = null;
		Double totalHistoric = new Double(0);
		Double totalLeading = new Double(0);
		Double totalInitial = new Double(0);
		Boolean hasInitail = false;
		Map<String,String> historicMap = new HashMap<>();
		Map<String,List<Awards>> awardMap = new LinkedHashMap<>();
		Awards award = null;
		Gson g = new Gson();
		Double leading = null;
		String competativeField = "";
		List<Awards> awards = null;
		try{
			String accessToken = getAccessCode();
			List<CustomAttributeValue> attributesDetails = customAttrDao.getAttributes(taskId);
			for (CustomAttributeValue values : attributesDetails) {
				if(values.getKey().equals(SapAribaConstant.DOCUMENT_ID))
					documentId = values.getAttributeValue();
				if(values.getKey().equals(SapAribaConstant.ITEMS))
					items = mapper.readValue(values.getAttributeValue(), Map.class);
				if(values.getKey().equals(SapAribaConstant.SUPPLIERS))
					suppliersMapping = mapper.readValue(values.getAttributeValue(), Map.class);
			}

			RestResponse itemDetailsResponse = restUserSapAriba.callRestService(
					SapAribaConstant.ITEM_DEATIL_URL+documentId+"?realm=foulath-T&$select=itemResponses", accessToken);
			JSONArray itemResposneList = ((JSONObject)(itemDetailsResponse.getResponseObject())).getJSONArray("itemResponses");

			for (Object itemResponseObj: itemResposneList) {
				JSONObject itemId = (JSONObject)itemResponseObj;
				if(items.containsKey(itemId.getJSONObject("item").optString("itemId"))){
					if(itemResponseMap.containsKey(itemId.getJSONObject("item").optString("itemId"))){
						SupplierValues sv = new SupplierValues();
						sv.setSupplierId(itemId.getString("supplierUniqueName"));
						JSONArray valueList = itemId.getJSONObject("item").getJSONArray("terms");
						for (Object valueObj : valueList) {
							if(((JSONObject)valueObj).optString("fieldId").equals("PRICE"))
							{
								try{
									sv.setPrize(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString());
									currency = ((JSONObject)valueObj).getJSONObject("value").optString("currency");
								}catch (Exception e) {
									System.err.println("[ARIBA Content]no prize");
								}
							}
							if(((JSONObject)valueObj).optString("fieldId").equals("QUANTITY"))
							{
								try{
									sv.setQuantity(((JSONObject)valueObj).getJSONObject("value").optString("quantity"));
									quantityUOM = ((JSONObject)valueObj).getJSONObject("value").optString("uom");
									sv.setQuantity(sv.getQuantity()+" "+quantityUOM);
								}catch (Exception e) {
									System.err.println("[ARIBA Content]no prize");
								}
							}
							if(((JSONObject)valueObj).optString("fieldId").equals("EXTENDEDPRICE"))
							{
								try{
									sv.setExtendedPrize(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString());
								}catch (Exception e) {
									System.err.println("[ARIBA Content]no extende prize");
								}
							}
							if(((JSONObject)valueObj).optString("fieldId").equals("SAVINGS"))
							{
								try{
									sv.setSavings(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString());
								}catch (Exception e) {
									System.err.println("[ARIBA Content]no saving");
								}
							}
						}

						itemResponseMap.get(itemId.getJSONObject("item").optString("itemId")).add(sv);
					}else{

						supplierValues = new ArrayList<>();
						SupplierValues sv = new SupplierValues();
						sv.setSupplierId(itemId.getString("supplierUniqueName"));
						JSONArray valueList = itemId.getJSONObject("item").getJSONArray("terms");
						for (Object valueObj : valueList) {
							if(((JSONObject)valueObj).optString("fieldId").equals("PRICE"))
							{
								try{
									sv.setPrize(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString());
								}catch (Exception e) {
									sv.setPrize(null);
									System.err.println("[ARIBA Content]no prize");
								}
							}
							if(((JSONObject)valueObj).optString("fieldId").equals("QUANTITY"))
							{
								try{
									sv.setQuantity(((JSONObject)valueObj).getJSONObject("value").optString("quantity"));
									quantityUOM = ((JSONObject)valueObj).getJSONObject("value").optString("uom");
									sv.setQuantity(sv.getQuantity()+" "+quantityUOM);
								}catch (Exception e) {
									sv.setQuantity(null);
									System.err.println("[ARIBA Content]no prize");
								}
							}
							if(((JSONObject)valueObj).optString("fieldId").equals("EXTENDEDPRICE"))
							{
								try{
									sv.setExtendedPrize(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString());
									extendedPrice = ((JSONObject)valueObj).getJSONObject("value").optDouble("amount");
								}catch (Exception e) {
									sv.setExtendedPrize(null);
									System.err.println("[ARIBA Content]no extende prize");
								}
							}
							if(((JSONObject)valueObj).optString("fieldId").equals("SAVINGS"))
							{
								try{
									sv.setSavings(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString());
									saving = ((JSONObject)valueObj).getJSONObject("value").optDouble("amount");
								}catch (Exception e) {
									sv.setSavings(null);
									System.err.println("[ARIBA Content]no saving");
								}
							}
						}

						historicExtendedPrice = saving + extendedPrice;
						ItemValues curItem = mapper.readValue(g.toJson(items.get(itemId.getJSONObject("item").optString("itemId"))), ItemValues.class);
						quantity = Double.valueOf(
								(curItem.getQuantity().replace(quantityUOM, "")));
						historicPrice = historicExtendedPrice/quantity;
						if(curItem.getCompetitiveTermFieldId().equals(SapAribaConstant.FIELD_PRICE))
							historicMap.put(itemId.getJSONObject("item").optString("itemId"), BigDecimal.valueOf(historicPrice).toPlainString());
						else
							historicMap.put(itemId.getJSONObject("item").optString("itemId"), BigDecimal.valueOf(historicExtendedPrice).toPlainString());
						supplierValues.add(sv);
						itemResponseMap.put(itemId.getJSONObject("item").optString("itemId"), supplierValues);
					}
				}
			}
			supplierDetail = new SuppliersDetail();
			headerdtos = new ArrayList<>();
			headerdto = new HeaderDto();
			headerdto.setName("Item");
			headerdto.setKey("Item");
			headerdtos.add(headerdto);
			headerdto = new HeaderDto();
			headerdto.setName("Lead Participant");
			headerdto.setKey("Lead Participant");
			headerdtos.add(headerdto);
			headerdto = new HeaderDto();
			headerdto.setName("Quantity");
			headerdto.setKey("Quantity");
			headerdtos.add(headerdto);
			headerdto = new HeaderDto();
			headerdto.setName("Initial");
			headerdto.setKey("Initial");
			headerdtos.add(headerdto);
			headerdto = new HeaderDto();
			headerdto.setName("Historic");
			headerdto.setKey("Historic");
			headerdtos.add(headerdto);
			headerdto = new HeaderDto();
			headerdto.setName("Leading");
			headerdto.setKey("Leading");
			headerdtos.add(headerdto);

			valueDtos = new ArrayList<>();
			Set<String> suppliers = new LinkedHashSet<>();
			for (Map.Entry<String, ItemValues> item : items.entrySet()) {
				valueDto = new ValueDto();
				System.err.println(item.getKey());
				System.err.println(item.getValue());
				item.setValue(mapper.readValue(g.toJson(item.getValue()), ItemValues.class));
				System.err.println(item.getValue());
				valueDto.setItemName(item.getValue().getItemName());
				if(!ServicesUtil.isEmpty(item.getValue().getInitialPrize()))
					valueDto.setInitial(item.getValue().getInitialPrize()+" "+currency);
				valueDto.setHistoric(historicMap.get(item.getKey())+" "+currency);
				totalHistoric += Double.valueOf(historicMap.get(item.getKey()));
				if(!ServicesUtil.isEmpty(item.getValue().getInitialPrize()))
				{
					hasInitail = true;
					totalInitial += Double.valueOf(item.getValue().getInitialPrize());
				}
				valueDto.setQuantity(item.getValue().getQuantity());
				if(SapAribaConstant.FIELD_PRICE.equals(item.getValue().getCompetitiveTermFieldId()))
					leading = Double.parseDouble(itemResponseMap.get(item.getKey()).get(0).getPrize());
				else if(SapAribaConstant.FIELD_EXTENDEDPRICE.equals(item.getValue().getCompetitiveTermFieldId()))
					leading = Double.parseDouble(itemResponseMap.get(item.getKey()).get(0).getExtendedPrize());

				competativeField = item.getValue().getCompetitiveTermFieldId();
				List<String> prices = new ArrayList<>();
				for (SupplierValues strValues : itemResponseMap.get(item.getKey())) {
					if(SapAribaConstant.FIELD_PRICE.equals(item.getValue().getCompetitiveTermFieldId())){
						if(!ServicesUtil.isEmpty(strValues.getPrize()))
						{	
							prices.add(strValues.getPrize()+" "+currency);

							Double next = Double.parseDouble(strValues.getPrize());  
							if(next<=leading)
							{
								valueDto.setLeading(strValues.getPrize()+" "+currency);
								valueDto.setLeadParticipant(suppliersMapping.get(strValues.getSupplierId()));
								valueDto.setSavings(strValues.getSavings()+" "+currency);
								leading = next;
							}	
						}else{
							prices.add("");
						}
					}
					else if(SapAribaConstant.FIELD_EXTENDEDPRICE.equals(item.getValue().getCompetitiveTermFieldId()))
					{
						if(!ServicesUtil.isEmpty(strValues.getExtendedPrize()))
						{	
							prices.add(strValues.getExtendedPrize()+" "+currency);

							Double next = Double.parseDouble(strValues.getExtendedPrize());  
							if(next<=leading)
							{
								valueDto.setLeading(strValues.getExtendedPrize()+" "+currency);
								valueDto.setLeadParticipant(suppliersMapping.get(strValues.getSupplierId()));
								valueDto.setSavings(strValues.getSavings()+" "+currency);
								leading = next;
							}	
						}else{
							prices.add("");
						}
					}
					suppliers.add(strValues.getSupplierId());
				}
				totalLeading += leading;
				valueDto.setPrices(prices);
				valueDtos.add(valueDto);

			}
			for (String str : suppliers) {
				headerdto = new HeaderDto();
				headerdto.setName(suppliersMapping.get(str));
				headerdto.setKey(suppliersMapping.get(str));
				headerdtos.add(headerdto);
			}
			//			headerdtos.add(headerdto);
			headerdto = new HeaderDto();
			headerdto.setName("Savings");
			headerdto.setKey("Savings");
			headerdtos.add(headerdto);

			supplierDetail.setHeaderdto(headerdtos);

			supplierDetail.setValueDto(valueDtos);

			if(competativeField.equals(SapAribaConstant.FIELD_EXTENDEDPRICE))
				supplierDetail.setCompetativeField(SapAribaConstant.FIELD_EXTENDEDPRICE_VALUE);
			else if(competativeField.equals(SapAribaConstant.FIELD_PRICE))
				supplierDetail.setCompetativeField(SapAribaConstant.FIELD_PRICE_VALUE);

			aribaResposeDto.setSupplierDetail(supplierDetail);

			RestResponse scenarioResponse = restUserSapAriba.callRestService(
					SapAribaConstant.ITEM_DEATIL_URL+documentId+"?realm=foulath-T&$select=scenario", accessToken);
			JSONArray scenarioResposneList = ((JSONObject)(scenarioResponse.getResponseObject())).getJSONArray("scenario");


			for (Object scenario : scenarioResposneList) {
				JSONArray supplierBids = ((JSONObject)scenario).getJSONArray("supplierBids");
				for (Object bids : supplierBids) {

					JSONObject bid = (JSONObject)bids;
					if(items.containsKey(bid.getJSONObject("item").optString("itemId")))
					{
						if(awardMap.containsKey(bid.getJSONObject("item").optString("itemId"))){
							//							awards = awardMap.get(bid.getJSONObject("item").optString("itemId"));

							award = new Awards();
							award.setAllocation(bid.optString("winningSplitValue"));
							award.setSupplierName(suppliersMapping.get(bid.optString("supplierUniqueName")));
							for (Object valueObj : bid.getJSONObject("item").getJSONArray("terms")) {
								if(((JSONObject)valueObj).optString("fieldId").equals("PRICE"))
								{
									try{
										award.setPrice(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString()+" "+currency);
									}catch (Exception e) {
										award.setPrice(null);
										System.err.println("[ARIBA Content]no prize");
									}
									continue;
								}
								if(((JSONObject)valueObj).optString("fieldId").equals("QUANTITY"))
								{
									try{
										award.setQuantity(((JSONObject)valueObj).getJSONObject("value").optString("quantity"));
										quantityUOM = ((JSONObject)valueObj).getJSONObject("value").optString("uom");
										award.setQuantity(award.getQuantity()+" "+quantityUOM);
									}catch (Exception e) {
										award.setQuantity(null);
										System.err.println("[ARIBA Content]no prize");
									}
									continue;
								}
								if(((JSONObject)valueObj).optString("fieldId").equals("EXTENDEDPRICE"))
								{
									try{
										award.setExtendedPrize(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString()+" "+currency);
										extendedPrice = ((JSONObject)valueObj).getJSONObject("value").optDouble("amount");
									}catch (Exception e) {
										award.setExtendedPrize(null);
										System.err.println("[ARIBA Content]no extende prize");
									}
									continue;
								}
								if(((JSONObject)valueObj).optString("fieldId").equals("SAVINGS"))
								{
									try{
										award.setSavings(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString()+" "+currency);
										saving = ((JSONObject)valueObj).getJSONObject("value").optDouble("amount");
									}catch (Exception e) {
										award.setSavings(null);
										System.err.println("[ARIBA Content]no saving");
									}
									continue;
								}
							}

							awardMap.get(bid.getJSONObject("item").optString("itemId")).add(award);
						}else{
							awards = new ArrayList<>();
							award = new Awards();
							award.setAllocation(bid.optString("winningSplitValue"));
							award.setSupplierName(suppliersMapping.get(bid.optString("supplierUniqueName")));
							for (Object valueObj : bid.getJSONObject("item").getJSONArray("terms")) {
								if(((JSONObject)valueObj).optString("fieldId").equals("PRICE"))
								{
									try{
										award.setPrice(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString()+" "+currency);
									}catch (Exception e) {
										award.setPrice(null);
										System.err.println("[ARIBA Content]no prize");
									}
									continue;
								}
								if(((JSONObject)valueObj).optString("fieldId").equals("QUANTITY"))
								{
									try{
										award.setQuantity(((JSONObject)valueObj).getJSONObject("value").optString("quantity"));
										quantityUOM = ((JSONObject)valueObj).getJSONObject("value").optString("uom");
										award.setQuantity(award.getQuantity()+" "+quantityUOM);
									}catch (Exception e) {
										award.setQuantity(null);
										System.err.println("[ARIBA Content]no prize");
									}
									continue;
								}
								if(((JSONObject)valueObj).optString("fieldId").equals("EXTENDEDPRICE"))
								{
									try{
										award.setExtendedPrize(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString()+" "+currency);
										extendedPrice = ((JSONObject)valueObj).getJSONObject("value").optDouble("amount");
									}catch (Exception e) {
										award.setExtendedPrize(null);
										System.err.println("[ARIBA Content]no extende prize");
									}
									continue;
								}
								if(((JSONObject)valueObj).optString("fieldId").equals("SAVINGS"))
								{
									try{
										award.setSavings(((JSONObject)valueObj).getJSONObject("value").getBigDecimal("amount").toPlainString()+" "+currency);
										saving = ((JSONObject)valueObj).getJSONObject("value").optDouble("amount");
									}catch (Exception e) {
										award.setSavings(null);
										System.err.println("[ARIBA Content]no saving");
									}
									continue;
								}
							}

							awards.add(award);
							awardMap.put(bid.getJSONObject("item").optString("itemId"), awards);

						}

					}
				}
			}
			awardDetails = new ArrayList<>();
			for (Map.Entry<String, List<Awards>> awdObj : awardMap.entrySet()) {
				details = new AwardDetails();
				details.setItemName(items.get(awdObj.getKey()).getItemName());
				details.setAwards(awdObj.getValue());
				awardDetails.add(details);
			}
			System.err.println(awardDetails);
			aribaResposeDto.setAwardDetails(awardDetails);

			report = new Report();
			report.setHistoric(BigDecimal.valueOf(totalHistoric).toPlainString()+" "+currency);
			report.setLeading(BigDecimal.valueOf(totalLeading).toPlainString()+" "+currency);
			report.setSavings(BigDecimal.valueOf((totalHistoric-totalLeading)).toPlainString()+" "+currency);
			report.setLeadingVsHistoric(BigDecimal.valueOf(totalHistoric-totalLeading).toPlainString()+" "+currency);
			if(hasInitail)
			{
				report.setInitial(BigDecimal.valueOf(totalInitial).toPlainString()+" "+currency);
				report.setLeadingVsInitial(BigDecimal.valueOf((totalInitial-totalLeading)).toPlainString()+" "+currency);
			}
			aribaResposeDto.setReport(report);

		}catch (Exception e) {
			System.err.println("ParseSapAriba.getDetails()error"+e.getMessage());
		}
		return aribaResposeDto;
	}

	public ParseResponse parse(String id){
		List<TaskEventsDto> tasks = null;
		List<ProcessEventsDto> processes = null;
		List<TaskOwnersDto> owners = null;

		JSONObject processDetail = null;
		JSONObject taskObject = null;
		JSONArray taskList = null;
		String processId = null;
		ParseResponse parseResponse = null;
		Map<String,String> documentMap = new HashMap<>();
		try{
			String accessToken = getAccessCode();
			Map<String,UserIDPMappingDto> userIDPSFMapping = fetchUsers();
			processId = id;
			processes = new ArrayList<>();
			tasks = new ArrayList<>();
			owners = new ArrayList<>();
			RestResponse taskResponse = restUserSapAriba.callRestService(
					SapAribaConstant.TASK_DEATIL_URL+processId+"?realm=foulath-T", accessToken);
			ProcessEventsDto process = new ProcessEventsDto();
			processDetail = (JSONObject) taskResponse.getResponseObject();
			JSONObject taskOwner = processDetail.optJSONObject("owner");
			taskOwner = taskOwner.optJSONObject("user");

			process.setName("Approval For Award for RFQ".replace(" ", ""));
			process.setRequestId(processId);// need to know
			process.setProcessDefinitionId(processId);//need to know
			process.setProcessDisplayName(processDetail.getString("title"));
			process.setProcessId(processId);
			process.setSubject(processDetail.getString("workspaceTitle"));

			if(userIDPSFMapping.containsKey(taskOwner.getString("uniqueName"))){
				process.setStartedBy(userIDPSFMapping.get(taskOwner.getString("uniqueName")).getUserId());
				process.setStartedByDisplayName(userIDPSFMapping.get(taskOwner.getString("uniqueName")).getUserFirstName()+" "
						+userIDPSFMapping.get(taskOwner.getString("uniqueName")).getUserLastName());
			}else{
				process.setStartedBy(taskOwner.getString("uniqueName"));
				process.setStartedByDisplayName(taskOwner.getString("name"));
			}
			process.setStartedAt(new Date(Long.valueOf(processDetail.getLong("beginDate"))));

			String status = processDetail.getString("status");
			if(SapAribaConstant.PENDING.equals(status))
				process.setStatus("RUNNIING");
			if(SapAribaConstant.COMPLETED.equals(status))
				process.setStatus("COMPLETED");

//			process.setSubject(processDetail.optString("description"));

			processes.add(process);

			String docId = processDetail.getJSONObject("document").getString("id");

			documentMap.put(processId, docId);

			taskList = processDetail.optJSONArray("approvalRequests");

			for (Object objTask : taskList) {
				taskObject = (JSONObject) objTask;

				TaskEventsDto task = new TaskEventsDto();
				if(taskObject.getString("approvalState").equals("Pending"))
					continue;

				if(taskObject.getString("approvalState").equals("Approved")||taskObject.getString("approvalState").equals("Denied"))
					task.setCompletedAt(new Date(Long.valueOf(taskObject.getLong("approvedDate"))));
				task.setCreatedAt(new Date(Long.valueOf(taskObject.getLong("activationDate"))));
				task.setCompletionDeadLine(
						new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
				task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));

				task.setCurrentProcessor("");
				task.setCurrentProcessorDisplayName("");

				if(taskObject.getString("approvalState").equals("Approved") || taskObject.getString("approvalState").equals("Denied"))
				{
					JSONObject approvedByOwner = taskObject.getJSONObject("approvedBy");
					approvedByOwner =  approvedByOwner.getJSONObject("user");
					if(userIDPSFMapping.containsKey(approvedByOwner.getString("uniqueName"))){
						task.setCurrentProcessor(userIDPSFMapping.get(approvedByOwner.getString("uniqueName")).getUserId());
						task.setCurrentProcessorDisplayName(userIDPSFMapping.get(approvedByOwner.getString("uniqueName")).getUserFirstName()+" "
								+userIDPSFMapping.get(approvedByOwner.getString("uniqueName")).getUserLastName());
					}else if(approvedByOwner.getString("uniqueName").equals("allwyn.augustine%40incture.com")){
						task.setCurrentProcessor("P000006");
						task.setCurrentProcessorDisplayName("Shailesh Shetty");
					}
					else{
						task.setCurrentProcessor(approvedByOwner.getString("uniqueName"));
						task.setCurrentProcessorDisplayName(approvedByOwner.getString("name"));
					}
				}

				task.setDescription(processDetail.getString("title")+" - Created By "+taskOwner.getString("name"));
				task.setName("Approval");
				task.setOrigin("ARIBA");
				task.setPriority("MEDIUM");
				task.setProcessName(process.getName());
				task.setSubject(process.getSubject());
				task.setProcessId(process.getProcessId());
				task.setEventId(taskObject.getString("id"));
				if(taskObject.getString("approvalState").equals("Ready for approval"))
					task.setStatus("READY");
				else if(taskObject.getString("approvalState").equals("Approved"))
					task.setStatus("COMPLETED");
				else if(taskObject.getString("approvalState").equals("Denied"))
					task.setStatus("COMPLETED");

				task.setUpdatedAt(new Date(Long.valueOf(taskObject.getLong("activationDate"))));//need to know

				tasks.add(task);

				JSONArray ownerList = taskObject.optJSONArray("approvers");
				for (Object approvers : ownerList) {
					JSONObject approver = (JSONObject)approvers;
					if(approver.getString("type").equals("group")){

						JSONObject ownerDetail = approver.optJSONObject("group");
						RestResponse groupDetail = restUserSapAriba.callRestService(
								SapAribaConstant.GROUP_DETAIL_URL+ownerDetail.getString("uniqueName")
								+"/members?realm=foulath-T", accessToken);
						JSONArray groupList = (JSONArray)groupDetail.getResponseObject();
						for (Object grp : groupList) {
							JSONObject userDetail = (JSONObject)grp;

							TaskOwnersDto owner = new TaskOwnersDto();
							owner.setIsProcessed(false);
							owner.setEventId(task.getEventId());
							if(userIDPSFMapping.containsKey(userDetail.getString("uniqueName"))){
								owner.setOwnerEmail(userIDPSFMapping.get(userDetail.getString("uniqueName")).getUserEmail());
								owner.setTaskOwner(userIDPSFMapping.get(userDetail.getString("uniqueName")).getUserId());
								owner.setTaskOwnerDisplayName(userIDPSFMapping.get(userDetail.getString("uniqueName")).getUserFirstName()+" "+
										userIDPSFMapping.get(userDetail.getString("uniqueName")).getUserLastName());
							}else if("allwyn.augustine%40incture.com".equals(userDetail.getString("uniqueName"))){
								owner.setOwnerEmail("shailesh.shetty@incture.com");
								owner.setTaskOwner("P000006");
								owner.setTaskOwnerDisplayName("Shailesh Shetty");
							}
							else{
								owner.setOwnerEmail(userDetail.getString("emailAddress"));
								owner.setTaskOwner(userDetail.getString("uniqueName"));
								owner.setTaskOwnerDisplayName(userDetail.getString("name"));
							}
							owners.add(owner);
						}

					}else if(approver.getString("type").equals("user")){
						TaskOwnersDto owner = new TaskOwnersDto();
						JSONObject ownerDetail = approver.optJSONObject("user");
						owner.setIsProcessed(false);
						owner.setEventId(task.getEventId());
						if(userIDPSFMapping.containsKey(ownerDetail.getString("uniqueName"))){
							owner.setOwnerEmail(userIDPSFMapping.get(ownerDetail.getString("uniqueName")).getUserEmail());
							owner.setTaskOwner(userIDPSFMapping.get(ownerDetail.getString("uniqueName")).getUserId());
							owner.setTaskOwnerDisplayName(userIDPSFMapping.get(ownerDetail.getString("uniqueName")).getUserFirstName()+" "+
									userIDPSFMapping.get(ownerDetail.getString("uniqueName")).getUserLastName());
						}else if("allwyn.augustine%40incture.com".equals(ownerDetail.getString("uniqueName"))){
							owner.setOwnerEmail("shailesh.shetty@incture.com");
							owner.setTaskOwner("P000006");
							owner.setTaskOwnerDisplayName("Shailesh Shetty");
						}
						else{
							owner.setOwnerEmail(ownerDetail.getString("emailAddress"));
							owner.setTaskOwner(ownerDetail.getString("uniqueName"));
							owner.setTaskOwnerDisplayName(ownerDetail.getString("name"));
						}
						owners.add(owner);

					}

				}

			}
			parseResponse = new ParseResponse(tasks, processes, owners, 0, null);
		}catch(Exception e){
			System.err.println("ParseSapAriba.parseDetails()"+e.getMessage());
		}
		return parseResponse;
	}

}
