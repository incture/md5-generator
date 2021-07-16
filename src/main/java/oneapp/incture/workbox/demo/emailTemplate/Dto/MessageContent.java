package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.List;

public class MessageContent {

	String messageBody;
	List<AttachmentResponseDto> attributesDetails;
	@Override
	public String toString() {
		return "MessageContent [messageBody=" + messageBody + ", attributesDetails=" + attributesDetails + "]";
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public List<AttachmentResponseDto> getAttributesDetails() {
		return attributesDetails;
	}
	public void setAttributesDetails(List<AttachmentResponseDto> attributesDetails) {
		this.attributesDetails = attributesDetails;
	}
	
	
}
