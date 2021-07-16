package oneapp.incture.workbox.demo.adapter_base.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class LayoutTemplateDto extends BaseDto {

	private String layoutId;

	private String layoutName;

	private String label;

	private String parentLayoutName;

	private String sequence;

	private String level;
	private String sourceKey;

	private String layoutType;
	private String layoutSize;
	
	private Boolean isDeleted;
	private String validForUsage;
	
	public String getVaildForUsage() {
		return validForUsage;
	}

	public void setValidForUsage(String validForUsage) {
		this.validForUsage = validForUsage;
	}

	
	public String getSourceKey() {
		return sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	private List<LayoutAttributesTemplateDto> layoutAttributesData = new ArrayList<>();

	private List<LayoutTemplateDto> subLayoutsData = new ArrayList<>();

	public LayoutTemplateDto() {
		super();
	}

	public LayoutTemplateDto(String layoutId, String layoutName, String label, String parentLayoutName, String sequence,
			String level, String layoutType, List<LayoutAttributesTemplateDto> layoutAttributesData,
			List<LayoutTemplateDto> subLayoutsData) {
		super();
		this.layoutId = layoutId;
		this.layoutName = layoutName;
		this.label = label;
		this.parentLayoutName = parentLayoutName;
		this.sequence = sequence;
		this.level = level;
		this.layoutType = layoutType;
		this.layoutAttributesData = layoutAttributesData;
		this.subLayoutsData = subLayoutsData;
	}

	public LayoutTemplateDto(String layoutId, String layoutName, String label, String parentLayoutName, String sequence,
			String level, String layoutType, String layoutSize, List<LayoutAttributesTemplateDto> layoutAttributesData,
			List<LayoutTemplateDto> subLayoutsData) {
		super();
		this.layoutId = layoutId;
		this.layoutName = layoutName;
		this.label = label;
		this.parentLayoutName = parentLayoutName;
		this.sequence = sequence;
		this.level = level;
		this.layoutType = layoutType;
		this.layoutSize = layoutSize;
		this.layoutAttributesData = layoutAttributesData;
		this.subLayoutsData = subLayoutsData;
	}

	public String getLayoutSize() {
		return layoutSize;
	}

	public void setLayoutSize(String layoutSize) {
		this.layoutSize = layoutSize;
	}

	public List<LayoutAttributesTemplateDto> getLayoutAttributesData() {
		return layoutAttributesData;
	}

	public void setLayoutAttributesData(List<LayoutAttributesTemplateDto> layoutAttributesData) {
		this.layoutAttributesData = layoutAttributesData;
	}

	public List<LayoutTemplateDto> getSubLayoutsData() {
		return subLayoutsData;
	}

	public void setSubLayoutsData(List<LayoutTemplateDto> subLayoutsData) {
		this.subLayoutsData = subLayoutsData;
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

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "LayoutTemplateDto [layoutId=" + layoutId + ", layoutName=" + layoutName + ", label=" + label
				+ ", parentLayoutName=" + parentLayoutName + ", sequence=" + sequence + ", level=" + level
				+ ", sourceKey=" + sourceKey + ", layoutType=" + layoutType + ", layoutSize=" + layoutSize
				+ ", isDeleted=" + isDeleted + ", validForUsage=" + validForUsage + ", layoutAttributesData="
				+ layoutAttributesData + ", subLayoutsData=" + subLayoutsData + "]";
	}




	

}
