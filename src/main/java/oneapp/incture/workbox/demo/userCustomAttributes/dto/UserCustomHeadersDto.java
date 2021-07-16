package oneapp.incture.workbox.demo.userCustomAttributes.dto;

public class UserCustomHeadersDto {

	private String userId;
	
	private String processName;
	
	private String key;
	
	private String label;
	
	private Integer sequence;
	
	private Boolean isActive;
	
	private String dataType;
	
	private Boolean isMandatory;
	
	private Boolean isEditable;
	
	private Boolean isVisible;
	
	private Boolean isDeleted;
	
	private String origin;
	
	private String description;
	
	private String customDto;
	private String primaryKey;
	private String attributeValues;
	private String value;
	private String attributePath;
	private String dependantOn;
	private String defaultValue;
	private String dropDownType;
	private Boolean isRunTime;
	private String runTimeType;
	private String url;
	
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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

	public String getRunTimeType() {
		return runTimeType;
	}

	public void setRunTimeType(String runTimeType) {
		this.runTimeType = runTimeType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAttributePath() {
		return attributePath;
	}

	public void setAttributePath(String attributePath) {
		this.attributePath = attributePath;
	}

	public String getDependantOn() {
		return dependantOn;
	}

	public void setDependantOn(String dependantOn) {
		this.dependantOn = dependantOn;
	}

	public String getCustomDto() {
		return customDto;
	}

	public void setCustomDto(String customDto) {
		this.customDto = customDto;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(String attributeValues) {
		this.attributeValues = attributeValues;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	@Override
	public String toString() {
		return "UserCustomHeadersDto [userId=" + userId + ", processName=" + processName + ", key=" + key + ", label="
				+ label + ", sequence=" + sequence + ", isActive=" + isActive + ", dataType=" + dataType
				+ ", isMandatory=" + isMandatory + ", isEditable=" + isEditable + ", isVisible=" + isVisible
				+ ", isDeleted=" + isDeleted + ", origin=" + origin + ", description=" + description + ", customDto="
				+ customDto + ", primaryKey=" + primaryKey + ", attributeValues=" + attributeValues + ", value=" + value
				+ ", attributePath=" + attributePath + ", dependantOn=" + dependantOn + ", defaultValue=" + defaultValue
				+ ", dropDownType=" + dropDownType + ", isRunTime=" + isRunTime + ", runTimeType=" + runTimeType
				+ ", url=" + url + "]";
	}
	
}
