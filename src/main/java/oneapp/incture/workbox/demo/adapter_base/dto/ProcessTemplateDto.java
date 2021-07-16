package oneapp.incture.workbox.demo.adapter_base.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class ProcessTemplateDto extends BaseDto {

	private String processName;
	private String templateId;
	private String taskName;
	private String ownerId;
	private String taskType;
	private Boolean runTimeUser;
	private String customKey;
	private String subject;
	private String description;
	private String sourceId;
	private String targetId;
	private String url;
	private String taskNature;

	@Override
	public String toString() {
		return "ProcessTemplateDto [processName=" + processName + ", templateId=" + templateId + ", taskName="
				+ taskName + ", ownerId=" + ownerId + ", taskType=" + taskType + ", runTimeUser=" + runTimeUser
				+ ", customKey=" + customKey + ", subject=" + subject + ", description=" + description + ", sourceId="
				+ sourceId + ", targetId=" + targetId + ", url=" + url + ", taskNature=" + taskNature + "]";
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public Boolean getRunTimeUser() {
		return runTimeUser;
	}

	public void setRunTimeUser(Boolean runTimeUser) {
		this.runTimeUser = runTimeUser;
	}

	public String getCustomKey() {
		return customKey;
	}

	public void setCustomKey(String customKey) {
		this.customKey = customKey;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTaskNature() {
		return taskNature;
	}

	public void setTaskNature(String taskNature) {
		this.taskNature = taskNature;
	}

	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub

	}

}
