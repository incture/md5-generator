package oneapp.incture.workbox.demo.dynamicpage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATALOGUE_GROUP_MAPPING")
public class CatalogueGroupMapping implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5759860695607045799L;

	@Id
	@Column(name = "CATALOGUE_NAME")
	private String catalogueName;
	
	@Id
	@Column(name = "GROUP_ID")
	private String groupId;
	
	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;
	
	@Column(name = "GROUP_TYPE")
	private String groupType;
	
	@Column(name = "VISIBILITY")
	private Boolean visibility;
	
	@Column(name = "EDITABILITY")
	private Boolean editability;
	
	@Column(name = "TITLE")
	private String title;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
		return "PlantGroupMapping [catalogueName=" + catalogueName + ", groupId=" + groupId + ", processName="
				+ processName + ", groupType=" + groupType + ", visibility=" + visibility + ", editability="
				+ editability + ", title=" + title + "]";
	}
	
	
}
