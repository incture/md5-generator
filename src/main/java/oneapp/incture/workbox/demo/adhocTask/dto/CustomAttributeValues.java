package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;
import java.util.Map;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;

public class CustomAttributeValues {

	List<CustomAttributeValue> customAttributeValues;
	Map<String,String> attrKeyValues;
	@Override
	public String toString() {
		return "CustomAttributeValues [customAttributeValues=" + customAttributeValues + "]";
	}
	public List<CustomAttributeValue> getCustomAttributeValues() {
		return customAttributeValues;
	}
	public void setCustomAttributeValues(List<CustomAttributeValue> customAttributeValues) {
		this.customAttributeValues = customAttributeValues;
	}
	public Map<String, String> getAttrKeyValues() {
		return attrKeyValues;
	}
	public void setAttrKeyValues(Map<String, String> attrKeyValues) {
		this.attrKeyValues = attrKeyValues;
	}
	
	
	
}
