package oneapp.incture.workbox.demo.inbox.dto;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class WorkflowResponseDto {

	private Object data;
	private ResponseMessage responseMessage;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "WorkflowResponseDto [data=" + data + ", responseMessage=" + responseMessage + "]";
	}	
	
}
