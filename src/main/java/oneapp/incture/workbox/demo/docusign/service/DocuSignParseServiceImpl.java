package oneapp.incture.workbox.demo.docusign.service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.docusign.dto.SigningURLDto;
import oneapp.incture.workbox.demo.docusign.dto.TaskAndAuditDto;
import oneapp.incture.workbox.demo.docusign.util.AccessToken;
import oneapp.incture.workbox.demo.docusign.util.DocusignConstant;
import oneapp.incture.workbox.demo.docusign.util.RestCall;



@Repository
//@Transactional
public class DocuSignParseServiceImpl implements DocuSignParseService {

	
	@Autowired
	AccessToken accessToken;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	RestCall restCall;
	
	@Autowired
	TaskEventsDao taskEventsDao;
	
	@Autowired
	ProcessEventsDao processEventsDao;
	
	@Autowired
	TaskOwnersDao taskOwnersDao;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,UserIDPMappingDto> fetchUsers() {
		UserIDPMappingDto userDetails = null;
		
		String userofSF = "Select user_login_name,user_email,user_first_name,user_id,user_last_name,docusign_id"
				+ " from user_idp_mapping where docusign_id is not null";
		Query sfUsersQuery = this.getSession().createSQLQuery(userofSF);
		List<Object[]> userList = sfUsersQuery.list();
		
		Map<String, UserIDPMappingDto> userMapping = new HashMap<>();
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
	private Map<String,UserIDPMappingDto> fetchUserdetails(String puserid) {
		UserIDPMappingDto userDetails = null;
		System.err.println(userDetails);
		String userofSF = "Select user_login_name,user_email,user_first_name,user_id,user_last_name,docusign_id"
				+ " from user_idp_mapping where docusign_id is not null";
		Query sfUsersQuery = this.getSession().createSQLQuery(userofSF);
		List<Object[]> userList = sfUsersQuery.list();
		
		Map<String, UserIDPMappingDto> userMapping = new HashMap<>();
		for (Object[] obj : userList) {
			
			userDetails = new UserIDPMappingDto();
			userDetails.setUserEmail((String)obj[1]);
			userDetails.setUserFirstName((String)obj[2]);
			userDetails.setUserId((String)obj[3]);
			userDetails.setUserLastName((String)obj[4]);
			userDetails.setUserLoginName((String)obj[0]);
			userDetails.setDocusignId((String)obj[5]);
			
			userMapping.put((String)obj[0], userDetails);
		}
		return userMapping;
	}
	public GenericResponseDto parseDocuSign(){
		System.err.println("[WBP-Dev][Docusign][EnvelopeService][parseDocuSign]");
		Map<String,UserIDPMappingDto> users = fetchUsers();
		System.err.println(users);
		String accessTokenEnvelope = accessToken.getJwtAccessToken();
		GenericResponseDto genericResponseDto = new GenericResponseDto();
		 Object[] obj = new Object[4];
		RestResponse processResponse = getEnvelopes(accessTokenEnvelope);
		List<TaskEventsDto> taskEventsDtos = new ArrayList<TaskEventsDto>();
		List<ProcessEventsDto> processEventsDtos = new ArrayList<ProcessEventsDto>();
		List<TaskOwnersDto> ownersDtos = new ArrayList<TaskOwnersDto>();
		List<TaskAuditDto> audits = new ArrayList<TaskAuditDto>();
		TaskAndAuditDto taskAndAuditDto = new TaskAndAuditDto();

		
		processEventsDtos = setProcessDto(processResponse,users);
		System.err.println(processEventsDtos);
		
		if(!ServicesUtil.isEmpty(processEventsDtos)&&processResponse.getResponseCode()==200){
			for (ProcessEventsDto processIterator : processEventsDtos) {
				TaskOwnersDto owner = new TaskOwnersDto();
				List<TaskEventsDto> taskWithSameOwner = new ArrayList<TaskEventsDto>();
				RestResponse taskResponse = getRecipients(accessTokenEnvelope,processIterator.getProcessId());
				JSONObject jsonObject = (JSONObject)taskResponse.getResponseObject();
				System.err.println(jsonObject.toString());
				if(!ServicesUtil.isEmpty(taskResponse.getResponseObject())&&taskResponse.getResponseCode()==200){
					taskAndAuditDto = setTaskAndAudit(taskResponse, processIterator,users);
					taskEventsDtos.addAll(taskAndAuditDto.getTasks());
					audits.addAll(taskAndAuditDto.getTaskaudits());
					ownersDtos.addAll(setOwners(taskResponse,users));
				}
				
			}

			obj[0] = processEventsDtos;
			obj[1] = taskEventsDtos;
			obj[2] = ownersDtos;
			obj[3] = audits;
			genericResponseDto.setData(obj);
			genericResponseDto.setMessage("SUCCESS");
			genericResponseDto.setStatus("200");
		}
		genericResponseDto.setMessage("NO NEW TASK OR PROCESS");
		genericResponseDto.setStatus("200");
		
		return genericResponseDto;
		
	}

	
	
	public RestResponse getEnvelopes(String accessTokenEnvelope){
		System.err.println("[WBP-Dev][Docusign][EnvelopeService][getEnvelopes]");

		RestResponse restResponse = new RestResponse();
		Instant instant = Instant.now().minusMillis(1000*60*5);
		String requestUrl = DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes?from_date=2020-11-28";
		restResponse = restCall.callGetService(requestUrl, accessTokenEnvelope);
		return restResponse;
	}
	
	public RestResponse getEnvelopes(String accessTokenEnvelope,String envelopeID){
		System.err.println("[WBP-Dev][Docusign][EnvelopeService][getEnvelopeWithEnvelopeId]");

		List<ProcessEventsDto> processEventsDtos = new ArrayList<ProcessEventsDto>();
		RestResponse restResponse = new RestResponse();
		int code=500;
		Date date = new Date();
		Date currentDate = new Date(System.currentTimeMillis() - 60 * 5 * 1000);
		String dateString = null;
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateString = sdfr.format(currentDate);
		String requestUrl = DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes?envelope_ids="+envelopeID;
		restResponse = restCall.callGetService(requestUrl, accessTokenEnvelope);
		System.err.println("getenvelope"+restResponse);
		return restResponse;
	}
	public RestResponse getRecipients(String accessTokenRecipient,String envelopeId){
		System.err.println("[WBP-Dev][Docusign][EnvelopeService][getRecipients]");
		RestResponse restResponse = new RestResponse();
		String requestUrl = DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes/"+envelopeId+"/recipients";
		System.out.println(requestUrl);
		restResponse = restCall.callGetService(requestUrl, accessTokenRecipient);
		return restResponse;
	}
	
	public SigningURLDto getSigningUrl(String envelopeId,String pUserId){
		Map<String,UserIDPMappingDto> user = fetchUserdetails(pUserId);
		String accessTokenSigning = accessToken.getJwtAccessTokenUserId(user.get(pUserId).getDocusignId());
		System.err.println("[WBP-Dev][Docusign][EnvelopeService][getSigningURL]"+accessTokenSigning);
		SigningURLDto urlDto = new SigningURLDto();
		urlDto.setResponsecode(500);
		urlDto.setMessage("FAILURE");
		String payload = "{\"userName\":\""+user.get(pUserId).getDocusignId()+"\",\"email\":\""+user.get(pUserId).getUserEmail()+"\",\"returnUrl\":\"https://workbox-kbniwmq1aj.dispatcher.hana.ondemand.com/index.html?hc_reset#/UnifiedInbox\",\"authenticationMethod\":\"Email\"}";
		String requestUrl = DocusignConstant.baseUrl+"/v2.1/accounts/"+DocusignConstant.accountIdIncture+"/envelopes/"+envelopeId+"/views/recipient";
		System.err.println(payload+requestUrl);
		RestResponse response = restCall.callPostService(requestUrl, accessTokenSigning, payload);
		System.err.println(response.toString());
		if(response.getResponseCode()==201){
			JSONObject urlObject = (JSONObject)response.getResponseObject();
			urlDto.setUrl(ServicesUtil.isEmpty(urlObject.optString("url"))? null: urlObject.getString("url"));
			urlDto.setResponsecode(200);
			urlDto.setMessage("SUCCESS");
		}
	
		return urlDto;
	}
	
	
	public List<ProcessEventsDto> setProcessDto(RestResponse response,Map<String,UserIDPMappingDto> users){
		System.err.println("[WBP-Dev][Docusign][EnvelopeService][setProcessDto]");
		JSONObject jsonObject =(JSONObject)response.getResponseObject();
		List<ProcessEventsDto> processEventsDtos = new ArrayList<ProcessEventsDto>();
		if(!ServicesUtil.isEmpty(response)&&response.getResponseCode()==200){
			JSONArray jsonArray =jsonObject.getJSONArray("envelopes");
			for (Object object : jsonArray) {
				JSONObject envelope = (JSONObject)object;
				JSONObject CreatedBy =(JSONObject) envelope.get("sender");
				ProcessEventsDto process = new ProcessEventsDto();
				//++++++
				if("completed".equals(envelope.getString("status"))||"voided".equals(envelope.get("status"))||"declined".equals(envelope.getString("status"))){
					process.setStatus("COMPLETED");
				}
				else{
					process.setStatus("RUNNING");
				}
				process.setProcessId(envelope.getString("envelopeId"));
				process.setRequestId(envelope.getString("envelopeId"));
				if(!ServicesUtil.isEmpty(envelope.optString("emailSubject"))){
					if(envelope.getString("emailSubject").contains(":")){
						process.setSubject("Docusign : Signing for"+envelope.getString("emailSubject").split(":")[1]);
					}
					else{
						process.setSubject("DocusignConstant :"+envelope.getString("emailSubject"));
					}
				}
				process.setStartedBy(users.get(CreatedBy.getString("userId")).getUserId());
				process.setStartedAt(ServicesUtil.convertAdminFromStringToDateUTC(envelope.getString("createdDateTime")));
				if(!ServicesUtil.isEmpty(envelope.optString("completedDateTime"))){
					process.setCompletedAt(ServicesUtil.isEmpty(envelope.optString("completedDateTime")) ? null
							: ServicesUtil.convertAdminFromStringToDateUTC(
									envelope.optString("completedDateTime")));
				}
				else{
					process.setCompletedAt(ServicesUtil.isEmpty(envelope.optString("declinedDateTime")) ? null
						: ServicesUtil.convertAdminFromStringToDateUTC(
								envelope.optString("declinedDateTime")));
				}
//				process.setCompletedAt(ServicesUtil.isEmpty(envelope.optString("completedDateTime")) ? null
//						: ServicesUtil.convertAdminFromStringToDateUTC(
//								envelope.optString("completedDateTime")));
				process.setStartedByDisplayName(users.get(CreatedBy.getString("userId")).getUserFirstName()+" "
						+users.get(CreatedBy.getString("userId")).getUserLastName());
				process.setName("SIGNING");
				process.setProcessDefinitionId(envelope.getString("envelopeId"));
				process.setStartedByUser(CreatedBy.getString("userId"));
				process.setProcessDisplayName("SIGNING");
				processEventsDtos.add(process);
			}
			return processEventsDtos;
		}
		return null;
		
	}
	
	public TaskAndAuditDto setTaskAndAudit(RestResponse taskResponse,ProcessEventsDto process,Map<String,UserIDPMappingDto> users){
		TaskAndAuditDto taskAndAudit = new TaskAndAuditDto();
		List<TaskAuditDto> auditList = new ArrayList<TaskAuditDto>();
		System.err.println("[WBP-Dev][Docusign][EnvelopeService][setTask]");
		long time = System.currentTimeMillis();
		String lastSignedDate=new String();
		List<TaskEventsDto> taskEventsDtos = new ArrayList<TaskEventsDto>();
		if(!ServicesUtil.isEmpty(taskResponse.getResponseObject())&&taskResponse.getResponseCode()==200){
			JSONObject jsonObject = (JSONObject)taskResponse.getResponseObject();
			JSONArray recipients = jsonObject.getJSONArray("signers");
			JSONArray carboncopies = jsonObject.getJSONArray("carbonCopies");
			if(!ServicesUtil.isEmpty(carboncopies)){
				for (Object object : carboncopies) {
					recipients.put((JSONObject)object);
				}				
			}
			if(!ServicesUtil.isEmpty(recipients)){
				for (Object recipientObject : recipients) {
					JSONObject recipient = (JSONObject) recipientObject;
					boolean isaccount = users.containsKey(recipient.getString("userId"));
					//++++++
					if(("sent".equals(recipient.getString("status"))||"delivered".equals(recipient.getString("status"))||"completed".equals(recipient.getString("status"))||"autoresponded".equals(recipient.getString("status"))||"declined".equals(recipient.getString("status")))&&isaccount==true){
						TaskEventsDto task = new TaskEventsDto();
						task.setRequestId(process.getRequestId());
						task.setEventId(recipient.getString("recipientIdGuid"));
						
						
						if(!ServicesUtil.isEmpty(recipient.optString("routingOrder"))){
							if("1".equals(recipient.optString("routingOrder"))){
								task.setCreatedAt(ServicesUtil.isEmpty(process.getStartedAt())? null : process.getStartedAt());
							}
							else if (!ServicesUtil.isEmpty(lastSignedDate)) {
								task.setCreatedAt(ServicesUtil.convertAdminFromStringToDateUTC(lastSignedDate));
							}
						}
						if(!ServicesUtil.isEmpty(recipient.optString("signedDateTime"))){
							lastSignedDate=recipient.optString("signedDateTime");
						}
						
						 
						task.setProcessId(process.getProcessId());
						//task.setDescription("Signing by"+recipient.getString("name")+" ROLE: "+(ServicesUtil.isEmpty(recipient.optString("roleName")) ? "No Roles Assigned":recipient.optString("roleName")));
						//task.setSubject("Signing by "+recipient.getString("name"));
						task.setDescription(process.getSubject());
						task.setSubject(process.getSubject());
						task.setName("SIGNER");
						//++++++
						if("completed".equals(recipient.get("status"))||"autoresponded".equals(recipient.getString("status"))||"declined".equals(recipient.getString("status"))){
							task.setStatus("COMPLETED");
							task.setCurrentProcessor(users.get(recipient.getString("userId")).getUserId());
							task.setCurrentProcessorDisplayName(users.get(recipient.getString("userId")).getUserFirstName()+" "+
									users.get(recipient.getString("userId")).getUserLastName());
							TaskAuditDto audit = new TaskAuditDto();
							audit.setAuditId(recipient.getString("recipientId")+recipient.getString("recipientIdGuid"));
							if("declined".equals(recipient.getString("status"))){
								audit.setAction("Rejected");
							}
							else{
								audit.setAction("Approved");
							}
							audit.setCompletedAt(ServicesUtil.convertAdminFromStringToDateUTC(recipient.optString("signedDateTime")));
							audit.setCompletedAtString(recipient.optString("signedDateTime"));
							audit.setEventId(recipient.optString("recipientIdGuid"));
							audit.setUpdatedAt(ServicesUtil.convertAdminFromStringToDateUTC(recipient.optString("signedDateTime")));
							audit.setUpdatedAtString(recipient.optString("signedDateTime"));
							audit.setUserId(users.get(recipient.getString("userId")).getUserId());
							audit.setUserName(users.get(recipient.getString("userId")).getUserFirstName()+" "+
									users.get(recipient.getString("userId")).getUserLastName());
							auditList.add(audit);
							System.err.println("GSON"+new Gson().toJson(auditList));
						}
						else{
							task.setStatus("READY");
						}
						
						if(!ServicesUtil.isEmpty(recipient.optString("signedDateTime"))){
							task.setCompletedAt(ServicesUtil.isEmpty(recipient.optString("signedDateTime")) ? null
									: ServicesUtil.convertAdminFromStringToDateUTC(
											recipient.optString("signedDateTime")));
						}
						else{
							task.setCompletedAt(ServicesUtil.isEmpty(recipient.optString("declinedDateTime")) ? null
									: ServicesUtil.convertAdminFromStringToDateUTC(
											recipient.optString("declinedDateTime")));
						}
						
						
//						task.setCompletedAt(ServicesUtil.isEmpty(recipient.optString("signedDateTime")) ? null
//								: ServicesUtil.convertAdminFromStringToDateUTC(
//										recipient.optString("signedDateTime")));
						task.setPriority("MEDIUM");
						if(!ServicesUtil.isEmpty(task.getCreatedAt())){
						task.setCompletionDeadLine(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 3)));
						}
						task.setProcessName("SIGINING");
						if(!ServicesUtil.isEmpty(task.getCreatedAt())){
						task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 2)));
						}
						task.setOrigin("DOCUSIGN");
						task.setOwnersName(process.getStartedBy());
						task.setCreatedBy(process.getStartedBy());
						Date date = new Date(System.currentTimeMillis());
						task.setUpdatedAt(date);
						if(process.getStatus().equals("COMPLETED")){
							task.setStatus("COMPLETED");
						}
						
						System.err.println(task.toString());
						taskEventsDtos.add(task);
					}
					
					
				}
				taskAndAudit.setTasks(taskEventsDtos);
				taskAndAudit.setTaskaudits(auditList);
				return taskAndAudit;
			}
		}
		return null;
	}
	
	
	
	public List<TaskOwnersDto> setOwners(RestResponse taskResponse,Map<String,UserIDPMappingDto> users){
		System.err.println("[WBP-Dev][Docusign][EnvelopeService][setOwners]");
		List<TaskOwnersDto> taskOwnersDtos = new ArrayList<>();
		if(!ServicesUtil.isEmpty(taskResponse.getResponseObject())&&taskResponse.getResponseCode()==200){
			JSONObject jsonObject = (JSONObject)taskResponse.getResponseObject();
			JSONArray recipients = jsonObject.getJSONArray("signers");
			if(!ServicesUtil.isEmpty(recipients)){
				for (Object object : recipients) {
					JSONObject recipient = (JSONObject)object;
					boolean isaccount = users.containsKey(recipient.getString("userId"));
					if(("sent".equals(recipient.getString("status"))||"delivered".equals(recipient.getString("status"))||"completed".equals(recipient.getString("status"))||"autoresponded".equals(recipient.getString("status")))&&isaccount==true){
						TaskOwnersDto ownersDto = new TaskOwnersDto();
						ownersDto.setEventId(recipient.getString("recipientIdGuid"));
						ownersDto.setTaskOwner(users.get(recipient.getString("userId")).getUserId());
						ownersDto.setTaskOwnerDisplayName(users.get(recipient.getString("userId")).getUserFirstName()+" "+
															users.get(recipient.getString("userId")).getUserLastName());
						ownersDto.setOwnerEmail(users.get(recipient.getString("userId")).getUserEmail());
						taskOwnersDtos.add(ownersDto);
					}
				}
			}
			return taskOwnersDtos;
			
		}
		
		return null;
		
		
	}
	
}
