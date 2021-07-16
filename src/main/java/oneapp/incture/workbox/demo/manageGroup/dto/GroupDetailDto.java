package oneapp.incture.workbox.demo.manageGroup.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class GroupDetailDto {

	String groupName;
	String groupId;
	List<UserDetailDto> userDetail;
	ResponseMessage responseMessage;
	
	public List<UserDetailDto> getUserDetail() {
		return userDetail;
	}
	public void setUserDetail(List<UserDetailDto> userDetail) {
		this.userDetail = userDetail;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "GroupDetailDto [groupName=" + groupName + ", groupId=" + groupId + ", responseMessage="
				+ responseMessage + "]";
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}
