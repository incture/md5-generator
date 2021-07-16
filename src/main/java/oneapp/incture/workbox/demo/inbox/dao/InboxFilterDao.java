package oneapp.incture.workbox.demo.inbox.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dao.ActionConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.detailpage.dao.DetailPageDao;
import oneapp.incture.workbox.demo.detailpage.service.DetailPageFacade;
import oneapp.incture.workbox.demo.inbox.dto.AdvanceFilterDetailDto;
import oneapp.incture.workbox.demo.inbox.dto.HeaderDto;
import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.InboxTasksHeaderDto;
import oneapp.incture.workbox.demo.inbox.dto.QuickFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.SortingDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxRequestFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxResponseDto;
import oneapp.incture.workbox.demo.inbox.util.FilterConstants;
import oneapp.incture.workbox.demo.userCustomAttributes.dao.UserCustomHeadersDao;

@Repository("InboxFilterDao")
//////@Transactional
public class InboxFilterDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	UserCustomHeadersDao userCustomHeadersDao;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	CustomAttributeDao customAttributeDao;

	@Autowired
	InboxTypeDao inboxTypeDao;

	@Autowired
	ActionConfigDao actionConfigDao;
	
	@Autowired
	DetailPageDao detailPageDao;

	public String getSelectQuery() {
		String selectQuery = " SELECT DISTINCT PE.REQUEST_ID, PE.NAME AS PROCESS_NAME,"
				+ " TE.EVENT_ID, TE.DESCRIPTION, TE.NAME AS TASK_NAME, TE.SUBJECT, "
				+ " PE.STARTED_BY, TE.CREATED_AT, TE.STATUS, TE.CUR_PROC, "
				+ " 'TS.SLA', TE.PROCESS_ID, TE.URL, TE.COMP_DEADLINE, " + "TE.FORWARDED_BY, TE.FORWARDED_AT, "
				+ " PCT.PROCESS_DISPLAY_NAME, IA.IS_CLAIMED, IA.IS_RELEASED,"
				+ " IA.USER_CLAIMED, IA.MODIFIED_AT, TE.COMPLETED_AT, PE.STARTED_BY_DISP,TE.ORIGIN,TE.TASK_TYPE,TE.UPDATED_AT, "
				+ " CASE WHEN TE.COMPLETED_AT IS NULL THEN (CASE WHEN CURRENT_TIMESTAMP > TE.COMP_DEADLINE THEN 'BREACHED' "
				+ " WHEN CURRENT_TIMESTAMP > ADD_SECONDS(TE.COMP_DEADLINE,-PCT.CRITICAL_DATE*60*60"
				+ ") AND CURRENT_TIMESTAMP < TE.COMP_DEADLINE THEN 'CRITICAL' " + " ELSE 'ON_TIME' END) "
				+ "	ELSE (CASE WHEN TE.COMPLETED_AT > TE.COMP_DEADLINE THEN 'BREACHED' "
				+ " ELSE 'ON_TIME' END) END AS TASK_SLA,(CASE WHEN (TE.BUSINESS_STATUS IS NOT NULL AND TE.BUSINESS_STATUS != '' ) THEN "
				+ " TE.BUSINESS_STATUS ELSE (SC.BUSINESS_STATUS) END) AS BUSINESS_STATUS ";
		return selectQuery;
	}

	public String getPinnedTaskQuery(Token user) {
		String PinnedTaskQuery = ",(SELECT PINNED_TASK_ID FROM PINNED_TASK WHERE PINNED_TASK_ID = TE.EVENT_ID "
				+ "AND USER_ID = '" + user.getLogonName() + "') ";
		return PinnedTaskQuery;
	}

	public static String getCommonJoinQuery() {
		String commonJoinQuery = " FROM TASK_EVENTS TE INNER JOIN PROCESS_EVENTS PE ON TE.PROCESS_ID = PE.PROCESS_ID "
				+ " LEFT JOIN TASK_OWNERS TW ON TE.EVENT_ID = TW.EVENT_ID "
				+ " LEFT JOIN INBOX_ACTIONS IA ON TE.EVENT_ID = IA.TASK_ID "
				+ " LEFT JOIN STATUS_CONFIG SC ON SC.STATUS = TE.STATUS AND SC.PROCESS_NAME ='DEFAULT' "
				+ " INNER JOIN PROCESS_CONFIG_TB PCT ON PE.NAME = PCT.PROCESS_NAME "
				+ " LEFT OUTER JOIN (SELECT CAV.TASK_ID FROM CUSTOM_ATTR_VALUES CAV GROUP BY TASK_ID) AS CAV "
				+ " ON TE.EVENT_ID = CAV.TASK_ID OR (TE.PROCESS_ID = CAV.TASK_ID AND TE.ORIGIN = 'Ad-hoc') ";

		// (select cav.task_id from custom_attr_values cav group by task_id) as
		// cav

		return commonJoinQuery;
	}

	public static String getCommonJoinQueryForFileExport() {
		String commonJoinQuery = " INNER JOIN PROCESS_EVENTS PE ON TE.PROCESS_ID = PE.PROCESS_ID "
				+ " LEFT JOIN TASK_OWNERS TW ON TE.EVENT_ID = TW.EVENT_ID "
				+ " LEFT JOIN INBOX_ACTIONS IA ON TE.EVENT_ID = IA.TASK_ID "
				+ " INNER JOIN PROCESS_CONFIG_TB PCT ON PE.NAME = PCT.PROCESS_NAME "
				+ " LEFT OUTER JOIN (SELECT CAV.TASK_ID FROM CUSTOM_ATTR_VALUES CAV GROUP BY TASK_ID) AS CAV "
				+ " ON TE.EVENT_ID = CAV.TASK_ID ";

		// (select cav.task_id from custom_attr_values cav group by task_id) as
		// cav

		return commonJoinQuery;
	}

	public static String getCommonConditionQuery(String inboxType, QuickFilterDto filterDto) {
		if (PMCConstant.NOTIFICATION.equalsIgnoreCase(inboxType)) {
			String commonConditionQuery = " WHERE (PE.STATUS <> 'CANCELED') "
					+ "AND TE.COMP_DEADLINE IS NOT NULL AND TW.TASK_OWNER IS NOT NULL";
			return commonConditionQuery;
		}
		String commonConditionQuery = " WHERE (PE.STATUS <> 'CANCELED' AND PE.STATUS <> 'COMPLETED') "
				+ "AND TE.COMP_DEADLINE IS NOT NULL ";

		if (!PMCConstant.PANEL_TEMPLATE_ID_DRAFT.equalsIgnoreCase(inboxType))
			commonConditionQuery += "AND TW.TASK_OWNER IS NOT NULL ";

		if (!ServicesUtil.isEmpty(filterDto) && (PMCConstant.TASK_COMPLETED.equalsIgnoreCase(filterDto.getStatus())
				|| PMCConstant.TASK_STATUS_ALL_COMPLETED.equalsIgnoreCase(filterDto.getStatus()))) {
			commonConditionQuery = " WHERE PE.STATUS NOT IN ('CANCELED') AND TE.COMP_DEADLINE IS NOT NULL AND TW.TASK_OWNER IS NOT NULL ";
			return commonConditionQuery;
		}
		if (ServicesUtil.isEmpty(inboxType)
				|| (!PMCConstant.PANEL_TEMPLATE_ID_COMPLETEDTASKS.equalsIgnoreCase(inboxType)
						&& !PMCConstant.PANEL_TEMPLATE_ID_DRAFT.equalsIgnoreCase(inboxType))
						&& !PMCConstant.PANEL_TEMPLATE_ID_CREATEDTASKS.equalsIgnoreCase(inboxType)) {// &&
																										// !PMCConstant.PANEL_TEMPLATE_ID_ADMININBOX.equalsIgnoreCase(inboxType)
			String query = "";
			if (filterDto != null && filterDto.getUserId() != null)
				query = " '" + filterDto.getUserId() + "' ";
			else
				query = " TW.TASK_OWNER ";
			// commonConditionQuery += " AND TE.STATUS IN
			// ('READY','RESERVED','DRAFT') ";
			commonConditionQuery += " AND (TE.STATUS = '" + PMCConstant.TASK_STATUS_READY + "' " + "OR (TE.CUR_PROC = "
					+ query + "AND TE.STATUS = '" + PMCConstant.TASK_STATUS_RESERVED + "')) ";
			// + "OR TE.STATUS = 'DRAFT') ";
		}
		if (PMCConstant.PANEL_TEMPLATE_ID_COMPLETEDTASKS.equalsIgnoreCase(inboxType)
				|| PMCConstant.PANEL_TEMPLATE_ID_ALLTASKS.equalsIgnoreCase(inboxType)
				|| PMCConstant.PANEL_TEMPLATE_ID_REQUESTORCOMPLETED.equalsIgnoreCase(inboxType)) {
			commonConditionQuery = " WHERE 1=1 ";
		}

		return commonConditionQuery;
	}

	public WorkboxResponseDto getInboxFilterData(InboxFilterDto inboxFilterDto, Token token) {

		WorkboxResponseDto workboxResponseDto = new WorkboxResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(PMCConstant.STATUS_FAILURE);
		responseMessage.setStatusCode("1");
		responseMessage.setMessage(PMCConstant.STATUS_FAILURE);

		workboxResponseDto.setResponseMessage(responseMessage);

		// for groups inboxType is "groupId" and inboxName is "Groups" so need
		// to swap
		if (!ServicesUtil.isEmpty(inboxFilterDto.getInboxName()) && !ServicesUtil.isEmpty(inboxFilterDto.getInboxType())
				&& !"AllTask".equals(inboxFilterDto.getInboxName())) {
			String temp = inboxFilterDto.getInboxName();
			inboxFilterDto.setInboxName(inboxFilterDto.getInboxType());
			inboxFilterDto.setInboxType(temp);
		}

		try {
//			User user = UserManagementUtil.getLoggedInUser();
//			System.err.println("[WBP-Dev]user : " + token.getLogonName());
			ArrayNode arrayNode = objectMapper.createArrayNode();
			List<WorkBoxDto> workBoxDtos = null;
			InboxTasksHeaderDto inboxHeaderDto = new InboxTasksHeaderDto();
			HeaderDto headerDto = null;
			String dataQuery = "";
			String countQuery = "";
			String orderByQuery = "";
			String process = "";

			List<HeaderDto> headers = null;

			// Preparing COMMON condition query-----------

			String selectQuery = getSelectQuery();

			String pinnedTaskQuery = getPinnedTaskQuery(token);

			String commonJoinQuery = getCommonJoinQuery();

			String commonConditionQuery = getCommonConditionQuery(inboxFilterDto.getInboxType(),
					inboxFilterDto.getQuickFilter());

			// Preparing ADVANCED condition query-------------

			String advancedFilterQuery = getAdvanceFilterQuery(inboxFilterDto);

			// Preparing QuickFilter query----------------------

			String quickFilterQuery = prepareQuickFilterQuery(inboxFilterDto.getQuickFilter(), token);

			// Preparing INBOXTYPE condition query---------------

			String inboxTypeQuery = "";
			System.err.println("[WBP-Dev][InboxFilter]" + inboxFilterDto.getInboxType());
			switch (inboxFilterDto.getInboxType()) {
			case PMCConstant.PANEL_TEMPLATE_ID_MYINBOX:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getMyTaskQuery(token.getLogonName());
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_ADMININBOX:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getAdminInboxTaskQuery(inboxFilterDto);
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_GROUPS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getTaskByGroupQuery(inboxFilterDto.getInboxName());
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_SUBSTITUTIONINBOX:
				System.err.println("[WBP-Dev][Substitution check] called");
				inboxTypeQuery = inboxTypeQuery + inboxTypeDao.getSubstitutedTaskQuery(token.getLogonName());
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_DRAFT:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getDraftTaskQuery();
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_VIEWS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getViewTaskQuery();
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_CREATEDTASKS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getCreatedTaskQuery();
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_COMPLETEDTASKS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getCompletedTaskQuery();
				break;
			case PMCConstant.PINNED_TASKS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getPinnedTaskQuery();
				break;
			case PMCConstant.USER_TASKS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getUserTaskQuery(inboxFilterDto.getUserId());
				break;

			case PMCConstant.PANEL_TEMPLATE_ID_ALLTASKS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getAllTaskQuery();
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_REQUESTORCOMPLETED:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getRequestorCompletedQuery("");
				break;

			default:
				// code block
			}

			// Prepare order by query
			if (!ServicesUtil.isEmpty(inboxFilterDto.getSortingDtos())) {
				orderByQuery = getOrderByQuery(inboxFilterDto.getSortingDtos());
			} else {
				orderByQuery = " ORDER BY 8 DESC ";
			}

			// Preparing COUNT query

			countQuery = "SELECT COUNT(*) FROM ( " + selectQuery + commonJoinQuery + commonConditionQuery
					+ advancedFilterQuery + quickFilterQuery + inboxTypeQuery + " )";

			System.err.println("[WBP-Dev][PMC][Demo][getfilterQuery][countQuery][new]" + countQuery);

			Query countQry = this.getSession().createSQLQuery(countQuery.trim());
			setQueryParameter(inboxFilterDto, token, countQry);
			BigDecimal taskCount = ServicesUtil.getBigDecimal(countQry.uniqueResult());

			System.err.println("[WBP-Dev]taskCount : " + taskCount);

			if (!ServicesUtil.isEmpty(inboxFilterDto.getReturnCountOnly()) && inboxFilterDto.getReturnCountOnly()) {
				responseMessage.setStatus("Success");
				responseMessage.setStatusCode("0");
				responseMessage.setMessage("Task Count Fetched Successfully");
				workboxResponseDto.setCount(taskCount);
				workboxResponseDto.setResponseMessage(responseMessage);
				return workboxResponseDto;

			}

			dataQuery = selectQuery + pinnedTaskQuery + commonJoinQuery + commonConditionQuery + advancedFilterQuery
					+ quickFilterQuery + inboxTypeQuery + orderByQuery;

			Query q = this.getSession().createSQLQuery(dataQuery.trim());

			System.err.println("[WBP-Dev][PMC][Demo][WorkBoxFacade][getWorkboxFilterData][DataQuery][new]" + dataQuery);
			setQueryParameter(inboxFilterDto, token, q);

			if (!ServicesUtil.isEmpty(inboxFilterDto.getPage()) && inboxFilterDto.getPage() > 0) {
				int first = (inboxFilterDto.getPage() - 1) * PMCConstant.PAGE_SIZE;
				int last = PMCConstant.PAGE_SIZE;
				q.setFirstResult(first);
				q.setMaxResults(last);
			}

			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			String processName = getProcessFilterIfPresent(inboxFilterDto);

			if (ServicesUtil.isEmpty(resultList)) {
				responseMessage.setMessage(PMCConstant.NO_RESULT);
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {

				workBoxDtos = getWorkBoxDto(resultList, arrayNode, token.getLogonName(), inboxFilterDto.getInboxType(),
						processName);

				headers = getStandardHeaders(
						userCustomHeadersDao.getUserDefinedCustomAttributes(token.getLogonName(), "STANDARD"),
						PMCConstant.STANDARD_HEADER,  processName);
				if(processName.equalsIgnoreCase("CourseRegistration")||processName.equalsIgnoreCase("Reimbursementofregisteredcourses"))
				{
					headerDto = new HeaderDto();
					headerDto.setType("STANDARD");
					headerDto.setKey("processDisplayName");
					headerDto.setName("Process Name");
					headers.add(headerDto);
					
				}
				

				if (!ServicesUtil.isEmpty(inboxFilterDto.getAdvanceFilter())
						&& !ServicesUtil.isEmpty(inboxFilterDto.getAdvanceFilter().getFilterMap())
						&& inboxFilterDto.getAdvanceFilter().getFilterMap().containsKey("pe.NAME")) {
					process = inboxFilterDto.getAdvanceFilter().getFilterMap().get("pe.NAME").getValue();
				}
				if (!ServicesUtil.isEmpty(process)) {

					System.err.println("InboxFilterDao.getInboxFilterData()custom headers fething start time : "
							+ System.currentTimeMillis());
					List<CustomAttributeTemplate> customHeaders = userCustomHeadersDao.getUserDefinedCustomAttributes(
							token.getLogonName(), process.substring(1, process.length() - 1));
					System.err.println("InboxFilterDao.getInboxFilterData() custom headers fetching end time : "
							+ System.currentTimeMillis());

					System.err.println("InboxFilterDao.getInboxFilterData()custom headers processing start time : "
							+ System.currentTimeMillis());
					customHeaders = customHeaders.stream()
							.filter(a -> a.getIsActive().equals(true) && !a.getDataType().equalsIgnoreCase("BUTTON")
									&& !a.getDataType().equalsIgnoreCase("ATTACHMENT")
									&& !a.getProcessName().equalsIgnoreCase("STANDARD"))
							.limit(3)
							.collect(Collectors.toList());

					/*
					 * headers = getStandardHeaders(customHeaders.stream() .filter(a ->
					 * a.getProcessName().equalsIgnoreCase("STANDARD")).collect(
					 * Collectors.toList()), PMCConstant.STANDARD_HEADER);
					 */

					System.err.println("InboxFilterDao.getInboxFilterData() custom headers processing end time : "
							+ System.currentTimeMillis());

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
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][InboxFiletrData]error" + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return workboxResponseDto;
	}

	private String getProcessFilterIfPresent(InboxFilterDto inboxFilterDto) {
		String processName = "";
		if (!ServicesUtil.isEmpty(inboxFilterDto.getAdvanceFilter())) {

			if (!ServicesUtil.isEmpty(inboxFilterDto.getAdvanceFilter().getFilterMap())) {

				processName = inboxFilterDto.getAdvanceFilter().getFilterMap().containsKey("pe.NAME")
						? inboxFilterDto.getAdvanceFilter().getFilterMap().get("pe.NAME").getValue()
						: "";
				if (!processName.contains(",")) {
					processName = processName.replaceAll("'", "");
				} else {
					return "";
				}

			} else if (!ServicesUtil.isEmpty(inboxFilterDto.getQuickFilter())
					&& !ServicesUtil.isEmpty(inboxFilterDto.getQuickFilter().getProcessName())) {
				processName = inboxFilterDto.getQuickFilter().getProcessName();

			}
		}
		return processName;

	}

	public String getAdvanceFilterQuery(InboxFilterDto inboxFilterDto) {
		String advancedFilterQuery = "";

		if (!ServicesUtil.isEmpty(inboxFilterDto.getAdvanceFilter())) {

			WorkboxRequestFilterDto advancedFilterDto = inboxFilterDto.getAdvanceFilter();
			if (!ServicesUtil.isEmpty(advancedFilterDto.getAdvanceSearch())) {
				advancedFilterQuery += " AND CONTAINS (*,'%" + advancedFilterDto.getAdvanceSearch()
						+ "%',FUZZY (0.6))  ";
			} else {
				if (!ServicesUtil.isEmpty(advancedFilterDto.getFilterMap())) {

					String processName = advancedFilterDto.getFilterMap().containsKey("pe.NAME")
							? advancedFilterDto.getFilterMap().get("pe.NAME").getValue()
							: "";
					if (!processName.contains(",")) {
						processName = processName.replaceAll("'", "");
					} else {
						processName = "";
					}

					String advanceQuery = prepareAdvanceFilterQuery(advancedFilterDto.getFilterMap(), processName,
							inboxFilterDto.getInboxType());

					if (!ServicesUtil.isEmpty(advanceQuery)) {
						advanceQuery = " AND ( 1=1 " + advanceQuery + ") ";
					}
					advancedFilterQuery += advanceQuery;

				}
			}
		}
		return advancedFilterQuery;
	}

	private void setQueryParameter(InboxFilterDto inboxFilterDto, Token user, Query q) {
		// TODO Auto-generated method stub
		switch (inboxFilterDto.getInboxType()) {
		/*
		 * case PMCConstant.PANEL_TEMPLATE_ID_MYINBOX: q.setParameter("taskOwner",
		 * user.getName().toUpperCase()); break;
		 */
		case PMCConstant.PANEL_TEMPLATE_ID_ADMININBOX:
			break;
		case PMCConstant.PANEL_TEMPLATE_ID_GROUPS:
			q.setParameter("taskOwner", user.getLogonName());
			if (inboxFilterDto.getInboxName() != null) {
				q.setParameter("groupId", inboxFilterDto.getInboxName());
			}
			break;
		// case PMCConstant.PANEL_TEMPLATE_ID_SUBSTITUTIONINBOX:
		// q.setParameter("taskOwner", user.getName().toUpperCase());
		// break;
		case PMCConstant.PANEL_TEMPLATE_ID_DRAFT:
			q.setParameter("taskOwner", user.getLogonName());
			break;
		case PMCConstant.PANEL_TEMPLATE_ID_VIEWS:
			break;
		case PMCConstant.PANEL_TEMPLATE_ID_CREATEDTASKS:
			q.setParameter("taskOwner", user.getLogonName());
			break;
		case PMCConstant.PANEL_TEMPLATE_ID_COMPLETEDTASKS:
			q.setParameter("taskOwner", user.getLogonName());
			break;
		case PMCConstant.PINNED_TASKS:
			q.setParameter("taskOwner", user.getLogonName());
			break;
		case PMCConstant.PANEL_TEMPLATE_ID_REQUESTORCOMPLETED:
			q.setParameter("taskOwner", user.getLogonName());
			break;
		default:
			// code block
		}
	}

	/* For standards headers */
	private List<HeaderDto> getStandardHeaders(List<CustomAttributeTemplate> standardHeaders, String headerType, String processName) {
		HeaderDto headerDto = null;
		List<HeaderDto> headers = null;
		if (!ServicesUtil.isEmpty(standardHeaders) && standardHeaders.size() > 0) {
			headers = new ArrayList<HeaderDto>();
			for (CustomAttributeTemplate customAttributeTemplate : standardHeaders) {
				headerDto = new HeaderDto();
				headerDto.setType("STANDARD");
				headerDto.setKey(customAttributeTemplate.getKey());
				headerDto.setName(customAttributeTemplate.getLabel());
				
				if(processName.equalsIgnoreCase("CourseRegistration")||processName.equalsIgnoreCase("Reimbursementofregisteredcourses") ||
						processName.equalsIgnoreCase("CourseRegistrationDemo") || processName.equalsIgnoreCase("ReimbursementofregisteredcoursesDemo")){			
					if(customAttributeTemplate.getKey().equalsIgnoreCase("createdAt")) {
						continue;
					}
					if(customAttributeTemplate.getKey().equals("status")) {
						headerDto.setKey("businessStatus");
					}	
				}
				headers.add(headerDto);
			}
		}
		return headers;
	}

	/* converting result set to required Dto */
	private List<WorkBoxDto> getWorkBoxDto(List<Object[]> resultList, ArrayNode arrayNode, String userId,
			String inboxType, String processName) {
		System.err.println("[WBP-Dev]getWorkBoxDto ");
		ObjectNode objectNode = null;
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yy");
		// simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("IST"));
		List<WorkBoxDto> workBoxDtos = new ArrayList<WorkBoxDto>();
		Map<String, Map<String, List<ActionConfigDto>>> actionDto = actionConfigDao.getActionConfig(inboxType);
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
			workBoxDto.setStatus(
					workBoxDto.getStatus().equalsIgnoreCase("APPROVE") ? "APPROVED" : workBoxDto.getStatus());
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
			workBoxDto.setUrl(obj[12] == null ? null : (String) obj[12]);
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
			if (workBoxDto.getStartedBy() != null && workBoxDto.getStartedBy().contains("oauth_client")) {
				workBoxDto.setStartedByDisp("Workflow System");
			}
			workBoxDto.setTaskType(obj[24] == null ? null : (String) obj[24]);
			workBoxDto.setUpdatedAt(
					obj[25] == null ? null : simpleDateFormat1.format(ServicesUtil.resultAsDate(obj[25])));
			workBoxDto.setTaskSla(obj[26] != null ? obj[26].toString() : null);
			workBoxDto.setBusinessStatus(obj[27] != null ? obj[27].toString() : workBoxDto.getStatus());
			workBoxDto.setPinned(obj[28] != null ? true : false);
			if (!ServicesUtil.isEmpty(actionDto)) {
				if (actionDto.containsKey(workBoxDto.getProcessName() + ":" + workBoxDto.getName())) {
					if (actionDto.get(workBoxDto.getProcessName() + ":" + workBoxDto.getName())
							.containsKey((String) obj[8])) {
						workBoxDto.setActions(actionDto.get(workBoxDto.getProcessName() + ":" + workBoxDto.getName())
								.get((String) obj[8]));
					} else {
						workBoxDto.setActions(actionDto.get("DEFAULT:DEFAULT").get((String) obj[8]));
					}
				} else {
					workBoxDto.setActions(actionDto.get("DEFAULT:DEFAULT").get((String) obj[8]));
				}
			}
			if (!ServicesUtil.isEmpty(processName)) {
				List<CustomAttributeValue> custAttributes = customAttributeDao
						.getCustomAttributes(workBoxDto.getTaskId());

				if (!ServicesUtil.isEmpty(custAttributes)) {
					objectNode = objectMapper.createObjectNode();
					for (CustomAttributeValue customAttributeValue : custAttributes) {
						objectNode.put(customAttributeValue.getKey(), customAttributeValue.getAttributeValue());
					}
				}
			}
			try {
				workBoxDto.setOrigin(obj[23] == null ? null : (String) obj[23]);
			} catch (Exception e) {
				System.err.println(
						"[WBP-Dev]WorkBoxDao.getWorkBoxDto() error while parsing origin form query results : " + e);
			}
			workBoxDto.setIsCustomLayout(false);
			workBoxDto.setIsCustomLayout(detailPageDao.checkIfLayoutExists(workBoxDto.getProcessName(), workBoxDto.getName()));
			arrayNode.add(objectNode);
			workBoxDtos.add(workBoxDto);
		}
		return workBoxDtos;
	}

	private List<WorkBoxDto> addCustomAttributestoList(List<WorkBoxDto> workBoxDtos) {

		List<String> instanceIdList = new ArrayList<>();
		Long start = System.currentTimeMillis();
		// List<CustomAttributeValue> customAttriubuteValues = null;
		// JsonObject jsonObject = null;

		for (WorkBoxDto taskDto : workBoxDtos) {
			instanceIdList.add(taskDto.getTaskId());
		}

		// customAttriubuteValues =
		// getCustomAttributeValuesForInstaceIdList(instanceIdList);
		System.err.println("[WBP-Dev]WorkBoxDao.addCustomAttributestoList() To fetch custom attributes ---->"
				+ (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();

		System.err.println("[WBP-Dev]WorkBoxDao.addCustomAttributestoList() to add custom attributes to tasks : "
				+ (System.currentTimeMillis() - start));

		return workBoxDtos;

	}

	@SuppressWarnings({ "unchecked", "unused" })
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

	@SuppressWarnings("unused")
	public String prepareAdvanceFilterQuery(Map<String, AdvanceFilterDetailDto> filterMap, String processName,
			String inboxType) {
		String commonConditionQuery = "";
		String value = "";
		String condition = "";
		String level = "";
		String upperLimit = "";
		String lowerLimit = "";

		Set<Entry<String, AdvanceFilterDetailDto>> customSet = new HashSet<>();
		for (Entry<String, AdvanceFilterDetailDto> entry : filterMap.entrySet()) {
			AdvanceFilterDetailDto advanceFilterDetailDto = entry.getValue();
			value = advanceFilterDetailDto.getValue();
			condition = advanceFilterDetailDto.getCondition();
			level = advanceFilterDetailDto.getLevel();
			upperLimit = advanceFilterDetailDto.getUpperLimit();
			lowerLimit = advanceFilterDetailDto.getLowerLimit();
			advanceFilterDetailDto
					.setOperator(advanceFilterDetailDto.getOperator().equalsIgnoreCase(null) ? FilterConstants.OPTR_AND
							: advanceFilterDetailDto.getOperator());
			if (advanceFilterDetailDto.getLevel().equalsIgnoreCase("ca") && !ServicesUtil.isEmpty(processName)) {
				customSet.add(entry);
			} else {

				commonConditionQuery += advanceFilterDetailDto.getOperator() + "  ";
				if (FilterConstants.DATE_TYPE.equalsIgnoreCase(advanceFilterDetailDto.getDataType())) {

					if (FilterConstants.EQUALS.equalsIgnoreCase(condition)) {
						commonConditionQuery += entry.getKey() + "= '" + value + "' ";
					} else {
						commonConditionQuery += " (" + entry.getKey() + " between '" + lowerLimit + "' And '"
								+ upperLimit + "') ";
					}
				} else if (FilterConstants.STRING_TYPE.equalsIgnoreCase(advanceFilterDetailDto.getDataType())) {
					value = value.toUpperCase();
					if (FilterConstants.EQUALS.equalsIgnoreCase(condition)) {

						if (entry.getKey().equalsIgnoreCase("te.FORWARDED_BY")) {
							commonConditionQuery += "  te.event_Id in (select distinct te.event_id from ( select ta.event_id,ta.user_id ,ROW_NUMBER() OVER(PARTITION BY ta.event_id ORDER BY ta.updated_at DESC)"
									+ "AS rk FROM task_audit ta where ta.action_type='Forward' and ta.event_id=te.event_id ) where  rk = 1 and UPPER(user_id)=UPPER('"
									+ value + "') ) ";

						} else if (entry.getKey().equalsIgnoreCase("te.COMP_DEADLINE")) {
							commonConditionQuery += prepareTaskStateQuery(value, inboxType);
						} else {
							commonConditionQuery += "UPPER(" + entry.getKey() + ") = '" + value + "' ";
						}
					}
					if (FilterConstants.IN.equalsIgnoreCase(condition)) {
						if (entry.getKey().equalsIgnoreCase("te.COMP_DEADLINE")) {
							commonConditionQuery += prepareTaskStateQuery(value, inboxType);

						} else
							commonConditionQuery += "UPPER(" + entry.getKey() + ") in (" + value + ") ";
					}
					if (FilterConstants.CONTAINS.equalsIgnoreCase(condition)) {
						commonConditionQuery += "UPPER(" + entry.getKey() + ") like '%" + value + "%' ";
					}
					if (FilterConstants.STARTS_WITH.equalsIgnoreCase(condition)) {
						commonConditionQuery += "UPPER(" + entry.getKey() + ") like '" + value + "%' ";
					}
					if (FilterConstants.ENDS_WITH.equalsIgnoreCase(condition)) {
						commonConditionQuery += "UPPER(" + entry.getKey() + ") like '%" + value + "' ";
					}
				}
				if (FilterConstants.NUMBER_TYPE.equalsIgnoreCase(advanceFilterDetailDto.getDataType())) {
					if (FilterConstants.EQUALS.equalsIgnoreCase(condition)) {
						commonConditionQuery += entry.getKey() + " " + "= ' " + value + "' ";
					}

					if (FilterConstants.IN.equalsIgnoreCase(condition)) {
						commonConditionQuery += entry.getKey() + " in (" + value + ") ";
					}
					if (FilterConstants.GREATER_THAN.equalsIgnoreCase(condition)) {
						commonConditionQuery += entry.getKey() + " " + "> ' " + lowerLimit + "' ";
					}
					if (FilterConstants.LESSER_THAN.equalsIgnoreCase(condition)) {
						commonConditionQuery += entry.getKey() + " " + "< ' " + upperLimit + "' ";
					}
				}
				if (FilterConstants.BOOLEAN_TYPE.equalsIgnoreCase(advanceFilterDetailDto.getDataType())) {
					if (entry.getKey().equalsIgnoreCase("cav.ATTACHMENT")) {

						commonConditionQuery += " te.event_Id in (select cav.task_id from custom_attr_values cav inner join custom_attr_template cat on cat.process_name =cav.process_name "
								+ "					where cat.data_type = 'ATTACHMENT' AND cav.attr_value IS NOT NULL) ";

					} else if (entry.getKey().equalsIgnoreCase("te.FORWARDED_AT")) {

						commonConditionQuery += "  te.event_Id in (select distinct event_id from ( select ta.event_id ,ROW_NUMBER() OVER(PARTITION BY ta.event_id ORDER BY ta.updated_at DESC)"
								+ "AS rk FROM task_audit ta where ta.action_type='Forward' and ta.event_id=te.event_id) where  rk = 1 ) ";

					}
				}
			}
		}

		if (!ServicesUtil.isEmpty(customSet))
			commonConditionQuery += getCustomAttributeAdvancedQuery(processName, customSet);
		return commonConditionQuery;
	}

	@SuppressWarnings("unused")
	private String getCustomAttributeAdvancedQuery(String processName,
			Set<Entry<String, AdvanceFilterDetailDto>> customSet) {
		String customSearchQuery = "";
		String processQuery = "";
		Boolean flag = false;// to check custom filter has values are not
		String value = "";
		String condition = "";
		String level = "";
		String upperLimit = "";
		String lowerLimit = "";

		processQuery = processQuery + "'" + processName + "' ";

		if (!ServicesUtil.isEmpty(customSet)) {
			Set<Entry<String, AdvanceFilterDetailDto>> customSearch = customSet;
			int noOfCustomAttribute = customSet.size();
			customSearchQuery += " AND (cav.task_id IN (SELECT distinct ca1.task_id FROM custom_attr_values ca1 ";
			for (int k = 2; k <= noOfCustomAttribute; k++) {
				customSearchQuery += " join custom_attr_values ca" + k + " on ca" + (k - 1) + ".task_id=ca" + k
						+ ".task_id ";
			}
			customSearchQuery += " WHERE (1=1  ";
			int i = 1;
			for (Entry<String, AdvanceFilterDetailDto> customValue : customSearch) {
				AdvanceFilterDetailDto detailDto = customValue.getValue();
				value = detailDto.getValue();
				condition = detailDto.getCondition();
				level = detailDto.getLevel();
				upperLimit = detailDto.getUpperLimit();
				lowerLimit = detailDto.getLowerLimit();
				if (!ServicesUtil.isEmpty(customValue.getValue())) {

					flag = true;

					customSearchQuery += " AND (ca" + i + ".key='" + customValue.getKey().replaceAll("ca.", "")
							+ "' and ca" + i + ".process_name in (" + processQuery + ") ";
					detailDto.setOperator(detailDto.getOperator().equalsIgnoreCase(null) ? FilterConstants.OPTR_AND
							: detailDto.getOperator());
					customSearchQuery += detailDto.getOperator() + "  ";

					if (FilterConstants.DATE_TYPE.equalsIgnoreCase(detailDto.getDataType())) {

						if (FilterConstants.EQUALS.equalsIgnoreCase(condition)) {

							customSearchQuery += "  ca" + i + ".attr_value " + "= '" + value + "') ";
						} else {
							customSearchQuery += " (ca" + i + ".attr_value between '" + lowerLimit + "' And '"
									+ upperLimit + "')) ";

						}
					}

					if (FilterConstants.STRING_TYPE.equalsIgnoreCase(detailDto.getDataType())) {
						if (FilterConstants.IN.equalsIgnoreCase(condition)) {
							customSearchQuery += "upper(ca" + i + ".attr_value) in (" + value + ")) ";
						}

						if (FilterConstants.EQUALS.equalsIgnoreCase(condition)) {
							customSearchQuery += " upper(ca" + i + ".attr_value) like upper('" + value + "')) ";
						}
						if (FilterConstants.CONTAINS.equalsIgnoreCase(condition)) {
							customSearchQuery += " upper(ca" + i + ".attr_value) like upper('%" + value + "%')) ";
						}
						if (FilterConstants.STARTS_WITH.equalsIgnoreCase(condition)) {
							customSearchQuery += " upper(ca" + i + ".attr_value) like upper('" + value + "%')) ";
						}
						if (FilterConstants.ENDS_WITH.equalsIgnoreCase(condition)) {
							customSearchQuery += " upper(ca" + i + ".attr_value) like upper('%" + value + "')) ";
						}

					}

					if (FilterConstants.NUMBER_TYPE.equalsIgnoreCase(detailDto.getDataType())) {

						if (FilterConstants.EQUALS.equalsIgnoreCase(condition)) {
							customSearchQuery += " upper(ca" + i + ".attr_value) like upper('" + value + "')) ";
						}
						if (FilterConstants.GREATER_THAN.equalsIgnoreCase(condition)) {

							customSearchQuery += " ca" + i + ".attr_value > '" + lowerLimit + "') ";

						}
						if (FilterConstants.LESSER_THAN.equalsIgnoreCase(condition)) {
							customSearchQuery += " ca" + i + ".attr_value < '" + upperLimit + "') ";
						}

					}

					i++;
				}

			}
			customSearchQuery = customSearchQuery.substring(0, customSearchQuery.length() - 3);
			customSearchQuery += "))) ))";
		}

		if (!flag) {
			customSearchQuery = "";
		}
		return customSearchQuery;
	}

	private String getOrderByQuery(List<SortingDto> sortingDtos) {
		String orderByQuery = "";
		String orderType = "";
		String orderBy = "";

		System.err.println("[WBP-Dev]LocalTest.getOrderByQuery()" + sortingDtos);

		for (SortingDto sortingDto : sortingDtos) {
			orderType = sortingDto.getOrderType();
			orderBy = sortingDto.getOrderBy();

			if (!ServicesUtil.isEmpty(orderType) && !ServicesUtil.isEmpty(orderBy)) {

				if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_CREATED_AT)) {
					orderByQuery += orderByQuery + " 8 " + orderType + ",";

				} else if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_SLA_DUE_DATE)) {
					orderByQuery += orderByQuery + " 14 " + orderType + ",";

				} else if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_REQUEST_ID)) {
					orderByQuery += orderByQuery + " 1 " + orderType + ",";

				}

				else if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_COMPLETED_DATE)) {
					orderByQuery += orderByQuery + " 22 " + orderType + ",";

				} else if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_UPDATED_DATE)) {
					orderByQuery += orderByQuery + " 25 " + orderType + ",";

				}
			} else {
				if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_CREATED_AT))
					orderByQuery += orderByQuery + " 8 ASC,";
				else if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_SLA_DUE_DATE))
					orderByQuery += orderByQuery + "  14 ASC,";
				else if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_REQUEST_ID))
					orderByQuery += orderByQuery + "  1 ASC,";
				else if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_COMPLETED_DATE))
					orderByQuery += orderByQuery + "  22 DESC,";
				else if (orderBy.equalsIgnoreCase(PMCConstant.ORDER_TYPE_UPDATED_DATE))
					orderByQuery += orderByQuery + "  25 ASC,";

			}

		}

		if (!ServicesUtil.isEmpty(orderByQuery)) {
			orderByQuery = " ORDER BY " + orderByQuery;
			orderByQuery = orderByQuery.substring(0, orderByQuery.length() - 1);
		}

		System.err.println("[WBP-Dev]LocalTest.getOrderByQuery() orderByQuery" + orderByQuery);

		return orderByQuery;
	}

	public static String prepareQuickFilterQuery(QuickFilterDto quickFilterDto, Token token) {

		String quickFilterQuery = "";

		if (!ServicesUtil.isEmpty(quickFilterDto)) {

			if (!ServicesUtil.isEmpty(quickFilterDto.getProcessName())
					&& !PMCConstant.SEARCH_ALL.contains(quickFilterDto.getProcessName())) {

				quickFilterQuery += " AND te.PROC_NAME in ('" + quickFilterDto.getProcessName() + "') ";
			}
			if (!ServicesUtil.isEmpty(quickFilterDto.getEventId())) {
				quickFilterQuery += " AND TE.EVENT_ID='" + quickFilterDto.getEventId() + "'";
			}
			if (!ServicesUtil.isEmpty(quickFilterDto.getDuration())) {

				String dateFrom = "";
				String dateTo = "";
				if (!ServicesUtil.isEmpty(quickFilterDto.getDuration())) {
					if (quickFilterDto.getDuration().contains("-")) {
						dateFrom = quickFilterDto.getDuration();
						dateTo = ServicesUtil.getNextDay(quickFilterDto.getDuration());
					} else {
						dateFrom = ServicesUtil
								.monthStartDate(ServicesUtil.getMonthInNumber(quickFilterDto.getDuration()));
						dateTo = ServicesUtil.monthEndDate(ServicesUtil.getMonthInNumber(quickFilterDto.getDuration()));
					}
				}
				quickFilterQuery += " AND TE.COMPLETED_AT BETWEEN '" + dateFrom + "' AND '" + dateTo + "' ";

			}

			if (!ServicesUtil.isEmpty(quickFilterDto.getStatus())
					&& !PMCConstant.SEARCH_ALL.equals(quickFilterDto.getStatus())
					&& !PMCConstant.TASK_STATUS_ALL_COMPLETED.equalsIgnoreCase(quickFilterDto.getStatus())) {
				quickFilterQuery += " AND TE.STATUS IN ('" + quickFilterDto.getStatus() + "') ";
			}
			if (!ServicesUtil.isEmpty(quickFilterDto.getStatus())
					&& PMCConstant.TASK_STATUS_ALL_COMPLETED.equalsIgnoreCase(quickFilterDto.getStatus())) {
				quickFilterQuery += " AND TE.STATUS IN ('" + PMCConstant.TASK_COMPLETED + "','"
						+ PMCConstant.TASK_STATUS_RESOLVED + "','APPROVE','REJECT','DONE') ";
			}

			if (!ServicesUtil.isEmpty(quickFilterDto.getUserId())) {
				if (!ServicesUtil.isEmpty(quickFilterDto.getStatus())
						&& PMCConstant.SEARCH_READY.equals(quickFilterDto.getStatus())) {
					quickFilterQuery += " AND TW.TASK_OWNER='" + quickFilterDto.getUserId() + "' ";

				} else if (!ServicesUtil.isEmpty(quickFilterDto.getStatus())
						&& PMCConstant.SEARCH_RESERVED.equals(quickFilterDto.getStatus())) {
					quickFilterQuery += " AND TE.CUR_PROC='" + quickFilterDto.getUserId() + "' ";

				} else
					// quickFilterQuery += " AND (TE.CUR_PROC ='" +
					// quickFilterDto.getUserId() + "' OR TW.TASK_OWNER = '"
					// + quickFilterDto.getUserId() + "') ";
					//

					quickFilterQuery += " AND ((TE.STATUS IN ('READY') AND TW.TASK_OWNER='" + quickFilterDto.getUserId()
							+ "') " + " OR (TE.STATUS IN ('RESERVED') AND TE.CUR_PROC='" + quickFilterDto.getUserId()
							+ "'))";

			}
			if (!ServicesUtil.isEmpty(quickFilterDto.getFreeFilter())) {

				if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.FOR_ME)) {

					quickFilterQuery += "  and te.event_Id not in (select distinct event_id from ( select ta.event_id ,ROW_NUMBER() OVER(PARTITION BY ta.event_id ORDER BY ta.updated_at DESC)"
							+ "AS rk FROM task_audit ta where ta.action_type='Forward' and ta.event_id=te.event_id and UPPER(ta.send_to_user)=UPPER('"
							+ token.getLogonName() + "')) where  rk = 1 ) AND ( TW.TASK_OWNER = '"
							+ token.getLogonName() + "'  AND ( TE.STATUS = 'READY' OR TE.CUR_PROC = '"
							+ token.getLogonName() + "' )) ";

				} else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.ALL_FORWARDED)) {

					quickFilterQuery += "  and te.event_Id in (select distinct event_id from ( select ta.event_id ,ROW_NUMBER() OVER(PARTITION BY ta.event_id ORDER BY ta.updated_at DESC)"
							+ "AS rk FROM task_audit ta where ta.action_type='Forward' and ta.event_id=te.event_id) where  rk = 1 ) ";
				}

				else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.REPORT_FILTER_SLA_BREACHED)) {
					quickFilterQuery += " AND CURRENT_TIMESTAMP > (ADD_SECONDS(TE.COMP_DEADLINE,-"
							+ PMCConstant.CRITICAL_TIME + ")) ";
					if (!ServicesUtil.isEmpty(quickFilterDto.getStatus())) {

						if (!ServicesUtil.isEmpty(quickFilterDto.getStatus())
								&& quickFilterDto.getStatus().equals(PMCConstant.TASK_COMPLETED))
							quickFilterQuery += " AND TE.COMPLETED_AT > TE.COMP_DEADLINE ";
					}
				}

				else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.REPORT_FILTER_IN_TIME)) {
					quickFilterQuery += " AND CURRENT_TIMESTAMP < (ADD_SECONDS(TE.COMP_DEADLINE,-" + 0 + ")) ";
					if (!ServicesUtil.isEmpty(quickFilterDto.getStatus())) {

						if (!ServicesUtil.isEmpty(quickFilterDto.getStatus())
								&& quickFilterDto.getStatus().equals(PMCConstant.TASK_COMPLETED))
							quickFilterQuery += " AND TE.COMPLETED_AT < TE.COMP_DEADLINE ";
					}
				}

				else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.REPORT_FILTER_CRITICAL)) {
					quickFilterQuery += " AND CURRENT_TIMESTAMP > (ADD_SECONDS(TE.COMP_DEADLINE,-"
							+ "PCT.CRITICAL_DATE*60*60)) AND CURRENT_TIMESTAMP < TE.COMP_DEADLINE ";
				}

				else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.COMPLETED_WITH_SLA)) {
					quickFilterQuery += " AND te.comp_deadline>te.completed_at ";

				}

				else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.COMPLETED_WITHOUT_SLA)) {
					quickFilterQuery += " AND te.comp_deadline<te.completed_at ";

				}

				else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.RESERVED_WITH_SLA)) {
					quickFilterQuery += " AND current_timestamp < te.comp_deadline and te.status in('"
							+ PMCConstant.TASK_STATUS_RESERVED + "', '" + PMCConstant.TASK_STATUS_IN_PROGRESS + "')";

				} else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.RESERVED_WITHOUT_SLA)) {
					quickFilterQuery += " AND current_timestamp > te.comp_deadline and te.status in('"
							+ PMCConstant.TASK_STATUS_RESERVED + "', '" + PMCConstant.TASK_STATUS_IN_PROGRESS + "')";
					// + PMCConstant.TASK_STATUS_RESOLVED + "') ";

				} else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.READY_WITH_SLA)) {
					quickFilterQuery += " AND te.comp_deadline > current_timestamp ";

				} else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.READY_WITHOUT_SLA)) {
					quickFilterQuery += " AND te.comp_deadline < current_timestamp ";

				}

				else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.REPORT_ON_TIME)) {
					quickFilterQuery += "  AND CURRENT_TIMESTAMP < ADD_SECONDS(comp_deadline, - ("
							+ "PCT.CRITICAL_DATE*60*60)) AND te.STATUS <> 'COMPLETED'  ";
					// + "and pe.request_id is not null ";

				}

				else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.REPORT_SLA_BREACHED)) {
					// Removed request id not null check - Sourav
					quickFilterQuery += " AND CURRENT_TIMESTAMP > comp_deadline AND te.STATUS not in ('COMPLETED','CANCELED')   ";

				}

				else if (quickFilterDto.getFreeFilter().equalsIgnoreCase(PMCConstant.REPORT_CRITICAL)) {
					quickFilterQuery += " and current_timestamp between comp_deadline and ADD_SECONDS(comp_deadline, - ("
							+ "PCT.CRITICAL_DATE*60*60)) 	AND te.STATUS <> 'COMPLETED' ";
					// + " and pe.request_id is not null";
				}

			}
		}

		return quickFilterQuery;

	}

	public List<Object[]> getInboxFilterDataForFileExport(InboxFilterDto inboxFilterDto, Boolean isChatBot,
			String processName, String selectQueryForFileExport, Token token) {

		WorkboxResponseDto workboxResponseDto = new WorkboxResponseDto();
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(PMCConstant.STATUS_FAILURE);
		responseMessage.setStatusCode("1");
		responseMessage.setMessage(PMCConstant.STATUS_FAILURE);

		workboxResponseDto.setResponseMessage(responseMessage);

		// for groups inboxType is "groupId" and inboxName is "Groups" so need
		// to swap
		if (!ServicesUtil.isEmpty(inboxFilterDto.getInboxName()) && !ServicesUtil.isEmpty(inboxFilterDto.getInboxType())
				&& !"AllTask".equals(inboxFilterDto.getInboxName())) {
			String temp = inboxFilterDto.getInboxName();
			inboxFilterDto.setInboxName(inboxFilterDto.getInboxType());
			inboxFilterDto.setInboxType(temp);
		}

		try {
//			User user = UserManagementUtil.getLoggedInUser();
			System.err.println("[WBP-Dev]user : " + token);
			String dataQuery = "";
			String countQuery = "";
			String orderByQuery = "";

			// Preparing COMMON condition query-----------

			String selectQuery = getSelectQuery();

			String pinnedTaskQuery = getPinnedTaskQuery(token);
			String commonJoinQuery = "";
			if (!ServicesUtil.isEmpty(selectQueryForFileExport))
				commonJoinQuery = getCommonJoinQueryForFileExport();
			else
				commonJoinQuery = getCommonJoinQuery();

			String commonConditionQuery = getCommonConditionQuery(inboxFilterDto.getInboxType(),
					inboxFilterDto.getQuickFilter());

			// Preparing ADVANCED condition query-------------

			String advancedFilterQuery = getAdvanceFilterQuery(inboxFilterDto);

			// Preparing QuickFilter query----------------------

			String quickFilterQuery = prepareQuickFilterQuery(inboxFilterDto.getQuickFilter(), token);

			// Preparing INBOXTYPE condition query---------------

			String inboxTypeQuery = "";
			System.err.println("[WBP-Dev][InboxFilter]" + inboxFilterDto.getInboxType());
			switch (inboxFilterDto.getInboxType()) {
			case PMCConstant.PANEL_TEMPLATE_ID_MYINBOX:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getMyTaskQuery(token.getLogonName());
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_ADMININBOX:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getAdminInboxTaskQuery(inboxFilterDto);
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_GROUPS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getTaskByGroupQuery(inboxFilterDto.getInboxName());
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_SUBSTITUTIONINBOX:
				System.err.println("[WBP-Dev][Substitution check] called");
				inboxTypeQuery = inboxTypeQuery + inboxTypeDao.getSubstitutedTaskQuery(token.getLogonName());
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_DRAFT:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getDraftTaskQuery();
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_VIEWS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getViewTaskQuery();
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_CREATEDTASKS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getCreatedTaskQuery();
				break;
			case PMCConstant.PANEL_TEMPLATE_ID_COMPLETEDTASKS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getCompletedTaskQuery();
				break;
			case PMCConstant.PINNED_TASKS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getPinnedTaskQuery();
				break;
			case PMCConstant.USER_TASKS:
				inboxTypeQuery = inboxTypeQuery + InboxTypeDao.getUserTaskQuery(inboxFilterDto.getUserId());
				break;
			default:
				// code block
			}

			// Prepare order by query
			if (!ServicesUtil.isEmpty(inboxFilterDto.getSortingDtos())) {
				orderByQuery = getOrderByQuery(inboxFilterDto.getSortingDtos());
			} else {
				orderByQuery = " ORDER BY 8 DESC ";
			}

			// Preparing COUNT query

			dataQuery = selectQuery + pinnedTaskQuery + selectQueryForFileExport + commonJoinQuery
					+ commonConditionQuery + advancedFilterQuery + quickFilterQuery + inboxTypeQuery + orderByQuery;

			Query q = this.getSession().createSQLQuery(dataQuery.trim());

			System.err.println("[WBP-Dev][PMC][Demo][WorkBoxFacade][getWorkboxFilterData][DataQuery][new]" + dataQuery);
			setQueryParameter(inboxFilterDto, token, q);

			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			return resultList;
		} catch (Exception e) {
			System.err.println("InboxFilterDao.getSelectQueryForFileExport() exception:" + e.getMessage());
			return null;

		}

	}

	public String prepareTaskStateQuery(String taskState, String inboxType) {

		String query = "";

		if (taskState.contains(FilterConstants.IN_TIME.toUpperCase())) {

			if (PMCConstant.PANEL_TEMPLATE_ID_COMPLETEDTASKS.equalsIgnoreCase(inboxType)) {
				query += " ( TE.COMPLETED_AT < TE.COMP_DEADLINE )";
			} else {
				query += "  ( CURRENT_TIMESTAMP < ADD_SECONDS( te.comp_deadline, - ("
						+ "PCT.CRITICAL_DATE*60*60)) AND te.STATUS <> 'COMPLETED')  ";
			}

		}
		if (taskState.contains(FilterConstants.CRITICAL.toUpperCase())) {

			if (!query.equals(""))
				query += " OR ";

			query += "  (CURRENT_TIMESTAMP > (ADD_SECONDS(TE.COMP_DEADLINE,-"
					+ "PCT.CRITICAL_DATE*60*60)) AND CURRENT_TIMESTAMP < TE.COMP_DEADLINE) ";

		}
		if (taskState.contains(FilterConstants.SLA_BREACHED.toUpperCase())) {

			if (!query.equals(""))
				query += " OR ";

			if (PMCConstant.PANEL_TEMPLATE_ID_COMPLETEDTASKS.equalsIgnoreCase(inboxType))
				query += " (te.comp_deadline < te.completed_at)";
			else
				query += " (current_timestamp > te.comp_deadline) ";
		}

		if (!query.equals("")) {
			query = "(" + query + ")";
		}
		return query;
	}

//	public static void main(String[] args) {
//		String s = "p000100";
//		System.out.println(s.toUpperCase());
//	}

}