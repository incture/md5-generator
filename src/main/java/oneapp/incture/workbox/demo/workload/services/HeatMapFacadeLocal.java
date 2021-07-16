package oneapp.incture.workbox.demo.workload.services;


import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.workload.dto.SearchListResponseDto;
import oneapp.incture.workbox.demo.workload.dto.UserWorkLoadResponseDto;
import oneapp.incture.workbox.demo.workload.dto.UserWorkloadRequestDto;

public interface HeatMapFacadeLocal {

	public SearchListResponseDto getSearchList();

	public UserWorkLoadResponseDto getUserWorkLoad(UserWorkloadRequestDto dto,Token token);
	
	public SearchListResponseDto getStatusList();

}
