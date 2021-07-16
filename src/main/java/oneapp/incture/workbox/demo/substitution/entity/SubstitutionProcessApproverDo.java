package oneapp.incture.workbox.demo.substitution.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "SUBSTITUTION_PROCESS_APPROVER")
public class SubstitutionProcessApproverDo implements BaseDo , Serializable{
	
	private static final long serialVersionUID = 122L;
	
	@Id
	@Column(name = "PROCESS")
	private String process;
	
	@Id
	@Column(name = "APPROVING_USER")
	private String approvingUser;
	
	@Column(name = "PROCESS_DISPLAY_NAME")
	private String processDisplayName;
	
	
	
	@Column(name = "APPROVING_USER_NAME")
	private String approvingUserName;
	
	@Column(name = "IS_ACTIVATED")
	private boolean isActivated;
	
	@Column(name = "IS_APPROVEL_REQUIRED")
	private boolean isApprovelRequired;
	
	@Column(name = "IS_DELETED")
	private boolean isDeleted = false;
	
	

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getProcessDisplayName() {
		return processDisplayName;
	}

	public void setProcessDisplayName(String processDisplayName) {
		this.processDisplayName = processDisplayName;
	}

	public String getApprovingUser() {
		return approvingUser;
	}

	public void setApprovingUser(String approvingUser) {
		this.approvingUser = approvingUser;
	}

	public String getApprovingUserName() {
		return approvingUserName;
	}

	public void setApprovingUserName(String approvingUserName) {
		this.approvingUserName = approvingUserName;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	
	
	public boolean isApprovelRequired() {
		return isApprovelRequired;
	}

	public void setApprovelRequired(boolean isApprovelRequired) {
		this.isApprovelRequired = isApprovelRequired;
	}
	

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
    public Object getPrimaryKey() {
        // TODO Auto-generated method stub
        return null;
    }
	

}
