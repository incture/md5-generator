package oneapp.incture.workbox.demo.manageGroup.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.manageGroup.dao.GroupDao;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupDetailDto;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupDto;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupResponseDto;
import oneapp.incture.workbox.demo.manageGroup.dto.UpdateDetailDto;
import oneapp.incture.workbox.demo.manageGroup.util.GroupParse;
import oneapp.incture.workbox.demo.manageGroup.util.GroupValidation;
import oneapp.incture.workbox.demo.manageGroup.util.ManageGroupConstant;

@Service
//////@Transactional
public class ManageGroupServiceImpl implements ManageGroupService{

	@Autowired
	private GroupDao groupDao;

	@Autowired
	private GroupValidation validate;

	@Autowired
	private GroupParse groupParse;

	public GroupResponseDto getAllGroup(String type,String name) {
		GroupResponseDto groupResponseDto = null;

		if(type.equals(ManageGroupConstant.CUSTOM))
			groupResponseDto = groupDao.getAllGroup(name);
		if(type.equals(ManageGroupConstant.IDP))
			groupResponseDto = groupParse.getIdpGroups();
		return groupResponseDto;

	}

	public GroupDetailDto getGroupDetail(String type,String groupId) {
		GroupDetailDto groupDetailDto = null;

		if(type.equals(ManageGroupConstant.CUSTOM))
			groupDetailDto = groupDao.getGroupDetail(groupId);
		if(type.equals(ManageGroupConstant.IDP))
			groupDetailDto = groupParse.getIdpGroupsById(groupId);

		return groupDetailDto;

	}

	public ResponseMessage createCustomGroup(GroupDetailDto groupDetailDto)
	{
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setStatus(ManageGroupConstant.FAILURE);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);
		try{
			System.err.println("[WBP-Dev]in");
			resp = validate.validateGroup(groupDetailDto);
			if(ManageGroupConstant.FAILURE.equals(resp.getStatus()))
				return resp;

			String res = groupDao.checkGroupNameAlreadyExists(groupDetailDto.getGroupName());
			if(res.equals(ManageGroupConstant.FAILURE))
			{
				resp.setMessage("Group Name Already Exists");
				resp.setStatus(ManageGroupConstant.FAILURE);
				resp.setStatusCode("1");
				return resp;
			}

			if(res.equals(ManageGroupConstant.SUCCESS))
			{
				List<GroupDto> groups = groupParse.parseCustomGroup(groupDetailDto);

				groupDao.saveOrUpdateGroup(groups);
			}
			resp.setMessage(ManageGroupConstant.SUCCESS);
			resp.setStatus(ManageGroupConstant.SUCCESS);
			resp.setStatusCode(ManageGroupConstant.STATUS_CODE_SUCCESS);

		}catch (Exception e) {
			System.err.println("[WBP-Dev]Create Custom Group Error"+e);
		}

		return resp;
	}

	@Override
	public ResponseMessage updateCustomGroup(GroupDetailDto groupDetailDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setStatus(ManageGroupConstant.FAILURE);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);

		try{
			resp = validate.validateGroupUpdation(groupDetailDto);
			System.err.println(resp);
			if(!ManageGroupConstant.FAILURE.equals(resp.getStatus()))
			{
				resp.setStatus(ManageGroupConstant.FAILURE);
				resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);
				resp.setMessage("All members cannot be deleted");
				return resp;
			}

			UpdateDetailDto response = groupParse.updateResponseGenerator(groupDetailDto);

			if(!response.getGroupdetail().isEmpty())
				groupDao.saveOrUpdateGroup(response.getGroupdetail());

			if(!"".equals(response.getDeleteString()))
				groupDao.deleteFromGroup(response.getDeleteString(),groupDetailDto.getGroupId());


			resp.setMessage(ManageGroupConstant.SUCCESS);
			resp.setStatus(ManageGroupConstant.SUCCESS);
			resp.setStatusCode(ManageGroupConstant.STATUS_CODE_SUCCESS);

		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-PRODUCT][MANAGE GROUP]ERROR"+e.getMessage());
		}
		return resp;
	}
}
