package oneapp.incture.workbox.demo.adhocTask.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskTemplateDto;

@Repository("adhocTaskTemplateDao")
public class TaskTemplateDao {

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
	public List<TaskTemplateDto> getTaskDetail(String processName) {
		List<TaskTemplateDto> taskTemplateList = null;
		TaskTemplateDto taskTemplateDto = null;

		String eventName = "select task_name , task_type,owner_id from Task_Template where process_name= :val order by step_number";
		Query eventNameQuery = getSession().createSQLQuery(eventName);
		eventNameQuery.setParameter("val",processName);
		List<Object[]> taskDetail = eventNameQuery.list();

		taskTemplateList = new ArrayList<TaskTemplateDto>();
		for (Object[] obj : taskDetail) {
			taskTemplateDto = new TaskTemplateDto();
			taskTemplateDto.setOwnerId(ServicesUtil.isEmpty(obj[2])?null:obj[2].toString());
			taskTemplateDto.setTaskType(obj[1].toString());
			taskTemplateDto.setTaskName(obj[0].toString());
			taskTemplateList.add(taskTemplateDto);
		}
		System.err.println(taskTemplateList);
		return taskTemplateList;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getTaskOwnersDetails(String processName) {

		String selectTaskTemplate = "SELECT DISTINCT PT.OWNER_ID,PT.TASK_NAME,PT.TEMPLATE_ID,PT.RUN_TIME_USER,"
                + "PT.TASK_TYPE,PT.CUSTOM_KEY,PT.SUBJECT,PT.DESCRIPTION,"
                + "PT.SOURCE_ID,PT.TARGET_ID,PT.URL,PT.TASK_NATURE FROM PROCESS_TEMPLATE PT WHERE PROCESS_NAME = '"+processName+"'";    
        Query taskTemplateQry = getSession().createSQLQuery(selectTaskTemplate);
        List<Object[]> taObjects = taskTemplateQry.list();

        return taObjects;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getCommonTaskOwnersDetails(String processName) {

		String selectTaskTemplate = "SELECT DISTINCT PT.OWNER_ID,PT.TASK_NAME,PT.TEMPLATE_ID,PT.RUN_TIME_USER,"
				+ " PT.TASK_TYPE,PT.CUSTOM_KEY,PT.SUBJECT,PT.DESCRIPTION,"
				+ " PT.SOURCE_ID,PT.TARGET_ID,PT.URL,PT.TASK_NATURE , TAS.OWNER_SEQU_TYPE ,"
				+ " TAS.ATTR_TYPE_ID , TAS.ATTR_TYPE_NAME , TAS.ORDER_BY FROM PROCESS_TEMPLATE PT "
				+ " JOIN TASK_OWNER_TEMPLATE_SEQUENCE TAS ON PT.PROCESS_NAME = TAS.PROCESS_NAME"
				+ " AND PT.TEMPLATE_ID = TAS.TEMPLATE_ID" 
				+ " WHERE PT.PROCESS_NAME = '"+ processName+ "'";	
        Query taskTemplateQry = getSession().createSQLQuery(selectTaskTemplate);
        List<Object[]> taObjects = taskTemplateQry.list();

        return taObjects;
	}

}
