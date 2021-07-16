package oneapp.incture.workbox.demo.adapter_base.services;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeTemplateResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.KeyValueResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;

public interface CustomAttributeFacadeLocal {

	CustomAttributeTemplateResponse getCustomAttributeTemplates(String processName, String key, Boolean isActive);

	ResponseMessage saveOrUpdateCustomAttributes(List<CustomAttributeTemplate> customAttributeTemplates);

	KeyValueResponseDto  getAutoCompleteValues(String processName, String key, String attributeValue);

	CustomAttributeTemplateResponse getAllAttributes();

}
