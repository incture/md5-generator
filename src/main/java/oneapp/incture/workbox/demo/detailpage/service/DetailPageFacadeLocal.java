package oneapp.incture.workbox.demo.detailpage.service;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.DetailsPageReponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskConfigurationDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskTemplateTableDto;
import oneapp.incture.workbox.demo.detailpage.dto.DynamicTaskDetailsDto;

public interface DetailPageFacadeLocal {

	DynamicTaskDetailsDto getDynamicDetails(String taskId, String selectType);

	DetailsPageReponseDto getDynamicDetailsNew(String taskId);

	TaskConfigurationDto taskConfiguration(String processName, String taskName);

	ResponseMessage configurationUpdate(TaskTemplateTableDto taskTemplateTableDto);

	DetailsPageReponseDto dynamicDetailFromContext(String taskId);
	
	List<TaskAuditDto> getAuditLogDetails(String taskId, String signature);
}
