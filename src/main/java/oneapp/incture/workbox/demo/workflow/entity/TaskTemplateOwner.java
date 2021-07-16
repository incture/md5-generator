package oneapp.incture.workbox.demo.workflow.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "TASK_TEMPLATE_OWNER")
public class TaskTemplateOwner implements BaseDo, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", columnDefinition = "VARCHAR(255)")
	private String id;
	@Id
	@Column(name = "OWNER_ID", columnDefinition = "VARCHAR(1000)")
	private String ownerId;

	@Column(name = "TYPE", columnDefinition = "VARCHAR(255)")
	private String type;
	
	@Column(name = "NAME", columnDefinition = "VARCHAR(255)")
	private String idName;

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TaskTemplateOwner [id=" + id + ", ownerId=" + ownerId + ", type=" + type + ", idName=" + idName + "]";
	}

	@Override
	public Object getPrimaryKey() {
		return id + ownerId;
	}

}
