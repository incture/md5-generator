package oneapp.incture.workbox.demo.ecc.dto;

/**
 * This is the ECC process Dto class . To holds all the ECC process information .
 * @author Venkatesh.Kesary
 * @version 1.0
 * @since 13 April 2020
 * 
 */
public class ECCProcessDto {

	/**
	 * Holds the Task Definition id of the process.
	 */
	private String taskDefinationId;
	/**
	 * Holds the process Name .
	 */
	private String processName;
	/**
	 * SLA of the process possible valures are xx hours , xx days , xx months etc
	 */
	private String sla;

	/**
	 * 
	 */
	public ECCProcessDto() {
		super();
	}

	/**
	 * @param taskDefinationId
	 * @param processName
	 * @param sla
	 */
	public ECCProcessDto(String taskDefinationId, String processName, String sla) {
		super();
		this.taskDefinationId = taskDefinationId;
		this.processName = processName;
		this.sla = sla;
	}

	/**
	 * @return the taskDefinationId
	 */
	public String getTaskDefinationId() {
		return taskDefinationId;
	}

	/**
	 * @param taskDefinationId
	 *            the taskDefinationId to set
	 */
	public void setTaskDefinationId(String taskDefinationId) {
		this.taskDefinationId = taskDefinationId;
	}

	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}

	/**
	 * @param processName
	 *            the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * @return the sla
	 */
	public String getSla() {
		return sla;
	}

	/**
	 * @param sla
	 *            the sla to set
	 */
	public void setSla(String sla) {
		this.sla = sla;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ECCProcessDto [taskDefinationId=" + taskDefinationId + ", processName=" + processName + ", sla=" + sla
				+ "]";
	}

}
