package oneapp.incture.workbox.demo.adhocTask.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "TASK_TEMPLATE_VALUE")
public class TaskValueDo implements BaseDo,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@EmbeddedId
//	private TaskValueDoPk taskValueDoPk;
	@Id
	@Column(name = "PROCESS_ID", columnDefinition = "NVARCHAR(200)")
	private String processId;
	@Id
	@Column(name = "STEP_NUMBER", columnDefinition = "INTEGER")
	private Integer stepNumber;
	
	@Column(name = "OWNER_ID" , columnDefinition = "NVARCHAR(30)")
	private String ownerId;
	
	@Column(name = "TASK_NAME" , columnDefinition = "NVARCHAR(100)")
	private String taskName;
	
	@Column(name = "TASK_TYPE" , columnDefinition = "NVARCHAR(50)")
	private String taskType;
	
	@Column(name = "DESCRIPTION" , columnDefinition = "NVARCHAR(1000)")
	private String description;
	
 
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
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


	public String getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getTaskType() {
		return taskType;
	}


	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}


	@Override
	public String toString() {
		return "TaskValueDo [processId=" + processId + ", stepNumber=" + stepNumber + ", ownerId=" + ownerId
				+ ", taskName=" + taskName + ", taskType=" + taskType + ", description=" + description + "]";
	}


	@Override
	public Object getPrimaryKey() {
		return processId+stepNumber;
	}
	
	
	
}
