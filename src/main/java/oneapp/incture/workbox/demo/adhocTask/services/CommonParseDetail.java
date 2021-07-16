package oneapp.incture.workbox.demo.adhocTask.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import net.objecthunter.exp4j.ExpressionBuilder;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessTemplateDao;
import oneapp.incture.workbox.demo.adapter_base.dao.StatusConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.CrossConstantDao;
import oneapp.incture.workbox.demo.adhocTask.dao.CustomAttributeTemplateDao;
import oneapp.incture.workbox.demo.adhocTask.dao.CustomAttributeValuesTableAdhocDao;
import oneapp.incture.workbox.demo.adhocTask.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adhocTask.dao.ProcessEventDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskEventDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskTemplateDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskTemplateOwnerDao;
import oneapp.incture.workbox.demo.adhocTask.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.adhocTask.dao.UserRoleDao;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeValuesTableAdhocDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ProcessAttributesDto;
import oneapp.incture.workbox.demo.adhocTask.dto.RequestIdDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TableContentDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskCreationDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskDetailDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ValueListDto;
import oneapp.incture.workbox.demo.adhocTask.util.AttributeDetials;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.adhocTask.util.TimeZoneConvertion;
import oneapp.incture.workbox.demo.adhocTask.util.WorkflowPayloadCreation;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.document.service.DocumentService;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;
import oneapp.incture.workbox.demo.workflow.dao.OwnerSelectionRuleDao;
import oneapp.incture.workbox.demo.workflow.dao.RuleDao;
import oneapp.incture.workbox.demo.workflow.dto.OwnerSelectionRuleDto;
import oneapp.incture.workbox.demo.workflow.dto.RuleDto;
import oneapp.incture.workbox.demo.workflow.dto.TaskTemplateOwnerDto;

@Component
public class CommonParseDetail {

	@Autowired
	UserIDPMappingDao userIDPMappingDao;

	@Autowired
	ProcessEventDao processEventDao;

	@Autowired
	ProcessConfigDao processConfigDao;

	@Autowired
	TaskTemplateDao taskTemplateDao;

	@Autowired
	TaskTemplateOwnerDao taskTemplateOwnerDao;

	@Autowired
	DocumentService documentService;

	@Autowired
	ProcessTemplateDao processTemplateDao;

	@Autowired
	CustomAttributeTemplateDao customAttributeTemplateDao;

	@Autowired
	CrossConstantDao constantDao;

	@Autowired
	WorkflowPayloadCreation workflowPayloadCreation;

	/* Map<String,String> eventOwners; */

	@Autowired
	StatusConfigDao statusConfigDao;

	@Autowired
	UserRoleDao userRoleDao;

	@Autowired
	OwnerSelectionRuleDao ownerSelectionRuleDao;

	@Autowired
	RuleDao ruleDao;

	@Autowired
	private TaskEventDao taskEventDao;

	@Autowired
	AttributeDetials attributeDetials;

	@Autowired
	SharepointUploadFile sharepointUploadFile;

	@Autowired
	CustomAttributeValuesTableAdhocDao customAttributeValuesTableAdhocDao;

	public TaskCreationDto createSubmitResponse(AttributesResponseDto tasksToSubmit) {

		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

		TaskCreationDto taskCreationDto = null;
		List<TaskEventsDto> tasks = null;
		List<ProcessEventsDto> processes = null;
		List<CustomAttributeValue> customAttributeValues = null;
		List<TaskOwnersDto> owners = null;
		List<TaskAuditDto> auditDtos = null;

		// List<String> eventIdList =
		// generateEventIdList(tasksToSubmit.getListOfProcesssAttributes().size());
		List<String> processIdList = generateProcessIdList(tasksToSubmit.getListOfProcesssAttributes().size());
		TaskDetailDto taskDetailDto = null;

		try {
			String ownerName = userIDPMappingDao.getOwnerDetailById(tasksToSubmit.getResourceid().split(" ")).get(0)
					.getTaskOwnername();
			List<CustomAttributeTemplateDto> taskCustomAttrs = customAttributeTemplateDao
					.getTaskCustomAttributes(tasksToSubmit.getProcessName());
			taskCreationDto = new TaskCreationDto();
			// taskCreationDto = setTaskValues(tasksToSubmit,processIdList);
			taskCreationDto = setTaskValuesNew(tasksToSubmit, processIdList, taskCustomAttrs);
			Gson g = new Gson();
			System.err.println(g.toJson(taskCreationDto));
			if (TaskCreationConstant.FAILURE.equals(taskCreationDto.getResponseMessage().getStatus()))
				return taskCreationDto;
			List<String> eventIdList = generateEventIdList(taskCreationDto.getTaskCount());

			// setting processEvents common
			RequestIdDto requestIdDto = processEventDao.getRequestId(tasksToSubmit.getActionType(),
					tasksToSubmit.getProcessName(), tasksToSubmit.getRequestId());

			System.err.println("request id" + requestIdDto);
			processes = setAllProcessEvent(taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(),
					processIdList, ownerName, tasksToSubmit.getActionType(), requestIdDto,
					tasksToSubmit.getProcessName());

			customAttributeValues = new ArrayList<CustomAttributeValue>();
			tasks = new ArrayList<TaskEventsDto>();
			owners = new ArrayList<TaskOwnersDto>();

			customAttributeValues = setAllCustomAttributeValues(
					taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(), eventIdList,
					processIdList, taskCustomAttrs, taskCreationDto.getTemplateDtos(), tasksToSubmit.getProcessName());
			taskDetailDto = setAllTaskEventsAndOwners(tasksToSubmit, ownerName, processIdList, eventIdList, processes,
					tasksToSubmit.getActionType(), taskCreationDto, requestIdDto);

			auditDtos = new ArrayList<TaskAuditDto>();
			auditDtos = setAllAudit(tasks);

			taskCreationDto.setProcesses(processes);
			taskCreationDto.setCustomAttributeValues(customAttributeValues);
			taskCreationDto.setOwners(taskDetailDto.getTaskOwnersDtos());
			taskCreationDto.setTasks(taskDetailDto.getTaskEventsDtos());
			taskCreationDto.setAuditDtos(auditDtos);

			// taskCreationDto.getTaskValues().remove(0);

			resp.setMessage(TaskCreationConstant.SUCCESS);
			resp.setStatus(TaskCreationConstant.SUCCESS);
			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
			

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][PARSE DRAFT REQUEST]ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		taskCreationDto.setResponseMessage(resp);
		return taskCreationDto;
	}

	private TaskCreationDto setTaskValuesNew(AttributesResponseDto tasksToSubmit, List<String> processIdList,
			List<CustomAttributeTemplateDto> taskCustomAttrs) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(TaskCreationConstant.FAILURE);
		responseMessage.setStatus(TaskCreationConstant.FAILURE);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		TaskCreationDto taskCreationDto = null;
		Integer processIndex = 0;
		// List<TaskValueDto> taskValues = null;
		List<TaskTemplateOwnerDto> templateOwnerDtos = null;
		// TaskValueDto taskValueDto = null;
		List<ProcessTemplateValueDto> processTemplateValues = null;
		ProcessTemplateValueDto processTemplateValue = null;
		TaskTemplateOwnerDto taskTemplateOwnerDto = null;
		Map<String, String> attrValue = null;
		Map<String, String> attrType = null;
		Map<String, List<ProcessTemplateValueDto>> templateMap = new LinkedHashMap<>();
		Map<String, ProcessTemplateValueDto> allTemplateMap = null;
		String description = "";
		String subject = "";
		Integer taskCount = 0;
		List<CustomAttributeTemplateDto> allCustomAttr = null;
		Map<String, Map<String, String>> processRelatedAttribute = new HashMap<>();

		List<Object[]> taskOwnerDetailList = taskTemplateDao.getCommonTaskOwnersDetails(tasksToSubmit.getProcessName());
		Map<String, String> users = userIDPMappingDao.getusers();
		Map<String, String> groups = workflowPayloadCreation.getGroupsDetailsNew();
		Map<String, String> roles = userRoleDao.getRoleDetail();
		System.err.println(taskOwnerDetailList);
		processTemplateValues = new ArrayList<ProcessTemplateValueDto>();
		templateOwnerDtos = new ArrayList<TaskTemplateOwnerDto>();
		for (ProcessAttributesDto instanceDetail : tasksToSubmit.getListOfProcesssAttributes()) {
			attrValue = new HashMap<>();
			attrType = new HashMap<>();
			allCustomAttr = new ArrayList<>();

			allCustomAttr.addAll(instanceDetail.getCustomAttributeTemplateDto());
			allCustomAttr.addAll(taskCustomAttrs);
			for (CustomAttributeTemplateDto attribute : allCustomAttr) {
				if (ServicesUtil.isEmpty(attribute.getAttributePath()) || "".equals(attribute.getAttributePath())) {
					if (TaskCreationConstant.DROPDOWN.equals(attribute.getDataType())
							&& attribute.getIsRunTime()) {
						StringBuilder ids = new StringBuilder("");
						for (ValueListDto value : attribute.getValueList()) {
							if (TaskCreationConstant.GROUP.equalsIgnoreCase(value.getType()))
								ids.append(groups.get(value.getId()));
							else if (TaskCreationConstant.ROLE.equalsIgnoreCase(value.getType()))
								ids.append(roles.get(value.getId()));
							else if (TaskCreationConstant.INDIVIDUAL.equalsIgnoreCase(value.getType()))
								ids.append(users.get(value.getId()));
							else
								continue;
							ids.append(",");
						}
						if (!ServicesUtil.isEmpty(ids))
							attrValue.put(attribute.getKey(), ids.substring(0, ids.length() - 1));
						else
							attrValue.put(attribute.getKey(), "");
					} else
						attrValue.put(attribute.getKey(), attribute.getValue());
					attrType.put(attribute.getKey(), attribute.getDataType());
				} else {
					String[] attr = attribute.getAttributePath().split("[$]");
					String value = "";
					String s = "";
					for (String str : attr) {
						if (str.contains("{")) {
							s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
							if (attrValue.containsKey(s))
								str = str.replace("{" + s + "}",
										(ServicesUtil.isEmpty(attrValue.get(s))) ? "" : attrValue.get(s));
						}
						value += str;
					}
					attrValue.put(attribute.getKey(), value);
					attrType.put(attribute.getKey(), attribute.getDataType());
				}
			}
			allTemplateMap = new LinkedHashMap<>();
			for (Object[] taskOwnerDetail : taskOwnerDetailList) {

				processTemplateValue = new ProcessTemplateValueDto();

				List<OwnerSelectionRuleDto> rules = ownerSelectionRuleDao.getRules(tasksToSubmit.getProcessName(),
						ServicesUtil.asString(taskOwnerDetail[1]));
				if (ServicesUtil.isEmpty(rules)) {
					if (ServicesUtil.asBoolean(taskOwnerDetail[3])) {

						for (CustomAttributeTemplateDto attribute : instanceDetail.getCustomAttributeTemplateDto()) {

							if (TaskCreationConstant.DESCRIPTION.equals(attribute.getKey()))
								description = attribute.getValue();
							if (TaskCreationConstant.DROPDOWN.equals(attribute.getDataType())
									&& attribute.getIsRunTime()
									&& attribute.getKey().equals(taskOwnerDetail[5])) {
								if (attribute.getValueList().isEmpty()) {
									taskCreationDto = new TaskCreationDto();
									// taskCreationDto.setResponseMessage(responseMessage);
									// return taskCreationDto;
									processTemplateValue.setOwnerId(null);
									break;
								} else {
									List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = createOwnerId(
											attribute.getValueList());
									System.err.println("[WBP-Dev]in" + taskTemplateOwnerDtos);
									templateOwnerDtos.add(taskTemplateOwnerDto);
									processTemplateValue.setOwnerId(taskTemplateOwnerDtos.get(0).getOwnerId());
									StringBuilder ids = new StringBuilder("");
									for (ValueListDto value : attribute.getValueList()) {
										ids.append(value.getId());
										ids.append(",");
									}
									// attribute.setValue(taskTemplateOwnerDao
									// .getOwnersInString(taskTemplateOwnerDtos.get(0).getOwnerId()));
									attribute.setValue(ids.substring(0, ids.length() - 1));
									break;
								}
							}
						}
					} else {
						processTemplateValue.setOwnerId(
								ServicesUtil.isEmpty(taskOwnerDetail[0]) ? null : taskOwnerDetail[0].toString());
					}
				} else {
					List<ValueListDto> listDtos = new ArrayList<>();
					ValueListDto valueListDto = null;
					List<String> addedApprovers = new ArrayList<String>();
					for (OwnerSelectionRuleDto rule : rules) {
						try {
							ScriptEngineManager mgr = new ScriptEngineManager();
							ScriptEngine engine = mgr.getEngineByName("JavaScript");
							String foo = "";
							if (attrType.get(rule.getCustom_key()).equalsIgnoreCase(TaskCreationConstant.NUMERIC))
								foo = attrValue.get(rule.getCustom_key())
										+ (rule.getLogic().equals("=") ? "==" : rule.getLogic()) + rule.getValue();
							else
								foo = "'" + attrValue.get(rule.getCustom_key()) + "'"
										+ (rule.getLogic().equals("=") ? "==" : rule.getLogic()) + "'" + rule.getValue()
										+ "'";
							System.err.println(engine.eval(foo));
							String result = engine.eval(foo).toString();

							if ("true".equalsIgnoreCase(result)) {
								for (String value : rule.getApprover()) {
									if (!addedApprovers.contains(value)) {
										valueListDto = new ValueListDto();
										valueListDto.setId(value);
										valueListDto.setType("individual");
										listDtos.add(valueListDto);
										addedApprovers.add(value);
									}

								}
							}
						} catch (Exception e) {
							System.err.println("[WBP-Dev]Calculating Expression Failed" + e.getMessage());
						}
					}

					List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = createOwnerId(listDtos);
					System.err.println("[WBP-Dev]in" + taskTemplateOwnerDtos);
					templateOwnerDtos.add(taskTemplateOwnerDto);

					processTemplateValue.setOwnerId(null);
					if (!ServicesUtil.isEmpty(taskTemplateOwnerDtos)) {
						processTemplateValue.setOwnerId(taskTemplateOwnerDtos.get(0).getOwnerId());
					}

				}

				processTemplateValue.setProcessId(processIdList.get(processIndex));
				processTemplateValue
						.setCustomKey(ServicesUtil.isEmpty(taskOwnerDetail[5]) ? null : (String) taskOwnerDetail[5]);
				processTemplateValue.setTaskName((String) taskOwnerDetail[1]);
				processTemplateValue.setTaskType((String) taskOwnerDetail[4]);
				description = ServicesUtil.isEmpty(taskOwnerDetail[6]) ? "" : (String) taskOwnerDetail[6];
				String[] desc = description.split("[$]");
				description = "";
				String s = "";
				for (String str : desc) {
					if (str.contains("{")) {
						s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
						if (attrValue.containsKey(s))
							str = str.replace("{" + s + "}",
									(ServicesUtil.isEmpty(attrValue.get(s))) ? "" : attrValue.get(s));
					}
					description = description + str;
				}
				System.err.println("description" + description);
				processTemplateValue.setDescription(description);
				subject = ServicesUtil.isEmpty(taskOwnerDetail[7]) ? "" : (String) taskOwnerDetail[7];
				desc = subject.split("[$]");
				subject = "";
				for (String str : desc) {
					if (str.contains("{")) {
						s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
						if (attrValue.containsKey(s))
							str = str.replace("{" + s + "}",
									(ServicesUtil.isEmpty(attrValue.get(s))) ? "" : attrValue.get(s));
					}
					System.err.println(str);
					subject += str;
				}
				System.err.println("subject" + subject);
				processTemplateValue.setSubject(subject);
				processTemplateValue.setRunTimeUser(ServicesUtil.asBoolean(taskOwnerDetail[3]));
				processTemplateValue
						.setSourceId(ServicesUtil.isEmpty(taskOwnerDetail[8]) ? null : (String) taskOwnerDetail[8]);
				processTemplateValue
						.setTargetId(ServicesUtil.isEmpty(taskOwnerDetail[9]) ? null : (String) taskOwnerDetail[9]);
				processTemplateValue
						.setTemplateId(ServicesUtil.isEmpty(taskOwnerDetail[2]) ? null : (String) taskOwnerDetail[2]);

				String page = "";
				if ("ResourcePlanningWorkflowForRMG".equalsIgnoreCase(tasksToSubmit.getProcessName())) {
					if ("L1 Assign PM".equalsIgnoreCase(processTemplateValue.getTaskName().trim()))
						page = "?page=pmallocation&";
					else if ("L2 Create RLS Demand".equalsIgnoreCase(processTemplateValue.getTaskName()))
						page = "?page=resourceRequest&";
					else if ("L3 Allocate resources".equalsIgnoreCase(processTemplateValue.getTaskName()))
						page = "?page=resourceAllocation&";
					else if ("L5 CEO Approval".equalsIgnoreCase(processTemplateValue.getTaskName()))
						page = "?page=ceoApproval&";
					else if ("L5 RM Manager approval".equalsIgnoreCase(processTemplateValue.getTaskName()))
						page = "?page=rmManagerApproval&";
					else if ("L5 RM head approval".equalsIgnoreCase(processTemplateValue.getTaskName()))
						page = "?page=rmHeadApproval&";

				}
				if ("AnnualLeavePlannerForRMG".equalsIgnoreCase(tasksToSubmit.getProcessName())) {
					page = "?page=AnnualleavePlanner&";
				}
				if ("AnnualLeavePlannerForRMG".equalsIgnoreCase(tasksToSubmit.getProcessName()))
					processTemplateValue.setUrl(ServicesUtil.isEmpty(taskOwnerDetail[10]) ? null
							: (((String) taskOwnerDetail[10]) + page + "projectCode="
									+ attrValue.get("b24i4b00bc47b")));
				if ("ResourcePlanningWorkflowForRMG".equalsIgnoreCase(tasksToSubmit.getProcessName()))
					processTemplateValue.setUrl(ServicesUtil.isEmpty(taskOwnerDetail[10]) ? null
							: (((String) taskOwnerDetail[10]) + page + "projectCode="
									+ attrValue.get("14j8fi71j1g6h7")));
				else if ((ServicesUtil.isEmpty(processTemplateValue.getUrl())))
					processTemplateValue
							.setUrl(ServicesUtil.isEmpty(taskOwnerDetail[10]) ? null : (String) taskOwnerDetail[10]);

				processTemplateValue
						.setTaskNature(ServicesUtil.isEmpty(taskOwnerDetail[11]) ? null : (String) taskOwnerDetail[11]);
				processTemplateValue
						.setOwnerSeqType(ServicesUtil.isEmpty(taskOwnerDetail[12]) ? "" : (String) taskOwnerDetail[12]);
				processTemplateValue
						.setAttrTypeName(ServicesUtil.isEmpty(taskOwnerDetail[14]) ? "" : (String) taskOwnerDetail[14]);
				processTemplateValue
						.setOrderBy(ServicesUtil.isEmpty(taskOwnerDetail[15]) ? "" : (String) taskOwnerDetail[15]);
				processTemplateValues.add(processTemplateValue);

				if (ServicesUtil.isEmpty(processTemplateValue.getSourceId())) {
					taskCount++;
					if (processTemplateValue.getOwnerSeqType().equals(TaskCreationConstant.SEQUENTIAL_TASK)
							|| processTemplateValue.getOwnerSeqType().equals(TaskCreationConstant.PARALLEL_TASK)) {
						taskCount += taskTemplateOwnerDao.getOwners(processTemplateValue.getOwnerId()).size() - 1;
					}

					if (templateMap.containsKey(processTemplateValue.getProcessId())) {
						templateMap.get(processTemplateValue.getProcessId()).add(processTemplateValue);
					} else {
						List<ProcessTemplateValueDto> dtos = new ArrayList<>();
						dtos.add(processTemplateValue);
						templateMap.put(processTemplateValue.getProcessId(), dtos);
					}
				}

				{
					allTemplateMap.put(processTemplateValue.getTemplateId(), processTemplateValue);
				}

				processRelatedAttribute.put(processTemplateValue.getProcessId(), attrValue);

			}

			// Checking if the task is first task is role based
			for (ProcessTemplateValueDto processTemplate : templateMap.get(processIdList.get(processIndex))) {

				if (!ServicesUtil.isEmpty(processTemplate.getTaskNature())
						&& "Rule Based".equalsIgnoreCase(processTemplate.getTaskNature())) {
					List<RuleDto> rules = ruleDao.getRulesById(processTemplate.getTemplateId());
					List<ProcessTemplateValueDto> targetIdsFromRules = getTargetDetails(allTemplateMap, rules,
							attrValue);
					if (targetIdsFromRules.size() > 1)
						taskCount += targetIdsFromRules.size() - 1;
					if (!ServicesUtil.isEmpty(targetIdsFromRules)) {
						templateMap.put(processIdList.get(processIndex), targetIdsFromRules);
					}
				}
			}

			processIndex++;
		}
		taskCreationDto = new TaskCreationDto();

		taskCreationDto.setTaskValues(null);
		taskCreationDto.setProcessRelatedAttribute(processRelatedAttribute);
		taskCreationDto.setProcessTemplateValues(processTemplateValues);
		taskCreationDto.setTemplateDtos(templateMap);
		taskCreationDto.setTaskCount(taskCount);
		taskCreationDto.setTemplateOwnerDtos(templateOwnerDtos);
		taskCreationDto.setAttributesResponseDto(tasksToSubmit);
		responseMessage.setMessage(TaskCreationConstant.SUCCESS);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
		responseMessage.setStatus(TaskCreationConstant.SUCCESS);
		taskCreationDto.setResponseMessage(responseMessage);
		return taskCreationDto;
	}

	public List<ProcessEventsDto> setAllProcessEvent(List<ProcessAttributesDto> listOfProcesssAttributes,
			List<String> processIdList, String ownerName, String actionType, RequestIdDto requestIdDto,
			String processName) {
		List<ProcessEventsDto> processes = null;
		ProcessEventsDto processEventsDto = null;
		Integer i = 0;
		String requestIdTemp = null;
		String reqIdString = null;
		Integer id = 0;
		String requestId = null;
		Object[] requestDetail = null;

		requestIdTemp = requestIdDto.getRequestId().substring(0, requestIdDto.getRequestId().length() - 4);
		requestId = requestIdDto.getRequestId();
		id = requestIdDto.getId();
		requestDetail = requestIdDto.getRequestDetail();

		processes = new ArrayList<ProcessEventsDto>();
		for (ProcessAttributesDto dto : listOfProcesssAttributes) {

			String subject = null;
			TimeZoneConvertion timeZoneConvertion = null;
			timeZoneConvertion = new TimeZoneConvertion();
			Boolean flag = true;
			processEventsDto = new ProcessEventsDto();
			for (CustomAttributeTemplateDto obj : dto.getCustomAttributeTemplateDto()) {
				if (TaskCreationConstant.DESCRIPTION.equals(obj.getKey())) {
					subject = obj.getValue();
					flag = false;
					dto.getCustomAttributeTemplateDto().remove(obj);
				}
				if (flag == false)
					break;
			}
			try {
				processEventsDto.setProcessId(processIdList.get(i));
				processEventsDto.setName(processName);
				processEventsDto.setRequestId(requestId);
				if (actionType.equals("Submit"))
					processEventsDto.setStatus(TaskCreationConstant.RUNNING);
				else
					processEventsDto.setStatus(TaskCreationConstant.RUNNING);
				processEventsDto.setCompletedAt(null);
				processEventsDto
						.setStartedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
				processEventsDto.setStartedBy(dto.getResourceId());
				processEventsDto.setStartedByDisplayName(ownerName);
				processEventsDto.setSubject(subject);

				reqIdString = requestId;
				reqIdString = reqIdString.substring(reqIdString.length() - 4);

				id = id + 1;

				if (requestDetail != null)
					requestId = requestDetail[1].toString() + String.format("%04d", id);
				else
					requestId = requestIdTemp + String.format("%04d", id);

			} catch (Exception e) {
				System.err
						.println("[WBP-Dev]SampleProcessEventDao.saveProcessEvent() Error while storing object : " + e);

			}
			processes.add(processEventsDto);
			i++;
		}

		return processes;
	}

	private List<CustomAttributeValue> setAllCustomAttributeValues(List<ProcessAttributesDto> listOfProcesssAttributes,
			List<String> eventIdList, List<String> processIdList, List<CustomAttributeTemplateDto> taskCustomAttr,
			Map<String, List<ProcessTemplateValueDto>> templateDtos, String processName) {

		List<CustomAttributeValue> customAttributeValues = null;
		List<AttachmentRequestDto> attachmentList = null;
		AttachmentRequestDto attachmentRequestDto = null;
		CustomAttributeValue attributeValue = null;
		Map<String, String> attrValue = null;
		Map<String, String> budgetValues = null;
		Integer i = 0, k = 0;
		String value = "";
		Map<String, List<String>> tableValues = new HashMap<>();
		List<CustomAttributeValuesTableAdhocDto> customAttributeValuesTableAdhocDtos = new ArrayList<CustomAttributeValuesTableAdhocDto>();
		List<String> rowValues = new ArrayList<String>();

		System.err.println("[WBP-Dev]done");
		customAttributeValues = new ArrayList<CustomAttributeValue>();
		for (ProcessAttributesDto attrValues : listOfProcesssAttributes) {
			attrValue = new HashMap<>();
			// adding the non visible process level attributes
			List<CustomAttributeTemplateDto> customAttributeTemplateDtos = attributeDetials
					.getNotVisibleAttributeResponseDto(processName);
			for (CustomAttributeTemplateDto customAttributeTemplateDto : customAttributeTemplateDtos) {
				attrValues.getCustomAttributeTemplateDto().add(customAttributeTemplateDto);
			}

			for (CustomAttributeTemplateDto obj : attrValues.getCustomAttributeTemplateDto()) {
				if (obj.getProcessName().equals(TaskCreationConstant.STANDARD)) {
					continue;
				}
				processName = obj.getProcessName();
				attributeValue = new CustomAttributeValue();

				attributeValue.setKey(obj.getKey());
				if (ServicesUtil.isEmpty(obj.getAttributePath())) {
					if (obj.getDataType().equals(TaskCreationConstant.ATTACHMENT) && !"".equals(obj.getValue())) {

						attachmentRequestDto = new AttachmentRequestDto();
						attachmentRequestDto.setEncodedFileContent(obj.getValue());
						if (!ServicesUtil.isEmpty(obj.getAttachmentName()))
							attachmentRequestDto.setFileName(obj.getAttachmentName());
						else
							attachmentRequestDto.setFileName(obj.getLabel());
						if (!ServicesUtil.isEmpty(obj.getAttachmentSize()))
							attachmentRequestDto.setFileSize(obj.getAttachmentSize());

						attachmentRequestDto.setFileType(obj.getAttachmentType());
						attachmentList = new ArrayList<>();
						attachmentList.add(attachmentRequestDto);
						DocumentResponseDto responseDto = sharepointUploadFile.uploadFile(attachmentList,
								attachmentRequestDto.getFileName());
						Gson g = new Gson();
						attributeValue.setAttributeValue(g.toJson(responseDto.getAttachmentIds()));

					} else if (obj.getDataType().equals(TaskCreationConstant.TABLE)) {

						if (!ServicesUtil.isEmpty(obj.getTableContents())) {
							Integer rowNumber = 1;
							for (TableContentDto tableContent : obj.getTableContents()) {
								List<CustomAttributeTemplateDto> rowAttributes = tableContent.getTableAttributes();
								for (CustomAttributeTemplateDto customAttributeTemplateDto : rowAttributes) {
									if (tableValues.containsKey(customAttributeTemplateDto.getKey())) {
										tableValues.get(customAttributeTemplateDto.getKey())
												.add(customAttributeTemplateDto.getValue());
									} else {
										rowValues = new ArrayList<String>();
										rowValues.add(customAttributeTemplateDto.getValue());
										tableValues.put(customAttributeTemplateDto.getKey(), rowValues);
									}
									
									//saving process level table contents
									CustomAttributeValuesTableAdhocDto customAttributeValuesTableAdhocDto = new CustomAttributeValuesTableAdhocDto();
									customAttributeValuesTableAdhocDto.setTaskId(processIdList.get(i));
									customAttributeValuesTableAdhocDto
											.setProcessName(obj.getKey());
									customAttributeValuesTableAdhocDto.setKey(customAttributeTemplateDto.getKey());
									customAttributeValuesTableAdhocDto.setRowNumber(rowNumber);
									customAttributeValuesTableAdhocDto
											.setAttributeValue(customAttributeTemplateDto.getValue());
									customAttributeValuesTableAdhocDto.setDependantOn(processName);
									customAttributeValuesTableAdhocDtos.add(customAttributeValuesTableAdhocDto);
									
								}
								rowNumber++;
							}
						}
					}

					else {
						attributeValue.setAttributeValue(obj.getValue());
						if ("ProjectProposalDocumentApproval".equalsIgnoreCase(obj.getProcessName())) {
							if (obj.getKey().equalsIgnoreCase("2693jcccd5da7"))
								attributeValue.setAttributeValue(budgetValues.get("4GYUKI003"));
							else if (obj.getKey().equalsIgnoreCase("h3117e8462i1"))
								attributeValue.setAttributeValue(budgetValues.get("3DE1TY0011"));
							else if (obj.getKey().equalsIgnoreCase("dbaif84h6i2f"))
								attributeValue.setAttributeValue(budgetValues.get("4YULJ59001"));
						}
					}
				} else {
					if (obj.getAttributePath().contains("$")) {
						String[] attr = obj.getAttributePath().split("[$]");
						value = "";
						String s = "";
						for (String str : attr) {
							if (str.contains("{")) {
								s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
								if (attrValue.containsKey(s))
									str = str.replace("{" + s + "}",
											(ServicesUtil.isEmpty(attrValue.get(s))) ? "" : attrValue.get(s));
							}
							value += str;
						}
						attributeValue.setAttributeValue(value);
						if (obj.getDataType().contains(TaskCreationConstant.CALCULATION)) {

							String dataType = obj.getDataType().split("-")[0];
							switch (dataType) {
							case "INPUT NUMERIC":
								try {
									value = EvaluateExpression(attrValue, obj.getAttributePath());
									attributeValue.setAttributeValue(value);
								} catch (Exception e) {
									attributeValue.setAttributeValue("");
									System.err.println("[WBP-Dev]Calculating Expression Failed" + e.getMessage());
								}
								break;

							default:
								attributeValue.setAttributeValue("");
								break;
							}
						}
					} else {
						attributeValue.setAttributeValue(obj.getAttributePath());
					}
				}

				attributeValue.setProcessName(obj.getProcessName());
				if (TaskCreationConstant.PROCESS.equals(obj.getOrigin()))
					attributeValue.setTaskId(processIdList.get(i));
				else
					attributeValue.setTaskId(eventIdList.get(i));

				attrValue.put(attributeValue.getKey(), attributeValue.getAttributeValue());
				customAttributeValues.add(attributeValue);
			}

			CustomAttributeValue attributeValue2 = null;
			List<ProcessTemplateValueDto> templateValueDtos = templateDtos.get(processIdList.get(i));

			System.err.println("Attribute values :" + new Gson().toJson(attrValue));
			System.err.println("Table values :" + new Gson().toJson(tableValues));

			for (ProcessTemplateValueDto processTemplateValueDto : templateValueDtos) {
				taskCustomAttr = customAttributeTemplateDao
						.getTaskCustomAttributesByTemplateId(processTemplateValueDto.getTemplateId());
				System.err.println("Task level attributes : " + new Gson().toJson(taskCustomAttr));
				List<TaskOwnersDto> taskOwnerList = taskTemplateOwnerDao
						.getOwners(processTemplateValueDto.getOwnerId());

				switch (processTemplateValueDto.getOwnerSeqType()) {
				case TaskCreationConstant.GROUP_TASK:
					for (CustomAttributeTemplateDto customAttributeTemplateDto : taskCustomAttr) {
						attributeValue2 = setCustomAttributeValue(customAttributeTemplateDto, attrValue,
								processIdList.get(i), eventIdList.get(k), tableValues, 
								processTemplateValueDto.getTaskName(), customAttributeValuesTableAdhocDtos);
						attrValue.put(attributeValue2.getKey(), attributeValue2.getAttributeValue());
						customAttributeValues.add(attributeValue2);

					}
					k++;
					break;
				case TaskCreationConstant.PARALLEL_TASK:
				case TaskCreationConstant.SEQUENTIAL_TASK:
					for (TaskOwnersDto taskOwnersDto : taskOwnerList) {
						for (CustomAttributeTemplateDto customAttributeTemplateDto : taskCustomAttr) {
							attributeValue2 = setCustomAttributeValue(customAttributeTemplateDto, attrValue,
									processIdList.get(i), eventIdList.get(k), tableValues, 
									processTemplateValueDto.getTaskName(), customAttributeValuesTableAdhocDtos);
							attrValue.put(attributeValue2.getKey(), attributeValue2.getAttributeValue());
							customAttributeValues.add(attributeValue2);
						}
						k++;
					}
					break;
				default:
					break;
				}

//				for (CustomAttributeTemplateDto customAttributeTemplateDto : taskCustomAttr) {
//
//					switch (processTemplateValueDto.getOwnerSeqType()) {
//					case TaskCreationConstant.GROUP_TASK:
//
//						attributeValue2 = setCustomAttributeValue(customAttributeTemplateDto, attrValue,
//								processIdList.get(i), eventIdList.get(k), tableValues, rowValues.size(),
//								processTemplateValueDto.getTaskName(), customAttributeValuesTableAdhocDtos);
//						attrValue.put(attributeValue2.getKey(), attributeValue2.getAttributeValue());
//						customAttributeValues.add(attributeValue2);
//						k++;
//						break;
//					case TaskCreationConstant.PARALLEL_TASK:
//					case TaskCreationConstant.SEQUENTIAL_TASK:
//						for (TaskOwnersDto taskOwnersDto : taskOwnerList) {
//							attributeValue2 = setCustomAttributeValue(customAttributeTemplateDto, attrValue,
//									processIdList.get(i), eventIdList.get(k), tableValues, rowValues.size(),
//									processTemplateValueDto.getTaskName(), customAttributeValuesTableAdhocDtos);
//							attrValue.put(attributeValue2.getKey(), attributeValue2.getAttributeValue());
//							customAttributeValues.add(attributeValue2);
//							k++;
//						}
//						break;
//					default:
//						break;
//					}
//				}

			}
			i++;
			attrValue.clear();
		}

		System.err.println("Table attribute values : " + new Gson().toJson(customAttributeValuesTableAdhocDtos));
		customAttributeValuesTableAdhocDao.saveOrUpdateTableValues(customAttributeValuesTableAdhocDtos);
		return customAttributeValues;
	}

	String EvaluateExpression(Map<String, String> attrValue, String exp) {
		int j = 0;
		String key = "";
		String finalExp = "";
		System.err.println("[WBP-Dev] Attribute path : " + exp);
		while (j < exp.length() - 1) {
			char ch = exp.charAt(j);
			char next = exp.charAt(j + 1);
			if (ch == '{') {
				while (ch != '}') {
					key += ch;
					j++;
					ch = exp.charAt(j);
				}
				String value = attrValue.get(key.substring(1, key.length()));
				if (value.equals("") && !finalExp.equals("")) {
					finalExp = finalExp.substring(0, finalExp.length() - 1);
				} else {
					finalExp += value;
				}
				key = "";
			}
			if (ch == '\'' && next != ' ' && !finalExp.equals("")) {
				finalExp += next;
			}
			j++;
		}
		System.err.println("[WBP-Dev] Final exp : " + finalExp);
		String value = String.valueOf(new ExpressionBuilder(finalExp).build().evaluate());
		return value;
	}

	//for setting task level custom attribute
	private CustomAttributeValue setCustomAttributeValue(CustomAttributeTemplateDto customAttributeTemplateDto,
			Map<String, String> attrValue, String processId, String taskId, Map<String, List<String>> tableValues, String taskName,
			List<CustomAttributeValuesTableAdhocDto> customAttributeValuesTableAdhocDtos) {

		CustomAttributeValue attributeValue2 = new CustomAttributeValue();
		attributeValue2.setKey(customAttributeTemplateDto.getKey());
		if (!ServicesUtil.isEmpty(customAttributeTemplateDto.getAttributePath())) {

			if (customAttributeTemplateDto.getAttributePath().contains("$")) {
				String[] attr = customAttributeTemplateDto.getAttributePath().split("[$]");
				String value = "";
				String s = "";
				for (String str : attr) {
					if (str.contains("{")) {
						s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
						if (attrValue.containsKey(s))
							str = str.replace("{" + s + "}",
									(ServicesUtil.isEmpty(attrValue.get(s))) ? "" : attrValue.get(s));
						else
							str = "";
					}

					value += str;
				}
				attributeValue2.setAttributeValue(value);
				if (customAttributeTemplateDto.getDataType().contains(TaskCreationConstant.CALCULATION)) {

					String dataType = customAttributeTemplateDto.getDataType().split("-")[0];
					switch (dataType) {
					case "INPUT NUMERIC":
						try {
							value = EvaluateExpression(attrValue, customAttributeTemplateDto.getAttributePath());
							attributeValue2.setAttributeValue(value);
						} catch (Exception e) {
							System.err.println("[WBP-Dev]Calculating Expression Failed" + e.getMessage());
							attributeValue2.setAttributeValue("");
						}
						break;

					default:
						attributeValue2.setAttributeValue("");
						break;
					}
				} else if (customAttributeTemplateDto.getDataType().equals(TaskCreationConstant.TABLE)) {
					
					if(!ServicesUtil.isEmpty(customAttributeTemplateDto.getTableContents())) {
						for (CustomAttributeTemplateDto rowAttributeTemplateDto : customAttributeTemplateDto
								.getTableContents().get(0).getTableAttributes()) {
							
							String attributePath = rowAttributeTemplateDto.getAttributePath();
							if (attributePath.contains("{")) {
								String key = attributePath.substring(attributePath.indexOf("{") + 1,
										attributePath.indexOf("}"));
								for(int rowNumber = 1; rowNumber <= tableValues.get(key).size() ; rowNumber++) {
									CustomAttributeValuesTableAdhocDto customAttributeValuesTableAdhocDto = new CustomAttributeValuesTableAdhocDto();
									customAttributeValuesTableAdhocDto.setTaskId(taskId);
									customAttributeValuesTableAdhocDto
											.setProcessName(rowAttributeTemplateDto.getProcessName());
									customAttributeValuesTableAdhocDto.setKey(rowAttributeTemplateDto.getKey());
									customAttributeValuesTableAdhocDto.setRowNumber(rowNumber);
									customAttributeValuesTableAdhocDto
											.setAttributeValue(tableValues.get(key).get(rowNumber - 1));
									customAttributeValuesTableAdhocDto.setDependantOn(taskName);
									customAttributeValuesTableAdhocDtos.add(customAttributeValuesTableAdhocDto);
								}
							}
						}						
					}

//					for (int rowNumber = 1; rowNumber <= rowCount; rowNumber++) {
//						if (!ServicesUtil.isEmpty(customAttributeTemplateDto.getTableContents())) {
////							String dependantOn = customAttributeTemplateDto.getProcessName();
//							for (CustomAttributeTemplateDto rowAttributeTemplateDto : customAttributeTemplateDto
//									.getTableContents().get(0).getTableAttributes()) {
//								String attributePath = rowAttributeTemplateDto.getAttributePath();
//								if (attributePath.contains("{")) {
//									String key = attributePath.substring(attributePath.indexOf("{") + 1,
//											attributePath.indexOf("}"));
//									
//									CustomAttributeValuesTableAdhocDto customAttributeValuesTableAdhocDto = new CustomAttributeValuesTableAdhocDto();
//									customAttributeValuesTableAdhocDto.setTaskId(taskId);
//									customAttributeValuesTableAdhocDto
//											.setProcessName(rowAttributeTemplateDto.getProcessName());
//									customAttributeValuesTableAdhocDto.setKey(rowAttributeTemplateDto.getKey());
//									customAttributeValuesTableAdhocDto.setRowNumber(rowNumber);
//									customAttributeValuesTableAdhocDto
//											.setAttributeValue(tableValues.get(key).get(rowNumber - 1));
//									customAttributeValuesTableAdhocDto.setDependantOn(taskName);
//									customAttributeValuesTableAdhocDtos.add(customAttributeValuesTableAdhocDto);
//								}
//
//							}
//						}
//					}
				}

			} else {
				attributeValue2.setAttributeValue(customAttributeTemplateDto.getAttributePath());
			}
		} else {
			attributeValue2.setAttributeValue("");
		}
		attributeValue2.setProcessName(customAttributeTemplateDto.getProcessName());
		if (TaskCreationConstant.PROCESS.equals(customAttributeTemplateDto.getOrigin()))
			attributeValue2.setTaskId(processId);
		else
			attributeValue2.setTaskId(taskId);
		return attributeValue2;

	}

	private TaskDetailDto setAllTaskEventsAndOwners(AttributesResponseDto tasksToSubmit, String ownerName,
			List<String> processIdList, List<String> eventIdList, List<ProcessEventsDto> processes, String actionType,
			TaskCreationDto taskCreationDto, RequestIdDto requestIdDto) {

		List<TaskEventsDto> taskEventsDtos = null;
		List<TaskOwnersDto> taskOwnersDtos = null;
		TaskEventsDto taskEventsDto = null;
		TimeZoneConvertion timeZoneConvertion = null;
		TaskDetailDto taskDetailDto = new TaskDetailDto();
		timeZoneConvertion = new TimeZoneConvertion();
		Integer i = 0;
		ProcessConfigDto processConfigDto = processConfigDao.getProcessDetail(tasksToSubmit.getProcessName());

		String sla = processConfigDto.getSla();

		String requestIdTemp = null;
		String reqIdString = null;
		Integer id = 0;
		String requestId = null;
		Object[] requestDetail = null;

		requestIdTemp = requestIdDto.getRequestId().substring(0, 2);
		requestId = requestIdDto.getRequestId();
		id = requestIdDto.getId();
		requestDetail = requestIdDto.getRequestDetail();

		taskEventsDtos = new ArrayList<TaskEventsDto>();
		taskOwnersDtos = new ArrayList<TaskOwnersDto>();
		for (Entry<String, List<ProcessTemplateValueDto>> process : taskCreationDto.getTemplateDtos().entrySet()) {
			for (ProcessTemplateValueDto obj : process.getValue()) {

				List<TaskOwnersDto> taskOwnerList = taskTemplateOwnerDao.getOwners(obj.getOwnerId());
				List<String> taskIds = new ArrayList<>();
				String userList = "";
				List<TaskEventsDto> currentTasks = new ArrayList<>();
				Date currentTime = ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC());
				switch (obj.getOwnerSeqType()) {
				case TaskCreationConstant.GROUP_TASK:

					taskEventsDto = setTaskEvent(eventIdList.get(i), process.getKey(), actionType, processConfigDto,
							obj, tasksToSubmit, requestId, "", TaskCreationConstant.NEW, currentTime);
					for (TaskOwnersDto taskOwnersDto : taskOwnerList) {
						taskOwnersDto.setEventId(eventIdList.get(i));
						taskOwnersDto.setIsProcessed(false);
						taskOwnersDto.setIsSubstituted(false);
						taskOwnersDto.setEnRoute(true);
					}
					taskOwnersDtos.addAll(taskOwnerList);
					taskEventsDtos.add(taskEventsDto);
					i++;
					break;
				case TaskCreationConstant.PARALLEL_TASK:
					for (TaskOwnersDto taskOwnersDto : taskOwnerList) {

						taskEventsDto = setTaskEvent(eventIdList.get(i), process.getKey(), actionType, processConfigDto,
								obj, tasksToSubmit, requestId, taskOwnersDto.getTaskOwner(), TaskCreationConstant.NEW,
								currentTime);
						taskEventsDtos.add(taskEventsDto);
						taskOwnersDto.setEventId(eventIdList.get(i));
						taskOwnersDto.setIsProcessed(false);
						taskOwnersDto.setIsSubstituted(false);
						taskOwnersDto.setEnRoute(true);
						i++;
					}
					taskOwnersDtos.addAll(taskOwnerList);
					break;
				case TaskCreationConstant.SEQUENTIAL_TASK:
					for (TaskOwnersDto taskOwnersDto : taskOwnerList) {
						taskEventsDto = setTaskEvent(eventIdList.get(i), process.getKey(), actionType, processConfigDto,
								obj, tasksToSubmit, requestId, taskOwnersDto.getTaskOwner(),
								TaskCreationConstant.NOT_STARTED, currentTime);
						taskEventsDtos.add(taskEventsDto);
						currentTasks.add(taskEventsDto);
						taskOwnersDto.setEventId(eventIdList.get(i));
						taskOwnersDto.setIsProcessed(false);
						taskOwnersDto.setIsSubstituted(false);
						taskOwnersDto.setEnRoute(true);
						taskIds.add(eventIdList.get(i));
						userList += "'" + taskOwnersDto.getTaskOwner() + "',";
						i++;
					}
					taskOwnersDtos.addAll(taskOwnerList);
					List<String> userIds = userIDPMappingDao.getUsersWithOrderByNameWithType(obj.getOrderBy(),
							userList.substring(0, userList.length() - 1));
					if (!ServicesUtil.isEmpty(taskOwnerList)) {
						if (!ServicesUtil.isEmpty(userIds)) {
							String userId = userIds.get(0);
							for (TaskEventsDto task : currentTasks) {
								if (task.getOwnerId().equals(userId)) {
									task.setStatus("READY");
								}
							}
						}
					}
					break;

				default:
					break;
				}
			}
			reqIdString = requestId;
			reqIdString = reqIdString.substring(reqIdString.length() - 4);

			id = id + 1;

			if (requestDetail != null)
				requestId = requestDetail[1].toString() + String.format("%04d", id);
			else
				requestId = requestIdTemp + String.format("%04d", id);
		}

		taskDetailDto.setTaskEventsDtos(taskEventsDtos);
		taskDetailDto.setTaskOwnersDtos(taskOwnersDtos);
		return taskDetailDto;
	}

	private TaskEventsDto setTaskEvent(String eventId, String processId, String actionType,
			ProcessConfigDto processConfigDto, ProcessTemplateValueDto obj, AttributesResponseDto tasksToSubmit,
			String requestId, String ownerId, String status, Date currentTime) {

		TaskEventsDto taskEventsDto = new TaskEventsDto();

		taskEventsDto.setEventId(eventId);
		taskEventsDto.setName(obj.getTaskName());
		taskEventsDto.setProcessId(processId);
		taskEventsDto.setCreatedBy(tasksToSubmit.getResourceid());
		taskEventsDto.setCurrentProcessor(ownerId);
//		taskEventsDto.setCurrentProcessorDisplayName("");
		taskEventsDto.setOrigin(TaskCreationConstant.TASK_MANAGEMENT_ORIGIN);
		taskEventsDto.setPriority("MEDIUM");
		taskEventsDto.setProcessName(tasksToSubmit.getProcessName());
		taskEventsDto.setSlaDueDate(null);
		if ("Submit".equals(actionType) || "Resubmit".equals(actionType))
			taskEventsDto.setStatus(status);
		else if ("Save".equals(actionType))
			taskEventsDto.setStatus(TaskCreationConstant.DRAFT);
		taskEventsDto.setStatusFlag("0");
		taskEventsDto.setSubject(obj.getDescription().replaceAll("[{].*}", "").trim().replaceAll(" +", " "));
		taskEventsDto.setDescription(
				obj.getSubject().replaceAll("[{].*}", "").trim().replaceAll(" +", " ") + " - " + requestId);
		taskEventsDto.setTaskMode("NULL");
		taskEventsDto.setTaskType(obj.getTaskType());
		taskEventsDto.setCreatedAt(currentTime);
		taskEventsDto.setCompletedAt(null);
		taskEventsDto.setUpdatedAt(currentTime);
		String businessStatus = statusConfigDao.getBusinessStatus(taskEventsDto.getProcessName(),
				taskEventsDto.getName(), taskEventsDto.getStatus());
		taskEventsDto.setBusinessStatus(ServicesUtil.isEmpty(businessStatus) ? null : businessStatus);
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(processConfigDto.getSla().trim()));

		taskEventsDto.setSlaDueDate(cal.getTime());
		taskEventsDto.setCompletionDeadLine(cal.getTime());
		taskEventsDto.setOwnerId(ownerId);

		if (!ServicesUtil.isEmpty(obj.getUrl())) {
			taskEventsDto
					.setDetailUrl(obj.getUrl() + TaskCreationConstant.URL_REQUEST_PARAM + taskEventsDto.getEventId());
			if ((taskEventsDto.getProcessName().toLowerCase()).startsWith("dt"))
				taskEventsDto.setDetailUrl(obj.getUrl() + "/" + taskEventsDto.getEventId());
			if (taskEventsDto.getProcessName().equals("ResourcePlanningWorkflowForRMG")) {
				taskEventsDto.setDetailUrl(obj.getUrl() + "&taskId=" + taskEventsDto.getEventId());

				if (taskEventsDto.getName().contains("L5")) {
					taskEventsDto
							.setDetailUrl(obj.getUrl() + "&taskId=" + taskEventsDto.getEventId() + "&exception=true");

				}
			}

		} else if (!ServicesUtil.isEmpty(processConfigDto.getDetailURL())) {
			taskEventsDto.setDetailUrl(processConfigDto.getDetailURL() + TaskCreationConstant.URL_REQUEST_PARAM
					+ taskEventsDto.getEventId());
			if ((taskEventsDto.getProcessName().toLowerCase()).startsWith("dt"))
				taskEventsDto.setDetailUrl(processConfigDto.getDetailURL() + "/" + taskEventsDto.getEventId());
			if (taskEventsDto.getProcessName().equals("ResourcePlanningWorkflowForRMG")) {
				taskEventsDto.setDetailUrl(processConfigDto.getDetailURL() + "&taskId=" + taskEventsDto.getEventId());

				if (taskEventsDto.getName().contains("L5")) {
					taskEventsDto.setDetailUrl(processConfigDto.getDetailURL() + "&taskId=" + taskEventsDto.getEventId()
							+ "&exception=true");

				}
			}

		}
		return taskEventsDto;
	}

	private List<String> generateProcessIdList(int size) {
		List<String> processIdList = new ArrayList<>();
		for (int j = 0; j < size; j++) {
			processIdList.add(generateProcessId());
		}

		return processIdList;
	}

	private String generateProcessId() {
		String processId = UUID.randomUUID().toString().replaceAll("-", "");
		return processId.toString();
	}

	private List<String> generateEventIdList(int size) {
		List<String> eventIdList = new ArrayList<>();
		for (int j = 0; j < size; j++) {
			eventIdList.add(generateEventId() + 1);
		}

		return eventIdList;
	}

	private String generateEventId() {
		String eventId = UUID.randomUUID().toString().replaceAll("-", "");
		return eventId;
	}

	public List<TaskTemplateOwnerDto> createOwnerId(List<ValueListDto> valueList) {

		List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = null;
		TaskTemplateOwnerDto templateOwnerDto = null;

		String ownerId = taskTemplateOwnerDao.createOwnerId(valueList);
		// String ownerId = "O-266398683";
		taskTemplateOwnerDtos = new ArrayList<TaskTemplateOwnerDto>();
		for (ValueListDto value : valueList) {

			templateOwnerDto = new TaskTemplateOwnerDto();

			templateOwnerDto.setId(value.getId());
			templateOwnerDto.setOwnerId(ownerId.toString());
			templateOwnerDto.setType(value.getType());
			templateOwnerDto.setIdName(
					(value.getType().equals("individual") ? userIDPMappingDao.getUserName(value.getId()) : ""));
			taskTemplateOwnerDtos.add(templateOwnerDto);
		}

		return taskTemplateOwnerDtos;

	}

	private List<ProcessTemplateValueDto> getTargetDetails(Map<String, ProcessTemplateValueDto> allTemplateMap,
			List<RuleDto> rules, Map<String, String> attrValue) {
		List<ProcessTemplateValueDto> processTemplateValueDtos = new ArrayList<>();
		String targetIds = "";
		List<String> targetId = new ArrayList<>();
		for (RuleDto ruleDto : rules) {
			try {
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine engine = mgr.getEngineByName("JavaScript");
				String foo = "'" + attrValue.get(ruleDto.getCustom_key()) + "'"
						+ (ruleDto.getLogic().equals("=") ? "==" : ruleDto.getLogic()) + "'" + ruleDto.getValue() + "'";
				System.err.println(engine.eval(foo));
				String result = engine.eval(foo).toString();

				if ("true".equalsIgnoreCase(result)) {
					targetIds += ruleDto.getDestination() + ",";
					if (!targetId.contains(ruleDto.getDestination())) {
						processTemplateValueDtos.add(allTemplateMap.get(ruleDto.getDestination()));
						targetId.add(ruleDto.getDestination());
					}
				}
			} catch (Exception e) {
				System.err.println("error" + e);
			}

		}
		if (!targetIds.equalsIgnoreCase("")) {
			targetIds = targetIds.substring(0, targetIds.length() - 1);
		}
		return processTemplateValueDtos;
	}

	private List<TaskAuditDto> setAllAudit(List<TaskEventsDto> tasks) {
		List<TaskAuditDto> auditDtos = new ArrayList<>();
		TaskAuditDto auditDto = null;
		try {
			for (TaskEventsDto task : tasks) {
				auditDto = new TaskAuditDto();
				auditDto.setAuditId(UUID.randomUUID().toString().replaceAll("-", ""));
				auditDto.setAction("New");
				auditDto.setEventId(task.getEventId());
				auditDto.setUserId(task.getCreatedBy());
				auditDto.setUserName(userIDPMappingDao.getUserName(task.getCreatedBy()));
				auditDto.setUpdatedAt(task.getCreatedAt());
				auditDtos.add(auditDto);
				System.err.println(auditDtos);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev] Error setting Audit" + e.getMessage());
		}
		return auditDtos;
	}

	public TaskCreationDto createSubmitDraftResponse(AttributesResponseDto attributesResponseDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

		TaskCreationDto taskCreationDto = null;
		List<TaskEventsDto> tasks = null;
		List<ProcessEventsDto> processes = null;
		List<CustomAttributeValue> customAttributeValues = null;
		List<TaskOwnersDto> owners = null;
		List<TaskAuditDto> auditDtos = null;
		TaskDetailDto taskDetailDto = null;
		try {
			List<String> processIdList = new ArrayList<String>();
			processIdList.add(attributesResponseDto.getProcessId());
			List<String> eventIdList = new ArrayList<String>();
			eventIdList.add(attributesResponseDto.getListOfProcesssAttributes().get(0).getCustomAttributeTemplateDto()
					.get(0).getTaskId());
			String ownerName = userIDPMappingDao.getOwnerDetailById(attributesResponseDto.getResourceid().split(" "))
					.get(0).getTaskOwnername();
			System.err.println(ownerName);
			List<CustomAttributeTemplateDto> taskCustomAttrs = customAttributeTemplateDao
					.getTaskCustomAttributes(attributesResponseDto.getProcessName());

			taskCreationDto = new TaskCreationDto();
			// taskCreationDto = setTaskValues(tasksToSubmit,processIdList);
			taskCreationDto = setTaskValuesNew(attributesResponseDto, processIdList, taskCustomAttrs);
			if (taskCreationDto.getTaskCount() > 1) {
				eventIdList.addAll(generateEventIdList(taskCreationDto.getTaskCount() - 1));
			}
			Gson g = new Gson();
			System.err.println(g.toJson(taskCreationDto));
			if (TaskCreationConstant.FAILURE.equals(taskCreationDto.getResponseMessage().getStatus()))
				return taskCreationDto;
			customAttributeValues = new ArrayList<CustomAttributeValue>();
			customAttributeValues = setAllCustomAttributeValues(
					taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(), eventIdList,
					processIdList, taskCustomAttrs, taskCreationDto.getTemplateDtos(),
					attributesResponseDto.getProcessName());
			processes = new ArrayList<ProcessEventsDto>();
			RequestIdDto requestIdDto = processEventDao.getRequestId("Submit", attributesResponseDto.getProcessName(),
					"");
			processes = setAllProcessEvent(taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(),
					processIdList, ownerName, "Submit", requestIdDto, attributesResponseDto.getProcessName());

			taskDetailDto = setAllTaskEventsAndOwners(attributesResponseDto, ownerName, processIdList, eventIdList,
					processes, "Submit", taskCreationDto, requestIdDto);

			auditDtos = new ArrayList<TaskAuditDto>();
			auditDtos = setAllAudit(tasks);

			taskCreationDto.setProcesses(processes);
			taskCreationDto.setCustomAttributeValues(customAttributeValues);
			taskCreationDto.setOwners(taskDetailDto.getTaskOwnersDtos());
			taskCreationDto.setTasks(taskDetailDto.getTaskEventsDtos());
			taskCreationDto.setAuditDtos(auditDtos);

			resp.setMessage(TaskCreationConstant.SUCCESS);
			resp.setStatus(TaskCreationConstant.SUCCESS);
			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][PARSE DRAFT REQUEST]ERROR:" + e);
			e.printStackTrace();
		}
		taskCreationDto.setResponseMessage(resp);
		return taskCreationDto;
	}

	public TaskCreationDto taskOwnerIdGeneration(AttributesResponseDto tasksToSave) {
		TaskCreationDto taskCreationDto = new TaskCreationDto();
		taskCreationDto.setTemplateOwnerDtos(new ArrayList<>());
		for (ProcessAttributesDto processAttibutes : tasksToSave.getListOfProcesssAttributes()) {

			for (CustomAttributeTemplateDto attributes : processAttibutes.getCustomAttributeTemplateDto()) {
				if (TaskCreationConstant.DROPDOWN.equals(attributes.getDataType()) && attributes.getIsRunTime()
						&& !attributes.getValueList().isEmpty()) {
					List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = createOwnerId(attributes.getValueList());
					StringBuilder ids = new StringBuilder("");
					for (ValueListDto value : attributes.getValueList()) {
						ids.append(value.getId());
						ids.append(",");
					}
					if (!ServicesUtil.isEmpty(ids))
						attributes.setValue(ids.substring(0, ids.length() - 1));
					else
						attributes.setValue("");

					System.err.println(taskTemplateOwnerDtos);
					taskCreationDto.getTemplateOwnerDtos().addAll(taskTemplateOwnerDtos);
					System.err.println(taskCreationDto);
				}
			}
		}
		taskCreationDto.setAttributesResponseDto(tasksToSave);
		return taskCreationDto;
	}

	public TaskCreationDto createDraftResponse(AttributesResponseDto tasksToSave) {

		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

		TaskCreationDto taskCreationDto = null;
		List<TaskEventsDto> tasks = null;
		List<ProcessEventsDto> processes = null;
		List<CustomAttributeValue> customAttributeValues = null;

		List<String> eventIdList = generateEventIdList(tasksToSave.getListOfProcesssAttributes().size());
		List<String> processIdList = generateProcessIdList(tasksToSave.getListOfProcesssAttributes().size());

		try {
			String ownerName = userIDPMappingDao.getOwnerDetailById(tasksToSave.getResourceid().split(" ")).get(0)
					.getTaskOwnername();
			List<CustomAttributeTemplateDto> taskCustomAttrs = customAttributeTemplateDao
					.getTaskCustomAttributes(tasksToSave.getProcessName());

			taskCreationDto = new TaskCreationDto();
			taskCreationDto = taskOwnerIdGeneration(tasksToSave);
			customAttributeValues = new ArrayList<CustomAttributeValue>();
			// customAttributeValues = setCustomAttributeValue
			// (taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(),eventIdList);
			customAttributeValues = setAllCustomAttributeValues(
					taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(), eventIdList,
					processIdList, taskCustomAttrs, tasksToSave.getProcessName());
			processes = new ArrayList<ProcessEventsDto>();
			RequestIdDto requestIdDto = processEventDao.getRequestId(tasksToSave.getActionType(), tasksToSave
					.getListOfProcesssAttributes().get(0).getCustomAttributeTemplateDto().get(0).getProcessName(), "");
			processes = setAllProcessEvent(taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(),
					processIdList, ownerName, tasksToSave.getActionType(), requestIdDto, tasksToSave.getProcessName());
			// List<TaskTemplateDto> taskTemplateDto =
			// taskTemplateDao.getTaskDetail(tasksToSave.getProcessName());
			taskCreationDto = setTaskValuesNew2(tasksToSave, processIdList);
			tasks = new ArrayList<TaskEventsDto>();
			tasks = setAllTaskEvent(tasksToSave, ownerName, processIdList, eventIdList, processes,
					tasksToSave.getActionType(), taskCreationDto, requestIdDto);

			taskCreationDto.setCustomAttributeValues(customAttributeValues);
			taskCreationDto.setProcesses(processes);
			taskCreationDto.setTasks(tasks);

			resp.setMessage(TaskCreationConstant.SUCCESS);
			resp.setStatus(TaskCreationConstant.SUCCESS);
			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
			
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][PARSE DRAFT REQUEST]ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		taskCreationDto.setResponseMessage(resp);
		return taskCreationDto;
	}

	private List<TaskEventsDto> setAllTaskEvent(AttributesResponseDto tasksToSubmit, String ownerName,
			List<String> processIdList, List<String> eventIdList, List<ProcessEventsDto> processes, String actionType,
			TaskCreationDto taskCreationDto, RequestIdDto requestIdDto) {

		List<TaskEventsDto> taskEventsDtos = null;
		TaskEventsDto taskEventsDto = null;
		TimeZoneConvertion timeZoneConvertion = null;
		timeZoneConvertion = new TimeZoneConvertion();
		Integer i = 0;
		ProcessConfigDto processConfigDto = processConfigDao.getProcessDetail(tasksToSubmit.getProcessName());

		String sla = processConfigDto.getSla();

		String requestIdTemp = null;
		String reqIdString = null;
		Integer id = 0;
		String requestId = null;
		Object[] requestDetail = null;

		requestIdTemp = requestIdDto.getRequestId().substring(0, 2);
		requestId = requestIdDto.getRequestId();
		id = requestIdDto.getId();
		requestDetail = requestIdDto.getRequestDetail();

		taskEventsDtos = new ArrayList<TaskEventsDto>();
		for (Entry<String, List<ProcessTemplateValueDto>> process : taskCreationDto.getTemplateDtos().entrySet()) {
			for (ProcessTemplateValueDto obj : process.getValue()) {

				// List<TaskOwnersDto> taskOwnerList =
				// taskTemplateOwnerDao.getOwners(obj.getOwnerId());
//				List<String> taskIds = new ArrayList<>();
//				String userList = "";
//				List<TaskEventsDto> currentTasks = new ArrayList<>();
//				Date currentTime = ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC());
//				switch (obj.getOwnerSeqType()) {
//				case TaskCreationConstant.GROUP_TASK:
//
//					taskEventsDto = setTaskEvent(eventIdList.get(i), process.getKey(), actionType, processConfigDto,
//							obj, tasksToSubmit, requestId, obj.getOwnerId(), TaskCreationConstant.NEW, currentTime);
//					taskEventsDtos.add(taskEventsDto);
//					i++;
//					break;
//				case TaskCreationConstant.PARALLEL_TASK:
//					for (TaskOwnersDto taskOwnersDto : taskOwnerList) {
//
//						taskEventsDto = setTaskEvent(eventIdList.get(i), process.getKey(), actionType, processConfigDto,
//								obj, tasksToSubmit, requestId, taskOwnersDto.getTaskOwner(), TaskCreationConstant.NEW,
//								currentTime);
//						taskEventsDtos.add(taskEventsDto);
//						i++;
//					}
//					break;
//				case TaskCreationConstant.SEQUENTIAL_TASK:
//					for (TaskOwnersDto taskOwnersDto : taskOwnerList) {
//						taskEventsDto = setTaskEvent(eventIdList.get(i), process.getKey(), actionType, processConfigDto,
//								obj, tasksToSubmit, requestId, taskOwnersDto.getTaskOwner(),
//								TaskCreationConstant.NOT_STARTED, currentTime);
//						taskEventsDtos.add(taskEventsDto);
//						currentTasks.add(taskEventsDto);
//						taskIds.add(eventIdList.get(i));
//						userList += "'" + taskOwnersDto.getTaskOwner() + "',";
//						i++;
//					}
//					break;
//
//				default:
//					break;
//				}

				// if("Rule Based".equalsIgnoreCase(obj.getTaskNature())){
				// List<RuleDto> rules =
				// ruleDao.getRulesById(obj.getTemplateId());
				// String targetIdsFromRules = getTargetDetails(rules
				// ,taskCreationDto.getProcessRelatedAttribute().get(process.getKey()));
				// List<ProcessTemplateDto> processTemplate =
				// processTemplateDao.getDetailByTargetIds(targetIdsFromRules);
				// }else{

				taskEventsDto = new TaskEventsDto();
				taskEventsDto.setEventId(eventIdList.get(i));
				taskEventsDto.setName(obj.getTaskName());
				taskEventsDto.setProcessId(process.getKey());
				taskEventsDto.setCreatedBy(tasksToSubmit.getResourceid());
				taskEventsDto.setCurrentProcessor("");
				taskEventsDto.setCurrentProcessorDisplayName("");
				taskEventsDto.setOrigin(TaskCreationConstant.TASK_MANAGEMENT_ORIGIN);
				taskEventsDto.setPriority("MEDIUM");
				taskEventsDto.setProcessName(tasksToSubmit.getProcessName());
				taskEventsDto.setSlaDueDate(null);
				if ("Submit".equals(actionType) || "Resubmit".equals(actionType))
					taskEventsDto.setStatus(TaskCreationConstant.NEW);
				else if ("Save".equals(actionType))
					taskEventsDto.setStatus(TaskCreationConstant.DRAFT);
				taskEventsDto.setStatusFlag("0");
				taskEventsDto.setSubject(obj.getDescription().replaceAll("[{].*}", "").trim().replaceAll(" +", " "));
				taskEventsDto.setDescription(
						requestId + ": " + obj.getSubject().replaceAll("[{].*}", "").trim().replaceAll(" +", " "));
				taskEventsDto.setTaskMode("NULL");
				taskEventsDto.setTaskType(obj.getTaskType());
				taskEventsDto
						.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
				taskEventsDto.setCompletedAt(null);
				taskEventsDto
						.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
				String businessStatus = statusConfigDao.getBusinessStatus(taskEventsDto.getProcessName(),
						taskEventsDto.getName(), taskEventsDto.getStatus());
				taskEventsDto.setBusinessStatus(ServicesUtil.isEmpty(businessStatus) ? null : businessStatus);
				Calendar cal = Calendar.getInstance();

				cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(sla.trim()));

				taskEventsDto.setSlaDueDate(cal.getTime());
				taskEventsDto.setCompletionDeadLine(cal.getTime());

				if (!ServicesUtil.isEmpty(obj.getUrl())) {
					taskEventsDto.setDetailUrl(
							obj.getUrl() + TaskCreationConstant.URL_REQUEST_PARAM + taskEventsDto.getEventId());
					if ((taskEventsDto.getProcessName().toLowerCase()).startsWith("dt"))
						taskEventsDto.setDetailUrl(obj.getUrl() + "/" + taskEventsDto.getEventId());
					if (taskEventsDto.getProcessName().equals("ResourcePlanningWorkflowForRMG")) {
						taskEventsDto.setDetailUrl(obj.getUrl() + "&taskId=" + taskEventsDto.getEventId());

						if (taskEventsDto.getName().contains("L5")) {
							taskEventsDto.setDetailUrl(
									obj.getUrl() + "&taskId=" + taskEventsDto.getEventId() + "&exception=true");

						}
					}

				} else if (!ServicesUtil.isEmpty(processConfigDto.getDetailURL())) {
					taskEventsDto.setDetailUrl(processConfigDto.getDetailURL() + TaskCreationConstant.URL_REQUEST_PARAM
							+ taskEventsDto.getEventId());
					if ((taskEventsDto.getProcessName().toLowerCase()).startsWith("dt"))
						taskEventsDto.setDetailUrl(processConfigDto.getDetailURL() + "/" + taskEventsDto.getEventId());
					if (taskEventsDto.getProcessName().equals("ResourcePlanningWorkflowForRMG")) {
						taskEventsDto.setDetailUrl(
								processConfigDto.getDetailURL() + "&taskId=" + taskEventsDto.getEventId());

						if (taskEventsDto.getName().contains("L5")) {
							taskEventsDto.setDetailUrl(processConfigDto.getDetailURL() + "&taskId="
									+ taskEventsDto.getEventId() + "&exception=true");

						}
					}

				}
				taskEventsDto.setOwnerId(obj.getOwnerId());
				taskEventsDtos.add(taskEventsDto);
				i++;
			}
			reqIdString = requestId;
			reqIdString = reqIdString.substring(reqIdString.length() - 4);

			id = id + 1;

			if (requestDetail != null)
				requestId = requestDetail[1].toString() + String.format("%04d", id);
			else
				requestId = requestIdTemp + String.format("%04d", id);
		}

		return taskEventsDtos;
	}

	private TaskCreationDto setTaskValuesNew2(AttributesResponseDto tasksToSubmit, List<String> processIdList) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(TaskCreationConstant.FAILURE);
		responseMessage.setStatus(TaskCreationConstant.FAILURE);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		TaskCreationDto taskCreationDto = null;
		Integer processIndex = 0;
		List<ProcessTemplateValueDto> processTemplateValues = null;
		ProcessTemplateValueDto processTemplateValue = null;
		Map<String, String> attrValue = new HashMap<>();
		Map<String, List<ProcessTemplateValueDto>> templateMap = new HashMap<>();
		String description = "";
		String subject = "";
		Integer taskCount = 0;

		List<Object[]> taskOwnerDetailList = taskTemplateDao.getCommonTaskOwnersDetails(tasksToSubmit.getProcessName());

		System.err.println(taskOwnerDetailList);
		processTemplateValues = new ArrayList<ProcessTemplateValueDto>();
		for (ProcessAttributesDto instanceDetail : tasksToSubmit.getListOfProcesssAttributes()) {

			for (CustomAttributeTemplateDto attribute : instanceDetail.getCustomAttributeTemplateDto()) {
				attrValue.put(attribute.getKey(), attribute.getValue());
			}
			for (Object[] taskOwnerDetail : taskOwnerDetailList) {

				processTemplateValue = new ProcessTemplateValueDto();

				processTemplateValue.setProcessId(processIdList.get(processIndex));
				processTemplateValue
						.setCustomKey(ServicesUtil.isEmpty(taskOwnerDetail[5]) ? null : (String) taskOwnerDetail[5]);
				processTemplateValue.setTaskName((String) taskOwnerDetail[1]);
				processTemplateValue.setTaskType((String) taskOwnerDetail[4]);
				description = ServicesUtil.isEmpty(taskOwnerDetail[6]) ? "" : (String) taskOwnerDetail[6];
				String[] desc = description.split("[$]");
				description = "";
				String s = "";
				for (String str : desc) {
					if (str.contains("{")) {
						s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
						if (attrValue.containsKey(s))
							str = str.replace("{" + s + "}",
									(ServicesUtil.isEmpty(attrValue.get(s))) ? "" : attrValue.get(s));
					}
					description = description + str;
				}
				processTemplateValue.setDescription(description);
				subject = ServicesUtil.isEmpty(taskOwnerDetail[7]) ? "" : (String) taskOwnerDetail[7];
				desc = subject.split("[$]");
				subject = "";
				for (String str : desc) {
					if (str.contains("{")) {
						s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
						if (attrValue.containsKey(s))
							str = str.replace("{" + s + "}",
									(ServicesUtil.isEmpty(attrValue.get(s))) ? "" : attrValue.get(s));
					}
					System.err.println(str);
					subject += str;
				}
				processTemplateValue.setSubject(subject);
				processTemplateValue.setRunTimeUser(ServicesUtil.asBoolean(taskOwnerDetail[3]));
				processTemplateValue
						.setSourceId(ServicesUtil.isEmpty(taskOwnerDetail[8]) ? null : (String) taskOwnerDetail[8]);
				processTemplateValue
						.setTargetId(ServicesUtil.isEmpty(taskOwnerDetail[9]) ? null : (String) taskOwnerDetail[9]);
				processTemplateValue
						.setTemplateId(ServicesUtil.isEmpty(taskOwnerDetail[2]) ? null : (String) taskOwnerDetail[2]);
				processTemplateValue
						.setUrl(ServicesUtil.isEmpty(taskOwnerDetail[10]) ? null : (String) taskOwnerDetail[10]);
				processTemplateValue
						.setOwnerSeqType(ServicesUtil.isEmpty(taskOwnerDetail[12]) ? "" : (String) taskOwnerDetail[12]);
				processTemplateValue
						.setAttrTypeName(ServicesUtil.isEmpty(taskOwnerDetail[14]) ? "" : (String) taskOwnerDetail[14]);
				processTemplateValue
						.setOrderBy(ServicesUtil.isEmpty(taskOwnerDetail[15]) ? "" : (String) taskOwnerDetail[15]);
				processTemplateValues.add(processTemplateValue);

				if (ServicesUtil.isEmpty(processTemplateValue.getSourceId())) {
					taskCount++;
					if (templateMap.containsKey(processTemplateValue.getProcessId())) {
						templateMap.get(processTemplateValue.getProcessId()).add(processTemplateValue);
					} else {
						List<ProcessTemplateValueDto> dtos = new ArrayList<>();
						dtos.add(processTemplateValue);
						templateMap.put(processTemplateValue.getProcessId(), dtos);
					}
				}
			}

			processIndex++;
		}
		taskCreationDto = new TaskCreationDto();

		taskCreationDto.setProcessTemplateValues(processTemplateValues);
		taskCreationDto.setTemplateDtos(templateMap);
		taskCreationDto.setTaskCount(taskCount);
		responseMessage.setMessage(TaskCreationConstant.SUCCESS);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
		responseMessage.setStatus(TaskCreationConstant.SUCCESS);
		taskCreationDto.setResponseMessage(responseMessage);
		return taskCreationDto;
	}

	// for submit type save
	private List<CustomAttributeValue> setAllCustomAttributeValues(List<ProcessAttributesDto> listOfProcesssAttributes,
			List<String> eventIdList, List<String> processIdList, List<CustomAttributeTemplateDto> taskCustomAttr,
			String processName) {

		List<CustomAttributeValue> customAttributeValues = null;
		List<AttachmentRequestDto> attachmentList = null;
		AttachmentRequestDto attachmentRequestDto = null;
		CustomAttributeValue attributeValue = null;
		Map<String, String> attrValue = null;
		Map<String, String> budgetValues = null;
		Integer i = 0;
		String value = "";
		Map<String, List<String>> tableValues = new HashMap<>();
		List<CustomAttributeValuesTableAdhocDto> customAttributeValuesTableAdhocDtos = new ArrayList<CustomAttributeValuesTableAdhocDto>();
		List<String> rowValues = new ArrayList<String>();

		System.err.println("[WBP-Dev]done");
		customAttributeValues = new ArrayList<CustomAttributeValue>();
		for (ProcessAttributesDto attrValues : listOfProcesssAttributes) {
			attrValue = new HashMap<>();

			// adding the non visible process level attributes
			List<CustomAttributeTemplateDto> customAttributeTemplateDtos = attributeDetials
					.getNotVisibleAttributeResponseDto(processName);
			for (CustomAttributeTemplateDto customAttributeTemplateDto : customAttributeTemplateDtos) {
				attrValues.getCustomAttributeTemplateDto().add(customAttributeTemplateDto);
			}

			for (CustomAttributeTemplateDto obj : attrValues.getCustomAttributeTemplateDto()) {
				if (obj.getProcessName().equals(TaskCreationConstant.STANDARD)) {
					continue;
				}
				processName = obj.getProcessName();
				attributeValue = new CustomAttributeValue();

				attributeValue.setKey(obj.getKey());
				if (ServicesUtil.isEmpty(obj.getAttributePath())) {
					if (obj.getDataType().equals(TaskCreationConstant.ATTACHMENT) && !"".equals(obj.getValue())) {

						attachmentRequestDto = new AttachmentRequestDto();
						attachmentRequestDto.setEncodedFileContent(obj.getValue());
						if (!ServicesUtil.isEmpty(obj.getAttachmentName()))
							attachmentRequestDto.setFileName(obj.getAttachmentName());
						else
							attachmentRequestDto.setFileName(obj.getLabel());
						if (!ServicesUtil.isEmpty(obj.getAttachmentSize()))
							attachmentRequestDto.setFileSize(obj.getAttachmentSize());

						attachmentRequestDto.setFileType(obj.getAttachmentType());
						attachmentList = new ArrayList<>();
						attachmentList.add(attachmentRequestDto);
						DocumentResponseDto responseDto = sharepointUploadFile.uploadFile(attachmentList,
								attachmentRequestDto.getFileName());
						Gson g = new Gson();
						attributeValue.setAttributeValue(g.toJson(responseDto.getAttachmentIds()));

					} else if (obj.getDataType().equals(TaskCreationConstant.TABLE)) {

						if (!ServicesUtil.isEmpty(obj.getTableContents())) {
							Integer rowNumber = 1;
							for (TableContentDto tableContent : obj.getTableContents()) {
								List<CustomAttributeTemplateDto> rowAttributes = tableContent.getTableAttributes();
								for (CustomAttributeTemplateDto customAttributeTemplateDto : rowAttributes) {
									if (tableValues.containsKey(customAttributeTemplateDto.getKey())) {
										tableValues.get(customAttributeTemplateDto.getKey())
												.add(customAttributeTemplateDto.getValue());
									} else {
										rowValues = new ArrayList<String>();
										rowValues.add(customAttributeTemplateDto.getValue());
										tableValues.put(customAttributeTemplateDto.getKey(), rowValues);
									}
									
									//saving process level table contents
									CustomAttributeValuesTableAdhocDto customAttributeValuesTableAdhocDto = new CustomAttributeValuesTableAdhocDto();
									customAttributeValuesTableAdhocDto.setTaskId(processIdList.get(i));
									customAttributeValuesTableAdhocDto
											.setProcessName(obj.getKey());
									customAttributeValuesTableAdhocDto.setKey(customAttributeTemplateDto.getKey());
									customAttributeValuesTableAdhocDto.setRowNumber(rowNumber);
									customAttributeValuesTableAdhocDto
											.setAttributeValue(customAttributeTemplateDto.getValue());
									customAttributeValuesTableAdhocDto.setDependantOn(processName);
									customAttributeValuesTableAdhocDtos.add(customAttributeValuesTableAdhocDto);
								}
								rowNumber++;
							}
						}
					} else {
						attributeValue.setAttributeValue(obj.getValue());
						if ("ProjectProposalDocumentApproval".equalsIgnoreCase(obj.getProcessName())) {
							if (obj.getKey().equalsIgnoreCase("2693jcccd5da7"))
								attributeValue.setAttributeValue(budgetValues.get("4GYUKI003"));
							else if (obj.getKey().equalsIgnoreCase("h3117e8462i1"))
								attributeValue.setAttributeValue(budgetValues.get("3DE1TY0011"));
							else if (obj.getKey().equalsIgnoreCase("dbaif84h6i2f"))
								attributeValue.setAttributeValue(budgetValues.get("4YULJ59001"));
						}
					}
				} else {
					if (obj.getAttributePath().contains("$")) {
						String[] attr = obj.getAttributePath().split("[$]");
						value = "";
						String s = "";
						for (String str : attr) {
							if (str.contains("{")) {
								s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
								if (attrValue.containsKey(s))
									str = str.replace("{" + s + "}",
											(ServicesUtil.isEmpty(attrValue.get(s))) ? "" : attrValue.get(s));
							}
							value += str;
						}
						attributeValue.setAttributeValue(value);
						if (obj.getDataType().contains(TaskCreationConstant.CALCULATION)) {

							String dataType = obj.getDataType().split("-")[0];
							switch (dataType) {
							case "INPUT NUMERIC":
								try {
									value = EvaluateExpression(attrValue, obj.getAttributePath());
									attributeValue.setAttributeValue(value);
								} catch (Exception e) {
									attributeValue.setAttributeValue("");
									System.err.println("[WBP-Dev]Calculating Expression Failed" + e.getMessage());
								}
								break;

							default:
								attributeValue.setAttributeValue("");
								break;
							}
						}

					} else {
						attributeValue.setAttributeValue(obj.getAttributePath());
					}
				}
				attributeValue.setProcessName(obj.getProcessName());
				if (TaskCreationConstant.PROCESS.equals(obj.getOrigin()))
					attributeValue.setTaskId(processIdList.get(i));
				else
					attributeValue.setTaskId(eventIdList.get(i));

				attrValue.put(attributeValue.getKey(), attributeValue.getAttributeValue());
				customAttributeValues.add(attributeValue);
			}

			CustomAttributeValue attributeValue2 = null;
			System.err.println("Attribute values :" + new Gson().toJson(attrValue));
			System.err.println("Table values :" + new Gson().toJson(tableValues));
			
			for (CustomAttributeTemplateDto customAttributeTemplateDto : taskCustomAttr) {
				attributeValue2 = new CustomAttributeValue();
				attributeValue2.setKey(customAttributeTemplateDto.getKey());

				if (!ServicesUtil.isEmpty(customAttributeTemplateDto.getAttributePath())) {

					if (customAttributeTemplateDto.getAttributePath().contains("$")) {
						String[] attr = customAttributeTemplateDto.getAttributePath().split("[$]");
						value = "";
						String s = "";
						for (String str : attr) {
							if (str.contains("{")) {
								s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
								if (attrValue.containsKey(s))
									str = str.replace("{" + s + "}",
											(ServicesUtil.isEmpty(attrValue.get(s))) ? "" : attrValue.get(s));
								else
									str = "";
							}

							value += str;
						}
						attributeValue2.setAttributeValue(value);
						if (customAttributeTemplateDto.getDataType().contains(TaskCreationConstant.CALCULATION)) {

							String dataType = customAttributeTemplateDto.getDataType().split("-")[0];
							switch (dataType) {
							case "INPUT NUMERIC":
								try {
									value = EvaluateExpression(attrValue,
											customAttributeTemplateDto.getAttributePath());
									attributeValue.setAttributeValue(value);
								} catch (Exception e) {
									attributeValue.setAttributeValue("");
									System.err.println("[WBP-Dev]Calculating Expression Failed" + e.getMessage());
								}
								break;

							default:
								attributeValue.setAttributeValue("");
								break;
							}
						}
						else if (customAttributeTemplateDto.getDataType().equals(TaskCreationConstant.TABLE)) {
							
							if(!ServicesUtil.isEmpty(customAttributeTemplateDto.getTableContents())) {
								for (CustomAttributeTemplateDto rowAttributeTemplateDto : customAttributeTemplateDto
										.getTableContents().get(0).getTableAttributes()) {
									
									String attributePath = rowAttributeTemplateDto.getAttributePath();
									if (attributePath.contains("{")) {
										String key = attributePath.substring(attributePath.indexOf("{") + 1,
												attributePath.indexOf("}"));
										for(int rowNumber = 1; rowNumber <= tableValues.get(key).size() ; rowNumber++) {
											CustomAttributeValuesTableAdhocDto customAttributeValuesTableAdhocDto = new CustomAttributeValuesTableAdhocDto();
											customAttributeValuesTableAdhocDto.setTaskId(eventIdList.get(i));
											customAttributeValuesTableAdhocDto
													.setProcessName(rowAttributeTemplateDto.getProcessName());
											customAttributeValuesTableAdhocDto.setKey(rowAttributeTemplateDto.getKey());
											customAttributeValuesTableAdhocDto.setRowNumber(rowNumber);
											customAttributeValuesTableAdhocDto
													.setAttributeValue(tableValues.get(key).get(rowNumber - 1));
											//giving dependent on as key since task name is not available it will be updated on resubmit
											customAttributeValuesTableAdhocDto.setDependantOn(key);
											customAttributeValuesTableAdhocDtos.add(customAttributeValuesTableAdhocDto);
										}
									}
								}						
							}

//							for (int rowNumber = 1; rowNumber <= rowValues.size(); rowNumber++) {
//
//								if (!ServicesUtil.isEmpty(customAttributeTemplateDto.getTableContents())) {
//									for (CustomAttributeTemplateDto rowAttributeTemplateDto : customAttributeTemplateDto
//											.getTableContents().get(0).getTableAttributes()) {
//										String attributePath = rowAttributeTemplateDto.getAttributePath();
//										if (attributePath.contains("{")) {
//											String key = attributePath.substring(attributePath.indexOf("{") + 1,
//													attributePath.indexOf("}"));
//											CustomAttributeValuesTableAdhocDto customAttributeValuesTableAdhocDto = new CustomAttributeValuesTableAdhocDto();
//											customAttributeValuesTableAdhocDto.setTaskId(eventIdList.get(i));
//											customAttributeValuesTableAdhocDto
//													.setProcessName(rowAttributeTemplateDto.getProcessName());
//											customAttributeValuesTableAdhocDto.setKey(rowAttributeTemplateDto.getKey());
//											customAttributeValuesTableAdhocDto.setRowNumber(rowNumber);
//											customAttributeValuesTableAdhocDto
//													.setAttributeValue(tableValues.get(key).get(rowNumber - 1));
//											//giving dependent on as key since task name is not available it will be updated on resubmit
//											customAttributeValuesTableAdhocDto.setDependantOn(key);
//											customAttributeValuesTableAdhocDtos.add(customAttributeValuesTableAdhocDto);
//										}
//
//									}
//								}
//							}
						}
					} else {
						attributeValue2.setAttributeValue(customAttributeTemplateDto.getAttributePath());
					}

				} else {
					attributeValue2.setAttributeValue("");
				}

				attributeValue2.setProcessName(customAttributeTemplateDto.getProcessName());
				if (TaskCreationConstant.PROCESS.equals(customAttributeTemplateDto.getOrigin()))
					attributeValue2.setTaskId(processIdList.get(i));
				else
					attributeValue2.setTaskId(eventIdList.get(i));

				attrValue.put(attributeValue2.getKey(), attributeValue2.getAttributeValue());
				customAttributeValues.add(attributeValue2);
			}
			i++;

			attrValue.clear();
		}
		System.err.println("Table attribute values : " + new Gson().toJson(customAttributeValuesTableAdhocDtos));
		customAttributeValuesTableAdhocDao.saveOrUpdateTableValues(customAttributeValuesTableAdhocDtos);
		return customAttributeValues;
	}

}
