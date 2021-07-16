package oneapp.incture.workbox.demo.notification.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class IDPMappingDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getUserMails(Set<String> userIds) {
		StringBuilder userId = new StringBuilder("");
		for (String id : userIds) {
			userId.append("'");
			userId.append(id);
			userId.append("'");
			userId.append(",");
		}
		Query fetchMailIdsQuery = this.getSession().createSQLQuery("SELECT USER_ID,USER_EMAIL FROM USER_IDP_MAPPING WHERE USER_ID"
				+ " IN ("+userId.substring(0, userId.length()-1)+")");
		List<Object[]> mailIds = fetchMailIdsQuery.list();
		return mailIds;
	}
	
	public String getUserName(String userId) {
		Query fetchUserNameQuery = this.getSession().createSQLQuery("SELECT USER_FIRST_NAME,USER_LAST_NAME FROM USER_IDP_MAPPING WHERE USER_ID='" + userId + "'");
		Object[] res = (Object[]) fetchUserNameQuery.uniqueResult();
		
		String name = res[0].toString() + " " + res[1].toString();
		return name;
	}

	
}
