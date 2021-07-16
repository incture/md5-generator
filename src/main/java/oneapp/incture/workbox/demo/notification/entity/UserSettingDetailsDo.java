package oneapp.incture.workbox.demo.notification.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "USER_SETTING_DETAILS")
public class UserSettingDetailsDo implements BaseDo,Serializable{


	private static final long serialVersionUID = -338057976909649600L;

	@Id
	@Column(name = "PROFILE_SETTING_ID" , columnDefinition = "VARCHAR(255)")
	private String profileSettingId;
	
	@Id
	@Column(name = "ADDITONAL_SETTING_ID" , columnDefinition = "VARCHAR(255)")
	private String additionalSettingId;
	
	@Column(name = "VALUE" , columnDefinition = "VARCHAR(255)")
	private String value;
	
	@Column(name = "MORE" , columnDefinition = "VARCHAR(255)")
	private String more;
	
	@Column(name = "DEFAULT")
	private Boolean isDefault;
	
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getProfileSettingId() {
		return profileSettingId;
	}

	public void setProfileSettingId(String profileSettingId) {
		this.profileSettingId = profileSettingId;
	}

	public String getAdditionalSettingId() {
		return additionalSettingId;
	}

	public void setAdditionalSettingId(String additionalSettingId) {
		this.additionalSettingId = additionalSettingId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	
	@Override
	public String toString() {
		return "UserSettingDetailsDo [profileSettingId=" + profileSettingId + ", additionalSettingId="
				+ additionalSettingId + ", value=" + value + ", more=" + more + ", isDefault=" + isDefault + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}	
	
}
