package oneapp.incture.workbox.demo.notification.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.notification.entity.NotificationDo;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationDto;
import oneapp.incture.workbox.demo.notification.util.NotificationConstant;

@Repository
public class NotificationDao extends BaseDao<NotificationDo, NotificationDto> {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected NotificationDo importDto(NotificationDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		NotificationDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getNotificationId())) {
			entity = new NotificationDo();
			entity.setNotificationId(fromDto.getNotificationId());
			if (!ServicesUtil.isEmpty(fromDto.getDescription()))
				entity.setDescription(fromDto.getDescription());
			if (!ServicesUtil.isEmpty(fromDto.getId()))
				entity.setEventId(fromDto.getId());
			if (!ServicesUtil.isEmpty(fromDto.getEventName()))
				entity.setEventName(fromDto.getEventName());
			if (!ServicesUtil.isEmpty(fromDto.getNotificationType()))
				entity.setNotificationType(fromDto.getNotificationType());
			if (!ServicesUtil.isEmpty(fromDto.getPriority()))
				entity.setPriority(fromDto.getPriority());
			if (!ServicesUtil.isEmpty(fromDto.getTitle()))
				entity.setTitle(fromDto.getTitle());
			if (!ServicesUtil.isEmpty(fromDto.getUserId()))
				entity.setUserId(fromDto.getUserId());
			if (!ServicesUtil.isEmpty(fromDto.getUserName()))
				entity.setUserName(fromDto.getUserName());
			if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
				entity.setCreatedAt(fromDto.getCreatedAt());
			if (!ServicesUtil.isEmpty(fromDto.getEventId()))
				entity.setEventId(fromDto.getEventId());
		}
		return entity;
	}

	@Override
	protected NotificationDto exportDto(NotificationDo entity) {
		NotificationDto notificationDto = new NotificationDto();
		notificationDto.setNotificationId(entity.getNotificationId());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			notificationDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getEventId()))
			notificationDto.setId(entity.getEventId());
		if (!ServicesUtil.isEmpty(entity.getEventName()))
			notificationDto.setEventName(entity.getEventName());
		if (!ServicesUtil.isEmpty(entity.getNotificationType()))
			notificationDto.setNotificationType(entity.getNotificationType());
		if (!ServicesUtil.isEmpty(entity.getPriority()))
			notificationDto.setPriority(entity.getPriority());
		if (!ServicesUtil.isEmpty(entity.getTitle()))
			notificationDto.setTitle(entity.getTitle());
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			notificationDto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getUserName()))
			notificationDto.setUserName(entity.getUserName());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			notificationDto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getEventId()))
			notificationDto.setEventId(entity.getEventId());
		return notificationDto;
	}

	public void saveOrUpdateNotificaions(List<NotificationDto> notificationDtos) {
		Session session = null;
		if (!ServicesUtil.isEmpty(notificationDtos) && !notificationDtos.isEmpty()) {
			// session = this.getSession();
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for (int i = 0; i < notificationDtos.size(); i++) {
				NotificationDto currentTask = notificationDtos.get(i);
				currentTask.setCreatedAt(new Date());
				try {
					session.saveOrUpdate(importDto(currentTask));
				} catch (InvalidInputFault | ExecutionFault | NoResultFault e) {
					e.printStackTrace();
				}
				if (i % 20 == 0 && i > 0) {
					session.flush();
					session.clear();
				}
			}
			session.flush();
			session.clear();
			tx.commit();
			session.close();

		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getNotificationDetail(String userId, String Page) {
		List<Object[]> result = null;
		int page = 0;
		try {
			Query fetchNotifications = this.getSession()
					.createSQLQuery("SELECT NOTIFICATION_ID,DESCRIPTION,EVENT_NAME,PRIORITY,"
							+ "USER_ID,TITLE,CREATED_AT,EVENT_ID,TYPE,USER_NAME FROM NOTIFICATIONS WHERE UPPER(USER_ID) = UPPER('"
							+ userId + "') ORDER BY CREATED_AT DESC ");

			System.err.println("[WBP-Dev][WORKBOX-NEW]getNotificationDetail Query Before Pagination"
					+ fetchNotifications.toString());
			if (!ServicesUtil.isEmpty(Page))
				page = Integer.parseInt(Page);
			if (!ServicesUtil.isEmpty(page) && page > 0) {
				int first = (page - 1) * NotificationConstant.NOTIFICATION_LIMIT;
				int last = NotificationConstant.NOTIFICATION_LIMIT;
				fetchNotifications.setFirstResult(first);
				fetchNotifications.setMaxResults(last);
			}
			System.err.println("[WBP-Dev][WORKBOX-NEW]getNotificationDetail Query" + fetchNotifications.toString());

			result = fetchNotifications.list();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]getNotificationDetail ERROR" + e.getLocalizedMessage());

		}
		return result;
	}

	public Integer getChatNotificationDetail(String userId, String chatId) {
		Integer count = 0;
		try {
			Query countQry = this.getSession()
					.createSQLQuery("SELECT COUNT(*) FROM NOTIFICATIONS WHERE UPPER(USER_ID) = UPPER('"
							+ userId + "') AND EVENT_ID = '" + chatId + "'");

			System.err.println("[WBP-Dev][WORKBOX-NEW]getChatNotificationDetail Query "
					+ countQry.toString());

			count = ((BigInteger) countQry.uniqueResult()).intValue();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]getNotificationDetail ERROR" + e.getLocalizedMessage());

		}
		return count;
	}
	
	public Integer getTotalNotificationCount(String userId) {
		Integer count = 0;
		try {
			String countQry = "SELECT COUNT(*) FROM NOTIFICATIONS WHERE UPPER(USER_ID) = UPPER('" + userId + "')";
			Query fetchQry = this.getSession().createSQLQuery(countQry);
			count = ((BigInteger) fetchQry.uniqueResult()).intValue();
		} catch (Exception e) {
			System.err.println("[WBP-Dev] GET CHAT COUNT" + e.getMessage());
		}
		return count;
	}

	public Integer deleteNotifications(String deleteStr) {
		try {
			Session session = null;
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			int resultRows = session.createSQLQuery(deleteStr).executeUpdate();
			tx.commit();
			session.close();
			return resultRows;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW]DELETE NOTIFICATIONS ERROR" + e);
		}
		return null;
	}

}
