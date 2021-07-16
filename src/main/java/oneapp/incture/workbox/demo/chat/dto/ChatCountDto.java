package oneapp.incture.workbox.demo.chat.dto;

public class ChatCountDto {

	String chatId;
	Integer noOfChat;
	
	@Override
	public String toString() {
		return "ChatCountDto [chatId=" + chatId + ", noOfChat=" + noOfChat + "]";
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public Integer getNoOfChat() {
		return noOfChat;
	}
	public void setNoOfChat(Integer noOfChat) {
		this.noOfChat = noOfChat;
	}

	
}
