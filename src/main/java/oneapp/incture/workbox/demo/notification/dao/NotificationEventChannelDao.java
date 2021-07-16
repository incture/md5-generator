package oneapp.incture.workbox.demo.notification.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationEventChannelDto;
import  oneapp.incture.workbox.demo.notification.entity.NotificationChannelEventMappingDo;

/*import oneapp.incture.workbox.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.notification.dto.NotificationEventChannelDto;*/

//@Transactional
@Repository("NotificationEventChannelDao")
public class NotificationEventChannelDao
		extends BaseDao<NotificationChannelEventMappingDo, NotificationEventChannelDto> {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected NotificationChannelEventMappingDo importDto(NotificationEventChannelDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		NotificationChannelEventMappingDo entity = new NotificationChannelEventMappingDo();

		if (!ServicesUtil.isEmpty(fromDto.getChannel()))
			entity.setChannel(fromDto.getChannel());
		if (!ServicesUtil.isEmpty(fromDto.getEventId()))
			entity.setEventId(fromDto.getEventId());
		if (!ServicesUtil.isEmpty(fromDto.getIsDefault()))
			entity.setIsDefault(fromDto.getIsDefault());

		return entity;
	}

	@Override
	protected NotificationEventChannelDto exportDto(NotificationChannelEventMappingDo entity) {

		NotificationEventChannelDto NotificationEventChannelDto = new NotificationEventChannelDto();

		if (!ServicesUtil.isEmpty(entity.getChannel()))
			NotificationEventChannelDto.setChannel(entity.getChannel());
		if (!ServicesUtil.isEmpty(entity.getEventId()))
			NotificationEventChannelDto.setEventId(entity.getEventId());
		if (!ServicesUtil.isEmpty(entity.getIsDefault()))
			NotificationEventChannelDto.setIsDefault(entity.getIsDefault());

		return NotificationEventChannelDto;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getAllChannels() {

		List<String> resultList = new ArrayList<>();
		try {
			Query query = getSession().createSQLQuery("select distinct CHANNEL from  EVENT_CHANNEL_MAPPING");
			resultList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;

	}

	@SuppressWarnings("unchecked")
	public List<String> getAllEvents() {

		List<String> resultList = new ArrayList<>();
		try {
			Query query = getSession().createSQLQuery("select distinct event_id from EVENT_CHANNEL_MAPPING");
			resultList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;

	}
	
	public String deleteChannel(String channel) {
		String queryString = "delete from EVENT_CHANNEL_MAPPING where channel = '" + channel + "'";

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

	public String deleteEventId(String eventId) {
		String queryString = "delete from EVENT_CHANNEL_MAPPING where event_id = '" + eventId + "'";

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

	public String deleteEventGroup(String viewName) {
		String queryString = "delete from EVENT_CHANNEL_MAPPING where event_id in "
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
