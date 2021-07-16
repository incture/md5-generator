package oneapp.incture.workbox.demo.notification.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.notification.entity.NotificationEventsDo;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationEventDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationSettingDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewDetailRequestDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewDetailResponseDto;

@Repository
public class NotificationEventsDao extends BaseDao<NotificationEventsDo, NotificationEventDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected NotificationEventsDo importDto(NotificationEventDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		NotificationEventsDo entity = new NotificationEventsDo();
		if (!ServicesUtil.isEmpty(fromDto.getEventId()))
			entity.setEventId(fromDto.getEventId());
		if (!ServicesUtil.isEmpty(fromDto.getId()))
			entity.setId(fromDto.getId());
		if (!ServicesUtil.isEmpty(fromDto.getAdminUser()))
			entity.setAdminUser(fromDto.getAdminUser());
		if (!ServicesUtil.isEmpty(fromDto.getPriority()))
			entity.setPriority(fromDto.getPriority());
		if (!ServicesUtil.isEmpty(fromDto.getEventGroup()))
			entity.setEventGroup(fromDto.getEventGroup());
		if (!ServicesUtil.isEmpty(fromDto.getEventName()))
			entity.setEventName(fromDto.getEventName());
		if (!ServicesUtil.isEmpty(fromDto.getIsDefault()))
			entity.setIsDefault(fromDto.getIsDefault());
		return entity;
	}

	@Override
	protected NotificationEventDto exportDto(NotificationEventsDo entity) {

		NotificationEventDto dto = new NotificationEventDto();

		if (!ServicesUtil.isEmpty(entity.getEventId()))
			dto.setEventId(entity.getEventId());
		if (!ServicesUtil.isEmpty(entity.getId()))
			dto.setId(entity.getId());
		if (!ServicesUtil.isEmpty(entity.getAdminUser()))
			dto.setAdminUser(entity.getAdminUser());
		if (!ServicesUtil.isEmpty(entity.getPriority()))
			dto.setPriority(entity.getPriority());
		if (!ServicesUtil.isEmpty(entity.getEventGroup()))
			dto.setEventGroup(entity.getEventGroup());
		if (!ServicesUtil.isEmpty(entity.getEventName()))
			dto.setEventName(entity.getEventName());
		if (!ServicesUtil.isEmpty(entity.getIsDefault()))
			dto.setIsDefault(entity.getIsDefault());

		return dto;
	}

	@SuppressWarnings("unchecked")
	public NotificationViewDetailResponseDto getViewDetail(String viewName, String viewType) {
		NotificationViewDetailResponseDto detailResponseDto = new NotificationViewDetailResponseDto();
		List<NotificationEventDto> eventDtos = new ArrayList<>();
		List<Object[]> resultList = null;

		String queryString = "";
		try {
			if (!"Profile".equals(viewType)) {
				queryString = "select ne.event_group, ne.event_name, ne.event_id, ne.default, "
						+ "ne.priority, (SELECT COALESCE(STRING_AGG (ecm.channel, ', '), '')FROM event_channel_mapping ecm "
						+ "where ecm.event_id=ne.event_id ) as channelList"
						+ ", vs.settings,(SELECT nev.default from notification_events nev where nev.id = '2' "
						+ "and ne.event_id=nev.event_id) as isenable from notification_events ne inner join "
						+ "view_setting vs on ne.event_group=vs.view_name where vs.user_id='Admin' and ne.id = '1' and vs.id='1' ";
				if (!ServicesUtil.isEmpty(viewType)) {
					queryString += " and vs.view_type='" + viewType + "'";
				}
				if (!ServicesUtil.isEmpty(viewName)) {
					queryString += " and vs.view_name='" + viewName + "'";
				}
				System.err.println("NotificationEventsDao.getViewDetail() getViewDetail" + queryString);
				
			} else {
				queryString = "" + "select ne.event_group, ne.event_name, ne.event_id, ne.default, " + "ne.priority, "
						+ "(SELECT COALESCE(STRING_AGG (ecm.channel, ', '), '')FROM event_channel_mapping ecm where ecm.event_id=ne.event_id ) as channelList "
						+ ", nps.setting_id,(SELECT nev.default from notification_events nev where nev.id = '2' "
						+ "and ne.event_id=nev.event_id) as isenable " + "from notification_profile_setting nps "
						+ "join user_notification_config unc on unc.id=nps.profile_id "
						+ "join  notification_events ne on unc.event_id=ne.event_id "
						+ "where nps.user_id='Admin' and ne.id = '1'";

				if (!ServicesUtil.isEmpty(viewType)) {
					queryString += " and unc.type='" + viewType + "'";
				}
				if (!ServicesUtil.isEmpty(viewName)) {
					queryString += " and nps.profile_name='" + viewName + "'";
				}
				System.err.println("NotificationEventsDao.getViewDetail() getViewDetail1" + queryString);
			}

			System.err.println("NotificationEventsDao.getViewDetail() queryStringAdmin" + queryString);
			Query query = getSession().createSQLQuery(queryString);

			resultList = query.list();

			if (!ServicesUtil.isEmpty(resultList)) {

				for (Object[] obj : resultList) {
					NotificationEventDto eventDto = new NotificationEventDto();
					eventDto.setEventGroup(obj[0] == null ? null : (String) obj[0]);
					eventDto.setEventName(obj[1] == null ? null : (String) obj[1]);
					eventDto.setEventId(obj[2] == null ? null : (String) obj[2]);
					byte flag = (obj[3] == null ? 0 : (byte) obj[3]);
					eventDto.setIsDefault(flag == 1 ? true : false);
					byte isEnable = (obj[7] == null ? flag : (byte) obj[7]);
					eventDto.setIsEnable(isEnable == 1 ? true : false);

					eventDto.setPriority(obj[4] == null ? null : (String) obj[4]);

					String channelList = obj[5] == null ? null : (String) obj[5];
					eventDto.setChannelList(Arrays.asList(channelList.split("\\s*,\\s*")));
					eventDto.setSettingId(obj[6] == null ? null : (String) obj[6]);
					eventDtos.add(eventDto);
				}

				detailResponseDto.setEventDtos(eventDtos);
				System.err.println("NotificationEventsDao.getViewDetail() detailResponseDto" + detailResponseDto);

			}
		} catch (Exception e) {
			System.err.println("NotificationEventsDao.getViewDetail() error" + e.getMessage());
		}

		return detailResponseDto;

	}

	@SuppressWarnings("unchecked")
	public NotificationViewDetailResponseDto getUserViewDetail(NotificationViewDetailRequestDto requestDto) {
		NotificationViewDetailResponseDto detailResponseDto =  null;
		List<NotificationEventDto> eventDtos = new ArrayList<>();
		List<Object[]> resultList = null;
		System.err.println("NotificationEventsDao.getViewDetail() entering testing" +requestDto);
		String queryString = "";
		if (!"Profile".equals(requestDto.getViewType())) {
			/*
			 * queryString =
			 * "select distinct ne.event_group, ne.event_name, ne.event_id, ne.default, ne.priority,unc.channel, vs.settings"
			 * + ",(SELECT nev.default from notification_events nev where nev.id = '2' " +
			 * "and ne.event_id=nev.event_id) as isenable " +
			 * " from user_notification_config unc inner join notification_events ne" +
			 * " on ne.event_id=unc.event_id" +
			 * " inner join view_setting vs on ne.event_group=vs.view_name " +
			 * " where vs.user_id='" + requestDto.getUserId() + "' and ne.id = '1'";
			 */
			queryString = "SELECT DISTINCT ne.event_group,	ne.event_name,ne.event_id,	ne.default,ne.priority,(SELECT COALESCE(STRING_AGG(uc.channel,', '	),'')"
					+ " FROM user_notification_config AS uc WHERE uc.id = '" + requestDto.getUserId()
					+ "' AND ne.event_id = uc.event_id GROUP BY event_id) AS channelList,vs.settings"
					+ ",(SELECT nev.default from notification_events nev where nev.id = '2' "
					+ "and ne.event_id=nev.event_id) as isenable "
					+ " FROM user_notification_config AS unc INNER JOIN notification_events AS ne ON ne.event_id = unc.event_id"
					+ " INNER JOIN view_setting AS vs ON ne.event_group = vs.view_name WHERE vs.user_id = '"
					+ requestDto.getUserId() + "'" + " AND vs.user_id = unc.id and ne.id = '1'";

			if (!ServicesUtil.isEmpty(requestDto.getViewType())) {
				if ("Where".equalsIgnoreCase(requestDto.getViewType())) {
					if (!ServicesUtil.isEmpty(requestDto.getViewName()))
						queryString += " and unc.channel='" + requestDto.getViewName() + "' and "
								+ "(SELECT vst.default from view_setting vst where vst.user_id='Admin' and vst.id = '2' "
								+ "and vst.view_name = vs.view_name) = 1 and (SELECT nev.default from notification_events nev where nev.id = '2'"
								+ " and ne.event_id=nev.event_id) =1";
				} else {
					queryString += " and vs.view_type='" + requestDto.getViewType() + "'";
					queryString += " and vs.view_name='" + requestDto.getViewName() + "'";
				}
			}

			else {
				queryString += " and (SELECT vst.default from view_setting vst where vst.user_id='Admin' and vst.id = '2' "
						+ "and vst.view_name = vs.view_name) = 1 and (SELECT nev.default from notification_events nev where nev.id = '2' "
						+ " and ne.event_id=nev.event_id) =1";
			}
		}
		
		else {
			
			queryString = "" + "select distinct ne.event_group, ne.event_name, ne.event_id, ne.default, "
					+ "ne.priority, " + " (select COALESCE(STRING_AGG (channel, ', '), '') "
					+ "from user_notification_config unc join notification_profile_setting"
					+ " nps on nps.profile_id=unc.id where nps.profile_name='" + requestDto.getViewName() + "' "
					+ "and nps.user_id='" + requestDto.getUserId()
					+ "' and event_id=ne.event_id) as channelList , nps.setting_id "
					+ ",(SELECT nev.default from notification_events nev where nev.id = '2' "
					+ "and ne.event_id=nev.event_id) as isenable " + "from notification_profile_setting nps "
					+ "join user_notification_config unc on unc.id=nps.profile_id "
					+ "join  notification_events ne on unc.event_id=ne.event_id " + "where nps.user_id='"
					+ requestDto.getUserId() + "' and ne.id = '1' ";

			// nps.profile_name='"+ requestDto.getViewName() + "' and
			// unc.type='" + requestDto.getViewType() + "' and

			if (!ServicesUtil.isEmpty(requestDto.getViewType())) {
				queryString += " and unc.type='" + requestDto.getViewType() + "'";
			}
			if (!ServicesUtil.isEmpty(requestDto.getViewName())) {
				queryString += " and nps.profile_name='" + requestDto.getViewName() + "'";
			}
		}
		System.err.println("NotificationEventsDao.getUserViewDetail() queryString " + queryString);

		Query query = getSession().createSQLQuery(queryString);

		

		resultList = query.list();

		if (!ServicesUtil.isEmpty(resultList)) {
			detailResponseDto = new NotificationViewDetailResponseDto();

			for (Object[] obj : resultList) {
				NotificationEventDto eventDto = new NotificationEventDto();
				eventDto.setEventGroup(obj[0] == null ? null : (String) obj[0]);
				eventDto.setEventName(obj[1] == null ? null : (String) obj[1]);
				eventDto.setEventId(obj[2] == null ? null : (String) obj[2]);
				byte flag = (obj[3] == null ? 0 : (byte) obj[3]);
				eventDto.setIsDefault(flag == 1 ? true : false);
				byte isEnable = (obj[7] == null ? flag : (byte) obj[7]);
				eventDto.setIsEnable(isEnable == 1 ? true : false);

				eventDto.setPriority(obj[4] == null ? null : (String) obj[4]);

				String channelList = obj[5] == null ? null : (String) obj[5];
				eventDto.setChannelList(Arrays.asList(channelList.split("\\s*,\\s*")));
				eventDto.setSettingId(obj[6] == null ? null : (String) obj[6]);
				eventDtos.add(eventDto);
			}

			detailResponseDto.setEventDtos(eventDtos);
			System.err.println("NotificationEventsDao.getUserViewDetail() detailResponseDto" + detailResponseDto);

		}

		return detailResponseDto;

	}

	@SuppressWarnings("unchecked")
	public List<NotificationSettingDto> getSettingDetail(String profileSettingId, String tabType, String isAdmin, Token token) {
		List<NotificationSettingDto> settingDtos = null;
		String queryString = " ";
		System.err.println("NotificationEventsDao.getSettingDetail() data " +profileSettingId);
		System.err.println("NotificationEventsDao.getSettingDetail() data1 " +tabType);
		System.err.println("NotificationEventsDao.getSettingDetail() data2 " +isAdmin);
		if (ServicesUtil.isEmpty(profileSettingId) && "AdditionalSetting".equals(tabType)) {

			if (ServicesUtil.isEmpty(isAdmin)) {
				//profileSettingId = UserManagementUtil.getLoggedInUser().getName();
				profileSettingId=token.getLogonName();
				System.err.println("NotificationEventsDao.getSettingDetail() profileSettingId " +profileSettingId);
			}
			else {
				profileSettingId = "Admin";
			System.err.println("NotificationEventsDao.getSettingDetail() profileSettingId1 " +profileSettingId);
			}
			queryString = "select usd.ADDITONAL_SETTING_ID, usd.VALUE,usd.more,ads.setting_name, ads.DATATYPE,usd.PROFILE_SETTING_ID,"
					+ "(select default from user_setting_details where profile_setting_id in ('Admin') "
					+ "and ADDITONAL_SETTING_ID = usd.ADDITONAL_SETTING_ID) as isEnable "
					+ " from user_setting_details usd " + " inner join additional_setting "
					+ " ads on usd.ADDITONAL_SETTING_ID= ads.ADDITONAL_SETTING_ID where usd.profile_setting_id='"
					+ profileSettingId + "'";
			System.err.println("NotificationEventsDao.getSettingDetail() queryString if " +queryString);
		} else {
			queryString = "select usd.ADDITONAL_SETTING_ID, usd.VALUE,usd.more,ads.setting_name, ads.DATATYPE,usd.PROFILE_SETTING_ID,"
					+ "(select default from user_setting_details where profile_setting_id in "
					+ "(select settings from VIEW_SETTING where view_name in (select view_name from VIEW_SETTING "
					+ "where settings ='" + profileSettingId + "') and user_id = 'Admin' and id='1') "
					+ "and ADDITONAL_SETTING_ID = usd.ADDITONAL_SETTING_ID) as isEnable "
					+ " from user_setting_details usd " + " inner join additional_setting "
					+ " ads on usd.ADDITONAL_SETTING_ID= ads.ADDITONAL_SETTING_ID where usd.profile_setting_id='"
					+ profileSettingId + "'";
			System.err.println("NotificationEventsDao.getSettingDetail() queryString else " +queryString);
		}

		Query query = getSession().createSQLQuery(queryString);

		System.err.println("getResult [QueryString]" + queryString);

		List<Object[]> resultList = query.list();
		System.err.println("NotificationEventsDao.getSettingDetail() resultList1 " +resultList);
		boolean value = false;
		if (ServicesUtil.isEmpty(resultList)) {
			value = true;
			System.err.println("NotificationEventsDao.getSettingDetail() entering if test " +resultList);
			if (!"AdditionalSetting".equals(tabType)) {
				query = getSession().createSQLQuery(
						"select usd.ADDITONAL_SETTING_ID, usd.VALUE,usd.more,ads.setting_name, ads.DATATYPE,usd.PROFILE_SETTING_ID, "
								+ "(select default from user_setting_details where profile_setting_id in "
								+ "(select settings from VIEW_SETTING where view_name in (select view_name from VIEW_SETTING "
								+ "where settings ='1') and user_id = 'Admin' and id='1') "
								+ "and ADDITONAL_SETTING_ID = usd.ADDITONAL_SETTING_ID) as isEnable "
								+ "from user_setting_details usd " + " inner join additional_setting "
								+ " ads on usd.ADDITONAL_SETTING_ID= ads.ADDITONAL_SETTING_ID where usd.profile_setting_id='1'");
				System.err.println("NotificationEventsDao.getSettingDetail() query if " +query);
			} else {
				query = getSession().createSQLQuery(
						"select usd.ADDITONAL_SETTING_ID, usd.VALUE,usd.more,ads.setting_name, ads.DATATYPE,usd.PROFILE_SETTING_ID, "
								+ "(select default from user_setting_details where profile_setting_id in ('Admin') "
								+ "and ADDITONAL_SETTING_ID = usd.ADDITONAL_SETTING_ID) as isEnable "
								+ "from user_setting_details usd " + " inner join additional_setting "
								+ " ads on usd.ADDITONAL_SETTING_ID= ads.ADDITONAL_SETTING_ID where usd.profile_setting_id='1'");
				System.err.println("NotificationEventsDao.getSettingDetail() query else " +query);
			}
			

		}
		resultList = query.list();
		if (resultList != null) {
			System.err.println("NotificationEventsDao.getSettingDetail() resultList " +resultList);
			settingDtos = new ArrayList<>();
			for (Object[] obj : resultList) {
				NotificationSettingDto settingDto = new NotificationSettingDto();
				settingDto.setAdditionalSettingId(obj[0] == null ? null : (String) obj[0]);
				settingDto.setValue(obj[1] == null ? null : (String) obj[1]);
				settingDto.setMore(obj[2] == null ? null : (String) obj[2]);
				settingDto.setSettingName(obj[3] == null ? null : (String) obj[3]);
				settingDto.setDataType(obj[4] == null ? null : (String) obj[4]);
				if (value)
					settingDto.setProfileSettingId(profileSettingId);
				else {
					if (ServicesUtil.isEmpty(isAdmin))
						settingDto.setProfileSettingId(obj[5] == null ? null : (String) obj[5]);
					else
						settingDto.setProfileSettingId(profileSettingId);
				}

				byte isEnable = (obj[6] == null ? 1 : (byte) obj[6]);
				settingDto.setIsEnable(isEnable == 1 ? true : false);

				if (!"Vibration".equalsIgnoreCase(settingDto.getAdditionalSettingId()))
					settingDto.setSelectionList(
							"/inbox/selectionList?selectionParameter=" + settingDto.getAdditionalSettingId());
				settingDtos.add(settingDto);
				
			}
		}
		System.err.println("NotificationEventsDao.getSettingDetail() settingDto " +settingDtos);

		return settingDtos;
	}

	@SuppressWarnings("unchecked")
	public List<NotificationSettingDto> getAllSettingDetail(String userId, String event) {
		List<NotificationSettingDto> settingDtos = null;

		// Based on Priority
		// Setting Details based on additional settings configured by user
		Query query = getSession().createSQLQuery(
				"select usd.ADDITONAL_SETTING_ID, usd.VALUE,usd.more,ads.setting_name, ads.DATATYPE,usd.PROFILE_SETTING_ID from "
						+ "user_setting_details usd inner join additional_setting ads on usd.ADDITONAL_SETTING_ID= ads.ADDITONAL_SETTING_ID "
						+ "where usd.profile_setting_id= '" + userId + "'");

		List<Object[]> resultList = query.list();

		// Setting Details based on profile settings configured by user
		if (ServicesUtil.isEmpty(resultList)) {

			query = getSession().createSQLQuery(
					"select usd.ADDITONAL_SETTING_ID, usd.VALUE,usd.more,ads.setting_name, ads.DATATYPE,usd.PROFILE_SETTING_ID from "
							+ "user_setting_details usd inner join additional_setting ads on usd.ADDITONAL_SETTING_ID= ads.ADDITONAL_SETTING_ID "
							+ "inner join notification_profile_setting nps on nps.SETTING_ID= usd.PROFILE_SETTING_ID where "
							+ "nps.user_id = '" + userId + "' and nps.is_active = '1'");

			resultList = query.list();
		}

		// Setting Details based on event object configured by user
		if (ServicesUtil.isEmpty(resultList)) {

			query = getSession().createSQLQuery(
					"select usd.ADDITONAL_SETTING_ID, usd.VALUE,usd.more,ads.setting_name, ads.DATATYPE,usd.PROFILE_SETTING_ID from "
							+ "user_setting_details usd inner join additional_setting ads on usd.ADDITONAL_SETTING_ID= ads.ADDITONAL_SETTING_ID "
							+ "inner join VIEW_SETTING vs on vs.SETTINGS= usd.PROFILE_SETTING_ID "
							+ "where vs.user_id = '" + userId + "' and vs.VIEW_NAME IN ('" + event + "')");

			resultList = query.list();
		}

		// Setting Details based on Channel object configured by user
		if (ServicesUtil.isEmpty(resultList)) {

			query = getSession().createSQLQuery(
					"select usd.ADDITONAL_SETTING_ID, usd.VALUE,usd.more,ads.setting_name, ads.DATATYPE,usd.PROFILE_SETTING_ID from "
							+ "user_setting_details usd inner join additional_setting ads on usd.ADDITONAL_SETTING_ID= ads.ADDITONAL_SETTING_ID "
							+ "inner join VIEW_SETTING vs on vs.SETTINGS= usd.PROFILE_SETTING_ID "
							+ "where vs.user_id = '" + userId + "' and vs.VIEW_NAME IN ('Web')");

			resultList = query.list();
		}

		// Default Setting Details if not configured by user
		if (ServicesUtil.isEmpty(resultList)) {

			query = getSession().createSQLQuery(
					"select usd.ADDITONAL_SETTING_ID, usd.VALUE,usd.more,ads.setting_name, ads.DATATYPE,usd.PROFILE_SETTING_ID from user_setting_details usd "
							+ " inner join additional_setting"
							+ " ads on usd.ADDITONAL_SETTING_ID= ads.ADDITONAL_SETTING_ID where usd.profile_setting_id='1'");

			resultList = query.list();
		}

		System.err.println("NotificationEventsDao.getAllSettingDetail() Query " + query);

		if (resultList != null) {
			settingDtos = new ArrayList<>();
			for (Object[] obj : resultList) {
				NotificationSettingDto settingDto = new NotificationSettingDto();
				settingDto.setAdditionalSettingId(obj[0] == null ? null : (String) obj[0]);
				settingDto.setValue(obj[1] == null ? null : (String) obj[1]);
				settingDto.setMore(obj[2] == null ? null : (String) obj[2]);
				settingDto.setSettingName(obj[3] == null ? null : (String) obj[3]);
				settingDto.setDataType(obj[4] == null ? null : (String) obj[4]);
				settingDto.setProfileSettingId(obj[5] == null ? null : (String) obj[5]);

				if (!"Vibration".equalsIgnoreCase(settingDto.getAdditionalSettingId()))
					settingDto.setSelectionList(
							"/inbox/selectionList?selectionParameter=" + settingDto.getAdditionalSettingId());
				settingDtos.add(settingDto);
			}
		}

		return settingDtos;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllEvents() {

		List<String> resultList = new ArrayList<>();
		try {
			Query query = getSession().createSQLQuery("select distinct EVENT_GROUP from NOTIFICATION_EVENTS");
			resultList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;

	}

	public String saveOrUpdate(NotificationEventDto fromDto) throws InvalidInputFault, NoResultFault {
		Session session=null;
		try {
			session=sessionFactory.openSession();
			Transaction tx=session.beginTransaction();
			session.saveOrUpdate(importDto(fromDto));
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (ExecutionFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}
	
	public String deleteEventGroup(String eventGroup) {
		String queryString = "delete from NOTIFICATION_EVENTS where EVENT_GROUP = '" + eventGroup + "'";

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

	public String deleteEvents(String eventName) {
		String queryString = "delete from NOTIFICATION_EVENTS where EVENT_NAME = '" + eventName + "'";

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
