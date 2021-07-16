package oneapp.incture.workbox.demo.inbox.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class TaskAuditingDto extends BaseDto{

	private String eventId;
	
	private String approvedById;
	
	private String comment;
	
	private String actionType;

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
		return "TaskAuditingDto [eventId=" + eventId + ", approvedById=" + approvedById + ", comment=" + comment
				+ ", actionType=" + actionType + "]";
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
