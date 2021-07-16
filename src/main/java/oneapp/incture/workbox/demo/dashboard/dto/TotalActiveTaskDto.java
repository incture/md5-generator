package oneapp.incture.workbox.demo.dashboard.dto;


import java.math.BigInteger;

public class TotalActiveTaskDto {

	private String processName;
	private String processDisplayName;
	private BigInteger inTime;
	private BigInteger slaBreached;
	private BigInteger critical;
	
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
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
	
	public String getProcessDisplayName() {
		return processDisplayName;
	}
	public void setProcessDisplayName(String processDisplayName) {
		this.processDisplayName = processDisplayName;
	}
	@Override
	public String toString() {
		return "TotalActiveTaskDto [processName=" + processName + ", processDisplayName=" + processDisplayName
				+ ", inTime=" + inTime + ", slaBreached=" + slaBreached + ", critical=" + critical + "]";
	}

	
}
