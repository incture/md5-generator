package oneapp.incture.workbox.demo.notification.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class NotificationConfigDto extends BaseDto{

	private String userId;
	private String userName;
	private String enableChannel;
	private String enableAction;
	private String eventOrigin;
	
	public String getEventOrigin() {
		return eventOrigin;
	}
	public void setEventOrigin(String eventOrigin) {
		this.eventOrigin = eventOrigin;
	}
	public NotificationConfigDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NotificationConfigDto(String userId, String userName, String enableChannel, String enableAction) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.enableChannel = enableChannel;
		this.enableAction = enableAction;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEnableChannel() {
		return enableChannel;
	}
	public void setEnableChannel(String enableChannel) {
		this.enableChannel = enableChannel;
	}
	public String getEnableAction() {
		return enableAction;
	}
	public void setEnableAction(String enableAction) {
		this.enableAction = enableAction;
	}	
	@Override
	public String toString() {
		return "NotificationConfigDto [userId=" + userId + ", userName=" + userName + ", enableChannel=" + enableChannel
				+ ", enableAction=" + enableAction + ", eventOrigin=" + eventOrigin + "]";
	}
	
	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
	}

}
