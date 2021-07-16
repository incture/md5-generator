package oneapp.incture.workbox.demo.emailTemplate.Dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.emailTemplate.Dto.CustomAttributeKeys;

@Repository
public class CrossConstantsDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public List<CustomAttributeKeys> getUserKeys() {
		List<CustomAttributeKeys> customAttributeKeys = new ArrayList<>();
		CustomAttributeKeys attributeKeys = null;
		Query query = this.getSession().createSQLQuery(
				"SELECT CONSTANT_VALUE,CONSTANT_NAME FROM CROSS_CONSTANTS " + "WHERE CONSTANT_ID = 'recipient'");
		List<Object[]> result = query.list();

		if (!ServicesUtil.isEmpty(result)) {
			for (Object[] obj : result) {
				attributeKeys = new CustomAttributeKeys();
				attributeKeys.setKey(obj[0].toString());
				attributeKeys.setLabel(obj[1].toString());
				customAttributeKeys.add(attributeKeys);
			}
		}
		return customAttributeKeys;
	}

}
