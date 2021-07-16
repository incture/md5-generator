package oneapp.incture.workbox.demo.sharepoint.util;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;

public class TaskAndOwnerDto {

	
    private	List<TaskEventsDto> tasks;
	private List<TaskOwnersDto> owners;
	List<CustomAttributeValue> customAttributeValues ;
	
	public List<CustomAttributeValue> getCustomAttributeValues() {
		return customAttributeValues;
	}
	public void setCustomAttributeValues(List<CustomAttributeValue> customAttributeValues) {
		this.customAttributeValues = customAttributeValues;
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
	@Override
	public String toString() {
		return "TaskAndOwnerDto [tasks=" + tasks + ", owners=" + owners + "]";
	}
	
	
}
