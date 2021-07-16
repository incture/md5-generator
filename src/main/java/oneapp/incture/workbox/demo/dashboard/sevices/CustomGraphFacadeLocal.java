package oneapp.incture.workbox.demo.dashboard.sevices;

import java.util.List;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.dashboard.dto.CustomGraphDto;
import oneapp.incture.workbox.demo.dashboard.dto.GraphConfigDetail;
import oneapp.incture.workbox.demo.dashboard.dto.GraphConfigurationDto;
import oneapp.incture.workbox.demo.dashboard.dto.GraphDetail;
import oneapp.incture.workbox.demo.dashboard.dto.GraphListDto;


public interface CustomGraphFacadeLocal {

	GraphListDto getGraphList(String type, Token token);

	GraphConfigDetail getGraphDetails(String graphName);

	ResponseMessage createOrUpdateGraph(GraphConfigurationDto graphConfigurationDto, String type,Token token);

	ResponseMessage deleteGraph(String graphId, String type);

	CustomGraphDto getCustomGraph(String graphId, Integer page);

	ResponseMessage updateGraph(GraphDetail graphDetail);

	ResponseMessage updateGraphList(List<GraphDetail> graphDetails);

}
