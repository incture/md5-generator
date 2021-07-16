package oneapp.incture.workbox.demo.manageGroup.util;

import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupDetailDto;
import oneapp.incture.workbox.demo.manageGroup.dto.UserDetailDto;

@Component
public class GroupValidation {

	public ResponseMessage validateGroup(GroupDetailDto groupDetailDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);
		
		if("".equals(groupDetailDto.getGroupName()) || groupDetailDto.getGroupName() == null)
			resp.setMessage("Enter the Group Name");
		
		if(groupDetailDto.getUserDetail().isEmpty())
			resp.setMessage("Please Select the Group Members");
		
		resp.setMessage(ManageGroupConstant.SUCCESS);
		resp.setStatus(ManageGroupConstant.SUCCESS);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_SUCCESS);
		
		return resp;
	}

	public ResponseMessage validateGroupUpdation(GroupDetailDto groupDetailDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setStatus(ManageGroupConstant.SUCCESS);
		resp.setMessage(ManageGroupConstant.SUCCESS);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_SUCCESS);
		
		for ( UserDetailDto res : groupDetailDto.getUserDetail()) {

			if(!res.getIsEdited().equals(3))
			{
				resp.setStatus(ManageGroupConstant.FAILURE);
				resp.setMessage(ManageGroupConstant.FAILURE);
				resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);
				break;
			}
		}
		
		return resp;
	}

}
