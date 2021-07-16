package oneapp.incture.workbox.demo.adapterJira.dto;

public class StatusDto {
	private String statusId;
	private String statusName;
	private String processName;
	
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
	@Override
	public String toString() {
		return "StatusDto [statusId=" + statusId + ", statusName=" + statusName + ", processName=" + processName + "]";
	}
	
}
