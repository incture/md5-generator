package oneapp.incture.workbox.demo.workload.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dao.InboxFilterDao;
import oneapp.incture.workbox.demo.inbox.dto.QuickFilterDto;
import oneapp.incture.workbox.demo.workload.dto.SearchListDto;
import oneapp.incture.workbox.demo.workload.dto.UserWorkloadDto;

/**
 * @author Neelam Raj
 *
 */
@Repository
//////@Transactional
public class HeatMapDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<SearchListDto> getSearchList(String type) {
		List<SearchListDto> dtos = new ArrayList<SearchListDto>();
		SearchListDto dto = null;
		String queryString = "";
		if (!ServicesUtil.isEmpty(type)) {
			if (PMCConstant.SEARCH_PROCESS.equals(type)) {
				queryString = "select distinct process_name, process_display_name from process_config_tb order by process_display_name asc ";
			} else if (PMCConstant.SEARCH_STATUS.equals(type)) {
				queryString = "select distinct constant_name,constant_value from cross_constants where CONSTANT_ID='"
						+ PMCConstant.SEARCH_STATUS + "'";
			}
		}
		Query query = this.getSession().createSQLQuery(queryString);
		List<Object[]> resultList = query.list();
		for (Object[] obj : resultList) {
			dto = new SearchListDto();
			dto.setKey((String) obj[0]);
			dto.setValue((String) obj[1]);
			dtos.add(dto);
		}

		return dtos;
	}

	@SuppressWarnings("unchecked")
	public List<UserWorkloadDto> getUserWorkload(String processName, String requestId, String status) {
		List<UserWorkloadDto> dtos = new ArrayList<UserWorkloadDto>();
		UserWorkloadDto dto = null;
		try {
			String tempQuery = "";
			String query = "SELECT C.TASK_OWNER_DISP AS OWNER_NAME,C.TASK_OWNER AS OWNER, COUNT(C.TASK_OWNER) AS TASK_COUNT "
					+ "from PROCESS_EVENTS A, TASK_OWNERS C , TASK_EVENTS B INNER JOIN PROCESS_CONFIG_TB AS Pct ON B.PROC_NAME = PCT.PROCESS_NAME where A.PROCESS_ID = B.PROCESS_ID and B.EVENT_ID = C.EVENT_ID";
			String groupQuery = " group by C.TASK_OWNER, C.TASK_OWNER_DISP";

			if (!ServicesUtil.isEmpty(processName) && !processName.equals(PMCConstant.SEARCH_ALL)) {
				tempQuery = tempQuery
						+ " and A.PROCESS_ID IN (select D.process_id from PROCESS_EVENTS D where D.name IN ( '"
						+ processName + "'))";
			}
			if (!ServicesUtil.isEmpty(requestId)) {
				tempQuery = tempQuery + " and A.REQUEST_ID = '" + requestId + "'";
			}
			if (!ServicesUtil.isEmpty(status)) {
				if (PMCConstant.SEARCH_READY.equalsIgnoreCase(status)) {
					tempQuery = tempQuery + " and B.STATUS = '" + status + "'";
				} else if (PMCConstant.SEARCH_RESERVED.equalsIgnoreCase(status)) {
					tempQuery = tempQuery + " and B.STATUS = '" + status + "' and C.IS_PROCESSED = 1";
				} else if (PMCConstant.TASK_STATUS_IN_PROGRESS.equalsIgnoreCase(status)) {
					tempQuery = tempQuery + " and B.STATUS = '" + status + "' and C.IS_PROCESSED = 1";
				} else if (PMCConstant.TASK_STATUS_RESOLVED.equalsIgnoreCase(status)) {
					tempQuery = tempQuery + " and B.STATUS = '" + status + "' and C.IS_PROCESSED = 1";
				} else {
					tempQuery = tempQuery + " and (B.STATUS = '" + PMCConstant.TASK_STATUS_READY + "' or (B.STATUS in('"
							+ PMCConstant.TASK_STATUS_RESERVED + "','" + PMCConstant.TASK_STATUS_IN_PROGRESS + "','"
							+ PMCConstant.TASK_STATUS_RESOLVED + "')  and C.IS_PROCESSED = 1))";
				}
			}
			tempQuery = tempQuery + " AND A.STATUS not in ('CANCELED','COMPLETED') AND A.REQUEST_ID IS NOT NULL";
			query = query + tempQuery + groupQuery;

			Query q = this.getSession().createSQLQuery(query);
			List<Object[]> resultList = q.list();
			System.err.println("[WBP-Dev][PMC][DEMO][heatMap Query]" + query);
			// List<Object[]> resultList = getTaskCountWithOwners(processName,
			// requestId, taskStatus);
			for (Object[] obj : resultList) {
				dto = new UserWorkloadDto();
				dto.setUserName((String) obj[0]);
				dto.setUserId((String) obj[1]);
				dto.setNoOfTask((BigInteger) obj[2]);
				dtos.add(dto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][HeatMapDao][getuserWorkLoad][error]" + e.getMessage());
		}

		return dtos;
	}

	@SuppressWarnings("unchecked")
	public List<UserWorkloadDto> getUserWorkloadNew(String processName, String requestId, String status,Token token) {
		List<UserWorkloadDto> dtos = new ArrayList<UserWorkloadDto>();
		UserWorkloadDto dto = null;
		try {
			String query = "SELECT MAX(tw.TASK_OWNER_DISP) AS OWNER_NAME,tw.TASK_OWNER AS OWNER, COUNT(tw.TASK_OWNER) AS TASK_COUNT "
					+ InboxFilterDao.getCommonJoinQuery() + InboxFilterDao.getCommonConditionQuery(null, null)+" ";

			QuickFilterDto filterDto = new QuickFilterDto();
			filterDto.setProcessName(processName);
			filterDto.setStatus(status);

			query += InboxFilterDao.prepareQuickFilterQuery(filterDto,token);
			if(!ServicesUtil.isEmpty(filterDto.getStatus())
						&& PMCConstant.SEARCH_RESERVED.equals(filterDto.getStatus()))
				query += "AND TE.CUR_PROC = tw.TASK_OWNER";
			
			String groupQuery = " group by tw.TASK_OWNER ";

			query = query + groupQuery;
			System.err.println("[WBP-Dev][PMC][DEMO][heatMap Query][start tym]" + query + "start tym"
					+ System.currentTimeMillis());

			Query q = this.getSession().createSQLQuery(query);
			List<Object[]> resultList = q.list();
			System.err.println("[WBP-Dev][PMC][DEMO][heatMap End tym]" + System.currentTimeMillis());
			// List<Object[]> resultList = getTaskCountWithOwners(processName,
			// requestId, taskStatus);
			for (Object[] obj : resultList) {
				dto = new UserWorkloadDto();
				dto.setUserName((String) obj[0]);
				dto.setUserId((String) obj[1]);
				dto.setNoOfTask((BigInteger) obj[2]);
				dtos.add(dto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][HeatMapDao][getuserWorkLoad][error]" + e.getMessage());
			e.printStackTrace();
		}

		return dtos;
	}

	@SuppressWarnings("unchecked")
	public List<SearchListDto> getStatusList() {
		List<SearchListDto> dtos = new ArrayList<SearchListDto>();
		SearchListDto dto = null;
		String queryString = "select distinct constant_name,constant_value from cross_constants where CONSTANT_ID='"
				+ PMCConstant.SEARCH_STATUS + "'";
		Query query = this.getSession().createSQLQuery(queryString);
		List<Object[]> resultList = query.list();
		for (Object[] obj : resultList) {
			dto = new SearchListDto();
			dto.setKey((String) obj[0]);
			dto.setValue((String) obj[1]);
			dtos.add(dto);
		}
		return dtos;
	}

}