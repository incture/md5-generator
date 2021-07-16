package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

public class NotificationChannelDto {
	private String channelName;
	private List<String> actionTypes;
	
	public NotificationChannelDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NotificationChannelDto(String channelName, List<String> actionTypes) {
		super();
		this.channelName = channelName;
		this.actionTypes = actionTypes;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public List<String> getActionTypes() {
		return actionTypes;
	}
	public void setActionTypes(List<String> actionTypes) {
		this.actionTypes = actionTypes;
	}
	@Override
	public String toString() {
		return "NotificationChannelDto [channelName=" + channelName + ", actionTypes=" + actionTypes + "]";
	}
	
}
