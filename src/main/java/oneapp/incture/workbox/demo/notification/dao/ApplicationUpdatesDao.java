package oneapp.incture.workbox.demo.notification.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//import com.sap.db.jdbc.Transaction;

import oneapp.incture.workbox.demo.notification.entity.ApplicationUpdatesDo;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.dto.ApplicationUpdatesDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationResponseDto;
import oneapp.incture.workbox.demo.notification.dto.PushNotificationDto;
import oneapp.incture.workbox.demo.notification.util.NotificationConstant;
import oneapp.incture.workbox.demo.notification.util.TimeZoneConvertion;

@Repository("ApplicationUpdatesDao")
public class ApplicationUpdatesDao extends BaseDao<ApplicationUpdatesDo, ApplicationUpdatesDto> {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	/**
	 * Generating query condition for getting userId based on actionType and
	 * channel
	 * 
	 * @param actionType
	 * @return
	 */

	public String getUserIdQueryFrequency(String frequency) {

		String userQuery = "";
		String date = "";
		DateFormat dateFormat = new SimpleDateFormat("ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		int seconds = Integer.parseInt(dateFormat.format(new Date()).toString());

		switch (frequency) {
		case "Never":
			userQuery = userQuery + " and usd.value IN ('" + frequency + "') and unc.id NOT IN('Admin')";
			break;

		case "Every hour":

			if (seconds >= 0 && seconds <= 30) {
				dateFormat = new SimpleDateFormat("0000-00-00-000-00-mm-00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + " and usd.value IN ('" + frequency + "')" + " and usd.more IN ('" + date + "')"
					+ " and unc.id NOT IN('Admin')";
			break;
		case "Every month":

			if (seconds >= 0 && seconds <= 30) {
				dateFormat = new SimpleDateFormat("0000-00-dd-000-hh-mm-00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + " and usd.value IN ('" + frequency + "')" + " and usd.more IN ('" + date + "')"
					+ " and unc.id NOT IN('Admin')";
			break;
		case "Every week":

			if (seconds >= 0 && seconds <= 30) {
				dateFormat = new SimpleDateFormat("0000-00-00-EE-hh-mm-00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + " and usd.value IN ('" + frequency + "')" + " and usd.more IN ('" + date + "')"
					+ " and unc.id NOT IN('Admin')";
			break;
		case "Every year":

			if (seconds >= 0 && seconds <= 45) {
				dateFormat = new SimpleDateFormat("0000-MM-dd-000-hh-mm-00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + " and usd.value IN ('" + frequency + "')" + " and usd.more IN ('" + date + "')"
					+ " and unc.id NOT IN('Admin')";
			break;
		case "Custom":

			if (seconds >= 0 && seconds <= 30) {
				dateFormat = new SimpleDateFormat("hh:mm:00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + " and usd.value IN ('" + frequency + "')" + " and usd.more IN ('" + date + "')"
					+ " and unc.id NOT IN('Admin')";
			break;
		case "Every day":

			if (seconds >= 0 && seconds <= 30) {
				dateFormat = new SimpleDateFormat("hh:mm:00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + " and usd.value IN ('" + frequency + "')" + " and usd.more IN ('" + date + "')"
					+ " and unc.id NOT IN('Admin')";
			break;
		default:

			break;
		}
		return userQuery;
	}

	/**
	 * Generating query condition for different action types
	 * 
	 * @param actionType
	 * @param channel
	 * @param userId
	 * @return
	 */
	public String getActionQuery(String actionType, String channel, String userId, String frequency) {
		String actionQuery = "";
		switch (actionType) {
		case NotificationConstant.ADMIN_SETTING_CHANGES:
			if (userId != null)
				actionQuery = " ( au.action_type In('Admin Setting Changes') )" + "and ne.EVENT_NAME IN ('" + actionType
						+ "')  and unc.CHANNEL IN ('" + channel + "') " + getUserIdQueryFrequency(frequency);
			else
				actionQuery = " ( au.action_type In('Admin Setting Changes') )" + "and ne.EVENT_NAME IN ('" + actionType
						+ "')  and unc.CHANNEL IN ('" + channel + "') " + getUserIdQueryFrequency(frequency);
			break;
		case NotificationConstant.SYSTEM_UPDATE:
			if (userId != null)
				actionQuery = " ( au.action_type In('System Updates') )" + "and ne.EVENT_NAME IN ('" + actionType
						+ "')  and unc.CHANNEL IN ('" + channel + "') " + getUserIdQueryFrequency(frequency);
			else
				actionQuery = " ( au.action_type In('System Updates') )" + "and ne.EVENT_NAME IN ('" + actionType
						+ "')  and unc.CHANNEL IN ('" + channel + "') " + getUserIdQueryFrequency(frequency);
			break;
		case NotificationConstant.USER_CONFIGURATION_UPDATES:
			if (userId != null)
				actionQuery = " ( au.action_type In('User Configuration Updates') )" + "and ne.EVENT_NAME IN ('"
						+ actionType + "')  and unc.CHANNEL IN ('" + channel + "') "
						+ getUserIdQueryFrequency(frequency);
			else
				actionQuery = " ( au.action_type In('User Configuration Updates') )" + "and ne.EVENT_NAME IN ('"
						+ actionType + "')  and unc.CHANNEL IN ('" + channel + "') "
						+ getUserIdQueryFrequency(frequency);
			break;
		default:

			break;
		}
		return actionQuery;
	}

	/**
	 * Generating time range query condition for actions
	 * 
	 * @param timeRange
	 * @return
	 */
	public String getTimeRangeQueryForAction(Integer timeRange) {
		String timeRangeQuery = "";
		if (timeRange != null)
			timeRangeQuery = timeRangeQuery + " au.DATE_OF_RELEASE > Add_seconds(CURRENT_TIMESTAMP, - ( " + timeRange
					+ " ))";
		return timeRangeQuery;
	}

	/**
	 * Fetching updated action for Web Notification
	 * 
	 * @param actionTypeList
	 * @param userId
	 * @param timeRange
	 * @return
	 */
	public List<NotificationDto> getWebNotificationDataForApplicationUpdates(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<NotificationDto> notificationDtos = new ArrayList<>();
		TimeZoneConvertion timeZoneConvertion = new TimeZoneConvertion();

		String fetchStr = "select distinct au.project_code,au.project_name,au.version_number,au.date_of_release,au.version_details,"
				+ "au.roles, au.description, au.organisation, au.action_type, unc.id, au.id as update_id from APPLICATION_UPDATES au "
				+ "inner join USER_NOTIFICATION_CONFIG unc on unc.id NOT IN('Admin') inner join NOTIFICATION_EVENTS ne "
				+ "on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name "
				+ "inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForAction(timeRange);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Never");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
								userId, "Never");
				} else {
					fetchStr = fetchStr + " OR "
							+ getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Never");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(60);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every hour");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
								userId, "Every hour");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
							userId, "Every hour");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(2592000);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every month");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
								userId, "Every month");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
							userId, "Every month");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every week");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
								userId, "Every week");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
							userId, "Every week");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(86400);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every day");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
								userId, "Every day");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
							userId, "Every day");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(31536000);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every year");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
								userId, "Every year");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
							userId, "Every year");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Custom");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB,
								userId, "Custom");
				} else {
					fetchStr = fetchStr + " OR "
							+ getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Custom");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ")) order by au.project_code";

		System.err.println("[WBP-Dev][getWebNotificationDataForApplication]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {

				NotificationDto notificationDto = new NotificationDto();
				String notificationId = UUID.randomUUID().toString().replaceAll("-", "");

				String actionType = obj[8] == null ? null : (String) obj[8];
				notificationDto.setDescription(obj[6] == null ? null : (String) obj[6]);
				notificationDto.setUserId(obj[9] == null ? null : (String) obj[9]);
				notificationDto.setUserName("Admin");

				notificationDto.setTitle(
						"Application Update" + " :  Version Number : " + (obj[2] == null ? null : (String) obj[2]));
				notificationDto.setId(obj[10] == null ? null : (String) obj[10]);
				notificationDto.setOrigin("Application Updates");
				notificationDto.setEventName("Version Details : " + (obj[4] == null ? null : (String) obj[4])
						+ ", Date of Release : " + (obj[3] == null ? null : (Date) obj[3]));

				notificationDto
						.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
				notificationDto.setNotificationId(notificationId);
				notificationDto.setNotificationType(actionType);
				notificationDto.setPriority(NotificationConstant.HIGH);
				notificationDto.setStatus(true);

				notificationDtos.add(notificationDto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getWebNotificationDataForAction] Error : " + e.getMessage());
		}
		return notificationDtos;
	}

	public List<NotificationResponseDto> getMailNotificationDataForApplication(List<String> actionTypeList,
			String userId, Integer timeRange) {
		List<NotificationResponseDto> notificationDtos = new ArrayList<>();
		String fetchStr = "select distinct au.project_code,au.project_name,au.version_number,au.date_of_release,au.version_details,"
				+ "au.roles, au.description, au.organisation, au.action_type, unc.id, au.id as update_id, uim.user_email from APPLICATION_UPDATES au "
				+ "inner join USER_NOTIFICATION_CONFIG unc on unc.id NOT IN('Admin') inner join NOTIFICATION_EVENTS ne "
				+ "on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name "
				+ "inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id "
				+ "inner join user_idp_mapping uim ON uim.user_id = unc.id where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForAction(timeRange);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Never");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_EMAIL,
								userId, "Never");
				} else {
					fetchStr = fetchStr + " OR "
							+ getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Never");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(60);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every hour");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_EMAIL,
								userId, "Every hour");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every hour");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(2592000);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every month");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_EMAIL,
								userId, "Every month");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every month");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every week");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_EMAIL,
								userId, "Every week");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every week");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(86400);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every day");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_EMAIL,
								userId, "Every day");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every day");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(31536000);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every year");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_EMAIL,
								userId, "Every year");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every year");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Custom");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_EMAIL,
								userId, "Custom");
				} else {
					fetchStr = fetchStr + " OR "
							+ getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Custom");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ")) order by au.project_code";

		System.err.println("[WBP-Dev][getMailNotificationDataForApplication]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {

				NotificationResponseDto notificationDto = new NotificationResponseDto();

				String actionType = obj[8] == null ? null : (String) obj[8];
				notificationDto.setDescription("Version Number : " + (obj[2] == null ? null : (String) obj[2])
						+ ", Version Details : " + (obj[4] == null ? null : (String) obj[4]) + ", Date of Release : "
						+ (obj[3] == null ? null : (Date) obj[3]) + ", Role : "
						+ (obj[5] == null ? "Everyone" : (String) obj[5]));
				notificationDto.setUserId(obj[11] == null ? null : (String) obj[11]);
				notificationDto.setUserName("Admin");

				notificationDto.setTitle("Application Update" + " : " + (obj[6] == null ? null : (String) obj[6])
						+ ", Version Number : " + (obj[2] == null ? null : (String) obj[2]));
				notificationDto.setOrigin("Application Updates");

				notificationDto.setNotificationType(actionType);
				notificationDto.setPriority(NotificationConstant.HIGH);

				notificationDtos.add(notificationDto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getMailNotificationDataForApplication] Error : " + e.getMessage());
		}

		return notificationDtos;
	}

	public List<PushNotificationDto> getPushNotificationDataForApplication(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<PushNotificationDto> pushNotificationDtos = new ArrayList<>();
		String fetchStr = "select distinct au.version_number, unc.id, from APPLICATION_UPDATES au "
				+ "inner join USER_NOTIFICATION_CONFIG unc on unc.id NOT IN('Admin') inner join NOTIFICATION_EVENTS ne "
				+ "on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name "
				+ "inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForAction(timeRange);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Never");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_PUSH,
								userId, "Never");
				} else {
					fetchStr = fetchStr + " OR "
							+ getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Never");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(60);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every hour");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_PUSH,
								userId, "Every hour");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every hour");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(2592000);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every month");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_PUSH,
								userId, "Every month");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every month");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every week");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_PUSH,
								userId, "Every week");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every week");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(86400);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every day");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_PUSH,
								userId, "Every day");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every day");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(31536000);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every year");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_PUSH,
								userId, "Every year");
				} else {
					fetchStr = fetchStr + " OR " + getActionQuery(action,
							NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every year");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ") )";
		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;

		for (String action : actionTypeList) {
			if (NotificationConstant.ADMIN_SETTING_CHANGES.equals(action)
					|| NotificationConstant.SYSTEM_UPDATE.equals(action)
					|| NotificationConstant.USER_CONFIGURATION_UPDATES.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And (" + getActionQuery(action,
								NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Custom");
					else
						fetchStr = fetchStr + getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_PUSH,
								userId, "Custom");
				} else {
					fetchStr = fetchStr + " OR "
							+ getActionQuery(action, NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Custom");
				}
				count = ++count;
			}
		}

		fetchStr = fetchStr + ")) order by au.project_code";

		System.err.println("[WBP-Dev][getMailNotificationDataForApplication]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				PushNotificationDto notificationDto = new PushNotificationDto();
				List<String> userList = new ArrayList<>();
				notificationDto.setData(
						"Application Update : , Version Number : " + (obj[0] == null ? null : (String) obj[0]));
				userList.add(obj[1] == null ? null : (String) obj[1]);
				notificationDto.setUsers(userList);
				notificationDto.setAlert("Application Updates");

				pushNotificationDtos.add(notificationDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getMailNotificationDataForNew] Error : " + e.getMessage());
		}

		return pushNotificationDtos;
	}

	@Override
	protected ApplicationUpdatesDto exportDto(ApplicationUpdatesDo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public String saveOrUpdate(ApplicationUpdatesDto fromDto) throws InvalidInputFault, NoResultFault {
		try {
			saveOrUpdate(importDto(fromDto));
			return PMCConstant.SUCCESS;
		} catch (ExecutionFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}

	@Override
	protected ApplicationUpdatesDo importDto(ApplicationUpdatesDto fromDto) {

		ApplicationUpdatesDo entity = new ApplicationUpdatesDo();
		if (!ServicesUtil.isEmpty(fromDto.getId()))
			entity.setId(fromDto.getId());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			entity.setDescription(fromDto.getDescription());
		if (!ServicesUtil.isEmpty(fromDto.getOrganisation()))
			entity.setOrganisation(fromDto.getOrganisation());
		if (!ServicesUtil.isEmpty(fromDto.getVersionNumber()))
			entity.setVersionNumber(fromDto.getVersionNumber());
		if (!ServicesUtil.isEmpty(fromDto.getVersionDetails()))
			entity.setVersionDetails(fromDto.getVersionDetails());
		if (!ServicesUtil.isEmpty(fromDto.getRoles()))
			entity.setRoles(fromDto.getRoles());
		if (!ServicesUtil.isEmpty(fromDto.getDateOfRelease()))
			entity.setDateOfRelease(fromDto.getDateOfRelease());
		if (!ServicesUtil.isEmpty(fromDto.getProjectCode()))
			entity.setProjectCode(fromDto.getProjectCode());
		if (!ServicesUtil.isEmpty(fromDto.getProjectName()))
			entity.setProjectName(fromDto.getProjectName());
		if (!ServicesUtil.isEmpty(fromDto.getActionType()))
			entity.setActionType(fromDto.getActionType());
		return entity;
	}

}
