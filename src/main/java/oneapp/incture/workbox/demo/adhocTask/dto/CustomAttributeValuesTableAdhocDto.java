package oneapp.incture.workbox.demo.adhocTask.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class CustomAttributeValuesTableAdhocDto extends BaseDto {
	
	private String taskId;
	private String processName;
	private String key;
	private int rowNumber;
	private String attributeValue;
	private String dependantOn;
	
	
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	public String getDependantOn() {
		return dependantOn;
	}
	public void setDependantOn(String dependantOn) {
		this.dependantOn = dependantOn;
	}
	@Override
	public String toString() {
		return "CustomAttributeValuesTableAdhocDto [taskId=" + taskId + ", processName=" + processName + ", key=" + key
				+ ", rowNumber=" + rowNumber + ", attributeValue=" + attributeValue + ", dependantOn=" + dependantOn
				+ "]";
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
