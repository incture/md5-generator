package oneapp.incture.workbox.demo.notification.service;

import java.util.List;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.notification.dto.NotificationDetail;
import oneapp.incture.workbox.demo.notification.dto.NotificationRequest;
import oneapp.incture.workbox.demo.notification.dto.NotificationRequestDto;

public interface NotificationService {

	NotificationDetail getNotifications(String page,Token token);

	ResponseMessage removeSeenNotification(String type, String id,Token token);

	ResponseMessage saveNotification(List<NotificationRequest> notificationRequests,Token token);

	NotificationDetail getNotifications(String userId,String page);

	ResponseMessage addNotificationConfiguration(NotificationRequestDto notificationDto,Token token);

	NotificationRequestDto getNotificationConfiguration(String userId);

	void sendMail();

}
