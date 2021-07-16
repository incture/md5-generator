package oneapp.incture.workbox.demo.emailTemplate.Dto;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class EmailTemplate {
	
	EmailTemplateDto emailTemplate;
	EmailContent emailContent;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "EmailTemplate [emailTemplate=" + emailTemplate + ", emailContent=" + emailContent + ", responseMessage="
				+ responseMessage + "]";
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	public EmailTemplateDto getEmailTemplate() {
		return emailTemplate;
	}
	public void setEmailTemplate(EmailTemplateDto emailTemplate) {
		this.emailTemplate = emailTemplate;
	}
	public EmailContent getEmailContent() {
		return emailContent;
	}
	public void setEmailContent(EmailContent emailContent) {
		this.emailContent = emailContent;
	}
	
	
}
