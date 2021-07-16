package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;

public class AdhocActionDto {

	String userId;
	String sendTOUser;
	String actionType;
	String comment;
	String instanceId;
	String processName;
	List<CustomAttributeTemplateDto> attrValues;

	@Override
	public String toString() {
		return "AdhocActionDto [userId=" + userId + ", sendTOUser=" + sendTOUser + ", actionType=" + actionType
				+ ", comment=" + comment + ", instanceId=" + instanceId + ", processName=" + processName
				+ ", attrValues=" + attrValues + "]";
	}

	public String getUserId() {
		return userId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSendTOUser() {
		return sendTOUser;
	}

	public void setSendTOUser(String sendTOUser) {
		this.sendTOUser = sendTOUser;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public List<CustomAttributeTemplateDto> getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(List<CustomAttributeTemplateDto> attrValues) {
		this.attrValues = attrValues;
	}

}
