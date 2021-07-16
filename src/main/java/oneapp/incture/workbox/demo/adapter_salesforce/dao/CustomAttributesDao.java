package oneapp.incture.workbox.demo.adapter_salesforce.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomAttributesDao {

	@Autowired
	SessionFactory sessionFactory;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	
	@SuppressWarnings("unchecked")
	public List<String> getCustomAttributesByProcess(String processName) {
		
		Query customAttributesQuery = this.getSession().createSQLQuery("Select key from CUSTOM_ATTR_TEMPLATE "
				+ "	where PROCESS_NAME = '"+processName+"'");
		return customAttributesQuery.list();
	}

}
