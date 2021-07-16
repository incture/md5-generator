package oneapp.incture.workbox.demo.workflow.dto;

import java.util.List;

public class OwnerSelectionRuleDto {

	String ruleId;
	String custom_key;
	String condition;
	List<String> approver;
	String logic;
	String value;
	String attributeName;
	Boolean isDeleted;


	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	public String getLogic() {
		return logic;
	}
	public void setLogic(String logic) {
		this.logic = logic;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	@Override
	public String toString() {
		return "OwnerSelectionRuleDto [ruleId=" + ruleId + ", custom_key=" + custom_key + ", condition=" + condition
				+ ", approver=" + approver + ", logic=" + logic + ", value=" + value + ", attributeName="
				+ attributeName + ", isDeleted=" + isDeleted + "]";
	}
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getCustom_key() {
		return custom_key;
	}
	public void setCustom_key(String custom_key) {
		this.custom_key = custom_key;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public List<String> getApprover() {
		return approver;
	}
	public void setApprover(List<String> approver) {
		this.approver = approver;
	}
	
	
}
