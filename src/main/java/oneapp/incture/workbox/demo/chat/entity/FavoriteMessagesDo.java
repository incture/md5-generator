package oneapp.incture.workbox.demo.chat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "FAVORITE_MESSAGES")
public class FavoriteMessagesDo implements BaseDo{

	@Column(name = "CHAT_ID" , columnDefinition = "NVARCHAR(100)")
	private String chatId;
	
	@Id
	@Column(name = "MESSAGE_ID" , columnDefinition = "NVARCHAR(100)")
	private String messageId;
	
	@Column(name = "USER_ID" , columnDefinition = "NVARCHAR(100)")
	private String userId;


	
	public String getChatId() {
		return chatId;
	}


	public void setChatId(String chatId) {
		this.chatId = chatId;
	}


	public String getMessageId() {
		return messageId;
	}


	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	@Override
	public String toString() {
		return "FavoriteMessagesDo [chatId=" + chatId + ", messageId=" + messageId + ", userId=" + userId + "]";
	}


	@Override
	public Object getPrimaryKey() {
		return null;
	}
}
