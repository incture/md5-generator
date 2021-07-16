package oneapp.incture.workbox.demo.chat.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class ChatResposne {

	String chatId;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "ChatResposne [chatId=" + chatId + ", responseMessage=" + responseMessage + "]";
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
