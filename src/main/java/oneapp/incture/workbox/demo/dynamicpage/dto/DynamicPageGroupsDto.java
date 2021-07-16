package oneapp.incture.workbox.demo.dynamicpage.dto;

public class DynamicPageGroupsDto {

	private String groupId;
	private String processName;
	private String groupType;
	private String visibility;
	private Boolean isEditable;
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
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
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
		return "DynamicPageGroupsDto [groupId=" + groupId + ", processName=" + processName + ", groupType=" + groupType
				+ ", visibility=" + visibility + ", isEditable=" + isEditable + ", title=" + title + "]";
	}
	
}
