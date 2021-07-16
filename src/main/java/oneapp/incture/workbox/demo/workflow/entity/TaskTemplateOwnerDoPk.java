package oneapp.incture.workbox.demo.workflow.entity;

import java.io.Serializable;

import javax.persistence.Column;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

public class TaskTemplateOwnerDoPk implements BaseDo, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "ID" , columnDefinition = "VARCHAR(255)")
	private String id;

	@Column(name = "OWNER_ID" , columnDefinition = "VARCHAR(1000)")
	private String ownerId;

	@Override
	public String toString() {
		return "TaskTemplateOwnerDoPk [id=" + id + ", ownerId=" + ownerId + "]";
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

	public TaskTemplateOwnerDoPk(String id, String ownerId) {
		super();
		this.id = id;
		this.ownerId = ownerId;
	}

	public TaskTemplateOwnerDoPk() {
		super();
	}

	@Override
	public Object getPrimaryKey() {
		return null;
	}
	
}
