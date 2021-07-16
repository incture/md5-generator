package oneapp.incture.workbox.demo.emailTemplate.Dto;

public class CustomAttributeKeys {

	String key;
	String label;
	@Override
	public String toString() {
		return "CustomAttributeKeys [key=" + key + ", label=" + label + "]";
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
