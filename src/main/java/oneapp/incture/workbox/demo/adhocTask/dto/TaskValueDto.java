package oneapp.incture.workbox.demo.adhocTask.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class TaskValueDto extends BaseDto{
	
	private String processId;
	private String taskName;
	private String ownerId;
	private Integer stepNumber;
	private String taskType;
	private String description;

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
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
	public Integer getStepNumber() {
		return stepNumber;
	}
	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	@Override
	public String toString() {
		return "TaskValueDto [processId=" + processId + ", taskName=" + taskName + ", ownerId=" + ownerId
				+ ", stepNumber=" + stepNumber + ", taskType=" + taskType + ", description=" + description + "]";
	}
	@Override
	public Boolean getValidForUsage() {
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
	}
	
}
