package oneapp.incture.workbox.demo.chatbot.dto;


import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxResponseDto;

public class ChatBotActionRequest {

	@JsonProperty(required = false)
	private Map<String, List<EntityDto>> entities;
	private ChatBotUserDto userDto;
	private Map<String, EntityDto> memory;
	private WorkboxResponseDto workboxResponseDto;
	private String taskChoice;

	public Map<String, List<EntityDto>> getEntities() {
		return entities;
	}

	public void setEntities(Map<String, List<EntityDto>> entities) {
		this.entities = entities;
	}

	public ChatBotUserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(ChatBotUserDto userDto) {
		this.userDto = userDto;
	}

	public Map<String, EntityDto> getMemory() {
		return memory;
	}

	public void setMemory(Map<String, EntityDto> memory) {
		this.memory = memory;
	}

	public WorkboxResponseDto getWorkboxResponseDto() {
		return workboxResponseDto;
	}

	public void setWorkboxResponseDto(WorkboxResponseDto workboxResponseDto) {
		this.workboxResponseDto = workboxResponseDto;
	}

	public String getTaskChoice() {
		return taskChoice;
	}

	public void setTaskChoice(String taskChoice) {
		this.taskChoice = taskChoice;
	}

	@Override
	public String toString() {
		return "ChatBotActionRequest [entities=" + entities + ", userDto=" + userDto + ", memory=" + memory
				+ ", workboxResponseDto=" + workboxResponseDto + ", taskChoice=" + taskChoice + "]";
	}

}
