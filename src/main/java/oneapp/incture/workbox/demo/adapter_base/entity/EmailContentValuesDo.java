package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMAIL_CONTENT_VALUES")
public class EmailContentValuesDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4236048215579398481L;

	@Id
	@Column(name = "TEMPLATE_ID")
	private String templateId;
	
	@Id
	@Column(name = "EVENT_ID")
	private String eventId;
	
	@Column(name = "TO")
	private String to;
	
	@Column(name = "CC")
	private String cc;
	
	@Column(name = "BCC")
	private String bcc;
	
	@Column(name = "SUBJECT")
	private String subject;
	
	@Column(name = "MESSAGE_BODY")
	private String messageBody;
	
	@Column(name = "IS_ATTACHMENT")
	private Boolean isAttachment;
	
	@Column(name = "ATTACHMENT_ID")
	private String attachmentId;
	
	@Column(name = "IS_TABLE_CONTENT")
	private Boolean isTableContent;
	
	@Column(name = "TABLE_CONTENT_ID")
	private String tableContentId;
	
	@Column(name = "FRAME_ID")
	private String frameId;

	@Override
	public String toString() {
		return "EmailContentValuesDo [templateId=" + templateId + ", eventId=" + eventId + ", to=" + to + ", cc=" + cc
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

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
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
	public Object getPrimaryKey() {
		return templateId;
	}	
	
}
