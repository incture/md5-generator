package oneapp.incture.workbox.demo.adhocTask.util;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValueTableAdhocDo;

public class ParseResponse {
	List<TaskEventsDto> tasks;
	List<TaskOwnersDto> owners;
	List<CustomAttributeValue> customAttributeValues;
	List<CustomAttributeValueTableAdhocDo> customTableAttributeValues;
	public List<CustomAttributeValueTableAdhocDo> getCustomTableAttributeValues() {
		return customTableAttributeValues;
	}

	public void setCustomTableAttributeValues(List<CustomAttributeValueTableAdhocDo> customTableAttributeValues) {
		this.customTableAttributeValues = customTableAttributeValues;
	}

	List<TaskAuditDto> auditDtos;
	ResponseMessage responseMessage;

	@Override
	public String toString() {
		return "ParseResponse [tasks=" + tasks + ", owners=" + owners + ", customAttributeValues="
				+ customAttributeValues + ", customTableAttributeValues=" + customTableAttributeValues + ", auditDtos="
				+ auditDtos + ", responseMessage=" + responseMessage + "]";
	}

	public List<TaskAuditDto> getAuditDtos() {
		return auditDtos;
	}

	public void setAuditDtos(List<TaskAuditDto> auditDtos) {
		this.auditDtos = auditDtos;
	}

	public List<TaskEventsDto> getTasks() {
		return tasks;
	}

	public void setTasks(List<TaskEventsDto> tasks) {
		this.tasks = tasks;
	}

	public List<TaskOwnersDto> getOwners() {
		return owners;
	}

	public void setOwners(List<TaskOwnersDto> owners) {
		this.owners = owners;
	}

	public List<CustomAttributeValue> getCustomAttributeValues() {
		return customAttributeValues;
	}

	public void setCustomAttributeValues(List<CustomAttributeValue> customAttributeValues) {
		this.customAttributeValues = customAttributeValues;
	}

	public ResponseMessage getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}

}
