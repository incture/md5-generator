package oneapp.incture.workbox.demo.dynamicpage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DYNAMIC_GROUPS_TEMPLATE")
public class DynamicPageGroups implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -613117824459665415L;

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
	
	@Column(name = "IS_EDITABLE")
	private Boolean isEditable;
	
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

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
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

	@Override
	public String toString() {
		return "DynamicPageGroups [groupId=" + groupId + ", processName=" + processName + ", groupType=" + groupType
				+ ", visibility=" + visibility + ", isEditable=" + isEditable + ", title=" + title + "]";
	}
	
}
