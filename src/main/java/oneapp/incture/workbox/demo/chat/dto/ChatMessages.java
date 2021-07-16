package oneapp.incture.workbox.demo.chat.dto;

import java.util.List;

public class ChatMessages {

	List<MessageDetailDto> messages;
	List<Members> members;
	@Override
	public String toString() {
		return "ChatMessages [messages=" + messages + ", members=" + members + "]";
	}
	public List<MessageDetailDto> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageDetailDto> messages) {
		this.messages = messages;
	}
	public List<Members> getMembers() {
		return members;
	}
	public void setMembers(List<Members> members) {
		this.members = members;
	}
	
	
	
}
