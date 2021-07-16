package oneapp.incture.workbox.demo.adhocTask.util;

public class ProcessDescDto {

	String description;
	String processName;
	String requestId;
	String eventId;
	@Override
	public String toString() {
		return "ProcessDescDto [description=" + description + ", processName=" + processName + ", requestId="
				+ requestId + ", eventId=" + eventId + "]";
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
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
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	
	
}
