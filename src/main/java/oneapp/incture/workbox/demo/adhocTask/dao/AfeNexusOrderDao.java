package oneapp.incture.workbox.demo.adhocTask.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



@Repository
public class AfeNexusOrderDao {
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}
	
	public String getFilterForNexus() {
		
		String query = "select order_type from AFE_NEXUS_ORDER where process_name = 'AFENexus'";
		return (String) this.getSession().createSQLQuery(query).uniqueResult();
		
		
	}

}
