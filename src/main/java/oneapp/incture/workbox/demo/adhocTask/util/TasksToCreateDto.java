package oneapp.incture.workbox.demo.adhocTask.util;

import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;

public class TasksToCreateDto {

	AttributesResponseDto tasksToSave ;
	AttributesResponseDto tasksToSubmit ;
	public AttributesResponseDto getTasksToSave() {
		return tasksToSave;
	}
	public void setTasksToSave(AttributesResponseDto tasksToSave) {
		this.tasksToSave = tasksToSave;
	}
	public AttributesResponseDto getTasksToSubmit() {
		return tasksToSubmit;
	}
	public void setTasksToSubmit(AttributesResponseDto tasksToSubmit) {
		this.tasksToSubmit = tasksToSubmit;
	}

}
