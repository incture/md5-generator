package oneapp.incture.workbox.demo.detailpage.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class CustomDetailDto {

	public CustomDetailDto() {
		super();
	}

	public CustomDetailDto(List<DynamicDetailDto> dynamicDetails, List<DynamicButtonDto> dynamicButtons,
			ResponseMessage responseMessage) {
		super();
		this.dynamicDetails = dynamicDetails;
		this.dynamicButtons = dynamicButtons;
		this.responseMessage = responseMessage;
	}

	private List<DynamicDetailDto> dynamicDetails;
	List<DynamicButtonDto> dynamicButtons;
	private String taskId;
	private String comment;
	private String userId;
	private String origin;
	public String getOrigin() {
		
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/* To be removed later */
	private String subject;
	
	private ResponseMessage responseMessage;

	public List<DynamicDetailDto> getDynamicDetails() {
		return dynamicDetails;
	}

	public void setDynamicDetails(List<DynamicDetailDto> dynamicDetails) {
		this.dynamicDetails = dynamicDetails;
	}

	public List<DynamicButtonDto> getDynamicButtons() {
		return dynamicButtons;
	}

	public void setDynamicButtons(List<DynamicButtonDto> dynamicButtons) {
		this.dynamicButtons = dynamicButtons;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "CustomDetailDto [dynamicDetails=" + dynamicDetails + ", dynamicButtons=" + dynamicButtons + ", taskId="
				+ taskId + ", comment=" + comment + ", userId=" + userId + ", origin=" + origin + ", subject=" + subject
				+ ", responseMessage=" + responseMessage + "]";
	}

	


}
