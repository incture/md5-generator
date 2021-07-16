package oneapp.incture.workbox.demo.dynamicpage.dto;

import java.util.List;

public class DynamicPageGroupResponseDto {

	private String catalogueName;
	private String groupId;
	private String groupType;
	private Boolean visibility;
	private Boolean isEditable;
	private String title;
	private String processName;
	private List<ProcessDetailsMasterDto> poFieldDetails;
	
	
	public String getCatalogueName() {
		return catalogueName;
	}
	public void setCatalogueName(String catalogueName) {
		this.catalogueName = catalogueName;
	}
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
	public List<ProcessDetailsMasterDto> getPoFieldDetails() {
		return poFieldDetails;
	}
	public void setPoFieldDetails(List<ProcessDetailsMasterDto> poFieldDetails) {
		this.poFieldDetails = poFieldDetails;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	@Override
	public String toString() {
		return "DynamicPageGroupResponseDto [catalogueName=" + catalogueName + ", groupId=" + groupId + ", groupType="
				+ groupType + ", visibility=" + visibility + ", isEditable=" + isEditable + ", title=" + title
				+ ", processName=" + processName + ", poFieldDetails=" + poFieldDetails + "]";
	}
	
}
