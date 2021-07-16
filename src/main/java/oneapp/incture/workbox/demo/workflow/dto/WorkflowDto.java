package oneapp.incture.workbox.demo.workflow.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.ActionConfigDo;
import oneapp.incture.workbox.demo.adapter_base.entity.StatusConfigDo;
import oneapp.incture.workbox.demo.workflow.entity.OwnerSelectionRuleDo;
import oneapp.incture.workbox.demo.workflow.entity.OwnerSequenceTypeDo;

public class WorkflowDto {

	List<CustomAttributeTemplateDto> attributes;
	List<TaskTemplateDto> taskTemplates;
	ProcessConfigTbDto processConfigTbDto;
	List<TaskTemplateOwnerDto> taskTemplateOwnerDtos;
	List<ProcessTemplateDto> processTemplateDtos;
	List<CrossConstantDto> crossConstantDto;
	ResponseMessage responseMessage;
	List<RuleDto> ruleDtos;
	List<ActionConfigDo> actionConfigDos;
	List<StatusConfigDo> statusConfigDos;
	List<OwnerSelectionRuleDo> ownerSelectionRuleDos;
	List<OwnerSequenceTypeDo> ownerSequenceTypeDos;

	
	public List<OwnerSelectionRuleDo> getOwnerSelectionRuleDos() {
		return ownerSelectionRuleDos;
	}

	public void setOwnerSelectionRuleDos(List<OwnerSelectionRuleDo> ownerSelectionRuleDos) {
		this.ownerSelectionRuleDos = ownerSelectionRuleDos;
	}

	public List<StatusConfigDo> getStatusConfigDos() {
		return statusConfigDos;
	}

	public void setStatusConfigDos(List<StatusConfigDo> statusConfigDos) {
		this.statusConfigDos = statusConfigDos;
	}

	public List<ActionConfigDo> getActionConfigDos() {
		return actionConfigDos;
	}

	public void setActionConfigDos(List<ActionConfigDo> actionConfigDos) {
		this.actionConfigDos = actionConfigDos;
	}

	public List<RuleDto> getRuleDtos() {
		return ruleDtos;
	}

	public void setRuleDtos(List<RuleDto> ruleDtos) {
		this.ruleDtos = ruleDtos;
	}

	public List<ProcessTemplateDto> getProcessTemplateDtos() {
		return processTemplateDtos;
	}

	public void setProcessTemplateDtos(List<ProcessTemplateDto> processTemplateDtos) {
		this.processTemplateDtos = processTemplateDtos;
	}

	public List<CrossConstantDto> getCrossConstantDto() {
		return crossConstantDto;
	}

	public void setCrossConstantDto(List<CrossConstantDto> crossConstantDto) {
		this.crossConstantDto = crossConstantDto;
	}

	public List<TaskTemplateOwnerDto> getTaskTemplateOwnerDtos() {
		return taskTemplateOwnerDtos;
	}

	public void setTaskTemplateOwnerDtos(List<TaskTemplateOwnerDto> taskTemplateOwnerDtos) {
		this.taskTemplateOwnerDtos = taskTemplateOwnerDtos;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	public ProcessConfigTbDto getProcessConfigTbDto() {
		return processConfigTbDto;
	}

	public void setProcessConfigTbDto(ProcessConfigTbDto processConfigTbDto) {
		this.processConfigTbDto = processConfigTbDto;
	}

	public List<TaskTemplateDto> getTaskTemplates() {
		return taskTemplates;
	}

	public void setTaskTemplates(List<TaskTemplateDto> taskTemplates) {
		this.taskTemplates = taskTemplates;
	}

	@Override
	public String toString() {
		return "WorkflowDto [attributes=" + attributes + ", taskTemplates=" + taskTemplates + ", processConfigTbDto="
				+ processConfigTbDto + ", taskTemplateOwnerDtos=" + taskTemplateOwnerDtos + ", processTemplateDtos="
				+ processTemplateDtos + ", crossConstantDto=" + crossConstantDto + ", responseMessage="
				+ responseMessage + ", ruleDtos=" + ruleDtos + ", actionConfigDos=" + actionConfigDos
				+ ", statusConfigDos=" + statusConfigDos + ", ownerSelectionRuleDos=" + ownerSelectionRuleDos
				+ ", ownerSequenceTypeDos=" + ownerSequenceTypeDos + "]";
	}

	public List<CustomAttributeTemplateDto> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<CustomAttributeTemplateDto> attributes) {
		this.attributes = attributes;
	}

	public List<OwnerSequenceTypeDo> getOwnerSequenceTypeDos() {
		return ownerSequenceTypeDos;
	}

	public void setOwnerSequenceTypeDos(List<OwnerSequenceTypeDo> ownerSequenceTypeDos) {
		this.ownerSequenceTypeDos = ownerSequenceTypeDos;
	}

}
