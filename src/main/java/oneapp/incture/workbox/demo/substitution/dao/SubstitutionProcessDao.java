package oneapp.incture.workbox.demo.substitution.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.entity.PinnedTaskDo;
import oneapp.incture.workbox.demo.inbox.entity.PinnedTaskDoPK;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionProcessDto;
import oneapp.incture.workbox.demo.substitution.entity.SubstitutionProcessDo;

/**
 * @author Neelam Raj
 *
 */
@Repository
public class SubstitutionProcessDao extends BaseDao<SubstitutionProcessDo, SubstitutionProcessDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	UserTaskMappingDao userTaskDao;

	@Autowired
	TaskOwnersDao taskOwnerDao;

	@Override
	protected SubstitutionProcessDo importDto(SubstitutionProcessDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		SubstitutionProcessDo entity = new SubstitutionProcessDo();
		if (!ServicesUtil.isEmpty(fromDto.getRuleId()))
			entity.setRuleId(fromDto.getRuleId());
		if (!ServicesUtil.isEmpty(fromDto.getProcess()))
			entity.setProcess(fromDto.getProcess());

		return entity;
	}

	@Override
	protected SubstitutionProcessDto exportDto(SubstitutionProcessDo entity) {

		SubstitutionProcessDto dto = new SubstitutionProcessDto();
		if (!ServicesUtil.isEmpty(entity.getRuleId()))
			dto.setRuleId(entity.getRuleId());
		if (!ServicesUtil.isEmpty(entity.getProcess()))
			dto.setProcess(entity.getProcess());

		return dto;
	}

	public String deleteRecord(String ruleId) {

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction(); 
		String deleteQuery = "Delete from SUBSTITUTION_PROCESSES where rule_id='" + ruleId + "'";

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
					.println("[Workbox][SubstitutionRuleDao][getSubstitutedUserTask][Error]" + e.getLocalizedMessage());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String updateRecord(List<String> processList, String ruleId) {
		List<String> procList = new ArrayList<>(processList);
		String resultStr = "";
		try {
			// Old subs. to be deleted
			if (!ServicesUtil.isEmpty(procList)) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction(); 
				String deleteQuery = "Delete from SUBSTITUTION_PROCESSES where rule_id='" + ruleId + "'";
				System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionRuleDao]deleteQuery" + deleteQuery);

				int result = session.createSQLQuery(deleteQuery).executeUpdate();
				tx.commit();
				session.close();
				if (result > 0)
					resultStr += PMCConstant.DELETE;
			}
			// New subs. to be created
			if (!ServicesUtil.isEmpty(procList)) {
				for (String str : procList) {
					try {
						SubstitutionProcessDto subsDto = new SubstitutionProcessDto(ruleId, str);
						saveSubstitute(subsDto);
					} catch (Exception e) {
						System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionRuleDao][updateRecord][Error]" + e + " [ruleId]"
								+ ruleId + "[str]" + str);
					}
				}
				resultStr += PMCConstant.CREATE;
			}
			return resultStr;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox]Error :" + e.getLocalizedMessage());
		}
		return PMCConstant.FAILURE;
	}

	public void saveSubstitute(SubstitutionProcessDto substitutionProcessDto) {
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction(); 
		try {
			session.saveOrUpdate(importDto(substitutionProcessDto));
			session.flush();
			session.clear();
		} catch (Exception e) {
			System.err.println("[WBP-Dev] Error inserting Values"+e);
			e.printStackTrace();
		}
		
		tx.commit();
		session.close();
	}
}
