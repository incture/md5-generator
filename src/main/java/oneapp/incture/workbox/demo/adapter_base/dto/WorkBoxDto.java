package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class WorkBoxDto extends BaseDto {
	private String requestId;
	private String processName;
	private String name;
	private String subject;
	private String startedBy;
	private String createdAt;
	private String completedAt;
	private String status;
	private String taskOwner;
	private String taskId;
	private String processId;
	private String taskDescription;
	private String detailURL;
	private float timePercentCompleted;
	private String timeLeftDisplayString;
	private boolean isBreached;
	private String slaDisplayDate;
	private String sla;
	private String url;
	private String startedByDisp;
//	private CustomAttributeDto customAttributes;
	/* this attribute is added for setting sla due date*/
	private String slaDueDate;
	private String taskType;
	private String updatedAt;
	private String taskSla;
	/* Forwarded At And Forwarded By Added */
	private String forwardedBy;
	private String forwardedAt;
	private Boolean pinned;
	/* Process Display Name */
	private String processDisplayName;
	
	// Custom attributes in json format
//	private String customAttributes;
	
	private boolean isCritical;
	private String origin;
	private String businessStatus;
	private List<ActionConfigDto> actions;
	
	private Boolean isCustomLayout;
	
	public Boolean getIsCustomLayout() {
		return isCustomLayout;
	}

	public void setIsCustomLayout(Boolean isCustomLayout) {
		this.isCustomLayout = isCustomLayout;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public List<ActionConfigDto> getActions() {
		return actions;
	}

	public void setActions(List<ActionConfigDto> actions) {
		this.actions = actions;
	}

	public Boolean getPinned() {
		return pinned;
	}

	public void setPinned(Boolean pinned) {
		this.pinned = pinned;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	
	public boolean isCritical() {
		return isCritical;
	}

	public String getStartedByDisp() {
		return startedByDisp;
	}

	public void setStartedByDisp(String startedByDisp) {
		this.startedByDisp = startedByDisp;
	}

	public void setCritical(boolean isCritical) {
		this.isCritical = isCritical;
	}

	public String getSlaDueDate() {
		return slaDueDate;
	}

	public void setSlaDueDate(String slaDueDate) {
		this.slaDueDate = slaDueDate;
	}

//	public CustomAttributeDto getCustomAttributes() {
//		return customAttributes;
//	}
//
//	public void setCustomAttributes(CustomAttributeDto customAttributes) {
//		this.customAttributes = customAttributes;
//	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getDetailURL() {
		return detailURL;
	}

	public void setDetailURL(String detailURL) {
		this.detailURL = detailURL;
	}


	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStartedBy() {
		return startedBy;
	}

	public void setStartedBy(String startedBy) {
		this.startedBy = startedBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(String completedAt) {
		this.completedAt = completedAt;
	}

	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getTimeLeftDisplayString() {
		return timeLeftDisplayString;
	}

	public void setTimeLeftDisplayString(String timeLeftDisplayString) {
		this.timeLeftDisplayString = timeLeftDisplayString;
	}

	public float getTimePercentCompleted() {
		return timePercentCompleted;
	}

	public void setTimePercentCompleted(float timePercentCompleted) {
		this.timePercentCompleted = timePercentCompleted;
	}

	public String getForwardedBy() {
		return forwardedBy;
	}

	public void setForwardedBy(String forwardedBy) {
		this.forwardedBy = forwardedBy;
	}

	public String getForwardedAt() {
		return forwardedAt;
	}

	public void setForwardedAt(String forwardedAt) {
		this.forwardedAt = forwardedAt;
	}

	public String getProcessDisplayName() {
		return processDisplayName;
	}

	public void setProcessDisplayName(String processDisplayName) {
		this.processDisplayName = processDisplayName;
	}
	public String getTaskSla() {
		return taskSla;
	}

	public void setTaskSla(String taskSla) {
		this.taskSla = taskSla;
	}

	@Override
	public String toString() {
		return "WorkBoxDto [requestId=" + requestId + ", processName=" + processName + ", name=" + name + ", subject="
				+ subject + ", startedBy=" + startedBy + ", createdAt=" + createdAt + ", completedAt=" + completedAt
				+ ", status=" + status + ", taskOwner=" + taskOwner + ", taskId=" + taskId + ", processId=" + processId
				+ ", taskDescription=" + taskDescription + ", detailURL=" + detailURL + ", timePercentCompleted="
				+ timePercentCompleted + ", timeLeftDisplayString=" + timeLeftDisplayString + ", isBreached="
				+ isBreached + ", slaDisplayDate=" + slaDisplayDate + ", sla=" + sla + ", url=" + url
				+ ", startedByDisp=" + startedByDisp + ", slaDueDate=" + slaDueDate + ", taskType=" + taskType
				+ ", updatedAt=" + updatedAt + ", taskSla=" + taskSla + ", forwardedBy=" + forwardedBy
				+ ", forwardedAt=" + forwardedAt + ", pinned=" + pinned + ", processDisplayName=" + processDisplayName
				+ ", isCritical=" + isCritical + ", origin=" + origin + ", businessStatus=" + businessStatus
				+ ", actions=" + actions + "]";
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

	
}
