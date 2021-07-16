package oneapp.incture.workbox.demo.workload.dto;

public class UserWorkloadRequestDto {

	
	
	private String processName;
	private String requestId;
	private String taskStatus;
	
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	@Override
	public String toString() {
		return "UserWorkloadRequestDto [processName=" + processName + ", requestId=" + requestId + ", taskStatus="
				+ taskStatus + "]";
	}
	
	
	
}
