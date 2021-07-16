package oneapp.incture.workbox.demo.dashboard.util;

public interface CustomGraphConstant {

	String COUNT_QUERY = ", ( SELECT count(*) FROM ( SELECT DISTINCT request_id, te.event_id "
			+ ", CASE WHEN TE.COMPLETED_AT IS NULL THEN (CASE WHEN CURRENT_TIMESTAMP > TE.COMP_DEADLINE "
			+ "THEN 'Sla Breached' WHEN CURRENT_TIMESTAMP > ADD_SECONDS(TE.COMP_DEADLINE,-PCT.CRITICAL_DATE*60*60) AND  "
			+ "CURRENT_TIMESTAMP < TE.COMP_DEADLINE THEN 'Critical' ELSE 'In Time' END) ELSE (CASE WHEN TE.COMPLETED_AT > "
			+ "TE.COMP_DEADLINE THEN 'Sla Breached' ELSE 'In Time' END) END AS TASK_SLA "
			+ "FROM TASK_EVENTS TE INNER JOIN PROCESS_EVENTS PE ON TE.PROCESS_ID = PE.PROCESS_ID "
			+ " LEFT JOIN TASK_OWNERS TW ON TE.EVENT_ID = TW.EVENT_ID "
			+ "INNER JOIN PROCESS_CONFIG_TB PCT ON PE.NAME = PCT.PROCESS_NAME WHERE 1=1 ";
	
	String USER_LIST = "userList";
	String PROCESS_LIST = "processList";
	String GROUP_LIST = "groupList";
	String STATUS = "status";
	String ORIGIN = "origin";
	String TASK_COUNT = "taskCount";
	String TASK_STATE = "comp_deadline";
	String CREATED_BY = "createdBy";
	String CREATED_ON = "createdOn";
	String COMPLETED_ON = "completedOn";
	String DUE_DATE = "dueDate";
	String X_AXIS = "xAxis";
	String Y_AXIS = "yAxis";
	String SLA_BREACHED = "slaBreached";
	String CRITICAL = "critical";
	String ON_TIME = "inTime";
	Integer MAX_COUNT = 15;
}
