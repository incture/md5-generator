package oneapp.incture.workbox.demo.adapter_salesforce.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TaskEventDao {

	@Autowired
	SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<String> getprocessIdByOrigin(String origin) {

		Query query = this.getSession().createSQLQuery("select distinct process_id from Task_Events where origin='"+origin+"'"
				+ " and proc_name = 'Campaign'");
		return query.list();
	}

	public String getProcessIdUsingTaskId(String eventId) {

		Query query = this.getSession().createSQLQuery("select process_id from Task_Events where event_id =:eventId");
		query.setParameter("eventId", eventId);
		String processId = (String) query.uniqueResult();
		return processId;

	}

}
