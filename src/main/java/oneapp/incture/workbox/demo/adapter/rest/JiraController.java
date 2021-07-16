package oneapp.incture.workbox.demo.adapter.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapterJira.dto.AttributeGenericDto;
import oneapp.incture.workbox.demo.adapterJira.dto.CommentDto;
import oneapp.incture.workbox.demo.adapterJira.dto.ScreenGenericDto;
import oneapp.incture.workbox.demo.adapterJira.dto.StatusDto;
import oneapp.incture.workbox.demo.adapterJira.services.JiraService;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;


@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/jira")
public class JiraController {

	@Autowired
	JiraService jiraService;
	
	@RequestMapping(value = "/getProcessEvent", method = RequestMethod.GET)
	public List<ProcessEventsDto> getProcess() {
		List<ProcessEventsDto> res = jiraService.setProcess();
		return res;
	}

	@RequestMapping(value = "/getTaskEvent", method = RequestMethod.GET)
	public List<TaskEventsDto> getTask() {
		List<TaskEventsDto> res = jiraService.setTask();
		return res;
	}

	@RequestMapping(value = "/getTaskOwner", method = RequestMethod.GET)
	public List<TaskOwnersDto> getTaskOwner() {
		List<TaskOwnersDto> res = jiraService.setTaskOwner();
		return res;
	}

	@RequestMapping(value = "/getCAV", method = RequestMethod.GET)
	public List<CustomAttributeValue> getCAV() {
		List<CustomAttributeValue> res = jiraService.setCustomAttributeValue();
		return res;
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseMessage getAll() {
		ResponseMessage res = jiraService.setAll();
		return res;
	}

//	@RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
//	public Map<String, UserIDPMappingDto> getAllUsers() {
//		Map<String, UserIDPMappingDto> userDetails = jiraService.fetchUsers(0);
//		return userDetails;
//	}

	@RequestMapping(value = "/actions", method = RequestMethod.POST, produces = "application/json")
     public ResponseMessage taskActions(@RequestBody ActionDto dto)  {
         ResponseMessage resp = null;
         System.err.println("[WBP-Dev][WORKBOX][WorkboxRest][action][dto]" + dto.toString());
        
         for (ActionDtoChild childDto : dto.getTask()) {
             if (!ServicesUtil.isEmpty(childDto.getActionType())) {
            	 
                 resp = jiraService.completeTransitionOrForward(dto , childDto);
             }
         }
        
         return resp;
        
     }
	 
	@RequestMapping(value = "/getStatus", method = RequestMethod.GET)
	 @ResponseBody
		public List<StatusDto> getStatusDropdown(@RequestParam String taskId) {
		 	System.err.println(taskId);
			List<StatusDto> res = jiraService.setStatusDropdown(taskId);
			return res;
	}
	
	@RequestMapping(value = "/getAssignee", method = RequestMethod.GET)
	public List<AttributeGenericDto> getAssignee() {
		List<AttributeGenericDto> assignees = jiraService.fetchAssignee();
		return assignees;
	}
	
	@RequestMapping(value = "/fetchResolution", method = RequestMethod.GET)
	public List<AttributeGenericDto> getResolution() {
		List<AttributeGenericDto> resolutions = jiraService.fetchResolution();
		return resolutions;
	}
	
	@RequestMapping(value = "/fetchDefaultRootCause", method = RequestMethod.GET)
	public List<AttributeGenericDto> getDefaultRootCause() {
		List<AttributeGenericDto> defaultRootCauses = jiraService.fetchDefaultRootCause();
		return defaultRootCauses;
	}
	
	@RequestMapping(value = "/getComment", method = RequestMethod.GET)
	public List<CommentDto> getComment(@RequestParam String eventId) {
		List<CommentDto> commentDtos = jiraService.fetchComment(eventId);
		return commentDtos;
	}
	
	@RequestMapping(value = "/fetchFields", method = RequestMethod.GET)
	public List<ScreenGenericDto> getFields(@RequestParam String processName, @RequestParam String taskId) {
		List<ScreenGenericDto> screenGenericDtos = jiraService.fetchFields(processName, taskId);
		return screenGenericDtos;
	}
}
