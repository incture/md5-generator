package oneapp.incture.workbox.demo.inbox.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class UserIDPMappingResponseDto extends BaseDto {

	private List<UserIDPMappingDto> dto;
	private ResponseMessage message;
	

	@Override
	public String toString() {
		return "UserIDPMappingResponseDto [dto=" + dto + ", message=" + message + "]";
	}

	public List<UserIDPMappingDto> getDto() {
		return dto;
	}

	public void setDto(List<UserIDPMappingDto> dto) {
		this.dto = dto;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	};
	

}
