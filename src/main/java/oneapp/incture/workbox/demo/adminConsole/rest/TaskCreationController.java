package oneapp.incture.workbox.demo.adminConsole.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adhocTask.dto.AdhocActionDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ApproverDto;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributesDto;
import oneapp.incture.workbox.demo.adhocTask.services.TaskCreationService;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/tasks",produces="application/json")
public class TaskCreationController {

	@Autowired
	TaskCreationService taskCreationService;
	
	@RequestMapping(value = "/getProcessAttributes/{processName}",method=RequestMethod.GET,produces = "application/json")
	public AttributesResponseDto getProcessAttributes(@PathVariable String processName)
	{
		return taskCreationService.getProcessAttributes(processName);
	}
	
	@RequestMapping(value = "/createTasks",method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage createTasks(@RequestBody AttributesResponseDto attributesResponseDto
			,@AuthenticationPrincipal Token token)
	{
		
		return taskCreationService.createTasks(attributesResponseDto,token);
	}
	
	@RequestMapping(value = "deleteDraft/{processId}/{eventId}",method=RequestMethod.GET,produces = "application/json")
	public ResponseMessage deleteDraft(@PathVariable String processId,@PathVariable String eventId)
	{
		return taskCreationService.deleteDraft(processId,eventId);
	}
	
	@RequestMapping(value = "viewDraft/{eventId}",method=RequestMethod.GET,produces = "application/json")
	public AttributesResponseDto viewDraft(@PathVariable String eventId)
	{
		return taskCreationService.viewDraft(eventId);
	}
	
	@RequestMapping(value = "updateTaskAttributes",method=RequestMethod.POST,produces = "application/json")
	public ResponseMessage updateTaskAttributes(@RequestBody CustomAttributesDto customAttributeDto)
	{
		return taskCreationService.updateTaskAttributes(customAttributeDto);
	}
	
	@RequestMapping(value = "action",method=RequestMethod.POST,produces = "application/json")
	public ResponseMessage actionOnAdhoc(@RequestBody AdhocActionDto actionDto)
	{
		return taskCreationService.actionOnAdhoc(actionDto);
	}
	
	@RequestMapping(value = "getApprover",method=RequestMethod.GET,produces = "application/json")
	public ApproverDto getApprover()
	{
		return taskCreationService.getApprover();
	}
}
