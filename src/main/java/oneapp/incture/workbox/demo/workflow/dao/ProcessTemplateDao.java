package oneapp.incture.workbox.demo.workflow.dao;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.ActionConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ActionTypeConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.dao.UserDetailsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.entity.ActionConfigDo;
import oneapp.incture.workbox.demo.adapter_base.entity.ActionTypeConfigDo;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.StatusConfigDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.manageGroup.dao.GroupDao;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.StatusDto;
import oneapp.incture.workbox.demo.workflow.dto.TaskTemplateOwnerDto;
import oneapp.incture.workbox.demo.workflow.dto.TeamDetailDto;
import oneapp.incture.workbox.demo.workflow.entity.OwnerSequenceTypeDo;
import oneapp.incture.workbox.demo.workflow.util.WorkflowCreationConstant;

@Repository("workflowCreationPT")
public class ProcessTemplateDao extends BaseDao<ProcessTemplate, ProcessTemplateDto> {

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private UserDetailsDao userDetailsDao;

	@Autowired
	private TaskTemplateOwnerDao taskTemplateOwnerDao;

	@Autowired
	ActionTypeConfigDao actionTypeConfigDao;
	
	@Autowired
	ActionConfigDao actionConfigDao;
	
	@Autowired
	oneapp.incture.workbox.demo.adapter_base.dao.StatusConfigDao statusConfigDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	OwnerSequenceTypeDao ownerSequenceTypeDao;

	@Override
	protected ProcessTemplate importDto(ProcessTemplateDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ProcessTemplate entity = new ProcessTemplate();
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getProcessName())
				&& !ServicesUtil.isEmpty(fromDto.getTemplateId())) {
			entity.setProcessName(fromDto.getProcessName());
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
			if (!ServicesUtil.isEmpty(fromDto.getTaskNature()))
				entity.setTaskNature(fromDto.getTaskNature());
		}
		return entity;
	}

	@Override
	protected ProcessTemplateDto exportDto(ProcessTemplate entity) {
		ProcessTemplateDto processTemplateDto = new ProcessTemplateDto();
		processTemplateDto.setProcessName(entity.getProcessName());
		processTemplateDto.setTemplateId(entity.getTemplateId());
		if (!ServicesUtil.isEmpty(entity.getTaskName()))
			processTemplateDto.setTaskName(entity.getTaskName());
		if (!ServicesUtil.isEmpty(entity.getOwnerId()))
			processTemplateDto.setOwnerId(entity.getOwnerId());
		if (!ServicesUtil.isEmpty(entity.getTaskType()))
			processTemplateDto.setTaskType(entity.getTaskType());
		if (!ServicesUtil.isEmpty(entity.getRunTimeUser()))
			processTemplateDto.setRunTimeUser(entity.getRunTimeUser());
		if (!ServicesUtil.isEmpty(entity.getCustomKey()))
			processTemplateDto.setCustomKey(entity.getCustomKey());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			processTemplateDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			processTemplateDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getSourceId()))
			processTemplateDto.setSourceId(entity.getSourceId());
		if (!ServicesUtil.isEmpty(entity.getTargetId()))
			processTemplateDto.setTargetId(entity.getTargetId());
		if (!ServicesUtil.isEmpty(entity.getTaskNature()))
			processTemplateDto.setTaskNature(entity.getTaskNature());
		return processTemplateDto;
	}

	public void saveOrUpdateProcessTemplate(List<ProcessTemplateDto> processTemplateDtos) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (!ServicesUtil.isEmpty(processTemplateDtos) && !processTemplateDtos.isEmpty()) {
				
				for (int i = 0; i < processTemplateDtos.size(); i++) {
					ProcessTemplateDto currentTask = processTemplateDtos.get(i);
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
			System.err.println("[WBP-Dev]Saving of Process Template ERROR" + e.getMessage());
		}
	}

	public String updateProcessTemplate(CustomProcessCreationDto updateProcessDto) {
		StringBuilder ownerId = null;
		String result = WorkflowCreationConstant.FAILURE;
		ProcessTemplateDto processTemplateDto = null;
		List<ProcessTemplateDto> processTemplateDtos = null;
		List<TaskTemplateOwnerDto> templateOwnerDtos = null;
		ActionConfigDo actionConfigDo = null;
		List<ActionConfigDo> actionConfigDos = new ArrayList<>();
		List<StatusConfigDo> statusConfigDos = new ArrayList<>();
		OwnerSequenceTypeDo ownerSequenceTypeDo = null;
		List<OwnerSequenceTypeDo> ownerSequenceTypeDos = new ArrayList<>();
		try {
			templateOwnerDtos = new ArrayList<>();
			processTemplateDtos = new ArrayList<>();
			Map<String,List<ActionTypeConfigDo>> actionDtos = actionTypeConfigDao.getActionTypeConfig();
			for (TeamDetailDto team : updateProcessDto.getTeamDetailDto()) { // to
																				// Add
																				// a
																				// new
																				// task
																				// Template
				if (WorkflowCreationConstant.TWO.equals(team.getIsEdited())) {
					ownerId = new StringBuilder("O");
					Random rn = new Random();
					int randRequest = (int) (1000 + rn.nextInt() * (9999 - 1000) + 1);
					ownerId.append(randRequest);

					processTemplateDto = new ProcessTemplateDto();
					processTemplateDto.setProcessName(team.getProcessName().trim());
					processTemplateDto.setTaskName(team.getEventName());
					if(team.getSourceId().contains("startTask")) {
						processTemplateDto.setSourceId(null);
					} else {
						processTemplateDto.setSourceId(String.join(",", team.getSourceId()));
					}
					processTemplateDto.setTemplateId(team.getTemplateId());
					if(team.getTargetId().contains("endTask")) {
						processTemplateDto.setTargetId(null);
					} else {
						processTemplateDto.setTargetId(String.join(",", team.getTargetId()));
					}
					processTemplateDto.setSubject(team.getSubject());
					processTemplateDto.setDescription(team.getDescription());
					processTemplateDto.setRunTimeUser((team.getRunTimeUser() == 1) ? true : false);
					processTemplateDto.setTaskNature(team.getTaskNature());
					if (!processTemplateDto.getRunTimeUser()) {
						processTemplateDto.setOwnerId(ownerId.toString());
						templateOwnerDtos.addAll(createDtoForTaskTemplateOwner(team, ownerId.toString()));
					}
					processTemplateDto.setTaskType(team.getTaskType());
					processTemplateDto
							.setCustomKey(ServicesUtil.isEmpty(team.getCustomKey()) ? null : team.getCustomKey());
					processTemplateDtos.add(processTemplateDto);
					if(actionDtos.containsKey(processTemplateDto.getTaskType())){
						for (ActionTypeConfigDo actions : actionDtos.get(processTemplateDto.getTaskType())) {
							actionConfigDo = new ActionConfigDo();
							actionConfigDo.setProcessName(processTemplateDto.getProcessName());
							actionConfigDo.setTaskName(processTemplateDto.getTaskName());
							actionConfigDo.setActions(actions.getActions());
							actionConfigDo.setStatus(actions.getStatus());
							actionConfigDo.setType(actions.getType());
							actionConfigDo.setActionNature(actions.getActionNature());
							actionConfigDos.add(actionConfigDo);
						}
					}
					if(!ServicesUtil.isEmpty(team.getStatusDto()))
						statusConfigDos.addAll(getStatus(processTemplateDto.getProcessName(),processTemplateDto.getTaskName()
								,team.getStatusDto()));
					
					//task owner sequence
					if(!ServicesUtil.isEmpty(team.getOwnerSequenceType())) {
						ownerSequenceTypeDo = new OwnerSequenceTypeDo();
						ownerSequenceTypeDo.setOwnerSequType(team.getOwnerSequenceType().getOwnerSequType());
						ownerSequenceTypeDo.setOrderBy(team.getOwnerSequenceType().getOrderBy());
						ownerSequenceTypeDo.setAttrTypeId(team.getOwnerSequenceType().getAttrTypeId());
						ownerSequenceTypeDo.setAttrTypeName(team.getOwnerSequenceType().getAttrTypeName());
						ownerSequenceTypeDo.setProcessName(team.getProcessName().replace(" ", ""));
						ownerSequenceTypeDo.setTemplateId(team.getTemplateId());
						ownerSequenceTypeDos.add(ownerSequenceTypeDo);
					}
					
				} else if (WorkflowCreationConstant.ONE.equals(team.getIsEdited()))// To
																					// update
																					// the
																					// basic
																					// detail
																					// of
																					// the
																					// tasks
																					// which
																					// are
																					// already
																					// present
				{

					Session session = sessionFactory.openSession();
					Transaction tx = session.beginTransaction();
					Query updateTaskTemplate = session
							.createSQLQuery("UPDATE PROCESS_TEMPLATE SET TASK_NAME= :val1,"
									+ "TASK_TYPE= :val2, RUN_TIME_USER= :val6, CUSTOM_KEY= :val7,"
									+ "DESCRIPTION= :val8, SUBJECT= :val9,SOURCE_ID= :val10,TARGET_ID= :val11,URL= :val12"
									+ ",TASK_NATURE= :val13 "
									+ "WHERE PROCESS_NAME= :val3 AND TEMPLATE_ID= :val4");
					
					String sourceId =  null;
					String targetId =  null;
					if(!team.getSourceId().contains("startTask")) {
						sourceId = String.join(",", team.getSourceId());
					}
					if(!team.getTargetId().contains("endTask")) {
						targetId = String.join(",", team.getTargetId());
					}
					
					updateTaskTemplate.setParameter("val1", team.getEventName());
					updateTaskTemplate.setParameter("val2", team.getTaskType());
					updateTaskTemplate.setParameter("val3", team.getProcessName());
					updateTaskTemplate.setParameter("val4", team.getTemplateId());
					updateTaskTemplate.setParameter("val6", (team.getRunTimeUser() == 1) ? true : false);
					updateTaskTemplate.setParameter("val8", team.getDescription());
					updateTaskTemplate.setParameter("val9", team.getSubject());
					updateTaskTemplate.setParameter("val10", sourceId);
					updateTaskTemplate.setParameter("val11", targetId);
					updateTaskTemplate.setParameter("val12", team.getUrl());
					updateTaskTemplate.setParameter("val13", team.getTaskNature());
					if (team.getRunTimeUser() == 1)
						updateTaskTemplate.setParameter("val7", team.getCustomKey());
					else
						updateTaskTemplate.setParameter("val7", null);

					updateTaskTemplate.executeUpdate();
					tx.commit();
					session.close();
					
					actionTypeConfigDao.deleteInActionConfigTask(team.getProcessName(),team.getEventName());
					if(actionDtos.containsKey(team.getTaskType())){
						for (ActionTypeConfigDo actions : actionDtos.get(team.getTaskType())) {
							actionConfigDo = new ActionConfigDo();
							actionConfigDo.setProcessName(team.getProcessName());
							actionConfigDo.setTaskName(team.getEventName());
							actionConfigDo.setActions(actions.getActions());
							actionConfigDo.setStatus(actions.getStatus());
							actionConfigDo.setType(actions.getType());
							actionConfigDo.setActionNature(actions.getActionNature());
							actionConfigDos.add(actionConfigDo);
						}
					}
					
					if(!ServicesUtil.isEmpty(team.getStatusDto()))
						statusConfigDos.addAll(getStatus(team.getProcessName(),team.getEventName()
								,team.getStatusDto()));
					
					//task owner sequence
					if(!ServicesUtil.isEmpty(team.getOwnerSequenceType())) {
						ownerSequenceTypeDo = new OwnerSequenceTypeDo();
						ownerSequenceTypeDo.setOwnerSequType(team.getOwnerSequenceType().getOwnerSequType());
						ownerSequenceTypeDo.setOrderBy(team.getOwnerSequenceType().getOrderBy());
						ownerSequenceTypeDo.setAttrTypeId(team.getOwnerSequenceType().getAttrTypeId());
						ownerSequenceTypeDo.setAttrTypeName(team.getOwnerSequenceType().getAttrTypeName());
						ownerSequenceTypeDo.setProcessName(team.getProcessName().replace(" ", ""));
						ownerSequenceTypeDo.setTemplateId(team.getTemplateId());
						ownerSequenceTypeDos.add(ownerSequenceTypeDo);
					}
					
				} else if (WorkflowCreationConstant.FOUR.equals(team.getIsEdited())) {// To
																						// update
																						// the
																						// task
																						// owner
																						// plus
																						// the
																						// basic
																						// detail
																						// of
																						// the
																						// tasks
																						// which
																						// are
																						// already
																						// present

					ownerId = new StringBuilder("O");
					Random rn = new Random();
					int randRequest = (int) (1000 + rn.nextInt() * (9999 - 1000) + 1);
					ownerId.append(randRequest);

					if (team.getRunTimeUser() != 1) {
						System.err.println("in");
						templateOwnerDtos.addAll(createDtoForTaskTemplateOwner(team, ownerId.toString()));
					}
					Session session = sessionFactory.openSession();
					Transaction tx = session.beginTransaction();
					Query updateTaskTemplate = session
							.createSQLQuery("UPDATE PROCESS_TEMPLATE SET TASK_NAME= :val1,"
									+ "TASK_TYPE= :val2, OWNER_ID= :val5, RUN_TIME_USER= :val6, CUSTOM_KEY= :val7,"
									+ "DESCRIPTION= :val8, SUBJECT= :val9,SOURCE_ID= :val10,TARGET_ID= :val11,URL= :val12,"
									+ "TASK_NATURE= :val13 "
									+ "WHERE PROCESS_NAME= :val3 AND TEMPLATE_ID= :val4");
					
					String sourceId =  null;
					String targetId =  null;
					if(!team.getSourceId().contains("startTask")) {
						sourceId = String.join(",", team.getSourceId());
					}
					if(!team.getTargetId().contains("endTask")) {
						targetId = String.join(",", team.getTargetId());
					}
					
					updateTaskTemplate.setParameter("val1", team.getEventName());
					updateTaskTemplate.setParameter("val2", team.getTaskType());
					updateTaskTemplate.setParameter("val3", team.getProcessName());
					updateTaskTemplate.setParameter("val4", team.getTemplateId());
					updateTaskTemplate.setParameter("val8", team.getDescription());
					updateTaskTemplate.setParameter("val9", team.getSubject());
					updateTaskTemplate.setParameter("val10", sourceId);
					updateTaskTemplate.setParameter("val11", targetId);
					updateTaskTemplate.setParameter("val12", team.getUrl());
					updateTaskTemplate.setParameter("val13", team.getTaskNature());
					if (team.getRunTimeUser() == 1)
						updateTaskTemplate.setParameter("val5", null);
					else
						updateTaskTemplate.setParameter("val5", ownerId.toString());

					updateTaskTemplate.setParameter("val6", (team.getRunTimeUser() == 1) ? true : false);
					if (team.getRunTimeUser() == 1)
						updateTaskTemplate.setParameter("val7", team.getCustomKey());
					else
						updateTaskTemplate.setParameter("val7", null);

					updateTaskTemplate.executeUpdate();
					tx.commit();
					session.close();
					actionTypeConfigDao.deleteInActionConfigTask(team.getProcessName(),team.getEventName());
					if(actionDtos.containsKey(team.getTaskType())){
						for (ActionTypeConfigDo actions : actionDtos.get(team.getTaskType())) {
							actionConfigDo = new ActionConfigDo();
							actionConfigDo.setProcessName(team.getProcessName());
							actionConfigDo.setTaskName(team.getEventName());
							actionConfigDo.setActions(actions.getActions());
							actionConfigDo.setStatus(actions.getStatus());
							actionConfigDo.setType(actions.getType());
							actionConfigDo.setActionNature(actions.getActionNature());
							actionConfigDos.add(actionConfigDo);
						}
					}
					
					if(!ServicesUtil.isEmpty(team.getStatusDto()))
						statusConfigDos.addAll(getStatus(team.getProcessName(),team.getEventName()
								,team.getStatusDto()));
					
					//task owner sequence
					if(!ServicesUtil.isEmpty(team.getOwnerSequenceType())) {
						ownerSequenceTypeDo = new OwnerSequenceTypeDo();
						ownerSequenceTypeDo.setOwnerSequType(team.getOwnerSequenceType().getOwnerSequType());
						ownerSequenceTypeDo.setOrderBy(team.getOwnerSequenceType().getOrderBy());
						ownerSequenceTypeDo.setAttrTypeId(team.getOwnerSequenceType().getAttrTypeId());
						ownerSequenceTypeDo.setAttrTypeName(team.getOwnerSequenceType().getAttrTypeName());
						ownerSequenceTypeDo.setProcessName(team.getProcessName().replace(" ", ""));
						ownerSequenceTypeDo.setTemplateId(team.getTemplateId());
						ownerSequenceTypeDos.add(ownerSequenceTypeDo);
					}

				} else if (WorkflowCreationConstant.THREE.equals(team.getIsEdited())) { // to
																						// delete
																						// the
																						// Task
																						// template
					Session session = sessionFactory.openSession();
					Transaction tx = session.beginTransaction();
					System.err.println("[WBP-Dev]in delete");
					Query deleteTask = session.createSQLQuery(
							"DELETE FROM PROCESS_TEMPLATE WHERE PROCESS_NAME= :val1" + " AND TEMPLATE_ID= :val2");
					deleteTask.setParameter("val1", team.getProcessName());
					deleteTask.setParameter("val2", team.getTemplateId());

					deleteTask.executeUpdate();
					
					Query deleteAction = session.createSQLQuery(
							"DELETE FROM ACTION_CONFIG WHERE PROCESS_NAME= :val1" + " AND TASK_NAME= :val2");
					deleteAction.setParameter("val1", team.getProcessName());
					deleteAction.setParameter("val2", team.getEventName());

					deleteAction.executeUpdate();
					tx.commit();
					session.close();
				}

			}
			saveOrUpdateProcessTemplate(processTemplateDtos);
			actionConfigDao.savaOrUpdateActionConfig(actionConfigDos);
			taskTemplateOwnerDao.saveOrUpdateTaskTemplateOwners(templateOwnerDtos);
			statusConfigDao.savaOrUpdateStatusConfig(statusConfigDos);
			ownerSequenceTypeDao.saveOrUpdateOwnerSequence(ownerSequenceTypeDos);
			result = WorkflowCreationConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW UPDATION][TASK TEMPLATE]:" + e);
			e.printStackTrace();
		}
		return result;
	}

	

	private List<StatusConfigDo> getStatus(String processName, String taskName, StatusDto statusDto) {
		List<StatusConfigDo> statusConfigDos = new ArrayList<>();
		StatusConfigDo statusConfig = null;
		if(!ServicesUtil.isEmpty(statusDto)){
			
			if(!ServicesUtil.isEmpty(statusDto.getReady())){
				statusConfig = new StatusConfigDo();
				statusConfig.setProcessName(processName);
				statusConfig.setTaskName(taskName);
				statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
				statusConfig.setBusinessStatus(statusDto.getReady());
				statusConfig.setStatus(WorkflowCreationConstant.READY);
				statusConfigDos.add(statusConfig);
			}
			if(!ServicesUtil.isEmpty(statusDto.getReserved())){
				statusConfig = new StatusConfigDo();
				statusConfig.setProcessName(processName);
				statusConfig.setTaskName(taskName);
				statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
				statusConfig.setBusinessStatus(statusDto.getReserved());
				statusConfig.setStatus(WorkflowCreationConstant.RESERVED);
				statusConfigDos.add(statusConfig);
			}
			if(!ServicesUtil.isEmpty(statusDto.getCompleted())){
				statusConfig = new StatusConfigDo();
				statusConfig.setProcessName(processName);
				statusConfig.setTaskName(taskName);
				statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
				statusConfig.setBusinessStatus(statusDto.getCompleted());
				statusConfig.setStatus(WorkflowCreationConstant.COMPLETED);
				statusConfigDos.add(statusConfig);
			}
			if(!ServicesUtil.isEmpty(statusDto.getApprove())){
				statusConfig = new StatusConfigDo();
				statusConfig.setProcessName(processName);
				statusConfig.setTaskName(taskName);
				statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
				statusConfig.setBusinessStatus(statusDto.getApprove());
				statusConfig.setStatus(WorkflowCreationConstant.APPROVE);
				statusConfigDos.add(statusConfig);
			}
			if(!ServicesUtil.isEmpty(statusDto.getReject())){
				statusConfig = new StatusConfigDo();
				statusConfig.setProcessName(processName);
				statusConfig.setTaskName(taskName);
				statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
				statusConfig.setBusinessStatus(statusDto.getReject());
				statusConfig.setStatus(WorkflowCreationConstant.REJECT);
				statusConfigDos.add(statusConfig);
			}
			if(!ServicesUtil.isEmpty(statusDto.getResolved())){
				statusConfig = new StatusConfigDo();
				statusConfig.setProcessName(processName);
				statusConfig.setTaskName(taskName);
				statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
				statusConfig.setBusinessStatus(statusDto.getResolved());
				statusConfig.setStatus(WorkflowCreationConstant.RESOLVED);
				statusConfigDos.add(statusConfig);
			}
			if(!ServicesUtil.isEmpty(statusDto.getDone())){
				statusConfig = new StatusConfigDo();
				statusConfig.setProcessName(processName);
				statusConfig.setTaskName(taskName);
				statusConfig.setSystem(WorkflowCreationConstant.TASK_MANAGEMENT_ORIGIN);
				statusConfig.setBusinessStatus(statusDto.getDone());
				statusConfig.setStatus(WorkflowCreationConstant.DONE);
				statusConfigDos.add(statusConfig);
			}
		}
		return statusConfigDos;
	}

	public List<TaskTemplateOwnerDto> createDtoForTaskTemplateOwner(TeamDetailDto team, String ownerId) {
		List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = null;
		TaskTemplateOwnerDto taskTemplateOwner = null;
		try {
			taskTemplateOwnerDtos = new ArrayList<>();
			if (!team.getGroup().isEmpty()) {
				for (String groupId : team.getGroup()) {

					taskTemplateOwner = new TaskTemplateOwnerDto();
					taskTemplateOwner.setId(groupId);
					taskTemplateOwner.setOwnerId(ownerId);
					taskTemplateOwner.setType(WorkflowCreationConstant.GROUP);
					taskTemplateOwner.setIdName(groupDao.getGroupName(groupId));
					taskTemplateOwnerDtos.add(taskTemplateOwner);
				}
			}

			if (!team.getIndividual().isEmpty()) {
				for (String resourceId : team.getIndividual()) {

					taskTemplateOwner = new TaskTemplateOwnerDto();
					taskTemplateOwner.setId(resourceId);
					taskTemplateOwner.setOwnerId(ownerId);
					taskTemplateOwner.setType(WorkflowCreationConstant.INDIVIDUAL);
					taskTemplateOwner.setIdName(userDetailsDao.getUserName(resourceId));
					taskTemplateOwnerDtos.add(taskTemplateOwner);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW CREATION][TASK OWNER TEMPLATE] ERROR" + e.getMessage());
		}
		return taskTemplateOwnerDtos;
	}

	public void deleteInProcessTemplate(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInPT = "DELETE FROM PROCESS_TEMPLATE WHERE PROCESS_NAME IN "
				+ "(SELECT DISTINCT PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName))";
		Query deleteInPTQuery = session.createSQLQuery(deleteInPT);
		deleteInPTQuery.setParameterList("processName", processName);
		deleteInPTQuery.executeUpdate();
		tx.commit();
		session.close();
		
	}

	@SuppressWarnings("unchecked")
	public ProcessTemplate getProcessTemplateByProcessAndName(String processName, String taskName) {
		if (!ServicesUtil.isEmpty(processName)) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ProcessTemplate.class);
			if (!ServicesUtil.isEmpty(taskName)) {
				criteria.add(Restrictions.eq("taskName", taskName));
			}
			criteria.add(Restrictions.eq("processName", processName));

			ProcessTemplate processTemplate = (ProcessTemplate) criteria.uniqueResult();
			tx.commit();
			session.close();
			return processTemplate;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ProcessTemplate getProcessTemplateById(String templateId, String processName) {
		if (!ServicesUtil.isEmpty(templateId)) {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(ProcessTemplate.class);
			criteria.add(Restrictions.eq("templateId", templateId));
			criteria.add(Restrictions.eq("processName", processName));

			ProcessTemplate processTemplate = (ProcessTemplate) criteria.uniqueResult();
			tx.commit();
			session.close();
			return processTemplate;
		}
		return null;
	}

}
