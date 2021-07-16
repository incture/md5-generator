package oneapp.incture.workbox.demo.notification.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.notification.config.MailNotification;
import oneapp.incture.workbox.demo.notification.dao.IDPMappingDao;
import oneapp.incture.workbox.demo.notification.dao.NotificationDao;
import oneapp.incture.workbox.demo.notification.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.notification.dao.TaskOwnerDao;
import oneapp.incture.workbox.demo.notification.dto.NotificationDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationRequest;
import oneapp.incture.workbox.demo.notification.dto.NotificationResponse;
import oneapp.incture.workbox.demo.notification.dto.NotificationResponseDto;

@Component
public class NotificationUtil {

	@Autowired
	TaskOwnerDao taskOwnerDao;

	@Autowired
	TaskEventsDao taskEvents;

	@Autowired
	TimeZoneConvertion timeZoneConvertion;

	@Autowired
	NotificationDao notificationDao;

	@Autowired
	MailNotification mailNotification;

	@Autowired
	IDPMappingDao idpMappingDao;

	public NotificationResponse createResponse(List<NotificationRequest> notificationRequests) {
		NotificationResponse notificationResponse = new NotificationResponse();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.FAILURE);
		resp.setStatus(NotificationConstant.FAILURE);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);
		notificationResponse.setResponseMessage(resp);
		List<NotificationDto> notificationDtos = null;
		NotificationDto notificationDto = null;
		Set<String> userIds = new HashSet<>();
		List<TaskOwnersDto> ownersDetail = null;
		Object[] taskDetail = null;
		String notificationId = null;
		TaskOwnersDto user = null;
		String description = "";
		try {
			notificationDtos = new ArrayList<>();
			for (NotificationRequest notification : notificationRequests) {
				taskDetail = taskEvents.getTaskDetail(notification.getEventId());
				String title = taskDetail[1].toString() + " : " + taskDetail[2].toString();
				switch (notification.getAction()) {

				case "NEW":
					ownersDetail = taskOwnerDao.getOwners(notification.getEventId());
					description = taskDetail[3].toString();
					for (TaskOwnersDto owner : ownersDetail) {
						try {
							notificationDto = new NotificationDto();
							notificationId = UUID.randomUUID().toString().replaceAll("-", "");
							notificationDto.setCreatedAt(
									ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToIst()));
							notificationDto.setDescription(description);
							notificationDto.setId(notification.getEventId());
							notificationDto.setOrigin("Task");
							notificationDto.setEventName(taskDetail[0].toString());
							notificationDto.setNotificationId(notificationId);
							notificationDto.setNotificationType(NotificationConstant.NEW_TASK);
							notificationDto.setPriority(NotificationConstant.HIGH);
							notificationDto.setTitle(title);
							notificationDto.setUserId(owner.getTaskOwner());
							notificationDto.setUserName(owner.getTaskOwnerDisplayName());
							if (!ServicesUtil.isEmpty(notification.getOrigin())
									&& "AD-hoc".equals(notification.getOrigin())) {
								notificationDto.setStatus(true);
							} else {
								notificationDto.setStatus(false);
							}
							notificationDtos.add(notificationDto);
							System.err.println(notificationDto);
							userIds.add(owner.getTaskOwner());
							System.err.println(userIds);
						} catch (Exception e) {
							System.err.println("[WBP-Dev][WORKBOX-NEW]ERROR DATA MISSING" + e);

						}
					}
					break;

				case "TAGGED":
					user = taskEvents.getTaskCreator(notification.getEventId());
					description = "TAGGED TO : " + taskDetail[3].toString();
					notificationDto = new NotificationDto();
					notificationId = UUID.randomUUID().toString().replaceAll("-", "");
					notificationDto
					.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToIst()));
					notificationDto.setDescription(description);
					notificationDto.setId(notification.getEventId());
					notificationDto.setOrigin("Task");
					notificationDto.setEventName(taskDetail[0].toString());
					notificationDto.setNotificationId(notificationId);
					notificationDto.setNotificationType(NotificationConstant.TAGGED);
					notificationDto.setPriority(NotificationConstant.HIGH);
					notificationDto.setTitle(title);
					notificationDto.setUserId(user.getTaskOwner());
					notificationDto.setUserName(user.getTaskOwnerDisplayName());
					notificationDto.setStatus(true);
					notificationDtos.add(notificationDto);
					userIds.add(user.getTaskOwner());
					break;

				case "Released":
					ownersDetail = taskOwnerDao.getOwners(notification.getEventId());
					notificationDao.deleteNotifications(
							"DELETE FROM NOTIFICATIONS WHERE EVENT_ID = '" + notification.getEventId() + "'");
					description = "TASK : " + taskDetail[3].toString() + " : RELEASED";
					for (TaskOwnersDto owner : ownersDetail) {
						notificationDto = new NotificationDto();
						notificationId = UUID.randomUUID().toString().replaceAll("-", "");
						notificationDto.setCreatedAt(
								ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToIst()));
						notificationDto.setDescription(description);
						notificationDto.setId(notification.getEventId());
						notificationDto.setOrigin("Task");
						notificationDto.setEventName(taskDetail[0].toString());
						notificationDto.setNotificationId(notificationId);
						notificationDto.setNotificationType(NotificationConstant.RELEASED);
						notificationDto.setPriority(NotificationConstant.HIGH);
						notificationDto.setTitle(title);
						notificationDto.setUserId(owner.getTaskOwner());
						notificationDto.setUserName(owner.getTaskOwnerDisplayName());
						notificationDto.setStatus(true);
						notificationDtos.add(notificationDto);
						userIds.add(owner.getTaskOwner());
					}
					break;

				default:
					user = taskEvents.getTaskCreator(notification.getEventId());
					notificationDao.deleteNotifications(
							"DELETE FROM NOTIFICATIONS WHERE EVENT_ID = '" + notification.getEventId() + "'");
					description = "TASK : " + taskDetail[3].toString() + " : " + notification.getAction();
					notificationDto = new NotificationDto();
					notificationId = UUID.randomUUID().toString().replaceAll("-", "");
					notificationDto
					.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToIst()));
					notificationDto.setDescription(description);
					notificationDto.setId(notification.getEventId());
					notificationDto.setOrigin("Task");
					notificationDto.setEventName(taskDetail[0].toString());
					notificationDto.setNotificationId(notificationId);
					notificationDto.setNotificationType(notification.getAction());
					notificationDto.setPriority(NotificationConstant.HIGH);
					notificationDto.setTitle(title);
					notificationDto.setUserId(user.getTaskOwner());
					notificationDto.setUserName(user.getTaskOwnerDisplayName());
					notificationDto.setStatus(true);
					notificationDtos.add(notificationDto);
					userIds.add(user.getTaskOwner());
					break;

				}

			}
			resp.setMessage(NotificationConstant.SUCCESS);
			resp.setStatus(NotificationConstant.SUCCESS);
			resp.setStatusCode(NotificationConstant.STATUS_CODE_SUCCESS);
			notificationResponse.setNotificationDtos(notificationDtos);
			notificationResponse.setUserIds(userIds);
			System.err.println(notificationResponse);
			notificationResponse.setResponseMessage(resp);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]NOTIFICATION UTIL ERROR:" + e);
		}
		return notificationResponse;
	}

	public List<NotificationResponseDto> setNotificationResponse(List<Object[]> response) {
		List<NotificationResponseDto> notificationResponseDtos = null;
		NotificationResponseDto notificationResponseDto = null;
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));
		try {
			notificationResponseDtos = new ArrayList<NotificationResponseDto>();
			for (Object[] res : response) {
				notificationResponseDto = new NotificationResponseDto();
				notificationResponseDto.setCreatedAt(simpleDateFormat1.format(ServicesUtil.resultAsDate(res[6])));
				String s2 = res[1] == null ? null : res[1].toString();
				if(s2 != null && !(res[8].toString().equals("Chat")))
					s2 = s2.substring(res[1] == null ? null : res[1].toString().indexOf(":") + 1);
				notificationResponseDto.setDescription(s2 == null ? null : s2.trim());
				notificationResponseDto.setId(res[7].toString());
				notificationResponseDto.setEventName(res[2].toString());
				notificationResponseDto.setNotificationId(res[0].toString());
				notificationResponseDto.setNotificationType(res[8].toString());
				notificationResponseDto.setPriority(res[3].toString());
				String s1 = res[5] == null ? null : res[5].toString();
				if(s1 != null){
					s1 = s1.substring(res[5] == null ? null : res[5].toString().indexOf(":") + 1);
				}
				notificationResponseDto.setTitle(s1 == null ? null : s1.trim());
				notificationResponseDto.setUserId(res[4].toString());
				notificationResponseDto.setUserName(res[9] == null ? null : (String) res[9]);
				notificationResponseDto.setOrigin("Task");
				// String timeDifference =
				// getTime(res[0].toString(),notificationResponseDto.getCreatedAt());
				// notificationResponseDto.setTimeDiff(timeDifference);

				notificationResponseDtos.add(notificationResponseDto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]GET NOTIFICATION UTIL ERROR:" + e);
		}
		return notificationResponseDtos;
	}

	@SuppressWarnings("unused")
	private String getTime(String notificationId, String fromTimeString) {
		TimeZoneConvertion zoneConvertion = null;
		if (fromTimeString == null) {
			return null;
		} else {
			zoneConvertion = new TimeZoneConvertion();
			String fromTime1 = fromTimeString.substring(0, 20);
			String fromTime = fromTime1.substring(0, 10) + " " + fromTime1.substring(10);

			String toTimeString = zoneConvertion.convertToIst();

			String toTime = toTimeString.substring(0, 10) + " " + toTimeString.substring(11);
			SimpleDateFormat newSd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date1 = null;
			try {
				date1 = newSd.parse(fromTime);
			} catch (Exception e) {
				System.err.println("[WBP-Dev]date 1 :" + e);
			}

			Date date2 = null;
			try {
				date2 = newSd.parse(toTime);
			} catch (Exception e) {
				System.err.println("[WBP-Dev]date 2 :" + e);
			}
			Integer difference = null;
			if(date2 != null && date1 != null)
				difference = (int) ((date2.getTime() - date1.getTime()) / 1000);

			String timeDiff = null;
			if(difference != null)
				timeDiff = getTimeStringSwitch(Math.abs(difference));

			return timeDiff;
		}
	}

	private String getTimeStringSwitch(Integer seconds) {
		Integer reminder = 0;
		String timeString = null;
		System.err.println(seconds);
		if (seconds == 0) {
			timeString = " 0 secs ago";
		} else if (seconds > 2592000) {
			timeString = seconds / 2592000 + " month ago";
		} else if (seconds > 86400) {
			timeString = seconds / 86400 + " days ago";
		} else if (seconds > 3600) {
			reminder = seconds % 3600;
			timeString = seconds / 3600 + " hrs and " + reminder / 60 + " mins ago";
		} else if (seconds > 60) {
			reminder = seconds % 60;
			timeString = seconds / 60 + " mins and " + reminder + " secs ago ";
		} else {
			timeString = "0 secs ago";
		}
		return timeString;
	}

	public void sendMail(NotificationResponse response) {
		Map<String, String> userEmailMap = null;
		try {
			List<Object[]> mailIds = idpMappingDao.getUserMails(response.getUserIds());
			userEmailMap = new HashMap<>();

			for (Object[] obj : mailIds) {
				userEmailMap.put(obj[0].toString(), obj[1].toString());
			}

			for (NotificationDto notificationDto : response.getNotificationDtos()) {
				if (notificationDto.getStatus().equals(true))
					if (userEmailMap.containsKey(notificationDto.getUserId()))
						mailNotification.mail(userEmailMap.get(notificationDto.getUserId()), notificationDto.getTitle(),
								notificationDto.getDescription());
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX_NEW] MAIL UTIL ERROR:" + e);
		}
	}

	public void sendMail() {

		mailNotification.mail("preetham.r@incture.com", "Test",
				"Description");
	}

}
