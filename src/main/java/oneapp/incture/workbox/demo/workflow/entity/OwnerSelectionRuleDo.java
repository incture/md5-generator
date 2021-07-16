package oneapp.incture.workbox.demo.workflow.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "OWNER_SELECTION_RULE")
public class OwnerSelectionRuleDo  implements BaseDo, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8790582765205074389L;

	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;
	
	@Id
	@Column(name = "TASK_NAME")
	private String taskName;
	
	@Id
	@Column(name = "RULE_ID")
	private String ruleId;
	
	@Column(name = "ATTRIBUTE")
	private String attribute;
	
	@Column(name = "RULE_CONDITION")
	private String condition;
	
	@Column(name = "APPROVER")
	private String approver;
	
	@Column(name = "LOGIC")
	private String logic;
	
	@Column(name = "VALUE")
	private String value;
	
	@Column(name = "ATTRIBUTE_NAME")
	private String attributeName;
	
	@Column(name = "IS_DELETED")
	private Boolean isDeleted;


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
		return "OwnerSelectionRuleDo [processName=" + processName + ", taskName=" + taskName + ", ruleId=" + ruleId
				+ ", attribute=" + attribute + ", condition=" + condition + ", approver=" + approver + ", logic="
				+ logic + ", value=" + value + ", attributeName=" + attributeName + ", isDeleted=" + isDeleted + "]";
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
