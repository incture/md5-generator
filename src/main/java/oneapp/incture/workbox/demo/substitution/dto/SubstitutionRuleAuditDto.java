package oneapp.incture.workbox.demo.substitution.dto;


import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class SubstitutionRuleAuditDto {

	public SubstitutionRuleAuditDto() {
		super();
	}
	public SubstitutionRuleAuditDto(String ruleId, String userId,String columnName,
			String columnValueOld,
			String columnValueNew,String actionType,Date updatedAt,String updateAtInString) {
		this.ruleId = ruleId;
		this.userId=userId;
		this.columnName=columnName;
		this.columnValueOld=columnValueOld;
		this.columnValueNew=columnValueNew;
		this.actionType=actionType;
		this.updatedAt=updatedAt;
		this.updateAtInString=updateAtInString;
	}
	
	private String ruleId;
	private String userId;
	private String columnName;
	private String columnValueOld;
	private String columnValueNew;
	private String actionType;
	private Date updatedAt;
	private String updateAtInString;
	
	public String getUpdateAtInString() {
		return updateAtInString;
	}
	public void setUpdateAtInString(String updateAtInString) {
		this.updateAtInString = updateAtInString;
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
	public void setColumnValueOld(String columnValueOld) {
		this.columnValueOld = columnValueOld;
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


