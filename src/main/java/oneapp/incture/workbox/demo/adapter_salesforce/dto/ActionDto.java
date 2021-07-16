package oneapp.incture.workbox.demo.adapter_salesforce.dto;

import java.util.List;

public class ActionDto {

	private String userId;
	private String userDisplay;
	private Boolean isAdmin;
	private List<TaskDto> task;
	@Override
	public String toString() {
		return "ActionDto [userId=" + userId + ", userDisplay=" + userDisplay + ", isAdmin=" + isAdmin + ", task="
				+ task + "]";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserDisplay() {
		return userDisplay;
	}
	public void setUserDisplay(String userDisplay) {
		this.userDisplay = userDisplay;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public List<TaskDto> getTask() {
		return task;
	}
	public void setTask(List<TaskDto> task) {
		this.task = task;
	}

}
