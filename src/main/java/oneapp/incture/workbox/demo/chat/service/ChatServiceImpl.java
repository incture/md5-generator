package oneapp.incture.workbox.demo.chat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.chat.dao.ChatInfoDetailDao;
import oneapp.incture.workbox.demo.chat.dao.ChatUsersDao;
import oneapp.incture.workbox.demo.chat.dao.FavoriteMessagesDao;
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
import oneapp.incture.workbox.demo.chat.dto.UserDetail;
import oneapp.incture.workbox.demo.chat.util.ChatConstant;
import oneapp.incture.workbox.demo.chat.util.ChatParser;
import com.sap.security.um.user.User;

//import oneapp.incture.workbox.inbox.dto.UserIDPMappingResponseDto;
//import oneapp.incture.workbox.inbox.sevices.UserIDPMappingFacadeLocal;

@Service
//////@Transactional
public class ChatServiceImpl implements ChatService{

	@Autowired
	private ChatParser chatParse;

	@Autowired
	private ChatUsersDao chatUserDao;

	@Autowired
	private ChatInfoDetailDao chatInfoDetailDao;

	@Autowired
	private FavoriteMessagesDao favoriteMessagesDao;

//	@Autowired
//	private UserIDPMappingFacadeLocal userIdpMappingService;

	@Override
	public ChatResposne sendMessage(MessageDetailDto chatMessageDetailDto) {
		ChatResposne chatResposne = new ChatResposne();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		chatResposne.setResponseMessage(resp);
		ChatRequestDto chatRequestDto = null;
		long start = System.currentTimeMillis();
		try{
			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());
			chatMessageDetailDto.setSentBy(user.getName().toUpperCase());

			chatRequestDto = new ChatRequestDto();
			chatRequestDto.setChatId(chatMessageDetailDto.getChatId());
			if(!ServicesUtil.isEmpty(chatMessageDetailDto.getChatName()))
				chatRequestDto.setChatName(chatMessageDetailDto.getChatName());
			chatRequestDto.setChatType(chatMessageDetailDto.getChatType());
			chatRequestDto.setUserId(chatMessageDetailDto.getSentBy());
			chatRequestDto.setUsers(chatMessageDetailDto.getUsers());
			MessageDetailResponse res = registerChat(chatRequestDto);
			chatResposne.setChatId(chatMessageDetailDto.getChatId());
			if(chatRequestDto.getChatType().equals(ChatConstant.DIRECT) && "".equals(chatRequestDto.getChatId()))
			{
				chatMessageDetailDto.setChatId(res.getCurrentChatId());
				chatResposne.setChatId(res.getCurrentChatId());
			}
			start = System.currentTimeMillis();
			System.err.println("[WBP-Dev][WORKBOX- SEND MESSAGE][UPDATE CHAT USER]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			
			MessageDetailResponse response = chatParse.createMessageDetailResponse(chatMessageDetailDto);
			System.err.println("[WBP-Dev][WORKBOX- SEND MESSAGE][CHAT RESPONSE]" + (System.currentTimeMillis() - start));
			if(ChatConstant.FAILURE.equals(response.getResponseMessage().getStatus()))
			{
				chatResposne.setResponseMessage(resp);
				return chatResposne;
			}

			start = System.currentTimeMillis();
			chatParse.broadcastMessage(response);
			System.err.println("[WBP-Dev][WORKBOX- SEND MESSAGE][CHAT BROADCAST]" + (System.currentTimeMillis() - start));
			
			chatParse.saveMessage(response);


			resp.setMessage("Message sent");
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
			chatResposne.setResponseMessage(resp);

		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX_NEW][CHAT SEND MESSGAE]SERVICE ERROR"+e.getMessage());
		}
		return chatResposne;
	}

	@SuppressWarnings("unused")
	@Override
	public ChatHistoryResponse getchatHistory(ChatRequestDto chatRequestDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		ChatHistoryResponse response = new ChatHistoryResponse();
		response.setResponseMessage(resp);

		try{
			User user = UserManagementUtil.getLoggedInUser();
			Gson g = new Gson();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + g.toJson(user));
			chatRequestDto.setUserId(user.getName().toUpperCase());
			
			MessageDetailResponse res = null;
			if("TASK".equals(chatRequestDto.getChatType()))
				res = registerChat(new ChatRequestDto(chatRequestDto.getChatId(), chatRequestDto.getUserId(), 
						chatRequestDto.getChatType(), chatRequestDto.getChatName()));

			System.err.println("ChatServiceImpl.getchatHistory() before chat history  : ");
			response = chatParse.getChatHistory(chatRequestDto);

			System.err.println(" chat history respones : "+response);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]CHAT HISTORY SERVICE ERROR"+e.getMessage());
		}
		return response;
	}

	@Override
	public MessageDetailResponse registerChat(ChatRequestDto chatRequestDto) {
		System.err.println("ChatServiceImpl.registerChat() request Data : "+chatRequestDto);
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		MessageDetailResponse response = new MessageDetailResponse();
		try{
			System.err.println("[WBP-Dev]here");
			if("TASK".equals(chatRequestDto.getChatType()))
			{

				System.err.println("ChatServiceImpl.registerChat() inside chat type check : ");
				response = chatParse.registerChatUsers(chatRequestDto);

			}
			else if("DIRECT".equals(chatRequestDto.getChatType())){

				response = chatParse.registerDirectChatUsers(chatRequestDto);
			}
			else if("GROUP".equals(chatRequestDto.getChatType())){
				response = chatParse.registerGroupChatUsers(chatRequestDto);
			}

			if(!ServicesUtil.isEmpty(response.getChatUsersDtos()))
				chatUserDao.saveOrUpdateChatUser(response.getChatUsersDtos());
			System.err.println(response.getChatInfoDetails());
			if(!ServicesUtil.isEmpty(response.getChatInfoDetails()))
				chatInfoDetailDao.saveOrUpdateChatInfos(response.getChatInfoDetails());
			resp = response.getResponseMessage();
			System.err.println(response);

			System.err.println("ChatServiceImpl.registerChat() end of register chat ");
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW] REGISTER CHAT SERVICE"+e);
			response.setResponseMessage(resp);

		}
		return response;
	}

	@Override
	public ResponseMessage closeOpenChat(ChatRequestDto chatRequestDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);

		try{
			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());
			chatRequestDto.setUserId(user.getName().toUpperCase());
			chatUserDao.closeOpenChat(chatRequestDto);
			resp.setMessage("Chat Closed");
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]CLOSE OPEN CHAT ERROR"+e);
		}
		return resp;
	}

	@Override
	public ResponseMessage pinChat(ChatRequestDto chatRequestDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);

		try{
			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());
			chatRequestDto.setUserId(user.getName().toUpperCase());
			if(!ServicesUtil.isEmpty(chatRequestDto.getIsPinned())){
				chatUserDao.pinChat(chatRequestDto);
				resp.setMessage("Pinned status updated");
				resp.setStatus(ChatConstant.SUCCESS);
				resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
			}
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]CLOSE OPEN CHAT ERROR"+e);
		}
		return resp;
	}

	@Override
	public ChatDetailsDto getPinnedChats() {
		ChatDetailsDto chatDetailsDto = new ChatDetailsDto();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		chatDetailsDto.setResponseMessage(resp);
		try{
			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());

			chatDetailsDto = chatParse.getPinnedChatDetails(user.getName().toUpperCase());
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]PINNED CHAT DETIALS SERVICE ERROR"+e);
		}
		return chatDetailsDto;
	}

	@Override
	public ChatDetailsDto getChatList(ChatRequestDto chatRequestDto) {
		ChatDetailsDto chatDetailsDto = new ChatDetailsDto();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		chatDetailsDto.setResponseMessage(resp);
		try{
			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());
			chatRequestDto.setUserId(user.getName().toUpperCase());
			chatDetailsDto = chatParse.getChatDetailByType(chatRequestDto);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW] CHAT DETIALS LIST SERVICE ERROR"+e);
		}
		return chatDetailsDto;
	}

	@Override
	public ResponseMessage favoriteChat(FavoriteMessageDto favoriteMessageDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);

		try{
			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());
			favoriteMessageDto.setUserId(user.getName().toUpperCase());
			if(favoriteMessageDto.getFavorite().equals(true))
				favoriteMessagesDao.saveOrUpdateFavoriteMessage(favoriteMessageDto);
			else
				favoriteMessagesDao.messageUnfavorite(favoriteMessageDto);
			resp.setMessage(ChatConstant.SUCCESS);
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]FAVORITE CHAT ERROR"+e);
		}
		return resp;
	}

	@Override
	public ChatFavoriteResponse getAllFavorite() {
		ChatFavoriteResponse favoriteResponse = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);

		try{
			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());
			favoriteResponse = chatParse.getAllFavorite(user.getName().toUpperCase());
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]GET ALL FAVORITE CHATS"+e);
		}
		return favoriteResponse;
	}

	@Override
	public ChatUserResponse getUserList() {
//		ChatUserResponse chatUserResponse = new ChatUserResponse();
//		ResponseMessage resp = new ResponseMessage();
//		resp.setMessage(ChatConstant.FAILURE);
//		resp.setStatus(ChatConstant.FAILURE);
//		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
//		chatUserResponse.setResponseMessage(resp);
//
//		try{
//			User user = UserManagementUtil.getLoggedInUser();
//			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user.toString());
//			UserIDPMappingResponseDto usersList = userIdpMappingService.getIDPUser();
//			List<UserDetail> userDetails = chatParse.setUsersChat(usersList,user.getName().toUpperCase());
//
//			if(!ServicesUtil.isEmpty(userDetails))
//				chatUserResponse.setUserDetails(userDetails);
//
//			resp.setMessage(ChatConstant.SUCCESS);
//			resp.setStatus(ChatConstant.SUCCESS);
//			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
//			chatUserResponse.setResponseMessage(resp);
//
//		}catch (Exception e) {
//			System.err.println("[WBP-Dev][WORKBOX NEW]GET ALL USER CHATS"+e);
//		}
//		return chatUserResponse;
		return null;
	}

	@Override
	public List<ChatMemberDto> getTaskOwners(String taskId) {
		List<ChatMemberDto> chatMemberDtos = new ArrayList<>();
		
		try{
			chatMemberDtos = chatParse.getChatMembersFromTask(taskId);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][Task Owners]"+e);
		}
		return chatMemberDtos;
		
	}


}
