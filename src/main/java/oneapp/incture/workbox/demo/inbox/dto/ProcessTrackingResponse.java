package oneapp.incture.workbox.demo.inbox.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;

public class ProcessTrackingResponse {
private ProcessEventsDto process;
private List<TaskEventsDto> tasks;
private ResponseMessage message;
public ProcessEventsDto getProcess() {
	return process;
}
public void setProcess(ProcessEventsDto process) {
	this.process = process;
}
public List<TaskEventsDto> getTasks() {
	return tasks;
}
public void setTasks(List<TaskEventsDto> tasks) {
	this.tasks = tasks;
}
public ResponseMessage getMessage() {
	return message;
}
public void setMessage(ResponseMessage responseMessage) {
	this.message = responseMessage;
}
@Override
public String toString() {
	return "ProcessTrackingResponse [process=" + process + ", tasks=" + tasks + ", message=" + message + "]";
}




}
