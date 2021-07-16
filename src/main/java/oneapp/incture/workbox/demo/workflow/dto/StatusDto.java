package oneapp.incture.workbox.demo.workflow.dto;

public class StatusDto {

	String ready;
	String reserved;
	String completed;
	String resolved;
	String approve;
	String reject;
	String done;
	@Override
	public String toString() {
		return "StatusDto [ready=" + ready + ", reserved=" + reserved + ", completed=" + completed + ", resolved="
				+ resolved + ", approve=" + approve + ", reject=" + reject + ", done=" + done + "]";
	}
	public String getReady() {
		return ready;
	}
	public void setReady(String ready) {
		this.ready = ready;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getCompleted() {
		return completed;
	}
	public void setCompleted(String completed) {
		this.completed = completed;
	}
	public String getResolved() {
		return resolved;
	}
	public void setResolved(String resolved) {
		this.resolved = resolved;
	}
	public String getApprove() {
		return approve;
	}
	public void setApprove(String approve) {
		this.approve = approve;
	}
	public String getReject() {
		return reject;
	}
	public void setReject(String reject) {
		this.reject = reject;
	}
	public String getDone() {
		return done;
	}
	public void setDone(String done) {
		this.done = done;
	}
	
}
