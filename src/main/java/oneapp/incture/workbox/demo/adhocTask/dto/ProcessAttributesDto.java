package oneapp.incture.workbox.demo.adhocTask.dto;

import java.util.List;

public class ProcessAttributesDto {

	List<CustomAttributeTemplateDto> customAttributeTemplateDto;
	String resourceId;
	@Override
	public String toString() {
		return "ProcessAttributesDto [customAttributeTemplateDto=" + customAttributeTemplateDto + ", resourceId="
				+ resourceId + "]";
	}
	public List<CustomAttributeTemplateDto> getCustomAttributeTemplateDto() {
		return customAttributeTemplateDto;
	}
	public void setCustomAttributeTemplateDto(List<CustomAttributeTemplateDto> customAttributeTemplateDto) {
		this.customAttributeTemplateDto = customAttributeTemplateDto;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	
	
}
