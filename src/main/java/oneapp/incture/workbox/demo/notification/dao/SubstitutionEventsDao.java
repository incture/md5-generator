package oneapp.incture.workbox.demo.notification.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationResponseDto;
import oneapp.incture.workbox.demo.notification.dto.PushNotificationDto;
import oneapp.incture.workbox.demo.notification.util.NotificationConstant;
import oneapp.incture.workbox.demo.notification.util.TimeZoneConvertion;

@Repository("SubstitutionEventsDao")
public class SubstitutionEventsDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}



	public String getUserIdQueryFrequency(String actionType, String channel, String frequency) {

		String userQuery = "";
		String date = "";
		DateFormat dateFormat = new SimpleDateFormat("ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		int seconds = Integer.parseInt(dateFormat.format(new Date()).toString());

		/*
		 * dateFormat = new SimpleDateFormat("dd");
		 * dateFormat.setTimeZone(TimeZone.getTimeZone("IST")); int currentDate
		 * = Integer.parseInt(dateFormat.format(new Date()).toString());
		 * 
		 * dateFormat = new SimpleDateFormat("EEEE");
		 * dateFormat.setTimeZone(TimeZone.getTimeZone("IST")); String week =
		 * dateFormat.format(new Date()).toString();
		 * 
		 * dateFormat = new SimpleDateFormat("MMMM");
		 * dateFormat.setTimeZone(TimeZone.getTimeZone("IST")); String month =
		 * dateFormat.format(new Date()).toString();
		 * 
		 * dateFormat = new SimpleDateFormat("hh");
		 * dateFormat.setTimeZone(TimeZone.getTimeZone("IST")); int hours =
		 * Integer.parseInt(dateFormat.format(new Date()).toString());
		 * 
		 * dateFormat = new SimpleDateFormat("mm");
		 * dateFormat.setTimeZone(TimeZone.getTimeZone("IST")); int minutes =
		 * Integer.parseInt(dateFormat.format(new Date()).toString());
		 */

		switch (frequency) {
		case "Never":
			userQuery = userQuery + "select unc.id from USER_NOTIFICATION_CONFIG unc inner join NOTIFICATION_EVENTS "
					+ "ne on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name"
					+ " inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id where ne.EVENT_NAME IN "
					+ "('" + actionType + "') " + " and unc.CHANNEL IN ('" + channel + "') and" + " usd.value IN ('"
					+ frequency + "')" + "and unc.id NOT IN('Admin') and ne.id='1'";
			break;
		case "Every hour":

			if (seconds >= 0 && seconds <= 45) {
				dateFormat = new SimpleDateFormat("0000-00-00-000-00-mm-00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + "select unc.id from USER_NOTIFICATION_CONFIG unc inner join NOTIFICATION_EVENTS "
					+ "ne on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name"
					+ " inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id where ne.EVENT_NAME IN "
					+ "('" + actionType + "') " + " and unc.CHANNEL IN ('" + channel + "') and" + " usd.value IN ('"
					+ frequency + "') and" + " usd.more IN ('" + date + "')"
					+ "and unc.id NOT IN('Admin') and ne.id='1'";
			break;
		case "Every month":

			if (seconds >= 0 && seconds <= 45) {
				dateFormat = new SimpleDateFormat("0000-00-dd-000-hh-mm-00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			//date = "0000-00-22-000-10-30-00 AM";
			userQuery = userQuery + "select unc.id from USER_NOTIFICATION_CONFIG unc inner join NOTIFICATION_EVENTS "
					+ "ne on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name"
					+ " inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id where ne.EVENT_NAME IN "
					+ "('" + actionType + "') " + " and unc.CHANNEL IN ('" + channel + "') and" + " usd.value IN ('"
					+ frequency + "') and" + " usd.more IN ('" + date + "')"
					+ "and unc.id NOT IN('Admin') and ne.id='1'";
			break;
		case "Every week":

			if (seconds >= 0 && seconds <= 45) {
				dateFormat = new SimpleDateFormat("0000-00-00-EE-hh-mm-00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + "select unc.id from USER_NOTIFICATION_CONFIG unc inner join NOTIFICATION_EVENTS "
					+ "ne on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name"
					+ " inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id where ne.EVENT_NAME IN "
					+ "('" + actionType + "') " + " and unc.CHANNEL IN ('" + channel + "') and" + " usd.value IN ('"
					+ frequency + "') and" + " usd.more IN ('" + date + "')"
					+ "and unc.id NOT IN('Admin') and ne.id='1'";
			break;
		case "Every year":

			if (seconds >= 0 && seconds <= 45) {
				dateFormat = new SimpleDateFormat("0000-MM-dd-000-hh-mm-00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + "select unc.id from USER_NOTIFICATION_CONFIG unc inner join NOTIFICATION_EVENTS "
					+ "ne on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name"
					+ " inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id where ne.EVENT_NAME IN "
					+ "('" + actionType + "') " + " and unc.CHANNEL IN ('" + channel + "') and" + " usd.value IN ('"
					+ frequency + "') and" + " usd.more IN ('" + date + "')"
					+ "and unc.id NOT IN('Admin') and ne.id='1'";
			break;
		case "Custom":

			if (seconds >= 0 && seconds <= 45) {
				dateFormat = new SimpleDateFormat("hh:mm:00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + "select unc.id from USER_NOTIFICATION_CONFIG unc inner join NOTIFICATION_EVENTS "
					+ "ne on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name"
					+ " inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id where ne.EVENT_NAME IN "
					+ "('" + actionType + "') " + " and unc.CHANNEL IN ('" + channel + "') and" + " usd.value IN ('"
					+ frequency + "') and" + " usd.more IN ('" + date + "')"
					+ "and unc.id NOT IN('Admin') and ne.id='1'";
			break;
		case "Every day":

			if (seconds >= 0 && seconds <= 45) {
				dateFormat = new SimpleDateFormat("0000-00-00-000-hh-mm-00 aa");
				dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
				date = dateFormat.format(new Date()).toString();
			}

			userQuery = userQuery + "select unc.id from USER_NOTIFICATION_CONFIG unc inner join NOTIFICATION_EVENTS "
					+ "ne on unc.event_id=ne.event_id inner join VIEW_SETTING vs on unc.id= vs.user_id and ne.EVENT_GROUP = vs.view_name"
					+ " inner join USER_SETTING_DETAILS usd on  vs.settings = usd.profile_setting_id where ne.EVENT_NAME IN "
					+ "('" + actionType + "') " + " and unc.CHANNEL IN ('" + channel + "') and" + " usd.value IN ('"
					+ frequency + "') and" + " usd.more IN ('" + date + "')"
					+ "and unc.id NOT IN('Admin') and ne.id='1'";
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
		case NotificationConstant.FORWARD:
			if (userId != null)
				actionQuery = " ( ta.action_type In('Forward') and " + " ta.send_to_user IN ('" + userId + "'))";
			else
				actionQuery = " ( ta.action_type In('Forward') and " + " ta.send_to_user IN ("
						+ getUserIdQueryFrequency(actionType, channel, frequency) + "))";
			break;
		case NotificationConstant.APPROVE:
			if (userId != null)
				actionQuery = " ( ta.action_type In('Approved' , 'Approve' , 'Done','Resolved') AND ta.event_id IN( "
						+ "select te1.event_id from task_events te1 INNER JOIN process_events Pe1 ON Pe1.process_id = te1.process_id where Pe1.STARTED_BY IS NOT NULL )"
						+ " and  Pe.STARTED_BY IN ('" + userId + "') " + " ) ";
			else
				actionQuery = " ( ta.action_type In('Approved' , 'Approve', 'Done','Resolved') AND ta.event_id IN( "
						+ "select te1.event_id from task_events te1 INNER JOIN process_events Pe1 ON Pe1.process_id = te1.process_id where Pe1.STARTED_BY IS NOT NULL )"
						+ " and  Pe.STARTED_BY IN (" + getUserIdQueryFrequency(actionType, channel, frequency) + ") "
						+ " ) ";
			break;
		case NotificationConstant.REJECT:
			if (userId != null)
				actionQuery = " ( ta.action_type In('Rejected' , 'Reject') AND ta.event_id IN( "
						+ "select te1.event_id from task_events te1 INNER JOIN process_events Pe1 ON Pe1.process_id = te1.process_id where Pe1.STARTED_BY IS NOT NULL )"
						+ " and  Pe.STARTED_BY IN ('" + userId + "') " + " ) ";
			else
				actionQuery = " ( ta.action_type In('Rejected' , 'Reject') AND ta.event_id IN( "
						+ "select te1.event_id from task_events te1 INNER JOIN process_events Pe1 ON Pe1.process_id = te1.process_id where Pe1.STARTED_BY IS NOT NULL )"
						+ " and  Pe.STARTED_BY IN (" + getUserIdQueryFrequency(actionType, channel, frequency) + ") "
						+ " ) ";
			break;
		case NotificationConstant.NEW_TASK:
			if (userId != null)
				actionQuery = " tw.task_owner IN ('" + userId + "') ";
			else
				actionQuery = " tw.task_owner IN (" + getUserIdQueryFrequency(actionType, channel, frequency) + ") ";
			break;

		case NotificationConstant.SUBSTITUTION:
			if (userId != null)
				actionQuery = "sr.substituting_user IN ('" + userId + "')";
			else
				actionQuery = "sr.substituting_user IN (" + getUserIdQueryFrequency(actionType, channel, frequency)
						+ ")";
			break;
			
		case NotificationConstant.ACTIVATION_SUBSTITUTION:
			if (userId != null)
				actionQuery = "do1.substituting_user IN ('" + userId + "')";
			else
				actionQuery = "do1.substituting_user IN (" + getUserIdQueryFrequency(actionType, channel, frequency)
						+ ")";
			break;
			
		case NotificationConstant.SUBSTITUTION_APPROVAL:
			if (userId != null)
				actionQuery = " tw.task_owner IN ('" + userId + "') ";
			else
				actionQuery = " tw.task_owner IN (" + getUserIdQueryFrequency(actionType, channel, frequency) + ") ";
		default:

			break;
		}
		return actionQuery;
	}



	/**
	 * Generating time range query condition for actions substitution
	 * 
	 * @param timeRange
	 * @return
	 */
	public String getTimeRangeQueryForActionSubtitution(Integer timeRange) {
		String timeRangeQuery = "";
		if (timeRange != null)
			timeRangeQuery = timeRangeQuery + " sr.modified_at > Add_seconds(CURRENT_TIMESTAMP, - ( " + timeRange
					+ " ))";
		return timeRangeQuery;
	}
	
	public String getTimeRangeQueryForSubstitutionChange(Integer timeRange) {
		String timeRangeQuery = "";
		if (timeRange != null)
			timeRangeQuery = timeRangeQuery + " do1.modified_at > Add_seconds(CURRENT_TIMESTAMP, - (" + timeRange
					+ " ))";
		return timeRangeQuery;
	}
	
	public String getTimeRangeQueryForNew(Integer timeRange) {
		String timeRangeQuery = "";
		if (timeRange != null)
			timeRangeQuery = timeRangeQuery + " te.created_at > Add_seconds(CURRENT_TIMESTAMP, - ( " + timeRange
					+ " ))";
		return timeRangeQuery;
	}

	
	
	public List<PushNotificationDto> getPushNotificationDataForNewSubstitution(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<PushNotificationDto> pushNotificationDtos = new ArrayList<>();
		String fetchStr = "	select sr.rule_id,sr.substituted_user_name,sr.substituting_user_name,sr.substituting_user,sr.created_by,sr.updated_at,sp.processes,sr.is_active,sr.is_enable,sr.start_date,sr.end_date"+
				" from substitution_rule sr inner join "
					+ "(SELECT rule_id,STRING_AGG(PROCESS, ', ') AS processes FROM substitution_processes group by rule_id)"
				+ " as sp on sp.rule_id = sr.rule_id"
				+ " where ((is_deleted='0') and (is_active='1')) and ( ";

		fetchStr = fetchStr + "( " + getTimeRangeQueryForActionSubtitution(30);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForActionSubtitution(60);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)){

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForActionSubtitution(2592000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForActionSubtitution(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForActionSubtitution(86400);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForActionSubtitution(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForActionSubtitution(timeRange);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + " ) ) )";

		System.err.println("[WBP-Dev][getPushNotificationDataForSubstitution]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				PushNotificationDto notificationDto = new PushNotificationDto();
				List<String> userList = new ArrayList<>();
				String actionType = NotificationConstant.SUBSTITUTION;

				notificationDto.setData("Substituted by "+ (obj[2]==null?null:(String)obj[2]) + " From : " +(obj[9] == null ? null
						: ServicesUtil.convertFromDateToStringWithoutT(ServicesUtil.resultAsDate(obj[9])))
						+ ", To : " + (obj[10] == null ? null
								: ServicesUtil.convertFromDateToStringWithoutT(ServicesUtil.resultAsDate(obj[10])))
								+ ", Processes : " + (obj[6] == null ? null
										: (String) obj[6]));
				notificationDto.setAlert(NotificationConstant.SUBSTITUTION);
				userList.add(obj[3] == null ? null : (String) obj[3]);
				notificationDto.setUsers(userList);
				pushNotificationDtos.add(notificationDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getPushNotificationDataForNewSubstitution] Error : " + e.getMessage());
		}
		return pushNotificationDtos;
	}

	
	
	public List<NotificationResponseDto> getMailNotificationDataForNewSubstitution(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<NotificationResponseDto> notificationDtos = new ArrayList<>();
		String fetchStr = "select sr.rule_id,sr.substituted_user_name,sr.substituting_user_name,sr.substituting_user,sr.created_by,sr.updated_at,sp.processes,sr.is_active,sr.is_enable,sr.start_date,sr.end_date,uim.user_email"+
				" from substitution_rule sr inner join "
					+ "(SELECT rule_id,STRING_AGG(PROCESS, ', ') AS processes FROM substitution_processes group by rule_id)"
				+ " as sp on sp.rule_id = sr.rule_id inner join user_idp_mapping uim on sr.substituting_user = uim.user_id"
				+ " where ((is_deleted='0') and (is_active='1')) and ( ";

		fetchStr = fetchStr + "( " + getTimeRangeQueryForActionSubtitution(30);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(60);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)){
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(2592000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {
				if (count == 0) {
					
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(86400);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + ") ) )";

		System.err.println("[WBP-Dev][getMailNotificationDataForNewSubstitution]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				NotificationResponseDto notificationDto = new NotificationResponseDto();
				String actionType = NotificationConstant.SUBSTITUTION;

				notificationDto.setDescription("Substituted by " + (obj[1]==null?null:(String)obj[1])+" From : "+(obj[9] == null ? null
						: ServicesUtil.convertFromDateToStringWithoutT(ServicesUtil.resultAsDate(obj[9])))
						+ ", To : " + (obj[10] == null ? null
								: ServicesUtil.convertFromDateToStringWithoutT(ServicesUtil.resultAsDate(obj[10])))
								+ ", Processes : " + (obj[6] == null ? null
										: (String) obj[6]));
				

				notificationDto.setUserId(obj[11] == null ? null : (String) obj[11]);

				notificationDto.setTitle(NotificationConstant.SUBSTITUTION);

				notificationDtos.add(notificationDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getMailNotificationDataForNewSubstitution] Error : " + e.getMessage());
		}
		return notificationDtos;
	}



	public List<NotificationDto> getWebNotificationDataForNewSubstitution(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<NotificationDto> notificationDtos = new ArrayList<>();
		TimeZoneConvertion timeZoneConvertion = new TimeZoneConvertion();

		String fetchStr = "	select sr.rule_id,sr.substituted_user_name,sr.substituting_user_name,sr.substituting_user,sr.created_by,sr.updated_at,sp.processes,sr.is_active,sr.is_enable,sr.start_date,sr.end_date"
				+ " from substitution_rule sr inner join "
				+ "(SELECT rule_id,STRING_AGG(PROCESS, ', ') AS processes FROM substitution_processes group by rule_id)"
				+ " as sp on sp.rule_id = sr.rule_id" + " where ((is_deleted='0') and (is_active='1')) and ( ";
		fetchStr = fetchStr + " ( " + getTimeRangeQueryForActionSubtitution(30);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(60);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(2592000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + "))";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + "))";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(86400);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + "))";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + "))";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForActionSubtitution(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + ")))";

		System.err.println("[WBP-Dev][getWebNotificationDataForNewSubstitution      Query]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {

					NotificationDto notificationDto = new NotificationDto();
					String notificationId = UUID.randomUUID().toString().replaceAll("-", "");

					notificationDto.setDescription("Substituted From : " +(obj[9] == null ? null
									: ServicesUtil.convertFromDateToStringWithoutT(ServicesUtil.resultAsDate(obj[9])))
									+ ", To : " + (obj[10] == null ? null
											: ServicesUtil.convertFromDateToStringWithoutT(ServicesUtil.resultAsDate(obj[10])))
											+ ", Processes : " + (obj[6] == null ? null
													: (String) obj[6]));
					
					notificationDto.setUserId(obj[3] == null ? null : (String) obj[3]);
					notificationDto.setUserName(obj[1] == null ? null : (String) obj[1]);

					notificationDto.setTitle(NotificationConstant.SUBSTITUTION);
					notificationDto.setId(obj[0] == null ? null : (String) obj[0]);
					notificationDto.setOrigin("Task");
					notificationDto.setEventName("New Substitution");

					notificationDto
							.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
					notificationDto.setNotificationId(notificationId);
					notificationDto.setNotificationType(NotificationConstant.SUBSTITUTION);
					notificationDto.setPriority(NotificationConstant.HIGH);
					notificationDto.setStatus(true);

					notificationDtos.add(notificationDto);
				}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getWebNotificationDataForNewSubstitution] Error : " + e.getMessage());
		}
		return notificationDtos;
	}
	
	public List<PushNotificationDto> getPushNotiForSubstitutionActivation(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<PushNotificationDto> pushNotificationDtos = new ArrayList<>();
		String fetchStr = " select do1.rule_id,do1.substituted_user_name,do1.substituting_user_name,do1.substituting_user,"
				+ "do1.created_by,do1.updated_at,sp.processes,do1.is_active,do1.is_enable,do1.start_date,do1.end_date from Substitution_Rule do1 inner join "
				+ "(select max(version) as version,rule_Id from Substitution_Rule group by rule_Id) do2 "
				+ "on do2.rule_id=do1.rule_id and do2.version=do1.version inner join "
					+ "(SELECT rule_id,STRING_AGG(PROCESS, ', ') AS processes FROM substitution_processes group by rule_id)"
				+ " as sp on sp.rule_id = do1.rule_id "
				+ "where ((do1.is_deleted='0') and (do1.is_active='1')) and (";

		fetchStr = fetchStr + "( " + getTimeRangeQueryForSubstitutionChange(30);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSubstitutionChange(60);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)){

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSubstitutionChange(2592000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSubstitutionChange(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSubstitutionChange(86400);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSubstitutionChange(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSubstitutionChange(timeRange);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And  (" + getActionQuery(action,
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

		fetchStr = fetchStr + " ) ) )";

		System.err.println("[WBP-Dev][getPushNotificationDataForSubstitution]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				PushNotificationDto notificationDto = new PushNotificationDto();
				List<String> userList = new ArrayList<>();

				notificationDto.setData("Substituted Processes : " + (obj[6] == null ? null
										: (String) obj[6]) + " is Activated");
				notificationDto.setAlert(NotificationConstant.ACTIVATION_SUBSTITUTION);
				userList.add(obj[3] == null ? null : (String) obj[3]);
				notificationDto.setUsers(userList);
				pushNotificationDtos.add(notificationDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getPushNotificationDataForNewSubstitution] Error : " + e.getMessage());
		}
		return pushNotificationDtos;
	}
	
	public List<NotificationResponseDto> getMailNotiForSubstitutionActivation(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<NotificationResponseDto> notificationDtos = new ArrayList<>();
		String fetchStr = "	select do1.rule_id,do1.substituted_user_name,do1.substituting_user_name,do1.substituting_user,do1.created_by,do1.updated_at,sp.processes,do1.is_active,do1.is_enable,do1.start_date,do1.end_date,uim.user_email"+
				" from substitution_rule do1 inner join "
					+ "(SELECT rule_id,STRING_AGG(PROCESS, ', ') AS processes FROM substitution_processes group by rule_id)"
				+ " as sp on sp.rule_id = do1.rule_id inner join user_idp_mapping uim on do1.substituting_user = uim.user_id inner join "
				+ "(select max(version) as version,rule_Id from Substitution_Rule group by rule_Id) do2 "
				+ "on do2.rule_id=do1.rule_id and do2.version=do1.version "
				+ " where ((is_deleted='0') and (is_active='1')) and ( ";

		fetchStr = fetchStr + "( " + getTimeRangeQueryForSubstitutionChange(30);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(60);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)){
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(2592000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(86400);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {
				if (count == 0) {
					if (timeRange != null)
						fetchStr = fetchStr + " And ( " + getActionQuery(action,
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

		fetchStr = fetchStr + ") ) )";

		System.err.println("[WBP-Dev][getMailNotificationDataForNewSubstitution]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				NotificationResponseDto notificationDto = new NotificationResponseDto();
				String actionType = NotificationConstant.ACTIVATION_SUBSTITUTION;

				notificationDto.setDescription("Substituted Processes : " + (obj[6] == null ? null
										: (String) obj[6]) + " is Activated");
				

				notificationDto.setUserId(obj[11] == null ? null : (String) obj[11]);

				notificationDto.setTitle(NotificationConstant.ACTIVATION_SUBSTITUTION);

				notificationDtos.add(notificationDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getMailNotificationDataForNewSubstitution] Error : " + e.getMessage());
		}
		return notificationDtos;
	}
	
	public List<NotificationDto> getWebNotiForSubstitutionActivation(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<NotificationDto> notificationDtos = new ArrayList<>();
		TimeZoneConvertion timeZoneConvertion = new TimeZoneConvertion();

		String fetchStr = " select do1.rule_id,do1.substituted_user_name,do1.substituting_user_name,do1.substituting_user,"
				+ "do1.created_by,do1.updated_at,sp.processes,do1.is_active,do1.is_enable,do1.start_date,do1.end_date from Substitution_Rule do1 inner join "
				+ "(select max(version) as version,rule_Id from Substitution_Rule group by rule_Id) do2 "
				+ "on do2.rule_id=do1.rule_id and do2.version=do1.version inner join "
				+ "(SELECT rule_id,STRING_AGG(PROCESS, ', ') AS processes FROM substitution_processes group by rule_id)"
				+ " as sp on sp.rule_id = do1.rule_id "
				+ "where ((do1.is_deleted='0') and (do1.is_active='1')) and (";
		fetchStr = fetchStr + " ( " + getTimeRangeQueryForSubstitutionChange(30);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(60);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(2592000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + "))";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + "))";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(86400);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + "))";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + "))";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSubstitutionChange(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.ACTIVATION_SUBSTITUTION.equals(action)) {

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

		fetchStr = fetchStr + ")))";

		System.err.println("[WBP-Dev][getWebNotificationDataForNewSubstitution]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {

					NotificationDto notificationDto = new NotificationDto();
					String notificationId = UUID.randomUUID().toString().replaceAll("-", "");

					notificationDto.setDescription("Substituted From : " +(obj[9] == null ? null
							: ServicesUtil.convertFromDateToStringWithoutT(ServicesUtil.resultAsDate(obj[9])))
							+ ", To : " + (obj[10] == null ? null
									: ServicesUtil.convertFromDateToStringWithoutT(ServicesUtil.resultAsDate(obj[10]))));
				
					notificationDto.setUserId(obj[3] == null ? null : (String) obj[3]);
					notificationDto.setUserName(obj[1] == null ? null : (String) obj[1]);

					notificationDto.setTitle("Substition Activated for " + (obj[6] == null ? null
							: (String) obj[6]) +"");
					notificationDto.setId(obj[0] == null ? null : (String) obj[0]);
					notificationDto.setOrigin("Task");
					notificationDto.setEventName("Substitution Activation");

					notificationDto
							.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
					notificationDto.setNotificationId(notificationId);
					notificationDto.setNotificationType(NotificationConstant.ACTIVATION_SUBSTITUTION);
					notificationDto.setPriority(NotificationConstant.HIGH);
					notificationDto.setStatus(true);

					notificationDtos.add(notificationDto);
				}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getWebNotificationDataForNewSubstitution] Error : " + e.getMessage());
		}
		return notificationDtos;
	}
	
	public List<NotificationDto> getWebNotiForSubstitutionApproval(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<NotificationDto> notificationDtos = new ArrayList<>();
		TimeZoneConvertion timeZoneConvertion = new TimeZoneConvertion();

		String fetchStr = "select distinct te.event_id,tw.task_owner,tw.task_owner_disp,te.subject,te.proc_name,pe.request_id,te.name"
				+ " from task_events te INNER JOIN task_owners tw" + " ON tw.event_id = te.event_id"
				+ " INNER JOIN process_events Pe ON Pe.process_id = te.process_id" + " where te.PROC_NAME = 'SubstitutionProcessApproval' and ";
		
		fetchStr = fetchStr + " ( " + getTimeRangeQueryForNew(30);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Never");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Never");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(60);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every hour");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every hour");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(2592000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every month");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every month");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every week");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every week");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(86400);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every day");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every day");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(31536000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every year");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every year");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(timeRange);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Custom");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Custom");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " order by te.event_id";
		
		System.err.println("[WBP-Dev][getWebNotificationDataForSubstitutionApproval]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {

					NotificationDto notificationDto = new NotificationDto();
					String notificationId = UUID.randomUUID().toString().replaceAll("-", "");

					notificationDto.setDescription("Substitution Approval Required");
				
					notificationDto.setUserId(obj[1] == null ? null : (String) obj[1]);
					notificationDto.setUserName(obj[2] == null ? null : (String) obj[2]);

					notificationDto.setTitle(
							(obj[4] == null ? null : (String) obj[4]) + " : " + (obj[5] == null ? null : (String) obj[5]));
					notificationDto.setId(obj[0] == null ? null : (String) obj[0]);
					notificationDto.setOrigin("Substitution");
					notificationDto.setEventName(NotificationConstant.SUBSTITUTION_APPROVAL);

					notificationDto
							.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
					notificationDto.setNotificationId(notificationId);
					notificationDto.setNotificationType(NotificationConstant.SUBSTITUTION_APPROVAL);
					notificationDto.setPriority(NotificationConstant.HIGH);
					notificationDto.setStatus(true);

					notificationDtos.add(notificationDto);
				}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getWebNotificationDataForSubstitutionApproval] Error : " + e.getMessage());
		}
		return notificationDtos;
	}

	public List<NotificationResponseDto> getMailNotiForSubstitutionApproval(String userId, Integer timeRange) {
		Map<String, NotificationResponseDto> notiMap = new HashMap<>();
		List<NotificationResponseDto> notificationDtos = new ArrayList<>();
		String fetchStr = "select distinct te.event_id,uim.user_email,te.subject,pct.process_display_name,pe.request_id"
				+ " from task_events te INNER JOIN task_owners tw" + " ON tw.event_id = te.event_id"
				+ " INNER JOIN process_events Pe ON Pe.process_id = te.process_id"
				+ " INNER JOIN user_idp_mapping uim ON uim.user_id = tw.task_owner"
				+ " INNER JOIN process_config_tb pct ON pct.process_name = te.proc_name" + " where te.PROC_NAME = 'SubstitutionProcessApproval' and ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForNew(30);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Never");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Never");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(60);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every hour");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every hour");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(2592000);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every month");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every month");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every week");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every week");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(86400);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every day");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every day");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(31536000);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every year");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every year");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(timeRange);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Custom");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Custom");

		fetchStr = fetchStr + ") ) order by te.event_id";

		System.err.println("[WBP-Dev][getMailNotiForSubstitutionApproval]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				NotificationResponseDto notificationDto = new NotificationResponseDto();
				String eventId = obj[0] == null ? null : (String) obj[0];
				notificationDto.setTitle(
						(obj[3] == null ? null : (String) obj[3]) + " : " + (obj[4] == null ? null : (String) obj[4]));
				notificationDto.setDescription("Substitution Approval Required");
				notificationDto.setUserId(obj[1] == null ? null : (String) obj[1]);

				if (notiMap.get(eventId) != null) {
					NotificationResponseDto dto = notiMap.get(eventId);
					dto.setUserId(dto.getUserId() + "," + (obj[1] == null ? null : (String) obj[1]));

					notiMap.put(eventId, dto);
				} else {
					notiMap.put(eventId, notificationDto);
				}

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getMailNotiForSubstitutionApproval] Error : " + e.getMessage());
		}

		for (Map.Entry<String, NotificationResponseDto> entry : notiMap.entrySet()) {
			notificationDtos.add(entry.getValue());
		}
		return notificationDtos;
	}
	
	public List<PushNotificationDto> getPushNotiSubstitutionApproval(String userId, Integer timeRange) {
		List<PushNotificationDto> pushNotificationDtos = new ArrayList<>();
		Map<String, PushNotificationDto> notiMap = new HashMap<>();
		String fetchStr = "select distinct te.event_id,tw.task_owner,te.SUBJECT, te.PROC_NAME ,te.created_at"
				+ " from task_events te inner join task_owners tw " + " ON tw.event_id = te.event_id " + " where te.PROC_NAME = 'SubstitutionProcessApproval' and ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForNew(30);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Never");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Never");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(60);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every hour");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every hour");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(2592000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every month");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every month");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every week");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every week");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(86400);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every day");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every day");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(31536000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every year");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every year");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Custom");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SUBSTITUTION_APPROVAL,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Custom");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " order by te.event_id";

		System.err.println("[WBP-Dev][getPushNotiSubstitutionApproval]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				PushNotificationDto notificationDto = new PushNotificationDto();
				List<String> userList = new ArrayList<>();

				String eventId = obj[0] == null ? null : (String) obj[0];

				notificationDto.setData(obj[2] == null ? null : (String) obj[2]);
				notificationDto.setAlert(obj[3] == null ? null : (String) obj[3]);
				userList.add(obj[1] == null ? null : (String) obj[1]);
				notificationDto.setUsers(userList);

				if (notiMap.get(eventId) != null) {
					PushNotificationDto dto = notiMap.get(eventId);
					dto.getUsers().add(obj[1] == null ? null : (String) obj[1]);

					notiMap.put(eventId, dto);
				} else {
					String subject = notificationDto.getData();
					notificationDto.setData(subject + ", Substitution Approval Required");

					notiMap.put(eventId, notificationDto);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getPushNotiSubstitutionApproval] Error : " + e.getMessage());
		}

		for (Map.Entry<String, PushNotificationDto> entry : notiMap.entrySet()) {
			pushNotificationDtos.add(entry.getValue());
		}
		return pushNotificationDtos;
	}


}
