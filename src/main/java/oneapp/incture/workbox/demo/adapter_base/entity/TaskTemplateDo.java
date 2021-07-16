package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TASK_TEMPLATE_TABLE")
public class TaskTemplateDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;
	@Id
	@Column(name = "TASK_NAME")
	private String taskName;
	@Id
	@Column(name = "ORIGIN")
	private String origin;

	@Column(name = "PARENT_TASK_NAME")
	private String parentTaskName;

	@Column(name = "TEMPLATE_ID")
	private String templateId;

	public TaskTemplateDo() {
		super();
	}

	public TaskTemplateDo(String processName, String taskName, String origin, String parentTaskName,
			String templateId) {
		super();
		this.processName = processName;
		this.taskName = taskName;
		this.origin = origin;
		this.parentTaskName = parentTaskName;
		this.templateId = templateId;
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

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getParentTaskName() {
		return parentTaskName;
	}

	public void setParentTaskName(String parentTaskName) {
		this.parentTaskName = parentTaskName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	@Override
	public String toString() {
		return "ProcessTemplateDo [processName=" + processName + ", taskName=" + taskName + ", origin=" + origin
				+ ", parentTaskName=" + parentTaskName + ", templateId=" + templateId + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
