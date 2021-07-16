package oneapp.incture.workbox.demo.workflow.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Embeddable
public class TaskTemplateDoPk implements BaseDo, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "PROCESS_NAME" , columnDefinition = "VARCHAR(255)")
	private String processName;
	
	@Column(name = "STEP_NUMBER" , columnDefinition = "INTEGER")
	private Integer stepNumber;

	@Override
	public String toString() {
		return "TaskTemplateDoPk [processName=" + processName + ", stepNumber=" + stepNumber + "]";
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}

	public TaskTemplateDoPk(String processName, Integer stepNumber) {
		super();
		this.processName = processName;
		this.stepNumber = stepNumber;
	}

	public TaskTemplateDoPk() {
		super();
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}
	
	

}
