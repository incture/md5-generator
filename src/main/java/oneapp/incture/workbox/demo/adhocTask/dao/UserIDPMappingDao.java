package oneapp.incture.workbox.demo.adhocTask.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskOwnerDto;
import oneapp.incture.workbox.demo.adhocTask.util.UserIDPMappingDto;

@Repository("adhocUserIDPMapping")
public class UserIDPMappingDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	public UserIDPMappingDto getOwnerDetail(String eventId) {
		UserIDPMappingDto userIDPMappingDto = null;

		Query userQry = getSession().createSQLQuery("SELECT USER_ID, USER_FIRST_NAME,USER_LAST_NAME "
				+ "FROM USER_IDP_MAPPING "
				+ "WHERE USER_ID = (SELECT OWNER_ID FROM TASK_EVENTS "
				+ "WHERE EVENT_ID = '"+eventId+"')");
		Object[] userDetail = (Object[]) userQry.uniqueResult();

		userIDPMappingDto = new UserIDPMappingDto();
		userIDPMappingDto.setUserFirstName(userDetail[1].toString());
		userIDPMappingDto.setUserId(userDetail[0].toString());
		userIDPMappingDto.setUserLastName(userDetail[2].toString());

		return userIDPMappingDto;
	}

	@SuppressWarnings("unchecked")
	public List<TaskOwnerDto> getOwnerDetailById(String[] sendToUser) {
		Session session = sessionFactory.openSession();
		List<TaskOwnerDto> userDeatils = null;
		TaskOwnerDto taskOwnerDto = null;
		StringBuilder userList = new StringBuilder("");
		for (String user : sendToUser) {
			userList.append("'");
			userList.append(user);
			userList.append("',");
		}
		Query userQry = session.createSQLQuery("SELECT USER_ID, USER_FIRST_NAME,USER_LAST_NAME,USER_EMAIL "
				+ "FROM USER_IDP_MAPPING "
				+ "WHERE USER_ID in ("+userList.substring(0, userList.length()-1)+")");
		
		List<Object[]> userDetailList = userQry.list();

		userDeatils = new ArrayList<TaskOwnerDto>();
		for (Object[] obj : userDetailList) {
			
			taskOwnerDto = new TaskOwnerDto();
			taskOwnerDto.setTaskOwner(obj[0].toString());
			taskOwnerDto.setEmail(obj[3].toString());
			taskOwnerDto.setTaskOwnername(obj[1].toString()+" "+obj[2].toString());
			
			userDeatils.add(taskOwnerDto);
		}
		
		return userDeatils;

	}

	public String getUserName(String userId) {
		Session session  = sessionFactory.openSession();
		Query fetchNameQry = session.createSQLQuery("SELECT USER_FIRST_NAME || ' ' || USER_LAST_NAME "
				+ "FROM USER_IDP_MAPPING WHERE USER_ID = '"+userId+"'");
		String name = (String) fetchNameQry.uniqueResult();
		return name;
	}

	public UserIDPMappingDto getUserDetail(String userId) {
		UserIDPMappingDto userIDPMappingDto = null;
		String fetchDetailStr = "SELECT DISTINCT USER_FIRST_NAME , USER_LAST_NAME ,USER_ROLE "
				+ "FROM USER_IDP_MAPPING WHERE USER_ID = '"+userId+"'";
		Query fetchDetailQry = this.getSession().createSQLQuery(fetchDetailStr);		
		Object[] userDetails = (Object[]) fetchDetailQry.uniqueResult();
		userIDPMappingDto = new UserIDPMappingDto();
		
		userIDPMappingDto.setUserFirstName(userDetails[0].toString());
		userIDPMappingDto.setUserLastName(userDetails[1].toString());
		userIDPMappingDto.setUserRole(ServicesUtil.isEmpty(userDetails[2])?null:userDetails[2].toString());
		return userIDPMappingDto;
	}
	
	public Map<String, String> getusers() {
		Map<String, String> users = new HashMap<String, String>();
		String fetchDetailStr = "SELECT DISTINCT USER_FIRST_NAME || ' ' ||USER_LAST_NAME ,USER_Id "
				+ "FROM USER_IDP_MAPPING ";
		Query fetchDetailQry = this.getSession().createSQLQuery(fetchDetailStr);
		List<Object[]> result = fetchDetailQry.list();

		for (Object[] obj : result) {
			users.put(ServicesUtil.isEmpty(obj[1]) ? "" : obj[1].toString(),
					ServicesUtil.isEmpty(obj[0]) ? "" : obj[0].toString());
		}
		return users;
	}
	
	public Map<String, Integer> getUsersWithBudget() {

		Map<String, Integer> budgetDetails = new HashMap<>();
		String bugetQuery = "select user_id , afe_nexus_budget from user_idp_mapping where afe_nexus_budget is not NULL";
		List<Object[]> resultList = this.getSession().createSQLQuery(bugetQuery).list();
		for (Object[] obj : resultList) {
			budgetDetails.put((String) obj[0], (Integer) obj[1]);
		}
		return budgetDetails;

	}
	
	public List<String> getUsersWithOrder(String filterType) {

		String budgetQuery = "select user_id  from user_idp_mapping where afe_nexus_budget is not NULL order by ";
		
		if(filterType.equals("Alphabetical")) {
			budgetQuery += " user_first_name , user_last_name , afe_nexus_budget ";
		}
		else {
			budgetQuery += " afe_nexus_budget ";
		}	
		if(filterType.equals("Descending")) {
			budgetQuery += " DESC";
		}
		
		return this.getSession().createSQLQuery(budgetQuery).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getUsersWithOrderByNameWithType(String filterType , String userList) {

		String userOrderQuery = "select user_id  from user_idp_mapping where user_id in(" + userList + 
				") order by user_first_name " + filterType + " , user_last_name " + filterType;	
		System.err.println(userOrderQuery);
		return this.getSession().createSQLQuery(userOrderQuery).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getUsersWithOrderByName(String eventId , String userList) {
		
		
		String typeQuery = "select ts.order_by  from task_events te join process_template_value  pt " +
									" on te.name = pt.task_name and te.process_id = pt.process_id join task_owner_template_sequence ts" + 
									" on ts.template_id = pt.template_id where te.event_id = :eventId";
		
		String filterType = (String) this.getSession().createSQLQuery(typeQuery).setParameter("eventId", eventId).uniqueResult();
		String userOrderQuery = "select user_id  from user_idp_mapping where user_id in('" + userList + 
				") order by user_first_name "+ filterType +" , user_last_name " + filterType;	
		System.err.println(userOrderQuery);
		return this.getSession().createSQLQuery(userOrderQuery).list();
	}

}
