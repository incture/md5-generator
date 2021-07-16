package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;
import java.util.Map;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValueTableAdhocDo;
import oneapp.incture.workbox.demo.workflow.dto.TaskTemplateOwnerDto;

public class TaskCreationDto {

	public TaskCreationDto() {
		super();
	}

	private List<TaskEventsDto> tasks;
	private List<ProcessTemplateValueDto> processTemplateValues;
	private List<ProcessEventsDto> processes;
	private List<TaskOwnersDto> owners;
	private int resultCount;
	private List<CustomAttributeValue> customAttributeValues;
	private List<CustomAttributeValueTableAdhocDo> customAttributeValue;
	private List<TaskValueDto> taskValues;
	private List<TaskTemplateOwnerDto> templateOwnerDtos;
	private AttributesResponseDto attributesResponseDto;
	private List<TaskAuditDto> auditDtos;
	private Integer taskCount;
	private Map<String, List<ProcessTemplateValueDto>> templateDtos;
	private Map<String,Map<String,String>> processRelatedAttribute;

	public List<CustomAttributeValueTableAdhocDo> getCustomAttributeValue() {
		return customAttributeValue;
	}

	public void setCustomAttributeValue(List<CustomAttributeValueTableAdhocDo> customAttributeValue) {
		this.customAttributeValue = customAttributeValue;
	}

	public Map<String, List<ProcessTemplateValueDto>> getTemplateDtos() {
		return templateDtos;
	}

	public void setTemplateDtos(Map<String, List<ProcessTemplateValueDto>> templateDtos) {
		this.templateDtos = templateDtos;
	}

	public Integer getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(Integer taskCount) {
		this.taskCount = taskCount;
	}

	public List<ProcessTemplateValueDto> getProcessTemplateValues() {
		return processTemplateValues;
	}

	public void setProcessTemplateValues(List<ProcessTemplateValueDto> processTemplateValues) {
		this.processTemplateValues = processTemplateValues;
	}

	public List<TaskAuditDto> getAuditDtos() {
		return auditDtos;
	}

	public void setAuditDtos(List<TaskAuditDto> auditDtos) {
		this.auditDtos = auditDtos;
	}

	public List<TaskTemplateOwnerDto> getTemplateOwnerDtos() {
		return templateOwnerDtos;
	}

	public void setTemplateOwnerDtos(List<TaskTemplateOwnerDto> templateOwnerDtos) {
		this.templateOwnerDtos = templateOwnerDtos;
	}

	private ResponseMessage responseMessage;

	public AttributesResponseDto getAttributesResponseDto() {
		return attributesResponseDto;
	}

	public void setAttributesResponseDto(AttributesResponseDto attributesResponseDto) {
		this.attributesResponseDto = attributesResponseDto;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "TaskCreationDto [tasks=" + tasks + ", processTemplateValues=" + processTemplateValues + ", processes="
				+ processes + ", owners=" + owners + ", resultCount=" + resultCount + ", customAttributeValues="
				+ customAttributeValues + ", customAttributeValue=" + customAttributeValue + ", taskValues="
				+ taskValues + ", templateOwnerDtos=" + templateOwnerDtos + ", attributesResponseDto="
				+ attributesResponseDto + ", auditDtos=" + auditDtos + ", taskCount=" + taskCount + ", templateDtos="
				+ templateDtos + ", processRelatedAttribute=" + processRelatedAttribute + ", responseMessage="
				+ responseMessage + "]";
	}

	public List<TaskEventsDto> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskEventsDto> tasks) {
		this.tasks = tasks;
	}

	public List<ProcessEventsDto> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessEventsDto> processes) {
		this.processes = processes;
	}

	public List<TaskOwnersDto> getOwners() {
		return owners;
	}

	public void setOwners(List<TaskOwnersDto> owners) {
		this.owners = owners;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	public List<CustomAttributeValue> getCustomAttributeValues() {
		return customAttributeValues;
	}

	public void setCustomAttributeValues(List<CustomAttributeValue> customAttributeValues) {
		this.customAttributeValues = customAttributeValues;
	}

	public List<TaskValueDto> getTaskValues() {
		return taskValues;
	}

	public void setTaskValues(List<TaskValueDto> taskValues) {
		this.taskValues = taskValues;
	}

	public Map<String, Map<String, String>> getProcessRelatedAttribute() {
		return processRelatedAttribute;
	}

	public void setProcessRelatedAttribute(Map<String, Map<String, String>> processRelatedAttribute) {
		this.processRelatedAttribute = processRelatedAttribute;
	}
	

}
