package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValuesTableDo;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;



@Repository
//////@Transactional
public class EventsUpdateDao {

	private static final int _HIBERNATE_BATCH_SIZE = 100;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void saveOrUpdateProcesses(List<ProcessEventsDo> processes) {
		Session session = null;
		if(!ServicesUtil.isEmpty(processes) && processes.size() > 0) {
			//session = this.getSession();
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for(int i = 0; i < processes.size(); i++) {
				session.saveOrUpdate(processes.get(i));
				if(i % _HIBERNATE_BATCH_SIZE == 0 && i > 0) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();
			session.close();
//			if(!session.getTransaction().wasCommitted()) {
//				session.flush();
////			}
//			session.close();
		}
	}
	
	public void saveOrUpdateTasks(List<TaskEventsDo> tasks) {
		Session session = null;
		if(!ServicesUtil.isEmpty(tasks) && tasks.size() > 0) {
			//session = this.getSession();
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for(int i = 0; i < tasks.size(); i++) {
				TaskEventsDo currentTask = tasks.get(i);
				//currentTask.setUpdatedAt(new Date());
			
				session.saveOrUpdate(currentTask);	
				if(i % _HIBERNATE_BATCH_SIZE == 0 && i > 0) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();
			session.close();
//			if(!session.getTransaction().wasCommitted()) {
//				session.flush();
////			}
//			session.close();
		}
	}
	
	public void saveOrUpdateCustValues(List<CustomAttributeValuesTableDo> custValues) {
		Session session = null;
		Gson g = new Gson();
		if(!ServicesUtil.isEmpty(custValues) && custValues.size() > 0) {
			//session = this.getSession();
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			try {
				for(int i = 0; i < custValues.size(); i++) {
					CustomAttributeValuesTableDo currentTask = custValues.get(i);
					//currentTask.setUpdatedAt(new Date());
					System.err.println(g.toJson(currentTask));
					session.saveOrUpdate(currentTask);
					if(i % _HIBERNATE_BATCH_SIZE == 0 && i > 0) {
						session.flush();
						session.clear();
					}
					
				}
				tx.commit();
				session.close();
			} catch (Exception e) {
				System.err.println("[WBP-Dev]Can not insert custValues : With exception : "+e);
				
			}
			
			
		}
	}
	
	public void saveOrUpdateOwners(List<TaskOwnersDo> owners) {
		Session session = null;
		if(!ServicesUtil.isEmpty(owners) && owners.size() > 0) {
			//session = this.getSession();
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			try {
				for(int i = 0; i < owners.size(); i++) {
					TaskOwnersDo ownersDo = owners.get(i);
					if(!ServicesUtil.isEmpty(ownersDo)){
						if(ownersDo.getTaskOwnersDoPK().getTaskOwner().equalsIgnoreCase("P000188")) {
							System.err.println("EventsUpdateDao.saveOrUpdateOwners() owner : "+ownersDo);
						}
						session.saveOrUpdate(ownersDo);
						if(i % _HIBERNATE_BATCH_SIZE == 0 && i > 0) {
							session.flush();
							session.clear();
						}
					} else {
						System.err.println("[WBP-Dev]Can not insert as primary key columns are NULL : "+owners.get(i));
					}
					
				}
				tx.commit();
				session.close();
			} catch (Exception e) {
				System.err.println("[WBP-Dev]Can not insert owner : With exception : "+e);
				
			}
			
//			if(!session.getTransaction().wasCommitted()) {
//				session.flush();
////			}
//			session.close();
		}
	}
	public void saveOrUpdateCustomAttributes(List<CustomAttributeValue> attributeValues) {
		long start=System.currentTimeMillis();
		Session session = null;
		try{
			if(!ServicesUtil.isEmpty(attributeValues) && attributeValues.size() > 0) {
				session = this.getSession();
				for(int i = 0; i < attributeValues.size(); i++) {
					CustomAttributeValue attributeValue = attributeValues.get(i);
					//currentTask.setUpdatedAt(new Date());
				
					session.saveOrUpdate(attributeValue);
					if(i % _HIBERNATE_BATCH_SIZE == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
//				if(!session.getTransaction().wasCommitted()) {
					session.flush();
//				}
			}
		}catch(Exception e){
			System.err.println("EventsUpdateDao.saveOrUpdateCustomAttributes() exception : "+e.getMessage());
			e.printStackTrace();
		}
		
		System.err.println("[WBP-Dev]EventsUpdateDao.saveOrUpdateCustomAttributes() db insertion time : "+(System.currentTimeMillis()-start)+" msec.");
	}
	
	
}
