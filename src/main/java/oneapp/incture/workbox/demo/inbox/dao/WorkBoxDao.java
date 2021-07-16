package oneapp.incture.workbox.demo.inbox.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dto.KeyValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.KeyValueResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dto.FilterLayoutDto;
import oneapp.incture.workbox.demo.inbox.dto.FilterMetadataDto;
import oneapp.incture.workbox.demo.inbox.dto.HeaderDto;
import oneapp.incture.workbox.demo.inbox.dto.InboxTasksHeaderDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxResponseDto;
import oneapp.incture.workbox.demo.userCustomAttributes.dao.UserCustomHeadersDao;

/**
 * @author Neelam Raj
 *
 */
@Repository("WorkBoxDaoDecoupling")
//////@Transactional
public class WorkBoxDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	CustomAttributeDao customAttributeDao;

	@Autowired
	UserCustomHeadersDao userCustomHeadersDao;
	/* Report Filter Detail Data */

	public WorkboxResponseDto getInboxReport(List<String> processName, String status, String orderBy, String orderType,
			int skipCount, int maxCount, int page, String taskOwner, String reportFilter, String completedOn) {

		String selectQuery = "";
		String commonJoinQuery = "";
		String commonConditionQuery = "";
		String dataQuery = "";
		String countQuery = "";
		String processQuery = "";
		String reportNavigationQuery = "";
		String orderByQuery = "";
		ArrayNode arrayNode = objectMapper.createArrayNode();
		WorkboxResponseDto workboxResponseDto = new WorkboxResponseDto();
		ResponseMessage message = new ResponseMessage();
		List<WorkBoxDto> workBoxDtos = null;
		InboxTasksHeaderDto inboxHeaderDto = new InboxTasksHeaderDto();
		List<HeaderDto> headers = getStandardHeaders(
				customAttributeDao.getCustomAttributeTemplates(PMCConstant.STANDARD_HEADER, null, true),
				PMCConstant.STANDARD_HEADER);
		selectQuery = " SELECT DISTINCT PE.REQUEST_ID, PE.NAME AS PROCESS_NAME,"
				+ " TE.EVENT_ID, TE.DESCRIPTION, TE.NAME AS TASK_NAME, TE.SUBJECT, "
				+ " PE.STARTED_BY, TE.CREATED_AT, TE.STATUS, TE.CUR_PROC, "
				+ " TS.SLA, TE.PROCESS_ID, TE.URL, TE.COMP_DEADLINE, TE.FORWARDED_BY, TE.FORWARDED_AT, "
				+ " PCT.PROCESS_DISPLAY_NAME, IA.IS_CLAIMED, IA.IS_RELEASED, IA.USER_CLAIMED, IA.MODIFIED_AT, TE.COMPLETED_AT, PE.STARTED_BY_DISP,TE.ORIGIN,TE.TASK_TYPE ";

		commonJoinQuery = " FROM TASK_EVENTS TE INNER JOIN PROCESS_EVENTS PE ON TE.PROCESS_ID = PE.PROCESS_ID "
				+ " INNER JOIN TASK_OWNERS TW ON TE.EVENT_ID = TW.EVENT_ID "
				+ " LEFT OUTER JOIN INBOX_ACTIONS IA ON TE.EVENT_ID = IA.TASK_ID "
				+ " INNER JOIN PROCESS_CONFIG_TB PCT ON PE.NAME = PCT.PROCESS_NAME "
				+ " LEFT OUTER JOIN CUSTOM_ATTR_VALUES CAV ON TE.EVENT_ID = CAV.TASK_ID "
				+ " LEFT OUTER JOIN TASK_SLA TS ON TE.NAME = TS.TASK_DEF";

		commonConditionQuery = " WHERE (PE.STATUS <> 'CANCELED' AND PE.STATUS <> 'COMPLETED') AND TE.COMP_DEADLINE IS NOT NULL ";

		// conditions to get completed tasks
		if (!ServicesUtil.isEmpty(status) && status.equalsIgnoreCase("COMPLETED")) {
			commonConditionQuery = " WHERE (PE.STATUS <> 'CANCELED' ) ";
			System.err.println("[WBP-Dev]WorkBoxDaoDecoupling.getInboxReport()" + commonConditionQuery);
		}

		if (!ServicesUtil.isEmpty(processName))
			processQuery = getProcessQuery(processName);

		reportNavigationQuery = processQuery
				+ reportNavigationQuery(status, reportFilter, taskOwner, completedOn, processName);

		orderByQuery = getOrderByQuery(orderType, orderBy);

		try {
			countQuery = "SELECT COUNT(*) FROM ( " + selectQuery + commonJoinQuery + commonConditionQuery
					+ reportNavigationQuery + ")";
			Query cq = this.getSession().createSQLQuery(countQuery.trim());
			BigDecimal count = ServicesUtil.getBigDecimal(cq.uniqueResult());
			System.err.println(
					"[WBP-Dev][PMC][DEMO][WorkBoxFacade][Report][getWorkboxFilterData][CountQuery]" + countQuery);
			System.err.println("[WBP-Dev][PMC][DEMO][WorkBoxFacade][Report][getWorkboxFilterData][DataQuery][parts]"
					+ selectQuery + commonJoinQuery + commonConditionQuery + reportNavigationQuery + orderByQuery);

			dataQuery = selectQuery + commonJoinQuery + commonConditionQuery + reportNavigationQuery + orderByQuery;
			Query q = this.getSession().createSQLQuery(dataQuery.trim());
			System.err.println(
					"[WBP-Dev][PMC][DEMO][WorkBoxFacade][Report][getWorkboxFilterData][DataQuery]" + dataQuery);

			if (!ServicesUtil.isEmpty(maxCount) && maxCount > 0 && !ServicesUtil.isEmpty(skipCount) && skipCount >= 0) {
				int first = skipCount;
				int last = maxCount;
				q.setFirstResult(first);
				q.setMaxResults(last);
			}
			if (!ServicesUtil.isEmpty(page) && page > 0) {
				int first = (page - 1) * PMCConstant.PAGE_SIZE;
				int last = PMCConstant.PAGE_SIZE;
				q.setFirstResult(first);
				q.setMaxResults(last);
			}

			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();
			if (ServicesUtil.isEmpty(resultList)) {
				try {
					throw new NoResultFault(PMCConstant.NO_RESULT);
				} catch (NoResultFault e) {
					System.err.println(PMCConstant.NO_RESULT);
					message.setStatus(PMCConstant.NO_RESULT);
					message.setStatusCode("1");
					workboxResponseDto.setResponseMessage(message);
				}
			} else
				workBoxDtos = getWorkBoxDto(resultList, processName, arrayNode, taskOwner, null);
			if (ServicesUtil.isEmpty(workBoxDtos)) {
				workBoxDtos = new ArrayList<WorkBoxDto>();
			}

			// get custom attributes in json format for all tasks
			if (!ServicesUtil.isEmpty(workBoxDtos)) {
				workBoxDtos = addCustomAttributestoList(workBoxDtos);
			}

			workboxResponseDto.setWorkBoxDtos(workBoxDtos);
			inboxHeaderDto.setHeaders(headers);
			workboxResponseDto.setHeaderDto(inboxHeaderDto);
			workboxResponseDto.setCustomAttributes(arrayNode);
			workboxResponseDto.setPageCount(PMCConstant.PAGE_SIZE);
			workboxResponseDto.setCount(count);
			workboxResponseDto.setWorkBoxDtos(workBoxDtos);
			message.setStatus("Success");
			message.setStatusCode("0");
			message.setMessage("Process Details Fetched Successfully");
			workboxResponseDto.setResponseMessage(message);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][WorkboxDao][getReportFilter][Error]" + e.getLocalizedMessage());
		}
		return workboxResponseDto;
	}

	/* Filter Detail Data */

	public WorkboxResponseDto getFilterData(String requestId, List<String> processName, String createdBy,
			String createdAt, String status, String orderBy, String orderType, int skipCount, int maxCount, int page,
			Boolean isChatBot, String taskOwner, Boolean isAdmin, String inboxType, String origin, String inboxName) {

		WorkboxResponseDto workboxResponseDto = new WorkboxResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		ArrayNode arrayNode = objectMapper.createArrayNode();
		List<WorkBoxDto> workBoxDtos = null;
		HeaderDto headerDto = null;
		InboxTasksHeaderDto inboxHeaderDto = new InboxTasksHeaderDto();
		String filterQuery = "";
		String inboxTypeQuery = "";
		String processQuery = "";
		String dataQuery = "";
		String countQuery = "";
		String orderByQuery = "";

		List<HeaderDto> headers = getStandardHeaders(
				customAttributeDao.getCustomAttributeTemplates(PMCConstant.STANDARD_HEADER, null, true),
				PMCConstant.STANDARD_HEADER);

		String selectQuery = " SELECT " + " DISTINCT PE.REQUEST_ID, PE.NAME AS PROCESS_NAME,"
				+ " TE.EVENT_ID, TE.DESCRIPTION, TE.NAME AS TASK_NAME, TE.SUBJECT, "
				+ " PE.STARTED_BY, TE.CREATED_AT, TE.STATUS, TE.CUR_PROC, "
				+ " TS.SLA, TE.PROCESS_ID, TE.URL, TE.COMP_DEADLINE, TE.FORWARDED_BY, TE.FORWARDED_AT, "
				+ " PCT.PROCESS_DISPLAY_NAME, IA.IS_CLAIMED, IA.IS_RELEASED, IA.USER_CLAIMED, IA.MODIFIED_AT, TE.COMPLETED_AT, PE.STARTED_BY_DISP,TE.ORIGIN,TE.TASK_TYPE";

		String commonJoinQuery = " FROM TASK_EVENTS TE INNER JOIN PROCESS_EVENTS PE ON TE.PROCESS_ID = PE.PROCESS_ID "
				+ " INNER JOIN TASK_OWNERS TW ON TE.EVENT_ID = TW.EVENT_ID "
				+ " LEFT JOIN INBOX_ACTIONS IA ON TE.EVENT_ID = IA.TASK_ID "
				+ " INNER JOIN PROCESS_CONFIG_TB PCT ON PE.NAME = PCT.PROCESS_NAME "
				+ " LEFT OUTER JOIN CUSTOM_ATTR_VALUES CAV ON TE.EVENT_ID = CAV.TASK_ID "
				+ " LEFT OUTER JOIN TASK_SLA TS ON TE.NAME = TS.TASK_DEF";

		String commonConditionQuery = " WHERE (PE.STATUS <> 'CANCELED' AND PE.STATUS <> 'COMPLETED') AND TE.COMP_DEADLINE IS NOT NULL ";

		// conditions to get completed tasks
		if (!ServicesUtil.isEmpty(status) && status.equalsIgnoreCase("COMPLETED")) {
			commonConditionQuery = " WHERE (PE.STATUS <> 'CANCELED' AND PE.STATUS = 'COMPLETED') ";
			System.err.println("[WBP-Dev]WorkBoxDaoDecoupling.getFilterData()" + commonConditionQuery);
		}
		if (!ServicesUtil.isEmpty(requestId)) {
			filterQuery += " AND PE.REQUEST_ID LIKE '%" + requestId + "%' ";
		}
		if (!ServicesUtil.isEmpty(createdBy)) {
			filterQuery += " AND PE.STARTED_BY = '" + createdBy + "' ";
		}
		if (!ServicesUtil.isEmpty(createdAt)) {
			filterQuery += " AND TO_CHAR(CAST(TE.CREATED_AT AS DATE),'MM/DD/YYYY')= '" + createdAt + "' ";
		}
		if (!ServicesUtil.isEmpty(origin)) {
			filterQuery += " AND UPPER(TE.ORIGIN) = UPPER('" + origin + "') ";
		}
		/* Task Owner search */

		if (!ServicesUtil.isEmpty(status)) {

			filterQuery += " AND te.STATUS = '" + status + "' ";
			if (!ServicesUtil.isEmpty(isAdmin) && !isAdmin) {
				if (PMCConstant.TASK_COMPLETED.equals(status))

					filterQuery += " AND TE.CUR_PROC = '" + taskOwner + "'";
				else {

					filterQuery += " AND tw.TASK_OWNER = '" + taskOwner + "'";
				}
				if (status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESERVED)
						/* Search for Manual Task's status addition */
						|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_IN_PROGRESS)
						|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESOLVED)) {
					filterQuery += " AND  te.CUR_PROC = '" + taskOwner + "' ";
				}

				else {
					if (PMCConstant.INBOX_TYPE_MY_TASK.equals(inboxType)) {
						if (status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESERVED)
								|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_READY)
								|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_IN_PROGRESS)
								|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESOLVED)) {
							filterQuery += " AND (tw.TASK_OWNER = '" + taskOwner
									+ "' and (te.status='READY' OR te.CUR_PROC = '" + taskOwner + "'))";
						}
					}
				}

			} else if (!ServicesUtil.isEmpty(isAdmin) && isAdmin) {
				if (status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESERVED)
						|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_IN_PROGRESS)
						|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESOLVED)
								&& inboxType.equalsIgnoreCase(PMCConstant.INBOX_TYPE_GROUP_TASK)) {
					filterQuery += " AND tw.TASK_OWNER = '" + taskOwner
							+ "' and (te.status = 'READY' or te.cur_proc = '" + taskOwner + "')";
				} else if (status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESERVED)
						|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_IN_PROGRESS)
						|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESOLVED)
								&& inboxType.equalsIgnoreCase(PMCConstant.INBOX_TYPE_MY_TASK)) {
					filterQuery += " AND tw.task_owner = '" + taskOwner
							+ "' and (te.status = 'RESERVED' and te.cur_proc = '" + taskOwner + "')";
				} else if (status.equalsIgnoreCase(PMCConstant.TASK_STATUS_READY)) {
					if (inboxType.equalsIgnoreCase(PMCConstant.INBOX_TYPE_MY_TASK)) {
						filterQuery += " AND tw.task_owner = '" + taskOwner + "' ";
					}
				}
			}

		} else {
			filterQuery += " AND te.STATUS <> 'COMPLETED' ";
			if (!ServicesUtil.isEmpty(isAdmin) && !ServicesUtil.isEmpty(inboxType) && isAdmin
					&& PMCConstant.INBOX_TYPE_GROUP_TASK.equals(inboxType)) {
			} else /*
					 * Admin Groups task every thing should come ..so no filter
					 */
				filterQuery += " AND (tw.TASK_OWNER = '" + taskOwner + "' and (te.status='RESERVED' AND te.CUR_PROC = '"
						+ taskOwner + "'))";

		}
		/*
		 * Substituted Users query : we are adding substitution query only for
		 * user workload filter details as when inboxType and isAdmin is coming
		 * then substitution is getting added there but for other then that we
		 * are adding here
		 */

		if (!ServicesUtil.isEmpty(isAdmin) && !ServicesUtil.isEmpty(inboxType) && isAdmin
				&& PMCConstant.INBOX_TYPE_GROUP_TASK.equals(inboxType)) {
			/*
			 * Admin's Group task every thing should come ..so no substitution
			 * filter Adding task where taskOwner is the owner and task is in
			 * ready state or reserved by her
			 */
		} else if (ServicesUtil.isEmpty(isAdmin) && ServicesUtil.isEmpty(inboxType)) {
			if (!ServicesUtil.isEmpty(status)) {
				if (status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESERVED))
					filterQuery += " AND (  te.status <> 'COMPLETED' AND tw.TASK_OWNER = '" + taskOwner
							+ "' AND ( te.CUR_PROC = '" + taskOwner + "')) ";
				if (status.equalsIgnoreCase(PMCConstant.TASK_STATUS_READY))
					filterQuery += " AND (  te.status <> 'COMPLETED' AND (tw.TASK_OWNER = '" + taskOwner
							+ "' OR te.CUR_PROC = '" + taskOwner + "')) ";
			} else {
				filterQuery += " AND (  te.status <> 'COMPLETED' AND (tw.TASK_OWNER = '" + taskOwner
						+ "' OR te.CUR_PROC = '" + taskOwner + "')) ";
			}
		}
		processQuery = getProcessQuery(processName);

		if (!ServicesUtil.isEmpty(inboxType) && !ServicesUtil.isEmpty(isAdmin)) {
			String reportFilter = "";
			inboxTypeQuery = getInboxTypeQuery(inboxType, isAdmin, taskOwner, reportFilter, status);
		}
		orderByQuery = getOrderByQuery(orderType, orderBy);

		countQuery = "SELECT COUNT(*) FROM ( " + selectQuery + commonJoinQuery + commonConditionQuery + processQuery
				+ filterQuery + inboxTypeQuery + " )";

		System.err.println("[WBP-Dev][PMC][Demo][getfilterQuery][countQuery]" + countQuery);

		Query countQry = this.getSession().createSQLQuery(countQuery.trim());
		BigDecimal taskCount = ServicesUtil.getBigDecimal(countQry.uniqueResult());

		dataQuery = selectQuery + commonJoinQuery + commonConditionQuery + processQuery + filterQuery + inboxTypeQuery
				+ orderByQuery;

		Query q = this.getSession().createSQLQuery(dataQuery.trim());
		System.err.println("[WBP-Dev][PMC][Demo][WorkBoxFacade][getWorkboxFilterData][DataQuery]" + dataQuery);

		if (!ServicesUtil.isEmpty(maxCount) && maxCount > 0 && !ServicesUtil.isEmpty(skipCount) && skipCount >= 0) {
			int first = skipCount;
			int last = maxCount;
			q.setFirstResult(first);
			q.setMaxResults(last);
		}
		if (!ServicesUtil.isEmpty(page) && page > 0) {
			int first = (page - 1) * PMCConstant.PAGE_SIZE;
			int last = PMCConstant.PAGE_SIZE;
			q.setFirstResult(first);
			q.setMaxResults(last);
		}

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = q.list();

		if (ServicesUtil.isEmpty(resultList)) {
			responseMessage.setMessage(PMCConstant.NO_RESULT);
			responseMessage.setStatus(PMCConstant.SUCCESS);
			responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
		} else {
			workBoxDtos = getWorkBoxDto(resultList, processName, arrayNode, taskOwner, inboxType);
			if (!ServicesUtil.isEmpty(processName) && processName.size() == 1) {
				/*
				 * List<CustomAttributeTemplate> customHeaders =
				 * customAttributeDao
				 * .getCustomAttributeTemplates(processName.get(0), null, true);
				 */
				List<CustomAttributeTemplate> customHeaders = userCustomHeadersDao
						.getUserDefinedCustomAttributes(taskOwner, processName.get(0));
				customHeaders = customHeaders.stream().filter(a -> a.getIsActive().equals(true))
						.collect(Collectors.toList());
				System.err.println("WorkBoxDao.getFilterData() custom headers : " + customHeaders);
				if (!ServicesUtil.isEmpty(customHeaders) && customHeaders.size() > 0) {
					for (CustomAttributeTemplate customAttributeTemplate : customHeaders) {
						headerDto = new HeaderDto();
						headerDto.setType("CUSTOM");
						headerDto.setKey(customAttributeTemplate.getKey());
						headerDto.setName(customAttributeTemplate.getLabel());
						if (ServicesUtil.isEmpty(headers)) {
							headers = new ArrayList<HeaderDto>();
							headers.add(headerDto);
						} else {
							headers.add(headerDto);
						}
					}
				}
			}
			responseMessage.setStatus("Success");
			responseMessage.setStatusCode("0");
			responseMessage.setMessage("Task Details Fetched Successfully");
		}

		// get custom attributes in json format for all tasks
		if (!ServicesUtil.isEmpty(workBoxDtos)) {
			workBoxDtos = addCustomAttributestoList(workBoxDtos);
		}

		workboxResponseDto.setWorkBoxDtos(workBoxDtos);
		inboxHeaderDto.setHeaders(headers);
		workboxResponseDto.setHeaderDto(inboxHeaderDto);
		workboxResponseDto.setCustomAttributes(arrayNode);
		workboxResponseDto.setPageCount(PMCConstant.PAGE_SIZE);
		workboxResponseDto.setCount(taskCount);

		workboxResponseDto.setResponseMessage(responseMessage);

		return workboxResponseDto;
	}

	private List<WorkBoxDto> addCustomAttributestoList(List<WorkBoxDto> workBoxDtos) {

		List<String> instanceIdList = new ArrayList<>();
		Long start = System.currentTimeMillis();
		List<CustomAttributeValue> customAttriubuteValues = null;
		JsonObject jsonObject = null;

		for (WorkBoxDto taskDto : workBoxDtos) {
			instanceIdList.add(taskDto.getTaskId());
		}

		customAttriubuteValues = getCustomAttributeValuesForInstaceIdList(instanceIdList);
		System.err.println("[WBP-Dev]WorkBoxDao.addCustomAttributestoList() To fetch custom attributes ---->"
				+ (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();
		for (WorkBoxDto taskDto : workBoxDtos) {
			jsonObject = new JsonObject();

			for (CustomAttributeValue customValue : customAttriubuteValues) {
				if (taskDto.getTaskId().equals(customValue.getTaskId())) {
					String val = customValue.getAttributeValue();
					if (null != val)
						val = val.trim();
					jsonObject.addProperty(customValue.getKey(), val);

				}
			}

			// taskDto.setCustomAttributes(jsonObject.toString());

		}
		System.err.println("[WBP-Dev]WorkBoxDao.addCustomAttributestoList() to add custom attributes to tasks : "
				+ (System.currentTimeMillis() - start));

		return workBoxDtos;

	}

	@SuppressWarnings("unchecked")
	private List<CustomAttributeValue> getCustomAttributeValuesForInstaceIdList(List<String> instanceIdList) {

		List<CustomAttributeValue> customAttributevalue = null;
		try {
			Query query = this.getSession()
					.createQuery("From CustomAttributeValue cv where cv.taskId IN (:instanceIdList) ");

			query.setParameterList("instanceIdList", instanceIdList);
			customAttributevalue = (List<CustomAttributeValue>) query.list();

		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev]WorkBoxDao.getCustomAttributeValuesForInstaceIdList() exception:  " + e.getMessage());
			e.printStackTrace();
		}
		return customAttributevalue;

	}

	// Advanced filter on custom attributes
	public WorkboxResponseDto getFilterData(String requestId, List<String> processName, String createdBy,
			String createdAt, String status, String orderBy, String orderType, int skipCount, int maxCount, int page,
			Boolean isChatBot, String taskOwner, Boolean isAdmin, String inboxType, String origin,
			List<CustomAttributeTemplate> customAttributeTemplates) {

		WorkboxResponseDto workboxResponseDto = new WorkboxResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		ArrayNode arrayNode = objectMapper.createArrayNode();
		List<WorkBoxDto> workBoxDtos = null;
		HeaderDto headerDto = null;
		InboxTasksHeaderDto inboxHeaderDto = new InboxTasksHeaderDto();
		String filterQuery = "";
		String inboxTypeQuery = "";
		String processQuery = "";
		String dataQuery = "";
		String countQuery = "";
		String orderByQuery = "";
		try {

			List<HeaderDto> headers = getStandardHeaders(
					customAttributeDao.getCustomAttributeTemplates(PMCConstant.STANDARD_HEADER, null, true),
					PMCConstant.STANDARD_HEADER);

			String selectQuery = " SELECT " + " DISTINCT PE.REQUEST_ID, PE.NAME AS PROCESS_NAME,"
					+ " TE.EVENT_ID, TE.DESCRIPTION, TE.NAME AS TASK_NAME, TE.SUBJECT, "
					+ " PE.STARTED_BY, TE.CREATED_AT, TE.STATUS, TE.CUR_PROC, "
					+ " TS.SLA, TE.PROCESS_ID, TE.URL, TE.COMP_DEADLINE, TE.FORWARDED_BY, TE.FORWARDED_AT, "
					+ " PCT.PROCESS_DISPLAY_NAME, IA.IS_CLAIMED, IA.IS_RELEASED, IA.USER_CLAIMED, IA.MODIFIED_AT,"
					+ " TE.COMPLETED_AT, PE.STARTED_BY_DISP,TE.ORIGIN,TE.TASK_TYPE";

			String commonJoinQuery = " FROM TASK_EVENTS TE INNER JOIN PROCESS_EVENTS PE ON TE.PROCESS_ID = PE.PROCESS_ID "
					+ " INNER JOIN TASK_OWNERS TW ON TE.EVENT_ID = TW.EVENT_ID "
					+ " LEFT JOIN INBOX_ACTIONS IA ON TE.EVENT_ID = IA.TASK_ID "
					+ " INNER JOIN PROCESS_CONFIG_TB PCT ON PE.NAME = PCT.PROCESS_NAME "
					+ " LEFT OUTER JOIN CUSTOM_ATTR_VALUES CAV ON TE.EVENT_ID = CAV.TASK_ID "
					+ " LEFT OUTER JOIN TASK_SLA TS ON TE.NAME = TS.TASK_DEF";

			String commonConditionQuery = " WHERE (PE.STATUS <> 'CANCELED' AND PE.STATUS <> 'COMPLETED') AND TE.COMP_DEADLINE IS NOT NULL ";

			// conditions to get completed tasks
			if (!ServicesUtil.isEmpty(status) && status.equalsIgnoreCase("COMPLETED")) {
				commonConditionQuery = " WHERE (PE.STATUS <> 'CANCELED') ";
				System.err.println("[WBP-Dev]WorkBoxDaoDecoupling.getFilterData()" + commonConditionQuery);
			}
			if (!ServicesUtil.isEmpty(requestId)) {
				filterQuery += " AND PE.REQUEST_ID LIKE '%" + requestId + "%' ";
			}
			if (!ServicesUtil.isEmpty(createdBy)) {
				filterQuery += " AND PE.STARTED_BY = '" + createdBy + "' ";
			}
			if (!ServicesUtil.isEmpty(createdAt)) {
				filterQuery += " AND TO_CHAR(CAST(TE.CREATED_AT AS DATE),'MM/DD/YYYY')= '" + createdAt + "' ";
			}
			if (!ServicesUtil.isEmpty(origin)) {
				filterQuery += " AND UPPER(TE.ORIGIN) = UPPER('" + origin + "') ";
			}
			/* Task Owner search */

			if (!ServicesUtil.isEmpty(status)) {

				filterQuery += " AND te.STATUS = '" + status + "' ";
				if (!ServicesUtil.isEmpty(isAdmin) && !isAdmin) {
					if (PMCConstant.TASK_COMPLETED.equals(status))

						filterQuery += " AND TE.CUR_PROC = '" + taskOwner + "'";
					else {

						filterQuery += " AND tw.TASK_OWNER = '" + taskOwner + "'";
					}
					if (status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESERVED)
							/* Search for Manual Task's status addition */
							|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_IN_PROGRESS)
							|| status.equalsIgnoreCase(PMCConstant.TASK_STATUS_RESOLVED)) {
						filterQuery += " AND  te.CUR_PROC = '" + taskOwner + "' ";
					}

				}
			} else {
				filterQuery += " AND te.STATUS <> 'COMPLETED' ";
				if (!ServicesUtil.isEmpty(isAdmin) && !ServicesUtil.isEmpty(inboxType) && isAdmin
						&& PMCConstant.INBOX_TYPE_GROUP_TASK.equals(inboxType)) {
				} else /*
						 * Admin Groups task every thing should come ..so no
						 * filter
						 */
					filterQuery += " AND (tw.TASK_OWNER = '" + taskOwner + "' and (te.status='READY' OR te.CUR_PROC = '"
							+ taskOwner + "'))";

			}
			/*
			 * Substituted Users query : we are adding substitution query only
			 * for user workload filter details as when inboxType and isAdmin is
			 * coming then substitution is getting added there but for other
			 * then that we are adding here
			 */

			if (!ServicesUtil.isEmpty(isAdmin) && !ServicesUtil.isEmpty(inboxType) && isAdmin
					&& PMCConstant.INBOX_TYPE_GROUP_TASK.equals(inboxType)) {
				/*
				 * Admin's Group task every thing should come ..so no
				 * substitution filter Adding task where taskOwner is the owner
				 * and task is in ready state or reserved by her
				 */
			} else if (ServicesUtil.isEmpty(isAdmin) && ServicesUtil.isEmpty(inboxType))
				filterQuery += " OR (tw.is_substituted = 1	AND ( te.status <> 'COMPLETED' AND tw.TASK_OWNER = '"
						+ taskOwner + "' AND (te.status = 'READY' OR te.CUR_PROC = '" + taskOwner + "')))";

			processQuery = getProcessQuery(processName);

			if (!ServicesUtil.isEmpty(inboxType) && !ServicesUtil.isEmpty(isAdmin)) {
				String reportFilter = "";
				inboxTypeQuery = getInboxTypeQuery(inboxType, isAdmin, taskOwner, reportFilter, status);
			}

			// advaced filter on custom attributes
			String advancedFilter = "";
			if (!ServicesUtil.isEmpty(customAttributeTemplates) && customAttributeTemplates.size() >= 0) {
				advancedFilter = getCustomAttributeQuery(customAttributeTemplates, processName);
				System.err.println("[WBP-Dev]WorkBoxDao.getFilterData() advanced filter condition : " + advancedFilter);
			}

			orderByQuery = getOrderByQuery(orderType, orderBy);

			countQuery = "SELECT COUNT(*) FROM ( " + selectQuery + commonJoinQuery + commonConditionQuery + processQuery
					+ filterQuery + advancedFilter + inboxTypeQuery + " )";

			System.err.println("[WBP-Dev][PMC][Demo][getfilterQuery][countQuery]" + countQuery);

			Query countQry = this.getSession().createSQLQuery(countQuery.trim());
			BigDecimal taskCount = ServicesUtil.getBigDecimal(countQry.uniqueResult());

			dataQuery = selectQuery + commonJoinQuery + commonConditionQuery + processQuery + filterQuery
					+ advancedFilter + inboxTypeQuery + orderByQuery;

			Query q = this.getSession().createSQLQuery(dataQuery.trim());
			System.err.println(
					"[PMC][Demo][WorkBoxFacade][getWorkboxFilterData][DataQuery] advanced filter " + dataQuery);

			if (!ServicesUtil.isEmpty(maxCount) && maxCount > 0 && !ServicesUtil.isEmpty(skipCount) && skipCount >= 0) {
				int first = skipCount;
				int last = maxCount;
				q.setFirstResult(first);
				q.setMaxResults(last);
			}
			if (!ServicesUtil.isEmpty(page) && page > 0) {
				int first = (page - 1) * PMCConstant.PAGE_SIZE;
				int last = PMCConstant.PAGE_SIZE;
				q.setFirstResult(first);
				q.setMaxResults(last);
			}

			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			if (ServicesUtil.isEmpty(resultList)) {
				responseMessage.setMessage(PMCConstant.NO_RESULT);
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {
				workBoxDtos = getWorkBoxDto(resultList, processName, arrayNode, taskOwner, inboxType);
				if (!ServicesUtil.isEmpty(processName) && processName.size() == 1) {
					List<CustomAttributeTemplate> customHeaders = customAttributeDao
							.getCustomAttributeTemplates(processName.get(0), null, true);
					if (!ServicesUtil.isEmpty(customHeaders) && customHeaders.size() > 0) {
						for (CustomAttributeTemplate customAttributeTemplate : customHeaders) {
							headerDto = new HeaderDto();
							headerDto.setType("CUSTOM");
							headerDto.setKey(customAttributeTemplate.getKey());
							headerDto.setName(customAttributeTemplate.getLabel());
							if (ServicesUtil.isEmpty(headers)) {
								headers = new ArrayList<HeaderDto>();
								headers.add(headerDto);
							} else {
								headers.add(headerDto);
							}
						}
					}
				}
				responseMessage.setStatus("Success");
				responseMessage.setStatusCode("0");
				responseMessage.setMessage("Task Details Fetched Successfully");
			}

			// get custom attributes in json format for all tasks
			if (!ServicesUtil.isEmpty(workBoxDtos)) {
				workBoxDtos = addCustomAttributestoList(workBoxDtos);
			}

			workboxResponseDto.setWorkBoxDtos(workBoxDtos);
			inboxHeaderDto.setHeaders(headers);
			workboxResponseDto.setHeaderDto(inboxHeaderDto);
			workboxResponseDto.setCustomAttributes(arrayNode);
			workboxResponseDto.setPageCount(PMCConstant.PAGE_SIZE);
			workboxResponseDto.setCount(taskCount);

			workboxResponseDto.setResponseMessage(responseMessage);
		} catch (Exception e) {
			System.err.println("[WBP-Dev]WorkBoxDao.getAdvanceFilterData() error: " + e.getStackTrace());
		}

		return workboxResponseDto;
	}

	/* Method fetches process query */
	private String getProcessQuery(List<String> processName) {
		String processQuery = "";

		if (!ServicesUtil.isEmpty(processName) && processName.size() > 0
				&& !PMCConstant.SEARCH_ALL.equalsIgnoreCase(processName.get(0))) {
			processQuery += " AND te.PROC_NAME IN (";
			for (String process : processName) {

				processQuery = processQuery + "'" + process + "',";
			}
			processQuery = processQuery.substring(0, processQuery.length() - 1);
			processQuery = processQuery + ")";
		}
		System.err.println("[WBP-Dev][PMC][processQuery]" + processQuery);
		return processQuery;
	}

	/* All report navigation filters are mentioned */
	private String reportNavigationQuery(String status, String reportType, String user, String completedOn,
			List<String> processName) {
		String queryString = "";
		String reportQuery = "";
		String statusQuery = "";
		String criticalTime = "0";
		String userQuery = "";
		String dateFrom = "";
		String dateTo = "";
		try {
			if (!ServicesUtil.isEmpty(reportType) && !reportType.equals(PMCConstant.REPORT_SLA_BREACHED))
				criticalTime = PMCConstant.CRITICAL_TIME;

			if ((!ServicesUtil.isEmpty(status) && status.equals("NOT COMPLETED")) || ServicesUtil.isEmpty(status))
				statusQuery = " AND TE.STATUS <> 'COMPLETED' ";
			else if (PMCConstant.TASK_STATUS_RESERVED.equals(status))
				statusQuery = " AND TE.STATUS in ('" + PMCConstant.TASK_STATUS_IN_PROGRESS + "','"
						+ PMCConstant.TASK_STATUS_RESERVED + "','" + PMCConstant.TASK_STATUS_RESOLVED + "') ";

			else
				statusQuery = " AND TE.STATUS='" + status + "' ";

			if (!ServicesUtil.isEmpty(user) && ServicesUtil.isEmpty(reportType)) {
				userQuery = " AND (TE.STATUS in ('" + PMCConstant.TASK_STATUS_IN_PROGRESS + "','"
						+ PMCConstant.TASK_STATUS_RESERVED + "','" + PMCConstant.TASK_STATUS_RESOLVED
						+ "') AND TE.CUR_PROC = '" + user
						+ "' OR (TE.EVENT_ID IN (SELECT EVENT_ID FROM TASK_OWNERS WHERE TASK_OWNER='" + user
						+ "' AND EVENT_ID IN "
						+ " (SELECT DISTINCT TO1.EVENT_ID FROM TASK_EVENTS TE1 JOIN PROCESS_CONFIG_TB"
						+ " PC1 ON PC1.PROCESS_NAME =TE1.PROC_NAME JOIN TASK_OWNERS TO1 ON TE1.EVENT_ID=TO1.EVENT_ID "
						+ "WHERE TE1.STATUS ='READY' " + " GROUP BY TO1.EVENT_ID HAVING COUNT(TO1.EVENT_ID)=1))))"
						+ " OR ( tw.is_substituted = 1	AND ( te.status <> 'COMPLETED' AND tw.TASK_OWNER = '" + user
						+ "' AND (te.status = 'READY' OR te.CUR_PROC = '" + user + "'))) ";
			} else if (!ServicesUtil.isEmpty(user))
				userQuery = " and tw.task_owner = '" + user + "' and (te.status = 'READY' or te.cur_proc = '" + user
						+ "') " + getProcessQuery(processName);

			if (!ServicesUtil.isEmpty(completedOn)) {
				reportType = PMCConstant.REPORT_COMPLETED_ON;
			}
			if (!ServicesUtil.isEmpty(reportType))
				switch (reportType) {
				case PMCConstant.REPORT_ON_TIME:

					if (!ServicesUtil.isEmpty(status) && status.equals(PMCConstant.TASK_COMPLETED))
						reportQuery += " AND TE.COMPLETED_AT < TE.COMP_DEADLINE ";
					else
						reportQuery += " AND CURRENT_TIMESTAMP < (ADD_SECONDS(TE.COMP_DEADLINE,-" + criticalTime
								+ ")) ";
					break;
				case PMCConstant.REPORT_CRITICAL:
					reportQuery += " AND CURRENT_TIMESTAMP > (ADD_SECONDS(TE.COMP_DEADLINE,-" + criticalTime
							+ ")) AND CURRENT_TIMESTAMP < TE.COMP_DEADLINE ";
					break;
				case PMCConstant.REPORT_SLA_BREACHED:
					if (!ServicesUtil.isEmpty(status) && status.equals(PMCConstant.TASK_COMPLETED))
						reportQuery += " AND TE.COMPLETED_AT > TE.COMP_DEADLINE ";
					else
						reportQuery += " AND CURRENT_TIMESTAMP > (ADD_SECONDS(TE.COMP_DEADLINE,-" + criticalTime
								+ ")) ";
					break;
				case PMCConstant.REPORT_COMPLETED_ON:
					if (!ServicesUtil.isEmpty(completedOn)) {
						if (completedOn.contains("-")) {
							// dateFrom = completedOn;
							// dateTo = ServicesUtil.getPreviousDay(dateFrom);

							dateFrom = completedOn;
							dateTo = ServicesUtil.getNextDay(completedOn);

						} else {
							dateFrom = ServicesUtil.monthStartDate(ServicesUtil.getMonthInNumber(completedOn));
							dateTo = ServicesUtil.monthEndDate(ServicesUtil.getMonthInNumber(completedOn));

						}
					}
					reportQuery += " AND TE.COMPLETED_AT BETWEEN '" + dateFrom + "' AND '" + dateTo + "' ";
					break;
				default:
				}
			queryString += statusQuery + userQuery + reportQuery;
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][PMC][WorkboxDao][GetreportFilter][getReportQuery][error]" + e.getLocalizedMessage());
		}
		return queryString;
	}

	/* For standards headers */
	private List<HeaderDto> getStandardHeaders(List<CustomAttributeTemplate> standardHeaders, String headerType) {
		HeaderDto headerDto = null;
		List<HeaderDto> headers = null;
		if (!ServicesUtil.isEmpty(standardHeaders) && standardHeaders.size() > 0) {
			headers = new ArrayList<HeaderDto>();
			for (CustomAttributeTemplate customAttributeTemplate : standardHeaders) {
				headerDto = new HeaderDto();
				headerDto.setType("STANDARD");
				headerDto.setKey(customAttributeTemplate.getKey());
				headerDto.setName(customAttributeTemplate.getLabel());
				headers.add(headerDto);
			}
		}
		return headers;
	}

	/* converting result set to required Dto */
	private List<WorkBoxDto> getWorkBoxDto(List<Object[]> resultList, List<String> processName, ArrayNode arrayNode,
			String userId, String inboxType) {
		ObjectNode objectNode = null;
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yy");
		// simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("IST"));
		List<WorkBoxDto> workBoxDtos = new ArrayList<WorkBoxDto>();
		for (Object[] obj : resultList) {
			WorkBoxDto workBoxDto = new WorkBoxDto();
			workBoxDto.setRequestId(obj[0] == null ? null : (String) obj[0]);
			workBoxDto.setProcessName(obj[1] == null ? null : (String) obj[1]);
			workBoxDto.setTaskId(obj[2] == null ? null : (String) obj[2]);
			workBoxDto.setTaskDescription(obj[3] == null ? null : (String) obj[3]);
			workBoxDto.setName(obj[4] == null ? null : (String) obj[4]);
			workBoxDto.setSubject(obj[5] == null ? null : (String) obj[5]);
			workBoxDto.setStartedBy(obj[6] == null ? null : (String) obj[6]);
			workBoxDto
					.setCreatedAt(obj[7] == null ? null : simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[7])));
			workBoxDto.setStatus(obj[8] == null ? null : (String) obj[8]);
			// CUR_PROC using user_id obj[9]

			// if (!ServicesUtil.isEmpty(inboxType) &&
			// PMCConstant.INBOX_TYPE_GROUP_TASK.equals(inboxType)) {
			if (!ServicesUtil.isEmpty(obj[8]) && (PMCConstant.TASK_STATUS_RESERVED.equals((String) obj[8])
					|| PMCConstant.TASK_STATUS_RESOLVED.equals((String) obj[8])
					|| PMCConstant.TASK_STATUS_IN_PROGRESS.equals((String) obj[8])))
				workBoxDto.setTaskOwner((String) obj[9]);
			else
				workBoxDto.setTaskOwner(null);
			// } else
			// workBoxDto.setTaskOwner(userId);
			if (!ServicesUtil.isEmpty(obj[13]) && !ServicesUtil.isEmpty(obj[7])) {
				System.err.println("[WBP-Dev][PMC][WorkBoxFacade][getWorkboxFilterData][ created]" + obj[7]);
				Calendar created = ServicesUtil.timeStampToCal(obj[7]);
				Calendar slaDate = ServicesUtil.timeStampToCal(obj[13]);
				String timeLeftString = ServicesUtil.getSLATimeLeft(slaDate);
				if (timeLeftString.equalsIgnoreCase("Breach")) {
					workBoxDto.setBreached(true);
				} else {
					workBoxDto.setBreached(false);
					workBoxDto.setTimeLeftDisplayString(timeLeftString);
					workBoxDto.setTimePercentCompleted(ServicesUtil.getPercntTimeCompleted(created, slaDate));
				}

				Calendar slaActualDate = Calendar.getInstance();
				slaActualDate.setTime(slaDate.getTime());
				slaDate.add(Calendar.HOUR_OF_DAY, 5);

				Calendar currentTime = Calendar.getInstance();
				if (currentTime.before(slaActualDate) && currentTime.after(slaDate)) {
					workBoxDto.setCritical(true);
				} else {
					workBoxDto.setCritical(false);
				}

			}
			workBoxDto.setSla(obj[10] == null ? null : ServicesUtil.asString(obj[10]));
			workBoxDto.setProcessId(obj[11] == null ? null : (String) obj[11]);
			workBoxDto.setUrl(obj[12] == null ? null : (String) obj[11]);
			workBoxDto.setSlaDueDate(
					obj[13] == null ? null : simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[13])));
			workBoxDto.setSlaDisplayDate(
					obj[13] == null ? null : simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[13])));

			workBoxDto.setSlaDisplayDate(
					obj[13] == null ? null : simpleDateFormat2.format(ServicesUtil.resultAsDate(obj[13])));
			workBoxDto.setForwardedBy(obj[14] == null ? null : (String) obj[14]);
			workBoxDto.setForwardedAt(
					obj[15] == null ? null : simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[15])));
			workBoxDto.setProcessDisplayName(obj[16] == null ? (String) obj[1] : (String) obj[16]);
			workBoxDto.setCompletedAt(
					obj[21] == null ? null : simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[21])));

			workBoxDto.setStartedByDisp(obj[22] == null ? null : (String) obj[22]);
			workBoxDto.setTaskType(obj[24] == null ? null : (String) obj[24]);
			try {
				workBoxDto.setOrigin(obj[23] == null ? null : (String) obj[23]);
			} catch (Exception e) {
				System.err.println(
						"[WBP-Dev]WorkBoxDao.getWorkBoxDto() error while parsing origin form query results : " + e);
			}

			if (!ServicesUtil.isEmpty(processName) && processName.size() == 1) {
				List<CustomAttributeValue> custAttributes = customAttributeDao
						.getCustomAttributes(workBoxDto.getTaskId());

				if (!ServicesUtil.isEmpty(custAttributes)) {
					objectNode = objectMapper.createObjectNode();
					for (CustomAttributeValue customAttributeValue : custAttributes) {
						objectNode.put(customAttributeValue.getKey(), customAttributeValue.getAttributeValue());
					}
				}
			}

			// if (!ServicesUtil.isEmpty(obj[20])) {
			// switch (workBoxDto.getStatus()) {
			// case PMCConstant.TASK_STATUS_READY:
			// if (!ServicesUtil.isEmpty(obj[17]) &&
			// ServicesUtil.asBoolean(obj[17])) {
			// workBoxDto.setStatus(PMCConstant.TASK_STATUS_RESERVED);
			// }
			// break;
			// case PMCConstant.TASK_STATUS_RESERVED:
			// if (!ServicesUtil.isEmpty(obj[18]) &&
			// ServicesUtil.asBoolean(obj[18])) {
			// workBoxDto.setStatus(PMCConstant.TASK_STATUS_READY);
			// }
			// break;
			// }
			// }
			//

			// if (!ServicesUtil.isEmpty(processName) && processName.size() ==
			// 1) {
			// List<CustomAttributeValue> custAttributes = customAttributeDao
			// .getCustomAttributes(workBoxDto.getTaskId());
			//
			// if (!ServicesUtil.isEmpty(custAttributes)) {
			// objectNode = objectMapper.createObjectNode();
			// for (CustomAttributeValue customAttributeValue : custAttributes)
			// {
			// objectNode.put(customAttributeValue.getKey(),
			// customAttributeValue.getAttributeValue());
			// }
			// }
			// }

			// Status Change Block
			// {
			// if (!ServicesUtil.isEmpty(obj[20])) {
			// switch (workBoxDto.getStatus()) {
			// case PMCConstant.TASK_STATUS_READY:
			// if (!ServicesUtil.isEmpty(obj[17]) &&
			// ServicesUtil.asBoolean(obj[17])) {
			// workBoxDto.setStatus(PMCConstant.TASK_STATUS_RESERVED);
			// }
			// break;
			// case PMCConstant.TASK_STATUS_RESERVED:
			// if (!ServicesUtil.isEmpty(obj[18]) &&
			// ServicesUtil.asBoolean(obj[18])) {
			// workBoxDto.setStatus(PMCConstant.TASK_STATUS_READY);
			// }
			// break;
			// }
			// }
			// }

			arrayNode.add(objectNode);
			workBoxDtos.add(workBoxDto);
		}
		return workBoxDtos;
	}

	/* Order by Query not being used now */
	private String getOrderByQuery(String orderType, String orderBy) {
		String orderByQuery = "";

		if (ServicesUtil.isEmpty(orderType) && ServicesUtil.isEmpty(orderBy))
			orderByQuery = orderByQuery + " ORDER BY 8 DESC";
		else {
			if (!ServicesUtil.isEmpty(orderType) && !ServicesUtil.isEmpty(orderBy)) {

				if (orderType.equalsIgnoreCase(PMCConstant.ORDER_TYPE_CREATED_AT)) {
					if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_BY_ASC))
						orderByQuery = orderByQuery + " ORDER BY 8 ASC";
					else {
						if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_BY_DESC))
							orderByQuery = orderByQuery + " ORDER BY 8 DESC";
					}
				} else if (orderType.equalsIgnoreCase(PMCConstant.ORDER_TYPE_SLA_DUE_DATE)) {
					if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_BY_ASC))
						orderByQuery = orderByQuery + " ORDER BY 14 ASC";
					else {
						if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_BY_DESC))
							orderByQuery = orderByQuery + " ORDER BY 14 DESC";
					}
				}
			} else {
				if (orderType.equalsIgnoreCase(PMCConstant.ORDER_TYPE_CREATED_AT))
					orderByQuery = orderByQuery + " ORDER BY 8 ASC";
				else {
					if (orderType.equalsIgnoreCase(PMCConstant.ORDER_TYPE_SLA_DUE_DATE))
						orderByQuery = orderByQuery + " ORDER BY 14 ASC";
				}
			}

		}
		return orderByQuery;
	}

	/* inbox action query */
	// private String getStatusString(String status, String taskOwner) {
	// String ret = "";
	// if (PMCConstant.TASK_STATUS_READY.equalsIgnoreCase(status)) {
	// ret = " IA.IS_RELEASED = TRUE";
	// } else if (PMCConstant.TASK_STATUS_RESERVED.equalsIgnoreCase(status)) {
	// ret = " IA.IS_CLAIMED = TRUE and IA.USER_CLAIMED = '" + taskOwner + "'";
	// } else if (PMCConstant.TASK_COMPLETED.equals(status)) {
	// ret = " IA.IS_RELEASED= TRUE or ( IA.IS_CLAIMED = TRUE and
	// IA.USER_CLAIMED = '" + taskOwner + "' )";
	// }
	// return ret;
	// }
	//
	// /* Inbox Type + isAdmin query is getting fetched by this method */
	private String getInboxTypeQuery(String inboxType, Boolean isAdmin, String taskOwner, String reportFilter,
			String status) {
		String inboxTypeQuery = "";
		if (!ServicesUtil.isEmpty(inboxType) && ServicesUtil.isEmpty(reportFilter)) {
			if (PMCConstant.INBOX_TYPE_GROUP_TASK.equals(inboxType)) {
				if (isAdmin.equals(false)) {
					inboxTypeQuery = " AND TE.EVENT_ID IN (SELECT EVENT_ID FROM TASK_OWNERS WHERE TASK_OWNER='"
							+ taskOwner + "' AND EVENT_ID IN (SELECT DISTINCT TO1.EVENT_ID FROM TASK_EVENTS TE1 JOIN"
							+ " PROCESS_CONFIG_TB PC1 ON PC1.PROCESS_NAME=TE1.PROC_NAME JOIN TASK_OWNERS TO1 "
							+ " ON TE1.EVENT_ID=TO1.EVENT_ID WHERE te.status='READY' "
							+ " GROUP BY TO1.EVENT_ID HAVING COUNT(TO1.EVENT_ID)>1)) and (tw.task_owner='" + taskOwner
							+ "' and tw.is_substituted is null ) ";
				} else {
					/*
					 * As admin can see all task irrespective of anything
					 * backlog
					 */

				}
			} else if (PMCConstant.INBOX_TYPE_MY_INBOX.equals(inboxType)) {

				if (PMCConstant.TASK_COMPLETED.equals(status))
					inboxTypeQuery = " AND te.cur_proc='" + taskOwner + "' ";
				else if (isAdmin.equals(false))
					inboxTypeQuery = " AND (te.status in ('" + PMCConstant.TASK_STATUS_RESERVED + "','"
							+ PMCConstant.TASK_STATUS_IN_PROGRESS + "','" + PMCConstant.TASK_STATUS_READY + "','"
							+ PMCConstant.TASK_STATUS_RESOLVED + "')) "
							+ " and ((te.event_id in (select event_id from task_owners where task_owner='" + taskOwner
							+ "' and event_id in (select distinct to1.event_id from task_events te1"
							+ " join PROCESS_CONFIG_TB pc1 on pc1.process_name =te1.proc_name "
							+ " join task_owners to1 on te1.event_id=to1.event_id where te.status='READY' "
							+ " group by to1.event_id having count(to1.event_id)=1)))" + ""
							+ " OR ( tw.is_substituted = 1	AND ( te.status <> 'COMPLETED' AND tw.TASK_OWNER = '"
							+ taskOwner + "' AND (te.status = 'READY' OR te.CUR_PROC = '" + taskOwner + "'))))";
			} else if (PMCConstant.INBOX_TYPE_ADMIN.equals(inboxType)) {

				inboxTypeQuery += " or te.event_id in (select te1.event_id from task_events te1 inner join "
						+ "task_owners to1 on to1.event_id = te1.event_id inner join process_config_tb pct1 "
						+ "on pct1.process_name = te1.proc_name where te1.status = 'READY' and "
						+ "te1.event_id in (select distinct event_id from task_owners group by "
						+ "event_id having count(*) = 1) and to1.task_owner = '" + taskOwner + "') ";
			} else if (PMCConstant.INBOX_TYPE_SUBSTITUTION_INBOX.equals(inboxType)) {

				if (PMCConstant.TASK_COMPLETED.equals(status))
					inboxTypeQuery = " AND te.cur_proc='" + taskOwner + "'  and  tw.is_substituted = 1 ";
				else if (isAdmin.equals(false))
					inboxTypeQuery = " AND ((te.status in ('" + PMCConstant.TASK_STATUS_RESERVED + "','"
							+ PMCConstant.TASK_STATUS_IN_PROGRESS + "','" + PMCConstant.TASK_STATUS_READY + "','"
							+ PMCConstant.TASK_STATUS_RESOLVED + "')) "

							+ " and ( tw.is_substituted = 1	AND ( te.status <> 'COMPLETED' AND tw.TASK_OWNER = '"
							+ taskOwner + "' AND (te.status = 'READY' OR te.CUR_PROC = '" + taskOwner + "'))))";

			}
		}
		/* for Completed task */

		return inboxTypeQuery;
	}

	/* If custom Attributes gets added then we will use this method */
	private String getCustomAttributeQuery(List<CustomAttributeTemplate> customDto, List<String> processName) {
		String customSearchQuery = "";
		String processQuery = "";
		Boolean flag = false;// to check custom filter has values are not
		for (String process : processName) {
			processQuery = processQuery + "'" + process + "',";
		}
		processQuery = processQuery.substring(0, processQuery.length() - 1);

		if (!ServicesUtil.isEmpty(customDto)) {
			List<CustomAttributeTemplate> customSearch = customDto;
			int noOfCustomAttribute = customDto.size();
			customSearchQuery += " AND (cav.task_id IN (SELECT distinct ca1.task_id FROM custom_attr_values ca1 ";
			for (int k = 2; k <= noOfCustomAttribute; k++) {
				customSearchQuery += " join custom_attr_values ca" + k + " on ca" + (k - 1) + ".task_id=ca" + k
						+ ".task_id ";
			}
			customSearchQuery += " WHERE ";
			int i = 1;
			for (CustomAttributeTemplate customValue : customSearch) {
				if (!ServicesUtil.isEmpty(customValue.getValue())) {
					flag = true;
					customSearchQuery += " (ca" + i + ".key='" + customValue.getKey() + "' and ca" + i
							+ ".process_name in (" + processQuery + ") and upper(ca" + i + ".attr_value) like upper('%"
							+ customValue.getValue() + "%')) and";
					i++;
				}
			}
			customSearchQuery = customSearchQuery.substring(0, customSearchQuery.length() - 3);
			customSearchQuery += ") )";
		}

		if (!flag) {
			customSearchQuery = "";
		}
		return customSearchQuery;
	}

	/*
	 * It's calling from SLAManagementFacade for getting process name along with
	 * task count for my inbox and group inbox /admin inbox
	 */

	public FilterMetadataDto getFilterMetadata(String processName,Token token) {
		FilterMetadataDto metadataDto = new FilterMetadataDto();
		List<FilterLayoutDto> standardFilter = new ArrayList<>();
		List<FilterLayoutDto> customFilter = new ArrayList<>();
		if (ServicesUtil.isEmpty(processName)) {

			String selectQuery = "SELECT COLUMN_ID,COLUMN_NAME,LABEL,DATA_TYPE,TABLE_NAME,TABLE_ALIAS,FILTER_TYPE from filter_metadata where is_active= 1 ORDER BY COLUMN_ID  ";
			Query q = this.getSession().createSQLQuery(selectQuery.trim());
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				FilterLayoutDto layoutDto = new FilterLayoutDto();
				layoutDto.setColumnId(obj[0] == null ? null : (int) obj[0]);
				layoutDto.setColumnName(obj[1] == null ? null : (String) obj[1]);
				layoutDto.setLabel(obj[2] == null ? null : (String) obj[2]);
				layoutDto.setDatatype(obj[3] == null ? null : (String) obj[3]);
				layoutDto.setTableName(obj[4] == null ? null : (String) obj[4]);
				layoutDto.setTableAlias(obj[5] == null ? null : (String) obj[5]);
				layoutDto.setFilterType(obj[6] == null ? null : (String) obj[6]);
				layoutDto.setSelectionList("/inbox/selectionList?selectionParameter=" + layoutDto.getTableAlias() + "."
						+ layoutDto.getColumnName());
				layoutDto.setConditionList("/inbox/selectionList?selectionParameter=" + layoutDto.getDatatype());

				if (PMCConstant.COLUMNS_FOR_AUTOCOMPLETE.contains(layoutDto.getColumnName().toLowerCase())) {
					layoutDto.setSelectionList("/inbox/selectionList?selectionParameter=" + layoutDto.getTableAlias()
							+ "." + layoutDto.getColumnName());
					layoutDto.setAutoCompleteType(PMCConstant.DEFAULT);
				}
				standardFilter.add(layoutDto);

			}
		} else {

			List<CustomAttributeTemplate> attributeTemplates = userCustomHeadersDao
					.getUserDefinedCustomAttributes(token.getLogonName(), processName);
			int i = 1;
			for (CustomAttributeTemplate customAttributeTemplate : attributeTemplates) {

				if (customAttributeTemplate.getIsActive() && !("button".equalsIgnoreCase(customAttributeTemplate.getDataType()))) {
					FilterLayoutDto dto = new FilterLayoutDto();
					dto.setColumnName(customAttributeTemplate.getKey());
					dto.setColumnId(i);
					dto.setLabel(customAttributeTemplate.getLabel());

					dto.setDatatype(
							customAttributeTemplate.getDataType().equalsIgnoreCase("DATETYPE") ? "DATETYPE" : "STRING");
					dto.setFilterType(customAttributeTemplate.getDataType().equalsIgnoreCase("DATETYPE") ? "DATEFILTER"
							: "INPUT");
					if (customAttributeTemplate.getDataType().equalsIgnoreCase("DROPDOWN")) {
						dto.setFilterType("COMBOBOX");
					}
					dto.setTableAlias("ca");
					dto.setTableName("CUSTOM_ATTR_VALUES");

					if (!customAttributeTemplate.getDataType().equalsIgnoreCase("DATETYPE")) {
						dto.setSelectionList("/customAttribute/autoComplete?processName=" + processName + "&key="
								+ customAttributeTemplate.getKey() + "&value=");
						dto.setAutoCompleteType(PMCConstant.CUSTOM);
					}

					dto.setConditionList("/inbox/selectionList?selectionParameter=" + dto.getDatatype());

					customFilter.add(dto);
				}
				i++;

			}

		}

		metadataDto.setCustomFilter(customFilter);
		metadataDto.setStandardFilter(standardFilter);

		return metadataDto;

	}

	public KeyValueResponseDto getSelectionList(String selection) {

		KeyValueResponseDto responseDto = new KeyValueResponseDto();

		String selectQuery = "SELECT CONSTANT_NAME,CONSTANT_VALUE from cross_constants where UPPER(constant_ID)=UPPER('"
				+ selection + "')  ";

		try {
			Query q = this.getSession().createSQLQuery(selectQuery.trim());
			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			if (!ServicesUtil.isEmpty(resultList)) {
				List<KeyValueDto> dtos = new ArrayList<>();
				for (Object[] obj : resultList) {
					KeyValueDto dto = new KeyValueDto();
					dto.setValue(obj[0] == null ? null : (String) obj[0]);
					dto.setKey(obj[1] == null ? null : (String) obj[1]);
					dtos.add(dto);

				}
				responseDto.setKeyValuePairs(dtos);
				responseDto
						.setMessage(new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS, "Values Found"));

			}

			else {
				responseDto.setMessage(
						new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE, "No value found"));
			}

		} catch (Exception e) {
			responseDto.setMessage(new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE, "Error"));

		}

		return responseDto;

	}

}
