package oneapp.incture.workbox.demo.notification.dto;

import java.util.List;

public class PushNotificationDto {
	private String alert;
	private String data;
	private String sound="default";
	private List<String> users;
	
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSound() {
		return sound;
	}
	public void setSound(String sound) {
		this.sound = sound;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	@Override
	public String toString() {
		return "PushNotificationDto [alert=" + alert + ", data=" + data + ", sound=" + sound + ", users=" + users + "]";
	}
	
}
