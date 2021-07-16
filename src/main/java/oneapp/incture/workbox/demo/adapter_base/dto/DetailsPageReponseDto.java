package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;

public class DetailsPageReponseDto {

	private String taskId;
	private String processName;
	private List<DetailPageDto> layouts=new ArrayList<DetailPageDto>();
	
	public DetailsPageReponseDto() {
		super();
	}
	public DetailsPageReponseDto(String taskId, String processName, List<DetailPageDto> layouts) {
		super();
		this.taskId = taskId;
		this.processName = processName;
		this.layouts = layouts;
	}
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public List<DetailPageDto> getLayouts() {
		return layouts;
	}
	public void setLayouts(List<DetailPageDto> layouts) {
		this.layouts = layouts;
	}
	
	
	
	
}
