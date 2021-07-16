package oneapp.incture.workbox.demo.chatbot.dto;


public class EntityDto {

	public String getScalar() {
		return scalar;
	}

	public void setScalar(String scalar) {
		this.scalar = scalar;
	}

	private String scalar;

	private String confidence;

	private String raw;

	private String value;

	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "EntityDto [scalar=" + scalar + ", confidence=" + confidence + ", raw=" + raw + ", value=" + value + "]";
	}

	

}
