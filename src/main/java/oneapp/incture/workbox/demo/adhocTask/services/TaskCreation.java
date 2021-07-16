package oneapp.incture.workbox.demo.adhocTask.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.CustomAttributeValueDao;
import oneapp.incture.workbox.demo.adhocTask.dao.ProcessEventDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskEventDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskOwnerDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskTemplateDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskTemplateOwnerDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskValueDao;
import oneapp.incture.workbox.demo.adhocTask.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ProcessAttributesDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskCreationDto;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.adhocTask.util.TasksToCreateDto;

import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.security.um.user.User;

@Component
public class TaskCreation {

	@Autowired
	ProcessEventDao processEventDao;

	@Autowired
	TaskEventDao taskEventDao;

	@Autowired
	TaskValueDao taskValueDao;

	@Autowired
	TaskOwnerDao taskOwnerDao;

	@Autowired
	TaskTemplateDao taskTemplateDao;

	@Autowired
	TaskTemplateOwnerDao taskTemplateOwnerDao;

//	@Autowired
//	ParseDetail parseDetail;
	@Autowired
	CommonParseDetail parseDetail;

	@Autowired
	CustomAttributeValueDao customAttributeValueDao;

	@Autowired
	UserIDPMappingDao userIDPMappingDao;

	
	public TaskCreationDto createTasks(AttributesResponseDto attributesResponseDto ,  Token token) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(TaskCreationConstant.FAILURE);
		responseMessage.setStatus(TaskCreationConstant.FAILURE);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		StringBuilder responseMsg = new StringBuilder();
		TaskCreationDto taskCreationDto = new TaskCreationDto();
		TaskCreationDto taskCreationDto2 = new TaskCreationDto();
		TaskCreationDto taskCreationDto3 = new TaskCreationDto();

		attributesResponseDto = setLoggedInUser(attributesResponseDto,token);
		responseMessage = attributesResponseDto.getResponseMessage();
		if (attributesResponseDto.getResponseMessage().getStatus().equals(TaskCreationConstant.FAILURE)) {
			taskCreationDto.setResponseMessage(attributesResponseDto.getResponseMessage());
			return taskCreationDto;
		}
		switch (attributesResponseDto.getActionType()) {
		case "Submit":
			if (!attributesResponseDto.getListOfProcesssAttributes().isEmpty()) {

				taskCreationDto2 = parseDetail.createSubmitResponse(attributesResponseDto);

				if (taskCreationDto2.getResponseMessage().getStatus().equals(TaskCreationConstant.FAILURE)) {
					responseMessage.setMessage("Task Creation Failed  " + responseMsg);
					//taskCreationDto.setResponseMessage(responseMessage);
					return taskCreationDto;
				}
				taskCreationDto = taskCreationDto2;
				responseMsg.append(" Number of Task Created: " + attributesResponseDto.getListOfProcesssAttributes().size());
			}

			responseMessage.setMessage(responseMsg.toString());

			break;

		case "Resubmit":
			AttributesResponseDto tasksToSave3 = null;
			AttributesResponseDto tasksToSubmit3 = null;

			tasksToSave3 = new AttributesResponseDto();
			tasksToSubmit3 = new AttributesResponseDto();

			if (!attributesResponseDto.getListOfProcesssAttributes().isEmpty()) {

				taskCreationDto3 = parseDetail.createSubmitResponse(attributesResponseDto);

				if (taskCreationDto3.getResponseMessage().getStatus().equals(TaskCreationConstant.FAILURE)) {
					responseMessage.setMessage("Task Creation Failed  " + responseMsg);
					//taskCreationDto.setResponseMessage(responseMessage);
					return taskCreationDto;
				}
				
				taskCreationDto = taskCreationDto3;
				responseMsg.append(" Number of Task Created: " + attributesResponseDto.getListOfProcesssAttributes().size());
			}

			responseMessage.setMessage(responseMsg.toString());

			break;

		case "Save":
			boolean flagNoDescription = false;

			for (ProcessAttributesDto dto : attributesResponseDto.getListOfProcesssAttributes()) {
				for (CustomAttributeTemplateDto catDto : dto.getCustomAttributeTemplateDto()) {
					if (TaskCreationConstant.DESCRIPTION.equals(catDto.getKey())
							&& (catDto.getValue() == null || catDto.getValue().equals(""))) {
						flagNoDescription = true;
						break;
					}
				}
				if (flagNoDescription == true)
					break;
			}

			if (flagNoDescription == true) {
				responseMessage = new ResponseMessage();
				responseMessage.setMessage(" Filling Description is Mandatory for Saving!!");
				responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
				responseMessage.setStatus(TaskCreationConstant.FAILURE);
				taskCreationDto.setResponseMessage(responseMessage);
				return taskCreationDto;
			}

			taskCreationDto = parseDetail.createDraftResponse(attributesResponseDto);

			if (taskCreationDto.getResponseMessage().getStatus().equals(TaskCreationConstant.FAILURE)) {
				//taskCreationDto.setResponseMessage(responseMessage);
				return taskCreationDto;
			}

			responseMessage = taskCreationDto.getResponseMessage();
			responseMessage.setMessage(responseMsg
					.append("Number of Task Saved: " + attributesResponseDto.getListOfProcesssAttributes().size())
					.toString());
			break;

		case "SecondSave":
			if (attributesResponseDto.getIsEdited().equals("2")) {
				responseMessage = customAttributeValueDao.updateCustomAttributeValues(attributesResponseDto);
			} else {
				responseMessage = new ResponseMessage();
				responseMessage.setMessage("No changes are made");
				responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
				responseMessage.setStatus(TaskCreationConstant.SUCCESS);
			}

			break;

		case "SecondSubmit":

			taskCreationDto = parseDetail.createSubmitDraftResponse(attributesResponseDto);

			if (taskCreationDto.getResponseMessage().getStatus().equals(TaskCreationConstant.FAILURE)) {
				//taskCreationDto.setResponseMessage(responseMessage);
				return taskCreationDto;
			}
			responseMessage = taskCreationDto.getResponseMessage();
			responseMessage.setMessage(responseMsg
					.append("Number of Task Created: " + attributesResponseDto.getListOfProcesssAttributes().size())
					.toString());

		}

		taskCreationDto.setResponseMessage(responseMessage);
		return taskCreationDto;
	}

	private AttributesResponseDto setLoggedInUser(AttributesResponseDto attributesResponseDto, Token token) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Failed to get LoggedIn userId");
		responseMessage.setStatus(TaskCreationConstant.FAILURE);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
		attributesResponseDto.setResponseMessage(responseMessage);
		try {
//			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev]user : " + token.getLogonName());
			attributesResponseDto.setResourceid(token.getLogonName());
			for (ProcessAttributesDto taskInstance : attributesResponseDto.getListOfProcesssAttributes()) {
				taskInstance.setResourceId(token.getLogonName());
			}
			attributesResponseDto.setResponseMessage(responseMessage);
			responseMessage.setMessage(TaskCreationConstant.SUCCESS);
			responseMessage.setStatus(TaskCreationConstant.SUCCESS);
			responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
			attributesResponseDto.setResponseMessage(responseMessage);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WBProduct-Dev]ERROR IN FETCHING LOGGEDD-IN ID" + e);

		}
		return attributesResponseDto;
	}

	// private ResponseMessage submitDraft(AttributesResponseDto
	// attributesResponseDto) {
	//
	// ResponseMessage resp = new ResponseMessage();
	// resp.setMessage(TaskCreationConstant.FAILURE);
	// resp.setStatus(TaskCreationConstant.FAILURE);
	// resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
	//
	// try
	// {
	// List<String> processIdList = new ArrayList<String>();
	// processIdList.add(attributesResponseDto.getProcessId());
	// attributesResponseDto =
	// taskValueDao.saveAllTaskValue(attributesResponseDto,processIdList);
	//
	// ProcessDescDto processDescDto =
	// processEventDao.updateProcessEvents(attributesResponseDto);
	//
	// List<TaskTemplateDto> taskTemplateDto =
	// taskTemplateDao.getTaskDetail(attributesResponseDto.getListOfProcesssAttributes().get(0).getCustomAttributeTemplateDto().get(0).getProcessName());
	//
	// List<String> eventIdList = new ArrayList<String>();
	// eventIdList.add(processDescDto.getEventId());
	//
	// taskEventDao.updateTaskEvents(attributesResponseDto,processDescDto.getEventId());
	//
	// taskOwnerDao.saveAllTaskOwner(attributesResponseDto, eventIdList,
	// processIdList,taskTemplateDto.get(0).getOwnerId());
	//
	// customAttributeValueDao.saveCustomAttributeValue(attributesResponseDto.getListOfProcesssAttributes(),
	// eventIdList);
	//
	// resp.setMessage("Task Is Created");
	// resp.setStatus(TaskCreationConstant.SUCCESS);
	// resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
	//
	// }catch (Exception e) {
	// System.err.println("[WBP-Dev][WORKBOX][TASKCREATION]ERROR : SUBMIT
	// DRAFT"+e.getMessage());
	// resp.setMessage("Task Is Not Created");
	// }
	//
	// return resp;
	// }

	// private ResponseMessage submitAllTask(AttributesResponseDto
	// tasksToSubmit) {
	// ResponseMessage resp = new ResponseMessage();
	// resp.setMessage(TaskCreationConstant.FAILURE);
	// resp.setStatus(TaskCreationConstant.FAILURE);
	// resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
	//
	// List<String> eventIdList =
	// generateEventIdList(tasksToSubmit.getListOfProcesssAttributes().size());
	// List<String> processIdList =
	// generateProcessIdList(tasksToSubmit.getListOfProcesssAttributes().size());
	//
	// String ownerName =
	// userIDPMappingDao.getOwnerDetailById(tasksToSubmit.getResourceid().split("
	// ")).get(0).getTaskOwnername();;
	// try{
	//
	// tasksToSubmit =
	// taskValueDao.saveAllTaskValue(tasksToSubmit,processIdList);
	//
	// List<ProcessDescDto> processDescDto =
	// processEventDao.saveAllProcessEvent(
	// tasksToSubmit.getListOfProcesssAttributes(),processIdList,ownerName,
	// tasksToSubmit.getActionType());
	//
	// List<TaskTemplateDto> taskTemplateDto =
	// taskTemplateDao.getTaskDetail(tasksToSubmit.getListOfProcesssAttributes().get(0).getCustomAttributeTemplateDto().get(0).getProcessName());
	//
	// taskEventDao.saveAllTaskEvent(tasksToSubmit, ownerName,
	// processIdList,eventIdList,processDescDto,
	// tasksToSubmit.getActionType(), taskTemplateDto);
	//
	// taskOwnerDao.saveAllTaskOwner(tasksToSubmit,eventIdList,processIdList,taskTemplateDto.get(0).getOwnerId());
	//
	// customAttributeValueDao.saveCustomAttributeValue(tasksToSubmit.getListOfProcesssAttributes(),eventIdList);
	//
	// resp.setMessage(TaskCreationConstant.SUCCESS);
	// resp.setStatus(TaskCreationConstant.SUCCESS);
	// resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
	//
	// }catch (Exception e) {
	// System.err.println("[WBP-Dev][WORKBOX-TASKCREATION]ERROR :
	// "+e.getMessage());
	// resp.setMessage(e.getMessage());
	// resp.setStatus(TaskCreationConstant.FAILURE);
	// resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
	// }
	//
	// return resp;
	// }

	// private ResponseMessage saveAllTasks(AttributesResponseDto tasksToSave) {
	// TaskCreationDto taskCreationDto = null;
	// ResponseMessage resp = new ResponseMessage();
	// resp.setMessage(TaskCreationConstant.FAILURE);
	// resp.setStatus(TaskCreationConstant.FAILURE);
	// resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
	// taskCreationDto = new TaskCreationDto();
	// taskCreationDto.setResponseMessage(resp);
	// List<String> eventIdList =
	// generateEventIdList(tasksToSave.getListOfProcesssAttributes().size());
	// List<String> processIdList =
	// generateProcessIdList(tasksToSave.getListOfProcesssAttributes().size());
	//
	// String ownerName =
	// userIDPMappingDao.getOwnerDetailById(tasksToSave.getResourceid().split("
	// ")).get(0).getTaskOwnername();
	// try{
	// tasksToSave = taskTemplateOwnerDao.taskOwnerIdGeneration(tasksToSave);
	// customAttributeValueDao.saveCustomAttributeValue(tasksToSave.getListOfProcesssAttributes(),eventIdList);
	// List<ProcessDescDto> processDescDto =
	// processEventDao.saveAllProcessEvent(
	// tasksToSave.getListOfProcesssAttributes(),processIdList,ownerName,
	// tasksToSave.getActionType());
	// List<TaskTemplateDto> taskTemplateDto =
	// taskTemplateDao.getTaskDetail(tasksToSave.getListOfProcesssAttributes().get(0).getCustomAttributeTemplateDto().get(0).getProcessName());
	// taskEventDao.saveAllTaskEvent(tasksToSave, ownerName,
	// processIdList,eventIdList,processDescDto,
	// tasksToSave.getActionType(), taskTemplateDto);
	//
	// resp.setMessage(TaskCreationConstant.SUCCESS);
	// resp.setStatus(TaskCreationConstant.SUCCESS);
	// resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
	// taskCreationDto.setResponseMessage(resp);
	//
	// }catch (Exception e) {
	// System.err.println("[WBP-Dev][WORKBOX-TASKCREATION]ERROR :
	// "+e.getMessage());
	// resp.setMessage(e.getMessage());
	// resp.setStatus(TaskCreationConstant.FAILURE);
	// resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
	// }
	//
	// return resp;
	// }

	private TasksToCreateDto getTasksToCreate(AttributesResponseDto attributesResponseDto) {

		TasksToCreateDto tasksToCreateDto = null;
		List<ProcessAttributesDto> listOfProcesssAttributesToSave = null;
		ProcessAttributesDto processAttributesToSaveDto = null;
		List<CustomAttributeTemplateDto> customAttributeTemplateToSaveDto = null;
		AttributesResponseDto tasksToSave = new AttributesResponseDto();
		List<ProcessAttributesDto> listOfProcesssAttributesToSubmit = null;
		ProcessAttributesDto processAttributesToSubmitDto = null;
		List<CustomAttributeTemplateDto> customAttributeTemplateToSubmitDto = null;
		AttributesResponseDto tasksToSubmit = new AttributesResponseDto();

		Boolean flag = true;

		tasksToSave.setActionType("Save");
		tasksToSave.setProcessName(attributesResponseDto.getProcessName());
		tasksToSave.setResourceid(attributesResponseDto.getResourceid());
		tasksToSave.setType(attributesResponseDto.getType());

		tasksToSubmit.setActionType("Submit");
		tasksToSubmit.setProcessName(attributesResponseDto.getProcessName());
		tasksToSubmit.setResourceid(attributesResponseDto.getResourceid());
		tasksToSubmit.setType(attributesResponseDto.getType());

		listOfProcesssAttributesToSave = new ArrayList<ProcessAttributesDto>();
		listOfProcesssAttributesToSubmit = new ArrayList<ProcessAttributesDto>();

		for (ProcessAttributesDto instanceDetail : attributesResponseDto.getListOfProcesssAttributes()) {

			flag = true;

			processAttributesToSaveDto = new ProcessAttributesDto();
			processAttributesToSubmitDto = new ProcessAttributesDto();

			customAttributeTemplateToSaveDto = new ArrayList<CustomAttributeTemplateDto>();
			customAttributeTemplateToSubmitDto = new ArrayList<CustomAttributeTemplateDto>();

			for (CustomAttributeTemplateDto attribute : instanceDetail.getCustomAttributeTemplateDto()) {

				if (attribute.getIsMandatory()) {

					if (attribute.getDataType().equals(TaskCreationConstant.DROPDOWN)) {

						if (TaskCreationConstant.RESOURCE.equals(attribute.getDropDownType())) {
							if (attribute.getValueList() == null || attribute.getValueList().isEmpty()) {
								flag = false;
								break;

							}
						} else {
							System.err.println("inside");
							if (attribute.getValue() == null || attribute.getValue().equals("")) {

								flag = false;
								break;
							}
						}
					} else {

						if (attribute.getValue() == null || attribute.getValue().equals("")) {

							flag = false;
							break;

						}
					}
				}
			}

			if (flag == false) {

				processAttributesToSaveDto.setResourceId(instanceDetail.getResourceId());
				customAttributeTemplateToSaveDto.addAll(instanceDetail.getCustomAttributeTemplateDto());
				processAttributesToSaveDto.setCustomAttributeTemplateDto(customAttributeTemplateToSaveDto);

				listOfProcesssAttributesToSave.add(processAttributesToSaveDto);

			} else {

				processAttributesToSubmitDto.setResourceId(instanceDetail.getResourceId());
				customAttributeTemplateToSubmitDto.addAll(instanceDetail.getCustomAttributeTemplateDto());
				processAttributesToSubmitDto.setCustomAttributeTemplateDto(customAttributeTemplateToSubmitDto);

				listOfProcesssAttributesToSubmit.add(processAttributesToSubmitDto);
			}

		}
		tasksToSave.setListOfProcesssAttributes(listOfProcesssAttributesToSave);
		tasksToSubmit.setListOfProcesssAttributes(listOfProcesssAttributesToSubmit);

		tasksToCreateDto = new TasksToCreateDto();
		tasksToCreateDto.setTasksToSave(tasksToSave);
		tasksToCreateDto.setTasksToSubmit(tasksToSubmit);

		return tasksToCreateDto;
	}

	@SuppressWarnings("unused")
	private ResponseMessage validateSubmitRequest(AttributesResponseDto attributesResponseDto) {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage("Validation is Proper");
		responseMessage.setStatus(TaskCreationConstant.SUCCESS);
		responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);

		if (attributesResponseDto.getResourceid() == null) {

			responseMessage = new ResponseMessage();
			responseMessage.setMessage("Submitter is not mentioned");
			responseMessage.setStatus(TaskCreationConstant.FAILURE);
			responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

			return responseMessage;

		}

		for (ProcessAttributesDto instanceDetail : attributesResponseDto.getListOfProcesssAttributes()) {

			if (instanceDetail.getResourceId() == null) {

				responseMessage = new ResponseMessage();
				responseMessage.setMessage("Submitter is not mentioned");
				responseMessage.setStatus(TaskCreationConstant.FAILURE);
				responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

				return responseMessage;
			}

		}

		return responseMessage;
	}

	/*
	 * public List<NotificationRequest> createNotification(List<TaskEventsDto>
	 * tasks) { List<NotificationRequest> notificationRequests = null;
	 * NotificationRequest notificationRequest = null;
	 * 
	 * try { notificationRequests = new ArrayList<>(); for (TaskEventsDto res :
	 * tasks) { notificationRequest = new NotificationRequest();
	 * notificationRequest.setAction("NEW");
	 * notificationRequest.setEventId(res.getEventId());
	 * notificationRequest.setOrigin(TaskCreationConstant.TASK_MANAGEMENT_ORIGIN);
	 * notificationRequests.add(notificationRequest); } } catch (Exception e) {
	 * System.err.println("[WBP-Dev][WORKBOX-NEW]ADDING NOTIFICATION:" + e); }
	 * return notificationRequests; }
	 */

}
