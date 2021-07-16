package oneapp.incture.workbox.demo.workflow.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class DropDownRequestDto {

	String customKey;
	List<ValuesDto> values;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "DropDownRequestDto [customKey=" + customKey + ", values=" + values + ", responseMessage="
				+ responseMessage + "]";
	}
	public String getCustomKey() {
		return customKey;
	}
	public void setCustomKey(String customKey) {
		this.customKey = customKey;
	}
	public List<ValuesDto> getValues() {
		return values;
	}
	public void setValues(List<ValuesDto> values) {
		this.values = values;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
