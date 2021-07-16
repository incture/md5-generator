package oneapp.incture.workbox.demo.chat.config;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import oneapp.incture.workbox.demo.chat.dto.ChatHistoryResponse;

public class ChatDetailEncoder implements Encoder.Text<ChatHistoryResponse>{

	private static Gson gson = new Gson();
	
	@Override
	public void init(EndpointConfig config) {
		// Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// Auto-generated method stub
		
	}

	@Override
	public String encode(ChatHistoryResponse dto) throws EncodeException {
		 return gson.toJson(dto);
	}

}
