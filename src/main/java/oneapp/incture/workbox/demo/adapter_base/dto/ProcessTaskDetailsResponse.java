package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.List;

public class ProcessTaskDetailsResponse {
;
	private String processName;
	private String origin;
	private List<TaskDetails> tasks;
	
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public List<TaskDetails> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskDetails> tasks) {
		this.tasks = tasks;
	}
	@Override
	public String toString() {
		return "ProcessTaskDetailsResponse [processName=" + processName + ", origin=" + origin + ", tasks=" + tasks
				+ "]";
	}
	
	
}
