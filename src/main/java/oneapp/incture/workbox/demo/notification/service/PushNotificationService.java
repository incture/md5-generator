package oneapp.incture.workbox.demo.notification.service;

import oneapp.incture.workbox.demo.notification.dto.PushNotificationDto;

public interface PushNotificationService {

	
	Boolean notifyUser(PushNotificationDto pushNotificationDto);
	
	
}
