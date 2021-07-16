package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class AttributesDetails {
	
	List<CustomAttributeKeys> customAttibutes;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "AttributesDetails [customAttibutes=" + customAttibutes + ", responseMessage=" + responseMessage + "]";
	}
	public List<CustomAttributeKeys> getCustomAttibutes() {
		return customAttibutes;
	}
	public void setCustomAttibutes(List<CustomAttributeKeys> customAttibutes) {
		this.customAttibutes = customAttibutes;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
