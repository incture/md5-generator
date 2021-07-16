package oneapp.incture.workbox.demo.dynamicpage.dto;

public class TaskTypeDependentValuesDto {

	private String key;
	private String dependencyKey;
	private String dependenyValue;
	private String taskType;
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
	public String getDependenyValue() {
		return dependenyValue;
	}
	public void setDependenyValue(String dependenyValue) {
		this.dependenyValue = dependenyValue;
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
		return "TaskTypeDependentValuesDto [key=" + key + ", dependencyKey=" + dependencyKey + ", dependenyValue="
				+ dependenyValue + ", taskType=" + taskType + ", values=" + values + "]";
	}
	
	
}
