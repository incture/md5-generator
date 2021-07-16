package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.entity.ActionTypeConfigDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class ActionTypeConfigDao {

	@Autowired
	SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,List<ActionTypeConfigDo>> getActionTypeConfig(){
		Map<String,List<ActionTypeConfigDo>> actionDto = new HashMap<String, List<ActionTypeConfigDo>>();
		List<ActionTypeConfigDo> actionConfigDos = null;
		ActionTypeConfigDo actionConfigDo = null;
		Query q = this.getSession().createSQLQuery("SELECT ACTION_TYPE, STATUS, ACTIONS, TYPE , ACTION_NATURE FROM ACTION_TYPE_CONFIG ");
		List<Object[]> resultList = q.list();
		
		for (Object[] obj : resultList) {
			if(actionDto.containsKey(obj[0].toString())){
				actionConfigDos = actionDto.get(obj[0].toString());
				actionConfigDo = new ActionTypeConfigDo();
				actionConfigDo.setStatus(obj[1].toString());
				actionConfigDo.setActions(obj[2].toString());
				actionConfigDo.setType(obj[3].toString());
				actionConfigDo.setActionNature(ServicesUtil.isEmpty(obj[4])? null :obj[4].toString());
				actionConfigDos.add(actionConfigDo);
			}else{
				actionConfigDos = new ArrayList<ActionTypeConfigDo>();
				actionConfigDo = new ActionTypeConfigDo();
				actionConfigDo.setStatus(obj[1].toString());
				actionConfigDo.setActions(obj[2].toString());
				actionConfigDo.setType(obj[3].toString());
				actionConfigDo.setActionNature(ServicesUtil.isEmpty(obj[4])? null :obj[4].toString());
				actionConfigDos.add(actionConfigDo);
				actionDto.put(obj[0].toString(), actionConfigDos);
			}
		}
			
		return actionDto;
	}

	public void deleteInActionConfig(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInPCT = "DELETE FROM ACTION_CONFIG WHERE PROCESS_NAME IN (:processName)";
		Query deleteInPCTQuery = session.createSQLQuery(deleteInPCT);
		deleteInPCTQuery.setParameterList("processName", processName);
		deleteInPCTQuery.executeUpdate();
		tx.commit();
		session.close();
	}

	public void deleteInActionConfigTask(String processName,String taskName) {
		try{
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
		String deleteInPCT = "DELETE FROM ACTION_CONFIG WHERE PROCESS_NAME IN (:processName) AND TASK_NAME IN (:taskName)";
		Query deleteInPCTQuery = session.createSQLQuery(deleteInPCT);
		deleteInPCTQuery.setParameter("processName", processName);
		deleteInPCTQuery.setParameter("taskName", taskName);
		deleteInPCTQuery.executeUpdate();
		tx.commit();
		session.close();
		}catch (Exception e) {
			System.err.println("ActionTypeConfigDao.deleteInActionConfigTask() error"+e);
		}
	}
}
