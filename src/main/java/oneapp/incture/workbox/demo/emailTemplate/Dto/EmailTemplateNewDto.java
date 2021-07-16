package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;


public class EmailTemplateNewDto {
	private List<EmailTemplate> emailTemplates;
	private ResponseMessage responseMeassage;
	
	
	

	@Override
	public String toString() {
		return "EmailTemplateNewDto [emailTemplates=" + emailTemplates + ", responseMeassage=" + responseMeassage + "]";
	}



	public List<EmailTemplate> getEmailTemplates() {
		return emailTemplates;
	}



	public void setEmailTemplates(List<EmailTemplate> emailTemplates) {
		this.emailTemplates = emailTemplates;
	}



	public ResponseMessage getResponseMeassage() {
		return responseMeassage;
	}

	public void setResponseMeassage(ResponseMessage responseMeassage) {
		this.responseMeassage = responseMeassage;
	}



}
