package oneapp.incture.workbox.demo.workflow.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("TaskEventsDao_Workflow")
public class TaskEventDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	public void deleteIntaskEvents(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInTE = "DELETE FROM TASK_EVENTS WHERE PROC_NAME IN "
				+ "(SELECT DISTINCT PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName))";
		Query  deleteInTEQuery = session.createSQLQuery(deleteInTE);
		deleteInTEQuery.setParameterList("processName", processName);
		deleteInTEQuery.executeUpdate();
		tx.commit();
		session.close();
	}
	
}
