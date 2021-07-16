package oneapp.incture.workbox.demo.cai.chatbot.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;

public class ChatbotTasksDto {

	List<TaskEventsDto> tasks;
	Integer count;

	public List<TaskEventsDto> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskEventsDto> tasks) {
		this.tasks = tasks;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "ChatbotTasksDto [tasks=" + tasks + ", count=" + count + "]";
	}

}
