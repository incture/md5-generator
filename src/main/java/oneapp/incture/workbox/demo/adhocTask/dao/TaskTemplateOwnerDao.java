package oneapp.incture.workbox.demo.adhocTask.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.ValueListDto;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.workflow.entity.TaskTemplateOwner;

@Repository("adhocTaskTemplateOwnerDao")
//////@Transactional
public class TaskTemplateOwnerDao {

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	UserIDPMappingDao userIDPMappingDao;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public String createOwnerId(List<ValueListDto> valueList) {

		TaskTemplateOwner taskTemplateOwnerDo = null;
		StringBuilder ownerId = null;
		Session session = null;
		Integer i =1;
		ownerId = new StringBuilder("O");
		Random rn = new Random();
		int randRequest = (int) (1000 + rn.nextInt() * (9999 - 1000) + 1);
		ownerId.append(randRequest);

		session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		for (ValueListDto value : valueList) {

			taskTemplateOwnerDo = new TaskTemplateOwner();

//			taskTemplateOwnerDo.setTaskTemplateOwnerDoPk(new TaskTemplateOwnerDoPk(value.getId(), ownerId.toString()));
			taskTemplateOwnerDo.setId(value.getId());
			taskTemplateOwnerDo.setOwnerId(ownerId.toString());
			taskTemplateOwnerDo.setType(value.getType());
			taskTemplateOwnerDo.setIdName((value.getType().equals("individual")?userIDPMappingDao.getUserName(value.getId()):""));

			session.saveOrUpdate(taskTemplateOwnerDo);
			if (i % 20 == 0 && i > 0) {
				session.flush();
				session.clear();
			}
			i++;
		}
		session.flush();
		session.clear();
		tx.commit();
		session.close();
		
		
		return ownerId.toString();

	}

	@SuppressWarnings("unchecked")
	public List<ValueListDto> getUsersList(String OwnerId) {
		
		ValueListDto valueListDto = null;
		List<ValueListDto> listOfUser = null;
		
		try{
			String owners = "Select ID,TYPE from TASK_TEMPLATE_OWNER where OWNER_ID = '"+OwnerId+"'";
			Query ownerQuery = getSession().createSQLQuery(owners);
			List<Object[]> ownerList = ownerQuery.list();
			
			listOfUser = new ArrayList<ValueListDto>();
			for (Object[] obj : ownerList) {
				valueListDto = new ValueListDto();
				
				valueListDto.setId(obj[0].toString());
				valueListDto.setType(obj[1].toString());
				
				listOfUser.add(valueListDto);
			}
			
		}catch (Exception e) {
			return null;
		}
		
		return listOfUser;
	}

	@SuppressWarnings("unchecked")
	public List<TaskOwnersDto> getOwners(String ownerId) {
		List<TaskOwnersDto> taskOwnerList = null;
		TaskOwnersDto taskOwnersDto = null;
		Session session = sessionFactory.openSession();
		String ownersSelect = "select tot.id,case when tot.type = 'group' then g.user_name else "
				+ "(select user_first_name ||' '|| user_last_name from user_Idp_mapping where user_id = tot.id) "
				+ "end as user_name,case when tot.type = 'group' then g.user_id else tot.id end as ids"
				+ ",case when tot.type = 'group' then "
				+ "(select rp1.USER_EMAIL from user_idp_mapping rp1 where rp1.user_id=g.user_id) else "
				+ "(select USER_EMAIL from user_idp_mapping where user_id = tot.id) end as email,"
				+ "tot.type,g.group_name "
				+ "from task_template_owner tot  "
				+ "full join groups g on g.group_id = tot.id "
				+ "full join user_idp_mapping rp on rp.user_id =  g.user_id "
				+ "where tot.owner_id = '"+ownerId+"'";
		Query ownerQry = session.createSQLQuery(ownersSelect);
		List<Object[]> ownerList = ownerQry.list();
		
		taskOwnerList = new ArrayList<TaskOwnersDto>();
		for (Object[] obj : ownerList) {
			
			taskOwnersDto = new TaskOwnersDto();
			taskOwnersDto.setTaskOwner(obj[2].toString());
			taskOwnersDto.setTaskOwnerDisplayName(obj[1].toString());
			taskOwnersDto.setOwnerEmail(ServicesUtil.isEmpty(obj[3]) ? "" : obj[3].toString());
			if(TaskCreationConstant.GROUP.equals(obj[4].toString()))
			{
				taskOwnersDto.setGroupId(obj[0].toString());
				taskOwnersDto.setGroupOwner(obj[5].toString());
			}
			taskOwnerList.add(taskOwnersDto);
			
		}
		
		return taskOwnerList;
	}

	public String getOwnersInString(String ownerId) {
		
		String selectOwner = "select string_agg(teamName,',') from "
				+ "( select distinct (case when c.type='group' then b.group_name else rp.USER_FIRST_NAME ||' '|| USER_LAST_NAME end) as teamName "
				+ "from GROUPS b  full join TASK_TEMPLATE_OWNER as c on b.GROUP_ID = c.ID "
				+ " full join USER_IDP_MAPPING rp on rp.user_id = c.id "
				+ "where c.owner_id = '"+ownerId+"')";
		Query getOwnerQry = getSession().createSQLQuery(selectOwner);
		String ownerString = (String) getOwnerQry.uniqueResult();
		
		return ownerString;
	}

//	public AttributesResponseDto taskOwnerIdGeneration(AttributesResponseDto tasksToSave) {
//		
//		for (ProcessAttributesDto processAttibutes : tasksToSave.getListOfProcesssAttributes()) {
//			
//			for (CustomAttributeTemplateDto attributes : processAttibutes.getCustomAttributeTemplateDto()) {
//				if(TaskCreationConstant.DROPDOWN.equals(attributes.getDataType()) &&
//						TaskCreationConstant.RESOURCE.equals(attributes.getDropDownType())){
//					attributes.setValue(createOwnerId(attributes.getValueList()));
//				}
//			}
//		}
//		return tasksToSave;
//	}
}
