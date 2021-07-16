package oneapp.incture.workbox.demo.dashboard.dto;


import java.math.BigInteger;

public class UserWorkCountDto {

	private String userName;
	private String userId;
	private BigInteger inTime;
	private BigInteger critical;
	private BigInteger slaBreached;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigInteger getInTime() {
		return inTime;
	}
	public void setInTime(BigInteger inTime) {
		this.inTime = inTime;
	}
	
	public BigInteger getSlaBreached() {
		return slaBreached;
	}
	public void setSlaBreached(BigInteger slaBreached) {
		this.slaBreached = slaBreached;
	}
	public BigInteger getCritical() {
		return critical;
	}
	public void setCritical(BigInteger critical) {
		this.critical = critical;
	}
	@Override
	public String toString() {
		return "UserWorkCountDto [userName=" + userName + ", userId=" + userId + ", inTime=" + inTime + ", critical="
				+ critical + ", slaBreached=" + slaBreached + "]";
	}

}