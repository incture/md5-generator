package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.List;

import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;

public class EmailContent {

	private String templateId;
	private int version;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String messageBody;
	private Boolean isAttachment;
	private List<AttachmentResponseDto> attachmentDetail;
	private List<AttachmentResponseDto> attachmentList;
	private Boolean isTableContent;
	private String tableContentId;
	private String attachmentId;
	private List<EmailTableContentDto> emailTableContent;
	private String frameId;
	@Override
	public String toString() {
		return "EmailContent [templateId=" + templateId + ", verssion=" + version + ", to=" + to + ", cc=" + cc + ", bcc=" + bcc + ", subject="
				+ subject + ", messageBody=" + messageBody + ", isAttachment=" + isAttachment + ", attachmentDetail="
				+ attachmentDetail + ", isTableContent=" + isTableContent + ", tableContentId=" + tableContentId
				+ ", attachmentId=" + attachmentId + ", emailTableContent=" + emailTableContent + ", frameId=" + frameId
				+ "]";
	}
	public List<AttachmentResponseDto> getAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<AttachmentResponseDto> attachmentList) {
		this.attachmentList = attachmentList;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public Boolean getIsAttachment() {
		return isAttachment;
	}
	public void setIsAttachment(Boolean isAttachment) {
		this.isAttachment = isAttachment;
	}
	public List<AttachmentResponseDto> getAttachmentDetail() {
		return attachmentDetail;
	}
	public void setAttachmentDetail(List<AttachmentResponseDto> attachmentDetail) {
		this.attachmentDetail = attachmentDetail;
	}
	public Boolean getIsTableContent() {
		return isTableContent;
	}
	public void setIsTableContent(Boolean isTableContent) {
		this.isTableContent = isTableContent;
	}
	public String getTableContentId() {
		return tableContentId;
	}
	public void setTableContentId(String tableContentId) {
		this.tableContentId = tableContentId;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public List<EmailTableContentDto> getEmailTableContent() {
		return emailTableContent;
	}
	public void setEmailTableContent(List<EmailTableContentDto> emailTableContent) {
		this.emailTableContent = emailTableContent;
	}
	public String getFrameId() {
		return frameId;
	}
	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}
	
	
}
