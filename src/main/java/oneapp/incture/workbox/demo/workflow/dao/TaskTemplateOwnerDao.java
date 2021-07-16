package oneapp.incture.workbox.demo.workflow.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.workflow.dto.StatusDto;
import oneapp.incture.workbox.demo.workflow.dto.TaskTemplateOwnerDto;
import oneapp.incture.workbox.demo.workflow.dto.TeamDetailDto;
import oneapp.incture.workbox.demo.workflow.entity.TaskTemplateOwner;
import oneapp.incture.workbox.demo.workflow.util.WorkflowCreationConstant;

@Repository("TaskTemplateOwnerDao")
public class TaskTemplateOwnerDao extends BaseDao<TaskTemplateOwner, TaskTemplateOwnerDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected TaskTemplateOwner importDto(TaskTemplateOwnerDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		TaskTemplateOwner entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getId())
				&& !ServicesUtil.isEmpty(fromDto.getOwnerId())) {
			entity = new TaskTemplateOwner();
			entity.setId(fromDto.getId());
			entity.setOwnerId(fromDto.getOwnerId());
			if (!ServicesUtil.isEmpty(fromDto.getType()))
				entity.setType(fromDto.getType());
			if (!ServicesUtil.isEmpty(fromDto.getIdName()))
				entity.setIdName(fromDto.getIdName());
		}
		return entity;
	}

	@Override
	protected TaskTemplateOwnerDto exportDto(TaskTemplateOwner entity) {
		TaskTemplateOwnerDto taskTemplateOwnerDto = new TaskTemplateOwnerDto();
		taskTemplateOwnerDto.setId(entity.getId());
		taskTemplateOwnerDto.setOwnerId(entity.getOwnerId());
		if (!ServicesUtil.isEmpty(entity.getType()))
			taskTemplateOwnerDto.setType(entity.getType());
		if (!ServicesUtil.isEmpty(entity.getIdName()))
			taskTemplateOwnerDto.setIdName(entity.getIdName());
		return null;
	}

	public void saveOrUpdateTaskTemplateOwners(List<TaskTemplateOwnerDto> taskOwners) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (!ServicesUtil.isEmpty(taskOwners) && !taskOwners.isEmpty()) {
				
				for (int i = 0; i < taskOwners.size(); i++) {
					TaskTemplateOwnerDto currentTask = taskOwners.get(i);
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING TASK TEMAPLATE OWNER] ERROR:" + e.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked" })
	public List<TeamDetailDto> getTeamDetail(String processName, Map<String, StatusDto> taskStatusConfig) {

		TeamDetailDto teamDetailList = null;
		Map<String, TeamDetailDto> taskDetail = null;
		TeamDetailDto teamDetail = null;
		List<String> teamNameDetail = null;
		List<TeamDetailDto> teamsDetailList = null;

		try {
			String taskDetailQry = "SELECT PT.PROCESS_NAME,PT.TEMPLATE_ID,PT.TASK_NAME,PT.OWNER_ID,	PT.TASK_TYPE,"
					+ "PT.RUN_TIME_USER,PT.CUSTOM_KEY,PT.SUBJECT,PT.DESCRIPTION,PT.SOURCE_ID,PT.TARGET_ID,ID, TYPE,PT.URL,PT.TASK_NATURE "
					+ "FROM PROCESS_TEMPLATE AS PT LEFT JOIN TASK_TEMPLATE_OWNER AS TOT ON TOT.OWNER_ID = PT.OWNER_ID "
					+ "where PROCESS_NAME = '" + processName + "'";
			Query taskDetailQuery = getSession().createSQLQuery(taskDetailQry);
			List<Object[]> teamList = taskDetailQuery.list();

			taskDetail = new LinkedHashMap<>();

			for (Object[] obj : teamList) {

				Integer setRunTime = 0;

				if (taskDetail.containsKey((String) obj[1])) {
					teamDetailList = new TeamDetailDto();

					teamDetailList = taskDetail.get((String) obj[1]);

					if (teamDetailList.getTemplateId().equals(obj[1].toString())) {
						teamDetail = new TeamDetailDto();

						teamDetail = teamDetailList;

						if (WorkflowCreationConstant.GROUP.equals(obj[12]))
							teamDetail.getGroup().add(obj[11].toString());
						else if (WorkflowCreationConstant.INDIVIDUAL.equals(obj[12]))
							teamDetail.getIndividual().add(obj[11].toString());
					}

					taskDetail.replace((String) obj[1], teamDetail);

				} else {
					teamDetail = new TeamDetailDto();
					teamNameDetail = new ArrayList<>();

					teamDetail.setEventName((String) obj[2]);
					teamDetail.setTemplateId((String) obj[1]);
					teamDetail.setDescription(ServicesUtil.isEmpty(obj[8]) ? "" : (String) obj[8]);
					teamDetail.setSubject(ServicesUtil.isEmpty(obj[7]) ? "" : (String) obj[7]);
					if (!ServicesUtil.isEmpty(obj[9]))
						teamDetail.setSourceId(Arrays.asList(((String) obj[9]).split(",")));
					else
						teamDetail.setSourceId(new ArrayList<>());
					if (!ServicesUtil.isEmpty(obj[10]))
						teamDetail.setTargetId(Arrays.asList(((String) obj[10]).split(",")));
					else
						teamDetail.setTargetId(new ArrayList<>());
					teamNameDetail.add(ServicesUtil.isEmpty(obj[11]) ? "" : (String) obj[11]);
					
					if(!ServicesUtil.isEmpty(obj[12])) {
						if (WorkflowCreationConstant.GROUP.equals(obj[12])) {
							teamDetail.setGroup(teamNameDetail);
							teamDetail.setIndividual(new ArrayList<>());
						} else if (WorkflowCreationConstant.INDIVIDUAL.equals(obj[12])) {
							teamDetail.setIndividual(teamNameDetail);
							teamDetail.setGroup(new ArrayList<>());
						} else {
							teamDetail.setIndividual(new ArrayList<>());
							teamDetail.setGroup(new ArrayList<>());
						}
					}
					else {
						teamDetail.setIndividual(new ArrayList<>());
						teamDetail.setGroup(new ArrayList<>());
					}

					teamDetail.setIsEdited(0);
					teamDetail.setProcessName(processName);
					teamDetail.setCustomKey(ServicesUtil.isEmpty(obj[6]) ? null : obj[6].toString());

					if (!ServicesUtil.isEmpty(obj[5])  && ServicesUtil.asBoolean(obj[5]))
						setRunTime = 1;

					teamDetail.setRunTimeUser(setRunTime);
					teamDetail.setTaskType(ServicesUtil.isEmpty(obj[4]) ? "" : (String) obj[4]);
					teamDetail.setUrl(ServicesUtil.isEmpty(obj[13]) ? null : obj[13].toString());
					teamDetail.setTaskNature(ServicesUtil.isEmpty(obj[14]) ? null : obj[14].toString());
					if(taskStatusConfig.containsKey(teamDetail.getEventName()))
						teamDetail.setStatusDto(taskStatusConfig.get(teamDetail.getEventName()));
					taskDetail.put((String) obj[1], teamDetail);
				}
				
			}

			System.err.println(taskDetail);
			teamsDetailList = new ArrayList<>();
			for (Entry<String, TeamDetailDto> entry : taskDetail.entrySet()) {

				teamsDetailList.add(entry.getValue());
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW DETAIL][TASK DETAIL]ERROR:" + e.getMessage());
		}

		return teamsDetailList;

	}

}
