package oneapp.incture.workbox.demo.adhocTask.dto;

public class CrossConstantDto {

	String id;
	String value;
	String name;
	@Override
	public String toString() {
		return "CrossConstantDto [id=" + id + ", value=" + value + ", name=" + name + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
