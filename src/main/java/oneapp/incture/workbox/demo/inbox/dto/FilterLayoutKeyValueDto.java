package oneapp.incture.workbox.demo.inbox.dto;

public class FilterLayoutKeyValueDto {

	private String key;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "FilterLayoutKeyValueDto [key=" + key + ", value=" + value + "]";
	}

}
