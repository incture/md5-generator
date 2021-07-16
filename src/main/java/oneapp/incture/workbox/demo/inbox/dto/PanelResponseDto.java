package oneapp.incture.workbox.demo.inbox.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class PanelResponseDto {
	private List<InboxTypeDto> inboxTypeDtos;
	private ResponseMessage responseMessage;
	public List<InboxTypeDto> getInboxTypeDtos() {
		return inboxTypeDtos;
	}
	public void setInboxTypeDtos(List<InboxTypeDto> inboxTypeDtos) {
		this.inboxTypeDtos = inboxTypeDtos;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	@Override
	public String toString() {
		return "PanelResponseDto [inboxTypeDtos=" + inboxTypeDtos + ", responseMessage=" + responseMessage + "]";
	}
	
	
}
