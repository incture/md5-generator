package oneapp.incture.workbox.demo.chat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "CHAT_MESSAGE_DETAILS")
public class ChatMessageDetailDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "MESSAGE_ID" , columnDefinition = "NVARCHAR(100)")
	private String messageId;
	
	@Column(name = "CHAT_ID" , columnDefinition = "NVARCHAR(100)")
	private String chatId;
	
	@Column(name = "MESSAGE" , columnDefinition = "NVARCHAR(1000)")
	private String message;
	
	@Column(name = "SENT_AT" , columnDefinition = "LONGDATE")
	private Date sentAt;
	
	@Column(name = "SENT_BY" , columnDefinition = "NVARCHAR(50)")
	private String sentBy;
	
	@Column(name = "ATTACHMENT_ID" , columnDefinition = "NVARCHAR(100)")
	private String attachmentId;
	
	@Column(name = "FAVORITE" , columnDefinition = "BOOLEAN")
	private Boolean favorite;

	@Override
	public String toString() {
		return "ChatMessageDetailDo [messageId=" + messageId + ", chatId=" + chatId + ", message=" + message
				+ ", sentAt=" + sentAt + ", sentBy=" + sentBy + ", attachmentId=" + attachmentId + ", favorite="
				+ favorite + "]";
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getSentAt() {
		return sentAt;
	}

	public void setSentAt(Date sentAt) {
		this.sentAt = sentAt;
	}

	public String getSentBy() {
		return sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	@Override
	public Object getPrimaryKey() {
		return messageId;
	}
	
	

	
}
