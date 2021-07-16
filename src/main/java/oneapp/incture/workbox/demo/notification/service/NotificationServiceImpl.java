package oneapp.incture.workbox.demo.notification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.security.um.user.User;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.notification.config.NotificationWebSocket;
import oneapp.incture.workbox.demo.notification.dao.IDPMappingDao;
import oneapp.incture.workbox.demo.notification.dao.NotificationConfigDao;
import oneapp.incture.workbox.demo.notification.dao.NotificationDao;
import oneapp.incture.workbox.demo.notification.dto.NotificationActionDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationConfigDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationDetail;
import oneapp.incture.workbox.demo.notification.dto.NotificationRequest;
import oneapp.incture.workbox.demo.notification.dto.NotificationRequestDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationResponse;
import oneapp.incture.workbox.demo.notification.dto.NotificationResponseDto;
import oneapp.incture.workbox.demo.notification.util.NotificationConstant;
import oneapp.incture.workbox.demo.notification.util.NotificationUtil;

@Service
//@Transactional
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	NotificationUtil notificationUtil;

	@Autowired
	NotificationDao notificationDao;

	@Autowired
	NotificationWebSocket notificationWebSocket;

	@Autowired
	NotificationConfigDao notificationConfigDao;

	@Autowired
	IDPMappingDao idpMappingDao;

	@Async
	public ResponseMessage saveNotification(List<NotificationRequest> notificationRequests,Token token) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.FAILURE);
		resp.setStatus(NotificationConstant.FAILURE);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);
		NotificationDetail notificationDetail = null;
		try {
			NotificationResponse response = notificationUtil.createResponse(notificationRequests);

			if (response.getResponseMessage().getStatus().equals(NotificationConstant.FAILURE))
				return resp;

			notificationDao.saveOrUpdateNotificaions(response.getNotificationDtos());
			resp = response.getResponseMessage();
			System.err.println(response.getUserIds());
			for (String user : response.getUserIds()) {
				notificationDetail = getNotifications(user,token);
				if (!ServicesUtil.isEmpty(notificationDetail))
					notificationWebSocket.sendMessage(notificationDetail);
			}

			notificationUtil.sendMail(response);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]NOTIFICATION ERROR:" + e);
		}
		return resp;
	}

	@Override
	public NotificationDetail getNotifications(String userId, String page) {
		NotificationDetail notificationDetail = new NotificationDetail();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.FAILURE);
		resp.setStatus(NotificationConstant.FAILURE);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);
		notificationDetail.setResponseMessage(resp);
		try {

			List<Object[]> response = notificationDao.getNotificationDetail(userId,page);

			List<NotificationResponseDto> notificationDtos = notificationUtil.setNotificationResponse(response);

			notificationDetail.setNotifications(notificationDtos);
			resp.setMessage("Notification Fetched");
			resp.setStatus(NotificationConstant.SUCCESS);
			resp.setStatusCode(NotificationConstant.STATUS_CODE_SUCCESS);
			notificationDetail.setResponseMessage(resp);
			notificationDetail.setCount(notificationDtos.size());
			notificationDetail.setUserId(userId);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]GET NOTIFICATION ERROR:" + e);
		}
		return notificationDetail;
	}

	@Override
	public NotificationDetail getNotifications(String page, Token token) {
		NotificationDetail notificationDetail = new NotificationDetail();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.FAILURE);
		resp.setStatus(NotificationConstant.FAILURE);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);
		notificationDetail.setResponseMessage(resp);
		try {
			//User user = UserManagementUtil.getLoggedInUser();
			String user=token.getLogonName();
			System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user);

			List<Object[]> response = notificationDao.getNotificationDetail(user.toUpperCase(),page);
			Integer count = notificationDao.getTotalNotificationCount(user.toUpperCase());

			List<NotificationResponseDto> notificationDtos = notificationUtil.setNotificationResponse(response);

			notificationDetail.setNotifications(notificationDtos);
			resp.setMessage("Notification Fetched");
			resp.setStatus(NotificationConstant.SUCCESS);
			resp.setStatusCode(NotificationConstant.STATUS_CODE_SUCCESS);
			notificationDetail.setResponseMessage(resp);
			notificationDetail.setCount(notificationDtos.size());
			notificationDetail.setTotalNotificationCount(count);
			notificationDetail.setUserId(user.toUpperCase());

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]GET NOTIFICATION ERROR:" + e);
		}
		return notificationDetail;
	}

	@Override
	public ResponseMessage removeSeenNotification(String type, String id,Token token) {
		String deleteStr = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.FAILURE);
		resp.setStatus(NotificationConstant.FAILURE);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);
		try {

			if (NotificationConstant.ALL.equals(type)) {
				//User user = UserManagementUtil.getLoggedInUser();
				String user= token.getLogonName();
				System.err.println("[WBP-Dev][WBProduct-Dev]user : " + user);
				deleteStr = "DELETE FROM NOTIFICATIONS WHERE USER_ID = '" + user.toUpperCase() + "'";
			} else if (NotificationConstant.SINGLE.equals(type)) {
				if (!ServicesUtil.isEmpty(id))
					deleteStr = "DELETE FROM NOTIFICATIONS WHERE NOTIFICATION_ID = '" + id + "'";
				else
					return resp;
			}
			Integer res = notificationDao.deleteNotifications(deleteStr);

			if (!ServicesUtil.isEmpty(res) && res > 0) {
					resp.setMessage("Notification Deleted");
					resp.setStatus(NotificationConstant.SUCCESS);
					resp.setStatusCode(NotificationConstant.STATUS_CODE_SUCCESS);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]REMEOVE NOTIFICATION SERVICE ERROR:" + e);
		}
		return resp;
	}

	@Override
	public NotificationRequestDto getNotificationConfiguration(String userId) {
		NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.FAILURE);
		resp.setStatus(NotificationConstant.FAILURE);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);
		try {
			notificationRequestDto.setNotificationactiondtos(notificationConfigDao.getConfiguration(userId));
			notificationRequestDto.setUserId(userId);
			System.err.println("[WBP-Dev][getNotificationConfiguration] dto :" + notificationRequestDto.toString());

			if (ServicesUtil.isEmpty(notificationRequestDto.getNotificationactiondtos())
					&& ServicesUtil.isEmpty(notificationRequestDto.getActionTypes())
					&& ServicesUtil.isEmpty(notificationRequestDto.getChannels()))
				resp.setMessage(PMCConstant.NO_RESULT);

			resp.setMessage(NotificationConstant.SUCCESS);
			resp.setStatus(NotificationConstant.SUCCESS);
			resp.setStatusCode(NotificationConstant.STATUS_CODE_SUCCESS);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][getNotificationConfiguration]NOTIFICATION ERROR:" + e);
		}
		notificationRequestDto.setResponseMessage(resp);
		return notificationRequestDto;
	}

	public ResponseMessage addNotificationConfiguration(NotificationRequestDto notificationDto,Token token) {
		// TODO Auto-generated method stub
		List<NotificationConfigDto> notificationConfigDtos = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(NotificationConstant.FAILURE);
		resp.setStatus(NotificationConstant.FAILURE);
		resp.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);
		try {
			if (notificationDto != null && (notificationDto.getNotificationactiondtos() != null
					&& notificationDto.getNotificationactiondtos().size() > 0)) {
				notificationConfigDtos = new ArrayList<>();
				String userName = "";
				if (notificationDto.getUserId() != null
						&& notificationDto.getUserId().equalsIgnoreCase(NotificationConstant.ADMIN)) {
					userName = NotificationConstant.ADMIN;
				} else {
					//User user = UserManagementUtil.getLoggedInUser();
					String userId = token.getLogonName().toUpperCase();
					userName = idpMappingDao.getUserName(userId);
					notificationDto.setUserId(userId);
				}
				String deleteStr = "DELETE FROM NOTIFICATION_CONFIG WHERE USER_ID = '" + notificationDto.getUserId() + "'";
				Integer res = notificationDao.deleteNotifications(deleteStr);

				for (NotificationActionDto actionDto : notificationDto.getNotificationactiondtos()) {
					if (actionDto.getChannelLists() != null && actionDto.getChannelLists().size() > 0) {
						for (String channel : actionDto.getChannelLists()) {
							NotificationConfigDto configDto = new NotificationConfigDto();

							configDto.setEnableChannel(channel);
							configDto.setEventOrigin(actionDto.getEventOrigin());
							configDto.setEnableAction(actionDto.getActionType());
							configDto.setUserId(notificationDto.getUserId());
							configDto.setUserName(userName);

							notificationConfigDtos.add(configDto);
						}
					} 
				}
				notificationConfigDao.saveOrUpdateNotificaionConfigs(notificationConfigDtos);

				resp.setMessage(NotificationConstant.SUCCESS);
				resp.setStatus(NotificationConstant.SUCCESS);
				resp.setStatusCode(NotificationConstant.STATUS_CODE_SUCCESS);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]NOTIFICATION ERROR:" + e);
		}
		return resp;
	}

	@Override
	public void sendMail() {
		
		notificationUtil.sendMail();
	}

}
