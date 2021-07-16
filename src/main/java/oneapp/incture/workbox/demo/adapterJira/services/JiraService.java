package oneapp.incture.workbox.demo.adapterJira.services;

import java.util.List;

import oneapp.incture.workbox.demo.adapterJira.dto.AttributeGenericDto;
import oneapp.incture.workbox.demo.adapterJira.dto.CommentDto;
import oneapp.incture.workbox.demo.adapterJira.dto.ScreenGenericDto;
import oneapp.incture.workbox.demo.adapterJira.dto.StatusDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;


public interface JiraService {
	
	public List<ProcessEventsDto> setProcess();
	
	public List<TaskEventsDto> setTask();
	
	public List<TaskOwnersDto> setTaskOwner();
	
	public List<CustomAttributeValue> setCustomAttributeValue();
	
	public ResponseMessage setAll();
	
	public ResponseMessage completeTransitionOrForward(ActionDto actionDto, ActionDtoChild actionDtoChild);
	
	public List<StatusDto> setStatusDropdown(String taskId);
	
	public List<AttributeGenericDto> fetchAssignee();
	
	public List<AttributeGenericDto> fetchResolution();
	
	public List<AttributeGenericDto> fetchDefaultRootCause();
	 
	public List<ScreenGenericDto> fetchFields(String processName, String taskId);
	
	public List<CommentDto> fetchComment(String eventId);
	
}
