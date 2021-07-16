package oneapp.incture.workbox.demo.adhocTask.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.TaskValueDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ValueListDto;
import oneapp.incture.workbox.demo.adhocTask.entity.TaskValueDo;
import oneapp.incture.workbox.demo.workflow.entity.OwnerSequenceTypeDo;

@Repository("adhocTaskValueDao")
public class TaskValueDao extends BaseDao<TaskValueDo, TaskValueDto> {
	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	TaskTemplateOwnerDao taskTemplateOwnerDao;
	
	@Autowired
	private AfeNexusOrderDao afeNexusOrderDao;

	@Override
	protected TaskValueDo importDto(TaskValueDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		TaskValueDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getProcessId())
				&& !ServicesUtil.isEmpty(fromDto.getTaskName())) {
			entity = new TaskValueDo();
			// entity.setTaskValueDoPk(new TaskValueDoPk(fromDto.getProcessId(),
			// fromDto.getStepNumber()));
			entity.setProcessId(fromDto.getProcessId());
			entity.setStepNumber(fromDto.getStepNumber());
			if (!ServicesUtil.isEmpty(fromDto.getTaskType()))
				entity.setTaskType(fromDto.getTaskType());
			if (!ServicesUtil.isEmpty(fromDto.getOwnerId()))
				entity.setOwnerId(fromDto.getOwnerId());
			if (!ServicesUtil.isEmpty(fromDto.getTaskName()))
				entity.setTaskName(fromDto.getTaskName());
			if (!ServicesUtil.isEmpty(fromDto.getDescription()))
				entity.setDescription(fromDto.getDescription());

		}
		return entity;
	}

	@Override
	protected TaskValueDto exportDto(TaskValueDo entity) {
		TaskValueDto taskValueDto = new TaskValueDto();
		taskValueDto.setProcessId(entity.getProcessId());
		taskValueDto.setStepNumber(entity.getStepNumber());
		if (!ServicesUtil.isEmpty(entity.getTaskType()))
			taskValueDto.setTaskType(entity.getTaskType());
		if (!ServicesUtil.isEmpty(entity.getOwnerId()))
			taskValueDto.setOwnerId(entity.getOwnerId());
		if (!ServicesUtil.isEmpty(entity.getTaskName()))
			taskValueDto.setTaskName(entity.getTaskName());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			taskValueDto.setDescription(entity.getDescription());

		return taskValueDto;
	}

	public void saveOrUpdateTaskValues(List<TaskValueDto> taskValueDtos) {
		Session session = null;
		try {
			if (!ServicesUtil.isEmpty(taskValueDtos) && taskValueDtos.size() > 0) {
				session = this.getSession();
				for (int i = 0; i < taskValueDtos.size(); i++) {
					TaskValueDto currentTask = taskValueDtos.get(i);
					session.saveOrUpdate(importDto(currentTask));
					if (i % 20 == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING TASK VALUES] ERROR:" + e.getMessage());
		}
	}

	// @SuppressWarnings("unchecked")
	// public AttributesResponseDto saveAllTaskValue(AttributesResponseDto
	// tasksToSubmit, List<String> processIdList) {
	//
	// TaskValueDo taskValueDo = null;
	// Transaction tx = null;
	// Integer count = 0;
	// Integer processIndex = 0;
	//
	// Session session = sessionFactory.openSession();
	//
	// tx = session.beginTransaction();
	//
	// String selectTaskTemplate = "select distinct
	// tt.owner_id,tt.task_name,tt.step_number,tt.run_time_user,tt.task_type"
	// + " from task_template tt "
	// +"join process_config_tb pct on pct.process_name = tt.process_name "
	// +"join task_template_owner tot on tt.owner_id =tot.owner_id "
	// + "where pct.process_name=
	// '"+tasksToSubmit.getListOfProcesssAttributes().get(0).getCustomAttributeTemplateDto().get(0).getProcessName()+"'
	// ";
	//
	// Query taskTemplateQry = getSession().createSQLQuery(selectTaskTemplate);
	//
	// List <Object[]> taskOwnerDetailList = taskTemplateQry.list();
	//
	//
	// for (ProcessAttributesDto instanceDetail :
	// tasksToSubmit.getListOfProcesssAttributes()) {
	//
	// for (Object[] taskOwnerDetail : taskOwnerDetailList) {
	//
	// taskValueDo = new TaskValueDo();
	//
	// if(((Boolean)taskOwnerDetail[3])){
	//
	// for (CustomAttributeTemplateDto attribute :
	// instanceDetail.getCustomAttributeTemplateDto()) {
	//
	// if(TaskCreationConstant.DROPDOWN.equals(attribute.getDataType()) &&
	// attribute.getDataTypeKey().equals(1)
	// && attribute.getKey().contains((String)taskOwnerDetail[1]+" Owner")){
	//
	// if(attribute.getValueList().isEmpty())
	// {
	// taskValueDo.setValue(taskOwnerDetail[0].toString());
	// String ownerNames =
	// taskTemplateOwnerDao.getOwnersInString(taskOwnerDetail[0].toString());
	// attribute.setValue(ownerNames);
	// break;
	// }
	// else{
	//
	// String ownerId =
	// taskTemplateOwnerDao.createOwnerId(attribute.getValueList());
	//
	// taskValueDo.setValue(ownerId);
	// attribute.setValue(taskTemplateOwnerDao.getOwnersInString(ownerId));
	// break;
	// }
	// }
	// }
	// }
	// else{
	// taskValueDo.setValue(taskOwnerDetail[0].toString());
	// }
	//
	// taskValueDo.setProcessId(processIdList.get(processIndex));
	// taskValueDo.setStepNumber((Integer) taskOwnerDetail[2]);
	// taskValueDo.setTaskName((String)taskOwnerDetail[1]);
	// taskValueDo.setTaskType((String)taskOwnerDetail[4]);
	//
	// session.save(taskValueDo);
	//
	// if(count % 50 == 0){
	// session.flush();
	// session.clear();
	// }
	//
	// }
	//
	// processIndex++;
	// }
	//
	// tx.commit();
	// session.close();
	//
	// return tasksToSubmit;
	// }

	public TaskValueDo fetchTaskDetail(String processId, String createdBy) {
		
		TaskValueDo taskValueDo = null;
		try {
			Session session =  sessionFactory.openSession();
			String detailSelect = "SELECT TASK_NAME,OWNER_ID,TASK_TYPE,STEP_NUMBER FROM TASK_TEMPLATE_VALUE WHERE PROCESS_ID = '"
					+ processId + "' AND " + " STEP_NUMBER = (SELECT COUNT(*)+1 FROM TASK_EVENTS WHERE PROCESS_ID = '"
					+ processId + "' " + "AND CREATED_BY = '" + createdBy + "')";
			Query detailQry = session.createSQLQuery(detailSelect);
			Object[] taskDetail = (Object[]) detailQry.uniqueResult();
			System.err.println("[WBP-Dev] next task details SQL" + detailSelect + System.currentTimeMillis());
			taskValueDo = new TaskValueDo();
			// taskValueDo.setTaskValueDoPk(new TaskValueDoPk(processId, null));
			taskValueDo.setProcessId(processId);
			taskValueDo.setStepNumber((Integer) taskDetail[3]);
			taskValueDo.setOwnerId(taskDetail[1].toString());
			taskValueDo.setTaskType(taskDetail[2].toString());
			taskValueDo.setTaskName(taskDetail[0].toString());
			System.err.println("[WBP-Dev] next task details" + taskValueDo + System.currentTimeMillis());
			return taskValueDo;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][TASK VALUE] NO NEXT TASK ERROR" + e.getMessage());
		}
		return null;
	}

	public TaskValueDo saveInTaskvalue(List<String> ownerList, String taskName, String processId) {
		List<ValueListDto> valueList = new ArrayList<ValueListDto>();
		TaskValueDo taskValueDo = null;
		for (String userId : ownerList) {
			valueList.add(new ValueListDto(userId, "individual"));
		}

		String ownerId = taskTemplateOwnerDao.createOwnerId(valueList);

		taskValueDo = new TaskValueDo();
		// taskValueDo.setTaskValueDoPk(new TaskValueDoPk(processId, 0));
		taskValueDo.setProcessId(processId);
		taskValueDo.setStepNumber(null);
		taskValueDo.setTaskName(taskName);
		taskValueDo.setTaskType("Accept/Decline");
		taskValueDo.setOwnerId(ownerId);

		return taskValueDo;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getFutureTaskDetailsByProcessId(String processId) {

		Query query = this.getSession().createSQLQuery(
				" select te.step_number , te.task_type, te.task_name , te.description , to.id , to.name from task_template_value te join task_template_owner to on te.owner_id=to.owner_id where te.process_id=:processId order by te.step_Number ");
		query.setParameter("processId", processId);
		return query.list();
	}

//	@SuppressWarnings("unchecked")
//	public List<Object[]> getFutureTaskDetailsByProcessIdForAdhoc(String processId) {
//		List<Object[]> list = new ArrayList<>();
//
//		String targetId = null;
//		Query query = this.getSession().createSQLQuery(
//				" select te.target_id , te.task_type, te.task_name , te.description , to.id , to.name , te.subject from process_template_value te join task_template_owner to on te.owner_id=to.owner_id where te.process_id='"
//						+ processId + "' and te.source_id is null ");
//		list = query.list();
//		if (list != null && list.size() > 0) {
//			Object[] obj = list.get(0);
//			targetId = (obj[0] == null ? null : (String) obj[0]);
//		}
//		while (targetId != null) {
//			query = this.getSession().createSQLQuery(
//					" select te.target_id , te.task_type, te.task_name , te.description , to.id , to.name , te.subject from process_template_value te join task_template_owner to on te.owner_id=to.owner_id where te.process_id='"
//							+ processId + "' and te.template_id= '" + targetId + "' ");
//
//			List<Object[]> listItem = query.list();
//			if (listItem != null && listItem.size() > 0) {
//				Object[] obj = listItem.get(0);
//				targetId = (obj[0] == null ? null : (String) obj[0]);
//			} else {
//				targetId = null;
//			}
//			list.addAll(listItem);
//		}
//
//		return list;
//	}

	public void deleteCurDetail(String processId, Integer stepNumber) {
		Session session = sessionFactory.openSession();
		Transaction tx=session.beginTransaction(); 
		String deleteInTV = "DELETE FROM TASK_TEMPLATE_VALUE WHERE PROCESS_ID= :processId AND STEP_NUMBER= :stepNumber";
		Query deleteInTVQuery = session.createSQLQuery(deleteInTV);
		deleteInTVQuery.setParameter("processId", processId);
		deleteInTVQuery.setParameter("stepNumber", stepNumber);
		deleteInTVQuery.executeUpdate();
		tx.commit();
		session.close();
		

	}
	
	public Boolean getRejectStatus(String processId) {
		Query query = this.getSession().createSQLQuery("select * from task_events where process_id = '"+processId+"' "
				+ "and status = 'REJECT'");
		if(query.list().size()>0)
			return true;
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getFutureTaskDetailsByProcessIdForAdhoc(String processId) {
		List<Object[]> list = new ArrayList<>();

		String targetId = null;
		Query query = null;
		String taskQuery = null;
		OwnerSequenceTypeDo ownerSequenceTypeDo = null;
		String filterQuery = "select tos.* from process_template_value pt join task_owner_template_sequence tos "
				+ "on tos.template_id = pt.template_id where process_id= :processId and source_id is null";
		ownerSequenceTypeDo = (OwnerSequenceTypeDo) this.getSession().createSQLQuery(filterQuery)
				.addEntity(OwnerSequenceTypeDo.class).setParameter("processId", processId).uniqueResult();
		if (!ServicesUtil.isEmpty(ownerSequenceTypeDo)) {
			if (ownerSequenceTypeDo.getOwnerSequType().equals("Group")) {
				taskQuery = "select   te.target_id , te.task_type, te.task_name , te.description, "
						+ " te.subject , te.owner_id ,  te.template_id , tos.owner_sequ_type  from process_template_value te " +
							" join task_owner_template_sequence tos on te.template_id = tos.template_id "
						+ " where te.process_id=:processId and te.source_id is null ";
			} else {
//				taskQuery = " select   te.target_id , te.task_type, te.task_name , te.description , te.subject , te.owner_id , te.template_id , "
//						+ "  tos.owner_sequ_type , to.id , to.name from process_template_value te join task_template_owner to on te.owner_id=to.owner_id "
//						+ " join task_owner_template_sequence tos on te.template_id = tos.template_id "
//						+ " where te.process_id=:processId and te.source_id is null order by to.name "
//						+ ownerSequenceTypeDo.getOrderBy();
				
				//all the group members will have seperate task on story board
				taskQuery = " select te.target_id , te.task_type, te.task_name , te.description , te.subject , te.owner_id , te.template_id , " +
                        " tos.owner_sequ_type , to.id , case when g.user_name is null then to.name else g.user_name end as user_name ,  to.name " +
                        " from process_template_value te join task_template_owner to on te.owner_id=to.owner_id " +
                        " join task_owner_template_sequence tos on te.template_id = tos.template_id full join groups g on g.group_id = to.id " +
                        " where te.process_id=:processId and te.source_id is null order by user_name " + ownerSequenceTypeDo.getOrderBy();
			}
		}
		query = this.getSession().createSQLQuery(taskQuery).setParameter("processId", processId);
		list = query.list();

		if (list != null && list.size() > 0) {
			Object[] obj = list.get(0);
			targetId = (obj[0] == null ? null : (String) obj[0]);
		}
		while (targetId != null) {
			filterQuery = "select do from OwnerSequenceTypeDo do where do.templateId = :templateId";
			ownerSequenceTypeDo = (OwnerSequenceTypeDo) this.getSession().createQuery(filterQuery)
					.setParameter("templateId", targetId).uniqueResult();
			if (!ServicesUtil.isEmpty(ownerSequenceTypeDo)) {

				if (ownerSequenceTypeDo.getOwnerSequType().equals("Group")) {
					taskQuery = "select   te.target_id , te.task_type, te.task_name , te.description, "
							+ " te.subject , te.owner_id , te.template_id , tos.owner_sequ_type  from process_template_value te "
							+ " join task_owner_template_sequence tos on te.template_id = tos.template_id "
							+ " where te.process_id=:processId and te.template_id= :templateId ";
				} else {

//					taskQuery = " select te.target_id , te.task_type, te.task_name , te.description , te.subject , te.owner_id , te.template_id , "
//							+ " tos.owner_sequ_type , to.id , to.name  from process_template_value te join task_template_owner to on te.owner_id=to.owner_id "
//							+ " join task_owner_template_sequence tos on te.template_id = tos.template_id "
//							+ " where te.process_id=:processId and te.template_id= :templateId order by to.name "
//							+ ownerSequenceTypeDo.getOrderBy();
					
					//all the group members will have seperate task on story board
					taskQuery = "  select te.target_id , te.task_type, te.task_name , te.description , te.subject , te.owner_id , te.template_id , " +
                            " tos.owner_sequ_type , to.id , case when g.user_name is null then to.name else g.user_name end as user_name ,  to.name  " +
                            " from process_template_value te join task_template_owner to on te.owner_id=to.owner_id " +
                            " join task_owner_template_sequence tos on te.template_id = tos.template_id full join groups g on g.group_id = to.id " +
                            " where te.process_id=:processId and te.template_id= :templateId order by user_name " + ownerSequenceTypeDo.getOrderBy();
				}
			}

			query = this.getSession().createSQLQuery(taskQuery).setParameter("processId", processId)
					.setParameter("templateId", targetId);

			List<Object[]> listItem = query.list();
			if (listItem != null && listItem.size() > 0) {
				Object[] obj = listItem.get(0);
				targetId = (obj[0] == null ? null : (String) obj[0]);
			} else {
				targetId = null;
			}
			for (Object[] object : listItem) {
				list.add(object);
			}

		}

		return list;

	}
	
	public List<Object[]> getFutureTaskDetailsByProcessIdForAdhocAFE(String processId) {
		List<Object[]> list = new ArrayList<>();
		
		//for task if rejected
		Query statusQry = this.getSession().createSQLQuery("select * from task_events where process_id = '"+processId+"' "
				+ "and status = 'REJECT'");
		if(statusQry.list().size()>0)
			return list;

		String targetId = null;
		Query query = this.getSession().createSQLQuery(
				" select te.target_id , te.task_type, te.task_name , te.description , te.subject , te.owner_id , te.template_id ," 
						+ " tos.owner_sequ_type , to.id , to.name from process_template_value te join task_template_owner to on te.owner_id=to.owner_id " + 
						" join task_owner_template_sequence tos on te.template_id = tos.template_id " +
						" where te.process_id='" +processId + "' and te.source_id is null ");
		list = query.list();
		if (list != null && list.size() > 0) {
			Object[] obj = list.get(0);
			targetId = (obj[0] == null ? null : (String) obj[0]);
		}
		String filterType = afeNexusOrderDao.getFilterForNexus();
		while (targetId != null) {
			String queryStr = "select te.target_id , te.task_type, te.task_name , te.description , te.subject , te.owner_id , te.template_id , "
					+ " tos.owner_sequ_type , case when g.user_id is null then to.id else g.user_id end as user_id , case when g.user_name is null then to.name else g.user_name end as user_name , to.name   from process_template_value te join task_template_owner to on te.owner_id=to.owner_id " 
					+ " join task_owner_template_sequence tos on te.template_id = tos.template_id " 
					+ " full join user_idp_mapping on user_id= to.id  full join groups g on g.group_id = to.id " + " where te.process_id='" + processId
					+ "' and te.template_id= '" + targetId + "'" + " order by ";

			if (filterType.equals("Alphabetical")) {
				queryStr += " user_first_name , user_last_name , afe_nexus_budget ";
			} else {
				queryStr += " afe_nexus_budget ";
				;
			}
			if (filterType.equals("Descending")) {
				queryStr += " DESC";
			}

			query = this.getSession().createSQLQuery(queryStr);

			List<Object[]> listItem = query.list();
			if (listItem != null && listItem.size() > 0) {
				Object[] obj = listItem.get(0);
				targetId = (obj[0] == null ? null : (String) obj[0]);
			} else {
				targetId = null;
			}
			list.addAll(listItem);
		}
		return list;
		
	}


}
