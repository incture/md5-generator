package oneapp.incture.workbox.demo.chat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Embeddable
public class ChatUsersDoPk implements BaseDo, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2715612375205743698L;

	@Column(name = "CHAT_ID" , columnDefinition = "NVARCHAR(100)")
	private String chatId;
	
	@Column(name = "USER_ID" , columnDefinition = "NVARCHAR(100)")
	private String userId;

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	@Override
	public String toString() {
		return "ChatUsersDoPk [userId=" + userId + ", chatId=" + chatId + "]";
	}

	public ChatUsersDoPk() {
		super();
	}

	public ChatUsersDoPk(String userId, String chatId) {
		super();
		this.userId = userId;
		this.chatId = chatId;
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}
}
