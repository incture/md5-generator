package oneapp.incture.workbox.demo.adhocTask.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessTemplateValueDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskAuditDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValueTableAdhocDo;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessTemplate;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.AfeNexusOrderDao;
import oneapp.incture.workbox.demo.adhocTask.dao.CrossConstantDao;
import oneapp.incture.workbox.demo.adhocTask.dao.CustomAttributeValueDao;
import oneapp.incture.workbox.demo.adhocTask.dao.ProcessEventDao;
import oneapp.incture.workbox.demo.adhocTask.dao.SubstitutionDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskEventDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskOwnerDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskTemplateDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskValueDao;
import oneapp.incture.workbox.demo.adhocTask.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeValues;
import oneapp.incture.workbox.demo.adhocTask.dto.PpdDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskOwnerDto;
import oneapp.incture.workbox.demo.adhocTask.entity.TaskValueDo;
import oneapp.incture.workbox.demo.ecc.services.EccActionFacade;
import oneapp.incture.workbox.demo.substitution.dto.UserTaskMappingDto;
import oneapp.incture.workbox.demo.substitution.services.SubstitutionRuleFacadeLocal;
import oneapp.incture.workbox.demo.workflow.dao.ProcessTemplateDao;
import oneapp.incture.workbox.demo.workflow.dao.RuleDao;
import oneapp.incture.workbox.demo.workflow.dto.RuleDto;

@Component
public class ActionFacade {

	@Autowired
	SubstitutionDao substitutionDao;

	@Autowired
	private ProcessEventDao processEventDao;

	@Autowired
	private TaskEventDao taskEventDao;

	@Autowired
	CrossConstantDao crossConstantDao;

	@Autowired
	private TaskOwnerDao taskOwnerDao;

	@Autowired
	private CustomAttributeValueDao customAttributeValueDao;

	@Autowired
	private UserIDPMappingDao userIDPMappingDao;

	// Map<String, String> ppdCostCenterValueMap;

	@Autowired
	private TaskValueDao taskValueDao;

	@Autowired
	TaskEventsDao taskEventsDao;
	@Autowired
	TaskOwnersDao taskOwnersDao;
	@Autowired
	CustomAttributeDao customAttrDao;
	@Autowired
	TaskAuditDao taskAuditDao;

	@Autowired
	RuleDao ruleDao;

	@Autowired
	PropertiesConstants getProperty;

	@Autowired
	TaskTemplateDao taskTemplateDao;

	@Autowired
	ProcessTemplateDao processTemplateDao;

	@Autowired
	ProcessTemplateValueDao processTemplateValueDao;

	@Autowired
	SubstitutionRuleFacadeLocal substitutionService;

	@Autowired
	private TimeZoneConvertion timeZoneConvertion;

	@Autowired
	private EccActionFacade eccActionFacade;

	@Autowired
	private AfeNexusOrderDao afeNexusOrderDao;

	List<TaskEventsDto> tasks;
	List<TaskOwnersDto> owners;
	List<CustomAttributeValue> customAttributeValueDtos;
	List<CustomAttributeValueTableAdhocDo> customTableAttributeValueDtos;

	public ParseResponse actionOnTask(ActionDtoChild childDto, ActionDto dto, Token token) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		tasks = new ArrayList<TaskEventsDto>();
		owners = new ArrayList<TaskOwnersDto>();
		customAttributeValueDtos = new ArrayList<CustomAttributeValue>();
		customTableAttributeValueDtos = new ArrayList<CustomAttributeValueTableAdhocDo>();
		ParseResponse response = new ParseResponse();
		response.setResponseMessage(resp);

		// for (TaskDto task : actionDto.getTask()) {
		try {
			/*
			 * User user = UserManagementUtil.getLoggedInUser();
			 * System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());
			 * dto.setUserId(user.getName().toUpperCase());
			 */
			dto.setUserDisplay(userIDPMappingDao.getUserName(dto.getUserId()));
			switch (childDto.getActionType()) {
			case TaskCreationConstant.CLAIM:

				try {
					Boolean flag = true;

					// SubstitutionRuleResponseDto subsDto =
					// substitutionService.getSubstituted(dto.getUserId());

					if (!childDto.getIsAdmin()) {
						List<String> dtoList = substitutionService.getSubstitutedUserForTask(dto.getUserId(),
								childDto.getInstanceId());
						if (!ServicesUtil.isEmpty(dtoList) || dtoList.size() > 0) {

							List<TaskOwnerDto> taskOwnerDto = setTaskOwner(dto.getUserId().split(","));
							List<UserTaskMappingDto> list = setUserTaskMapping(dtoList, dto.getUserId(),
									childDto.getInstanceId());
							taskOwnerDao.saveInTaskOwner(childDto.getInstanceId(), taskOwnerDto, true);
							substitutionService.saveUserTaskMapping(list);
						}
					}

					if (!dto.getIsChatbot()) {

						if (taskEventDao.updateStatusInTaskEvents(childDto.getInstanceId(),
								TaskCreationConstant.RESERVED, dto.getUserId(), dto.getUserDisplay(),
								childDto.getIsAdmin(), token) < 1)
							flag = false;
					} else {
						if (taskEventDao.updateStatusChatbotInTaskEvents(childDto.getInstanceId(),
								TaskCreationConstant.RESERVED, dto.getUserId(), dto.getUserDisplay(),
								childDto.getIsAdmin()) < 1)
							flag = false;
					}

					if (!flag) {
						resp.setMessage(PMCConstant.CLAIM_FAILURE);
					} else {
						processEventDao.updateStatusInProcessEvents(childDto.getInstanceId(),
								TaskCreationConstant.RUNNING);
						//taskOwnerDao.updateIsProcessed(childDto.getInstanceId(), dto.getUserId(), 1);
						resp.setMessage(PMCConstant.CLAIM_SUCCESS);

					}
					resp.setStatus(TaskCreationConstant.SUCCESS);
					resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
					response.setResponseMessage(resp);
				} catch (Exception e) {
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
				}
				break;

			case TaskCreationConstant.RELEASE:
				try {

					if (!dto.getIsChatbot()) {
						taskEventDao.updateStatusInTaskEvents(childDto.getInstanceId(), TaskCreationConstant.READY, "",
								"", childDto.getIsAdmin(), token);
					} else {
						taskEventDao.updateStatusChatbotInTaskEvents(childDto.getInstanceId(),
								TaskCreationConstant.READY, "", "", childDto.getIsAdmin());
					}

					processEventDao.updateStatusInProcessEvents(childDto.getInstanceId(), TaskCreationConstant.RUNNING);
					Boolean isSubstituted = substitutionService.getIsSubstituted(childDto.getInstanceId(),
							dto.getUserId());
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]isSubstituted : " + isSubstituted);
					if (isSubstituted)
						taskOwnerDao.deleteTaskOwner(childDto.getInstanceId(), dto.getUserId());
					else
						taskOwnerDao.updateIsProcessed(childDto.getInstanceId(), dto.getUserId(), 0);

					resp.setMessage(PMCConstant.RELEASE_SUCCESS);
					resp.setStatus(TaskCreationConstant.SUCCESS);
					resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
					response.setResponseMessage(resp);

				} catch (Exception e) {
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
				}
				break;

			case TaskCreationConstant.FORWARD:
				try {
					if (ServicesUtil.isEmpty(childDto.getSendToUser())) {
						resp.setMessage("Select an Owner");
						resp.setStatus(TaskCreationConstant.FAILURE);
						resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
						response.setResponseMessage(resp);
						return response;
					}
					String[] ownerList = childDto.getSendToUser().split(",");
					if (Arrays.asList(ownerList).isEmpty() || Arrays.asList(ownerList).size() > 1) {
						resp.setMessage("Select an Owner");
						resp.setStatus(TaskCreationConstant.FAILURE);
						resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
						response.setResponseMessage(resp);
						return response;
					}
					List<TaskOwnerDto> taskOwnerDto = setTaskOwner(ownerList);

					if (!childDto.getIsAdmin()) {
						List<String> dtoList = substitutionService.getSubstitutedUserForTask(dto.getUserId(),
								childDto.getInstanceId());
						if (!ServicesUtil.isEmpty(dtoList) || dtoList.size() > 0) {

							List<UserTaskMappingDto> list = setUserTaskMapping(dtoList, dto.getUserId(),
									childDto.getInstanceId());
							substitutionService.saveUserTaskMapping(list);
						}
					}

					int update = taskEventDao.updateStatusInTaskEvents(childDto.getInstanceId(),
							TaskCreationConstant.RESERVED, taskOwnerDto.get(0).getTaskOwner(),
							taskOwnerDto.get(0).getTaskOwnername(), childDto.getIsAdmin(), token);

					if (update < 1) {
						resp.setMessage(PMCConstant.FORWARD_FAILURE);
						resp.setStatus(TaskCreationConstant.FAILURE);
						resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
						response.setResponseMessage(resp);
						return response;
					}
					taskEventDao.updateForwardedByInTE(childDto.getInstanceId(),
							userIDPMappingDao.getUserName(dto.getUserId()));
					processEventDao.updateStatusInProcessEvents(childDto.getInstanceId(), TaskCreationConstant.RUNNING);
					taskOwnerDao.updateIsProcessed(childDto.getInstanceId(), dto.getUserId(), 0);
					taskOwnerDao.saveInTaskOwner(childDto.getInstanceId(), taskOwnerDto, false);
					resp.setMessage(PMCConstant.FORWARD_SUCCESS);
					resp.setStatus(TaskCreationConstant.SUCCESS);
					resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
					response.setResponseMessage(resp);
				} catch (Exception e) {
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
					e.printStackTrace();
				}
				break;

			case TaskCreationConstant.NOMINATE:
				StringBuilder ownerNames = new StringBuilder("");
				try {
					String[] ownerList = childDto.getSendToUser().split(",");

					if (childDto.getGroupName().isEmpty() && Arrays.asList(ownerList).isEmpty()) {
						resp.setMessage("Nominate Failed");
						resp.setStatus(TaskCreationConstant.FAILURE);
						resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
						response.setResponseMessage(resp);
						return response;
					}
					List<TaskOwnerDto> taskOwnerDto = setTaskOwner(ownerList);

					taskEventDao.updateStatusInTaskEvents(childDto.getInstanceId(), TaskCreationConstant.READY, "", "",
							childDto.getIsAdmin(), token);
					taskOwnerDao.updateIsProcessed(childDto.getInstanceId(), dto.getUserId(), 0);
					taskOwnerDao.saveInTaskOwner(childDto.getInstanceId(), taskOwnerDto, false);

					for (TaskOwnerDto owners : taskOwnerDto) {
						ownerNames.append(owners.getTaskOwnername());
						ownerNames.append(", ");
					}

					response.setResponseMessage(resp);
				} catch (Exception e) {
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
				}
				break;

			case TaskCreationConstant.ACCEPTED:
			case TaskCreationConstant.DECLINED:

				try {

					taskEventDao.updateStatusInTaskEvents(childDto.getInstanceId(), childDto.getActionType(),
							dto.getUserId(), dto.getUserDisplay(), childDto.getIsAdmin(), token);
					UserIDPMappingDto userIDPMappingDto = userIDPMappingDao.getOwnerDetail(childDto.getInstanceId());
					taskEventDao.updateStatusInParentTask(taskEventDao.getParentTaskId(childDto.getInstanceId()),
							TaskCreationConstant.RESERVED, userIDPMappingDto.getUserId(),
							userIDPMappingDto.getUserFirstName() + " " + userIDPMappingDto.getUserLastName());

					response.setResponseMessage(resp);
				} catch (Exception e) {

					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
				}
				break;

			case TaskCreationConstant.SEND:
				try {

					String[] ownerList = childDto.getSendToUser().split(",");
					if (Arrays.asList(ownerList).isEmpty() || Arrays.asList(ownerList).size() > 1) {
						resp.setMessage("Select an Owner");
						resp.setStatus(TaskCreationConstant.FAILURE);
						resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
						response.setResponseMessage(resp);
						return response;
					}

					taskEventDao.updateStatusInParentTask(childDto.getInstanceId(), TaskCreationConstant.RESERVED, "",
							"");
					taskOwnerDao.updateIsProcessed(childDto.getInstanceId(), dto.getUserId(), 0);
					createSubTask(childDto.getInstanceId(), Arrays.asList(ownerList), dto.getUserId(),
							childDto.getTaskName());

					/*
					 * taskEventsDao.saveOrUpdateTasks(tasks);
					 * taskOwnersDao.saveOrUpdateOwners(owners);
					 * customAttrDao.addCustomAttributeValue( customAttributeValues);
					 */
					response.setCustomTableAttributeValues(customTableAttributeValueDtos);
					response.setCustomAttributeValues(customAttributeValueDtos);
					response.setOwners(owners);
					response.setTasks(tasks);
					response.setResponseMessage(resp);
				} catch (Exception e) {
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
				}
				break;

			/*
			 * case TaskCreationConstant.APPROVE: case TaskCreationConstant.REJECT: try {
			 * 
			 * taskEventDao.updateStatusInTaskEvents(childDto.getInstanceId(),
			 * childDto.getActionType(), dto.getUserId(), dto.getUserDisplay(),
			 * dto.getIsAdmin());
			 * 
			 * response.setResponseMessage(resp); } catch (Exception e) {
			 * System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " +
			 * e.getMessage()); } break;
			 */

			case TaskCreationConstant.REJECT:
				try {

					List<String> dtoList = substitutionService.getSubstitutedUserForTask(dto.getUserId(),
							childDto.getInstanceId());
					if (!ServicesUtil.isEmpty(dtoList) || dtoList.size() > 0) {

						List<TaskOwnerDto> taskOwnerDto = setTaskOwner(dto.getUserId().split(","));
						taskOwnerDao.saveInTaskOwner(childDto.getInstanceId(), taskOwnerDto, true);
						taskEventDao.updateStatusChatbotInTaskEvents(childDto.getInstanceId(),
								TaskCreationConstant.READY, "", "", childDto.getIsAdmin());// releasing
						// task
					}

					if (!dto.getIsChatbot()) {
						taskEventDao.updateStatusInTaskEvents(childDto.getInstanceId(), childDto.getActionType(),
								dto.getUserId(), dto.getUserDisplay(), childDto.getIsAdmin(), token);
					} else {
						taskEventDao.updateStatusChatbotInTaskEvents(childDto.getInstanceId(), childDto.getActionType(),
								dto.getUserId(), dto.getUserDisplay(), childDto.getIsAdmin());
					}
					processEventDao.updateStatusInProcessEvents(childDto.getInstanceId(), "COMPLETED");
					resp.setMessage(PMCConstant.REJECTED_SUCCESS);
					resp.setStatus(TaskCreationConstant.SUCCESS);
					resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);

					response.setResponseMessage(resp);
				} catch (Exception e) {
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
				}
				break;

			case TaskCreationConstant.RESOLVED:
			case "Resolve":
				try {

					List<String> dtoList = substitutionService.getSubstitutedUserForTask(dto.getUserId(),
							childDto.getInstanceId());
					if (!ServicesUtil.isEmpty(dtoList) || dtoList.size() > 0) {

						List<TaskOwnerDto> taskOwnerDto = setTaskOwner(dto.getUserId().split(","));
						taskOwnerDao.saveInTaskOwner(childDto.getInstanceId(), taskOwnerDto, true);
						taskEventDao.updateStatusChatbotInTaskEvents(childDto.getInstanceId(),
								TaskCreationConstant.READY, "", "", childDto.getIsAdmin());// releasing
						// task
					}

					taskEventDao.updateStatusInTaskEvents(childDto.getInstanceId(), TaskCreationConstant.RESOLVED,
							dto.getUserId(), dto.getUserDisplay(), childDto.getIsAdmin(), token);
					resp.setMessage("Resolved Successfully");
					resp.setStatus(TaskCreationConstant.SUCCESS);
					resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);

					response.setResponseMessage(resp);
				} catch (Exception e) {
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
				}
				break;

			case TaskCreationConstant.COMPLETE:
			case TaskCreationConstant.COMPLETED:
			case TaskCreationConstant.APPROVE:
			case TaskCreationConstant.DONE:
				try {
					List<String> dtoList = substitutionService.getSubstitutedUserForTask(dto.getUserId(),
							childDto.getInstanceId());
					if (!ServicesUtil.isEmpty(dtoList) || dtoList.size() > 0) {

						List<TaskOwnerDto> taskOwnerDto = setTaskOwner(dto.getUserId().split(","));
						taskOwnerDao.saveInTaskOwner(childDto.getInstanceId(), taskOwnerDto, true);
						taskEventDao.updateStatusChatbotInTaskEvents(childDto.getInstanceId(),
								TaskCreationConstant.READY, "", "", childDto.getIsAdmin());// releasing
						// task
					}
					substitutionService.getIsSubstituted(childDto.getInstanceId(), dto.getUserId());

					if (childDto.getActionType().equals(TaskCreationConstant.COMPLETE))
						childDto.setActionType(TaskCreationConstant.COMPLETED);

					// checkConditionToApprove(childDto.getInstanceId());
					int count = 0;
					if (!dto.getIsChatbot()) {
						count = taskEventDao.updateStatusInTaskEvents(childDto.getInstanceId(),
								childDto.getActionType(), dto.getUserId(), dto.getUserDisplay(), childDto.getIsAdmin(),
								token);
					} else {
						count = taskEventDao.updateStatusChatbotInTaskEvents(childDto.getInstanceId(),
								childDto.getActionType(), dto.getUserId(), dto.getUserDisplay(), childDto.getIsAdmin());
					}

					if (count < 1) {
						resp.setMessage("Task is Already Approved");
						resp.setStatus(TaskCreationConstant.SUCCESS);
						resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
						response.setResponseMessage(resp);
						System.err.println("out");
						return response;
					}
					System.err.println("[WBP-Dev] Inside Done Action" + System.currentTimeMillis());

					String res = createNextTaskNew(childDto.getInstanceId());

					System.err.println("ActionFacade.actionOnTask() new task creation status" + res);

					// if (TaskCreationConstant.FAILURE.equals(res))
					// processEventDao.updateStatusInProcessEvents(childDto.getInstanceId(),
					// "COMPLETED");

					if (TaskCreationConstant.FAILURE.equals(res)) {

						processEventDao.updateStatusInProcessEvents(childDto.getInstanceId(), "COMPLETED");
						String processName = "";
						processName = processEventDao.getProcessName(childDto.getInstanceId());
						String requestId = taskEventDao.getRequestId(childDto.getInstanceId());

						if (!ServicesUtil.isEmpty(processName)
								&& "ProjectProposalDocumentApproval".equals(processName)) {

							// For PPD Demo, duduction of the approved budget
							String costCenter = customAttributeValueDao
									.getCostCenterFromCustomAttributes(childDto.getInstanceId());
							String getApprovedBudget = customAttributeValueDao
									.getBudgetFromCustomAttributes(childDto.getInstanceId());
							String requestor = processEventDao.getStartedBy(childDto.getInstanceId());
							// ppdCostCenterValueMap =
							// crossConstantDao.getPPDAvaibaleBudget();
							customAttributeValueDao.saveAnCustomAttribute(new CustomAttributeValue(
									childDto.getInstanceId(), "238394c8j2hg2", "gia69b268cfe", "Open"));
							List<Object[]> cusAttr = customAttributeValueDao
									.getAttributesForInstance(childDto.getInstanceId());

							if (costCenter.contains(",")) {
								List<String> list = new ArrayList<String>(Arrays.asList(costCenter.split(",")));
								int size = list.size();
								for (String str : list) {
									String updateResult = crossConstantDao.updateCostCenter(str,
											"" + (Integer.parseInt(getApprovedBudget) / size), requestId);
									if ("FAILURE".equals(updateResult))
										crossConstantDao.insertCostCenter(str,
												"" + (Integer.parseInt(getApprovedBudget) / size), requestId);

									System.err.println(
											"[costCenter]" + costCenter + "[updatedBudget]" + getApprovedBudget);

									// ECC update
									// List<Object[]> cusAttr =
									// customAttributeValueDao.getAttributesForInstance(childDto.getInstanceId());
									// PpdDto ppdDto = new PpdDto(str, requestId, budget, availBudget,
									// requestor, deadline, childDto.getComment(), "", prjctClosureDate);
									Integer budget = (Integer.parseInt(getApprovedBudget) / size);
									PpdDto ppdDto = createUpdateDto(str, requestId, childDto.getComment(), cusAttr,
											requestor, budget.toString(), dto.getUserDisplay());
									Gson gson = new Gson();
									System.err.println("ECC Custom Attribute Dto" + gson.toJson(ppdDto));
									String url = getProperty.getValue("CC_VIRTUAL_HOST")
											+ getProperty.getValue("PPD_VALUE_UPDATE");
									eccActionFacade.updateCustomAttributes(gson.toJson(ppdDto), url);
								}
							} else {
								String updateResult = crossConstantDao.updateCostCenter(costCenter, getApprovedBudget,
										requestId);
								if ("FAILURE".equals(updateResult))
									crossConstantDao.insertCostCenter(costCenter, getApprovedBudget, requestId);

								System.err.println("[costCenter]" + costCenter + "[updatedBudget]" + getApprovedBudget);

								// ecc
								// List<Object[]> cusAttr =
								// customAttributeValueDao.getAttributesForInstance(childDto.getInstanceId());
								// PpdDto ppdDto = new PpdDto(str, requestId, budget, availBudget,
								// requestor, deadline, childDto.getComment(), "", prjctClosureDate);
								PpdDto ppdDto = createUpdateDto(costCenter, requestId, childDto.getComment(), cusAttr,
										requestor, getApprovedBudget, dto.getUserDisplay());
								Gson gson = new Gson();
								System.err.println("ECC Custom Attribute Dto" + gson.toJson(ppdDto));
								String url = getProperty.getValue("CC_VIRTUAL_HOST")
										+ getProperty.getValue("PPD_VALUE_UPDATE");
								System.err.println(url);
								eccActionFacade.updateCustomAttributes(gson.toJson(ppdDto), url);

							}
						}
					}

					/*
					 * taskEventsDao.saveOrUpdateTasks(tasks);
					 * taskOwnersDao.saveOrUpdateOwners(owners);
					 * customAttrDao.addCustomAttributeValue( customAttributeValues);
					 */

					substitutionDao.updateApprovalRequest(childDto.getInstanceId(), 1);

					resp.setMessage(PMCConstant.APPROVED_SUCCESS);
					resp.setStatus(TaskCreationConstant.SUCCESS);
					resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
					response.setCustomTableAttributeValues(customTableAttributeValueDtos);
					response.setCustomAttributeValues(customAttributeValueDtos);
					response.setOwners(owners);
					response.setTasks(tasks);
					response.setResponseMessage(resp);

				} catch (Exception e) {
					System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
				}
				break;

			default:
				resp.setMessage(TaskCreationConstant.ACTION_NOT_SUPPORTED);
				resp.setStatus(TaskCreationConstant.FAILURE);
				resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
				break;
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][TASKS ACTION]ERROR : " + e.getMessage());
			e.printStackTrace();
		}
		// }
		// taskEventsDao.saveOrUpdateTasks(tasks);
		// taskOwnersDao.saveOrUpdateOwners(owners);
		// customAttrDao.addCustomAttributeValue(customAttributeValues);
		return response;
	}

	public Boolean checkConditionToApprove(String instanceId) {
		String processName = "";
		processName = processEventDao.getProcessName(instanceId);

		if ("ProjectProposalDocumentApproval".equalsIgnoreCase(processName)) {
			String costCenter = customAttributeValueDao.getCostCenterFromCustomAttributes(instanceId);
			String getApprovedBudget = customAttributeValueDao.getBudgetFromCustomAttributes(instanceId);

			Map<String, String> availBudget = crossConstantDao.getPPDAvaibaleBudget();
			String[] costCenters = costCenter.split(",");
			int size = costCenters.length;
			for (String str : costCenters) {

				if ((Integer.parseInt(getApprovedBudget) / size) <= Integer.parseInt(availBudget.get(str))) {
					return true;
				} else
					return false;
			}
		}
		return true;
	}

	private PpdDto createUpdateDto(String costCenter, String requestId, String comments, List<Object[]> cusAttr,
			String requestor, String budget, String approver) {
		PpdDto ppdDto = new PpdDto();

		ppdDto.setComments(comments);
		ppdDto.setCostCenter(costCenter);
		ppdDto.setPpdId(requestId);
		ppdDto.setRequestor(requestor);
		ppdDto.setApprover(approver);
		Map<String, String> budgetValues = crossConstantDao.getPPDAvaibaleBudget();
		for (Object[] obj : cusAttr) {
			if (obj[0].toString().equalsIgnoreCase("ebgbhj9c760dc"))
				ppdDto.setBudget(budget);
			if (costCenter.equals("4GYUKI003")) {
				if (obj[0].toString().equalsIgnoreCase("2693jcccd5da7"))
					ppdDto.setAvailBudget(budgetValues.get("4GYUKI003"));
			} else if (costCenter.equals("3DE1TY0011")) {
				if (obj[0].toString().equalsIgnoreCase("h3117e8462i1"))
					ppdDto.setAvailBudget(budgetValues.get("3DE1TY0011"));
			} else if (costCenter.equals("4YULJ59001")) {
				if (obj[0].toString().equalsIgnoreCase("dbaif84h6i2f"))
					ppdDto.setAvailBudget(budgetValues.get("4YULJ59001"));
			}
			if (obj[0].toString().equalsIgnoreCase("gcc94fbe944i")) {
				String attachment = obj[1].toString();
				if (!ServicesUtil.isEmpty(attachment)) {
					JSONArray jsonArray = new JSONArray(attachment);
					if (((JSONObject) jsonArray.get(0)).has("attachmentName"))
						ppdDto.setAttachments(((JSONObject) jsonArray.get(0)).optString("attachmentName"));
					else
						ppdDto.setAttachments("");
				} else
					ppdDto.setAttachments("");
			}
			if (obj[0].toString().equalsIgnoreCase("af2j78id1135")) {
				String inDate = obj[1].toString();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Timestamp ts = new Timestamp(((java.util.Date) df.parse(inDate)).getTime());
					Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(inDate);
					ppdDto.setPrjctClosureDate(inDate + "T00:00:00");
					ppdDto.setDeadline(inDate + "T00:00:00");
				} catch (Exception e) {
					System.err.println("[WBP-Dev]Time error" + e);

				}

			}
		}

		return ppdDto;
	}

//	public static void main(String args[]){
//		String s= "[{\"attachmentSize\":\"47066\",\"attachmentType\":\"application/png\",\"attachmentName\":\"Process flow 1\",\"attachmentId\":\"xaf_a7-SGCtfyipUWhY-jsREMgI4vvJIw0Tq7XebTHY\"}]";
//		JSONArray jsonArray = new JSONArray(s);
//		String att = ((JSONObject)jsonArray.get(0)).optString("attachmentName");
//		System.err.println(att);
//	}

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

	private void createSubTask(String eventId, List<String> ownerList, String userId, String taskName) {

		try {
			TaskEventsDto taskEventsDto = taskEventDao.getPreviousTaskDetail(eventId);// change
			// owner
			// of
			// task
			taskEventsDto.setCreatedBy(userId);
			TaskValueDo taskValueDo = taskValueDao.saveInTaskvalue(ownerList, taskName, taskEventsDto.getProcessId());

			String newEventId = generateEventId();
			tasks.add(taskEventDao.setTaskEvents(newEventId, taskEventsDto, taskValueDo.getTaskName(),
					taskValueDo.getTaskType()));
			owners.addAll(taskOwnerDao.setTaskOwners(newEventId, taskValueDo.getOwnerId()));
			customAttributeValueDtos.addAll(customAttributeValueDao.setCustomAttributes(newEventId, eventId));

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][NEXT TASK CREATION]ERROR:" + e.getMessage());
		}

	}

	private List<TaskOwnerDto> setTaskOwner(String[] sendToUser) {

		List<TaskOwnerDto> userDetails = userIDPMappingDao.getOwnerDetailById(sendToUser);

		return userDetails;
	}

	@SuppressWarnings("unused")
	private String createNextTask(String eventId) {
		String res = TaskCreationConstant.SUCCESS;
		TaskAuditDto auditDto = null;
		System.err.println("[WBP-Dev] Next task creation" + System.currentTimeMillis());
		try {
			TaskEventsDto taskEventsDto = taskEventDao.getPreviousTaskDetail(eventId);
			TaskValueDo taskValueDo = taskValueDao.fetchTaskDetail(taskEventsDto.getProcessId(),
					taskEventsDto.getCreatedBy());
			if (taskValueDo == null)
				return TaskCreationConstant.FAILURE;

			res = TaskCreationConstant.SUCCESS;
			String newEventId = generateEventId();
			tasks.add(taskEventDao.setTaskEvents(newEventId, taskEventsDto, taskValueDo.getTaskName(),
					taskValueDo.getTaskType()));
			owners.addAll(taskOwnerDao.setTaskOwners(newEventId, taskValueDo.getOwnerId()));
			customAttributeValueDtos.addAll(customAttributeValueDao.setCustomAttributes(newEventId, eventId));

			// deleting the record
			taskValueDao.deleteCurDetail(taskValueDo.getProcessId(), taskValueDo.getStepNumber());

			System.err.println(
					"[WBP-Dev] task Details" + tasks + owners + customAttributeValueDtos + System.currentTimeMillis());
			auditDto = new TaskAuditDto();
			auditDto.setAuditId(UUID.randomUUID().toString().replaceAll("-", ""));
			auditDto.setAction("New");
			auditDto.setEventId(newEventId);
			auditDto.setUserId(taskEventsDto.getCreatedBy());
			auditDto.setUserName(userIDPMappingDao.getUserName(taskEventsDto.getCreatedBy()));
			auditDto.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
			taskAuditDao.saveOrUpdateAudit(auditDto);

			// for (TaskOwnersDto taskOwnersDto : owners) {
			// if(!ServicesUtil.isEmpty(taskOwnersDto.getGroupOwner())){
			// if(groupList.contains(taskOwnersDto.getGroupOwner()))
			// continue;
			// else{
			// groupList.add(taskOwnersDto.getGroupOwner());
			// if("".equals(ownerNames) || ServicesUtil.isEmpty(ownerNames))
			// ownerNames.append(taskOwnersDto.getGroupOwner());
			// else{
			// ownerNames.append(", ");
			// ownerNames.append(taskOwnersDto.getGroupOwner());
			// }
			// }
			// }else{
			// if("".equals(ownerNames) || ServicesUtil.isEmpty(ownerNames))
			// ownerNames.append(taskOwnersDto.getTaskOwnerDisplayName());
			// else{
			// ownerNames.append(", ");
			// ownerNames.append(taskOwnersDto.getTaskOwnerDisplayName());
			// }
			// }
			// }

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][NEXT TASK CREATION]ERROR:" + e.getMessage());
		}
		return res;
	}

	private String createNextTaskNew(String eventId) {
		String res = TaskCreationConstant.SUCCESS;
		TaskAuditDto auditDto = null;
		System.err.println("[WBP-Dev] Next task creation" + System.currentTimeMillis());
		Map<String, Integer> userMapAFE = new HashMap<>();
		List<String> userIds = new ArrayList<>();
		try {

			TaskEventsDto taskEventsDto = taskEventDao.getPreviousTaskDetail(eventId);

			if ("AFENexus".equalsIgnoreCase(taskEventsDto.getProcessName())) {

				userMapAFE = userIDPMappingDao.getUsersWithBudget();
				String filterType = afeNexusOrderDao.getFilterForNexus();
				userIds = userIDPMappingDao.getUsersWithOrder(filterType);
				int minIndex = userIds.size() - 1;

				// check if any not started task exists for process id
				List<TaskEventsDto> taskEvents = taskEventDao.getAllNotStartedTasks(taskEventsDto.getProcessId());

				if (!ServicesUtil.isEmpty(taskEvents)) {
					String taskId = taskEvents.get(0).getEventId();
					int index = userIds.indexOf(taskEvents.get(0).getCurrentProcessor());
					for (TaskEventsDto task : taskEvents) {
						index = userIds.indexOf(task.getCurrentProcessor());
						if (index != -1 && index < minIndex) {
							minIndex = index;
							taskId = task.getEventId();
						}
					}

					// change status of existing task
					taskEventDao.updateStatus(taskId);
					return TaskCreationConstant.SUCCESS;
				}
			}

			String taskType = taskEventsDto.getOwnerSeqType();
			System.err.println("[WBP-Dev][WORKBOX][NEXT TASK CREATION] taskType : " + taskType);
			if (!ServicesUtil.isEmpty(taskType)) {
				List<TaskEventsDto> taskEvents = null;
				switch (taskType) {
				case TaskCreationConstant.SEQUENTIAL_TASK:
					taskEvents = taskEventDao.getAllPendingTask(taskEventsDto.getProcessId(), "NOT STARTED");
					String userList = "";
					if (!ServicesUtil.isEmpty(taskEvents)) {
						for (TaskEventsDto dto : taskEvents) {
							userList += dto.getOwnerId() + "','";
						}
						userIds = userIDPMappingDao.getUsersWithOrderByName(eventId,
								userList.substring(0, userList.length() - 2));

						// after filtering first user will be current task owner
						if (!ServicesUtil.isEmpty(userIds)) {
							String userId = userIds.get(0);
							String taskId = taskEvents.get(0).getEventId();
							for (TaskEventsDto task : taskEvents) {
								if (task.getOwnerId().equals(userId)) {
									taskId = task.getEventId();
								}
							}
							// change status of existing task
							taskEventDao.updateStatusToReady(taskId);
							return TaskCreationConstant.SUCCESS;
						}
					}
					break;

				case TaskCreationConstant.PARALLEL_TASK:
					taskEvents = taskEventDao.getAllPendingTask(taskEventsDto.getProcessId(), "READY,RESERVED");
					if (taskEvents.size() > 0) {
						return TaskCreationConstant.SUCCESS;
					}

				default:
					break;
				}
			}

			// check if the task type is service
			String targetId = processTemplateValueDao.getTargetId(taskEventsDto.getProcessId(),
					taskEventsDto.getName());

			ProcessTemplate template = processTemplateDao.getProcessTemplateById(targetId,
					taskEventsDto.getProcessName());

			if (!ServicesUtil.isEmpty(template)) {
				if ("Rule Based".equalsIgnoreCase(template.getTaskNature())) {
					String targetIdsFromRules = businessRuleCalculationForRMG(template.getTemplateId(), eventId,
							taskEventsDto.getProcessId());
					if (targetIdsFromRules != null && !"".equalsIgnoreCase(targetIdsFromRules)) {
						targetId = targetIdsFromRules;
					}
				}
			}

			res = taskEventDao.validateNextTask(taskEventsDto);

			if (TaskCreationConstant.SUCCESS.equals(res))
				res = TaskCreationConstant.SUCCESS;

			if ((targetId == null && res.equals(TaskCreationConstant.SUCCESS))
					|| res.equals(TaskCreationConstant.SUCCESS))
				return res;

			List<ProcessTemplateValueDto> processTemplateValueDtos = processTemplateValueDao
					.fetchTaskDetailRMG(taskEventsDto, targetId);
			if (processTemplateValueDtos == null || processTemplateValueDtos.isEmpty())
				return TaskCreationConstant.FAILURE;

			res = TaskCreationConstant.SUCCESS;

			for (ProcessTemplateValueDto processTemplateValueDto : processTemplateValueDtos) {
				if ("AFENexus".equalsIgnoreCase(taskEventsDto.getProcessName())
						&& processTemplateValueDto.getRunTimeUser()) {
					return setTaskForAFENexus(processTemplateValueDto, taskEventsDto, userMapAFE, userIds);

				}
				//List<ProcessTemplateValueDto> dtos = new ArrayList<>();
				//List<ProcessTemplateValueDto> dtos1 = new ArrayList<>();
				//dtos1.add(processTemplateValueDto);
				String ownerTypeSeq = processTemplateValueDto.getOwnerSeqType();
				List<CustomAttributeValueTableAdhocDo> attributeValueTables = new ArrayList<>();

				// setting task for sequential and parallel flow
				if (ownerTypeSeq.equals(TaskCreationConstant.SEQUENTIAL_TASK)) {
					res = setTaskForAdHoc(processTemplateValueDto, taskEventsDto, TaskCreationConstant.SEQUENTIAL_TASK ,attributeValueTables);
					return res;
				} else if (ownerTypeSeq.equals(TaskCreationConstant.PARALLEL_TASK)) {
					res = setTaskForAdHoc(processTemplateValueDto, taskEventsDto, TaskCreationConstant.PARALLEL_TASK , attributeValueTables);
					return res;
				} else {
					CustomAttributeValues customAttributeValues = customAttributeValueDao
							.setCustomAttributesNew(processTemplateValueDto , attributeValueTables);

					TaskEventsDto task = taskEventDao.setTaskEvents(processTemplateValueDto,
							customAttributeValues.getAttrKeyValues(), taskEventsDto);
					processTemplateValueDto.setEventId(task.getEventId());
					//dtos.add(processTemplateValueDto);
					tasks.add(task);
					owners.addAll(taskOwnerDao.setTaskOwners(processTemplateValueDto.getEventId(),
							processTemplateValueDto.getOwnerId()));
					
					if (!ServicesUtil.isEmpty(customAttributeValues)
							&& !ServicesUtil.isEmpty(customAttributeValues.getCustomAttributeValues()))
						customAttributeValueDtos.addAll(customAttributeValues.getCustomAttributeValues());
					if (!ServicesUtil.isEmpty(attributeValueTables))
						customTableAttributeValueDtos.addAll(attributeValueTables);

					auditDto = new TaskAuditDto();
					auditDto.setAuditId(UUID.randomUUID().toString().replaceAll("-", ""));
					auditDto.setAction("New");
					auditDto.setEventId(processTemplateValueDto.getEventId());
					auditDto.setUserId(taskEventsDto.getCreatedBy());
					auditDto.setUserName(userIDPMappingDao.getUserName(taskEventsDto.getCreatedBy()));
					auditDto.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
					taskAuditDao.saveOrUpdateAudit(auditDto);
				}
			}

			// deleting the record
			// taskValueDao.deleteCurDetail(taskValueDo.getProcessId(),taskValueDo.getStepNumber());

		} catch (Exception e) {
			res = TaskCreationConstant.FAILURE;
			System.err.println("[WBP-Dev][WORKBOX][NEXT TASK CREATION]ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return res;
	}

	private String setTaskForAdHoc(ProcessTemplateValueDto processTemplateValueDto, TaskEventsDto taskEventsDto,
			String sequenceType , List<CustomAttributeValueTableAdhocDo> attributeValueTables) {
		List<ProcessTemplateValueDto> dtos = new ArrayList<>();
		List<ProcessTemplateValueDto> dtos1 = new ArrayList<>();
		//List<String> taskIds = new ArrayList<>();

		List<TaskOwnersDto> taskOwners = taskOwnerDao.setTaskOwners(processTemplateValueDto.getEventId(),
				processTemplateValueDto.getOwnerId());

		// Integer amount =
		// customAttrDao.getAmount(taskEventsDto.getProcessId());
		String status = sequenceType.equals(TaskCreationConstant.SEQUENTIAL_TASK) ? "NOT STARTED" : "READY";
		Date currentTime = ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC());
		String userIdString = "'";
		for (TaskOwnersDto taskOwnersDto : taskOwners) {

			String taskId = UUID.randomUUID().toString().replace("-", "");
			taskOwnersDto.setEventId(taskId);
			//taskIds.add(taskId);
			processTemplateValueDto.setEventId(taskId);
//			dtos1.add(processTemplateValueDto);
			CustomAttributeValues customAttributeValues = customAttributeValueDao
					.setCustomAttributesNew(processTemplateValueDto ,attributeValueTables);

			TaskEventsDto task = taskEventDao.setTaskEvents(processTemplateValueDto,
					customAttributeValues.getAttrKeyValues(), taskEventsDto, status, taskOwnersDto.getTaskOwner(),
					taskOwnersDto.getTaskOwnerDisplayName(), currentTime);
			processTemplateValueDto.setEventId(task.getEventId());
			dtos.add(processTemplateValueDto);
			tasks.add(task);
			owners.add(taskOwnersDto);
			userIdString += taskOwnersDto.getTaskOwner() + "','";
			if (!ServicesUtil.isEmpty(customAttributeValues)
					&& !ServicesUtil.isEmpty(customAttributeValues.getCustomAttributeValues()))
				customAttributeValueDtos.addAll(customAttributeValues.getCustomAttributeValues());
			if (!ServicesUtil.isEmpty(attributeValueTables))
                customTableAttributeValueDtos.addAll(attributeValueTables);
		}

		System.err.println("userIds" + userIdString);
		if (sequenceType.equals(TaskCreationConstant.SEQUENTIAL_TASK)) {
			// userIds need to be filtered
			List<String> userIdsList = userIDPMappingDao.getUsersWithOrderByNameWithType(
					processTemplateValueDto.getOrderBy(), userIdString.substring(0, userIdString.length() - 2));

			// after filtering first user will be current task owner
			if (!ServicesUtil.isEmpty(userIdsList)) {
				String userId = userIdsList.get(0);
				for (TaskEventsDto task : tasks) {
					if (task.getOwnerId().equals(userId)) {
						task.setStatus("READY");
					}
				}
			}
		}

		TaskAuditDto auditDto = new TaskAuditDto();
		auditDto.setAuditId(UUID.randomUUID().toString().replaceAll("-", ""));
		auditDto.setAction("New");
		auditDto.setEventId(processTemplateValueDto.getEventId());
		auditDto.setUserId(taskEventsDto.getCreatedBy());
		auditDto.setUserName(userIDPMappingDao.getUserName(taskEventsDto.getCreatedBy()));
		auditDto.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
		try {
			taskAuditDao.saveOrUpdateAudit(auditDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TaskCreationConstant.SUCCESS;
	}

	private String generateEventId() {
		String eventId = UUID.randomUUID().toString().replaceAll("-", "");
		return eventId;
	}

	private String businessRuleCalculationForRMG(String ruleTypeId, String eventId, String processId) {
		System.err.println("ActionFacade.businessRuleCalculationForRMG() starting business rules ");
		String targetIds = "";
		List<RuleDto> rules = ruleDao.getRulesById(ruleTypeId);
		System.err.println("ActionFacade.businessRuleCalculationForRMG()  business rules " + rules);

		try {
			for (RuleDto ruleDto : rules) {

				// billing <50, billing >150 - RM head
				// project StartDate>curDate - manager
				// project type= product - CEO
				if (customAttrDao.getCustomAttributesWithCondition(eventId, processId, ruleDto.getCondition(),
						ruleDto.getCustom_key())) {
					targetIds += ruleDto.getDestination() + ",";
				}

			}
			if (!targetIds.equalsIgnoreCase("")) {
				targetIds = targetIds.substring(0, targetIds.length() - 1);
			}
			System.err.println("ActionFacade.businessRuleCalculationForRMG()  business rules targetIds" + targetIds);

		} catch (Exception e) {
			System.err.println("ActionFacade.businessRuleCalculationForRMG() exception e" + e.getMessage());
		}
		// Fetch Rules of this task
		// fetch custom attribute values for this task
		// iterate through rules and verify them
		// If verified , set destination as corresponding task

		// 1. If PM requests for resource allocation changes, it has be 15days
		// prior from created date. Exception cases will trigger workflow to RMG
		// Head else RMG Admin.

		// 2. Resource allocation changes from Long Term period (>6 months) to
		// Short Term less than a month should trigger an approval workflow to
		// RMG head
		// 3. Resource allocation to project � without billing (only
		// utilisation), requires RMG head approval. PM to request for the same.
		// 4. Resource allocation to project � with <50% billing, requires RMG
		// head approval. PM to request for the same.
		// 5. Resource allocation to multiple projects � with >150% billing,
		// requires RMG head approval.
		// 6. Soft lock allocation more than 15 days � need RMG head approval.
		// 7. Resource allocations with deviations in resource pyramid structure
		// need RMG Head approval. If total deviation is more than 20%
		//
		// 1. If a resource requested by PM is back dated(if startDate < current
		// date ), resource allocation approvals to be triggered to RMG manager
		// for approvals
		//
		//
		// 1.If project type is 'product' requires CEO approval
		return targetIds;
	}

	private String setTaskForAFENexus(ProcessTemplateValueDto processTemplateValueDto, TaskEventsDto taskEventsDto,
			Map<String, Integer> userMapAFE, List<String> userIds) {
		List<ProcessTemplateValueDto> dtos = new ArrayList<>();
		List<ProcessTemplateValueDto> dtos1 = new ArrayList<>();

		List<TaskOwnersDto> taskOwners = taskOwnerDao.setTaskOwners(processTemplateValueDto.getEventId(),
				processTemplateValueDto.getOwnerId());

		Integer amount = customAttrDao.getAmount(taskEventsDto.getProcessId());
		Date currentTime = ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC());
		for (TaskOwnersDto taskOwnersDto : taskOwners) {
			if (userMapAFE.get(taskOwnersDto.getTaskOwner()) < amount) {
				continue;
			}
			String taskId = UUID.randomUUID().toString().replace("-", "");
			taskOwnersDto.setEventId(taskId);
			processTemplateValueDto.setEventId(taskId);
			// dtos1.add(processTemplateValueDto);
			CustomAttributeValues customAttributeValues = customAttributeValueDao
					.setCustomAttributesNew(processTemplateValueDto ,new ArrayList<>());

			TaskEventsDto task = taskEventDao.setTaskEvents(processTemplateValueDto,
					customAttributeValues.getAttrKeyValues(), taskEventsDto, "NOT STARTED",
					taskOwnersDto.getTaskOwner(), taskOwnersDto.getTaskOwnerDisplayName(), currentTime);
			processTemplateValueDto.setEventId(task.getEventId());
			dtos.add(processTemplateValueDto);
			tasks.add(task);
			owners.add(taskOwnersDto);
			if (!ServicesUtil.isEmpty(customAttributeValues)
					&& !ServicesUtil.isEmpty(customAttributeValues.getCustomAttributeValues()))
				customAttributeValueDtos.addAll(customAttributeValues.getCustomAttributeValues());
		}

		System.err.println("userIds" + userIds);
		String taskId = tasks.get(0).getEventId();
		int minIndex = userIds.size() - 1;
		int index = userIds.indexOf(tasks.get(0).getCurrentProcessor());

		System.err.println("minIndex" + minIndex);
		System.err.println("index" + index);
		for (TaskEventsDto task : tasks) {
			index = userIds.indexOf(task.getCurrentProcessor());
			if (index != -1 && index < minIndex) {
				minIndex = index;
				taskId = task.getEventId();
			}

		}
		System.err.println("minIndex" + minIndex);
		System.err.println("taskId" + taskId);

		for (TaskEventsDto task : tasks) {
			if (task.getEventId().equals(taskId)) {
				task.setStatus("RESERVED");
			}
		}

		TaskAuditDto auditDto = new TaskAuditDto();
		auditDto.setAuditId(UUID.randomUUID().toString().replaceAll("-", ""));
		auditDto.setAction("New");
		auditDto.setEventId(processTemplateValueDto.getEventId());
		auditDto.setUserId(taskEventsDto.getCreatedBy());
		auditDto.setUserName(userIDPMappingDao.getUserName(taskEventsDto.getCreatedBy()));
		auditDto.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
		try {
			taskAuditDao.saveOrUpdateAudit(auditDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return TaskCreationConstant.SUCCESS;
	}
}
