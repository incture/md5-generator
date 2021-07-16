package oneapp.incture.workbox.demo.workflow.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("AdhocTaskTemplate")
public class TaskTemplateDao{

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}
	//
//	@Autowired
//	private TaskTemplateOwnerDao taskTemplateOwnerDao;
//
//	@Autowired
//	private GroupDao groupDao;
//
//	@Autowired
//	private UserDetailsDao userDetailsDao;
//
//	@Override
//	protected TaskTemplate importDto(TaskTemplateDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
//		TaskTemplate entity = null;
//		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getProcessName())
//				&& !ServicesUtil.isEmpty(fromDto.getStepNumber())){
//			entity = new TaskTemplate();
//			entity.setTaskTemplateDoPk(new TaskTemplateDoPk(fromDto.getProcessName(), fromDto.getStepNumber()));
//			if (!ServicesUtil.isEmpty(fromDto.getOwnerId()))
//				entity.setOwnerId(fromDto.getOwnerId());
//			if (!ServicesUtil.isEmpty(fromDto.getTaskName()))
//				entity.setTaskName(fromDto.getTaskName());
//			if (!ServicesUtil.isEmpty(fromDto.getTaskType()))
//				entity.setTaskType(fromDto.getTaskType());
//			if (!ServicesUtil.isEmpty(fromDto.getRunTimeUser()))
//				entity.setRunTimeUser(fromDto.getRunTimeUser());
//			if (!ServicesUtil.isEmpty(fromDto.getCustomKey()))
//				entity.setCustomKey(fromDto.getCustomKey());
//		}
//		return entity;
//	}
//
//	@Override
//	protected TaskTemplateDto exportDto(TaskTemplate entity) {
//		TaskTemplateDto templateDto = new TaskTemplateDto();
//		templateDto.setProcessName(entity.getTaskTemplateDoPk().getProcessName());
//		templateDto.setStepNumber(entity.getTaskTemplateDoPk().getStepNumber());
//		if (!ServicesUtil.isEmpty(entity.getOwnerId()))
//			templateDto.setOwnerId(entity.getOwnerId());
//		if (!ServicesUtil.isEmpty(entity.getTaskName()))
//			templateDto.setTaskName(entity.getTaskName());
//		if (!ServicesUtil.isEmpty(entity.getTaskType()))
//			templateDto.setTaskType(entity.getTaskType());
//		if (!ServicesUtil.isEmpty(entity.getRunTimeUser()))
//			templateDto.setRunTimeUser(entity.getRunTimeUser());
//		if (!ServicesUtil.isEmpty(entity.getCustomKey()))
//			templateDto.setCustomKey(entity.getCustomKey());
//		return templateDto;
//	}
//
//	public void saveOrUpdateTaskTemplate(List<TaskTemplateDto> taskTempaltes) {
//		Session session = null;
//		try{
//			if (!ServicesUtil.isEmpty(taskTempaltes) && !taskTempaltes.isEmpty()) {
//				session = this.getSession();
//				for (int i = 0; i < taskTempaltes.size(); i++) {
//					TaskTemplateDto currentTask = taskTempaltes.get(i);
//					session.saveOrUpdate(importDto(currentTask));
//					if (i % 20 == 0 && i > 0) {
//						session.flush();
//						session.clear();
//					}
//				}
//				session.flush();
//				session.clear();
//			}
//		}catch (Exception e) {
//			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING TASK TEMPLATE] ERROR:"+e.getMessage());
//		}
//	}
//
//	public String updateTaskTemplate(CustomProcessCreationDto updateProcessDto) {
//
//		Integer count = new Integer(0);
//		StringBuilder ownerId = null;
//		String result = WorkflowCreationConstant.FAILURE;
//		Integer stepNumber = new Integer(0);
//		List<TaskTemplateDto> templateDtos = null;
//		TaskTemplateDto taskTemplateDto = null;
//		List<TaskTemplateOwnerDto> templateOwnerDtos = null;
//
//		try{
//			templateOwnerDtos = new ArrayList<>();
//			templateDtos = new ArrayList<>();
//			for (TeamDetailDto team : updateProcessDto.getTeamDetailDto()) {
//				count++;
//				if(WorkflowCreationConstant.TWO.equals(team.getIsEdited()))
//				{
//					stepNumber++;
//					ownerId = new StringBuilder("O");
//					Random rn = new Random();
//					int randRequest = (int) (1000 + rn.nextInt() * (9999 - 1000) + 1);
//					ownerId.append(randRequest);
//
//					taskTemplateDto = new TaskTemplateDto();
//					taskTemplateDto.setProcessName(team.getProcessName().trim());
//					taskTemplateDto.setStepNumber(stepNumber);
//					taskTemplateDto.setTaskName(team.getEventName());
//					taskTemplateDto.setRunTimeUser((team.getRunTimeUser() == 1) ? true : false);
//					if(!taskTemplateDto.getRunTimeUser())
//					{
//						taskTemplateDto.setOwnerId(ownerId.toString());
//						templateOwnerDtos.addAll(createDtoForTaskTemplateOwner(team, ownerId.toString()));
//					}
//					taskTemplateDto.setTaskType(team.getTaskType());
//					taskTemplateDto.setCustomKey(ServicesUtil.isEmpty(team.getCustomKey())?null:team.getCustomKey());
//					templateDtos.add(taskTemplateDto);
//
//					count--;
//
//				}
//				else if(WorkflowCreationConstant.ONE.equals(team.getIsEdited()))
//				{
//					stepNumber++;
//
//					Query updateTaskTemplate = getSession().createSQLQuery("UPDATE TASK_TEMPLATE SET TASK_NAME= :val1,"
//							+ " TASK_TYPE= :val2, STEP_NUMBER= :val5, RUN_TIME_USER= :val6, CUSTOM_KEY= :val7 "
//							+ "WHERE PROCESS_NAME= :val3 AND STEP_NUMBER= :val4");
//					updateTaskTemplate.setParameter("val1", team.getEventName());
//					updateTaskTemplate.setParameter("val2", team.getTaskType());
//					updateTaskTemplate.setParameter("val3", team.getProcessName());
//					updateTaskTemplate.setParameter("val4", count);
//					updateTaskTemplate.setParameter("val5", stepNumber);
//					updateTaskTemplate.setParameter("val6", (team.getRunTimeUser() == 1) ? true : false);
//					if(team.getRunTimeUser()==1)
//						updateTaskTemplate.setParameter("val7", team.getCustomKey());
//					else
//						updateTaskTemplate.setParameter("val7", null);
//
//					updateTaskTemplate.executeUpdate();
//				}
//				else if(WorkflowCreationConstant.FOUR.equals(team.getIsEdited())){
//
//					stepNumber++;
//					ownerId = new StringBuilder("O");
//					Random rn = new Random();
//					int randRequest = (int) (1000 + rn.nextInt() * (9999 - 1000) + 1);
//					ownerId.append(randRequest);
//
//					if(team.getRunTimeUser()!=1)
//					{
//						System.err.println("in");
//						templateOwnerDtos.addAll(createDtoForTaskTemplateOwner(team, ownerId.toString()));
//					}
//
//					Query updateTaskTemplate = getSession().createSQLQuery("UPDATE TASK_TEMPLATE SET TASK_NAME= :val1,"
//							+ " TASK_TYPE= :val2,OWNER_ID= :val5, STEP_NUMBER= :val6, RUN_TIME_USER= :val7 , CUSTOM_KEY= :val8 "
//							+ "WHERE PROCESS_NAME= :val3 AND STEP_NUMBER= :val4");
//					updateTaskTemplate.setParameter("val1", team.getEventName());
//					updateTaskTemplate.setParameter("val2", team.getTaskType());
//					updateTaskTemplate.setParameter("val3", team.getProcessName());
//					updateTaskTemplate.setParameter("val4", count);
//					if(team.getRunTimeUser()==1)
//						updateTaskTemplate.setParameter("val5",null);
//					else
//						updateTaskTemplate.setParameter("val5",ownerId.toString());
//					updateTaskTemplate.setParameter("val6", stepNumber);
//					updateTaskTemplate.setParameter("val7", (team.getRunTimeUser() == 1) ? true : false);
//					if(team.getRunTimeUser()==1)
//						updateTaskTemplate.setParameter("val8", team.getCustomKey());
//					else
//						updateTaskTemplate.setParameter("val8", null);
//
//					updateTaskTemplate.executeUpdate();
//
//				}
//				else if(WorkflowCreationConstant.THREE.equals(team.getIsEdited())){
//
//					System.err.println("[WBP-Dev]in delete");
//					Query deleteTask = getSession().createSQLQuery("DELETE FROM TASK_TEMPLATE WHERE PROCESS_NAME= :val1"
//							+ " AND TASK_NAME= :val2 AND STEP_NUMBER= :val3");
//					deleteTask.setParameter("val1", team.getProcessName());
//					deleteTask.setParameter("val2", team.getEventName());
//					deleteTask.setParameter("val3", count);
//
//					deleteTask.executeUpdate();
//				}
//				else if(WorkflowCreationConstant.ZERO.equals(team.getIsEdited())){
//
//					stepNumber++;
//					System.err.println("[WBP-Dev]Step"+stepNumber+count);
//					Query updateTaskTemplate = getSession().createSQLQuery("UPDATE TASK_TEMPLATE SET STEP_NUMBER= :val6 "
//							+ "WHERE PROCESS_NAME= :val3 AND STEP_NUMBER= :val4");
//					updateTaskTemplate.setParameter("val3", team.getProcessName());
//					updateTaskTemplate.setParameter("val4", count);
//					updateTaskTemplate.setParameter("val6", stepNumber);
//
//					updateTaskTemplate.executeUpdate();
//				}
//			}
//			saveOrUpdateTaskTemplate(templateDtos);
//			taskTemplateOwnerDao.saveOrUpdateTaskTemplateOwners(templateOwnerDtos);
//			result = WorkflowCreationConstant.SUCCESS;
//		}catch (Exception e) {
//			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW UPDATION][TASK TEMPLATE]:"+e.getMessage());
//		}
//		return result;
//	}
//
//	public void deleteInTaskTemplate(String processName) {
//		String deleteInTT = "DELETE FROM TASK_TEMPLATE WHERE PROCESS_NAME IN "
//				+ "(SELECT DISTINCT PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName))";
//		Query  deleteInTTQuery= getSession().createSQLQuery(deleteInTT);
//		deleteInTTQuery.setParameter("processName", processName);
//		deleteInTTQuery.executeUpdate();
//	}
//
//	public List<TaskTemplateOwnerDto> createDtoForTaskTemplateOwner(TeamDetailDto team,
//			String ownerId) {
//		List<TaskTemplateOwnerDto> taskTemplateOwnerDtos = null;
//		TaskTemplateOwnerDto taskTemplateOwner = null;
//		try{
//			taskTemplateOwnerDtos = new ArrayList<>();
//			if(!team.getGroup().isEmpty())
//			{
//				for (String groupId : team.getGroup()) {
//
//					taskTemplateOwner = new TaskTemplateOwnerDto();
//					taskTemplateOwner.setId(groupId);
//					taskTemplateOwner.setOwnerId(ownerId);
//					taskTemplateOwner.setType(WorkflowCreationConstant.GROUP);
//					taskTemplateOwner.setIdName(groupDao.getGroupName(groupId));
//					taskTemplateOwnerDtos.add(taskTemplateOwner);
//				}
//			}
//
//			if(!team.getIndividual().isEmpty() )
//			{
//				for (String resourceId : team.getIndividual()) {
//
//					taskTemplateOwner = new TaskTemplateOwnerDto();
//					taskTemplateOwner.setId(resourceId);
//					taskTemplateOwner.setOwnerId(ownerId);
//					taskTemplateOwner.setType(WorkflowCreationConstant.INDIVIDUAL);
//					taskTemplateOwner.setIdName(userDetailsDao.getUserName(resourceId));
//					taskTemplateOwnerDtos.add(taskTemplateOwner);
//				}
//			}
//		}catch (Exception e) {
//			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW CREATION][TASK OWNER TEMPLATE] ERROR"+e.getMessage());
//		}
//		return taskTemplateOwnerDtos;
//	}
//
	@SuppressWarnings("unchecked")
	public List<String> geProcessCustomKeys(String processName) {
		
		Query fetchCustomKeyQry = this.getSession().createSQLQuery("SELECT CUSTOM_KEY FROM PROCESS_TEMPLATE "
				+ "WHERE PROCESS_NAME ='"+processName+"' AND RUN_TIME_USER = 1");
		List<String> customKeys = (List<String>) fetchCustomKeyQry.list();
		return customKeys;
	}
//
//	public List<String> getCustomKeysByEventId(String eventId) {
//
//		Query fetchCustomKeyQry = this.getSession().createSQLQuery("SELECT CUSTOM_KEY FROM PROCESS_TEMPLATE_VALUE "
//				+ " WHERE PROCESS_ID IN "
//				+ "(SELECT TE.PROCESS_ID FROM TASK_EVENTS TE WHERE TE.EVENT_ID = '"+eventId+"') AND RUN_TIME_USER = TRUE");
//		List<String> customKeys = (List<String>) fetchCustomKeyQry.list();
//		return customKeys;
//	}
//
}
