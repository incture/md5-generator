package oneapp.incture.workbox.demo.workflow.dto;

import java.util.List;

public class TeamDetailDto {

	String processName;
	List<String> group;
	String eventName;
	String templateId;
	List<String> individual;
	String taskType;
	Integer runTimeUser;
	String customKey;
	List<CustomAttributeTemplateDto> customAttributes;
	String CustomattrTypes;
	List<String> sourceId;
	List<String> targetId;
	String subject;
	String description;
	Integer isEdited;
	String url;
	List<RuleDto> rules;
	List<OwnerSelectionRuleDto> ownerSelectionRules;
	String taskNature;
	StatusDto statusDto;
	OwnerSequenceTypeDto ownerSequenceType;

	public List<OwnerSelectionRuleDto> getOwnerSelectionRules() {
		return ownerSelectionRules;
	}

	public void setOwnerSelectionRules(List<OwnerSelectionRuleDto> ownerSelectionRules) {
		this.ownerSelectionRules = ownerSelectionRules;
	}

	public StatusDto getStatusDto() {
		return statusDto;
	}

	public void setStatusDto(StatusDto statusDto) {
		this.statusDto = statusDto;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public List<String> getSourceId() {
		return sourceId;
	}

	public void setSourceId(List<String> sourceId) {
		this.sourceId = sourceId;
	}

	public List<String> getTargetId() {
		return targetId;
	}

	public void setTargetId(List<String> targetId) {
		this.targetId = targetId;
	}

	@Override
	public String toString() {
		return "TeamDetailDto [processName=" + processName + ", group=" + group + ", eventName=" + eventName
				+ ", templateId=" + templateId + ", individual=" + individual + ", taskType=" + taskType
				+ ", runTimeUser=" + runTimeUser + ", customKey=" + customKey + ", customAttributes=" + customAttributes
				+ ", CustomattrTypes=" + CustomattrTypes + ", sourceId=" + sourceId + ", targetId=" + targetId
				+ ", subject=" + subject + ", description=" + description + ", isEdited=" + isEdited + ", url=" + url
				+ ", rules=" + rules + ", ownerSelectionRules=" + ownerSelectionRules + ", taskNature=" + taskNature
				+ ", statusDto=" + statusDto + ", ownerSequenceType=" + ownerSequenceType + "]";
	}

	public List<RuleDto> getRules() {
		return rules;
	}

	public void setRules(List<RuleDto> rules) {
		this.rules = rules;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public List<String> getGroup() {
		return group;
	}

	public void setGroup(List<String> group) {
		this.group = group;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public List<String> getIndividual() {
		return individual;
	}

	public void setIndividual(List<String> individual) {
		this.individual = individual;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Integer getRunTimeUser() {
		return runTimeUser;
	}

	public void setRunTimeUser(Integer runTimeUser) {
		this.runTimeUser = runTimeUser;
	}

	public String getCustomKey() {
		return customKey;
	}

	public void setCustomKey(String customKey) {
		this.customKey = customKey;
	}

	public List<CustomAttributeTemplateDto> getCustomAttributes() {
		return customAttributes;
	}

	public void setCustomAttributes(List<CustomAttributeTemplateDto> customAttributes) {
		this.customAttributes = customAttributes;
	}

	public String getCustomattrTypes() {
		return CustomattrTypes;
	}

	public void setCustomattrTypes(String customattrTypes) {
		CustomattrTypes = customattrTypes;
	}

	public Integer getIsEdited() {
		return isEdited;
	}

	public void setIsEdited(Integer isEdited) {
		this.isEdited = isEdited;
	}

	public String getTaskNature() {
		return taskNature;
	}

	public void setTaskNature(String taskNature) {
		this.taskNature = taskNature;
	}

	public OwnerSequenceTypeDto getOwnerSequenceType() {
		return ownerSequenceType;
	}

	public void setOwnerSequenceType(OwnerSequenceTypeDto ownerSequenceType) {
		this.ownerSequenceType = ownerSequenceType;
	}

}
