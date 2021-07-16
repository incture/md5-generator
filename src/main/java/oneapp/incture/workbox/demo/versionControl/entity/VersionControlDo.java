package oneapp.incture.workbox.demo.versionControl.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "VERSION_CONTROL")

public class VersionControlDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VERSION_ID", columnDefinition = "VARCHAR(255)")
	private String versionId;
	
	@Column(name = "VERSION_NUMBER", columnDefinition = "VARCHAR(255)")
	private String versionNumber;

	@Column(name = "PROJECT_CODE", columnDefinition = "VARCHAR(255)")
	private String projectCode;

	@Column(name = "PROJECT_NAME", columnDefinition = "VARCHAR(255)")
	private String projectName;

	@Column(name = "DATE_OF_RELEASE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfRelease;

	@Column(name = "DETAIL_TYPE", columnDefinition = "VARCHAR(255)")
	private String detailType;
	
	@Column(name = "LABEL_DESC", columnDefinition = "VARCHAR(255)")
	private String labelDesc;
	
	@Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(255)")
	private String description;
	
	@Column(name = "LINK_LABEL", columnDefinition = "VARCHAR(255)")
	private String linkLabel;
	
	@Column(name = "LINK", columnDefinition = "VARCHAR(255)")
	private String link;
	
	@Column(name = "DOCUMENT_ID", columnDefinition = "VARCHAR(255)")
	private String documentId;
	
	@Column(name = "LANGUAGE", columnDefinition = "VARCHAR(255)")
	private String language;
	
	@Column(name = "OS_DETAILS", columnDefinition = "VARCHAR(255)")
	private String osDetails;
	
	@Column(name = "AUTHOR", columnDefinition = "VARCHAR(255)")
	private String author;
	
	@Column(name = "APPLICATION_SIZE", columnDefinition = "VARCHAR(255)")
	private String applicationSize;
	
	@Column(name = "USERS", columnDefinition = "VARCHAR(255)")
	private String users;
	
	@Column(name = "FRONTEND_VERSION", columnDefinition = "VARCHAR(255)")
	private String frontendVersion;
	
	@Column(name = "GIT_DETAILS", columnDefinition = "VARCHAR(255)")
	private String gitDetails;
	
	public String getVersionNumber() {
		return versionNumber;
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

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Object getPrimaryKey() {
		return null;
	}

	@Override
	public String toString() {
		return "VersionControlDo [versionId=" + versionId + ", versionNumber=" + versionNumber + ", projectCode="
				+ projectCode + ", projectName=" + projectName + ", dateOfRelease=" + dateOfRelease + ", detailType="
				+ detailType + ", labelDesc=" + labelDesc + ", description=" + description + ", linkLabel=" + linkLabel
				+ ", link=" + link + ", documentId=" + documentId + ", language=" + language + ", osDetails="
				+ osDetails + ", author=" + author + ", applicationSize=" + applicationSize + ", users=" + users
				+ ", frontendVersion=" + frontendVersion + ", gitDetails=" + gitDetails + "]";
	}

}