package oneapp.incture.workbox.demo.chat.dto;

public class ChatResponseDto {

	String chatId;
	String userId;
	String chatName; 
	String chatType;
	MessageDetailDto message;
	Boolean isPinned;
	
	public Boolean getIsPinned() {
		return isPinned;
	}
	public void setIsPinned(Boolean isPinned) {
		this.isPinned = isPinned;
	}
	@Override
	public String toString() {
		return "ChatResponseDto [chatId=" + chatId + ", userId=" + userId + ", chatName=" + chatName + ", chatType="
				+ chatType + ", message=" + message + ", isPinned=" + isPinned + "]";
	}
	public MessageDetailDto getMessage() {
		return message;
	}
	public void setMessage(MessageDetailDto message) {
		this.message = message;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	public String getChatType() {
		return chatType;
	}
	public void setChatType(String chatType) {
		this.chatType = chatType;
	}
	
	
	
}
