package oneapp.incture.workbox.demo.substitution.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "SUBSTITUTION_RULE")

public class SubstitutionRuleDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "RULE_ID", length = 70)
	private String ruleId = UUID.randomUUID().toString().replaceAll("-", "");
	
	@Id
	@Column(name = "VERSION", length = 10)
	private int version;

	@Column(name = "SUBSTITUTED_USER", length = 70)
	private String substitutedUser;

	@Column(name = "SUBSTITUTED_USER_NAME", length = 70)
	private String substitutedUserName;

	@Column(name = "SUBSTITUTING_USER", length = 70)
	private String substitutingUser;

	@Column(name = "SUBSTITUTING_USER_NAME", length = 70)
	private String substitutingUserName;

	@Column(name = "END_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "START_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "IS_ACTIVE")
	private boolean isActive;

	@Column(name = "IS_ENABLE")
	private boolean isEnabled;
	
	@Column(name = "IS_DELETED")
	private boolean isDeleted;

	@Column(name = "UPDATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@Column(name = "CREATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "MODIFIED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedAt;

	@Column(name = "CREATED_BY", length = 70)
	private String createdBy;
	@Column(name = "CREATED_BY_DISP", length = 255)
	private String createdByDisp;

	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedByDisp() {
		return createdByDisp;
	}

	public void setCreatedByDisp(String createdByDisp) {
		this.createdByDisp = createdByDisp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Object getPrimaryKey() {
		return ruleId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getSubstitutedUser() {
		return substitutedUser;
	}

	public void setSubstitutedUser(String substitutedUser) {
		this.substitutedUser = substitutedUser;
	}

	public String getSubstitutedUserName() {
		return substitutedUserName;
	}

	public void setSubstitutedUserName(String substitutedUserName) {
		this.substitutedUserName = substitutedUserName;
	}

	public String getSubstitutingUser() {
		return substitutingUser;
	}

	public void setSubstitutingUser(String substitutingUser) {
		this.substitutingUser = substitutingUser;
	}

	public String getSubstitutingUserName() {
		return substitutingUserName;
	}

	public void setSubstitutingUserName(String substitutingUserName) {
		this.substitutingUserName = substitutingUserName;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	
	

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "SubstitutionRuleDo [ruleId=" + ruleId + ", version=" + version + ", substitutedUser=" + substitutedUser
				+ ", substitutedUserName=" + substitutedUserName + ", substitutingUser=" + substitutingUser
				+ ", substitutingUserName=" + substitutingUserName + ", endDate=" + endDate + ", startDate=" + startDate
				+ ", isActive=" + isActive + ", isEnabled=" + isEnabled + ", isDeleted=" + isDeleted + ", updatedAt="
				+ updatedAt + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + ", createdBy=" + createdBy
				+ ", createdByDisp=" + createdByDisp + "]";
	}

	
	

}