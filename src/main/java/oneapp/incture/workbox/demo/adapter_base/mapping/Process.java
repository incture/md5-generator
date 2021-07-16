package oneapp.incture.workbox.demo.adapter_base.mapping;

public class Process implements Event {

	public Process() {
		super();
	}

	public Process(String id, String definitionId, String definitionVersion, String subject, String status,
			String businessKey, String startedAt, String startedBy, String completedAt) {
		super();
		this.id = id;
		this.definitionId = definitionId;
		this.definitionVersion = definitionVersion;
		this.subject = subject;
		this.status = status;
		this.businessKey = businessKey;
		this.startedAt = startedAt;
		this.startedBy = startedBy;
		this.completedAt = completedAt;
	}

	private String id;
	private String definitionId;
	private String definitionVersion;
	private String subject;
	private String status;
	private String businessKey;
	private String startedAt;
	private String startedBy;
	private String completedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public String getDefinitionVersion() {
		return definitionVersion;
	}

	public void setDefinitionVersion(String definitionVersion) {
		this.definitionVersion = definitionVersion;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(String startedAt) {
		this.startedAt = startedAt;
	}

	public String getStartedBy() {
		return startedBy;
	}

	public void setStartedBy(String startedBy) {
		this.startedBy = startedBy;
	}

	public String getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(String completedAt) {
		this.completedAt = completedAt;
	}

	@Override
	public String toString() {
		return "Process [id=" + id + ", definitionId=" + definitionId + ", definitionVersion=" + definitionVersion
				+ ", subject=" + subject + ", status=" + status + ", businessKey=" + businessKey + ", startedAt="
				+ startedAt + ", startedBy=" + startedBy + ", completedAt=" + completedAt + "]";
	}

}
