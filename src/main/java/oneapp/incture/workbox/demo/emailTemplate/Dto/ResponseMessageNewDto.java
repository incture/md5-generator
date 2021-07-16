package oneapp.incture.workbox.demo.emailTemplate.Dto;

public class ResponseMessageNewDto {
	private String status;
	private String statusCode;
	private String message;
	
	public ResponseMessageNewDto(String status, String statusCode, String message) {
		this.status=status;
		this.statusCode=statusCode;
		this.message=message;
		// TODO Auto-generated constructor stub
	}

	public ResponseMessageNewDto() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ResponseMessageNewDto [status=" + status + ", statusCode=" + statusCode + ", message=" + message + "]";
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
