package oneapp.incture.workbox.demo.adhocTask.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDoPK;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskOwnerDto;

@Repository("adhocTaskOwnerDao")
public class TaskOwnerDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private TaskTemplateOwnerDao templateOwnerDao;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	public void updateIsProcessed(String event, String userId, int isProcessed) {

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Boolean isProcess = false;
		if(isProcessed == 1)
			isProcess = true;
		Query updateIsprocessed = session.createSQLQuery("UPDATE TASK_OWNERS SET IS_PROCESSED = "+isProcessed+" WHERE EVENT_ID = '"+event+"'"
				+ " AND TASK_OWNER = '"+userId+"'");
		updateIsprocessed.executeUpdate();
		tx.commit();
		session.close();

	}

	public void saveInTaskOwner(String event, List<TaskOwnerDto> taskOwnerDto, boolean isSubstituted) {

		TaskOwnersDo taskOwnersDo = null;
		Transaction tx = null;
		Integer count = 0;
		Session session = sessionFactory.openSession();
		tx = session.beginTransaction();

		for (TaskOwnerDto owner : taskOwnerDto) {

			taskOwnersDo = new TaskOwnersDo();
			count++;
			taskOwnersDo.setEnRoute(true);
			taskOwnersDo.setTaskOwnersDoPK(new TaskOwnersDoPK(event, owner.getTaskOwner()));
			taskOwnersDo.setIsProcessed(true);             			
			taskOwnersDo.setIsSubstituted(isSubstituted);          
			taskOwnersDo.setOwnerEmail(owner.getEmail());
			taskOwnersDo.setTaskOwnerDisplayName(owner.getTaskOwnername()); 

			session.saveOrUpdate(taskOwnersDo);
			if(count % 50 == 0){
				session.flush();
				session.clear();
			}
		}
		tx.commit();
		session.close();


	}

	public List<TaskOwnersDto> setTaskOwners(String newEventId, String ownerId) {

		List<TaskOwnersDto> taskOwnerList = templateOwnerDao.getOwners(ownerId);

		for (TaskOwnersDto taskOwnersDto : taskOwnerList) {

			taskOwnersDto.setEventId(newEventId);
			taskOwnersDto.setIsProcessed(false);

		}
		return taskOwnerList;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getTaskOwners(String chatId) {

		String ownerStr = "SELECT TASK_OWNER,TASK_OWNER_DISP,USER_FIRST_NAME,USER_LAST_NAME,USER_EMAIL FROM TASK_OWNERS"
				+ " INNER JOIN USER_IDP_MAPPING ON upper(USER_ID) = upper(TASK_OWNER) WHERE EVENT_ID = '"+chatId+"'";
		Query ownerQry = this.getSession().createSQLQuery(ownerStr);

		return ownerQry.list();
	}

	public Boolean getTaskOwner(String instanceId, String userId) {
		try{
			String ownerStr = "SELECT IS_SUBSTITUTED FROM TASK_OWNERS WHERE EVENT_ID = '"+instanceId+"'"
					+ " AND TASK_OWNER = '"+userId+"'";
			Query ownerQry = this.getSession().createSQLQuery(ownerStr);
			Object res = (Object) ownerQry.uniqueResult();
			
			if(ServicesUtil.isEmpty(res))
				return false;
			else 
				return ((byte)res!=0);
			
			
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Error fetching task Owner Detail"+e);
		}
		
		return false;
	}

	public void deleteTaskOwner(String instanceId, String userId) {
		
		try{
			String ownerStr = "DELETE FROM TASK_OWNERS WHERE EVENT_ID = '"+instanceId+"'"
					+ " AND TASK_OWNER = '"+userId+"'";
			Query ownerQry = this.getSession().createSQLQuery(ownerStr);
			ownerQry.executeUpdate();
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Error deleting task Owner"+e);
		}
	}


}
