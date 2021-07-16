package oneapp.incture.workbox.demo.chat.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class ChatDetailsDto {

	List<ChatResponseDto> chatResponseDtos;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "ChatDetailsDto [chatResponseDtos=" + chatResponseDtos + ", responseMessage=" + responseMessage + "]";
	}
	public List<ChatResponseDto> getChatResponseDtos() {
		return chatResponseDtos;
	}
	public void setChatResponseDtos(List<ChatResponseDto> chatResponseDtos) {
		this.chatResponseDtos = chatResponseDtos;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
}
