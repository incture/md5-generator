package oneapp.incture.workbox.demo.emailTemplate.Dto;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class EmailTemplateGenericDto {
	
	
	private EmailTemplate emailTemplate;
	private ResponseMessage message;
	
    
	@Override
	public String toString() {
		return "EmailTemplateGenericDto [emailTemplate=" + emailTemplate + ", message=" + message + "]";
	}
	
	
	public EmailTemplate getEmailTemplate() {
		return emailTemplate;
	}


	public void setEmailTemplate(EmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
	}


	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	
	

}
