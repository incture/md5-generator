package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessTemplateVaueDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class ProcessTemplateValueDao extends BaseDao<ProcessTemplateVaueDo, ProcessTemplateValueDto> {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession(){
		try {
			return sessionFactory.getCurrentSession();
		} catch(Exception e) {
			return sessionFactory.openSession();
		}
	}
	
	@Override
	protected ProcessTemplateVaueDo importDto(ProcessTemplateValueDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ProcessTemplateVaueDo entity = new ProcessTemplateVaueDo();
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getProcessId())
				&& !ServicesUtil.isEmpty(fromDto.getTemplateId())) {
			entity.setProcessId(fromDto.getProcessId());
			entity.setTemplateId(fromDto.getTemplateId());
			if (!ServicesUtil.isEmpty(fromDto.getTaskName()))
				entity.setTaskName(fromDto.getTaskName());
			if (!ServicesUtil.isEmpty(fromDto.getOwnerId()))
				entity.setOwnerId(fromDto.getOwnerId());
			if (!ServicesUtil.isEmpty(fromDto.getTaskType()))
				entity.setTaskType(fromDto.getTaskType());
			if (!ServicesUtil.isEmpty(fromDto.getRunTimeUser()))
				entity.setRunTimeUser(fromDto.getRunTimeUser());
			if (!ServicesUtil.isEmpty(fromDto.getCustomKey()))
				entity.setCustomKey(fromDto.getCustomKey());
			if (!ServicesUtil.isEmpty(fromDto.getSubject()))
				entity.setSubject(fromDto.getSubject());
			if (!ServicesUtil.isEmpty(fromDto.getDescription()))
				entity.setDescription(fromDto.getDescription());
			if (!ServicesUtil.isEmpty(fromDto.getSourceId()))
				entity.setSourceId(fromDto.getSourceId());
			if (!ServicesUtil.isEmpty(fromDto.getTargetId()))
				entity.setTargetId(fromDto.getTargetId());
			if (!ServicesUtil.isEmpty(fromDto.getUrl()))
				entity.setUrl(fromDto.getUrl());
		}
		return entity;
	}

	@Override
	protected ProcessTemplateValueDto exportDto(ProcessTemplateVaueDo entity) {
		ProcessTemplateValueDto processTemplateValueDto = new ProcessTemplateValueDto();
		processTemplateValueDto.setProcessId(entity.getProcessId());
		processTemplateValueDto.setTemplateId(entity.getTemplateId());
		if (!ServicesUtil.isEmpty(entity.getTaskName()))
			processTemplateValueDto.setTaskName(entity.getTaskName());
		if (!ServicesUtil.isEmpty(entity.getOwnerId()))
			processTemplateValueDto.setOwnerId(entity.getOwnerId());
		if (!ServicesUtil.isEmpty(entity.getTaskType()))
			processTemplateValueDto.setTaskType(entity.getTaskType());
		if (!ServicesUtil.isEmpty(entity.getRunTimeUser()))
			processTemplateValueDto.setRunTimeUser(entity.getRunTimeUser());
		if (!ServicesUtil.isEmpty(entity.getCustomKey()))
			processTemplateValueDto.setCustomKey(entity.getCustomKey());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			processTemplateValueDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			processTemplateValueDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getSourceId()))
			processTemplateValueDto.setSourceId(entity.getSourceId());
		if (!ServicesUtil.isEmpty(entity.getTargetId()))
			processTemplateValueDto.setTargetId(entity.getTargetId());
		if (!ServicesUtil.isEmpty(entity.getUrl()))
			processTemplateValueDto.setUrl(entity.getUrl());
		return processTemplateValueDto;
	}

	public void saveOrUpdateProcessTemplateValue(List<ProcessTemplateValueDto> processTemplateValueDtos) {
		
		try {
			if (!ServicesUtil.isEmpty(processTemplateValueDtos) && processTemplateValueDtos.size() > 0) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < processTemplateValueDtos.size(); i++) {
					ProcessTemplateValueDto currentTask = processTemplateValueDtos.get(i);
					session.saveOrUpdate(importDto(currentTask));
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
			System.err.println("[WBP-Dev]Saving of Process Template Value ERROR" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<ProcessTemplateValueDto> fetchTaskDetail(TaskEventsDto taskEventsDto) {
		List<ProcessTemplateValueDto> processTemplateValueDtos = null;
		ProcessTemplateValueDto processTemplateValueDto = null;
		try {
			String targetId = getTargetId(taskEventsDto.getProcessId(), taskEventsDto.getName());
			System.err.println("[WBP-Dev] template id of next task-" + targetId);

			String detailSelect = "SELECT TASK_NAME,OWNER_ID,TASK_TYPE,TARGET_ID,SUBJECT,DESCRIPTION,TEMPLATE_ID,URL "
					+ "FROM PROCESS_TEMPLATE_VALUE " + "WHERE TEMPLATE_ID in ('" + targetId.replace(",", "','")
					+ "') AND PROCESS_ID = '" + taskEventsDto.getProcessId() + "'";
			Query detailQry = getSession().createSQLQuery(detailSelect);
			List<Object[]> taskDetail = detailQry.list();
			System.err.println("[WBP-Dev] next task details SQL" + detailSelect + System.currentTimeMillis());
			processTemplateValueDtos = new ArrayList<ProcessTemplateValueDto>();
			for (Object[] task : taskDetail) {

				processTemplateValueDto = new ProcessTemplateValueDto();
				processTemplateValueDto.setProcessId(taskEventsDto.getProcessId());
				processTemplateValueDto.setOwnerId(task[1].toString());
				processTemplateValueDto.setTaskType(task[2].toString());
				processTemplateValueDto.setTaskName(task[0].toString());
				processTemplateValueDto.setTargetId(ServicesUtil.isEmpty(task[3]) ? null : task[3].toString());
				processTemplateValueDto.setSubject(ServicesUtil.isEmpty(task[4]) ? null : task[4].toString());
				processTemplateValueDto.setDescription(ServicesUtil.isEmpty(task[5]) ? null : task[5].toString());
				processTemplateValueDto.setTemplateId(ServicesUtil.isEmpty(task[6]) ? null : task[6].toString());
				processTemplateValueDto.setUrl(ServicesUtil.isEmpty(task[7]) ? null : task[7].toString());
				processTemplateValueDto.setEventId(UUID.randomUUID().toString().replace("-", ""));
				processTemplateValueDtos.add(processTemplateValueDto);
			}
			System.err.println("[WBP-Dev] next task details" + processTemplateValueDto + System.currentTimeMillis());
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][TASK VALUE] NO NEXT TASK ERROR" + e.getMessage());
		}
		return processTemplateValueDtos;
	}

	@SuppressWarnings("unchecked")
	public List<ProcessTemplateValueDto> fetchTaskDetailRMG(TaskEventsDto taskEventsDto, String targetId) {
		List<ProcessTemplateValueDto> processTemplateValueDtos = null;
		ProcessTemplateValueDto processTemplateValueDto = null;
		try {
			System.err.println("[WBP-Dev] template id of next task-" + targetId);

			String detailSelect = "SELECT TASK_NAME,OWNER_ID,TASK_TYPE,TARGET_ID,SUBJECT,DESCRIPTION,PT.TEMPLATE_ID,URL," + 
										" RUN_TIME_USER,TOS.OWNER_SEQU_TYPE,TOS.ATTR_TYPE_NAME , TOS.ORDER_BY " +
										" FROM PROCESS_TEMPLATE_VALUE PT JOIN TASK_OWNER_TEMPLATE_SEQUENCE TOS ON PT.TEMPLATE_ID = TOS.TEMPLATE_ID  " + 
										" WHERE PT.TEMPLATE_ID in ('" + targetId.replace(",", "','") + "') AND PROCESS_ID = '" + taskEventsDto.getProcessId() + "'";
			Query detailQry = getSession().createSQLQuery(detailSelect);
			List<Object[]> taskDetail = detailQry.list();
			System.err.println("[WBP-Dev] next task details SQL" + detailSelect + System.currentTimeMillis());
			processTemplateValueDtos = new ArrayList<ProcessTemplateValueDto>();
			for (Object[] task : taskDetail) {

				processTemplateValueDto = new ProcessTemplateValueDto();
				processTemplateValueDto.setProcessId(taskEventsDto.getProcessId());
				processTemplateValueDto.setOwnerId(task[1].toString());
				processTemplateValueDto.setTaskType(task[2].toString());
				processTemplateValueDto.setTaskName(task[0].toString());
				processTemplateValueDto.setTargetId(ServicesUtil.isEmpty(task[3]) ? null : task[3].toString());
				processTemplateValueDto.setSubject(ServicesUtil.isEmpty(task[4]) ? null : task[4].toString());
				processTemplateValueDto.setDescription(ServicesUtil.isEmpty(task[5]) ? null : task[5].toString());
				processTemplateValueDto.setTemplateId(ServicesUtil.isEmpty(task[6]) ? null : task[6].toString());
				processTemplateValueDto.setUrl(ServicesUtil.isEmpty(task[7]) ? null : task[7].toString());
				processTemplateValueDto.setEventId(UUID.randomUUID().toString().replace("-", ""));
				processTemplateValueDto.setRunTimeUser(ServicesUtil.asBoolean(task[8]));
				processTemplateValueDto.setOwnerSeqType(ServicesUtil.isEmpty(task[9])? "" : task[9].toString());
				processTemplateValueDto.setAttrTypeName(ServicesUtil.isEmpty(task[10])? "" : task[10].toString());
				processTemplateValueDto.setOrderBy(ServicesUtil.isEmpty(task[11])? "" : task[11].toString());
				processTemplateValueDtos.add(processTemplateValueDto);
			}
			System.err.println("[WBP-Dev] next task details" + processTemplateValueDto + System.currentTimeMillis());
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][TASK VALUE] NO NEXT TASK ERROR" + e.getMessage());
		}
		return processTemplateValueDtos;
	}

	public String getTargetId(String processId, String name) {
		try {
			Session session = sessionFactory.openSession();
			
			String detailSelect = "SELECT TARGET_ID FROM PROCESS_TEMPLATE_VALUE WHERE PROCESS_ID = '" + processId + "'"
					+ " AND TASK_NAME = '" + name + "'";
			Query detailQry = session.createSQLQuery(detailSelect);
			String targetId = (String) detailQry.uniqueResult();
			return targetId;
		} catch (Exception e) {
			System.err.println("[WBP-Dev]No Target Id Fetched" + e.getMessage());
		}
		return null;
	}
	
	public void deleteInProcessTemplateValue(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInPTV = "DELETE FROM PROCESS_TEMPLATE_VALUE WHERE PROCESS_ID IN "
				+ "(SELECT PROCESS_ID  FROM PROCESS_EVENTS WHERE NAME IN ("
				+ " SELECT DISTINCT PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName)))";
		Query deleteInPTVQuery =session.createSQLQuery(deleteInPTV);
		deleteInPTVQuery.setParameterList("processName", processName);
		deleteInPTVQuery.executeUpdate();
		tx.commit();
		session.close();
	}

}
