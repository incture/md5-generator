package oneapp.incture.workbox.demo.manageGroup.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupDetailDto;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupDto;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupResponseDto;
import oneapp.incture.workbox.demo.manageGroup.dto.UpdateDetailDto;
import oneapp.incture.workbox.demo.manageGroup.dto.UserDetailDto;
//import com.sap.core.connectivity.api.configuration.DestinationConfiguration;


@Component
public class GroupParse {

	public List<GroupDto> parseCustomGroup(GroupDetailDto groupDetailDto) {

		List<GroupDto> groups = null;
		GroupDto groupDto = null;
		try{
			groups = new ArrayList<>();
			String groupId = UUID.randomUUID().toString().replaceAll("-", "");
			for (UserDetailDto grp : groupDetailDto.getUserDetail()) {
				groupDto = new GroupDto();
				groupDto.setGroupId(groupId);
				groupDto.setGroupName(groupDetailDto.getGroupName());
				groupDto.setUserId(grp.getUserId());
				groupDto.setUserName(grp.getUserName());
				groups.add(groupDto);
			}

		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX] [GROUP CREATION] ERROR"+e.getMessage());
		}

		return groups;
	}

	public UpdateDetailDto updateResponseGenerator(GroupDetailDto groupDetailDto) {
		UpdateDetailDto updateDetailDto = null;
		GroupDto groupDto = null;
		StringBuilder deleteName = new StringBuilder("");
		List<GroupDto> groupDtos = new ArrayList<>();
		for (UserDetailDto grp : groupDetailDto.getUserDetail()) {

			if(grp.getIsEdited().equals(2))
			{
				groupDto = new GroupDto();
				groupDto.setGroupName(groupDetailDto.getGroupName());
				groupDto.setGroupId(groupDetailDto.getGroupId());
				groupDto.setUserId(grp.getUserId());
				groupDto.setUserName(grp.getUserName());
				groupDtos.add(groupDto);
				continue;
			}
			if(grp.getIsEdited().equals(3))
			{
				deleteName.append("'"+grp.getUserId()+"',");
			}
		}
		updateDetailDto = new UpdateDetailDto();
		updateDetailDto.setDeleteString(deleteName.toString());
		updateDetailDto.setGroupdetail(groupDtos);

		return updateDetailDto;
	}

	public GroupResponseDto getIdpGroups(){
		GroupResponseDto groupResponseDto = null;
		List<GroupDetailDto> groupListDto = null;
		GroupDetailDto detailDto = null;
		JSONArray groupList = null;
		JSONObject groupDetail = null;
		Integer startIndex = null;
		Integer totalResults = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setStatus(ManageGroupConstant.FAILURE);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);

		try{
//			DestinationConfiguration destConfiguration = ServicesUtil.getDest(ManageGroupConstant.IAS_DESTINATION);

			String url = "";//destConfiguration.getProperty(ManageGroupConstant.DESTINATION_URL);
			String userName = "";//destConfiguration.getProperty(ManageGroupConstant.DESTINATION_USER);
			String password = "";//destConfiguration.getProperty(ManageGroupConstant.DESTINATION_PWD);

			System.err.println("[WBP-Dev]IAS Service base URL : "+url);
			System.err.println("[WBP-Dev]IAS Service user Id : "+userName);
			System.err.println("[WBP-Dev]IAS Service password : "+password);
			groupListDto = new ArrayList<>();
			startIndex = new Integer(0);
			do{
				RestResponse restResponse = IASRestUtil.callIASRestService(url,userName,password,startIndex,null);

				groupList = ((JSONObject)restResponse.getResponseObject()).getJSONArray("Resources");

				totalResults = ((JSONObject)restResponse.getResponseObject()).getInt("totalResults");

				for (Object group : groupList) {
					detailDto = new GroupDetailDto();
					groupDetail = (JSONObject) group;
					detailDto.setGroupId(groupDetail.getString("id"));
					detailDto.setGroupName(groupDetail.getString("displayName"));
					groupListDto.add(detailDto);
				}

				startIndex = startIndex+100;
			}while(totalResults > startIndex);

			resp.setMessage(ManageGroupConstant.SUCCESS);
			resp.setStatus(ManageGroupConstant.SUCCESS);
			resp.setStatusCode(ManageGroupConstant.STATUS_CODE_SUCCESS);
			groupResponseDto = new GroupResponseDto();
			groupResponseDto.setDto(groupListDto);
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX]MANAGE GROUP IAS SERVICE ERROR"+e.getMessage());
		}

		if(groupResponseDto == null){
			groupResponseDto = new GroupResponseDto();
		}
		groupResponseDto.setResponseMessage(resp);
		return groupResponseDto;
	}

	public GroupDetailDto getIdpGroupsById(String groupId) {
		
		List<UserDetailDto> userListDto = null;
		UserDetailDto userDetailDto = null;
		JSONArray userList = null;
		JSONObject userDetail = null;
		Integer startIndex = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(ManageGroupConstant.FAILURE);
		resp.setStatus(ManageGroupConstant.FAILURE);
		resp.setStatusCode(ManageGroupConstant.STATUS_CODE_FAILURE);

		GroupDetailDto detailDto = new GroupDetailDto();
		try{
//			DestinationConfiguration destConfiguration = ServicesUtil.getDest(ManageGroupConstant.IAS_DESTINATION);

			String url = "";//destConfiguration.getProperty(ManageGroupConstant.DESTINATION_URL);
			String userName = "";//destConfiguration.getProperty(ManageGroupConstant.DESTINATION_USER);
			String password = "";//destConfiguration.getProperty(ManageGroupConstant.DESTINATION_PWD);

			System.err.println("[WBP-Dev]IAS Service base URL : "+url);
			System.err.println("[WBP-Dev]IAS Service user Id : "+userName);
			System.err.println("[WBP-Dev]IAS Service password : "+password);

			RestResponse restResponse = IASRestUtil.callIASRestService(url,userName,password,startIndex,groupId);

			if (restResponse.getResponseCode() == 200) {
				
				detailDto.setGroupId(((JSONObject)restResponse.getResponseObject()).optString("id"));
				detailDto.setGroupName(((JSONObject)restResponse.getResponseObject()).optString("displayName"));
				userList = ((JSONObject)restResponse.getResponseObject()).getJSONArray("members");

				userListDto = new ArrayList<>();
				for (Object user : userList) {
					userDetailDto = new UserDetailDto();
					userDetail = (JSONObject) user;
					userDetailDto.setUserId(userDetail.optString("value"));
					userDetailDto.setUserName(userDetail.optString("display"));
					userListDto.add(userDetailDto);
				}
				detailDto.setUserDetail(userListDto);
				resp.setMessage(ManageGroupConstant.SUCCESS);
				resp.setStatus(ManageGroupConstant.SUCCESS);
				resp.setStatusCode(ManageGroupConstant.STATUS_CODE_SUCCESS);
			}

		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX]MANAGE GROUP IAS SERVICE ERROR"+e.getMessage());
		}
		detailDto.setResponseMessage(resp);
		return detailDto;
	}
}
