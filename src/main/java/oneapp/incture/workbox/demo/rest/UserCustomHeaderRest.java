package oneapp.incture.workbox.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeTemplateResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.userCustomAttributes.dto.UserCustomHeadersDto;
import oneapp.incture.workbox.demo.userCustomAttributes.services.UserCustomHeadersFacadeLocal;


@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "workbox/customHeaders", produces = "application/json")
public class UserCustomHeaderRest {

	@Autowired
	private UserCustomHeadersFacadeLocal userCustomHeadersFacadeLocal;

	@RequestMapping(value = "/getCustomAttributes", method = RequestMethod.GET)
	public List<CustomAttributeTemplate> getUserDefinedCustomAttributes(@RequestParam(value = "userId", required = true) String userId, 
				@RequestParam(value = "processName", required = true) String processName){
		return userCustomHeadersFacadeLocal.getUserDefinedCustomAttributes(userId, processName);
	}
	
	@RequestMapping(value = "/saveCustomAttributes", method = RequestMethod.POST)
	public ResponseMessage saveOrUpdateUserCustomization(@RequestBody List<UserCustomHeadersDto> dto, 
			@RequestParam(value = "userId", required = false) String userId) {
		return userCustomHeadersFacadeLocal.saveOrUpdateUserCustomization(dto,userId);
	}
	
	@RequestMapping(value = "/getCustomTemplates", method = RequestMethod.GET)
	public CustomAttributeTemplateResponse getCustomTemplates(@RequestParam(value = "processName") String processName,
			@RequestParam(value = "key", defaultValue = "") String key,
			@RequestParam(value = "isActive", required = false) Boolean isActive,
			@RequestParam(value = "userId", required = false) String userId) {
		return userCustomHeadersFacadeLocal.getCustomAttributeTemplates(processName, key, isActive, userId);
	}
}
