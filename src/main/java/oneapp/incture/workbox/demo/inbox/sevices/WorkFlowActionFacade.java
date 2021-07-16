package oneapp.incture.workbox.demo.inbox.sevices;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.cloud.security.xsuaa.token.XsuaaToken;

import oneapp.incture.workbox.demo.adapterJira.services.JiraActionFacade;
import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.InboxActionsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskAuditDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_salesforce.services.SalesforceActionFacade;
import oneapp.incture.workbox.demo.adhocTask.dao.CustomAttributeValuesTableAdhocDao;
import oneapp.incture.workbox.demo.adhocTask.util.ActionFacade;
import oneapp.incture.workbox.demo.adhocTask.util.ParseResponse;
import oneapp.incture.workbox.demo.ecc.services.EccActionFacade;
import oneapp.incture.workbox.demo.inbox.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.inbox.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkflowResponseDto;
import oneapp.incture.workbox.demo.inbox.util.Workflow;
import oneapp.incture.workbox.demo.notification.service.PushNotificationService;
import oneapp.incture.workbox.demo.scpadapter.scheduler.UpdateWorkflow;
import oneapp.incture.workbox.demo.scpadapter.services.ScpWorkflowActionFacadeLocal;
import oneapp.incture.workbox.demo.scpadapter.util.WorkflowContextData;
import oneapp.incture.workbox.demo.sharepoint.util.SharePointActionFacadeLocal;
import oneapp.incture.workbox.demo.sharepoint.util.TaskEventChangeDto;
import oneapp.incture.workbox.demo.successfactors.services.SFActionFacade;
import oneapp.incture.workbox.demo.successfactors.services.SuccessFactorActionFacade;
import oneapp.incture.workbox.demo.successfactors.util.SuccessFactorsUtil;
import oneapp.incture.workbox.demo.zoho.services.ZohoActionFacade;
import oneapp.incture.workbox.demo.zohoExpense.service.ZohoExpenseActionFacade;

@Service("WorkFlowActionFacade")
public class WorkFlowActionFacade implements WorkFlowActionFacadeLocal {
	// private String BASE_URL =
	// "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/";

	// private String TOKEN_URL =
	// "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/rest/xsrf-token";
	// "https://flpnwc-kbniwmq1aj.dispatcher.hana.ondemand.com/sap/fiori/bpmmyinbox/bpmworkflowruntime/odata/tcm";

	@Autowired
	Workflow workflow;

	@Autowired
	PropertiesConstants getProperty;
	@Autowired
	UpdateWorkflow updateWorkflow;

	@Autowired
	TaskEventsDao taskEvents;

	@Autowired
	ProcessEventsDao processEvents;

	@Autowired
	TaskOwnersDao taskOwners;

	@Autowired
	SFActionFacade sfActionFacade;

	@Autowired
	SuccessFactorActionFacade successFactorActionFacade;

	@Autowired
	SalesforceActionFacade salesforceActions;

	@Autowired
	InboxActionsDao inboxActions;

	@Autowired
	SuccessFactorsUtil sfUtil;

	@Autowired
	PushNotificationService pushNotificationService;

	@Autowired
	ActionFacade adhocActionFacade;

	@Autowired
	ScpWorkflowActionFacadeLocal scpWorkflowActionFacade;

	@Autowired
	EccActionFacade eccActionFacade;
	
	@Autowired
	ZohoActionFacade zohoActionFacade;
	
	@Autowired
	ZohoExpenseActionFacade zohoExpenseActionFacade;

	@Autowired
	JiraActionFacade jiraActionFacade;

	@Autowired
	CustomAttributeValuesTableAdhocDao customAttributeValuesTableAdhocDao;


	/*
	 * @Autowired private CustomAttributeValueDao customAttributeValueDao;
	 */

	@Autowired
	CustomAttributeDao customAttrDao;

	@Autowired
	SharePointActionFacadeLocal sharePointActionFacade;

	@Autowired
	UserIDPMappingDao userIdpMappingDao;

	@Autowired
	TaskAuditDao taskAuditDao;

	@Override
	public ResponseMessage taskAction(ActionDto dto, XsuaaToken token) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode("1");
		Integer count = 0;
		String message = "";
		String errorMsg = "Action not Supported for Origin";
		Boolean error = false;
		System.err.println("[WBP-Dev]Task Action Request :" + dto.toString());
		if (!ServicesUtil.isEmpty(dto) && !dto.getTask().isEmpty()) {
			// if (ServicesUtil.isEmpty(dto.getOrigin())) {
			// dto.setOrigin("SCP");
			// }
			if (!dto.getIsChatbot()) {
//				User user = UserManagementUtil.getLoggedInUser();
				System.err.println("[WBP-Dev][WBProduct-Dev]user : " + token.getLogonName());
				dto.setUserId(token.getLogonName());
			}
			UserIDPMappingDto userDeatil = userIdpMappingDao.getUserDetail(dto.getUserId());
//			if (PMCConstant.ADMIN.equals(userDeatil.getUserRole()))
//				dto.setIsAdmin(true);
//			else
//				dto.setIsAdmin(false);

			for (ActionDtoChild childDto : dto.getTask()) {

				System.err.println("[WBP-Dev]childDto :" + childDto.toString());

				if (("ECC".equals(childDto.getOrigin()) && childDto.getInstanceId().length() > 12)
						|| "BPM".equals(childDto.getOrigin()))
					childDto.setOrigin("SCP");

				switch (childDto.getOrigin()) {

				case "SuccessFactors":
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						if (childDto.getComment() == null)
							childDto.setComment("");
						if (childDto.getActionType() == null)
							return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
						if (childDto.getActionType().equalsIgnoreCase("Approve")) {
							resp = sfActionFacade.completeSuccessFactorTask(childDto, childDto.getComment(),
									childDto.getActionType(), childDto.getSubject());
									/*
									 * sfActionFacade.completeSFTask(childDto, childDto.getComment(),
									 * childDto.getActionType(), childDto.getSubject());
									 */
							message = PMCConstant.APPROVED_SUCCESS;
						} else if (childDto.getActionType().equalsIgnoreCase("Reject")) {
							resp = sfActionFacade.completeSuccessFactorTask(childDto, childDto.getComment(),
									childDto.getActionType(), childDto.getSubject());
							message = PMCConstant.REJECTED_SUCCESS;
						} else if (childDto.getActionType().equalsIgnoreCase("Claim")) {
							error = true;
							if (!errorMsg.contains("SuccessFactors"))
								errorMsg += " SuccessFactors,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						} else if (childDto.getActionType().equalsIgnoreCase("Forward")) {
							error = true;
							if (!errorMsg.contains("SuccessFactors"))
								errorMsg += " SuccessFactors,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						} else if (childDto.getActionType().equalsIgnoreCase("Release")) {
							error = true;
							if (!errorMsg.contains("SuccessFactors"))
								errorMsg += " SuccessFactors,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						}

					}
					break;

				case "SuccessFactor":
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						if (childDto.getComment() == null)
							childDto.setComment("");
						if (childDto.getActionType() == null)
							return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
						if (childDto.getActionType().equalsIgnoreCase("Approve")) {
							resp = successFactorActionFacade.completeSuccessFactorTask(childDto.getInstanceId(),
									childDto.getComment(), childDto.getActionType(), childDto.getSubject(),
									dto.getUserId(), childDto.getProcessType());
							message = PMCConstant.APPROVED_SUCCESS;
						} else if (childDto.getActionType().equalsIgnoreCase("Reject")) {
							resp = successFactorActionFacade.completeSuccessFactorTask(childDto.getInstanceId(),
									childDto.getComment(), childDto.getActionType(), childDto.getSubject(),
									dto.getUserId(), childDto.getProcessType());
							message = PMCConstant.REJECTED_SUCCESS;
						} else if (childDto.getActionType().equalsIgnoreCase("Claim")) {
							error = true;
							if (!errorMsg.contains("SuccessFactors"))
								errorMsg += " SuccessFactors,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						} else if (childDto.getActionType().equalsIgnoreCase("Forward")) {
							error = true;
							if (!errorMsg.contains("SuccessFactors"))
								errorMsg += " SuccessFactors,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						} else if (childDto.getActionType().equalsIgnoreCase("Release")) {
							error = true;
							if (!errorMsg.contains("SuccessFactors"))
								errorMsg += " SuccessFactors,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						}

					}
					break;

				/*
				 * case "SF": if (!ServicesUtil.isEmpty(childDto.getActionType())) { if
				 * (childDto.getComment() == null) childDto.setComment(""); if
				 * (childDto.getActionType() == null) return new ResponseMessage("failed", "",
				 * "Action Type is Null"); if
				 * (childDto.getActionType().equalsIgnoreCase("Approve")) { resp =
				 * sfActionFacade.completeSuccessFactorTask(childDto, childDto.getComment(),
				 * childDto.getActionType(), childDto.getSubject()); } else if
				 * (childDto.getActionType().equalsIgnoreCase("Reject")) { resp =
				 * sfActionFacade.completeSuccessFactorTask(childDto, childDto.getComment(),
				 * childDto.getActionType(), childDto.getSubject()); }else if
				 * (childDto.getActionType().equalsIgnoreCase("CLAIM")){ return new
				 * ResponseMessage(PMCConstant.STATUS_SUCCESS,"0",PMCConstant.
				 * SERVICE_NOT_AVAILABLE); }else if
				 * (childDto.getActionType().equalsIgnoreCase("Forward")){ return new
				 * ResponseMessage(PMCConstant.STATUS_SUCCESS,"0",PMCConstant.
				 * SERVICE_NOT_AVAILABLE); }else if
				 * (childDto.getActionType().equalsIgnoreCase("RELEASE")){ return new
				 * ResponseMessage(PMCConstant.STATUS_SUCCESS,"0",PMCConstant.
				 * SERVICE_NOT_AVAILABLE); } } break;
				 */

				case "SCP":
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						if (childDto.getComment() == null)
							childDto.setComment("");
						if (childDto.getActionType() == null)
							resp = new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
//						return scpWorkflowActionFacade.taskAction(dto, childDto);
						resp = scpWorkflowActionFacade.taskAction(dto, childDto,token);
//						
						message = resp.getMessage();
					}
					break;
				case "ECC":
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						if (childDto.getActionType().equalsIgnoreCase("Approve")
								|| childDto.getActionType().equalsIgnoreCase("Reject")) {
							String url = "";

							if (PMCConstant.PURCHASE_ORDER.equalsIgnoreCase(childDto.getProcessType())) {

								url = getProperty.getValue("CC_VIRTUAL_HOST") + getProperty.getValue("POApprovalURL");
							} else if (PMCConstant.PURCHASE_REQUISITION.equalsIgnoreCase(childDto.getProcessType())) {
								url = getProperty.getValue("CC_VIRTUAL_HOST") + getProperty.getValue("PRApprovalURL");

							}

							resp = eccActionFacade.approveECCTask(dto, childDto, url, childDto.getProcessType());
						} else {
							error = true;
							if (!errorMsg.contains("ECC"))
								errorMsg += " ECC,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						}
					}
					break;

				case "Salesforce":
					System.err.println("[WBP-Dev][SALESFORCE][ACTION]Inside salesforce");
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						if (childDto.getComment() == null)
							childDto.setComment("");
						if (childDto.getActionType() == null)
							return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
						if (childDto.getActionType().equalsIgnoreCase("Approve")) {
							resp = salesforceActions.completeSalesforceTask(childDto.getInstanceId(),
									childDto.getComment(), childDto.getActionType());
							message = PMCConstant.APPROVED_SUCCESS;
						} else if (childDto.getActionType().equalsIgnoreCase("Reject")) {
							resp = salesforceActions.completeSalesforceTask(childDto.getInstanceId(),
									childDto.getComment(), childDto.getActionType());
							message = PMCConstant.REJECTED_SUCCESS;
						} else if (childDto.getActionType().equalsIgnoreCase("Claim")) {
							error = true;
							if (!errorMsg.contains("Salesforce"))
								errorMsg += " Salesforce,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						} else if (childDto.getActionType().equalsIgnoreCase("Forward")) {
							error = true;
							if (!errorMsg.contains("Salesforce"))
								errorMsg += " Salesforce,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						} else if (childDto.getActionType().equalsIgnoreCase("Release")) {
							error = true;
							if (!errorMsg.contains("Salesforce"))
								errorMsg += " Salesforce,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						}

					}
					break;

				case "Sharepoint":
					System.err.println("[WBP-Dev][SHAREPOINT][ACTION]Inside Sharepoint");
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						if (childDto.getActionType() == null)
							resp = new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
						if (childDto.getActionType().equalsIgnoreCase("Approve")) {
							resp = sharePointActionFacade.approveData(new TaskEventChangeDto("Completed", "100",
									childDto.getInstanceId(), childDto.getProcessId()));
							message = PMCConstant.APPROVED_SUCCESS;
						} else if (childDto.getActionType().equalsIgnoreCase("Reject")) {
							error = true;
							if (!errorMsg.contains("Sharepoint"))
								errorMsg += " Sharepoint,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						} else if (childDto.getActionType().equalsIgnoreCase("Claim")) {
							error = true;
							if (!errorMsg.contains("Sharepoint"))
								errorMsg += " Sharepoint,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						} else if (childDto.getActionType().equalsIgnoreCase("Forward")) {
							error = true;
							if (!errorMsg.contains("Sharepoint"))
								errorMsg += " Sharepoint,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						} else if (childDto.getActionType().equalsIgnoreCase("Release")) {
							error = true;
							if (!errorMsg.contains("Sharepoint"))
								errorMsg += " Sharepoint,";
							resp = new ResponseMessage(PMCConstant.FAILURE, "0", PMCConstant.ACTION_NOT_SUPPORTED);
						}

					}
					break;

				case "Ad-hoc":
					System.err.println("[WBP-Dev][AD-HOC][ACTION]Inside Adhoc" + childDto + System.currentTimeMillis());
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						if (childDto.getComment() == null)
							childDto.setComment("");
						if (childDto.getActionType() == null)
							return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
						ParseResponse response = adhocActionFacade.actionOnTask(childDto, dto, token);
						System.err.println("[WBP-Dev][AD-HOC][ACTION] payload : " + new Gson().toJson(response));
						taskEvents.saveOrUpdateTasks(response.getTasks());
						taskOwners.saveOrUpdateOwners(response.getOwners());
						customAttrDao.addCustomAttributeValue(response.getCustomAttributeValues());
						taskAuditDao.saveOrUpdateAudits(response.getAuditDtos());
						customAttributeValuesTableAdhocDao.saveOrUpdateTableValuesDo(response.getCustomTableAttributeValues());
						resp = response.getResponseMessage();
						message = resp.getMessage();
					}
					break;

				case "JIRA":
					System.err.println("[WBP-Dev][SHAREPOINT][ACTION]Inside Jira");
					if (childDto.getActionType() == null)
						return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						resp = jiraActionFacade.completeTransitionOrForward(dto, childDto);
						message = resp.getMessage();
					}
					break;
				case "Zoho":
					System.err.println("[WBP-Dev][SHAREPOINT][ACTION]Inside Zoho");
					if (childDto.getActionType() == null)
						return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						if (childDto.getActionType() == null)
							return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
						if (childDto.getActionType().equalsIgnoreCase("Approve")) {
							resp = zohoActionFacade.acceptOrRejectRequest(dto, childDto, "1");
							message = resp.getMessage();
						} else if (childDto.getActionType().equalsIgnoreCase("Reject")) {
							resp = zohoActionFacade.acceptOrRejectRequest(dto, childDto, "0");
							message = resp.getMessage();
						} else if (childDto.getActionType().equalsIgnoreCase("Forward")) {
							resp = zohoActionFacade.forwardRequest(dto, childDto);
							message = resp.getMessage();
						} else {
							return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type not supported");
						}
					}

					break;

				case "ZohoExpenses":
					System.err.println("[WBP-Dev][SHAREPOINT][ACTION]Inside ZohoExpense");

					if (childDto.getActionType() == null)
						return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");
					if (!ServicesUtil.isEmpty(childDto.getActionType())) {
						if (childDto.getActionType().equalsIgnoreCase("Approve")) {
							resp = zohoExpenseActionFacade.acceptOrRejectRequest(dto, childDto, "approve");
							message = PMCConstant.APPROVED_SUCCESS;
						} else if (childDto.getActionType().equalsIgnoreCase("Reject")) {
							resp = zohoExpenseActionFacade.acceptOrRejectRequest(dto, childDto, "reject");
							message = PMCConstant.REJECTED_SUCCESS;
						} else {
							return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type not supported");
						}
					}

					break;

				case PMCConstant.MANUAL_TASK_ORIGIN:
					/*
					 * System.err.println( "[WORKBOX][WorkflowActionFacade][taskAction]"); if
					 * (dto.getActionType() == null) return new ResponseMessage("Action Failed", "",
					 * "Action Type is Null"); if (dto.getComment() == null) dto.setComment("");
					 * return maualTaskActions(dto);
					 */
				default:

					resp.setMessage(PMCConstant.ACTION_NOT_SUPPORTED);
					resp.setStatus(PMCConstant.FAILURE);
					resp.setStatusCode("1");
				}

				if (PMCConstant.STATUS_SUCCESS.equalsIgnoreCase(resp.getStatus())) {
					count++;
					try {
						taskAuditDao.saveOrUpdateAudit(new TaskAuditDto(
								UUID.randomUUID().toString().replaceAll("-", ""), childDto.getInstanceId(),
								dto.getUserId(), userDeatil.getUserFirstName() + " " + userDeatil.getUserLastName(),
								childDto.getComment(), childDto.getActionType(), new Date(),
								(ServicesUtil.isEmpty(childDto.getSendToUser()) ? null : childDto.getSendToUser()),
								(ServicesUtil.isEmpty(childDto.getSendToUser()) ? null
										: userIdpMappingDao.getUserName(childDto.getSendToUser()))));
					} catch (Exception e) {
						System.err.println("[WBP-Dev]Error Adding Audit" + e.getMessage());
					}
				}
			}
			if (count != 0) {
				if (count == 1)
					resp.setMessage("Task " + message);
				else {
					if ("jabilinventorymanagement".equalsIgnoreCase(dto.getProcessName()))
						resp.setMessage("Task Updated Successfully");
					else
						resp.setMessage(count + " Tasks " + message);
				}
			}
			if (error)
				resp.setMessage(resp.getMessage() + ", " + (dto.getTask().size() - count)
						+ ((dto.getTask().size() - count) > 1 ? " Tasks" : " Task")
						+ errorMsg.substring(0, errorMsg.length() - 1));

			if (!ServicesUtil.isEmpty(dto.getProcessLabel())
					&& (dto.getProcessLabel().equalsIgnoreCase("inventoryparentworkflow")
							|| dto.getProcessLabel().equalsIgnoreCase("ic_manager_approval_process")
							|| dto.getProcessLabel().equalsIgnoreCase("analyst_appproval_process"))) {
				resp.setMessage("Submitted Successfully.");
			}
			return resp;
		}
		return new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE, "Task Not Found");
	}

	// public String updateRecipientUserInWorkflow(String taskId, String
	// payload) {
	// try {
	// String requestUrl = PMCConstant.REQUEST_URL_INST + "task-instances/" +
	// taskId;
	//
	// RestResponse restResponse = SCPRestUtil.callRestService(requestUrl,
	// PMCConstant.SAML_HEADER_KEY_TI, payload,
	// "PATCH", "application/json", false, "Fetch", null, null, tokens[0],
	// tokens[1]);
	// if (!ServicesUtil.isEmpty(restResponse) &&
	// (restResponse.getResponseCode() >=
	// 200)
	// && (restResponse.getResponseCode() <= 207)) {
	// taskOwners.deleteUser(taskId, null);
	// }
	// } catch (Exception e) {
	// System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][updateRecipientUserInWorkflow][error]"
	// + e.getMessage());
	// e.printStackTrace();
	// System.err.println("[WBP-Dev][PMC][SubstitutionRuleFacade][updateRecipientUserInWorkflow][error]"
	// + e.getMessage());
	// }
	// return PMCConstant.SUCCESS;
	//
	// }

	/*
	 * public String getPayloadForForward(String processor, String taskId) { String
	 * listToString = "{ \"processor\":"; if (!ServicesUtil.isEmpty(processor)) {
	 * listToString += "\""; listToString += processor; }
	 * 
	 * listToString += "\""; List<String> recipientUser = getRecipientUser(taskId,
	 * PMCConstant.FORWARD_TASK, null); if (!ServicesUtil.isEmpty(processor) &&
	 * recipientUser.size() > 0 && !ServicesUtil.isEmpty(recipientUser)) {
	 * recipientUser.add(processor); } else if (ServicesUtil.isEmpty(recipientUser)
	 * && !ServicesUtil.isEmpty(processor)) { recipientUser = new ArrayList<>();
	 * recipientUser.add(processor); } if (!ServicesUtil.isEmpty(recipientUser) &&
	 * recipientUser.size() > 0) { listToString += " ,\"recipientUsers\":";
	 * listToString += "\""; for (String str : recipientUser) { listToString += str
	 * + ","; } listToString = listToString.substring(0, listToString.length() - 1);
	 * listToString += "\""; } listToString += "}"; return listToString; }
	 * 
	 * public List<String> getRecipientUser(String taskId, String type, JSONObject
	 * resource) { List<String> resultList = new ArrayList<String>(); JSONObject
	 * resources = resource; try { if (type.equals(PMCConstant.FORWARD_TASK)) {
	 * String requestUrl = PMCConstant.REQUEST_URL_INST + "task-instances/" +
	 * taskId; Object responseObject = SCPRestUtil.callRestService(requestUrl,
	 * PMCConstant.SAML_HEADER_KEY_TI, null, "GET", "application/json", false, null,
	 * null, null, tokens[0], tokens[1]).getResponseObject(); JSONObject jsonObject
	 * = ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject;
	 * resources = (JSONObject) jsonObject; } JSONArray jsonArray =
	 * resources.getJSONArray("recipientUsers"); for (int i = 0; i <
	 * jsonArray.length(); i++) resultList.add(jsonArray.optString(i)); String
	 * currentProcessor = resource.optString("processor");
	 * 
	 * if (!ServicesUtil.isEmpty(currentProcessor))
	 * resultList.add(currentProcessor); } catch (Exception e) { System.err.println(
	 * "[PMC][SubstitutionRuleFacade][getRecipientUser][error]" + e.getMessage()); }
	 * return resultList;
	 * 
	 * }
	 */

	/*
	 * private static ResponseMessage maualTaskActions(ActionDto dto) { String url =
	 * PMCConstant.MANUAL_TASK_URL + "/api/manual-task/updateStatus";
	 * ResponseMessage response = new ResponseMessage();
	 * response.setStatus(PMCConstant.FAILURE);
	 * response.setStatusCode(PMCConstant.CODE_FAILURE);
	 * response.setMessage("Failed to update the task"); String token = ""; try {
	 * JSONObject entity = new JSONObject(dto); Object responseObject =
	 * RestUtil.callRestService(url, null, entity.toString(), "POST",
	 * "application/json", true, null, null, null, null, null).getResponseObject();
	 * System.err.println("[WBP-Dev][WORKBOX][Manual WorkflowAction][URL]" + url +
	 * ",[entity]" + entity.toString()); JSONObject obj =
	 * ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject; if
	 * (!ServicesUtil.isEmpty(obj) &&
	 * PMCConstant.CODE_SUCCESS.equals(obj.optString("statusCode"))) {
	 * System.err.println("[WBP-Dev][WORKBOX][Manual WorkflowAction]" +
	 * responseObject); response.setStatus(PMCConstant.SUCCESS);
	 * response.setStatusCode(PMCConstant.CODE_SUCCESS);
	 * response.setMessage("Task Successfully " + dto.getActionType() + "ed"); } }
	 * catch (Exception e) {
	 * System.err.println("[WBP-Dev][WORKBOX][Manual WorkflowAction][Error]" +
	 * e.getLocalizedMessage() + ",[message] Failed to update the task:" +
	 * e.toString() + "token:" + token); }
	 *
	 * return response; }
	 */

	// public static void main(String args[]) {
	//
	// ActionRequestDto dto1 = new ActionRequestDto("COMPLETED", new
	// ActionRequestContextDto("false", "sdfg"));
	// JSONObject entity = new JSONObject(dto1);
	// System.out.println(entity.toString());
	//
	// }

	// private ResponseMessage doAdminAction(ActionDto dto) {
	// ResponseMessage message = null;
	// for (ActionDtoChild childDto : dto.getTask()) {
	// switch (childDto.getActionType().toUpperCase()) {
	// case "CLAIM":
	// //message = adminClaim(childDto.getInstanceId(), dto.getUserId());
	// case "RELEASE":
	// //message = adminRelease(childDto.getInstanceId());
	// case "FORWARD":
	// // return adminForward(dto.getInstanceList(),
	// // dto.getSendToUser());
	// message = bulkForward(childDto, dto);
	//
	// case "APPROVE":
	// return approveTask(childDto, dto);
	// case "REJECT":
	// return rejectTask(childDto, dto);
	//
	// }
	// }
	// return null;
	// }

	/*
	 * @SuppressWarnings("unused") private ResponseMessage adminForward(List<String>
	 * instanceList, String userId) { return
	 * updateWorkflow.adminForward(instanceList, userId); }
	 */

	/*
	 * private ResponseMessage adminRelease(List<String> instance) {
	 * inboxActions.saveOrUpdateAction(new InboxActions(instance.get(0), new Date(),
	 * null, true, null)); return updateWorkflow.adminRelease(instance); }
	 * 
	 * private ResponseMessage adminClaim(List<String> instance, String processor) {
	 * inboxActions.saveOrUpdateAction(new InboxActions(instance.get(0), new Date(),
	 * true, null, processor)); return updateWorkflow.adminClaim(instance,
	 * processor); }
	 */

	/*
	 * // TODO
	 * 
	 * @SuppressWarnings("unused") private ResponseMessage
	 * assignProcessor(ActionDtoChild childDto, String processor) { if
	 * (ServicesUtil.isEmpty(processor)) return new ResponseMessage("failed", "",
	 * "Please Specify the Valid processor"); ResponseMessage responseMessage = new
	 * ResponseMessage();
	 * 
	 * try {
	 * 
	 * } catch (Exception e) {
	 * 
	 * }
	 * 
	 * return null; }
	 */

	/*
	 * private ResponseMessage relaseTask(ActionDtoChild childDto, String userId) {
	 * ResponseMessage response = new ResponseMessage(); try { // for (String
	 * instanceId : instanceList) { String instanceId = childDto.getInstanceId();
	 * String releaseTask = PMCConstant.REQUEST_BASE_URL_TC +
	 * "/Release?SAP__Origin=%27NA%27&InstanceID='" + instanceId + "'"; // String
	 * token = getScrfToken(RELEASE_TASK); RestResponse restResponse =
	 * RestUtil.callRestService(releaseTask, PMCConstant.SAML_HEADER_KEY_TC, null,
	 * "POST", "application/json", true, "Fetch", null, null, null, null); if
	 * (!ServicesUtil.isEmpty(restResponse) &&
	 * !ServicesUtil.isEmpty(restResponse.getResponseCode())) { if
	 * (restResponse.getResponseCode() == 201) { // releaseInstance(instanceId,
	 * userId); inboxActions.saveOrUpdateAction(new InboxActions(instanceId, new
	 * Date(), null, true, userId)); } }
	 * 
	 * response.setStatus("Success");
	 * response.setStatusCode(PMCConstant.CODE_SUCCESS);
	 * response.setMessage("Task Successfully Released"); } catch (Exception e) {
	 * response.setStatus("Fail"); response.setStatusCode(PMCConstant.CODE_FAILURE);
	 * response.setMessage("Fail to release the task"); } return response; }
	 */

	/*
	 * private ResponseMessage claimTask(ActionDtoChild childDto, ActionDto dto) {
	 * ResponseMessage response = new ResponseMessage();
	 * 
	 * try { // System.err.println("[WBP-Dev]token for claim request: "+token); //
	 * for (String instanceId : instanceList) { String instanceId =
	 * childDto.getInstanceId(); String claimTask = PMCConstant.REQUEST_BASE_URL_TC
	 * + "/Claim?SAP__Origin=%27NA%27&InstanceID='" + instanceId + "'"; // String
	 * token = getScrfToken(RELEASE_TASK); RestResponse restResponse =
	 * RestUtil.callRestService(claimTask, PMCConstant.SAML_HEADER_KEY_TC, null,
	 * "POST", "application/json", true, "Fetch", null, null, null, null);
	 * System.err.println("[WBP-Dev]rest Response= " + restResponse); if
	 * (!ServicesUtil.isEmpty(restResponse) &&
	 * !ServicesUtil.isEmpty(restResponse.getResponseCode())) { if
	 * (restResponse.getResponseCode() == 201) { // claimInstance(instanceId,
	 * userId); inboxActions .saveOrUpdateAction(new InboxActions(instanceId, new
	 * Date(), true, null, dto.getUserId())); response.setStatus("Success");
	 * response.setStatusCode(PMCConstant.CODE_SUCCESS);
	 * response.setMessage("Task Successfully Claimed"); } else {
	 * 
	 * response.setStatus("Fail"); response.setStatusCode(PMCConstant.CODE_FAILURE);
	 * response.setMessage("Failed to Claim the task with status " +
	 * restResponse.getResponseCode() + " " + restResponse.getResponseObject()); } }
	 * 
	 * } catch (Exception e) { response.setStatus("Fail");
	 * response.setStatusCode(PMCConstant.CODE_FAILURE);
	 * response.setMessage("Failed to Claim the task" + e.toString());
	 * System.err.println("[WBP-Dev]claim failed:" + e.getStackTrace()); } return
	 * response; }
	 */

	/*
	 * @SuppressWarnings("unused") private void claimInstance(String instanceId,
	 * String userId) { int updateTE; int updateTW; // String updateTEQuery =
	 * "UPDATE TASK_EVENTS SET STATUS = 'RESERVED' // WHERE EVENT_ID =
	 * '"+instanceId+"'"; // String updateTWQuery = "UPDATE TASK_OWNERS SET
	 * IS_PROCESSED = '1' // WHERE EVENT_ID = '"+instanceId+"' AND TASK_OWNER =
	 * '"+userId+"'"; try { // updateTE =
	 * taskEvents.executeUpdateQuery(updateTEQuery); // updateTW =
	 * taskOwners.executeUpdateQuery(updateTWQuery);
	 * 
	 * updateTE = taskEvents.changeTaskStatus(new TaskEventsDo(instanceId,
	 * "RESERVED", userId)); updateTW = taskOwners.changeOwnerStatus(new
	 * TaskOwnersDo(new TaskOwnersDoPK(instanceId, userId), true));
	 * 
	 * System.err.
	 * println("[WBP-Dev][WorkflowAction]Updating Claim in DB Success : " + updateTE
	 * + ", " + updateTW);
	 * 
	 * } catch (Exception ex) { System.err.
	 * println("[WBP-Dev][WorkflowAction]Updating Claim in DB Failed : " +
	 * ex.getMessage()); } }
	 * 
	 * @SuppressWarnings("unused") private void releaseInstance(String instanceId,
	 * String userId) { int updateTE; int updateTW; // String updateTEQuery =
	 * "UPDATE TASK_EVENTS SET STATUS = 'READY' WHERE // EVENT_ID =
	 * '"+instanceId+"'"; // String updateTWQuery = "UPDATE TASK_OWNERS SET
	 * IS_PROCESSED = '0' // WHERE EVENT_ID = '"+instanceId+"' AND TASK_OWNER =
	 * '"+userId+"'"; try { // updateTE =
	 * taskEvents.executeUpdateQuery(updateTEQuery); // updateTW =
	 * taskOwners.executeUpdateQuery(updateTWQuery);
	 * 
	 * updateTE = taskEvents.changeTaskStatus(new TaskEventsDo(instanceId, "READY",
	 * null)); updateTW = taskOwners.changeOwnerStatus(new TaskOwnersDo(new
	 * TaskOwnersDoPK(instanceId, userId), false));
	 * 
	 * System.err.
	 * println("[WBP-Dev][WorkflowAction]Updating Release in DB Success : " +
	 * updateTE + ", " + updateTW); } catch (Exception ex) { System.err.
	 * println("[WBP-Dev][WorkflowAction]Updating Claim in DB Failed : " +
	 * ex.getMessage()); } }
	 * 
	 * private ResponseMessage rejectTask(ActionDtoChild childDto, ActionDto
	 * actionDto) {
	 * 
	 * String REJECT_TASK = PMCConstant.REQUEST_URL_INST + "task-instances/";
	 * ResponseMessage response = new ResponseMessage(); String responseMessage =
	 * ""; int count = 0; List<TaskStatus> listTasks = new ArrayList<>(); TaskStatus
	 * taskStatus = null; int taskCount = 0;
	 * 
	 * String message = ""; try { // for (String instanceId :
	 * actionDto.getInstanceList()) { String instanceId = childDto.getInstanceId();
	 * String url = REJECT_TASK + instanceId; // String token = getScrfToken(url);
	 * count++; ActionRequestDto dto = new ActionRequestDto("COMPLETED", new
	 * ActionRequestContextDto("false", childDto.getComment())); JSONObject entity =
	 * new JSONObject(dto); RestResponse restResponse =
	 * SCPRestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI,
	 * entity.toString(), "PATCH", "application/json", true, "Fetch", null, null,
	 * null, null);
	 * System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() Object :" +
	 * restResponse); if (!ServicesUtil.isEmpty(restResponse) &&
	 * (restResponse.getResponseCode() >= 200) && (restResponse.getResponseCode() <=
	 * 207)) {
	 * 
	 * // Notify the user
	 * 
	 * Boolean status = notityUser(childDto, taskCount,actionDto); System.err.
	 * println("WorkFlowActionFacade.rejectTask() Notification sent : " + status);
	 * 
	 * taskEvents.updateTaskEventToCompleted(instanceId);
	 * taskEvents.updateTaskComment(instanceId, actionDto.getUserId(),
	 * childDto.getComment(), childDto.getActionType());
	 * 
	 * taskStatus = new TaskStatus(); taskStatus.setEventId(instanceId);
	 * taskStatus.setIsApproved(false);
	 * taskStatus.setComments(childDto.getComment()); listTasks.add(taskStatus);
	 * 
	 * } else { count--; responseMessage += instanceId + ","; message =
	 * restResponse.getResponseObject().toString(); }
	 * 
	 * taskCount++; // } response.setStatus(PMCConstant.SUCCESS);
	 * response.setStatusCode(PMCConstant.CODE_SUCCESS);
	 * 
	 * if (count != 0) { if (count == 1)
	 * response.setMessage("Task Successfully Rejected"); else
	 * response.setMessage(count + " Tasks are Successfully Rejected"); } else {
	 * response.setMessage("Unable to Reject Task:" + responseMessage + " : " +
	 * message); response.setStatus(PMCConstant.FAILURE);
	 * response.setStatusCode(PMCConstant.CODE_FAILURE); }
	 * 
	 * } catch (Exception e) { response.setStatus(PMCConstant.FAILURE);
	 * response.setStatusCode(PMCConstant.CODE_FAILURE);
	 * response.setMessage("failed to Reject the task"); }
	 * 
	 * taskEvents.saveTaskStatus(listTasks);
	 * 
	 * return response;
	 * 
	 * }
	 * 
	 * @SuppressWarnings("unused") private ResponseMessage
	 * approveTask(ActionDtoChild childActionDto, ActionDto actionDto) { String
	 * APPROVE_TASK = PMCConstant.REQUEST_URL_INST + "task-instances/";
	 * ResponseMessage response = new ResponseMessage(); String token = ""; int
	 * count = 0;
	 * 
	 * List<TaskStatus> listTasks = new ArrayList<>(); TaskStatus taskStatus = null;
	 * int taskCount = 0;
	 * 
	 * NotificationDto notificationdto =null; List<String> users=null ;
	 * 
	 * String message = ""; try { // for (String instanceId :
	 * actionDto.getInstanceList()) { String instanceId =
	 * childActionDto.getInstanceId(); String url = APPROVE_TASK + instanceId; //
	 * token = getScrfToken(url); count++; ActionRequestDto dto = new
	 * ActionRequestDto("COMPLETED", new ActionRequestContextDto("true",
	 * childActionDto.getComment())); JSONObject entity = new JSONObject(dto); //
	 * System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() // :"
	 * +entity); RestResponse restResponse = SCPRestUtil.callRestService(url,
	 * PMCConstant.SAML_HEADER_KEY_TI, entity.toString(), "PATCH",
	 * "application/json", true, "Fetch", null, null, null, null);
	 * 
	 * System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() Object :" +
	 * restResponse);
	 * 
	 * if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >=
	 * 200) && (restResponse.getResponseCode() <= 207)) {
	 * 
	 * // Notify the user // Boolean status = notityUser(actionDto, taskCount); //
	 * System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() //
	 * Notification sent : " + status);
	 * 
	 * taskEvents.updateTaskEventToCompleted(instanceId);
	 * taskEvents.updateTaskComment(instanceId, actionDto.getUserId(),
	 * childActionDto.getComment(), childActionDto.getActionType()); taskStatus =
	 * new TaskStatus(); taskStatus.setEventId(instanceId);
	 * taskStatus.setIsApproved(true);
	 * taskStatus.setComments(childActionDto.getComment());
	 * listTasks.add(taskStatus);
	 * 
	 * } else { count--;
	 * 
	 * message = restResponse.getResponseObject().toString(); }
	 * 
	 * taskCount++; // }
	 * 
	 * response.setStatus(PMCConstant.SUCCESS);
	 * response.setStatusCode(PMCConstant.CODE_SUCCESS);
	 * 
	 * if (count != 0) { if (count != 1) response.setMessage(count +
	 * " Tasks are Successfully Approved"); else
	 * response.setMessage(" Task Successfully Approved"); } else {
	 * response.setMessage("Unable to Approve Tasks : " + message);
	 * response.setStatus(PMCConstant.FAILURE);
	 * response.setStatusCode(PMCConstant.CODE_FAILURE); }
	 * 
	 * } catch (Exception e) { response.setStatus(PMCConstant.FAILURE);
	 * response.setStatusCode(PMCConstant.CODE_FAILURE);
	 * response.setMessage("Failed to update the task:" + e.toString() + "token:" +
	 * token); }
	 * 
	 * taskEvents.saveTaskStatus(listTasks);
	 * 
	 * return response; }
	 * 
	 * private Boolean notityUser(ActionDtoChild childDto, int taskCount, ActionDto
	 * dto) { // Notify the requester NotificationDto notificationdto = null;
	 * List<String> users = null; String actionType = "";
	 * 
	 * if (childDto.getActionType().equalsIgnoreCase("Approve")) { actionType =
	 * " is Approved By "; if (ServicesUtil.isEmpty(childDto.getRequesterIds())) {
	 * System.err.
	 * println("WorkFlowActionFacade.notityUser() : No requester id found to send notification --> "
	 * + childDto); return false; } } else if
	 * (childDto.getActionType().equalsIgnoreCase("Reject")) { actionType =
	 * " is Rejected By ";
	 * 
	 * if (ServicesUtil.isEmpty(childDto.getRequesterIds())) { System.err.
	 * println("WorkFlowActionFacade.notityUser() : No requester id found to send notification --> "
	 * + childDto); return false; } } else if
	 * (childDto.getActionType().equalsIgnoreCase("Forward")) { actionType =
	 * " is Forwarded By "; } else { actionType = " "; }
	 * 
	 * try { notificationdto = new NotificationDto(); users = new ArrayList<>();
	 * 
	 * if (childDto.getActionType().equalsIgnoreCase("Forward")) {
	 * users.add(childDto.getSendToUser()); } else {
	 * users.add(childDto.getRequesterIds().get(taskCount)); }
	 * 
	 * notificationdto.setAlert(childDto.getProcessLabel());
	 * notificationdto.setData(childDto.getSubject() + actionType +
	 * dto.getUserDisplay()); notificationdto.setUsers(users); } catch (Exception e)
	 * { System.err.println("[WBP-Dev]WorkFlowActionFacade.notityUser() error : " +
	 * e); } return pushNotificationService.notifyUser(notificationdto); }
	 * 
	 * public static void main(String[] args) { ActionDto dto = new ActionDto();
	 * List<ActionDtoChild> actionDtoChilds = new ArrayList<>(); ActionDtoChild
	 * childActionDto = new ActionDtoChild();
	 * childActionDto.setActionType("Approve"); dto.setUserId("P000100");
	 * dto.setAction("Approve"); // List<String> list = new ArrayList<>(); //
	 * list.add("6c709ec9-98c5-11e9-a8d0-00163e8c1bdb");
	 * childActionDto.setInstanceId("6c709ec9-98c5-11e9-a8d0-00163e8c1bd");
	 * childActionDto.setOrigin("SCP"); actionDtoChilds.add(childActionDto);
	 * dto.setTask(actionDtoChilds); dto.setIsAdmin(false); dto.setIsChatBot(true);
	 * WorkFlowActionFacade wf = new WorkFlowActionFacade();
	 * wf.approveTask(childActionDto,dto); }
	 * 
	 * @SuppressWarnings("unused") private ResponseMessage
	 * approveTaskForChatBot(ActionDtoChild childDto, ActionDto dto) { System.err.
	 * println("[WBP-Dev]WorkFlowActionFacade.approveTask() action dto : " + dto);
	 * String APPROVE_TASK = PMCConstant.REQUEST_URL_INST + "task-instances/";
	 * ResponseMessage response = new ResponseMessage(); String token = ""; int
	 * count = 0;
	 * 
	 * // List<String> instanceList = dto.getInstanceList(); try { // for (String
	 * instanceId : instanceList) { String url = APPROVE_TASK +
	 * childDto.getInstanceId(); // token = getScrfToken(url);
	 * 
	 * // claiming the task first // String tokenurl = // //
	 * "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/rest/v1/xsrf-token";
	 * // String claimPayload = "{\"processor\":\"" + dto.getUserId() + // "\"}"; //
	 * String approvalPaload = "{\"status\":\"COMPLETED\"}";
	 * 
	 * // System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() claim //
	 * payload : " + claimPayload); //
	 * System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() // approve
	 * payload : " + approvalPaload);
	 * 
	 * // claiming the task first String tokenurl =
	 * "https://bpmworkflowruntimea2d6007ea-a8b98c03e.hana.ondemand.com/workflow-service/rest/v1/xsrf-token";
	 * String claimPayload = "{\"processor\":\"" + dto.getUserId() + "\"}"; String
	 * approvalPaload = "{\"status\":\"COMPLETED\"}"; // //
	 * System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() claim //
	 * payload : "+claimPayload); //
	 * System.err.println("[WBP-Dev]WorkFlowActionFacade.approveTask() // approve
	 * payload : "+approvalPaload);
	 * 
	 * System.err.
	 * println("[WBP-Dev]WorkFlowActionFacade.approveTask() token type : ");
	 * 
	 * RestResponse restResponse1 = SCPRestUtil.callRestService(url, null,
	 * claimPayload, "PATCH", "application/json", false, "Fetch", "P000092",
	 * "Workbox@123", null, null);
	 * 
	 * System.err.
	 * println("WorkFlowActionFacade.approveTask() claim task response :" +
	 * restResponse1);
	 * 
	 * RestResponse restResponse2 = SCPRestUtil.callRestService(url, null,
	 * approvalPaload, "PATCH", "application/json", false, "Fetch", "P000092",
	 * "Workbox@123", null, null);
	 * 
	 * System.err. println("WorkFlowActionFacade.approveTask() approve response :" +
	 * restResponse2); count++; if (!ServicesUtil.isEmpty(restResponse2) &&
	 * (restResponse2.getResponseCode() >= 200) && (restResponse2.getResponseCode()
	 * <= 207)) {
	 * 
	 * taskEvents.updateTaskEventToCompleted(childDto.getInstanceId());
	 * taskEvents.updateTaskComment(childDto.getInstanceId(), dto.getUserId(),
	 * childDto.getComment(), childDto.getActionType()); } else { count--;
	 * 
	 * }
	 * 
	 * // } response.setStatus(PMCConstant.SUCCESS);
	 * response.setStatusCode(PMCConstant.CODE_SUCCESS);
	 * 
	 * if (count != 0) { if (count != 1) response.setMessage(count +
	 * " Tasks are Successfully Approved"); else
	 * response.setMessage(" Task Successfully Approved"); } else {
	 * response.setMessage("Unable to Approve Tasks");
	 * response.setStatus(PMCConstant.FAILURE);
	 * response.setStatusCode(PMCConstant.CODE_FAILURE); }
	 * 
	 * } catch (Exception e) { response.setStatus(PMCConstant.FAILURE);
	 * response.setStatusCode(PMCConstant.CODE_FAILURE);
	 * response.setMessage("Failed to update the task:" + e.toString() + "token:" +
	 * token); }
	 * 
	 * return response; }
	 * 
	 * @SuppressWarnings("unused") private ResponseMessage
	 * approveManualTask(List<String> instanceList, String comment) { String
	 * APPROVE_TASK = PMCConstant.REQUEST_URL_INST + "task-instances/";
	 * ResponseMessage response = new ResponseMessage(); String token = ""; try {
	 * for (String instanceId : instanceList) { String url = APPROVE_TASK +
	 * instanceId; // token = getScrfToken(url); ActionRequestDto dto = new
	 * ActionRequestDto("COMPLETED", new ActionRequestContextDto("true", comment));
	 * JSONObject entity = new JSONObject(dto); Object responseObject =
	 * RestUtil.callRestService(url, PMCConstant.SAML_HEADER_KEY_TI,
	 * entity.toString(), "PATCH", "application/json", true, "Fetch", null, null,
	 * null, null).getResponseObject(); JSONObject obj =
	 * ServicesUtil.isEmpty(responseObject) ? null : (JSONObject) responseObject; }
	 * response.setStatus(PMCConstant.SUCCESS);
	 * response.setStatusCode(PMCConstant.CODE_SUCCESS);
	 * response.setMessage("Tasks Approved"); } catch (Exception e) {
	 * response.setStatus(PMCConstant.FAILURE);
	 * response.setStatusCode(PMCConstant.CODE_FAILURE);
	 * response.setMessage("Failed to update the task:" + e.toString() + "token:" +
	 * token); }
	 * 
	 * return response; }
	 * 
	 * 
	 * @SuppressWarnings("unused") private String getScrfToken(String host) { String
	 * token = ""; // , GET_TOKEN = ""; // if (host.equalsIgnoreCase("workflow")) //
	 * GET_TOKEN = BASE_URL + "/v1/xsrf-token"; // else // GET_TOKEN =
	 * FIORI_BASE_URL; Object responseObject = SCPRestUtil.callRestService(host,
	 * null, "GET", "application/json", true, "Fetch", null, null, null, null);
	 * JSONObject tokenObj = ServicesUtil.isEmpty(responseObject) ? null :
	 * (JSONObject) responseObject; if (ServicesUtil.isEmpty(tokenObj)) return
	 * "Error"; else return tokenObj.optString("x-csrf-token", "ss"); }
	 * 
	 * // Method to create new WorkFlow Instance.
	 */
	@Override
	public ResponseMessage createWorkFlowInstance(String payload) {
		ResponseMessage response = new ResponseMessage();
		System.err.println("[WBP-Dev]WorkFlowInstance Payload:" + payload);
		String res = updateWorkflow.createWorkFlowInstance(payload);

		if ((PMCConstant.SUCCESS).equalsIgnoreCase(res)) {
			response.setMessage("WorkFlow Instance Created SuccessFully");
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
		}

		else {
			response.setMessage("Failed to create WorkFlow Instance");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);

		}

		return response;

	}

	@Override
	public ResponseMessage createWorkFlowInstanceMap() {
		ResponseMessage response = new ResponseMessage();
		Map<String, String> payload = prepareSampleData();
		System.err.println("WorkFlowInstance Payload:" + payload);
		String res = updateWorkflow.createWorkFlowInstanceMap(payload);

		if ((PMCConstant.SUCCESS).equalsIgnoreCase(res)) {
			response.setMessage("WorkFlow Instance Created SuccessFully");
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
		}

		else {
			response.setMessage("Failed to create WorkFlow Instance");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);

		}

		return response;

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

	@Override
	public ResponseMessage createWorkFlowInstance(JSONObject payload) {
		ResponseMessage response = new ResponseMessage();
		System.err.println("[WBP-Dev]WorkFlowInstance Payload:" + payload);

		String res = updateWorkflow.createWorkFlowInstance(payload);

		if ((PMCConstant.SUCCESS).equalsIgnoreCase(res)) {
			response.setMessage("WorkFlow Instance Created SuccessFully");
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
		}

		else {
			response.setMessage("Failed to create WorkFlow Instance");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);

		}

		return response;

	}

	@Override
	public ResponseMessage actionsSync(List<ActionDto> dtos, XsuaaToken token) {
		ResponseMessage res = new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE,
				"Fail to Sync Actions");
		try {
			int count = 0;

			for (ActionDto actionDto : dtos) {
				ResponseMessage response = taskAction(actionDto, token);
				if (response.getStatus().equals(PMCConstant.FAILURE))
					count++;

			}

			res = new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS, "Sync Successful");
			if (count != 0) {
				res.setMessage(count + " Actions are not Synchronized properly");
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WorkFlowActionFacade][actionSync]:" + e.getMessage());
		}
		return res;
	}

	@Override
	public WorkflowResponseDto getContextData(String taskId) {
		WorkflowResponseDto responseDto = new WorkflowResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			RestResponse response = workflow.getContextData(taskId);
			System.err.println(
					"[WBP-Dev]WorkFlowActionFacade.getContextData() " + response.getResponseObject().toString());
			responseDto.setData(response.getResponseObject().toString());
			responseMessage.setMessage("Context Fetched Successfully");
			responseMessage.setStatus("pass");
			responseMessage.setStatusCode("200");
			responseDto.setResponseMessage(responseMessage);
		} catch (Exception e) {
			responseMessage.setMessage(e.getMessage());
			responseMessage.setStatus("Fail");
			responseMessage.setStatusCode("500");
			responseDto.setResponseMessage(responseMessage);
		}
		return responseDto;

	}
}
