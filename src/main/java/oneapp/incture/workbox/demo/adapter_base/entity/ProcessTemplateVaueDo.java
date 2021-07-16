package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROCESS_TEMPLATE_VALUE")
public class ProcessTemplateVaueDo implements BaseDo, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "PROCESS_ID", columnDefinition = "VARCHAR(1000)")
	private String processId;
	@Id
	@Column(name = "TEMPLATE_ID", columnDefinition = "VARCHAR(255)")
	private String templateId;
	
	@Column(name = "TASK_NAME", columnDefinition = "VARCHAR(255)")
	private String taskName;

	@Column(name = "OWNER_ID", columnDefinition = "VARCHAR(1000)")
	private String ownerId;

	@Column(name = "TASK_TYPE", columnDefinition = "VARCHAR(255)")
	private String taskType;

	@Column(name = "RUN_TIME_USER", columnDefinition = "BOOLEAN")
	private Boolean runTimeUser;

	@Column(name = "CUSTOM_KEY", columnDefinition = "VARCHAR(255)")
	private String customKey;

	@Column(name = "SUBJECT", columnDefinition = "VARCHAR(500)")
	private String subject;

	@Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(255)")
	private String description;

	@Column(name = "SOURCE_ID", columnDefinition = "VARCHAR(500)")
	private String sourceId;

	@Column(name = "TARGET_ID", columnDefinition = "VARCHAR(255)")
	private String targetId;
	
	@Column(name = "URL", columnDefinition = "VARCHAR(200)")
	private String url;

	
	public String getProcessId() {
		return processId;
	}


	public void setProcessId(String processId) {
		this.processId = processId;
	}


	public String getTemplateId() {
		return templateId;
	}


	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	@Override
	public String toString() {
		return "ProcessTemplateVaueDo [processId=" + processId + ", templateId=" + templateId + ", taskName=" + taskName
				+ ", ownerId=" + ownerId + ", taskType=" + taskType + ", runTimeUser=" + runTimeUser + ", customKey="
				+ customKey + ", subject=" + subject + ", description=" + description + ", sourceId=" + sourceId
				+ ", targetId=" + targetId + ", url=" + url + "]";
	}


	@Override
	public Object getPrimaryKey() {
		return processId + templateId;
	}
	
}
