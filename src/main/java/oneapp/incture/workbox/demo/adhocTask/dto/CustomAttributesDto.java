package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;

public class CustomAttributesDto {

	List<CustomAttributeValue> customAttributes;
	String eventId;
	@Override
	public String toString() {
		return "CustomAttributesDto [customAttributes=" + customAttributes + ", eventId=" + eventId + "]";
	}
	public List<CustomAttributeValue> getCustomAttributes() {
		return customAttributes;
	}
	public void setCustomAttributes(List<CustomAttributeValue> customAttributes) {
		this.customAttributes = customAttributes;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
}
