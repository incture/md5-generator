package oneapp.incture.workbox.demo.notification.config;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.notification.dto.NotificationDetail;

public class NotificationDetailDecoder implements Decoder.Text<NotificationDetail>{

	private static Gson gson = new Gson();

	@Override
	public NotificationDetail decode(String s) throws DecodeException {
		return gson.fromJson(s, NotificationDetail.class);
	}

	@Override
	public boolean willDecode(String s) {
		return s != null;
	}

	@Override
	public void init(EndpointConfig endpointConfig) {
		// Custom initialization logic
	}

	@Override
	public void destroy() {
		// Close resources
	}


}
