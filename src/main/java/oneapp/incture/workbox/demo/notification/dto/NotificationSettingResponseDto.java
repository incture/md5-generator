package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class NotificationSettingResponseDto {

	List<NotificationSettingDto> settingDtos;
	ResponseMessage message;

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	public List<NotificationSettingDto> getSettingDtos() {
		return settingDtos;
	}

	public void setSettingDtos(List<NotificationSettingDto> settingDtos) {
		this.settingDtos = settingDtos;
	}

	@Override
	public String toString() {
		return "NotificationSettingResponseDto [settingDtos=" + settingDtos + ", message=" + message + "]";
	}

}
