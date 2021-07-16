package oneapp.incture.workbox.demo.adhocTask.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaskTemplateDto {

	String taskName;
	String ownerId;
	String taskType;
	@Override
	public String toString() {
		return "TaskTemplateDto [taskName=" + taskName + ", ownerId=" + ownerId + ", taskType=" + taskType + "]";
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
	
	
	
	
}
