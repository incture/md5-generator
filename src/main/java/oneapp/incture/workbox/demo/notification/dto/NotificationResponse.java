package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;
import java.util.Set;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class NotificationResponse {

	List<NotificationDto> notificationDtos;
	Set<String> userIds;
	ResponseMessage responseMessage;


	public Set<String> getUserIds() {
		return userIds;
	}
	public void setUserIds(Set<String> userIds) {
		this.userIds = userIds;
	}
	public List<NotificationDto> getNotificationDtos() {
		return notificationDtos;
	}
	public void setNotificationDtos(List<NotificationDto> notificationDtos) {
		this.notificationDtos = notificationDtos;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	

}
