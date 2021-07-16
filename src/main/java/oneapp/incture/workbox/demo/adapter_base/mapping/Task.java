package oneapp.incture.workbox.demo.adapter_base.mapping;

import java.util.List;

public class Task implements Event {

	public Task() {
		super();
	}

	public Task(String activityId, String claimedAt, String completedAt, String createdAt, String description,
			String id, String processor, List<String> recipientUsers, List<String> recipientGroups, String status,
			String subject, String workflowDefinitionId, String workflowInstanceId, String priority, String dueDate,
			String createdBy, String definitionId,String lastChangedAt) {
		super();
		this.activityId = activityId;
		this.claimedAt = claimedAt;
		this.completedAt = completedAt;
		this.createdAt = createdAt;
		this.description = description;
		this.id = id;
		this.processor = processor;
		this.recipientUsers = recipientUsers;
		this.recipientGroups = recipientGroups;
		this.status = status;
		this.subject = subject;
		this.workflowDefinitionId = workflowDefinitionId;
		this.workflowInstanceId = workflowInstanceId;
		this.priority = priority;
		this.dueDate = dueDate;
		this.createdBy = createdBy;
		this.definitionId = definitionId;
		this.lastChangedAt=lastChangedAt;
	}

	private String activityId;
	private String claimedAt;
	private String completedAt;
	private String createdAt;
	private String description;
	private String id;
	private String processor;
	private List<String> recipientUsers;
	private List<String> recipientGroups;
	private String status;
	private String subject;
	private String workflowDefinitionId;
	private String workflowInstanceId;
	private String priority;
	private String dueDate;
	private String createdBy;
	private String definitionId;
	private List<Attribute> attributes;
	private String lastChangedAt;

	
	public String getLastChangedAt() {
		return lastChangedAt;
	}

	public void setLastChangedAt(String lastChangedAt) {
		this.lastChangedAt = lastChangedAt;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getClaimedAt() {
		return claimedAt;
	}

	public void setClaimedAt(String claimedAt) {
		this.claimedAt = claimedAt;
	}

	public String getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(String completedAt) {
		this.completedAt = completedAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public List<String> getRecipientUsers() {
		return recipientUsers;
	}

	public void setRecipientUsers(List<String> recipientUsers) {
		this.recipientUsers = recipientUsers;
	}

	public List<String> getRecipientGroups() {
		return recipientGroups;
	}

	public void setRecipientGroups(List<String> recipientGroups) {
		this.recipientGroups = recipientGroups;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getWorkflowDefinitionId() {
		return workflowDefinitionId;
	}

	public void setWorkflowDefinitionId(String workflowDefinitionId) {
		this.workflowDefinitionId = workflowDefinitionId;
	}

	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}

	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "Task [activityId=" + activityId + ", claimedAt=" + claimedAt + ", completedAt=" + completedAt
				+ ", createdAt=" + createdAt + ", description=" + description + ", id=" + id + ", processor="
				+ processor + ", recipientUsers=" + recipientUsers + ", recipientGroups=" + recipientGroups
				+ ", status=" + status + ", subject=" + subject + ", workflowDefinitionId=" + workflowDefinitionId
				+ ", workflowInstanceId=" + workflowInstanceId + ", priority=" + priority + ", dueDate=" + dueDate
				+ ", createdBy=" + createdBy + ", definitionId=" + definitionId + ", attributes=" + attributes
				+ ", lastChangedAt=" + lastChangedAt + "]";
	}

}
