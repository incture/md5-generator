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
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.workflow.dto.RuleDto;
import oneapp.incture.workbox.demo.workflow.entity.ServiceRuleEntity;

@Repository("RuleDao")
public class RuleDao extends BaseDao<ServiceRuleEntity, RuleDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
		
	}
	
	protected ServiceRuleEntity importDto(RuleDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		ServiceRuleEntity entity = new ServiceRuleEntity();
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getRuleId())
				&& !ServicesUtil.isEmpty(fromDto.getRuleTypeId())) {
			entity.setRuleTypeId(fromDto.getRuleTypeId());
			entity.setRuleId(fromDto.getRuleId());

			if (!ServicesUtil.isEmpty(fromDto.getCustom_key()))
				entity.setCustom_key(fromDto.getCustom_key());
			if (!ServicesUtil.isEmpty(fromDto.getDestination()))
				entity.setDestination(fromDto.getDestination());
			if (!ServicesUtil.isEmpty(fromDto.getOperator()))
				entity.setOperator(fromDto.getOperator());
			if (!ServicesUtil.isEmpty(fromDto.getRuleType()))
				entity.setRuleType(fromDto.getRuleType());
			if (!ServicesUtil.isEmpty(fromDto.getCondition()))
				entity.setCondition(fromDto.getCondition());
			if (!ServicesUtil.isEmpty(fromDto.getAttributeName()))
				entity.setAttributeName(fromDto.getAttributeName());
			if (!ServicesUtil.isEmpty(fromDto.getLogic()))
				entity.setLogic(fromDto.getLogic());
			if (!ServicesUtil.isEmpty(fromDto.getValue()))
				entity.setValue(fromDto.getValue());

		}
		return entity;
	}

	@Override
	protected RuleDto exportDto(ServiceRuleEntity entity) {
		RuleDto ruleDTo = new RuleDto();
		if (!ServicesUtil.isEmpty(entity) && !ServicesUtil.isEmpty(entity.getRuleId())
				&& !ServicesUtil.isEmpty(entity.getRuleTypeId())) {
			ruleDTo.setRuleTypeId(entity.getRuleTypeId());
			ruleDTo.setRuleId(entity.getRuleId());

			if (!ServicesUtil.isEmpty(entity.getCustom_key()))
				ruleDTo.setCustom_key(entity.getCustom_key());
			if (!ServicesUtil.isEmpty(entity.getDestination()))
				ruleDTo.setDestination(entity.getDestination());
			if (!ServicesUtil.isEmpty(entity.getOperator()))
				ruleDTo.setOperator(entity.getOperator());
			if (!ServicesUtil.isEmpty(entity.getRuleType()))
				ruleDTo.setRuleType(entity.getRuleType());
			if (!ServicesUtil.isEmpty(entity.getCondition()))
				ruleDTo.setCondition(entity.getCondition());
			if (!ServicesUtil.isEmpty(entity.getAttributeName()))
				ruleDTo.setAttributeName(entity.getAttributeName());
			if (!ServicesUtil.isEmpty(entity.getLogic()))
				ruleDTo.setLogic(entity.getLogic());
			if (!ServicesUtil.isEmpty(entity.getValue()))
				ruleDTo.setValue(entity.getValue());

		}
		return ruleDTo;
	}

	public void saveOrUpdateRules(List<RuleDto> ruleDtos) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (!ServicesUtil.isEmpty(ruleDtos) && !ruleDtos.isEmpty()) {
				
				for (int i = 0; i < ruleDtos.size(); i++) {
					RuleDto currentTask = ruleDtos.get(i);
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
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Saving of Process Template ERROR" + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<RuleDto> getRulesById(String templateId) {
		List<RuleDto> ruleDtos = new ArrayList<>();
		RuleDto ruleDto = null;

		try {
			Query fetchQry = this.getSession()
					.createSQLQuery("SELECT RULE_ID,RULE_TYPE_ID,RULE_TYPE,CUSTOM_KEY,RULE_CONDITION,DESTINATION"
							+ ",OPERATOR,LOGIC,VALUE,ATTRIBUTE_NAME "
							+ " FROM SERVICE_RULE_TABLE" + " WHERE RULE_TYPE_ID = '" + templateId + "'");
			List<Object[]> fetchResult = fetchQry.list();
			if (!ServicesUtil.isEmpty(fetchResult)) {
				for (Object[] obj : fetchResult) {
					ruleDto = new RuleDto();
					ruleDto.setRuleId((String) obj[0]);

					ruleDto.setRuleTypeId((String) obj[1]);
					ruleDto.setRuleType((String) obj[2]);

					ruleDto.setCustom_key((String) obj[3]);
					ruleDto.setCondition((String) obj[4]);

					ruleDto.setDestination((String) obj[5]);
					ruleDto.setOperator((String) obj[6]);
					ruleDto.setAttributeName(ServicesUtil.asString(obj[9]));
					ruleDto.setLogic(ServicesUtil.asString(obj[7]));
					ruleDto.setValue(ServicesUtil.asString(obj[8]));
					ruleDtos.add(ruleDto);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Error in getting constant values" + e.getMessage());
		}
		return ruleDtos;
	}

}
