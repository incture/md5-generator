package oneapp.incture.workbox.demo.workflow.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class ProcessConfigListDto {
	private List<ProcessConfigTbDto> processDetails;
	private ResponseMessage responseMessage ;
	public ProcessConfigListDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProcessConfigListDto(List<ProcessConfigTbDto> processDetails, ResponseMessage responseMessage) {
		super();
		this.processDetails = processDetails;
		this.responseMessage = responseMessage;
	}
	public List<ProcessConfigTbDto> getProcessDetails() {
		return processDetails;
	}
	public void setProcessDetails(List<ProcessConfigTbDto> processDetails) {
		this.processDetails = processDetails;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "ProcessConfigListDto [processDetails=" + processDetails + ", responseMessage=" + responseMessage + "]";
	}	
}
