package oneapp.incture.workbox.demo.ecc.eccadapter;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;

public class TaskDetailsListDto {

	private List<ProcessEventsDto> processEventsDto;
	private List<TaskEventsDto> taskEvensDto;
	private List<TaskOwnersDto> taskOwnersDto;
	private List<CustomAttributeValueDto> customAttributeValueDto;
	private List<String> substitutedIds;
	
	
	public List<ProcessEventsDto> getProcessEventsDto() {
		return processEventsDto;
	}
	public void setProcessEventsDto(List<ProcessEventsDto> processEventsDto) {
		this.processEventsDto = processEventsDto;
	}
	public List<String> getSubstitutedIds() {
		return substitutedIds;
	}
	public void setSubstitutedIds(List<String> substitutedIds) {
		this.substitutedIds = substitutedIds;
	}
	public List<TaskEventsDto> getTaskEvensDto() {
		return taskEvensDto;
	}
	public void setTaskEvensDto(List<TaskEventsDto> taskEvensDto) {
		this.taskEvensDto = taskEvensDto;
	}
	public List<TaskOwnersDto> getTaskOwnersDto() {
		return taskOwnersDto;
	}
	public void setTaskOwnersDto(List<TaskOwnersDto> taskOwnersDto) {
		this.taskOwnersDto = taskOwnersDto;
	}
	public List<CustomAttributeValueDto> getCustomAttributeValueDto() {
		return customAttributeValueDto;
	}
	public void setCustomAttributeValueDto(List<CustomAttributeValueDto> customAttributeValueDto) {
		this.customAttributeValueDto = customAttributeValueDto;
	}
	@Override
	public String toString() {
		return "TaskDetailsListDto [processEventsDto=" + processEventsDto + ", taskEvensDto=" + taskEvensDto
				+ ", taskOwnersDto=" + taskOwnersDto + ", customAttributeValueDto=" + customAttributeValueDto
				+ ", substitutedIds=" + substitutedIds + "]";
	}
	
	
	
}
