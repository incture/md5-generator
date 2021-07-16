package oneapp.incture.workbox.demo.adhocTask.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dao.StatusConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.adhocTask.util.TimeZoneConvertion;

@Repository("adhocTaskEventDao")
public class TaskEventDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	ProcessConfigDao processConfigDao;
	
	@Autowired
	ProcessEventDao processEventDao;
	
	@Autowired
	StatusConfigDao statusConfigDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	@Autowired
	private TimeZoneConvertion timeZoneConvertion;

	/*
	 * public void saveAllTaskEvent(AttributesResponseDto tasksToSave, String
	 * ownerName, List<String> processIdList, List<String> eventIdList,
	 * List<ProcessDescDto> processDescDto, String actionType,
	 * List<TaskTemplateDto> taskTemplateDto) {
	 * 
	 * // WebsocketChatConfiguration webChat = null;
	 * 
	 * TimeZoneConvertion timeZoneConvertion = null;
	 * 
	 * TaskEventsDo taskEvent = null; timeZoneConvertion = new
	 * TimeZoneConvertion(); Integer i = 0; Integer count = 0;
	 * 
	 * Transaction tx = null;
	 * 
	 * 
	 * 
	 * String subject= ownerName + " created " +
	 * tasksToSave.getListOfProcesssAttributes().get(0).
	 * getCustomAttributeTemplateDto().get(0).getProcessName();
	 * 
	 * Session session = sessionFactory.openSession();
	 * 
	 * tx = session.beginTransaction();
	 * 
	 * for (ProcessDescDto obj : processDescDto) {
	 * 
	 * count++; taskEvent = new TaskEventsDo();
	 * 
	 * taskEvent.setEventId(eventIdList.get(i));
	 * taskEvent.setName(taskTemplateDto.get(0).getTaskName());
	 * taskEvent.setProcessId(processIdList.get(i));
	 * taskEvent.setCreatedBy(tasksToSave.getResourceid());
	 * taskEvent.setCurrentProcessor("");
	 * taskEvent.setCurrentProcessorDisplayName("");
	 * taskEvent.setDescription(obj.getDescription());
	 * taskEvent.setForwardedAt(ServicesUtil.convertAdminFromStringToDate(
	 * timeZoneConvertion.convertToIst())); taskEvent.setForwardedBy(ownerName);
	 * taskEvent.setOrigin("Manual"); taskEvent.setPriority("0");
	 * taskEvent.setCompletionDeadLine(null);
	 * taskEvent.setProcessName(tasksToSave.getListOfProcesssAttributes().get(0)
	 * .getCustomAttributeTemplateDto().get(0).getProcessName());
	 * taskEvent.setSlaDueDate(null); if("Submit".equals(actionType))
	 * taskEvent.setStatus(TaskCreationConstant.NEW); else
	 * if("Save".equals(actionType))
	 * taskEvent.setStatus(TaskCreationConstant.DRAFT);
	 * taskEvent.setStatusFlag("0"); taskEvent.setSubject(subject);
	 * taskEvent.setTaskMode("NULL");
	 * taskEvent.setTaskType(taskTemplateDto.get(0).getTaskType());
	 * taskEvent.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(
	 * timeZoneConvertion.convertToIst())); taskEvent.setCompletedAt(null);
	 * taskEvent.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(
	 * timeZoneConvertion.convertToIst()));
	 * 
	 * // webChat = new WebsocketChatConfiguration(); // try{ //
	 * webChat.addNewTaskCreated(eventIdList.get(i),
	 * customAttributeTemplateDtoObject.getResourceid()); // }catch (Exception
	 * e) { // // System.err.println("[WBP-Dev]TaskEventDao.saveNextTask()"+
	 * ":error in task event"); // }
	 * 
	 * session.save(taskEvent);
	 * 
	 * if(count % 50 == 0){ session.flush(); session.clear(); }
	 * 
	 * i++; }
	 * 
	 * tx.commit(); session.close();
	 * 
	 * 
	 * }
	 */
	public void updateTaskEvents(AttributesResponseDto attributesResponseDto, String eventId) {

		TimeZoneConvertion timeZoneConvertion = null;
		timeZoneConvertion = new TimeZoneConvertion();

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String taskString = "update task_events set status = '" + TaskCreationConstant.READY + "' ,"
				+ "created_at = :updatedAt ,updated_at = :updatedAt " + "where event_id = :eventId";
		Query q = session.createSQLQuery(taskString);
		q.setParameter("updatedAt", ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
		q.setParameter("eventId", eventId);
		q.executeUpdate();
		tx.commit();
		session.close();

	}

	public ResponseMessage deleteDraftTask(String processId) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query taskQry = session.createSQLQuery("delete from task_events where process_id =:processId");
			taskQry.setParameter("processId", processId);
			if (taskQry.executeUpdate() < 1) {
				resp.setMessage("Process Not found");
			} else {
				resp.setMessage(TaskCreationConstant.SUCCESS);
				resp.setStatus(TaskCreationConstant.SUCCESS);
				resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][DRAFT DELETION] ERROR : " + e.getMessage());
		}

		return resp;
	}

	public int updateStatusInTaskEvents(String event, String status, String userId, String userDisplay,
			Boolean isAdmin,Token token) {

		String businessStatus = statusConfigDao.getBusinessStatusByEventId(event, status);
		String update = "UPDATE TASK_EVENTS SET STATUS ='" + status.toUpperCase() + "', CUR_PROC = '" + userId + "',"
				+ "CUR_PROC_DISP = '" + userDisplay + "', UPDATED_AT = '" + timeZoneConvertion.convertToUTC() + "',"
						+ " BUSINESS_STATUS = '"+businessStatus+"' ";
		if (status.equals(TaskCreationConstant.COMPLETED) || status.equals(TaskCreationConstant.DONE)
				|| status.equals(TaskCreationConstant.APPROVE) || status.equals(TaskCreationConstant.REJECT)
				|| status.equals(TaskCreationConstant.ACCEPTED) || status.equals(TaskCreationConstant.DECLINED)) {
			update = update + ",COMPLETED_AT = '" + timeZoneConvertion.convertToUTC() + "'";
		}
		update = update + " WHERE EVENT_ID= '" + event + "' ";

		userId = token.getLogonName();
		if (!isAdmin) {
			if (status.equals(TaskCreationConstant.RESERVED))
				update = update + "AND (STATUS = '" + TaskCreationConstant.READY + "' OR STATUS = '" + TaskCreationConstant.RESERVED + "')";
		}

		if (status.equals(TaskCreationConstant.COMPLETED) || status.equals(TaskCreationConstant.DONE)
				|| status.equals(TaskCreationConstant.APPROVED) || status.equals(TaskCreationConstant.REJECTED)
				|| status.equals(TaskCreationConstant.ACCEPTED) || status.equals(TaskCreationConstant.DECLINED)) {
			update = update + " AND ((STATUS = '" + TaskCreationConstant.RESERVED + "' AND CUR_PROC = '" + userId + "')"
					+ " OR (STATUS in ( '" + TaskCreationConstant.READY + "','"
					+ TaskCreationConstant.RESOLVED.toUpperCase() + "')))";
		}
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query updateTEQry = session.createSQLQuery(update);
		
		int count =  updateTEQry.executeUpdate();
		tx.commit();
		session.close();
		return count;
		
		
	}

	public int updateStatusChatbotInTaskEvents(String event, String status, String userId, String userDisplay,
			Boolean isAdmin) {

		String update = "UPDATE TASK_EVENTS SET STATUS ='" + status.toUpperCase() + "', CUR_PROC = '" + userId + "',"
				+ "CUR_PROC_DISP = '" + userDisplay + "', UPDATED_AT = '" + timeZoneConvertion.convertToUTC() + "'";
		if (status.equals(TaskCreationConstant.COMPLETED) || status.equals(TaskCreationConstant.DONE)
				|| status.equals(TaskCreationConstant.APPROVE) || status.equals(TaskCreationConstant.REJECT)
				|| status.equals(TaskCreationConstant.ACCEPTED) || status.equals(TaskCreationConstant.DECLINED)) {
			update = update + ",COMPLETED_AT = '" + timeZoneConvertion.convertToUTC() + "'";
		}

		update = update + " WHERE EVENT_ID= '" + event + "' ";

		if (!isAdmin) {
			if (status.equals(TaskCreationConstant.RESERVED))
				update = update + "AND (STATUS = '" + TaskCreationConstant.READY + "' OR CUR_PROC = '" + userId + "')";
		}

		if (status.equals(TaskCreationConstant.COMPLETED) || status.equals(TaskCreationConstant.DONE)
				|| status.equals(TaskCreationConstant.APPROVED) || status.equals(TaskCreationConstant.REJECTED)
				|| status.equals(TaskCreationConstant.ACCEPTED) || status.equals(TaskCreationConstant.DECLINED)) {
			update = update + " AND ((STATUS = '" + TaskCreationConstant.RESERVED + "' AND CUR_PROC = '" + userId + "')"
					+ " OR (STATUS in ( '" + TaskCreationConstant.READY + "','"
					+ TaskCreationConstant.RESOLVED.toUpperCase() + "')))";
		}
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query updateTEQry = session.createSQLQuery(update);
		
		int count = updateTEQry.executeUpdate();
		tx.commit();
		session.close();
		return count;
		
	}

	public void updateStatusInParentTask(String event, String status, String userId, String userDisplay) {

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String update = "UPDATE TASK_EVENTS SET STATUS ='" + status + "', CUR_PROC = '" + userId + "',"
				+ "CUR_PROC_DISP = '" + userDisplay + "', UPDATED_AT = '" + timeZoneConvertion.convertToUTC()
				+ "' WHERE EVENT_ID= '" + event + "' ";

		Query updateTEQry = session.createSQLQuery(update);
		updateTEQry.executeUpdate();
		tx.commit();
		session.close();

	}

	public String getParentTaskId(String eventId) {

		String eventDetial = "SELECT TOP 1 EVENT_ID FROM TASK_EVENTS "
				+ "WHERE PROCESS_ID = (SELECT PROCESS_ID FROM TASK_EVENTS WHERE EVENT_ID = '" + eventId + "')"
				+ " AND EVENT_ID<>'" + eventId + "' " + "ORDER BY UPDATED_AT DESC";
		Query eventQry = getSession().createSQLQuery(eventDetial);
		String parentEvent = (String) eventQry.uniqueResult();

		return parentEvent;
	}

	public TaskEventsDto getPreviousTaskDetail(String eventId) {
		TaskEventsDto taskEventsDto = null;
		Session session = sessionFactory.openSession();
//		String taskSelect = "SELECT DESCRIPTION,PROC_NAME,PROCESS_ID,CREATED_BY,SUBJECT,FORWARDED_BY,URL,NAME "
//				+ "FROM TASK_EVENTS WHERE EVENT_ID = '" + eventId + "'";
		String taskSelect = "SELECT  TE.DESCRIPTION,PROC_NAME,TE.PROCESS_ID,CREATED_BY,TE.SUBJECT,FORWARDED_BY,TE.URL,TE.NAME , TS.OWNER_SEQU_TYPE " + 
				" FROM TASK_EVENTS TE JOIN PROCESS_TEMPLATE_VALUE  PT ON TE.NAME = PT.TASK_NAME AND TE.PROCESS_ID = PT.PROCESS_ID " + 
				" JOIN TASK_OWNER_TEMPLATE_SEQUENCE TS ON TS.TEMPLATE_ID = PT.TEMPLATE_ID WHERE TE.EVENT_ID = '" + eventId + "'";
		Query detailQry = session.createSQLQuery(taskSelect);
		Object[] taskDetail = (Object[]) detailQry.uniqueResult();

		taskEventsDto = new TaskEventsDto();
		// taskEventsDto.setDescription(taskDetail[0].toString());
		taskEventsDto.setProcessName(taskDetail[1].toString());
		taskEventsDto.setProcessId(taskDetail[2].toString());
		taskEventsDto.setCreatedBy(taskDetail[3].toString());
		// taskEventsDto.setSubject(taskDetail[4].toString());
		taskEventsDto.setDetailUrl(ServicesUtil.isEmpty(taskDetail[6]) ? null : taskDetail[6].toString());
		taskEventsDto.setName(taskDetail[7].toString());
		taskEventsDto.setOwnerSeqType(taskDetail[8].toString());
		// taskEventsDto.setForwardedBy(taskDetail[5].toString());

		return taskEventsDto;
	}

	public TaskEventsDto setTaskEvents(String generateEventId, TaskEventsDto taskEventsDto, String taskName,
			String taskType) {
		TaskEventsDto taskEvent = null;

		ProcessConfigDto sla = processConfigDao.getProcessDetail(taskEventsDto.getProcessName());
		taskEvent = new TaskEventsDto();
		taskEvent.setEventId(generateEventId);
		taskEvent.setName(taskName);
		taskEvent.setProcessId(taskEventsDto.getProcessId());
		taskEvent.setCreatedBy(taskEventsDto.getCreatedBy());
		taskEvent.setCurrentProcessor("");
		taskEvent.setCurrentProcessorDisplayName("");
		taskEvent.setDescription(taskEventsDto.getDescription());
		/*
		 * taskEvent.setForwardedAt(ServicesUtil.convertAdminFromStringToDate(
		 * timeZoneConvertion.convertToUTC()));
		 * taskEvent.setForwardedBy(taskEventsDto.getForwardedBy());
		 */
		taskEvent.setOrigin(TaskCreationConstant.TASK_MANAGEMENT_ORIGIN);
		taskEvent.setPriority("0");
		taskEvent.setCompletionDeadLine(null);
		taskEvent.setProcessName(taskEventsDto.getProcessName());
		taskEvent.setSlaDueDate(null);
		taskEvent.setStatus(TaskCreationConstant.NEW);
		taskEvent.setStatusFlag("0");
		taskEvent.setSubject(taskEventsDto.getSubject());
		taskEvent.setTaskMode("NULL");
		taskEvent.setTaskType(taskType);
		taskEvent.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
		taskEvent.setCompletedAt(null);
		taskEvent.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(sla.getSla().trim()));
		taskEvent.setSlaDueDate(cal.getTime());
		taskEvent.setCompletionDeadLine(cal.getTime());
		if (!ServicesUtil.isEmpty(taskEventsDto.getDetailUrl()))
			taskEvent
					.setDetailUrl(sla.getDetailURL() + TaskCreationConstant.URL_REQUEST_PARAM + taskEvent.getEventId());

		return taskEvent;
	}

	public void updateDescription(String processId, String value) {

		Query updateTE = getSession().createSQLQuery(
				"UPDATE TASK_EVENTS SET DESCRIPTION = '" + value + "' " + "WHERE PROCESS_ID = '" + processId + "' ");
		updateTE.executeUpdate();
	}

	public void updateForwardedByInTE(String eventId, String userName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query updateTE = session
				.createSQLQuery("UPDATE TASK_EVENTS SET FORWARDED_BY = '" + userName + "' " + ",FORWARDED_AT = '"
						+ timeZoneConvertion.convertToUTC() + "'" + "WHERE EVENT_ID = '" + eventId + "' ");
		updateTE.executeUpdate();
		tx.commit();
		session.close();
	}

	public TaskEventsDto setTaskEvents(ProcessTemplateValueDto processTemplateValueDto,
			Map<String, String> attrKeyValues, TaskEventsDto taskEventsDtos) {
		TaskEventsDto taskEvent = null;
		System.out.println(" [WBP-Dev][WORKBOX][adhoctask][taskeventdao][settaskevents]");
		ProcessConfigDto sla = processConfigDao.getProcessDetail(taskEventsDtos.getProcessName());
		String requestId = processEventDao.getProcessRequestId(processTemplateValueDto.getProcessId());
		taskEvent = new TaskEventsDto();
		taskEvent.setEventId(processTemplateValueDto.getEventId());
		taskEvent.setName(processTemplateValueDto.getTaskName());
		taskEvent.setProcessId(processTemplateValueDto.getProcessId());
		taskEvent.setCreatedBy(taskEventsDtos.getCreatedBy());
		taskEvent.setCurrentProcessor("");
		taskEvent.setCurrentProcessorDisplayName("");
		String description = ServicesUtil.isEmpty(processTemplateValueDto.getDescription()) ? ""
				: processTemplateValueDto.getDescription();
		String[] desc = description.split("[$]");
		description = "";
		String s = "";
		for (String str : desc) {
			if (str.contains("{")) {
				s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
				if (attrKeyValues.containsKey(s))
					str = str.replace("{" + s + "}", attrKeyValues.get(s));
			}
			description = description + str;
		}
		String subject = ServicesUtil.isEmpty(processTemplateValueDto.getSubject()) ? ""
				: processTemplateValueDto.getSubject();
		desc = subject.split("[$]");
		subject = "";
		for (String str : desc) {
			if (str.contains("{")) {
				s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
				if (attrKeyValues.containsKey(s))
					str = str.replace("{" + s + "}", attrKeyValues.get(s));
			}
			System.err.println(str);
			subject += str;
		}
		taskEvent.setDescription(subject+" - "+requestId);
		taskEvent.setSubject(description);
		taskEvent.setOrigin(TaskCreationConstant.TASK_MANAGEMENT_ORIGIN);
		taskEvent.setPriority("0");
		taskEvent.setProcessName(taskEventsDtos.getProcessName());
		taskEvent.setSlaDueDate(null);
		taskEvent.setStatus(TaskCreationConstant.NEW);
		taskEvent.setStatusFlag("0");
		taskEvent.setTaskMode("NULL");
		taskEvent.setTaskType(processTemplateValueDto.getTaskType());
		taskEvent.setCreatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
		taskEvent.setCompletedAt(null);
		taskEvent.setUpdatedAt(ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToUTC()));
		
		String businessStatus = statusConfigDao.getBusinessStatus(taskEventsDtos.getProcessName(),
				taskEventsDtos.getName(), taskEvent.getStatus());
		taskEvent.setBusinessStatus(ServicesUtil.isEmpty(businessStatus) ? null : businessStatus);

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(sla.getSla().trim()));
		taskEvent.setSlaDueDate(cal.getTime());
		taskEvent.setCompletionDeadLine(cal.getTime());

		if (!ServicesUtil.isEmpty(processTemplateValueDto.getUrl())) {

			if (taskEvent.getProcessName().equals("ResourcePlanningWorkflowForRMG")) {

				if (taskEvent.getName().contains("L5")) {
					taskEvent.setDetailUrl(
							processTemplateValueDto.getUrl() + "&taskId=" + taskEvent.getEventId() + "&exception=true");
				} else {
					taskEvent.setDetailUrl(processTemplateValueDto.getUrl() + "&taskId=" + taskEvent.getEventId());
				}
			} else if ((taskEvent.getProcessName().toLowerCase()).startsWith("dt")) {
				taskEvent.setDetailUrl(processTemplateValueDto.getUrl() + "/" + taskEvent.getEventId());
			} else
				taskEvent.setDetailUrl(processTemplateValueDto.getUrl() + TaskCreationConstant.URL_REQUEST_PARAM
						+ taskEvent.getEventId());

		} else if (!ServicesUtil.isEmpty(sla.getDetailURL())) {
			if (taskEvent.getProcessName().equals("ResourcePlanningWorkflowForRMG")) {
				if (taskEvent.getName().contains("L5")) {
					taskEvent
							.setDetailUrl(sla.getDetailURL() + "&taskId=" + taskEvent.getEventId() + "&exception=true");

				} else {
					taskEvent.setDetailUrl(sla.getDetailURL() + "&taskId=" + taskEvent.getEventId());
				}
			} else if ((taskEvent.getProcessName().toLowerCase()).startsWith("dt")) {
				taskEvent.setDetailUrl(sla.getDetailURL() + "/" + taskEvent.getEventId());
			} else
				taskEvent.setDetailUrl(
						sla.getDetailURL() + TaskCreationConstant.URL_REQUEST_PARAM + taskEvent.getEventId());

		}

		return taskEvent;
	}

	public String validateNextTask(TaskEventsDto taskEventsDto) {
		String res = TaskCreationConstant.SUCCESS;
		String targetId = "";
		String sourceId = "";
		try {
			String fetchDetail = "SELECT PROCESS_ID,TEMPLATE_ID,SOURCE_ID,TARGET_ID FROM PROCESS_TEMPLATE_VALUE"
					+ " WHERE PROCESS_ID = '" + taskEventsDto.getProcessId() + "' AND TASK_NAME = '"
					+ taskEventsDto.getName() + "'";
			Query fetchDetailQry = this.getSession().createSQLQuery(fetchDetail);
			Object[] curTaskDetail = (Object[]) fetchDetailQry.uniqueResult();
			targetId = ServicesUtil.isEmpty(curTaskDetail[3]) ? null : curTaskDetail[3].toString();

			if (targetId == null) {
				Query fetchPendingTasks = this.getSession()
						.createSQLQuery("SELECT COUNT(*) FROM TASK_EVENTS " + "WHERE PROCESS_ID = '"
								+ taskEventsDto.getProcessId() + "' AND STATUS IN ('READY','RESERVED')");
				BigInteger count = (BigInteger) fetchPendingTasks.uniqueResult();
				if (count.intValue() > 0)
					return res;
				else if (count.intValue() == 0)
					return TaskCreationConstant.FAILURE;
			} else if ((targetId.split(",").length) > 1) {
				return TaskCreationConstant.FAILURE;
			} else {
				Query fetchNextTaskQry = this.getSession().createSQLQuery("SELECT TARGET_ID,SOURCE_ID,TASK_NAME"
						+ " FROM PROCESS_TEMPLATE_VALUE " + "WHERE PROCESS_ID = '" + taskEventsDto.getProcessId()
						+ "' AND TEMPLATE_ID = '" + targetId + "'");
				Object[] fetchNextTask = (Object[]) fetchNextTaskQry.uniqueResult();
				sourceId = ServicesUtil.isEmpty(fetchNextTask[1]) ? null : fetchNextTask[1].toString();
				if (sourceId != null) {
					if (sourceId.split(",").length == 1) {
						return TaskCreationConstant.FAILURE;
					} else {
						sourceId = sourceId.replace(targetId, "");
						Query fetchPendingTasks = this.getSession().createSQLQuery("SELECT COUNT(*) FROM TASK_EVENTS TE "
								+ "INNER JOIN PROCESS_TEMPLATE_VALUE PTV ON PTV.TASK_NAME = TE.NAME "
								+ "WHERE PTV.PROCESS_ID = '" + taskEventsDto.getProcessId() + "' AND "
								+ "TE.PROCESS_ID = '" + taskEventsDto.getProcessId() + "' AND "
								+ "TE.STATUS IN ('READY','RESERVED') AND PTV.TEMPLATE_ID IN ('"
								+ sourceId.replace(",", "','") + "')");
						BigInteger count = (BigInteger) fetchPendingTasks.uniqueResult();
						if (count.intValue() > 0)
							return res;
						else
							return TaskCreationConstant.FAILURE;
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]ERROR validating next task" + e.getMessage());
		}
		return res;
	}

	public String getProcessName(String eventId) {
		Query processNameQry = getSession()
				.createSQLQuery("SELECT PROC_NAME FROM TASK_EVENTS " + "WHERE EVENT_ID = '" + eventId + "'");
		String processName = (String) processNameQry.uniqueResult();
		return processName;
	}

	public String getProcessId(String eventId) {
		Query processIdQry = getSession()
				.createSQLQuery("SELECT PROCESS_ID FROM TASK_EVENTS " + "WHERE EVENT_ID = '" + eventId + "'");
		String processId = (String) processIdQry.uniqueResult();
		return processId;
	}

	public String getRequestId(String taskId) {
		Query processIdQry = getSession()
				.createSQLQuery("SELECT pe.REQUEST_ID FROM TASK_EVENTS te"
						+ " inner join process_events pe on pe.process_id = te.process_id " + "WHERE EVENT_ID = '" + taskId + "'");
		String processId = (String) processIdQry.uniqueResult();
		return processId;
	}
	
	public List<TaskEventsDto> getAllNotStartedTasks(String processId) {
		List<TaskEventsDto> eventsDtos = new ArrayList<TaskEventsDto>();
		TaskEventsDto eventsDto = null;
		Query processIdQry = getSession()
				.createSQLQuery("select event_id,cur_proc from task_events where process_id ='"+processId+"' "
						+ "and status = 'NOT STARTED'");
		List<Object[]> result = processIdQry.list();
		
		for (Object[] obj : result) {
			eventsDto = new TaskEventsDto();
			eventsDto.setEventId(ServicesUtil.asString(obj[0]));
			eventsDto.setCurrentProcessor(ServicesUtil.asString(obj[1]));
			eventsDtos.add(eventsDto);
		}
		return eventsDtos;
	}
	
	public Integer updateStatus(String eventId){
		Session session = getSession();
		Transaction transaction = session.beginTransaction();
		Query updateTEQry = session.createSQLQuery("UPDATE TASK_EVENTS SET STATUS = 'RESERVED' WHERE"
				+ " EVENT_ID = '"+eventId+"'");
		
		int result = updateTEQry.executeUpdate();
		transaction.commit();
		session.close();
		return result;
		
	}
	
	public TaskEventsDto setTaskEvents(ProcessTemplateValueDto processTemplateValueDto,
			Map<String, String> attrKeyValues, TaskEventsDto taskEventsDtos,String status, String curProc,
			String curProcName , Date currentTime) {
		TaskEventsDto taskEvent = null;

		ProcessConfigDto sla = processConfigDao.getProcessDetail(taskEventsDtos.getProcessName());
		String requestId = processEventDao.getProcessRequestId(processTemplateValueDto.getProcessId());
		taskEvent = new TaskEventsDto();
		taskEvent.setEventId(processTemplateValueDto.getEventId());
		taskEvent.setName(processTemplateValueDto.getTaskName());
		taskEvent.setProcessId(processTemplateValueDto.getProcessId());
		taskEvent.setCreatedBy(taskEventsDtos.getCreatedBy());
		//taskEvent.setCurrentProcessor("");
		
		String description = ServicesUtil.isEmpty(processTemplateValueDto.getDescription()) ? ""
				: processTemplateValueDto.getDescription();
		String[] desc = description.split("[$]");
		description = "";
		String s = "";
		for (String str : desc) {
			if (str.contains("{")) {
				s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
				if (attrKeyValues.containsKey(s))
					str = str.replace("{" + s + "}", attrKeyValues.get(s));
			}
			description = description + str;
		}
		String subject = ServicesUtil.isEmpty(processTemplateValueDto.getSubject()) ? ""
				: processTemplateValueDto.getSubject();
		desc = subject.split("[$]");
		subject = "";
		for (String str : desc) {
			if (str.contains("{")) {
				s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
				if (attrKeyValues.containsKey(s))
					str = str.replace("{" + s + "}", attrKeyValues.get(s));
			}
			System.err.println(str);
			subject += str;
		}
		taskEvent.setDescription(subject.replaceAll("[{].*}", "").trim().replaceAll(" +", " ")+" - "+requestId);
		taskEvent.setSubject(description.replaceAll("[{].*}", "").trim().replaceAll(" +", " "));
		taskEvent.setOrigin(TaskCreationConstant.TASK_MANAGEMENT_ORIGIN);
		taskEvent.setPriority("0");
		taskEvent.setProcessName(taskEventsDtos.getProcessName());
		taskEvent.setSlaDueDate(null);
		taskEvent.setStatus(status);
		String businessStatus = statusConfigDao.getBusinessStatus(taskEventsDtos.getProcessName(),processTemplateValueDto.getTaskName() ,TaskCreationConstant.NEW);
		taskEvent.setStatusFlag("0");
		taskEvent.setBusinessStatus(ServicesUtil.isEmpty(businessStatus)?null:businessStatus);
		taskEvent.setTaskMode("NULL");
		taskEvent.setTaskType(processTemplateValueDto.getTaskType());
		taskEvent.setCreatedAt(currentTime);
		taskEvent.setCompletedAt(null);
		taskEvent.setUpdatedAt(currentTime);
		taskEvent.setOwnerId(curProc);
		taskEvent.setCurrentProcessor(curProc);
		taskEvent.setCurrentProcessorDisplayName(curProcName);
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(sla.getSla().trim()));
		taskEvent.setSlaDueDate(cal.getTime());
		taskEvent.setCompletionDeadLine(cal.getTime());

		if (!ServicesUtil.isEmpty(processTemplateValueDto.getUrl())) {

			if (taskEvent.getProcessName().equals("ResourcePlanningWorkflowForRMG")) {

				if (taskEvent.getName().contains("L5")) {
					taskEvent.setDetailUrl(
							processTemplateValueDto.getUrl() + "&taskId=" + taskEvent.getEventId() + "&exception=true");
				} else {
					taskEvent.setDetailUrl(processTemplateValueDto.getUrl() + "&taskId=" + taskEvent.getEventId());
				}
			} else if ((taskEvent.getProcessName().toLowerCase()).startsWith("dt")) {
				taskEvent.setDetailUrl(processTemplateValueDto.getUrl() + "/" + taskEvent.getEventId());
			} else
				taskEvent.setDetailUrl(processTemplateValueDto.getUrl() + TaskCreationConstant.URL_REQUEST_PARAM
						+ taskEvent.getEventId());

		} else if (!ServicesUtil.isEmpty(sla.getDetailURL())) {
			if (taskEvent.getProcessName().equals("ResourcePlanningWorkflowForRMG")) {
				if (taskEvent.getName().contains("L5")) {
					taskEvent
							.setDetailUrl(sla.getDetailURL() + "&taskId=" + taskEvent.getEventId() + "&exception=true");

				} else {
					taskEvent.setDetailUrl(sla.getDetailURL() + "&taskId=" + taskEvent.getEventId());
				}
			} else if ((taskEvent.getProcessName().toLowerCase()).startsWith("dt")) {
				taskEvent.setDetailUrl(sla.getDetailURL() + "/" + taskEvent.getEventId());
			} else
				taskEvent.setDetailUrl(
						sla.getDetailURL() + TaskCreationConstant.URL_REQUEST_PARAM + taskEvent.getEventId());

		}

		return taskEvent;
	}
	
	public String checkTaskType(String eventId) {
		
		String typeQuery = "select  ts.owner_sequ_type  from task_events te join process_template_value  pt" + 
								" on te.name = pt.task_name and te.process_id = pt.process_id join task_owner_template_sequence ts" +
								" on ts.template_id = pt.template_id where te.event_id = :eventId";
		return (String) this.getSession().createSQLQuery(typeQuery).setParameter("eventId", eventId).uniqueResult();
	}
	
	public List<TaskEventsDto> getAllPendingTask(String processId , String status) {
		List<TaskEventsDto> eventsDtos = new ArrayList<TaskEventsDto>();
		TaskEventsDto eventsDto = null;
		Query processIdQry = getSession()
				.createSQLQuery("select te.event_id,te.cur_proc,tw.task_owner from task_events te join task_owners tw on te.event_id = tw.event_id where process_id ='"+processId+"' "
						+ "and status in ('"+ status.replace(",", "','") + "')");
		List<Object[]> result = processIdQry.list();
		
		for (Object[] obj : result) {
			eventsDto = new TaskEventsDto();
			eventsDto.setEventId(ServicesUtil.asString(obj[0]));
			eventsDto.setCurrentProcessor(ServicesUtil.asString(obj[1]));
			eventsDto.setOwnerId(ServicesUtil.asString(obj[2]));
			eventsDtos.add(eventsDto);
		}
		return eventsDtos;
	}
	
	public Integer updateStatusToReady(String eventId){
		Session session = getSession();
		Transaction tx = session.beginTransaction();
 		Integer  result = session.createSQLQuery("UPDATE TASK_EVENTS SET STATUS = 'READY' WHERE"
				+ " EVENT_ID = '"+eventId+"'").executeUpdate();
		tx.commit();
 		session.close();
 		return result;
		
	}

}
