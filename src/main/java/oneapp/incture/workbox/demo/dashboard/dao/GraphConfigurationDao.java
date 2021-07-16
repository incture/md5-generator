package oneapp.incture.workbox.demo.dashboard.dao;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.GraphConfigurationDo;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dashboard.dto.GraphConfigurationDto;
import oneapp.incture.workbox.demo.dashboard.dto.GraphDetail;



@Repository("GraphConfigurationDao")
public class GraphConfigurationDao extends BaseDao<GraphConfigurationDo, GraphConfigurationDto>{

	@Autowired
    private SessionFactory sessionFactory;
	
	@Override
	protected GraphConfigurationDto exportDto(GraphConfigurationDo entity) {

		GraphConfigurationDto GraphConfigurationDto = new GraphConfigurationDto();

		if (!ServicesUtil.isEmpty(entity.getGraphConfigId()))
			GraphConfigurationDto.setGraphConfigId(entity.getGraphConfigId());
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			GraphConfigurationDto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getGraphName()))
			GraphConfigurationDto.setGraphName(entity.getGraphName());
		if (!ServicesUtil.isEmpty(entity.getChartType()))
			GraphConfigurationDto.setChartType(entity.getChartType());
		if (!ServicesUtil.isEmpty(entity.getGridView()))
			GraphConfigurationDto.setGridView(entity.getGridView());
		if (!ServicesUtil.isEmpty(entity.getShowLegends()))
			GraphConfigurationDto.setShowLegends(entity.getShowLegends());
		if (!ServicesUtil.isEmpty(entity.getxLabel()))
			GraphConfigurationDto.setxLabel(entity.getxLabel());
		if (!ServicesUtil.isEmpty(entity.getxParameter()))
			GraphConfigurationDto.setxParameter(entity.getxParameter());
		if (!ServicesUtil.isEmpty(entity.getxScrollbar()))
			GraphConfigurationDto.setxScrollbar(entity.getxScrollbar());
		if (!ServicesUtil.isEmpty(entity.getxCategory()))
			GraphConfigurationDto.setxCategory(entity.getxCategory());
		if (!ServicesUtil.isEmpty(entity.getyLabel()))
			GraphConfigurationDto.setyLabel(entity.getyLabel());
		if (!ServicesUtil.isEmpty(entity.getyParameter()))
			GraphConfigurationDto.setyParameter(entity.getyParameter());
		if (!ServicesUtil.isEmpty(entity.getyScrollbar()))
			GraphConfigurationDto.setyScrollbar(entity.getyScrollbar());
		if (!ServicesUtil.isEmpty(entity.getyCategory()))
			GraphConfigurationDto.setyCategory(entity.getyCategory());
		if (!ServicesUtil.isEmpty(entity.getSequence()))
			GraphConfigurationDto.setSequence(entity.getSequence());
		if (!ServicesUtil.isEmpty(entity.getFrameDetail()))
			GraphConfigurationDto.setFrameDetail(entity.getFrameDetail());
		if (!ServicesUtil.isEmpty(entity.getIsActive()))
			GraphConfigurationDto.setIsActive(entity.getIsActive());
		if (!ServicesUtil.isEmpty(entity.getFilterData()))
			GraphConfigurationDto.setFilterData(entity.getFilterData());
		if (!ServicesUtil.isEmpty(entity.getxFilter()))
			GraphConfigurationDto.setxFilter(Arrays.asList(entity.getxFilter().split(",")));
		if (!ServicesUtil.isEmpty(entity.getyFilter()))
			GraphConfigurationDto.setyFilter(Arrays.asList(entity.getyFilter().split(",")));
		if (!ServicesUtil.isEmpty(entity.getxAxisTopValue()))
			GraphConfigurationDto.setxAxisTopValue(entity.getxAxisTopValue());
		if (!ServicesUtil.isEmpty(entity.getyAxisTopValue()))
			GraphConfigurationDto.setyAxisTopValue(entity.getyAxisTopValue());

		return GraphConfigurationDto;
	}

	@Override
	protected GraphConfigurationDo importDto(GraphConfigurationDto fromDto) {

		GraphConfigurationDo entity = new GraphConfigurationDo();

		if (!ServicesUtil.isEmpty(fromDto.getGraphConfigId()))
			entity.setGraphConfigId(fromDto.getGraphConfigId());
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			entity.setUserId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getGraphName()))
			entity.setGraphName(fromDto.getGraphName());
		if (!ServicesUtil.isEmpty(fromDto.getChartType()))
			entity.setChartType(fromDto.getChartType());
		if (!ServicesUtil.isEmpty(fromDto.getGridView()))
			entity.setGridView(fromDto.getGridView());
		if (!ServicesUtil.isEmpty(fromDto.getShowLegends()))
			entity.setShowLegends(fromDto.getShowLegends());
		if (!ServicesUtil.isEmpty(fromDto.getxLabel()))
			entity.setxLabel(fromDto.getxLabel());
		if (!ServicesUtil.isEmpty(fromDto.getxParameter()))
			entity.setxParameter(fromDto.getxParameter());
		if (!ServicesUtil.isEmpty(fromDto.getxScrollbar()))
			entity.setxScrollbar(fromDto.getxScrollbar());
		if (!ServicesUtil.isEmpty(fromDto.getxCategory()))
			entity.setxCategory(fromDto.getxCategory());
		if (!ServicesUtil.isEmpty(fromDto.getyLabel()))
			entity.setyLabel(fromDto.getyLabel());
		if (!ServicesUtil.isEmpty(fromDto.getyParameter()))
			entity.setyParameter(fromDto.getyParameter());
		if (!ServicesUtil.isEmpty(fromDto.getyScrollbar()))
			entity.setyScrollbar(fromDto.getyScrollbar());
		if (!ServicesUtil.isEmpty(fromDto.getyCategory()))
			entity.setyCategory(fromDto.getyCategory());
		if (!ServicesUtil.isEmpty(fromDto.getSequence()))
			entity.setSequence(fromDto.getSequence());
		if (!ServicesUtil.isEmpty(fromDto.getFrameDetail()))
			entity.setFrameDetail(fromDto.getFrameDetail());
		if (!ServicesUtil.isEmpty(fromDto.getIsActive()))
			entity.setIsActive(fromDto.getIsActive());
		if (!ServicesUtil.isEmpty(fromDto.getFilterData()))
			entity.setFilterData(fromDto.getFilterData());
		if (!ServicesUtil.isEmpty(fromDto.getxFilter()))
			entity.setxFilter(String.join(",", fromDto.getxFilter()));
		if (!ServicesUtil.isEmpty(fromDto.getyFilter()))
			entity.setyFilter(String.join(",", fromDto.getyFilter()));
		if (!ServicesUtil.isEmpty(fromDto.getxAxisTopValue()))
			entity.setxAxisTopValue(fromDto.getxAxisTopValue());
		if (!ServicesUtil.isEmpty(fromDto.getyAxisTopValue()))
			entity.setyAxisTopValue(fromDto.getyAxisTopValue());

		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<GraphDetail> getUserGraphs(String userId, String type) {
		List<GraphDetail> graphDetails = new ArrayList<>();
		GraphDetail graphDetail = null;
		try{
			if(!ServicesUtil.isEmpty(type) || PMCConstant.ADMIN.equals(type))
				userId = PMCConstant.ADMIN;
			Query getGraphQry = this.getSession().createSQLQuery("SELECT GRAPH_NAME, CHART_TYPE, GRAPH_CONFIG_ID,IS_ACTIVE,SEQUENCE,USER_ID "
					+ " FROM GRAPH_CONFIGURATION WHERE USER_ID = '"+userId+"' "
					+ "or GRAPH_CONFIG_ID in (SELECT GRAPH_CONFIG_ID FROM GRAPH_CONFIGURATION "
					+ "WHERE USER_ID = 'Admin' and GRAPH_NAME not in "
					+ "(select GRAPH_NAME from GRAPH_CONFIGURATION where USER_ID = '"+userId+"')) "
					+ " ORDER BY case when user_Id = 'Admin' then 1 else 2 end ,SEQUENCE");
			List<Object[]> result = getGraphQry.list();

			Integer i =1;
			if(!ServicesUtil.isEmpty(result)){
				for (Object[] obj : result) {
					graphDetail = new GraphDetail();
					graphDetail.setGraphName(ServicesUtil.isEmpty(obj[0])?null:obj[0].toString());
					graphDetail.setChartType(ServicesUtil.isEmpty(obj[1])?null:obj[1].toString());
					graphDetail.setGraphConfigId(ServicesUtil.isEmpty(obj[2])?null:obj[2].toString());
					graphDetail.setIsActive(ServicesUtil.isEmpty(obj[3])?false:(boolean) obj[3]);
					graphDetail.setSequence(i);
					graphDetail.setUserId(ServicesUtil.isEmpty(obj[5])?null:obj[5].toString());
					graphDetails.add(graphDetail);
					i++;
				}
			}
		}catch (Exception e) {
			System.err.println("[WBP-DEV] GraphConfigurationDao.getUserGraphs() error"+e);
		}
		return graphDetails;
	}

	public GraphConfigurationDo getGraphDetail(String graphId) {
		GraphConfigurationDo graphConfigurationDo = null;
		try{
			Query getGraphQry = this.getSession().createQuery("select p from GraphConfigurationDo p "
					+ "where p.graphConfigId= '"+graphId+"'");
			graphConfigurationDo = (GraphConfigurationDo) getGraphQry.uniqueResult();

		}catch (Exception e) {
			System.err.println("[WBP-DEV]GraphConfigurationDao.getGraphDetail() error :"+e);
		}
		return graphConfigurationDo;
	}

	public ResponseMessage saveOrUpdateGraphConfiguration(GraphConfigurationDto graphConfigurationDto) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		Session session=null;
		try {
			session = sessionFactory.openSession();
	        Transaction tx = session.beginTransaction();
	        session.saveOrUpdate(importDto(graphConfigurationDto));
			tx.commit();
			session.close();
			resp.setMessage(PMCConstant.SUCCESS);
			resp.setStatus(PMCConstant.SUCCESS);
			resp.setStatusCode(PMCConstant.CODE_SUCCESS);
		} catch (Exception e) {
			System.err.println("[WBP-Dev]saveOrUpdateGraphConfig " + e);
		}
		return resp;
	}

	public ResponseMessage deleteGraph(String graphId, String type) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		Session session=null;
		try {
			session = sessionFactory.openSession();
	        Transaction tx = session.beginTransaction();
			String deleteQryStr = "DELETE FROM GRAPH_CONFIGURATION "
					+ "WHERE GRAPH_CONFIG_ID = '"+graphId+"' ";
			if(ServicesUtil.isEmpty(type) || !PMCConstant.ADMIN.equalsIgnoreCase(type))
				deleteQryStr = deleteQryStr+" AND USER_ID != 'Admin'";
			Query deleteQry = session.createSQLQuery(deleteQryStr);
			Integer count = deleteQry.executeUpdate();
			if(count>0){
				resp.setMessage("Graph is Deleted");
				resp.setStatus(PMCConstant.SUCCESS);
				resp.setStatusCode(PMCConstant.CODE_SUCCESS);
			}else {
				resp.setMessage("Default Graph Cannot be deleted");
				resp.setStatus(PMCConstant.SUCCESS);
				resp.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev]delete Graph Config " + e);
		}
		return resp;
	}

	public Integer updateGraphIsActive(GraphDetail graphDetail) {
		Session session=null;
		session=sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query deleteQry = session.createSQLQuery("UPDATE GRAPH_CONFIGURATION SET IS_ACTIVE= :val "
				+ "WHERE GRAPH_CONFIG_ID = '"+graphDetail.getGraphConfigId()+"'");
		deleteQry.setParameter("val", graphDetail.getIsActive());
		Integer count = deleteQry.executeUpdate();
		tx.commit();
		session.close();
		return count;
	}

	public ResponseMessage updateGraphList(List<GraphDetail> graphDetails) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		Session session=null;
		try{
			
			session=sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for (GraphDetail graphs : graphDetails) {
				
				Query updateQry = session.createSQLQuery("UPDATE GRAPH_CONFIGURATION SET SEQUENCE = :val "
						+ "WHERE GRAPH_CONFIG_ID = '"+graphs.getGraphConfigId()+"'");
				updateQry.setParameter("val", graphs.getSequence());
				updateQry.executeUpdate();	
				
			}
			tx.commit();
			session.close();
			resp.setMessage("Graph Sequence Updated");
			resp.setStatus(PMCConstant.SUCCESS);
			resp.setStatusCode(PMCConstant.CODE_SUCCESS);
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Graph Config updateGraphList" + e);
		}
		return resp;
	}

	public GraphConfigurationDto setGraphDto(GraphConfigurationDo graphConfigurationDo) {
		GraphConfigurationDto graphConfigurationDto = new GraphConfigurationDto();
		graphConfigurationDto = exportDto(graphConfigurationDo);
		return graphConfigurationDto;
	}

}