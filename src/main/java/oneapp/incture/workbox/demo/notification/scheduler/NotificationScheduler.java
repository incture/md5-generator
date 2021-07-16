package oneapp.incture.workbox.demo.notification.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.config.MailNotification;
import oneapp.incture.workbox.demo.notification.config.NotificationWebSocket;
import oneapp.incture.workbox.demo.notification.dao.ApplicationUpdatesDao;
import oneapp.incture.workbox.demo.notification.dao.NotificationDao;
import oneapp.incture.workbox.demo.notification.dao.SubstitutionEventsDao;
import oneapp.incture.workbox.demo.notification.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.notification.dto.NotificationActionDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationChannelDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationDetail;
import oneapp.incture.workbox.demo.notification.dto.NotificationDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationRequestDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationResponseDto;
import oneapp.incture.workbox.demo.notification.dto.PushNotificationDto;
import oneapp.incture.workbox.demo.notification.service.PushNotificationService;
import oneapp.incture.workbox.demo.notification.util.NotificationConstant;

@Component("notificationScheduler")
public class NotificationScheduler {

	@Autowired
	TaskEventsDao taskEvents;

	@Autowired
	SubstitutionEventsDao substitutionEventsDao;

	@Autowired
	ApplicationUpdatesDao applicationUpdates;

	@Autowired
	PushNotificationService notificationService;

	@Autowired
	MailNotification mailNotification;

	@Autowired
	NotificationWebSocket notificationWebSocket;

	@Autowired
	NotificationDao notificationDao;

	@Scheduled(fixedDelay = 15000)
	public void sendScheduledNotification() {

		System.err.println("[WBP-Dev][sendNotification][NotificationThread][Scheduler][Start][New] : "
				+ System.currentTimeMillis());
		NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
		notificationRequestDto.setTimeRange(NotificationConstant.NOTIFICATTION_SCHEDULER_INTERVAL);
		prepareNotificationRequest(notificationRequestDto);
		System.err.println(
				"[WBP-Dev][sendNotification][NotificationThread][Scheduler][End][New] : " + System.currentTimeMillis());

	}

	public void prepareNotificationRequest(NotificationRequestDto notificationRequestDto) {
		System.err.println("[sendScheduledNotification][prepareNotificationRequest]notificationRequestDto :"
				+ notificationRequestDto);
		if (!ServicesUtil.isEmpty(notificationRequestDto)) {

			List<NotificationChannelDto> notificationChanneldtos = null;

			if (notificationRequestDto.getNotificationactiondtos() != null) {
				NotificationChannelDto Channeldto = null;

				Map<String, List<String>> channelMap = new HashMap<>();

				for (NotificationActionDto notificationActionDto : notificationRequestDto.getNotificationactiondtos()) {
					List<String> actionType = null;

					for (String channel : notificationActionDto.getChannelLists()) {
						if (channelMap.containsKey(channel)) {
							actionType = new ArrayList<>();
							actionType = channelMap.get(channel);
							actionType.add(notificationActionDto.getActionType());
						} else {
							actionType = new ArrayList<>();
							actionType.add(notificationActionDto.getActionType());
						}
						channelMap.put(channel, actionType);
					}
				}
				notificationChanneldtos = new ArrayList<>();

				for (Map.Entry<String, List<String>> entry : channelMap.entrySet()) {
					Channeldto = new NotificationChannelDto();
					Channeldto.setChannelName(entry.getKey());
					Channeldto.setActionTypes(entry.getValue());
					notificationChanneldtos.add(Channeldto);
					System.err.println(
							"[WBP-Dev][Workbox] Channel: " + entry.getKey() + ", ActionType: " + entry.getValue());
				}

			}

			if (notificationRequestDto.getNotificationChanneldtos() != null)
				notificationChanneldtos = notificationRequestDto.getNotificationChanneldtos();
			else if (notificationRequestDto.getChannels() != null)
				notificationChanneldtos = taskEvents.fetchNotificationConfigDetail(notificationRequestDto.getChannels(),
						null, notificationRequestDto.getUserId());
			else if (notificationRequestDto.getActionTypes() != null)
				notificationChanneldtos = taskEvents.fetchNotificationConfigDetail(null,
						notificationRequestDto.getActionTypes(), notificationRequestDto.getUserId());
			else
				notificationChanneldtos = taskEvents.fetchNotificationConfigDetail(null, null,
						notificationRequestDto.getUserId());

			System.err.println("[sendScheduledNotification][prepareNotificationRequest]notificationChanneldtos : "
					+ notificationChanneldtos);

			// Main trigger point
			sendNotification(notificationChanneldtos, notificationRequestDto.getUserId(),
					notificationRequestDto.getTimeRange());
		}
	}

	public void sendNotification(List<NotificationChannelDto> notificationChanneldtos, String userId,
			Integer timeRange) {
		try {
			for (NotificationChannelDto notificationChannelDto : notificationChanneldtos) {

				if ("Web".equalsIgnoreCase(notificationChannelDto.getChannelName())) {
					System.err.println("[WBP-Dev][sendNotification][webNotificationThread][SchedulerStart] : "
							+ System.currentTimeMillis());
					webNotification(notificationChannelDto, userId, timeRange);
					System.err.println("[WBP-Dev][sendNotification][webNotificationThread][SchedulerEnd] : "
							+ System.currentTimeMillis());

				} else if ("Email".equalsIgnoreCase(notificationChannelDto.getChannelName())) {
					System.err.println("[WBP-Dev][sendNotification][mailNotificationThread][SchedulerStart] : "
							+ System.currentTimeMillis());
					mailNotification(notificationChannelDto, userId, timeRange);
					System.err.println("[WBP-Dev][sendNotification][mailNotificationThread][SchedulerEnd] : "
							+ System.currentTimeMillis());

				} else if ("Mobile".equalsIgnoreCase(notificationChannelDto.getChannelName())) {
					System.err.println("[WBP-Dev][sendNotification][pushNotificationThread][SchedulerStart] : "
							+ System.currentTimeMillis());
					pushNotification(notificationChannelDto, userId, timeRange);
					System.err.println("[WBP-Dev][sendNotification][pushNotificationThread][SchedulerEnd] : "
							+ System.currentTimeMillis());

				}

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][sendNotification][Exception] : " + e.getMessage());

		}
	}

	public void mailNotification(NotificationChannelDto notificationChannelDto, String userId, Integer timeRange) {
		Boolean actionTypeNewTask = false;
		ListIterator<String> listItr = notificationChannelDto.getActionTypes().listIterator();
		while (listItr.hasNext()) {
			if (listItr.next().equalsIgnoreCase(NotificationConstant.NEW_TASK)) {
				actionTypeNewTask = true;
				listItr.remove();
			}
		}

		List<NotificationResponseDto> mailNotificationDtos = new ArrayList<>();
		if (actionTypeNewTask)
			mailNotificationDtos.addAll(taskEvents.getMailNotificationDataForNew(userId, timeRange));
		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			mailNotificationDtos.addAll(taskEvents
					.getMailNotificationDataForAction(notificationChannelDto.getActionTypes(), userId, timeRange));

		mailNotificationDtos.addAll(taskEvents.getMailNotificationDataForSLA(userId, timeRange));

		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			mailNotificationDtos.addAll(substitutionEventsDao
					.getMailNotiForSubstitutionActivation(notificationChannelDto.getActionTypes(), userId, timeRange));

		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			mailNotificationDtos.addAll(substitutionEventsDao.getMailNotificationDataForNewSubstitution(
					notificationChannelDto.getActionTypes(), userId, timeRange));

		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			mailNotificationDtos.addAll(substitutionEventsDao.getMailNotiForSubstitutionApproval(userId, timeRange));

		mailNotificationDtos.addAll(applicationUpdates
				.getMailNotificationDataForApplication(notificationChannelDto.getActionTypes(), userId, timeRange));

		if (!mailNotificationDtos.isEmpty()) {
			for (NotificationResponseDto dto : mailNotificationDtos) {
				System.err.println(
						"[WBP-Dev][sendNotification][mailNotification]mailNotificationDtos : " + dto.toString());
				mailNotification.mail(dto.getUserId(), dto.getTitle(), dto.getDescription());
			}
		}
	}

	public void pushNotification(NotificationChannelDto notificationChannelDto, String userId, Integer timeRange) {
		Boolean actionTypeNewTask = false;
		ListIterator<String> listItr = notificationChannelDto.getActionTypes().listIterator();
		while (listItr.hasNext()) {
			if (listItr.next().equalsIgnoreCase(NotificationConstant.NEW_TASK)) {
				actionTypeNewTask = true;
				listItr.remove();
			}
		}
		List<PushNotificationDto> pushNotificationDtos = new ArrayList<>();

		if (actionTypeNewTask)
			pushNotificationDtos.addAll(taskEvents.getPushNotificationDataForNew(userId, timeRange));
		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			pushNotificationDtos.addAll(taskEvents
					.getPushNotificationDataForAction(notificationChannelDto.getActionTypes(), userId, timeRange));

		pushNotificationDtos.addAll(taskEvents.getPushNotificationDataForSLA(userId, timeRange));

		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			pushNotificationDtos.addAll(substitutionEventsDao.getPushNotificationDataForNewSubstitution(
					notificationChannelDto.getActionTypes(), userId, timeRange));

		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			pushNotificationDtos.addAll(substitutionEventsDao.getPushNotiSubstitutionApproval(userId, timeRange));

		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			pushNotificationDtos.addAll(substitutionEventsDao
					.getPushNotiForSubstitutionActivation(notificationChannelDto.getActionTypes(), userId, timeRange));
		if (!pushNotificationDtos.isEmpty()) {
			for (PushNotificationDto pushNotificationDto : pushNotificationDtos) {
				System.err.println("[WBP-Dev][sendNotification][pushNotification]pushNotificationDto : "
						+ pushNotificationDto.toString());
				notificationService.notifyUser(pushNotificationDto);
			}
		}
	}

	public void webNotification(NotificationChannelDto notificationChannelDto, String userId, Integer timeRange) {
		NotificationDetail notificationDetail = new NotificationDetail();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.FAILURE);
		resp.setStatus(NotificationConstant.FAILURE);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);

		Boolean actionTypeNewTask = false;
		ListIterator<String> listItr = notificationChannelDto.getActionTypes().listIterator();
		while (listItr.hasNext()) {
			if (listItr.next().equalsIgnoreCase(NotificationConstant.NEW_TASK)) {
				actionTypeNewTask = true;
				listItr.remove();
			}
		}

		List<NotificationDto> webNotificationDtos = new ArrayList<>();
		if (actionTypeNewTask)
			webNotificationDtos.addAll(taskEvents.getWebNotificationDataForNew(userId, timeRange));
		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			webNotificationDtos.addAll(taskEvents
					.getWebNotificationDataForAction(notificationChannelDto.getActionTypes(), userId, timeRange));

		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			webNotificationDtos.addAll(substitutionEventsDao
					.getWebNotiForSubstitutionActivation(notificationChannelDto.getActionTypes(), userId, timeRange));

		webNotificationDtos.addAll(taskEvents.getWebNotificationDataForSLA(userId, timeRange));

		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			webNotificationDtos.addAll(substitutionEventsDao.getWebNotificationDataForNewSubstitution(
					notificationChannelDto.getActionTypes(), userId, timeRange));

		webNotificationDtos.addAll(applicationUpdates.getWebNotificationDataForApplicationUpdates(
				notificationChannelDto.getActionTypes(), userId, timeRange));

		if (notificationChannelDto.getActionTypes() != null && notificationChannelDto.getActionTypes().size() > 0)
			webNotificationDtos.addAll(substitutionEventsDao
					.getWebNotiForSubstitutionApproval(notificationChannelDto.getActionTypes(), userId, timeRange));

		System.err.println(
				"[WBP-Dev][sendNotification][webNotification]webNotificationDto size : " + webNotificationDtos);

		if (!webNotificationDtos.isEmpty()) {
			createWebNotification(webNotificationDtos, resp);
			System.err.println("[WBP-Dev][sendNotification][webNotification] SUCCESS");
			if (resp.getMessage().equalsIgnoreCase(NotificationConstant.SUCCESS)) {
				for (NotificationDto dto : webNotificationDtos) {
					System.err.println(
							"[WBP-Dev][sendNotification][webNotification]webNotificationDtos : " + dto.toString());

					NotificationResponseDto responseDto = new NotificationResponseDto(dto);
					List<NotificationResponseDto> notifications = new ArrayList<>();
					notifications.add(responseDto);
					notificationDetail.setNotifications(notifications);
					notificationDetail.setResponseMessage(resp);
					notificationDetail.setUserId(responseDto.getUserId());

					notificationWebSocket.sendMessage(notificationDetail);
				}
			}
		}
	}

	public void createWebNotification(List<NotificationDto> webNotificationDtos, ResponseMessage resp) {
		try {
			notificationDao.saveOrUpdateNotificaions(webNotificationDtos);
			resp.setMessage(NotificationConstant.SUCCESS);
			resp.setStatus(NotificationConstant.SUCCESS);
			resp.setStatusCode(NotificationConstant.STATUS_CODE_SUCCESS);

		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][sendNotification][webNotification][createWebNotification] error : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public ResponseMessage sendChatNotification(NotificationDto notificationDto) {

		NotificationDetail notificationDetail = new NotificationDetail();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.FAILURE);
		resp.setStatus(NotificationConstant.FAILURE);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);

		System.err.println(
				"[WBP-Dev][sendChatNotification][webNotification]webNotificationDtos : " + notificationDto.toString());

		Integer response = notificationDao.getChatNotificationDetail(notificationDto.getUserId(),
				notificationDto.getEventId());
		if (response > 0) {
			String deleteStr = "DELETE FROM NOTIFICATIONS WHERE EVENT_ID = '" + notificationDto.getEventId() + "'";
			notificationDao.deleteNotifications(deleteStr);
		}
		createWebNotification(Arrays.asList(notificationDto), resp);
		if (resp.getMessage().equalsIgnoreCase(NotificationConstant.SUCCESS)) {
			NotificationResponseDto responseDto = new NotificationResponseDto(notificationDto);
			List<NotificationResponseDto> notifications = new ArrayList<>();
			notifications.add(responseDto);
			notificationDetail.setNotifications(notifications);
			notificationDetail.setResponseMessage(resp);
			notificationDetail.setUserId(responseDto.getUserId());

			notificationWebSocket.sendMessage(notificationDetail);
		}
		return resp;
	}
}
