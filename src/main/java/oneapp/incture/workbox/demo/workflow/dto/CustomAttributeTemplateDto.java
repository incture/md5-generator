package oneapp.incture.workbox.demo.workflow.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class CustomAttributeTemplateDto extends BaseDto {

	String label;
	String key;
	String dataType;
	Boolean isMandatory;
	Boolean isActive;
	Boolean isEditable;
	String description;
	Integer isEdited;
	String processName;
	Boolean isRunTime;
	String runTimeType;
	String origin;
	String attributePath;
	String dependantOn;
	Boolean isVisible;
	List<CustomAttributeTemplateDto> tableAttributes;
	List<ValuesDto> dropDownValues;
	Boolean isDeleted;
	String defaultValue;
	Boolean copy;
	Boolean validForUsage;
	String taskName;
	Integer sequence;
	

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Boolean getCopy() {
		return copy;
	}

	public void setCopy(Boolean copy) {
		this.copy = copy;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setValidForUsage(Boolean validForUsage) {
		this.validForUsage = validForUsage;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRunTimeType() {
		return runTimeType;
	}

	public void setRunTimeType(String runTimeType) {
		this.runTimeType = runTimeType;
	}

	public List<ValuesDto> getDropDownValues() {
		return dropDownValues;
	}

	public void setDropDownValues(List<ValuesDto> dropDownValues) {
		this.dropDownValues = dropDownValues;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public String getDependantOn() {
		return dependantOn;
	}

	public void setDependantOn(String dependantOn) {
		this.dependantOn = dependantOn;
	}

	public String getAttributePath() {
		return attributePath;
	}

	public void setAttributePath(String attributePath) {
		this.attributePath = attributePath;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Boolean getIsRunTime() {
		return isRunTime;
	}

	public void setIsRunTime(Boolean isRunTime) {
		this.isRunTime = isRunTime;
	}

	public Integer getIsEdited() {
		return isEdited;
	}

	public void setIsEdited(Integer isEdited) {
		this.isEdited = isEdited;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	@Override
	public String toString() {
		return "CustomAttributeTemplateDto [label=" + label + ", key=" + key + ", dataType=" + dataType
				+ ", isMandatory=" + isMandatory + ", isActive=" + isActive + ", isEditable=" + isEditable
				+ ", description=" + description + ", isEdited=" + isEdited + ", processName=" + processName
				+ ", isRunTime=" + isRunTime + ", runTimeType=" + runTimeType + ", origin=" + origin
				+ ", attributePath=" + attributePath + ", dependantOn=" + dependantOn + ", isVisible=" + isVisible
				+ ", tableAttributes=" + tableAttributes + ", dropDownValues=" + dropDownValues + ", isDeleted="
				+ isDeleted + ", defaultValue=" + defaultValue + ", copy=" + copy + ", validForUsage=" + validForUsage
				+ ", taskName=" + taskName + ", sequence=" + sequence + "]";
	}

	public List<CustomAttributeTemplateDto> getTableAttributes() {
		return tableAttributes;
	}

	public void setTableAttributes(List<CustomAttributeTemplateDto> tableAttributes) {
		this.tableAttributes = tableAttributes;
	}

	@Override
	public Boolean getValidForUsage() {
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}



}
