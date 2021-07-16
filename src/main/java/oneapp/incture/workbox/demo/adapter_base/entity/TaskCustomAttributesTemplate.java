package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TASK_CUSTOM_ATTRIBUTES_TEAMPLATE")
public class TaskCustomAttributesTemplate implements BaseDo, Serializable{

	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;

	@Id
	@Column(name = "TASK_NAME")
	private String taskName;

	@Id
	@Column(name = "ORIGIN")
	private String origin;

	@Id
	@Column(name = "KEY")
	private String key;

	@Column(name = "LABEL")
	private String label;

	public TaskCustomAttributesTemplate() {
		super();
	}

	public TaskCustomAttributesTemplate(String processName, String taskName, String origin, String key, String label) {
		super();
		this.processName = processName;
		this.taskName = taskName;
		this.origin = origin;
		this.key = key;
		this.label = label;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "TaskCustomAttributesTemplate [processName=" + processName + ", taskName=" + taskName + ", origin="
				+ origin + ", key=" + key + ", label=" + label + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
