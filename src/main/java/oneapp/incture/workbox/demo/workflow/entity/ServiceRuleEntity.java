package oneapp.incture.workbox.demo.workflow.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "SERVICE_RULE_TABLE")
public class ServiceRuleEntity implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "RULE_ID", columnDefinition = "VARCHAR(1000)")
	private String ruleId;

	@Column(name = "RULE_TYPE_ID", columnDefinition = "VARCHAR(1000)")
	private String ruleTypeId;

	@Column(name = "RULE_TYPE", columnDefinition = "VARCHAR(255)")
	private String ruleType;

	@Column(name = "CUSTOM_KEY", columnDefinition = "VARCHAR(255)")
	private String custom_key;

	@Column(name = "RULE_CONDITION", columnDefinition = "VARCHAR(255)")
	private String condition;

	@Column(name = "DESTINATION", columnDefinition = "VARCHAR(255)")
	private String destination;

	@Column(name = "OPERATOR", columnDefinition = "VARCHAR(255)")
	private String operator;
	
	@Column(name = "LOGIC", columnDefinition = "VARCHAR(255)")
	private String logic;
	
	@Column(name = "VALUE", columnDefinition = "VARCHAR(255)")
	private String value;
	
	@Column(name = "ATTRIBUTE_NAME", columnDefinition = "VARCHAR(255)")
	private String attributeName;

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

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleTypeId() {
		return ruleTypeId;
	}

	public void setRuleTypeId(String ruleTypeId) {
		this.ruleTypeId = ruleTypeId;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "ServiceRuleEntity [ruleId=" + ruleId + ", ruleTypeId=" + ruleTypeId + ", ruleType=" + ruleType
				+ ", custom_key=" + custom_key + ", condition=" + condition + ", destination=" + destination
				+ ", operator=" + operator + ", logic=" + logic + ", value=" + value + ", attributeName="
				+ attributeName + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
