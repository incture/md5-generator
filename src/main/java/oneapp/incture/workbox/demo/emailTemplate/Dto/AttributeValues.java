package oneapp.incture.workbox.demo.emailTemplate.Dto;

public class AttributeValues {

	String attributeValue;
	String dataType;
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public String toString() {
		return "AttributeValues [attributeValue=" + attributeValue + ", dataType=" + dataType + "]";
	}
		
	
}
