package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;

public class WorkboxMasterDto {

	public WorkboxMasterDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WorkboxMasterDto(List<ProcessEventsDto> processes, List<TaskEventsDto> tasks, List<TaskOwnersDto> owners,
			List<CustomAttributeValue> attrDtos) {
		super();
		this.processes = processes;
		this.tasks = tasks;
		this.owners = owners;
		this.attrDtos = attrDtos;
	}

	@Override
	public String toString() {
		return "WorkboxMasterDto [processes=" + processes + ", tasks=" + tasks + ", owners=" + owners + ", attrDtos="
				+ attrDtos + "]";
	}

	private List<ProcessEventsDto> processes;
	private List<TaskEventsDto> tasks;
	private List<TaskOwnersDto> owners;
	List<CustomAttributeValue> attrDtos;

	public List<CustomAttributeValue> getAttrDtos() {
		return attrDtos;
	}

	public void setAttrDtos(List<CustomAttributeValue> attrDtos) {
		this.attrDtos = attrDtos;
	}

	public List<ProcessEventsDto> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessEventsDto> processes) {
		this.processes = processes;
	}

	public List<TaskEventsDto> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskEventsDto> tasks) {
		this.tasks = tasks;
	}

	public List<TaskOwnersDto> getOwners() {
		return owners;
	}

	public void setOwners(List<TaskOwnersDto> owners) {
		this.owners = owners;
	}

}
