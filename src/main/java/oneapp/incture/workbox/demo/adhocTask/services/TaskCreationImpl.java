package oneapp.incture.workbox.demo.adhocTask.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessTemplateValueDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskAuditDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValueTableAdhocDo;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.CustomAttributeValueDao;
import oneapp.incture.workbox.demo.adhocTask.dao.ProcessEventDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskEventDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskTemplateOwnerDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskValueDao;
import oneapp.incture.workbox.demo.adhocTask.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.adhocTask.dto.AdhocActionDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ApproverDto;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CrossConstantResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributesDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskCreationDto;
import oneapp.incture.workbox.demo.adhocTask.util.AttributeDetials;
import oneapp.incture.workbox.demo.adhocTask.util.CrossConstantUtil;
import oneapp.incture.workbox.demo.adhocTask.util.ParseResponse;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.emailTemplate.service.EmailTemplateService;

//import oneapp.incture.workbox.demo.ecc.services.EccActionFacade;
//import oneapp.incture.workbox.demo.notification.service.NotificationService;

@Service
//////@Transactional
@EnableAsync
public class TaskCreationImpl implements TaskCreationService {

	@Autowired
	AttributeDetials attributeDetials;

	@Autowired
	UserIDPMappingDao userIdpMappingDao;

	@Autowired
	TaskCreation taskCreation;

	@Autowired
	ProcessEventDao processEventDao;

	@Autowired
	TaskEventDao taskEventDao;

	@Autowired
	ProcessEventsDao processEventsDao;
	@Autowired
	TaskEventsDao taskEventsDao;
	@Autowired
	TaskOwnersDao taskOwnersDao;
	@Autowired
	CustomAttributeDao customAttrDao;
	@Autowired
	TaskValueDao taskValue;
	@Autowired
	TaskTemplateOwnerDao taskTemplateOwnerDao;
	@Autowired
	TaskAuditDao taskAuditDao;
	
	@Autowired
	EmailTemplateService emailTemplateService;

//	@Autowired
//	ActionFacade adhocActionFacade;

	@Autowired
	CustomAttributeValueDao customAttributeValueDao;
//	@Autowired
//	NotificationService notificationService;

	@Autowired
	CrossConstantUtil crossConstantUtil;

	@Autowired
	ProcessTemplateValueDao processTemplateValueDao;
	
//	@Autowired
//	EccActionFacade eccActionFacade;

	@Override
	public AttributesResponseDto getProcessAttributes(String processName) {
		return attributeDetials.fetchDeatils(processName);
	}

	@Override
	public ResponseMessage createTasks(AttributesResponseDto attributesResponseDto,Token token) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(TaskCreationConstant.FAILURE);
		responseMessage.setStatus(TaskCreationConstant.FAILURE);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		long start = System.currentTimeMillis();
		try {
			
			TaskCreationDto response = taskCreation.createTasks(attributesResponseDto,token);
			Gson g = new Gson();
			System.err.println("[WBP-Dev][WORKBOX-NEW][TASK CREATION] PAYLOAD: " + g.toJson(response));
			responseMessage.setMessage(g.toJson(response));
			start = System.currentTimeMillis();

			System.err.println(response.getProcesses());
			processEventsDao.saveOrUpdateProcesses(response.getProcesses());
			System.err.println(
					"[WBP-Dev][WORKBOX- TASK CREATION][saveOrUpdateProcesses]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskEventsDao.saveOrUpdateTasks(response.getTasks());
			System.err.println(
					"[WBP-Dev][WORKBOX- TASK CREATION][saveOrUpdateTasks]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskOwnersDao.saveOrUpdateOwners(response.getOwners());
			System.err.println(
					"[WBP-Dev][WORKBOX- TASK CREATION][saveOrUpdateOwners]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			customAttrDao.addCustomAttributeValue(response.getCustomAttributeValues());
			System.err.println(
					"[WBP-Dev][WORKBOX- TASK CREATION][saveOrUpdateAttributes]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			// taskTemplateOwnerDao.saveOrUpdateTaskTemplateOwners(response.getTemplateOwnerDtos());
			// System.err.println("[WBP-Dev][WORKBOX- TASK
			// CREATION][addCustomAttributeValue]" + (System.currentTimeMillis()
			// - start));
			// start = System.currentTimeMillis();

			// taskValue.saveOrUpdateTaskValues(response.getTaskValues());
			processTemplateValueDao.saveOrUpdateProcessTemplateValue(response.getProcessTemplateValues());
			System.err.println(
					"[WBP-Dev][WORKBOX- TASK CREATION][saveOrUpdateTaskValues]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskAuditDao.saveOrUpdateAudits(response.getAuditDtos());
			System.err.println(
					"[WBP-Dev][WORKBOX- TASK CREATION][saveOrUpdateTaskAudits]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			List<TaskEventsDto> taskTto = response.getTasks();
			responseMessage = response.getResponseMessage();
			if (!ServicesUtil.isEmpty(taskTto) && taskTto.size() > 0) {
				for (TaskEventsDto taskEventsDto : taskTto) {
					if ("DummyRMG".equalsIgnoreCase(taskEventsDto.getName())) {
						AdhocActionDto actionDto = new AdhocActionDto();
						actionDto.setActionType("Done");
						actionDto.setComment("dummy task");
						actionDto.setUserId("P000006");
						actionDto.setInstanceId(taskEventsDto.getEventId());
						actionDto.setProcessName(taskEventsDto.getProcessName());
						responseMessage.setMessage(actionOnAdhoc(actionDto).getMessage());
					}
				}
			}
			emailTemplateService.sendAllMails(response.getTasks());
//			List<NotificationRequest> notificationRequests = taskCreation.createNotification(response.getTasks());
//			notificationService.saveNotification(notificationRequests);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][TASK CREATION IMPL]ERROR" + e.getMessage());
			e.printStackTrace();
		}

		return responseMessage;

	}

	@Override
	public ResponseMessage deleteDraft(String processId, String eventId) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

		resp = processEventDao.deleteDraftProcess(processId);
		if (resp.getStatus().equals(TaskCreationConstant.FAILURE))
			return resp;
		resp = taskEventDao.deleteDraftTask(processId);
		if (resp.getStatus().equals(TaskCreationConstant.FAILURE))
			return resp;
		resp = customAttributeValueDao.deleteDraftAttributes(eventId);
		if (resp.getStatus().equals(TaskCreationConstant.FAILURE))
			return resp;

		resp.setMessage("Draft is Deleted");
		resp.setStatus(TaskCreationConstant.SUCCESS);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
		return resp;
	}

	@Override
	public AttributesResponseDto viewDraft(String eventId) {

		return attributeDetials.fetchDraftDetail(eventId);

	}

	@Override
	public CrossConstantResponseDto getConstants(String name) {

		return crossConstantUtil.fetchConstants(name);
	}

	@Override
	public ResponseMessage updateTaskAttributes(CustomAttributesDto customAttributeDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		try {

			List<CustomAttributeValue> customAttributes = attributeDetials.perpareAttrValueDto(customAttributeDto);

			// customAttrDao.addCustomAttributeValue(customAttributes);

			customAttrDao.updateCustomAttributes(customAttributes);

			resp.setMessage("Task Attributes Updated");
			resp.setStatus(TaskCreationConstant.SUCCESS);
			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);

		} catch (Exception e) {
			System.err.println("[WBP-Dev]ERROR updating Custom Attributes" + e.getMessage());
		}
		return resp;
	}

	@Override
	public ResponseMessage actionOnAdhoc(AdhocActionDto actionDto) {
		ResponseMessage resp = new ResponseMessage();
		ParseResponse response = new ParseResponse();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		System.err.println("RMS actionDto"+actionDto);
		try {
			Integer count = 0;
			String message = "";
			ActionDto dto = attributeDetials.createActionDto(actionDto);

			if (!ServicesUtil.isEmpty(dto.getTask().get(0).getActionType())) {
				if (dto.getTask().get(0).getComment() == null)
					dto.getTask().get(0).setComment("");
				if (dto.getTask().get(0).getActionType() == null)
					return new ResponseMessage(PMCConstant.FAILURE, "", "Action Type is Null");

				addCustomAttributeForAdhoc(actionDto.getAttrValues());

//				response = adhocActionFacade.actionOnTask(dto.getTask().get(0), dto);
				taskEventsDao.saveOrUpdateTasks(response.getTasks());
				taskOwnersDao.saveOrUpdateOwners(response.getOwners());
				customAttrDao.addCustomAttributeValue(response.getCustomAttributeValues());
				customAttrDao.addCustomAttributeValueTable(response.getCustomTableAttributeValues());
				taskAuditDao.saveOrUpdateAudits(response.getAuditDtos());
				resp = response.getResponseMessage();

				message = resp.getMessage();
			}

			if (PMCConstant.STATUS_SUCCESS.equals(resp.getStatus())) {
				count++;
				try {
					taskAuditDao.saveOrUpdateAudit(new TaskAuditDto(UUID.randomUUID().toString().replaceAll("-", ""),
							dto.getTask().get(0).getInstanceId(), dto.getUserId(), dto.getUserDisplay(),
							dto.getTask().get(0).getComment(), dto.getTask().get(0).getActionType(), new Date(),
							(ServicesUtil.isEmpty(dto.getTask().get(0).getSendToUser()) ? null
									: dto.getTask().get(0).getSendToUser()),
							(ServicesUtil.isEmpty(dto.getTask().get(0).getSendToUser()) ? null
									: userIdpMappingDao.getUserName(dto.getTask().get(0).getSendToUser()))));
				} catch (Exception e) {
					System.err.println("[WBP-Dev]Error Adding Audit" + e.getMessage());
				}
			}

			if (count != 0) {
				if (count == 1)
					resp.setMessage("Task " + message);
				else
					resp.setMessage(count + " Tasks " + message);
				if (!ServicesUtil.isEmpty(response.getTasks())) {
					TaskEventsDto dto2 = response.getTasks().get(0);
					resp.setMessage(dto2.getEventId());
				}
			}
			return resp;
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Error In Actions" + e.getMessage());
		}
		return resp;
	}

	public void addCustomAttributeForAdhoc(List<CustomAttributeTemplateDto> attributeTemplateDto) {
		List<CustomAttributeValueTableAdhocDo> tableList = new ArrayList<>();
		List<CustomAttributeValue> valueList = new ArrayList<>();
		if (!ServicesUtil.isEmpty(attributeTemplateDto)) {
			System.err.println("attributeTemplateDto"+attributeTemplateDto);
			for (CustomAttributeTemplateDto customAttributeTemplateDto : attributeTemplateDto) {

				CustomAttributeValue attributeValue = new CustomAttributeValue();
				attributeValue.setAttributeValue(customAttributeTemplateDto.getValue());
				attributeValue.setKey(customAttributeTemplateDto.getKey());
				attributeValue.setProcessName(customAttributeTemplateDto.getProcessName());
				attributeValue.setTaskId(customAttributeTemplateDto.getTaskId());
				valueList.add(attributeValue);

				if (!ServicesUtil.isEmpty(customAttributeTemplateDto.getTableAttributes())) {
					for (CustomAttributeTemplateDto catTableDto : customAttributeTemplateDto.getTableAttributes()) {
						CustomAttributeValueTableAdhocDo tableValue = new CustomAttributeValueTableAdhocDo();
						tableValue.setAttributeValue(catTableDto.getValue());
						tableValue.setKey(catTableDto.getKey());
						tableValue.setProcessName(catTableDto.getProcessName());
						tableValue.setTaskId(catTableDto.getTaskId());
						tableValue.setRowNumber(catTableDto.getRowNumber());
						tableValue.setDependantOn(catTableDto.getDependantOn());
						tableList.add(tableValue);
					}

				}

			}
			try {
				System.err.println("RMG tableList"+tableList);
				customAttrDao.addCustomAttributeValue(valueList);
				customAttrDao.addCustomAttributeValueTable(tableList);
			} catch (Exception e) {
				System.err.println("TaskCreationImpl.addCustomAttributeForAdhoc()" + e.getMessage());
				e.printStackTrace();

			}
		}

	}

	@Override
	public ApproverDto getApprover() {
		String approver = "";
		ApproverDto approverDto = null;
		try{
			approver = customAttrDao.getCFAApprover();
			approverDto = new ApproverDto();
			approverDto.setName(approver);
			
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Error for Approver"+e);
		}
		return approverDto;
	}

}
