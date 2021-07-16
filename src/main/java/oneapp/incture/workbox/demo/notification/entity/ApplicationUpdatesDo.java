package oneapp.incture.workbox.demo.notification.entity;

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
@Table(name = "APPLICATION_UPDATES")
public class ApplicationUpdatesDo implements BaseDo,Serializable{


	private static final long serialVersionUID = -338057976909649600L;

	@Id
	@Column(name = "ID" , columnDefinition = "VARCHAR(255)")
	private String id;
	
	@Column(name = "PROJECT_CODE" , columnDefinition = "VARCHAR(255)")
	private String projectCode;
	
	@Column(name = "PROJECT_NAME" , columnDefinition = "VARCHAR(255)")
	private String projectName;
	
	@Id
	@Column(name = "VERSION_NUMBER" , columnDefinition = "VARCHAR(255)")
	private String versionNumber;
	
	@Column(name = "DATE_OF_RELEASE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOfRelease;
	
	@Column(name = "VERSION_DETAILS" , columnDefinition = "VARCHAR(255)")
	private String versionDetails;
	
	@Column(name = "ROLES" , columnDefinition = "VARCHAR(255)")
	private String roles;
	
	@Column(name = "DESCRIPTION" , columnDefinition = "VARCHAR(255)")
	private String description;
	
	@Column(name = "ORGANISATION" , columnDefinition = "VARCHAR(255)")
	private String organisation;
	
	@Column(name = "ACTION_TYPE" , columnDefinition = "VARCHAR(255)")
	private String actionType;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Date getDateOfRelease() {
		return dateOfRelease;
	}

	public void setDateOfRelease(Date dateOfRelease) {
		this.dateOfRelease = dateOfRelease;
	}

	public String getVersionDetails() {
		return versionDetails;
	}

	public void setVersionDetails(String versionDetails) {
		this.versionDetails = versionDetails;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		return "ApplicationUpdatesDo [id=" + id + ", projectCode=" + projectCode + ", projectName=" + projectName
				+ ", versionNumber=" + versionNumber + ", dateOfRelease=" + dateOfRelease + ", versionDetails="
				+ versionDetails + ", roles=" + roles + ", description=" + description + ", organisation="
				+ organisation + ", actionType=" + actionType + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return id;
	}
	
	
}
