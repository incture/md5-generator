package oneapp.incture.workbox.demo.emailTemplate.Dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttributeValues;

@Repository("emailCustomAttrValueDao")
public class CustomAttrValueDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public Map<String, AttributeValues> getCustAttr(String eventId) {
//		List<CustomAttributeValue> attributeValues = new ArrayList<>();
//		Map<String,String> attrValuesMap = new HashMap<String, String>();
		Map<String, AttributeValues> attrValueMap = new HashMap<>();
		AttributeValues attrValue = null;
		Session session = sessionFactory.openSession();
		Query q = session.createSQLQuery(
				"SELECT CAV.ATTR_VALUE,CAV.KEY,CAT.DATA_TYPE,CAT.IS_RUN_TIME FROM CUSTOM_ATTR_VALUES CAV "
						+ "INNER JOIN CUSTOM_ATTR_TEMPLATE CAT ON CAT.KEY = CAV.KEY " + "WHERE TASK_ID = '" + eventId
						+ "'");
		List<Object[]> attrValues = q.list();
		if (!ServicesUtil.isEmpty(attrValues)) {
			for (Object[] attr : attrValues) {
				// attrValuesMap.put(attr[1].toString(), attr[0].toString());
				if (!ServicesUtil.isEmpty(attr[3]) && ((Short) attr[3] == 1)) {
					attrValue = new AttributeValues();
					String attrKey = ServicesUtil.isEmpty(attr[0]) ? "" : attr[0].toString();
					if (!ServicesUtil.isEmpty(attrKey)) {
						List<String> ids = Arrays.asList(attrKey.split(","));// attrKey.split(,);
						for (String id : ids) {
							Query query = session.createSQLQuery(
									"select user_first_name,user_last_name from user_idp_mapping where user_login_name='"
											+ id + "'");
							System.err.println(query);
							List<Object[]> name = query.list();
							String fName=ServicesUtil.isEmpty(name) ? "": name.get(0)[0].toString();
							String lName=ServicesUtil.isEmpty(name) ? "": name.get(0)[1].toString();
							if (ServicesUtil.isEmpty(attrValue.getAttributeValue())) {
								attrValue.setAttributeValue(ServicesUtil.isEmpty(attr[0]) ? "": (fName+" "+lName));
							} else {
								attrValue.setAttributeValue(attrValue.getAttributeValue() + ","
										+ (ServicesUtil.isEmpty(attr[0]) ? "":(fName+" "+lName)));
							}
							attrValue.setDataType(ServicesUtil.isEmpty(attr[2]) ? "" : attr[2].toString());
							attrValueMap.put(ServicesUtil.isEmpty(attr[1]) ? "" : attr[1].toString(), attrValue);
						}

					}
				} else {
					attrValue = new AttributeValues();
					attrValue.setAttributeValue(ServicesUtil.isEmpty(attr[0]) ? "" : attr[0].toString());
					attrValue.setDataType(ServicesUtil.isEmpty(attr[2]) ? "" : attr[2].toString());
					attrValueMap.put(ServicesUtil.isEmpty(attr[1]) ? "" : attr[1].toString(), attrValue);
				}

			}
		}
		return attrValueMap;
	}

}
