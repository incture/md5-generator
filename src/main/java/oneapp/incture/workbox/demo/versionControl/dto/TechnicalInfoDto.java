package oneapp.incture.workbox.demo.versionControl.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class TechnicalInfoDto extends BaseDto {
	
	private String versionNumber;
	private String dateofRelease;
	private String language;
	private String osDetails;
	private String author;
	private String applicationSize;
	private String users;
	private String frontendVersion;
	private String gitDetails;
	private String validForUsage;
	
	
	public void setValidForUsage(String validForUsage) {
		this.validForUsage = validForUsage;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getDateofRelease() {
		return dateofRelease;
	}
	public void setDateofRelease(String dateofRelease) {
		this.dateofRelease = dateofRelease;
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
	public String toString() {
		return "TechnicalInfoDto [versionNumber=" + versionNumber + ", dateofRelease=" + dateofRelease + ", language="
				+ language + ", osDetails=" + osDetails + ", author=" + author + ", applicationSize=" + applicationSize
				+ ", users=" + users + ", frontendVersion=" + frontendVersion + ", gitDetails=" + gitDetails
				+ ", validForUsage=" + validForUsage + "]";
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
