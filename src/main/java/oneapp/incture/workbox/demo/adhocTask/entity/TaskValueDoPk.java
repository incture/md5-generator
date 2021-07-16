package oneapp.incture.workbox.demo.adhocTask.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Embeddable
public class TaskValueDoPk implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "PROCESS_ID", columnDefinition = "NVARCHAR(200)")
	private String processId;

	@Column(name = "STEP_NUMBER", columnDefinition = "INTEGER")
	private Integer stepNumber;

	public TaskValueDoPk(String processId, Integer stepNumber) {
		super();
		this.processId = processId;
		this.stepNumber = stepNumber;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}

	@Override
	public String toString() {
		return "TaskValueDoPk [processId=" + processId + ", stepNumber=" + stepNumber + "]";
	}

	public TaskValueDoPk() {
		super();
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}

}
