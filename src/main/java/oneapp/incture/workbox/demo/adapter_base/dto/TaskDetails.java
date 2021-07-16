package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.List;

public class TaskDetails {

	private String taskName;
	private String taskDisplayName;
	
	private List<TaskAttributes> attributes;
	
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDisplayName() {
		return taskDisplayName;
	}
	public void setTaskDisplayName(String taskDisplayName) {
		this.taskDisplayName = taskDisplayName;
	}
	public List<TaskAttributes> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<TaskAttributes> attributes) {
		this.attributes = attributes;
	}
	@Override
	public String toString() {
		return "TaskDetails [taskName=" + taskName + ", taskDisplayName=" + taskDisplayName + ", attributes="
				+ attributes + "]";
	}
	
	
}
