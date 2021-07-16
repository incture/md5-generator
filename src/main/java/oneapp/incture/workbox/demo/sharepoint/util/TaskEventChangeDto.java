package oneapp.incture.workbox.demo.sharepoint.util;

public class TaskEventChangeDto {
	
	private String status; 
	private String percentComplete;
	private String eventId;
	private String processId;
	
	public TaskEventChangeDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public TaskEventChangeDto(String status, String percentComplete, String eventId, String processId) {
		super();
		this.status = status;
		this.percentComplete = percentComplete;
		this.eventId = eventId;
		this.processId = processId;
	}

	/**
	 * @return
	 */
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getPercentComplete() {
		return percentComplete;
	}
	public void setPercentComplete(String percentComplete) {
		this.percentComplete = percentComplete;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	

}
