package oneapp.incture.workbox.demo.workflow.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.entity.CrossConstantDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.workflow.dto.CrossConstantDto;
import oneapp.incture.workbox.demo.workflow.dto.ValuesDto;

@Repository("WorkflowCrossConstant")
public class CrossConstantDao extends BaseDao<CrossConstantDo, CrossConstantDto>{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected CrossConstantDo importDto(CrossConstantDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		CrossConstantDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto)){
			entity = new CrossConstantDo();
			if (!ServicesUtil.isEmpty(fromDto.getConstantId()))
				entity.setConstantId(fromDto.getConstantId());
			if (!ServicesUtil.isEmpty(fromDto.getConstantName()))
				entity.setConstantName(fromDto.getConstantName());
			if (!ServicesUtil.isEmpty(fromDto.getConstantValue()))
				entity.setConstantValue(fromDto.getConstantValue());
		}
		return entity;
	}

	@Override
	protected CrossConstantDto exportDto(CrossConstantDo entity) {
		
		CrossConstantDto CrossConstantDto = new CrossConstantDto();
		if (!ServicesUtil.isEmpty(entity)){
			if (!ServicesUtil.isEmpty(entity.getConstantId()))
				CrossConstantDto.setConstantId(entity.getConstantId());
			if (!ServicesUtil.isEmpty(entity.getConstantName()))
				CrossConstantDto.setConstantName(entity.getConstantName());
			if (!ServicesUtil.isEmpty(entity.getConstantValue()))
				CrossConstantDto.setConstantValue(entity.getConstantValue());
		}
		return CrossConstantDto;
	}

	public void saveOrUpdateCrossConstant(CrossConstantDto crossConstantDto) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try{
			if (!ServicesUtil.isEmpty(crossConstantDto)) {
				
				session.saveOrUpdate(importDto(crossConstantDto));
				
				session.flush();
				session.clear();
			}
			tx.commit();
			session.close();
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING Cross Constant] ERROR:"+e.getMessage());
		}
	}

	public void saveOrUpdateCrossConstants(List<CrossConstantDto> crossConstantDtos) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try{
			if (!ServicesUtil.isEmpty(crossConstantDtos) && !crossConstantDtos.isEmpty()) {
				
				for (int i = 0; i < crossConstantDtos.size(); i++) {
					CrossConstantDto currentTask = crossConstantDtos.get(i);
					session.saveOrUpdate(importDto(currentTask));
					if (i % 20 == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				tx.commit();
				session.close();
			}
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING Cross Constant] ERROR:"+e.getMessage());
		}
	}

	public void deleteCrossConstant(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteIncC = "DELETE FROM CROSS_CONSTANTS WHERE CONSTANT_NAME IN (:processName) AND CONSTANT_ID = 'pe.name'";
		Query  deleteIncCTQuery= session.createSQLQuery(deleteIncC);
		deleteIncCTQuery.setParameterList("processName", processName);
		deleteIncCTQuery.executeUpdate();
		tx.commit();
		session.close();

	}
	
	public void deleteCrossConstant(String processName,String id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteIncC = "DELETE FROM CROSS_CONSTANTS WHERE CONSTANT_NAME IN (:processName) AND CONSTANT_ID IN (:id)";
		Query  deleteIncCTQuery= session.createSQLQuery(deleteIncC);
		deleteIncCTQuery.setParameter("id", id);
		deleteIncCTQuery.setParameter("processName", processName);
		deleteIncCTQuery.executeUpdate();
		tx.commit();
		session.close();

	}

	public void removeValues(String cutomKey, List<String> valuesToRemove) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteQueryStr = "DELETE FROM CROSS_CONSTANTS WHERE CONSTANT_ID ='"+cutomKey+"' AND "
				+ "CONSTANT_NAME IN ('"+String.join("','", valuesToRemove)+"')";
		Query  deleteIncCTQuery= session.createSQLQuery(deleteQueryStr);
		deleteIncCTQuery.executeUpdate();
		tx.commit();
		session.close();
	}

	@SuppressWarnings("unchecked")
	public List<ValuesDto> getValues(String customKey) {
		List<ValuesDto> valuesDtos = new ArrayList<>();
		ValuesDto valuesDto = null;
		try{
			Query fetchQry = this.getSession().createSQLQuery("SELECT CONSTANT_NAME,CONSTANT_VALUE FROM CROSS_CONSTANTS"
					+ " WHERE CONSTANT_ID = '"+customKey+"'");
			List<Object[]> fetchResult = fetchQry.list();
			if(!ServicesUtil.isEmpty(fetchResult)){
				for (Object[] obj : fetchResult) {
					valuesDto = new ValuesDto();
					valuesDto.setIsEdited(0);
					valuesDto.setValue(obj[0].toString());
					valuesDto.setValueName(obj[1].toString());
					valuesDtos.add(valuesDto);
				}
			}
		}catch(Exception e){
			System.err.println("[WBP-Dev]Error in getting constant values"+e.getMessage());
		}
		return valuesDtos;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getActionValues(String customKey) {
		List<Object[]> fetchResult = null;
		try{
			
			Query fetchQry = this.getSession().createSQLQuery("select cc2.constant_id,cc2.constant_name from cross_constants cc1 join cross_constants cc2 "
					+ "on cc1.constant_name=cc2.constant_id where cc1.constant_id='"+customKey+"'");
			
			fetchResult = fetchQry.list();
	
		}catch(Exception e){
			System.err.println("[WBP-Dev]Error in getting constant values"+e.getMessage());
		}
		return fetchResult;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> getAllDropDownValues(String processName) {
		List<Object[]> fetchResult = new ArrayList<>();
		try{
			
			Query fetchQry = this.getSession().createSQLQuery("SELECT CONSTANT_ID, CONSTANT_NAME,CONSTANT_VALUE"
					+ " FROM CROSS_CONSTANTS WHERE CONSTANT_ID IN"
					+ " (SELECT KEY FROM CUSTOM_ATTR_TEMPLATE WHERE PROCESS_NAME = '"+processName+"' OR "
					+ "PROCESS_NAME IN (SELECT TEMPLATE_ID FROM PROCESS_TEMPLATE"
					+ " WHERE PROCESS_NAME = '"+processName+"'))");
			
			fetchResult = fetchQry.list();
	
		}catch(Exception e){
			System.err.println("[WBP-Dev]Error in getting constant values"+e.getMessage());
		}
		return fetchResult;
	
	}

}
