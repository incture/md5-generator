package oneapp.incture.workbox.demo.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.zoho.services.ZohoTest;
import oneapp.incture.workbox.demo.zoho.services.ZohoActionFacade;
import oneapp.incture.workbox.demo.zoho.services.ZohoService;



@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/zoho")
public class ZohoAdapterController {

	@Autowired
	ZohoService zohoService;
	
	@Autowired
	ZohoActionFacade zohoActionFacade;
	
	@Autowired
	ZohoTest test;
	

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public String test() {
		return "Hello";
	}
	
	@RequestMapping(value="/{processName}/setAll" , method = RequestMethod.GET)
	public ResponseMessage setAll(@PathVariable String processName){
		return zohoService.setAll(processName.toLowerCase());
	}
	
	@RequestMapping(value="/{processName}/getAttributes" , method = RequestMethod.GET)
	public List<String> getAttributes(@PathVariable String processName){
		return zohoService.getCustomAttributeValues(processName);
	}
	
	@RequestMapping(value="/check/{templateId}" , method = RequestMethod.GET)
	public List<CustomAttributeTemplateDto> check(@PathVariable String templateId){
		List<CustomAttributeTemplateDto> result = test.getTaskCustomAttributesByTemplateId(templateId);
		System.err.println(new Gson().toJson(result));
		return result;
	}
	
	
	
//	 @RequestMapping(value = "/actions", method = RequestMethod.POST, produces = "application/json")
//     public ResponseMessage taskActions(@RequestBody ActionDto dto)  {
//         ResponseMessage resp = null;
//         System.err.println("[WBP-Dev][WORKBOX][WorkboxRest][action][dto]" + dto.toString());        
//         for (ActionDtoChild childDto : dto.getTask()) {
//             if (!ServicesUtil.isEmpty(childDto.getActionType())) {            	 
//                 resp = zohoActionFacade.acceptOrRejectRequest(dto, childDto);
//             }
//         }
//        
//         return resp;
//        
//     }
	

//	@RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
//	public Map<String, UserIDPMappingDto> getAllUsers() {
//		Map<String, UserIDPMappingDto> userDetails = zohoService.fetchUsers(0);
//		return userDetails;
//	}
//	
//	@RequestMapping(value = "/getProcessEvent", method = RequestMethod.GET)
//	public List<ProcessEventsDto> getProcess() {
//		List<ProcessEventsDto> res = zohoService.setProcess();
//		return res;
//	}
//	
//	@RequestMapping(value = "/getTaskEvent", method = RequestMethod.GET)
//	public List<TaskEventsDto> getTask() {
//		List<TaskEventsDto> res = zohoService.getTask();
//		return res;
//	}

}
