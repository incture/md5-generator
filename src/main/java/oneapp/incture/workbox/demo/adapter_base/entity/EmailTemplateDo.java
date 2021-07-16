package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMAIL_TEMPLATE")
public class EmailTemplateDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;

	@Id
	@Column(name = "TASK_NAME")
	private String taskName;

	@Id
	@Column(name = "TEMPLATE_ID")
	private String templateId;
	
	@Id
	@Column(name="VERSION")
	private int version;

	@Column(name = "EMAIL_TYPE_ID")
	private String emailTypeId;
	
	@Column(name = "TEMPLATE_TYPE")
	private String templateType;
	
	@Column(name = "TEMPLATE_NAME")
	private String templateName;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "OWNER_ID")
	private String ownerId;

	

	@Override
	public String toString() {
		return "EmailTemplateDo [processName=" + processName + ", taskName=" + taskName + ", templateId=" + templateId
				+ ", version=" + version + ", emailTypeId=" + emailTypeId + ", templateType=" + templateType
				+ ", templateName=" + templateName + ", status=" + status + ", ownerId=" + ownerId + "]";
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
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
