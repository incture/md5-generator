package oneapp.incture.workbox.demo.notification.dto;

public class NotificationRequest {

	String eventId;
	String action;
	String origin;
	@Override
	public String toString() {
		return "NotificationRequest [eventId=" + eventId + ", action=" + action + ", origin=" + origin + "]";
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
	
}
