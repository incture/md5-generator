package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonInclude;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskAuditDto extends BaseDto{

	private String auditId;
	private String eventId;
	private String userId;
	private String userName;
	private String comment;
	private String action;
	private Date updatedAt;
	private String sendToUser;
	private String sendToUserName;
	private String updatedAtString;
	private String platform;
	private String signatureVerified;
	
	private String requestId;
	private Date completedAt;
	private String completedAtString;
	
	
	
	 
	public String getCompletedAtString() {
		return completedAtString;
	}
	public void setCompletedAtString(String completedAtString) {
		this.completedAtString = completedAtString;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Date getCompletedAt() {
		return completedAt;
	}
	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSendToUserName() {
		return sendToUserName;
	}
	public void setSendToUserName(String sendToUserName) {
		this.sendToUserName = sendToUserName;
	}
	public String getSendToUser() {
		return sendToUser;
	}
	public void setSendToUser(String sendToUser) {
		this.sendToUser = sendToUser;
	}
	public String getUpdatedAtString() {
		return updatedAtString;
	}
	public void setUpdatedAtString(String updatedAtString) {
		this.updatedAtString = updatedAtString;
	}
	public String getAuditId() {
		return auditId;
	}
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	public TaskAuditDto() {
		super();
	}
	
	public TaskAuditDto(String auditId, String eventId, String userId, String userName, String comment, String action,
			Date updatedAt, String sendToUser, String sendToUserName) {
		super();
		this.auditId = auditId;
		this.eventId = eventId;
		this.userId = userId;
		this.userName = userName;
		this.comment = comment;
		this.action = action;
		this.updatedAt = updatedAt;
		this.sendToUser = sendToUser;
		this.sendToUserName = sendToUserName;
	}
	
	
	public TaskAuditDto(String auditId, String eventId, String userId, String userName, String comment, String action,
			Date updatedAt, String sendToUser, String sendToUserName, String updatedAtString, String platform,
			String signatureVerified, String requestId, Date completedAt) {
		super();
		this.auditId = auditId;
		this.eventId = eventId;
		this.userId = userId;
		this.userName = userName;
		this.comment = comment;
		this.action = action;
		this.updatedAt = updatedAt;
		this.sendToUser = sendToUser;
		this.sendToUserName = sendToUserName;
		this.updatedAtString = updatedAtString;
		this.platform = platform;
		this.signatureVerified = signatureVerified;
		this.requestId = requestId;
		this.completedAt = completedAt;
	}
	@Override
	public String toString() {
		return "TaskAuditDto [auditId=" + auditId + ", eventId=" + eventId + ", userId=" + userId + ", userName="
				+ userName + ", comment=" + comment + ", action=" + action + ", updatedAt=" + updatedAt
				+ ", sendToUser=" + sendToUser + ", sendToUserName=" + sendToUserName + ", updatedAtString="
				+ updatedAtString + ", platform=" + platform + ", signatureVerified=" + signatureVerified
				+ ", requestId=" + requestId + ", completedAt=" + completedAt + ", completedAtString="
				+ completedAtString + "]";
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
