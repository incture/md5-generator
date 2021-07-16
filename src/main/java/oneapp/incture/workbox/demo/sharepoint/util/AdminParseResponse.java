package oneapp.incture.workbox.demo.sharepoint.util;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;

public class AdminParseResponse {

		public AdminParseResponse(List<TaskEventsDto> tasks, List<ProcessEventsDto> processes,
				List<TaskOwnersDto> owners, List<WorkBoxDto> workbox, int resultCount) {
			super();
			this.tasks = tasks;
			this.processes = processes;
			this.owners = owners;
			this.workbox = workbox;
			this.resultCount = resultCount;
		}

		private List<TaskEventsDto> tasks;
		private List<ProcessEventsDto> processes;
		private List<TaskOwnersDto> owners;
		private List<WorkBoxDto> workbox;
		private int resultCount;
		private List<CustomAttributeValue> customAttributeValues;

		public List<TaskEventsDto> getTasks() {
			return tasks;
		}

		public AdminParseResponse(List<TaskEventsDto> tasks, List<ProcessEventsDto> processes,
				List<TaskOwnersDto> owners, List<WorkBoxDto> workbox, int resultCount,
				List<CustomAttributeValue> customAttributeValues) {
			super();
			this.tasks = tasks;
			this.processes = processes;
			this.owners = owners;
			this.workbox = workbox;
			this.resultCount = resultCount;
			this.customAttributeValues = customAttributeValues;
		}

		public List<CustomAttributeValue> getCustomAttributeValues() {
			return customAttributeValues;
		}

		public void setCustomAttributeValues(List<CustomAttributeValue> customAttributeValues) {
			this.customAttributeValues = customAttributeValues;
		}

		public void setTasks(List<TaskEventsDto> tasks) {
			this.tasks = tasks;
		}

		public List<ProcessEventsDto> getProcesses() {
			return processes;
		}

		public void setProcesses(List<ProcessEventsDto> processes) {
			this.processes = processes;
		}

		public List<TaskOwnersDto> getOwners() {
			return owners;
		}

		public void setOwners(List<TaskOwnersDto> owners) {
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
			return "AdminParseResponse [tasks=" + tasks + ", processes=" + processes + ", owners=" + owners
					+ ", workbox=" + workbox + ", resultCount=" + resultCount + "]";
		}

	}
