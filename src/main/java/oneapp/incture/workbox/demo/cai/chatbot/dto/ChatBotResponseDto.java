package oneapp.incture.workbox.demo.cai.chatbot.dto;

import java.util.List;

public class ChatBotResponseDto {

	List<ChatBotReplyDto> replies;
	ChatBotRequestCoversation conversation;

	public List<ChatBotReplyDto> getReplies() {
		return replies;
	}

	public void setReplies(List<ChatBotReplyDto> replies) {
		this.replies = replies;
	}

	public ChatBotRequestCoversation getConversation() {
		return conversation;
	}

	public void setConversation(ChatBotRequestCoversation conversation) {
		this.conversation = conversation;
	}

	@Override
	public String toString() {
		return "ChatBotResponseDto [replies=" + replies + ", conversation=" + conversation + "]";
	}

}
