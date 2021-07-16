package oneapp.incture.workbox.demo.versionControl.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class VersionsDto extends BaseDto {
	
	private String versionNumber;
	private String dateofRelease;
	
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
	
	@Override
	public String toString() {
		return "VersionsDto [versionNumber=" + versionNumber + ", dateofRelease=" + dateofRelease + "]";
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
