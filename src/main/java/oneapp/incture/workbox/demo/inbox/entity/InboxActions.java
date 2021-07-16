package oneapp.incture.workbox.demo.inbox.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//@Entity
@Table(name = "INBOX_ACTIONS")
public class InboxActions {

	public InboxActions() {
		super();
	}

	public InboxActions(String taskId, Date modifiedAt, Boolean isClaimed, Boolean isReleased, String userClaimed) {
		super();
		this.taskId = taskId;
		this.modifiedAt = modifiedAt;
		this.isClaimed = isClaimed;
		this.isReleased = isReleased;
		this.userClaimed = userClaimed;
	}

	@Id
	@Column(name = "TASK_ID", length = 45)
	private String taskId;

	@Column(name = "MODIFIED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedAt;

	@Column(name = "IS_CLAIMED")
	private Boolean isClaimed;

	@Column(name = "IS_RELEASED")
	private Boolean isReleased;
	
	@Column(name = "USER_CLAIMED", length = 40)
	private String userClaimed;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public boolean isClaimed() {
		return isClaimed;
	}

	public void setClaimed(boolean isClaimed) {
		this.isClaimed = isClaimed;
	}

	public boolean isReleased() {
		return isReleased;
	}

	public void setReleased(boolean isReleased) {
		this.isReleased = isReleased;
	}
	
	public String isUserClaimed() {
		return userClaimed;
	}

	public void setUserClaimed(String userClaimed) {
		this.userClaimed = userClaimed;
	}

	@Override
	public String toString() {
		return "InboxActions [taskId=" + taskId + ", modifiedAt=" + modifiedAt + ", isClaimed=" + isClaimed
				+ ", isReleased=" + isReleased + ", userClaimed=" + userClaimed + "]";
	}

}
