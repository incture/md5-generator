package oneapp.incture.workbox.demo.chatbot.dto;


public class ChatBotConversationDto {
	String language;
	ChatBotRequestDto memory;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	

	public ChatBotRequestDto getMemory() {
		return memory;
	}

	public void setMemory(ChatBotRequestDto memory) {
		this.memory = memory;
	}

	@Override
	public String toString() {
		return "ChatBotConversationDto [language=" + language + ", memory=" + memory + "]";
	}

}
