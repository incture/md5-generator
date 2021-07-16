package oneapp.incture.workbox.demo.adapter_base.dto;

public class CustomAttributeValueDto {

	private String taskId;
	private String processName;
	private String key;
	private String attributeValue;
	private int rowNumber;
	private String dataType;
	private String dependantOn;
	private String attributeTemplate;

	public CustomAttributeValueDto(String taskId, String processName, String key, String attributeValue, int rowNumber,
			String dataType, String dependantOn) {
		super();
		this.taskId = taskId;
		this.processName = processName;
		this.key = key;
		this.attributeValue = attributeValue;
		this.rowNumber = rowNumber;
		this.dataType = dataType;
		this.dependantOn = dependantOn;
	}

	public String getDependantOn() {
		return dependantOn;
	}

	public void setDependantOn(String dependantOn) {
		this.dependantOn = dependantOn;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public CustomAttributeValueDto(String taskId, String processName, String key, String attributeValue) {
		super();
		this.taskId = taskId;
		this.processName = processName;
		this.key = key;
		this.attributeValue = attributeValue;
	}

	public CustomAttributeValueDto(String taskId, String processName) {
		super();
		this.taskId = taskId;
		this.processName = processName;
	}

	public CustomAttributeValueDto() {
		super();
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

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	
	public String getAttributeTemplate() {
		return attributeTemplate;
	}

	public void setAttributeTemplate(String attributeTemplate) {
		this.attributeTemplate = attributeTemplate;
	}

	@Override
	public String toString() {
		return "CustomAttributeValueDto [taskId=" + taskId + ", processName=" + processName + ", key=" + key
				+ ", attributeValue=" + attributeValue + ", rowNumber=" + rowNumber + ", dataType=" + dataType
				+ ", dependantOn=" + dependantOn + ", attributeTemplate=" + attributeTemplate + "]";
	}

	

}
