package oneapp.incture.workbox.demo.dynamicpage.dto;

public class ProcessDetailsMasterDto {

	private String key;
	private String processName;
	private String label;
	private Boolean isMandatory;
	private String dataType;
	private Boolean visibility;
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
	public Boolean getVisibility() {
		return visibility;
	}
	public void setVisibility(Boolean visibility) {
		this.visibility = visibility;
	}
	public Boolean getEditability() {
		return editability;
	}
	public void setEditability(Boolean editability) {
		this.editability = editability;
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
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	@Override
	public String toString() {
		return "ProcessDetailsMasterDto [key=" + key + ", processName=" + processName + ", label=" + label
				+ ", isMandatory=" + isMandatory + ", dataType=" + dataType + ", visibility=" + visibility
				+ ", serviceUrl=" + serviceUrl + ", dependency=" + dependency + ", groupId=" + groupId + ", eccKey="
				+ eccKey + ", editability=" + editability + "]";
	}
	
}
