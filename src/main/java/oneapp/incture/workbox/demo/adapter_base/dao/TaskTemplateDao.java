package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.SessionFactory;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskTemplateTableDto;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskTemplateDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TemplateTableDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;


@Repository
public class TaskTemplateDao extends BaseDao<TaskTemplateDo, TaskTemplateTableDto> {
	@Autowired 
	private SessionFactory sessionFactory;

	@Override
	protected TaskTemplateDo importDto(TaskTemplateTableDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		TaskTemplateDo entity = new TaskTemplateDo();
		if (!ServicesUtil.isEmpty(fromDto.getProcessName())) {
			entity.setProcessName(fromDto.getProcessName());
			if (!ServicesUtil.isEmpty(fromDto.getTemplateId()))
				entity.setTemplateId(fromDto.getTemplateId());
			if (!ServicesUtil.isEmpty(fromDto.getTaskName()))
				entity.setTaskName(fromDto.getTaskName());
			if (!ServicesUtil.isEmpty(fromDto.getParentTaskName()))
				entity.setParentTaskName(fromDto.getParentTaskName());
			if (!ServicesUtil.isEmpty(fromDto.getTemplateId()))
				entity.setTemplateId(fromDto.getTemplateId());
			if (!ServicesUtil.isEmpty(fromDto.getOrigin()))
				entity.setOrigin(fromDto.getOrigin());
		}
		return entity;
	}

	@Override
	protected TaskTemplateTableDto exportDto(TaskTemplateDo entity) {
		TaskTemplateTableDto processTemplateDto = new TaskTemplateTableDto();

		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			processTemplateDto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getTemplateId()))
			processTemplateDto.setTemplateId(entity.getTemplateId());
		if (!ServicesUtil.isEmpty(entity.getTaskName()))
			processTemplateDto.setTaskName(entity.getTaskName());
		if (!ServicesUtil.isEmpty(entity.getParentTaskName()))
			processTemplateDto.setParentTaskName(entity.getParentTaskName());
		if (!ServicesUtil.isEmpty(entity.getOrigin()))
			processTemplateDto.setOrigin(entity.getOrigin());

		return processTemplateDto;
	}

	@Override
	public void create(TaskTemplateTableDto dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		super.create(dto);
	}
	
	public void create(List<TaskTemplateTableDto> dtos) throws ExecutionFault, InvalidInputFault, NoResultFault {
		for(TaskTemplateTableDto dto:dtos){
		super.create(dto);
		}
	}
	@Override
	public void update(TaskTemplateTableDto dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		super.update(dto);
	}

	@Override
	public void delete(TaskTemplateTableDto dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		super.delete(dto);
	}
	
	public void updateTaskTemplateTable(List<TaskTemplateTableDto> taskTemplateTableDtos) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(taskTemplateTableDtos) && !taskTemplateTableDtos.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < taskTemplateTableDtos.size(); i++) {
					TaskTemplateTableDto currentTask = taskTemplateTableDtos.get(i);
					session.update(importDto(currentTask));
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][UPDATING Task Template Table] ERROR:"+e.getMessage());
		}
	}
	
	public void createTaskTemplateTable(List<TaskTemplateTableDto> taskTemplateTableDtos,String templateId) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(taskTemplateTableDtos) && !taskTemplateTableDtos.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < taskTemplateTableDtos.size(); i++) {
					TaskTemplateTableDto currentTask = taskTemplateTableDtos.get(i);
					currentTask.setTemplateId(templateId);
					session.save(importDto(currentTask));
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING Task Template Table] ERROR:"+e.getMessage());
		}
	}
	public void saveOrUpdateTaskTemplateTable(List<TaskTemplateTableDto> taskTemplateTableDtos) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(taskTemplateTableDtos) && !taskTemplateTableDtos.isEmpty()) {
				//session = this.getSession();
			    session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < taskTemplateTableDtos.size(); i++) {
					TaskTemplateTableDto currentTask = taskTemplateTableDtos.get(i);
					
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
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING Task Template Table] ERROR:"+e.getMessage());
		}
	}
//List<TemplateTableDo> templateTabels
	
	public void createTemplateTables(List<TemplateTableDo> templateTableDo) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(templateTableDo) && !templateTableDo.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < templateTableDo.size(); i++) {
					session.save(templateTableDo.get(i));
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING Task Template Table] ERROR:"+e.getMessage());
		}
	}
	public String getNextTemplateId() {
		
		return (String) this.getSession().createSQLQuery("SELECT TOP 1 TEMPLATE_ID FROM TASK_TEMPLATE_TABLE"
																			+ " ORDER BY TEMPLATE_ID DESC").uniqueResult();
	}
}
