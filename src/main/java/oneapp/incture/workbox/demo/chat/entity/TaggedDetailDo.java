package oneapp.incture.workbox.demo.chat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "TAGGED_DETAIL")
public class TaggedDetailDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TaggedDetailDoPk taggedDetailDoPk;
	
	@Column(name = "CHAT_ID" , columnDefinition = "NVARCHAR(100)")
	private String chatId;

	public TaggedDetailDoPk getTaggedDetailDoPk() {
		return taggedDetailDoPk;
	}

	public void setTaggedDetailDoPk(TaggedDetailDoPk taggedDetailDoPk) {
		this.taggedDetailDoPk = taggedDetailDoPk;
	}

	public String getChatId() {
		return chatId;
	}

	public void setChatId(String chatId) {
		this.chatId = chatId;
	}

	@Override
	public String toString() {
		return "TaggedDetailDo [taggedDetailDoPk=" + taggedDetailDoPk + ", chatId=" + chatId + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return taggedDetailDoPk;
	}
}
