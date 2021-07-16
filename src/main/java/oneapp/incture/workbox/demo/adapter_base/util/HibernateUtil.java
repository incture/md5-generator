package oneapp.incture.workbox.demo.adapter_base.util;

import java.sql.Connection;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtil {

	@Autowired
	private SessionFactory sessionFactory;

	private static Session session;

	private static int sessionCount;

	@PostConstruct
	public static void setSessionCount() {
		HibernateUtil.sessionCount = 0;
	}

	public synchronized Session getSession() {
		if (session == null || sessionFactory.isClosed()) {
			session = sessionFactory.openSession();
		}
		return session;
	}

	public int getSessionCount() {
		return sessionCount;
	}

	public Statistics getStatistics() {
		return sessionFactory.getStatistics();
	}

	public static String closeSession() {
		Connection close = null;
		if (session != null) {
			session.close();
			if (close == null) {
				session = null;
			}
		}
		return "Success";
	}

}
