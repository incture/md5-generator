package oneapp.incture.workbox.demo.cai.chatbot.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatbotTasksDto;

@Repository
public class ChatbotDaoImpl implements ChatbotDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public ChatbotTasksDto getAllTasksAdmin(String processType, Integer page) {

		ChatbotTasksDto dto = new ChatbotTasksDto();
		List<TaskEventsDto> tasks = null;

		String selectQuery = "";
		String commonJoinQuery = "";
		String commonConditionQuery = "";
		String definationCondition = "";
		String countQuery = "";
		String orderByQuery = "";
		String dataQuery = "";
		Integer startIndex = 1;

		try {
			Session session = sessionFactory.getCurrentSession();
			if (!ServicesUtil.isEmpty(processType)) {
				// get Defination Id for process type
				// List<ProcessConfigDo> processList = new
				// ArrayList<ProcessConfigDo>();
				// Criteria ct = session.createCriteria(ProcessConfigDo.class);
				// ct.add(Restrictions.eq("processName", processType));
				// processList = ct.list();
				// if (!ServicesUtil.isEmpty(processList) &&
				// !ServicesUtil.isEmpty(processList.get(0))) {
				// ProcessConfigDo processTypeEntity = processList.get(0);

				selectQuery = " SELECT  DISTINCT " // Index
						+ " TE.EVENT_ID, " // -----> 0
						+ " TE.COMPLETED_AT, " // -----> 1
						+ " TE.COMP_DEADLINE, " // -----> 2
						+ " TE.CREATED_AT, " // -----> 3
						+ " TE.CUR_PROC, " // -----> 4
						+ " TE.CUR_PROC_DISP, " // -----> 5
						+ " TE.DESCRIPTION, " // -----> 6
						+ " TE.FORWARDED_BY, " // -----> 7
						+ " TE.FORWARDED_AT, " // -----> 8
						+ " TE.PRIORITY, " // -----> 9
						+ " TE.PROCESS_ID ," // -----> 10
						+ " TE.PROC_NAME ," // -----> 11
						+ " TE.SLA_DUE_DATES, " // -----> 12
						+ " TE.STATUS , " // -----> 13
						+ " TE.SUBJECT, " // -----> 14
						+ " TE.URL, " // -----> 15
						+ " TE.UPDATED_AT," // -----> 16
						+ "TE.ORIGIN," // -----> 17
						+ "PE.REQUEST_ID," // -----> 18
						+ "TE.CREATED_BY "; // ----->19

				commonJoinQuery = " FROM TASK_EVENTS TE INNER JOIN TASK_OWNERS TW ON TE.EVENT_ID=TW.EVENT_ID "
						+ "INNER JOIN  PROCESS_EVENTS AS PE ON PE.PROCESS_ID=TE.PROCESS_ID ";
				commonConditionQuery = " WHERE   (PE.STATUS <> 'CANCELED' AND PE.STATUS <> 'COMPLETED') AND TE.COMP_DEADLINE IS NOT NULL AND  TE.STATUS <>'COMPLETED' and TE.PROC_NAME='"
						+ processType + "' ";

				// Definition filter condition
				/*
				 * if (!ServicesUtil.isEmpty(processTypeEntity.getDefinationId(
				 * ))) definationCondition =
				 * prepareDefinationCondition(processTypeEntity.
				 * getDefinationId());
				 */
				countQuery = "SELECT COUNT(*) FROM (" + selectQuery + commonJoinQuery + commonConditionQuery + ")";
				Query cq = session.createSQLQuery(countQuery.trim());
				BigDecimal count = ServicesUtil.getBigDecimal(cq.uniqueResult());
				Integer count2 = Integer.valueOf(count.toString());

				dto.setCount(count2);
				orderByQuery = " ORDER BY TE.CREATED_AT DESC ";

				dataQuery = selectQuery + commonJoinQuery + commonConditionQuery + orderByQuery;
				Query query = session.createSQLQuery(dataQuery);

				/*
				 * if(!ServicesUtil.isEmpty(filterDto.getPgNm()))
				 * startIndex=(filterDto.getPgNm() - 1) * 10;
				 */

				if (!ServicesUtil.isEmpty(page) && page>0)
				{
					startIndex = (page - 1) * 10;

				if (startIndex < count2) {
					query.setFirstResult(startIndex);
					query.setMaxResults(10);
				} else if (startIndex > count2) {
					query.setFirstResult(count2 - 10);
					query.setMaxResults(10);
				} 
				}
				

				List<Object[]> results = query.list();

				tasks = exportToTaskEventsDto(results);

			} else {
				System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksAdmin() No process type found : ");
			}

			dto.setTasks(tasks);
		} catch (Exception e) {
			System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksAdmin() error : " + e);
		}

		return dto;
	}

	@SuppressWarnings({ "unused", "unchecked" })
	@Override
	public ChatbotTasksDto getAllTasksOfOwner(String userId, String processType, Integer page, String action) {
		ChatbotTasksDto dto = new ChatbotTasksDto();
		List<TaskEventsDto> tasks = null;

		String selectQuery = "";
		String commonJoinQuery = "";
		String commonConditionQuery = "";
		String definationCondition = "";
		String countQuery = "";
		String orderByQuery = "";
		String dataQuery = "";
		Integer startIndex = 1;

		try {
			Session session = sessionFactory.getCurrentSession();
			if (!ServicesUtil.isEmpty(processType)) {
				// // get Defination Id for process type
				// List<ProcessConfigDo> processList = new
				// ArrayList<ProcessConfigDo>();
				// Criteria ct = session.createCriteria(ProcessConfigDo.class);
				// ct.add(Restrictions.eq("processName", processType));
				// processList = ct.list();
				// if (!ServicesUtil.isEmpty(processList) &&
				// !ServicesUtil.isEmpty(processList.get(0))) {
				// ProcessConfigDo processTypeEntity = processList.get(0);

				selectQuery = " SELECT  DISTINCT " // Index
						+ " TE.EVENT_ID, " // -----> 0
						+ " TE.COMPLETED_AT, " // -----> 1
						+ " TE.COMP_DEADLINE, " // -----> 2
						+ " TE.CREATED_AT, " // -----> 3
						+ " TE.CUR_PROC, " // -----> 4
						+ " TE.CUR_PROC_DISP, " // -----> 5
						+ " TE.DESCRIPTION, " // -----> 6
						+ " TE.FORWARDED_BY, " // -----> 7
						+ " TE.FORWARDED_AT, " // -----> 8
						+ " TE.PRIORITY, " // -----> 9
						+ " TE.PROCESS_ID ," // -----> 10
						+ " TE.PROC_NAME ," // -----> 11
						+ " TE.SLA_DUE_DATES, " // -----> 12
						+ " TE.STATUS , " // -----> 13
						+ " TE.SUBJECT, " // -----> 14
						+ " TE.URL, " // -----> 15
						+ " TE.UPDATED_AT,"// -----> 16
						+ "TE.ORIGIN," // -----> 17
						+ "PE.REQUEST_ID," // -----> 18
						+ "TE.CREATED_BY "; // ----->19

				commonJoinQuery = " FROM TASK_EVENTS TE INNER JOIN TASK_OWNERS TW ON TE.EVENT_ID=TW.EVENT_ID "
						+ "INNER JOIN  PROCESS_EVENTS AS PE ON PE.PROCESS_ID=TE.PROCESS_ID WHERE TE.PROC_NAME='"
						+ processType
						+ "' AND (PE.STATUS <> 'CANCELED' AND PE.STATUS <> 'COMPLETED') AND TE.COMP_DEADLINE IS NOT NULL ";
				if (PMCConstant.ACTION_TYPE_CLAIM.equalsIgnoreCase(action)) {
					commonConditionQuery = " AND TE.STATUS <> 'COMPLETED' AND TE.STATUS = 'READY' AND TW.TASK_OWNER = '" + userId + "' ";

				} else if (PMCConstant.ACTION_TYPE_RELEASE.equalsIgnoreCase(action)) {
					
					commonConditionQuery = "  AND TE.STATUS <> 'COMPLETED' AND (TE.STATUS = 'RESERVED' AND TE.CUR_PROC='"
							+ userId + "') ";
				}
				else if (PMCConstant.ACTION_TYPE_REJECT.equalsIgnoreCase(action)) {

					commonConditionQuery = "  AND TE.STATUS <> 'COMPLETED' AND (TE.TASK_TYPE <> 'Done' AND TE.TASK_TYPE <> 'Complete/Resolve')"
							+ " AND (TE.STATUS = 'RESERVED' AND TE.CUR_PROC='"
							+ userId + "') ";
				}
				else {

//					commonConditionQuery = "( TW.TASK_OWNER = '" + userId + "'  AND ( TE.STATUS = 'READY' OR TE.CUR_PROC = '" + userId + "')) ";
					
					commonConditionQuery = "  AND TE.STATUS IN ('READY', 'RESERVED') AND ( TE.CUR_PROC = '" + userId + "' "
							+ " OR TW.TASK_OWNER = '" + userId + "') ";


				}

				/*
				 * // Definition filter condition if
				 * (!ServicesUtil.isEmpty(processTypeEntity.getDefinationId( )))
				 * definationCondition =
				 * prepareDefinationCondition(processTypeEntity.
				 * getDefinationId());
				 */
				System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksOfOwner() dataQuery" + dataQuery);

				countQuery = "SELECT COUNT(*) FROM (" + selectQuery + commonJoinQuery + commonConditionQuery + ")";
				Query cq = session.createSQLQuery(countQuery.trim());
				BigDecimal count = ServicesUtil.getBigDecimal(cq.uniqueResult());
				Integer count2 = Integer.valueOf(count.toString());

				dto.setCount(count2);
				orderByQuery = " ORDER BY TE.CREATED_AT DESC ";

				dataQuery = selectQuery + commonJoinQuery + commonConditionQuery + orderByQuery;

				System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksOfOwner() dataQuery" + dataQuery);
				Query query = session.createSQLQuery(dataQuery);

				/*
				 * if(!ServicesUtil.isEmpty(filterDto.getPgNm()))
				 * startIndex=(filterDto.getPgNm() - 1) * 10;
				 */

				if (!ServicesUtil.isEmpty(page) && page>0){
					startIndex = (page - 1) * 10;
				

				if (startIndex < count2) {
					query.setFirstResult(startIndex);
					query.setMaxResults(10);
				} else if (startIndex > count2) {
					query.setFirstResult(count2 - 10);
					query.setMaxResults(10);
				} 
				}
				

				List<Object[]> results = query.list();

				tasks = exportToTaskEventsDto(results);

			} else {
				System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksOfOwner() No process type found : ");
			}

			dto.setTasks(tasks);
		} catch (

		Exception e) {
			System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksOfOwner() error : " + e);
		}

		return dto;
	}

	@SuppressWarnings("unused")
	private String prepareDefinationCondition(String definationId) {
		String definationCondition = "";
		if (!ServicesUtil.isEmpty(definationId)) {
			definationCondition = " AND TE.PROCESS_ID IN (";
			for (String id : definationId.split(",")) {
				definationCondition = definationCondition + "'" + id + "',";
			}

			definationCondition = definationCondition.substring(0, definationCondition.length() - 1);
			definationCondition += ") ";
		}

		return definationCondition;
	}

	@SuppressWarnings("unused")
	private List<TaskEventsDto> exportToTaskEventsDto(List<Object[]> results) {
		List<TaskEventsDto> list = new ArrayList<>();
		TaskEventsDto dto = null;
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));
		Long start = System.currentTimeMillis();
		if (!ServicesUtil.isEmpty(results)) {
			try {
				for (Object[] obj : results) {
					dto = new TaskEventsDto();
					dto.setEventId(ServicesUtil.isEmpty(obj[0]) ? null : (String) obj[0]);
					dto.setCompletedAt(ServicesUtil.isEmpty(obj[1]) ? null : ServicesUtil.resultAsDate(obj[1]));
					dto.setCompletionDeadLine(ServicesUtil.isEmpty(obj[2]) ? null : ServicesUtil.resultAsDate(obj[2]));
					dto.setCreatedAt(ServicesUtil.isEmpty(obj[3]) ? null : ServicesUtil.resultAsDate(obj[3]));
					dto.setCurrentProcessor(ServicesUtil.isEmpty(obj[4]) ? null : (String) obj[4]);
					dto.setCurrentProcessorDisplayName(ServicesUtil.isEmpty(obj[5]) ? null : (String) obj[5]);
					dto.setDescription(ServicesUtil.isEmpty(obj[6]) ? null : (String) obj[6]);
					dto.setForwardedBy(ServicesUtil.isEmpty(obj[7]) ? null : (String) obj[7]);
					dto.setForwardedAt(ServicesUtil.isEmpty(obj[8]) ? null : ServicesUtil.resultAsDate(obj[8]));
					dto.setPriority(ServicesUtil.isEmpty(obj[9]) ? null : (String) obj[9]);
					dto.setProcessId(ServicesUtil.isEmpty(obj[10]) ? null : (String) obj[10]);
					dto.setProcessName(ServicesUtil.isEmpty(obj[11]) ? null : (String) obj[11]);
					dto.setSlaDueDate(ServicesUtil.isEmpty(obj[12]) ? null : ServicesUtil.resultAsDate(obj[12]));
					dto.setStatus(ServicesUtil.isEmpty(obj[13]) ? null : (String) obj[13]);
					dto.setSubject(ServicesUtil.isEmpty(obj[14]) ? null : (String) obj[14]);
					dto.setDetailUrl(ServicesUtil.isEmpty(obj[15]) ? null : (String) obj[15]);
					dto.setOrigin(ServicesUtil.isEmpty(obj[17]) ? null : (String) obj[17]);
					dto.setRequestId(ServicesUtil.isEmpty(obj[18]) ? null : (String) obj[18]);
					dto.setCreatedBy(ServicesUtil.isEmpty(obj[19]) ? null : (String) obj[19]);

					list.add(dto);
				}
			} catch (Exception e) {
				System.err.println("[WBP-Dev]WorkBoxFilterDao.exportToTaskEventsDto()");
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomAttributeValue> getCustomAttributeValuesForInstaceId(String taskId) {

		Session session = sessionFactory.getCurrentSession();

		List<CustomAttributeValue> customAttributevalue = null;
		try {
			Query query = session.createQuery("From CustomAttributeValue cv where cv.taskId=:taskId");
			query.setParameter("taskId", taskId);
			customAttributevalue = query.list();
		} catch (Exception e) {
			System.err.println("[WBP-Dev]AdlsaWbTaskDaoImpl.getDbCountForTaskOfUser() exception:  " + e.getMessage());
			e.printStackTrace();
		}

		return customAttributevalue;
	}

	public static ChatbotTasksDto getAllTasksOfOwnertest(String userId, String processType, Integer page,
			String action) {
		ChatbotTasksDto dto = new ChatbotTasksDto();
		List<TaskEventsDto> tasks = null;

		String selectQuery = "";
		String commonJoinQuery = "";
		String commonConditionQuery = "";
		String definationCondition = "";
		String countQuery = "";
		String orderByQuery = "";
		String dataQuery = "";
		Integer startIndex = 1;

		try {
			if (!ServicesUtil.isEmpty(processType)) {
				// // get Defination Id for process type
				// List<ProcessConfigDo> processList = new
				// ArrayList<ProcessConfigDo>();
				// Criteria ct = session.createCriteria(ProcessConfigDo.class);
				// ct.add(Restrictions.eq("processName", processType));
				// processList = ct.list();
				// if (!ServicesUtil.isEmpty(processList) &&
				// !ServicesUtil.isEmpty(processList.get(0))) {
				// ProcessConfigDo processTypeEntity = processList.get(0);

				selectQuery = " SELECT  DISTINCT " // Index
						+ " TE.EVENT_ID, " // -----> 0
						+ " TE.COMPLETED_AT, " // -----> 1
						+ " TE.COMP_DEADLINE, " // -----> 2
						+ " TE.CREATED_AT, " // -----> 3
						+ " TE.CUR_PROC, " // -----> 4
						+ " TE.CUR_PROC_DISP, " // -----> 5
						+ " TE.DESCRIPTION, " // -----> 6
						+ " TE.FORWARDED_BY, " // -----> 7
						+ " TE.FORWARDED_AT, " // -----> 8
						+ " TE.PRIORITY, " // -----> 9
						+ " TE.PROCESS_ID ," // -----> 10
						+ " TE.PROC_NAME ," // -----> 11
						+ " TE.SLA_DUE_DATES, " // -----> 12
						+ " TE.STATUS , " // -----> 13
						+ " TE.SUBJECT, " // -----> 14
						+ " TE.URL, " // -----> 15
						+ " TE.UPDATED_AT,"// -----> 16
						+ "TE.ORIGIN," // -----> 17
						+ "PE.REQUEST_ID," // -----> 18
						+ "TE.CREATED_BY "; // ----->19

				commonJoinQuery = " FROM TASK_EVENTS TE INNER JOIN TASK_OWNERS TW ON TE.EVENT_ID=TW.EVENT_ID "
						+ "INNER JOIN  PROCESS_EVENTS AS PE ON PE.PROCESS_ID=TE.PROCESS_ID WHERE TE.PROC_NAME='"
						+ processType + "' ";
				if (PMCConstant.ACTION_TYPE_CLAIM.equalsIgnoreCase(action)) {
					commonConditionQuery = "  AND TE.STATUS = 'READY' AND TW.TASK_OWNER = '" + userId + "' ";

				} else if (PMCConstant.ACTION_TYPE_RELEASE.equalsIgnoreCase(action)) {
					commonConditionQuery = " TE.CUR_PROC='" + userId + "' ";

				} else {

					commonConditionQuery = "  AND TE.STATUS <> 'COMPLETED' AND (TE.CUR_PROC = '" + userId + "' "
							+ " OR TW.TASK_OWNER = '" + userId + "') ";

				}

				/*
				 * // Definition filter condition if
				 * (!ServicesUtil.isEmpty(processTypeEntity.getDefinationId( )))
				 * definationCondition =
				 * prepareDefinationCondition(processTypeEntity.
				 * getDefinationId());
				 */
				System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksOfOwner() dataQuery" + dataQuery);

				// countQuery = "SELECT COUNT(*) FROM (" + selectQuery +
				// commonJoinQuery + commonConditionQuery + ")";
				// Query cq = session.createSQLQuery(countQuery.trim());
				// BigDecimal count =
				// ServicesUtil.getBigDecimal(cq.uniqueResult());
				// Integer count2 = Integer.valueOf(count.toString());
				//
				// dto.setCount(count2);
				orderByQuery = " ORDER BY TE.CREATED_AT DESC ";

				dataQuery = selectQuery + commonJoinQuery + commonConditionQuery + orderByQuery;

				System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksOfOwner() dataQuery" + dataQuery);
				// Query query = session.createSQLQuery(dataQuery);

				/*
				 * if(!ServicesUtil.isEmpty(filterDto.getPgNm()))
				 * startIndex=(filterDto.getPgNm() - 1) * 10;
				 */

				if (!ServicesUtil.isEmpty(page))
					startIndex = (page - 1) * 10;

				// if (startIndex < count2) {
				// query.setFirstResult(startIndex);
				// } else if (startIndex > count2) {
				// query.setFirstResult(count2 - 10);
				// } else {
				// query.setFirstResult(0);
				// }
				// query.setMaxResults(10);
				//
				// List<Object[]> results = query.list();
				//
				// tasks = exportToTaskEventsDto(results);

			} else {
				System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksOfOwner() No process type found : ");
			}

			dto.setTasks(tasks);
		} catch (

		Exception e) {
			System.err.println("[WBP-Dev]ChatbotDaoImpl.getAllTasksOfOwner() error : " + e);
		}

		return dto;
	}

//	public static void main(String[] args) {
//		getAllTasksOfOwnertest("P000006", "travelandexpenseapproval", 1, "APPROVE");
//	}

}
