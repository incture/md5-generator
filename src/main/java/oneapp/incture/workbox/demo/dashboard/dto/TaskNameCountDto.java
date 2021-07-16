package oneapp.incture.workbox.demo.dashboard.dto;


import java.math.BigInteger;

public class TaskNameCountDto {

	private String strName;
	private String status;
	private BigInteger taskCount;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public BigInteger getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(BigInteger taskCount) {
		this.taskCount = taskCount;
	}

	@Override
	public String toString() {
		return "TaskNameCountDto [strName=" + strName + ", status=" + status + ", taskCount=" + taskCount + "]";
	}

}
