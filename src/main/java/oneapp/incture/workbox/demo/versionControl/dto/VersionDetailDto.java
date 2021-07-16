package oneapp.incture.workbox.demo.versionControl.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class VersionDetailDto extends BaseDto {
	
	private String versionNumber;
	private String projectCode;
	private String projectName;
	
	List<TypesDetailDto> whatsNew;
	List<TypesDetailDto> bugFixes;
	List<TypesDetailDto> improvements;
	
	TechnicalInfoDto technicalInformation;

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

	public TechnicalInfoDto getTechnicalInformation() {
		return technicalInformation;
	}

	public void setTechnicalInformation(TechnicalInfoDto technicalInformation) {
		this.technicalInformation = technicalInformation;
	}
	
	public List<TypesDetailDto> getWhatsNew() {
		return whatsNew;
	}

	public void setWhatsNew(List<TypesDetailDto> whatsNew) {
		this.whatsNew = whatsNew;
	}

	public List<TypesDetailDto> getBugFixes() {
		return bugFixes;
	}

	public void setBugFixes(List<TypesDetailDto> bugFixes) {
		this.bugFixes = bugFixes;
	}

	public List<TypesDetailDto> getImprovements() {
		return improvements;
	}

	public void setImprovements(List<TypesDetailDto> improvements) {
		this.improvements = improvements;
	}
	
	

	@Override
	public String toString() {
		return "VersionDetailDto [versionNumber=" + versionNumber + ", projectCode=" + projectCode + ", projectName="
				+ projectName + ", whatsNew=" + whatsNew + ", bugFixes=" + bugFixes + ", improvements=" + improvements
				+ ", technicalInformation=" + technicalInformation + ", validForUsage=" + validForUsage + "]";
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
