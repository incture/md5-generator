package oneapp.incture.workbox.demo.workflow.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class TaskTemplateDto extends BaseDto {

	private String processName;
	private Integer stepNumber;
	private String taskId;
	private String taskName;
	private String ownerId;
	private String taskType;
	private Boolean runTimeUser;
	private String customKey;
	private String sourceId;
	private String destinationId;
	private String taskNature;

	
	@Override
	public String toString() {
		return "TaskTemplateDto [processName=" + processName + ", stepNumber=" + stepNumber + ", taskId=" + taskId
				+ ", taskName=" + taskName + ", ownerId=" + ownerId + ", taskType=" + taskType + ", runTimeUser="
				+ runTimeUser + ", customKey=" + customKey + ", sourceId=" + sourceId + ", destinationId="
				+ destinationId + ", taskNature=" + taskNature + "]";
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(String destinationId) {
		this.destinationId = destinationId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getCustomKey() {
		return customKey;
	}

	public void setCustomKey(String customKey) {
		this.customKey = customKey;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
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
	
	

	public String getTaskNature() {
		return taskNature;
	}

	public void setTaskNature(String taskNature) {
		this.taskNature = taskNature;
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
	}

}
