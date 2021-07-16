package oneapp.incture.workbox.demo.dashboard.sevices;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.cloud.security.xsuaa.token.Token;
//import com.sap.security.um.user.User;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.GraphConfigurationDo;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.dashboard.dao.GraphConfigurationDao;
import oneapp.incture.workbox.demo.dashboard.dto.CustomGraphDto;
import oneapp.incture.workbox.demo.dashboard.dto.GraphConfigDetail;
import oneapp.incture.workbox.demo.dashboard.dto.GraphConfigurationDto;
import oneapp.incture.workbox.demo.dashboard.dto.GraphDataDto;
import oneapp.incture.workbox.demo.dashboard.dto.GraphDetail;
import oneapp.incture.workbox.demo.dashboard.dto.GraphListDto;
import oneapp.incture.workbox.demo.dashboard.util.GraphConfigurationUtil;
import oneapp.incture.workbox.demo.inbox.dao.UserIDPMappingDao;



@Service
//@Transactional
public class CustomGraphFacade implements CustomGraphFacadeLocal{

	@Autowired
	GraphConfigurationDao graphConfigurationDao;

	@Autowired
	GraphConfigurationUtil graphConfigurationUtil;

	@Autowired
	UserIDPMappingDao userIdpMappingDao;

	@Override
	public GraphListDto getGraphList(String type, Token token) {
		GraphListDto graphListDto = new GraphListDto();
		List<GraphDetail> graphDetails = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		try{
			//User user = UserManagementUtil.getLoggedInUser();
			graphDetails = graphConfigurationDao.getUserGraphs(token.getLogonName().toUpperCase(),type);
			graphListDto.setGraphDetails(graphDetails);
			if(ServicesUtil.isEmpty(graphDetails)){
				resp.setMessage("Graph List is Empty");
				resp.setStatus(PMCConstant.SUCCESS);
				resp.setStatusCode(PMCConstant.CODE_SUCCESS);
			}else{
				resp.setMessage("Graph List Fetched");
				resp.setStatus(PMCConstant.SUCCESS);
				resp.setStatusCode(PMCConstant.CODE_SUCCESS);
			}

		}catch (Exception e) {
			System.err.println("[WBP-DEV]CustomGraphFacade.getGraphList() ERROR :"+e);
		}
		graphListDto.setResponseMessage(resp);
		return graphListDto;
	}

	@Override
	public GraphConfigDetail getGraphDetails(String graphId) {
		GraphConfigDetail graphConfigDetail = new GraphConfigDetail();
		GraphConfigurationDo graphConfigurationDo = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		try{
			graphConfigurationDo = graphConfigurationDao.getGraphDetail(graphId);
			
			if(!ServicesUtil.isEmpty(graphConfigurationDo)){
				graphConfigDetail.setGraphConfigurationDo(graphConfigurationDao.setGraphDto(graphConfigurationDo));
				resp.setMessage("Graph Detail Fetched");
				resp.setStatus(PMCConstant.SUCCESS);
				resp.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		}catch (Exception e) {
			System.err.println("[WBP-DEV]CustomGraphFacade.getGraphDetails() error :"+e);
		}
		graphConfigDetail.setResponseMessage(resp);
		return graphConfigDetail;
	}

	@Override
	public ResponseMessage createOrUpdateGraph(GraphConfigurationDto graphConfigurationDto,String type,Token token) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		try{
			//User user = UserManagementUtil.getLoggedInUser();
			if(ServicesUtil.isEmpty(graphConfigurationDto.getGraphConfigId()) ||
					(PMCConstant.ADMIN.equalsIgnoreCase(graphConfigurationDto.getUserId()) && 
							!PMCConstant.ADMIN.equalsIgnoreCase(type))){
				String graphId = UUID.randomUUID().toString().replace("-", "");
				graphConfigurationDto.setGraphConfigId(graphId);
			}
		
			if(!PMCConstant.ADMIN.equalsIgnoreCase(type))
				graphConfigurationDto.setUserId(token.getLogonName().toUpperCase());
			else if(PMCConstant.ADMIN.equalsIgnoreCase(type))
				graphConfigurationDto.setUserId(PMCConstant.ADMIN);

			resp = graphConfigurationDao.saveOrUpdateGraphConfiguration(graphConfigurationDto);
		}catch (Exception e) {
			System.err.println("[WBP-DEV]CustomGraphFacade.createOrUpdateGraph() error :"+e);
		}
		return resp;
	}

	@Override
	public ResponseMessage deleteGraph(String graphId,String type) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		try{
			resp = graphConfigurationDao.deleteGraph(graphId,type);
		}catch (Exception e) {
			System.err.println("[WBP-DEV]CustomGraphFacade.createOrUpdateGraph() error :"+e);
		}
		return resp;
	}

	@Override
	public CustomGraphDto getCustomGraph(String graphId,Integer page) {
		CustomGraphDto customGraphDto = new CustomGraphDto();
		List<GraphDataDto> graphCounts = null;
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		try{
			GraphConfigurationDo graphConfigurationDo = graphConfigurationDao.getGraphDetail(graphId);

			graphCounts = graphConfigurationUtil.getGraphCount(graphConfigurationDo,page);
			
			if(!ServicesUtil.isEmpty(graphCounts)){
				customGraphDto.setData(graphCounts);
				customGraphDto.setGraphConfigurationDo(graphConfigurationDo);
				resp.setMessage(PMCConstant.SUCCESS);
				resp.setStatus(PMCConstant.SUCCESS);
				resp.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
			customGraphDto.setTotalCount(graphConfigurationUtil.getTotalCount(graphConfigurationDo));
			
		}catch (Exception e) {
			System.err.println("[WBP-DEV] Get Custom Graph details error :"+e);
		}
		customGraphDto.setResponseMessage(resp);
		return customGraphDto;
	}

	@Override
	public ResponseMessage updateGraph(GraphDetail graphDetail) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		try{
			Integer count = graphConfigurationDao.updateGraphIsActive(graphDetail);
			if(count>0){
				resp.setMessage(PMCConstant.SUCCESS);
				resp.setStatus(PMCConstant.SUCCESS);
				resp.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		}catch (Exception e) {
			System.err.println("[WBP-Dev]CustomGraphFacade.updateGraph() error :"+e);
		}
		return resp;
	}

	@Override
	public ResponseMessage updateGraphList(List<GraphDetail> graphDetails) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		try{
			resp = graphConfigurationDao.updateGraphList(graphDetails);

		}catch (Exception e) {
			System.err.println("[WBP-Dev]CustomGraphFacade.updateGraph() error :"+e);
		}
		return resp;
	}


}
