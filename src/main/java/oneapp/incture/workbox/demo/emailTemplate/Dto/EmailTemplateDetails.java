package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class EmailTemplateDetails {

	List<EmailTemplateDto> emailTemplateDtos;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "EmailTemplateDetails [emailTemplateDtos=" + emailTemplateDtos + ", responseMessage=" + responseMessage
				+ "]";
	}
	public List<EmailTemplateDto> getEmailTemplateDtos() {
		return emailTemplateDtos;
	}
	public void setEmailTemplateDtos(List<EmailTemplateDto> emailTemplateDtos) {
		this.emailTemplateDtos = emailTemplateDtos;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
}
