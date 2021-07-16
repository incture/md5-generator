package oneapp.incture.workbox.demo.inbox.dto;

public class TaskRestDto {

	private String processId;
	private String requestId;
	private String processName;
	private String taskId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public TaskRestDto() {
		super();
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
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

	@Override
	public String toString() {
		return "TaskRestDto [processId=" + processId + ", requestId=" + requestId + ", processName=" + processName
				+ ", taskId=" + taskId + "]";
	}

}
