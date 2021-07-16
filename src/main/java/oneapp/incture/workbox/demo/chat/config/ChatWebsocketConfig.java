package oneapp.incture.workbox.demo.chat.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.chat.dto.ChatHistoryResponse;

//import oneapp.incture.workbox.config.HibernateConfiguration;

@Service
@ComponentScan(basePackages="oneapp.incture.workbox")
@ServerEndpoint(value = "/websocketChat/{userId}", decoders = ChatDetailDecoder.class, encoders = ChatDetailEncoder.class)
public class ChatWebsocketConfig {

	private Session session;
	private static final Set<ChatWebsocketConfig> websocketconfigs = new CopyOnWriteArraySet<>();
	private static HashMap<String,Session> users = new HashMap<>();
	private static HashMap<Session,String> usersref = new HashMap<>();

//	@SuppressWarnings("resource")
//	@OnOpen
//	public void onOpen(Session session, @PathParam("userId") String resourceId)throws IOException ,EncodeException
//	{
//		this.session = session;
//		websocketconfigs.add(this);
//		users.put(resourceId,session);
//		usersref.put(session,resourceId);
//		System.err.println(users);
//		ChatHistoryResponse chatHistoryResponse = new ChatHistoryResponse();
//		ChatCountDto chatCountDto = null;
//		List<ChatCountDto> unseenCount = new ArrayList<>();
//		try
//		{

//			ApplicationContext applicationContext = new
//					AnnotationConfigApplicationContext(HibernateConfiguration.class);
//			ChatUsersDao chatUsersDao = applicationContext.getBean(ChatUsersDao.class);

//			List<Object[]> chatCounts = chatUsersDao.getChatCounts(new ChatRequestDto("", resourceId));
//
//			for (Object[] obj : chatCounts) {
//				chatCountDto = new ChatCountDto();
//				chatCountDto.setChatId(obj[0].toString());
//				chatCountDto.setNoOfChat((Integer) obj[1]);
//				unseenCount.add(chatCountDto);
//			}
//
//		}catch (Exception e) {
//			System.err.println("[WBP-Dev][WORKBOX NEW]CHAT ON OPEN ERROR"+e);
//		}
//		chatHistoryResponse.setUnseenCount(unseenCount);
//		ResponseMessage resp = new ResponseMessage();
//		resp.setMessage(ChatConstant.SUCCESS);
//		resp.setStatus(ChatConstant.SUCCESS);
//		resp.setStatusCode(ChatConstant.STATUS_CODE_SUCCESS);
//		chatHistoryResponse.setResponseMessage(resp);
//		session.getBasicRemote().sendObject(chatHistoryResponse);
//
//	}

	@OnClose
	public void onClose(Session session)throws IOException, EncodeException
	{
		websocketconfigs.remove(this);
		System.err.println(session);
		users.remove(usersref.get(session));
		usersref.remove(session);
		System.err.println(users);
	}

	@OnMessage
	public void onMessage(Session session,ChatHistoryResponse chatHistoryResponse)throws IOException, EncodeException{
		if(users.containsKey(chatHistoryResponse.getCurrentUserId()))
			broadcast(chatHistoryResponse,users.get(chatHistoryResponse.getCurrentUserId()));
	}

	private static void broadcast(ChatHistoryResponse chatHistoryResponse,Session currentSession)throws IOException, EncodeException
	{

		try {
			currentSession.getBasicRemote().sendObject(chatHistoryResponse);
		} catch (IOException|EncodeException e1) {

			e1.printStackTrace();
		}

	}

	public void sendMessage(ChatHistoryResponse chatHistoryResponse) {
		try
		{
			System.err.println("[WBP-Dev]here");
			onMessage(session,chatHistoryResponse);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
