package oneapp.incture.workbox.demo.notification.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import io.swagger.annotations.ApiOperation;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.notification.dto.NotificationDetail;
import oneapp.incture.workbox.demo.notification.dto.NotificationDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationRequestDto;
import oneapp.incture.workbox.demo.notification.dto.PushNotificationDto;
import oneapp.incture.workbox.demo.notification.scheduler.NotificationScheduler;
import oneapp.incture.workbox.demo.notification.service.NotificationService;
import oneapp.incture.workbox.demo.notification.service.PushNotificationService;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/notification", produces = "application/json")
public class NotificationController {

	@Autowired
	NotificationService notificationService;

	@Autowired
	PushNotificationService pushNotificationService;

	@Autowired
	NotificationScheduler notificationScheduler;

	@RequestMapping(value = "/getNotification", method = RequestMethod.GET, produces = "application/json")
	public NotificationDetail getNotifiation(@RequestParam(value = "page", required = false) String page,
			@AuthenticationPrincipal Token token) {
		return notificationService.getNotifications(page, token);
	}

	@RequestMapping(value = "/getUserNotification", method = RequestMethod.GET, produces = "application/json")
	public NotificationDetail getUserNotifiation(@PathVariable String userId, @PathVariable String page) {
		return notificationService.getNotifications(userId, page);
	}

	@RequestMapping(value = "/removeNotification/{type}", method = RequestMethod.GET, produces = "application/json")
	public ResponseMessage removeSeenNotification(@PathVariable String type, @RequestParam(required = false) String id,
			@AuthenticationPrincipal Token token) {
		return notificationService.removeSeenNotification(type, id, token);
	}

	/*
	 * @RequestMapping(value = "/test",method=RequestMethod.POST,produces =
	 * "application/json") public ResponseMessage
	 * removeSeenNotification(@RequestBody List<NotificationRequest>
	 * notificationRequests) { return
	 * notificationService.saveNotification(notificationRequests); }
	 */

	@ApiOperation("To test push notification functionality ")
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String testNotification(@RequestBody PushNotificationDto notificationDto) {

		System.err.println("NotificationController.testNotification() requested data : " + notificationDto);
		try {
			pushNotificationService.notifyUser(notificationDto);
		} catch (Exception e) {
			System.err.println("NotificationController.testNotification() error : " + e);
			return "Failled";
		}
		return "Success ";
	}

	@RequestMapping(value = "/sendNotification", method = RequestMethod.POST, produces = "application/json")
	public String sendNotification(@RequestBody NotificationRequestDto notificationDto) {
		System.err.println("NotificationController.sendNotification() requested data : " + notificationDto);
		try {
			notificationScheduler.prepareNotificationRequest(notificationDto);
		} catch (Exception e) {
			System.err.println("NotificationController.sendNotification() error : " + e);
			return "Failled";
		}
		return "Success ";
	}

	@RequestMapping(value = "/addConfiguration", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage addNotificationConfiguration(@RequestBody NotificationRequestDto notificationDto,@AuthenticationPrincipal Token token) {
		System.err.println("NotificationController.addNotificationConfiguration() requested data : " + notificationDto);
		return notificationService.addNotificationConfiguration(notificationDto,token);
	}

	@RequestMapping(value = "/getConfiguration", method = RequestMethod.GET, produces = "application/json")
	public NotificationRequestDto getNotificationConfiguration(
			@RequestParam(value = "userId", required = false) String userId) {
		System.err.println("NotificationController.getNotificationConfiguration() requested user:" + userId);
		return notificationService.getNotificationConfiguration(userId);
	}

	@RequestMapping(value = "/sendMail", method = RequestMethod.GET, produces = "application/json")
	public void sendMail() {

		notificationService.sendMail();
	}

	@RequestMapping(value = "/sendScheduledNotification", method = RequestMethod.GET, produces = "application/json")
	public void sendScheduledNotification() {

		notificationScheduler.sendScheduledNotification();
	}
	
	@RequestMapping(value = "/sendChatNotification", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage sendChatNotification(@RequestBody NotificationDto notificationDto,@AuthenticationPrincipal Token token) {
		System.err.println("NotificationController.sendChatNotification() requested data : " + notificationDto);
		return notificationScheduler.sendChatNotification(notificationDto);
	}

}
