package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;

public class LayoutDataForFromsAndLineItems {

	
	private Integer index;
	private String layoutType;
	
	private List<LayoutAttributesTemplateDto> layoutAttributes=new ArrayList<>();
	
	List<FormLayoutTemplate> formLayoutData=new ArrayList<>();
	
	
	public List<FormLayoutTemplate> getFormLayoutData() {
		return formLayoutData;
	}
	public void setFormLayoutData(List<FormLayoutTemplate> formLayoutData) {
		this.formLayoutData = formLayoutData;
	}
	public LayoutDataForFromsAndLineItems() {
		super();
	}
	public LayoutDataForFromsAndLineItems(Integer index, String layoutType,
			List<LayoutAttributesTemplateDto> layoutAttributes) {
		super();
		this.index = index;
		this.layoutType = layoutType;
		this.layoutAttributes = layoutAttributes;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getLayoutType() {
		return layoutType;
	}
	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}
	public List<LayoutAttributesTemplateDto> getLayoutAttributes() {
		return layoutAttributes;
	}
	public void setLayoutAttributes(List<LayoutAttributesTemplateDto> layoutAttributes) {
		this.layoutAttributes = layoutAttributes;
	}
	@Override
	public String toString() {
		return "LayoutDataForFromsAndLineItems [index=" + index + ", layoutType=" + layoutType + ", layoutAttributes="
				+ layoutAttributes + ", formLayoutData=" + formLayoutData + "]";
	}
	
	
}
