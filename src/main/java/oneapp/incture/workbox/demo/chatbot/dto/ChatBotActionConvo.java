package oneapp.incture.workbox.demo.chatbot.dto;


public class ChatBotActionConvo {
	String language;
	ChatBotActionRequest memory;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ChatBotActionRequest getMemory() {
		return memory;
	}

	public void setMemory(ChatBotActionRequest memory) {
		this.memory = memory;
	}

	@Override
	public String toString() {
		return "ChatBotConversationDto [language=" + language + ", memory=" + memory + "]";
	}

}
