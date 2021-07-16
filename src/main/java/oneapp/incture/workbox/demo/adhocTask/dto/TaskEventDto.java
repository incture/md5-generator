package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;

public class TaskEventDto {

	private String requestId;
	private String eventId;
	private String processId;
	private String description;
	private String subject;
	private String name;
	private String status;
	private String currentProcessor;
	private String priority;
	private Date createdAt;
	private Date completedAt;
	private Date completionDeadLine;
	private List<String> Owners;
	private Map<String,TaskOwnersDto> Recipents;
	private String ownersName;
	private String createdAtInString;
	private String completedAtInString;
	private String completionDeadLineInString;
	private String currentProcessorDisplayName;
	private String processName;
	private String statusFlag;
	private String taskMode;
	private String taskType;
	private Date forwardedAt;
	private String forwardedBy;
	private String forwardedAtInString;
	private String urgentsla;
	private float timePercentCompleted;
	private String timeLeftDisplayString;
	private boolean isBreached;
	private String slaDisplayDate;
	private String sla;
	private String origin;
	private String detailUrl;
	private String prevTask;
	private Date slaDueDate;
	private Date updatedAt;
	private String createdBy;
	
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Override
	public String toString() {
		return "TaskEventDto [requestId=" + requestId + ", eventId=" + eventId + ", processId=" + processId
				+ ", description=" + description + ", subject=" + subject + ", name=" + name + ", status=" + status
				+ ", currentProcessor=" + currentProcessor + ", priority=" + priority + ", createdAt=" + createdAt
				+ ", completedAt=" + completedAt + ", completionDeadLine=" + completionDeadLine + ", Owners=" + Owners
				+ ", Recipents=" + Recipents + ", ownersName=" + ownersName + ", createdAtInString=" + createdAtInString
				+ ", completedAtInString=" + completedAtInString + ", completionDeadLineInString="
				+ completionDeadLineInString + ", currentProcessorDisplayName=" + currentProcessorDisplayName
				+ ", processName=" + processName + ", statusFlag=" + statusFlag + ", taskMode=" + taskMode
				+ ", taskType=" + taskType + ", forwardedAt=" + forwardedAt + ", forwardedBy=" + forwardedBy
				+ ", forwardedAtInString=" + forwardedAtInString + ", urgentsla=" + urgentsla
				+ ", timePercentCompleted=" + timePercentCompleted + ", timeLeftDisplayString=" + timeLeftDisplayString
				+ ", isBreached=" + isBreached + ", slaDisplayDate=" + slaDisplayDate + ", sla=" + sla + ", origin="
				+ origin + ", detailUrl=" + detailUrl + ", prevTask=" + prevTask + ", slaDueDate=" + slaDueDate
				+ ", updatedAt=" + updatedAt + ", createdBy=" + createdBy + "]";
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrentProcessor() {
		return currentProcessor;
	}
	public void setCurrentProcessor(String currentProcessor) {
		this.currentProcessor = currentProcessor;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}
	public Date getCompletionDeadLine() {
		return completionDeadLine;
	}
	public void setCompletionDeadLine(Date completionDeadLine) {
		this.completionDeadLine = completionDeadLine;
	}
	public List<String> getOwners() {
		return Owners;
	}
	public void setOwners(List<String> owners) {
		Owners = owners;
	}
	public Map<String, TaskOwnersDto> getRecipents() {
		return Recipents;
	}
	public void setRecipents(Map<String, TaskOwnersDto> recipents) {
		Recipents = recipents;
	}
	public String getOwnersName() {
		return ownersName;
	}
	public void setOwnersName(String ownersName) {
		this.ownersName = ownersName;
	}
	public String getCreatedAtInString() {
		return createdAtInString;
	}
	public void setCreatedAtInString(String createdAtInString) {
		this.createdAtInString = createdAtInString;
	}
	public String getCompletedAtInString() {
		return completedAtInString;
	}
	public void setCompletedAtInString(String completedAtInString) {
		this.completedAtInString = completedAtInString;
	}
	public String getCompletionDeadLineInString() {
		return completionDeadLineInString;
	}
	public void setCompletionDeadLineInString(String completionDeadLineInString) {
		this.completionDeadLineInString = completionDeadLineInString;
	}
	public String getCurrentProcessorDisplayName() {
		return currentProcessorDisplayName;
	}
	public void setCurrentProcessorDisplayName(String currentProcessorDisplayName) {
		this.currentProcessorDisplayName = currentProcessorDisplayName;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getTaskMode() {
		return taskMode;
	}
	public void setTaskMode(String taskMode) {
		this.taskMode = taskMode;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public Date getForwardedAt() {
		return forwardedAt;
	}
	public void setForwardedAt(Date forwardedAt) {
		this.forwardedAt = forwardedAt;
	}
	public String getForwardedBy() {
		return forwardedBy;
	}
	public void setForwardedBy(String forwardedBy) {
		this.forwardedBy = forwardedBy;
	}
	public String getForwardedAtInString() {
		return forwardedAtInString;
	}
	public void setForwardedAtInString(String forwardedAtInString) {
		this.forwardedAtInString = forwardedAtInString;
	}
	public String getUrgentsla() {
		return urgentsla;
	}
	public void setUrgentsla(String urgentsla) {
		this.urgentsla = urgentsla;
	}
	public float getTimePercentCompleted() {
		return timePercentCompleted;
	}
	public void setTimePercentCompleted(float timePercentCompleted) {
		this.timePercentCompleted = timePercentCompleted;
	}
	public String getTimeLeftDisplayString() {
		return timeLeftDisplayString;
	}
	public void setTimeLeftDisplayString(String timeLeftDisplayString) {
		this.timeLeftDisplayString = timeLeftDisplayString;
	}
	public boolean isBreached() {
		return isBreached;
	}
	public void setBreached(boolean isBreached) {
		this.isBreached = isBreached;
	}
	public String getSlaDisplayDate() {
		return slaDisplayDate;
	}
	public void setSlaDisplayDate(String slaDisplayDate) {
		this.slaDisplayDate = slaDisplayDate;
	}
	public String getSla() {
		return sla;
	}
	public void setSla(String sla) {
		this.sla = sla;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public String getPrevTask() {
		return prevTask;
	}
	public void setPrevTask(String prevTask) {
		this.prevTask = prevTask;
	}
	public Date getSlaDueDate() {
		return slaDueDate;
	}
	public void setSlaDueDate(Date slaDueDate) {
		this.slaDueDate = slaDueDate;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
