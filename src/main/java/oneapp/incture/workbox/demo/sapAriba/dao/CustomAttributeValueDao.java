package oneapp.incture.workbox.demo.sapAriba.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("CustomAttributeValueDao")
//@Transactional
public class CustomAttributeValueDao {

	@Autowired
	SessionFactory sessionFactory;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void addCustomAttributeValue(List<CustomAttributeValue> customAttributeValues) {
		if (!ServicesUtil.isEmpty(customAttributeValues) && customAttributeValues.size() > 0) {
			try{
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
			for (CustomAttributeValue customAttributeValue : customAttributeValues) {
				this.getSession().saveOrUpdate(customAttributeValue);
				tx.commit();
				session.close();
			}
			}
			catch(Exception e){
				System.err.println("CustomAttributeDao.addCustomAttributeValue() error : "+e);
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<CustomAttributeValue> getAttributes(String taskId) {
		if (!ServicesUtil.isEmpty(taskId)) {

			Query query = this.getSession().createQuery(
					"select p from CustomAttributeValue p where p.taskId= :taskId");
			query.setParameter("taskId", taskId);
			return query.list();

		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
    public List<String> getCustomAttributesByProcess(String processName) {

 

        Query customAttributesQuery = this.getSession().createSQLQuery(
                "Select key from CUSTOM_ATTR_TEMPLATE " + " where PROCESS_NAME = '" + processName + "'");
        return customAttributesQuery.list();

 

    }
}
