package oneapp.incture.workbox.demo.dashboard.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dashboard.dto.DashboardDto;
import oneapp.incture.workbox.demo.dashboard.dto.TaskNameCountDto;
import oneapp.incture.workbox.demo.dashboard.dto.TileDetailsDto;
import oneapp.incture.workbox.demo.dashboard.dto.TotalActiveTaskDto;
import oneapp.incture.workbox.demo.dashboard.dto.UserWorkCountDto;
import oneapp.incture.workbox.demo.inbox.dao.InboxFilterDao;
import oneapp.incture.workbox.demo.inbox.dao.UserCustomFilterDao;
import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.QuickFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.UserCustomFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxRequestFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxResponseDto;

/**
 * @author Neelam Raj
 *
 */
@Repository
//////@Transactional
public class DashboardDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	UserCustomFilterDao customFilterDao;

	@Autowired
	InboxFilterDao filterDao;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public DashboardDto getDetails(String processName, String graphType, String duration, String userId,
			Integer taskPage, Integer userPage
			,Token token) {
		DashboardDto dto = new DashboardDto();
		List<TileDetailsDto> tiles = new ArrayList<>();
		// TileDetailsDto tile=new TileDetailsDto();

		try {
			// this
			BigInteger completedWithSLA = getCount(PMCConstant.COMPLETED_WITH_SLA,token);
			BigInteger completedWithoutSLA = getCount(PMCConstant.COMPLETED_WITHOUT_SLA,token);
			BigInteger reservedWithSLA = getCount(PMCConstant.RESERVED_WITH_SLA,token);
			BigInteger reservedWithoutSLA = getCount(PMCConstant.RESERVED_WITHOUT_SLA,token);
			BigInteger readyWithSLA = getCount(PMCConstant.READY_WITH_SLA,token);
			BigInteger readyWithoutSLA = getCount(PMCConstant.READY_WITHOUT_SLA,token);
			BigInteger criticalTask = getCount(PMCConstant.REPORT_FILTER_CRITICAL,token);

			if (graphType.contains("taskCount") || ServicesUtil.isEmpty(graphType)) {

				// Fetch all filters where is_tile=true & isActive=true
				List<UserCustomFilterDto> customTileList = customFilterDao.getFilters(userId, false, true, true);
				if (!ServicesUtil.isEmpty(customTileList)) {
					for (UserCustomFilterDto userCustomFilterDto : customTileList) {
						Gson gson = new Gson();

						WorkboxRequestFilterDto requestFilterDto = gson.fromJson(userCustomFilterDto.getFilterData(),
								WorkboxRequestFilterDto.class);
						InboxFilterDto filterDto = gson.fromJson(userCustomFilterDto.getFilterData(),
								InboxFilterDto.class);
						filterDto.setAdvanceFilter(requestFilterDto);
						filterDto.setReturnCountOnly(true);
						WorkboxResponseDto responseDto = filterDao.getInboxFilterData(filterDto,token);
						filterDto.setReturnCountOnly(false);

						// TileDetailsDto(String tileId, String key, String
						// value, String label,
						// F String status, String type, String threshold,
						// String description, String tilePayload)
						tiles.add(new TileDetailsDto(userCustomFilterDto.getFilterId() + "",
								userCustomFilterDto.getFilterName(), "" + responseDto.getCount(),
								userCustomFilterDto.getFilterName(), userCustomFilterDto.getIsActive() + "",
								userCustomFilterDto.getFilterType() + "", userCustomFilterDto.getThreshold(),
								userCustomFilterDto.getDescription(), userCustomFilterDto.getFilterData(),
								userCustomFilterDto.getUserId()));
					}
				}
				// Add custom Tiles
				dto.setTiles(tiles);
			}

			if (graphType.contains("activeTasksGraph") || ServicesUtil.isEmpty(graphType)) {
				dto.setTotalActiveTaskList(getTotalActiveTaskList(taskPage,token));
			}
			if (graphType.contains("userWorkCountGraph") || ServicesUtil.isEmpty(graphType)) {
				dto.setUserWorkCountList(getUserWorkCountList(processName, userPage,token));
			}

			if (graphType.contains("taskCompletionTrendGraph") || ServicesUtil.isEmpty(graphType)) {
				dto.setTaskCompletionTrendList(getTaskCompletionRange(duration));
			}

			if (graphType.contains("taskSummary") || ServicesUtil.isEmpty(graphType)) {
				List<TaskNameCountDto> list = new ArrayList<TaskNameCountDto>();

				list.add(taskNameCountDto(PMCConstant.REPORT_ON_TIME, completedWithSLA, PMCConstant.REPORT_COMPLETED));
				list.add(taskNameCountDto(PMCConstant.REPORT_SLA_BREACHED, completedWithoutSLA,
						PMCConstant.REPORT_COMPLETED));
				list.add(taskNameCountDto(PMCConstant.REPORT_ON_TIME, reservedWithSLA, PMCConstant.REPORT_RESERVED));
				list.add(taskNameCountDto(PMCConstant.REPORT_SLA_BREACHED, reservedWithoutSLA,
						PMCConstant.REPORT_RESERVED));
				list.add(taskNameCountDto(PMCConstant.REPORT_ON_TIME, readyWithSLA, PMCConstant.REPORT_READY));
				list.add(taskNameCountDto(PMCConstant.REPORT_SLA_BREACHED, readyWithoutSLA, PMCConstant.REPORT_READY));

				dto.setTaskDonutList(list);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][GRAPH DAO]" + e.getMessage() + e.getLocalizedMessage());
		}
		return dto;
	}

	@SuppressWarnings("unchecked")
	public List<TaskNameCountDto> getQuickLinkList(String userId,Token token) {
		List<TaskNameCountDto> result = new ArrayList<>();
		TaskNameCountDto dto = null;
		try {
			String queryStringLinks = " Select distinct quick_Link from user_quick_link where user_id ='" + userId
					+ "' ";
			String queryString = "";
			String commonQuery = " select count(*) from ( SELECT DISTINCT pe.REQUEST_ID AS REQUEST_ID,te1.event_id from "
					+ "	TASK_EVENTS te1 LEFT OUTER JOIN PROCESS_CONFIG_TB AS Pct "
					+ "	ON TE1.PROC_NAME = PCT.PROCESS_NAME  	LEFT OUTER JOIN TASK_SLA AS ts "
					+ "	ON te1.NAME = ts.TASK_DEF  	LEFT OUTER JOIN PROCESS_EVENTS AS pe "
					+ "	ON pe.process_id = te1.process_id  	INNER JOIN TASK_OWNERS AS tw "
					+ "	ON tw.event_id = te1.event_id WHERE (PE.STATUS <> 'CANCELED' AND PE.STATUS <> 'COMPLETED') "
					+ " AND TE1.COMP_DEADLINE IS NOT NULL AND (tw.TASK_OWNER = '" + userId
					+ "' and (te1.status='READY' OR te1.CUR_PROC = '" + userId + "')) ";
			Object value = null;
			List<String> resultList = this.getSession().createSQLQuery(queryStringLinks).list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (String str : resultList) {
					dto = new TaskNameCountDto();
					queryString = commonQuery;
					switch (str) {

					case PMCConstant.REPORT_ON_TIME:

						queryString += InboxFilterDao
						.prepareQuickFilterQuery(new QuickFilterDto(null, null, null, null, str),token);

						dto.setStrName(PMCConstant.REPORT_ON_TIME);

						break;
					case PMCConstant.REPORT_SLA_BREACHED:

						queryString += InboxFilterDao
						.prepareQuickFilterQuery(new QuickFilterDto(null, null, null, null, str),token);
						dto.setStrName(PMCConstant.REPORT_SLA_BREACHED);

						break;
					case PMCConstant.REPORT_CRITICAL:
						queryString += InboxFilterDao
						.prepareQuickFilterQuery(new QuickFilterDto(null, null, null, null, str),token);

						dto.setStrName(PMCConstant.REPORT_CRITICAL);
					}
					System.err.println("[WBP-Dev]queryString [quickLink]" + queryString);
					Query q = this.getSession().createSQLQuery(queryString);
					value = q.uniqueResult();
					dto.setTaskCount((BigInteger) value);
					result.add(dto);
				}
			} else {
				dto = new TaskNameCountDto();
				queryString = commonQuery
						+ " and current_timestamp between  comp_deadline and ADD_SECONDS(comp_deadline, - ("
						+ PMCConstant.CRITICAL_TIME + ")) 	AND te1.STATUS <> 'COMPLETED' "
						+ " and pe.request_id is not null)";

				dto.setStrName(PMCConstant.REPORT_CRITICAL);
				Query q = this.getSession().createSQLQuery(queryString);
				value = q.uniqueResult();
				dto.setTaskCount((BigInteger) value);
				result.add(dto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]ERROR :" + e.getLocalizedMessage());
		}
		return result;
	}

	private List<TaskNameCountDto> getTaskDto(List<String> listOfDates, Object[] obj, int value) {

		List<TaskNameCountDto> list = new ArrayList<TaskNameCountDto>();
		TaskNameCountDto dto = null;
		for (int i = 0; i < value; i++) {
			dto = new TaskNameCountDto();
			dto.setStrName(listOfDates.get(i));
			dto.setTaskCount((BigInteger) obj[i]);
			list.add(dto);
		}
		return list;
	}

	private BigInteger getCount(String taskName,Token token) {
		Object resultList = null;

		try {
			String queryString = "select count(*) from (select distinct pe.request_id,te.event_id ";

			/*
			 * + "FROM TASK_EVENTS AS te INNER JOIN PROCESS_CONFIG_TB AS Pct " +
			 * "	ON TE.PROC_NAME = PCT.PROCESS_NAME 	INNER JOIN PROCESS_EVENTS AS pe "
			 * +
			 * "	ON pe.process_id = te.process_id join TASK_OWNERS AS tw on tw.event_id=te.event_id "
			 * +
			 * " WHERE (PE.STATUS <> 'CANCELED') AND TE.COMP_DEADLINE IS NOT NULL "
			 * ;
			 */

			queryString += InboxFilterDao.getCommonJoinQuery();
			if (taskName.equalsIgnoreCase(PMCConstant.COMPLETED_WITH_SLA)
					|| taskName.equalsIgnoreCase(PMCConstant.COMPLETED_WITHOUT_SLA))
				queryString += InboxFilterDao.getCommonConditionQuery(null,
						new QuickFilterDto(null, PMCConstant.TASK_COMPLETED, null, null, null));
			else
				queryString += InboxFilterDao.getCommonConditionQuery(null, null);

			if (PMCConstant.COMPLETED_WITH_SLA.equals(taskName))
				queryString += InboxFilterDao.prepareQuickFilterQuery(
						new QuickFilterDto(null, PMCConstant.TASK_STATUS_ALL_COMPLETED, null, null, taskName),token);
			else if (PMCConstant.COMPLETED_WITHOUT_SLA.equals(taskName))
				queryString += InboxFilterDao.prepareQuickFilterQuery(
						new QuickFilterDto(null, PMCConstant.TASK_STATUS_ALL_COMPLETED, null, null, taskName),token);
			else if (PMCConstant.RESERVED_WITH_SLA.equals(taskName))
				queryString += InboxFilterDao
				.prepareQuickFilterQuery(new QuickFilterDto(null, "RESERVED", null, null, taskName),token);
			else if (PMCConstant.RESERVED_WITHOUT_SLA.equals(taskName))
				queryString += InboxFilterDao
				.prepareQuickFilterQuery(new QuickFilterDto(null, "RESERVED", null, null, taskName),token);
			else if (PMCConstant.READY_WITH_SLA.equals(taskName))
				queryString += InboxFilterDao
				.prepareQuickFilterQuery(new QuickFilterDto(null, "READY", null, null, taskName),token);
			else if (PMCConstant.READY_WITHOUT_SLA.equals(taskName))
				queryString += InboxFilterDao
				.prepareQuickFilterQuery(new QuickFilterDto(null, "READY", null, null, taskName),token);
			else if (PMCConstant.REPORT_FILTER_CRITICAL.equalsIgnoreCase(taskName))
				queryString += InboxFilterDao
				.prepareQuickFilterQuery(new QuickFilterDto(null, null, null, null, taskName),token);
			else {

				queryString += InboxFilterDao
						.prepareQuickFilterQuery(new QuickFilterDto(null, null, null, taskName, null),token);

			}
			queryString += " )";

			System.err.println("[WBP-Dev]GraphDao.getCount() graph taskName : " + taskName + " query : " + queryString);
			Query q = this.getSession().createSQLQuery(queryString);
			resultList = q.uniqueResult();
		} catch (Exception ex) {
			System.err.println("[WBP-Dev]Exception while graphDao getCount : " + ex.getMessage());
		}
		return (BigInteger) resultList;
	}

	private TaskNameCountDto taskNameCountDto(String taskName, BigInteger count, String status) {
		TaskNameCountDto dto = new TaskNameCountDto();
		dto.setStrName(taskName);
		dto.setTaskCount(count);
		dto.setStatus(status);
		return dto;
	}

	@SuppressWarnings("unused")
	private List<TotalActiveTaskDto> getTotalActiveTaskListOLD(Integer taskPage) {
		List<TotalActiveTaskDto> list = new ArrayList<TotalActiveTaskDto>();
		TotalActiveTaskDto dto = null;

		/*
		 * Added process status filter and removed request id null checks from
		 * the query Modified By : Sourav Das
		 */
		String queryString = "SELECT TE.PROC_NAME, PC.PROCESS_DISPLAY_NAME, ( "
				+ "	select count(*) from ( SELECT DISTINCT pe.REQUEST_ID AS REQUEST_ID,te1.event_id from "
				+ "	TASK_EVENTS te1 LEFT OUTER JOIN PROCESS_CONFIG_TB AS Pct "
				+ "	ON TE1.PROC_NAME = PCT.PROCESS_NAME  	LEFT OUTER JOIN TASK_SLA AS ts "
				+ "	ON te1.NAME = ts.TASK_DEF  	LEFT OUTER JOIN PROCESS_EVENTS AS pe "
				+ "	ON pe.process_id = te1.process_id  	INNER JOIN TASK_OWNERS AS tw "
				+ "	ON tw.event_id = te1.event_id WHERE TE1.PROC_NAME = TE.PROC_NAME "
				+ "	AND CURRENT_TIMESTAMP < ADD_SECONDS(comp_deadline, - (" + PMCConstant.CRITICAL_TIME + ")) "
				+ "         AND pe.status not in ('CANCELED','COMPLETED') "
				+ "			AND te1.STATUS <> 'COMPLETED' AND te1.STATUS<>'CANCELED' ) )AS IN_TIME,  	( "
				+ "		select count(*) from (  SELECT DISTINCT pe.REQUEST_ID AS REQUEST_ID,te1.event_id from "
				+ "		TASK_EVENTS te1  		LEFT OUTER JOIN PROCESS_CONFIG_TB AS Pct "
				+ "	ON TE1.PROC_NAME = PCT.PROCESS_NAME  	LEFT OUTER JOIN TASK_SLA AS ts "
				+ "	ON te1.NAME = ts.TASK_DEF  	LEFT OUTER JOIN PROCESS_EVENTS AS pe "
				+ "	ON pe.process_id = te1.process_id  	INNER JOIN TASK_OWNERS AS tw "
				+ "	ON tw.event_id = te1.event_id WHERE TE1.PROC_NAME = TE.PROC_NAME and pe.status not in ('CANCELED','COMPLETED') "
				+ "			and		current_timestamp between  comp_deadline and ADD_SECONDS(comp_deadline, - ("
				+ PMCConstant.CRITICAL_TIME + ")) 	AND te1.STATUS <> 'COMPLETED' AND te1.STATUS<>'CANCELED' "
				+ " )) AS CRITICAL,  	( "
				+ "	select count(*) from ( SELECT DISTINCT pe.REQUEST_ID AS REQUEST_ID,te1.event_id from "
				+ "		TASK_EVENTS te1 LEFT OUTER JOIN PROCESS_CONFIG_TB AS Pct "
				+ "	ON TE1.PROC_NAME = PCT.PROCESS_NAME 	LEFT OUTER JOIN TASK_SLA AS ts "
				+ "	ON te1.NAME = ts.TASK_DEF 	LEFT OUTER JOIN PROCESS_EVENTS AS pe "
				+ "	ON pe.process_id = te1.process_id 	INNER JOIN TASK_OWNERS AS tw "
				+ "	ON tw.event_id = te1.event_id WHERE TE1.PROC_NAME = TE.PROC_NAME and pe.status not in ('CANCELED','COMPLETED')"
				+ "			AND CURRENT_TIMESTAMP > comp_deadline			AND te1.STATUS <> 'COMPLETED' AND te1.STATUS<>'CANCELED' "
				+ "	)) AS SLA_BREACHED FROM TASK_EVENTS AS TE 	INNER JOIN PROCESS_CONFIG_TB AS PC "
				+ "	ON PC.PROCESS_NAME = TE.PROC_NAME GROUP BY TE.PROC_NAME, "
				+ "	PC.PROCESS_DISPLAY_NAME ORDER BY IN_TIME+CRITICAL+SLA_BREACHED DESC";

		if (taskPage != null && taskPage > 0)
			queryString = queryString + " LIMIT 5 OFFSET " + 5 * (taskPage - 1);

		System.err.println("[WBP-Dev][getTotalActiveTaskList] : " + queryString);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = this.getSession().createSQLQuery(queryString).list();
		for (Object[] obj : resultList) {
			dto = new TotalActiveTaskDto();
			dto.setProcessName((String) obj[0]);
			dto.setProcessDisplayName((String) obj[1]);
			dto.setInTime((BigInteger) obj[2]);
			dto.setCritical((BigInteger) obj[3]);
			dto.setSlaBreached((BigInteger) obj[4]);
			list.add(dto);
		}
		return list;
	}

	private List<TotalActiveTaskDto> getTotalActiveTaskList(Integer taskPage,Token token) {
		List<TotalActiveTaskDto> list = new ArrayList<TotalActiveTaskDto>();
		TotalActiveTaskDto dto = null;

		String commonQuery = "SELECT count(*)	FROM (SELECT DISTINCT pe.REQUEST_ID AS REQUEST_ID,TE.EVENT_ID" + " "
				+ InboxFilterDao.getCommonJoinQuery() + InboxFilterDao.getCommonConditionQuery(null, null)
				+ " AND TE.PROC_NAME=TE1.PROC_NAME ";

		String inTimeQuery = commonQuery + InboxFilterDao.prepareQuickFilterQuery(
				new QuickFilterDto(null, null, null, null, PMCConstant.REPORT_ON_TIME),token) + " ) ";
		String criticalQuery = commonQuery + InboxFilterDao.prepareQuickFilterQuery(
				new QuickFilterDto(null, null, null, null, PMCConstant.REPORT_CRITICAL),token) + " ) ";
		String slaBreachedQuery = commonQuery + InboxFilterDao.prepareQuickFilterQuery(
				new QuickFilterDto(null, null, null, null, PMCConstant.REPORT_SLA_BREACHED),token) + " ) ";

		String queryString = "SELECT TE1.PROC_NAME, PC1.PROCESS_DISPLAY_NAME, (" + inTimeQuery + ") AS IN_TIME, ("
				+ criticalQuery + ") AS CRITICAL," + "(" + slaBreachedQuery
				+ ") AS SLA_BREACHED FROM TASK_EVENTS AS TE1 INNER JOIN PROCESS_CONFIG_TB AS PC1"
				+ "	ON PC1.PROCESS_NAME = TE1.PROC_NAME GROUP BY TE1.PROC_NAME,PC1.PROCESS_DISPLAY_NAME "
				+ "ORDER BY (IN_TIME+CRITICAL+SLA_BREACHED) DESC;";

		if (taskPage != null && taskPage > 0)
			queryString = queryString + " LIMIT 5 OFFSET " + 5 * (taskPage - 1);

		System.err.println("[WBP-Dev][getTotalActiveTaskList] : " + queryString);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = this.getSession().createSQLQuery(queryString).list();

		BigInteger inTime = BigInteger.ZERO;
		BigInteger slaBreached = BigInteger.ZERO;
		BigInteger critical = BigInteger.ZERO;
		for (Object[] obj : resultList) {
			if("ic_manager_approval_process".equalsIgnoreCase((String) obj[0]) || 
					"analyst_appproval_process".equalsIgnoreCase((String) obj[0]) ||
					"inventoryparentworkflow".equalsIgnoreCase((String) obj[0])){
				inTime = inTime.add((BigInteger) obj[2]);
				slaBreached = slaBreached.add((BigInteger) obj[4]);
				critical = critical.add((BigInteger) obj[3]);
			}else{
				dto = new TotalActiveTaskDto();
				dto.setProcessName((String) obj[0]);
				dto.setProcessDisplayName((String) obj[1]);
				dto.setInTime((BigInteger) obj[2]);
				dto.setCritical((BigInteger) obj[3]);
				dto.setSlaBreached((BigInteger) obj[4]);
				list.add(dto);
			}
		}
		dto = new TotalActiveTaskDto();
		dto.setProcessName("ic_manager_approval_process','analyst_appproval_process','inventoryparentworkflow");
		dto.setProcessDisplayName("Case Management Process");
		dto.setInTime(inTime);
		dto.setCritical(critical);
		dto.setSlaBreached(slaBreached);
		list.add(dto);
		return list;
	}

	private List<UserWorkCountDto> getUserWorkCountList(String processName, Integer userPage,Token token) {
		List<UserWorkCountDto> list = new ArrayList<>();
		UserWorkCountDto dto = null;

		if (PMCConstant.SEARCH_ALL.equals(processName) || ServicesUtil.isEmpty(processName))
			processName = "";
		else
			processName = " AND te.PROC_NAME in( '" + processName + "') ";

		// String commonQuery = "and TE.status in ('RESERVED') " + processName +
		// " AND te.cur_proc = TW1.task_owner ";
		String commonQuery = " " + processName + " AND tw.task_owner = TW1.task_owner ";
		// + "AND (te.status = 'READY' or te.cur_proc = tw1.task_owner)";

		String inTimeQuery = "SELECT count(*) FROM ( SELECT DISTINCT request_id, te.event_id "
				+ InboxFilterDao.getCommonJoinQuery() + " "
				+ InboxFilterDao.getCommonConditionQuery(null, null) + "  " + commonQuery + " " + InboxFilterDao
				.prepareQuickFilterQuery(new QuickFilterDto(null, null, null, null, PMCConstant.REPORT_ON_TIME),token)
				+ " ) ";

		String criticalQuery = "SELECT count(*) FROM ( SELECT DISTINCT request_id, te.event_id "
				+ InboxFilterDao.getCommonJoinQuery() + " " + InboxFilterDao.getCommonConditionQuery(null, null) + " "
				+ commonQuery + " " + InboxFilterDao.prepareQuickFilterQuery(
						new QuickFilterDto(null, null, null, null, PMCConstant.REPORT_FILTER_CRITICAL),token)
				+ " ) ";

		String slaBreachedQuery = "SELECT count(*) FROM ( SELECT DISTINCT request_id, te.event_id "
				+ InboxFilterDao.getCommonJoinQuery() + " " + InboxFilterDao.getCommonConditionQuery(null, null) + " "
				+ commonQuery + " " + InboxFilterDao.prepareQuickFilterQuery(
						new QuickFilterDto(null, null, null, null, PMCConstant.REPORT_SLA_BREACHED),token)
				+ " ) ";

		String mainQuery = "SELECT DISTINCT TW1.task_owner, " + "MAX(TW1.task_owner_disp), ( " + inTimeQuery
				+ ") in_time, " + "(" + criticalQuery + ") AS CRITICAL, (" + slaBreachedQuery + ") AS BREACHED "
				+ "FROM TASK_EVENTS AS te1 INNER JOIN PROCESS_CONFIG_TB AS PC1 ON "
				+ "TE1.PROC_NAME = PC1.PROCESS_NAME LEFT OUTER JOIN TASK_SLA AS ts ON te1.NAME = ts.TASK_DEF "
				+ "INNER JOIN PROCESS_EVENTS AS pe ON pe.process_id = te1.process_id INNER JOIN TASK_OWNERS AS TW1 ON "
				+ "TW1.event_id = te1.event_id WHERE TW1.task_owner_disp IS NOT NULL "
				+ "GROUP BY TW1.TASK_OWNER ORDER BY IN_TIME + CRITICAL + BREACHED DESC";

		/*
		 * String queryString =
		 * " SELECT DISTINCT TW1.task_owner, TW1.task_owner_disp,	( " +
		 * "		SELECT count(*) FROM ( SELECT DISTINCT request_id, " +
		 * "					te.event_id FROM TASK_EVENTS AS te " +
		 * "					INNER JOIN PROCESS_CONFIG_TB AS Pct " +
		 * "					ON TE.PROC_NAME = PCT.PROCESS_NAME " +
		 * "					LEFT OUTER JOIN TASK_SLA AS ts  ON te.NAME = ts.TASK_DEF "
		 * + "					INNER JOIN PROCESS_EVENTS AS pe " +
		 * "					ON pe.process_id = te.process_id " +
		 * "					INNER JOIN TASK_OWNERS AS tw  ON tw.event_id = te.event_id "
		 * + "				WHERE TE.status in('" +
		 * PMCConstant.TASK_STATUS_RESERVED + "') " + processName +
		 * "					AND tw.task_owner = TW1.task_owner " +
		 * "					AND (te.status = '" +
		 * PMCConstant.TASK_STATUS_READY + "' or te.cur_proc = tw1.task_owner) "
		 * + "					AND CURRENT_TIMESTAMP < Add_seconds( " +
		 * "							TE.comp_deadline,- (" +
		 * PMCConstant.CRITICAL_TIME + ") ))  	) AS in_time, " +
		 * "	(  		SELECT count(*) FROM ( " +
		 * "						SELECT DISTINCT request_id, te.event_id " +
		 * "				FROM TASK_EVENTS AS te " +
		 * "					INNER JOIN PROCESS_CONFIG_TB AS Pct " +
		 * "					ON TE.PROC_NAME = PCT.PROCESS_NAME " +
		 * "					LEFT OUTER JOIN TASK_SLA AS ts  ON te.NAME = ts.TASK_DEF "
		 * + "					INNER JOIN PROCESS_EVENTS AS pe " +
		 * "					ON pe.process_id = te.process_id " +
		 * "					INNER JOIN TASK_OWNERS AS tw ON tw.event_id = te.event_id "
		 * + "				WHERE TE.status in('" +
		 * PMCConstant.TASK_STATUS_RESERVED + "') " + processName +
		 * "						AND tw.task_owner = TW1.task_owner " +
		 * "					AND (te.status = '" +
		 * PMCConstant.TASK_STATUS_READY + "' or te.cur_proc = tw1.task_owner) "
		 * +
		 * "					AND CURRENT_TIMESTAMP > Add_seconds(	comp_deadline,	- ("
		 * + PMCConstant.CRITICAL_TIME + ") " +
		 * "						) AND CURRENT_TIMESTAMP < comp_deadline  			) "
		 * + "	) AS CRITICAL,( SELECT count(*) FROM ( " +
		 * "						SELECT DISTINCT request_id,te.event_id " +
		 * "				FROM TASK_EVENTS AS te " +
		 * "					INNER JOIN PROCESS_CONFIG_TB AS Pct " +
		 * "					ON TE.PROC_NAME = PCT.PROCESS_NAME " +
		 * "					LEFT OUTER JOIN TASK_SLA AS ts ON te.NAME = ts.TASK_DEF "
		 * + "					INNER JOIN PROCESS_EVENTS AS pe " +
		 * "					ON pe.process_id = te.process_id " +
		 * "					INNER JOIN TASK_OWNERS AS tw  ON tw.event_id = te.event_id "
		 * + "				WHERE TE.status in('" +
		 * PMCConstant.TASK_STATUS_RESERVED + "') " + processName +
		 * "					AND tw.task_owner = TW1.task_owner " +
		 * "					AND (te.status = '" +
		 * PMCConstant.TASK_STATUS_READY + "' or te.cur_proc = TW1.task_owner) "
		 * +
		 * "					AND CURRENT_TIMESTAMP > comp_deadline)) AS BREACHED "
		 * + "FROM TASK_EVENTS AS te1 	INNER JOIN PROCESS_CONFIG_TB AS PC1 " +
		 * "	ON TE1.PROC_NAME = PC1.PROCESS_NAME  	LEFT OUTER JOIN TASK_SLA AS ts "
		 * + "	ON te1.NAME = ts.TASK_DEF 	INNER JOIN PROCESS_EVENTS AS pe " +
		 * "	ON pe.process_id = te1.process_id  	INNER JOIN TASK_OWNERS AS TW1 "
		 * +
		 * "	ON TW1.event_id = te1.event_id  WHERE TW1.task_owner_disp IS NOT NULL ORDER BY IN_TIME+CRITICAL+BREACHED DESC"
		 * ;
		 */

		if (userPage != null && userPage > 0)
			mainQuery = mainQuery + " LIMIT 10 OFFSET " + 10 * (userPage - 1);

		System.err.println("[WBP-Dev][getUserWorkCountList]user : " + mainQuery);
		@SuppressWarnings("unchecked")
		List<Object[]> resultList = this.getSession().createSQLQuery(mainQuery).list();
		for (Object[] obj : resultList) {
			dto = new UserWorkCountDto();
			dto.setUserName((String) obj[1]);
			dto.setUserId((String) obj[0]);
			dto.setInTime((BigInteger) obj[2]);
			dto.setCritical((BigInteger) obj[3]);
			dto.setSlaBreached((BigInteger) obj[4]);
			list.add(dto);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private List<TaskNameCountDto> getTaskCompletionRange(String duration) {
		List<TaskNameCountDto> list = null;
		try {
			if (!ServicesUtil.isEmpty(duration)) {

				list = new ArrayList<TaskNameCountDto>();
				List<String> listOfString = null;
				String queryString = "select ";
				String subQuery = "";
				if (PMCConstant.GRAPH_TREND_WEEK.equals(duration)) {

					listOfString = ServicesUtil.getPreviousDates();
					for (int i = 0; i < listOfString.size() - 1; i++) {
						subQuery += "(select count(*) from (SELECT distinct request_id,te.event_id ";

						subQuery += InboxFilterDao.getCommonJoinQuery();
						subQuery += InboxFilterDao.getCommonConditionQuery(null,
								new QuickFilterDto(null, PMCConstant.TASK_COMPLETED, null, null, null));

						subQuery += " AND TE.STATUS NOT IN('READY','RESERVED','DRAFT')  and te.completed_at between "
								+ "'" + listOfString.get(i) + " 00:00:00' " + "and '" + listOfString.get(i)
								+ " 23:59:59')) as d" + (i + 1) + ",";
					}
					//te.status in ('COMPLETED','RESOLVED','APPROVED','REJECTED','DONE')
				} else if (PMCConstant.GRAPH_TREND_MONTH.equals(duration)) {

					listOfString = ServicesUtil.getPreviousMonths();
					for (int i = 0; i < 3; i++) {
						subQuery += "(select count(*) from (SELECT distinct request_id,te.event_id ";

						subQuery += InboxFilterDao.getCommonJoinQuery();
						subQuery += InboxFilterDao.getCommonConditionQuery(null,
								new QuickFilterDto(null, PMCConstant.TASK_COMPLETED, null, null, null));

						subQuery += " AND TE.STATUS NOT IN('READY','RESERVED','DRAFT') and te.completed_at between '"
								+ ServicesUtil.monthStartDate(listOfString.get(i)) + "' and '"
								+ ServicesUtil.monthEndDate(listOfString.get(i)) + "')) as m" + (i + 1) + ",";
					}
					//te.status in ('COMPLETED','RESOLVED','APPROVED','REJECTED','DONE')
					listOfString = ServicesUtil.getPreviousMonthsName();
				}

				queryString += subQuery.substring(0, subQuery.length() - 1) + " from dummy";

				System.err.println("[WBP-Dev]month" + queryString);

				List<Object[]> resultList = this.getSession().createSQLQuery(queryString).list();

				if (listOfString != null)
					list = getTaskDto(listOfString, resultList.get(0), (PMCConstant.GRAPH_TREND_WEEK.equals(duration))
							? listOfString.size() - 1 : listOfString.size());

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]error:" + e.getLocalizedMessage() + "," + e.getMessage());
		}
		return list;

	}

//	public static void main(String[] args) {
//		String queryString = "SELECT TE.PROC_NAME, PC.PROCESS_DISPLAY_NAME, ( "
//				+ "	select count(*) from ( SELECT DISTINCT pe.REQUEST_ID AS REQUEST_ID,te1.event_id from "
//				+ "	TASK_EVENTS te1 LEFT OUTER JOIN PROCESS_CONFIG_TB AS Pct "
//				+ "	ON TE1.PROC_NAME = PCT.PROCESS_NAME  	LEFT OUTER JOIN TASK_SLA AS ts "
//				+ "	ON te1.NAME = ts.TASK_DEF  	LEFT OUTER JOIN PROCESS_EVENTS AS pe "
//				+ "	ON pe.process_id = te1.process_id  	INNER JOIN TASK_OWNERS AS tw "
//				+ "	ON tw.event_id = te1.event_id WHERE TE1.PROC_NAME = TE.PROC_NAME "
//				+ "			AND CURRENT_TIMESTAMP < ADD_SECONDS(comp_deadline, - (" + PMCConstant.CRITICAL_TIME + ")) "
//				+ "			AND te1.STATUS <> 'COMPLETED' AND te1.STATUS<>'CANCELED' and pe.request_id is not null ) )AS IN_TIME,  	( "
//				+ "		select count(*) from (  SELECT DISTINCT pe.REQUEST_ID AS REQUEST_ID,te1.event_id from "
//				+ "		TASK_EVENTS te1  		LEFT OUTER JOIN PROCESS_CONFIG_TB AS Pct "
//				+ "	ON TE1.PROC_NAME = PCT.PROCESS_NAME  	LEFT OUTER JOIN TASK_SLA AS ts "
//				+ "	ON te1.NAME = ts.TASK_DEF  	LEFT OUTER JOIN PROCESS_EVENTS AS pe "
//				+ "	ON pe.process_id = te1.process_id  	INNER JOIN TASK_OWNERS AS tw "
//				+ "	ON tw.event_id = te1.event_id WHERE TE1.PROC_NAME = TE.PROC_NAME "
//				+ "			and		current_timestamp between  ADD_SECONDS(comp_deadline, - ("
//				+ PMCConstant.CRITICAL_TIME
//				+ ")) AND comp_deadline 	AND te1.STATUS <> 'COMPLETED' AND te1.STATUS<>'CANCELED' "
//				+ " and pe.request_id is not null)) AS CRITICAL,  	( "
//				+ "	select count(*) from ( SELECT DISTINCT pe.REQUEST_ID AS REQUEST_ID,te1.event_id from "
//				+ "		TASK_EVENTS te1 LEFT OUTER JOIN PROCESS_CONFIG_TB AS Pct "
//				+ "	ON TE1.PROC_NAME = PCT.PROCESS_NAME 	LEFT OUTER JOIN TASK_SLA AS ts "
//				+ "	ON te1.NAME = ts.TASK_DEF 	LEFT OUTER JOIN PROCESS_EVENTS AS pe "
//				+ "	ON pe.process_id = te1.process_id 	INNER JOIN TASK_OWNERS AS tw "
//				+ "	ON tw.event_id = te1.event_id WHERE TE1.PROC_NAME = TE.PROC_NAME "
//				+ "			AND CURRENT_TIMESTAMP > comp_deadline			AND te1.STATUS <> 'COMPLETED' AND te1.STATUS<>'CANCELED' and pe.request_id is not null "
//				+ "	)) AS SLA_BREACHED FROM TASK_EVENTS AS TE 	INNER JOIN PROCESS_CONFIG_TB AS PC "
//				+ "	ON PC.PROCESS_NAME = TE.PROC_NAME GROUP BY TE.PROC_NAME, 	PC.PROCESS_DISPLAY_NAME";
//
//		System.out.println(queryString);
//	}
	//
	// private String readyAfterSLAQuery() {
	// // TODO Auto-generated method stub
	// return " AND te.comp_deadline < current_timestamp and te.status ='READY'
	// ";
	// }
	//
	// private String readyWithSLAQuery() {
	// // TODO Auto-generated method stub
	// return " AND te.comp_deadline > current_timestamp and te.status ='READY'
	// ";
	// }
	//
	// private String reservedAfterSLAQuery() {
	// // TODO Auto-generated method stub
	// return " AND current_timestamp > te.comp_deadline and te.status in('" +
	// PMCConstant.TASK_STATUS_RESERVED
	// + "', '" + PMCConstant.TASK_STATUS_IN_PROGRESS + "','" +
	// PMCConstant.TASK_STATUS_RESOLVED + "') ";
	// }
	//
	// private String reservedWithSLAQuery() {
	// // TODO Auto-generated method stub
	// return " AND current_timestamp < te.comp_deadline and te.status in('" +
	// PMCConstant.TASK_STATUS_RESERVED
	// + "', '" + PMCConstant.TASK_STATUS_IN_PROGRESS + "','" +
	// PMCConstant.TASK_STATUS_RESOLVED + "')";
	// }
	//
	// private String completedAfterSLAQuery() {
	// // TODO Auto-generated method stub
	// return " AND te.comp_deadline<te.completed_at and te.status ='COMPLETED'
	// ";
	// }
	//
	// // Tasks completed with SLA
	// private String completedOnTimeQuery() {
	// // TODO Auto-generated method stub
	// return "AND te.comp_deadline>te.completed_at and te.status ='COMPLETED'
	// ";
	// }
}