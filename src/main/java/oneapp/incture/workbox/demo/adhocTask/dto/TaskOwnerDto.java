package oneapp.incture.workbox.demo.adhocTask.dto;

public class TaskOwnerDto {

	private String taskOwnername;
	private String taskOwner;
	private String email;
	
	public String getTaskOwnername() {
		return taskOwnername;
	}
	public void setTaskOwnername(String taskOwnername) {
		this.taskOwnername = taskOwnername;
	}
	public String getTaskOwner() {
		return taskOwner;
	}
	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "TaskOwnerDto [taskOwnername=" + taskOwnername + ", taskOwner=" + taskOwner + ", email=" + email + "]";
	}
}
