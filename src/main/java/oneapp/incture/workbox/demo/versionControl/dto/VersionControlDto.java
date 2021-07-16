package oneapp.incture.workbox.demo.versionControl.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class VersionControlDto extends BaseDto {
	
	private String versionId;
	private String versionNumber;
	private String projectCode;
	private String projectName;
	private Date dateOfRelease;
	private String dateofRelease;
	private String detailType;
	private String labelDesc;
	private String description;
	private String linkLabel;
	private String link;
	private String documentId;
	private String language;
	private String osDetails;
	private String author;
	private String applicationSize;
	private String users;
	private String frontendVersion;
	private String gitDetails;

	
	public String getVersionNumber() {
		return versionNumber;
	}

	public String getDateofRelease() {
		return dateofRelease;
	}

	public void setDateofRelease(String dateofRelease) {
		this.dateofRelease = dateofRelease;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getDateOfRelease() {
		return dateOfRelease;
	}

	public void setDateOfRelease(Date dateOfRelease) {
		this.dateOfRelease = dateOfRelease;
	}

	public String getDetailType() {
		return detailType;
	}

	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}

	public String getLabelDesc() {
		return labelDesc;
	}

	public void setLabelDesc(String labelDesc) {
		this.labelDesc = labelDesc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLinkLabel() {
		return linkLabel;
	}

	public void setLinkLabel(String linkLabel) {
		this.linkLabel = linkLabel;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOsDetails() {
		return osDetails;
	}

	public void setOsDetails(String osDetails) {
		this.osDetails = osDetails;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getApplicationSize() {
		return applicationSize;
	}

	public void setApplicationSize(String applicationSize) {
		this.applicationSize = applicationSize;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getFrontendVersion() {
		return frontendVersion;
	}

	public void setFrontendVersion(String frontendVersion) {
		this.frontendVersion = frontendVersion;
	}

	public String getGitDetails() {
		return gitDetails;
	}

	public void setGitDetails(String gitDetails) {
		this.gitDetails = gitDetails;
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	@Override
	public String toString() {
		return "VersionControlDto [versionId=" + versionId + ", versionNumber=" + versionNumber + ", projectCode="
				+ projectCode + ", projectName=" + projectName + ", dateOfRelease=" + dateOfRelease + ", dateofRelease="
				+ dateofRelease + ", detailType=" + detailType + ", labelDesc=" + labelDesc + ", description="
				+ description + ", linkLabel=" + linkLabel + ", link=" + link + ", documentId=" + documentId
				+ ", language=" + language + ", osDetails=" + osDetails + ", author=" + author + ", applicationSize="
				+ applicationSize + ", users=" + users + ", frontendVersion=" + frontendVersion + ", gitDetails="
				+ gitDetails + "]";
	}
	
}
