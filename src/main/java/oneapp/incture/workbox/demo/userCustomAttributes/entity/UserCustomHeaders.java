package oneapp.incture.workbox.demo.userCustomAttributes.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_CUSTOM_HEADERS")
public class UserCustomHeaders implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7759054661186172760L;

	@Id
	@Column(name = "USER_ID")
	private String userId;
	
	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;
	
	@Id
	@Column(name = "KEY")
	private String key;
	
	@Column(name = "LABEL")
	private String label;
	
	@Column(name = "SEQUENCE")
	private Integer sequence;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "DATA_TYPE")
	private String dataType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public String toString() {
		return "UserCustomHeaders [userId=" + userId + ", processName=" + processName + ", key=" + key + ", label="
				+ label + ", sequence=" + sequence + ", isActive=" + isActive + ", dataType=" + dataType + "]";
	}

	

	
}
