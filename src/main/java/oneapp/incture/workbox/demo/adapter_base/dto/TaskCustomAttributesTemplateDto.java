package oneapp.incture.workbox.demo.adapter_base.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;


public class TaskCustomAttributesTemplateDto extends BaseDto{

	
	private String processName;

	
	private String taskName;

	
	private String origin;

	
	private String key;

	
	private String label;

	public TaskCustomAttributesTemplateDto() {
		super();
	}

	public TaskCustomAttributesTemplateDto(String processName, String taskName, String origin, String key, String label) {
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
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

}
