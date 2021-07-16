package oneapp.incture.workbox.demo.adhocTask.dto;

public class ValueListDto {

	String id;
	String type;
	@Override
	public String toString() {
		return "ValueListDto [id=" + id + ", type=" + type + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ValueListDto(String id, String type) {
		super();
		this.id = id;
		this.type = type;
	}
	public ValueListDto() {
		super();
	}
	
}
