package oneapp.incture.workbox.demo.adapter_base.dto;

public class TaskAttributes {

	private String id;
	private String label;
	private String type;
	
	public TaskAttributes() {
		super();
	}
	public TaskAttributes(String id, String label, String type) {
		super();
		this.id = id;
		this.label = label;
		this.type = type;
	}
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
	@Override
	public String toString() {
		return "TaskAttributes [id=" + id + ", label=" + label + ", type=" + type + "]";
	}
	
	
}
