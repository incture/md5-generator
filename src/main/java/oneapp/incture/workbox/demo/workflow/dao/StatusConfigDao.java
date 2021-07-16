package oneapp.incture.workbox.demo.workflow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.entity.StatusConfigDo;
import oneapp.incture.workbox.demo.workflow.dto.StatusDto;
import oneapp.incture.workbox.demo.workflow.util.WorkflowCreationConstant;

@Repository("StatusConfigDao")
public class StatusConfigDao {

	@Autowired
	SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Map<String, StatusDto> getTasksBusinessStatus(String processName) {
		Map<String, StatusDto> statusConfig = new HashMap<String, StatusDto>();
		StatusDto statusDto = null;
		try{
			String getStatus = "select p from StatusConfigDo p where p.processName= :processName";
			Query statusQry = this.getSession().createQuery(getStatus);
			statusQry.setParameter("processName", processName);
			List<StatusConfigDo> statusConfigDos = statusQry.list();
			for (StatusConfigDo statusConfigDo : statusConfigDos) {
				if(statusConfig.containsKey(statusConfigDo.getTaskName())){
					statusDto = statusConfig.get(statusConfigDo.getTaskName());
					statusDto = setDto(statusDto,statusConfigDo.getStatus(),statusConfigDo.getBusinessStatus());
				}else{
					statusDto = new StatusDto();
					statusDto = setDto(statusDto,statusConfigDo.getStatus(),statusConfigDo.getBusinessStatus());
					statusConfig.put(statusConfigDo.getTaskName(), statusDto);
				}
			}
		}catch (Exception e) {
			System.err.println("[WBP-DEV] getTasksBusinessStatus ERROR"+e);
		}
		return statusConfig;
	}

	private StatusDto setDto(StatusDto statusDto, String status, String businessStatus) {
		
		switch (status) {
		case WorkflowCreationConstant.READY:
			statusDto.setReady(businessStatus);
			break;
		case WorkflowCreationConstant.RESERVED:
			statusDto.setReserved(businessStatus);
			break;
		case WorkflowCreationConstant.COMPLETED:
			statusDto.setCompleted(businessStatus);
			break;
		case WorkflowCreationConstant.APPROVE:
			statusDto.setApprove(businessStatus);
			break;
		case WorkflowCreationConstant.REJECT:
			statusDto.setReject(businessStatus);
			break;
		case WorkflowCreationConstant.RESOLVED:
			statusDto.setResolved(businessStatus);
			break;
		case WorkflowCreationConstant.DONE:
			statusDto.setDone(businessStatus);
			break;

		default:
			break;
		}
		return statusDto;
	}

	public void deleteInStatusConfig(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInSC = "DELETE FROM STATUS_CONFIG WHERE PROCESS_NAME IN (:processName)";
		Query deleteInSCQuery = session.createSQLQuery(deleteInSC);
		deleteInSCQuery.setParameterList("processName", processName);
		deleteInSCQuery.executeUpdate();
		tx.commit();
		session.close();
	}
}
