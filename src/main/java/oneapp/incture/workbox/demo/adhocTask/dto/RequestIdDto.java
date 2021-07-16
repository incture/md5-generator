package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.Arrays;

public class RequestIdDto {

	String requestId;
	Integer id;
	Object[] requestDetail;
	
	public Object[] getRequestDetail() {
		return requestDetail;
	}
	public void setRequestDetail(Object[] requestDetail) {
		this.requestDetail = requestDetail;
	}
	@Override
	public String toString() {
		return "RequestIdDto [requestId=" + requestId + ", id=" + id + ", requestDetail="
				+ Arrays.toString(requestDetail) + "]";
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
