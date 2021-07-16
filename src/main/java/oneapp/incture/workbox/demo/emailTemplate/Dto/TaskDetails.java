package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class TaskDetails {

	List<TaskDetailDto> taskDetails;
	ResponseMessage responseMessage;
	@Override
	public String toString() {
		return "TaskDetails [taskDetails=" + taskDetails + ", responseMessage=" + responseMessage + "]";
	}
	public List<TaskDetailDto> getTaskDetails() {
		return taskDetails;
	}
	public void setTaskDetails(List<TaskDetailDto> taskDetails) {
		this.taskDetails = taskDetails;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
