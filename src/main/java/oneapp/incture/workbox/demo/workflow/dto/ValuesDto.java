package oneapp.incture.workbox.demo.workflow.dto;

public class ValuesDto {

	String value;
	String valueName;
	Integer isEdited;
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	@Override
	public String toString() {
		return "ValuesDto [value=" + value + ", isEdited=" + isEdited + "]";
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getIsEdited() {
		return isEdited;
	}
	public void setIsEdited(Integer isEdited) {
		this.isEdited = isEdited;
	}
	
	
	
	
	
}
