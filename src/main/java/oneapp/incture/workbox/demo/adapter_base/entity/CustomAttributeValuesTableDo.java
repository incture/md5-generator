package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOM_ATTRIBUTE_VALUES_TABLE_JABIL")
public class CustomAttributeValuesTableDo implements BaseDo, Serializable {

	@Id
	@Column(name = "TASK_ID", length = 50)
	private String taskId;
	@Id
	@Column(name = "PROCESS_NAME", length = 50)
	private String processName;

	@Id
	@Column(name = "KEY", length = 50)
	private String key;
	
	@Id
	@Column(name = "INDEX")
	private int index;
	
	@Column(name = "ATTR_VALUE", length = 2000)
	private String attributeValue;

	
	public CustomAttributeValuesTableDo() {
		super();
	}

	public CustomAttributeValuesTableDo(String taskId, String processName, String key, int index,
			String attributeValue) {
		super();
		this.taskId = taskId;
		this.processName = processName;
		this.key = key;
		this.index = index;
		this.attributeValue = attributeValue;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	@Override
	public String toString() {
		return "CustomAttributeValuesTableDo [taskId=" + taskId + ", processName=" + processName + ", key=" + key
				+ ", index=" + index + ", attributeValue=" + attributeValue + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public Object getPrimaryKey() {
//		// TODO Auto-generated method stub
//		return taskId+processName+key+index;
//	}
	
	
	
}
