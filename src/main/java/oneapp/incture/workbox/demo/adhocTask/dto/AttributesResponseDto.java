package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class AttributesResponseDto {

	List<ProcessAttributesDto> listOfProcesssAttributes;
	String type;
	String resourceid;
	String actionType;
	String processName;
	String processId;
	String isEdited;
	String requestId;
	ResponseMessage responseMessage ;
	@Override
	public String toString() {
		return "AttributesResponseDto [listOfProcesssAttributes=" + listOfProcesssAttributes + ", type=" + type
				+ ", resourceid=" + resourceid + ", actionType=" + actionType + ", processName=" + processName
				+ ", processId=" + processId + ", isEdited=" + isEdited + ", requestId=" + requestId
				+ ", responseMessage=" + responseMessage + "]";
	}
	public List<ProcessAttributesDto> getListOfProcesssAttributes() {
		return listOfProcesssAttributes;
	}
	public void setListOfProcesssAttributes(List<ProcessAttributesDto> listOfProcesssAttributes) {
		this.listOfProcesssAttributes = listOfProcesssAttributes;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getResourceid() {
		return resourceid;
	}
	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getIsEdited() {
		return isEdited;
	}
	public void setIsEdited(String isEdited) {
		this.isEdited = isEdited;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
