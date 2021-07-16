package oneapp.incture.workbox.demo.dynamicpage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TASK_TYPE_DEPENDENT_VALUES")
public class TaskTypeDependentValues implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2601608055138287106L;

	@Id
	@Column(name = "KEY")
	private String key;
	
	@Id
	@Column(name = "DEPENDENCY_KEY")
	private String dependencyKey;
	
	@Id
	@Column(name = "DEPENDENCY_VALUE")
	private String dependencyValue;
	
	@Id
	@Column(name = "TASK_TYPE")
	private String taskType;
	
	@Id
	@Column(name = "VALUE")
	private String values;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDependencyKey() {
		return dependencyKey;
	}

	public void setDependencyKey(String dependencyKey) {
		this.dependencyKey = dependencyKey;
	}

	public String getDependencyValue() {
		return dependencyValue;
	}

	public void setDependencyValue(String dependencyValue) {
		this.dependencyValue = dependencyValue;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "TaskTypeDependentValues [key=" + key + ", dependencyKey=" + dependencyKey + ", dependencyValue="
				+ dependencyValue + ", taskType=" + taskType + ", values=" + values + "]";
	}
	
	
	
}
