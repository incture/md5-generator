package oneapp.incture.workbox.demo.substitution.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class SubstitutionGroupMappingDto extends BaseDto{

	private String process;
	private String userGroupId;
	private String groupName;

	

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(String userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	
	public SubstitutionGroupMappingDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SubstitutionGroupMappingDto [process=" + process + ", userGroupId=" + userGroupId + ", groupName="
				+ groupName + "]";
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
