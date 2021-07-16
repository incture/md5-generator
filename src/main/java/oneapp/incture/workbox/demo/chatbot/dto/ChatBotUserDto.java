package oneapp.incture.workbox.demo.chatbot.dto;


public class ChatBotUserDto  {
	private String userId;
	private String userName;
	private String isAdmin;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	@Override
	public String toString() {
		return "ChatBotUserDto [userId=" + userId + ", userName=" + userName + ", isAdmin=" + isAdmin + "]";
	}

	
}
