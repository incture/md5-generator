package oneapp.incture.workbox.demo.notification.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "NOTIFICATION_CONFIG")
public class NotificationConfigDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -338057976909649600L;

	@Id
	@Column(name = "USER_ID" , columnDefinition = "VARCHAR(255)")
	private String userId;
	
	@Column(name = "USER_NAME" , columnDefinition = "VARCHAR(255)")
	private String userName;
	
	@Id
	@Column(name = "ENABLE_CHANNEL" , columnDefinition = "VARCHAR(255)")
	private String enableChannel;
	
	@Id
	@Column(name = "ENABLE_ACTION" , columnDefinition = "VARCHAR(255)")
	private String enableAction;
	
	@Column(name = "SOURCE_EVENT", columnDefinition = "VARCHAR(100)")
	private String sourceEvent;

	public String getSourceEvent() {
		return sourceEvent;
	}

	public void setSourceEvent(String sourceEvent) {
		this.sourceEvent = sourceEvent;
	}

	@Override
	public String toString() {
		return "NotificationConfigDo [userId=" + userId + ", userName=" + userName + ", enableChannel=" + enableChannel
				+ ", enableAction=" + enableAction + ", sourceEvent=" + sourceEvent + "]";
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEnableChannel() {
		return enableChannel;
	}
	public void setEnableChannel(String enableChannel) {
		this.enableChannel = enableChannel;
	}
	public String getEnableAction() {
		return enableAction;
	}
	public void setEnableAction(String enableAction) {
		this.enableAction = enableAction;
	}
	
	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}	
	
}
