package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "LAYOUT_ATTRIBUTES_TEMPLATE_TABLE")
public class LayoutAttributesTemplate implements BaseDo, Serializable {

	@Id
	@Column(name = "LAYOUT_ID")
	private String layoutId;

	@Id
	@Column(name = "KEY")
	private String key;
	@Id
	@Column(name = "SOURCE_INDEX")
	private String sourceIndex;

	@Column(name = "KEY_LABEL")
	private String keyLabel;

	@Column(name = "KEY_TYPE")
	private String keyType;

	@Column(name = "SOURCE_KEY")
	private String sourceKey;

	@Column(name = "IS_EDITABLE")
	private Boolean isEditable;

	@Column(name = "IS_MANDATORY")
	private Boolean isMandatory;

	@Column(name = "IS_VISIBLE")
	private Boolean isVisible;

	@Column(name = "HAS_ACTION")
	private Boolean hasAction;

	@Column(name = "ACTION_URL")
	private String actionURL;

	@Column(name = "SEQUENCE")
	private String sequence;

	@Column(name = "VALUE_HELP_ID")
	private String valueHelpId;
	
	@Column(name="IS_RUNTIME")
	private Boolean isRunTime;

	@Column(name="RUNTIME_TYPE")
	private String runTimeType;


	public Boolean getIsRuntime() {
		return isRunTime;
	}

	public void setIsRuntime(Boolean isRunTime) {
		this.isRunTime = isRunTime;
	}

	public String getRunTimeType() {
		return runTimeType;
	}

	public void setRunTimeType(String runTimeType) {
		this.runTimeType = runTimeType;
	}


	public LayoutAttributesTemplate() {
		super();
	}

	public LayoutAttributesTemplate(String layoutId, String key, String sourceIndex, String keyLabel, String keyType,
			String sourceKey, Boolean isEditable, Boolean isMandatory, Boolean isVisible, Boolean hasAction,
			String actionURL, String sequence, String valueHelpId) {
		super();
		this.layoutId = layoutId;
		this.key = key;
		this.sourceIndex = sourceIndex;
		this.keyLabel = keyLabel;
		this.keyType = keyType;
		this.sourceKey = sourceKey;
		this.isEditable = isEditable;
		this.isMandatory = isMandatory;
		this.isVisible = isVisible;
		this.hasAction = hasAction;
		this.actionURL = actionURL;
		this.sequence = sequence;
		this.valueHelpId = valueHelpId;
	}

	public String getSourceIndex() {
		return sourceIndex;
	}

	public void setSourceIndex(String sourceIndex) {
		this.sourceIndex = sourceIndex;
	}

	public String getSourceKey() {
		return sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
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
		return "LayoutAttributesTemplate [layoutId=" + layoutId + ", key=" + key + ", sourceIndex=" + sourceIndex
				+ ", keyLabel=" + keyLabel + ", keyType=" + keyType + ", sourceKey=" + sourceKey + ", isEditable="
				+ isEditable + ", isMandatory=" + isMandatory + ", isVisible=" + isVisible + ", hasAction=" + hasAction
				+ ", actionURL=" + actionURL + ", sequence=" + sequence + ", valueHelpId=" + valueHelpId
				+ ", isRunTime=" + isRunTime + ", runTimeType=" + runTimeType + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
