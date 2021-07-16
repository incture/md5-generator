package oneapp.incture.workbox.demo.adapter_base.util;

public class OwnerCacheInstance {

	private String taskId;
	private String owner;
	private Boolean isProcessed;

	public OwnerCacheInstance(String taskId, String owner, Boolean isProcessed) {
		super();
		this.taskId = taskId;
		this.owner = owner;
		this.isProcessed = isProcessed;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Boolean getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(Boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	@Override
	public String toString() {
		return "OwnerCacheInstance [taskId=" + taskId + ", owner=" + owner + ", isProcessed=" + isProcessed + "]";
	}

}
