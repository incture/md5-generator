package oneapp.incture.workbox.demo.adhocTask.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;


@Repository
public class CrossConstantDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getConstants(String name) {

		Query fetchQry = getSession().createSQLQuery("SELECT CONSTANT_NAME, CONSTANT_VALUE FROM CROSS_CONSTANTS"
				+ " WHERE CONSTANT_ID = '" + name + "' order by case CONSTANT_VALUE when 'None' then 1 else 2 end");
		return fetchQry.list();
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getPPDAvaibaleBudget() {
		Map<String, String> value = new HashMap<>();

		try {
			Query fetchQry = getSession().createSQLQuery(
					"select cost_center, total_amount-approved_budget from (select ccp1.cost_center as cost_center, amount as total_amount,				(select sum(cast(ccp2.amount as int)) from cost_center_ppd ccp2				where ccp2.cost_center=ccp1.cost_center and type <>'Total Budget')				as approved_budget				from cost_center_ppd ccp1 where ccp1.type='Total Budget')");
			List<Object[]> obj = fetchQry.list();
			System.err.println("[getPPDAvaibaleBudget]" + fetchQry);
			if (!ServicesUtil.isEmpty(obj))
				for (Object[] objec : obj) {
					String budget = "";
					if (!ServicesUtil.isEmpty(objec[1])) {
						budget = ((Integer)objec[1]).toString();
					} else {
						budget = "1000000";
					}
					value.put((String) objec[0], budget);
				}
			System.err.println("[getPPDAvaibaleBudget]" + value.toString());
		} catch (Exception e) {
			System.err.println("[eoororo]" + e.getLocalizedMessage());
		}
		return value;
	}

	public String insertCostCenter(String costCenter, String budget, String type) {
		String query = "INSERT INTO COST_CENTER_PPD VALUES(	'" + costCenter + "',	'" + budget + "',	'" + type
				+ "')";
		try {
			Query insertQuery = getSession().createSQLQuery(query);
			insertQuery.executeUpdate();
			return "SUCCESS";
		} catch (Exception e) {
			System.err.println("insertCostCenter error" + e.getMessage());
			return "FAILURE";
		}

	}

	public String updateCostCenter(String costCenter, String budget, String type) {
		String query = "update COST_CENTER_PPD set amount='" + budget + "' where type='" + type + "' and cost_center='"
				+ costCenter + "'";
		try {
			Query insertQuery = getSession().createSQLQuery(query);
			int count = insertQuery.executeUpdate();
			if(count > 0)
				return "SUCCESS";
			else 
				return "FAILURE";
		} catch (Exception e) {
			System.err.println("updateCostCenter error" + e.getMessage());
			return "FAILURE";
		}

	}

}
