package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

public class EmailTemplateDto extends BaseDto{

	private String processName;
	private String taskName;
	private String templateId;
	private int latestVersion;
	private List<VersionStatusDto> versions;
	private String emailTypeId;
	private String templateType;
	private String templateName;
	private String status;
	private String ownerId;
	private String validForUsage;
	
	public void setValidForUsage(String validForUsage) {
		this.validForUsage = validForUsage;
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
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	public String getEmailTypeId() {
		return emailTypeId;
	}
	public void setEmailTypeId(String emailTypeId) {
		this.emailTypeId = emailTypeId;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
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
		return "EmailTemplateDto [processName=" + processName + ", taskName=" + taskName + ", templateId=" + templateId
				+ ", latestVersion=" + latestVersion + ", versions=" + versions + ", emailTypeId=" + emailTypeId
				+ ", templateType=" + templateType + ", templateName=" + templateName + ", status=" + status
				+ ", ownerId=" + ownerId + "]";
	}
	public int getLatestVersion() {
		return latestVersion;
	}
	public void setLatestVersion(int latestVersion) {
		this.latestVersion = latestVersion;
	}
	public List<VersionStatusDto> getVersions() {
		return versions;
	}
	public void setVersions(List<VersionStatusDto> versions) {
		this.versions = versions;
	}

	
		
	
}

