package oneapp.incture.workbox.demo.sapAriba.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;;

public class ParseResponse {

	private List<TaskEventsDto> tasks;
	private List<ProcessEventsDto> processes;
	private List<TaskOwnersDto> owners;
	private int resultCount;
	private List<CustomAttributeValue> customAttributeValues;
	@Override
	public String toString() {
		return "ParseResponse [tasks=" + tasks + ", processes=" + processes + ", owners=" + owners + ", resultCount="
				+ resultCount + ", customAttributeValues=" + customAttributeValues + "]";
	}
	public List<TaskEventsDto> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskEventsDto> tasks) {
		this.tasks = tasks;
	}
	public List<ProcessEventsDto> getProcesses() {
		return processes;
	}
	public void setProcesses(List<ProcessEventsDto> processes) {
		this.processes = processes;
	}
	public List<TaskOwnersDto> getOwners() {
		return owners;
	}
	public void setOwners(List<TaskOwnersDto> owners) {
		this.owners = owners;
	}
	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public List<CustomAttributeValue> getCustomAttributeValues() {
		return customAttributeValues;
	}
	public void setCustomAttributeValues(List<CustomAttributeValue> customAttributeValues) {
		this.customAttributeValues = customAttributeValues;
	}
	public ParseResponse(List<TaskEventsDto> tasks, List<ProcessEventsDto> processes, List<TaskOwnersDto> owners,
			int resultCount, List<CustomAttributeValue> customAttributeValues) {
		super();
		this.tasks = tasks;
		this.processes = processes;
		this.owners = owners;
		this.resultCount = resultCount;
		this.customAttributeValues = customAttributeValues;
	}
	

}
