package oneapp.incture.workbox.demo.adminConsole.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupDetailDto;
import oneapp.incture.workbox.demo.manageGroup.dto.GroupResponseDto;
import oneapp.incture.workbox.demo.manageGroup.services.ManageGroupService;


@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/group",produces="application/json")
public class ManageGroupController {

	@Autowired
	private ManageGroupService manageGroupService;
	
	@RequestMapping(value = "/getAllGroup/{type}",method=RequestMethod.GET,produces = "application/json")
	public GroupResponseDto getAllGroup(@PathVariable String type,@RequestParam(required = false) String groupName)
	{
		return manageGroupService.getAllGroup(type,groupName);
	}
	
	@RequestMapping(value = "/createGroup",method=RequestMethod.POST,produces = "application/json")
	public ResponseMessage createCustomGroup(@RequestBody GroupDetailDto groupDetailDto)
	{
		return manageGroupService.createCustomGroup(groupDetailDto);
	}
	
	@RequestMapping(value = "/getGroupDetail/{type}/{groupId}",method=RequestMethod.GET,produces = "application/json")
	public GroupDetailDto getGroupDetail(@PathVariable String type,@PathVariable String groupId)
	{
		return manageGroupService.getGroupDetail(type,groupId);
	}
	
	@RequestMapping(value = "/updateGroup",method=RequestMethod.POST,produces = "application/json")
	public ResponseMessage updateCustomGroup(@RequestBody GroupDetailDto groupDetailDto)
	{
		return manageGroupService.updateCustomGroup(groupDetailDto);
	}
}
