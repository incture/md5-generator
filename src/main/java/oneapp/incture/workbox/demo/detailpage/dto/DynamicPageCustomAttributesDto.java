package oneapp.incture.workbox.demo.detailpage.dto;

import java.util.ArrayList;
import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;

public class DynamicPageCustomAttributesDto {

	List<CustomAttributeTemplate> attributeTemplates = new ArrayList<CustomAttributeTemplate>();
	List<CustomAttributeTemplate> lineItemAttributeTemplates = new ArrayList<CustomAttributeTemplate>();
	List<CustomAttributeTemplate> attachmentAttributeTemplates = new ArrayList<CustomAttributeTemplate>();
	
	
	public List<CustomAttributeTemplate> getAttachmentAttributeTemplates() {
		return attachmentAttributeTemplates;
	}
	public void setAttachmentAttributeTemplates(List<CustomAttributeTemplate> attachmentAttributeTemplates) {
		this.attachmentAttributeTemplates = attachmentAttributeTemplates;
	}
	public List<CustomAttributeTemplate> getAttributeTemplates() {
		return attributeTemplates;
	}
	public void setAttributeTemplates(List<CustomAttributeTemplate> attributeTemplates) {
		this.attributeTemplates = attributeTemplates;
	}
	public List<CustomAttributeTemplate> getLineItemAttributeTemplates() {
		return lineItemAttributeTemplates;
	}
	public void setLineItemAttributeTemplates(List<CustomAttributeTemplate> lineItemAttributeTemplates) {
		this.lineItemAttributeTemplates = lineItemAttributeTemplates;
	}
	@Override
	public String toString() {
		return "DynamicPageCustomAttributesDto [attributeTemplates=" + attributeTemplates
				+ ", lineItemAttributeTemplates=" + lineItemAttributeTemplates + ", attachmentAttributeTemplates="
				+ attachmentAttributeTemplates + "]";
	}
	
	
	


}
