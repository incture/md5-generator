package oneapp.incture.workbox.demo.notification.dao;

import java.util.ArrayList;
import java.util.List;
import com.sap.cloud.security.xsuaa.token.Token;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.notification.entity.NotificationUserConfigDo;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationEventDto;

//@Transactional
@Repository("NotificationUserConfigDao")
public class NotificationUserConfigDao extends BaseDao<NotificationUserConfigDo, NotificationEventDto> {

	@Autowired
	private SessionFactory sessionFactory;
	@Override
	protected NotificationEventDto exportDto(NotificationUserConfigDo entity) {

		NotificationEventDto dto = new NotificationEventDto();

		if (!ServicesUtil.isEmpty(entity.getEventId()))
			dto.setEventId(entity.getEventId());
		if (!ServicesUtil.isEmpty(entity.getId()))
			dto.setUserId(entity.getId());
		if (!ServicesUtil.isEmpty(entity.getChannel()))
			dto.setChannel(entity.getChannel());
		if (!ServicesUtil.isEmpty(entity.getType()))
			dto.setType(entity.getType());

		return dto;
	}

	@Override
	protected NotificationUserConfigDo importDto(NotificationEventDto fromDto) {

		NotificationUserConfigDo entity = new NotificationUserConfigDo();
		if (!ServicesUtil.isEmpty(fromDto.getEventId()))
			entity.setEventId(fromDto.getEventId());
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			entity.setId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getChannel()))
			entity.setChannel(fromDto.getChannel());
		if (!ServicesUtil.isEmpty(fromDto.getType()))
			entity.setType(fromDto.getType());
		return entity;
	}

	public String saveOrUpdate(NotificationEventDto fromDto) {
		Session session=null;
		try {
			session=sessionFactory.openSession();
			Transaction tx=session.beginTransaction();
			session.saveOrUpdate(importDto(fromDto));
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}

	public String deleteUserConfig(String eventIds,Token token) {
		Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

		String queryString = "delete from user_notification_config where id='"
				+ token.getLogonName() + "'";

		try {

			if (!ServicesUtil.isEmpty(eventIds)) {
				queryString += " and event_id in ('" + eventIds.substring(0, eventIds.length() - 3) + "')";
			}

			Query query = session.createSQLQuery(queryString);
			query.executeUpdate();
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}

	public String deleteProfileConfig(String userId) {
		
		String queryString = "delete from user_notification_config where id ='" + userId + "'";

		try {
			Session session = sessionFactory.openSession();
	        Transaction tx = session.beginTransaction();
			System.err.println("[deleteProfileConfig][Query String ]" + queryString);
			Query query = session.createSQLQuery(queryString);
			query.executeUpdate();
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}

	public String getUserProfile(String viewName,Token token) {

		Query fetchNameQry = this.getSession()
				.createSQLQuery("select profile_id from notification_profile_setting" + " where user_id='"
						+ token.getLogonName().toUpperCase() + "' " + " and profile_name='" + viewName
						+ "'");
		String profileId = (String) fetchNameQry.uniqueResult();
		System.err.println("[getUserProfile][profileId]" + profileId);

		return profileId;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAllUserConfigured() {

		List<String> resultList = new ArrayList<>();
		try {
			Query query = getSession().createSQLQuery("select distinct id from USER_NOTIFICATION_CONFIG ");
			resultList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;

	}
	
	public String deleteViewDetail(String eventId) {
		String queryString = "delete from USER_NOTIFICATION_CONFIG where event_id = '" + eventId + "'";

		try {
			Session session = null;
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			System.err.println("[deleteViewDetail][Query String ]" + queryString);
			session.createSQLQuery(queryString).executeUpdate();
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}

	public String deleteChannel(String channel) {
		String queryString = "delete from USER_NOTIFICATION_CONFIG where channel = '" + channel + "'";

		try {
			Session session = null;
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			System.err.println("[deleteChannel][Query String ]" + queryString);
			session.createSQLQuery(queryString).executeUpdate();
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}

	public String deleteEventGroup(String viewName) {
		String queryString = "delete from USER_NOTIFICATION_CONFIG where event_id in "
				+ "(select distinct event_id from NOTIFICATION_EVENTS where event_group IN ('" + viewName + "'))";

		try {
			Session session = null;
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			System.err.println("[deleteEventGroup][Query String ]" + queryString);
			session.createSQLQuery(queryString).executeUpdate();
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}
	

}