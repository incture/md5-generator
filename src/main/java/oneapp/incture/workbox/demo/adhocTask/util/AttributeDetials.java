package oneapp.incture.workbox.demo.adhocTask.util;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.CrossConstantDao;
import oneapp.incture.workbox.demo.adhocTask.dao.CustomAttributeTemplateDao;
import oneapp.incture.workbox.demo.adhocTask.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskEventDao;
import oneapp.incture.workbox.demo.adhocTask.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.adhocTask.dto.AdhocActionDto;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributesDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ProcessAttributesDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TableContentDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ValueListDto;
import oneapp.incture.workbox.demo.workflow.dao.TaskTemplateDao;

@Component
public class AttributeDetials {

	@Autowired
	CustomAttributeTemplateDao customAttributeTemplateDao;

	@Autowired
	ProcessConfigDao processConfigDao;

	@Autowired
	TaskTemplateDao taskTemplateDao;

	@Autowired
	UserIDPMappingDao userIdpMappingDao;

	@Autowired
	TaskEventDao taskEventsDao;

	@Autowired
	CrossConstantDao constantDao;

	public AttributesResponseDto fetchDeatils(String processName) {

		AttributesResponseDto attributesResponseDto = null;
		List<ProcessAttributesDto> listOfProcesssAttributes = null;
		ProcessAttributesDto processAttributes = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

		attributesResponseDto = new AttributesResponseDto();
		attributesResponseDto.setResponseMessage(resp);
		try {
			List<Object[]> attributeList = customAttributeTemplateDao.fetchAttributes(processName , "Process");
			List<String> customKeys = taskTemplateDao.geProcessCustomKeys(processName);
			/*
			 * if(ServicesUtil.isEmpty(attributeList)) {
			 * resp.setMessage("No Attributes Fetched");
			 * resp.setStatus(TaskCreationConstant.FAILURE);
			 * resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
			 * attributesResponseDto.setResponseMessage(resp);
			 * attributesResponseDto.setListOfProcesssAttributes(new
			 * ArrayList<ProcessAttributesDto>());
			 * attributesResponseDto.setProcessName(processName); return
			 * attributesResponseDto; }
			 */

			List<CustomAttributeTemplateDto> listOfAttibutes = getAttributeResponseDto(attributeList, processName , false);
			String processType = processConfigDao.getTaskType(processName);
			processAttributes = new ProcessAttributesDto();
			processAttributes.setCustomAttributeTemplateDto(listOfAttibutes);
			listOfProcesssAttributes = new ArrayList<>();
			listOfProcesssAttributes.add(processAttributes);
			attributesResponseDto.setListOfProcesssAttributes(listOfProcesssAttributes);
			attributesResponseDto.setProcessName(processName);
			attributesResponseDto.setType(processType);

			System.err.println("[WBP-Dev][WORKBOX-Task CREATION][GET_ATTRIBUTES] DETAILS :" + listOfAttibutes);
			resp.setMessage("ATTRIBUTES ARE FETCHED");
			resp.setStatus(TaskCreationConstant.SUCCESS);
			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);

			attributesResponseDto.setResponseMessage(resp);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-Task CREATION][GET_ATTRIBUTES] ERROR: " + e.getMessage());

			attributesResponseDto = new AttributesResponseDto();
			attributesResponseDto.setResponseMessage(resp);
			attributesResponseDto.setProcessName(processName);
			attributesResponseDto.setListOfProcesssAttributes(listOfProcesssAttributes);
		}

		return attributesResponseDto;
	}

	private List<CustomAttributeTemplateDto> getAttributeResponseDto(List<Object[]> attributeList, String processName,
			boolean isTableAttr) {

		List<ValueListDto> valueList = null;
		List<CustomAttributeTemplateDto> listOfAttibutes = null;
		CustomAttributeTemplateDto attributeDesc = null;
		Map<String, String> budgetValues = null;
		List<TableContentDto> tableContents = null;
		listOfAttibutes = new ArrayList<CustomAttributeTemplateDto>();
		try {
			if ("ProjectProposalDocumentApproval".equalsIgnoreCase(processName)) {
				budgetValues = constantDao.getPPDAvaibaleBudget();
			}

			if (!isTableAttr) {
				attributeDesc = new CustomAttributeTemplateDto();
				attributeDesc.setDataType("TEXT AREA");
				attributeDesc.setIsMandatory(true);
				attributeDesc.setKey("description");
				attributeDesc.setLabel("Description");
				attributeDesc.setProcessName("STANDARD");
				attributeDesc.setTaskId("");
				attributeDesc.setAttrDes("");
				attributeDesc.setIsActive(true);
				attributeDesc.setIsDeleted(false);
				attributeDesc.setIsEditable(true);
				attributeDesc.setValue("");
				attributeDesc.setIsEdited(0);
				attributeDesc.setDataTypeKey(0);
				listOfAttibutes.add(attributeDesc);
			}
			for (Object[] obj : attributeList) {
				attributeDesc = new CustomAttributeTemplateDto();
				attributeDesc.setProcessName(obj[0].toString());
				attributeDesc.setKey(obj[1].toString());
				attributeDesc.setLabel(obj[2].toString());
				// System.err.println((Short) obj[3] != 0);
				if (!ServicesUtil.isEmpty(obj[3]))
					attributeDesc.setIsActive(ServicesUtil.asBoolean(obj[3]));
				attributeDesc.setIsDeleted(false);
				if (!ServicesUtil.isEmpty(obj[4]))
					attributeDesc.setIsMandatory(ServicesUtil.asBoolean(obj[4]));
				if (!ServicesUtil.isEmpty(obj[5]))
					attributeDesc.setIsEditable(ServicesUtil.asBoolean(obj[5]));
				attributeDesc.setDataType(obj[6].toString());
				if (!ServicesUtil.isEmpty(obj[7]))
					attributeDesc.setAttrDes(obj[7].toString());
				valueList = new ArrayList<ValueListDto>();
				attributeDesc.setValueList(valueList);
				if (!ServicesUtil.isEmpty(obj[11]))
					attributeDesc.setIsRunTime(ServicesUtil.asBoolean(obj[11]));
				else
					attributeDesc.setIsRunTime(false);

				attributeDesc.setDataTypeKey(obj[6].equals(TaskCreationConstant.DROPDOWN) && attributeDesc.getIsRunTime() ? 1 : 0);

				if (attributeDesc.getDataType().equals(TaskCreationConstant.DROPDOWN))
					attributeDesc.setUrl(UrlsForDropdown.getDropdownUrl((String) obj[2], (String) obj[1],
							attributeDesc.getIsRunTime(),
							ServicesUtil.isEmpty(obj[12]) ? PMCConstant.INDIVIDUAL : obj[12].toString()));

				if (attributeDesc.getDataType().equals(TaskCreationConstant.DROPDOWN) && attributeDesc.getIsRunTime())
					attributeDesc.setDropDownType(
							ServicesUtil.isEmpty(obj[12]) ? PMCConstant.INDIVIDUAL : obj[12].toString());

				if (attributeDesc.getIsRunTime() && attributeDesc.getDataType().equals(TaskCreationConstant.DROPDOWN)
						&& "NovoPOCSampleData".equalsIgnoreCase(attributeDesc.getProcessName()))
					attributeDesc.setDropDownType(TaskCreationConstant.GROUP);

				if (attributeDesc.getDataType().equals(TaskCreationConstant.DROPDOWN)
						&& "NovoPOCSampleData".equalsIgnoreCase(attributeDesc.getProcessName()))
					attributeDesc.setUrl("/group/getAllGroup/CUSTOM");

				attributeDesc.setOrigin(ServicesUtil.isEmpty(obj[8]) ? "" : obj[8].toString());
				attributeDesc.setAttributePath(ServicesUtil.isEmpty(obj[9]) ? null : obj[9].toString());
				attributeDesc.setDependantOn(ServicesUtil.isEmpty(obj[10]) ? null : obj[10].toString());

				attributeDesc.setValue(ServicesUtil.isEmpty(obj[13]) ? "" : obj[13].toString());

				// attributeDesc.setDefaultValue(ServicesUtil.isEmpty(obj[13]) ? null :
				// obj[13].toString());
				if ("ProjectProposalDocumentApproval".equalsIgnoreCase(processName)) {
					if (attributeDesc.getKey().equalsIgnoreCase("2693jcccd5da7"))
						attributeDesc.setValue(budgetValues.get("4GYUKI003"));
					else if (attributeDesc.getKey().equalsIgnoreCase("h3117e8462i1"))
						attributeDesc.setValue(budgetValues.get("3DE1TY0011"));
					else if (attributeDesc.getKey().equalsIgnoreCase("dbaif84h6i2f"))
						attributeDesc.setValue(budgetValues.get("4YULJ59001"));
				}

				if (attributeDesc.getDataType().equals(TaskCreationConstant.TABLE)) {
					List<Object[]> tableAttributes = customAttributeTemplateDao.fetchAttributes(attributeDesc.getKey(),
							"Table");
					tableContents = new ArrayList<>();
					TableContentDto tableContentDto = new TableContentDto();
					tableContentDto.setTableAttributes(getAttributeResponseDto(tableAttributes, attributeDesc.getProcessName(), true));
					tableContents.add(tableContentDto);
					attributeDesc.setTableContents(tableContents);
					
				}

				listOfAttibutes.add(attributeDesc);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-PRODUCT][TASK CREATION]ATTRIBUTE SETTING ERROR");
			e.printStackTrace();
		}
		return listOfAttibutes;
	}

	public AttributesResponseDto fetchDraftDetail(String eventId) {

		AttributesResponseDto attributesResponseDto = new AttributesResponseDto();
		List<ProcessAttributesDto> listOfProcesssAttributes = null;
		ProcessAttributesDto processAttributesDto = null;
		List<CustomAttributeTemplateDto> customAttributeTemplateDto = null;
		// String processId = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

		try {
			customAttributeTemplateDto = customAttributeTemplateDao.getDraftDetails(eventId);
			processAttributesDto = new ProcessAttributesDto();
			processAttributesDto.setCustomAttributeTemplateDto(customAttributeTemplateDto);
			listOfProcesssAttributes = new ArrayList<ProcessAttributesDto>();
			listOfProcesssAttributes.add(processAttributesDto);

			attributesResponseDto.setListOfProcesssAttributes(listOfProcesssAttributes);
			System.err.println(customAttributeTemplateDto);
			attributesResponseDto.setIsEdited("1");
			attributesResponseDto.setType("Single Instance");
			attributesResponseDto.setProcessId(taskEventsDao.getProcessId(eventId));
			attributesResponseDto.setProcessName(taskEventsDao.getProcessName(eventId));
			resp.setMessage("Draft task Detail Fetched");
			resp.setStatus(TaskCreationConstant.SUCCESS);
			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][DRAFT VIEW] ERROR: " + e.getMessage());
		}

		attributesResponseDto.setResponseMessage(resp);
		return attributesResponseDto;

	}

	public List<CustomAttributeValue> perpareAttrValueDto(CustomAttributesDto customAttributeDto) {
		List<CustomAttributeValue> customAttributeValues = null;
		CustomAttributeValue customAttributeValue = null;
		try {
			customAttributeValues = new ArrayList<>();
			for (CustomAttributeValue attr : customAttributeDto.getCustomAttributes()) {
				customAttributeValue = new CustomAttributeValue();
				if (!ServicesUtil.isEmpty(customAttributeDto.getEventId())) {
					customAttributeValue.setTaskId(customAttributeDto.getEventId());
					customAttributeValue.setAttributeValue(attr.getAttributeValue());
					customAttributeValue.setKey(attr.getKey());
					// customAttributeValue.setProcessName(attr.getProcessName());
					customAttributeValues.add(customAttributeValue);
				}
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev] ERROR preparing AttributeDto" + e.getMessage());
		}
		return customAttributeValues;
	}

	public ActionDto createActionDto(AdhocActionDto actionDto) {
		ActionDto dto = null;
		ActionDtoChild actionDtoChild = null;
		List<ActionDtoChild> tasks = null;
		try {
			dto = new ActionDto();
			// User user = UserManagementUtil.getLoggedInUser();
			// dto.setUserId(user.getName().toUpperCase());

			dto.setIsChatbot(false);
			UserIDPMappingDto userDeatil = userIdpMappingDao.getUserDetail(actionDto.getUserId());
			dto.setUserDisplay(userDeatil.getUserFirstName() + " " + userDeatil.getUserLastName());
			actionDtoChild = new ActionDtoChild();
			if (PMCConstant.ADMIN.equals(userDeatil.getUserRole()))
				actionDtoChild.setIsAdmin(true);
			else
				actionDtoChild.setIsAdmin(false);
			actionDtoChild.setAction(actionDto.getActionType());
			actionDtoChild.setActionType(actionDto.getActionType());
			actionDtoChild.setComment(actionDto.getComment());
			actionDtoChild.setInstanceId(actionDto.getInstanceId());
			actionDtoChild.setSendToUser(actionDto.getSendTOUser());
			if (ServicesUtil.isEmpty(actionDto.getProcessName())) {
				actionDtoChild.setProcessLabel("ResourcePlanning");
			} else
				actionDtoChild.setProcessLabel(actionDto.getProcessName());

			tasks = new ArrayList<>();
			tasks.add(actionDtoChild);
			dto.setTask(tasks);
		} catch (Exception e) {
			System.err.println("[WBP-Dev] ERROR preparing ActionDto" + e.getMessage());
		}
		return dto;
	}

	public List<CustomAttributeTemplateDto> getNotVisibleAttributeResponseDto(String processName) {

		List<ValueListDto> valueList = null;
		List<CustomAttributeTemplateDto> listOfAttibutes = null;
		CustomAttributeTemplateDto attributeDesc = null;
		Map<String, String> budgetValues = null;
		listOfAttibutes = new ArrayList<CustomAttributeTemplateDto>();
		try {
			if ("ProjectProposalDocumentApproval".equalsIgnoreCase(processName)) {
				budgetValues = constantDao.getPPDAvaibaleBudget();
			}
			List<Object[]> attributeList = customAttributeTemplateDao.fetchNotVisibleAttributes(processName);
			for (Object[] obj : attributeList) {
				attributeDesc = new CustomAttributeTemplateDto();
				attributeDesc.setProcessName(obj[0].toString());
				attributeDesc.setKey(obj[1].toString());
				attributeDesc.setLabel(obj[2].toString());
				if (!ServicesUtil.isEmpty(obj[3]))
					attributeDesc.setIsActive(ServicesUtil.asBoolean(obj[3]));
				attributeDesc.setIsDeleted(false);
				if (!ServicesUtil.isEmpty(obj[4]))
					attributeDesc.setIsMandatory(ServicesUtil.asBoolean(obj[4]));
				if (!ServicesUtil.isEmpty(obj[5]))
					attributeDesc.setIsEditable(ServicesUtil.asBoolean(obj[5]));
				attributeDesc.setDataType(obj[6].toString());
				if (!ServicesUtil.isEmpty(obj[7]))
					attributeDesc.setAttrDes(obj[7].toString());
				valueList = new ArrayList<ValueListDto>();
				attributeDesc.setValueList(valueList);
				if (!ServicesUtil.isEmpty(obj[11]))
					attributeDesc.setIsRunTime(ServicesUtil.asBoolean(obj[11]));
				else
					attributeDesc.setIsRunTime(false);

				attributeDesc.setDataTypeKey(obj[6].equals(TaskCreationConstant.DROPDOWN) && attributeDesc.getIsRunTime() ? 1 : 0);

				if (attributeDesc.getDataType().equals(TaskCreationConstant.DROPDOWN))
					attributeDesc.setUrl(UrlsForDropdown.getDropdownUrl((String) obj[2], (String) obj[1],
							attributeDesc.getIsRunTime(),
							ServicesUtil.isEmpty(obj[12]) ? PMCConstant.INDIVIDUAL : obj[12].toString()));

				if (attributeDesc.getDataType().equals(TaskCreationConstant.DROPDOWN) && attributeDesc.getIsRunTime())
					attributeDesc.setDropDownType(
							ServicesUtil.isEmpty(obj[12]) ? PMCConstant.INDIVIDUAL : obj[12].toString());

				if (attributeDesc.getIsRunTime() && attributeDesc.getDataType().equals(TaskCreationConstant.DROPDOWN)
						&& "NovoPOCSampleData".equalsIgnoreCase(attributeDesc.getProcessName()))
					attributeDesc.setDropDownType(TaskCreationConstant.GROUP);

				if (attributeDesc.getDataType().equals(TaskCreationConstant.DROPDOWN)
						&& "NovoPOCSampleData".equalsIgnoreCase(attributeDesc.getProcessName()))
					attributeDesc.setUrl("/group/getAllGroup/CUSTOM");

				attributeDesc.setOrigin(ServicesUtil.isEmpty(obj[8]) ? "" : obj[8].toString());
				attributeDesc.setAttributePath(ServicesUtil.isEmpty(obj[9]) ? null : obj[9].toString());
				attributeDesc.setDependantOn(ServicesUtil.isEmpty(obj[10]) ? null : obj[10].toString());

				attributeDesc.setValue(ServicesUtil.isEmpty(obj[13]) ? "" : obj[13].toString());

				// attributeDesc.setDefaultValue(ServicesUtil.isEmpty(obj[13]) ? null :
				// obj[13].toString());
				if ("ProjectProposalDocumentApproval".equalsIgnoreCase(processName)) {
					if (attributeDesc.getKey().equalsIgnoreCase("2693jcccd5da7"))
						attributeDesc.setValue(budgetValues.get("4GYUKI003"));
					else if (attributeDesc.getKey().equalsIgnoreCase("h3117e8462i1"))
						attributeDesc.setValue(budgetValues.get("3DE1TY0011"));
					else if (attributeDesc.getKey().equalsIgnoreCase("dbaif84h6i2f"))
						attributeDesc.setValue(budgetValues.get("4YULJ59001"));
				}
				listOfAttibutes.add(attributeDesc);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-PRODUCT][TASK CREATION]ATTRIBUTE SETTING ERROR" + e.getMessage());
		}
		return listOfAttibutes;
	}

}
