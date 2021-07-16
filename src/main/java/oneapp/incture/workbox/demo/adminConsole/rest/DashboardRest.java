package oneapp.incture.workbox.demo.adminConsole.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.dashboard.dto.CustomGraphDto;
import oneapp.incture.workbox.demo.dashboard.dto.DashboardResponseDto;
import oneapp.incture.workbox.demo.dashboard.dto.GraphConfigDetail;
import oneapp.incture.workbox.demo.dashboard.dto.GraphConfigurationDto;
import oneapp.incture.workbox.demo.dashboard.dto.GraphDetail;
import oneapp.incture.workbox.demo.dashboard.dto.GraphListDto;
import oneapp.incture.workbox.demo.dashboard.dto.RequestDto;
import oneapp.incture.workbox.demo.dashboard.sevices.CustomGraphFacadeLocal;
import oneapp.incture.workbox.demo.dashboard.sevices.DashboardFacadeLocal;

@RestController
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/Dashboard", produces = "application/json")
public class DashboardRest {

	@Autowired
	private CustomGraphFacadeLocal customGraphFacadeLocal;
	
	@Autowired
	private DashboardFacadeLocal dashboardFacadeLocal;

	@RequestMapping(value = "/getAllDetails", method = RequestMethod.GET)
	public DashboardResponseDto getDashboardDetails(
			@RequestParam(value = "processName", required = false) String processName,
			@RequestParam(value = "GraphType", required = false) String GraphType,
			@RequestParam(value = "duration", required = false) String duration,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "taskPage", required = false) Integer taskPage,
			@RequestParam(value = "userPage", required = false) Integer userPage,
			@AuthenticationPrincipal Token token) {
		return dashboardFacadeLocal.getGraphDetails(processName, GraphType, duration, userId, taskPage, userPage,
				token);
	}

	@RequestMapping(value = "/createQuickLink", method = RequestMethod.POST)
	public ResponseMessage createQuickLink(@RequestBody RequestDto dto) {
		return dashboardFacadeLocal.createQuickLink(dto);
	}

	@RequestMapping(value = "/getGraphList", method = RequestMethod.GET)
	public GraphListDto createQuickLink(@RequestParam(value = "type", required = false) String type,@AuthenticationPrincipal Token token) {
		return customGraphFacadeLocal.getGraphList(type,token);
	}

	@RequestMapping(value = "/updateGraphList", method = RequestMethod.POST)
	public ResponseMessage updateGraphList(@RequestBody List<GraphDetail> graphDetails) {
		return customGraphFacadeLocal.updateGraphList(graphDetails);
	}

	@RequestMapping(value = "/updateGraph", method = RequestMethod.POST)
	public ResponseMessage updateGraph(@RequestBody GraphDetail graphDetail) {
		return customGraphFacadeLocal.updateGraph(graphDetail);
	}

	@RequestMapping(value = "/getGraphDetails/{graphId}", method = RequestMethod.GET)
	public GraphConfigDetail getGraphDetails(@PathVariable String graphId) {
		return customGraphFacadeLocal.getGraphDetails(graphId);
	}

	@RequestMapping(value = "/createGraph", method = RequestMethod.POST)
	public ResponseMessage createGraph(@RequestBody GraphConfigurationDto graphConfigurationDto,
			@RequestParam(value = "type", required = false) String type,@AuthenticationPrincipal Token token) {
		return customGraphFacadeLocal.createOrUpdateGraph(graphConfigurationDto, type,token);
	}

	@RequestMapping(value = "/deleteGraph/{graphId}", method = RequestMethod.GET)
	public ResponseMessage deleteGraph(@PathVariable String graphId,
			@RequestParam(value = "type", required = false) String type) {
		return customGraphFacadeLocal.deleteGraph(graphId, type);
	}

	@RequestMapping(value = "/getCustomGraph/{graphId}", method = RequestMethod.GET)
	public CustomGraphDto getCustomGraph(@PathVariable String graphId,
			@RequestParam(value = "page", required = false) Integer page) {
		return customGraphFacadeLocal.getCustomGraph(graphId, page);
	}
}