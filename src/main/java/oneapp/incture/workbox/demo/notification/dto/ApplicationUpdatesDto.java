package oneapp.incture.workbox.demo.notification.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class ApplicationUpdatesDto extends BaseDto{

	private String id;
	private String projectCode;
	private String projectName;
	private String versionNumber;
	private String versionDetails;
	private String roles;
	private String description;
	private String organisation;
	private Date dateOfRelease;
	private String actionType;

	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public Date getDateOfRelease() {
		return dateOfRelease;
	}
	public void setDateOfRelease(Date dateOfRelease) {
		this.dateOfRelease = dateOfRelease;
	}
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	@Override
	public String toString() {
		return "ApplicationUpdatesDto [id=" + id + ", projectCode=" + projectCode + ", projectName=" + projectName
				+ ", versionNumber=" + versionNumber + ", versionDetails=" + versionDetails + ", roles=" + roles
				+ ", description=" + description + ", organisation=" + organisation + ", dateOfRelease=" + dateOfRelease
				+ ", actionType=" + actionType + "]";
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
	
	
	
}
