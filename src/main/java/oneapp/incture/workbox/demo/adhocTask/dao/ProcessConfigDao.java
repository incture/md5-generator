package oneapp.incture.workbox.demo.adhocTask.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("adhocProcessConfigDao")
public class ProcessConfigDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	public String getTaskType(String processName) {
		
		Query processDetailQry = getSession().createSQLQuery("SELECT PROCESS_TYPE FROM PROCESS_CONFIG_TB "
				+ "WHERE PROCESS_NAME = '"+processName+"'");
		String processType = (String) processDetailQry.uniqueResult();
		return processType;
	}

	public String getSlaForProcess(String processName) {
		
		Query slaQry = getSession().createSQLQuery("SELECT SLA FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME = '"+processName+"'");
		String sla = (String) slaQry.uniqueResult();
		return sla;
	}

	public String getProcessNameDisplay(String processName) {
		Query processNameQry = getSession().createSQLQuery("SELECT PROCESS_DISPLAY_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME = '"+processName+"'");
		String processNameDisplay = (String) processNameQry.uniqueResult();
		return processNameDisplay;
	}

	public ProcessConfigDto getProcessDetail(String processName) {
		ProcessConfigDto configDto = new ProcessConfigDto();
		Session session = sessionFactory.openSession();
		Query processDetailQry = session.createSQLQuery("SELECT PROCESS_DISPLAY_NAME,SLA,URL FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME = '"+processName+"'");
		Object[] processDetail = (Object[]) processDetailQry.uniqueResult();
		configDto.setSla(processDetail[1].toString());
		configDto.setProcessDisplayName(processDetail[0].toString());
		configDto.setDetailURL(ServicesUtil.isEmpty(processDetail[2])?null:processDetail[2].toString());
		return configDto;
	}
	
}
