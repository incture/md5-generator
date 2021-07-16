package oneapp.incture.workbox.demo.chat.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
import oneapp.incture.workbox.demo.chat.service.ChatService;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/chat",produces="application/json")
public class ChatController {

	@Autowired
	private ChatService chatService;
	
	//To Send a Message
	@RequestMapping(value = "/sendMessage",method=RequestMethod.POST,produces = "application/json")
	public ChatResposne sendMessage(@RequestBody MessageDetailDto chatMessageDetailDto)
	{
		return chatService.sendMessage(chatMessageDetailDto);
	}
	
	//To get the Chat History 
	@RequestMapping(value = "/chatHistory",method=RequestMethod.POST,produces = "application/json")
	public ChatHistoryResponse chatHistory(@RequestBody ChatRequestDto chatRequestDto)
	{
		System.err.println("ChatController.chatHistory() request data : "+chatRequestDto);
		return chatService.getchatHistory(chatRequestDto);
	}
	
	@RequestMapping(value = "/registerChat",method=RequestMethod.POST,produces = "application/json")
	public MessageDetailResponse registerChat(@RequestBody ChatRequestDto chatRequestDto)
	{
		return chatService.registerChat(chatRequestDto);
	}
	
	//To Close the chat pop-up
	@RequestMapping(value = "/closeOpenChat",method=RequestMethod.POST,produces = "application/json")
	public ResponseMessage closeOpenChat(@RequestBody ChatRequestDto chatRequestDto)
	{
		return chatService.closeOpenChat(chatRequestDto);
	}
	
	//To pin or unpin  chat
	@RequestMapping(value = "/pinChat",method=RequestMethod.POST,produces = "application/json")
	public ResponseMessage pinChat(@RequestBody ChatRequestDto chatRequestDto)
	{
		return chatService.pinChat(chatRequestDto);
	}
	
	//To get list of Pined chats
	@RequestMapping(value = "/getPinnedChat",method=RequestMethod.GET,produces = "application/json")
	public ChatDetailsDto getPinnedChats()
	{
		return chatService.getPinnedChats();
	}
	
	//To get list of Chats for the user based on type
	@RequestMapping(value = "/getChatList",method=RequestMethod.POST,produces = "application/json")
	public ChatDetailsDto getChatList(@RequestBody ChatRequestDto chatRequestDto)
	{
		return chatService.getChatList(chatRequestDto);
	}
	
	@RequestMapping(value = "/favoriteChat",method=RequestMethod.POST,produces = "application/json")
	public ResponseMessage favoriteChat(@RequestBody FavoriteMessageDto favoriteMessageDto)
	{
		return chatService.favoriteChat(favoriteMessageDto);
		
	}
	
	@RequestMapping(value = "/getAllFavorite",method=RequestMethod.GET,produces = "application/json")
	public ChatFavoriteResponse getAllFavorite()
	{
		return chatService.getAllFavorite();
	}
	
	@RequestMapping(value = "/getUserList",method=RequestMethod.GET,produces = "application/json")
	public ChatUserResponse getUserList()
	{
		return chatService.getUserList();
	}
	
	@RequestMapping(value = "/getTaskOwners/{taskId}",method=RequestMethod.GET,produces = "application/json")
	public List<ChatMemberDto> getTaskOwners(@PathVariable String taskId)
	{
		return chatService.getTaskOwners(taskId);
	}
	
}
