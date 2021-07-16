package oneapp.incture.workbox.demo.baseAdapter.rest;

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
import oneapp.incture.workbox.demo.adapter_base.dto.KeyValueResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.services.CustomAttributeFacadeLocal;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/customAttribute", produces = "application/json")
public class CustomAttributeRest {

	@Autowired
	CustomAttributeFacadeLocal customAttributeFacade;

	@RequestMapping(value = "/getCustomTemplates", method = RequestMethod.GET)
	public CustomAttributeTemplateResponse getCustomTemplates(@RequestParam(value = "processName") String processName,
			@RequestParam(value = "key", defaultValue = "") String key,
			@RequestParam(value = "isActive", required = false) Boolean isActive) {
		return customAttributeFacade.getCustomAttributeTemplates(processName, key, isActive);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseMessage saveCustomAttributes(@RequestBody List<CustomAttributeTemplate> customAttributeTemplates) {
		return customAttributeFacade.saveOrUpdateCustomAttributes(customAttributeTemplates);
	}

	@RequestMapping(value = "/autoComplete", method = RequestMethod.GET)
	public KeyValueResponseDto getAutoCompleteValues(@RequestParam("processName") String processName,
			@RequestParam("key") String key, @RequestParam("value") String value) {
		return customAttributeFacade.getAutoCompleteValues(processName, key, value);
	}

	@RequestMapping(value = "/getAllAttributes", method = RequestMethod.GET)
	public CustomAttributeTemplateResponse getAllAttributes() {
		return customAttributeFacade.getAllAttributes();
	}

}
