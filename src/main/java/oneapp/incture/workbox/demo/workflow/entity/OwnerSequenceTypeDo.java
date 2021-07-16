package oneapp.incture.workbox.demo.workflow.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Entity
@Table(name = "TASK_OWNER_TEMPLATE_SEQUENCE")
public class OwnerSequenceTypeDo implements BaseDo, Serializable{
	
	
	private static final long serialVersionUID = -8790582765205074389L;

	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;

	@Id
	@Column(name = "TEMPLATE_ID")
	private String templateId;
	
	@Column(name = "OWNER_SEQU_TYPE")
	private String ownerSequType;
	
	@Column(name = "ATTR_TYPE_ID")
	private String attrTypeId;
	
	@Column(name = "ATTR_TYPE_NAME")
	private String attrTypeName;
	
	@Column(name = "ORDER_BY")
	private String orderBy;
	
	
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getOwnerSequType() {
		return ownerSequType;
	}

	public void setOwnerSequType(String ownerSequType) {
		this.ownerSequType = ownerSequType;
	}

	public String getAttrTypeId() {
		return attrTypeId;
	}

	public void setAttrTypeId(String attrTypeId) {
		this.attrTypeId = attrTypeId;
	}

	public String getAttrTypeName() {
		return attrTypeName;
	}

	public void setAttrTypeName(String attrTypeName) {
		this.attrTypeName = attrTypeName;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}


	@Override
	public String toString() {
		return "OwnerSequenceTypeDo [processName=" + processName + ", templateId=" + templateId + ", ownerSequType="
				+ ownerSequType + ", attrTypeId=" + attrTypeId + ", attrTypeName=" + attrTypeName + ", orderBy="
				+ orderBy + "]";
	}


	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
