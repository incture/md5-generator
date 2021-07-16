package oneapp.incture.workbox.demo.dashboard.sevices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.security.um.user.User;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.dashboard.dao.DashboardDao;
import oneapp.incture.workbox.demo.dashboard.dao.UserQuickLinkDao;
import oneapp.incture.workbox.demo.dashboard.dto.DashboardDto;
import oneapp.incture.workbox.demo.dashboard.dto.DashboardResponseDto;
import oneapp.incture.workbox.demo.dashboard.dto.RequestDto;
import oneapp.incture.workbox.demo.dashboard.dto.UserQuickLinkDto;

@Service("DashboardFacade")
public class DashboardFacade implements DashboardFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(DashboardFacade.class);

	@Autowired
	DashboardDao graphDao;

	@Autowired
	UserQuickLinkDao quickLinkDao;

	@Override
	public DashboardResponseDto getGraphDetails(String processType, String graphType, String duration, String userId,
			Integer taskPage, Integer userPage,Token token) {
		DashboardResponseDto response = new DashboardResponseDto();
		DashboardDto graphDto = null;
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setStatus("FAILURE");
		responseDto.setStatusCode("1");

		if (!ServicesUtil.isEmpty(graphType)) {
			if (graphType.contains("taskCount")) {
//				User user = UserManagementUtil.getLoggedInUser();
				userId = token.getLogonName();
			}
			if (graphType.contains("taskCompletionTrend") && ServicesUtil.isEmpty(duration)) {
				responseDto.setMessage("Duration is mandatory while requesting task completion trend graph");
				response.setMessage(responseDto);
				System.err.println("DashboardFacade.getGraphDetails() response : " + response);
				return response;
			}
		} else {
//			User user = UserManagementUtil.getLoggedInUser();
			userId =token.getLogonName();
			graphType = "";
		}
		try {
			graphDto = graphDao.getDetails(processType, graphType, duration, userId, taskPage, userPage,token);

			if (!ServicesUtil.isEmpty(graphDto)) {
				response.setGraphDto(graphDto);
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

	@Override
	public ResponseMessage createQuickLink(RequestDto requestdto) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setMessage(PMCConstant.FAILURE);
		responseDto.setStatusCode(PMCConstant.CODE_FAILURE);
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);

		try {
			String user = requestdto.getStrName();
			UserQuickLinkDto dto = null;
			for (String str : requestdto.getIds()) {
				dto = new UserQuickLinkDto();
				dto.setQuickLink(str);
				dto.setUserId(user);
				quickLinkDao.create(dto);
			}
			responseDto.setMessage("Quick Link " + PMCConstant.CREATED_SUCCESS);
			responseDto.setStatus(PMCConstant.SUCCESS);
			responseDto.setStatusCode("0");
		} catch (Exception e) {
			logger.error("[PMC][HeatMap][getUserWorkLoad][error]" + e.getMessage());
			responseDto.setMessage("Quick Link " + PMCConstant.CREATE_FAILURE);
		}

		return responseDto;
	}

	@Override
	public DashboardResponseDto getDetails(String processType, String graphType, String duration, String userId,
			Integer taskPage, Integer userPage) {
		// TODO Auto-generated method stub
		return null;
	}
}