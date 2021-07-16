package oneapp.incture.workbox.demo.workflow.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomAttributeValueDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	public void deleteInAttrValue(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInCAV = "DELETE FROM CUSTOM_ATTR_VALUES WHERE TASK_ID IN "
				+ "(SELECT EVENT_ID FROM TASK_EVENTS WHERE PROC_NAME IN "
				+ "(SELECT DISTINCT PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName)))";
		Query deleteInCAVQuery = session.createSQLQuery(deleteInCAV);
		deleteInCAVQuery.setParameterList("processName", processName);
		deleteInCAVQuery.executeUpdate();
		tx.commit();
		session.close();
	}

}
