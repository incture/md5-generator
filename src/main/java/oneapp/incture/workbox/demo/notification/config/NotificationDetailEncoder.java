package oneapp.incture.workbox.demo.notification.config;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.notification.dto.NotificationDetail;

public class NotificationDetailEncoder implements Encoder.Text<NotificationDetail>{

	private static Gson gson = new Gson();

	@Override
	public String encode(NotificationDetail dto) throws EncodeException {
		 return gson.toJson(dto);
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
