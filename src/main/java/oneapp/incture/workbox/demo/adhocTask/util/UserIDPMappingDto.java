package oneapp.incture.workbox.demo.adhocTask.util;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserIDPMappingDto{

	private String userFirstName;
	private String userLastName;
	private String userEmail;
	private String userLoginName;
	private String userId;
	private String userRole;
	

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
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

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	@Override
	public String toString() {
		return "UserIDPMappingDto [userFirstName=" + userFirstName + ", userLastName=" + userLastName + ", userEmail="
				+ userEmail + ", userLoginName=" + userLoginName + ", userId=" + userId + ", userRole=" + userRole
				+ "]";
	}
	
	

}
