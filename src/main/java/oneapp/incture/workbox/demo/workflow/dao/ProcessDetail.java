package oneapp.incture.workbox.demo.workflow.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessTemplateValueDao;
import oneapp.incture.workbox.demo.adapter_base.dao.UserDetailsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.manageGroup.dao.GroupDao;
import oneapp.incture.workbox.demo.workflow.dto.CrossConstantDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.DropDownRequestDto;
import oneapp.incture.workbox.demo.workflow.dto.ProcessConfigTbDto;
import oneapp.incture.workbox.demo.workflow.dto.RuleDto;
import oneapp.incture.workbox.demo.workflow.dto.TaskTemplateDto;
import oneapp.incture.workbox.demo.workflow.dto.TaskTemplateOwnerDto;
import oneapp.incture.workbox.demo.workflow.dto.TeamDetailDto;
import oneapp.incture.workbox.demo.workflow.dto.ValuesDto;
import oneapp.incture.workbox.demo.workflow.dto.WorkflowDto;
import oneapp.incture.workbox.demo.workflow.services.DropDownResposeDto;
import oneapp.incture.workbox.demo.workflow.util.WorkflowCreationConstant;


@Component("ProcessDetailDao")
public class ProcessDetail {

	@Autowired
	private ProcessConfigDao processConfigDao;

	@Autowired
	private CustomAttributeValueDao customAttributeValueDao;

	@Autowired
	private TaskOwnerDao taskOwnerDao;

	@Autowired
	private TaskEventDao taskEventDao;

	@Autowired
	private ProcessEventDao processEventDao;

	@Autowired
	private TaskTemplateOwnerDao taskTemplateOwnerDao;
	@Autowired
	private CrossConstantDao crossConstantDao;

	@Autowired
	private UserDetailsDao userDetailsDao;

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private ProcessTemplateDao processTemplateDao;

	@Autowired
	private ProcessTemplateValueDao processTemplateValueDao;

	@Autowired
	RuleDao ruleDao;


	private Map<String, List<ValuesDto>> createMapDropDownValues(List<Object[]> constantDos) {
		Map<String, List<ValuesDto>> dropDownValues = new HashMap<>();
		List<ValuesDto> valuesDtos = null;
		ValuesDto valuesDto = null;
		for (Object[] obj : constantDos) {
			valuesDto = new ValuesDto();
			valuesDto.setIsEdited(0);
			valuesDto.setValue(ServicesUtil.isEmpty(obj[1]) ? "" : obj[1].toString());
			valuesDto.setValueName(ServicesUtil.isEmpty(obj[2]) ? "" : obj[2].toString());
			if (dropDownValues.containsKey(obj[0])) {
				dropDownValues.get(obj[0]).add(valuesDto);
			} else {
				valuesDtos = new ArrayList<>();
				valuesDtos.add(valuesDto);
				dropDownValues.put(obj[0].toString(), valuesDtos);
			}
		}
		return dropDownValues;
	}


	public WorkflowDto generateWorkFlowDto(CustomProcessCreationDto customProcessCreation) {
		WorkflowDto workflowDto = null;
		List<TaskTemplateDto> taskTemplates = null;
		TaskTemplateDto taskTemplateDto = null;
		List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = null;
		StringBuilder ownerId = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		try {
			if (!customProcessCreation.getCustomAttribute().isEmpty()
					|| !ServicesUtil.isEmpty(customProcessCreation.getCustomAttribute())) {
				for (CustomAttributeTemplateDto attr : customProcessCreation.getCustomAttribute()) {
					attr.setProcessName(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""));
				}
			}
			ProcessConfigTbDto processConfigTbDto = getProcessConfigResponse(customProcessCreation.getProcessDetail());
			CrossConstantDto crossConstantDto = getCrossConstant(customProcessCreation.getProcessDetail());
			Integer stepNumber = 0;
			taskTemplates = new ArrayList<>();
			taskTemplateOwnerDtos = new ArrayList<>();
			for (TeamDetailDto team : customProcessCreation.getTeamDetailDto()) {
				stepNumber++;
				ownerId = new StringBuilder("O");
				Random rn = new Random();
				int randRequest = (int) (1000 + rn.nextInt() * (9999 - 1000) + 1);
				ownerId.append(randRequest);

				taskTemplateDto = new TaskTemplateDto();
				taskTemplateDto.setProcessName(team.getProcessName().replace(" ", ""));
				taskTemplateDto.setStepNumber(stepNumber);
				taskTemplateDto.setTaskName(team.getEventName());
				taskTemplateDto.setRunTimeUser((team.getRunTimeUser() == 1) ? true : false);
				if (!taskTemplateDto.getRunTimeUser()) {
					taskTemplateDto.setOwnerId(ownerId.toString());
					taskTemplateOwnerDtos.addAll(createDtoForTaskTemplateOwner(team, ownerId.toString()));
				}
				taskTemplateDto.setTaskType(team.getTaskType());
				taskTemplateDto.setCustomKey(ServicesUtil.isEmpty(team.getCustomKey()) ? null : team.getCustomKey());
				taskTemplates.add(taskTemplateDto);
			}
			resp.setMessage(WorkflowCreationConstant.SUCCESS);
			resp.setStatus(WorkflowCreationConstant.SUCCESS);
			resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
			workflowDto = new WorkflowDto();
			workflowDto.setResponseMessage(resp);
			workflowDto.setAttributes(customProcessCreation.getCustomAttribute());
			workflowDto.setProcessConfigTbDto(processConfigTbDto);
			workflowDto.setTaskTemplates(taskTemplates);
			workflowDto.setTaskTemplateOwnerDtos(taskTemplateOwnerDtos);
			// workflowDto.setCrossConstantDto(crossConstantDto);

		} catch (Exception e) {
			System.err
					.println("[WBP-Dev][WORKBOX][WORKFLOW CREATION][RESPONSE DTO STRUCTURING]ERROR:" + e.getMessage());
			workflowDto = new WorkflowDto();
			workflowDto.setResponseMessage(resp);
		}
		return workflowDto;
	}

	public WorkflowDto generateWorkFlowDtoNew(CustomProcessCreationDto customProcessCreation) {
		WorkflowDto workflowDto = null;
		List<TaskTemplateDto> taskTemplates = null;
		TaskTemplateDto taskTemplateDto = null;
		ProcessTemplateDto processTemplateDto = null;
		List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = null;
		List<ProcessTemplateDto> processTemplateDtos = null;
		List<RuleDto> ruleDtos = null;
		StringBuilder ownerId = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		List<CustomAttributeTemplateDto> customAttributeTemplateDtos = new ArrayList<>();
		List<CrossConstantDto> dropDownValues = new ArrayList<>();
		try {
			if (!customProcessCreation.getCustomAttribute().isEmpty()
					|| !ServicesUtil.isEmpty(customProcessCreation.getCustomAttribute())) {
				for (CustomAttributeTemplateDto attr : customProcessCreation.getCustomAttribute()) {
					attr.setProcessName(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""));
					attr.setOrigin(WorkflowCreationConstant.PROCESS);
					if ("DROPDOWN".equals(attr.getDataType()) && !ServicesUtil.isEmpty(attr.getDropDownValues()))
						dropDownValues.addAll(setDropDownValues(attr.getKey(), attr.getDropDownValues()));
				}
			}
			customAttributeTemplateDtos.addAll(customProcessCreation.getCustomAttribute());
			ProcessConfigTbDto processConfigTbDto = getProcessConfigResponse(customProcessCreation.getProcessDetail());
			dropDownValues.add(getCrossConstant(customProcessCreation.getProcessDetail()));
			taskTemplates = new ArrayList<>();
			taskTemplateOwnerDtos = new ArrayList<>();
			processTemplateDtos = new ArrayList<>();

			for (TeamDetailDto team : customProcessCreation.getTeamDetailDto()) {

				ownerId = new StringBuilder("O");
				Random rn = new Random();
				int randRequest = (int) (1000 + rn.nextInt() * (9999 - 1000) + 1);
				ownerId.append(randRequest);

				taskTemplateDto = new TaskTemplateDto();
				taskTemplateDto.setProcessName(team.getProcessName().replace(" ", ""));
				taskTemplateDto.setTaskId(team.getTemplateId());
				taskTemplateDto.setTaskName(team.getEventName());
				taskTemplateDto.setRunTimeUser((team.getRunTimeUser() == 1) ? true : false);
				taskTemplateDto.setSourceId(String.join(",", team.getSourceId()));
				if (!taskTemplateDto.getRunTimeUser()) {
					taskTemplateDto.setOwnerId(ownerId.toString());

				}
				taskTemplateDto.setTaskNature(team.getTaskNature());
				taskTemplateDto.setTaskType(team.getTaskType());
				taskTemplateDto.setCustomKey(ServicesUtil.isEmpty(team.getCustomKey()) ? null : team.getCustomKey());
				taskTemplates.add(taskTemplateDto);

				if (!ServicesUtil.isEmpty(team.getCustomAttributes())) {

					customAttributeTemplateDtos.addAll(team.getCustomAttributes());
					for (CustomAttributeTemplateDto attr : team.getCustomAttributes()) {
						if ("DROPDOWN".equals(attr.getDataType()) && !ServicesUtil.isEmpty(attr.getDropDownValues()))
							dropDownValues.addAll(setDropDownValues(attr.getKey(), attr.getDropDownValues()));
					}
				}

				// new table Process Template

				processTemplateDto = new ProcessTemplateDto();
				processTemplateDto.setProcessName(team.getProcessName().replace(" ", ""));
				processTemplateDto.setTemplateId(team.getTemplateId());
				processTemplateDto.setTaskName(team.getEventName());
				processTemplateDto.setRunTimeUser((team.getRunTimeUser() == 1) ? true : false);
				processTemplateDto.setSourceId(String.join(",", team.getSourceId()));
				processTemplateDto.setTargetId(String.join(",", team.getTargetId()));
				processTemplateDto.setSubject(team.getSubject());
				processTemplateDto.setDescription(team.getDescription());
				processTemplateDto.setUrl(team.getUrl());
				processTemplateDto.setTaskNature(team.getTaskNature());
				if (!processTemplateDto.getRunTimeUser()) {
					processTemplateDto.setOwnerId(ownerId.toString());
					taskTemplateOwnerDtos.addAll(createDtoForTaskTemplateOwner(team, ownerId.toString()));
				}
				processTemplateDto.setTaskType(team.getTaskType());
				processTemplateDto.setCustomKey(ServicesUtil.isEmpty(team.getCustomKey()) ? null : team.getCustomKey());
				processTemplateDtos.add(processTemplateDto);

				if (!ServicesUtil.isEmpty(team.getRules())) {
					ruleDtos = new ArrayList<>();
					ruleDtos = team.getRules();
				}

			}
			resp.setMessage(WorkflowCreationConstant.SUCCESS);
			resp.setStatus(WorkflowCreationConstant.SUCCESS);
			resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
			workflowDto = new WorkflowDto();
			workflowDto.setResponseMessage(resp);
			workflowDto.setAttributes(customAttributeTemplateDtos);
			workflowDto.setProcessConfigTbDto(processConfigTbDto);
			workflowDto.setTaskTemplates(taskTemplates);
			workflowDto.setProcessTemplateDtos(processTemplateDtos);
			workflowDto.setTaskTemplateOwnerDtos(taskTemplateOwnerDtos);
			workflowDto.setCrossConstantDto(dropDownValues);
			// workflowDto.setRuleDtos(ruleDtos);

		} catch (Exception e) {
			System.err
					.println("[WBP-Dev][WORKBOX][WORKFLOW CREATION][RESPONSE DTO STRUCTURING]ERROR:" + e.getMessage());
			workflowDto = new WorkflowDto();
			workflowDto.setResponseMessage(resp);
		}
		return workflowDto;
	}

	private List<CrossConstantDto> setDropDownValues(String key, List<ValuesDto> dropDownValues) {
		List<CrossConstantDto> crossConstantDtos = new ArrayList<>();
		CrossConstantDto constantDto = null;
		for (ValuesDto values : dropDownValues) {
			constantDto = new CrossConstantDto();
			constantDto.setConstantId(key);
			constantDto.setConstantName(values.getValue());
			constantDto.setConstantValue(values.getValueName());
			crossConstantDtos.add(constantDto);

		}
		return crossConstantDtos;
	}

	private CrossConstantDto getCrossConstant(ProcessConfigTbDto processDetail) {
		CrossConstantDto constantDto = new CrossConstantDto();
		constantDto.setConstantId("pe.name");
		constantDto.setConstantName(processDetail.getProcessName().replace(" ", ""));
		constantDto.setConstantValue(processDetail.getProcessName());
		return constantDto;
	}

	private ProcessConfigTbDto getProcessConfigResponse(ProcessConfigTbDto processDetail) {
		ProcessConfigTbDto processConfigTbDto = null;
		int sla = 0;
		int criticalDate = 0;

		try {
			if (processDetail.getSlaDays() != null)
				sla = 24 * processDetail.getSlaDays();
			if (processDetail.getSlaHours() != null)
				sla = sla + processDetail.getSlaHours();
			if (processDetail.getCriticalDateDays() != null)
				criticalDate = 24 * processDetail.getCriticalDateDays();
			if (processDetail.getCriticalDateHours() != null)
				criticalDate = criticalDate + processDetail.getCriticalDateHours();

			processConfigTbDto = new ProcessConfigTbDto();
			processConfigTbDto.setDescription(processDetail.getDescription());
			processConfigTbDto.setProcessName(processDetail.getProcessName().replace(" ", ""));
			processConfigTbDto.setProcessDisplayName(processDetail.getProcessName());
			processConfigTbDto.setLabelName(processDetail.getProcessName());
			processConfigTbDto.setSla(String.valueOf(sla));
			processConfigTbDto.setCriticalDate(String.valueOf(criticalDate));
			processConfigTbDto.setSubject(processDetail.getDescription());
			processConfigTbDto.setOrigin(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
			processConfigTbDto.setProcessType(processDetail.getProcessType());
			processConfigTbDto.setColor(processDetail.getColor());
			processConfigTbDto.setUrl(processDetail.getUrl());
			List<String> requestidList = processConfigDao.getRequestIdList();
			String[] processName = processDetail.getProcessName().split(" ");
			StringBuilder requestIdToken = new StringBuilder();

			for (int i = 0; i < processName.length; i++) {
				requestIdToken.append(processName[i].charAt(0));
			}
			String processRequestId = new String(requestIdToken).toUpperCase();
			while (requestidList.contains(processRequestId)) {
				processRequestId = processRequestId + 1;
			}
			processConfigTbDto.setProcessRequestId(processRequestId);
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][WORKBOX][WORKFLOW CREATION][PROCESSCONFIG DTO STRUCTURING]ERROR:" + e.getMessage());
		}
		return processConfigTbDto;
	}

	public List<TaskTemplateOwnerDto> createDtoForTaskTemplateOwner(TeamDetailDto team, String ownerId) {
		List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = null;
		TaskTemplateOwnerDto taskTemplateOwner = null;
		try {
			taskTemplateOwnerDtos = new ArrayList<>();
			if (!team.getGroup().isEmpty()) {
				for (String groupId : team.getGroup()) {

					taskTemplateOwner = new TaskTemplateOwnerDto();
					taskTemplateOwner.setId(groupId);
					taskTemplateOwner.setOwnerId(ownerId);
					taskTemplateOwner.setType(WorkflowCreationConstant.GROUP);
					taskTemplateOwner.setIdName(groupDao.getGroupName(groupId));
					taskTemplateOwnerDtos.add(taskTemplateOwner);
				}
			}

			if (!team.getIndividual().isEmpty()) {
				for (String resourceId : team.getIndividual()) {

					taskTemplateOwner = new TaskTemplateOwnerDto();
					taskTemplateOwner.setId(resourceId);
					taskTemplateOwner.setOwnerId(ownerId);
					taskTemplateOwner.setType(WorkflowCreationConstant.INDIVIDUAL);
					taskTemplateOwner.setIdName(userDetailsDao.getUserName(resourceId));
					taskTemplateOwnerDtos.add(taskTemplateOwner);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW CREATION][TASK OWNER TEMPLATE] ERROR" + e.getMessage());
		}
		return taskTemplateOwnerDtos;
	}

	public DropDownResposeDto createCrossConstantDto(DropDownRequestDto dropDownValues) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		DropDownResposeDto downResposeDto = new DropDownResposeDto();
		List<CrossConstantDto> crossConstants = null;
		CrossConstantDto constantDto = null;
		List<String> valuesToRemove = null;
		try {
			if (!ServicesUtil.isEmpty(dropDownValues.getValues())
					&& !ServicesUtil.isEmpty(dropDownValues.getCustomKey())) {
				crossConstants = new ArrayList<>();
				valuesToRemove = new ArrayList<>();
				for (ValuesDto value : dropDownValues.getValues()) {
					if (value.getIsEdited().equals(2)) {
						constantDto = new CrossConstantDto();
						constantDto.setConstantId(dropDownValues.getCustomKey());
						constantDto.setConstantName(value.getValue());
						constantDto.setConstantValue(value.getValueName());
						crossConstants.add(constantDto);
					} else if (value.getIsEdited().equals(3)) {
						valuesToRemove.add(value.getValue());
					}
				}

				downResposeDto.setCrossConstants(crossConstants);
				downResposeDto.setValuesToRemove(valuesToRemove);
				resp.setMessage(WorkflowCreationConstant.SUCCESS);
				resp.setStatus(WorkflowCreationConstant.SUCCESS);
				resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
				downResposeDto.setResposneMessage(resp);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ADD DROPDOWN VALUES ERROR]" + e.getMessage());
		}
		return downResposeDto;
	}

	public CustomProcessCreationDto addTaskLevelCustomAttr(CustomProcessCreationDto updateProcessDto) {

		for (TeamDetailDto tasks : updateProcessDto.getTeamDetailDto()) {
			if (tasks.getCustomAttributes() != null)
				updateProcessDto.getCustomAttribute().addAll(tasks.getCustomAttributes());
		}
		return updateProcessDto;
	}

}
