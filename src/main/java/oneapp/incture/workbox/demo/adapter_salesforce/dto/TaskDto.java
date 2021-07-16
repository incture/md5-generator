package oneapp.incture.workbox.demo.adapter_salesforce.dto;

import java.util.List;

public class TaskDto {

	private String action;
	private String actionType;
	private String instanceId;
	private String comment;
	private String Processor;
	private String origin;
	private String subject;
	private String ownerEmail;
	private String sendToUser;
	private String sendToUserDisplay;
	private String processType;
	private String processId;
	private String purchaseOrder;
	private String prRelCode;
	private String purchaseReq;
	private String poRelCode;
	private String bnfPo;
	private List<String> groupName;
	private String taskName;
	@Override
	public String toString() {
		return "TaskDto [action=" + action + ", actionType=" + actionType + ", instanceId=" + instanceId + ", comment="
				+ comment + ", Processor=" + Processor + ", origin=" + origin + ", subject=" + subject + ", ownerEmail="
				+ ownerEmail + ", sendToUser=" + sendToUser + ", sendToUserDisplay=" + sendToUserDisplay
				+ ", processType=" + processType + ", processId=" + processId + ", purchaseOrder=" + purchaseOrder
				+ ", prRelCode=" + prRelCode + ", purchaseReq=" + purchaseReq + ", poRelCode=" + poRelCode + ", bnfPo="
				+ bnfPo + ", groupName=" + groupName + ", taskName=" + taskName + "]";
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
	public String getProcessor() {
		return Processor;
	}
	public void setProcessor(String processor) {
		Processor = processor;
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
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
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
	public String getPrRelCode() {
		return prRelCode;
	}
	public void setPrRelCode(String prRelCode) {
		this.prRelCode = prRelCode;
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
	public List<String> getGroupName() {
		return groupName;
	}
	public void setGroupName(List<String> groupName) {
		this.groupName = groupName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	
	
}
