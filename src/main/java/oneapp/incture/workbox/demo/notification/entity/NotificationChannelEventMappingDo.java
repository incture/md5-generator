package oneapp.incture.workbox.demo.notification.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "EVENT_CHANNEL_MAPPING")
public class NotificationChannelEventMappingDo implements BaseDo, Serializable {

	private static final long serialVersionUID = -338057976909649600L;

	@Id
	@Column(name = "CHANNEL", columnDefinition = "VARCHAR(255)")
	private String channel;

	@Id
	@Column(name = "EVENT_ID", columnDefinition = "VARCHAR(255)")
	private String eventId;

	@Column(name = "DEFAULT")
	private Boolean isDefault;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public String toString() {
		return "NotificationChannelEventMappingDo [channel=" + channel + ", eventId=" + eventId + ", isDefault="
				+ isDefault + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
