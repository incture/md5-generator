package oneapp.incture.workbox.demo.dynamicpage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageGroupResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.services.DetailPageFacadeLocal;
import oneapp.incture.workbox.demo.dynamicpage.services.DynamicPageFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value="/workbox/dynamicPage", produces = "application/json")
public class DynamicPageRest {
	
	@Autowired
	private DynamicPageFacadeLocal dynamicPageFacadeLocal;
	
	@Autowired
	private DetailPageFacadeLocal detailPageFacade;
	
	@RequestMapping(value="/getDynamicDetails",method=RequestMethod.GET)
	public DynamicPageResponseDto getDynamicDetails(@RequestParam(value="catalogueName",required=true) String catalogueName,
			@RequestParam(value="processName",required=true) String processName) {
		
		DynamicPageResponseDto response = new DynamicPageResponseDto();
		if(ServicesUtil.isEmpty(catalogueName)) {
			response.setMessage("Catalogue name cannot be empty");
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
		}else {
			response = dynamicPageFacadeLocal.getDynamicDetailsForAdmin(catalogueName,processName);
		}
		return response;	
	}
	
	@RequestMapping(value="/saveCustomization", method = RequestMethod.POST)
	public ResponseMessage saveOrUpdateCustomization(@RequestBody List<DynamicPageGroupResponseDto> dynamicPageDtoList) {
		return 	dynamicPageFacadeLocal.saveOrUpdateCustomization(dynamicPageDtoList);
		
	}
	
	@RequestMapping(value = "/getPOFieldValues/{fieldName}", method=RequestMethod.GET)
	public GenericResponseDto getPOFieldValues(@PathVariable("fieldName") String fieldName, 
			@RequestParam(value="dependencyKey", required = false) String dependencyKey, 
			@RequestParam(value = "dependencyValue", required = false) String dependencyValue,
			@RequestParam(value = "taskType", required = false) String taskType) {
		GenericResponseDto response = new GenericResponseDto();
		if(ServicesUtil.isEmpty(fieldName)) {
			response.setMessage("Field Name Cannot Be Empty");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		else {
			response = dynamicPageFacadeLocal.getPOFieldValues(fieldName,dependencyKey,dependencyValue,taskType);
		}
		return response;
	}
	
	@RequestMapping(value="/getDynamicDetailsForUser",method=RequestMethod.GET)
	public DynamicPageResponseDto getDynamicDetailsForUser(@RequestParam(value="catalogueName",required=true) String catalogueName,
			@RequestParam(value="processName",required=true) String processName) {
		DynamicPageResponseDto response = new DynamicPageResponseDto();
		if(ServicesUtil.isEmpty(catalogueName)) {
			response.setMessage("Catalogue name cannot be empty");
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
		}else {
			response = dynamicPageFacadeLocal.getDynamicDetailsForUser(catalogueName,processName);
		}
		return response;	
	}
	
	@RequestMapping(value = "/reset/{catalogueName}", method = RequestMethod.GET )
	public ResponseMessage resetToDefault(@PathVariable("catalogueName") String catalogueName,
			@RequestParam(value="processName",required=true) String processName) {
		ResponseMessage response = new ResponseMessage();
		if(ServicesUtil.isEmpty(catalogueName)) {
			response.setMessage("Catalogue name cannot be empty");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}else {
			response = dynamicPageFacadeLocal.resetToDefault(catalogueName,processName);
		}
		return response;
	}
	
	@RequestMapping(value="/detailPage",method = RequestMethod.GET)
	public DynamicPageResponseDto getDetailPage(@RequestParam(value = "taskId",required = true) String taskId, 
			@RequestParam(value = "processName", required = true) String processName, 
			@RequestParam(value = "catalogue", required = true) String catalogue){
		
		return detailPageFacade.getDetailPage(taskId, processName, catalogue);
	}

}