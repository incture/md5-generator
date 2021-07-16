package oneapp.incture.workbox.demo.workload.dto;

import java.math.BigInteger;

public class UserWorkloadDto {

	private String userName;
	private BigInteger noOfTask;
	private String userId;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public BigInteger getNoOfTask() {
		return noOfTask;
	}
	public void setNoOfTask(BigInteger noOfTask) {
		this.noOfTask = noOfTask;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "UserWorkloadDto [userName=" + userName + ", noOfTask=" + noOfTask + ", userId=" + userId + "]";
	}
	
}

