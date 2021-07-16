package oneapp.incture.workbox.demo.inbox.sevices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.WorkloadRangeDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkloadRangeDto;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dao.ReportAgingDao;
import oneapp.incture.workbox.demo.inbox.dto.ProcessConfigResponse;
import oneapp.incture.workbox.demo.inbox.dto.ProcessListDto;
import oneapp.incture.workbox.demo.inbox.dto.ReportAgingDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkloadRangeResponse;


/**
 * Session Bean implementation class ConfigurationFacade
 */
@Service("ConfigurationFacade")
public class ConfigurationFacade implements ConfigurationFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationFacade.class);

	@Autowired
	private ProcessConfigDao processLabelDao;
	@Autowired
	private ProcessEventsDao processEventsDao;
	@Autowired
	private WorkloadRangeDao rangeDao;
	@Autowired
	private ReportAgingDao agingDao;

	@Override
	public ProcessListDto getAllProcessNames() {

		List<String> processNameList = null;
		try {
			processNameList = processEventsDao.getAllProcessName();
		} catch (NoResultFault e) {
		}
		ProcessListDto processListDto = new ProcessListDto();
		processListDto.setProcessNameList(processNameList);
		return processListDto;
	}

	@Override
	public ProcessConfigResponse getAllBusinessLabels() {
		ProcessConfigResponse response = new ProcessConfigResponse();
		ResponseMessage responseMessage = new ResponseMessage();
		List<ProcessConfigDto> processConfigDtos = null;
		try {
			processConfigDtos = getLabels(processLabelDao.getAllProcessConfigEntry());
			if (!ServicesUtil.isEmpty(processConfigDtos)) {
				responseMessage.setMessage("Business Labels Fetched successfully");
			} else {
				responseMessage.setMessage(PMCConstant.NO_RESULT);
			}

			responseMessage.setStatus("SUCCESS");
			responseMessage.setStatusCode("1");
		} catch (NoResultFault e) {
			logger.error(e.getMessage());
			responseMessage.setMessage("Failed due to" + e.getMessage());
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("0");
		}
		response.setProcessConfigDtos(processConfigDtos);
		response.setResponseMessage(responseMessage);
		return response;
	}

	private List<ProcessConfigDto> getLabels(List<ProcessConfigDto> processConfigDtos) {
		Set<String> userGroupSet = new LinkedHashSet<String>();
		if (!ServicesUtil.isEmpty(processConfigDtos)) {
			ProcessConfigDto allProcessDto = null;
			for (ProcessConfigDto dto : processConfigDtos) {
				if (!ServicesUtil.isEmpty(dto) && !ServicesUtil.isEmpty(dto.getUserGroup())) {
					if (PMCConstant.SEARCH_ALL.equals(dto.getUserGroup())) {
						// userGroupSet.add(dto.getUserGroup());
						allProcessDto = dto;
					} else {
						List<String> li = new ArrayList<String>();
						li.addAll(Arrays.asList(dto.getUserGroup().split(", ")));
						li.add(PMCConstant.SEARCH_SMALL_ALL);
						dto.setUserList(li);
						userGroupSet.addAll(dto.getUserList());
					}
				}
			}
			List<String> allUserList = new ArrayList<String>();
			allUserList.addAll(userGroupSet);
			if (allProcessDto != null)
				allProcessDto.setUserList(allUserList);
		}
		return processConfigDtos;
	}

	@Override
	public ProcessConfigResponse getUserBusinessLabels(String userRole) {
		ProcessConfigResponse response = new ProcessConfigResponse();
		ResponseMessage responseMessage = new ResponseMessage();
		List<ProcessConfigDto> processConfigDtos = null;
		try {
			processConfigDtos = getLabels(processLabelDao.getAllProcessConfigEntryByRole(userRole));
			if (!ServicesUtil.isEmpty(processConfigDtos)) {
				responseMessage.setMessage("User Business Labels Fetched successfully");
			} else {
				responseMessage.setMessage(PMCConstant.NO_RESULT);
			}
			responseMessage.setStatus("SUCCESS");
			responseMessage.setStatusCode("1");
		} catch (NoResultFault e) {
			logger.error(e.getMessage());
			responseMessage.setMessage("Failed due to" + e.getMessage());
			responseMessage.setStatus("FAILURE");
			responseMessage.setStatusCode("0");
		}
		response.setProcessConfigDtos(processConfigDtos);
		response.setResponseMessage(responseMessage);
		return response;
	}

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

	@Override
	public List<ReportAgingDto> getAgeingBuckets(String reportName) {
		return agingDao.getConfigByReportName(reportName);
	}

}
