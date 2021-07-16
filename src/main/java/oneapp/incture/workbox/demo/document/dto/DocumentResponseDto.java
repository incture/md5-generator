package oneapp.incture.workbox.demo.document.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class DocumentResponseDto {
	
	private ResponseMessage response;
	private List<AttachmentRequestDto> attachments;
	private List<AttachmentDetail> attachmentIds; 
	
	
	
	public List<AttachmentDetail> getAttachmentIds() {
		return attachmentIds;
	}
	public void setAttachmentIds(List<AttachmentDetail> attachmentIds) {
		this.attachmentIds = attachmentIds;
	}
	public ResponseMessage getResponse() {
		return response;
	}
	public void setResponse(ResponseMessage response) {
		this.response = response;
	}
	public List<AttachmentRequestDto> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentRequestDto> attachments) {
		this.attachments = attachments;
	}
	@Override
	public String toString() {
		return "DocumentResponseDto [response=" + response + ", attachments=" + attachments + ", attachmentIds="
				+ attachmentIds + "]";
	}
	
	

}
