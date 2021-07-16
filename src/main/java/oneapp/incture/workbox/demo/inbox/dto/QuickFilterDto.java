package oneapp.incture.workbox.demo.inbox.dto;

public class QuickFilterDto {

	private String processName;
	private String status;
	private String duration;
	private String userId;
	private String freeFilter;
	private String eventId;
	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public QuickFilterDto() {
		super();
	}

	public QuickFilterDto(String processName, String status, String duration, String userId, String freeFilter) {
		super();
		this.processName = processName;
		this.status = status;
		this.duration = duration;
		this.userId = userId;
		this.freeFilter = freeFilter;
	}

	public String getProcessName() {
		return processName;
	}

	public String getFreeFilter() {
		return freeFilter;
	}

	public void setFreeFilter(String freeFilter) {
		this.freeFilter = freeFilter;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "QuickFilterDto [processName=" + processName + ", status=" + status + ", duration=" + duration
				+ ", userId=" + userId + ", freeFilter=" + freeFilter + ", eventId=" + eventId + "]";
	}

}
