package oneapp.incture.workbox.demo.chat.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("chatTaskEvents")
public class TaskEventsDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	public String getTaskName(String eventId) {
		String fetchTaskName = "SELECT PE.REQUEST_ID || ' : ' || TE.DESCRIPTION AS TASK_NAME FROM PROCESS_EVENTS PE "
				+ " INNER JOIN TASK_EVENTS TE ON TE.PROCESS_ID = PE.PROCESS_ID  WHERE TE.EVENT_ID = '"+eventId+"'";
		Query fetchQry = getSession().createSQLQuery(fetchTaskName);
		String taskName = (String)fetchQry.uniqueResult();
		return taskName;
	}

	public String getTaskOwner(String eventId) {
		String fetchTaskOwner = "SELECT PE.STARTED_BY FROM PROCESS_EVENTS PE "
				+ "WHERE PE.PROCESS_ID = (SELECT PROCESS_ID FROM TASK_EVENTS WHERE EVENT_ID = '"+eventId+"')";
		Query fetchQry = getSession().createSQLQuery(fetchTaskOwner);
		String taskOwner = (String)fetchQry.uniqueResult();
		return taskOwner;
	}
}
