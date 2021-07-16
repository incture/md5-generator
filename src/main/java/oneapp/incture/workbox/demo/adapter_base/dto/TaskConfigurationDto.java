package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;

public class TaskConfigurationDto {
	
	
	private List<TaskTemplateTableDto> taskTemplateData=new ArrayList<>();

	
	
	public TaskConfigurationDto() {
		super();
	}

	public TaskConfigurationDto(List<TaskTemplateTableDto> taskTemplateData) {
		super();
		this.taskTemplateData = taskTemplateData;
	}

	public List<TaskTemplateTableDto> getTaskTemplateData() {
		return taskTemplateData;
	}

	public void setTaskTemplateData(List<TaskTemplateTableDto> taskTemplateData) {
		this.taskTemplateData = taskTemplateData;
	}

	@Override
	public String toString() {
		return "TaskConfigurationDto [taskTemplateData=" + taskTemplateData + "]";
	}

	
	
}
