package oneapp.incture.workbox.demo.zoho.dao;

import java.util.Map;

import javax.swing.plaf.basic.BasicTabbedPaneUI.TabSelectionHandler;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskEventsDo;
import oneapp.incture.workbox.demo.zoho.services.ZohoService;



@Repository
@EnableAsync
//@Transactional
public class ZohoTaskEventsDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	@Autowired
	TaskOwnersDao taskOwnersDao;
	

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	public void updateTaskStatus(String processName) {
		Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
		Query customAttributesQuery = this.getSession().createSQLQuery(
				"update  task_events set status = 'CANCELLED'" + " where status='READY' and  PROCESS_ID = '" + processName + "'");
		customAttributesQuery.executeUpdate();
		session.flush();
		session.clear();
		tx.commit();
		session.close();

	}
	
	
	@SuppressWarnings("unchecked")
	public void completeTaskStatus(String taskId) {
		Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
		Query customAttributesQuery = this.getSession().createSQLQuery(
				"update  task_events set status = 'COMPLETED'  where   event_id = '" + taskId + "'");
		customAttributesQuery.executeUpdate();
		session.flush();
		session.clear();
		tx.commit();
		session.close();

	}
	
	public void updateAssignee(String taskId , String pUserId, Map<String, UserIDPMappingDto> userList) {
		Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
		String taskOwnerDisplayName = userList.get(pUserId).getUserFirstName() + " " + userList.get(pUserId).getUserLastName();
		TaskEventsDo taskEventsDo = (TaskEventsDo) this.getSession().get(TaskEventsDo.class, taskId);
		taskEventsDo.setStatus("RESERVED");
		taskEventsDo.setCurrentProcessor(pUserId);
		taskEventsDo.setCurrentProcessorDisplayName(taskOwnerDisplayName);
		
		TaskOwnersDto taskOwnersDto =  new TaskOwnersDto();
		taskOwnersDto.setTaskOwner(pUserId);
		taskOwnersDto.setTaskOwnerDisplayName(taskOwnerDisplayName);
		taskOwnersDto.setEventId(taskId);
		taskOwnersDto.setOwnerEmail(userList.get(pUserId).getUserEmail());
		
		this.getSession().update(taskEventsDo);
		taskOwnersDao.saveOrUpdateTaskOwner(taskOwnersDto);
		session.flush();
		session.clear();
		tx.commit();
		session.close();
	}
	
	

}
