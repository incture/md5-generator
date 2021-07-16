package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.List;

public class ActionDto {

	private String action;
	private String actionType;
	private String userId;
	private String userDisplay;
	private String Processor;
	private String ownerEmail;
	private String sendToUser;
	private String sendToUserDisplay;
	private List<ActionDtoChild> task;

	private List<String> instanceList;
	private String comment;
	private String origin;

	private String subject;
	private String processType;
	private String processId;
	private String purchaseOrder;
	private String prRelCode;
	private String purchaseReq;
	private String poRelCode;
	private String bnfPo;
	
	private String processLabel;
	private String caseId;
	private String icManagerProcessId;
	
	private List<String> tasksubjects;

	private String headerName;
	private String headerValue;
	private String processName;

	private boolean isChatbot = false;
	
	private boolean isChatBot = false;
	
	

	
	public boolean getIsChatBot() {
		return isChatBot;
	}

	public void setChatBot(boolean isChatBot) {
		this.isChatBot = isChatBot;
	}

	public String getIcManagerProcessId() {
		return icManagerProcessId;
	}

	public void setIcManagerProcessId(String icManagerProcessId) {
		this.icManagerProcessId = icManagerProcessId;
	}

	

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getHeaderValue() {
		return headerValue;
	}

	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}

	public boolean getIsChatbot() {
		return isChatbot;
	}

	public void setIsChatbot(boolean isChatbot) {
		this.isChatbot = isChatbot;
	}

	public ActionDto() {
		super();
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getProcessor() {
		return Processor;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public List<String> getInstanceList() {
		return instanceList;
	}

	public void setInstanceList(List<String> instanceList) {
		this.instanceList = instanceList;
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

	public String getPoRelCode() {
		return poRelCode;
	}

	public void setPoRelCode(String poRelCode) {
		this.poRelCode = poRelCode;
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

	public String getSendToUserDisplay() {
		return sendToUserDisplay;
	}

	public void setSendToUserDisplay(String sendToUserDisplay) {
		this.sendToUserDisplay = sendToUserDisplay;
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

	public List<String> getTasksubjects() {
		return tasksubjects;
	}

	public void setTasksubjects(List<String> tasksubjects) {
		this.tasksubjects = tasksubjects;
	}

	public String getSendToUser() {
		return sendToUser;
	}

	public void setSendToUser(String sendToUser) {
		this.sendToUser = sendToUser;
	}

	public ActionDto(String userId, String userDisplay, List<ActionDtoChild> task) {
		super();
		this.userId = userId;
		this.userDisplay = userDisplay;
		this.task = task;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserDisplay() {
		return userDisplay;
	}

	public void setUserDisplay(String userDisplay) {
		this.userDisplay = userDisplay;
	}

	public List<ActionDtoChild> getTask() {
		return task;
	}

	public void setTask(List<ActionDtoChild> task) {
		this.task = task;
	}
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	@Override
	public String toString() {
		return "ActionDto [action=" + action + ", actionType=" + actionType + ", userId=" + userId + ", userDisplay="
				+ userDisplay + ", Processor=" + Processor + ", ownerEmail=" + ownerEmail + ", sendToUser=" + sendToUser
				+ ", sendToUserDisplay=" + sendToUserDisplay + ", task=" + task + ", instanceList=" + instanceList
				+ ", comment=" + comment + ", origin=" + origin + ", subject=" + subject + ", processType="
				+ processType + ", processId=" + processId + ", purchaseOrder=" + purchaseOrder + ", prRelCode="
				+ prRelCode + ", purchaseReq=" + purchaseReq + ", poRelCode=" + poRelCode + ", bnfPo=" + bnfPo
				+ ", processLabel=" + processLabel + ", caseId=" + caseId + ", icManagerProcessId=" + icManagerProcessId
				+ ", tasksubjects=" + tasksubjects + ", headerName=" + headerName + ", headerValue=" + headerValue
				+ ", processName=" + processName + ", isChatbot=" + isChatbot + ", isChatBot=" + isChatBot + "]";
	}

	
	

	
}
