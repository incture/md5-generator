package oneapp.incture.workbox.demo.notification.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class NotificationProfileSettingDto extends BaseDto {

	private String profileName;

	private String userId;

	private String profileId;

	private String settingId;

	private Boolean isActive;

	private String scheduledFrom;

	private String scheduledTo;
	
	private Boolean isEnable;
	private String validForUsage;

	public void setValidForUsage(String validForUsage) {
		this.validForUsage = validForUsage;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getSettingId() {
		return settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}


	public String getScheduledFrom() {
		return scheduledFrom;
	}

	public void setScheduledFrom(String scheduledFrom) {
		this.scheduledFrom = scheduledFrom;
	}

	public String getScheduledTo() {
		return scheduledTo;
	}

	public void setScheduledTo(String scheduledTo) {
		this.scheduledTo = scheduledTo;
	}

	@Override
	public String toString() {
		return "NotificationProfileSettingDto [profileName=" + profileName + ", userId=" + userId + ", profileId="
				+ profileId + ", settingId=" + settingId + ", isActive=" + isActive + ", scheduledFrom=" + scheduledFrom
				+ ", scheduledTo=" + scheduledTo + ", isEnable=" + isEnable + ", validForUsage=" + validForUsage + "]";
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

}