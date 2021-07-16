package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskAudit;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class TaskAuditDao extends BaseDao<TaskAudit, TaskAuditDto>{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	protected TaskAudit importDto(TaskAuditDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		TaskAudit entity = new TaskAudit();
		if (!ServicesUtil.isEmpty(fromDto.getAuditId()))
			entity.setAuditId(fromDto.getAuditId());
		if (!ServicesUtil.isEmpty(fromDto.getEventId()))
			entity.setEventId(fromDto.getEventId());
		if (!ServicesUtil.isEmpty(fromDto.getAction()))
			entity.setActionType(fromDto.getAction());
		if (!ServicesUtil.isEmpty(fromDto.getComment()))
			entity.setComment(fromDto.getComment());
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			entity.setUserId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getSendToUser()))
			entity.setSendToUser(fromDto.getSendToUser());
		if (!ServicesUtil.isEmpty(fromDto.getSendToUserName()))
			entity.setSendToUserName(fromDto.getSendToUserName());
		if (!ServicesUtil.isEmpty(fromDto.getUserName()))
			entity.setUserName(fromDto.getUserName());
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedAt()))
			entity.setUpdatedAt(fromDto.getUpdatedAt());
		return entity;
	}

	@Override
	protected TaskAuditDto exportDto(TaskAudit entity) {
		TaskAuditDto auditDto = new TaskAuditDto();
		if (!ServicesUtil.isEmpty(entity.getAuditId()))
			auditDto.setAuditId(entity.getAuditId());
		if (!ServicesUtil.isEmpty(entity.getEventId()))
			auditDto.setEventId(entity.getEventId());
		if (!ServicesUtil.isEmpty(entity.getActionType()))
			auditDto.setAction(entity.getActionType());
		if (!ServicesUtil.isEmpty(entity.getComment()))
			auditDto.setComment(entity.getComment());
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			auditDto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getSendToUser()))
			auditDto.setSendToUser(entity.getSendToUser());
		if (!ServicesUtil.isEmpty(entity.getSendToUserName()))
			auditDto.setSendToUserName(entity.getSendToUserName());
		if (!ServicesUtil.isEmpty(entity.getUserName()))
			auditDto.setUserName(entity.getUserName());
		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			auditDto.setUpdatedAt(entity.getUpdatedAt());
		return auditDto;
	}

	public void saveOrUpdateAudit(TaskAuditDto dto) throws Exception {

		try{
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(importDto(dto));
			
			tx.commit();
			session.close();
		}catch (Exception e) {
			System.err.println("[WBP-Dev]ERROR ENTERING AUDIT"+e.getMessage());
		}

	}

	public void saveOrUpdateAudits(List<TaskAuditDto> audits) {
		
		try{
			if (!ServicesUtil.isEmpty(audits) && audits.size() > 0) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < audits.size(); i++) {
					TaskAuditDto currentAudit = audits.get(i);
					session.saveOrUpdate(importDto(currentAudit));
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
		}catch (Exception e) {
			System.err.println("[WBP-Dev]ERROR ENTERING AUDIT"+e);
		}
	}

}
