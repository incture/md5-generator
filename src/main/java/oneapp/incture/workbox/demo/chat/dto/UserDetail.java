package oneapp.incture.workbox.demo.chat.dto;

public class UserDetail {

	String userId;
	String userFirstName;
	String userLastName;
	String userEmail;
	String compressedImage;
	String chatId;
	@Override
	public String toString() {
		return "ChatUserResponse [userId=" + userId + ", userFirstName=" + userFirstName + ", userLastName="
				+ userLastName + ", userEmail=" + userEmail + ", compressedImage=" + compressedImage + ", chatId="
				+ chatId + "]";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getCompressedImage() {
		return compressedImage;
	}
	public void setCompressedImage(String compressedImage) {
		this.compressedImage = compressedImage;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	
	
}
