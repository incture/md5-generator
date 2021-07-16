package oneapp.incture.workbox.demo.adapterJira.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapterJira.dao.JiraIssuesDao;
import oneapp.incture.workbox.demo.adapterJira.dto.AttributeGenericDto;
import oneapp.incture.workbox.demo.adapterJira.dto.CommentDto;
import oneapp.incture.workbox.demo.adapterJira.dto.ScreenGenericDto;
import oneapp.incture.workbox.demo.adapterJira.dto.StatusDto;
import oneapp.incture.workbox.demo.adapterJira.util.JiraConstants;
import oneapp.incture.workbox.demo.adapterJira.util.RestUserJira;
import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;



@Service
//@Transactional
public class JiraServiceImpl implements JiraService {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	RestUserJira restUserJira;

	@Autowired
	private ProcessEventsDao processEventsDao;

	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private TaskOwnersDao taskOwnersDao;

	@Autowired
	private CustomAttributeDao customAttributeDao;
	
	@Autowired
	private JiraActionFacade jiraActionFacade;
	
	@Autowired
	private JiraIssuesDao jiraIssuesDao;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public ResponseMessage setAll() {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Something went wrong");
		responseMessage.setStatus("Failure");
		responseMessage.setStatusCode("1");
		try {
			Map<String, UserIDPMappingDto> userLIst = jiraIssuesDao.fetchUsers(0);
			List<ProcessEventsDto> processEventsDtos = setProcess(userLIst);
			List<TaskEventsDto> taskEventDtos = setTask(userLIst);
			List<TaskOwnersDto> taskOwnersDtos = setTaskOwner(userLIst);
			List<CustomAttributeValue> customAttributeValuesDtos = setCustomAttributeValue();
			processEventsDao.saveOrUpdateProcesses(processEventsDtos);
			taskEventsDao.saveOrUpdateTasks(taskEventDtos);
			taskOwnersDao.saveOrUpdateOwners(taskOwnersDtos);
			customAttributeDao.addCustomAttributeValue(customAttributeValuesDtos);
			responseMessage.setMessage("Successfully updated");
			responseMessage.setStatus("Success");
			responseMessage.setStatusCode("200");
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][getAll]Error" + e);
		}
		return responseMessage;
	}

	public List<ProcessEventsDto> setProcess(Map<String, UserIDPMappingDto> userList) {
		JSONObject myObject  = restUserJira.getAPI();
		List<ProcessEventsDto> processes = new ArrayList<>();
		JSONArray myArray = myObject.getJSONArray("issues");

		try {
			for (Object object : myArray) {
				ProcessEventsDto process = new ProcessEventsDto();
				JSONObject jsonObject = (JSONObject) object;
				String issueId = jsonObject.getString("id");
				String processName = ((JSONObject) jsonObject).getString("key");
				jsonObject = (JSONObject) ((JSONObject) object).get("fields");
				String issueName = ((JSONObject) jsonObject.get("issuetype")).getString("name");
				if(!issueName.equals("Epic")){
//					process.setProcessId(((JSONObject) object).getString("id"));
//					process.setRequestId(((JSONObject) object).getString("id"));
					process.setProcessId(issueId);
					process.setRequestId(issueId);	
					process.setName(((JSONObject) jsonObject.get("project")).getString("name"));
					process.setSubject("JIRA : Issue " + processName);
					process.setStatus("RUNNING");
					process.setStartedBy(userList.get((((JSONObject) jsonObject.get("creator")).getString("accountId"))).getUserId());
					process.setStartedAt(ServicesUtil.isEmpty(jsonObject.optString("created")) ? null
							: ServicesUtil.convertFromStringToDateJira(jsonObject.optString("created")));
					process.setStartedByUser(
							userList.get((((JSONObject) jsonObject.get("creator")).getString("accountId"))).getUserId());
					process.setStartedByDisplayName(userList
							.get((((JSONObject) jsonObject.get("creator")).getString("accountId"))).getUserFirstName() + " "
							+ userList.get((((JSONObject) jsonObject.get("creator")).getString("accountId")))
									.getUserLastName());
//					process.setProcessDisplayName(((JSONObject) object).getString("key"));
//					process.setProcessDefinitionId(((JSONObject) object).getString("id"));
					process.setProcessDisplayName(processName);
					process.setProcessDefinitionId(issueId);
					processes.add(process);
				}
				
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][setProcess]Error" + e);
		}
		return processes;
	}

	public List<TaskEventsDto> setTask(Map<String, UserIDPMappingDto> userList) {
		JSONObject issueObject = restUserJira.getAPI();
		List<TaskEventsDto> tasks = new ArrayList<>();
		JSONArray issueArray = issueObject.getJSONArray("issues");

		try {
			Date updated = null;
			Date createdAt = null;
			for (Object issues : issueArray) {
				JSONObject issue = (JSONObject) issues;
				String issueId = issue.getString("id");
				String processName = ((JSONObject) issue).getString("key");
				issue = (JSONObject) ((JSONObject) issue).get("fields");
				String issueName = ((JSONObject) issue.get("issuetype")).getString("name");
				if(!issueName.equals("Epic")){
					String date = ServicesUtil.getJiraDateString(issue.getString("statuscategorychangedate"));
//					String description = ((JSONObject) issue.get("issuetype")).getString("description");
					String description = ((JSONObject) issue).getString("summary");
					String currentProcessorId = userList.get((((JSONObject) issue.get("assignee")).getString("accountId")))
							.getUserId();
					String currentProcessorDisplayName = userList
							.get((((JSONObject) issue.get("assignee")).getString("accountId"))).getUserFirstName() + " "
							+ userList.get((((JSONObject) issue.get("assignee")).getString("accountId"))).getUserLastName();
					String priority = ((JSONObject) issue.get("priority")).getString("name");
//					Date createdAt = ServicesUtil.isEmpty(issue.optString("statuscategorychangedate")) ? null
//							: ServicesUtil.convertFromStringToDateJira(issue.optString("statuscategorychangedate")); 
//					Date createdAt = ServicesUtil.convertFromStringToDateJira(issue.getString("statuscategorychangedate"));
					String ownersName = userList.get((((JSONObject) issue.get("assignee")).getString("accountId")))
							.getUserFirstName() + " "
							+ userList.get((((JSONObject) issue.get("assignee")).getString("accountId"))).getUserLastName();
					Date updatedAt = ServicesUtil.isEmpty(issue.optString("updated")) ? null
							: ServicesUtil.convertFromStringToDateJira(issue.optString("updated"));
					String createdBy = userList.get((((JSONObject) issue.get("creator")).getString("accountId")))
							.getUserFirstName() + " "
							+ userList.get((((JSONObject) issue.get("creator")).getString("accountId"))).getUserLastName();
					
					
					TaskEventsDto task = new TaskEventsDto();
					task.setRequestId(issueId + date);
					task.setEventId(issueId + date);
					task.setProcessId(issueId);
					task.setDescription("JIRA - " + processName + " : " + description);
					task.setSubject("JIRA : Issue " + processName);
					task.setName(issueName);
//					String status1 = ((JSONObject) issue.get("status")).getString("name");
					task.setStatus("RESERVED");
//					if (status1.equals("Done")) {
//						task.setStatus("COMPLETED");
//					}					
//					if (task.getStatus().equals("COMPLETED") || task.getStatus().equals("RESERVED")) {
						task.setCurrentProcessor(currentProcessorId);
						task.setCurrentProcessorDisplayName(currentProcessorDisplayName);
//					}
					if(ServicesUtil.isEmpty(updated)) {
						createdAt = ServicesUtil.isEmpty(issue.optString("created")) ? null
								: ServicesUtil.convertFromStringToDateJira(issue.optString("created"));
						
					}
					else {
						createdAt = updated;
					}
					updated = ServicesUtil.isEmpty(issue.optString("statuscategorychangedate")) ? null
							: ServicesUtil.convertFromStringToDateJira(issue.optString("statuscategorychangedate"));
//					updated = ServicesUtil.getJiraDateString(issue.getString("statuscategorychangedate"));
//					task.setCreatedAt(ServicesUtil.convertFromStringToDateJira(date));
					task.setCreatedAt(createdAt);				
//					if (task.getStatus().equals("COMPLETED")) {
//						task.setCompletedAt(updated);
//					}
					updated = createdAt;
					task.setPriority(priority);
					task.setCompletionDeadLine(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
					task.setOwnersName(ownersName);
					task.setProcessName(processName);
					task.setOrigin("JIRA");
					task.setSlaDueDate(new Date(task.getCreatedAt().getTime() + (1000 * 60 * 60 * 24 * 5)));
					task.setUpdatedAt(updatedAt);
					task.setCreatedBy(createdBy);
					tasks.add(task);
					
				}
				
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][setTask]Error" + e);
		}
		return tasks;
	}

	public List<TaskOwnersDto> setTaskOwner(Map<String, UserIDPMappingDto> userList) {
		JSONObject myObject = restUserJira.getAPI();
		List<TaskOwnersDto> taskOwners = new ArrayList<>();
		JSONArray myArray = myObject.getJSONArray("issues");

		try {
			for (Object object : myArray) {
				TaskOwnersDto taskOwner = new TaskOwnersDto();
				JSONObject jsonObject = (JSONObject) ((JSONObject) object).get("fields");
				String issueName = ((JSONObject) jsonObject.get("issuetype")).getString("name");
				if(!issueName.equals("Epic")){
					String date = ServicesUtil.getJiraDateString(jsonObject.getString("statuscategorychangedate"));
					taskOwner.setEventId(((JSONObject) object).getString("id") + date);
					taskOwner.setTaskOwner(ServicesUtil.isEmpty(
							userList.get((((JSONObject) jsonObject.get("assignee")).getString("accountId"))).getUserId())
									? null
									: userList.get((((JSONObject) jsonObject.get("assignee")).getString("accountId")))
											.getUserId());
					taskOwner.setTaskOwnerDisplayName(userList
							.get((((JSONObject) jsonObject.get("assignee")).getString("accountId"))).getUserFirstName()
							+ " " + userList.get((((JSONObject) jsonObject.get("assignee")).getString("accountId")))
									.getUserLastName());
					taskOwner.setOwnerEmail(ServicesUtil.isEmpty(
							userList.get((((JSONObject) jsonObject.get("assignee")).getString("accountId"))).getUserEmail())
									? null
									: userList.get((((JSONObject) jsonObject.get("assignee")).getString("accountId")))
											.getUserEmail());
					taskOwners.add(taskOwner);
				}
				
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][setTaskOwner]Error" + e);
		}
		return taskOwners;
	}

	public List<CustomAttributeValue> setCustomAttributeValue() {
		JSONObject myObject = restUserJira.getAPI();
		List<CustomAttributeValue> customAttributeValues = new ArrayList<>();
		JSONArray myArray = myObject.getJSONArray("issues");
		
		try {
			for (Object object : myArray) {
				JSONObject jsonObject = (JSONObject) object;
				String issueKey = ((JSONObject) jsonObject).getString("key");
				jsonObject = (JSONObject) ((JSONObject) object).get("fields");
				String issueName = ((JSONObject) jsonObject.get("issuetype")).getString("name");
				if(!issueName.equals("Epic")){
					String date = ServicesUtil.getJiraDateString(jsonObject.getString("statuscategorychangedate"));
					CustomAttributeValue customAttributeValue = new CustomAttributeValue();
					customAttributeValue.setTaskId(((JSONObject) object).getString("id") + date);
					customAttributeValue.setProcessName(((JSONObject) jsonObject.get("project")).getString("name"));
					customAttributeValue.setKey("Project");
					customAttributeValue.setAttributeValue(((JSONObject) jsonObject.get("project")).getString("name"));
					customAttributeValues.add(customAttributeValue);

					CustomAttributeValue customAttributeValue1 = new CustomAttributeValue();
					customAttributeValue1.setTaskId(((JSONObject) object).getString("id") + date);
					customAttributeValue1.setProcessName(((JSONObject) jsonObject.get("project")).getString("name"));
					customAttributeValue1.setKey("Issue Type");
					customAttributeValue1.setAttributeValue(((JSONObject) jsonObject.get("issuetype")).getString("name"));
					customAttributeValues.add(customAttributeValue1);

					CustomAttributeValue customAttributeValue2 = new CustomAttributeValue();
					customAttributeValue2.setTaskId(((JSONObject) object).getString("id") + date);
					customAttributeValue2.setProcessName(((JSONObject) jsonObject.get("project")).getString("name"));
					customAttributeValue2.setKey("Summary");
					customAttributeValue2.setAttributeValue(((JSONObject) jsonObject).getString("summary"));
					customAttributeValues.add(customAttributeValue2);

					CustomAttributeValue customAttributeValue3 = new CustomAttributeValue();
					customAttributeValue3.setTaskId(((JSONObject) object).getString("id") + date);
					customAttributeValue3.setProcessName(((JSONObject) jsonObject.get("project")).getString("name"));
					customAttributeValue3.setKey("Priority");
					customAttributeValue3.setAttributeValue(((JSONObject) jsonObject.get("priority")).getString("name"));
					customAttributeValues.add(customAttributeValue3);
					
					CustomAttributeValue customAttributeValue4 = new CustomAttributeValue();
					customAttributeValue4.setTaskId(((JSONObject) object).getString("id") + date);
					customAttributeValue4.setProcessName(((JSONObject) jsonObject.get("project")).getString("name"));
					customAttributeValue4.setKey("Issue");
					customAttributeValue4.setAttributeValue(issueKey);
					customAttributeValues.add(customAttributeValue4);
					
					CustomAttributeValue customAttributeValue5 = new CustomAttributeValue();
					customAttributeValue5.setTaskId(((JSONObject) object).getString("id") + date);
					customAttributeValue5.setProcessName(((JSONObject) jsonObject.get("project")).getString("name"));
					customAttributeValue5.setKey("Sprint");
					JSONArray sprint = jsonObject.getJSONArray("customfield_10020");
					customAttributeValue5.setAttributeValue(((JSONObject)sprint.get(0)).getString("name"));
					customAttributeValues.add(customAttributeValue5);
					
					CustomAttributeValue customAttributeValue6 = new CustomAttributeValue();
					customAttributeValue6.setTaskId(((JSONObject) object).getString("id") + date);
					customAttributeValue6.setProcessName(((JSONObject) jsonObject.get("project")).getString("name"));
					customAttributeValue6.setKey("Status");
					customAttributeValue6.setAttributeValue(((JSONObject) jsonObject.get("status")).getString("name"));
					customAttributeValues.add(customAttributeValue6);
					
//					CustomAttributeValue customAttributeValue7 = new CustomAttributeValue();
//					customAttributeValue7.setTaskId(((JSONObject) object).getString("id") + date);
//					customAttributeValue7.setProcessName(((JSONObject) jsonObject.get("project")).getString("name"));
//					customAttributeValue7.setKey("Assignee");
//					customAttributeValue7.setAttributeValue("");
//					customAttributeValues.add(customAttributeValue7);
//					
//					CustomAttributeValue customAttributeValue8 = new CustomAttributeValue();
//					customAttributeValue8.setTaskId(((JSONObject) object).getString("id") + date);
//					customAttributeValue8.setProcessName(((JSONObject) jsonObject.get("project")).getString("name"));
//					customAttributeValue8.setKey("Comments");
//					customAttributeValue8.setAttributeValue("");
//					customAttributeValues.add(customAttributeValue8);
				}
				
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][setCAV]Error" + e);
		}
		return customAttributeValues;
	}
	
	public List<StatusDto> setStatusDropdown(String taskId) {
		List<StatusDto> statuses = new ArrayList<>();
		String processName = jiraIssuesDao.getIssueName(taskId);
		
		try {
				RestResponse statusResponse = restUserJira
						.callGetResponse(JiraConstants.baseUrl + "/issue/" + processName + "/transitions");
				JSONObject statusObject = (JSONObject) statusResponse.getResponseObject();
				JSONArray statusArr = statusObject.getJSONArray("transitions");
				
				for (Object object : statusArr) {
					JSONObject obj = (JSONObject) object;
					StatusDto status1 = new StatusDto();
//					status1.setProcessName(processName);
					status1.setStatusId(obj.getString("id"));
					status1.setStatusName(obj.getString("name"));
					statuses.add(status1);
			}
		}
			catch (Exception e) {
				System.err.println("[WBP-Dev][JIRA][setStatusDropdown]Error" + e);
			}
				
		return statuses;
	}
	
	public List<AttributeGenericDto> fetchAssignee() {
		Map<String, UserIDPMappingDto> userList = jiraIssuesDao.fetchUsers(0);
		List<AttributeGenericDto> assignees = new ArrayList<>();
		
		try {
			RestResponse assigneeResponse = restUserJira.callGetResponseArray(JiraConstants.baseUrl + "/user/assignable/multiProjectSearch?projectKeys=WP");
			System.err.println(assigneeResponse.getResponseObject());
			JSONArray assigneeArray = new JSONArray((String) assigneeResponse.getResponseObject());
			
			for(Object assigneeObject : assigneeArray) {
				JSONObject assigneeObj = (JSONObject) assigneeObject;
				AttributeGenericDto assignee = new AttributeGenericDto();
				assignee.setAttributeId(userList.get(((JSONObject) assigneeObj).getString("accountId")).getUserId());
				assignee.setAttributeValue(userList.get(((JSONObject) assigneeObj).getString("accountId")).getUserFirstName()
							+ " " + userList.get(((JSONObject) assigneeObj).getString("accountId")).getUserLastName());
				assignees.add(assignee);
				
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][fetchAssignee]Error" + e);
		}
		return assignees;	
	}
	
	public List<AttributeGenericDto> fetchResolution() {
		List<AttributeGenericDto> resolutions = new ArrayList<>();
		
		try {
			RestResponse resolutionResponse = restUserJira.callGetResponseArray(JiraConstants.baseUrl + "/resolution");
			System.err.println(resolutionResponse);
			JSONArray resolutionArray = new JSONArray((String) resolutionResponse.getResponseObject());
			
			for(Object resolutionObject : resolutionArray) {
				JSONObject resolutionObj = (JSONObject) resolutionObject;
				AttributeGenericDto resolution = new AttributeGenericDto();
				resolution.setAttributeId(resolutionObj.getString("id"));
				resolution.setAttributeValue(resolutionObj.getString("name"));
				resolutions.add(resolution);
			}	
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][fetchResolution]Error" + e);
		}
		return resolutions;	
	}
	
	public List<AttributeGenericDto> fetchDefaultRootCause() {
		List<AttributeGenericDto> defaultRootCauses = new ArrayList<>();
		
		try {
			RestResponse fieldresponse = restUserJira.callGetResponse(JiraConstants.baseUrl + "/issue/createmeta?projectKeys=WP&expand=projects.issuetypes.fields");
			System.err.println(fieldresponse);
			JSONObject projectsObject = (JSONObject) fieldresponse.getResponseObject();
			JSONObject projectObj = (JSONObject) projectsObject.getJSONArray("projects").get(0);
			
			JSONArray customFieldArray =((JSONObject) projectObj.getJSONArray("issuetypes").get(0)).getJSONObject("fields").getJSONObject("customfield_10029").getJSONArray("allowedValues");
			for(Object customFieldObj : customFieldArray) {
				AttributeGenericDto defaultRootCause = new AttributeGenericDto();
				defaultRootCause.setAttributeId(((JSONObject) customFieldObj).getString("id"));
				defaultRootCause.setAttributeValue(((JSONObject) customFieldObj).getString("value"));
				defaultRootCauses.add(defaultRootCause);
			}
//			for(Object projectsObj : projectsArray) {
//				JSONObject issueTypesObject = (JSONObject) projectsObj;
//				JSONArray issueTypesArray = issueTypesObject.getJSONArray("issuetypes");
//				for(Object issueTypesObj : issueTypesArray) {
//					JSONObject fieldsObject = (JSONObject) ((JSONObject) issueTypesObj).get("fields");
//					JSONObject customFieldObject = (JSONObject) ((JSONObject) fieldsObject).get("customfield_10029");
//					JSONArray customFieldArray = customFieldObject.getJSONArray("allowedValues");
//					for(Object customFieldObj : customFieldArray) {
//						JSONObject customObj = (JSONObject) customFieldObj;
//						defaultRootCauseDto.setDefaultRootCauseId(customObj.getString("id"));
//						defaultRootCauseDto.setDefaultRootCauseName(customObj.getString("value"));
//						defaultRootCauseDtos.add(defaultRootCauseDto);	
//					}	
//				}
//			}	
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][fetchDefaultRootCause]Error" + e);
		}
		return defaultRootCauses;	
	}
	
	public List<CommentDto> fetchComment(String eventId) {
		Map<String, UserIDPMappingDto> userList = jiraIssuesDao.fetchUsers(0);
		List<CommentDto> commentDtos = new ArrayList<>();
		String processName = jiraIssuesDao.getProcessName(eventId);
		
		try {
			RestResponse commentResponse = restUserJira.callGetResponse(JiraConstants.baseUrl + "/issue/" + processName + "/comment");
			System.err.println(commentResponse);
			JSONObject commentObject = (JSONObject) commentResponse.getResponseObject();
			JSONArray commentArray = commentObject.getJSONArray("comments");
			
			for (Object commentObj : commentArray) {
				JSONObject commentOb = (JSONObject) commentObj;
				CommentDto commentDto = new CommentDto();
				Date createdAt = ServicesUtil.isEmpty(commentOb.optString("created")) ? null
						: ServicesUtil.convertFromStringToDateJira(commentOb.optString("created"));
				Date updatedAt = ServicesUtil.isEmpty(commentOb.optString("updated")) ? null
						: ServicesUtil.convertFromStringToDateJira(commentOb.optString("updated"));
				
				commentDto.setCommentId(commentOb.getString("id"));
				commentDto.setAuthorDisplayName(userList.get((((JSONObject) commentOb.get("author")).getString("accountId")))
						.getUserFirstName() + " " + userList.get((((JSONObject) commentOb.get("author")).getString("accountId"))).getUserLastName());
				commentDto.setCommentBody(commentOb.getString("body"));
				commentDto.setUpdateAuthorDisplayName(userList.get((((JSONObject) commentOb.get("updateAuthor")).getString("accountId")))
						.getUserFirstName() + " " + userList.get((((JSONObject) commentOb.get("updateAuthor")).getString("accountId"))).getUserLastName());
				commentDto.setCreatedAt(createdAt);
				commentDto.setUpdatedAt(updatedAt);
				commentDtos.add(commentDto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][fetchComment]Error" + e);
		}
		return commentDtos;	
	}
	
	public List<ScreenGenericDto> fetchFields(String processName, String taskId) {
//		JSONObject myObject = restUserJira.getAPI();
		List<ScreenGenericDto> screenGenericDtos = new ArrayList<>();
//		JSONArray myArray = myObject.getJSONArray("issues");
		
		try {
//			for (Object object : myArray) {
//				JSONObject jsonObject = (JSONObject) object;
//				jsonObject = (JSONObject) ((JSONObject) object).get("fields");
//				String issueName = ((JSONObject) jsonObject.get("issuetype")).getString("name");
//				if(!issueName.equals("Epic")){
//					String date = ServicesUtil.getJiraDateString(jsonObject.getString("statuscategorychangedate"));
					ScreenGenericDto screenGenericDto1 = new ScreenGenericDto();
					screenGenericDto1.setTaskId(taskId);
					screenGenericDto1.setProcessName(processName);
					screenGenericDto1.setKey("Assignee");
//					screenGenericDto1.setAttributeValue(new Gson().toJson(fetchAssignee()));
					screenGenericDto1.setAttributeValue(fetchAssignee());
					screenGenericDto1.setDataType("DROPDOWN");
					screenGenericDtos.add(screenGenericDto1);
					
					ScreenGenericDto screenGenericDto2 = new ScreenGenericDto();
					screenGenericDto2.setTaskId(taskId);
					screenGenericDto2.setProcessName(processName);
					screenGenericDto2.setKey("Description");
					screenGenericDto2.setAttributeValue(null);
					screenGenericDto2.setDataType("TEXT");
					screenGenericDtos.add(screenGenericDto2);
					
					ScreenGenericDto screenGenericDto3 = new ScreenGenericDto();
					screenGenericDto3.setTaskId(taskId);
					screenGenericDto3.setProcessName(processName);
					screenGenericDto3.setKey("Resolution");
//					screenGenericDto3.setAttributeValue(new Gson().toJson(fetchResolution()));
					screenGenericDto3.setAttributeValue(fetchResolution());
					screenGenericDto3.setDataType("DROPDOWN");
					screenGenericDtos.add(screenGenericDto3);
					
//					ScreenGenericDto screenGenericDto4 = new ScreenGenericDto();
//					screenGenericDto4.setTaskId(taskId);
//					screenGenericDto4.setProcessName(processName);
//					screenGenericDto4.setKey("Start Date");
//					screenGenericDto4.setAttributeValue("");
//					screenGenericDto4.setDataType("DATETYPE");
//					screenGenericDtos.add(screenGenericDto4);
					
					ScreenGenericDto screenGenericDto4 = new ScreenGenericDto();
					screenGenericDto4.setTaskId(taskId);
					screenGenericDto4.setProcessName(processName);
					screenGenericDto4.setKey("Summary");
					screenGenericDto4.setAttributeValue(null);
					screenGenericDto4.setDataType("TEXT");
					screenGenericDtos.add(screenGenericDto4);
					
					ScreenGenericDto screenGenericDto5 = new ScreenGenericDto();
					screenGenericDto5.setTaskId(taskId);
					screenGenericDto5.setProcessName(processName);
					screenGenericDto5.setKey("Default Root Cause");
//					screenGenericDto5.setAttributeValue(new Gson().toJson(fetchDefaultRootCause()));
					screenGenericDto5.setAttributeValue(fetchDefaultRootCause());
					screenGenericDto5.setDataType("DROPDOWN");
					screenGenericDtos.add(screenGenericDto5);
					
					ScreenGenericDto screenGenericDto6 = new ScreenGenericDto();
					screenGenericDto6.setTaskId(taskId);
					screenGenericDto6.setProcessName(processName);
					screenGenericDto6.setKey("Comment");
					screenGenericDto6.setAttributeValue(null);
					screenGenericDto6.setDataType("TEXT");
					screenGenericDtos.add(screenGenericDto6);
//					}
//				} 
			} catch (Exception e) {
				System.err.println("[WBP-Dev][JIRA][fetchFields]Error" + e);
			}	
		return screenGenericDtos;
		
	}
	
	@Override
	public List<ProcessEventsDto> setProcess() {
		Map<String, UserIDPMappingDto> userLIst = jiraIssuesDao.fetchUsers(0);
		List<ProcessEventsDto> result = setProcess(userLIst);
		return result;
	}

	@Override
	public List<TaskEventsDto> setTask() {
		Map<String, UserIDPMappingDto> userLIst = jiraIssuesDao.fetchUsers(0);
		List<TaskEventsDto> result = setTask(userLIst);
		return result;
	}

	@Override
	public List<TaskOwnersDto> setTaskOwner() {
		Map<String, UserIDPMappingDto> userLIst = jiraIssuesDao.fetchUsers(0);
		List<TaskOwnersDto> result = setTaskOwner(userLIst);
		return result;
	}

	@Override
	public ResponseMessage completeTransitionOrForward(ActionDto actionDto, ActionDtoChild actionDtoChild) {
		Map<String, UserIDPMappingDto> userList = jiraIssuesDao.fetchUsers(1);
		return jiraActionFacade.completeTransitionOrForward(actionDto, actionDtoChild);
	}
}