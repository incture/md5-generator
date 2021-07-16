package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.List;

public class EmailTemplateCreation {

	private EmailTemplateDto emailTemplateDto;
	private EmailContentDto emailContentDto;
	private List<EmailTableContentDto> emailTableContentDto;
	private List<AttachmentsDto> attachmentsDtos;
	@Override
	public String toString() {
		return "EmailTemplateCreation [emailTemplateDto=" + emailTemplateDto + ", emailContentDto=" + emailContentDto
				+ ", emailTableContentDto=" + emailTableContentDto + "]";
	}
	public List<AttachmentsDto> getAttachmentsDtos() {
		return attachmentsDtos;
	}
	public void setAttachmentsDtos(List<AttachmentsDto> attachmentsDtos) {
		this.attachmentsDtos = attachmentsDtos;
	}
	public EmailTemplateDto getEmailTemplateDto() {
		return emailTemplateDto;
	}
	public void setEmailTemplateDto(EmailTemplateDto emailTemplateDto) {
		this.emailTemplateDto = emailTemplateDto;
	}
	public EmailContentDto getEmailContentDto() {
		return emailContentDto;
	}
	public void setEmailContentDto(EmailContentDto emailContentDto) {
		this.emailContentDto = emailContentDto;
	}
	public List<EmailTableContentDto> getEmailTableContentDto() {
		return emailTableContentDto;
	}
	public void setEmailTableContentDto(List<EmailTableContentDto> emailTableContentDto) {
		this.emailTableContentDto = emailTableContentDto;
	}
	
	
}
