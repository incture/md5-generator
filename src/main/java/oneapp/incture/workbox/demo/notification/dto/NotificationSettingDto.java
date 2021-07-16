package oneapp.incture.workbox.demo.notification.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class NotificationSettingDto extends BaseDto {

	private String additionalSettingId;
	private String settingName;
	private String value;
	private String more;
	private String dataType;
	private String selectionList;
	private String profileSettingId;
	private Boolean isEnable;
	private Boolean isDefault;
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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getSelectionList() {
		return selectionList;
	}

	public void setSelectionList(String selectionList) {
		this.selectionList = selectionList;
	}

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	

	public String getAdditionalSettingId() {
		return additionalSettingId;
	}

	public void setAdditionalSettingId(String additionalSettingId) {
		this.additionalSettingId = additionalSettingId;
	}

	

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

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

	public String getProfileSettingId() {
		return profileSettingId;
	}

	public void setProfileSettingId(String profileSettingId) {
		this.profileSettingId = profileSettingId;
	}

	@Override
	public String toString() {
		return "NotificationSettingDto [additionalSettingId=" + additionalSettingId + ", settingName=" + settingName
				+ ", value=" + value + ", more=" + more + ", dataType=" + dataType + ", selectionList=" + selectionList
				+ ", profileSettingId=" + profileSettingId + ", isEnable=" + isEnable + ", isDefault=" + isDefault
				+ ", validForUsage=" + validForUsage + "]";
	}

	

}
