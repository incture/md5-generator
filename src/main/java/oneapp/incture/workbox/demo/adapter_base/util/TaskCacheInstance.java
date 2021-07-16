package oneapp.incture.workbox.demo.adapter_base.util;

public class TaskCacheInstance {

	public TaskCacheInstance() {
	}

	private String taskId;
	private String status;
	private String processor;

	public TaskCacheInstance(String taskId, String status, String processor) {
		this.taskId = taskId;
		this.status = status;
		this.processor = processor;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	@Override
	public String toString() {
		return "TaskCacheInstance [taskId=" + taskId + ", status=" + status + ", processor=" + processor + "]";
	}

}
