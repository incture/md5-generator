package oneapp.incture.workbox.demo.dynamicpage.dto;

public class ProcessDetailsCatalogueMappingDto {

	private String catalogueName;
	private String key;
	private String processName;
	private String label;
	private Boolean visibility;
	private Boolean isMandatory;
	private String dataType;
	private String serviceUrl;
	private String dependency;
	private String groupId;
	private String eccKey;
	private Boolean editability;

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
	public Boolean getIsMandatory() {
		return isMandatory;
	}
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getServiceUrl() {
		return serviceUrl;
	}
	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	public String getDependency() {
		return dependency;
	}
	public void setDependency(String dependency) {
		this.dependency = dependency;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getEccKey() {
		return eccKey;
	}
	public void setEccKey(String eccKey) {
		this.eccKey = eccKey;
	}
	public Boolean getEditability() {
		return editability;
	}
	public void setEditability(Boolean editability) {
		this.editability = editability;
	}
	public Boolean getVisibility() {
		return visibility;
	}
	public void setVisibility(Boolean visibility) {
		this.visibility = visibility;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getCatalogueName() {
		return catalogueName;
	}
	public void setCatalogueName(String catalogueName) {
		this.catalogueName = catalogueName;
	}
	@Override
	public String toString() {
		return "PurchaseOrderPlantMappingDto [catalogueName=" + catalogueName + ", key=" + key + ", processName="
				+ processName + ", label=" + label + ", visibility=" + visibility + ", isMandatory=" + isMandatory
				+ ", dataType=" + dataType + ", serviceUrl=" + serviceUrl + ", dependency=" + dependency + ", groupId="
				+ groupId + ", eccKey=" + eccKey + ", editability=" + editability + "]";
	}
	
	
}
