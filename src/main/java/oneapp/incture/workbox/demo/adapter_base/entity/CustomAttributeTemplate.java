package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.omg.CORBA.PRIVATE_MEMBER;

@Entity
@Table(name = "CUSTOM_ATTR_TEMPLATE")
public class CustomAttributeTemplate implements BaseDo, Serializable {

	public CustomAttributeTemplate() {
		super();
	}

	public CustomAttributeTemplate(String dataType, String label, Boolean isMandatory, Boolean isEditable) {
		super();
		this.dataType = dataType;
		this.label = label;
		this.isMandatory = isMandatory;
		this.isEditable = isEditable;
	}

	private static final long serialVersionUID = 1L;

	@Column(name = "DATA_TYPE", length = 50)
	private String dataType;

	@Column(name = "LABEL", length = 60)
	private String label;

	@Id
	@Column(name = "PROCESS_NAME", length = 32)
	private String processName;

	@Id
	@Column(name = "KEY", length = 50)
	private String key;

	@Column(name = "IS_ACTIVE")
	@ColumnDefault(value = "1")
	private Boolean isActive = true;

	@Column(name = "ATTR_DES", length = 200)
	private String description;

	@Transient
	private String value;

	/* Added for Custom Detail Screen */

	@Column(name = "IS_MAND")
	private Boolean isMandatory;

	@Column(name = "IS_EDITABLE")
	private Boolean isEditable;

	@Column(name = "ORIGIN")
	private String origin;

	@Column(name = "ATTRIBUTE_PATH")
	private String attributePath;

	@Column(name = "DEPENDANT_ON")
	private String dependantOn;
	
	@Column(name = "IS_VISIBLE")
	private Boolean isVisible;
	
	@Column(name = "IS_DELETED")
	private Boolean isDeleted;
	
	@Column(name = "IS_RUN_TIME")
	private Boolean isRunTime;
	
	@Column(name = "RUN_TIME_TYPE")
	private String runTimeType;
	
	@Column(name = "DEFAULT_VALUE")
	private String defaultValue;
	
	@Column(name = "SEQUENCE")
	private Integer sequence;

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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

	@Transient
	private List<CustomAttributeValue> attributeValues;

	@Transient
	private List<CustomAttributeTemplate> customDto;
	
	@Transient
	private String url;
	
	@Transient
	private String dropDownType;

	public String getRunTimeType() {
		return runTimeType;
	}

	public void setRunTimeType(String runTimeType) {
		this.runTimeType = runTimeType;
	}

	public String getDropDownType() {
		return dropDownType;
	}

	public void setDropDownType(String dropDownType) {
		this.dropDownType = dropDownType;
	}

	public Boolean getIsRunTime() {
		return isRunTime;
	}

	public void setIsRunTime(Boolean isRunTime) {
		this.isRunTime = isRunTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public List<CustomAttributeTemplate> getCustomDto() {
		return customDto;
	}

	public void setCustomDto(List<CustomAttributeTemplate> customDto) {
		this.customDto = customDto;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public List<CustomAttributeValue> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(List<CustomAttributeValue> attributeValues) {
		this.attributeValues = attributeValues;
	}

	public String getDependantOn() {
		return dependantOn;
	}

	public void setDependantOn(String dependantOn) {
		this.dependantOn = dependantOn;
	}

	@Override
	public String toString() {
		return "CustomAttributeTemplate [dataType=" + dataType + ", label=" + label + ", processName=" + processName
				+ ", key=" + key + ", isActive=" + isActive + ", description=" + description + ", value=" + value
				+ ", isMandatory=" + isMandatory + ", isEditable=" + isEditable + ", origin=" + origin
				+ ", attributePath=" + attributePath + ", dependantOn=" + dependantOn + ", isVisible=" + isVisible
				+ ", isDeleted=" + isDeleted + ", isRunTime=" + isRunTime + ", runTimeType=" + runTimeType
				+ ", defaultValue=" + defaultValue + ", sequence=" + sequence + ", attributeValues=" + attributeValues
				+ ", customDto=" + customDto + ", url=" + url + ", dropDownType=" + dropDownType + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	


	
}
