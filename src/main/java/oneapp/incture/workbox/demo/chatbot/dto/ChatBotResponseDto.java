package oneapp.incture.workbox.demo.chatbot.dto;


import java.util.List;

public class ChatBotResponseDto {

	List<ChatBotReplyDto> replies;
	ChatBotActionConvo conversation;

	public List<ChatBotReplyDto> getReplies() {
		return replies;
	}

	public void setReplies(List<ChatBotReplyDto> replies) {
		this.replies = replies;
	}

	public ChatBotActionConvo getConversation() {
		return conversation;
	}

	public void setConversation(ChatBotActionConvo conversation) {
		this.conversation = conversation;
	}

	@Override
	public String toString() {
		return "ChatBotResponseDto [replies=" + replies +"]";
	}

}
