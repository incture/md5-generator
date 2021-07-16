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
@Table(name = "SUBSTITUTION_APPROVAL_REQUEST")

public class SubstitutionApprovalRequestDo  implements BaseDo,Serializable {
	
	private static final long serialVersionUID = 1L; 

	@Id
	@Column(name = "RULE_ID")
	private String ruleId;

	@Id
	@Column(name = "VERSION")
	private int version;
	
	@Column(name="PROCESS")
	private String process;
	
	@Column(name = "APPROVAL_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date approvalTime;
	
	@Column(name="APPROVER_ID")
	private String approverId;
	
	@Column(name="APPROVER_DISPLAY_NAME")
	private String approverDisplayName;

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Date getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}

	
	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApproverDisplayName() {
		return approverDisplayName;
	}

	public void setApproverDisplayName(String approverDisplayName) {
		this.approverDisplayName = approverDisplayName;
	}

	@Override
    public Object getPrimaryKey() {
        // TODO Auto-generated method stub
        return null;
    }
}
