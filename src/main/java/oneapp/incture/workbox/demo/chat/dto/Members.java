package oneapp.incture.workbox.demo.chat.dto;

public class Members {

	String userName;
	String userId;
	@Override
	public String toString() {
		return "Members [userName=" + userName + ", userId=" + userId + "]";
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
