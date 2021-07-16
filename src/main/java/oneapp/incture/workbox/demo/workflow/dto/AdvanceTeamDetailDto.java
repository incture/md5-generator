package oneapp.incture.workbox.demo.workflow.dto;

import java.util.List;

public class AdvanceTeamDetailDto {
	
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
	String url;
	List<RuleDto> rules;
	List<OwnerSelectionRuleDto> ownerSelectionRules;
	String taskNature;
	StatusDto statusDto;
	OwnerSequenceTypeDto ownerSequenceType;
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
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<RuleDto> getRules() {
		return rules;
	}
	public void setRules(List<RuleDto> rules) {
		this.rules = rules;
	}
	public List<OwnerSelectionRuleDto> getOwnerSelectionRules() {
		return ownerSelectionRules;
	}
	public void setOwnerSelectionRules(List<OwnerSelectionRuleDto> ownerSelectionRules) {
		this.ownerSelectionRules = ownerSelectionRules;
	}
	public String getTaskNature() {
		return taskNature;
	}
	public void setTaskNature(String taskNature) {
		this.taskNature = taskNature;
	}
	public StatusDto getStatusDto() {
		return statusDto;
	}
	public void setStatusDto(StatusDto statusDto) {
		this.statusDto = statusDto;
	}
	public OwnerSequenceTypeDto getOwnerSequenceType() {
		return ownerSequenceType;
	}
	public void setOwnerSequenceType(OwnerSequenceTypeDto ownerSequenceType) {
		this.ownerSequenceType = ownerSequenceType;
	}
	@Override
	public String toString() {
		return "AdvanceTeamDetailDto [processName=" + processName + ", group=" + group + ", eventName=" + eventName
				+ ", templateId=" + templateId + ", individual=" + individual + ", taskType=" + taskType
				+ ", runTimeUser=" + runTimeUser + ", customKey=" + customKey + ", customAttributes=" + customAttributes
				+ ", CustomattrTypes=" + CustomattrTypes + ", sourceId=" + sourceId + ", targetId=" + targetId
				+ ", subject=" + subject + ", description=" + description + ", url=" + url + ", rules=" + rules
				+ ", ownerSelectionRules=" + ownerSelectionRules + ", taskNature=" + taskNature + ", statusDto="
				+ statusDto + ", ownerSequenceType=" + ownerSequenceType + "]";
	}
	
	

}
