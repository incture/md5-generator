package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TASK_AUDIT")
public class TaskAudit implements BaseDo,Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3426436320814731355L;

	@Id
	@Column(name = "AUDIT_ID", length = 50, nullable = false)
	private String auditId;
	
	@Column(name = "EVENT_ID", length = 50, nullable = false)
	private String eventId;

	@Column(name = "USER_ID", length = 50)
	private String userId;

	@Column(name = "COMMENT", length = 1000)
	private String comment;
	
	@Column(name = "ACTION_TYPE", length = 100)
	private String actionType;
	
	@Column(name = "UPDATED_AT")
	private Date updatedAt;
	
	@Column(name = "SEND_TO_USER", length = 100)
	private String sendToUser;
	
	@Column(name = "SEND_TO_USER_NAME", length = 100)
	private String sendToUserName;
	
	@Column(name = "USER_NAME", length = 100)
	private String userName;
	
	public String getSendToUserName() {
		return sendToUserName;
	}


	public void setSendToUserName(String sendToUserName) {
		this.sendToUserName = sendToUserName;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
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


	public String getActionType() {
		return actionType;
	}


	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


	public Date getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}


	public String getSendToUser() {
		return sendToUser;
	}


	public void setSendToUser(String sendToUser) {
		this.sendToUser = sendToUser;
	}


	@Override
	public String toString() {
		return "TaskAudit [auditId=" + auditId + ", eventId=" + eventId + ", userId=" + userId + ", comment=" + comment
				+ ", actionType=" + actionType + ", updatedAt=" + updatedAt + ", sendToUser=" + sendToUser
				+ ", sendToUserName=" + sendToUserName + ", userName=" + userName + "]";
	}


	@Override
	public Object getPrimaryKey() {
		return auditId;
	}
}
