package oneapp.incture.workbox.demo.cai.chatbot.dto;

public class ChatBotReplyDto {
	String type;
	String content;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ChatBotReplyDto [type=" + type + ", content=" + content + "]";
	}

}
