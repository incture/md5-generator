package oneapp.incture.workbox.demo.cai.chatbot.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatbotDataMemory implements ChatbotMemory {

	private UserInfo userInfo;
	private List<ProcessConfigDto> processes;
	private List<TaskEventsDto> tasks;
	private Integer totalTasks;
	private String deviceType;
	private String inboxType;
	private String instanceId;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	private EntityDto processKey;
	private EntityDto taskKey;
	private EntityDto action;
	
	

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public EntityDto getAction() {
		return action;
	}

	public void setAction(EntityDto action) {
		this.action = action;
	}

	public Integer getTotalTasks() {
		return totalTasks;
	}

	public void setTotalTasks(Integer totalTasks) {
		this.totalTasks = totalTasks;
	}

	public List<ProcessConfigDto> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessConfigDto> processes) {
		this.processes = processes;
	}

	public List<TaskEventsDto> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskEventsDto> tasks) {
		this.tasks = tasks;
	}

	@Override
	public String toString() {
		return "ChatbotDataMemory [userInfo=" + userInfo + ", processes=" + processes + ", tasks=" + tasks
				+ ", totalTasks=" + totalTasks + ", deviceType=" + deviceType + ", inboxType=" + inboxType
				+ ", instanceId=" + instanceId + ", processKey=" + processKey + ", taskKey=" + taskKey + ", action="
				+ action + "]";
	}

	public EntityDto getProcessKey() {
		return processKey;
	}

	public void setProcessKey(EntityDto processKey) {
		this.processKey = processKey;
	}

	public EntityDto getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(EntityDto taskKey) {
		this.taskKey = taskKey;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getInboxType() {
		return inboxType;
	}

	public void setInboxType(String inboxType) {
		this.inboxType = inboxType;
	}

}
