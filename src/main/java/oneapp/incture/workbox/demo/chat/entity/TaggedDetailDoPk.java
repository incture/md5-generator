package oneapp.incture.workbox.demo.chat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Embeddable
public class TaggedDetailDoPk implements BaseDo, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "MESSAGE_ID" ,columnDefinition = "NVARCHAR(100)")
	private String messageId;
	
	@Column(name = "USER_ID" , columnDefinition = "NVARCHAR(100)")
	private String userId;
	
	

	public TaggedDetailDoPk() {
		super();
	}

	public TaggedDetailDoPk(String messageId, String userId) {
		super();
		this.messageId = messageId;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "TaggedDetailDoPk [messageId=" + messageId + ", userId=" + userId + "]";
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
	public Object getPrimaryKey() {
		return null;
	}
	
	
}
