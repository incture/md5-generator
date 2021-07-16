package oneapp.incture.workbox.demo.dynamicpage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROCESS_DETAILS_CATALOGUE_MAPPING")
public class ProcessDetailsCatalogueMapping implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1469610687403361369L;

	@Id
	@Column(name = "CATALOGUE_NAME")
	private String catalogueName;
	
	@Id
	@Column(name = "KEY")
	private String key;
	
	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;
	
	@Column(name = "LABEL")
	private String label;
	
	@Column(name = "VISIBILITY")
	private Boolean visibility;
	
	@Column(name = "IS_MANDATORY")
	private Boolean isMandatory;
	
	@Column(name = "DATA_TYPE")
	private String dataType;
	
	@Column(name = "SERVICE_URL")
	private String serviceUrl;
	
	@Column(name = "DEPENDENCY")
	private String dependency;
	
	@Column(name = "GROUP_ID")
	private String groupId;
	
	@Column(name = "ECC_KEY")
	private String eccKey;
	
	@Column(name = "EDITABILITY")
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

	public Boolean getVisibility() {
		return visibility;
	}

	public void setVisibility(Boolean visibility) {
		this.visibility = visibility;
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
		return "ProcessDetailsCatalogueMapping [catalogueName=" + catalogueName + ", key=" + key + ", processName="
				+ processName + ", label=" + label + ", visibility=" + visibility + ", isMandatory=" + isMandatory
				+ ", dataType=" + dataType + ", serviceUrl=" + serviceUrl + ", dependency=" + dependency + ", groupId="
				+ groupId + ", eccKey=" + eccKey + ", editability=" + editability + "]";
	}

	

}
