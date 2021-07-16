package oneapp.incture.workbox.demo.notification.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.notification.dto.NotificationDetail;
import oneapp.incture.workbox.demo.notification.util.NotificationConstant;

@Service
@ComponentScan(basePackages="com.workbox")
@ServerEndpoint(value = "/workbox/websocketNotification/{userId}", decoders = NotificationDetailDecoder.class, encoders = NotificationDetailEncoder.class)
public class NotificationWebSocket {

	private Session session;
	private static final Set<NotificationWebSocket> websocketconfigs = new CopyOnWriteArraySet<>();
	private static HashMap<String,Session> users = new HashMap<>();
	private static HashMap<Session,String> usersref = new HashMap<>();
	
	@OnOpen
	public void onOpen(Session session, @PathParam("userId") String userId)throws IOException ,EncodeException
	{
		this.session = session;
		websocketconfigs.add(this);
		users.put(userId,session);
		usersref.put(session,userId);
		NotificationDetail notificationDetail = new NotificationDetail();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.SUCCESS);
		resp.setStatus(NotificationConstant.SUCCESS);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_SUCCESS);
		notificationDetail.setResponseMessage(resp);
		session.getBasicRemote().sendObject(notificationDetail);
	}
	
	@OnClose
	public void onClose(Session session)throws IOException, EncodeException
	{
		websocketconfigs.remove(this);
		users.remove(usersref.get(session));
		usersref.remove(session);
	}
	
	@OnMessage
	public void onMessage(Session session,NotificationDetail notificationDetail)throws IOException, EncodeException{
		
		try{
			if(users.containsKey(notificationDetail.getUserId()))
				broadcast(notificationDetail, users.get(notificationDetail.getUserId()));
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]NOTIFICTION WEBSOCKET ERROR:"+e);
		}
	}
	
	private static void broadcast(NotificationDetail notificationDetail,Session currentSession)throws IOException, EncodeException
	{

		try {
			currentSession.getBasicRemote().sendObject(notificationDetail);
		} catch (IOException|EncodeException e1) {

			e1.printStackTrace();
		}


	}
	
	public void sendMessage(NotificationDetail notificationDetail) {
		try
		{
			System.err.println("[WBP-Dev]here");
			onMessage(session,notificationDetail);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
