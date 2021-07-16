package oneapp.incture.workbox.demo.cai.chatbot.dto;

public class ChatBotRequestCoversation {
	private String language;
	private ChatbotMemory memory;
	private Boolean merge_memory = true;

	public Boolean getMerge_memory() {
		return merge_memory;
	}

	public void setMerge_memory(Boolean merge_memory) {
		this.merge_memory = merge_memory;
	}

	public ChatBotRequestCoversation() {
		super();
	}

	public ChatBotRequestCoversation(String language, ChatbotMemory memory, Boolean merge_memory) {
		super();
		this.language = language;
		this.memory = memory;
		this.merge_memory = merge_memory;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public ChatbotMemory getMemory() {
		return memory;
	}

	public void setMemory(ChatbotMemory memory) {
		this.memory = memory;
	}

	@Override
	public String toString() {
		return "ChatBotRequestCoversation [language=" + language + ", memory=" + memory + ", merge_memory="
				+ merge_memory + "]";
	}

}
