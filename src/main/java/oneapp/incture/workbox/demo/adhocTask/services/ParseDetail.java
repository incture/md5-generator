package oneapp.incture.workbox.demo.adhocTask.services;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Calendar;
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
import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.StatusConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeValuesTableDto;
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
import oneapp.incture.workbox.demo.adhocTask.dao.TaskTemplateDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskTemplateOwnerDao;
import oneapp.incture.workbox.demo.adhocTask.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.adhocTask.dao.UserRoleDao;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeValuesTableAdhocDto;
import oneapp.incture.workbox.demo.adhocTask.dto.PpdDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ProcessAttributesDto;
import oneapp.incture.workbox.demo.adhocTask.dto.RequestIdDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TableContentDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskCreationDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskValueDto;
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
import oneapp.incture.workbox.demo.workflow.dao.ProcessTemplateDao;
import oneapp.incture.workbox.demo.workflow.dao.RuleDao;
import oneapp.incture.workbox.demo.workflow.dto.OwnerSelectionRuleDto;
import oneapp.incture.workbox.demo.workflow.dto.RuleDto;
import oneapp.incture.workbox.demo.workflow.dto.TaskTemplateOwnerDto;

@Component
public class ParseDetail {

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
	SharepointUploadFile sharepointUploadFile;

	@Autowired
	CrossConstantDao constantDao;

	@Autowired
	AttributeDetials attributeDetials;

	@Autowired
	WorkflowPayloadCreation workflowPayloadCreation;

	@Autowired
	UserRoleDao userRoleDao;

	@Autowired
	OwnerSelectionRuleDao ownerSelectionRuleDao;

	@Autowired
	RuleDao ruleDao;

	@Autowired
	CustomAttributeValuesTableAdhocDao customAttributeValuesTableAdhocDao;
	
	@Autowired
	StatusConfigDao statusConfigDao;
	


	/* Map<String,String> eventOwners; */

//	public TaskCreationDto createSubmitResponse(AttributesResponseDto tasksToSubmit) {
//
//		ResponseMessage resp = new ResponseMessage();
//		resp.setMessage(TaskCreationConstant.FAILURE);
//		resp.setStatus(TaskCreationConstant.FAILURE);
//		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
//
//		TaskCreationDto taskCreationDto = null;
//		List<TaskEventsDto> tasks = null;
//		List<ProcessEventsDto> processes = null;
//		List<CustomAttributeValue> customAttributeValues = null;
//		List<TaskOwnersDto> owners = null;
//		List<TaskAuditDto> auditDtos = null;
//
//		// List<String> eventIdList =
//		// generateEventIdList(tasksToSubmit.getListOfProcesssAttributes().size());
//		List<String> processIdList = generateProcessIdList(tasksToSubmit.getListOfProcesssAttributes().size());
//
//		try {
//			String ownerName = userIDPMappingDao.getOwnerDetailById(tasksToSubmit.getResourceid().split(" ")).get(0)
//					.getTaskOwnername();
//			taskCreationDto = new TaskCreationDto();
//			// taskCreationDto = setTaskValues(tasksToSubmit,processIdList);
//			taskCreationDto = setTaskValuesNew(tasksToSubmit, processIdList);
//			Gson g = new Gson();
//			System.err.println(g.toJson(taskCreationDto));
//			if (TaskCreationConstant.FAILURE.equals(taskCreationDto.getResponseMessage().getStatus()))
//				return taskCreationDto;
//			List<String> eventIdList = generateEventIdList(taskCreationDto.getTaskCount());
//			customAttributeValues = new ArrayList<CustomAttributeValue>();
//			customAttributeValues = setCustomAttributeValue(
//					taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(), eventIdList,
//					processIdList);
//			processes = new ArrayList<ProcessEventsDto>();
//
//			RequestIdDto requestIdDto = processEventDao.getRequestId(tasksToSubmit.getActionType(),
//					tasksToSubmit.getProcessName(), tasksToSubmit.getRequestId());
//
//			processes = setAllProcessEvent(taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(),
//					processIdList, ownerName, tasksToSubmit.getActionType(), requestIdDto,
//					tasksToSubmit.getProcessName());
//
//			// List<TaskTemplateDto> taskTemplateDto =
//			// taskTemplateDao.getTaskDetail(
//			// tasksToSubmit.getProcessName());
//			// List<ProcessTemplateDto> processTemplateDto =
//			// processTemplateDao.getTemplateDetail(
//			// tasksToSubmit.getProcessName());
//			// System.err.println(taskTemplateDto);
//			tasks = new ArrayList<TaskEventsDto>();
//			// tasks = setAllTaskEvent(tasksToSubmit, ownerName,
//			// processIdList,eventIdList,processes,
//			// tasksToSubmit.getActionType(), taskTemplateDto);
//			tasks = setAllTaskEvent(tasksToSubmit, ownerName, processIdList, eventIdList, processes,
//					tasksToSubmit.getActionType(), taskCreationDto, requestIdDto);
//			// Map<String,String> ownerIds =
//			// createFirstOwners(taskCreationDto.getTaskValues());
//			owners = new ArrayList<TaskOwnersDto>();
//			// owners =
//			// setAllTaskOwners(tasksToSubmit,eventIdList,processIdList,ownerIds);
//			owners = setAllTaskOwners(tasksToSubmit, tasks);
//			auditDtos = new ArrayList<TaskAuditDto>();
//			auditDtos = setAllAudit(tasks);
//
//			taskCreationDto.setProcesses(processes);
//			taskCreationDto.setCustomAttributeValues(customAttributeValues);
//			taskCreationDto.setOwners(owners);
//			taskCreationDto.setTasks(tasks);
//			taskCreationDto.setAuditDtos(auditDtos);
//
//			// taskCreationDto.getTaskValues().remove(0);
//
//			resp.setMessage(TaskCreationConstant.SUCCESS);
//			resp.setStatus(TaskCreationConstant.SUCCESS);
//			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
//			taskCreationDto.setResponseMessage(resp);
//
//		} catch (Exception e) {
//			System.err.println("[WBP-Dev][WORKBOX-NEW][PARSE DRAFT REQUEST]ERROR:" + e);
//			e.printStackTrace();
//		}
//		return taskCreationDto;
//	}

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
			customAttributeValues = new ArrayList<CustomAttributeValue>();

			customAttributeValues = setCustomAttributeValue(
					taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(), eventIdList,
					processIdList, taskCustomAttrs, taskCreationDto.getTemplateDtos(), tasksToSubmit.getProcessName());
			processes = new ArrayList<ProcessEventsDto>();

			RequestIdDto requestIdDto = processEventDao.getRequestId(tasksToSubmit.getActionType(),
					tasksToSubmit.getProcessName(), tasksToSubmit.getRequestId());

			System.err.println("request id" + requestIdDto);
			processes = setAllProcessEvent(taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(),
					processIdList, ownerName, tasksToSubmit.getActionType(), requestIdDto,
					tasksToSubmit.getProcessName());

			// List<TaskTemplateDto> taskTemplateDto =
			// taskTemplateDao.getTaskDetail(
			// tasksToSubmit.getProcessName());
			// List<ProcessTemplateDto> processTemplateDto =
			// processTemplateDao.getTemplateDetail(
			// tasksToSubmit.getProcessName());
			// System.err.println(taskTemplateDto);
			tasks = new ArrayList<TaskEventsDto>();
			// tasks = setAllTaskEvent(tasksToSubmit, ownerName,
			// processIdList,eventIdList,processes,
			// tasksToSubmit.getActionType(), taskTemplateDto);
			tasks = setAllTaskEvent(tasksToSubmit, ownerName, processIdList, eventIdList, processes,
					tasksToSubmit.getActionType(), taskCreationDto, requestIdDto);
			// Map<String,String> ownerIds =
			// createFirstOwners(taskCreationDto.getTaskValues());
			owners = new ArrayList<TaskOwnersDto>();
			// owners =
			// setAllTaskOwners(tasksToSubmit,eventIdList,processIdList,ownerIds);
			owners = setAllTaskOwners(tasksToSubmit, tasks);
			auditDtos = new ArrayList<TaskAuditDto>();
			auditDtos = setAllAudit(tasks);

			taskCreationDto.setProcesses(processes);
			taskCreationDto.setCustomAttributeValues(customAttributeValues);
			taskCreationDto.setOwners(owners);
			taskCreationDto.setTasks(tasks);
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

	private PpdDto createUpdateDto(String requestId, List<CustomAttributeValue> customAttributeValues) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<TaskOwnersDto> setAllTaskOwners(AttributesResponseDto tasksToSubmit, List<TaskEventsDto> tasks) {

		List<TaskOwnersDto> ownersDtos = null;
		TaskOwnersDto taskOwnersDto = null;

		ownersDtos = new ArrayList<TaskOwnersDto>();
		for (TaskEventsDto task : tasks) {
			List<TaskOwnersDto> taskOwnerList = taskTemplateOwnerDao.getOwners(task.getOwnerId());// set
			// the
			// query
			// properly
			for (TaskOwnersDto obj : taskOwnerList) {

				taskOwnersDto = new TaskOwnersDto();
				taskOwnersDto.setEnRoute(true);

				taskOwnersDto.setEventId(task.getEventId());
				taskOwnersDto.setTaskOwner(obj.getTaskOwner());
				taskOwnersDto.setIsProcessed(false);
				taskOwnersDto.setIsSubstituted(false);
				taskOwnersDto.setOwnerEmail(obj.getOwnerEmail());
				taskOwnersDto.setTaskOwnerDisplayName(obj.getTaskOwnerDisplayName());
				taskOwnersDto.setGroupId(obj.getGroupId());
				taskOwnersDto.setGroupOwner(obj.getGroupOwner());
				ownersDtos.add(taskOwnersDto);

			}
		}
		return ownersDtos;
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
				taskEventsDto = new TaskEventsDto();
				taskEventsDto.setEventId(eventIdList.get(i));
				taskEventsDto.setName(obj.getTaskName());
				taskEventsDto.setProcessId(process.getKey());
				taskEventsDto.setCreatedBy(tasksToSubmit.getResourceid());
				taskEventsDto.setCurrentProcessor("");
				taskEventsDto.setCurrentProcessorDisplayName("");
				taskEventsDto.setDescription(obj.getSubject() + " - " + requestId);
				taskEventsDto.setOrigin(TaskCreationConstant.TASK_MANAGEMENT_ORIGIN);
				taskEventsDto.setPriority("MEDIUM");
				taskEventsDto.setProcessName(tasksToSubmit.getProcessName());
				taskEventsDto.setSlaDueDate(null);
				if ("Submit".equals(actionType) || "Resubmit".equals(actionType))
					taskEventsDto.setStatus(TaskCreationConstant.NEW);
				else if ("Save".equals(actionType))
					taskEventsDto.setStatus(TaskCreationConstant.DRAFT);
				taskEventsDto.setStatusFlag("0");
				taskEventsDto.setSubject(obj.getDescription());
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

	private List<CustomAttributeValue> setCustomAttributeValue(List<ProcessAttributesDto> listOfProcesssAttributes,
			List<String> eventIdList, List<String> processIdList, List<CustomAttributeTemplateDto> taskCustomAttr,
			Map<String, List<ProcessTemplateValueDto>> templateDtos, String processName) {

		List<CustomAttributeValue> customAttributeValues = null;
		List<AttachmentRequestDto> attachmentList = null;
		AttachmentRequestDto attachmentRequestDto = null;
		CustomAttributeValue attributeValue = null;
		Map<String, String> attrValue = null;
		Map<String, String> budgetValues = null;
		Map<String, List<String>> tableValues = new HashMap<>();
		List<CustomAttributeValuesTableAdhocDto> customAttributeValuesTableAdhocDtos = new ArrayList<CustomAttributeValuesTableAdhocDto>();
		List<String> rowValues = null;
		Integer i = 0;
		String value = "";

		System.err.println("[WBP-Dev]done");
		customAttributeValues = new ArrayList<CustomAttributeValue>();
		// if("ProjectProposalDocumentApproval".equalsIgnoreCase(listOfProcesssAttributes.get(0).getCustomAttributeTemplateDto().get(1).getProcessName())){
		// budgetValues = constantDao.getPPDAvaibaleBudget();
		// }
		for (ProcessAttributesDto attrValues : listOfProcesssAttributes) {
			attrValue = new HashMap<>();
			rowValues = new ArrayList<String>();
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
				// if(obj.getValue().equals(""))
				// {
				// continue;
				// }
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
						DocumentResponseDto responseDto = sharepointUploadFile.uploadFile(attachmentList,attachmentRequestDto.getFileName());
						Gson g = new Gson();
						attributeValue.setAttributeValue(g.toJson(responseDto.getAttachmentIds()));

					} else if (obj.getDataType().equals(TaskCreationConstant.TABLE)) {

						if (!ServicesUtil.isEmpty(obj.getTableContents())) {
							for (TableContentDto tableContent : obj.getTableContents()) {
								List<CustomAttributeTemplateDto> rowAttributes = tableContent.getTableAttributes();
								for (CustomAttributeTemplateDto customAttributeTemplateDto : rowAttributes) {
									if (tableValues.containsKey(customAttributeTemplateDto.getKey())) {
										tableValues.get(customAttributeTemplateDto.getKey())
												.add(customAttributeTemplateDto.getValue());
									} else {
										rowValues.add(customAttributeTemplateDto.getValue());
										tableValues.put(customAttributeTemplateDto.getKey(), rowValues);
									}
								}
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
									value = EvaluateExpression(attrValue ,obj.getAttributePath());
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
			// List<CustomAttributeTemplateDto> taskCustomAttr =
			// customAttributeTemplateDao
			// .getTaskCustomAttributes(processName);
			CustomAttributeValue attributeValue2 = null;
			System.err.println("Attribute values :" + new Gson().toJson(attrValue));
			System.err.println("Table values :" + new Gson().toJson(tableValues));
			// if (!taskCustomAttr.isEmpty()) {
			List<ProcessTemplateValueDto> templateValueDtos = templateDtos.get(processIdList.get(i));
			// source null
			for (ProcessTemplateValueDto processTemplateValueDto : templateValueDtos) {
				taskCustomAttr = customAttributeTemplateDao
						.getTaskCustomAttributesByTemplateId(processTemplateValueDto.getTemplateId());

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
										value = EvaluateExpression(attrValue ,customAttributeTemplateDto.getAttributePath());
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

								for (int rowNumber = 1; rowNumber <= rowValues.size(); rowNumber++) {

									if (!ServicesUtil.isEmpty(customAttributeTemplateDto.getTableContents())) {
//										String dependantOn = customAttributeTemplateDto.getProcessName();
										for (CustomAttributeTemplateDto rowAttributeTemplateDto : customAttributeTemplateDto
												.getTableContents().get(0).getTableAttributes()) {
											String attributePath = rowAttributeTemplateDto.getAttributePath();
											if (attributePath.contains("{")) {
												String key = attributePath.substring(attributePath.indexOf("{") + 1,
														attributePath.indexOf("}"));
												CustomAttributeValuesTableAdhocDto customAttributeValuesTableAdhocDto = new CustomAttributeValuesTableAdhocDto();
												customAttributeValuesTableAdhocDto.setTaskId(eventIdList.get(i));
												customAttributeValuesTableAdhocDto
														.setProcessName(rowAttributeTemplateDto.getProcessName());
//												customAttributeValuesTableAdhocDto
//														.setKey(key);
												customAttributeValuesTableAdhocDto.setKey(rowAttributeTemplateDto.getKey());
												customAttributeValuesTableAdhocDto.setRowNumber(rowNumber);
												customAttributeValuesTableAdhocDto
														.setAttributeValue(tableValues.get(key).get(rowNumber - 1));
												customAttributeValuesTableAdhocDto.setDependantOn(processTemplateValueDto.getTaskName());
												customAttributeValuesTableAdhocDtos
														.add(customAttributeValuesTableAdhocDto);
											}

										}
									}
								}
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
			}

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
				if(value.equals("") && !finalExp.equals("")) {
					finalExp = finalExp.substring(0 ,finalExp.length() - 1);
				}
				else {
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

		List<Object[]> taskOwnerDetailList = taskTemplateDao.getTaskOwnersDetails(tasksToSubmit.getProcessName());
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
					if ((ServicesUtil.asBoolean(taskOwnerDetail[3]))) {

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
				processTemplateValue.setRunTimeUser((ServicesUtil.asBoolean(taskOwnerDetail[3])));
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

		List<Object[]> taskOwnerDetailList = taskTemplateDao.getTaskOwnersDetails(tasksToSubmit.getProcessName());

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
							str = str.replace("{" + s + "}", attrValue.get(s));
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
							str = str.replace("{" + s + "}", attrValue.get(s));
					}
					System.err.println(str);
					subject += str;
				}
				processTemplateValue.setSubject(subject);
				processTemplateValue.setRunTimeUser((ServicesUtil.asBoolean(taskOwnerDetail[3])));
				processTemplateValue
						.setSourceId(ServicesUtil.isEmpty(taskOwnerDetail[8]) ? null : (String) taskOwnerDetail[8]);
				processTemplateValue
						.setTargetId(ServicesUtil.isEmpty(taskOwnerDetail[9]) ? null : (String) taskOwnerDetail[9]);
				processTemplateValue
						.setTemplateId(ServicesUtil.isEmpty(taskOwnerDetail[2]) ? null : (String) taskOwnerDetail[2]);
				processTemplateValue
						.setUrl(ServicesUtil.isEmpty(taskOwnerDetail[10]) ? null : (String) taskOwnerDetail[10]);
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

	private Map<String, String> createFirstOwners(List<TaskValueDto> taskValues) {
		Map<String, String> ownerIds = new LinkedHashMap<>();
		for (TaskValueDto taskValueDto : taskValues) {
			if (ownerIds.containsKey(taskValueDto.getProcessId()))
				continue;
			else
				ownerIds.put(taskValueDto.getProcessId(), taskValueDto.getOwnerId());
		}
		return ownerIds;
	}

	private List<TaskOwnersDto> setAllTaskOwners(AttributesResponseDto tasksToSubmit, List<String> eventIdList,
			List<String> processIdList, Map<String, String> ownerIds) {

		List<TaskOwnersDto> ownersDtos = null;
		TaskOwnersDto taskOwnersDto = null;
		// WebsocketChatConfiguration webChat = null;
		System.err.println(ownerIds);
		Integer count = 0;

		ownersDtos = new ArrayList<TaskOwnersDto>();
		// for (String eventId : eventIdList) {
		for (Map.Entry<String, String> owner : ownerIds.entrySet()) {
			List<TaskOwnersDto> taskOwnerList = taskTemplateOwnerDao.getOwners(owner.getValue());// set
			// the
			// query
			// properly
			for (TaskOwnersDto obj : taskOwnerList) {

				taskOwnersDto = new TaskOwnersDto();
				taskOwnersDto.setEnRoute(true);
				// taskOwnersDto.setEventId(eventId);
				taskOwnersDto.setEventId(eventIdList.get(count));
				taskOwnersDto.setTaskOwner(obj.getTaskOwner());
				taskOwnersDto.setIsProcessed(false);
				taskOwnersDto.setIsSubstituted(false);
				taskOwnersDto.setOwnerEmail(obj.getOwnerEmail());
				taskOwnersDto.setTaskOwnerDisplayName(obj.getTaskOwnerDisplayName());
				taskOwnersDto.setGroupId(obj.getGroupId());
				taskOwnersDto.setGroupOwner(obj.getGroupOwner());
				// webChat = new WebsocketChatConfiguration();
				// webChat.addNewTaskCreated(eventIdList.get(i),obj[1].toString());
				ownersDtos.add(taskOwnersDto);

				/*
				 * if(!ServicesUtil.isEmpty(taskOwnersDto.getGroupOwner())){
				 * if(groupList.contains(taskOwnersDto.getGroupOwner())) continue; else{
				 * groupList.add(taskOwnersDto.getGroupOwner());
				 * if(eventOwners.containsKey(taskOwnersDto.getEventId())) { name =
				 * eventOwners.get(taskOwnersDto.getEventId()); name += ", "; name +=
				 * taskOwnersDto.getGroupOwner();
				 * eventOwners.replace(taskOwnersDto.getEventId(), name); }else{
				 * eventOwners.put(taskOwnersDto.getEventId(), taskOwnersDto.getGroupOwner()); }
				 * } }else{ if(eventOwners.containsKey(taskOwnersDto.getEventId())) { name =
				 * eventOwners.get(taskOwnersDto.getEventId()); name += ", "; name +=
				 * taskOwnersDto.getTaskOwnerDisplayName();
				 * eventOwners.replace(taskOwnersDto.getEventId(), name); }else{
				 * eventOwners.put(taskOwnersDto.getEventId(),
				 * taskOwnersDto.getTaskOwnerDisplayName()); } }
				 * System.err.println(eventOwners);
				 */
			}
			count++;
		}
		return ownersDtos;
	}

	private TaskCreationDto setTaskValues(AttributesResponseDto tasksToSubmit, List<String> processIdList) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(TaskCreationConstant.FAILURE);
		responseMessage.setStatus(TaskCreationConstant.FAILURE);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		TaskCreationDto taskCreationDto = null;
		Integer processIndex = 0;
		List<TaskValueDto> taskValues = null;
		List<TaskTemplateOwnerDto> templateOwnerDtos = null;
		TaskValueDto taskValueDto = null;
		TaskTemplateOwnerDto taskTemplateOwnerDto = null;
		String description = "";

		List<Object[]> taskOwnerDetailList = taskTemplateDao.getTaskOwnersDetails(tasksToSubmit.getProcessName());

		System.err.println(taskOwnerDetailList);
		taskValues = new ArrayList<TaskValueDto>();
		templateOwnerDtos = new ArrayList<TaskTemplateOwnerDto>();
		for (ProcessAttributesDto instanceDetail : tasksToSubmit.getListOfProcesssAttributes()) {

			for (CustomAttributeTemplateDto attribute : instanceDetail.getCustomAttributeTemplateDto()) {
				if (TaskCreationConstant.DESCRIPTION.equals(attribute.getKey())) {
					description = attribute.getValue();
					break;
				}
			}
			for (Object[] taskOwnerDetail : taskOwnerDetailList) {

				taskValueDto = new TaskValueDto();

				if (((Boolean) taskOwnerDetail[3])) {

					for (CustomAttributeTemplateDto attribute : instanceDetail.getCustomAttributeTemplateDto()) {

						if (TaskCreationConstant.DESCRIPTION.equals(attribute.getKey()))
							description = attribute.getValue();

						// if(TaskCreationConstant.DROPDOWN.equals(attribute.getDataType())
						// && attribute.getDataTypeKey().equals(1)
						// &&
						// attribute.getKey().contains((String)taskOwnerDetail[1]+"
						// Owner")){
						// System.err.println("[WBP-Dev]in");
						// if(attribute.getValueList().isEmpty())
						// {
						// taskValueDto.setOwnerId(taskOwnerDetail[0].toString());
						// String ownerNames =
						// taskTemplateOwnerDao.getOwnersInString(taskOwnerDetail[0].toString());
						// attribute.setValue(ownerNames);
						// break;
						// }
						// else{
						// System.err.println("[WBP-Dev]in"+attribute.getValueList());
						// List<TaskTemplateOwnerDto> taskTemplateOwnerDtos =
						// createOwnerId(attribute.getValueList());
						// System.err.println("[WBP-Dev]in"+taskTemplateOwnerDtos);
						// templateOwnerDtos.add(taskTemplateOwnerDto);
						// taskValueDto.setOwnerId(taskTemplateOwnerDtos.get(0).getOwnerId());
						// attribute.setValue(taskTemplateOwnerDao.
						// getOwnersInString(taskTemplateOwnerDtos.get(0).getOwnerId()));
						// break;
						// }
						// }
						if (TaskCreationConstant.DROPDOWN.equals(attribute.getDataType())
								&& attribute.getIsRunTime()
								&& attribute.getKey().equals(taskOwnerDetail[5])) {
							if (attribute.getValueList().isEmpty()) {
								taskCreationDto = new TaskCreationDto();
								taskCreationDto.setResponseMessage(responseMessage);
								return taskCreationDto;
							} else {
								List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = createOwnerId(
										attribute.getValueList());
								System.err.println("[WBP-Dev]in" + taskTemplateOwnerDtos);
								templateOwnerDtos.add(taskTemplateOwnerDto);
								taskValueDto.setOwnerId(taskTemplateOwnerDtos.get(0).getOwnerId());
								attribute.setValue(taskTemplateOwnerDao
										.getOwnersInString(taskTemplateOwnerDtos.get(0).getOwnerId()));
								break;
							}
						}
					}
				} else {
					taskValueDto.setOwnerId(taskOwnerDetail[0].toString());
				}

				taskValueDto.setProcessId(processIdList.get(processIndex));
				taskValueDto.setStepNumber((Integer) taskOwnerDetail[2]);
				taskValueDto.setTaskName((String) taskOwnerDetail[1]);
				taskValueDto.setTaskType((String) taskOwnerDetail[4]);
				taskValueDto.setDescription(description);

				taskValues.add(taskValueDto);
			}

			processIndex++;
		}
		taskCreationDto = new TaskCreationDto();

		taskCreationDto.setTaskValues(taskValues);
		taskCreationDto.setTemplateOwnerDtos(templateOwnerDtos);
		taskCreationDto.setAttributesResponseDto(tasksToSubmit);
		responseMessage.setMessage(TaskCreationConstant.SUCCESS);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
		responseMessage.setStatus(TaskCreationConstant.SUCCESS);
		taskCreationDto.setResponseMessage(responseMessage);
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
			customAttributeValues = setCustomAttributeValue(
					taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(), eventIdList,
					processIdList, taskCustomAttrs);
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
			taskCreationDto.setResponseMessage(resp);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][PARSE DRAFT REQUEST]ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return taskCreationDto;
	}

	public List<TaskEventsDto> setAllTaskEvent(AttributesResponseDto tasksToSave, String ownerName,
			List<String> processIdList, List<String> eventIdList, List<ProcessEventsDto> processes, String actionType,
			List<TaskTemplateDto> taskTemplateDto) {

		// WebsocketChatConfiguration webChat = null;

		List<TaskEventsDto> taskEventsDtos = null;
		TaskEventsDto taskEventsDto = null;
		TimeZoneConvertion timeZoneConvertion = null;
		timeZoneConvertion = new TimeZoneConvertion();
		Integer i = 0;
		Integer count = 0;
		ProcessConfigDto processConfigDto = processConfigDao.getProcessDetail(tasksToSave.getProcessName());

		String taskDescription = ownerName + " created " + processConfigDto.getProcessDisplayName();

		String sla = processConfigDto.getSla();

		taskEventsDtos = new ArrayList<TaskEventsDto>();
		for (ProcessEventsDto obj : processes) {

			count++;
			taskEventsDto = new TaskEventsDto();
			taskEventsDto.setEventId(eventIdList.get(i));
			taskEventsDto.setName(taskTemplateDto.get(0).getTaskName());
			taskEventsDto.setProcessId(processIdList.get(i));
			taskEventsDto.setCreatedBy(tasksToSave.getResourceid());
			taskEventsDto.setCurrentProcessor("");
			taskEventsDto.setCurrentProcessorDisplayName("");
			taskEventsDto.setDescription(obj.getSubject());
			/*
			 * taskEventsDto.setForwardedAt(ServicesUtil. convertAdminFromStringToDate(
			 * timeZoneConvertion.convertToUTC())); taskEventsDto.setForwardedBy(ownerName);
			 */
			taskEventsDto.setOrigin(TaskCreationConstant.TASK_MANAGEMENT_ORIGIN);
			taskEventsDto.setPriority("MEDIUM");
			taskEventsDto.setProcessName(tasksToSave.getProcessName());
			taskEventsDto.setSlaDueDate(null);
			if ("Submit".equals(actionType))
				taskEventsDto.setStatus(TaskCreationConstant.NEW);
			else if ("Save".equals(actionType))
				taskEventsDto.setStatus(TaskCreationConstant.DRAFT);
			taskEventsDto.setStatusFlag("0");
			taskEventsDto.setSubject(taskDescription);
			taskEventsDto.setTaskMode("NULL");
			taskEventsDto.setTaskType(taskTemplateDto.get(0).getTaskType());
			taskEventsDto.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
			taskEventsDto.setCompletedAt(null);
			taskEventsDto.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
			// System.err.println(sla.replace("days", "").trim());
			Calendar cal = Calendar.getInstance();

			cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(sla.trim()));

			taskEventsDto.setSlaDueDate(cal.getTime());
			taskEventsDto.setCompletionDeadLine(cal.getTime());
			if (!ServicesUtil.isEmpty(processConfigDto.getDetailURL()))
				taskEventsDto.setDetailUrl(processConfigDto.getDetailURL() + TaskCreationConstant.URL_REQUEST_PARAM
						+ taskEventsDto.getEventId());
			// webChat = new WebsocketChatConfiguration();
			// try{
			// webChat.addNewTaskCreated(eventIdList.get(i),customAttributeTemplateDtoObject.getResourceid());
			// }catch (Exception e) {
			//
			// System.err.println("[WBP-Dev]TaskEventDao.saveNextTask()"+
			// ":error in task event");
			// }
			taskEventsDtos.add(taskEventsDto);
			i++;
		}

		return taskEventsDtos;
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

		requestIdTemp = requestIdDto.getRequestId().substring(0, 2);
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

	public List<CustomAttributeValue> setCustomAttributeValue(List<ProcessAttributesDto> listOfProcesssAttributes,
			List<String> eventIdList) {

		List<CustomAttributeValue> customAttributeValues = null;
		List<AttachmentRequestDto> attachmentList = null;
		AttachmentRequestDto attachmentRequestDto = null;
		CustomAttributeValue attributeValue = null;
		Integer eventIdCount = 0;

		System.err.println("[WBP-Dev]done");
		customAttributeValues = new ArrayList<CustomAttributeValue>();
		for (ProcessAttributesDto attrValues : listOfProcesssAttributes) {

			for (CustomAttributeTemplateDto obj : attrValues.getCustomAttributeTemplateDto()) {
				if (obj.getProcessName().equals(TaskCreationConstant.STANDARD)) {

					continue;
				}
				// if(obj.getValue().equals(""))
				// {
				// continue;
				// }

				attributeValue = new CustomAttributeValue();

				attributeValue.setKey(obj.getKey());
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
					DocumentResponseDto responseDto = sharepointUploadFile.uploadFile(attachmentList,attachmentRequestDto.getFileName());
					Gson g = new Gson();
					attributeValue.setAttributeValue(g.toJson(responseDto.getAttachmentIds()));

				} else {

					attributeValue.setAttributeValue(obj.getValue());
				}
				attributeValue.setProcessName(obj.getProcessName());
				attributeValue.setTaskId(eventIdList.get(eventIdCount));
				customAttributeValues.add(attributeValue);
			}
			eventIdCount++;
		}
		return customAttributeValues;

	}

	public List<CustomAttributeValue> setCustomAttributeValue(List<ProcessAttributesDto> listOfProcesssAttributes,
			List<String> eventIdList, String type) {

		List<CustomAttributeValue> customAttributeValues = null;
		List<AttachmentRequestDto> attachmentList = null;
		AttachmentRequestDto attachmentRequestDto = null;
		CustomAttributeValue attributeValue = null;
		Integer eventIdCount = 0;

		System.err.println("[WBP-Dev]done");
		customAttributeValues = new ArrayList<CustomAttributeValue>();
		for (ProcessAttributesDto attrValues : listOfProcesssAttributes) {

			for (CustomAttributeTemplateDto obj : attrValues.getCustomAttributeTemplateDto()) {
				if (obj.getProcessName().equals(TaskCreationConstant.STANDARD)) {

					continue;
				}
				// if(obj.getValue().equals(""))
				// {
				// continue;
				// }

				attributeValue = new CustomAttributeValue();

				attributeValue.setKey(obj.getKey());
				if (obj.getDataType().equals(TaskCreationConstant.ATTACHMENT) && !"".equals(obj.getValue())) {
					if (obj.getIsEdited() == 2) {
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
						DocumentResponseDto responseDto = sharepointUploadFile.uploadFile(attachmentList,attachmentRequestDto.getFileName());
						Gson g = new Gson();
						attributeValue.setAttributeValue(g.toJson(responseDto.getAttachmentIds()));
					} else {
						attributeValue.setAttributeValue(obj.getValue());
					}
				} else {

					attributeValue.setAttributeValue(obj.getValue());
				}
				attributeValue.setProcessName(obj.getProcessName());
				attributeValue.setTaskId(eventIdList.get(eventIdCount));
				customAttributeValues.add(attributeValue);
			}
			eventIdCount++;
		}
		return customAttributeValues;

	}

	public TaskCreationDto taskOwnerIdGeneration(AttributesResponseDto tasksToSave) {
		TaskCreationDto taskCreationDto = new TaskCreationDto();
		taskCreationDto.setTemplateOwnerDtos(new ArrayList<>());
		for (ProcessAttributesDto processAttibutes : tasksToSave.getListOfProcesssAttributes()) {

			for (CustomAttributeTemplateDto attributes : processAttibutes.getCustomAttributeTemplateDto()) {
				if (TaskCreationConstant.DROPDOWN.equals(attributes.getDataType())
						&& TaskCreationConstant.RESOURCE.equals(attributes.getDropDownType())
						&& !attributes.getValueList().isEmpty()) {
					List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = createOwnerId(attributes.getValueList());
					attributes.setValue(taskTemplateOwnerDtos.get(0).getOwnerId());
					System.err.println(taskTemplateOwnerDtos);
					taskCreationDto.getTemplateOwnerDtos().addAll(taskTemplateOwnerDtos);
					System.err.println(taskCreationDto);
				}
			}
		}
		taskCreationDto.setAttributesResponseDto(tasksToSave);
		return taskCreationDto;
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
			customAttributeValues = setCustomAttributeValue(
					taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(), eventIdList,
					processIdList, taskCustomAttrs, taskCreationDto.getTemplateDtos(),
					attributesResponseDto.getProcessName());
			processes = new ArrayList<ProcessEventsDto>();
			RequestIdDto requestIdDto = processEventDao.getRequestId("Submit", attributesResponseDto.getProcessName(),
					"");
			processes = setAllProcessEvent(taskCreationDto.getAttributesResponseDto().getListOfProcesssAttributes(),
					processIdList, ownerName, "Submit", requestIdDto, attributesResponseDto.getProcessName());
			tasks = new ArrayList<TaskEventsDto>();
			tasks = setAllTaskEvent(attributesResponseDto, ownerName, processIdList, eventIdList, processes, "Submit",
					taskCreationDto, requestIdDto);
			owners = new ArrayList<TaskOwnersDto>();
			owners = setAllTaskOwners(attributesResponseDto, tasks);

			auditDtos = new ArrayList<TaskAuditDto>();
			auditDtos = setAllAudit(tasks);

			taskCreationDto.setProcesses(processes);
			taskCreationDto.setCustomAttributeValues(customAttributeValues);
			taskCreationDto.setOwners(owners);
			taskCreationDto.setTasks(tasks);
			taskCreationDto.setAuditDtos(auditDtos);

			resp.setMessage(TaskCreationConstant.SUCCESS);
			resp.setStatus(TaskCreationConstant.SUCCESS);
			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
			taskCreationDto.setResponseMessage(resp);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][PARSE DRAFT REQUEST]ERROR:" + e);
		}
		return taskCreationDto;
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

	private List<CustomAttributeValue> setCustomAttributeValue(List<ProcessAttributesDto> listOfProcesssAttributes,
			List<String> eventIdList, List<String> processIdList, List<CustomAttributeTemplateDto> taskCustomAttr) {

		List<CustomAttributeValue> customAttributeValues = null;
		List<AttachmentRequestDto> attachmentList = null;
		AttachmentRequestDto attachmentRequestDto = null;
		CustomAttributeValue attributeValue = null;
		Map<String, String> attrValue = null;
		Map<String, String> budgetValues = null;
		Integer i = 0;
		String value = "";
		String processName = "";

		System.err.println("[WBP-Dev]done");
		customAttributeValues = new ArrayList<CustomAttributeValue>();
		for (ProcessAttributesDto attrValues : listOfProcesssAttributes) {
			attrValue = new HashMap<>();
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
						DocumentResponseDto responseDto =sharepointUploadFile.uploadFile(attachmentList,attachmentRequestDto.getFileName());
						Gson g = new Gson();
						attributeValue.setAttributeValue(g.toJson(responseDto.getAttachmentIds()));

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
									String exp = "";
									if (attr.length == 2) {
										value = attr[1].substring(attr[1].indexOf("{") + 1, attr[1].indexOf("}"));
										attributeValue.setAttributeValue(attrValue.get(value));
									} else {
										for (int j = 1; j < attr.length - 1; j++) {
											if (attr[j].contains("{")) {
												String firstField = "";
												String secondField = "";
												Pattern pattern = Pattern.compile(".*'([^']*)'.*");
												Matcher matcher = null;
												if (exp == "") {
													firstField = attr[j].substring(attr[j].indexOf("{") + 1,
															attr[j].indexOf("}"));
													secondField = attr[j + 1].substring(attr[j + 1].indexOf("{") + 1,
															attr[j + 1].indexOf("}"));
													matcher = pattern.matcher(attr[j]);
													if (matcher.matches()) {
														exp += attrValue.get(firstField) + matcher.group(1)
																+ attrValue.get(secondField);
														System.err.println(exp);
													}
												} else {
													secondField = attr[j + 1].substring(attr[j + 1].indexOf("{") + 1,
															attr[j + 1].indexOf("}"));
													matcher = pattern.matcher(attr[j]);
													if (matcher.matches()) {
														exp += matcher.group(1) + attrValue.get(secondField);
													}
												}
											}
										}

										System.err.println("[WBP-Dev][parseDeatail] expression " + exp);
										value = String.valueOf(new ExpressionBuilder(exp).build().evaluate());
										attributeValue.setAttributeValue(value);
									}
								} catch (Exception e) {
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
			// List<CustomAttributeTemplateDto> taskCustomAttr =
			// customAttributeTemplateDao
			// .getTaskCustomAttributes(processName);
			CustomAttributeValue attributeValue2 = null;
			// if (!taskCustomAttr.isEmpty()) {
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
									String exp = "";
									if (attr.length == 2) {
										value = attr[1].substring(attr[1].indexOf("{") + 1, attr[1].indexOf("}"));
										attributeValue2.setAttributeValue(attrValue.get(value));
									} else {
										for (int j = 1; j < attr.length - 1; j++) {
											if (attr[j].contains("{")) {
												String firstField = "";
												String secondField = "";
												Pattern pattern = Pattern.compile(".*'([^']*)'.*");
												Matcher matcher = null;
												if (exp == "") {
													firstField = attr[j].substring(attr[j].indexOf("{") + 1,
															attr[j].indexOf("}"));
													secondField = attr[j + 1].substring(attr[j + 1].indexOf("{") + 1,
															attr[j + 1].indexOf("}"));
													matcher = pattern.matcher(attr[j]);
													if (matcher.matches()) {
														exp += attrValue.get(firstField) + matcher.group(1)
																+ attrValue.get(secondField);
														System.err.println(exp);
													}
												} else {
													secondField = attr[j + 1].substring(attr[j + 1].indexOf("{") + 1,
															attr[j + 1].indexOf("}"));
													matcher = pattern.matcher(attr[j]);
													if (matcher.matches()) {
														exp += matcher.group(1) + attrValue.get(secondField);
													}
												}
											}
										}

										System.err.println("[WBP-Dev][parseDeatail] expression " + exp);
										value = String.valueOf(new ExpressionBuilder(exp).build().evaluate());
										System.err.println("[WBP-Dev][parseDeatail] value " + value);
										attributeValue2.setAttributeValue(value);
									}
								} catch (Exception e) {
									System.err.println("[WBP-Dev]Calculating Expression Failed" + e.getMessage());
									attributeValue2.setAttributeValue("");
								}
								break;

							default:
								attributeValue2.setAttributeValue("");
								break;
							}
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
			// }
			i++;

			attrValue.clear();
		}

		// check template id ,
		return customAttributeValues;
	}

}
