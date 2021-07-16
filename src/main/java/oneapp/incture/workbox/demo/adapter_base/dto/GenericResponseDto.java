package oneapp.incture.workbox.demo.adapter_base.dto;

public class GenericResponseDto {

	private Object data;
	private String message;
	private String status;
	private String statusCode;
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	@Override
	public String toString() {
		return "GenericResponseDto [data=" + data + ", message=" + message + ", status=" + status + ", statusCode="
				+ statusCode + "]";
	}
	
}
