package oneapp.incture.workbox.demo.substitution.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class UserTaskMappingDto extends BaseDto {
	
	private String substitutingUser;
	private String substitutedUser;
	private String taskId;
	
	
	public String getSubstitutingUser() {
		return substitutingUser;
	}
	public void setSubstitutingUser(String substitutingUser) {
		this.substitutingUser = substitutingUser;
	}
	public String getSubstitutedUser() {
		return substitutedUser;
	}
	public void setSubstitutedUser(String substitutedUser) {
		this.substitutedUser = substitutedUser;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Override
	public String toString() {
		return "UserTaskMappingDto [substitutingUser=" + substitutingUser + ", substitutedUser=" + substitutedUser
				+ ", taskId=" + taskId + "]";
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
