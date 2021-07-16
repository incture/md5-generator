package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: ProcessEventsDo
 *
 */
@Entity
@Table(name = "PROCESS_CONFIG_TB")
public class ProcessConfigDo implements BaseDo, Serializable {

	

	private static final long serialVersionUID = 1L;

	public ProcessConfigDo() {
		super();
	}

	@Id
	@Column(name = "PROCESS_NAME", length = 100)
	private String processName;

	@Column(name = "LABEL_NAME", length = 100)
	private String labelName;
	
	@Column(name = "USER_ROLE", length = 1000)
	private String userRole;
	
	@Column(name = "SLA", length = 20)
	private String sla;
	
	@Column(name = "URGENT_SLA", length = 20)
	private String urgentSla;
	
	@Column(name = "PROCESS_DISPLAY_NAME", length = 100)
	private String processDisplayName;
	
	@Column(name = "USER_GROUP", length = 1000)
	private String userGroup;
	
	@Column(name = "PROCESS_CONFIG_ID", length = 50)
	private String processConfigId =UUID.randomUUID().toString().replaceAll("-", "");
	
	@Column(name = "PROCESS_SUBJECT", length = 250)
	private String subject;
	
	@Column(name = "PROCESS_DESC", length = 250)
	private String description;
	
	@Column(name = "LANE_COUNT")
	private Integer laneCount;
	
	@Column(name = "PROCESS_REQUEST_ID")
	private String processRequestId;
	
	@Column(name = "PROCESS_TYPE")
	private String processType;
	
	@Column(name = "ORIGIN")
	private String origin;
	
	@Column(name = "PROCESS_COLOR")
	private String processColor;
	
	@Column(name = "CRITICAL_DATE")
	private String criticalDate;
	
	@Column(name = "URL", length = 255)
	private String url;
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProcessColor() {
		return processColor;
	}

	public void setProcessColor(String processColor) {
		this.processColor = processColor;
	}

	public String getProcessRequestId() {
		return processRequestId;
	}

	public void setProcessRequestId(String processRequestId) {
		this.processRequestId = processRequestId;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getProcessConfigId() {
		return processConfigId;
	}

	public void setProcessConfigId(String processConfigId) {
		this.processConfigId = processConfigId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLaneCount() {
		return laneCount;
	}

	public void setLaneCount(Integer laneCount) {
		this.laneCount = laneCount;
	}

	
	
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	
	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	public String getUrgentSla() {
		return urgentSla;
	}

	public void setUrgentSla(String urgentSla) {
		this.urgentSla = urgentSla;
	}
	
	public String getProcessDisplayName() {
		return processDisplayName;
	}

	public void setProcessDisplayName(String processDisplayName) {
		this.processDisplayName = processDisplayName;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	
	public String getCriticalDate() {
		return criticalDate;
	}

	public void setCriticalDate(String criticalDate) {
		this.criticalDate = criticalDate;
	}

	/**
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}


	@Override
	public String toString() {
		return "ProcessConfigDo [processName=" + processName + ", labelName=" + labelName + ", userRole=" + userRole
				+ ", sla=" + sla + ", urgentSla=" + urgentSla + ", processDisplayName=" + processDisplayName
				+ ", userGroup=" + userGroup + ", processConfigId=" + processConfigId + ", subject=" + subject
				+ ", description=" + description + ", laneCount=" + laneCount + ", processRequestId=" + processRequestId
				+ ", processType=" + processType + ", origin=" + origin + ", processColor=" + processColor
				+ ", criticalDate=" + criticalDate + ", url=" + url + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

//	public Object getPrimaryKey() {
//		return processName;
//	}

}
