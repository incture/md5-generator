package oneapp.incture.workbox.demo.chat.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskOwnerDao;
import oneapp.incture.workbox.demo.adhocTask.util.CompressImage;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.chat.config.ChatWebsocketConfig;
import oneapp.incture.workbox.demo.chat.dao.AttachmentsDao;
import oneapp.incture.workbox.demo.chat.dao.AttachmentsDto;
import oneapp.incture.workbox.demo.chat.dao.ChatInfoDetailDao;
import oneapp.incture.workbox.demo.chat.dao.ChatMessageDetailDao;
import oneapp.incture.workbox.demo.chat.dao.ChatUsersDao;
import oneapp.incture.workbox.demo.chat.dao.TaggedDetailDao;
import oneapp.incture.workbox.demo.chat.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.chat.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.chat.dto.AttachmentDetail;
import oneapp.incture.workbox.demo.chat.dto.ChatCountDto;
import oneapp.incture.workbox.demo.chat.dto.ChatDetailsDto;
import oneapp.incture.workbox.demo.chat.dto.ChatFavoriteResponse;
import oneapp.incture.workbox.demo.chat.dto.ChatHistoryResponse;
import oneapp.incture.workbox.demo.chat.dto.ChatInfoDetailDto;
import oneapp.incture.workbox.demo.chat.dto.ChatMemberDto;
import oneapp.incture.workbox.demo.chat.dto.ChatMessageDetailDto;
import oneapp.incture.workbox.demo.chat.dto.ChatMessages;
import oneapp.incture.workbox.demo.chat.dto.ChatRequestDto;
import oneapp.incture.workbox.demo.chat.dto.ChatResponseDto;
import oneapp.incture.workbox.demo.chat.dto.ChatUsersDto;
import oneapp.incture.workbox.demo.chat.dto.Members;
import oneapp.incture.workbox.demo.chat.dto.MessageDetailDto;
import oneapp.incture.workbox.demo.chat.dto.MessageDetailResponse;
import oneapp.incture.workbox.demo.chat.dto.TaggedDetailDto;
import oneapp.incture.workbox.demo.chat.dto.UserDetail;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.document.service.DocumentService;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;

//import oneapp.incture.workbox.inbox.dto.UserIDPMappingResponseDto;

@Component
public class ChatParser {

	@Autowired
	private ChatMessageDetailDao chatMessageDetailDao;

	@Autowired
	private ChatUsersDao chatUsersDao;

	@Autowired
	private UserIDPMappingDao userIDPMappingDao;

	@Autowired
	private DocumentService documentService;

	@Autowired
	private TaskOwnerDao taskOwnerDao;

	@Autowired
	private TaggedDetailDao taggedDetailDao;

	@Autowired
	private ChatUsersDao chatUserDao;

	@Autowired
	private AttachmentsDao attachmentDao;

	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private ChatInfoDetailDao chatInfoDetailDao;

	@Autowired
	private CompressImage compressImage;
	
	@Autowired
	SharepointUploadFile sharepointUploadFile;

	public MessageDetailResponse createMessageDetailResponse(MessageDetailDto messageDetailDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		MessageDetailResponse response = new MessageDetailResponse();
		ChatMessageDetailDto chatMessageDetailDto = null;
		List<TaggedDetailDto> taggedDetailDtos = null;
		List<ChatUsersDto> chatUsersDtos = null;
		ChatUsersDto chatUsersDto = null;
		List<AttachmentsDto> attachmentsDtos = null;
		AttachmentsDto attachmentsDto = null;
		AttachmentRequestDto attachmentRequestDto = null;
		List<AttachmentRequestDto> attachmentList = null;
		String attachmentId = null;
		TaggedDetailDto taggedDetailDto = null;
		DocumentResponseDto responseDto = new DocumentResponseDto();
		List<AttachmentDetail> attachmentDetails = new ArrayList<>();
		AttachmentDetail attachmentDetail = null;
		ChatHistoryResponse chatHistoryResponse = new ChatHistoryResponse();
		ChatMessages chatMessages = new ChatMessages();
		List<MessageDetailDto> messages = new ArrayList<>();
		try {
			List<String> chatMemebers = chatUsersDao.getUsersPresent(messageDetailDto.getChatId());
			String messageId = UUID.randomUUID().toString().replaceAll("-", "");
			chatMessageDetailDto = new ChatMessageDetailDto();
			chatMessageDetailDto.setAttachmentId(null);
			chatMessageDetailDto.setChatId(messageDetailDto.getChatId());
			chatMessageDetailDto.setFavorite(false);
			if (!ServicesUtil.isEmpty(messageDetailDto.getMessage()))
				chatMessageDetailDto.setMessage(messageDetailDto.getMessage());
			else
				chatMessageDetailDto.setMessage("");
			chatMessageDetailDto.setSentAt(ServicesUtil.convertAdminFromStringToDate(ServicesUtil.getUtcTime()));
			chatMessageDetailDto.setSentBy(messageDetailDto.getSentBy());
			chatMessageDetailDto.setMessageId(messageId);
			response.setChatMessageDetailDto(chatMessageDetailDto);

			taggedDetailDtos = new ArrayList<>();
			chatUsersDtos = new ArrayList<>();
			if (!ServicesUtil.isEmpty(messageDetailDto.getTaggedIds())) {
				for (String userId : messageDetailDto.getTaggedIds()) {
					taggedDetailDto = new TaggedDetailDto();
					taggedDetailDto.setChatId(messageDetailDto.getChatId());
					taggedDetailDto.setMessageId(messageId);
					taggedDetailDto.setUserId(userId);
					taggedDetailDtos.add(taggedDetailDto);
					chatUsersDto = new ChatUsersDto();
					chatUsersDto.setChatId(messageDetailDto.getChatId());
					chatUsersDto.setUnseenCount(0);
					chatUsersDto.setUserId(userId);
					chatUsersDto.setPinned(false);
					chatUsersDto.setUserName(userIDPMappingDao.getOwnerDetailById(userId));
					if (ServicesUtil.isEmpty(chatUsersDto.getUserName()))
						chatUsersDto.setUserName("Demo User");
					chatUsersDto.setChatOpenStatus(false);
					chatUsersDtos.add(chatUsersDto);
					if (!chatMemebers.contains(userId)) {
						chatMemebers.add(userId);
					}
				}

				response.setTaggedDetailDtos(taggedDetailDtos);
				response.setChatUsersDtos(chatUsersDtos);
			}
			attachmentsDtos = new ArrayList<>();

			if (!ServicesUtil.isEmpty(messageDetailDto.getAttachments())) {
				attachmentList = new ArrayList<>();
				for (AttachmentDetail attr : messageDetailDto.getAttachments()) {
					attachmentRequestDto = new AttachmentRequestDto();
					attr.getAttachmentName().replace("$", "");
					attachmentRequestDto.setEncodedFileContent(attr.getAttachment());
					attachmentRequestDto.setFileName(attr.getAttachmentName());
					attachmentRequestDto.setFileType(attr.getAttachmentType());
					attachmentRequestDto
					.setFileSize(ServicesUtil.isEmpty(attr.getAttachmentSize()) ? 0 : attr.getAttachmentSize());
					attachmentList.add(attachmentRequestDto);

				}
				responseDto = sharepointUploadFile.uploadFile(attachmentList,attachmentRequestDto.getFileName());
				Gson g = new Gson();
				Integer count = new Integer(0);
				System.err.println("[WBP-Dev][WORKBOX NEW]DOCUMNET" + g.toJson(responseDto));
				attachmentId = UUID.randomUUID().toString().replaceAll("-", "");
				if (responseDto.getResponse().getStatus().equals(ChatConstant.SUCCESS)) {
					for (oneapp.incture.workbox.demo.document.dto.AttachmentDetail doc : responseDto.getAttachmentIds()) {
						String compressedImg = null;
						System.err.println("[WBP-Dev]original image"
								+ messageDetailDto.getAttachments().get(count).getAttachment());
						if (messageDetailDto.getAttachments().get(count).getAttachmentType()
								.equals(TaskCreationConstant.FILE_TYPE_PNG)
								|| messageDetailDto.getAttachments().get(count).getAttachmentType()
								.equals(TaskCreationConstant.FILE_TYPE_JPG)
								|| messageDetailDto.getAttachments().get(count).getAttachmentType()
								.equals(TaskCreationConstant.FILE_TYPE_JPEG))
							compressedImg = compressImage.compress(
									messageDetailDto.getAttachments().get(count).getAttachment(),
									doc.getAttachmentType(), 0);
						System.err.println("[WBP-Dev]compressed image" + compressedImg);
						attachmentsDto = new AttachmentsDto();
						attachmentsDto.setAttachmentId(attachmentId);
						attachmentsDto.setDocumentId(doc.getAttachmentId());
						attachmentsDto.setDocumentType(doc.getAttachmentType());
						attachmentsDto.setDocumentName(doc.getAttachmentName());
						attachmentsDto.setDocumentSize(doc.getAttachmentSize());
						attachmentsDto.setCompressedImg((compressedImg == null) ? null : compressedImg.getBytes());

						attachmentsDtos.add(attachmentsDto);
						attachmentDetail = new AttachmentDetail();
						attachmentDetail.setAttachmentId(doc.getAttachmentId());
						attachmentDetail.setAttachmentName(doc.getAttachmentName());
						attachmentDetail.setAttachmentType(doc.getAttachmentType());
						attachmentDetail.setAttachment(compressedImg);
						attachmentDetail.setAttachmentSize(doc.getAttachmentSize());
						attachmentDetails.add(attachmentDetail);
						count++;

					}
					chatMessageDetailDto.setAttachmentId(attachmentId);
				}
			}
			messageDetailDto.setAttachments(attachmentDetails);
			messageDetailDto.setSentByName(userIDPMappingDao.getOwnerDetailById(messageDetailDto.getSentBy()));
			messageDetailDto.setSentAt(ServicesUtil.getIstTime());
			messageDetailDto.setMessageId(messageId);
			messages.add(messageDetailDto);
			chatMessages.setMessages(messages);
			chatHistoryResponse.setChatMessages(chatMessages);

			response.setChatHistoryResponse(chatHistoryResponse);
			response.setMembers(chatMemebers);
			response.setAttachmentDetails(attachmentDetails);
			response.setAttachmentsDtos(attachmentsDtos);
			resp.setMessage(ChatConstant.SUCCESS);
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX_NEW][CHAT] CREATING RESPONSE ERROR" + e.getMessage());
		}
		response.setResponseMessage(resp);
		return response;
	}

	public ChatHistoryResponse getChatHistory(ChatRequestDto chatRequestDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		ChatHistoryResponse response = new ChatHistoryResponse();;
		ChatMessages chatMessages = null;
		List<Members> members = null;
		List<ChatCountDto> unseenCount = null;
		Members member = null;
		List<MessageDetailDto> messages = null;
		List<AttachmentDetail> attachmentDetails = null;
		AttachmentDetail attachmentDetail = null;
		MessageDetailDto message = null;
		response.setResponseMessage(resp);
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));
		try {
			List<Object[]> messageDetails = chatMessageDetailDao.getMessages(chatRequestDto);
			List<Object[]> userDetails = chatUsersDao.getUserDetails(chatRequestDto);

			String chatName = chatInfoDetailDao.getChatName(chatRequestDto.getChatId());
			Integer count = chatMessageDetailDao.getTotalMessageCount(chatRequestDto);
			response.setTotalChatCount(count);
			response.setPageCount(ChatConstant.CHAT_LIMIT);
			response.setCurrentPage(chatRequestDto.getPage());
			messages = new ArrayList<>();
			for (int i = messageDetails.size() - 1; i >= 0; i--) {

				Object[] obj = messageDetails.get(i);

				message = new MessageDetailDto();
				message.setChatId(chatRequestDto.getChatId());
				message.setMessage(ServicesUtil.isEmpty(obj[1]) ? null : obj[1].toString());
				message.setSentBy(ServicesUtil.isEmpty(obj[2]) ? null : obj[2].toString());
				message.setSentByName(ServicesUtil.isEmpty(obj[7]) ? "" : obj[7].toString());
				message.setSentAt(simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[3])));
				if ((byte) obj[4] != 0)
					message.setFavorite(true);
				else
					message.setFavorite(false);
				if (!ServicesUtil.isEmpty(obj[6])) {
					message.setTaggedIds(Arrays.asList(obj[6].toString().split(",")));
				}
				attachmentDetails = new ArrayList<>();
				if (!ServicesUtil.isEmpty(obj[5])) {
					String[] attachments = obj[5].toString().split(",");

					for (String att : attachments) {
						attachmentDetail = new AttachmentDetail();
						String[] docs = att.split("[$]");
						attachmentDetail.setAttachmentId(docs[0]);
						attachmentDetail.setAttachmentType(docs[1]);
						attachmentDetail.setAttachmentName(docs[2]);
						attachmentDetail.setAttachmentSize(Integer.valueOf(docs[3]));
						if (attachmentDetail.getAttachmentType().equals(TaskCreationConstant.FILE_TYPE_PNG)
								|| attachmentDetail.getAttachmentType().equals(TaskCreationConstant.FILE_TYPE_JPG)
								|| attachmentDetail.getAttachmentType().equals(TaskCreationConstant.FILE_TYPE_JPEG))
							attachmentDetail.setAttachment(attachmentDao.getCompressedImage(docs[0]));
						attachmentDetails.add(attachmentDetail);
					}
				}
				message.setAttachments(attachmentDetails);
				message.setMessageId(obj[0].toString());
				messages.add(message);
			}

			members = new ArrayList<>();
			for (Object[] user : userDetails) {
				member = new Members();
				member.setUserId(ServicesUtil.isEmpty(user[1]) ? null : user[1].toString());
				if (ServicesUtil.isEmpty(user[1]))
					continue;
				member.setUserName(ServicesUtil.isEmpty(user[2])
						? userIDPMappingDao.getOwnerDetailById(user[1].toString()) : user[2].toString());
				members.add(member);
			}
			chatMessages = new ChatMessages();
			chatMessages.setMessages(messages);
			chatMessages.setMembers(members);

			unseenCount = new ArrayList<>();

			resp.setMessage("Messages fetching successful");
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
			response.setResponseMessage(resp);
			response.setChatMessages(chatMessages);
			response.setUnseenCount(unseenCount);
			response.setCurrentChatId(chatRequestDto.getChatId());
			response.setChatName(chatName);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW] CHAT HISORY CREATING RESPONSE ERROR" + e);
			e.printStackTrace();
		}
		return response;
	}

	public ChatHistoryResponse createChatResponse(MessageDetailResponse messageDetailResponse, List<String> userList) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		ChatHistoryResponse response = new ChatHistoryResponse();
		ChatMessages chatMessages = null;
		List<Members> members = null;
		List<ChatCountDto> unseenCount = null;
		List<MessageDetailDto> messages = null;
		List<AttachmentDetail> attachmentDetails = null;
		MessageDetailDto message = null;
		response.setResponseMessage(resp);
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));
		try {
			messages = new ArrayList<>();

			message = new MessageDetailDto();
			message.setChatId(messageDetailResponse.getChatMessageDetailDto().getChatId());
			message.setMessage(messageDetailResponse.getChatMessageDetailDto().getMessage());
			message.setSentBy(messageDetailResponse.getChatMessageDetailDto().getSentBy());
			message.setSentByName(
					userIDPMappingDao.getOwnerDetailById(messageDetailResponse.getChatMessageDetailDto().getSentBy()));
			message.setSentAt(simpleDateFormat1.format(messageDetailResponse.getChatMessageDetailDto().getSentAt()));
			if (!ServicesUtil.isEmpty(userList)) {
				message.setTaggedIds(userList);
			}
			attachmentDetails = new ArrayList<>();
			if (!ServicesUtil.isEmpty(messageDetailResponse.getAttachmentDetails())) {
				attachmentDetails.addAll(messageDetailResponse.getAttachmentDetails());
			}
			message.setAttachments(attachmentDetails);
			message.setMessageId(messageDetailResponse.getChatMessageDetailDto().getMessageId());
			messages.add(message);

			chatMessages = new ChatMessages();
			chatMessages.setMessages(messages);
			chatMessages.setMembers(members);

			resp.setMessage("Messages fetching successful");
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
			response.setResponseMessage(resp);
			response.setChatMessages(chatMessages);
			response.setUnseenCount(unseenCount);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW] CHAT HISORY CREATING RESPONSE ERROR" + e);
		}
		return response;
	}

	// @Async
	public void broadcastMessage(MessageDetailResponse response) {
		StringBuilder usercountIncreaseIds = new StringBuilder("");
		List<ChatCountDto> unseenCount = null;
		ChatCountDto chatCountDto = null;
		long start = System.currentTimeMillis();
		try {
			System.err.println("[WBP-Dev]here");

			ChatWebsocketConfig webChat = new ChatWebsocketConfig();

			ChatHistoryResponse chatHistoryResponse = response.getChatHistoryResponse();
			System.err.println(
					"[WBP-Dev][WORKBOX- SEND MESSAGE][chatHistoryResponse]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			System.err.println("[WBP-Dev][WORKBOX- SEND MESSAGE]memebers" + String.join("','", response.getMembers()));
			List<Object[]> userDeatils = chatUsersDao.getUserChatDetail(
					"'" + String.join("','", response.getMembers()) + "',",
					chatHistoryResponse.getChatMessages().getMessages().get(0).getChatId());
			System.err.println("[WBP-Dev][WORKBOX- SEND MESSAGE][userDeatils]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			for (Object[] obj : userDeatils) {

				unseenCount = new ArrayList<>();
				chatHistoryResponse.setCurrentUserId(obj[0].toString());
				if (((Byte) obj[4]) != 0) {
					chatCountDto = new ChatCountDto();
					chatCountDto.setChatId(obj[3].toString());
					chatCountDto.setNoOfChat((Integer) obj[2]);
					unseenCount.add(chatCountDto);
					chatHistoryResponse.setUnseenCount(unseenCount);
					System.err.println("[WBP-Dev]here");
					webChat.sendMessage(chatHistoryResponse);
					System.err.println(
							"[WBP-Dev][WORKBOX- SEND MESSAGE][sendMessage]" + (System.currentTimeMillis() - start));
					start = System.currentTimeMillis();
				} else {
					usercountIncreaseIds.append("'");
					usercountIncreaseIds.append(obj[0].toString());
					usercountIncreaseIds.append("'");
					usercountIncreaseIds.append(",");

					chatCountDto = new ChatCountDto();
					chatCountDto.setChatId(obj[3].toString());
					chatCountDto.setNoOfChat(((Integer) obj[2]) + 1);
					unseenCount.add(chatCountDto);
					chatHistoryResponse.setUnseenCount(unseenCount);

					webChat.sendMessage(chatHistoryResponse);
					System.err.println("[WBP-Dev][WORKBOX- SEND MESSAGE][sendMessage2]"
							+ (System.currentTimeMillis() - start));
					start = System.currentTimeMillis();
				}

			}

			if (!usercountIncreaseIds.equals(new StringBuilder("")))
				chatUsersDao.updateCount(chatHistoryResponse.getChatMessages().getMessages().get(0).getChatId(),
						usercountIncreaseIds.toString());

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]SENDING CHAT TO OTHERS" + e);
		}

	}

	public MessageDetailResponse registerChatUsers(ChatRequestDto chatRequestDto) {
		System.err.println("ChatParser.registerChatUsers() request data : " + chatRequestDto);
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		MessageDetailResponse response = new MessageDetailResponse();
		response.setResponseMessage(resp);
		List<ChatUsersDto> chatUsersDtos = null;
		ChatUsersDto chatUsersDto = null;
		List<ChatInfoDetailDto> chatInfoDetails = null;
		ChatInfoDetailDto chatInfoDetailDto = null;
		try {
			List<String> usersPresent = chatUsersDao.getUsersPresent(chatRequestDto.getChatId());
			List<Object[]> chatUsers = taskOwnerDao.getTaskOwners(chatRequestDto.getChatId());
			String creator = taskEventsDao.getTaskOwner(chatRequestDto.getChatId());
			System.err.println("ChatParser.registerChatUsers() exited user : " + usersPresent);
			System.err.println("ChatParser.registerChatUsers() users to be added : " + chatUsers.toString()
			+ " length :" + chatUsers.size());
			chatUsersDtos = new ArrayList<>();

			if (!ServicesUtil.isEmpty(creator) && !usersPresent.contains(creator)) {
				chatUsersDto = new ChatUsersDto();
				chatUsersDto.setChatId(chatRequestDto.getChatId());
				chatUsersDto.setPinned(false);
				chatUsersDto.setUserId(ServicesUtil.isEmpty(creator) ? null : creator);
				chatUsersDto.setUserName(
						ServicesUtil.isEmpty(creator) ? null : userIDPMappingDao.getOwnerDetailById(creator));
				chatUsersDto.setLastSeenTime(ServicesUtil.convertAdminFromStringToDate(ServicesUtil.getIstTime()));

				if (!ServicesUtil.isEmpty(creator)) {
					chatUsersDto.setUnseenCount(0);
					chatUsersDto.setChatOpenStatus(true);
				} else {
					chatUsersDto.setUnseenCount(0);
					chatUsersDto.setChatOpenStatus(false);
				}
				chatUsersDtos.add(chatUsersDto);
			}
			for (Object[] obj : chatUsers) {
				if ((!ServicesUtil.isEmpty(usersPresent) && !ServicesUtil.isEmpty(chatRequestDto)
						&& !ServicesUtil.isEmpty(chatRequestDto.getUserId()) && !ServicesUtil.isEmpty(obj[0]) && 
						usersPresent.contains(obj[0].toString())) || creator.equals(obj[0])) {
					continue;
				}
				chatUsersDto = new ChatUsersDto();
				chatUsersDto.setChatId(chatRequestDto.getChatId());
				chatUsersDto.setPinned(false);
				chatUsersDto.setUserId(ServicesUtil.isEmpty(obj[0]) ? null : obj[0].toString());
				chatUsersDto.setUserName(ServicesUtil.isEmpty(obj[1]) ? null : obj[1].toString());
				chatUsersDto.setLastSeenTime(ServicesUtil.convertAdminFromStringToDate(ServicesUtil.getIstTime()));

				if (!ServicesUtil.isEmpty(chatRequestDto.getUserId()) && !ServicesUtil.isEmpty(obj[0])
						&& chatRequestDto.getUserId().equals(obj[0].toString())) {
					chatUsersDto.setUnseenCount(0);
					chatUsersDto.setChatOpenStatus(true);
				} else {
					chatUsersDto.setUnseenCount(0);
					chatUsersDto.setChatOpenStatus(false);
				}
				chatUsersDtos.add(chatUsersDto);
			}

			chatInfoDetails = new ArrayList<>();
			if (usersPresent.isEmpty()) {
				String chatName = taskEventsDao.getTaskName(chatRequestDto.getChatId());
				chatInfoDetailDto = new ChatInfoDetailDto();
				chatInfoDetailDto.setChatId(chatRequestDto.getChatId());
				chatInfoDetailDto.setChatName(chatName);
				chatInfoDetailDto.setChatType(chatRequestDto.getChatType());

				chatInfoDetails.add(chatInfoDetailDto);
			}

			System.err.println("ChatParser.registerChatUsers() chat user : " + chatUsersDtos);
			response.setChatInfoDetails(chatInfoDetails);
			response.setChatUsersDtos(chatUsersDtos);
			resp.setMessage("Chat is registered");
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
			response.setResponseMessage(resp);

			System.err.println("ChatParser.registerChatUsers() chat history ; " + response);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]REGISTER CHAT USERS" + e);
		}
		return response;
	}

	public ChatDetailsDto getPinnedChatDetails(String userId) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		ChatDetailsDto chatDetailsDto = new ChatDetailsDto();
		List<ChatResponseDto> chatResponseDtos = null;
		ChatResponseDto chatResponseDto = null;
		MessageDetailDto messageDetailDto = null;
		String date1 = null;
		DateFormat indianFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		indianFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		chatDetailsDto.setResponseMessage(resp);
		try {
			List<Object[]> getPinnedChats = chatUsersDao.getPinnedChats(userId);
			chatResponseDtos = new ArrayList<>();
			if (!ServicesUtil.isEmpty(getPinnedChats)) {

				for (Object[] obj : getPinnedChats) {
					chatResponseDto = new ChatResponseDto();
					chatResponseDto.setChatType(obj[2].toString());
					chatResponseDto.setChatId(obj[0].toString());
					chatResponseDto.setIsPinned(true);
					if (!ServicesUtil.isEmpty(obj[4]))
						date1 = indianFormat.format(ServicesUtil.resultAsDate(obj[4]));
					if (!ServicesUtil.isEmpty(obj[1]) && "DIRECT".equals(chatResponseDto.getChatType())) {
						chatResponseDto.setChatName(
								obj[1].toString().replace(userIDPMappingDao.getOwnerDetailById(userId), ""));
					} else {
						chatResponseDto.setChatName(obj[1].toString());
					}
					if (!ServicesUtil.isEmpty(obj[3])) {
						messageDetailDto = new MessageDetailDto();
						messageDetailDto.setChatId(obj[0].toString());
						if (!ServicesUtil.isEmpty(obj[3]))
							messageDetailDto.setMessage(obj[3].toString());
						if (!ServicesUtil.isEmpty(obj[5]))
							messageDetailDto.setSentBy(obj[5].toString());
						if (!ServicesUtil.isEmpty(obj[4]))
							messageDetailDto.setSentAt(date1);
						chatResponseDto.setUserId(userId);
						chatResponseDto.setMessage(messageDetailDto);
					}
					chatResponseDtos.add(chatResponseDto);
				}

			}
			chatDetailsDto.setChatResponseDtos(chatResponseDtos);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]PINNED CHAT DETIALS PARSE ERROR" + e);
		}
		resp.setMessage("Pinned Chat Fetched");
		resp.setStatus(ChatConstant.SUCCESS);
		resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
		chatDetailsDto.setResponseMessage(resp);
		return chatDetailsDto;
	}

	public MessageDetailResponse registerDirectChatUsers(ChatRequestDto chatRequestDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		MessageDetailResponse response = new MessageDetailResponse();
		response.setResponseMessage(resp);
		List<ChatUsersDto> chatUsersDtos = null;
		ChatUsersDto chatUsersDto = null;
		List<ChatInfoDetailDto> chatInfoDetails = null;
		ChatInfoDetailDto chatInfoDetailDto = null;
		StringBuilder chatName = new StringBuilder("");
		try {
			if (!"".equals(chatRequestDto.getChatId())) {
				resp.setMessage("Chat is registered");
				resp.setStatus(ChatConstant.SUCCESS);
				resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
				response.setResponseMessage(resp);
				return response;
			}
			String chatId = UUID.randomUUID().toString().replaceAll("-", "");
			response.setCurrentChatId(chatId);
			chatUsersDtos = new ArrayList<>();
			chatUsersDto = new ChatUsersDto();
			chatUsersDto.setChatId(chatId);
			chatUsersDto.setChatOpenStatus(true);
			chatUsersDto.setLastSeenTime(ServicesUtil.convertAdminFromStringToDate(ServicesUtil.getIstTime()));
			chatUsersDto.setPinned(false);
			chatUsersDto.setUnseenCount(0);
			chatUsersDto.setUserId(chatRequestDto.getUserId());
			chatUsersDto.setUserName(userIDPMappingDao.getOwnerDetailById(chatRequestDto.getUserId()));
			chatUsersDtos.add(chatUsersDto);
			chatName.append(chatUsersDto.getUserName());
			for (String user : chatRequestDto.getUsers()) {
				chatUsersDto = new ChatUsersDto();
				chatUsersDto.setChatId(chatId);
				chatUsersDto.setChatOpenStatus(false);
				chatUsersDto.setLastSeenTime(ServicesUtil.convertAdminFromStringToDate(ServicesUtil.getIstTime()));
				chatUsersDto.setPinned(false);
				chatUsersDto.setUnseenCount(0);
				chatUsersDto.setUserId(user);
				chatUsersDto.setUserName(userIDPMappingDao.getOwnerDetailById(user));
				chatUsersDtos.add(chatUsersDto);
				chatName.append(" ");
				chatName.append(chatUsersDto.getUserName());
			}
			response.setChatUsersDtos(chatUsersDtos);

			chatInfoDetailDto = new ChatInfoDetailDto();
			chatInfoDetailDto.setChatId(chatId);
			chatInfoDetailDto.setChatName(chatName.toString());
			chatInfoDetailDto.setChatType(ChatConstant.DIRECT);
			chatInfoDetails = new ArrayList<>();
			chatInfoDetails.add(chatInfoDetailDto);
			response.setChatInfoDetails(chatInfoDetails);
			resp.setMessage("Chat is registered");
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
			response.setResponseMessage(resp);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]REGISTER CHAT USERS" + e);
		}
		return response;
	}

	public ChatDetailsDto getChatDetailByType(ChatRequestDto chatRequestDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		ChatDetailsDto chatDetailsDto = new ChatDetailsDto();
		List<ChatResponseDto> chatResponseDtos = null;
		ChatResponseDto chatResponseDto = null;
		chatDetailsDto.setResponseMessage(resp);
		MessageDetailDto messageDetailDto = null;
		String date1 = null;
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));

		try {
			List<Object[]> getChatLists = chatUsersDao.getChatList(chatRequestDto);
			resp.setMessage("List Is Empty");
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
			chatDetailsDto.setResponseMessage(resp);
			if (!ServicesUtil.isEmpty(getChatLists)) {
				chatResponseDtos = new ArrayList<>();
				for (Object[] obj : getChatLists) {
					chatResponseDto = new ChatResponseDto();
					chatResponseDto.setChatId(obj[0].toString());
					chatResponseDto.setChatType(obj[1].toString());
					if ((byte) obj[6] != 0)
						chatResponseDto.setIsPinned(true);
					else
						chatResponseDto.setIsPinned(false);
					if (!ServicesUtil.isEmpty(obj[4]))
						date1 = simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[4]));
					if (!ServicesUtil.isEmpty(obj[2]) && "DIRECT".equals(chatResponseDto.getChatType())) {
						chatResponseDto.setChatName(obj[2].toString()
								.replace(userIDPMappingDao.getOwnerDetailById(chatRequestDto.getUserId()), ""));
					} else {
						if (!ServicesUtil.isEmpty(obj[2]))
							chatResponseDto.setChatName(obj[2].toString());
						else
							chatResponseDto.setChatName("");
					}
					if (!ServicesUtil.isEmpty(obj[3])) {
						messageDetailDto = new MessageDetailDto();
						messageDetailDto.setChatId(obj[0].toString());
						if (!ServicesUtil.isEmpty(obj[3]))
							messageDetailDto.setMessage(obj[3].toString());
						if (!ServicesUtil.isEmpty(obj[5]))
							messageDetailDto.setSentBy(obj[5].toString());
						if (!ServicesUtil.isEmpty(obj[4]))
							messageDetailDto.setSentAt(date1);
						if ("DIRECT".equals(chatResponseDto.getChatType()))
							chatResponseDto.setUserId(obj[7].toString());
						chatResponseDto.setMessage(messageDetailDto);
					}

					chatResponseDtos.add(chatResponseDto);
				}

				chatDetailsDto.setChatResponseDtos(chatResponseDtos);
				resp.setMessage("Chat List Fetched");
				resp.setStatus(ChatConstant.SUCCESS);
				resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]CHAT DETIALS ON TYPE PARSE ERROR" + e);
			resp.setMessage(ChatConstant.FAILURE);
			resp.setStatus(ChatConstant.FAILURE);
			resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		}
		chatDetailsDto.setResponseMessage(resp);
		return chatDetailsDto;
	}

	public MessageDetailResponse registerGroupChatUsers(ChatRequestDto chatRequestDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		MessageDetailResponse response = new MessageDetailResponse();
		response.setResponseMessage(resp);
		List<ChatUsersDto> chatUsersDtos = null;
		ChatUsersDto chatUsersDto = null;
		List<ChatInfoDetailDto> chatInfoDetails = null;
		ChatInfoDetailDto chatInfoDetailDto = null;
		StringBuilder chatName = new StringBuilder("");
		String chatId = null;
		try{
			if (!"".equals(chatRequestDto.getChatId()) && chatRequestDto.getUsers().isEmpty()) {
				resp.setMessage("Chat is registered");
				resp.setStatus(ChatConstant.SUCCESS);
				resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
				response.setResponseMessage(resp);
				return response;
			}
			if ("".equals(chatRequestDto.getChatId())) {
				chatId = UUID.randomUUID().toString().replaceAll("-", "");
				response.setCurrentChatId(chatId);
			} else {
				chatId = chatRequestDto.getChatId();
				response.setCurrentChatId(chatId);
			}
			chatUsersDtos = new ArrayList<>();
			if (!ServicesUtil.isEmpty(chatRequestDto.getUserId()) || !"".equals(chatRequestDto.getUserId())) {
				chatUsersDto = new ChatUsersDto();
				chatUsersDto.setChatId(chatId);
				chatUsersDto.setChatOpenStatus(true);
				chatUsersDto.setLastSeenTime(ServicesUtil.convertAdminFromStringToDate(ServicesUtil.getIstTime()));
				chatUsersDto.setPinned(false);
				chatUsersDto.setUnseenCount(0);
				chatUsersDto.setUserId(chatRequestDto.getUserId());
				chatUsersDto.setUserName(userIDPMappingDao.getOwnerDetailById(chatRequestDto.getUserId()));
				chatUsersDtos.add(chatUsersDto);
				chatName.append(chatUsersDto.getUserName());
			}

			for (String user : chatRequestDto.getUsers()) {
				chatUsersDto = new ChatUsersDto();
				chatUsersDto.setChatId(chatId);
				chatUsersDto.setChatOpenStatus(false);
				chatUsersDto.setLastSeenTime(ServicesUtil.convertAdminFromStringToDate(ServicesUtil.getIstTime()));
				chatUsersDto.setPinned(false);
				chatUsersDto.setUnseenCount(0);
				chatUsersDto.setUserId(user);
				chatUsersDto.setUserName(userIDPMappingDao.getOwnerDetailById(user));
				chatUsersDtos.add(chatUsersDto);
				chatName.append(", ");
				chatName.append(chatUsersDto.getUserName());
			}
			response.setChatUsersDtos(chatUsersDtos);

			chatInfoDetails = new ArrayList<>();
			if ("".equals(chatRequestDto.getChatId())) {
				chatInfoDetailDto = new ChatInfoDetailDto();
				chatInfoDetailDto.setChatId(chatId);
				if (ServicesUtil.isEmpty(chatRequestDto.getChatName()) || "".equals(chatRequestDto.getChatName()))
					chatInfoDetailDto.setChatName(chatName.toString());
				else
					chatInfoDetailDto.setChatName(chatRequestDto.getChatName());
				chatInfoDetailDto.setChatType(ChatConstant.GROUP);
				chatInfoDetails.add(chatInfoDetailDto);
			}

			response.setChatInfoDetails(chatInfoDetails);
			resp.setMessage("Chat is registered");
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
			response.setResponseMessage(resp);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]REGISTER CHAT USERS"+e);
		}
		return response;
	}

	public ChatFavoriteResponse getAllFavorite(String userId) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ChatConstant.FAILURE);
		resp.setStatus(ChatConstant.FAILURE);
		resp.setStatusCode(ChatConstant.STATUS_CODE_FAILURE);
		DateFormat indianFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		indianFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		ChatFavoriteResponse favoriteResponse = new ChatFavoriteResponse();
		MessageDetailDto messageDetailDto = null;
		List<MessageDetailDto> messageDetailDtos = null;
		List<AttachmentDetail> attachmentDetails = null;
		AttachmentDetail attachmentDetail = null;
		favoriteResponse.setResponseMessage(resp);
		try {
			List<Object[]> chatDetail = chatMessageDetailDao.getAllFavorite(userId);

			messageDetailDtos = new ArrayList<>();
			for (Object[] obj : chatDetail) {
				messageDetailDto = new MessageDetailDto();
				messageDetailDto.setChatId(obj[0].toString());
				messageDetailDto.setMessageId(obj[1].toString());
				messageDetailDto.setMessage(ServicesUtil.isEmpty(obj[2]) ? "" : obj[2].toString());
				messageDetailDto.setSentBy(obj[3].toString());
				messageDetailDto.setSentByName(obj[9].toString());
				messageDetailDto.setFavorite(true);
				messageDetailDto.setSentAt(indianFormat.format(ServicesUtil.resultAsDate(obj[4])));
				messageDetailDto
				.setChatName(obj[5].toString().replace(userIDPMappingDao.getOwnerDetailById(userId), ""));

				messageDetailDto.setChatType(obj[6].toString());

				if (!ServicesUtil.isEmpty(obj[8])) {
					messageDetailDto.setTaggedIds(Arrays.asList(obj[8].toString().split(",")));
				}
				attachmentDetails = new ArrayList<>();
				if (!ServicesUtil.isEmpty(obj[7])) {
					String[] attachments = obj[7].toString().split(",");

					for (String att : attachments) {
						attachmentDetail = new AttachmentDetail();
						String[] docs = att.split("[$]");
						attachmentDetail.setAttachmentId(docs[0]);
						attachmentDetail.setAttachmentType(docs[1]);
						attachmentDetail.setAttachmentName(docs[2]);
						attachmentDetails.add(attachmentDetail);
					}
				}
				messageDetailDto.setAttachments(attachmentDetails);

				messageDetailDtos.add(messageDetailDto);
			}
			resp.setMessage("Favorite messages fetched");
			resp.setStatus(ChatConstant.SUCCESS);
			resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
			favoriteResponse.setMessages(messageDetailDtos);
			favoriteResponse.setResponseMessage(resp);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX NEW]FAVORITE CHATS" + e);
		}
		return favoriteResponse;
	}

	public List<UserDetail> setUsersChat(Object usersList, String loggedInUser) {//UserIDPMappingResponseDto
		List<UserDetail> userDetails = new ArrayList<>();
		UserDetail userDetail = null;
		//		try {
		//			Map<String, String> userChatMap = chatUsersDao.getChatUser(loggedInUser);
		//			for (UserIDPMappingDto detail : usersList.getDto()) {
		//				if (detail.getUserId().equals(loggedInUser))
		//					continue;
		//				userDetail = new UserDetail();
		//				userDetail.setUserId(detail.getUserId());
		//				userDetail.setCompressedImage(detail.getCompressedImage());
		//				userDetail.setUserEmail(detail.getUserEmail());
		//				userDetail.setUserFirstName(detail.getUserFirstName());
		//				userDetail.setUserLastName(detail.getUserLastName());
		//				if (userChatMap.containsKey(userDetail.getUserId()))
		//					userDetail.setChatId(userChatMap.get(userDetail.getUserId()));
		//				else
		//					userDetail.setChatId("");
		//
		//				userDetails.add(userDetail);
		//			}
		//		} catch (Exception e) {
		//			System.err.println("[WBP-Dev][WORKBOX NEW]Chat Users" + e);
		//		}
		return userDetails;
	}

	@Async
	public void saveMessage(MessageDetailResponse response) {
		long start = System.currentTimeMillis();
		try {
			start = System.currentTimeMillis();
			chatMessageDetailDao.saveOrUpdateChatMessage(response.getChatMessageDetailDto());
			System.err.println(
					"[WBP-Dev][WORKBOX- SEND MESSAGE][saveOrUpdateChatMessage]" + (System.currentTimeMillis() - start));

			start = System.currentTimeMillis();
			taggedDetailDao.saveOrUpdateTaggedDetail(response.getTaggedDetailDtos());
			System.err.println("[WBP-Dev][WORKBOX- SEND MESSAGE][saveOrUpdateTaggedDetail]"
					+ (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			chatUserDao.saveOrUpdateChatUser(response.getChatUsersDtos());
			System.err.println(
					"[WBP-Dev][WORKBOX- SEND MESSAGE][saveOrUpdateChatUser]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			attachmentDao.saveOrUpdateAttachments(response.getAttachmentsDtos());
			System.err.println(
					"[WBP-Dev][WORKBOX- SEND MESSAGE][saveOrUpdateAttachments]" + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][WORKBOX- SEND MESSAGE][save Chat message]"+e);
		}
	}

	public List<ChatMemberDto> getChatMembersFromTask(String taskId) {
		List<ChatMemberDto> chatMemberDtos = new ArrayList<>();
		ChatMemberDto chatMemberDto = null;
		try{
			List<Object[]> chatUsers = taskOwnerDao.getTaskOwners(taskId);
			String creator = taskEventsDao.getTaskOwner(taskId);
			List<Object[]> creatorDetails = userIDPMappingDao.getOwnerDetailsById(creator);
			try {
				chatMemberDto = new ChatMemberDto();
				chatMemberDto.setChatID(taskId);
				chatMemberDto.setDisplayName(creatorDetails.get(0)[0].toString());
				chatMemberDto.setEmail(ServicesUtil.isEmpty(creatorDetails.get(0)[3])?null:creatorDetails.get(0)[3].toString());
				chatMemberDto.setFirstName(creatorDetails.get(0)[1].toString());
				chatMemberDto.setLastName(creatorDetails.get(0)[2].toString());
				chatMemberDto.setId(creator);
				chatMemberDtos.add(chatMemberDto);
			}catch (Exception e) {
				System.err.println("error finiding the creator"+e);
			}
			for (Object[] obj : chatUsers) {
				if(!obj[0].toString().equalsIgnoreCase(creator))
				{
					chatMemberDto = new ChatMemberDto();
					chatMemberDto.setChatID(taskId);
					chatMemberDto.setDisplayName(obj[1].toString());
					chatMemberDto.setEmail(obj[4].toString());
					chatMemberDto.setFirstName(obj[2].toString());
					chatMemberDto.setLastName(obj[3].toString());
					chatMemberDto.setId(obj[0].toString());
					chatMemberDtos.add(chatMemberDto);
				}
			}

		}catch (Exception e) {
			System.err.println(
					"[WBP-Dev][WORKBOX- TASK MEMBERS][ERROR]"+e);
			e.printStackTrace();
		}
		return chatMemberDtos;
	}
}
