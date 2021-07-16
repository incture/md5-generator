package oneapp.incture.workbox.demo.inbox.dto;

public class PinnedTaskDto {

	private String pinnedTaskId;
	private String userId;
	private Boolean isPinned;
	
	public PinnedTaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PinnedTaskDto(String pinnedTaskId, String userId) {
		super();
		this.pinnedTaskId = pinnedTaskId;
		this.userId = userId;
	}
	
	public Boolean getIsPinned() {
		return isPinned;
	}
	public void setIsPinned(Boolean isPinned) {
		this.isPinned = isPinned;
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
		return "PinnedTaskDto [pinnedTaskId=" + pinnedTaskId + ", userId=" + userId + ", isPinned=" + isPinned + "]";
	}
	
}
