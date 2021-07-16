package oneapp.incture.workbox.demo.workload.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.workload.dao.HeatMapDao;
import oneapp.incture.workbox.demo.workload.dto.SearchListDto;
import oneapp.incture.workbox.demo.workload.dto.SearchListResponseDto;
import oneapp.incture.workbox.demo.workload.dto.UserWorkLoadResponseDto;
import oneapp.incture.workbox.demo.workload.dto.UserWorkloadDto;
import oneapp.incture.workbox.demo.workload.dto.UserWorkloadRequestDto;

@Service("HeatMapFacade")
public class HeatMapFacade implements HeatMapFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(HeatMapFacade.class);

	@Autowired
	HeatMapDao heatMapDao;

	ResponseMessage responseDto;

	public SearchListResponseDto getSearchList() {
		SearchListResponseDto dto = new SearchListResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {
			List<SearchListDto> procList = new ArrayList<SearchListDto>(), statusList = new ArrayList<SearchListDto>();
			procList = heatMapDao.getSearchList(PMCConstant.SEARCH_PROCESS);
			statusList = heatMapDao.getSearchList(PMCConstant.SEARCH_STATUS);
			if (!ServicesUtil.isEmpty(procList) || !ServicesUtil.isEmpty(statusList)) {
				dto.setProcList(procList);
				dto.setStatusList(statusList);
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			logger.error("[PMC][SubstitutionRuleFacade][getUserSubstitution][error]" + e.getMessage());
			responseDto.setMessage("Fetching data failed due to " + e.getMessage());
		}
		dto.setMessage(responseDto);
		return dto;
	}

	public UserWorkLoadResponseDto getUserWorkLoad(UserWorkloadRequestDto dto,Token token) {
		UserWorkLoadResponseDto response = new UserWorkLoadResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {
			List<UserWorkloadDto> dtos = heatMapDao.getUserWorkloadNew(dto.getProcessName(), dto.getRequestId(),
					dto.getTaskStatus(),token);

			if (!ServicesUtil.isEmpty(dtos)) {
				response.setUserWorkloadDtos(dtos);
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			logger.error("[PMC][HeatMap][getUserWorkLoad][error]" + e.getMessage());
			responseDto.setMessage("Fetching data failed due to " + e.getMessage());
		}
		response.setMessage(responseDto);
		return response;
	}

	public SearchListResponseDto getStatusList() {
		SearchListResponseDto dto = new SearchListResponseDto();
		responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");
		try {
			List<SearchListDto> statusList = new ArrayList<SearchListDto>();
			statusList = heatMapDao.getStatusList();
			if (!ServicesUtil.isEmpty(statusList)) {
				dto.setStatusList(statusList);
				responseDto.setMessage("Data fetched Successfully");
			} else {
				responseDto.setMessage(PMCConstant.NO_RESULT);
			}
			responseDto.setStatus("SUCCESS");
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			logger.error("[PMC][SubstitutionRuleFacade][getUserSubstitution][error]" + e.getMessage());
			responseDto.setMessage("Fetching data failed due to " + e.getMessage());
		}
		dto.setMessage(responseDto);
		return dto;
	}

}