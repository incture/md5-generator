package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "CUSTOM_ATTR_VALUES_TABLE")
public class CustomAttributeValueTableAdhocDo implements BaseDo , Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	// @EmbeddedId
	// CustomAttributeValuePk customAttributeValuePk;

	@Id
	@Column(name = "TASK_ID")
	private String taskId;
	@Id
	@Column(name = "PROCESS_NAME")
	private String processName;

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	@Id
	@Column(name = "KEY", length = 50)
	private String key;

	@Id
	@Column(name = "ROW_NUMBER")
	private int rowNumber;

	@Column(name = "DEPENDANT_ON")
	private String dependantOn;

	@Transient
	private String attributeTemplate;

	public CustomAttributeValueTableAdhocDo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomAttributeValueTableAdhocDo(String taskId, String processName, String key, String attributeValue) {
		super();
		this.taskId = taskId;
		this.processName = processName;
		this.key = key;
		this.attributeValue = attributeValue;
	}

	public CustomAttributeValueTableAdhocDo(String processName, String key, String attributeValue) {
		super();
		this.processName = processName;
		this.key = key;
		this.attributeValue = attributeValue;
	}

	// public CustomAttributeValuePk getCustomAttributeValuePk() {
	// return customAttributeValuePk;
	// }
	//
	// public void setCustomAttributeValuePk(CustomAttributeValuePk
	// customAttributeValuePk) {
	// this.customAttributeValuePk = customAttributeValuePk;
	// }

	public String getAttributeTemplate() {
		return attributeTemplate;
	}

	public String getDependantOn() {
		return dependantOn;
	}

	public void setDependantOn(String dependantOn) {
		this.dependantOn = dependantOn;
	}

	public void setAttributeTemplate(String attributeTemplate) {
		this.attributeTemplate = attributeTemplate;
	}

	@Column(name = "ATTR_VALUE", length = 2000)
	private String attributeValue;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "CustomAttributeValueTable [taskId=" + taskId + ", processName=" + processName + ", key=" + key
				+ ", rowNumber=" + rowNumber + ", dependantOn=" + dependantOn + ", attributeTemplate="
				+ attributeTemplate + ", attributeValue=" + attributeValue + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
