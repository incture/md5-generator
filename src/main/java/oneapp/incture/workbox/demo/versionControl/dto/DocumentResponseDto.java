package oneapp.incture.workbox.demo.versionControl.dto;

import java.util.ArrayList;
import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;


public class DocumentResponseDto {
	
	private ResponseMessage response;
	private List<AttachmentDto> attachments;
	private String documentId;
	
	public ResponseMessage getResponse() {
		return response;
	}

	public void setResponse(ResponseMessage response) {
		this.response = response;
	}

	public List<AttachmentDto> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDto> attachments) {
		this.attachments = attachments;
	}

	public void addAttachment(AttachmentDto attachments) {
		if(this.attachments == null)
			this.attachments = new ArrayList<>();
		this.attachments.add(attachments);
	}
	
	public void addAttachments(List<AttachmentDto> attachmentDetails) {
		if(this.attachments == null)
			this.attachments = new ArrayList<>();
		this.attachments.addAll(attachmentDetails);
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Override
	public String toString() {
		return "DocumentResponseDto [response=" + response + ", attachments=" + attachments + ", documentId="
				+ documentId + "]";
	}

}
