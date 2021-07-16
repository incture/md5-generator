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
public class TaskOwnerDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}
	
	public void deleteInTaskOwner(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInTaskOwner = "DELETE FROM TASK_OWNERS WHERE EVENT_ID IN "
				+ "(SELECT DISTINCT EVENT_ID FROM TASK_EVENTS WHERE PROC_NAME IN "
				+ "(SELECT DISTINCT PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName)))";
		Query  deleteInTaskOwnerQuery= session.createSQLQuery(deleteInTaskOwner);
		deleteInTaskOwnerQuery.setParameterList("processName", processName);
		deleteInTaskOwnerQuery.executeUpdate();
		tx.commit();
		session.close();
	}

}
