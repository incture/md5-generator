package oneapp.incture.workbox.demo.substitution.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class SubstitutionProcessApproverDto  extends BaseDto{

	
	private String process;
	private String processDisplayName;
	private String approvingUser;
	private String approvingUserName;
	private boolean activated;
	private boolean approvelRequired;
	private boolean validForUsage;
	private boolean approverNameEditable;
	private boolean isCrossVisible;
	private boolean isTickVisible;
	private boolean crossVisible;
	private boolean tickVisible;
	private boolean processEdit;
	

	
	
	public boolean getIsCrossVisible() {
		return isCrossVisible;
	}


	public void setIsCrossVisible(boolean isCrossVisible) {
		this.isCrossVisible = isCrossVisible;
	}


	public boolean getIsTickVisible() {
		return isTickVisible;
	}


	public void setIsTickVisible(boolean isTickVisible) {
		this.isTickVisible = isTickVisible;
	}


	public SubstitutionProcessApproverDto() {
	}
	
	
	public SubstitutionProcessApproverDto(String process, String processDisplayName, String approvingUser,
			String approvingUserName, boolean isActivated, boolean isApprovelRequired) {
		super();
		this.process = process;
		this.processDisplayName = processDisplayName;
		this.approvingUser = approvingUser;
		this.approvingUserName = approvingUserName;
		this.activated = isActivated;
		this.approvelRequired = isApprovelRequired;
	}
	
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
		return activated;
	}
	public void setActivated(boolean isActivated) {
		this.activated = isActivated;
	}

	public boolean isApprovelRequired() {
		return approvelRequired;
	}

	public void setApprovelRequired(boolean isApprovelRequired) {
		this.approvelRequired = isApprovelRequired;
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


	public boolean isApproverNameEditable() {
		return approverNameEditable;
	}


	public void setApproverNameEditable(boolean approverNameEditable) {
		this.approverNameEditable = approverNameEditable;
	}

	public boolean isProcessEdit() {
		return processEdit;
	}


	public void setProcessEdit(boolean processEdit) {
		this.processEdit = processEdit;
	}


	public boolean isCrossVisible() {
		return crossVisible;
	}


	public void setCrossVisible(boolean crossVisible) {
		this.crossVisible = crossVisible;
	}


	public boolean isTickVisible() {
		return tickVisible;
	}


	public void setTickVisible(boolean tickVisible) {
		this.tickVisible = tickVisible;
	}
    


}
