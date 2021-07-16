package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;



public class DetailPageDto {
	
	private String layoutId;
	private String layoutName;
	private String label;
	private String parentLayoutName;
	private String sequence;
	private String level;
	private String layoutType;
	private String sourceKey;
	private String layoutSize;

	private List<LayoutDataForFromsAndLineItems> data=new ArrayList<>();
	
	private List<LayoutAttributesTemplateDto> layoutAttributes=new ArrayList<>();
	
	private List<DetailPageDto> subLayots=new ArrayList<>();
	
	
	
	public DetailPageDto() {
		super();
	}

	
	public DetailPageDto(String layoutId, String layoutName, String label, String parentLayoutName, String sequence,
			String level, String layoutType, String sourceKey, String layoutSize,
			List<LayoutDataForFromsAndLineItems> data, List<LayoutAttributesTemplateDto> layoutAttributes,
			List<DetailPageDto> subLayots) {
		super();
		this.layoutId = layoutId;
		this.layoutName = layoutName;
		this.label = label;
		this.parentLayoutName = parentLayoutName;
		this.sequence = sequence;
		this.level = level;
		this.layoutType = layoutType;
		this.sourceKey = sourceKey;
		this.layoutSize = layoutSize;
		this.data = data;
		this.layoutAttributes = layoutAttributes;
		this.subLayots = subLayots;
	}




	public String getLayoutSize() {
		return layoutSize;
	}

	public void setLayoutSize(String layoutSize) {
		this.layoutSize = layoutSize;
	}

	public String getSourceKey() {
		return sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

	public List<LayoutDataForFromsAndLineItems> getData() {
		return data;
	}

	public void setData(List<LayoutDataForFromsAndLineItems> data) {
		this.data = data;
	}

	public String getLayoutId() {
		return layoutId;
	}
	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}
	public String getLayoutName() {
		return layoutName;
	}
	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getParentLayoutName() {
		return parentLayoutName;
	}
	public void setParentLayoutName(String parentLayoutName) {
		this.parentLayoutName = parentLayoutName;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
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

	public List<DetailPageDto> getSubLayots() {
		return subLayots;
	}
	public void setSubLayots(List<DetailPageDto> subLayots) {
		this.subLayots = subLayots;
	}
	@Override
	public String toString() {
		return "DetailPageDto [layoutId=" + layoutId + ", layoutName=" + layoutName + ", label=" + label
				+ ", parentLayoutName=" + parentLayoutName + ", sequence=" + sequence + ", level=" + level
				+ ", layoutType=" + layoutType + ", sourceKey=" + sourceKey + ", layoutSize=" + layoutSize + ", data="
				+ data + ", layoutAttributes=" + layoutAttributes + ", subLayots=" + subLayots + "]";
	}
	
	
	
}
