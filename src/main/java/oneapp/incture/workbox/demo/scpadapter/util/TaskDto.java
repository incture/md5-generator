package oneapp.incture.workbox.demo.scpadapter.util;

import java.util.ArrayList;
import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskAttributes;

public class TaskDto {

	private String id;
	private String processName;
	private String taskDisplayName;
	private String taskName;
	
	List<TaskAttributes> attributes=new ArrayList<>();

	public TaskDto() {
		super();
	}

	public TaskDto(String id, String processName) {
		super();
		this.id = id;
		this.processName = processName;
	}

	public TaskDto(String id, String processName, String taskDisplayName, String taskName) {
		super();
		this.id = id;
		this.processName = processName;
		this.taskDisplayName = taskDisplayName;
		this.taskName = taskName;
	}

	public TaskDto(String id, String processName, String taskDisplayName, String taskName,
			List<TaskAttributes> attributes) {
		super();
		this.id = id;
		this.processName = processName;
		this.taskDisplayName = taskDisplayName;
		this.taskName = taskName;
		this.attributes = attributes;
	}

	public List<TaskAttributes> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<TaskAttributes> attributes) {
		this.attributes = attributes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getTaskDisplayName() {
		return taskDisplayName;
	}

	public void setTaskDisplayName(String taskDisplayName) {
		this.taskDisplayName = taskDisplayName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public String toString() {
		return "TaskDto [id=" + id + ", processName=" + processName + ", taskDisplayName=" + taskDisplayName
				+ ", taskName=" + taskName + ", attributes=" + attributes + "]";
	}

}
