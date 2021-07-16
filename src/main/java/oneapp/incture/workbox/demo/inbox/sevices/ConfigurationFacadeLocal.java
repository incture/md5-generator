package oneapp.incture.workbox.demo.inbox.sevices;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.inbox.dto.ProcessConfigResponse;
import oneapp.incture.workbox.demo.inbox.dto.ProcessListDto;
import oneapp.incture.workbox.demo.inbox.dto.ReportAgingDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkloadRangeResponse;




public interface ConfigurationFacadeLocal {

	ProcessListDto getAllProcessNames();

	ProcessConfigResponse getAllBusinessLabels();

	ProcessConfigDto getBusinessLabelByProcessName(String processName);

	WorkloadRangeResponse getWorkLoadRange();

	List<ReportAgingDto> getAgeingBuckets(String reportName);

	ProcessConfigResponse getUserBusinessLabels(String userRole);

}
