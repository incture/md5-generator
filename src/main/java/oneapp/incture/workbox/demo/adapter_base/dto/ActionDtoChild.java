package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapterJira.dto.JiraFieldGenericDto;

public class ActionDtoChild {

	private String action;
	private String actionType;
	private String sendToUser;
	private String sendToUserDisplay;
	private String instanceId;
	private String comment;
	private String origin;
	private String subject;
	private String processType;
	private String processId;
	private String purchaseOrder;
	private String relCode;
	private String purchaseReq;
	private String poRelCode;
	private String bnfPo;
	private String processLabel;
	private String requesterId;
	private String Processor;
	private String ownerEmail;
	private List<String> requesterIds;
	private String taskName;
	private List<String> groupName;
	private Boolean isAdmin;
	private String platform;//type of device Web/Android
	private String signatureVerified;// YES/NO
	private String actionTypeId;
	private JiraFieldGenericDto jiraField;
	
	
	
	public String getActionTypeId() {
		return actionTypeId;
	}
	public void setActionTypeId(String actionTypeId) {
		this.actionTypeId = actionTypeId;
	}
	public JiraFieldGenericDto getJiraField() {
		return jiraField;
	}
	public void setJiraField(JiraFieldGenericDto jiraField) {
		this.jiraField = jiraField;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getSignatureVerified() {
		return signatureVerified;
	}
	public void setSignatureVerified(String signatureVerified) {
		this.signatureVerified = signatureVerified;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public List<String> getGroupName() {
		return groupName;
	}
	public void setGroupName(List<String> groupName) {
		this.groupName = groupName;
	}
	public List<String> getRequesterIds() {
		return requesterIds;
	}
	public void setRequesterIds(List<String> requesterIds) {
		this.requesterIds = requesterIds;
	}
	@Override
	public String toString() {
		return "ActionDtoChild [action=" + action + ", actionType=" + actionType + ", sendToUser=" + sendToUser
				+ ", sendToUserDisplay=" + sendToUserDisplay + ", instanceId=" + instanceId + ", comment=" + comment
				+ ", origin=" + origin + ", subject=" + subject + ", processType=" + processType + ", processId="
				+ processId + ", purchaseOrder=" + purchaseOrder + ", relCode=" + relCode + ", purchaseReq="
				+ purchaseReq + ", poRelCode=" + poRelCode + ", bnfPo=" + bnfPo + ", processLabel=" + processLabel
				+ ", requesterId=" + requesterId + ", Processor=" + Processor + ", ownerEmail=" + ownerEmail
				+ ", requesterIds=" + requesterIds + ", taskName=" + taskName + ", groupName=" + groupName
				+ ", isAdmin=" + isAdmin + ", platform=" + platform + ", signatureVerified=" + signatureVerified + "]";
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getSendToUser() {
		return sendToUser;
	}
	public void setSendToUser(String sendToUser) {
		this.sendToUser = sendToUser;
	}
	public String getSendToUserDisplay() {
		return sendToUserDisplay;
	}
	public void setSendToUserDisplay(String sendToUserDisplay) {
		this.sendToUserDisplay = sendToUserDisplay;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getPurchaseOrder() {
		return purchaseOrder;
	}
	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
	public String getRelCode() {
		return relCode;
	}
	public void setRelCode(String relCode) {
		this.relCode = relCode;
	}
	public String getPurchaseReq() {
		return purchaseReq;
	}
	public void setPurchaseReq(String purchaseReq) {
		this.purchaseReq = purchaseReq;
	}
	public String getPoRelCode() {
		return poRelCode;
	}
	public void setPoRelCode(String poRelCode) {
		this.poRelCode = poRelCode;
	}
	public String getBnfPo() {
		return bnfPo;
	}
	public void setBnfPo(String bnfPo) {
		this.bnfPo = bnfPo;
	}
	public String getProcessLabel() {
		return processLabel;
	}
	public void setProcessLabel(String processLabel) {
		this.processLabel = processLabel;
	}
	public String getRequesterId() {
		return requesterId;
	}
	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}
	public String getProcessor() {
		return Processor;
	}
	public void setProcessor(String processor) {
		Processor = processor;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	
}
