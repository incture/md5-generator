package oneapp.incture.workbox.demo.manageGroup.services;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupDetailDto;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupResponseDto;

public interface ManageGroupService {

	GroupResponseDto getAllGroup(String type,String groupName);

	ResponseMessage createCustomGroup(GroupDetailDto groupDetailDto);

	GroupDetailDto getGroupDetail(String groupId, String groupId2);

	ResponseMessage updateCustomGroup(GroupDetailDto groupDetailDto);

}
