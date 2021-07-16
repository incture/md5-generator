package oneapp.incture.workbox.demo.dynamicpage.dto;

public class CatalogueGroupMappingDto {

	private String catalogueName;
	private String groupId;
	private String groupType;
	private Boolean visibility;
	private Boolean editability;
	private String title;
	private String processName;
	
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
		return "PlantGroupMappingDto [catalogueName=" + catalogueName + ", groupId=" + groupId + ", groupType="
				+ groupType + ", visibility=" + visibility + ", editability=" + editability + ", title=" + title
				+ ", processName=" + processName + "]";
	}
	
	
}
