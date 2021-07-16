package oneapp.incture.workbox.demo.adapter_base.dao;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import oneapp.incture.workbox.demo.adapter_base.dto.AdhocTaskDetails;
import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskAudit;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskStatus;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystForm;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystFormData;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystLineItemsAndForms;
import oneapp.incture.workbox.demo.adapter_base.util.AnalystsLineItems;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;

@Repository("TaskEventsDao")
// ////@Transactional
public class TaskEventsDao extends BaseDao<TaskEventsDo, TaskEventsDto> {

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(TaskEventsDao.class);

	// private static final int _HIBERNATE_BATCH_SIZE = 500;

	@Override
	protected TaskEventsDto exportDto(TaskEventsDo entity) {
		TaskEventsDto taskEventsDto = new TaskEventsDto();
		taskEventsDto.setEventId(entity.getEventId());
		taskEventsDto.setProcessId(entity.getProcessId());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			taskEventsDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getName()))
			taskEventsDto.setName(entity.getName());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			taskEventsDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			taskEventsDto.setStatus(entity.getStatus());
		if (!ServicesUtil.isEmpty(entity.getCurrentProcessor()))
			taskEventsDto.setCurrentProcessor(entity.getCurrentProcessor());
		if (!ServicesUtil.isEmpty(entity.getPriority()))
			taskEventsDto.setPriority(entity.getPriority());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			taskEventsDto.setCreatedAt(entity.getCreatedAt());
		if (!ServicesUtil.isEmpty(entity.getCompletedAt()))
			taskEventsDto.setCompletedAt(entity.getCompletedAt());
		if (!ServicesUtil.isEmpty(entity.getCompletionDeadLine()))
			taskEventsDto.setCompletionDeadLine(entity.getCompletionDeadLine());
		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			taskEventsDto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getTaskMode()))
			taskEventsDto.setTaskMode(entity.getTaskMode());
		if (!ServicesUtil.isEmpty(entity.getStatusFlag()))
			taskEventsDto.setStatusFlag(entity.getStatusFlag());
		if (!ServicesUtil.isEmpty(entity.getCurrentProcessorDisplayName()))
			taskEventsDto.setCurrentProcessorDisplayName(entity.getCurrentProcessorDisplayName());
		if (!ServicesUtil.isEmpty(entity.getTaskType()))
			taskEventsDto.setTaskType(entity.getTaskType());
		if (!ServicesUtil.isEmpty(entity.getForwardedBy()))
			taskEventsDto.setForwardedBy(entity.getForwardedBy());
		if (!ServicesUtil.isEmpty(entity.getForwardedAt()))
			taskEventsDto.setForwardedAt(entity.getForwardedAt());
		if (!ServicesUtil.isEmpty(entity.getOrigin()))
			taskEventsDto.setOrigin(entity.getOrigin());
		if (!ServicesUtil.isEmpty(entity.getUrl()))
			taskEventsDto.setDetailUrl(entity.getUrl());
		if (!ServicesUtil.isEmpty(entity.getSlaDueDate()))
			taskEventsDto.setSlaDueDate(entity.getSlaDueDate());
		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			taskEventsDto.setUpdatedAt(entity.getUpdatedAt());
		if (!ServicesUtil.isEmpty(entity.getBusinessStatus()))
			taskEventsDto.setBusinessStatus(entity.getBusinessStatus());
		return taskEventsDto;
	}

	@Override
	protected TaskEventsDo importDto(TaskEventsDto fromDto) {
		TaskEventsDo entity = new TaskEventsDo();
		// entity.setTaskEventsDoPK(new TaskEventsDoPK());
		if (!ServicesUtil.isEmpty(fromDto.getEventId()))
			entity.setEventId(fromDto.getEventId());
		if (!ServicesUtil.isEmpty(fromDto.getProcessId()))
			entity.setProcessId(fromDto.getProcessId());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getCurrentProcessor()))
			entity.setCurrentProcessor(fromDto.getCurrentProcessor());
		if (!ServicesUtil.isEmpty(fromDto.getPriority()))
			entity.setPriority(fromDto.getPriority());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(fromDto.getCreatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getCompletedAt()))
			entity.setCompletedAt(fromDto.getCompletedAt());
		if (!ServicesUtil.isEmpty(fromDto.getCompletionDeadLine()))
			entity.setCompletionDeadLine(fromDto.getCompletionDeadLine());
		if (!ServicesUtil.isEmpty(fromDto.getCurrentProcessorDisplayName()))
			entity.setCurrentProcessorDisplayName(fromDto.getCurrentProcessorDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getDescription()))
			entity.setDescription(fromDto.getDescription());
		if (!ServicesUtil.isEmpty(fromDto.getName()))
			entity.setName(fromDto.getName());
		if (!ServicesUtil.isEmpty(fromDto.getSubject()))
			entity.setSubject(fromDto.getSubject());
		if (!ServicesUtil.isEmpty(fromDto.getProcessName()))
			entity.setProcessName(fromDto.getProcessName());
		if (!ServicesUtil.isEmpty(fromDto.getTaskMode()))
			entity.setTaskMode(fromDto.getTaskMode());
		if (!ServicesUtil.isEmpty(fromDto.getStatusFlag()))
			entity.setStatusFlag(fromDto.getStatusFlag());
		if (!ServicesUtil.isEmpty(fromDto.getTaskType()))
			entity.setTaskType(fromDto.getTaskType());
		if (!ServicesUtil.isEmpty(fromDto.getForwardedBy()))
			entity.setForwardedBy(fromDto.getForwardedBy());
		if (!ServicesUtil.isEmpty(fromDto.getForwardedAt()))
			entity.setForwardedAt(fromDto.getForwardedAt());
		if (!ServicesUtil.isEmpty(fromDto.getOrigin()))
			entity.setOrigin(fromDto.getOrigin());
		if (!ServicesUtil.isEmpty(fromDto.getDetailUrl()))
			entity.setUrl(fromDto.getDetailUrl());
		if (!ServicesUtil.isEmpty(fromDto.getSlaDueDate()))
			entity.setSlaDueDate(fromDto.getSlaDueDate());
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedAt()))
			entity.setUpdatedAt(fromDto.getUpdatedAt());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedBy()))
			entity.setCreatedBy(fromDto.getCreatedBy());
		if (!ServicesUtil.isEmpty(fromDto.getBusinessStatus()))
			entity.setBusinessStatus(fromDto.getBusinessStatus());
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<String> getOpenTaskIds() throws NoResultFault {
		Criteria criteria = this.getSession().createCriteria(TaskEventsDo.class);
		criteria.add(Restrictions.ne("status", "CANCELED"));
		criteria.add(Restrictions.ne("status", "COMPLETED"));
		criteria.setProjection(Projections.property("eventId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<String> taskIdList = (List<String>) criteria.list();
		if (ServicesUtil.isEmpty(taskIdList)) {
			throw new NoResultFault("NO RECORD FOUND");
		}

		return taskIdList;
	}

	@SuppressWarnings("unchecked")
	public Map<String, TaskEventsDo> getAllOpenTasksDo() {
		Criteria criteria = this.getSession().createCriteria(TaskEventsDo.class);
		criteria.add(Restrictions.ne("status", "CANCELED"));
		criteria.add(Restrictions.ne("status", "COMPLETED"));
		criteria.add(Restrictions.eq("proc_name", "'" + PMCConstant.PROCESS_NAME_DOC_APPROVAL + "'"));
		List<TaskEventsDo> taskEventsDo = (criteria.list());
		Map<String, TaskEventsDo> taskMap = new HashMap<String, TaskEventsDo>();
		if (!ServicesUtil.isEmpty(taskEventsDo)) {
			for (TaskEventsDo do1 : taskEventsDo) {
				taskMap.put(do1.getEventId(), do1);
			}
		}
		return taskMap;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getUserTaskCount(String userId, String processName, String requestId, String labelValue,
			String status, Date startDate, Date endDate) throws NoResultFault {
		// DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yy hh:mm:ss
		// a");
		DateFormat newDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tempQuery = "";
		String query = "select count(te.CREATED_AT) AS TASK_COUNT, (te.CREATED_AT)"
				// "select count(te.CREATED_AT) AS TASK_COUNT,
				// trunc(te.CREATED_AT)
				+ "AS CREATED_DATE from task_events te left join task_owners tw on te.event_id = tw.event_id where tw.task_owner ='"
				+ userId + "'";
		String groupQuery = " group by (te.CREATED_AT) ORDER BY (te.CREATED_AT)";
		// " group by trunc(te.CREATED_AT) ORDER BY trunc(te.CREATED_AT)";
		if (!ServicesUtil.isEmpty(processName) && !processName.equals(PMCConstant.SEARCH_ALL)) {
			tempQuery = tempQuery + " and te.PROCESS_ID IN (select D.process_id from PROCESS_EVENTS D where D.name = '"
					+ processName + "')";
		}
		if (!ServicesUtil.isEmpty(requestId)) {
			tempQuery = tempQuery
					+ " and te.PROCESS_ID IN (select D.process_id from PROCESS_EVENTS D where D.REQUEST_ID = '"
					+ requestId + "')";
		}
		if (!ServicesUtil.isEmpty(labelValue)) {
			tempQuery = tempQuery
					+ " and te.PROCESS_ID IN (select D.process_id from PROCESS_EVENTS D where D.SUBJECT like '%"
					+ labelValue + "%')";
		}
		if (!ServicesUtil.isEmpty(status)) {
			if (PMCConstant.SEARCH_READY.equalsIgnoreCase(status)) {
				tempQuery = tempQuery + " and te.STATUS = '" + status + "'";
			} else if (PMCConstant.SEARCH_RESERVED.equalsIgnoreCase(status)) {
				tempQuery = tempQuery + " and te.STATUS = '" + status + "' and tw.IS_PROCESSED = 1";
			} else {
				tempQuery = tempQuery + " and (te.STATUS = '" + PMCConstant.TASK_STATUS_READY + "' or (te.STATUS = '"
						+ PMCConstant.TASK_STATUS_RESERVED + "' and tw.IS_PROCESSED = 1))";
			}
		}
		if (!ServicesUtil.isEmpty(startDate) && !ServicesUtil.isEmpty(endDate)) {
			tempQuery = tempQuery + " and te.CREATED_AT between " + "'" + newDf.format(startDate) + "' and '"
					+ newDf.format(endDate) + "'";
			// + "TO_DATE('" + dateFormatter.format(startDate) + "', 'DD/MM/YY
			// hh:mi:ss AM') and TO_DATE('" + dateFormatter.format(endDate) +
			// "', 'DD/MM/YY hh:mi:ss PM')";
		}
		query = query + tempQuery + groupQuery;
		logger.error("get - " + query);
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (ServicesUtil.isEmpty(resultList)) {
			throw new NoResultFault("NO RECORD FOUND");
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getTaskCountByOwner(String userId, String processName, String requestId, String labelValue,
			String status, Date startDate, Date endDate) {
		String tempQuery = "";
		// DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yy hh:mm:ss
		// a");
		DateFormat newDf = new SimpleDateFormat("yyyy-MM-dd");
		String queryColumn = null;
		if (PMCConstant.SEARCH_COMPLETED.equalsIgnoreCase(status)) {
			queryColumn = "COMPLETED_AT";
			tempQuery = tempQuery + " and te.STATUS = '" + status + "'";
		} else {
			queryColumn = "CREATED_AT";
			if (PMCConstant.SEARCH_READY.equalsIgnoreCase(status)) {
				tempQuery = tempQuery + " and te.STATUS = '" + status + "'";
			} else if (PMCConstant.SEARCH_RESERVED.equalsIgnoreCase(status)) {
				tempQuery = tempQuery + " and te.STATUS = '" + status + "' and tw.IS_PROCESSED = 1";
			} else {
				tempQuery = tempQuery + " and (te.STATUS = '" + PMCConstant.TASK_STATUS_READY + "' or (te.STATUS = '"
						+ PMCConstant.TASK_STATUS_RESERVED + "' and tw.IS_PROCESSED = 1))";
			}
		}
		String query = "SELECT to_date(te." + queryColumn + ") AS TASK_DATE, COUNT(to_date(te." + queryColumn
		// "SELECT TRUNC(te." + queryColumn + ") AS TASK_DATE, COUNT(TRUNC(te."
		// + queryColumn
				+ ")) AS TASK_COUNT from task_owners tw left join task_events te on tw.event_id = te.event_id left join process_events pe on pe.process_id = te.process_id where tw.task_owner='"
				+ userId + "'";
		if (!ServicesUtil.isEmpty(processName) && !processName.equals(PMCConstant.SEARCH_ALL)) {
			tempQuery = tempQuery
					+ " and pe.PROCESS_ID IN (select D.process_id from PROCESS_EVENTS D where D.name IN ('"
					+ processName + "'))";
		}
		if (!ServicesUtil.isEmpty(requestId)) {
			tempQuery = tempQuery + " and pe.REQUEST_ID = '" + requestId + "'";
		}
		if (!ServicesUtil.isEmpty(labelValue)) {
			tempQuery = tempQuery + " and pe.SUBJECT like '%" + labelValue + "%'";
		}
		if (!ServicesUtil.isEmpty(startDate) && !ServicesUtil.isEmpty(endDate)) {
			if (PMCConstant.SEARCH_COMPLETED.equalsIgnoreCase(status))
				tempQuery = tempQuery + " and to_date(te.COMPLETED_AT) between ";
			else
				tempQuery = tempQuery + " and to_date(te.CREATED_AT) between ";
			tempQuery = tempQuery + "TO_DATE('" + newDf.format(startDate) + "') and TO_DATE('" + newDf.format(endDate)
					+ "')";
			// "TO_DATE('"+dateFormatter.format(startDate) + "', 'DD/MM/YY
			// hh:mi:ss AM') and TO_DATE('" + dateFormatter.format(endDate) +
			// "', 'DD/MM/YY hh:mi:ss PM')";
		}
		if (!PMCConstant.SEARCH_COMPLETED.equalsIgnoreCase(status))
			tempQuery = tempQuery + " and pe.status='" + PMCConstant.PROCESS_STATUS_IN_PROGRESS + "'";
		String groupQuery = " group by to_date(te." + queryColumn + ") ORDER BY to_date(te." + queryColumn + ")";
		// " group by trunc(te." + queryColumn + ") ORDER BY trunc(te." +
		// queryColumn + ")";
		query = query + tempQuery + groupQuery;
		logger.error("get_getTaskCountByOwner - " + query);
		Query q = this.getSession().createSQLQuery(query);
		List<Object[]> resultList = q.list();
		if (ServicesUtil.isEmpty(resultList)) {
			try {
				throw new NoResultFault("NO RECORD FOUND");
			} catch (NoResultFault e) {
				logger.error("Error : " + e);
				logger.error("Error : " + e.getLocalizedMessage());
			}
		}
		return resultList;
	}

	@SuppressWarnings("unused")
	private String getDateDifferenceInHours(Date date) {
		long t1 = new Date().getTime();
		long t2 = date.getTime();
		long diffinHrs = (t1 - t2) / (60 * 60 * 1000) % 60;
		return String.valueOf(diffinHrs);

	}

	@SuppressWarnings("unchecked")
	public List<TaskEventsDo> checkIfTaskInstanceExists(String instanceId) {
		Query query = this.getSession()
				.createSQLQuery("select te from TaskEventsDo te where te.taskEventsDoPK.eventId =:instanceId");
		query.setParameter("instanceId", instanceId);
		List<TaskEventsDo> taskEventsDos = (List<TaskEventsDo>) query.list();
		if (taskEventsDos.size() > 0) {
			return taskEventsDos;

		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public TaskEventsDto getTaskInstanceIfExists(String instanceId) {
		System.err.println("[WBP-Dev]check for instance");
		Query query = this.getSession().createQuery("select te from TaskEventsDo te where te.eventId = :instanceId");
		query.setParameter("instanceId", instanceId);
		List<TaskEventsDo> taskEventsDos = (List<TaskEventsDo>) query.list();
		System.err.println("[WBP-Dev]Already Preesent task" + taskEventsDos);

		if (taskEventsDos.size() > 0) {
			TaskEventsDto taskEventDto = exportDto(taskEventsDos.get(0));
			return taskEventDto;

		} else {
			System.err.println("[WBP-Dev]retruning null");
			return null;
		}
	}

	public TaskEventsDto saveOrUpdateTask(TaskEventsDto dto) throws Exception {

		this.getSession().saveOrUpdate(importDto(dto));
		return dto;

	}

	public String createTaskInstance(TaskEventsDto dto) {
		// logger.error("[PMC][TaskEventsDao][createTaskInstance]initiated with
		// " + dto);

		try {
			this.create(dto);
			return "SUCCESS";
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][createTaskInstance][error] " + e);
		}
		return "FAILURE";

	}

	public String updateTaskInstance(TaskEventsDto dto) {
		// logger.error("[PMC][TaskEventsDao][updateTaskInstance]initiated with
		// " + dto);
		try {
			update(dto);
			return "SUCCESS";
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][updateTaskInstance][error] " + e);
		}
		return "FAILURE";

	}

	public String updateTaskEventToReady(String instanceId) {
		// logger.error("[PMC][TaskOwnersDao][setTaskOwnersToReady][instanceId]"+instanceId);
		try {
			String queryString = "Update TaskEventsDo te set te.status = 'READY' , te.currentProcessor = '' , te.currentProcessorDisplayName = '' where te.taskEventsDoPK.eventId = '"
					+ instanceId + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			if (q.executeUpdate() > 0) {
				return "SUCCESS";
			}
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][setTaskOwnersToReady][error]" + e);
		}
		return "FAILURE";
	}

	public String updateTaskEventToCompleted(String instanceId) {
		// logger.error("[PMC][TaskOwnersDao][setTaskOwnersToReady][instanceId]"+instanceId);
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			String queryString = "Update TASK_EVENTS TE set TE.STATUS= 'COMPLETED' where TE.EVENT_ID ='" + instanceId
					+ "'";
			System.err.println(
					"[WBP-Dev]TaskEventsDao.updateTaskEventToCompleted() status update query : " + queryString);
			Query q = session.createSQLQuery(queryString);
			int value = q.executeUpdate();
			tx.commit();
			session.close();
			
			if (q.executeUpdate() > 0) {
				return "SUCCESS";
			}
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][updateTaskEventToCompleted][error]" + e);
		}
		return "FAILURE";
	}
	
	public String updateTaskEventToCompleted(String instanceId,String userId, String userName) {
		// logger.error("[PMC][TaskOwnersDao][setTaskOwnersToReady][instanceId]"+instanceId);
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			String queryString = "Update TASK_EVENTS TE set TE.STATUS= 'COMPLETED'"
					+ " , CUR_PROC = '" + userId + "',"
					+ "CUR_PROC_DISP = '" + userName + "', UPDATED_AT = '" + ServicesUtil.convertToUTC() + "'"
					+ ", COMPLETED_AT = '" + ServicesUtil.convertToUTC() + "' "
					+ " where TE.EVENT_ID ='" + instanceId
					+ "'";
			System.err.println(
					"[WBP-Dev]TaskEventsDao.updateTaskEventToCompleted() status update query : " + queryString);
			Query q = session.createSQLQuery(queryString);
			int value = q.executeUpdate();
			tx.commit();
			session.close();
			
			if (q.executeUpdate() > 0) {
				return "SUCCESS";
			}
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][updateTaskEventToCompleted][error]" + e);
		}
		return "FAILURE";
	}

	public String updateTaskEventToReserved(String instanceId, String user, String userDisplay) {
		try {
			String queryString = "Update TaskEventsDo te set te.status = 'RESERVED' , te.currentProcessor = '" + user
					+ "' , te.currentProcessorDisplayName = '" + userDisplay + "' where te.taskEventsDoPK.eventId = '"
					+ instanceId + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			if (q.executeUpdate() > 0) {
				return "SUCCESS";
			}
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][updateTaskEventToReserved][error]" + e);
		}
		return "FAILURE";
	}

	public String checkIfTaskIsReserved(String userId, String instanceId) {

		try {
			String queryString = "select count(te) from TaskEventsDo te where te.status = 'RESERVED' and te.taskEventsDoPK.eventId = '"
					+ instanceId + "' and te.currentProcessor = '" + userId + "'";
			Query q = this.getSession().createSQLQuery(queryString);
			Long count = (Long) q.uniqueResult();
			if (count > 0) {
				return "SUCCESS";
			}
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][checkIfTaskIsReserved][error]" + e);
		}
		return "FAILURE";

	}

	public String checkIfTaskIsReady(String instanceId) {

		try {
			String queryString = "select count(te) from TaskEventsDo te where te.status = 'READY' and te.taskEventsDoPK.eventId = '"
					+ instanceId + "' ";
			Query q = this.getSession().createSQLQuery(queryString);
			Long count = (Long) q.uniqueResult();
			if (count > 0) {
				return "SUCCESS";
			}
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][checkIfTaskIsReady][error]" + e);
		}
		return "FAILURE";

	}

//	@SuppressWarnings("unchecked")
//	public List<TaskEventsDo> getTaskDetailsByProcessId(String processId) {
//
//		Query query = this.getSession().createQuery("select te from TaskEventsDo te where te.processId =:processId");
//		query.setParameter("processId", processId);
//		return query.list();
//	}
	
	@SuppressWarnings("unchecked")
	public List<TaskEventsDo> getTaskDetailsByProcessId(String processId) {

		Query query = this.getSession().createQuery("select te from TaskEventsDo te where te.processId =:processId ");
		query.setParameter("processId", processId);
		List<TaskEventsDo> taskEventsDtos = query.list();
		Map<String, AdhocTaskDetails> templateMap = new LinkedHashMap<>();
		if(!ServicesUtil.isEmpty(taskEventsDtos)) {
			if(!taskEventsDtos.get(0).getOrigin().equals("Ad-hoc")) {
				return taskEventsDtos;
			}
			
			//fetching template details with order by condition
			String templateQuery = "select pt.template_id , te.event_id , ts.order_by , ts.owner_sequ_type from task_events te join process_template_value  pt " +
						        " on te.name = pt.task_name and te.process_id = pt.process_id join task_owner_template_sequence ts " +
								" on ts.template_id = pt.template_id where te.process_id = :processId order by te.created_at";
			List<Object[]> templateObjects = this.getSession().createSQLQuery(templateQuery).setParameter("processId", processId).list();
			AdhocTaskDetails adhocTaskDetails = null;
			for(Object[] templateObject : templateObjects) {
				String templateId = (String) templateObject[0];
				if(templateMap.containsKey(templateId)) {
					adhocTaskDetails = templateMap.get(templateId);
					adhocTaskDetails.setEventIds(adhocTaskDetails.getEventIds() + (String)templateObject[1] + "','" );
				}
				else {
					adhocTaskDetails = new AdhocTaskDetails();
					adhocTaskDetails.setEventIds("'" + (String)templateObject[1] + "','");
					adhocTaskDetails.setOrderBy((String)templateObject[2]);
					adhocTaskDetails.setOwnerTypeSeq((String)templateObject[3]);
					templateMap.put(templateId, adhocTaskDetails);
				}
			}
			taskEventsDtos = new ArrayList<>();
			for(String templateId : templateMap.keySet()) {
				adhocTaskDetails = templateMap.get(templateId);
				String eventIds = adhocTaskDetails.getEventIds().substring(0 , adhocTaskDetails.getEventIds().length() - 2);
				String taskQuery = null;
				if(adhocTaskDetails.getOwnerTypeSeq().equals(TaskCreationConstant.SEQUENTIAL_TASK)) {
					taskQuery = "select te.*  from task_events te join task_owners tw on te.event_id = tw.event_id  join" + 
							" user_idp_mapping uim on tw.task_owner = uim.user_id " + 
							" where te.event_id in("+ eventIds +") and tw.is_processed = 0 order by uim.user_first_name "+  adhocTaskDetails.getOrderBy()  +
							", uim.user_last_name " + adhocTaskDetails.getOrderBy() + " , te.completed_at desc";
				}
				else if (adhocTaskDetails.getOwnerTypeSeq().equals(TaskCreationConstant.PARALLEL_TASK)) {
					taskQuery = "select te.*  from task_events te join task_owners tw on te.event_id = tw.event_id  join" + 
							" user_idp_mapping uim on tw.task_owner = uim.user_id " + 
							" where te.event_id in("+ eventIds +") and tw.is_processed = 0 order by te.completed_at desc ,  uim.user_first_name "+  adhocTaskDetails.getOrderBy()  +
							", uim.user_last_name " + adhocTaskDetails.getOrderBy();
				}
				else {
					//for group only one taskId available
					taskQuery = "select te.* from task_events te where te.event_id = " + eventIds;
				}
				List<TaskEventsDo> currentTasks = this.getSession().createSQLQuery(taskQuery)		
						.addEntity(TaskEventsDo.class)
						.list();
				for(TaskEventsDo currentTask : currentTasks) {
//					currentTask.se
					taskEventsDtos.add(currentTask);
				}
				
						
			}
		}
		System.err.println("Tasks : " + new Gson().toJson(taskEventsDtos));
		return taskEventsDtos;
	}

	public Integer getTotalTasksCountByProcessName(String processName) {

		String sqlQuery = "select avg(counts) from ( select count(te.process_id) as counts from task_events te join process_events pe on"
				+ " te.process_Id=pe.process_id where proc_name=:processName and pe.status='COMPLETED' group by te.process_id order "
				+ "by count(te.process_id) desc ) ";

		Query query = this.getSession().createSQLQuery(sqlQuery);

		query.setParameter("processName", processName);
		Object resCount = query.uniqueResult();
		System.err.println("TaskEventsDao.getTotalTasksCountByProcessName() count is : " + resCount);
		if (!ServicesUtil.isEmpty(resCount)) {
			BigDecimal res = (BigDecimal) resCount;
			System.err.println("TaskEventsDao.getTotalTasksCountByProcessName() total tasks level : " + res.intValue()
					+ " of the process : " + processName);
			return res.intValue();
		}

		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getSLAByNameAndProcessId(String name, String processId) {

		String qry = "select A.SLA AS SLA, A.URGENT_SLA AS URGENT_SLA from TASK_SLA A where TASK_DEF='" + name
				+ "' and PROC_NAME IN( select PROC_NAME from PROCESS_EVENTS where PROCESS_ID = '" + processId + "')";
		logger.error("Query: " + qry);
		Query query = this.getSession().createSQLQuery(qry.toString());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<TaskEventsDto> getAllOpenTasks() {
		Criteria criteria = this.getSession().createCriteria(TaskEventsDo.class);
		criteria.add(Restrictions.ne("status", "CANCELED"));
		criteria.add(Restrictions.ne("status", "COMPLETED"));
		return this.exportDtoList(criteria.list());
	}

	public void saveOrUpdateTasks(List<TaskEventsDto> tasks) {
		
		if (!ServicesUtil.isEmpty(tasks) && tasks.size() > 0) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for (int i = 0; i < tasks.size(); i++) {
				TaskEventsDto currentTask = tasks.get(i);
				currentTask.setUpdatedAt(new Date());
				session.saveOrUpdate(importDto(currentTask));
				if (i % 20 == 0 && i > 0) {
					session.flush();
					session.clear();
				}
			}
			session.flush();
			session.clear();
			tx.commit();
			session.close();
			/*
			 * if(!session.getTransaction().wasCommitted()) { session.flush(); }
			 */
			// session.close();
		}
	}

	public int executeUpdateQuery(String query) {
		return this.getSession().createSQLQuery(query).executeUpdate();
	}

	public int changeTaskStatus(TaskEventsDo taskDo) {
		Session session = this.getSession();
		try {
			TaskEventsDo task = (TaskEventsDo) session.load(TaskEventsDo.class, taskDo.getEventId());
			task.setStatus(taskDo.getStatus());
			task.setCurrentProcessor(taskDo.getCurrentProcessor());
			session.saveOrUpdate(task);
			return 1;
		} catch (Exception ex) {
			System.err.println("[WBP-Dev]Exception saving Task to DB : " + ex);
			return 0;
		}
	}

	public String updateTaskEventListToCompleted(List<String> instanceIdList) {
		// logger.error("[PMC][TaskOwnersDao][setTaskOwnersToReady][instanceId]"+instanceId);

		try {
			String queryString = "Update \"WORKBOX_DEMO\".\"TASK_EVENTS\" TE set TE.STATUS= 'COMPLETED' where TE.EVENT_ID in ";
			String instanceList = "('";
			for (String instanceId : instanceIdList) {
				instanceList += instanceId + "','";
			}

			instanceList = instanceList.substring(0, instanceList.length() - 2);
			queryString += instanceList + ")";

			System.err.println(
					"[WBP-Dev]TaskEventsDao.updateTaskEventToCompleted() status update query : " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			if (q.executeUpdate() > 0) {
				return "SUCCESS";
			}
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][updateTaskEventToCompleted][error]" + e);
		}
		return "FAILURE";
	}

	public String updateToCompleted(String instance, String userId, String userDisplay) {
		String message = "";
		try {
			String query = "UPDATE TASK_EVENTS SET STATUS = 'COMPLETED' ";

			if (!ServicesUtil.isEmpty(userId))
				query += " , CUR_PROC = '" + userId + "' ";

			if (!ServicesUtil.isEmpty(userDisplay))
				query += ", CUR_PROC_DISP = '" + userDisplay + "' ";

			/*
			 * if (!ServicesUtil.isEmpty(comment)) query += ", COMMENT = '" +
			 * comment + "' ";
			 */

			query += " WHERE EVENT_ID = '" + instance + "' ";
			System.err.println("[WBP-Dev]WorkFlowActionFacade.updateToCompleted() query : " + query);
			int totalUpdated = this.getSession().createSQLQuery(query).executeUpdate();

			if (totalUpdated == 1)
				message = PMCConstant.SUCCESS;
			else
				message = PMCConstant.FAILURE;
		} catch (Exception e) {
			System.err.println("[WBP-Dev]WorkFlowActionFacade.updateToCompleted() " + e);
			message = "Failed to update the records";
		}

		return message;
	}

	public String getProcessIdUsingTaskId(String eventId) {

		Query query = this.getSession().createSQLQuery("select process_id from Task_Events where event_id =:eventId");
		query.setParameter("eventId", eventId);
		String processId = (String) query.uniqueResult();
		return processId;

	}

	public String updateTaskComment(String taskId, String approvedById, String comment, String actionType) {
		// logger.error("[PMC][TaskOwnersDao][setTaskOwnersToReady][instanceId]"+instanceId);
		try {
			// prepare entity
			TaskAudit auditingEntity = new TaskAudit();
			auditingEntity.setAuditId(UUID.randomUUID().toString().replaceAll("-", ""));
			auditingEntity.setEventId(taskId);
			auditingEntity.setUserId(approvedById);
			auditingEntity.setComment(ServicesUtil.isEmpty(comment) ? "Completed" : comment);
			auditingEntity.setActionType(actionType);
			auditingEntity.setUpdatedAt(new Date());

			this.getSession().saveOrUpdate(auditingEntity);
			return "Success";
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][updateTaskEventToCompleted][error]" + e);
		}
		return "FAILURE";
	}

	public ResponseMessage saveTaskStatus(List<TaskStatus> taskStatus) {

		ResponseMessage message = new ResponseMessage();
		message.setMessage(PMCConstant.CREATE_FAILURE);
		message.setStatus(PMCConstant.FAILURE);
		message.setStatusCode(PMCConstant.CODE_FAILURE);
		if (!ServicesUtil.isEmpty(taskStatus)) {
			try {
				Session session = this.getSession();
				for (TaskStatus task : taskStatus) {
					session.saveOrUpdate(task);
				}

				message.setMessage(PMCConstant.CREATED_SUCCESS);
				message.setStatus(PMCConstant.SUCCESS);
				message.setStatusCode(PMCConstant.CODE_SUCCESS);
			} catch (Exception e) {
				System.err
						.println("[WBP-Dev]WorkBoxDao.saveTaskStatus() error while updating Task_Status table : " + e);
				message.setMessage(e.toString());
			}
		} else {
			message.setMessage("No tasks found to insert : ");
			message.setStatus(PMCConstant.SUCCESS);
			message.setStatusCode(PMCConstant.CODE_SUCCESS);
		}
		return message;
	}

	public String updateTaskEventListToCompleted(String instanceId) {
		try {
			String queryString = "Update \"WORKBOX_DEMO\".\"TASK_EVENTS\" TE set TE.STATUS= 'COMPLETED' where TE.EVENT_ID in ";
			String instanceList = "('";

			instanceList += instanceId + "','";

			instanceList = instanceList.substring(0, instanceList.length() - 2);
			queryString += instanceList + ")";

			System.err.println(
					"[WBP-Dev]TaskEventsDao.updateTaskEventToCompleted() status update query : " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			if (q.executeUpdate() > 0) {
				return "SUCCESS";
			}
		} catch (Exception e) {
			logger.error("[PMC][TaskEventsDao][updateTaskEventToCompleted][error]" + e);
		}
		return "FAILURE";
	}

	public void deleteTimeSheetTasks(String userId, List<String> eventId) {

		String requiredString;

		if (eventId.size() > 0) {
			StringBuilder string = new StringBuilder();
			for (String str : eventId) {

				string.append("'");
				string.append(str);
				string.append("',");
			}
			requiredString = string.substring(0, string.length() - 1);
		} else
			requiredString = "' '";

		System.err.println("\n Executing DELTA ");

		String qry = "delete from TASK_EVENTS where STATUS='READY' and PROC_NAME='TimeSheet' and CUR_PROC='" + userId
				+ "'" + "and EVENT_ID not in (" + requiredString + ")";
		Query query = this.getSession().createSQLQuery(qry.toString());
		query.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	public GenericResponseDto checkForTasksExistence(String taskId, String caseId, String processName) {
		GenericResponseDto response = new GenericResponseDto();
		String query = "";
		try {
			query = "SELECT TE.EVENT_ID FROM TASK_EVENTS TE INNER JOIN CUSTOM_ATTR_VALUES CAV "
					+ "ON TE.EVENT_ID = CAV.TASK_ID WHERE CAV.KEY = 'caseId' " + "AND CAV.ATTR_VALUE = '" + caseId
					+ "' AND CAV.PROCESS_NAME = '" + processName + "' AND " + "TE.EVENT_ID != '" + taskId
					+ "' AND TE.STATUS IN ('READY','RESERVED')";

			List<Object> result = this.getSession().createSQLQuery(query).list();

			if (ServicesUtil.isEmpty(result)) {
				response.setData(false);
				response.setMessage("No Data Found");
				response.setStatus(PMCConstant.SUCCESS);
				response.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {
				response.setData(true);
				response.setMessage("Data Fetched SuccessFully");
				response.setStatus(PMCConstant.CODE_SUCCESS);
				response.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			System.err.println("TaskEventsDao.checkForTasksExistence() exception : " + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public AnalystLineItemsAndForms getAnalystLineItemsAndForms(String processName, String caseId) {
		List<AnalystsLineItems> lineItems = new ArrayList<AnalystsLineItems>();
		List<AnalystFormData> forms = new ArrayList<AnalystFormData>();
		List<AnalystForm> analystFormList = null;

		AnalystLineItemsAndForms dto = new AnalystLineItemsAndForms();
		String query = "";
		try {
			query = "SELECT KEY, ATTR_VALUE FROM CUSTOM_ATTR_VALUES WHERE "
					+ "TASK_ID IN (SELECT TASK_ID FROM CUSTOM_ATTR_VALUES " + "WHERE KEY = 'caseId' AND ATTR_VALUE = '"
					+ caseId + "' " + "AND PROCESS_NAME = '" + processName
					+ "') AND KEY IN ('forms','lineItems') ORDER BY LENGTH(ATTR_VALUE) DESC ";

			System.err.println("TaskEventsDao.getAnalystLineItemsAndForms() query : " + query);

			List<Object[]> result = this.getSession().createSQLQuery(query).list();

			for (Object[] obj : result) {
				JsonParser parser = new JsonParser();

				if (obj[0].toString().equalsIgnoreCase("forms")) {

					JsonElement jsonElement = parser.parse(obj[1].toString());
					JsonArray jsonArray = jsonElement.getAsJsonArray();

					for (Object jsonObj : jsonArray) {
						analystFormList = new ArrayList<AnalystForm>();
						JsonParser jsonParser = new JsonParser();
						JsonElement element = jsonParser.parse(jsonObj.toString());
						JsonObject jsonObject = element.getAsJsonObject();
						Set<String> keys = jsonObject.keySet();
						String s = "";
						String s1 = "";

						for (String key : keys) {

							if (key.equalsIgnoreCase("formData")) {
								JsonArray array = jsonObject.getAsJsonArray(key);
								System.out.println("TaskEventsDao.main() array : " + array);
								for (Object object : array) {
									Gson gson = new Gson();
									AnalystForm form = gson.fromJson(object.toString(), AnalystForm.class);
									analystFormList.add(form);
								}
							} else if (key.equalsIgnoreCase("formId")) {
								s = jsonObject.get("formId").getAsString();
								System.err.println("TaskEventsDao.main() form id " + s);

							} else if (key.equalsIgnoreCase("formStatus")) {
								s1 = jsonObject.get("formStatus").getAsString();
								System.err.println("TaskEventsDao.main() form id " + s);
							}

						}
						forms.add(new AnalystFormData(analystFormList, s, s1));
					}
					// forms.add(new AnalystFormData(analystFormList));
				}

				if (obj[0].toString().equalsIgnoreCase("lineItems")) {
					List<AnalystsLineItems> tempLineItems = new ArrayList<AnalystsLineItems>();
					Gson gson = new Gson();
					String lines = obj[1].toString();
					lines = lines.replace("Analyst", "analyst");
					lines = lines.replace("Total Cost", "totalCost");
					lines = lines.replace("Plant", "plant");
					lines = lines.replace("Movement Type", "movementType");
					lines = lines.replace("Material Number", "materialNumber");
					lines = lines.replace("Material Document Number", "materialDocumentNumber");
					lines = lines.replace("Work Cell", "workCell");
					lines = lines.replace("Line Item Number", "lineItemNumber");
					lines = lines.replace("Quantity", "quantity");
					lines = lines.replace("Date", "date");

					JsonElement jsonElement = parser.parse(lines);
					JsonArray jsonArray = jsonElement.getAsJsonArray();

					Type lineItemType = new TypeToken<ArrayList<AnalystsLineItems>>() {
					}.getType();
					tempLineItems = gson.fromJson(lines, lineItemType);
					lineItems.addAll(tempLineItems);
				}
			}
			dto.setForms(forms);
			dto.setLineItems(lineItems);
		} catch (Exception e) {
			System.err.println("TaskEventsDao.getAnalystLineItemsAndForms() exception : " + e.getMessage());
		}
		return dto;
	}

	// public static void main(String[] args) {
	//
	// JsonParser parser = new JsonParser();
	//
	// String s="[{\"Total
	// Cost\":\"$45.15\",\"Analyst\":\"P000092\",\"Plant\":\"MY01\",\"lineItemFormId\":\"J-2009\",\"Movement
	// Type\":\"Z92\",\"Material Number\":\"ASPCA-01241-01\",\"Material Document
	// Number\":\"5068\",\"Work Cell\":\"ARISTA\",\"Line Item
	// Number\":\"0002\",\"Quantity\":\"15\",\"Date\":\"20200131\"},{\"Total
	// Cost\":\"$45.15\",\"Analyst\":\"P000092\",\"Plant\":\"MY01\",\"lineItemFormId\":\"J-2009\",\"Movement
	// Type\":\"Z91\",\"Material Number\":\"ASPCA-01241-01\",\"Material Document
	// Number\":\"5069\",\"Work Cell\":\"ARISTA\",\"Line Item
	// Number\":\"0003\",\"Quantity\":\"15\",\"Date\":\"20200131\"}]";
	//
	//
	// List<AnalystsLineItems> tempLineItems = new
	// ArrayList<AnalystsLineItems>();
	// Gson gson = new Gson();
	// String lines = s;
	// lines = lines.replace("Analyst", "analyst");
	// lines = lines.replace("Total Cost", "totalCost");
	// lines = lines.replace("Plant", "plant");
	// lines = lines.replace("Movement Type", "movementType");
	// lines = lines.replace("Material Number", "materialNumber");
	// lines = lines.replace("Material Document Number",
	// "materialDocumentNumber");
	// lines = lines.replace("Work Cell", "workCell");
	// lines = lines.replace("Line Item Number", "lineItemNumber");
	// lines = lines.replace("Quantity", "quantity");
	// lines = lines.replace("Date", "date");
	//
	// JsonElement jsonElement = parser.parse(lines);
	// JsonArray jsonArray = jsonElement.getAsJsonArray();
	//
	// Type lineItemType = new TypeToken<ArrayList<AnalystsLineItems>>() {
	// }.getType();
	// tempLineItems = gson.fromJson(lines, lineItemType);
	// lineItems.addAll(tempLineItems);
	//
	//
	//
	// }

	@SuppressWarnings({ "unchecked", "unused" })
	public AnalystLineItemsAndForms getManagerLineItemsAndForms(String processId) {
		List<AnalystsLineItems> lineItems = new ArrayList<AnalystsLineItems>();
		List<AnalystFormData> forms = new ArrayList<AnalystFormData>();
		List<AnalystForm> analystFormList = null;

		AnalystLineItemsAndForms dto = new AnalystLineItemsAndForms();
		String query = "";
		try {
			query = "SELECT KEY, ATTR_VALUE FROM CUSTOM_ATTR_VALUES WHERE "
					+ "TASK_ID IN (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID='" + processId
					+ "') AND KEY IN ('forms','lineItems')  ";

			System.err.println("TaskEventsDao.getManagerLineItemsAndForms() query : " + query);

			List<Object[]> result = this.getSession().createSQLQuery(query).list();

			for (Object[] obj : result) {
				JsonParser parser = new JsonParser();

				if (obj[0].toString().equalsIgnoreCase("forms")) {

					JsonElement jsonElement = parser.parse(obj[1].toString());
					JsonArray jsonArray = jsonElement.getAsJsonArray();

					for (Object jsonObj : jsonArray) {
						analystFormList = new ArrayList<AnalystForm>();
						JsonParser jsonParser = new JsonParser();
						JsonElement element = jsonParser.parse(jsonObj.toString());
						JsonObject jsonObject = element.getAsJsonObject();
						Set<String> keys = jsonObject.keySet();
						String s = "";
						String s1 = "";
						for (String key : keys) {

							if (key.equalsIgnoreCase("formData")) {
								JsonArray array = jsonObject.getAsJsonArray(key);
								System.out.println("TaskEventsDao.main() array : " + array);
								for (Object object : array) {
									Gson gson = new Gson();
									AnalystForm form = gson.fromJson(object.toString(), AnalystForm.class);
									analystFormList.add(form);
								}
							} else if (key.equalsIgnoreCase("formId")) {
								s = jsonObject.get("formId").getAsString();
								System.err.println("TaskEventsDao.main() form id " + s);

							} else if (key.equalsIgnoreCase("formStatus")) {
								s1 = jsonObject.get("formStatus").getAsString();
								System.err.println("TaskEventsDao.main() form id " + s);
							}

						}
						forms.add(new AnalystFormData(analystFormList, s, s1));
					}
					// forms.add(new AnalystFormData(analystFormList));
				}

				if (obj[0].toString().equalsIgnoreCase("lineItems")) {
					List<AnalystsLineItems> tempLineItems = new ArrayList<AnalystsLineItems>();
					Gson gson = new Gson();
					String lines = obj[1].toString();
					lines = lines.replace("Analyst", "analyst");
					lines = lines.replace("Total Cost", "totalCost");
					lines = lines.replace("Plant", "plant");
					lines = lines.replace("Movement Type", "movementType");
					lines = lines.replace("Material Number", "materialNumber");
					lines = lines.replace("Material Document Number", "materialDocumentNumber");
					lines = lines.replace("Work Cell", "workCell");
					lines = lines.replace("Line Item Number", "lineItemNumber");
					lines = lines.replace("Quantity", "quantity");
					lines = lines.replace("Date", "date");

					JsonElement jsonElement = parser.parse(lines);
					JsonArray jsonArray = jsonElement.getAsJsonArray();

					Type lineItemType = new TypeToken<ArrayList<AnalystsLineItems>>() {
					}.getType();
					tempLineItems = gson.fromJson(lines, lineItemType);
					lineItems.addAll(tempLineItems);
				}
			}
			dto.setForms(forms);
			dto.setLineItems(lineItems);
		} catch (Exception e) {
			System.err.println("TaskEventsDao.getAnalystLineItemsAndForms() exception : " + e.getMessage());
		}
		return dto;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public AnalystLineItemsAndForms getAnalystLineAndForms(String taskId) {
		List<AnalystsLineItems> lineItems = new ArrayList<AnalystsLineItems>();
		List<AnalystFormData> forms = new ArrayList<AnalystFormData>();
		List<AnalystForm> analystFormList = null;

		AnalystLineItemsAndForms dto = new AnalystLineItemsAndForms();
		String query = "";
		try {
			query = "SELECT KEY, ATTR_VALUE FROM CUSTOM_ATTR_VALUES WHERE " + "TASK_ID ='" + taskId
					+ "' AND KEY IN ('forms','lineItems')  ";

			System.err.println("TaskEventsDao.getManagerLineItemsAndForms() query : " + query);

			List<Object[]> result = this.getSession().createSQLQuery(query).list();

			for (Object[] obj : result) {
				JsonParser parser = new JsonParser();

				if (obj[0].toString().equalsIgnoreCase("forms")) {

					JsonElement jsonElement = parser.parse(obj[1].toString());
					JsonArray jsonArray = jsonElement.getAsJsonArray();

					for (Object jsonObj : jsonArray) {
						analystFormList = new ArrayList<AnalystForm>();
						JsonParser jsonParser = new JsonParser();
						JsonElement element = jsonParser.parse(jsonObj.toString());
						JsonObject jsonObject = element.getAsJsonObject();
						Set<String> keys = jsonObject.keySet();
						String s = "";
						String s1 = "";
						for (String key : keys) {

							if (key.equalsIgnoreCase("formData")) {
								JsonArray array = jsonObject.getAsJsonArray(key);
								System.out.println("TaskEventsDao.main() array : " + array);
								for (Object object : array) {
									Gson gson = new Gson();
									AnalystForm form = gson.fromJson(object.toString(), AnalystForm.class);
									analystFormList.add(form);
								}
							} else if (key.equalsIgnoreCase("formId")) {
								s = jsonObject.get("formId").getAsString();
								System.err.println("TaskEventsDao.main() form id " + s);

							} else if (key.equalsIgnoreCase("formStatus")) {
								s1 = jsonObject.get("formStatus").getAsString();
								System.err.println("TaskEventsDao.main() form id " + s);
							}

						}
						forms.add(new AnalystFormData(analystFormList, s, s1));
					}
					// forms.add(new AnalystFormData(analystFormList));
				}

				if (obj[0].toString().equalsIgnoreCase("lineItems")) {
					List<AnalystsLineItems> tempLineItems = new ArrayList<AnalystsLineItems>();
					Gson gson = new Gson();
					String lines = obj[1].toString();
					lines = lines.replace("Analyst", "analyst");
					lines = lines.replace("Total Cost", "totalCost");
					lines = lines.replace("Plant", "plant");
					lines = lines.replace("Movement Type", "movementType");
					lines = lines.replace("Material Number", "materialNumber");
					lines = lines.replace("Material Document Number", "materialDocumentNumber");
					lines = lines.replace("Work Cell", "workCell");
					lines = lines.replace("Line Item Number", "lineItemNumber");
					lines = lines.replace("Quantity", "quantity");
					lines = lines.replace("Date", "date");

					JsonElement jsonElement = parser.parse(lines);
					JsonArray jsonArray = jsonElement.getAsJsonArray();

					Type lineItemType = new TypeToken<ArrayList<AnalystsLineItems>>() {
					}.getType();
					tempLineItems = gson.fromJson(lines, lineItemType);
					lineItems.addAll(tempLineItems);
				}
			}
			dto.setForms(forms);
			dto.setLineItems(lineItems);
		} catch (Exception e) {
			System.err.println("TaskEventsDao.getAnalystLineItemsAndForms() exception : " + e.getMessage());
		}
		return dto;
	}

	/*
	 * public static void main(String[] args) { String f =
	 * "[{\"formId\":\"J-2009\",\"formStatus\":\"SUBMITTED\",\"formData\":[{\"valueHelp\":[],\"value\":\"venky 2\",\"key\":\"What measures have been put into place to avoid further correction?\"},{\"valueHelp\":[\"Process\",\"Training\",\"Individual\",\"Tools\"],\"value\":\"Process\",\"key\":\"What was the main reason for the system correction?\"},{\"valueHelp\":[],\"value\":\"venky 2\",\"key\":\"Please provide details of any additional training or action against an individual or group taken as a result of the correction\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"Yes\",\"key\":\"What was the issue raised in JOS meeting?\"},{\"valueHelp\":[],\"value\":\"venky 2\",\"key\":\"Analyst Comment\"},{\"valueHelp\":[\"Yes\",\"No\"],\"value\":\"No\",\"key\":\"Was there a corrective action raised\"},{\"valueHelp\":[],\"value\":\"\",\"key\":\"Manager Comment\"}]}]"
	 * ; List<AnalystFormData> forms = new ArrayList<AnalystFormData>();
	 * List<AnalystForm> analystFormList = null;
	 * 
	 * JsonParser parser = new JsonParser(); JsonElement jsonElement =
	 * parser.parse(f); JsonArray jsonArray = jsonElement.getAsJsonArray();
	 * System.out.println("TaskEventsDao.main() json array : " + jsonArray); for
	 * (Object jsonObj : jsonArray) { analystFormList = new
	 * ArrayList<AnalystForm>(); JsonParser jsonParser = new JsonParser();
	 * JsonElement element = jsonParser.parse(jsonObj.toString());
	 * System.out.println("TaskEventsDao.main() element : " + element); JsonObject
	 * obj = element.getAsJsonObject();
	 * System.out.println("TaskEventsDao.main() json obj : " + obj); Set<String>
	 * keys = obj.keySet(); System.err.println("TaskEventsDao.main() keys" + keys);
	 * String s = ""; String s1 = ""; for (String key : keys) {
	 * 
	 * if (key.equalsIgnoreCase("formData")) { JsonArray array =
	 * obj.getAsJsonArray(key); System.out.println("TaskEventsDao.main() array : " +
	 * array); for (Object object : array) { Gson gson = new Gson(); AnalystForm
	 * form = gson.fromJson(object.toString(), AnalystForm.class);
	 * analystFormList.add(form); } } else if (key.equalsIgnoreCase("formId")) { s =
	 * obj.get("formId").getAsString();
	 * System.err.println("TaskEventsDao.main() form id " + s);
	 * 
	 * } else if (key.equalsIgnoreCase("formStatus")) { s1 =
	 * obj.get("formStatus").getAsString();
	 * System.err.println("TaskEventsDao.main() form id " + s); }
	 * 
	 * } // JsonElement element2 = jsonParser.parse(); // JsonArray
	 * array=element2.getAsJsonArray(); forms.add(new
	 * AnalystFormData(analystFormList, s, s1)); }
	 * System.out.println("TaskEventsDao.main() forms :" + forms);
	 * 
	 * String lines =
	 * "[{\"Total Cost\":\"$45.15\",\"Analyst\":\"P000092\",\"Plant\":\"MY01\",\"lineItemFormId\":\"J-2009\",\"Movement Type\":\"Z92\",\"Material Number\":\"ASPCA-01241-01\",\"Material Document Number\":\"5068\",\"Work Cell\":\"ARISTA\",\"Line Item Number\":\"0002\",\"Quantity\":\"15\",\"Date\":\"20200131\"},{\"Total Cost\":\"$45.15\",\"Analyst\":\"P000092\",\"Plant\":\"MY01\",\"lineItemFormId\":\"J-2009\",\"Movement Type\":\"Z91\",\"Material Number\":\"ASPCA-01241-01\",\"Material Document Number\":\"5069\",\"Work Cell\":\"ARISTA\",\"Line Item Number\":\"0003\",\"Quantity\":\"15\",\"Date\":\"20200131\"}]"
	 * ;
	 * 
	 * lines = lines.replace("Analyst", "analyst"); lines =
	 * lines.replace("Total Cost", "totalCost"); lines = lines.replace("Plant",
	 * "plant"); lines = lines.replace("Movement Type", "movementType"); lines =
	 * lines.replace("Material Number", "materialNumber"); lines =
	 * lines.replace("Material Document Number", "materialDocumentNumber"); lines =
	 * lines.replace("Work Cell", "workCell"); lines =
	 * lines.replace("Line Item Number", "lineItemNumber"); lines =
	 * lines.replace("Quantity", "quantity"); lines = lines.replace("Date", "date");
	 * 
	 * List<AnalystsLineItems> lineItems = new ArrayList<AnalystsLineItems>(); Gson
	 * gson = new Gson(); JsonElement jsonElement3 = parser.parse(lines); JsonArray
	 * jsonArray3 = jsonElement3.getAsJsonArray();
	 * System.out.println("\n TaskEventsDao.main() json array 3 " + jsonArray3);
	 * Type lineItemType = new TypeToken<ArrayList<AnalystsLineItems>>() {
	 * }.getType(); lineItems = gson.fromJson(lines, lineItemType);
	 * System.out.println("\n TaskEventsDao.main() line items " + lineItems);
	 * 
	 * }
	 */

	public GenericResponseDto getProcessStatusById(String processId) {
		GenericResponseDto dto = new GenericResponseDto();
		String query = "";
		try {
			query = "SELECT STATUS FROM PROCESS_EVENTS WHERE PROCESS_ID = '" + processId + "' ";
			String result = this.getSession().createSQLQuery(query).uniqueResult().toString();

			if (result != null) {
				dto.setData(result);
				dto.setMessage("Data Fetched SuccessFully");
				dto.setStatus(PMCConstant.SUCCESS);
				dto.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {
				dto.setMessage("No Result Found");
				dto.setStatus(PMCConstant.SUCCESS);
				dto.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {
			System.err.println("TaskEventsDao.getProcessStatusById() exception : " + e.getMessage());
			dto.setMessage("Internal Server Error");
			dto.setStatus(PMCConstant.FAILURE);
			dto.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		return dto;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public AnalystLineItemsAndForms getAnalystLineItemsAndFormsForRejectedForms(String processName,
			String icManagerProcessId) {
		List<AnalystsLineItems> lineItems = new ArrayList<AnalystsLineItems>();
		List<AnalystFormData> forms = new ArrayList<AnalystFormData>();
		List<AnalystForm> analystFormList = null;

		AnalystLineItemsAndForms dto = new AnalystLineItemsAndForms();
		String query = "";
		try {
			query = "SELECT KEY, ATTR_VALUE FROM CUSTOM_ATTR_VALUES WHERE "
					+ "TASK_ID IN (SELECT TASK_ID FROM CUSTOM_ATTR_VALUES "
					+ "WHERE KEY = 'icManagerProcessId' AND ATTR_VALUE = '" + icManagerProcessId + "' "
					+ "AND PROCESS_NAME = '" + processName
					+ "') AND KEY IN ('forms','lineItems') ORDER BY LENGTH(ATTR_VALUE) DESC ";

			System.err.println("TaskEventsDao.getAnalystLineItemsAndForms() query : " + query);

			List<Object[]> result = this.getSession().createSQLQuery(query).list();

			for (Object[] obj : result) {
				JsonParser parser = new JsonParser();

				if (obj[0].toString().equalsIgnoreCase("forms")) {

					JsonElement jsonElement = parser.parse(obj[1].toString());
					JsonArray jsonArray = jsonElement.getAsJsonArray();

					for (Object jsonObj : jsonArray) {
						analystFormList = new ArrayList<AnalystForm>();
						JsonParser jsonParser = new JsonParser();
						JsonElement element = jsonParser.parse(jsonObj.toString());
						JsonObject jsonObject = element.getAsJsonObject();
						Set<String> keys = jsonObject.keySet();
						String s = "";
						String s1 = "";
						for (String key : keys) {

							if (key.equalsIgnoreCase("formData")) {
								JsonArray array = jsonObject.getAsJsonArray(key);
								System.out.println("TaskEventsDao.main() array : " + array);
								for (Object object : array) {
									Gson gson = new Gson();
									AnalystForm form = gson.fromJson(object.toString(), AnalystForm.class);
									analystFormList.add(form);
								}
							} else if (key.equalsIgnoreCase("formId")) {
								s = jsonObject.get("formId").getAsString();
								System.err.println("TaskEventsDao.main() form id " + s);

							} else if (key.equalsIgnoreCase("formStatus")) {
								s1 = jsonObject.get("formStatus").getAsString();
								System.err.println("TaskEventsDao.main() form id " + s);
							}

						}
						forms.add(new AnalystFormData(analystFormList, s, s1));
					}
					// forms.add(new AnalystFormData(analystFormList));
				}

				if (obj[0].toString().equalsIgnoreCase("lineItems")) {
					List<AnalystsLineItems> tempLineItems = new ArrayList<AnalystsLineItems>();
					Gson gson = new Gson();
					String lines = obj[1].toString();
					lines = lines.replace("Analyst", "analyst");
					lines = lines.replace("Total Cost", "totalCost");
					lines = lines.replace("Plant", "plant");
					lines = lines.replace("Movement Type", "movementType");
					lines = lines.replace("Material Number", "materialNumber");
					lines = lines.replace("Material Document Number", "materialDocumentNumber");
					lines = lines.replace("Work Cell", "workCell");
					lines = lines.replace("Line Item Number", "lineItemNumber");
					lines = lines.replace("Quantity", "quantity");
					lines = lines.replace("Date", "date");

					JsonElement jsonElement = parser.parse(lines);
					JsonArray jsonArray = jsonElement.getAsJsonArray();

					Type lineItemType = new TypeToken<ArrayList<AnalystsLineItems>>() {
					}.getType();
					tempLineItems = gson.fromJson(lines, lineItemType);
					lineItems.addAll(tempLineItems);
				}
			}
			dto.setForms(forms);
			dto.setLineItems(lineItems);
		} catch (Exception e) {
			System.err.println("TaskEventsDao.getAnalystLineItemsAndForms() exception : " + e.getMessage());
		}
		return dto;
	}

	public void updateFormCustomAttributeValues(JSONArray jsonPayload3, String processId) {

		String query = "UPDATE CUSTOM_ATTR_VALUES SET ATTR_VALUE = '" + jsonPayload3.toString() + "' "
				+ "WHERE KEY = 'forms' AND TASK_ID IN (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '"
				+ processId + "')";

		this.getSession().createSQLQuery(query).executeUpdate();

	}

	@SuppressWarnings("unchecked")
	public List<TaskEventsDo> getTaskDetailsByCaseId(List<String> eventIds) {
		Query query = this.getSession().createQuery("select te from TaskEventsDo te where te.eventId in (:ids)");
		query.setParameterList("ids", eventIds);

		List<TaskEventsDo> taskEventsDos = (List<TaskEventsDo>) query.list();
		if (taskEventsDos.size() > 0) {
			return taskEventsDos;

		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<String> getprocessIdByOrigin(String string) {

		Query query = this.getSession()
				.createSQLQuery("select distinct process_id from Task_Events where origin='" + string + "'");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getPreviousCompletedProcess(String processId) {
		Query query = this.getSession()
				.createSQLQuery("select distinct process_id from process_events where process_id not in ('" + processId
						+ "') and request_id in (select request_id from process_events where process_id='" + processId
						+ "')");
		return query.list();

	}
	
	@SuppressWarnings("unchecked")
	public List<TaskEventsDo> getTaskDetailsByProcessIdAFE(String processId) {
		
		String fetchTypeQuery = "select order_type from AFE_NEXUS_ORDER where process_name = 'AFENexus'";
		String filterType = (String) this.getSession().createSQLQuery(fetchTypeQuery).uniqueResult();
		
		String queryStr = "select te.* from task_events te full join user_idp_mapping udp on te.cur_proc = udp.user_id "
				+ " where te.process_id= :processId  order by te.created_at , ";
		
		if(filterType.equals("Alphabetical")) {
			queryStr += " udp.user_first_name , udp.user_last_name , udp.afe_nexus_budget ";
		}
		else {
			queryStr += " udp.afe_nexus_budget ";;
		}	
		if(filterType.equals("Descending")) {
			queryStr += " DESC";
		}
		
		
		Query query = this.getSession().createSQLQuery(queryStr).addEntity(TaskEventsDo.class);
		query.setParameter("processId", processId);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getTaskSla(String processId) {
		Query query = this.getSession()
				.createSQLQuery("SELECT CASE WHEN TE.COMPLETED_AT IS NULL THEN"
						+ " (CASE WHEN CURRENT_TIMESTAMP > TE.COMP_DEADLINE THEN 'BREACHED' WHEN CURRENT_TIMESTAMP > "
						+ "ADD_SECONDS(TE.COMP_DEADLINE,-PCT.CRITICAL_DATE*60*60)"
						+ " AND CURRENT_TIMESTAMP < TE.COMP_DEADLINE THEN 'CRITICAL' ELSE 'ON_TIME' END) "
						+ "ELSE (CASE WHEN TE.COMPLETED_AT > TE.COMP_DEADLINE THEN 'BREACHED' "
						+ "ELSE 'ON_TIME' END) END AS TASK_SLA FROM TASK_EVENTS TE "
						+ "INNER JOIN PROCESS_CONFIG_TB PCT ON PCT.PROCESS_NAME = TE.PROC_NAME "
						+ "WHERE PROCESS_ID = '"+processId+"'");
		return query.list();
	}

}
