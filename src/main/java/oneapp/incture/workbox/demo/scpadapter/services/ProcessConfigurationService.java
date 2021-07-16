package oneapp.incture.workbox.demo.scpadapter.services;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigurationDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTaskDetailsResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessContextEntity;

public interface ProcessConfigurationService {

	List<String> getAllConfiguredProcesses();

	ProcessConfigurationDto newProcesses();

	ResponseMessage configureProcess(String content, String processDisplayName, String sla, String origin,
			String critical);

	ProcessTaskDetailsResponse getProcessTaskDetails(String processName);

	String getContextData(String processName);

	ProcessContextEntity processContextData(String processName);

	String updateProcessContextData(ProcessContextEntity processName);
}
