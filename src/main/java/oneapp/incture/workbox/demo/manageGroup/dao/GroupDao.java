package oneapp.incture.workbox.demo.manageGroup.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupDetailDto;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupDto;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupResponseDto;
import oneapp.incture.workbox.demo.manageGroup.dto.UserDetailDto;
import oneapp.incture.workbox.demo.manageGroup.entity.GroupDo;
import oneapp.incture.workbox.demo.manageGroup.entity.GroupDoPk;
import oneapp.incture.workbox.demo.manageGroup.util.ManageGroupConstant;


@Repository
public class GroupDao extends BaseDao<GroupDo, GroupDto>{
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	@Override
	protected GroupDo importDto(GroupDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		GroupDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getGroupId())
				&& !ServicesUtil.isEmpty(fromDto.getUserId())){
			entity = new GroupDo();
			entity.setGroupDoPk(new GroupDoPk(fromDto.getGroupId(), fromDto.getUserId()));
			if (!ServicesUtil.isEmpty(fromDto.getGroupName()))
				entity.setGroupName(fromDto.getGroupName());
			if (!ServicesUtil.isEmpty(fromDto.getGroupType()))
				entity.setGroupType(fromDto.getGroupType());
			if (!ServicesUtil.isEmpty(fromDto.getUserName()))
				entity.setUserName(fromDto.getUserName());
		}
		return entity;
	}

	@Override
	protected GroupDto exportDto(GroupDo entity) {
		GroupDto groupDto = new GroupDto();
		groupDto.setGroupId(entity.getGroupDoPk().getGroupId());
		groupDto.setUserId(entity.getGroupDoPk().getUserId());
		if (!ServicesUtil.isEmpty(entity.getGroupName()))
			groupDto.setGroupName(entity.getGroupName());
		if (!ServicesUtil.isEmpty(entity.getGroupType()))
			groupDto.setGroupType(entity.getGroupType());
		if (!ServicesUtil.isEmpty(entity.getUserName()))
			groupDto.setUserName(entity.getUserName());
		return groupDto;
	}

	public void saveOrUpdateGroup(List<GroupDto> groups) {
		
		try{
			if (!ServicesUtil.isEmpty(groups) && !groups.isEmpty()) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < groups.size(); i++) {
					GroupDto currentTask = groups.get(i);
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING GROUPS] ERROR:"+e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public GroupResponseDto getAllGroup(String name) {

		List<GroupDetailDto> groupListDto = null;
		GroupDetailDto groupDetailDto = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setStatus(ManageGroupConstant.FAILURE);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);

		try{
			if(name == null)
				name = "";

			String groupDetail = "SELECT GROUP_ID, GROUP_NAME FROM GROUPS WHERE LOWER(GROUP_NAME) LIKE LOWER('%"+name+"%') ";
			String groupBy = " GROUP BY GROUP_ID, GROUP_NAME";
			Query groupQry = getSession().createSQLQuery(groupDetail+groupBy);
			List<Object[]> groupsDetail = groupQry.list();

			groupListDto = new ArrayList<>();
			for (Object[] obj : groupsDetail) {
				groupDetailDto = new GroupDetailDto();
				groupDetailDto.setGroupId(obj[0].toString());
				groupDetailDto.setGroupName(obj[1].toString());

				groupListDto.add(groupDetailDto);
			}

		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX] [GROUP FETCHINNG FAILED]"+e.getMessage());
		}
		resp.setMessage("Group Detail Fetched");
		resp.setStatus(ManageGroupConstant.SUCCESS);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_SUCCESS);
		GroupResponseDto responseDto = new GroupResponseDto();
		responseDto.setResponseMessage(resp);
		responseDto.setDto(groupListDto);
		return responseDto;

	}

	@SuppressWarnings("unchecked")
	public GroupDetailDto getGroupDetail(String groupId) {

		GroupDetailDto groupDetailDto = null;
		List<UserDetailDto> userDetailList = null;
		UserDetailDto userDetailDto = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setStatus(ManageGroupConstant.FAILURE);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);

		try{
			Query groupQuery = getSession().createSQLQuery("SELECT GROUP_ID, USER_ID,USER_NAME,GROUP_NAME"
					+ " FROM GROUPS WHERE GROUP_ID = '"+groupId+"'");
			
			System.err.println("groupQuery"+groupQuery);
			List<Object[]> groupDetail = groupQuery.list();

			System.err.println("groupDetail"+groupDetail);
			userDetailList = new ArrayList<>();
			for (Object[] obj : groupDetail) {
				userDetailDto = new UserDetailDto();
				userDetailDto.setUserId(obj[1].toString());
				userDetailDto.setUserName(obj[2].toString());
				userDetailDto.setIsEdited(0);
				userDetailList.add(userDetailDto);

			}

			groupDetailDto = new GroupDetailDto();
			groupDetailDto.setUserDetail(userDetailList);
			groupDetailDto.setGroupId(groupDetail.get(0)[0].toString());
			groupDetailDto.setGroupName(groupDetail.get(0)[3].toString());
			resp.setMessage("Group Detail Fetched");
			resp.setStatus(ManageGroupConstant.SUCCESS);
			resp.setStatusCode(ManageGroupConstant.STATUS_CODE_SUCCESS);
			groupDetailDto.setResponseMessage(resp);

		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX] [GROUP DETAIL] ERROR ON GROUP DETAIL"+e.getMessage());
		}


		return groupDetailDto;
	}

	public ResponseMessage createCustomGroup(GroupDetailDto groupDetailDto) {

		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);

		try{

			String res = checkGroupNameAlreadyExists(groupDetailDto.getGroupName());
			if(res.equals(ManageGroupConstant.FAILURE))
			{
				resp.setMessage("Group Name Already Exists");
				return resp;
			}

			resp.setMessage(groupDetailDto.getGroupName()+" Created");
			resp.setStatus(ManageGroupConstant.SUCCESS);
			resp.setStatusCode(ManageGroupConstant.STATUS_CODE_SUCCESS);

		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX] [GROUP CREATION] ERROR"+e.getMessage());
		}

		return resp;
	}

	public String checkGroupNameAlreadyExists(String groupName) {
		String result = ManageGroupConstant.SUCCESS;
		try{
			Query groupqry = getSession().createSQLQuery("SELECT COUNT(*) FROM GROUPS WHERE GROUP_NAME = '"+groupName+"'");
			BigInteger count = (BigInteger) groupqry.uniqueResult();
			
			if(count.intValue()>0)
				result = ManageGroupConstant.FAILURE;
			return result;
		}catch (Exception e) {
			System.err.println("[WBP-Dev]GroupDao.checkGroupNameAlreadyExists()"+e.getMessage());
			return ManageGroupConstant.FAILURE;
		}
	}

	public void deleteFromGroup(String deleteString, String groupId) {
		try{
			
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			String deleteStr = "DELETE FROM GROUPS WHERE GROUP_ID = '"+groupId+"'"
					+ " AND USER_ID IN ("+deleteString.substring(0, deleteString.length()-1)+")";
			System.err.println(deleteStr);
			Query deleteQry = session.createSQLQuery(deleteStr);
			deleteQry.executeUpdate();
			tx.commit();
			session.close();
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-PRODUCT][MANAGE GROUP]ERROR"+e);
			e.printStackTrace();
		}
		
	}
	
	public String getGroupName(String groupId) {
		try{
			
			Query fetchNameQry = this.getSession().createSQLQuery("SELECT DISTINCT GROUP_NAME FROM GROUPS WHERE GROUP_ID = '"+groupId+"'");
			String name = (String) fetchNameQry.uniqueResult();
			return name;
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-PRODUCT][MANAGE GROUP]ERROR"+e.getMessage());
		}
		return null;
	}

}
