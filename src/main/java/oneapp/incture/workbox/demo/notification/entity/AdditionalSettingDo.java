package oneapp.incture.workbox.demo.notification.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "ADDITIONAL_SETTING")
public class AdditionalSettingDo implements BaseDo,Serializable{


	private static final long serialVersionUID = -338057976909649600L;

	@Column(name = "DATATYPE" , columnDefinition = "VARCHAR(255)")
	private String dataType;
	
	@Column(name = "SETTING_NAME" , columnDefinition = "VARCHAR(255)")
	private String settingName;
	
	@Id
	@Column(name = "ADDITONAL_SETTING_ID" , columnDefinition = "VARCHAR(255)")
	private String additionalSettingId;
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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
	public String toString() {
		return "AdditionalSettingDo [dataType=" + dataType + ", settingName=" + settingName + ", additionalSettingId="
				+ additionalSettingId + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}	
	
}
