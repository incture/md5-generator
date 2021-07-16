package oneapp.incture.workbox.demo.inbox.entity;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;



@Entity
@Table(name = "USER_IDP_MAPPING")
public class UserIDPMappingDo implements BaseDo {

	@Column(name = "SERIAL_ID", length = 150)
	private String serialId;

	@Column(name = "USER_ID", length = 100)
	private String userId;

	@Column(name = "USER_FIRST_NAME", length = 100)
	private String userFirstName;

	@Column(name = "USER_LAST_NAME", length = 100)
	private String userLastName;

	@Column(name = "USER_EMAIL", length = 100)
	private String userEmail;

	@Column(name = "USER_ROLE", length = 80)
	private String userRole;

	@Id
	@Column(name = "USER_LOGIN_NAME", length = 100)
	private String userLoginName;

	@Column(name = "TASK_ASSIGNABLE", length = 10)
	private String taskAssignable;

	@Column(name = "SHAREPOINT_ID", length = 10)
	private String sharepointId;

	@Column(name = "SUCCESSFACTORS_ID", length = 20)
	private String successFactorsId;
	
	@Column(name = "SALESFORCE_ID", length = 20)
	private String salesForceId;
	
	@Column(name = "THEME", length = 50)
	private String theme;
	
	@Column(name = "LANGUAGE",length = 50)
	private String language;

	@Column(name = "PROFILE_PIC")
	private byte[] profilePic;
	
	@Column(name = "COMPRESSED_IMAGE")
	private byte[] compressedImage;
	
	@Column(name = "AFE_NEXUS_BUDGET")
    private Integer afeNexusBudget;

	@Override
	public String toString() {
		return "UserIDPMappingDo [serialId=" + serialId + ", userId=" + userId + ", userFirstName=" + userFirstName
				+ ", userLastName=" + userLastName + ", userEmail=" + userEmail + ", userRole=" + userRole
				+ ", userLoginName=" + userLoginName + ", taskAssignable=" + taskAssignable + ", sharepointId="
				+ sharepointId + ", successFactorsId=" + successFactorsId + ", salesForceId=" + salesForceId
				+ ", theme=" + theme + ", language=" + language + ", profilePic=" + Arrays.toString(profilePic)
				+ ", compressedImage=" + Arrays.toString(compressedImage) + ", afeNexusBudget=" + afeNexusBudget + "]";
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public byte[] getCompressedImage() {
		return compressedImage;
	}

	public void setCompressedImage(byte[] compressedImage) {
		this.compressedImage = compressedImage;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
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

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getTaskAssignable() {
		return taskAssignable;
	}

	public void setTaskAssignable(String taskAssignable) {
		this.taskAssignable = taskAssignable;
	}

	public String getSharepointId() {
		return sharepointId;
	}

	public void setSharepointId(String sharepointId) {
		this.sharepointId = sharepointId;
	}

	public String getSuccessFactorsId() {
		return successFactorsId;
	}

	public void setSuccessFactorsId(String successFactorsId) {
		this.successFactorsId = successFactorsId;
	}

	public String getSalesForceId() {
		return salesForceId;
	}

	public void setSalesForceId(String salesForceId) {
		this.salesForceId = salesForceId;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public byte[] getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getAfeNexusBudget() {
		return afeNexusBudget;
	}

	public void setAfeNexusBudget(Integer afeNexusBudget) {
		this.afeNexusBudget = afeNexusBudget;
	}
	
	
}
