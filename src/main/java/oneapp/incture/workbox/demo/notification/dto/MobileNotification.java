package oneapp.incture.workbox.demo.notification.dto;

public class MobileNotification {

	private String alert;
	private String data;
	private String sound="default";
	
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
	@Override
	public String toString() {
		return "MobileNotification [alert=" + alert + ", data=" + data + ", sound=" + sound + "]";
	}
	
	
}
