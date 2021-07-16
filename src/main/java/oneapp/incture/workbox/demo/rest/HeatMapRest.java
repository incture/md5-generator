package oneapp.incture.workbox.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.workload.dto.SearchListResponseDto;
import oneapp.incture.workbox.demo.workload.dto.UserWorkLoadResponseDto;
import oneapp.incture.workbox.demo.workload.dto.UserWorkloadRequestDto;
import oneapp.incture.workbox.demo.workload.services.HeatMapFacadeLocal;

//@Api(value="user", description="heat map controller")
//@ApiOperation(value="heatmap")

@RestController
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/heatMap", produces = "application/json")
public class HeatMapRest {

	@Autowired
	private HeatMapFacadeLocal heatMapFacadeLocal;

	@RequestMapping(value = "/getSearchList", method = RequestMethod.GET)
	public SearchListResponseDto forwarding() {
		return heatMapFacadeLocal.getSearchList();
	}

	@RequestMapping(value = "/getUserWorkload", method = RequestMethod.POST)
	public UserWorkLoadResponseDto getUserWorkload(@RequestBody UserWorkloadRequestDto dto
			,@AuthenticationPrincipal Token token) {
		return heatMapFacadeLocal.getUserWorkLoad(dto,token);
	}
	
	@RequestMapping(value = "/getStatusList", method = RequestMethod.GET)
	public SearchListResponseDto getStatusList() {
		return heatMapFacadeLocal.getStatusList();
	}
}