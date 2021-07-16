package oneapp.incture.workbox.demo.emailTemplate.Dto;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class EmailContentDto extends BaseDto {

	private String templateId;
	private int version;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String messageBody;
	private Boolean isAttachment;
	private String attachmentId;
	private Boolean isTableContent;
	private String tableContentId;
	private String frameId;

	@Override
	public String toString() {
		return "EmailContentDto [templateId=" + templateId + ",verssion=" + version + ", to=" + to + ", cc=" + cc
				+ ", bcc=" + bcc + ", subject=" + subject + ", messageBody=" + messageBody + ", isAttachment="
				+ isAttachment + ", attachmentId=" + attachmentId + ", isTableContent=" + isTableContent
				+ ", tableContentId=" + tableContentId + ", frameId=" + frameId + "]";
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

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
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

	public String getFrameId() {
		return frameId;
	}

	public void setFrameId(String frameId) {
		this.frameId = frameId;
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

}
