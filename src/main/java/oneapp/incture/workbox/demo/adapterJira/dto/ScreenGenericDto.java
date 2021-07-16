package oneapp.incture.workbox.demo.adapterJira.dto;

import java.util.List;

public class ScreenGenericDto {
	private String taskId;
	private String processName;
	private String key;
	private List<AttributeGenericDto> attributeValue;
	private String dataType;
	
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
	public List<AttributeGenericDto> getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(List<AttributeGenericDto> attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@Override
	public String toString() {
		return "ScreenGenericDto [taskId=" + taskId + ", processName=" + processName + ", key=" + key
				+ ", attributeValue=" + attributeValue + ", dataType=" + dataType + "]";
	}

}
