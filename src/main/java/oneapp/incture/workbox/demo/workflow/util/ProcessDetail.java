package oneapp.incture.workbox.demo.workflow.util;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dao.ActionTypeConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessTemplateValueDao;
import oneapp.incture.workbox.demo.adapter_base.dao.UserDetailsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.ActionConfigDo;
import oneapp.incture.workbox.demo.adapter_base.entity.ActionTypeConfigDo;
import oneapp.incture.workbox.demo.adapter_base.entity.StatusConfigDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.manageGroup.dao.GroupDao;
import oneapp.incture.workbox.demo.workflow.dao.CrossConstantDao;
import oneapp.incture.workbox.demo.workflow.dao.CustomAttrTemplateDao;
import oneapp.incture.workbox.demo.workflow.dao.CustomAttributeValueDao;
import oneapp.incture.workbox.demo.workflow.dao.OwnerSelectionRuleDao;
import oneapp.incture.workbox.demo.workflow.dao.OwnerSequenceTypeDao;
import oneapp.incture.workbox.demo.workflow.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.workflow.dao.ProcessEventDao;
import oneapp.incture.workbox.demo.workflow.dao.ProcessTemplateDao;
import oneapp.incture.workbox.demo.workflow.dao.RuleDao;
import oneapp.incture.workbox.demo.workflow.dao.StatusConfigDao;
import oneapp.incture.workbox.demo.workflow.dao.TaskEventDao;
import oneapp.incture.workbox.demo.workflow.dao.TaskOwnerDao;
import oneapp.incture.workbox.demo.workflow.dao.TaskTemplateOwnerDao;
import oneapp.incture.workbox.demo.workflow.dto.AdvanceCustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.AdvanceProcessConfigTbDto;
import oneapp.incture.workbox.demo.workflow.dto.AdvanceTeamDetailDto;
import oneapp.incture.workbox.demo.workflow.dto.CrossConstantDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.DropDownRequestDto;
import oneapp.incture.workbox.demo.workflow.dto.OwnerSelectionRuleDto;
import oneapp.incture.workbox.demo.workflow.dto.ProcessConfigTbDto;
import oneapp.incture.workbox.demo.workflow.dto.RuleDto;
import oneapp.incture.workbox.demo.workflow.dto.StatusDto;
import oneapp.incture.workbox.demo.workflow.dto.TaskTemplateDto;
import oneapp.incture.workbox.demo.workflow.dto.TaskTemplateOwnerDto;
import oneapp.incture.workbox.demo.workflow.dto.TeamDetailDto;
import oneapp.incture.workbox.demo.workflow.dto.ValuesDto;
import oneapp.incture.workbox.demo.workflow.dto.WorkflowDto;
import oneapp.incture.workbox.demo.workflow.entity.OwnerSelectionRuleDo;
import oneapp.incture.workbox.demo.workflow.entity.OwnerSequenceTypeDo;
import oneapp.incture.workbox.demo.workflow.services.DropDownResposeDto;


@Component
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
	private CustomAttrTemplateDao customAttrTemplateDao;
	
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
	
	@Autowired
	ActionTypeConfigDao actionTypeConfigDao;
	
	@Autowired
	StatusConfigDao statusConfigDao;
	
	@Autowired
	OwnerSelectionRuleDao ownerSelectionRuleDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	OwnerSequenceTypeDao ownerSequenceTypeDao;
	


	public CustomProcessCreationDto fetchProcessDetail(String processName, String processType) {

		CustomProcessCreationDto customProcessCreationDto = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);

		System.err.println("[WBP-Dev]done");
		ProcessConfigTbDto processDetail = processConfigDao.getProcessDetail(processName, processType);
		if (ServicesUtil.isEmpty(processDetail)) {
			resp.setMessage("Process Does Not Exists");
			customProcessCreationDto = new CustomProcessCreationDto();
			customProcessCreationDto.setResponseMessage(resp);
			return customProcessCreationDto;
		}
		System.err.println("[WBP-Dev]done");
		Map<String,StatusDto> taskStatusConfig = statusConfigDao.getTasksBusinessStatus(processName);
		List<TeamDetailDto> taskDetail = taskTemplateOwnerDao.getTeamDetail(processName,taskStatusConfig);
		List<Object[]> constantDos = crossConstantDao.getAllDropDownValues(processName);
		Map<String, List<ValuesDto>> dropDownValues = createMapDropDownValues(constantDos);
		for (TeamDetailDto teamDetailDto : taskDetail) {
			teamDetailDto.setRules(ruleDao.getRulesById(teamDetailDto.getTemplateId()));
			teamDetailDto.setOwnerSelectionRules(ownerSelectionRuleDao.getRules(teamDetailDto.getProcessName(),teamDetailDto.getEventName()));
			teamDetailDto.setCustomAttributes(
					customAttrTemplateDao.getCustomAttributesIfExists(teamDetailDto.getTemplateId(), dropDownValues,processName));
			teamDetailDto.setOwnerSequenceType(ownerSequenceTypeDao.getOwnerSequence(processName, teamDetailDto.getTemplateId()));
		}
		List<CustomAttributeTemplateDto> attributeDetails = customAttrTemplateDao
				.getCustomAttributesIfExists(processName, dropDownValues,processName);
		System.err.println("[WBP-Dev]done");
		resp.setMessage(WorkflowCreationConstant.SUCCESS);
		resp.setStatus(WorkflowCreationConstant.SUCCESS);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
		customProcessCreationDto = new CustomProcessCreationDto();
		customProcessCreationDto.setCustomAttribute(attributeDetails);
		customProcessCreationDto.setProcessDetail(processDetail);
		customProcessCreationDto.setTeamDetailDto(taskDetail);
		customProcessCreationDto.setResponseMessage(resp);

		return customProcessCreationDto;
	}

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

	public ResponseMessage deleteprocess(List<String> processName) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		try {
			List<String> processList = processConfigDao.validateProcess(processName);
			if (processList.isEmpty()) {
				resp.setMessage("Process Cannot Be Deleted");
				return resp;
			}
			System.err.println("processList"+processList);
			processName.removeAll(processList);
			System.err.println("processName"+processName);
			customAttributeValueDao.deleteInAttrValue(processList);
			taskOwnerDao.deleteInTaskOwner(processList);
			processTemplateValueDao.deleteInProcessTemplateValue(processList);
			processTemplateDao.deleteInProcessTemplate(processList);
			customAttrTemplateDao.deleteInCAT(processList);
			taskEventDao.deleteIntaskEvents(processList);
			processEventDao.deleteInProcessEvents(processList);
			crossConstantDao.deleteCrossConstant(processList);
			processConfigDao.deleteInConfigTb(processList);
			actionTypeConfigDao.deleteInActionConfig(processList);
			statusConfigDao.deleteInStatusConfig(processList);
			ownerSelectionRuleDao.deleteOwnerSelectionRule(processList);
			String message = String.join(",", processList) + " deleted Successfully";
			if(!processName.isEmpty())
				message = message +" \n " +String.join(",", processName) + " cannot be deleted";
			resp.setMessage(message);
			resp.setStatus(WorkflowCreationConstant.SUCCESS);
			resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][PROCESS DELETION]ERROR:" + e.getMessage());
			e.printStackTrace();
		}
		return resp;
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
			CrossConstantDto crossConstantDto = getCrossConstant(customProcessCreation.getProcessDetail().getProcessName());
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
		List<ActionConfigDo> actionConfigDos = null;
		ActionConfigDo actionConfigDo = null;
		StatusConfigDo statusConfig = null;
		List<StatusConfigDo> statusConfigDos = null;
		ProcessTemplateDto processTemplateDto = null;
		List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = null;
		List<ProcessTemplateDto> processTemplateDtos = null;
		List<RuleDto> ruleDtos = new ArrayList<>();;
		StringBuilder ownerId = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		List<CustomAttributeTemplateDto> customAttributeTemplateDtos = new ArrayList<>();
		List<CrossConstantDto> dropDownValues = new ArrayList<>();
		List<OwnerSelectionRuleDo> ownerSelectionRuleDos = new ArrayList<>();
		OwnerSelectionRuleDo ownerSelectionRuleDo = null;
		OwnerSequenceTypeDo ownerSequenceTypeDo = null;
		List<OwnerSequenceTypeDo> ownerSequenceTypeDos = new ArrayList<>();
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
			dropDownValues.add(getCrossConstant(customProcessCreation.getProcessDetail().getProcessName()));
			taskTemplates = new ArrayList<>();
			taskTemplateOwnerDtos = new ArrayList<>();
			processTemplateDtos = new ArrayList<>();
			actionConfigDos = new ArrayList<>();
			Map<String,List<ActionTypeConfigDo>> actionDtos = actionTypeConfigDao.getActionTypeConfig();
			statusConfigDos = new ArrayList<>();
			
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
				if(!ServicesUtil.isEmpty(team.getSourceId()))
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
				if(!ServicesUtil.isEmpty(team.getSourceId())) {
					if(team.getSourceId().contains("startTask")) {
						processTemplateDto.setSourceId(null);
					} else {
						processTemplateDto.setSourceId(String.join(",", team.getSourceId()));
					}
				}			
				if(!ServicesUtil.isEmpty(team.getTargetId())) {
					if(team.getSourceId().contains("endTask")) {
						processTemplateDto.setTargetId(null);
					} else {
						processTemplateDto.setTargetId(String.join(",", team.getTargetId()));
					}
				}	
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
				
				//actions
				if(actionDtos.containsKey(processTemplateDto.getTaskType())){
					for (ActionTypeConfigDo actions : actionDtos.get(processTemplateDto.getTaskType())) {
						actionConfigDo = new ActionConfigDo();
						actionConfigDo.setProcessName(processTemplateDto.getProcessName());
						actionConfigDo.setTaskName(processTemplateDto.getTaskName());
						actionConfigDo.setActions(actions.getActions());
						actionConfigDo.setStatus(actions.getStatus());
						actionConfigDo.setType(actions.getType());
						actionConfigDo.setActionNature(actions.getActionNature());
						actionConfigDos.add(actionConfigDo);
					}
				}
				
				//business status
				if(!ServicesUtil.isEmpty(team.getStatusDto())){
					
					if(!ServicesUtil.isEmpty(team.getStatusDto().getReady())){
						statusConfig = new StatusConfigDo();
						statusConfig.setProcessName(processTemplateDto.getProcessName());
						statusConfig.setTaskName(processTemplateDto.getTaskName());
						statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
						statusConfig.setBusinessStatus(team.getStatusDto().getReady());
						statusConfig.setStatus(WorkflowCreationConstant.READY);
						statusConfigDos.add(statusConfig);
					}
					if(!ServicesUtil.isEmpty(team.getStatusDto().getReserved())){
						statusConfig = new StatusConfigDo();
						statusConfig.setProcessName(processTemplateDto.getProcessName());
						statusConfig.setTaskName(processTemplateDto.getTaskName());
						statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
						statusConfig.setBusinessStatus(team.getStatusDto().getReserved());
						statusConfig.setStatus(WorkflowCreationConstant.RESERVED);
						statusConfigDos.add(statusConfig);
					}
					if(!ServicesUtil.isEmpty(team.getStatusDto().getCompleted())){
						statusConfig = new StatusConfigDo();
						statusConfig.setProcessName(processTemplateDto.getProcessName());
						statusConfig.setTaskName(processTemplateDto.getTaskName());
						statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
						statusConfig.setBusinessStatus(team.getStatusDto().getCompleted());
						statusConfig.setStatus(WorkflowCreationConstant.COMPLETED);
						statusConfigDos.add(statusConfig);
					}
					if(!ServicesUtil.isEmpty(team.getStatusDto().getApprove())){
						statusConfig = new StatusConfigDo();
						statusConfig.setProcessName(processTemplateDto.getProcessName());
						statusConfig.setTaskName(processTemplateDto.getTaskName());
						statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
						statusConfig.setBusinessStatus(team.getStatusDto().getApprove());
						statusConfig.setStatus(WorkflowCreationConstant.APPROVE);
						statusConfigDos.add(statusConfig);
					}
					if(!ServicesUtil.isEmpty(team.getStatusDto().getReject())){
						statusConfig = new StatusConfigDo();
						statusConfig.setProcessName(processTemplateDto.getProcessName());
						statusConfig.setTaskName(processTemplateDto.getTaskName());
						statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
						statusConfig.setBusinessStatus(team.getStatusDto().getReject());
						statusConfig.setStatus(WorkflowCreationConstant.REJECT);
						statusConfigDos.add(statusConfig);
					}
					if(!ServicesUtil.isEmpty(team.getStatusDto().getResolved())){
						statusConfig = new StatusConfigDo();
						statusConfig.setProcessName(processTemplateDto.getProcessName());
						statusConfig.setTaskName(processTemplateDto.getTaskName());
						statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
						statusConfig.setBusinessStatus(team.getStatusDto().getResolved());
						statusConfig.setStatus(WorkflowCreationConstant.RESOLVED);
						statusConfigDos.add(statusConfig);
					}
					if(!ServicesUtil.isEmpty(team.getStatusDto().getDone())){
						statusConfig = new StatusConfigDo();
						statusConfig.setProcessName(processTemplateDto.getProcessName());
						statusConfig.setTaskName(processTemplateDto.getTaskName());
						statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
						statusConfig.setBusinessStatus(team.getStatusDto().getDone());
						statusConfig.setStatus(WorkflowCreationConstant.DONE);
						statusConfigDos.add(statusConfig);
					}
				}
				
				
				if (!ServicesUtil.isEmpty(team.getRules())) {
					
					ruleDtos.addAll(team.getRules());
				}
				
				if(!ServicesUtil.isEmpty(team.getOwnerSelectionRules())){
					for (OwnerSelectionRuleDto rules : team.getOwnerSelectionRules()) {
						ownerSelectionRuleDo = new OwnerSelectionRuleDo();
						ownerSelectionRuleDo.setApprover(String.join(",",rules.getApprover()));
						ownerSelectionRuleDo.setAttribute(rules.getCustom_key());
						ownerSelectionRuleDo.setCondition(rules.getCondition());
						ownerSelectionRuleDo.setProcessName(team.getProcessName().replace(" ", ""));
						ownerSelectionRuleDo.setRuleId(rules.getRuleId());
						ownerSelectionRuleDo.setTaskName(team.getEventName());
						ownerSelectionRuleDo.setAttributeName(rules.getAttributeName());
						ownerSelectionRuleDo.setLogic(rules.getLogic());
						ownerSelectionRuleDo.setValue(rules.getValue());
						ownerSelectionRuleDo.setIsDeleted(rules.getIsDeleted());
						ownerSelectionRuleDos.add(ownerSelectionRuleDo);
					}
				}
				
				//task owner sequence
				if(!ServicesUtil.isEmpty(team.getOwnerSequenceType())) {
					ownerSequenceTypeDo = new OwnerSequenceTypeDo();
					ownerSequenceTypeDo.setOwnerSequType(team.getOwnerSequenceType().getOwnerSequType());
					ownerSequenceTypeDo.setOrderBy(team.getOwnerSequenceType().getOrderBy());
					ownerSequenceTypeDo.setAttrTypeId(team.getOwnerSequenceType().getAttrTypeId());
					ownerSequenceTypeDo.setAttrTypeName(team.getOwnerSequenceType().getAttrTypeName());
					ownerSequenceTypeDo.setProcessName(team.getProcessName().replace(" ", ""));
					ownerSequenceTypeDo.setTemplateId(team.getTemplateId());
					ownerSequenceTypeDos.add(ownerSequenceTypeDo);
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
			workflowDto.setActionConfigDos(actionConfigDos);
			workflowDto.setStatusConfigDos(statusConfigDos);
			workflowDto.setOwnerSelectionRuleDos(ownerSelectionRuleDos);
			workflowDto.setRuleDtos(ruleDtos);
			workflowDto.setOwnerSequenceTypeDos(ownerSequenceTypeDos);

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

	private CrossConstantDto getCrossConstant(String processName) {
		CrossConstantDto constantDto = new CrossConstantDto();
		constantDto.setConstantId("pe.name");
		constantDto.setConstantName(processName.replace(" ", ""));
		constantDto.setConstantValue(processName);
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
	
	private ProcessConfigTbDto getAdvanceProcessConfigResponse(AdvanceProcessConfigTbDto processDetail) {
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
		
//		List<CrossConstantDto> dropDownValues = new ArrayList<>();
		for (TeamDetailDto tasks : updateProcessDto.getTeamDetailDto()) {
			if (tasks.getCustomAttributes() != null)
				updateProcessDto.getCustomAttribute().addAll(tasks.getCustomAttributes());
			
//			for (CustomAttributeTemplateDto attr : tasks.getCustomAttributes()) {
//				if ("DROPDOWN".equals(attr.getDataType()) && !ServicesUtil.isEmpty(attr.getDropDownValues()))
//					dropDownValues.addAll(setDropDownValues(attr.getKey(), attr.getDropDownValues()));
//			}
		}
		//crossConstantDao.saveOrUpdateCrossConstants(dropDownValues);
		return updateProcessDto;
	}

	public WorkflowDto generateAdvanceWorkFlowDto(AdvanceCustomProcessCreationDto customProcessCreation) {
	
		WorkflowDto workflowDto = null;
		List<TaskTemplateDto> taskTemplates = null;
		TaskTemplateDto taskTemplateDto = null;
		List<ActionConfigDo> actionConfigDos = null;
		ActionConfigDo actionConfigDo = null;
		StatusConfigDo statusConfig = null;
		List<StatusConfigDo> statusConfigDos = null;
		ProcessTemplateDto processTemplateDto = null;
		List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = null;
		List<ProcessTemplateDto> processTemplateDtos = null;
		List<RuleDto> ruleDtos = new ArrayList<>();;
		StringBuilder ownerId = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(WorkflowCreationConstant.FAILURE);
		resp.setStatus(WorkflowCreationConstant.FAILURE);
		resp.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		List<CustomAttributeTemplateDto> customAttributeTemplateDtos = new ArrayList<>();
		List<CrossConstantDto> dropDownValues = new ArrayList<>();
		List<OwnerSelectionRuleDo> ownerSelectionRuleDos = new ArrayList<>();
		OwnerSelectionRuleDo ownerSelectionRuleDo = null;
		OwnerSequenceTypeDo ownerSequenceTypeDo = null;
		List<OwnerSequenceTypeDo> ownerSequenceTypeDos = new ArrayList<>();
		
		try {
			
			if (!customProcessCreation.getProcessDetail().getCustomAttributes().isEmpty()
					|| !ServicesUtil.isEmpty(customProcessCreation.getProcessDetail().getCustomAttributes())) {
				for (CustomAttributeTemplateDto attr : customProcessCreation.getProcessDetail().getCustomAttributes()) {
					attr.setProcessName(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""));
					attr.setOrigin(WorkflowCreationConstant.PROCESS);
					if ("DROPDOWN".equals(attr.getDataType()) && !ServicesUtil.isEmpty(attr.getDropDownValues()))
						dropDownValues.addAll(setDropDownValues(attr.getKey(), attr.getDropDownValues()));
				}
			}
			
			customAttributeTemplateDtos.addAll(customProcessCreation.getProcessDetail().getCustomAttributes());
			ProcessConfigTbDto processConfigTbDto = getAdvanceProcessConfigResponse(customProcessCreation.getProcessDetail());
			dropDownValues.add(getCrossConstant(customProcessCreation.getProcessDetail().getProcessName()));
			taskTemplates = new ArrayList<>();
			taskTemplateOwnerDtos = new ArrayList<>();
			processTemplateDtos = new ArrayList<>();
			actionConfigDos = new ArrayList<>();
			Map<String,List<ActionTypeConfigDo>> actionDtos = actionTypeConfigDao.getActionTypeConfig();
			statusConfigDos = new ArrayList<>();
			
			
			for (TeamDetailDto team : customProcessCreation.getTeamDetailDto()) {
				
				
				if(ServicesUtil.isEmpty(team.getIsEdited())) {
					
					ownerId = new StringBuilder("O");
					Random rn = new Random();
					int randRequest = (int) (1000 + rn.nextInt() * (9999 - 1000) + 1);
					ownerId.append(randRequest);

					taskTemplateDto = new TaskTemplateDto();
					processTemplateDto = new ProcessTemplateDto();
					
					if(team.getTemplateId().equals("startTask") || team.getTemplateId().equals("endTask")) {
						
						//setting task template
						taskTemplateDto.setProcessName(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""));
						taskTemplateDto.setTaskId(team.getTemplateId());
						taskTemplateDto.setTaskName(team.getEventName());
						if(!ServicesUtil.isEmpty(team.getSourceId()))
							taskTemplateDto.setSourceId(String.join(",", team.getSourceId()));
						taskTemplates.add(taskTemplateDto);
						
						//setting process template
						processTemplateDto.setProcessName(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""));
						processTemplateDto.setTemplateId(team.getTemplateId());
						processTemplateDto.setTaskName(team.getEventName());
						if(!ServicesUtil.isEmpty(team.getSourceId()))
							processTemplateDto.setSourceId(String.join(",", team.getSourceId()));
						if(!ServicesUtil.isEmpty(team.getTargetId()))
							processTemplateDto.setTargetId(String.join(",", team.getTargetId()));
						processTemplateDtos.add(processTemplateDto);
					}
					else {
						
						taskTemplateDto.setProcessName(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""));
						taskTemplateDto.setTaskId(team.getTemplateId());
						taskTemplateDto.setTaskName(team.getEventName());
						taskTemplateDto.setRunTimeUser((team.getRunTimeUser() == 1) ? true : false);
						if(!ServicesUtil.isEmpty(team.getSourceId()))
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
						processTemplateDto.setProcessName(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""));
						processTemplateDto.setTemplateId(team.getTemplateId());
						processTemplateDto.setTaskName(team.getEventName());
						processTemplateDto.setRunTimeUser((team.getRunTimeUser() == 1) ? true : false);
						if(!ServicesUtil.isEmpty(team.getSourceId()))
							processTemplateDto.setSourceId(String.join(",", team.getSourceId()));
						if(!ServicesUtil.isEmpty(team.getTargetId()))
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
						
						//actions
						if(actionDtos.containsKey(processTemplateDto.getTaskType())){
							for (ActionTypeConfigDo actions : actionDtos.get(processTemplateDto.getTaskType())) {
								actionConfigDo = new ActionConfigDo();
								actionConfigDo.setProcessName(processTemplateDto.getProcessName());
								actionConfigDo.setTaskName(processTemplateDto.getTaskName());
								actionConfigDo.setActions(actions.getActions());
								actionConfigDo.setStatus(actions.getStatus());
								actionConfigDo.setType(actions.getType());
								actionConfigDo.setActionNature(actions.getActionNature());
								actionConfigDos.add(actionConfigDo);
							}
						}
						
						//business status
						if(!ServicesUtil.isEmpty(team.getStatusDto())){
							
							if(!ServicesUtil.isEmpty(team.getStatusDto().getReady())){
								statusConfig = new StatusConfigDo();
								statusConfig.setProcessName(processTemplateDto.getProcessName());
								statusConfig.setTaskName(processTemplateDto.getTaskName());
								statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
								statusConfig.setBusinessStatus(team.getStatusDto().getReady());
								statusConfig.setStatus(WorkflowCreationConstant.READY);
								statusConfigDos.add(statusConfig);
							}
							if(!ServicesUtil.isEmpty(team.getStatusDto().getReserved())){
								statusConfig = new StatusConfigDo();
								statusConfig.setProcessName(processTemplateDto.getProcessName());
								statusConfig.setTaskName(processTemplateDto.getTaskName());
								statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
								statusConfig.setBusinessStatus(team.getStatusDto().getReserved());
								statusConfig.setStatus(WorkflowCreationConstant.RESERVED);
								statusConfigDos.add(statusConfig);
							}
							if(!ServicesUtil.isEmpty(team.getStatusDto().getCompleted())){
								statusConfig = new StatusConfigDo();
								statusConfig.setProcessName(processTemplateDto.getProcessName());
								statusConfig.setTaskName(processTemplateDto.getTaskName());
								statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
								statusConfig.setBusinessStatus(team.getStatusDto().getCompleted());
								statusConfig.setStatus(WorkflowCreationConstant.COMPLETED);
								statusConfigDos.add(statusConfig);
							}
							if(!ServicesUtil.isEmpty(team.getStatusDto().getApprove())){
								statusConfig = new StatusConfigDo();
								statusConfig.setProcessName(processTemplateDto.getProcessName());
								statusConfig.setTaskName(processTemplateDto.getTaskName());
								statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
								statusConfig.setBusinessStatus(team.getStatusDto().getApprove());
								statusConfig.setStatus(WorkflowCreationConstant.APPROVE);
								statusConfigDos.add(statusConfig);
							}
							if(!ServicesUtil.isEmpty(team.getStatusDto().getReject())){
								statusConfig = new StatusConfigDo();
								statusConfig.setProcessName(processTemplateDto.getProcessName());
								statusConfig.setTaskName(processTemplateDto.getTaskName());
								statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
								statusConfig.setBusinessStatus(team.getStatusDto().getReject());
								statusConfig.setStatus(WorkflowCreationConstant.REJECT);
								statusConfigDos.add(statusConfig);
							}
							if(!ServicesUtil.isEmpty(team.getStatusDto().getResolved())){
								statusConfig = new StatusConfigDo();
								statusConfig.setProcessName(processTemplateDto.getProcessName());
								statusConfig.setTaskName(processTemplateDto.getTaskName());
								statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
								statusConfig.setBusinessStatus(team.getStatusDto().getResolved());
								statusConfig.setStatus(WorkflowCreationConstant.RESOLVED);
								statusConfigDos.add(statusConfig);
							}
							if(!ServicesUtil.isEmpty(team.getStatusDto().getDone())){
								statusConfig = new StatusConfigDo();
								statusConfig.setProcessName(processTemplateDto.getProcessName());
								statusConfig.setTaskName(processTemplateDto.getTaskName());
								statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
								statusConfig.setBusinessStatus(team.getStatusDto().getDone());
								statusConfig.setStatus(WorkflowCreationConstant.DONE);
								statusConfigDos.add(statusConfig);
							}
						}
						
						
						if (!ServicesUtil.isEmpty(team.getRules())) {
							
							ruleDtos.addAll(team.getRules());
						}
						
						if(!ServicesUtil.isEmpty(team.getOwnerSelectionRules())){
							for (OwnerSelectionRuleDto rules : team.getOwnerSelectionRules()) {
								ownerSelectionRuleDo = new OwnerSelectionRuleDo();
								ownerSelectionRuleDo.setApprover(String.join(",",rules.getApprover()));
								ownerSelectionRuleDo.setAttribute(rules.getCustom_key());
								ownerSelectionRuleDo.setCondition(rules.getCondition());
								ownerSelectionRuleDo.setProcessName(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""));
								ownerSelectionRuleDo.setRuleId(rules.getRuleId());
								ownerSelectionRuleDo.setTaskName(team.getEventName());
								ownerSelectionRuleDo.setAttributeName(rules.getAttributeName());
								ownerSelectionRuleDo.setLogic(rules.getLogic());
								ownerSelectionRuleDo.setValue(rules.getValue());
								ownerSelectionRuleDos.add(ownerSelectionRuleDo);
							}
						}
						
						//task owner sequence
						if(!ServicesUtil.isEmpty(team.getOwnerSequenceType())) {
							ownerSequenceTypeDo = new OwnerSequenceTypeDo();
							ownerSequenceTypeDo.setOwnerSequType(team.getOwnerSequenceType().getOwnerSequType());
							ownerSequenceTypeDo.setOrderBy(team.getOwnerSequenceType().getOrderBy());
							ownerSequenceTypeDo.setAttrTypeId(team.getOwnerSequenceType().getAttrTypeId());
							ownerSequenceTypeDo.setAttrTypeName(team.getOwnerSequenceType().getAttrTypeName());
							ownerSequenceTypeDo.setProcessName(customProcessCreation.getProcessDetail().getProcessName().replace(" ", ""));
							ownerSequenceTypeDo.setTemplateId(team.getTemplateId());
							ownerSequenceTypeDos.add(ownerSequenceTypeDo);
						}
						
					}
					
				}
				
				else {
					//deleting the task
					Session session = sessionFactory.openSession();
					Transaction tx = session.beginTransaction();
					System.err.println("[WBP-Dev]in delete");
					Query deleteTask = session.createSQLQuery(
							"DELETE FROM PROCESS_TEMPLATE WHERE PROCESS_NAME= :val1" + " AND TEMPLATE_ID= :val2");
					deleteTask.setParameter("val1", team.getProcessName());
					deleteTask.setParameter("val2", team.getTemplateId());

					deleteTask.executeUpdate();
					
					Query deleteAction = session.createSQLQuery(
							"DELETE FROM ACTION_CONFIG WHERE PROCESS_NAME= :val1" + " AND TASK_NAME= :val2");
					deleteAction.setParameter("val1", team.getProcessName());
					deleteAction.setParameter("val2", team.getEventName());

					deleteAction.executeUpdate();
					tx.commit();
					session.close();
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
			workflowDto.setActionConfigDos(actionConfigDos);
			workflowDto.setStatusConfigDos(statusConfigDos);
			workflowDto.setOwnerSelectionRuleDos(ownerSelectionRuleDos);
			workflowDto.setRuleDtos(ruleDtos);
			workflowDto.setOwnerSequenceTypeDos(ownerSequenceTypeDos);
			
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW CREATION][RESPONSE DTO STRUCTURING]ERROR:" + e.getMessage());
			workflowDto = new WorkflowDto();
			workflowDto.setResponseMessage(resp);
		}
		
		return workflowDto;
	}

}
