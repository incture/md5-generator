package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.Date;

public class SubstitutionTaskAuditDto {
	
	private String taskAuditId;
	private String eventId;
	private String substitutedUser;
	private String substitutingUser;
	private String actionType;
	private Date updateAt;
	private String updateAtInString;
	
	public SubstitutionTaskAuditDto(){}
	
	public SubstitutionTaskAuditDto(String eventId , String substitutingUser , String substitutedUser , String actionType , Date updateAt) {
		this.eventId = eventId;
		this.substitutedUser = substitutedUser;
		this.substitutingUser = substitutingUser;
		this.actionType = actionType;
		this.updateAt = updateAt;
		
	}
	
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getSubstitutedUser() {
		return substitutedUser;
	}
	public void setSubstitutedUser(String substitutedUser) {
		this.substitutedUser = substitutedUser;
	}
	public String getSubstitutingUser() {
		return substitutingUser;
	}
	public void setSubstitutingUser(String substitutingUser) {
		this.substitutingUser = substitutingUser;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public Date getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getTaskAuditId() {
		return taskAuditId;
	}

	public void setTaskAuditId(String taskAuditId) {
		this.taskAuditId = taskAuditId;
	}

	public String getUpdateAtInString() {
		return updateAtInString;
	}

	public void setUpdateAtInString(String updateAtInString) {
		this.updateAtInString = updateAtInString;
	}
	
	
	

}
