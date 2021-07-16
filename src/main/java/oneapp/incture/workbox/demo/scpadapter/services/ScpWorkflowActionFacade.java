package oneapp.incture.workbox.demo.scpadapter.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sap.cloud.security.xsuaa.token.XsuaaToken;

import oneapp.incture.workbox.demo.adapter_base.dao.InboxActionsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskAuditDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionRequestContextDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionRequestDto;
import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.entity.InboxActions;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskStatus;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystContextDto;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystFilterLineItemAndForm;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystFormData;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystLineItemsAndForms;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystsLineItems;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstantsStatic;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.notification.dto.PushNotificationDto;
import oneapp.incture.workbox.demo.notification.service.PushNotificationService;
import oneapp.incture.workbox.demo.scpadapter.util.AdminParse;
import oneapp.incture.workbox.demo.scpadapter.util.AdminParse.AdminParseResponse;
import oneapp.incture.workbox.demo.scpadapter.util.OAuth;
import oneapp.incture.workbox.demo.scpadapter.util.RestUtil;
import oneapp.incture.workbox.demo.scpadapter.util.SCPRestUtil;
import oneapp.incture.workbox.demo.scpadapter.util.SCPRestUtil.RestUtilAuth;
import oneapp.incture.workbox.demo.scpadapter.util.Workflow;
import oneapp.incture.workbox.demo.substitution.dto.UserTaskMappingDto;
import oneapp.incture.workbox.demo.substitution.services.SubstitutionRuleFacadeLocal;
import oneapp.incture.workbox.demo.workflowauthentication.util.WorkflowConstants;
import oneapp.incture.workbox.demo.workflowauthentication.util.WorkflowUtil;

@Service("ScpWorkflowActionFacade")
//////@Transactional
public class ScpWorkflowActionFacade implements ScpWorkflowActionFacadeLocal {

	@Autowired
	TaskEventsDao taskEvents;

	@Autowired
	PushNotificationService pushNotificationService;

	@Autowired
	SubstitutionRuleFacadeLocal substitutionService;

	@Autowired
	WorkflowUtil workflowUtil;

	@Autowired
	OAuth oAuth;
	String[] tokens = null;

	@Autowired
	Workflow workflow;

	@Autowired
	AdminParse adminParseResponse;

	@PostConstruct
	public void executeAfterConstructor() {
		tokens = oAuth.getToken();
	}

	@Autowired
	TaskOwnersDao taskOwners;

	@Autowired
	InboxActionsDao inboxActions;

	@Autowired
	PropertiesConstants getProperty;

	@Autowired
	TaskAuditDao taskAuditDao;

	@Autowired
	private UserIDPMappingDao userIDPMappingDao;
	
	@Override
	public ResponseMessage taskAction(ActionDto dto, ActionDtoChild childDto, XsuaaToken token) {
		if (!ServicesUtil.isEmpty(dto)) {
			dto.setUserDisplay(userIDPMappingDao.getUserName(dto.getUserId()));
			// for(ActionDtoChild childDto : dto.getTask()) {
			if (!ServicesUtil.isEmpty(childDto.getIsAdmin()) && !childDto.getIsAdmin()) {
				if (childDto.getComment() == null)
					childDto.setComment("");
				if (childDto.getActionType() == null)
					return new ResponseMessage("failed", "", "Action Type is Null");
				if (childDto.getActionType().equalsIgnoreCase("Approve")) {
					if (!ServicesUtil.isEmpty(dto.getIsChatbot()) && dto.getIsChatbot() == true) {
						return approveTaskForChatBot(childDto, dto);
					} else {
						List<String> dtoList = substitutionService.getSubstitutedUserForTask(dto.getUserId(),
								childDto.getInstanceId());
						if (!ServicesUtil.isEmpty(dtoList) || dtoList.size() > 0) {

							ActionDtoChild forwardDto = childDto;
							forwardDto.setSendToUser(dto.getUserId());
							bulkForward(childDto, dto, token);
						}
						substitutionService.getIsSubstituted(childDto.getInstanceId(), dto.getUserId());

						return approveTask(childDto, dto, token);
					}
				} else if (childDto.getActionType().equalsIgnoreCase("Reject")) {

					if (!ServicesUtil.isEmpty(dto.getIsChatbot()) && dto.getIsChatbot() == true) {
						return rejectTaskForChatBot(childDto, dto);
					}
					List<String> dtoList = substitutionService.getSubstitutedUserForTask(dto.getUserId(),
							childDto.getInstanceId());
					if (!ServicesUtil.isEmpty(dtoList) || dtoList.size() > 0) {

						ActionDtoChild forwardDto = childDto;
						forwardDto.setSendToUser(dto.getUserId());
						bulkForward(childDto, dto, token);
					}
					substitutionService.getIsSubstituted(childDto.getInstanceId(), dto.getUserId());
					return rejectTask(childDto, dto, token);
				} else if (childDto.getActionType().equalsIgnoreCase("Claim")) {

					if (!ServicesUtil.isEmpty(dto.getIsChatbot()) && dto.getIsChatbot() == true) {
						return claimTaskforChatBot(childDto, dto);
					}
					List<String> dtoList = substitutionService.getSubstitutedUserForTask(dto.getUserId(),
							childDto.getInstanceId());
					if (!ServicesUtil.isEmpty(dtoList) || dtoList.size() > 0) {

						ActionDtoChild forwardDto = childDto;
						forwardDto.setSendToUser(dto.getUserId());
						bulkForward(childDto, dto, token);
						List<UserTaskMappingDto> list = setUserTaskMapping(dtoList, dto.getUserId(),
								childDto.getInstanceId());
						substitutionService.saveUserTaskMapping(list);
					}
					return claimTask(childDto, dto, token);
				} else if (childDto.getActionType().equalsIgnoreCase("Release")) {

					if (!ServicesUtil.isEmpty(dto.getIsChatbot()) && dto.getIsChatbot() == true) {
						return relaseTaskforChatBot(childDto, dto);
					}
					return relaseTask(childDto, dto.getUserId(), token);
				} else if (childDto.getActionType().equalsIgnoreCase("AssignProcessor")) {
					return assignProcessor(childDto, childDto.getProcessor());
				} else if (childDto.getActionType().equalsIgnoreCase("Forward")) {
					if (!ServicesUtil.isEmpty(childDto.getInstanceId()))
						return bulkForward(childDto, dto, token);
				} else {
					return new ResponseMessage("failed", "", "Action Type is Not valid");
				}
			} else if (!ServicesUtil.isEmpty(childDto.getIsAdmin()) && childDto.getIsAdmin()) {
				return doAdminAction(dto, childDto, token);
			}
			// }
		}
		return new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE,
				PMCConstant.ACTION_NOT_SUPPORTED);
	}

	private List<UserTaskMappingDto> setUserTaskMapping(List<String> dtoList, String userId, String instanceId) {
		List<UserTaskMappingDto> taskUsers = null;
		UserTaskMappingDto userTaskMappingDto = null;
		try {
			taskUsers = new ArrayList<>();
			for (String substitutedUser : dtoList) {
				userTaskMappingDto = new UserTaskMappingDto();
				userTaskMappingDto.setSubstitutedUser(substitutedUser);
				userTaskMappingDto.setSubstitutingUser(userId);
				userTaskMappingDto.setTaskId(instanceId);
				taskUsers.add(userTaskMappingDto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Error creating UserTaskMappingDto" + e);
		}
		return taskUsers;
	}

	@SuppressWarnings("unused")
	private ResponseMessage approveTaskForChatBot(ActionDtoChild childDto, ActionDto dto) {
		System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() action dto : " + dto);
		String APPROVE_TASK = getProperty.getValue("REQUEST_URL_INST") + "task-instances/";
		ResponseMessage response = new ResponseMessage();
		String token = "";
		int count = 0;

		// List<String> instanceList = dto.getInstanceList();
		try {
			// for (String instanceId : instanceList) {
			String url = APPROVE_TASK + childDto.getInstanceId();
			// token = getScrfToken(url);

			// claiming the task first
			// String tokenurl =
			//
			// "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/rest/v1/xsrf-token";
			// String claimPayload = "{\"processor\":\"" + dto.getUserId() +
			// "\"}";
			// String approvalPaload = "{\"status\":\"COMPLETED\"}";

			// System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask()
			// claim
			// payload : " + claimPayload);
			// System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask()
			// approve payload : " + approvalPaload);

			// claiming the task first
			String tokenurl = "https://bpmworkflowruntimea2d6007ea-a8b98c03e.hana.ondemand.com/workflow-service/rest/v1/xsrf-token";
			String claimPayload = "{\"processor\":\"" + dto.getUserId() + "\"}";
			ActionRequestDto payload = new ActionRequestDto("COMPLETED",
					new ActionRequestContextDto("true", childDto.getComment()));

			System.err.println("[WBP-Dev]in approve task");
			JSONObject entity = new JSONObject(payload);
			//
			// System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask()
			// claim
			// payload : "+claimPayload);
			// System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask()
			// approve payload : "+approvalPaload);

			System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() token type : ");

			RestResponse restResponse2 = SCPRestUtil.callRestService(url, null, entity.toString(), "PATCH",
					PMCConstant.APPLICATION_JSON, false, "fetch", null, null, null, null,
					RestUtilAuth.PRINCIPAL_PROPOGATION_AUTH, null, null, dto.getHeaderName(), dto.getHeaderValue());

			System.err.println("[WBP-Dev]WorkFlowActionFacade.approveChatBotTask() approve response :" + restResponse2);
			count++;
			if (!ServicesUtil.isEmpty(restResponse2) && (restResponse2.getResponseCode() >= 200)
					&& (restResponse2.getResponseCode() <= 207)) {

				taskEvents.updateTaskEventToCompleted(childDto.getInstanceId());
				taskEvents.updateTaskComment(childDto.getInstanceId(), dto.getUserId(), childDto.getComment(),
						childDto.getActionType());
			} else {
				count--;

			}

			// }
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);

			if (count != 0) {
				if (count != 1)
					response.setMessage(count + " Tasks are Successfully Approved");
				else
					response.setMessage(" Task Successfully Approved");
			} else {
				response.setMessage("action failed with " + restResponse2.getResponseCode() + " "
						+ restResponse2.getResponseObject());
				response.setStatus(PMCConstant.FAILURE);
				response.setStatusCode(PMCConstant.CODE_FAILURE);
			}

		} catch (Exception e) {
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setMessage("Failed to update the task:" + e.toString() + "token:" + token);
		}

		return response;
	}

	@SuppressWarnings("unused")
	private ResponseMessage rejectTaskForChatBot(ActionDtoChild childDto, ActionDto dto) {
		System.err.println("[WBP-Dev]WorkFlowActionFacade.rejectChabotTask() action dto : " + dto);
		String REJECT_TASK = getProperty.getValue("REQUEST_URL_INST") + "task-instances/";
		ResponseMessage response = new ResponseMessage();
		String token = "";
		int count = 0;

		try {
			String url = REJECT_TASK + childDto.getInstanceId();

			String tokenurl = "https://bpmworkflowruntimea2d6007ea-a8b98c03e.hana.ondemand.com/workflow-service/rest/v1/xsrf-token";
			String claimPayload = "{\"processor\":\"" + dto.getUserId() + "\"}";
			ActionRequestDto payload = new ActionRequestDto("COMPLETED",
					new ActionRequestContextDto("false", childDto.getComment()));

			System.err.println("[WBP-Dev]in reject task");
			JSONObject entity = new JSONObject(payload);
			System.err.println("[WBP-Dev]WorkFlowActionFacade.rejectTask() token type : ");

			RestResponse restResponse2 = SCPRestUtil.callRestService(url, null, entity.toString(), "PATCH",
					PMCConstant.APPLICATION_JSON, false, "fetch", null, null, null, null,
					RestUtilAuth.PRINCIPAL_PROPOGATION_AUTH, null, null, dto.getHeaderName(), dto.getHeaderValue());

			System.err.println("[WBP-Dev]WorkFlowActionFacade.approveChatBotTask() reject response :" + restResponse2);
			count++;
			if (!ServicesUtil.isEmpty(restResponse2) && (restResponse2.getResponseCode() >= 200)
					&& (restResponse2.getResponseCode() <= 207)) {

				taskEvents.updateTaskEventToCompleted(childDto.getInstanceId());
				taskEvents.updateTaskComment(childDto.getInstanceId(), dto.getUserId(), childDto.getComment(),
						childDto.getActionType());
			} else {
				count--;

			}

			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);

			if (count != 0) {
				if (count != 1)
					response.setMessage(count + " Tasks are Successfully Rejected");
				else
					response.setMessage(" Task Successfully Rejected");
			} else {
				response.setMessage("action failed with " + restResponse2.getResponseCode() + " "
						+ restResponse2.getResponseObject());
				response.setStatus(PMCConstant.FAILURE);
				response.setStatusCode(PMCConstant.CODE_FAILURE);
			}

		} catch (Exception e) {
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setMessage("Failed to update the task:" + e.toString() + "token:" + token);
		}

		return response;
	}

	private ResponseMessage claimTaskforChatBot(ActionDtoChild childDto, ActionDto dto) {
		ResponseMessage response = new ResponseMessage();

		System.err.println("[WBP-Dev]WorkFlowActionFacade.ClaimChabotTask() action dto : " + dto);

		try {
			String instanceId = childDto.getInstanceId();
			String claimTask = getProperty.getValue("REQUEST_BASE_URL_TC") + "/Claim?SAP__Origin=%27NA%27&InstanceID='"
					+ instanceId + "'";

			RestResponse restResponse = SCPRestUtil.callRestService(claimTask, null, null, "POST",
					PMCConstant.APPLICATION_JSON, false, "fetch", null, null, null, null,
					RestUtilAuth.PRINCIPAL_PROPOGATION_AUTH, null, null, dto.getHeaderName(), dto.getHeaderValue());

			System.err.println("[WBP-Dev]rest Response= " + restResponse);
			if (!ServicesUtil.isEmpty(restResponse) && !ServicesUtil.isEmpty(restResponse.getResponseCode())) {
				if (restResponse.getResponseCode() == 201) {
					inboxActions
							.saveOrUpdateAction(new InboxActions(instanceId, new Date(), true, null, dto.getUserId()));
					response.setStatus("Success");
					response.setStatusCode(PMCConstant.CODE_SUCCESS);
					response.setMessage("Successfully Claimed");
				} else {

					response.setStatus(PMCConstant.FAILURE);
					response.setStatusCode(PMCConstant.CODE_FAILURE);
					response.setMessage("Failed to Claim the task with status " + restResponse.getResponseCode() + " "
							+ restResponse.getResponseObject());
				}
			}

		} catch (Exception e) {
			response.setStatus("Fail");
			response.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setMessage("Failed to Claim the task" + e.toString());
			System.err.println("[WBP-Dev]claim failed:" + e.getStackTrace());
		}
		return response;
	}

	private ResponseMessage relaseTaskforChatBot(ActionDtoChild childDto, ActionDto dto) {
		ResponseMessage response = new ResponseMessage();
		try {
			// for (String instanceId : instanceList) {
			String instanceId = childDto.getInstanceId();
			String releaseTask = getProperty.getValue("REQUEST_BASE_URL_TC")
					+ "/Release?SAP__Origin=%27NA%27&InstanceID='" + instanceId + "'";
			// String token = getScrfToken(RELEASE_TASK);
			RestResponse restResponse = SCPRestUtil.callRestService(releaseTask, null, null, "POST",
					PMCConstant.APPLICATION_JSON, false, "fetch", null, null, null, null,
					RestUtilAuth.PRINCIPAL_PROPOGATION_AUTH, null, null, dto.getHeaderName(), dto.getHeaderValue());
			System.err.println("[WBP-Dev]SCP Release action response" + restResponse);
			if (!ServicesUtil.isEmpty(restResponse) && !ServicesUtil.isEmpty(restResponse.getResponseCode())) {
				if (restResponse.getResponseCode() == 201) {
					// releaseInstance(instanceId, userId);
					inboxActions
							.saveOrUpdateAction(new InboxActions(instanceId, new Date(), null, true, dto.getUserId()));

					Boolean isSubstituted = substitutionService.getIsSubstituted(childDto.getInstanceId(),
							dto.getUserId());
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]isSubstituted : " + isSubstituted);
					response.setStatus("Success");
					response.setStatusCode(PMCConstant.CODE_SUCCESS);
					response.setMessage("Successfully Released");
				}

				else {

					response.setStatus(PMCConstant.FAILURE);
					response.setStatusCode(PMCConstant.CODE_FAILURE);
					response.setMessage("Failed to Release the task with status " + restResponse.getResponseCode() + " "
							+ restResponse.getResponseObject());
				}
			}

		} catch (Exception e) {
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setMessage("Fail to release the task");
		}
		return response;
	}

	@SuppressWarnings("unused")
	private ResponseMessage approveTask(ActionDtoChild childActionDto, ActionDto actionDto, XsuaaToken xsuaaToken) {
		String destinationUrl = getProperty.getValue("REQUEST_URL_INST") + "task-instances/";
		ResponseMessage response = new ResponseMessage();
		String token = "";
		int count = 0;

		List<TaskStatus> listTasks = new ArrayList<>();
		TaskStatus taskStatus = null;
		int taskCount = 0;
		/*
		 * NotificationDto notificationdto =null; List<String> users=null
		 */;

		String message = "";
		try {
			System.err.println("[WBP-Dev]in approve task");
			// for (String instanceId : actionDto.getInstanceList()) {
			String instanceId = childActionDto.getInstanceId();
			// token = getScrfToken(url);
			count++;
			// preparing context for approval
			JSONObject responseJson = new JSONObject();
			JSONObject context = new JSONObject();
			responseJson.put(WorkflowConstants.STATUS, "completed");
			context.put("status", "Approved");
			context.put("approverComments", childActionDto.getComment());
			context.put("isApproved", Boolean.TRUE);
			responseJson.put(WorkflowConstants.CONTEXT, context);
			System.err.println("[WBP-Dev]in approve task");
			// System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask()
			// :"+entity);

			RestResponse restResponse = workflowUtil.actionOnTask(responseJson.toString(), xsuaaToken, destinationUrl,
					instanceId);

			/*
			 * String[] accesstoken = getToken(); RestResponse restResponse =
			 * SCPRestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI,
			 * entity.toString(), "PATCH", "application/json", true, "Fetch", null, null,
			 * accesstoken[0], accesstoken[1]);
			 */

			System.err.println("[WBP-Dev]in approve task3");
			System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() Object :" + restResponse);

//			ActionRequestDto dto = new ActionRequestDto("COMPLETED",
//					new ActionRequestContextDto("true", childActionDto.getComment()));
//			
//			System.err.println("[WBP-Dev]in approve task");
//			JSONObject entity = new JSONObject(dto);
//			// System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask()
//			// :"+entity);
//			
//			RestResponse restResponse = SCPRestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI,
//					entity.toString(), "PATCH", "application/json", true, "Fetch", null, null, null, null);
//			System.err.println("[WBP-Dev]in approve task3");
//			System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() Object :" + restResponse);

			if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >= 200)
					&& (restResponse.getResponseCode() <= 207)) {

				// Notify the user
				// Boolean status = notityUser(actionDto, taskCount);
				// System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask()
				// Notification sent : " + status);
				System.err.println("[WBP-Dev]in approve task4");
				taskEvents.updateTaskEventToCompleted(instanceId,actionDto.getUserId(),actionDto.getUserDisplay());
				taskEvents.updateTaskComment(instanceId, actionDto.getUserId(), childActionDto.getComment(),
						childActionDto.getActionType());
				taskStatus = new TaskStatus();
				taskStatus.setEventId(instanceId);
				taskStatus.setIsApproved(true);
				taskStatus.setComments(childActionDto.getComment());
				listTasks.add(taskStatus);

				// Jabil POC changes @Venky

				// check for the Jabic process
				if (!ServicesUtil.isEmpty(actionDto.getProcessLabel())
						&& actionDto.getProcessLabel().equalsIgnoreCase("analyst_appproval_process")) {
					// check for the case id existance
					if (!ServicesUtil.isEmpty(actionDto.getCaseId())) {
						// logic to check any existing analyst tasks with the
						// businessStatus
						Boolean analystTaskCheck = false; // write logic to get
															// this data in
															// tasks events
															// @Pending @Done by
															// sourav

						GenericResponseDto genResp = taskEvents.checkForTasksExistence(instanceId,
								actionDto.getCaseId(), actionDto.getProcessLabel());
						System.err.println(
								"ScpWorkflowActionFacade.approveTask() Analyst task existance check response : "
										+ genResp);

						analystTaskCheck = (Boolean) genResp.getData();
						// check to create IC manager task

						if (!analystTaskCheck) {

							if (ServicesUtil.isEmpty(actionDto.getIcManagerProcessId())) {
								// logic to get the current task context data
								// based on task id

								RestResponse resp = workflow.getContextData(instanceId);
								System.err.println("ScpWorkflowActionFacade.approveTask() context response : " + resp);
								// prepare context data for IC Manager task
								AnalystContextDto contextDto = prepareContextData(resp.getResponseObject());

								// write login to get all line items and forms
								// for the businessStatus from analyst process

								AnalystLineItemsAndForms analystsData = null;

								analystsData = taskEvents.getAnalystLineItemsAndForms(actionDto.getProcessLabel(),
										actionDto.getCaseId());

								System.err.println(
										"ScpWorkflowActionFacade.approveTask() linItem and Forms : " + analystsData);
								AnalystLineItemsAndForms analystsData2 = analystsData;// filterLineItems(analystsData);

								System.err.println("ScpWorkflowActionFacade.approveTask() linItem and Forms  filter : "
										+ analystsData2);

								contextDto.setLineItems(analystsData2.getLineItems());
								contextDto.setForms(analystsData2.getForms());

								System.err.println("ScpWorkflowActionFacade.approveTask() context Dto ; " + contextDto);
								JSONObject payload = new JSONObject();
								JSONObject contextBody = new JSONObject(contextDto);

								payload.accumulate("definitionId", "ic_manager_approval_process");
								payload.accumulate("context", contextBody);

								System.err.println(
										"ScpWorkflowActionFacade.approveTask() IC manager task creation context :"
												+ payload.toString());

								createWorkFlowInstance(payload.toString());

							} else {
								// login to update ic manager task context with
								// newly submitted analyst form

								// get the context data from DB based on process
								// id
								GenericResponseDto genResponse = taskEvents
										.getProcessStatusById(actionDto.getIcManagerProcessId());
								System.err.println(
										"ScpWorkflowActionFacade.approveTask() process id status : " + genResponse);
								if (((String) genResponse.getData()).equalsIgnoreCase("RUNNING")) {

									AnalystLineItemsAndForms managerData = taskEvents
											.getManagerLineItemsAndForms(actionDto.getIcManagerProcessId());

									AnalystLineItemsAndForms analystData = taskEvents
											.getAnalystLineAndForms(instanceId);

									List<AnalystFormData> formsExisting = managerData.getForms();

									List<AnalystFormData> formsNew = analystData.getForms();

									List<AnalystFormData> formUpdate = getUpdatedForms(formsNew, formsExisting);
									JSONArray jsonPayload3 = new JSONArray(formUpdate);

									taskEvents.updateFormCustomAttributeValues(jsonPayload3,
											actionDto.getIcManagerProcessId());

								} else {
									// logic to get the current task context
									// data based on task id

									RestResponse resp = workflow.getContextData(instanceId);
									System.err.println(
											"ScpWorkflowActionFacade.approveTask() context response 2: " + resp);
									// prepare context data for IC Manager task
									AnalystContextDto contextDto = prepareContextData(resp.getResponseObject());

									// write login to get all line items and
									// forms for the businessStatus from analyst
									// process

									AnalystLineItemsAndForms analystsData = null;

									analystsData = taskEvents.getAnalystLineItemsAndFormsForRejectedForms(
											actionDto.getProcessLabel(), actionDto.getIcManagerProcessId());

//									System.err.println("ScpWorkflowActionFacade.approveTask() linItem and Forms 2: "
//											+ analystsData);
//									AnalystLineItemsAndForms analystsData2 = filterLineItems(analystsData);
//
//									System.err.println(
//											"ScpWorkflowActionFacade.approveTask() linItem and Forms  filter 2: "
//													+ analystsData2);

									contextDto.setLineItems(analystsData.getLineItems());
									contextDto.setForms(analystsData.getForms());

									System.err.println(
											"ScpWorkflowActionFacade.approveTask() context Dto2 ; " + contextDto);
									JSONObject payload = new JSONObject();
									JSONObject contextBody = new JSONObject(contextDto);

									payload.accumulate("definitionId", "ic_manager_approval_process");
									payload.accumulate("context", contextBody);

									System.err.println(
											"ScpWorkflowActionFacade.approveTask() IC manager task creation context 2:"
													+ payload.toString());

									createWorkFlowInstance(payload.toString());

								}

							}

						}

					} else {
						response.setMessage("Case Id not found to create IC Manager task ");
					}
				}

			} else {
				System.err.println("[WBP-Dev]in approve task5");
				count--;
				System.err.println("[WBP-Dev][WBProduct-Dev] restResponse" + restResponse);
				message = restResponse.getResponseObject().toString();
			}

			taskCount++;
			// }

			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
			System.err.println("[WBP-Dev]in approve task6");
			if (count != 0) {
				if (count != 1)
					response.setMessage(count + "are Successfully Approved");
				else
					response.setMessage("Successfully Approved");
			} else {
				response.setMessage("Unable to Approve Tasks : " + message);
				response.setStatus(PMCConstant.FAILURE);
				response.setStatusCode(PMCConstant.CODE_FAILURE);
			}

		} catch (Exception e) {
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setMessage("Failed to update the task:" + e.toString() + "token:" + token + " trace : "
					+ e.getStackTrace() + e.getSuppressed() + e.getMessage() + e.getCause());
			System.err.println("[WBP-Dev]in approve task error");
		}

		taskEvents.saveTaskStatus(listTasks);

		return response;
	}

	private List<AnalystFormData> getUpdatedForms(List<AnalystFormData> formsNew, List<AnalystFormData> formsExisting) {

		int index = -1;
		AnalystFormData newform = new AnalystFormData();
		for (AnalystFormData f : formsNew) {

			for (AnalystFormData ff : formsExisting) {
				index++;
				if (f.getFormId().equalsIgnoreCase(ff.getFormId())) {
//					System.err.println("Workflow.getUpdatedForms()");
					newform = f;
					break;
				}

			}
			break;
		}

//		System.out.println("Workflow.getUpdatedForms()"+newform);
//		System.out.println("Workflow.getUpdatedForms() index : "+index);
//		System.err.println("Workflow.getUpdatedForms() before "+formsExisting);
		if (index != -1) {
			formsExisting.add(index, newform);
			formsExisting.remove(index + 1);
		}
//		System.err.println("Workflow.getUpdatedForms() after"+formsExisting);
		return formsExisting;
	}

	private int findFormIndex(List<AnalystsLineItems> lineItemsManager, List<AnalystsLineItems> lineItemsAnalyst) {

		int index = 0;
		for (AnalystsLineItems lineItemAnlyst : lineItemsAnalyst) {
			int tempIndex = 0;
			for (AnalystsLineItems lineItemManager : lineItemsManager) {

				if (lineItemAnlyst.equals(lineItemManager)) {
					System.err.println("ScpWorkflowActionFacade.findFormIndex() matched index : " + tempIndex);
					index = tempIndex;
				}
				tempIndex++;
			}
			break;
		}
		// TODO Auto-generated method stub
		return index;
	}

	public String createWorkFlowInstance(String payload) {
		try {
			String requestUrl = getProperty.getValue("REQUEST_URL_INST") + "workflow-instances";

			RestResponse restResponse = RestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, payload,
					"POST", "application/json", false, "Fetch", null, null, tokens[0], tokens[1]);

			System.out.println("restResponse For Instance Creation: " + restResponse);
			if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >= 200)
					&& (restResponse.getResponseCode() <= 207)) {
				// taskOwnersDao.deleteUser(taskId, null);
				return PMCConstant.SUCCESS;
			} else
				return PMCConstant.FAILURE;
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][PMC][SubstitutionRuleFacade][updateRecipientUserInWorkflow][error]" + e.getMessage());

			return PMCConstant.FAILURE;
		}

	}

	private AnalystLineItemsAndForms filterLineItems(AnalystLineItemsAndForms analystsData) {

		AnalystLineItemsAndForms analystsDataReturn = new AnalystLineItemsAndForms();

		Map<String, AnalystFilterLineItemAndForm> map = new HashMap<>();
		int index = 0;
		for (AnalystsLineItems lineItem : analystsData.getLineItems()) {
			String key = lineItem.getMaterialDocumentNumber() + lineItem.getLineItemNumber();

			if (!map.containsKey(key)) {
				AnalystFilterLineItemAndForm filter = new AnalystFilterLineItemAndForm();
				filter.setLineItem(lineItem);
				filter.setFormData(analystsData.getForms().get(index));
				map.put(key, filter);
			}

			index++;
		}

		List<AnalystsLineItems> lineItems = new ArrayList<>();
		List<AnalystFormData> forms = new ArrayList<>();
		for (AnalystFilterLineItemAndForm filtr : map.values()) {
			lineItems.add(filtr.getLineItem());
			forms.add(filtr.getFormData());
		}
		analystsDataReturn.setLineItems(lineItems);
		analystsDataReturn.setForms(forms);

		return analystsDataReturn;
	}

	private AnalystContextDto prepareContextData(Object contextObject) {
		AnalystContextDto contextDto = new AnalystContextDto();
		System.err.println("ScpWorkflowActionFacade.prepareContextData() context dto : " + contextObject);
		try {
			JSONObject jsonContext = (JSONObject) contextObject;
			System.err.println("ScpWorkflowActionFacade.prepareContextData() json context : " + jsonContext);
			contextDto.setCaseId(jsonContext.getString("caseId"));
			contextDto.setApplication(jsonContext.getString("application"));
			contextDto.setIcManager(jsonContext.getString("icManager"));
			contextDto.setUrgency(jsonContext.getString("urgency"));
			contextDto.setRole("IC Manager");
			contextDto.setCurrentStatus("Pending investigation");

		} catch (Exception e) {

			System.err.println("ScpWorkflowActionFacade.prepareContextData() error : " + e);
		}
		System.err.println("ScpWorkflowActionFacade.prepareContextData() before retruning context dto : " + contextDto);
		return contextDto;
	}

	private ResponseMessage rejectTask(ActionDtoChild childDto, ActionDto actionDto, XsuaaToken token) {
		String destinationUrl = "https://api.workflow-sap.cfapps.eu10.hana.ondemand.com/workflow-service/rest/v1/task-instances/";
		String REJECT_TASK = getProperty.getValue("REQUEST_URL_INST") + "task-instances/";
		ResponseMessage response = new ResponseMessage();
		String responseMessage = "";
		int count = 0;
		List<TaskStatus> listTasks = new ArrayList<>();
		TaskStatus taskStatus = null;
		int taskCount = 0;

		String message = "";
		try {
			// for (String instanceId : actionDto.getInstanceList()) {
			String instanceId = childDto.getInstanceId();
			String url = REJECT_TASK + instanceId;
			// String token = getScrfToken(url);
			count++;
//			ActionRequestDto dto = new ActionRequestDto("COMPLETED",
//					new ActionRequestContextDto("false", childDto.getComment()));
//			JSONObject entity = new JSONObject(dto);
//			RestResponse restResponse = SCPRestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI,
//					entity.toString(), "PATCH", "application/json", true, "Fetch", null, null, null, null);
			JSONObject responseJson = new JSONObject();
			JSONObject context = new JSONObject();
			responseJson.put(WorkflowConstants.STATUS, "completed");
			context.put("status", "Rejected");
			context.put("approverComments", childDto.getComment());
			context.put("isApproved", Boolean.FALSE);
			responseJson.put(WorkflowConstants.CONTEXT, context);
			System.err.println("[WBP-Dev]in approve task");
			RestResponse restResponse = workflowUtil.actionOnTask(responseJson.toString(), token, destinationUrl,
					instanceId);
			System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() Object :" + restResponse);
			if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >= 200)
					&& (restResponse.getResponseCode() <= 207)) {

				// Notify the user

				Boolean status = notityUser(childDto, taskCount, actionDto);
				System.err.println("[WBP-Dev]WorkFlowActionFacade.rejectTask() Notification sent : " + status);

				taskEvents.updateTaskEventToCompleted(instanceId,actionDto.getUserId(),actionDto.getUserDisplay());
				taskEvents.updateTaskComment(instanceId, actionDto.getUserId(), childDto.getComment(),
						childDto.getActionType());

				taskStatus = new TaskStatus();
				taskStatus.setEventId(instanceId);
				taskStatus.setIsApproved(false);
				taskStatus.setComments(childDto.getComment());
				listTasks.add(taskStatus);

			} else {
				count--;
				responseMessage += instanceId + ",";
				message = restResponse.getResponseObject().toString();
			}

			taskCount++;
			// }
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);

			if (count != 0) {
				if (count == 1)
					response.setMessage("Successfully Rejected");
				else
					response.setMessage(count + "are Successfully Rejected");
			} else {
				response.setMessage("Unable to Reject Task:" + responseMessage + " : " + message);
				response.setStatus(PMCConstant.FAILURE);
				response.setStatusCode(PMCConstant.CODE_FAILURE);
			}

		} catch (Exception e) {
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setMessage("failed to Reject the task");
		}

		taskEvents.saveTaskStatus(listTasks);

		return response;

	}

	private ResponseMessage claimTask(ActionDtoChild childDto, ActionDto dto, XsuaaToken token) {
		ResponseMessage response = new ResponseMessage();

		try {
			// System.err.println("[WBP-Dev]token for claim request: "+token);
			// for (String instanceId : instanceList) {
			String instanceId = childDto.getInstanceId();
			String accessToken[] = getToken();

			String claimTask = getProperty.getValue("REQUEST_URL_INST") + "task-instances/" + instanceId;
			String payload = "{\"processor\":\"" + token.getLogonName() + "\"}";
			RestResponse restResponse = SCPRestUtil.callRestService(claimTask, PMCConstant.SAML_HEADER_KEY_TI, payload,
					"PATCH", "application/json", false, "Fetch", null, null, accessToken[0], accessToken[1]);
			// String url =
			// "https://api.workflow-sap.cfapps.eu10.hana.ondemand.com/workflow-service/odata/v1/tcm/Claim?SAP__Origin=%27NA%27&InstanceID='"+instanceId+"'";

//			String claimTask = getProperty.getValue("REQUEST_BASE_URL_TC") + "/Claim?SAP__Origin=%27NA%27&InstanceID='"
//					+ instanceId + "'";
//			// String token = getScrfToken(RELEASE_TASK);
//			RestResponse restResponse = RestUtil.callRestService(claimTask, PMCConstant.SAML_HEADER_KEY_TC, null,
//					"POST", "application/json", true, "Fetch", null, null, null, null);
//			RestResponse restResponse = workflowUtil.actionOnTask(null, token, url,null);

			System.err.println("[WBP-Dev]rest Response= " + restResponse);
			if (!ServicesUtil.isEmpty(restResponse) && !ServicesUtil.isEmpty(restResponse.getResponseCode())) {
				if (restResponse.getResponseCode() >= 200 && restResponse.getResponseCode() <= 207) {
					// claimInstance(instanceId, userId);
					inboxActions
							.saveOrUpdateAction(new InboxActions(instanceId, new Date(), true, null, dto.getUserId()));
					response.setStatus("Success");
					response.setStatusCode(PMCConstant.CODE_SUCCESS);
					response.setMessage("Successfully Claimed");
				} else {

					response.setStatus("Fail");
					response.setStatusCode(PMCConstant.CODE_FAILURE);
					response.setMessage("Failed to Claim the task with status " + restResponse.getResponseCode() + " "
							+ restResponse.getResponseObject());
				}
			}

		} catch (Exception e) {
			response.setStatus("Fail");
			response.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setMessage("Failed to Claim the task" + e.toString());
			System.err.println("[WBP-Dev]claim failed:" + e.getStackTrace());
		}
		return response;
	}

	private ResponseMessage relaseTask(ActionDtoChild childDto, String userId, XsuaaToken token) {
		ResponseMessage response = new ResponseMessage();
		try {
			// for (String instanceId : instanceList) {
			String instanceId = childDto.getInstanceId();
//			String url = "https://api.workflow-sap.cfapps.eu10.hana.ondemand.com/workflow-service/odata/v1/tcm/Release?SAP__Origin=%27NA%27&InstanceID='"+instanceId+"'";
//			
////			String releaseTask = getProperty.getValue("REQUEST_BASE_URL_TC")
////					+ "/Release?SAP__Origin=%27NA%27&InstanceID='" + instanceId + "'";
////			// String token = getScrfToken(RELEASE_TASK);
////			RestResponse restResponse = RestUtil.callRestService(releaseTask, PMCConstant.SAML_HEADER_KEY_TC, null,
////					"POST", "application/json", true, "Fetch", null, null, null, null);
//			RestResponse restResponse = workflowUtil.actionOnTask(null, token, url,null);
			String accessToken[] = getToken();

			String claimTask = getProperty.getValue("REQUEST_URL_INST") + "task-instances/" + instanceId;
			String payload = "{\"processor\":\"\"}";
			RestResponse restResponse = SCPRestUtil.callRestService(claimTask, PMCConstant.SAML_HEADER_KEY_TI, payload,
					"PATCH", "application/json", false, "Fetch", null, null, accessToken[0], accessToken[1]);
			System.err.println("[WBP-Dev]SCP Release action response" + restResponse);
			if (!ServicesUtil.isEmpty(restResponse) && !ServicesUtil.isEmpty(restResponse.getResponseCode())) {
				if (restResponse.getResponseCode() == 201) {
					// releaseInstance(instanceId, userId);
					inboxActions.saveOrUpdateAction(new InboxActions(instanceId, new Date(), null, true, userId));

					Boolean isSubstituted = substitutionService.getIsSubstituted(childDto.getInstanceId(), userId);
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]isSubstituted : " + isSubstituted);
					if (isSubstituted) {
						System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]isSubstituted : True");
						String result = updateRecipientUserInWorkflow(childDto.getInstanceId(),
								getPayloadForRemove(userId, childDto.getInstanceId()));

						System.err.println("[WBP-Dev]Remove Owner" + result);
						taskOwners.deleteTaskOwnerAsync(childDto.getInstanceId(), userId);
					}
				}
			}

			response.setStatus("Success");
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
			response.setMessage("Successfully Released");

		} catch (Exception e) {
			response.setStatus("Fail");
			response.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setMessage("Fail to release the task");
		}
		return response;
	}

	@SuppressWarnings("unused")
	private ResponseMessage assignProcessor(ActionDtoChild childDto, String processor) {
		if (ServicesUtil.isEmpty(processor))
			return new ResponseMessage("failed", "", "Please Specify the Valid processor");
		ResponseMessage responseMessage = new ResponseMessage();

		try {

		} catch (Exception e) {

		}

		return null;
	}

	public ResponseMessage bulkForward(ActionDtoChild childDto, ActionDto actionDto, XsuaaToken token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			String result = "";
			String unForwaredTask = "";
			String forwardedTask = "";
			int taskCount = 0;
			String task = childDto.getInstanceId();

			if (!childDto.getIsAdmin()) {

				List<String> dtoList = substitutionService.getSubstitutedUserForTask(actionDto.getUserId(),
						childDto.getInstanceId());
				if (!ServicesUtil.isEmpty(dtoList) || dtoList.size() > 0) {

					List<UserTaskMappingDto> list = setUserTaskMapping(dtoList, actionDto.getUserId(),
							childDto.getInstanceId());
					substitutionService.saveUserTaskMapping(list);
				}
			}

			result = updateRecipientUserInWorkflow(task, getPayloadForForward(childDto.getSendToUser(), task));
			if (result.equals(PMCConstant.SUCCESS)) {
				// Notify the user
//				Boolean status = notityUser(childDto, taskCount, actionDto);
//				System.err.println("[WBP-Dev]WorkFlowActionFacade.bulkForward() Notification sent : " + status);

				forwardedTask += "'" + task + "',";
			} else {
				unForwaredTask += "'" + task + "',";
			}
			taskCount++;

			if (ServicesUtil.isEmpty(unForwaredTask)) {
				responseDto.setMessage("Successfully Forwarded");
				responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
				responseDto.setStatusCode(PMCConstant.CODE_SUCCESS);
				System.err.println(
						"[WBP-Dev][PMC][SubstitutionRuleFacade][iterateTaskCollection][failed	for ]" + forwardedTask);

			} else {
				responseDto.setMessage("Task Forwarding Failed ");
				System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][BulkForward][failed for ]" + unForwaredTask);
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][BulkForward][failed for ]" + e.getMessage()
					+ " ," + childDto.getInstanceId());
		}
		return responseDto;
	}

	private ResponseMessage doAdminAction(ActionDto dto, ActionDtoChild childDto, XsuaaToken token) {
		ResponseMessage message = null;
		// for (ActionDtoChild childDto : dto.getTask()) {
		switch (childDto.getActionType().toUpperCase()) {
		case "CLAIM":
			message = new ResponseMessage(PMCConstant.STATUS_SUCCESS, "0", PMCConstant.SERVICE_NOT_AVAILABLE);
			// message = adminClaim(childDto.getInstanceId(),
			// dto.getUserId());
			break;
		case "RELEASE":
			// message = new
			// ResponseMessage(PMCConstant.STATUS_SUCCESS,"0",PMCConstant.SERVICE_NOT_AVAILABLE);
			message = relaseTask(childDto, dto.getUserId(), token);
			break;
		case "FORWARD":
			// return adminForward(dto.getInstanceList(),
			// dto.getSendToUser());
			message = bulkForward(childDto, dto, token);
			break;
		case "APPROVE":
			return approveTask(childDto, dto, token);
		case "REJECT":
			return rejectTask(childDto, dto, token);

		}
		// }
		return message;
	}

	private Boolean notityUser(ActionDtoChild childDto, int taskCount, ActionDto dto) {
		// Notify the requester
		PushNotificationDto notificationdto = null;
		List<String> users = null;
		String actionType = "";

		if (childDto.getActionType().equalsIgnoreCase("Approve")) {
			actionType = " is Approved By ";
			if (ServicesUtil.isEmpty(childDto.getRequesterIds())) {
				System.err.println(
						"[WBP-Dev]WorkFlowActionFacade.notityUser() : No requester id found to send notification --> "
								+ childDto);
				return false;
			}
		} else if (childDto.getActionType().equalsIgnoreCase("Reject")) {
			actionType = " is Rejected By ";

			if (ServicesUtil.isEmpty(childDto.getRequesterIds())) {
				System.err.println(
						"[WBP-Dev]WorkFlowActionFacade.notityUser() : No requester id found to send notification --> "
								+ childDto);
				return false;
			}
		} else if (childDto.getActionType().equalsIgnoreCase("Forward")) {
			actionType = " is Forwarded By ";
		} else {
			actionType = " ";
		}

		try {
			notificationdto = new PushNotificationDto();
			users = new ArrayList<>();

			if (childDto.getActionType().equalsIgnoreCase("Forward")) {
				users.add(childDto.getSendToUser());
			} else {
				users.add(childDto.getRequesterIds().get(taskCount));
			}

			notificationdto.setAlert(childDto.getProcessLabel());
			notificationdto.setData(childDto.getSubject() + actionType + dto.getUserDisplay());
			notificationdto.setUsers(users);
		} catch (Exception e) {
			System.err.println("[WBP-Dev]WorkFlowActionFacade.notityUser() error : " + e);
		}
		return pushNotificationService.notifyUser(notificationdto);
	}

	public String getPayloadForForward(String processor, String taskId) {
		String listToString = "{ \"processor\":";
		if (!ServicesUtil.isEmpty(processor)) {
			listToString += "\"";
			listToString += processor;
		}

		listToString += "\"";
		List<String> recipientUser = getRecipientUser(taskId, PMCConstant.FORWARD_TASK, null);
		if (!ServicesUtil.isEmpty(processor) && recipientUser.size() > 0 && !ServicesUtil.isEmpty(recipientUser)) {
			recipientUser.add(processor);
		} else if (ServicesUtil.isEmpty(recipientUser) && !ServicesUtil.isEmpty(processor)) {
			recipientUser = new ArrayList<>();
			recipientUser.add(processor);
		}
		if (!ServicesUtil.isEmpty(recipientUser) && recipientUser.size() > 0) {
			listToString += " ,\"recipientUsers\":";
			listToString += "\"";
			for (String str : recipientUser) {
				listToString += str + ",";
			}
			listToString = listToString.substring(0, listToString.length() - 1);
			listToString += "\"";
		}
		listToString += "}";
		return listToString;
	}

	public String updateRecipientUserInWorkflow(String taskId, String payload) {
		try {
			String[] token = getToken();
			String requestUrl = getProperty.getValue("REQUEST_URL_INST") + "task-instances/" + taskId;

			RestResponse restResponse = SCPRestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, payload,
					"PATCH", "application/json", false, "Fetch", null, null, token[0], token[1]);

			System.err.println("[WBP-Dev]Forward Response" + restResponse);
			if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >= 200)
					&& (restResponse.getResponseCode() <= 207)) {

				taskOwners.deleteUser(taskId, null);
			}
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][PMC][SubstitutionRuleFacade][updateRecipientUserInWorkflow][error]" + e.getMessage());

			System.err.println(
					"[WBP-Dev][PMC][SubstitutionRuleFacade][updateRecipientUserInWorkflow][error]" + e.getMessage());
		}
		return PMCConstant.SUCCESS;

	}

	public List<String> getRecipientUser(String taskId, String type, JSONObject resource) {
		List<String> resultList = new ArrayList<String>();
		JSONObject resources = resource;
		try {
			if (type.equals(PMCConstant.FORWARD_TASK)) {
				String requestUrl = getProperty.getValue("REQUEST_URL_INST") + "task-instances/" + taskId;
				Object responseObject = SCPRestUtil.callRestService(requestUrl, PMCConstant.SAML_HEADER_KEY_TI, null,
						"GET", "application/json", false, null, null, null, tokens[0], tokens[1]).getResponseObject();
				JSONObject jsonObject = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
				resources = (JSONObject) jsonObject;
			}
			if (resources != null) {
				JSONArray jsonArray = resources.getJSONArray("recipientUsers");
				for (int i = 0; i < jsonArray.length(); i++)
					resultList.add(jsonArray.optString(i));
				String currentProcessor = resource.optString("processor");

				if (!ServicesUtil.isEmpty(currentProcessor))
					resultList.add(currentProcessor);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][getRecipientUser][error]" + e.getMessage());
		}
		return resultList;

	}

	private String getPayloadForRemove(String processor, String taskId) {
		String listToString = "{ ";
		List<String> recipientUser = getRecipientUser(taskId, PMCConstant.FORWARD_TASK, null);
		if (!ServicesUtil.isEmpty(processor) && recipientUser.size() > 0 && !ServicesUtil.isEmpty(recipientUser)) {
			recipientUser.remove(processor);
		}

		if (!ServicesUtil.isEmpty(recipientUser) && recipientUser.size() > 0) {
			listToString += " \"recipientUsers\":";
			listToString += "\"";
			for (String str : recipientUser) {
				listToString += str + ",";
			}
			listToString = listToString.substring(0, listToString.length() - 1);
			listToString += "\"";
		}
		listToString += "}";
		System.err.println("[WBP-Dev]recipientUser" + listToString);
		return listToString;
	}

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

			String tokenUrl = "https://hrapps.authentication.eu10.hana.ondemand.com/oauth/token";

			String clientId = "sb-clone-034dcb79-1dc8-450b-984f-5f36ed6251d0!b19391|workflow!b10150";
			String clientSecret = "cdedf334-a3ad-490a-ba46-8de1e1c81613$PkglAB-PmNjHTe4bwTRsspfPcsilmqIz0jyNyoi03uA=";
			String body = "grant_type=password&username=shruti.patra@incture.com&password=Incture@123&client_id=sb-clone-d6948b2e-75f4-4342-bcc2-3cb5ceec57aa!b19391|workflow!b10150&response_type=toke&client_secret=b34313ff-e7bf-4ce5-96e5-6917fc5a4089$K-_IZvpC-0GxU8CSzAP4amMFEb_OuYNKHWP9-IY3Tqk=";

			body = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret
					+ "&response_type=token";

			String coentType = "application/x-www-form-urlencoded";

			Object responseObject = SCPRestUtil
					.callRestService(tokenUrl, null, body, "POST", coentType, false, null, null, null, null, null)
					.getResponseObject();

			Gson g = new Gson();
			System.err.println("AdminParse.getToken() response : " + g.toJson(responseObject));
			JSONObject resources = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
			result[0] = resources.optString("access_token");
			result[1] = resources.optString("token_type");
		} catch (Exception e) {
			System.err.println("[PMC][scpworkflowactionfacade][gettoken][error]" + e.getMessage());
		}
		return result;
	}

}
