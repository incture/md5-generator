package oneapp.incture.workbox.demo.notification.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "USER_NOTIFICATION_CONFIG")
public class NotificationUserConfigDo implements BaseDo, Serializable {

	private static final long serialVersionUID = -338057976909649600L;

	@Id
	@Column(name = "ID", columnDefinition = "VARCHAR(255)")
	private String id;

	@Id
	@Column(name = "CHANNEL", columnDefinition = "VARCHAR(255)")
	private String channel;

	@Id
	@Column(name = "EVENT_ID", columnDefinition = "VARCHAR(255)")
	private String eventId;

	@Column(name = "TYPE", columnDefinition = "VARCHAR(255)")
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "NotificationUserConfigDo [id=" + id + ", channel=" + channel + ", eventId=" + eventId + ", type=" + type
				+ "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
