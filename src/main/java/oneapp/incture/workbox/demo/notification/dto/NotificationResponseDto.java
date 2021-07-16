package oneapp.incture.workbox.demo.notification.dto;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class NotificationResponseDto {

	private String notificationId;
	private String description;
	private String eventName;
	private String priority;
	private String userId;
	private String userName;
	private String origin;
	private String title;
	private String createdAt;
	private String id;	
	private String notificationType;
	private String timeDiff;
	
	
	
	public NotificationResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NotificationResponseDto(String notificationId, String description, String eventName, String priority,
			String userId, String title, String createdAt, String eventId, String notificationType, String timeDiff) {
		super();
		this.notificationId = notificationId;
		this.description = description;
		this.eventName = eventName;
		this.priority = priority;
		this.userId = userId;
		this.title = title;
		this.createdAt = createdAt;
		this.id = eventId;
		this.notificationType = notificationType;
		this.timeDiff = timeDiff;
	}
	public NotificationResponseDto(NotificationDto dto) {
		// TODO Auto-generated constructor stub
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		this.notificationId = dto.getNotificationId();
		this.description = dto.getDescription();
		this.eventName = dto.getEventName();
		this.priority = dto.getPriority();
		this.userId = dto.getUserId();
		this.title = dto.getTitle();
		//this.createdAt = simpleDateFormat1.format(ServicesUtil.resultAsDate(dto.getCreatedAt()));
		this.createdAt = simpleDateFormat1.format(dto.getCreatedAt());
		this.id = dto.getId();
		this.notificationType = dto.getNotificationType();
		this.origin = dto.getOrigin();
	}
	
	@Override
	public String toString() {
		return "NotificationResponseDto [notificationId=" + notificationId + ", description=" + description
				+ ", eventName=" + eventName + ", priority=" + priority + ", userId=" + userId + ", userName="
				+ userName + ", origin=" + origin + ", title=" + title + ", createdAt=" + createdAt + ", id=" + id
				+ ", notificationType=" + notificationType + ", timeDiff=" + timeDiff + "]";
	}
	public String getTimeDiff() {
		return timeDiff;
	}
	public void setTimeDiff(String timeDiff) {
		this.timeDiff = timeDiff;
	}
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
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
}
