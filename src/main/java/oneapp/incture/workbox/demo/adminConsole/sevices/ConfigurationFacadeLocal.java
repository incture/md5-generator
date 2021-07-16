package oneapp.incture.workbox.demo.adminConsole.sevices;


import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adminConsole.dto.WorkloadRangeResponse;


public interface ConfigurationFacadeLocal {

	ProcessConfigDto getBusinessLabelByProcessName(String processName);

	WorkloadRangeResponse getWorkLoadRange();
}
