package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;

public class TaskDetailDto {

	List<TaskEventsDto> taskEventsDtos;
	List<TaskOwnersDto> taskOwnersDtos;
	//storing the eventId to change the status for sequential flow
	
	@Override
	public String toString() {
		return "TaskDetailDto [taskEventsDtos=" + taskEventsDtos + ", taskOwnersDtos=" + taskOwnersDtos +  "]";
	}
	
	public List<TaskEventsDto> getTaskEventsDtos() {
		return taskEventsDtos;
	}
	public void setTaskEventsDtos(List<TaskEventsDto> taskEventsDtos) {
		this.taskEventsDtos = taskEventsDtos;
	}
	public List<TaskOwnersDto> getTaskOwnersDtos() {
		return taskOwnersDtos;
	}
	public void setTaskOwnersDtos(List<TaskOwnersDto> taskOwnersDtos) {
		this.taskOwnersDtos = taskOwnersDtos;
	}
}
