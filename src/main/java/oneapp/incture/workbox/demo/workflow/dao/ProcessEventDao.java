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
public class ProcessEventDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	public void deleteInProcessEvents(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInPE = "DELETE FROM PROCESS_EVENTS WHERE NAME IN "
				+ "(SELECT DISTINCT PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName))";
		Query  deleteInPEQuery= session.createSQLQuery(deleteInPE);
		deleteInPEQuery.setParameterList("processName", processName);
		deleteInPEQuery.executeUpdate();
		tx.commit();
		session.close();
	}
	
}
