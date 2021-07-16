package oneapp.incture.workbox.demo.cai.chatbot.dto;

public class UserInfo {

	private String userId;
	private String name;
	private String userDispName;
	private String emailId;
	private boolean isAdmin;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserDispName() {
		return userDispName;
	}

	public void setUserDispName(String userDispName) {
		this.userDispName = userDispName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", name=" + name + ", userDispName=" + userDispName + ", emailId="
				+ emailId + ", isAdmin=" + isAdmin + "]";
	}

}
