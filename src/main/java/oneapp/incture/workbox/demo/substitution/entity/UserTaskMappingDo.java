package oneapp.incture.workbox.demo.substitution.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "USER_TASK_MAPPING")

public class UserTaskMappingDo implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SUBSTITUTING_USER", length = 100)
	private String substitutingUser;

	@Id
	@Column(name = "SUBSTITUTED_USER", length = 100)
	private String substitutedUser;

	@Id
	@Column(name = "TASK_ID", length = 200)
	private String taskId;

	public String getSubstitutingUser() {
		return substitutingUser;
	}

	public void setSubstitutingUser(String substitutingUser) {
		this.substitutingUser = substitutingUser;
	}

	public String getSubstitutedUser() {
		return substitutedUser;
	}

	public void setSubstitutedUser(String substitutedUser) {
		this.substitutedUser = substitutedUser;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return "UserTaskMappingDo [substitutingUser=" + substitutingUser + ", substitutedUser=" + substitutedUser
				+ ", taskId=" + taskId + "]";
	}

	public Object getPrimaryKey() {
		return null;
	}

}