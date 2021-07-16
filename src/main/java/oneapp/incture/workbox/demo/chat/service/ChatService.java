package oneapp.incture.workbox.demo.chat.service;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.chat.dto.ChatDetailsDto;
import oneapp.incture.workbox.demo.chat.dto.ChatFavoriteResponse;
import oneapp.incture.workbox.demo.chat.dto.ChatHistoryResponse;
import oneapp.incture.workbox.demo.chat.dto.ChatMemberDto;
import oneapp.incture.workbox.demo.chat.dto.ChatRequestDto;
import oneapp.incture.workbox.demo.chat.dto.ChatResposne;
import oneapp.incture.workbox.demo.chat.dto.ChatUserResponse;
import oneapp.incture.workbox.demo.chat.dto.FavoriteMessageDto;
import oneapp.incture.workbox.demo.chat.dto.MessageDetailDto;
import oneapp.incture.workbox.demo.chat.dto.MessageDetailResponse;

public interface ChatService {

	ChatResposne sendMessage(MessageDetailDto chatMessageDetailDto);

	ChatHistoryResponse getchatHistory(ChatRequestDto chatRequestDto);

	MessageDetailResponse registerChat(ChatRequestDto chatRequestDto);

	ResponseMessage closeOpenChat(ChatRequestDto chatRequestDto);

	ResponseMessage pinChat(ChatRequestDto chatRequestDto);

	ChatDetailsDto getPinnedChats();

	ChatDetailsDto getChatList(ChatRequestDto chatRequestDto);

	ResponseMessage favoriteChat(FavoriteMessageDto favoriteMessageDto);

	ChatFavoriteResponse getAllFavorite();

	ChatUserResponse getUserList();

	List<ChatMemberDto> getTaskOwners(String taskId);	

}
