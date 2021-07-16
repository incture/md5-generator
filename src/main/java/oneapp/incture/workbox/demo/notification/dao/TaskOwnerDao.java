package oneapp.incture.workbox.demo.notification.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;


@Repository("NotificationTaskOwner")
public class TaskOwnerDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	@SuppressWarnings("unchecked")
	public List<TaskOwnersDto> getOwners(String eventId) {
		
		List<TaskOwnersDto> taskOwnerDtos = new ArrayList<>();
		String ownerStr = "SELECT TASK_OWNER,TASK_OWNER_DISP FROM TASK_OWNERS WHERE EVENT_ID = '"+eventId+"'";
		Query ownerQry = getSession().createSQLQuery(ownerStr);
		List<Object[]> resultList = ownerQry.list();

		for (Object[] obj : resultList) {
			TaskOwnersDto taskOwnerDto = new TaskOwnersDto();

			taskOwnerDto.setTaskOwner(obj[0] == null ? null : (String) obj[0]);
			taskOwnerDto.setTaskOwnerDisplayName(obj[1] == null ? null : (String) obj[1]);
			
			taskOwnerDtos.add(taskOwnerDto);
	}

		return taskOwnerDtos;
}
}
