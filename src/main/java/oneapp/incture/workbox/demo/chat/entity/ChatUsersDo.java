package oneapp.incture.workbox.demo.chat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "CHAT_USER")
public class ChatUsersDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ChatUsersDoPk chatUsersDoPk;
	
	@Column(name = "LAST_SEEN_TIME" , columnDefinition = "TIMESTAMP")
	private Date lastSeenTime;
	
	@Column(name = "USER_NAME" , columnDefinition = "NVARCHAR(100)")
	private String userName;
	
	@Column(name = "PINNED" , columnDefinition = "BOOLEAN")
	private Boolean pinned;
	
	@Column(name = "UNSEEN_COUNT" , columnDefinition = "INTEGER")
	private Integer unseenCount;
	
	@Column(name = "CHAT_OPEN_STATUS" , columnDefinition = "BOOLEAN")
	private Boolean chatOpenStatus;
	
	public Boolean getChatOpenStatus() {
		return chatOpenStatus;
	}

	public void setChatOpenStatus(Boolean chatOpenStatus) {
		this.chatOpenStatus = chatOpenStatus;
	}

	public ChatUsersDoPk getChatUsersDoPk() {
		return chatUsersDoPk;
	}

	public void setChatUsersDoPk(ChatUsersDoPk chatUsersDoPk) {
		this.chatUsersDoPk = chatUsersDoPk;
	}

	public Date getLastSeenTime() {
		return lastSeenTime;
	}

	public void setLastSeenTime(Date lastSeenTime) {
		this.lastSeenTime = lastSeenTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getPinned() {
		return pinned;
	}

	public void setPinned(Boolean pinned) {
		this.pinned = pinned;
	}

	public Integer getUnseenCount() {
		return unseenCount;
	}

	public void setUnseenCount(Integer unseenCount) {
		this.unseenCount = unseenCount;
	}

	@Override
	public String toString() {
		return "ChatUsersDo [chatUsersDoPk=" + chatUsersDoPk + ", lastSeenTime=" + lastSeenTime + ", userName="
				+ userName + ", pinned=" + pinned + ", unseenCount=" + unseenCount + ", chatOpenStatus="
				+ chatOpenStatus + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return chatUsersDoPk;
	}

}
