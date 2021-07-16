package oneapp.incture.workbox.demo.manageGroup.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class GroupResponseDto {

	List<GroupDetailDto> dto;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "GroupResponseDto [dto=" + dto + ", responseMessage=" + responseMessage + "]";
	}
	public List<GroupDetailDto> getDto() {
		return dto;
	}
	public void setDto(List<GroupDetailDto> dto) {
		this.dto = dto;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	

	
	
}
