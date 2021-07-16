package oneapp.incture.workbox.demo.substitution.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;


@SuppressWarnings("serial")
@Entity
@Table(name = "SUBSTITUTION_RULE_AUDIT")

public class SubstitutionRuleAuditDo implements BaseDo, Serializable {

	@Id
    @Column(name = "RULE_AUDIT_ID", length = 70)
    private String ruleAuditId = UUID.randomUUID().toString().replaceAll("-", ""); 
	
	@Column(name = "RULE_ID", length = 200)
	private String ruleId;

	@Column(name = "USER_ID", length = 30)
	private String userId;
	
	@Column(name = "COLUMN_NAME", length = 100)
	private String columnName;
	
	@Column(name = "COLUMN_VALUE_OLD", length = 200)
	private String columnValueOld;
	
	@Column(name = "COLUMN_VALUE_NEW", length = 200)
	private String columnValueNew;
	
	@Column(name = "ACTION_TYPE", length = 30)
	private String actionType;
	
	@Column(name = "UPDATED_AT")
	private Date updatedAt;

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnValueOld() {
		return columnValueOld;
	}

	public void setColumnValueOld(String list) {
		this.columnValueOld = list;
	}

	public String getColumnValueNew() {
		return columnValueNew;
	}

	public void setColumnValueNew(String columnValueNew) {
		this.columnValueNew = columnValueNew;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	
}


