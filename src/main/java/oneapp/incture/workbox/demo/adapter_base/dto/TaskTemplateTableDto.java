package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class TaskTemplateTableDto extends BaseDto {

	private String processName;

	private String taskName;

	private String origin;

	private String parentTaskName;

	private String templateId;

	private List<LayoutTemplateDto> layoutsData = new ArrayList<>();

	public TaskTemplateTableDto() {
		super();
	}

	public TaskTemplateTableDto(String processName, String taskName, String origin, String parentTaskName,
			String templateId, List<LayoutTemplateDto> layoutsData) {
		super();
		this.processName = processName;
		this.taskName = taskName;
		this.origin = origin;
		this.parentTaskName = parentTaskName;
		this.templateId = templateId;
		this.layoutsData = layoutsData;
	}

	public List<LayoutTemplateDto> getLayoutsData() {
		return layoutsData;
	}

	public void setLayoutsData(List<LayoutTemplateDto> layoutsData) {
		this.layoutsData = layoutsData;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getParentTaskName() {
		return parentTaskName;
	}

	public void setParentTaskName(String parentTaskName) {
		this.parentTaskName = parentTaskName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	@Override
	public String toString() {
		return "TaskTemplateTableDto [processName=" + processName + ", taskName=" + taskName + ", origin=" + origin
				+ ", parentTaskName=" + parentTaskName + ", templateId=" + templateId + ", layoutsData=" + layoutsData
				+ "]";
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

}
