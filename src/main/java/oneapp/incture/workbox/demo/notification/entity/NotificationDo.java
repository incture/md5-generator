package oneapp.incture.workbox.demo.notification.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "NOTIFICATIONS")
public class NotificationDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1615180715258197832L;

	@Column(name = "NOTIFICATION_ID", columnDefinition = "NVARCHAR(100)")
	private String notificationId;
	
	@Column(name = "DESCRIPTION", columnDefinition = "NVARCHAR(1000)")
	private String description;
	
	@Column(name = "EVENT_NAME", columnDefinition = "NVARCHAR(255)")
	private String eventName;
	
	@Column(name = "PRIORITY", columnDefinition = "NVARCHAR(20)")
	private String priority;
	
	@Id
	@Column(name = "USER_ID", columnDefinition = "NVARCHAR(20)")
	private String userId;
	
	@Column(name = "USER_NAME", columnDefinition = "NVARCHAR(255)")
	private String userName;

	@Column(name = "TITLE", columnDefinition = "NVARCHAR(255)")
	private String title;

	@Column(name = "CREATED_AT", columnDefinition = "TIMESTAMP")
	private Date createdAt;
	
	@Id
	@Column(name = "EVENT_ID", columnDefinition = "NVARCHAR(100)")
	private String eventId;	

	@Id
	@Column(name = "TYPE", columnDefinition = "NVARCHAR(255)")
	private String notificationType;
	
	
	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	@Override
	public String toString() {
		return "NotificationDo [notificationId=" + notificationId + ", description=" + description + ", eventName="
				+ eventName + ", priority=" + priority + ", userId=" + userId + ", userName=" + userName + ", title="
				+ title + ", createdAt=" + createdAt + ", eventId=" + eventId + ", notificationType=" + notificationType
				+ "]";
	}

	@Override
	public Object getPrimaryKey() {
		return notificationId;
	}
}
