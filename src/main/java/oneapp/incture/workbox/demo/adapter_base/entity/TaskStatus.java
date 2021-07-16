package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TASK_STATUS")
public class TaskStatus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EVENT_ID", nullable = false)
	private String eventId;

	@Column(name = "IS_APPROVED")
	private Boolean isApproved ;

	@Column(name = "COMMENTS", length = 1000)
	private String comments;

	
	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "TaskStatus [eventId=" + eventId + ", isApproved=" + isApproved + ", comments=" + comments + "]";
	}
	
	
}
