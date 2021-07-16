package oneapp.incture.workbox.demo.notification.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "NOTIFICATION_PROFILE_SETTING")
public class NotificationProfileSettingDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	
	@Column(name = "PROFILE_NAME", length = 255)
	private String profileName;

	@Column(name = "USER_ID", length = 255)
	private String userId;

	@Id
	@Column(name = "PROFILE_ID", length = 255)
	private String profileId;

	@Column(name = "SETTING_ID", length = 255)
	private String settingId;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "SCHEDULED_FROM")
	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledFrom;

	
	@Column(name = "SCHEDULED_TO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ScheduledTo;

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

	public Date getScheduledFrom() {
		return scheduledFrom;
	}

	public void setScheduledFrom(Date scheduledFrom) {
		this.scheduledFrom = scheduledFrom;
	}

	public Date getScheduledTo() {
		return ScheduledTo;
	}

	public void setScheduledTo(Date ScheduledTo) {
		this.ScheduledTo = ScheduledTo;
	}

	public String toString() {
		return "NotificationProfileSettingDo [profileName=" + profileName + ", userId=" + userId + ", profileId="
				+ profileId + ", settingId=" + settingId + ", isActive=" + isActive + ", scheduledFrom=" + scheduledFrom
				+ ", ScheduledTo=" + ScheduledTo + "]";
	}

	public Object getPrimaryKey() {
		return profileId;
	}

}