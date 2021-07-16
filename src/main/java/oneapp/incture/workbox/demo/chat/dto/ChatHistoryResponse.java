package oneapp.incture.workbox.demo.chat.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;

public class ChatHistoryResponse {

	ChatMessages chatMessages;
	Integer totalChatCount;
	Integer pageCount;
	Integer currentPage;
	List<ChatCountDto> unseenCount;
	String currentChatId;
	String chatName;
	String currentUserId;
	ResponseMessage responseMessage;
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	@Override
	public String toString() {
		return "ChatHistoryResponse [chatMessages=" + chatMessages + ", totalChatCount=" + totalChatCount
				+ ", pageCount=" + pageCount + ", currentPage=" + currentPage + ", unseenCount=" + unseenCount
				+ ", currentChatId=" + currentChatId + ", chatName=" + chatName + ", currentUserId=" + currentUserId
				+ ", responseMessage=" + responseMessage + "]";
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getTotalChatCount() {
		return totalChatCount;
	}
	public void setTotalChatCount(Integer totalChatCount) {
		this.totalChatCount = totalChatCount;
	}
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	public String getCurrentUserId() {
		return currentUserId;
	}
	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}
	public String getCurrentChatId() {
		return currentChatId;
	}
	public void setCurrentChatId(String currentChatId) {
		this.currentChatId = currentChatId;
	}
	public ChatMessages getChatMessages() {
		return chatMessages;
	}
	public void setChatMessages(ChatMessages chatMessages) {
		this.chatMessages = chatMessages;
	}
	public List<ChatCountDto> getUnseenCount() {
		return unseenCount;
	}
	public void setUnseenCount(List<ChatCountDto> unseenCount) {
		this.unseenCount = unseenCount;
	}
	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	
	
}
