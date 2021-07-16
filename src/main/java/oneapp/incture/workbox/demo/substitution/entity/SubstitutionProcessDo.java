package oneapp.incture.workbox.demo.substitution.entity;

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
@Table(name = "SUBSTITUTION_PROCESSES")

public class SubstitutionProcessDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	// @Id
	// @Column(name = "PROCESS_RULE_ID", length = 70)
	// private String processRuleId =
	// UUID.randomUUID().toString().replaceAll("-", "");

	@Id
	@Column(name = "RULE_ID", length = 70)
	private String ruleId;

	@Id
	@Column(name = "PROCESS", length = 70)
	private String process;
	
	@Id
	@Column(name = "VERSION", length = 10)
	private int version;
	
	@Column(name = "STATUS")
	private int status;
	
	@Column(name = "STATUS_DEF")
	private String statusDef;
	
	@Column(name = "APPROVED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvedAt;
	
	

	public Date getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(Date approvedAt) {
		this.approvedAt = approvedAt;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}
	
	

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusDef() {
		return statusDef;
	}

	public void setStatusDef(String statusDef) {
		this.statusDef = statusDef;
	}

	
	@Override
	public String toString() {
		return "SubstitutionProcessDo [ruleId=" + ruleId + ", process=" + process + ", version=" + version + ", status="
				+ status + ", statusDef=" + statusDef + "]";
	}

	public Object getPrimaryKey() {
		return ruleId;
	}

}