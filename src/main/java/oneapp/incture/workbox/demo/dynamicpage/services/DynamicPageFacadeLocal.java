package oneapp.incture.workbox.demo.dynamicpage.services;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageGroupResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageResponseDto;

public interface DynamicPageFacadeLocal {

	public DynamicPageResponseDto getDynamicDetailsForAdmin(String plant, String processName);
	
	public ResponseMessage saveOrUpdateCustomization(List<DynamicPageGroupResponseDto> dynamicPageDtoList);
	
	public GenericResponseDto getPOFieldValues(String fieldName, String dependencyKey, String dependencyValue, String taskType);
	
	public DynamicPageResponseDto getDynamicDetailsForUser(String plant, String processName);
	
	public ResponseMessage resetToDefault(String plant, String processName);
}
