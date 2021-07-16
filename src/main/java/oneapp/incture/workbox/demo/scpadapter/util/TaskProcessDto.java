package oneapp.incture.workbox.demo.scpadapter.util;

public class TaskProcessDto {
	
	public TaskProcessDto(String taskId, String processDefinitionId) {
		this.taskId = taskId;
		this.processDefinitionId = processDefinitionId;
	}

	private String taskId;
	private String processDefinitionId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	@Override
	public String toString() {
		return "TaskProcessDto [taskId=" + taskId + ", processDefinitionId=" + processDefinitionId + "]";
	}

}
