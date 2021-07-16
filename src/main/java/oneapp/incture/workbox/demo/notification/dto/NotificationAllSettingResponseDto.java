package oneapp.incture.workbox.demo.notification.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class NotificationAllSettingResponseDto {

	NotificationSettingResponseDto taskSettingDtos;
	NotificationSettingResponseDto chatSettingDtos;
	NotificationSettingResponseDto peopleSettingDtos;
	NotificationSettingResponseDto reportsSettingDtos;
	NotificationSettingResponseDto applicationSettingDtos;
	
	ResponseMessage message;

	public NotificationSettingResponseDto getTaskSettingDtos() {
		return taskSettingDtos;
	}

	public void setTaskSettingDtos(NotificationSettingResponseDto taskSettingDtos) {
		this.taskSettingDtos = taskSettingDtos;
	}

	public NotificationSettingResponseDto getChatSettingDtos() {
		return chatSettingDtos;
	}

	public void setChatSettingDtos(NotificationSettingResponseDto chatSettingDtos) {
		this.chatSettingDtos = chatSettingDtos;
	}

	public NotificationSettingResponseDto getPeopleSettingDtos() {
		return peopleSettingDtos;
	}

	public void setPeopleSettingDtos(NotificationSettingResponseDto peopleSettingDtos) {
		this.peopleSettingDtos = peopleSettingDtos;
	}

	public NotificationSettingResponseDto getReportsSettingDtos() {
		return reportsSettingDtos;
	}

	public void setReportsSettingDtos(NotificationSettingResponseDto reportsSettingDtos) {
		this.reportsSettingDtos = reportsSettingDtos;
	}

	public NotificationSettingResponseDto getApplicationSettingDtos() {
		return applicationSettingDtos;
	}

	public void setApplicationSettingDtos(NotificationSettingResponseDto applicationSettingDtos) {
		this.applicationSettingDtos = applicationSettingDtos;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "NotificationAllSettingResponseDto [taskSettingDtos=" + taskSettingDtos + ", chatSettingDtos="
				+ chatSettingDtos + ", peopleSettingDtos=" + peopleSettingDtos + ", reportsSettingDtos="
				+ reportsSettingDtos + ", applicationSettingDtos=" + applicationSettingDtos + ", message=" + message
				+ "]";
	}

	

	
}
