package oneapp.incture.workbox.demo.adapter_base.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValueTableAdhocDo;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("CustomAttributeDao")

//////@Transactional
public class CustomAttributeDao {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	TaskEventsDao taskEvents;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	// add CustomAttribute Templates and Values
	public void addCustomAttributeTemp(CustomAttributeTemplate customAttributeTemplate) {
		if (!ServicesUtil.isEmpty(customAttributeTemplate)) {

			if (ServicesUtil.isEmpty(customAttributeTemplate.getIsActive()))
				customAttributeTemplate.setIsActive(true);

			System.err.println("[WBP-Dev]CustomAttributeDao:addCustomAttributeTemp:" + customAttributeTemplate);
			this.getSession().saveOrUpdate(customAttributeTemplate);
		}
	}

	public void addCustomAttributeTemp(List<CustomAttributeTemplate> customAttributeTemplates) {
		if (!ServicesUtil.isEmpty(customAttributeTemplates) && customAttributeTemplates.size() > 0) {
			for (CustomAttributeTemplate customAttributeTemp : customAttributeTemplates) {

				if (ServicesUtil.isEmpty(customAttributeTemp.getIsActive()))
					customAttributeTemp.setIsActive(true);

				// System.err.println("[WBP-Dev]CustomAttributeDao.addCustomAttributeTemp()
				// "+customAttributeTemp);
				this.getSession().saveOrUpdate(customAttributeTemp);
			}
		}
	}

	public void addCustomAttributeValue(CustomAttributeValue customAttributeValue) {
		if (!ServicesUtil.isEmpty(customAttributeValue)) {
			this.getSession().saveOrUpdate(customAttributeValue);
		}
	}

	public void addCustomAttributeForAdhoc(List<CustomAttributeValueDto> customAttributeValues) {
		List<CustomAttributeValue> valueList = new ArrayList<>();
		List<CustomAttributeValueTableAdhocDo> tableList = new ArrayList<>();

		for (CustomAttributeValueDto customAttributeValue : customAttributeValues) {

			if (!ServicesUtil.isEmpty(customAttributeValue)) {
				if (customAttributeValue.getRowNumber() == 0) {
					CustomAttributeValue attributeValue = new CustomAttributeValue();
					attributeValue.setAttributeValue(customAttributeValue.getAttributeValue());
					attributeValue.setKey(customAttributeValue.getKey());
					attributeValue.setProcessName(customAttributeValue.getProcessName());
					attributeValue.setTaskId(customAttributeValue.getTaskId());
					valueList.add(attributeValue);

				} else {
					CustomAttributeValueTableAdhocDo attributeValue = new CustomAttributeValueTableAdhocDo();
					attributeValue.setAttributeValue(customAttributeValue.getAttributeValue());
					attributeValue.setKey(customAttributeValue.getKey());
					attributeValue.setProcessName(customAttributeValue.getProcessName());
					attributeValue.setTaskId(customAttributeValue.getTaskId());
					attributeValue.setRowNumber(customAttributeValue.getRowNumber());
					attributeValue.setDependantOn(customAttributeValue.getDependantOn());
					tableList.add(attributeValue);

				}
			}

		}

		if (valueList.size() > 0) {
			addCustomAttributeValue(valueList);
		}
		if (tableList.size() > 0) {
			addCustomAttributeValueTable(tableList);

		}

	}

	//	////@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addCustomAttributeValue(List<CustomAttributeValue> customAttributeValues) {
		if (!ServicesUtil.isEmpty(customAttributeValues) && customAttributeValues.size() > 0) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for (int i = 0; i < customAttributeValues.size(); i++) {
				CustomAttributeValue currentTask = customAttributeValues.get(i);
				session.saveOrUpdate(currentTask);
				if (i % 20 == 0 && i > 0) {
					session.flush();
					session.clear();
				}
			}
			session.flush();
			session.clear();
			tx.commit();
			session.close();
			/*
			 * if(!session.getTransaction().wasCommitted()) { session.flush(); }
			 */
			// session.close();
		}
	}

	//	////@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void addCustomAttributeValueTable(List<CustomAttributeValueTableAdhocDo> customAttributeValues) {
		Session session = this.getSession();

		if (!ServicesUtil.isEmpty(customAttributeValues) && customAttributeValues.size() > 0) {

			try {
				for (CustomAttributeValueTableAdhocDo customAttributeValue : customAttributeValues) {
					session.saveOrUpdate(customAttributeValue);
					session.flush();

				}
			} catch (Exception e) {
				System.err.println("[WBP-Dev]CustomAttributeDao.addCustomAttributeValueTable() error : " + e);
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public List<CustomAttributeValue> getCustomAttributes(String taskId) {
		if (!ServicesUtil.isEmpty(taskId)) {

			Query query = this.getSession().createSQLQuery(
					"select cv.PROCESS_NAME,cv.KEY, cv.ATTR_VALUE, ct.LABEL , ct.SEQUENCE from CUSTOM_ATTR_VALUES cv, "
							+ "CUSTOM_ATTR_TEMPLATE ct where cv.PROCESS_NAME = ct.PROCESS_NAME and cv.KEY=ct.KEY and "
							+ "(cv.TASK_ID = '" + taskId + "' or (cv.TASK_ID in "
							+ "(select process_id from task_events where event_id = '" + taskId + "' and origin = '"
							+ PMCConstant.ADHOC + "'))) ORDER BY CASE WHEN SEQUENCE IS NOT NULL THEN SEQUENCE END , CASE WHEN SEQUENCE IS NULL THEN LABEL END ");
			
			System.err.println("Custom attribute query : " + query.getQueryString());
			return convertToCVList(query.list());

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public boolean getCustomAttributesWithConditionNew(String taskId, String processId, String Condition, String key) {
		if (!ServicesUtil.isEmpty(taskId)) {

			Query query = this.getSession().createSQLQuery(
					"select cv.PROCESS_NAME,cv.KEY, cv.ATTR_VALUE, ct.LABEL from CUSTOM_ATTR_VALUES cv, CUSTOM_ATTR_TEMPLATE ct where cv.PROCESS_NAME = ct.PROCESS_NAME and cv.KEY=ct.KEY and (cv.TASK_ID in ('"
							+ taskId + "','" + processId
							+ "') OR cv.TASK_ID in (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '" + processId
							+ "'))" + " and cv.KEY='" + key + "' and cv.attr_value " + Condition);
			System.err.println("CustomAttributeDao.getCustomAttributesWithCondition()" + query);

			Query query2 = this.getSession().createSQLQuery(
					"select cv.PROCESS_NAME,cv.KEY, cv.ATTR_VALUE, ct.LABEL from CUSTOM_ATTR_VALUES_TABLE cv, CUSTOM_ATTR_TEMPLATE ct where cv.PROCESS_NAME = ct.PROCESS_NAME and cv.KEY=ct.KEY and (cv.TASK_ID in ('"
							+ taskId + "','" + processId
							+ "') OR cv.TASK_ID in (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '" + processId
							+ "'))" + " and cv.KEY='" + key + "' and cv.attr_value " + Condition);
			System.err.println("CustomAttributeDao.getCustomAttributesWithCondition()" + query);


			if (!ServicesUtil.isEmpty(query.list()) || !ServicesUtil.isEmpty(query2.list()))
				return true;

		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean getCustomAttributesWithCondition(String taskId, String processId, String Condition, String key) {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		if (!ServicesUtil.isEmpty(taskId)) {
			String key1 = null;
			String[] key2 = null;
			if(key.contains("&&"))
			{
				key2 = key.split("&&");
				key = key2[1];
				key1 = key2[0];
			}
			key = key.trim().replace("'", "");
			key = key.replace("(", "");
			key = key.replace(")", "");
			String[] s1 = key.split("[^A-za-z0-9]");

			String expQueryStr =  "select cv.PROCESS_NAME,cv.KEY, cv.ATTR_VALUE, ct.LABEL, ct.DATA_TYPE";

			if(s1.length == 1){
				expQueryStr += " from CUSTOM_ATTR_VALUES cv, CUSTOM_ATTR_TEMPLATE ct where"
						+ " cv.PROCESS_NAME = ct.PROCESS_NAME and cv.KEY=ct.KEY and (cv.TASK_ID in ('"
						+ taskId + "','" + processId
						+ "') OR cv.TASK_ID in (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '" + processId
						+ "'))";
				expQueryStr	+= " and cv.KEY='" + key + "'";

				expQueryStr += " and cv.attr_value "+Condition.substring(0,Condition.indexOf("'")).trim()+""
						+ "(case when ct.data_type = 'DATETYPE' then "
						+ " cast(current_date as varchar) "
						+ "else '"+Condition.substring(Condition.indexOf("'")+1, Condition.lastIndexOf("'"))+"' end)" ;
			}else{

				expQueryStr += ",(select attr_value from CUSTOM_ATTR_VALUES where "
						+ "task_id = '"+processId+"' and key = '"+s1[1]+"') as date2";

				expQueryStr += ",(select attr_value from CUSTOM_ATTR_VALUES where "
						+ "task_id = '"+processId+"' and key = 'c23j5i762gd3') as isTriggred";

				expQueryStr += " from CUSTOM_ATTR_VALUES cv, CUSTOM_ATTR_TEMPLATE ct where"
						+ " cv.PROCESS_NAME = ct.PROCESS_NAME and cv.KEY=ct.KEY and (cv.TASK_ID in ('"
						+ taskId + "','" + processId
						+ "') OR cv.TASK_ID in (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '" + processId
						+ "'))";
				expQueryStr	+= " and cv.KEY='" + s1[0] + "'";

			}

			Query query = this.getSession().createSQLQuery(expQueryStr);
			System.err.println("CustomAttributeDao.getCustomAttributesWithCondition()" + query);

			List<Object[]> caValues = query.list();

			String expStr2 = "select cv.PROCESS_NAME,cv.KEY, cv.ATTR_VALUE, ct.LABEL, ct.DATA_TYPE ";

			if(s1.length == 1){
				expStr2	+=	" from CUSTOM_ATTR_VALUES_TABLE cv, CUSTOM_ATTR_TEMPLATE ct where cv.PROCESS_NAME = ct.PROCESS_NAME and cv.KEY=ct.KEY and (cv.TASK_ID in ('"
						+ taskId + "','" + processId
						+ "') OR cv.TASK_ID in (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '" + processId
						+ "'))";

				expStr2	+= " and cv.KEY='" + key + "'";

				expStr2 += " and cv.attr_value "+Condition.substring(0,Condition.indexOf("'")).trim()+""
						+ "(case when ct.data_type = 'DATETYPE' then "
						+ " cast(current_date as varchar) "
						+ "else '"+Condition.substring(Condition.indexOf("'")+1, Condition.lastIndexOf("'"))+"' end)" ;
			}else{
				expStr2 += ",(select attr_value from CUSTOM_ATTR_VALUES_TABLE where "
						+ "task_id = '"+taskId+"' and key = '"+s1[1]+"' and row_number = cv.row_number) as date2";

				expStr2 += ",(select attr_value from CUSTOM_ATTR_VALUES where "
						+ "task_id = '"+processId+"' and key = 'c23j5i762gd3') as isTriggred";

				expStr2 += " from CUSTOM_ATTR_VALUES_TABLE cv, CUSTOM_ATTR_TEMPLATE ct where"
						+ " cv.PROCESS_NAME = ct.PROCESS_NAME and cv.KEY=ct.KEY and (cv.TASK_ID in ('"
						+ taskId + "','" + processId
						+ "') OR cv.TASK_ID in (SELECT EVENT_ID FROM TASK_EVENTS WHERE PROCESS_ID = '" + processId
						+ "'))";
				expStr2	+= " and cv.KEY='" + s1[0] + "'";
			}
			Query query2 = this.getSession().createSQLQuery(expStr2);

			System.err.println("CustomAttributeDao.getCustomAttributesWithCondition()" + query);

			List<Object[]> caValues2 = query2.list();
			if(!caValues2.isEmpty())
				caValues.addAll(caValues2);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			for (Object[] obj : caValues) {

				String value = obj[2].toString();
				if("DATETYPE".equals(obj[4])){
					if(s1.length == 1){
						if(!ServicesUtil.isEmpty(caValues2) || !ServicesUtil.isEmpty(caValues))
							return true;
					}else if(!ServicesUtil.isEmpty(obj[5].toString())){
						try{
							Date date1 = format.parse(value);
							Date date2 = format.parse(obj[5].toString());
							if(key.contains("-")){
								if(Condition.contains("Months")){
									Calendar cal = Calendar.getInstance(); 
									cal.setTime(date1);
									Integer days = Integer.parseInt(Condition.replace("Months", "").replace(">", "").replace("<", "").trim());
									cal.add(Calendar.MONTH, days);
									System.err.println(cal.getTime());
									if(Condition.contains(">"))
										if(date2.after(cal.getTime()))
											return true;
									if(Condition.contains("<"))
										if(date2.before(cal.getTime()))
											return true;
								}else if(Condition.contains("Days")){
									Integer days = Integer.parseInt(Condition.replace("Days", "").replace("<", "").replace(">", "").trim());
									Calendar cal = Calendar.getInstance(); 
									cal.setTime(date1);
									cal.add(Calendar.DATE, days);
									System.err.println(cal.getTime());
									String isTriggered = ServicesUtil.isEmpty(obj[6])?"":obj[6].toString();
									if(key.contains("579h2g41d202d") && isTriggered.equalsIgnoreCase("false"))
										return false;
									if(key.contains("579h2g41d202d") && isTriggered.equalsIgnoreCase("true")){
										if(Condition.contains("<"))
											if(date2.before(cal.getTime()))
												return true;
										if(Condition.contains(">"))
											if(date2.after(cal.getTime()))
												return true;
									}else{

										if(Condition.contains("<"))
											if(date2.before(cal.getTime()))
												return true;
										if(Condition.contains(">"))
											if(date2.after(cal.getTime()))
												return true;
									}
								}

							}
						}catch (Exception e) {
							System.err.println("Date Exception"+e);
						}
					}
				}
				else if(value.matches(".*[a-zA-Z]+.*")){
					if(!ServicesUtil.isEmpty(key2))
					{
						if(key2.length > 1){
							if(value.equalsIgnoreCase(Condition.replaceAll("[^a-zA-Z0-9]", "")))
							{
								key1 = key1.replace(") ", ")$");
								System.err.println(key1);
								key2 = key1.split("[$]");

								if(getCustomAttributesWithCondition(taskId, processId, key2[1].trim(),key2[0].trim()))
									return true;
								else
									return false;
							}
						}

					}else if(value.equalsIgnoreCase(Condition.replaceAll("[^a-zA-Z0-9]", "")))
						return true;
				}else{
					try {
						String foo = value+Condition;
						/*String res = engine.eval(foo).toString();
						if(res.equalsIgnoreCase("true"))
							return true;*/
						if((boolean) engine.eval(foo))
							return true;
					} catch (Exception e) {
						System.err.println("[WBP-Dev]Calculating Expression Failed" + e.getMessage());
					}
				}
			}

		}
		return false;
	}

	//	public static void main(String args[]){
	//		String key = "('j1gfa9dbc56d'-'bdja6jif8a16') > 15 Days && 7379fi87gj54";
	//		String condition = "='Soft'";
	//		String key1 = null;
	//		String[] key2 = null;
	//		if(key.contains("&&"))
	//		{
	//			key2 = key.split("&&");
	//			key = key2[1];
	//			key1 = key2[0];
	//		}
	//		System.err.println(key);
	//		key = key.replace("'", "");
	//		key = key.replace("(", "");
	//		String[] s1 = key.split("[^A-za-z0-9]");
	//
	//		System.err.println(s1);
	//		System.err.println(key1);
	//
	//		if(key2.length > 1){
	//			key1 = key1.replace(") ", ")$");
	//			System.err.println(key1);
	//			key2 = key1.split("[$]");
	//
	//			System.err.println(key2[1].trim());
	//			System.err.println(key2[0].trim());
	//		}
	//	}

	@SuppressWarnings("unchecked")
	public Map<String, List<CustomAttributeValue>> getCustomAttributesAsMap(String processList) {

		Query query = this.getSession().createSQLQuery(
				"select cv.task_id,cv.PROCESS_NAME,cv.KEY, cv.ATTR_VALUE, ct.LABEL from CUSTOM_ATTR_VALUES cv, CUSTOM_ATTR_TEMPLATE ct where cv.PROCESS_NAME = ct.PROCESS_NAME and cv.KEY=ct.KEY and UPPER(cv.process_name) in ("
						+ processList + ")");

		return convertToCVMap(query.list());

	}

	private List<CustomAttributeValue> convertToCVList(List<Object[]> list) {

		List<CustomAttributeValue> customAttributeValues = null;
		CustomAttributeValue customAttributeValue = null;
		if (!ServicesUtil.isEmpty(list) && list.size() > 0) {
			customAttributeValues = new ArrayList<CustomAttributeValue>();
			for (Object[] object : list) {
				customAttributeValue = new CustomAttributeValue();
				customAttributeValue.setProcessName((String) object[0]);
				customAttributeValue.setKey((String) object[1]);
				customAttributeValue.setAttributeValue((String) object[2]);
				customAttributeValue.setAttributeTemplate((String) object[3]);

				customAttributeValues.add(customAttributeValue);
			}
		}
		return customAttributeValues;
	}

	private Map<String, List<CustomAttributeValue>> convertToCVMap(List<Object[]> list) {
		Map<String, List<CustomAttributeValue>> customMap = new HashMap();
		List<CustomAttributeValue> customAttributeValues = null;
		CustomAttributeValue customAttributeValue = new CustomAttributeValue();
		if (!ServicesUtil.isEmpty(list) && list.size() > 0) {

			for (Object[] object : list) {
				customAttributeValue = new CustomAttributeValue();
				customAttributeValue.setTaskId((String) object[0]);
				customAttributeValue.setProcessName((String) object[1]);
				customAttributeValue.setKey((String) object[2]);
				customAttributeValue.setAttributeValue((String) object[3]);
				customAttributeValue.setAttributeTemplate((String) object[4]);
				if (customMap.containsKey(customAttributeValue.getTaskId())) {
					customMap.get(customAttributeValue.getTaskId()).add(customAttributeValue);
				} else {
					customAttributeValues = new ArrayList<>();
					customAttributeValues.add(customAttributeValue);
					customMap.put(customAttributeValue.getTaskId(), customAttributeValues);
				}

			}
		}
		return customMap;

	}

	@SuppressWarnings("unchecked")
	public List<String> getKeysFromTemplate(String processName) {
		Criteria criteria = this.getSession().createCriteria(CustomAttributeTemplate.class);
		if (!ServicesUtil.isEmpty(processName)) {
			criteria.add(Restrictions.eq("processName", processName));
			criteria.setProjection(Projections.property("key"));

			return criteria.list();
		}

		return null;

	}

	@SuppressWarnings("unchecked")
	public List<String> getActiveKeysFromTemplate(String processName) {
		Criteria criteria = this.getSession().createCriteria(CustomAttributeTemplate.class);
		criteria.add(Restrictions.eq("isActive", true));
		criteria.setProjection(Projections.property("key"));
		criteria.add(Restrictions.ne("dataType", "BUTTON").ignoreCase());

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public List<CustomAttributeTemplate> getCustomAttributeTemplates(String processName, String key, Boolean isActive) {
		if (!ServicesUtil.isEmpty(processName)) {
			Criteria criteria = this.getSession().createCriteria(CustomAttributeTemplate.class);
			if (!ServicesUtil.isEmpty(isActive)) {
				criteria.add(Restrictions.eq("isActive", isActive));
			}
			criteria.add(Restrictions.eq("processName", processName));
			if (!ServicesUtil.isEmpty(key))
				criteria.add(Restrictions.eq("key", key));
			criteria.add(Restrictions.ne("dataType", "BUTTON").ignoreCase());
			System.err.println("[WBP-Dev]UserCustomHeadersDao.getUserDefinedCustomAttributes() criteria : "+criteria.list());
			return criteria.list();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAutoCompleteValues(String processName, String key, String attributeValue) {
		if (ServicesUtil.isEmpty(processName) || ServicesUtil.isEmpty(attributeValue))
			return null;
		Criteria criteria = this.getSession().createCriteria(CustomAttributeValue.class);
		if (!ServicesUtil.isEmpty(processName))
			criteria.add(Restrictions.eq("processName", processName));
		if (!ServicesUtil.isEmpty(key))
			criteria.add(Restrictions.eq("key", key));
		if (!ServicesUtil.isEmpty(attributeValue))
			criteria.add(Restrictions.ilike("attributeValue", attributeValue, MatchMode.ANYWHERE));
		else
			criteria.add(Restrictions.ne("attributeValue", ""));
		criteria.setProjection(Projections.distinct(Projections.property("attributeValue")));
		criteria.addOrder(Order.asc("attributeValue"));
		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public List<String> getCustomAttributesForAutoComplete(String processName, String key, String attributeValue) {
		String queryString = "";
		if (!ServicesUtil.isEmpty(attributeValue)) {
			queryString = "select distinct ATTR_VALUE from CUSTOM_ATTR_VALUES where PROCESS_NAME='" + processName
					+ "' and key = '" + key + "' and UPPER(attr_value) like UPPER('%" + attributeValue
					+ "%') and ATTR_VALUE is not null";
		} else
			queryString = "select distinct ATTR_VALUE from CUSTOM_ATTR_VALUES where PROCESS_NAME='" + processName
			+ "' and key = '" + key + "' and ATTR_VALUE is not null";

		Query query = this.getSession().createSQLQuery(queryString);

		return query.list();
	}

	@SuppressWarnings("unused")
	private String getOrigin(String processName) {
		/*
		 * switch (processName) { case "testworkflow": case
		 * "purchaseorderprocess": case "leaveapprovalprocess": return "SCP";
		 * case "61": case "141": return "SuccessFactors"; default: return
		 * "SCP"; }
		 */
		return "SCP";
	}

	public static void main2(String[] args) {

		String s = "[{\"unit\":\"unit\",\"quantity\":2,\"price\":2000,\"currency\":\"INR\",\"itemDescription\":\"description\"},{\"unit\":\"unit\",\"quantity\":2,\"price\":1000,\"currency\":\"INR\",\"itemDescription\":\"description\"}]";

		int i = 0;
		JSONArray jsonArray = new JSONArray(s);
		for (Object obj : jsonArray) {
			System.err.println("[WBP-Dev]---------------------------------" + (++i));
			if (obj instanceof JSONObject) {
				JSONObject jobj = (JSONObject) obj;
				String[] keys = JSONObject.getNames(jobj);
				for (String key : keys) {
					System.err.println(key + " : " + jobj.get(key));
				}
			}
		}
	}

	public ResponseMessage saveOrUpdateCustomAttributes(List<CustomAttributeTemplate> customAttributeTemplates) {
		ResponseMessage response = new ResponseMessage();
		Session session=null;
		try {
			if (ServicesUtil.isEmpty(customAttributeTemplates)) {
				response.setMessage("Custom Attribute Template Dto Cannot Be Null");
				response.setStatus(PMCConstant.FAILURE);
				response.setStatusCode(PMCConstant.CODE_FAILURE);
			} else {
			    session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (CustomAttributeTemplate obj : customAttributeTemplates) {
					session.saveOrUpdate(obj);
				}
				session.flush();
				session.clear();
				tx.commit();
				session.close();
				response.setMessage("Custom Attributes Updated Successfully");
				response.setStatus(PMCConstant.SUCCESS);
				response.setStatusCode(PMCConstant.CODE_SUCCESS);
			}

		} catch (Exception e) {
			System.err.println("CustomAttributeDao.saveOrUpdateCustomAttributes() exception : " + e);
			response.setMessage("Internal Server Error");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public List<CustomAttributeTemplate> getAllAttributesHeader() {
		List<CustomAttributeTemplate> list = new ArrayList<CustomAttributeTemplate>();
		String query = "";
		try {
			query = "SELECT PROCESS_NAME,KEY,LABEL,IS_ACTIVE,DATA_TYPE,IS_EDITABLE,IS_MAND FROM CUSTOM_ATTR_TEMPLATE ";
			List<Object[]> objectList = this.getSession().createSQLQuery(query).list();

			if (!ServicesUtil.isEmpty(objectList)) {

				for (Object[] obj : objectList) {
					CustomAttributeTemplate dto = new CustomAttributeTemplate();
					dto.setProcessName(obj != null ? obj[0].toString() : null);
					dto.setKey(obj[1] != null ? obj[1].toString() : null);
					dto.setLabel(obj[2] != null ? obj[2].toString() : null);
					dto.setIsActive(obj[3].toString().equalsIgnoreCase("1") ? true : false);
					dto.setDataType(obj[4] != null ? obj[4].toString() : null);
					dto.setIsEditable(obj[5] != null ? (obj[5].toString().equalsIgnoreCase("1") ? true : false) : null);
					dto.setIsMandatory(
							obj[6] != null ? (obj[6].toString().equalsIgnoreCase("1") ? true : false) : null);
					list.add(dto);
				}
			}
		} catch (Exception e) {
			System.err.println("CustomAttributeDao.getAllAttributesHeader() exception : " + e);
		}
		System.err.println("CustomAttributeDao.getAllAttributesHeader() list : " + list);
		return list;
	}

	public void updateCustomAttributes(List<CustomAttributeValue> customAttributes) {
		try {

			for (CustomAttributeValue customAttributeValue : customAttributes) {

				Query q = this.getSession().createSQLQuery("select distinct pe.request_id from process_events pe inner join task_events te on te.process_id = pe.process_id "
						+ "where te.event_id = '"+customAttributeValue.getTaskId()+"'");
				String requestId = (String) q.uniqueResult();

				String updateString = "UPDATE CUSTOM_ATTR_VALUES SET ATTR_VALUE = :val WHERE KEY = :key AND TASK_ID"
						+ " IN (select distinct te.event_id from task_events te "
						+ "inner join process_events pe on te.process_id = pe.process_id "
						+ "where pe.request_id = '"+requestId+"')";
				Query updateQry = this.getSession().createSQLQuery(updateString);
				updateQry.setParameter("val", customAttributeValue.getAttributeValue());
				updateQry.setParameter("key", customAttributeValue.getKey());
				//			updateQry.setParameter("eventId", customAttributeValue.getTaskId());
				updateQry.executeUpdate();
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev] Error updating custom Attributes" + e);
		}

	}

	@SuppressWarnings("unchecked")
	public List<CustomAttributeValueDto> getCustomAttributesDetails(String taskId) {

		List<CustomAttributeValueDto> customAttributeValues = null;
		CustomAttributeValueDto customAttributeValue = null;

		Query query = this.getSession().createSQLQuery(
				"select cv.PROCESS_NAME,cv.KEY, cv.ATTR_VALUE, ct.LABEL, ct.DATA_TYPE from CUSTOM_ATTR_VALUES cv, CUSTOM_ATTR_TEMPLATE ct where cv.PROCESS_NAME = ct.PROCESS_NAME and cv.KEY=ct.KEY and cv.TASK_ID = '"
						+ taskId + "'");

		List<Object[]> list = query.list();

		if (!ServicesUtil.isEmpty(list) && list.size() > 0) {
			customAttributeValues = new ArrayList<CustomAttributeValueDto>();
			for (Object[] object : list) {
				customAttributeValue = new CustomAttributeValueDto();
				customAttributeValue.setProcessName((String) object[0]);
				customAttributeValue.setKey((String) object[1]);
				customAttributeValue.setAttributeValue((String) object[2]);
				customAttributeValue.setAttributeTemplate((String) object[3]);
				customAttributeValue.setDataType(object[4].toString());

				customAttributeValues.add(customAttributeValue);
			}
		}
		return customAttributeValues;

	}

	@SuppressWarnings("unchecked")
	public List<String> getCustomAttributeByTaskIdAndKey(String key, String taskId) {
		Query query = this.getSession().createSQLQuery("select ca.task_id from custom_attr_values ca"
				+ " where ca.key= '" + key + "' and ca.attr_value in (select cav.attr_value "
				+ "from custom_attr_values cav where cav.task_id='" + taskId + "' and cav.key='" + key + "' ) ");

		return query.list();

	}

	public String getCFAApprover() {
		Query query = this.getSession().createSQLQuery("SELECT ATTR_VALUE FROM CUSTOM_ATTR_VALUES WHERE "
				+ "TASK_ID IN ( SELECT TOP 1 PROCESS_ID FROM PROCESS_EVENTS "
				+ "WHERE NAME = 'CFAApproverMatrixChangeProcess' AND STATUS = 'COMPLETED' ORDER BY COMPLETED_AT DESC) "
				+ "AND KEY = 'hc9d50aje5bic'");
		String approver = (String) query.uniqueResult();
		return approver;
	}
	
	public Integer getAmount(String processId) {
		Query query = this.getSession().createSQLQuery("SELECT ATTR_VALUE FROM CUSTOM_ATTR_VALUES WHERE "
				+ "TASK_ID = '"+processId+"' and key = '3bbhb28715bb3' ");
		String result = (String) query.uniqueResult();
		Integer amount = Integer.parseInt(result);
		return amount;
	}
	
}
