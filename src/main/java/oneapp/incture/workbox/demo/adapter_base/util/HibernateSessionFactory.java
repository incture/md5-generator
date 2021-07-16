package oneapp.incture.workbox.demo.adapter_base.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateSessionFactory {

	@Autowired
	private SessionFactory sessionFactory;
	
	public synchronized Session getCurrentOpenSession() {
		Session session = sessionFactory.getCurrentSession();
		System.err.println("[WBP-Dev][HibernateSessionFactory][Session] : "+session);
		return session;
	}
	
}
