package oneapp.incture.workbox.demo.adapter_base.mapping;

public class Attribute {

	public Attribute() {
		super();
	}

	public Attribute(String id, String label, String type, String value) {
		super();
		this.id = id;
		this.label = label;
		this.type = type;
		this.value = value;
	}

	private String id;
	private String label;
	private String type;
	private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Attribute [id=" + id + ", label=" + label + ", type=" + type + ", value=" + value + "]";
	}

}
