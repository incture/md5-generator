package oneapp.incture.workbox.demo.manageGroup.dto;

public class UserDetailDto {

	String userName;
	String userId;
	Integer isEdited;
	@Override
	public String toString() {
		return "UserDetailDto [userName=" + userName + ", userId=" + userId + ", isEdited=" + isEdited + "]";
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
	public Integer getIsEdited() {
		return isEdited;
	}
	public void setIsEdited(Integer isEdited) {
		this.isEdited = isEdited;
	}
	
	
}
