package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class NotificationViewResponseDto {

	List<NotificationViewSettingsDto> viewDtos;
	ResponseMessage message;

	public List<NotificationViewSettingsDto> getViewDtos() {
		return viewDtos;
	}

	public void setViewDtos(List<NotificationViewSettingsDto> viewDtos) {
		this.viewDtos = viewDtos;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "NotificationViewResponseDto [viewDtos=" + viewDtos + ", message=" + message + "]";
	}

}
