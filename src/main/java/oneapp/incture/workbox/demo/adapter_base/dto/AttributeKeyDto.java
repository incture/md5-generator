package oneapp.incture.workbox.demo.adapter_base.dto;

public class AttributeKeyDto {
	
	String key;
	String processName;
	String attributeType;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	@Override
	public String toString() {
		return "AttributeKeyDto [key=" + key + ", processName=" + processName + ", attributeType=" + attributeType
				+ "]";
	}
	
	

}
