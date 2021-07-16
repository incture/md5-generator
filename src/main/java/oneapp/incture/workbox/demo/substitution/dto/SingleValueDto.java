package oneapp.incture.workbox.demo.substitution.dto;

public class SingleValueDto {

	public SingleValueDto() {
	};

	public SingleValueDto(String value) {
		this.value = value;
	};

	@Override
	public String toString() {
		return "SingleValueDto [value=" + value + "]";
	}

	String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
