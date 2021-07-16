package oneapp.incture.workbox.demo.chat.config;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import oneapp.incture.workbox.demo.chat.dto.ChatHistoryResponse;

public class ChatDetailDecoder  implements Decoder.Text<ChatHistoryResponse>{

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
	public ChatHistoryResponse decode(String s) throws DecodeException {
		return gson.fromJson(s, ChatHistoryResponse.class);
	}

	@Override
	public boolean willDecode(String s) {
		return s != null;
	}

}
