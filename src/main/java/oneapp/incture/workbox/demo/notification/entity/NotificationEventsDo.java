package oneapp.incture.workbox.demo.notification.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "NOTIFICATION_EVENTS")
public class NotificationEventsDo implements BaseDo,Serializable{

	private static final long serialVersionUID = -338057976909649600L;

	@Column(name = "EVENT_GROUP" , columnDefinition = "VARCHAR(255)")
	private String eventGroup;
	
	@Column(name = "EVENT_NAME" , columnDefinition = "VARCHAR(255)")
	private String eventName;
	
	@Id
	@Column(name = "EVENT_ID" , columnDefinition = "VARCHAR(255)")
	private String eventId;
	
	@Column(name = "PRIORITY" , columnDefinition = "VARCHAR(255)")
	private String priority;
	
	@Column(name = "DEFAULT")
	private Boolean isDefault;
	
	@Id
	@Column(name = "ID", columnDefinition = "VARCHAR(255)")
	private String id;
	
	@Column(name = "ADMIN_USER", columnDefinition = "VARCHAR(255)")
	private String adminUser;
	
	@Column(name = "CATEGORY", columnDefinition = "VARCHAR(255)")
	private String category;
	
	public String getEventGroup() {
		return eventGroup;
	}

	public void setEventGroup(String eventGroup) {
		this.eventGroup = eventGroup;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(String adminUser) {
		this.adminUser = adminUser;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "NotificationEventsDo [eventGroup=" + eventGroup + ", eventName=" + eventName + ", eventId=" + eventId
				+ ", priority=" + priority + ", isDefault=" + isDefault + ", id=" + id + ", adminUser=" + adminUser
				+ ", category=" + category + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}	
	
}
