package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;

public class CustomAttributeTemplateDto {

	String processName;
	String key;
	String label;
	String processType;
	Boolean isEditable;
	Boolean isActive;
	Boolean isMandatory;
	int isEdited;
	String attrDes;
	String value;
	String dataType;
	List<ValueListDto> valueList;
	String attachmentType;
	Integer attachmentSize;
	String attachmentName;
	String attachmentId;
	String attachmentValue;
	Integer dataTypeKey;
	String dropDownType;
	String url;
	String taskId;
	String origin;
	String attributePath;
	String dependantOn;
	int rowNumber;
	List<CustomAttributeTemplateDto> tableAttributes;
	List<TableContentDto> tableContents;
	Boolean isDeleted;
	Boolean isRunTime;
	Boolean isVisible;

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Boolean getIsRunTime() {
		return isRunTime;
	}

	public void setIsRunTime(Boolean isRunTime) {
		this.isRunTime = isRunTime;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<CustomAttributeTemplateDto> getTableAttributes() {
		return tableAttributes;
	}

	public void setTableAttributes(List<CustomAttributeTemplateDto> tableAttributes) {
		this.tableAttributes = tableAttributes;
	}

	public String getDependantOn() {
		return dependantOn;
	}

	public void setDependantOn(String dependantOn) {
		this.dependantOn = dependantOn;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getAttributePath() {
		return attributePath;
	}

	public void setAttributePath(String attributePath) {
		this.attributePath = attributePath;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return "CustomAttributeTemplateDto [processName=" + processName + ", key=" + key + ", label=" + label
				+ ", processType=" + processType + ", isEditable=" + isEditable + ", isActive=" + isActive
				+ ", isMandatory=" + isMandatory + ", isEdited=" + isEdited + ", attrDes=" + attrDes + ", value="
				+ value + ", dataType=" + dataType + ", valueList=" + valueList + ", attachmentType=" + attachmentType
				+ ", attachmentSize=" + attachmentSize + ", attachmentName=" + attachmentName + ", attachmentId="
				+ attachmentId + ", attachmentValue=" + attachmentValue + ", dataTypeKey=" + dataTypeKey
				+ ", dropDownType=" + dropDownType + ", url=" + url + ", taskId=" + taskId + ", origin=" + origin
				+ ", attributePath=" + attributePath + ", dependantOn=" + dependantOn + ", rowNumber=" + rowNumber
				+ ", tableAttributes=" + tableAttributes + ", tableContents=" + tableContents + ", isDeleted="
				+ isDeleted + ", isRunTime=" + isRunTime + ", isVisible=" + isVisible + "]";
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public int getIsEdited() {
		return isEdited;
	}

	public void setIsEdited(int isEdited) {
		this.isEdited = isEdited;
	}

	public String getAttrDes() {
		return attrDes;
	}

	public void setAttrDes(String attrDes) {
		this.attrDes = attrDes;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public List<ValueListDto> getValueList() {
		return valueList;
	}

	public void setValueList(List<ValueListDto> valueList) {
		this.valueList = valueList;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Integer getAttachmentSize() {
		return attachmentSize;
	}

	public void setAttachmentSize(Integer attachmentSize) {
		this.attachmentSize = attachmentSize;
	}

	public Integer getDataTypeKey() {
		return dataTypeKey;
	}

	public void setDataTypeKey(Integer dataTypeKey) {
		this.dataTypeKey = dataTypeKey;
	}

	public String getDropDownType() {
		return dropDownType;
	}

	public void setDropDownType(String dropDownType) {
		this.dropDownType = dropDownType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public List<TableContentDto> getTableContents() {
		return tableContents;
	}

	public void setTableContents(List<TableContentDto> tableContents) {
		this.tableContents = tableContents;
	}

	public String getAttachmentValue() {
		return attachmentValue;
	}

	public void setAttachmentValue(String attachmentValue) {
		this.attachmentValue = attachmentValue;
	}
	

}
