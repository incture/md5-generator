package oneapp.incture.workbox.demo.workflow.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.workflow.dto.OwnerSelectionRuleDto;
import oneapp.incture.workbox.demo.workflow.entity.OwnerSelectionRuleDo;

@Repository
public class OwnerSelectionRuleDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}
	
	public void savaOrUpdateOwnerSelectionRule(List<OwnerSelectionRuleDo> ownerSelectionRuleDos) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (!ServicesUtil.isEmpty(ownerSelectionRuleDos) && !ownerSelectionRuleDos.isEmpty()) {
				
				for (int i = 0; i < ownerSelectionRuleDos.size(); i++) {
					OwnerSelectionRuleDo currentTask = ownerSelectionRuleDos.get(i);
					session.saveOrUpdate(currentTask);
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
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Saving of Status config ERROR" + e.getMessage());
		}
	}

	public List<OwnerSelectionRuleDto> getRules(String processName, String taskName) {
		List<OwnerSelectionRuleDto> ownerSelectionRuleDtos = new ArrayList<OwnerSelectionRuleDto>();
		try{
		
		OwnerSelectionRuleDto ownerSelectionRuleDto = null;
		String query =  "SELECT RULE_ID,ATTRIBUTE,RULE_CONDITION,APPROVER,ATTRIBUTE_NAME,LOGIC,VALUE , IS_DELETED FROM OWNER_SELECTION_RULE"
				+ " WHERE PROCESS_NAME = '"+processName+"' AND TASK_NAME = '"+taskName+"' AND IS_DELETED = 0";
		
		Query qry = this.getSession().createSQLQuery(query);
		List<Object[]> result = qry.list();
		
		for (Object[] obj : result) {
			ownerSelectionRuleDto = new OwnerSelectionRuleDto();
			ownerSelectionRuleDto.setApprover(Arrays.asList(ServicesUtil.asString(obj[3]).split(",")));
			ownerSelectionRuleDto.setCustom_key(ServicesUtil.asString(obj[1]));
			ownerSelectionRuleDto.setCondition(ServicesUtil.asString(obj[2]));
			ownerSelectionRuleDto.setRuleId(ServicesUtil.asString(obj[0]));
			ownerSelectionRuleDto.setAttributeName(ServicesUtil.asString(obj[4]));
			ownerSelectionRuleDto.setLogic(ServicesUtil.asString(obj[5]));
			ownerSelectionRuleDto.setValue(ServicesUtil.asString(obj[6]));
			ownerSelectionRuleDto.setIsDeleted(ServicesUtil.asBoolean(obj[7]));
			ownerSelectionRuleDtos.add(ownerSelectionRuleDto);
		}
		}catch (Exception e) {
			System.err.println("error"+e);
		}
		return ownerSelectionRuleDtos;
		
	}

	public void savaOrUpdateOwnerRule(List<OwnerSelectionRuleDto> ownerSelectionRules, String processName, String taskName) {
		List<OwnerSelectionRuleDo> ownerSelectionRuleDos = new ArrayList<>();
		OwnerSelectionRuleDo ownerSelectionRuleDo = null;
		if(!ServicesUtil.isEmpty(ownerSelectionRules)){
			for (OwnerSelectionRuleDto rules : ownerSelectionRules) {
				ownerSelectionRuleDo = new OwnerSelectionRuleDo();
				ownerSelectionRuleDo.setApprover(String.join(",",rules.getApprover()));
				ownerSelectionRuleDo.setAttribute(rules.getCustom_key());
				ownerSelectionRuleDo.setCondition(rules.getCondition());
				ownerSelectionRuleDo.setProcessName(processName);
				ownerSelectionRuleDo.setRuleId(rules.getRuleId());
				ownerSelectionRuleDo.setTaskName(taskName);
				ownerSelectionRuleDo.setAttributeName(rules.getAttributeName());
				ownerSelectionRuleDo.setLogic(rules.getLogic());
				ownerSelectionRuleDo.setValue(rules.getValue());
				ownerSelectionRuleDo.setIsDeleted(rules.getIsDeleted());
				ownerSelectionRuleDos.add(ownerSelectionRuleDo);
			}
			
			savaOrUpdateOwnerSelectionRule(ownerSelectionRuleDos);
		}
		
	}
	
	public void deleteOwnerSelectionRule(List<String> processName) {

		try {
			String query = "DELETE FROM OWNER_SELECTION_RULE WHERE PROCESS_NAME IN (:processName)";
			Session session = this.getSession();
			Transaction transaction = session.getTransaction();
			session.createSQLQuery(query).setParameter("processName", processName).executeUpdate();
			transaction.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[Dev][OwnerSelectionRuleDao][deleteOwnerSelectionRule] error : " + e.getMessage());
		}
	}
}
