package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;

public class ProcessDefinationDto {

	private String processId;
	private String processName;
	private String createdBy;
	private String createdAt;
	
	private String taskName;

	List<TaskAttributes> attributes = new ArrayList<TaskAttributes>();

	public ProcessDefinationDto() {
		super();
	}
	

	

	public ProcessDefinationDto(String processId, String processName, String createdBy, String createdAt,
			String taskName, List<TaskAttributes> attributes) {
		super();
		this.processId = processId;
		this.processName = processName;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		this.taskName = taskName;
		this.attributes = attributes;
	}




	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public List<TaskAttributes> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<TaskAttributes> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "ProcessDefinationDto [processId=" + processId + ", processName=" + processName + ", createdBy="
				+ createdBy + ", createdAt=" + createdAt + ", taskName=" + taskName + ", attributes=" + attributes
				+ "]";
	}

}
