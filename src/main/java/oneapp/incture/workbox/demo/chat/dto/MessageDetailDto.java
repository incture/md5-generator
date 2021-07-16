package oneapp.incture.workbox.demo.chat.dto;

import java.util.List;

public class MessageDetailDto {
	
	private String chatId;
	private String sentBy;
	private String sentByName;
	private String message;
	private String messageId;
	private String sentAt;
	private Boolean favorite;
	private List<AttachmentDetail> attachments;
	private List<String> taggedIds;
	private String chatName;
	private String chatType;
	private List<String> users;
	
	
	public String getSentByName() {
		return sentByName;
	}
	public void setSentByName(String sentByName) {
		this.sentByName = sentByName;
	}
	public Boolean getFavorite() {
		return favorite;
	}
	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}
	@Override
	public String toString() {
		return "MessageDetailDto [chatId=" + chatId + ", sentBy=" + sentBy + ", sentByName=" + sentByName + ", message="
				+ message + ", messageId=" + messageId + ", sentAt=" + sentAt + ", favorite=" + favorite
				+ ", attachments=" + attachments + ", taggedIds=" + taggedIds + ", chatName=" + chatName + ", chatType="
				+ chatType + ", users=" + users + "]";
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	public String getChatType() {
		return chatType;
	}
	public void setChatType(String chatType) {
		this.chatType = chatType;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getSentBy() {
		return sentBy;
	}
	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getSentAt() {
		return sentAt;
	}
	public void setSentAt(String sentAt) {
		this.sentAt = sentAt;
	}
	public List<AttachmentDetail> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentDetail> attachments) {
		this.attachments = attachments;
	}
	public List<String> getTaggedIds() {
		return taggedIds;
	}
	public void setTaggedIds(List<String> taggedIds) {
		this.taggedIds = taggedIds;
	}
	
	
	
	
}
