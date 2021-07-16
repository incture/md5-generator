package oneapp.incture.workbox.demo.adhocTask.dao;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.ServiceUI;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.sql.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeValueDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValuesTableDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;



//import oneapp.incture.workbox.substitution.dto.SubstitutionApprovalRequestDto;
//import oneapp.incture.workbox.substitution.util.ServicesUtilSubs;

@Repository
//@Transactional
public class SubstitutionDao  {
	
	
	@Autowired	
	private SessionFactory sessionFactory;
	
	

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getSubstitutedUserForTask(String userId, String eventId) {
		Session session = sessionFactory.openSession();
		String queryString = "SELECT DISTINCT SR.SUBSTITUTED_USER FROM SUBSTITUTION_PROCESSES SP "
				+ "INNER JOIN SUBSTITUTION_RULE SR ON SR.RULE_ID = SP.RULE_ID  "
				+ "WHERE SR.IS_ACTIVE = 1  AND SR.SUBSTITUTING_USER = '" + userId + "' AND (SP.PROCESS = ("
				+ " SELECT PROC_NAME FROM TASK_EVENTS WHERE EVENT_ID = '" + eventId + "') OR "
				+ "(SR.SUBSTITUTED_USER IN (SELECT TASK_OWNER FROM TASK_OWNERS WHERE EVENT_ID = '" + eventId + "')"
				+ " AND SP.PROCESS = 'ALL')) "
				+ "AND SR.SUBSTITUTING_USER NOT IN (SELECT TASK_OWNER FROM TASK_OWNERS WHERE EVENT_ID = '" + eventId
				+ "')";
		return session.createSQLQuery(queryString).list();
	}
	
	
	public void updateSubsRule(String approver, String requestor) {
		Session session = sessionFactory.openSession();
		Query updateQry = session
				.createSQLQuery("UPDATE SUBSTITUTION_RULE SET IS_ACTIVE = 1 " + "WHERE SUBSTITUTING_USER = '" + approver
						+ "' AND SUBSTITUTED_USER = '" + requestor + "' "
						+ "AND RULE_ID IN (SELECT DISTINCT SR.RULE_ID FROM SUBSTITUTION_RULE SR "
						+ "INNER JOIN SUBSTITUTION_PROCESSES SP ON SR.RULE_ID = SP.RULE_ID "
						+ "WHERE SR.SUBSTITUTING_USER = '" + approver + "' AND SR.SUBSTITUTED_USER = '" + requestor
						+ "' " + "AND (PROCESS = 'CFAApproverMatrixChangeProcess' OR PROCESS = 'ALL'))");
		updateQry.executeUpdate();

	}
	
	public void updateApprovalRequest(String taskId , int actionType) {
		try{
			//String qString = "update substitution_approval_request set approval_time='" + ServicesUtil.getUTCTime() + "'";
			//this.getSession().createSQLQuery(qString).executeUpdate();
			String ruleId = "";
			String processName = "";
			
			Session session = sessionFactory.openSession();
			Transaction tx  = session.beginTransaction();
			String ruleQuery = "select cav.key , cav.attr_value from task_events te join custom_attr_values  cav on te.process_id  = cav.task_id where te.event_id = '" + taskId + "' and cav.key in(" + TaskCreationConstant.ATTRIBUTE_KEY+ ")";
			List<Object[]> result = session.createSQLQuery(ruleQuery).list();
			
			ruleId = (String)result.get(0)[1];
			processName = (String)result.get(1)[1];
			if(((String)result.get(1)[0]).equalsIgnoreCase(TaskCreationConstant.RULE_KEY)) {
				ruleId = (String)result.get(1)[1];
				processName = (String)result.get(0)[1];
			}

			
			String versionQuery="Select max(version) from substitution_processes where rule_Id='"+ruleId+"'";
			Integer version= (Integer)session.createSQLQuery(versionQuery).list().get(0);
			
			Map<Integer, String> statusMapping = new HashMap<>();
			statusMapping.put(0, "PENDING");
			statusMapping.put(1, "APPROVED");
			statusMapping.put(2, "REJECTED");
			statusMapping.put(3, "CANCELED");
			
			String query = "update substitution_processes set status = " + actionType + " , status_def = '" + statusMapping.get(actionType) + "' , approved_at = '" + ServicesUtil.getIstTime()  + "' where rule_id = '"
					+ ruleId+ "' and version = " + version + " and process = '" + processName
					+ "'";
			session.createSQLQuery(query).executeUpdate();
			tx.commit();
			session.close();
		
		}catch (Exception e) {
			System.err.println("[WBP-Dev]SubstitutionDao.updateApprovalRequest() error"+e);
		}
	}
	

}
