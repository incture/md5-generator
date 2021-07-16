package oneapp.incture.workbox.demo.adapter_base.dto;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;


public class CustomAttributeValuesTableDto extends BaseDto {


	private String taskId;
	private String processName;
	private String key;
	private int index;
	private String attributeValue;

	
	
	public CustomAttributeValuesTableDto() {
		super();
	}

	public CustomAttributeValuesTableDto(String taskId, String processName, String key, int index,
			String attributeValue) {
		super();
		this.taskId = taskId;
		this.processName = processName;
		this.key = key;
		this.index = index;
		this.attributeValue = attributeValue;
	}
	
	


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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	public String toString() {
		return "CustomAttributeValuesTableDto [taskId=" + taskId + ", processName=" + processName + ", key=" + key
				+ ", index=" + index + ", attributeValue=" + attributeValue  + "]";
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
