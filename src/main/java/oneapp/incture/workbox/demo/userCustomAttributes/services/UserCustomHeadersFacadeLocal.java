package oneapp.incture.workbox.demo.userCustomAttributes.services;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeTemplateResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.userCustomAttributes.dto.UserCustomHeadersDto;

public interface UserCustomHeadersFacadeLocal {

	public List<CustomAttributeTemplate> getUserDefinedCustomAttributes(String userId, String processName);
	
	public ResponseMessage saveOrUpdateUserCustomization(List<UserCustomHeadersDto> userCustomHeadersDto, String userId);
	
	public CustomAttributeTemplateResponse getCustomAttributeTemplates(String processName, String key, Boolean isActive, String userId);
}
