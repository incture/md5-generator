package oneapp.incture.workbox.demo.substitution.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionProcessDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRulesDto;
import oneapp.incture.workbox.demo.substitution.entity.SubstitutionProcessDo;

@Repository
//@Transactional
public class SubstitutionProcessVersionDao extends BaseDao<SubstitutionProcessDo, SubstitutionProcessDto> {
	
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	UserTaskMappingDao userTaskDao;

	@Autowired
	TaskOwnersDao taskOwnerDao;

	@Autowired
	SubstitutionRuleAuditDao substitutionRuleAuditDao;

	@Autowired
	SubstitutionProcessApproverDao substitutionProcessApproverDao;
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		}catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	@Override
	protected SubstitutionProcessDo importDto(SubstitutionProcessDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		SubstitutionProcessDo entity = new SubstitutionProcessDo();
		if (!ServicesUtil.isEmpty(fromDto.getRuleId()))
			entity.setRuleId(fromDto.getRuleId());
		if (!ServicesUtil.isEmpty(fromDto.getVersion()))
			entity.setVersion(fromDto.getVersion());
		if (!ServicesUtil.isEmpty(fromDto.getProcess()))
			entity.setProcess(fromDto.getProcess());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getStatusDef()))
			entity.setStatusDef(fromDto.getStatusDef());
		if (!ServicesUtil.isEmpty(fromDto.getApprovedAt()))
			entity.setApprovedAt(ServicesUtil.convertFromStringToDateSubstitution(fromDto.getApprovedAt()));

		return entity;
	}

	@Override
	protected SubstitutionProcessDto exportDto(SubstitutionProcessDo entity) {

		SubstitutionProcessDto dto = new SubstitutionProcessDto();
		if (!ServicesUtil.isEmpty(entity.getRuleId()))
			dto.setRuleId(entity.getRuleId());
		if (!ServicesUtil.isEmpty(entity.getVersion()))
			dto.setVersion(entity.getVersion());
		if (!ServicesUtil.isEmpty(entity.getProcess()))
			dto.setProcess(entity.getProcess());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			dto.setStatus(entity.getStatus());
		if (!ServicesUtil.isEmpty(entity.getStatusDef()))
			dto.setStatusDef(entity.getStatusDef());
		if (!ServicesUtil.isEmpty(entity.getApprovedAt()))
			dto.setApprovedAt(ServicesUtil.convertFromDateToString(entity.getApprovedAt()));

		return dto;
	}

	public String deleteRecord(String ruleId) {

		String deleteQuery = "Delete from SUBSTITUTION_PROCESSES where rule_id='" + ruleId + "'";
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int resultRows = session.createSQLQuery(deleteQuery).executeUpdate();
		tx.commit();
		session.close();

		if (resultRows > 0)
			return PMCConstant.SUCCESS;

		else
			return PMCConstant.FAILURE;
	}

	@SuppressWarnings({ "unchecked" })
	public List<String> getProcessList(String substitutedUsers, String substitutingUser) {
		try {
			Session session = sessionFactory.openSession();
			String queryString = "select distinct sp.process from SUBSTITUTION_PROCESSES sp join"
					+ " substitution_rule sr on sr.rule_id=sp.rule_id where sr.substituted_user='" + substitutedUsers
					+ "' and sr.substituting_user='" + substitutingUser + "'";

			Query query = session.createSQLQuery(queryString);
			return query.list();
		} catch (Exception e) {
			System.err
					.println("[Workbox][SubstitutionProcessVersionDao][getSubstitutedUserTask][Error]" + e.getLocalizedMessage());
		}
		return null;
	}

	public String updateRecord(List<String> processList, String ruleId, int version, SubstitutionRulesDto dto,Token token) {
		List<String> procList = new ArrayList<String>(processList);
		String resultStr = "";
		try {
			// updating the previous approval request as cancelled
			cancelApprovalRequest(ruleId, version - 1);

			// New subs. to be created
			if (!ServicesUtil.isEmpty(procList)) {
				try {

					Map<Integer, String> statusMapping = new HashMap<>();
					statusMapping.put(0, "PENDING");
					statusMapping.put(1, "APPROVED");
					statusMapping.put(2, "REJECTED");
					statusMapping.put(3, "CANCELED");

					Map<String, Integer> processApprovalStatus = substitutionProcessApproverDao
							.checkForApprovalRequired(processList, ruleId, version, dto,token);
					for (Object key : processApprovalStatus.keySet()) {
						create(new SubstitutionProcessDto(ruleId, (String) key, version,
								(Integer) processApprovalStatus.get(key),
								statusMapping.get((Integer) processApprovalStatus.get(key)) , ServicesUtil.getUtcTime()));
					}
				} catch (Exception e) {
					System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionProcessVersionDao][updateRecord][Error]");
				}
				resultStr += PMCConstant.CREATE;
			}
			return resultStr;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox]Error :" + e.getLocalizedMessage());
		}
		return PMCConstant.FAILURE;
	}

	public void cancelApprovalRequest(String ruleId, int version) {	
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			
			String statusQuery = "select cav1.task_id from custom_attr_values cav1 join  (select task_id from custom_attr_values where attr_value = '"
					+ ruleId
					+ "' and process_name = 'd6118c84ij54') cav2 on cav1.task_id = cav2.task_id where cav1.attr_value = '"
					+ version + "'";
			String taskId = (String) session.createSQLQuery(statusQuery).list().get(0);
			String updateQuery = "update task_events set status = 'CANCELED' , completed_at = '" + ServicesUtil.getUTCTime()
					+ "' where event_id = '" + taskId + "'";
			session.createSQLQuery(updateQuery).executeUpdate();
			
			
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionProcessVersionDao][cancelApprovalRequest][Error] " + e.getMessage());
		}
		finally{
			String query = "update substitution_processes set status = 3 , status_def = 'CANCELED' where rule_id = '"
					+ ruleId + "' and version = " + version + " and status = 0";
			session.createSQLQuery(query).executeUpdate();
		}
		tx.commit();
		session.close();
		
	}

}
