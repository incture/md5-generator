package oneapp.incture.workbox.demo.adminConsole.sevices;


import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.WorkloadRangeDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkloadRangeDto;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adminConsole.dto.WorkloadRangeResponse;

/**
 * Session Bean implementation class ConfigurationFacade
 */
@Service("AdminConsoleConfigurationFacade")
public class ConfigurationFacade implements ConfigurationFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationFacade.class);

	@Autowired
	private ProcessConfigDao processLabelDao;
	@Autowired
	private WorkloadRangeDao rangeDao;

	
	@Override
	public ProcessConfigDto getBusinessLabelByProcessName(String processName) {
		ProcessConfigDto processConfigDto = null;
		if (!ServicesUtil.isEmpty(processName)) {
			processConfigDto = new ProcessConfigDto();
			processConfigDto.setProcessName(processName.trim());
			try {
				processConfigDto = processLabelDao.getByKeys(processConfigDto);
				if (!ServicesUtil.isEmpty(processConfigDto) && !ServicesUtil.isEmpty(processConfigDto.getUserGroup())) {
					processConfigDto.setUserList(Arrays.asList(processConfigDto.getUserGroup().split(",")));
				}
			} catch (ExecutionFault e) {
				e.printStackTrace();
			} catch (InvalidInputFault e) {
				e.printStackTrace();
			} catch (NoResultFault e) {
				logger.error(e.getMessage());
			}
		}
		return processConfigDto;
	}

	@Override
	public WorkloadRangeResponse getWorkLoadRange() {
		WorkloadRangeResponse response = new WorkloadRangeResponse();
		List<WorkloadRangeDto> rangeDtos = null;
		ResponseMessage responseMessage = new ResponseMessage();
		try {
			rangeDtos = rangeDao.getAllResults("WorkloadRangeDo");
			responseMessage.setMessage("Workload Ranges Fetched Sucessfully");
			responseMessage.setStatus("SUCCESS");
			responseMessage.setStatusCode("1");
		} catch (NoResultFault e) {
			responseMessage.setMessage("Failed due to" + e.getMessage());
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("0");
		}
		// response.setResponseMessage(responseMessage);
		response.setWorkloadRangeDtos(rangeDtos);
		return response;
	}


}
