package oneapp.incture.workbox.demo.adapter_base.entity;
/*package oneapp.incture.workbox.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TASK_AUDIT_LOGS")
public class TaskAuditing implements BaseDo,Serializable{
	
	*//**
	 * 
	 *//*
	private static final long serialVersionUID = -3497479815226245344L;

	@Id
	@Column(name = "EVENT_ID", length = 50, nullable = false)
	private String eventId;

	@Column(name = "USER_ID", length = 50)
	private String approvedById;

	@Column(name = "COMMENT", length = 1000)
	private String comment;
	
	@Id
	@Column(name = "ACTION_TYPE", length = 10)
	private String actionType;

	public TaskAuditing() {
		super();
	}
	

	public TaskAuditing(String eventId, String approvedById, String comment) {
		super();
		this.eventId = eventId;
		this.approvedById = approvedById;
		this.comment = comment;
	}


	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getApprovedById() {
		return approvedById;
	}

	public void setApprovedById(String approvedById) {
		this.approvedById = approvedById;
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


	@Override
	public String toString() {
		return "TaskAuditing [eventId=" + eventId + ", approvedById=" + approvedById + ", comment=" + comment
				+ ", actionType=" + actionType + "]";
	}


	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
*/