package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;

public class FormLayoutTemplate {
	
	String formId;
	String formStatus;
	
	List<List<LayoutAttributesTemplateDto>> formDataAttributes=new ArrayList<>();
	List<List<LayoutAttributesTemplateDto>> lineItemDataAttributes=new ArrayList<>();
	
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormStatus() {
		return formStatus;
	}
	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}
	public List<List<LayoutAttributesTemplateDto>> getFormDataAttributes() {
		return formDataAttributes;
	}
	public void setFormDataAttributes(List<List<LayoutAttributesTemplateDto>> formDataAttributes) {
		this.formDataAttributes = formDataAttributes;
	}
	public List<List<LayoutAttributesTemplateDto>> getLineItemDataAttributes() {
		return lineItemDataAttributes;
	}
	public void setLineItemDataAttributes(List<List<LayoutAttributesTemplateDto>> lineItemDataAttributes) {
		this.lineItemDataAttributes = lineItemDataAttributes;
	}
	@Override
	public String toString() {
		return "FormLayoutTemplate [formId=" + formId + ", formStatus=" + formStatus + ", formDataAttributes="
				+ formDataAttributes + ", lineItemDataAttributes=" + lineItemDataAttributes + "]";
	}
	
	

}
