package oneapp.incture.workbox.demo.notification.dao;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationChannelDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationResponseDto;
import oneapp.incture.workbox.demo.notification.dto.PushNotificationDto;
import oneapp.incture.workbox.demo.notification.util.NotificationConstant;
import oneapp.incture.workbox.demo.notification.util.TimeZoneConvertion;

@Repository("NotificationTaskEvents")
public class TaskEventsDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	public Object[] getTaskDetail(String eventId) {
		String fetchStr = "SELECT DISTINCT TE.NAME,PCT.PROCESS_DISPLAY_NAME,PE.REQUEST_ID,TE.DESCRIPTION FROM TASK_EVENTS TE "
				+ "INNER JOIN PROCESS_EVENTS PE ON PE.PROCESS_ID = TE.PROCESS_ID"
				+ " INNER JOIN PROCESS_CONFIG_TB PCT ON PCT.PROCESS_NAME=PE.NAME " + " WHERE EVENT_ID = '" + eventId
				+ "'";
		Query fetchQry = getSession().createSQLQuery(fetchStr);
		Object[] res = (Object[]) fetchQry.uniqueResult();

		return res;
	}

	public TaskOwnersDto getTaskCreator(String eventId) {

		TaskOwnersDto taskOwnerDtos = new TaskOwnersDto();
		Query taskCreatorQry = getSession()
				.createSQLQuery("SELECT STARTED_BY,STARTED_BY_DISP FROM PROCESS_EVENTS WHERE "
						+ "PROCESS_ID = (SELECT PROCESS_ID FROM TASK_EVENTS WHERE EVENT_ID= '" + eventId + "')");

		List<Object[]> resultList = taskCreatorQry.list();

		for (Object[] obj : resultList) {

			taskOwnerDtos.setTaskOwner(obj[0] == null ? null : (String) obj[0]);
			taskOwnerDtos.setTaskOwnerDisplayName(obj[1] == null ? null : (String) obj[1]);

		}

		return taskOwnerDtos;
	}

	/**
	 * Fetching newly created task for push notification
	 * 
	 * @param userId
	 * @param timeRange
	 * @return
	 */
	public List<PushNotificationDto> getPushNotificationDataForNew(String userId, Integer timeRange) {
		List<PushNotificationDto> pushNotificationDtos = new ArrayList<>();
		Map<String, PushNotificationDto> notiMap = new HashMap<>();
		String fetchStr = "select distinct te.event_id,tw.task_owner,te.SUBJECT, te.PROC_NAME ,te.created_at"
				+ " from task_events te inner join task_owners tw " + " ON tw.event_id = te.event_id " + " where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForNew(30);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Never");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Never");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(60);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every hour");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every hour");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(2592000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every month");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every month");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every week");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every week");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(86400);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every day");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every day");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(31536000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every year");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every year");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Custom");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Custom");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " order by te.event_id";

		System.err.println("[WBP-Dev][getPushNotificationDataForNew]fetchStr : " + fetchStr);

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
					notificationDto.setData(subject + ", was Created");

					notiMap.put(eventId, notificationDto);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getPushNotificationDataForNew] Error : " + e.getMessage());
		}

		for (Map.Entry<String, PushNotificationDto> entry : notiMap.entrySet()) {
			pushNotificationDtos.add(entry.getValue());
		}
		return pushNotificationDtos;
	}

	/**
	 * Generating query condition for getting userId based on actionType and
	 * channel
	 * 
	 * @param actionType
	 * @param channel
	 * @return
	 */
	public String getUserIdQuery(String actionType, String channel) {
		String userQuery = "";
		// userQuery = userQuery + " select user_id from notification_config
		// where enable_action IN ('" + actionType
		// + "') " + "and enable_channel IN ('" + channel + "') and user_id NOT
		// IN('Admin') " + " UNION "
		// + " select user_id from user_idp_mapping where user_id NOT IN (select
		// distinct user_id from notification_config) ";

		// userQuery = userQuery + " select user_id from notification_config
		// where enable_action IN ('" + actionType
		// + "') " + "and enable_channel IN ('" + channel + "') and user_id NOT
		// IN('Admin') ";

		userQuery = userQuery
				+ "select unc.id from USER_NOTIFICATION_CONFIG unc join NOTIFICATION_EVENTS ne on unc.event_id=ne.event_id where ne.EVENT_NAME IN ('"
				+ actionType + "') " + " and unc.CHANNEL IN ('" + channel + "') and unc.id NOT IN('Admin')  ";

		return userQuery;
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

			if (seconds >= 0 && seconds <= 35) {
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

			if (seconds >= 0 && seconds <= 35) {
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

			if (seconds >= 0 && seconds <= 35) {
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

			if (seconds >= 0 && seconds <= 35) {
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

			if (seconds >= 0 && seconds <= 35) {
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

			if (seconds >= 0 && seconds <= 35) {
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
			
		case NotificationConstant.SLA_TASK:
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
	 * Generating time range query condition for newly created task
	 * 
	 * @param timeRange
	 * @return
	 */
	public String getTimeRangeQueryForNew(Integer timeRange) {
		String timeRangeQuery = "";
		if (timeRange != null)
			timeRangeQuery = timeRangeQuery + " te.created_at > Add_seconds(CURRENT_TIMESTAMP, - ( " + timeRange
					+ " ))";
		return timeRangeQuery;
	}
	
	public String getTimeRangeQueryForSLA(Integer timeRange) {
		String timeRangeQuery = "";
		if (timeRange != null)
			timeRangeQuery = timeRangeQuery + " te.comp_deadline > Add_seconds(CURRENT_TIMESTAMP, - ( " + timeRange
					+ " )) and  te.status='READY'";
		return timeRangeQuery;
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
			timeRangeQuery = timeRangeQuery + " ta.updated_at > Add_seconds(CURRENT_TIMESTAMP, - ( " + timeRange
					+ " ))";
		return timeRangeQuery;
	}


	/**
	 * Fetching updated action for Push Notification
	 * 
	 * @param actionTypeList
	 * @param userId
	 * @param timeRange
	 * @return
	 */
	public List<PushNotificationDto> getPushNotificationDataForAction(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<PushNotificationDto> pushNotificationDtos = new ArrayList<>();
		String fetchStr = "select distinct ta.event_id,ta.action_type,ta.updated_at,ta.user_name,ta.send_to_user,te.subject,te.proc_name,te.created_by"
				+ " from task_audit ta inner join task_events te" + " ON ta.event_id = te.event_id INNER JOIN "
				+ "process_events Pe ON Pe.process_id = te.process_id" + " where";

		fetchStr = fetchStr + "( " + getTimeRangeQueryForAction(30);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForAction(60);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForAction(2592000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForAction(86400);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForAction(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForAction(timeRange);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + " ) ) order by ta.event_id";

		System.err.println("[WBP-Dev][getPushNotificationDataForAction]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				PushNotificationDto notificationDto = new PushNotificationDto();
				List<String> userList = new ArrayList<>();
				String actionType = obj[1] == null ? null : (String) obj[1];
				if (actionType != null && ("Approved".equalsIgnoreCase(actionType)
						|| "Approve".equalsIgnoreCase(actionType) || "Done".equalsIgnoreCase(actionType)
						|| "Resolved".equalsIgnoreCase(actionType) || "Completed".equalsIgnoreCase(actionType))) {
					notificationDto.setData((obj[5] == null ? null : (String) obj[5]) + ", Approved By : "
							+ (obj[3] == null ? null : (String) obj[3]));
					userList.add(obj[7] == null ? null : (String) obj[7]);
					notificationDto.setUsers(userList);
				} else if (actionType != null
						&& ("Rejected".equalsIgnoreCase(actionType) || "Reject".equalsIgnoreCase(actionType))) {
					notificationDto.setData((obj[5] == null ? null : (String) obj[5]) + ", Rejected By : "
							+ (obj[3] == null ? null : (String) obj[3]));
					userList.add(obj[7] == null ? null : (String) obj[7]);
					notificationDto.setUsers(userList);
				} else if (actionType != null && "Forward".equalsIgnoreCase(actionType)) {
					notificationDto.setData((obj[5] == null ? null : (String) obj[5]) + ", Forwarded By : "
							+ (obj[3] == null ? null : (String) obj[3]));
					userList.add(obj[4] == null ? null : (String) obj[4]);
					notificationDto.setUsers(userList);
				}
				notificationDto.setAlert(obj[6] == null ? null : (String) obj[6]);

				pushNotificationDtos.add(notificationDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getPushNotificationDataForAction] Error : " + e.getMessage());
		}
		return pushNotificationDtos;
	}
	
	/**
	 * Fetching newly created task for Mail or Web notification
	 * 
	 * @param timeRange
	 * @param userId
	 * 
	 * @param notificationType
	 * @return
	 */
	public List<NotificationResponseDto> getMailNotificationDataForNew(String userId, Integer timeRange) {
		Map<String, NotificationResponseDto> notiMap = new HashMap<>();
		List<NotificationResponseDto> notificationDtos = new ArrayList<>();
		String fetchStr = "select distinct te.event_id,uim.user_email,te.subject,pct.process_display_name,pe.request_id"
				+ " from task_events te INNER JOIN task_owners tw" + " ON tw.event_id = te.event_id"
				+ " INNER JOIN process_events Pe ON Pe.process_id = te.process_id"
				+ " INNER JOIN user_idp_mapping uim ON uim.user_id = tw.task_owner"
				+ " INNER JOIN process_config_tb pct ON pct.process_name = te.proc_name" + " where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForNew(30);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Never");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Never");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(60);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every hour");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every hour");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(2592000);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every month");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every month");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every week");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every week");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(86400);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every day");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every day");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(31536000);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every year");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every year");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForNew(timeRange);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Custom");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Custom");

		fetchStr = fetchStr + ") ) order by te.event_id";

		System.err.println("[WBP-Dev][getMailNotificationDataForNew]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				NotificationResponseDto notificationDto = new NotificationResponseDto();
				String eventId = obj[0] == null ? null : (String) obj[0];
				notificationDto.setTitle(
						(obj[3] == null ? null : (String) obj[3]) + " : " + (obj[4] == null ? null : (String) obj[4]));
				notificationDto.setDescription("TASK : " + (obj[2] == null ? null : (String) obj[2]) + ", was Created");
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
			System.err.println("[WBP-Dev][getMailNotificationDataForNew] Error : " + e.getMessage());
		}

		for (Map.Entry<String, NotificationResponseDto> entry : notiMap.entrySet()) {
			notificationDtos.add(entry.getValue());
		}
		return notificationDtos;
	}

	/**
	 * Fetching updated action for Mail or Web Notification
	 * 
	 * @param timeRange
	 * @param userId
	 * @param actionTypeList
	 * 
	 * @param notificationType
	 * @return
	 */
	public List<NotificationResponseDto> getMailNotificationDataForAction(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<NotificationResponseDto> notificationDtos = new ArrayList<>();
		String fetchStr = "select distinct ta.event_id,te.subject,pct.process_display_name,ta.user_name,ta.action_type,ta.send_to_user,te.created_by,pe.request_id,uim.user_email"
				+ " from task_audit ta inner join task_events te" + " ON ta.event_id = te.event_id"
				+ " INNER JOIN process_events Pe ON Pe.process_id = te.process_id"
				+ " INNER JOIN user_idp_mapping uim ON"
				+ " (uim.user_id = ta.send_to_user AND ta.send_to_user IS NOT NULL)" + " OR"
				+ " (uim.user_id = te.created_by AND ta.send_to_user IS NULL)"
				+ " INNER JOIN process_config_tb pct ON pct.process_name = te.proc_name" + " where ";

		fetchStr = fetchStr + "( " + getTimeRangeQueryForAction(30);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(60);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(2592000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(86400);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {
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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {
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

		fetchStr = fetchStr + ") ) order by ta.event_id";

		System.err.println("[WBP-Dev][getMailNotificationDataForAction]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				NotificationResponseDto notificationDto = new NotificationResponseDto();
				String actionType = obj[4] == null ? null : (String) obj[4];

				if (actionType != null && ("Approved".equalsIgnoreCase(actionType)
						|| "Approve".equalsIgnoreCase(actionType) || "Done".equalsIgnoreCase(actionType)
						|| "Resolved".equalsIgnoreCase(actionType) || "Completed".equalsIgnoreCase(actionType))) {
					notificationDto.setDescription("Task : " + (obj[1] == null ? null : (String) obj[1])
							+ ", Approved By : " + (obj[3] == null ? null : (String) obj[3]));
				} else if (actionType != null
						&& ("Rejected".equalsIgnoreCase(actionType) || "Reject".equalsIgnoreCase(actionType))) {
					notificationDto.setDescription("Task : " + (obj[1] == null ? null : (String) obj[1])
							+ ", Rejected By : " + (obj[3] == null ? null : (String) obj[3]));
				} else if (actionType != null && "Forward".equalsIgnoreCase(actionType)) {
					notificationDto.setDescription("Task : " + (obj[1] == null ? null : (String) obj[1])
							+ ", Forwarded By : " + (obj[3] == null ? null : (String) obj[3]));
				}

				notificationDto.setUserId(obj[8] == null ? null : (String) obj[8]);

				notificationDto.setTitle(
						(obj[2] == null ? null : (String) obj[2]) + " : " + (obj[7] == null ? null : (String) obj[7]));

				notificationDtos.add(notificationDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getMailNotificationDataForAction] Error : " + e.getMessage());
		}
		return notificationDtos;
	}
	

	/**
	 * Fetching newly created task for Web notification
	 * 
	 * @param userId
	 * @param timeRange
	 * @return
	 */
	public List<NotificationDto> getWebNotificationDataForNew(String userId, Integer timeRange) {
		TimeZoneConvertion timeZoneConvertion = new TimeZoneConvertion();
		List<NotificationDto> notificationDtos = new ArrayList<>();
		String fetchStr = "select distinct te.event_id,tw.task_owner,tw.task_owner_disp,te.subject,te.proc_name,pe.request_id,te.name"
				+ " from task_events te INNER JOIN task_owners tw" + " ON tw.event_id = te.event_id"
				+ " INNER JOIN process_events Pe ON Pe.process_id = te.process_id" + " where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForNew(30);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Never");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Never");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(60);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every hour");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every hour");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(2592000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every month");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every month");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every week");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every week");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(86400);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every day");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every day");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(31536000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every year");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every year");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForNew(timeRange);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Custom");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.NEW_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Custom");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " order by te.event_id";

		System.err.println("[WBP-Dev][getWebNotificationDataForNew]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				NotificationDto notificationDto = new NotificationDto();
				String notificationId = UUID.randomUUID().toString().replaceAll("-", "");

				notificationDto.setTitle(
						(obj[4] == null ? null : (String) obj[4]) + " : " + (obj[5] == null ? null : (String) obj[5]));
				notificationDto.setDescription("TASK : " + (obj[3] == null ? null : (String) obj[3]) + ", was Created");
				notificationDto.setUserId(obj[1] == null ? null : (String) obj[1]);
				notificationDto.setUserName(obj[2] == null ? null : (String) obj[2]);
				notificationDto.setId(obj[0] == null ? null : (String) obj[0]);
				notificationDto.setOrigin("Task");
				notificationDto.setEventName(obj[6] == null ? null : (String) obj[6]);

				notificationDto
						.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
				notificationDto.setNotificationId(notificationId);
				notificationDto.setNotificationType(NotificationConstant.NEW_TASK);
				notificationDto.setPriority(NotificationConstant.HIGH);
				notificationDto.setStatus(true);

				notificationDtos.add(notificationDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getWebNotificationDataForNew] Error : " + e.getMessage());
		}

		return notificationDtos;
	}

	/**
	 * Fetching updated action for Web Notification
	 * 
	 * @param actionTypeList
	 * @param userId
	 * @param timeRange
	 * @return
	 */
	public List<NotificationDto> getWebNotificationDataForAction(List<String> actionTypeList, String userId,
			Integer timeRange) {
		List<NotificationDto> notificationDtos = new ArrayList<>();
		TimeZoneConvertion timeZoneConvertion = new TimeZoneConvertion();

		String fetchStr = "select distinct ta.event_id,te.subject,te.proc_name,ta.user_name,ta.action_type,ta.send_to_user,"
				+ "te.created_by,pe.request_id,te.name,pe.started_by_disp,pe.started_by,dense_rank() over "
				+ "( partition by ta.event_id order by ta.updated_at desc ) as rank "
				+ " from task_audit ta inner join task_events te" + " ON ta.event_id = te.event_id"
				+ " INNER JOIN process_events Pe ON Pe.process_id = te.process_id" + " where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForAction(30);
		int count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(86400);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(31536000);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForAction(604800);
		count = 0;
		for (String action : actionTypeList) {
			if (NotificationConstant.APPROVE.equals(action) || NotificationConstant.REJECT.equals(action)
					|| NotificationConstant.NEW_TASK.equals(action) || NotificationConstant.FORWARD.equals(action)) {

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

		fetchStr = fetchStr + ")) order by ta.event_id";

		System.err.println("[WBP-Dev][getWebNotificationDataForAction]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {

				BigInteger rank = ((obj[11] == null ? null : (BigInteger) obj[11]));
				BigInteger firstRank = new BigInteger("1");

				if (firstRank.equals(rank)) {

					NotificationDto notificationDto = new NotificationDto();
					String notificationId = UUID.randomUUID().toString().replaceAll("-", "");

					String actionType = obj[4] == null ? null : (String) obj[4];
					if (actionType != null && ("Approved".equalsIgnoreCase(actionType)
							|| "Approve".equalsIgnoreCase(actionType) || "Done".equalsIgnoreCase(actionType)
							|| "Resolved".equalsIgnoreCase(actionType) || "Completed".equalsIgnoreCase(actionType))) {
						notificationDto.setDescription(obj[1] == null ? null : (String) obj[1]);
						notificationDto.setUserId(
								obj[6] == null ? (obj[10] == null ? null : (String) obj[10]) : (String) obj[6]);
						notificationDto.setUserName(obj[3] == null ? null : (String) obj[3]);
					} else if (actionType != null
							&& ("Rejected".equalsIgnoreCase(actionType) || "Reject".equalsIgnoreCase(actionType))) {
						notificationDto.setDescription(obj[1] == null ? null : (String) obj[1]);
						notificationDto.setUserId(
								obj[6] == null ? (obj[10] == null ? null : (String) obj[10]) : (String) obj[6]);
						notificationDto.setUserName(obj[3] == null ? null : (String) obj[3]);
					} else if (actionType != null && "Forward".equalsIgnoreCase(actionType)) {
						notificationDto.setDescription(obj[1] == null ? null : (String) obj[1]);
						notificationDto.setUserId(
								obj[5] == null ? (obj[10] == null ? null : (String) obj[10]) : (String) obj[5]);
						notificationDto.setUserName(obj[3] == null ? null : (String) obj[3]);
					}

					notificationDto.setTitle((obj[2] == null ? null : (String) obj[2]) + " : "
							+ (obj[7] == null ? null : (String) obj[7]));
					notificationDto.setId(obj[0] == null ? null : (String) obj[0]);
					notificationDto.setOrigin("Task");
					notificationDto.setEventName(obj[8] == null ? null : (String) obj[8]);

					notificationDto
							.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
					notificationDto.setNotificationId(notificationId);
					notificationDto.setNotificationType(actionType);
					notificationDto.setPriority(NotificationConstant.HIGH);
					notificationDto.setStatus(true);

					notificationDtos.add(notificationDto);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getWebNotificationDataForAction] Error : " + e.getMessage());
		}
		return notificationDtos;
	}
	
	public List<NotificationDto> getWebNotificationDataForSLA(String userId,
			Integer timeRange) {
		TimeZoneConvertion timeZoneConvertion = new TimeZoneConvertion();
		List<NotificationDto> notificationDtos = new ArrayList<>();
		String fetchStr = "select distinct te.event_id,tw.task_owner,tw.task_owner_disp,te.subject,te.proc_name,pe.request_id,te.name"
				+ " from task_events te INNER JOIN task_owners tw" + " ON tw.event_id = te.event_id"
				+ " INNER JOIN process_events Pe ON Pe.process_id = te.process_id" + " where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForSLA(30);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Never");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Never");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(60);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every hour");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every hour");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(2592000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every month");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every month");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every week");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every week");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(86400);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every day");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every day");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(31536000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every year");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Every year");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(timeRange);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Custom");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_WEB, userId, "Custom");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " order by te.event_id";

		System.err.println("[WBP-Dev][getWebNotificationDataForSLA]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				NotificationDto notificationDto = new NotificationDto();
				String notificationId = UUID.randomUUID().toString().replaceAll("-", "");

				notificationDto.setTitle("SLA Breached : " + (obj[5] == null ? null : (String) obj[5]));
				notificationDto.setDescription("TASK : " + (obj[3] == null ? null : (String) obj[3]) + ", is SLA Breached");
				notificationDto.setUserId(obj[1] == null ? null : (String) obj[1]);
				notificationDto.setUserName(obj[2] == null ? null : (String) obj[2]);
				notificationDto.setId(obj[0] == null ? null : (String) obj[0]);
				notificationDto.setOrigin("Task");
				notificationDto.setEventName(obj[6] == null ? null : (String) obj[6]);

				notificationDto
						.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
				notificationDto.setNotificationId(notificationId);
				notificationDto.setNotificationType(NotificationConstant.SLA_TASK);
				notificationDto.setPriority(NotificationConstant.HIGH);
				notificationDto.setStatus(true);

				notificationDtos.add(notificationDto);

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getWebNotificationDataForSLA] Error : " + e.getMessage());
		}

		return notificationDtos;
	}
	
	public List<NotificationResponseDto> getMailNotificationDataForSLA(String userId, Integer timeRange) {
		Map<String, NotificationResponseDto> notiMap = new HashMap<>();
		List<NotificationResponseDto> notificationDtos = new ArrayList<>();
		String fetchStr = "select distinct te.event_id,uim.user_email,te.subject,pct.process_display_name,pe.request_id"
				+ " from task_events te INNER JOIN task_owners tw" + " ON tw.event_id = te.event_id"
				+ " INNER JOIN process_events Pe ON Pe.process_id = te.process_id"
				+ " INNER JOIN user_idp_mapping uim ON uim.user_id = tw.task_owner"
				+ " INNER JOIN process_config_tb pct ON pct.process_name = te.proc_name" + " where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForSLA(30);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Never");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Never");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSLA(60);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every hour");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every hour");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSLA(2592000);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every month");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every month");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSLA(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every week");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every week");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSLA(86400);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every day");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every day");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSLA(31536000);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every year");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Every year");

		fetchStr = fetchStr + ") )";

		fetchStr = fetchStr + "OR ( " + getTimeRangeQueryForSLA(timeRange);
		if (timeRange != null)
			fetchStr = fetchStr + " And (" + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Custom");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_EMAIL, userId, "Custom");

		fetchStr = fetchStr + ") ) order by te.event_id";

		System.err.println("[WBP-Dev][getMailNotificationDataForSLA]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				NotificationResponseDto notificationDto = new NotificationResponseDto();
				String eventId = obj[0] == null ? null : (String) obj[0];
				notificationDto.setTitle("SLA Breached : " + (obj[4] == null ? null : (String) obj[4]));
				notificationDto.setDescription("TASK : " + (obj[2] == null ? null : (String) obj[2]) + ", is SLA Breached");
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
			System.err.println("[WBP-Dev][getMailNotificationDataForSLA] Error : " + e.getMessage());
		}

		for (Map.Entry<String, NotificationResponseDto> entry : notiMap.entrySet()) {
			notificationDtos.add(entry.getValue());
		}
		return notificationDtos;
	}
	
	public List<PushNotificationDto> getPushNotificationDataForSLA(String userId, Integer timeRange) {
		List<PushNotificationDto> pushNotificationDtos = new ArrayList<>();
		Map<String, PushNotificationDto> notiMap = new HashMap<>();
		String fetchStr = "select distinct te.event_id,tw.task_owner,te.SUBJECT, te.PROC_NAME ,te.created_at"
				+ " from task_events te inner join task_owners tw " + " ON tw.event_id = te.event_id " + " where ";

		fetchStr = fetchStr + " ( " + getTimeRangeQueryForSLA(30);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Never");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Never");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(60);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every hour");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every hour");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(2592000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every month");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every month");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every week");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every week");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(86400);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every day");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every day");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(31536000);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every year");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Every year");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " OR ( " + getTimeRangeQueryForSLA(604800);
		if (timeRange != null)
			fetchStr = fetchStr + " And " + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Custom");
		else
			fetchStr = fetchStr + getActionQuery(NotificationConstant.SLA_TASK,
					NotificationConstant.NOTIFICATION_CHANNEL_PUSH, userId, "Custom");

		fetchStr = fetchStr + ")";

		fetchStr = fetchStr + " order by te.event_id";

		System.err.println("[WBP-Dev][getPushNotificationDataForSLA]fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				PushNotificationDto notificationDto = new PushNotificationDto();
				List<String> userList = new ArrayList<>();

				String eventId = obj[0] == null ? null : (String) obj[0];

				notificationDto.setData(obj[2] == null ? null : (String) obj[2]);
				notificationDto.setAlert("SLA Breached : " + obj[3] == null ? null : (String) obj[3]);
				userList.add(obj[1] == null ? null : (String) obj[1]);
				notificationDto.setUsers(userList);

				if (notiMap.get(eventId) != null) {
					PushNotificationDto dto = notiMap.get(eventId);
					dto.getUsers().add(obj[1] == null ? null : (String) obj[1]);

					notiMap.put(eventId, dto);
				} else {
					String subject = notificationDto.getData();
					notificationDto.setData(subject + ", is SLA Breached");

					notiMap.put(eventId, notificationDto);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][getPushNotificationDataForSLA] Error : " + e.getMessage());
		}

		for (Map.Entry<String, PushNotificationDto> entry : notiMap.entrySet()) {
			pushNotificationDtos.add(entry.getValue());
		}
		return pushNotificationDtos;
	}


	/**
	 * Preparing channel query condition on notification config
	 * 
	 * @param channels
	 * @return
	 */
	private String getChannelQuery(List<String> channels) {
		String query = "and unc.CHANNEL in(";
		int count = 0;
		for (String channel : channels) {
			if (count == 0)
				query = query + " '" + channel + "'";
			else
				query = query + " ,'" + channel + "'";
			count = ++count;
		}
		query = query + ") ";
		return query;
	}

	/**
	 * Preparing action type query condition on notification config
	 * 
	 * @param actions
	 * @return
	 */
	private String getActionTypeQuery(List<String> actions) {
		String query = "and ne.EVENT_NAME in(";
		int count = 0;
		for (String action : actions) {
			if (count == 0)
				query = query + " '" + action + "'";
			else
				query = query + " ,'" + action + "'";
			count = ++count;
		}
		query = query + ") ";
		return query;
	}

	/**
	 * Fetching Notification config details based on channel, action and userid
	 * 
	 * @param channels
	 * @param actions
	 * @param userId
	 * @return
	 */
	public List<NotificationChannelDto> fetchNotificationConfigDetails(List<String> channels, List<String> actions,
			String userId) {
		List<NotificationChannelDto> notificationChannelDtos = new ArrayList<>();
		Map<String, NotificationChannelDto> channelMap = new HashMap<>();
		if (userId == null || userId.isEmpty()) {
			userId = "Admin";
		}
		String fetchStr = "select ENABLE_ACTION,ENABLE_CHANNEL from NOTIFICATION_CONFIG " + "where USER_ID='" + userId
				+ "' ";
		if (channels != null)
			fetchStr = fetchStr + getChannelQuery(channels);
		else if (actions != null)
			fetchStr = fetchStr + getActionTypeQuery(actions);
		fetchStr = fetchStr + " order by ENABLE_CHANNEL";

		System.err.println("[WBP-Dev][fetchNotificationConfigDetails] fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				List<String> actionTypes = new ArrayList<>();
				String channelName = obj[1] == null ? null : (String) obj[1];

				if (channelMap.get(channelName) != null) {
					NotificationChannelDto dto = channelMap.get(channelName);
					dto.getActionTypes().add(obj[0] == null ? null : (String) obj[0]);
					channelMap.put(channelName, dto);
				} else {
					actionTypes.add(obj[0] == null ? null : (String) obj[0]);
					NotificationChannelDto dto = new NotificationChannelDto();
					dto.setChannelName(channelName);
					dto.setActionTypes(actionTypes);
					channelMap.put(channelName, dto);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][TaskEvents][getPushNotificationData] Error : " + e.getMessage());
		}
		for (Map.Entry<String, NotificationChannelDto> entry : channelMap.entrySet()) {
			notificationChannelDtos.add(entry.getValue());
		}
		return notificationChannelDtos;
	}

	public List<NotificationChannelDto> fetchNotificationConfigDetail(List<String> channels, List<String> actions,
			String userId) {
		List<NotificationChannelDto> notificationChannelDtos = new ArrayList<>();
		Map<String, NotificationChannelDto> channelMap = new HashMap<>();
		if (userId == null || userId.isEmpty()) {
			userId = "Admin";
		}
		String fetchStr = "select distinct ne.EVENT_NAME,unc.CHANNEL FROM USER_NOTIFICATION_CONFIG unc join NOTIFICATION_EVENTS ne "
				+ "on unc.event_id=ne.event_id where unc.ID ='" + userId + "' and ne.id='1' "
				+ "and (SELECT nev.default from notification_events nev where nev.id = '2' "
				+ " and ne.event_id=nev.event_id) =1 and "
				+ "(SELECT vst.default from view_setting vst where vst.user_id='Admin' and vst.id = '2' "
				+ " and vst.view_name = ne.event_group) = 1 ";
		if (channels != null)
			fetchStr = fetchStr + getChannelQuery(channels);
		else if (actions != null)
			fetchStr = fetchStr + getActionTypeQuery(actions);
		fetchStr = fetchStr + " order by unc.CHANNEL";

		System.err.println("[WBP-Dev][fetchNotificationConfigDetail] fetchStr : " + fetchStr);

		try {
			Query q = getSession().createSQLQuery(fetchStr);
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				List<String> actionTypes = new ArrayList<>();
				String channelName = obj[1] == null ? null : (String) obj[1];

				if (channelMap.get(channelName) != null) {
					NotificationChannelDto dto = channelMap.get(channelName);
					dto.getActionTypes().add(obj[0] == null ? null : (String) obj[0]);
					channelMap.put(channelName, dto);
				} else {
					actionTypes.add(obj[0] == null ? null : (String) obj[0]);
					NotificationChannelDto dto = new NotificationChannelDto();
					dto.setChannelName(channelName);
					dto.setActionTypes(actionTypes);
					channelMap.put(channelName, dto);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][TaskEvents][getPushNotificationData] Error : " + e.getMessage());
		}
		for (Map.Entry<String, NotificationChannelDto> entry : channelMap.entrySet()) {
			notificationChannelDtos.add(entry.getValue());
		}
		return notificationChannelDtos;
	}
}
