package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

public class NotificationActionDto {

	private String actionType;
	private String eventOrigin;
	private List<String> channelLists;
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public List<String> getChannelLists() {
		return channelLists;
	}
	public void setChannelLists(List<String> channelLists) {
		this.channelLists = channelLists;
	}
	public String getEventOrigin() {
		return eventOrigin;
	}
	public void setEventOrigin(String eventOrigin) {
		this.eventOrigin = eventOrigin;
	}
	@Override
	public String toString() {
		return "NotificationActionDto [actionType=" + actionType + ", eventOrigin=" + eventOrigin + ", channelLists="
				+ channelLists + "]";
	}
	
	
}

