package oneapp.incture.workbox.demo.chat.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class ChatFavoriteResponse {

	List<MessageDetailDto> messages;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "ChatFavoriteResponse [messages=" + messages + ", responseMessage=" + responseMessage + "]";
	}
	public List<MessageDetailDto> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageDetailDto> messages) {
		this.messages = messages;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
