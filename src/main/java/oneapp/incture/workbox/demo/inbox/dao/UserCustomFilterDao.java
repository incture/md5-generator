package oneapp.incture.workbox.demo.inbox.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dto.UserCustomFilterDto;
import oneapp.incture.workbox.demo.inbox.entity.UserCustomFilter;

@Repository("UserCustomFilterDao")
//////@Transactional
public class UserCustomFilterDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public ResponseMessage saveFilterView(UserCustomFilter customFilter) {
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Filter " + PMCConstant.SAVE_SUCCESS);
		message.setStatus(PMCConstant.SUCCESS);
		message.setStatusCode(PMCConstant.CODE_SUCCESS);
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(customFilter);
			tx.commit();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[WBP-Dev]UserCustomFilterDao.saveFilterView()" + e.getMessage());
			message.setMessage("Filter " + PMCConstant.CREATE_FAILURE);
			message.setStatus(PMCConstant.FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);
		}

		return message;
	}

	public ResponseMessage saveAllFilterView(List<UserCustomFilter> customFilter,Token token) {
		ResponseMessage message = new ResponseMessage();
		String loggedInUser = token.getLogonName();//UserManagementUtil.getLoggedInUser().getName();
		message.setMessage("Filter " + PMCConstant.SAVE_SUCCESS);
		message.setStatus(PMCConstant.SUCCESS);
		message.setStatusCode(PMCConstant.CODE_SUCCESS);
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for (UserCustomFilter userCustomFilter : customFilter) {
				userCustomFilter.setUserId(loggedInUser);
				session.saveOrUpdate(userCustomFilter);

			}
			session.flush();
			session.clear();
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[WBP-Dev]UserCustomFilterDao.saveFilterView()" + e.getMessage());
			message.setMessage("Filter " + PMCConstant.SAVE_FAILURE);
			message.setStatus(PMCConstant.FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);
		}

		return message;
	}

	public ResponseMessage updateFilterView(UserCustomFilter customFilter) {
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Filter " + PMCConstant.SAVE_SUCCESS);
		message.setStatus(PMCConstant.SUCCESS);
		message.setStatusCode(PMCConstant.CODE_SUCCESS);
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(customFilter);
			tx.commit();
			session.close();
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println("[WBP-Dev]UserCustomFilterDao.saveFilterView()" + e.getMessage());
			message.setMessage("Filter " + PMCConstant.CREATE_FAILURE);
			message.setStatus(PMCConstant.FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);
		}

		return message;
	}

	@SuppressWarnings("unchecked")
	public List<UserCustomFilterDto> getFilters(String userId) {

		List<UserCustomFilterDto> customFilterDtos = new ArrayList<>();
		
		try {
			String query = "SELECT USER_ID,FILTER_NAME,FILTER_DATA,IS_VIEW,VIEW_NAME,FILTER_ID,IS_TILE,THRESHOLD,FILTER_TYPE,IS_ACTIVE,DESCRIPTION,FILTER_SEQUENCE,IS_TRAY,INBOX_TYPE FROM USER_CUSTOM_FILTER WHERE UPPER(USER_ID) in ('"
					+ userId.toUpperCase() + "','ADMIN')  ORDER BY FILTER_SEQUENCE";
			Query q = this.getSession().createSQLQuery(query.trim());
			List<Object[]> resultList = q.list();

			System.err.println("[WBP-Dev]UserCustomFilterDao.getFilters()  resultList" + resultList);
			for (Object[] obj : resultList) {
				UserCustomFilterDto filterDto = new UserCustomFilterDto();
				filterDto.setUserId(obj[0] == null ? null : (String) obj[0]);
				filterDto.setFilterName(obj[1] == null ? null : (String) obj[1]);
				filterDto.setFilterData(obj[2] == null ? null : (String) obj[2]);

				byte flag = (obj[3] == null ? 0 : (byte) obj[3]);
				filterDto.setIsView(flag == 1 ? true : false);

				filterDto.setViewName(obj[4] == null ? null : (String) obj[4]);
				filterDto.setFilterId(obj[5] == null ? null : (int) obj[5]);

				flag = (obj[6] == null ? 0 : (byte) obj[6]);
				filterDto.setIsTile(flag == 1 ? true : false);

				filterDto.setThreshold(obj[7] == null ? null : (String) obj[7]);
				filterDto.setFilterType(obj[8] == null ? null : (String) obj[8]);

				flag = (obj[9] == null ? 0 : (byte) obj[9]);
				filterDto.setActive(flag == 1 ? true : false);

				filterDto.setDescription(obj[10] == null ? null : (String) obj[10]);

				filterDto.setSequence(obj[11] == null ? 0 : (int) obj[11]);
				if(!ServicesUtil.isEmpty(obj[12]))
					filterDto.setTray(ServicesUtil.asBoolean(obj[12]));
				filterDto.setInboxType(obj[13] == null ? null : (String) obj[13]);
				customFilterDtos.add(filterDto);

			}

			System.err.println("[WBP-Dev]UserCustomFilterDao.getFilters() query customFilterDtos" + customFilterDtos);

		}

		catch (Exception e) {
			e.printStackTrace();

			System.err.println("[WBP-Dev]UserCustomFilterDao.getFilter()" + e.getStackTrace());

		}

		return customFilterDtos;
	}

	@SuppressWarnings("unchecked")
	public List<UserCustomFilterDto> getFilters(String userId, boolean isView, boolean isTile, boolean isActive) {

		List<UserCustomFilterDto> customFilterDtos = new ArrayList<>();
		String query = "";

		try {

			if (checkIfStandardTileExists(userId)) {
				query = "SELECT USER_ID,FILTER_NAME,FILTER_DATA,IS_VIEW,VIEW_NAME,FILTER_ID,IS_TILE,THRESHOLD,FILTER_TYPE,IS_ACTIVE,DESCRIPTION,FILTER_SEQUENCE  FROM USER_CUSTOM_FILTER WHERE UPPER(USER_ID) in ('"
						+ userId.toUpperCase() + "')";
			} else {
				query = "SELECT USER_ID,FILTER_NAME,FILTER_DATA,IS_VIEW,VIEW_NAME,FILTER_ID,IS_TILE,THRESHOLD,FILTER_TYPE,IS_ACTIVE,DESCRIPTION,FILTER_SEQUENCE  FROM USER_CUSTOM_FILTER WHERE UPPER(USER_ID) in ('"
						+ userId.toUpperCase() + "','ADMIN')  ";
			}

			if (isView)
				query = query + " AND IS_VIEW = 1";
			if (isTile)
				query = query + " AND IS_TILE = 1";
			if (isActive)
				query = query + " AND IS_ACTIVE = 1";

			query = query + " ORDER BY FILTER_SEQUENCE";
			Query q = this.getSession().createSQLQuery(query.trim());
			List<Object[]> resultList = q.list();

			System.err.println("[WBP-Dev]UserCustomFilterDao.getFilters()  resultList" + resultList);
			for (Object[] obj : resultList) {
				UserCustomFilterDto filterDto = new UserCustomFilterDto();
				filterDto.setUserId(obj[0] == null ? null : (String) obj[0]);
				filterDto.setFilterName(obj[1] == null ? null : (String) obj[1]);
				filterDto.setFilterData(obj[2] == null ? null : (String) obj[2]);

				byte flag = (obj[3] == null ? 0 : (byte) obj[3]);
				filterDto.setIsView(flag == 1 ? true : false);

				filterDto.setViewName(obj[4] == null ? null : (String) obj[4]);
				filterDto.setFilterId(obj[5] == null ? null : (int) obj[5]);

				flag = (obj[6] == null ? 0 : (byte) obj[6]);
				filterDto.setIsTile(flag == 1 ? true : false);

				filterDto.setThreshold(obj[7] == null ? null : (String) obj[7]);
				filterDto.setFilterType(obj[8] == null ? null : (String) obj[8]);

				flag = (obj[9] == null ? 0 : (byte) obj[9]);
				filterDto.setActive(flag == 1 ? true : false);

				filterDto.setDescription(obj[10] == null ? null : (String) obj[10]);

				filterDto.setSequence(obj[11] == null ? 0 : (int) obj[11]);

				customFilterDtos.add(filterDto);

			}

			System.err.println("[WBP-Dev]UserCustomFilterDao.getFilters() query customFilterDtos" + customFilterDtos);

		}

		catch (Exception e) {
			e.printStackTrace();

			System.err.println("[WBP-Dev]UserCustomFilterDao.getFilter()" + e.getStackTrace());

		}

		return customFilterDtos;
	}

	public ResponseMessage deleteFilter(int filterId) {
		String result = "";
		ResponseMessage message = new ResponseMessage();
		message.setMessage("Filter " + PMCConstant.DELETE_SUCCESS);
		message.setStatus(PMCConstant.SUCCESS);
		message.setStatusCode(PMCConstant.CODE_SUCCESS);

		String query = "DELETE FROM USER_CUSTOM_FILTER  WHERE FILTER_ID=" + filterId;

		System.err.println("[WBP-Dev]UserCustomFilterDao.deleteFilter() query" + query);
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int resultRows = session.createSQLQuery(query).executeUpdate();
		tx.commit();
		session.close();
		System.err.println("[WBP-Dev]UserCustomFilterDao.deleteFilter() result" + result);

		if (resultRows <= 0) {
			message.setMessage("Filter " + PMCConstant.DELETE_FAILURE);
			message.setStatus(PMCConstant.FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);

		}

		return message;
	}

	public ResponseMessage deleteView(int filterId) {
		String result = "";
		ResponseMessage message = new ResponseMessage();
		message.setMessage("View " + PMCConstant.DELETE_SUCCESS);
		message.setStatus(PMCConstant.SUCCESS);
		message.setStatusCode(PMCConstant.CODE_SUCCESS);

		try {

			String query = "UPDATE USER_CUSTOM_FILTER SET IS_VIEW = 0 WHERE FILTER_ID=" + filterId;
			System.err.println("[WBP-Dev]UserCustomFilterDao.deleteView() query" + query);
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			int resultRows = session.createSQLQuery(query).executeUpdate();
			tx.commit();
			session.close();
			System.err.println("[WBP-Dev]UserCustomFilterDao.deleteView() result" + result);

			if (resultRows <= 0) {
				message.setMessage("View " + PMCConstant.DELETE_FAILURE);
				message.setStatus(PMCConstant.FAILURE);
				message.setStatusCode(PMCConstant.CODE_FAILURE);

			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("View " + PMCConstant.DELETE_FAILURE);
			message.setStatus(PMCConstant.FAILURE);
			message.setStatusCode(PMCConstant.CODE_FAILURE);

			System.err.println("[WBP-Dev]UserCustomFilterDao.deleteView()" + e.getMessage());

		}

		return message;
	}

	@SuppressWarnings("unchecked")
	public boolean filterNameForDifferentIdExists(String userId, int filterId, String filterName) {

		String query = "SELECT * FROM USER_CUSTOM_FILTER WHERE UPPER(USER_ID)=UPPER('" + userId.toUpperCase()
				+ "') AND FILTER_ID <> " + filterId + " AND FILTER_NAME='" + filterName + "'";
		Query q = this.getSession().createSQLQuery(query.trim());
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			return true;
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	public boolean filterNameExists(String userId, String filterName) {

		String query = "SELECT * FROM USER_CUSTOM_FILTER WHERE UPPER(USER_ID)=UPPER('" + userId.toUpperCase()
				+ "') AND FILTER_NAME='" + filterName + "'";
		Query q = this.getSession().createSQLQuery(query.trim());
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			return true;
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	public boolean checkIfStandardTileExists(String userId) {

		String query = "SELECT * FROM USER_CUSTOM_FILTER WHERE UPPER(USER_ID)=UPPER('" + userId.toUpperCase()
				+ "') AND FILTER_TYPE='STANDARD' AND IS_TILE= 1 AND IS_ACTIVE = 1 ";
		Query q = this.getSession().createSQLQuery(query.trim());
		List<Object[]> resultList = q.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			return true;
		}
		return false;

	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getTaskBoadFilters(String userId, String inboxType, Integer page) {
		String query = "SELECT FILTER_NAME,VIEW_NAME,FILTER_DATA,FILTER_ID FROM USER_CUSTOM_FILTER WHERE UPPER(USER_ID)=UPPER('" + userId
				+ "') AND INBOX_TYPE='"+inboxType+"' AND IS_TRAY= true ";
		Query q = this.getSession().createSQLQuery(query.trim());
		if (!ServicesUtil.isEmpty(page) && page > 0) {
			int first = (page - 1) * PMCConstant.TRAY_SIZE;
			int last = PMCConstant.TRAY_SIZE;
			q.setFirstResult(first);
			q.setMaxResults(last);
		}
		List<Object[]> resultList = q.list();
		return resultList;		
	}

	public BigDecimal getTaskBoadFiltersCount(String userId, String inboxType) {
		String query = "SELECT count(*) FROM USER_CUSTOM_FILTER WHERE UPPER(USER_ID)=UPPER('" + userId
				+ "') AND INBOX_TYPE='"+inboxType+"' AND IS_TRAY= true ";
		Query q = this.getSession().createSQLQuery(query.trim());
		
		return ServicesUtil.getBigDecimal(q.uniqueResult());	
	}

}
