package oneapp.incture.workbox.demo.versionControl.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class VersionControlResponseDto extends BaseDto {
	
	VersionDetailDto versionDetails;
	List<VersionsDto> versions;
	ResponseMessage message;
	
	public List<VersionsDto> getVersions() {
		return versions;
	}
	public void setVersions(List<VersionsDto> versions) {
		this.versions = versions;
	}
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	
	public VersionDetailDto getVersionDetails() {
		return versionDetails;
	}
	public void setVersionDetails(VersionDetailDto versionDetails) {
		this.versionDetails = versionDetails;
	}
	
	
	@Override
	public String toString() {
		return "VersionControlResponseDto [versionDetails=" + versionDetails + ", versions=" + versions + ", message="
				+ message + "]";
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
