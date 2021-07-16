package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class NotificationRequestDto {

	private List<NotificationChannelDto> notificationChanneldtos;
	private List<NotificationActionDto> notificationactiondtos;
	private List<String> actionTypes;
	private List<String> channels;
	private Integer timeRange;
	private String userId;
	private ResponseMessage responseMessage;
	public NotificationRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NotificationRequestDto(List<NotificationChannelDto> notificationChanneldtos, List<String> actionTypes,
			List<String> channels, Integer timeRange, String userId, ResponseMessage responseMessage) {
		super();
		this.notificationChanneldtos = notificationChanneldtos;
		this.actionTypes = actionTypes;
		this.channels = channels;
		this.timeRange = timeRange;
		this.userId = userId;
		this.responseMessage = responseMessage;
	}
	
	
	public List<NotificationActionDto> getNotificationactiondtos() {
		return notificationactiondtos;
	}
	public void setNotificationactiondtos(List<NotificationActionDto> notificationactiondtos) {
		this.notificationactiondtos = notificationactiondtos;
	}
	public List<NotificationChannelDto> getNotificationChanneldtos() {
		return notificationChanneldtos;
	}
	public void setNotificationChanneldtos(List<NotificationChannelDto> notificationChanneldtos) {
		this.notificationChanneldtos = notificationChanneldtos;
	}
	public List<String> getActionTypes() {
		return actionTypes;
	}
	public void setActionTypes(List<String> actionTypes) {
		this.actionTypes = actionTypes;
	}
	public List<String> getChannels() {
		return channels;
	}
	public void setChannels(List<String> channels) {
		this.channels = channels;
	}
	public Integer getTimeRange() {
		return timeRange;
	}
	public void setTimeRange(Integer timeRange) {
		this.timeRange = timeRange;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "NotificationRequestDto [notificationChanneldtos=" + notificationChanneldtos
				+ ", notificationactiondtos=" + notificationactiondtos + ", actionTypes=" + actionTypes + ", channels="
				+ channels + ", timeRange=" + timeRange + ", userId=" + userId + ", responseMessage=" + responseMessage
				+ "]";
	}
		
}
