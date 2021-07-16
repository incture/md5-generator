package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

public class UserPushNotificationPayload {

	private MobileNotification notification;
	private List<String> users;
	
	public MobileNotification getNotification() {
		return notification;
	}
	public void setNotification(MobileNotification notification) {
		this.notification = notification;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	@Override
	public String toString() {
		return "UserPushNotificationPayload [notification=" + notification + ", users=" + users + "]";
	}
	
	
}
