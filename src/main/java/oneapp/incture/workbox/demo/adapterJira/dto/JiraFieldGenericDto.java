package oneapp.incture.workbox.demo.adapterJira.dto;

public class JiraFieldGenericDto {
	private String assignee;
	private String defaultRootCauseId;
	private String defaultRootCauseName;
	private String resolution;
	private String comment;
	private String description;
	private String summary;
	
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public String getDefaultRootCauseId() {
		return defaultRootCauseId;
	}
	public void setDefaultRootCauseId(String defaultRootCauseId) {
		this.defaultRootCauseId = defaultRootCauseId;
	}
	public String getDefaultRootCauseName() {
		return defaultRootCauseName;
	}
	public void setDefaultRootCauseName(String defaultRootCauseName) {
		this.defaultRootCauseName = defaultRootCauseName;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@Override
	public String toString() {
		return "JiraFieldGenericDto [assignee=" + assignee + ", defaultRootCauseId=" + defaultRootCauseId
				+ ", defaultRootCauseName=" + defaultRootCauseName + ", resolution=" + resolution + ", comment="
				+ comment + ", description=" + description + ", summary=" + summary + "]";
	}
}
