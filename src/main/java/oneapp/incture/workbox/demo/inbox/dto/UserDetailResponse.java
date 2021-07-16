package oneapp.incture.workbox.demo.inbox.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class UserDetailResponse {

	UserIDPMappingDto userIDPMappingDto;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "UserDetailResponse [userIDPMappingDto=" + userIDPMappingDto + ", responseMessage=" + responseMessage
				+ "]";
	}
	public UserIDPMappingDto getUserIDPMappingDto() {
		return userIDPMappingDto;
	}
	public void setUserIDPMappingDto(UserIDPMappingDto userIDPMappingDto) {
		this.userIDPMappingDto = userIDPMappingDto;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
}
