package oneapp.incture.workbox.demo.adapterJira.dto;

public class AttributeGenericDto {
	private String attributeId;
	private String attributeValue;
	
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	@Override
	public String toString() {
		return "AttributeGenericDto [attributeId=" + attributeId + ", attributeValue=" + attributeValue + "]";
	}
}
