package oneapp.incture.workbox.demo.chat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "CHAT_INFO_DETAIL")
public class ChatInfoDetailDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8415798797912495821L;

	@Id
	@Column(name = "CHAT_ID" , columnDefinition = "NVARCHAR(100)")
	private String chatId;
	
	@Column(name = "CHAT_TYPE" , columnDefinition = "NVARCHAR(20)")
	private String chatType;
	
	@Column(name = "CHAT_NAME" , columnDefinition = "NVARCHAR(100)")
	private String chatName;
	
	public String getChatId() {
		return chatId;
	}
	
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	public String getChatType() {
		return chatType;
	}

	public void setChatType(String chatType) {
		this.chatType = chatType;
	}

	public String getChatName() {
		return chatName;
	}
	
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

	@Override
	public String toString() {
		return "ChatInfoDetailDo [chatId=" + chatId + ", chatType=" + chatType + ", chatName=" + chatName + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return chatId;
	}

}
