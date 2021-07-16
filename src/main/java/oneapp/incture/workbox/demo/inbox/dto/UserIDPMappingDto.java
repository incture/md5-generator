package oneapp.incture.workbox.demo.inbox.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;



@XmlRootElement
public class UserIDPMappingDto extends BaseDto {

	private String serialId;
	private String userFirstName;
	private String userLastName;
	private String userEmail;
	private String userRole;
	private String userLoginName;
	private String userId;
	private String theme;
	private String profilePic;
	private String compressedImage;
	private String imageType;
	private String taskAssignable;
	private String sharepointId;
	private String successFactorsId;
	private String salesForceId;
	private String language;
	private Integer budget;
	private Boolean validForUsage;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getCompressedImage() {
		return compressedImage;
	}

	public void setCompressedImage(String compressedImage) {
		this.compressedImage = compressedImage;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
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

	@Override
	public String toString() {
		return "UserIDPMappingDto [serialId=" + serialId + ", userFirstName=" + userFirstName + ", userLastName="
				+ userLastName + ", userEmail=" + userEmail + ", userRole=" + userRole + ", userLoginName="
				+ userLoginName + ", userId=" + userId + ", theme=" + theme + ", profilePic=" + profilePic
				+ ", compressedImage=" + compressedImage + ", imageType=" + imageType + ", taskAssignable="
				+ taskAssignable + ", sharepointId=" + sharepointId + ", successFactorsId=" + successFactorsId
				+ ", salesForceId=" + salesForceId + ", language=" + language + ", budget=" + budget + "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	public Integer getBudget() {
		return budget;
	}

	public void setBudget(Integer budget) {
		this.budget = budget;
	}

}
