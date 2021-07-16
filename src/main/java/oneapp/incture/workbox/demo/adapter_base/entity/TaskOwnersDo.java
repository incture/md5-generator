package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: ProcessEventsDo
 *
 */
@Entity
@Table(name = "TASK_OWNERS")
public class TaskOwnersDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	public TaskOwnersDo() {
		super();
	}

	public TaskOwnersDo(TaskOwnersDoPK taskOwnersDoPK) {
		this.taskOwnersDoPK = taskOwnersDoPK;
	}

	public TaskOwnersDo(TaskOwnersDoPK taskOwnersDoPK, Boolean isProcessed) {
		this.taskOwnersDoPK = taskOwnersDoPK;
		this.isProcessed = isProcessed;
	}
	
	public TaskOwnersDo(TaskOwnersDoPK taskOwnersDoPK, Boolean isProcessed, Boolean enRoute) {
		this.taskOwnersDoPK = taskOwnersDoPK;
		this.isProcessed = isProcessed;
		this.enRoute = enRoute;
	}

	private static final long serialVersionUID = 8966817427208717661L;

	@EmbeddedId
	private TaskOwnersDoPK taskOwnersDoPK;

	@Column(name = "IS_PROCESSED")
	private Boolean isProcessed;

	@Column(name = "IS_SUBSTITUTED")
	private Boolean isSubstituted;
	
	@Column(name = "EN_ROUTE")
	private Boolean enRoute;

	@Column(name = "TASK_OWNER_DISP", length = 100)
	private String taskOwnerDisplayName;

	@Column(name = "TASK_OWNER_EMAIL", length = 60)
	private String ownerEmail;

	@Column(name = "IS_REVIEWER")
	private Boolean isReviewer;
	
	@Column(name = "GROUP_ID", length = 255)
	private String groupId;
	
	@Column(name = "GROUP_OWNER", length = 255)
	private String groupOwner;
	
	
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupOwner() {
		return groupOwner;
	}

	public void setGroupOwner(String groupOwner) {
		this.groupOwner = groupOwner;
	}

	public Boolean getIsReviewer() {
		return isReviewer;
	}

	public void setIsReviewer(Boolean isReviewer) {
		this.isReviewer = isReviewer;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public TaskOwnersDoPK getTaskOwnersDoPK() {
		return taskOwnersDoPK;
	}

	public void setTaskOwnersDoPK(TaskOwnersDoPK taskOwnersDoPK) {
		this.taskOwnersDoPK = taskOwnersDoPK;
	}

	public Boolean getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public Boolean getIsSubstituted() {
		return isSubstituted;
	}

	public void setIsSubstituted(Boolean isSubstituted) {
		this.isSubstituted = isSubstituted;
	}

	public Boolean getEnRoute() {
		return enRoute;
	}

	public void setEnRoute(Boolean enRoute) {
		this.enRoute = enRoute;
	}

	@Override
	public String toString() {
		return "TaskOwnersDo [taskOwnersDoPK=" + taskOwnersDoPK + ", isProcessed=" + isProcessed + ", isSubstituted="
				+ isSubstituted + ", enRoute=" + enRoute + ", taskOwnerDisplayName=" + taskOwnerDisplayName
				+ ", ownerEmail=" + ownerEmail + ", isReviewer=" + isReviewer + ", groupId=" + groupId + ", groupOwner="
				+ groupOwner + "]";
	}

	public String getTaskOwnerDisplayName() {
		return taskOwnerDisplayName;
	}

	public void setTaskOwnerDisplayName(String taskOwnerDisplayName) {
		this.taskOwnerDisplayName = taskOwnerDisplayName;
	}

	@Override
	public Object getPrimaryKey() {
		return taskOwnersDoPK;
	}

}
