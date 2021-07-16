package oneapp.incture.workbox.demo.substitution.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class SubstitutionApprovalRequestDto extends BaseDto{
	
	
	private String ruleId;
	private int version;
	private String process;
	private String approvalTime;
	private String approverId;
	private String approverDisplayName;
	private String actionType;
	
	public SubstitutionApprovalRequestDto(){}
	

	public SubstitutionApprovalRequestDto(String ruleId, int version, String process, String approvalTime, String approverId,
			String approverDisplayName) {
		
		this.ruleId = ruleId;
		this.version = version;
		this.process = process;
		this.approvalTime = approvalTime;
		this.approverId = approverId;
		this.approverDisplayName = approverDisplayName;
	}

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

	public String getApprovalTime() {
		return approvalTime;
	}

	public void setApprovalTime(String approvalTime) {
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

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Override
    public Boolean getValidForUsage() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void validate(EnOperation enOperation) throws InvalidInputFault {
        // TODO Auto-generated method stub
       
    }
	
	


}
