package oneapp.incture.workbox.demo.workflow.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TaskValueDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}
	
	public void deleteInTaskValue(String processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInTV = "DELETE FROM TASK_TEMPLATE_VALUE WHERE PROCESS_ID IN "
				+ "(SELECT DISTINCT PROCESS_ID FROM PROCESS_EVENTS WHERE NAME IN "
				+ "(SELECT DISTINCT PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName)))";
		Query  deleteInTVQuery= session.createSQLQuery(deleteInTV);
		deleteInTVQuery.setParameter("processName", processName);
		deleteInTVQuery.executeUpdate();
		tx.commit();
		session.close();
		
	}

}
