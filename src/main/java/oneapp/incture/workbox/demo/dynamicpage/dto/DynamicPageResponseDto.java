package oneapp.incture.workbox.demo.dynamicpage.dto;

import java.util.List;

public class DynamicPageResponseDto {

	private List<DynamicPageGroupResponseDto> groupList;
	private String message;
	private String status;
	private String statusCode;

	public List<DynamicPageGroupResponseDto> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<DynamicPageGroupResponseDto> groupList) {
		this.groupList = groupList;
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
		return "DynamicPageResponseDto [groupList=" + groupList + ", message=" + message + ", status=" + status
				+ ", statusCode=" + statusCode + "]";
	}
	
	
}
