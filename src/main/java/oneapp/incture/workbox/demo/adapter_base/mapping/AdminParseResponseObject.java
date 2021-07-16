package oneapp.incture.workbox.demo.adapter_base.mapping;

import java.util.ArrayList;
import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValuesTableDo;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDo;

public class AdminParseResponseObject {

	public AdminParseResponseObject(List<TaskEventsDo> tasks, List<ProcessEventsDo> processes,
			List<TaskOwnersDo> owners, List<WorkBoxDto> workbox, int resultCount) {
		super();
		this.tasks = tasks;
		this.processes = processes;
		this.owners = owners;
		this.workbox = workbox;
		this.resultCount = resultCount;
		
	}
	
	public AdminParseResponseObject(List<TaskEventsDo> tasks, List<ProcessEventsDo> processes,
			List<TaskOwnersDo> owners, List<WorkBoxDto> workbox, int resultCount, List<CustomAttributeValue> customAttributeValues) {
		super();
		this.tasks = tasks;
		this.processes = processes;
		this.owners = owners;
		this.workbox = workbox;
		this.resultCount = resultCount;
		this.customAttributeValues = customAttributeValues;
	}
	
	public AdminParseResponseObject(List<TaskEventsDo> tasks, List<ProcessEventsDo> processes,
			List<TaskOwnersDo> owners, List<WorkBoxDto> workbox, int resultCount, List<CustomAttributeValue> customAttributeValues,List<CustomAttributeValuesTableDo> custValues) {
		super();
		this.tasks = tasks;
		this.processes = processes;
		this.owners = owners;
		this.workbox = workbox;
		this.resultCount = resultCount;
		this.customAttributeValues = customAttributeValues;
		this.custValues=custValues;
	}
	
	private List<TaskEventsDo> tasks;
	private List<ProcessEventsDo> processes;
	private List<TaskOwnersDo> owners;
	private List<WorkBoxDto> workbox;
	private List<CustomAttributeValue> customAttributeValues;
	private int resultCount;
	
	List<CustomAttributeValuesTableDo> custValues=new ArrayList<>();
	

	
	public List<CustomAttributeValuesTableDo> getCustValues() {
		return custValues;
	}

	public void setCustValues(List<CustomAttributeValuesTableDo> custValues) {
		this.custValues = custValues;
	}

	public List<CustomAttributeValue> getCustomAttributeValues() {
		return customAttributeValues;
	}

	public void setCustomAttributeValues(List<CustomAttributeValue> customAttributeValues) {
		this.customAttributeValues = customAttributeValues;
	}

	public List<TaskEventsDo> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskEventsDo> tasks) {
		this.tasks = tasks;
	}

	public List<ProcessEventsDo> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessEventsDo> processes) {
		this.processes = processes;
	}

	public List<TaskOwnersDo> getOwners() {
		return owners;
	}

	public void setOwners(List<TaskOwnersDo> owners) {
		this.owners = owners;
	}

	public List<WorkBoxDto> getWorkbox() {
		return workbox;
	}

	public void setWorkbox(List<WorkBoxDto> workbox) {
		this.workbox = workbox;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	@Override
	public String toString() {
		return "AdminParseResponseObject [tasks=" + tasks + ", processes=" + processes + ", owners=" + owners
				+ ", workbox=" + workbox + ", customAttributeValues=" + customAttributeValues + ", resultCount="
				+ resultCount + ", custValues=" + custValues + "]";
	}

}