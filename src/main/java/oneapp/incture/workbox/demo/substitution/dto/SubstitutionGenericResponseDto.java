package oneapp.incture.workbox.demo.substitution.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class SubstitutionGenericResponseDto {

	private Object data;
	private ResponseMessage message;
	private int listCount;
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public ResponseMessage getMessage() {
		return message;
	}
	public void setMessage(ResponseMessage message) {
		this.message = message;
	}
	public int getListCount() {
		return listCount;
	}
	public void setListCount(int listCount) {
		this.listCount = listCount;
	}
	@Override
	public String toString() {
		return "SubstitutionGenericResponseDto [data=" + data + ", message=" + message + ", listCount=" + listCount
				+ "]";
	}
	
	
}

