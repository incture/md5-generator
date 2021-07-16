package oneapp.incture.workbox.demo.inbox.entity;

import java.io.Serializable;

import javax.persistence.Column;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

public class PinnedTaskDoPK implements BaseDo, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Column(name = "PINNED_TASK_ID", length = 255, nullable = false)
	private String pinnedTaskId;

	@Column(name = "USER_ID", length = 100, nullable = false)
	private String userId;
	
	public PinnedTaskDoPK() {
		super();
		// TODO Auto-generated constructor stub
	}
		
	public PinnedTaskDoPK(String pinnedTaskId, String userId) {
		super();
		this.pinnedTaskId = pinnedTaskId;
		this.userId = userId;
	}


	public String getPinnedTaskId() {
		return pinnedTaskId;
	}
	public void setPinnedTaskId(String pinnedTaskId) {
		this.pinnedTaskId = pinnedTaskId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "PinnedTaskDo [pinnedTaskId=" + pinnedTaskId + ", userId=" + userId + "]";
	}
	
	
	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
