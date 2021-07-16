package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class NotificationProfileResponseDto {

	List<NotificationProfileSettingDto> profileDto;
	ResponseMessage responseMessage;
	public List<NotificationProfileSettingDto> getProfileDto() {
		return profileDto;
	}
	public void setProfileDto(List<NotificationProfileSettingDto> profileDto) {
		this.profileDto = profileDto;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "NotificationProfileResponseDto [profileDto=" + profileDto + ", responseMessage=" + responseMessage
				+ "]";
	}


	
}
