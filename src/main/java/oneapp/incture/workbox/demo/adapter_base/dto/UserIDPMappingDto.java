package oneapp.incture.workbox.demo.adapter_base.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserIDPMappingDto{

	private String userFirstName;
	private String userLastName;
	private String userEmail;
	private String userLoginName;
	private String userId;
	private String zohoId;
	private String refreshToken;
	private String docusignId;
	private String jiraId;
	
	public String getJiraId() {
		return jiraId;
	}

	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
	}

	public String getDocusignId() {
		return docusignId;
	}

	public void setDocusignId(String docusignId) {
		this.docusignId = docusignId;
	}

	public String getZohoId() {
		return zohoId;
	}

	public void setZohoId(String zohoId) {
		this.zohoId = zohoId;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
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
				+ userEmail + ", userLoginName=" + userLoginName + ", userId=" + userId + "]";
	}

	

	
	
	

}
