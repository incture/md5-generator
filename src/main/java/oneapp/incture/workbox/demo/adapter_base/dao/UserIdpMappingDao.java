package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("BaseIdpMapping")
public class UserIdpMappingDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}
	public String getUserName(String userId) {
		Query fetchNameQry = this.getSession().createSQLQuery("SELECT USER_FIRST_NAME || ' ' || USER_LAST_NAME "
				+ "FROM USER_IDP_MAPPING WHERE USER_ID = '"+userId+"'");
		String name = (String) fetchNameQry.uniqueResult();
		return name;
	}
	public String getUserRole(String userId) {
		Query fetchNameQry = this.getSession().createSQLQuery("SELECT USER_ROLE "
				+ "FROM USER_IDP_MAPPING WHERE USER_ID = '"+userId+"'");
		String userRole = (String) fetchNameQry.uniqueResult();
		return userRole;
	}
	public String getUserLanguage(String userId) {
		Query fetchLangQry = this.getSession().createSQLQuery("SELECT LANGUAGE "
				+ "FROM USER_IDP_MAPPING WHERE USER_ID = '"+userId+"'");
		String userLang = (String) fetchLangQry.uniqueResult();
		return userLang;
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> getUsers() {
		Map<String, String> userDetails = new HashMap<String, String>();
		Query q = this.getSession().createSQLQuery("SELECT DISTINCT USER_ID,USER_EMAIL FROM USER_IDP_MAPPING");
		List<Object[]> result = q.list();
		
		if(!ServicesUtil.isEmpty(result)){
			for (Object[] obj : result) {
				userDetails.put(ServicesUtil.isEmpty(obj[0])?"":obj[0].toString(),ServicesUtil.isEmpty(obj[1])?"": obj[1].toString());
			}
		}
		return userDetails;
	}
}
