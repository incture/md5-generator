package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class NotificationDetail {

	List<NotificationResponseDto> notifications;
	ResponseMessage responseMessage;
	Integer totalNotificationCount;
	Integer count;
	String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "NotificationDetail [notifications=" + notifications + ", responseMessage=" + responseMessage
				+ ", totalNotificationCount=" + totalNotificationCount + ", count=" + count + ", userId=" + userId
				+ "]";
	}
	public List<NotificationResponseDto> getNotifications() {
		return notifications;
	}
	public void setNotifications(List<NotificationResponseDto> notifications) {
		this.notifications = notifications;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public Integer getTotalNotificationCount() {
		return totalNotificationCount;
	}
	public void setTotalNotificationCount(Integer totalNotificationCount) {
		this.totalNotificationCount = totalNotificationCount;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	

}

