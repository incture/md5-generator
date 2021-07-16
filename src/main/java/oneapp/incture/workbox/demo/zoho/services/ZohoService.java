package oneapp.incture.workbox.demo.zoho.services;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;



public interface ZohoService {

	public List<ProcessEventsDto> setProcess(Map<String, UserIDPMappingDto> userList, String accessTokenString,
			JSONArray processArray , String processName);

	public Map<String, UserIDPMappingDto> fetchUsers(int type);

	public List<TaskEventsDto> setTask(ProcessEventsDto processEventsDto, JSONObject approvalObject,
			Map<String, UserIDPMappingDto> userList, String accesTokenString , String processName);

	public ResponseMessage setAll(String processName);

	public String getUserbyZohoEmail(String email, String accessTokenString);

	public List<TaskOwnersDto> setOwner(ProcessEventsDto processEventsDto, JSONObject approvalObject,
			Map<String, UserIDPMappingDto> userList, String accessTokenString);

	public List<CustomAttributeValue> setCustomAttributeValues(Object object, JSONObject approvalObject,
			Map<String, UserIDPMappingDto> userList , String processName , List<String> attributes , JSONArray leaveTypeArray);
	
	public List<String> getCustomAttributeValues(String processName);

	public String setLeaveBalance(JSONArray leaveTypeArray, String leaveTypeName);
}
