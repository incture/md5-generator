package oneapp.incture.workbox.demo.substitution.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleAuditDto;
import oneapp.incture.workbox.demo.substitution.entity.SubstitutionRuleAuditDo;

@Repository
//@Transactional
public class SubstitutionRuleAuditDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		}catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	protected SubstitutionRuleAuditDo importDto(SubstitutionRuleAuditDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		SubstitutionRuleAuditDo entity = new SubstitutionRuleAuditDo();
		if (!ServicesUtil.isEmpty(fromDto.getRuleId()))
			entity.setRuleId(fromDto.getRuleId());
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			entity.setUserId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getColumnName()))
			entity.setColumnName(fromDto.getColumnName());
		if (!ServicesUtil.isEmpty(fromDto.getColumnValueNew()))
			entity.setColumnValueNew(fromDto.getColumnValueNew());
		if (!ServicesUtil.isEmpty(fromDto.getColumnValueOld()))
			entity.setColumnValueOld(fromDto.getColumnValueOld());
		if (!ServicesUtil.isEmpty(fromDto.getActionType()))
			entity.setActionType(fromDto.getActionType());
		if (!ServicesUtil.isEmpty(fromDto.getUpdatedAt()))
			entity.setUpdatedAt(fromDto.getUpdatedAt());

		return entity;
	}

	protected SubstitutionRuleAuditDto exportDto(SubstitutionRuleAuditDo entity) {
		SubstitutionRuleAuditDto dto = new SubstitutionRuleAuditDto();
		if (!ServicesUtil.isEmpty(entity.getRuleId()))
			dto.setRuleId(entity.getRuleId());
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			dto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getColumnName()))
			dto.setColumnName(entity.getColumnName());
		if (!ServicesUtil.isEmpty(entity.getColumnValueNew()))
			dto.setColumnValueNew(entity.getColumnValueNew());
		if (!ServicesUtil.isEmpty(entity.getColumnValueOld()))
			dto.setColumnValueOld(entity.getColumnValueOld());
		if (!ServicesUtil.isEmpty(entity.getActionType()))
			dto.setActionType(entity.getActionType());
		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			dto.setUpdatedAt(entity.getUpdatedAt());
		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			dto.setUpdateAtInString(ServicesUtil.convertFromDateToString(entity.getUpdatedAt()));

		return dto;

	}

	public void saveOrUpdateRuleAudit(List<SubstitutionRuleAuditDto> entity) throws Exception {
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for (int i = 0; i < entity.size(); i++) {
				session.saveOrUpdate(importDto(entity.get(i)));
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev]ERROR ENTERING AUDIT" + e.getMessage());
		}

	}

	@SuppressWarnings({ "unchecked" })
	public HashMap<String, String> getRuleAuditDetails(String ruleId) {
		HashMap<String, String> map = new HashMap<String, String>();
		Session session = sessionFactory.openSession();

		String queryString = "select substitutingUser,startDate,endDate from" + " SubstitutionRuleDo where ruleId='"
				+ ruleId + "'";
		Query query = session.createQuery(queryString);
		List<Object[]> resultList = query.list();

		String queryStringSubstitutionProcess = "select process from SubstitutionProcessDo where ruleId='" + ruleId + "'";
		Query querySubstitutionProcess = session.createQuery(queryStringSubstitutionProcess);
		List<Object> resultListSubstitutionProcess = querySubstitutionProcess.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			map.put("substitutingUser", (String) resultList.get(0)[0]);
			map.put("startDate", (String) ServicesUtil.convertFromDateToString((Date) resultList.get(0)[1]));
			map.put("endDate", (String) ServicesUtil.convertFromDateToString((Date) resultList.get(0)[2]));
			String valueSeperatedByComa = "";
			for (int i = 0; i < resultListSubstitutionProcess.size(); i++) {
				valueSeperatedByComa += (String) resultListSubstitutionProcess.get(i) + ",";
			}
			map.put("processList", valueSeperatedByComa.substring(0, valueSeperatedByComa.length() - 1));
		}

		return map;
	}
	
	@SuppressWarnings({ "unchecked" })
    public HashMap<String, String> getRuleAuditDetails(String ruleId,int version) {
		Session session = sessionFactory.openSession();
        HashMap<String, String> map = new HashMap<String, String>();
        String queryString = "select substitutingUser,startDate,endDate from" + " SubstitutionRuleDo where ruleId='"
                + ruleId + "' and version='"+version+"'";
        Query query = session.createQuery(queryString);
        List<Object[]> resultList = query.list();

        String queryStringSubstitutionProcess = "select process from SubstitutionProcessDo where ruleId='" + ruleId + "'";
        Query querySubstitutionProcess = this.getSession().createQuery(queryStringSubstitutionProcess);
        List<Object> resultListSubstitutionProcess = querySubstitutionProcess.list();
        if (!ServicesUtil.isEmpty(resultList)) {
            map.put("substitutingUser", (String) resultList.get(0)[0]);
            map.put("startDate", (String) ServicesUtil.convertFromDateToString((Date) resultList.get(0)[1]));
            map.put("endDate", (String) ServicesUtil.convertFromDateToString((Date) resultList.get(0)[2]));
            String valueSeperatedByComa = "";
            for (int i = 0; i < resultListSubstitutionProcess.size(); i++) {
                valueSeperatedByComa += (String) resultListSubstitutionProcess.get(i) + ",";
            }
            map.put("processList", valueSeperatedByComa.substring(0, valueSeperatedByComa.length() - 1));
        }

 

        return map;
    }

	public List<String> getRuleAuditColumn() {
		Session session = sessionFactory.openSession();
		String queryString = "select CONSTANT_NAME from CROSS_CONSTANTS where CONSTANT_ID='ra.substitution'";
		Query query = session.createSQLQuery(queryString);
		List<String> resultList = query.list();
		return resultList;

	}

	public List<SubstitutionRuleAuditDto> filter(SubstitutionRuleAuditDto dto) {
		Session session = sessionFactory.openSession();
		List<SubstitutionRuleAuditDto> ruleDtos = new ArrayList<SubstitutionRuleAuditDto>();
		try {
			SubstitutionRuleAuditDto substitutionRuleAuditDto = null;
			String queryString = "select do from SubstitutionRuleAuditDo do";

			if (!ServicesUtil.isEmpty(dto.getRuleId()) || (!ServicesUtil.isEmpty(dto.getUserId())
					|| !ServicesUtil.isEmpty(dto.getActionType()) || !ServicesUtil.isEmpty(dto.getColumnName())
					|| !ServicesUtil.isEmpty(dto.getColumnValueNew()) || !ServicesUtil.isEmpty(dto.getUpdatedAt())
					|| !ServicesUtil.isEmpty(dto.getColumnValueOld()))) {

				queryString += " where ";
				if (!ServicesUtil.isEmpty(dto.getRuleId())) {
					queryString += " upper(do.ruleId)=upper('" + dto.getRuleId() + "') and ";
				}
				if (!ServicesUtil.isEmpty(dto.getUserId())) {
					queryString += " upper(do.userId)=upper('" + dto.getUserId() + "') and ";
				}
				if (!ServicesUtil.isEmpty(dto.getActionType())) {
					queryString += " upper(do.actionType)=upper('" + dto.getActionType() + "') and ";
				}
				if (!ServicesUtil.isEmpty(dto.getColumnName())) {
					queryString += " upper(do.columnName)=upper('" + dto.getColumnName() + "') and ";
				}
				if (!ServicesUtil.isEmpty(dto.getColumnValueNew())) {
					queryString += " upper(do.columnValueNew)=upper('" + dto.getColumnValueNew() + "') and ";
				}
				if (!ServicesUtil.isEmpty(dto.getColumnValueOld())) {
					queryString += " upper(do.columnValueOld)=upper('" + dto.getColumnValueOld() + "') and ";
				}
				queryString = queryString.substring(0, queryString.length() - 4);
				queryString += " order by do.updatedAt desc ";
				System.err.println("[WBP-Dev][Workbox][queryString]" + queryString);
				Query query = session.createQuery(queryString);
				List<SubstitutionRuleAuditDo> resultList = query.list();
				if (!ServicesUtil.isEmpty(resultList)) {

					for (SubstitutionRuleAuditDo entity : resultList) {
						substitutionRuleAuditDto = new SubstitutionRuleAuditDto();

						substitutionRuleAuditDto = exportDto(entity);
						ruleDtos.add(substitutionRuleAuditDto);
					}
				}

			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][SubstitutionRuleAuditDao][filter][error]" + e.getLocalizedMessage());
		}
		return ruleDtos;
	}

}

