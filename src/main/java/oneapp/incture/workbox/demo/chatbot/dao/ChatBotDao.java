package oneapp.incture.workbox.demo.chatbot.dao;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

/**
 * @author Sandhya
 *
 */
@Repository
//////@Transactional
public class ChatBotDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public String getDetails(String processList, String status, String taskOwner, String timeline) {
		System.err.println("[WBP-Dev][ChatBotDao] [getDetails] invoked ");
		String condition = "";

		if (!ServicesUtil.isEmpty(processList)) {
			condition = "pct.PROCESS_DISPLAY_NAME IN (" + processList + ") AND ";
		}
		if (!ServicesUtil.isEmpty(status)) {
			condition = condition + " te.STATUS IN (" + status + ") AND ";
		}
		if (!ServicesUtil.isEmpty(taskOwner)) {
			condition = condition + " te.cur_proc_disp in (" + taskOwner + ") AND ";
		}
		if (!ServicesUtil.isEmpty(timeline)) {
			condition = condition + timeline + " AND ";
		}

		String queryString = "SELECT count(*) FROM (SELECT DISTINCT request_id, te.event_id "
				+ "FROM TASK_EVENTS AS te LEFT OUTER JOIN PROCESS_CONFIG_TB AS Pct "
				+ "ON TE.PROC_NAME = PCT.PROCESS_NAME LEFT OUTER JOIN TASK_SLA AS ts "
				+ "ON te.NAME = ts.TASK_DEF LEFT OUTER JOIN PROCESS_EVENTS AS pe ON "
				+ "pe.process_id = te.process_id INNER JOIN TASK_OWNERS AS tw " + "ON tw.event_id = te.event_id WHERE "
				+ condition + " te.event_id IS NOT NULL)";

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = this.getSession().createSQLQuery(queryString).list();
		System.err.println("[WBP-Dev][ChatBotDao] [getDetails] query - " + queryString);

		return resultList + "";
	}

}
