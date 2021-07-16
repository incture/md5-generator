package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.List;

public class KeyValueResponseDto {

	List<KeyValueDto> keyValuePairs;
	ResponseMessage message;

	public List<KeyValueDto> getKeyValuePairs() {
		return keyValuePairs;
	}

	public void setKeyValuePairs(List<KeyValueDto> keyValuePairs) {
		this.keyValuePairs = keyValuePairs;
	}

	public ResponseMessage getMessage() {
		return message;
	}

	public void setMessage(ResponseMessage message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "KeyValueResponseDto [keyValuePairs=" + keyValuePairs + ", message=" + message + "]";
	}

}
