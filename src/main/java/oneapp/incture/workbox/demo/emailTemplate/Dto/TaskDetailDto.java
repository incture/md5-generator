package oneapp.incture.workbox.demo.emailTemplate.Dto;

public class TaskDetailDto {

	String taskName;
	String taskDisplayName;
	@Override
	public String toString() {
		return "TaskDetailDto [taskName=" + taskName + ", taskDisplayName=" + taskDisplayName + "]";
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDisplayName() {
		return taskDisplayName;
	}
	public void setTaskDisplayName(String taskDisplayName) {
		this.taskDisplayName = taskDisplayName;
	}
	
	
}
