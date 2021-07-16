package oneapp.incture.workbox.demo.chat.dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.chat.dao.AttachmentsDto;

public class MessageDetailResponse {

	List<ChatInfoDetailDto> chatInfoDetails;
	List<AttachmentDetail> attachmentDetails;
	ChatMessageDetailDto chatMessageDetailDto;
	List<TaggedDetailDto> taggedDetailDtos;
	List<ChatUsersDto> chatUsersDtos;
	List<AttachmentsDto> attachmentsDtos;
	String currentChatId;
	List<String> members;
	ResponseMessage ResponseMessage;
	ChatHistoryResponse chatHistoryResponse;
	
	public ChatHistoryResponse getChatHistoryResponse() {
		return chatHistoryResponse;
	}
	public void setChatHistoryResponse(ChatHistoryResponse chatHistoryResponse) {
		this.chatHistoryResponse = chatHistoryResponse;
	}
	public List<String> getMembers() {
		return members;
	}
	public void setMembers(List<String> members) {
		this.members = members;
	}
	public String getCurrentChatId() {
		return currentChatId;
	}
	public void setCurrentChatId(String currentChatId) {
		this.currentChatId = currentChatId;
	}
	public List<AttachmentsDto> getAttachmentsDtos() {
		return attachmentsDtos;
	}
	public void setAttachmentsDtos(List<AttachmentsDto> attachmentsDtos) {
		this.attachmentsDtos = attachmentsDtos;
	}
	@Override
	public String toString() {
		return "MessageDetailResponse [chatInfoDetails=" + chatInfoDetails + ", attachmentDetails=" + attachmentDetails
				+ ", chatMessageDetailDto=" + chatMessageDetailDto + ", taggedDetailDtos=" + taggedDetailDtos
				+ ", chatUsersDtos=" + chatUsersDtos + ", attachmentsDtos=" + attachmentsDtos + ", currentChatId="
				+ currentChatId + ", members=" + members + ", ResponseMessage=" + ResponseMessage
				+ ", chatHistoryResponse=" + chatHistoryResponse + "]";
	}
	public List<ChatInfoDetailDto> getChatInfoDetails() {
		return chatInfoDetails;
	}
	public void setChatInfoDetails(List<ChatInfoDetailDto> chatInfoDetails) {
		this.chatInfoDetails = chatInfoDetails;
	}
	public List<AttachmentDetail> getAttachmentDetails() {
		return attachmentDetails;
	}
	public void setAttachmentDetails(List<AttachmentDetail> attachmentDetails) {
		this.attachmentDetails = attachmentDetails;
	}
	public ChatMessageDetailDto getChatMessageDetailDto() {
		return chatMessageDetailDto;
	}
	public void setChatMessageDetailDto(ChatMessageDetailDto chatMessageDetailDto) {
		this.chatMessageDetailDto = chatMessageDetailDto;
	}
	public List<TaggedDetailDto> getTaggedDetailDtos() {
		return taggedDetailDtos;
	}
	public void setTaggedDetailDtos(List<TaggedDetailDto> taggedDetailDtos) {
		this.taggedDetailDtos = taggedDetailDtos;
	}
	public List<ChatUsersDto> getChatUsersDtos() {
		return chatUsersDtos;
	}
	public void setChatUsersDtos(List<ChatUsersDto> chatUsersDtos) {
		this.chatUsersDtos = chatUsersDtos;
	}
	public ResponseMessage getResponseMessage() {
		return ResponseMessage;
	}
	public void setResponseMessage(ResponseMessage responseMessage) {
		ResponseMessage = responseMessage;
	}
	
	
}
