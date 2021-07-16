package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dto.UserDetailsDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("UserDetailsDao")
//////@Transactional
public class UserDetailsDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public UserDetailsDto getUserDetails(UserDetailsDto userDetails) {
		UserDetailsDto user = new UserDetailsDto();
		String query = "SELECT USER_ID,USER_LOGIN_NAME,USER_LAST_NAME, USER_EMAIL, USER_FIRST_NAME FROM USER_IDP_MAPPING ";
		if (!ServicesUtil.isEmpty(userDetails)) {
			query += "WHERE ";
			if (!ServicesUtil.isEmpty(userDetails.getUserId())) {
				query += "( USER_ID LIKE '%" + userDetails.getUserId() + "%' or  SHAREPOINT_ID LIKE '%"
						+ userDetails.getUserId() + "%') AND ";
			}
			if (!ServicesUtil.isEmpty(userDetails.getEmailId())) {
				query += " UPPER(USER_EMAIL) LIKE UPPER('%" + userDetails.getUserId() + "%')";
			} else {
				query += " 1 = 1 ";
			}
			List<Object[]> row = this.getSession().createSQLQuery(query).list();
			if (!ServicesUtil.isEmpty(row) && row.size() == 1) {
				Object[] userResult = row.get(0);
				user.setUserId(ServicesUtil.isEmpty(userResult[0]) ? null : (String) userResult[0]);
				user.setEmailId(ServicesUtil.isEmpty(userResult[3]) ? null : (String) userResult[3]);
				user.setDisplayName(ServicesUtil.isEmpty(userResult[4]) ? null : (String) userResult[4]);
			}
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<String>> getAllSharepointUserDetails() {
		Map<String, List<String>> userMap = new HashMap<String, List<String>>();
		List<String> userDetail = null;
		try {

			String query = "SELECT USER_ID,SHAREPOINT_ID,USER_LOGIN_NAME,USER_LAST_NAME, USER_EMAIL,"
					+ " USER_FIRST_NAME FROM USER_IDP_MAPPING  WHERE USER_ID IS NOT NULL AND SHAREPOINT_ID IS NOT NULL";
			System.err.println("[WBP-Dev][Sharepoint][SharepointUser query][GETSHARE]" + query);
			List<Object[]> row = this.getSession().createSQLQuery(query).list();

			if (!ServicesUtil.isEmpty(row) && row.size() > 0) {
				for (Object[] userResult : row) {
					userDetail = new ArrayList<String>();
					
					userDetail.add(ServicesUtil.isEmpty(userResult[5]) ? null : (String) userResult[5]);
					userDetail.add(ServicesUtil.isEmpty(userResult[3]) ? null : (String) userResult[3]);
					// userDetail.add(ServicesUtil.isEmpty(userResult[0]) ? null
					// :
					// (String) userResult[0]);

					userMap.put(ServicesUtil.isEmpty(userResult[0]) ? null : (String) userResult[0], userDetail);
				}
			}
		} catch (Exception e) {

			System.err.println("[WBP-Dev][Workbox][Sharepoint][Error]" + e);
			userDetail = new ArrayList<String>();
			userDetail.add("Shruti.patra@incture.com");
			userDetail.add("Shruti");
			userMap.put("P000057", userDetail);
		}
		return userMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<String>> getAllSuccessFactorsUserDetails() {
		Map<String, List<String>> userMap = new HashMap<String, List<String>>();
		List<String> userDetail = null;
		try {

			String query = "SELECT USER_ID,SUCCESSFACTORS_ID,USER_LOGIN_NAME, USER_FIRST_NAME || ' ' || USER_LAST_NAME,"
					+ " USER_EMAIL"
					+ " FROM USER_IDP_MAPPING  WHERE USER_ID IS NOT NULL AND SUCCESSFACTORS_ID IS NOT NULL";
			System.err.println("[WBP-Dev][SuccessFactor][SuccessFactor query][getSFUser]" + query);
			List<Object[]> row = this.getSession().createSQLQuery(query).list();

			if (!ServicesUtil.isEmpty(row) && row.size() > 0) {
				for (Object[] userResult : row) {
					userDetail = new ArrayList<String>();
					// userDetail.add(ServicesUtil.isEmpty(userResult[1]) ? null
					// : (String) userResult[4]);
					userDetail.add(ServicesUtil.isEmpty(userResult[1]) ? null : (String) userResult[1]);
					userDetail.add(ServicesUtil.isEmpty(userResult[3]) ? null : (String) userResult[3]);
					userDetail.add(ServicesUtil.isEmpty(userResult[4]) ? null : (String) userResult[4]);
					// userDetail.add(ServicesUtil.isEmpty(userResult[0]) ? null
					// :
					// (String) userResult[0]);

					userMap.put(ServicesUtil.isEmpty(userResult[0]) ? null : (String) userResult[0], userDetail);
				}
			}
		} catch (Exception e) {

			System.err.println("[WBP-Dev][Workbox][SuccessFactor][Error]" + e);
			userDetail = new ArrayList<String>();
			userDetail.add("sfadmin");
			userDetail.add("Aanya Singh");
			userDetail.add("preetham.r@incture.com");
			userMap.put("preetham.r@incture.com", userDetail);
		}

		System.err.println("[WBP-Dev]SuccessFactorsUtil.() usersMap:getAllSuccessFactorsUserDetails" + userMap);
		return userMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getAllUserMaping() {
		Map<String, String> userMap = new HashMap<String, String>();

		String query = "SELECT distinct SHAREPOINT_ID,USER_ID FROM USER_IDP_MAPPING  WHERE USER_ID IS NOT NULL AND SHAREPOINT_ID IS NOT NULL";
		System.err.println("[WBP-Dev][Sharepoint][SharepointUser query]" + query);
		List<Object[]> row = this.getSession().createSQLQuery(query).list();
		if (!ServicesUtil.isEmpty(row) && row.size() > 0) {
			for (Object[] userResult : row) {
				userMap.put(ServicesUtil.isEmpty(userResult[0]) ? null : (String) userResult[0],
						(String) userResult[1]);
			}
		}
		return userMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, List<String>> getAllUserDetails() {
		Map<String, List<String>> userMap = new HashMap<String, List<String>>();

		String query = "SELECT USER_ID,USER_LOGIN_NAME,USER_LAST_NAME, USER_EMAIL, USER_FIRST_NAME FROM USER_IDP_MAPPING ";
		List<Object[]> row = this.getSession().createSQLQuery(query).list();
		List<String> userDetail = null;
		if (!ServicesUtil.isEmpty(row) && row.size() > 0) {
			for (Object[] userResult : row) {
				userDetail = new ArrayList<String>();
				userDetail.add(ServicesUtil.isEmpty(userResult[3]) ? null : (String) userResult[3]);
				userDetail.add(ServicesUtil.isEmpty(userResult[4]) ? null : (String) userResult[4]);

				userMap.put(ServicesUtil.isEmpty(userResult[0]) ? null : (String) userResult[0], userDetail);
			}
		}
		return userMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, UserDetailsDto> getAllUserDetailsInMap() {
		Map<String, UserDetailsDto> userDetailsMap = new HashMap<String, UserDetailsDto>();

		String query = "SELECT USER_ID,USER_LOGIN_NAME,USER_LAST_NAME, USER_EMAIL, USER_FIRST_NAME FROM USER_IDP_MAPPING ";
		List<Object[]> row = this.getSession().createSQLQuery(query).list();

		if (!ServicesUtil.isEmpty(row) && row.size() > 0) {
			for (Object[] user : row) {
				UserDetailsDto dto = new UserDetailsDto();
				dto.setUserId(user[0] != null ? user[0].toString() : null);
				dto.setEmailId(user[3] != null ? user[3].toString() : null);
				dto.setDisplayName(user[4].toString() + " " + user[2].toString());
				userDetailsMap.put(dto.getUserId(), dto);
			}
		}
		System.err.println("UserDetailsDao.getAllUserDetailsInMap() user details map : " + userDetailsMap);
		return userDetailsMap;
	}

	// @SuppressWarnings("unchecked")
	// public UserDetailsDto getUserDetails(UserDetailsDto userDetails) {
	// UserDetailsDto user = new UserDetailsDto();
	// String query = "SELECT USER_ID, IS_DELETED, STATUS, USER_EMAIL, USER_NAME
	// FROM \"WORKBOX_AGCO_TEMP\".\"APT_USER\" ";
	// if(!ServicesUtil.isEmpty(userDetails)) {
	// query += "WHERE ";
	// if(!ServicesUtil.isEmpty(userDetails.getUserId())) {
	// query += " USER_ID LIKE '%"+userDetails.getUserId()+"%' AND ";
	// }
	// if(!ServicesUtil.isEmpty(userDetails.getEmailId())) {
	// query += " UPPER(USER_EMAIL) LIKE
	// UPPER('%"+userDetails.getUserId()+"%')";
	// } else {
	// query += " 1 = 1 ";
	// }
	// List<Object[]> row = this.getSession().createSQLQuery(query).list();
	// if(!ServicesUtil.isEmpty(row) && row.size() == 1) {
	// Object[] userResult = row.get(0);
	// user.setUserId(ServicesUtil.isEmpty(userResult[0]) ? null : (String)
	// userResult[0]);
	// user.setEmailId(ServicesUtil.isEmpty(userResult[3]) ? null : (String)
	// userResult[3]);
	// user.setDisplayName(ServicesUtil.isEmpty(userResult[4]) ? null : (String)
	// userResult[4]);
	// }
	// }
	// return user;
	// }
	//
	@SuppressWarnings("unchecked")
	public List<Object[]> getUserDetailResponse() {
		return this.getSession().createSQLQuery("SELECT USER_ID, USER_EMAIL, USER_FIRST_NAME FROM USER_IDP_MAPPING ")
				.list();
	}
	
	public String getUserName(String userId) {
		Query fetchNameQry = this.getSession().createSQLQuery("SELECT USER_FIRST_NAME || ' ' || USER_LAST_NAME "
				+ "FROM USER_IDP_MAPPING WHERE USER_ID = '"+userId+"'");
		String name = (String) fetchNameQry.uniqueResult();
		return name;
	}

}
