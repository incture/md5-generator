package oneapp.incture.workbox.demo.chat.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class ChatUserResponse {

	List<UserDetail> userDetails;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "ChatUserResponse [userDetails=" + userDetails + ", responseMessage=" + responseMessage + "]";
	}
	public List<UserDetail> getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(List<UserDetail> userDetails) {
		this.userDetails = userDetails;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
	
	
}
