package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class CrossConstantResponseDto {

	List<CrossConstantDto> dto ;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "CrossConstantResponseDto [dto=" + dto + ", responseMessage=" + responseMessage + "]";
	}
	public List<CrossConstantDto> getDto() {
		return dto;
	}
	public void setDto(List<CrossConstantDto> dto) {
		this.dto = dto;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
}
