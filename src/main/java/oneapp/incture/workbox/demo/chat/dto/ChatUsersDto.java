package oneapp.incture.workbox.demo.chat.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class ChatUsersDto extends BaseDto{

	private String userId;
	private String chatId;
	private Date lastSeenTime;
	private String userName;
	private Boolean pinned;
	private Integer unseenCount;
	private Boolean chatOpenStatus;
	@Override
	public String toString() {
		return "ChatUsersDto [userId=" + userId + ", chatId=" + chatId + ", lastSeenTime=" + lastSeenTime
				+ ", userName=" + userName + ", pinned=" + pinned + ", unseenCount=" + unseenCount + ", chatOpenStatus="
				+ chatOpenStatus + "]";
	}
	public Boolean getChatOpenStatus() {
		return chatOpenStatus;
	}
	public void setChatOpenStatus(Boolean chatOpenStatus) {
		this.chatOpenStatus = chatOpenStatus;
	}
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
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}
	
	
}
