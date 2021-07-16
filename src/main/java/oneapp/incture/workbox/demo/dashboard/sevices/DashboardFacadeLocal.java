package oneapp.incture.workbox.demo.dashboard.sevices;


import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.dashboard.dto.DashboardResponseDto;
import oneapp.incture.workbox.demo.dashboard.dto.RequestDto;
public interface DashboardFacadeLocal {

	DashboardResponseDto getGraphDetails(String processType, String graphType, String duration,
			String userId, Integer taskPage, Integer userPage,Token token);

	ResponseMessage createQuickLink(RequestDto requestdto);
	
	DashboardResponseDto getDetails(String processType, String graphType, String duration, String userId, Integer taskPage, Integer userPage);

}
