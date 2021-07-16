package oneapp.incture.workbox.demo.docusign.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;



public class TaskAndAuditDto {
	private List<TaskEventsDto> tasks;
	private List<TaskAuditDto> taskaudits;
	public List<TaskEventsDto> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskEventsDto> tasks) {
		this.tasks = tasks;
	}
	public List<TaskAuditDto> getTaskaudits() {
		return taskaudits;
	}
	public void setTaskaudits(List<TaskAuditDto> taskaudits) {
		this.taskaudits = taskaudits;
	}
	

}
