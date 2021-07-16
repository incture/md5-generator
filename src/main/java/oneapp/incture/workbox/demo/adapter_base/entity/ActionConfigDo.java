package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACTION_CONFIG")
public class ActionConfigDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4639296232687810757L;

	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;
	
	@Id
	@Column(name = "TASK_NAME")
	private String taskName;
	
	@Id
	@Column(name = "STATUS")
	private String status;
	
	@Id
	@Column(name = "ACTIONS")
	private String actions;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "ACTION_NATURE")
	private String actionNature;

	

	@Override
	public String toString() {
		return "ActionConfigDo [processName=" + processName + ", taskName=" + taskName + ", status=" + status
				+ ", actions=" + actions + ", type=" + type + ", actionNature=" + actionNature + "]";
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

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getActionNature() {
		return actionNature;
	}

	public void setActionNature(String actionNature) {
		this.actionNature = actionNature;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
