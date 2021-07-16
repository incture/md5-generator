package oneapp.incture.workbox.demo.adapter_base.util;

import java.util.List;

public class AnalystContextDto {

	private String caseId;
	
	private String urgency;
	
	private String application;
	
	private String currentStatus;
	
	private String parentId;
	
	private String analyst;
	
	private String role;
	
	private String icManagerProcessId;
	
	private String icManager;
	
	private List<AnalystsLineItems> lineItems;
	private List<AnalystFormData> forms;
	private String subCaseId;
	
	
	public String getSubCaseId() {
		return subCaseId;
	}

	public void setSubCaseId(String subCaseId) {
		this.subCaseId = subCaseId;
	}

	public String getIcManagerProcessId() {
		return icManagerProcessId;
	}

	public void setIcManagerProcessId(String icManagerProcessId) {
		this.icManagerProcessId = icManagerProcessId;
	}

	public List<AnalystFormData> getForms() {
		return forms;
	}

	public void setForms(List<AnalystFormData> forms) {
		this.forms = forms;
	}

	

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getAnalyst() {
		return analyst;
	}

	public void setAnalyst(String analyst) {
		this.analyst = analyst;
	}

	public String getIcManager() {
		return icManager;
	}

	public void setIcManager(String icManager) {
		this.icManager = icManager;
	}

	public List<AnalystsLineItems> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<AnalystsLineItems> lineItems) {
		this.lineItems = lineItems;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	@Override
	public String toString() {
		return "AnalystContextDto [caseId=" + caseId + ", urgency=" + urgency + ", application=" + application
				+ ", currentStatus=" + currentStatus + ", parentId=" + parentId + ", analyst=" + analyst + ", role="
				+ role + ", icManagerProcessId=" + icManagerProcessId + ", icManager=" + icManager + ", lineItems="
				+ lineItems + ", forms=" + forms + ", subCaseId=" + subCaseId + "]";
	}

	
	
	
}
