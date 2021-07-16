package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.entity.StatusConfigDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class StatusConfigDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}
	
	public String getBusinessStatus(String processName,String taskName,String status){
	
		String businessStatus = "";
		try{
		String businessStatusstr = "SELECT BUSINESS_STATUS FROM STATUS_CONFIG WHERE PROCESS_NAME = '"+processName+"'"
				+ " AND TASK_NAME = '"+taskName+"' "
				+ " AND STATUS = '"+status+"' ";
		Query businessStatusqry = this.getSession().createSQLQuery(businessStatusstr);
		businessStatus = (String) businessStatusqry.uniqueResult();
		}catch (Exception e) {
			System.err.println("[WBP-DEV] ERROR no busniness status"+e);
			businessStatus = "";
		}
		return businessStatus;
	}

	public String getBusinessStatusByEventId(String eventId, String status) {
		String businessStatus = "";
		try{
		String businessStatusstr = "SELECT SG.BUSINESS_STATUS FROM STATUS_CONFIG SG "
				+ "INNER JOIN TASK_EVENTS TE ON TE.PROC_NAME = SG.PROCESS_NAME AND TE.NAME = SG.TASK_NAME "
				+ "WHERE EVENT_ID = '"+eventId+"' AND SG.STATUS = '"+status.toUpperCase()+"'";
		Query businessStatusqry = this.getSession().createSQLQuery(businessStatusstr);
		businessStatus = (String) businessStatusqry.uniqueResult();
		if(businessStatus.equalsIgnoreCase("null") || ServicesUtil.isEmpty(businessStatus))
			businessStatus = "";
		}catch (Exception e) {
			System.err.println("[WBP-DEV] ERROR no busniness status"+e);
			businessStatus = "";
		}
		return businessStatus;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Map<String,List<String>>> getAllSCPProcessStatus() {
		Map<String, Map<String,List<String>>> process = new HashMap<>();
		Map<String,List<String>> taskStatuses = null;
		List<String> status = null;
		try{
			String businessStatusstr = "SELECT PROCESS_NAME, TASK_NAME, STATUS, BUSINESS_STATUS "
					+ "FROM STATUS_CONFIG WHERE SYSTEM = 'SCP' ";
			Query businessStatusqry = this.getSession().createSQLQuery(businessStatusstr);
			List<Object[]> businessStatus = businessStatusqry.list();
			
			for (Object[] obj : businessStatus) {
				if(process.containsKey((String)obj[0])){
					if(process.get(obj[0].toString()).containsKey(obj[1].toString())){
						taskStatuses = process.get(obj[0].toString());
						status = process.get(obj[0].toString()).get(obj[1]);
						status.add(obj[2].toString()+":"+obj[3].toString());
					}else{
						taskStatuses = process.get(obj[0].toString());
						status = new ArrayList<String>();
						status.add(obj[2].toString()+":"+obj[3].toString());
						taskStatuses.put(obj[1].toString(), status);
						process.replace(obj[0].toString(), taskStatuses);
					}
				}else{
					taskStatuses = new HashMap<>();
					status = new ArrayList<String>();
					status.add(obj[2].toString()+":"+obj[3].toString());
					taskStatuses.put(obj[1].toString(), status);
					process.put(obj[0].toString(), taskStatuses);
				}
			}
			Gson g = new Gson();
			System.err.println(g.toJson(process));
		}catch (Exception e) {
			System.err.println("[WBP-DEV] ERROR no busniness SAP status"+e);
		}
		
		
		return process;
	}

	public void savaOrUpdateStatusConfig(List<StatusConfigDo> statusConfigDos) {
		
		try {
			if (!ServicesUtil.isEmpty(statusConfigDos) && !statusConfigDos.isEmpty()) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < statusConfigDos.size(); i++) {
					StatusConfigDo currentTask = statusConfigDos.get(i);
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
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Saving of Status config ERROR" + e.getMessage());
		}
	}
	
}
