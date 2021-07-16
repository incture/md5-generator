package oneapp.incture.workbox.demo.chat.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("chatIDPMapping")
public class UserIDPMappingDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	public String getOwnerDetailById(String userId) {

		Query userQry = getSession().createSQLQuery("SELECT USER_FIRST_NAME||' '||USER_LAST_NAME"
				+ " FROM USER_IDP_MAPPING "
				+ "WHERE USER_ID = '"+userId+"'");
		
		String userName = (String) userQry.uniqueResult();
		return userName;

	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getOwnerDetailsById(String userId) {

		Query userQry = getSession().createSQLQuery("SELECT USER_FIRST_NAME||' '||USER_LAST_NAME"
				+ ",USER_FIRST_NAME,USER_LAST_NAME,USER_EMAIL FROM USER_IDP_MAPPING"
				+ " WHERE USER_ID = '"+userId+"'");
		
		List<Object[]> userName = userQry.list();
		return userName;

	}


}
