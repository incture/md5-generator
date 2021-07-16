package oneapp.incture.workbox.demo.adapter_base.dto;


import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;


public class LayoutAttributesTemplateDto extends BaseDto implements Cloneable{

	
	private String layoutId;

	
	private String key;

	private String sourceIndex;
	private String keyLabel;

	
	private String keyType;

	
	private Boolean isEditable;


	private Boolean isMandatory;

	private Boolean isVisible;

	
	private Boolean hasAction;

	
	private String actionURL;

	
	private String sequence;


	private String valueHelpId;
	
	private String keyValue;
	private Integer index;
	
	private String sourceKey;
	
	private Boolean isRunTime;
	private String runTimeType;
	private String url;
	private String validForUsage;
	
	
	

	
	public String getVaildForUsage() {
		return validForUsage;
	}


	public void setVaildForUsage(String validForUsage) {
		this.validForUsage = validForUsage;
	}


	public Boolean getIsRunTime() {
		return isRunTime;
	}


	public void setIsRunTime(Boolean isRunTime) {
		this.isRunTime = isRunTime;
	}


	public String getRunTimeType() {
		return runTimeType;
	}


	public void setRunTimeType(String runTimeType) {
		this.runTimeType = runTimeType;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getSourceIndex() {
		return sourceIndex;
	}


	public void setSourceIndex(String sourceIndex) {
		this.sourceIndex = sourceIndex;
	}


	public LayoutAttributesTemplateDto() {
		super();
	}


	public LayoutAttributesTemplateDto(String layoutId, String key, String keyLabel, String keyType, Boolean isEditable,
			Boolean isMandatory, Boolean isVisible, Boolean hasAction, String actionURL, String sequence,
			String valueHelpId, String keyValue, Integer index) {
		super();
		this.layoutId = layoutId;
		this.key = key;
		this.keyLabel = keyLabel;
		this.keyType = keyType;
		this.isEditable = isEditable;
		this.isMandatory = isMandatory;
		this.isVisible = isVisible;
		this.hasAction = hasAction;
		this.actionURL = actionURL;
		this.sequence = sequence;
		this.valueHelpId = valueHelpId;
		this.keyValue = keyValue;
		this.index = index;
	}

	
	public LayoutAttributesTemplateDto(String layoutId, String key, String keyLabel, String keyType, Boolean isEditable,
			Boolean isMandatory, Boolean isVisible, Boolean hasAction, String actionURL, String sequence,
			String valueHelpId, String keyValue, Integer index, String sourceKey) {
		super();
		this.layoutId = layoutId;
		this.key = key;
		this.keyLabel = keyLabel;
		this.keyType = keyType;
		this.isEditable = isEditable;
		this.isMandatory = isMandatory;
		this.isVisible = isVisible;
		this.hasAction = hasAction;
		this.actionURL = actionURL;
		this.sequence = sequence;
		this.valueHelpId = valueHelpId;
		this.keyValue = keyValue;
		this.index = index;
		this.sourceKey = sourceKey;
	}


	public String getSourceKey() {
		return sourceKey;
	}


	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}


	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKeyLabel() {
		return keyLabel;
	}

	public void setKeyLabel(String keyLabel) {
		this.keyLabel = keyLabel;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public Boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Boolean getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Boolean getHasAction() {
		return hasAction;
	}

	public void setHasAction(Boolean hasAction) {
		this.hasAction = hasAction;
	}

	public String getActionURL() {
		return actionURL;
	}

	public void setActionURL(String actionURL) {
		this.actionURL = actionURL;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getValueHelpId() {
		return valueHelpId;
	}

	public void setValueHelpId(String valueHelpId) {
		this.valueHelpId = valueHelpId;
	}

	



	@Override
	public String toString() {
		return "LayoutAttributesTemplateDto [layoutId=" + layoutId + ", key=" + key + ", sourceIndex=" + sourceIndex
				+ ", keyLabel=" + keyLabel + ", keyType=" + keyType + ", isEditable=" + isEditable + ", isMandatory="
				+ isMandatory + ", isVisible=" + isVisible + ", hasAction=" + hasAction + ", actionURL=" + actionURL
				+ ", sequence=" + sequence + ", valueHelpId=" + valueHelpId + ", keyValue=" + keyValue + ", index="
				+ index + ", sourceKey=" + sourceKey + ", isRunTime=" + isRunTime + ", runTimeType=" + runTimeType
				+ ", url=" + url + ", validForUsage=" + validForUsage + "]";
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
    public LayoutAttributesTemplateDto clone() {
           try {
                return (LayoutAttributesTemplateDto)super.clone();
            }
            catch (CloneNotSupportedException e) {
            return null;
               // This should never happen
            }
       }

}
