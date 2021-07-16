package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STATUS_CONFIG")
public class StatusConfigDo implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3934184114494376631L;

	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;
	
	@Id
	@Column(name = "TASK_NAME")
	private String taskName;
	
	@Id
	@Column(name = "STATUS")
	private String status;

	@Column(name = "SYSTEM")
	private String system;
	
	@Column(name = "BUSINESS_STATUS")
	private String businessStatus;

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "StatusConfigDo [processName=" + processName + ", taskName=" + taskName + ", status=" + status
				+ ", system=" + system + ", businessStatus=" + businessStatus + "]";
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}
	
	
}
