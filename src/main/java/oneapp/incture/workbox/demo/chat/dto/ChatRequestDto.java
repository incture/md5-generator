package oneapp.incture.workbox.demo.chat.dto;

import java.util.List;

public class ChatRequestDto {

	String chatId;
	String userId;
	String chatType;
	String chatName;
	Boolean isPinned;
	Integer page;
	List<String> users;
	
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public List<String> getUsers() {
		return users;
	}
	
	public Boolean getIsPinned() {
		return isPinned;
	}

	public void setIsPinned(Boolean isPinned) {
		this.isPinned = isPinned;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public ChatRequestDto(String chatId, String userId, String chatType, String chatName) {
		super();
		this.chatId = chatId;
		this.userId = userId;
		this.chatType = chatType;
		this.chatName = chatName;
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
	public ChatRequestDto() {
		super();
	}
	public ChatRequestDto(String chatId, String userId) {
		super();
		this.chatId = chatId;
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "ChatRequestDto [chatId=" + chatId + ", userId=" + userId + ", chatType=" + chatType + ", chatName="
				+ chatName + ", isPinned=" + isPinned + ", page=" + page + ", users=" + users + "]";
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
	
	
}
