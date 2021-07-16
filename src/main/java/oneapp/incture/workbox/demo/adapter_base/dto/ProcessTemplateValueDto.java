package oneapp.incture.workbox.demo.adapter_base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class ProcessTemplateValueDto extends BaseDto{

	private String processId;
	private String templateId;
	private String taskName;
	private String ownerId;
	private String taskType;
	private Boolean runTimeUser;
	private String customKey;
	private String subject;
	private String description;
	private String sourceId;
	private String targetId;
	private String url;
	private String ownerSeqType;
	private String attrTypeName;
	private String orderBy;
	private String taskNature;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String eventId;
	@Override
	public String toString() {
		return "ProcessTemplateValueDto [processId=" + processId + ", templateId=" + templateId + ", taskName="
				+ taskName + ", ownerId=" + ownerId + ", taskType=" + taskType + ", runTimeUser=" + runTimeUser
				+ ", customKey=" + customKey + ", subject=" + subject + ", description=" + description + ", sourceId="
				+ sourceId + ", targetId=" + targetId + ", url=" + url + ", ownerSeqType=" + ownerSeqType
				+ ", attrTypeName=" + attrTypeName + ", orderBy=" + orderBy + ", taskNature=" + taskNature
				+ ", eventId=" + eventId + "]";
	}
	public String getTaskNature() {
		return taskNature;
	}
	public void setTaskNature(String taskNature) {
		this.taskNature = taskNature;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public Boolean getRunTimeUser() {
		return runTimeUser;
	}
	public void setRunTimeUser(Boolean runTimeUser) {
		this.runTimeUser = runTimeUser;
	}
	public String getCustomKey() {
		return customKey;
	}
	public void setCustomKey(String customKey) {
		this.customKey = customKey;
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
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}
	public String getOwnerSeqType() {
		return ownerSeqType;
	}
	public void setOwnerSeqType(String ownerSeqType) {
		this.ownerSeqType = ownerSeqType;
	}
	public String getAttrTypeName() {
		return attrTypeName;
	}
	public void setAttrTypeName(String attrTypeName) {
		this.attrTypeName = attrTypeName;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	
}
