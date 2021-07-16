package oneapp.incture.workbox.demo.workflow.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "TASK_TEMPLATE")
public class TaskTemplate implements BaseDo,Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TaskTemplateDoPk taskTemplateDoPk;
	
	public TaskTemplateDoPk getTaskTemplateDoPk() {
		return taskTemplateDoPk;
	}

	public void setTaskTemplateDoPk(TaskTemplateDoPk taskTemplateDoPk) {
		this.taskTemplateDoPk = taskTemplateDoPk;
	}

	@Column(name = "OWNER_ID" , columnDefinition = "VARCHAR(1000)")
	private String ownerId;
	
	@Column(name = "TASK_NAME" , columnDefinition = "VARCHAR(255)")
	private String taskName;
	
	@Column(name = "TASK_TYPE" , columnDefinition = "VARCHAR(255)")
	private String taskType;
	
	@Column(name = "RUN_TIME_USER" , columnDefinition = "BOOLEAN")
	private Boolean runTimeUser;
	
	@Column(name = "CUSTOM_KEY" , columnDefinition = "VARCHAR(255)")
	private String customKey;
	
	
	public String getCustomKey() {
		return customKey;
	}

	public void setCustomKey(String customKey) {
		this.customKey = customKey;
	}

	public Boolean getRunTimeUser() {
		return runTimeUser;
	}

	public void setRunTimeUser(Boolean runTimeUser) {
		this.runTimeUser = runTimeUser;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	@Override
	public String toString() {
		return "TaskTemplate [taskTemplateDoPk=" + taskTemplateDoPk + ", ownerId=" + ownerId + ", taskName=" + taskName
				+ ", taskType=" + taskType + ", runTimeUser=" + runTimeUser + ", customKey=" + customKey + "]";
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public Object getPrimaryKey() {
		return taskTemplateDoPk;
	}
	

}
