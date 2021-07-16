package oneapp.incture.workbox.demo.substitution.dao;

import java.util.ArrayList;
import java.util.Date;
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

import com.sap.security.um.user.User;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionApprovalRequestDto;
import oneapp.incture.workbox.demo.substitution.entity.SubstitutionApprovalRequestDo;
import oneapp.incture.workbox.demo.substitution.util.ServicesUtilSubs;

@Repository
//@Transactional
public class ApprovalRequestDao extends BaseDao<SubstitutionApprovalRequestDo, SubstitutionApprovalRequestDto> {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		}
		catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	protected SubstitutionApprovalRequestDo importDto(SubstitutionApprovalRequestDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		SubstitutionApprovalRequestDo entity = new SubstitutionApprovalRequestDo();
		if (!ServicesUtil.isEmpty(fromDto.getRuleId()))
			entity.setRuleId(fromDto.getRuleId());
		if (!ServicesUtil.isEmpty(fromDto.getVersion()))
			entity.setVersion(fromDto.getVersion());
		if (!ServicesUtil.isEmpty(fromDto.getProcess()))
			entity.setProcess(fromDto.getProcess());
		if (!ServicesUtil.isEmpty(fromDto.getApprovalTime()))
			entity.setApprovalTime(ServicesUtil.convertFromStringToDateSubstitution(fromDto.getApprovalTime()));
		if (!ServicesUtil.isEmpty(fromDto.getApproverId()))
			entity.setApproverId(fromDto.getApproverId());
		if (!ServicesUtil.isEmpty(fromDto.getApproverDisplayName()));
			entity.setApproverDisplayName(fromDto.getApproverDisplayName());

		return entity;

	}

	protected SubstitutionApprovalRequestDto exportDto(SubstitutionApprovalRequestDo entity) {

		SubstitutionApprovalRequestDto dto = new SubstitutionApprovalRequestDto();
		if (!ServicesUtil.isEmpty(entity.getRuleId()))
			dto.setRuleId(entity.getRuleId());
		if (!ServicesUtil.isEmpty(entity.getProcess()))
			dto.setProcess(entity.getProcess());
		if (!ServicesUtil.isEmpty(entity.getVersion()))
			dto.setVersion(entity.getVersion());
		if (!ServicesUtil.isEmpty(entity.getApprovalTime()))
			dto.setApprovalTime(ServicesUtil.convertFromDateToString(entity.getApprovalTime()));
		if (!ServicesUtil.isEmpty(entity.getApproverId()))
			dto.setApproverId(entity.getApproverId());
		if (!ServicesUtil.isEmpty(entity.getApproverDisplayName()));
			dto.setApproverDisplayName(entity.getApproverDisplayName());

		return dto;
	}

	public void saveOrUpdateApprovalRequest(SubstitutionApprovalRequestDto entity) {
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(importDto(entity));
			session.flush();
			session.clear();
			tx.commit();
			session.close();
			//create(entity);

		} catch (Exception e) {
			System.err.println("[WBP-Dev][ApprovalRequestDao] ERROR ENTERING APPROVAL REQUEST" + e.getMessage());
		}

	}

	public void updateApprovalRequest(SubstitutionApprovalRequestDto dto) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			dto.setApprovalTime(ServicesUtilSubs.getUTCTime());
			session.saveOrUpdate(importDto(dto));
			
			Map<Integer, String> statusMapping = new HashMap<>();
			statusMapping.put(0, "PENDING");
			statusMapping.put(1, "APPROVED");
			statusMapping.put(2, "REJECTED");
			statusMapping.put(3, "CANCELED");
			
			String query = "update substitution_processes set status = " + dto.getActionType() + " , status_def = '" + statusMapping.get(Integer.parseInt(dto.getActionType())) + "' where rule_id = '"
					+ dto.getRuleId() + "' and version = " + dto.getVersion() + " and process = '" + dto.getProcess()
					+ "'";
			
			session.createSQLQuery(query).executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ApprovalRequestDao] error updating approval request " + e.getMessage());
		}

	}

	@SuppressWarnings({ "null", "unchecked" })
	public List<SubstitutionApprovalRequestDto> getApprovalRequests() {
		Session session = sessionFactory.openSession();
		List<Object[]> resultList = null;
		SubstitutionApprovalRequestDto dto = null;
		List<SubstitutionApprovalRequestDto> dtoList = new ArrayList<SubstitutionApprovalRequestDto>();
		User user = UserManagementUtil.getLoggedInUser();
		String userId = user.getName().toUpperCase();
		try {
			String queryString = "select ar.rule_id,ar.version,ar.process,ar.approval_Time,ar.approver_id,ar.approver_display_name from "
					+ "substitution_approval_request ar join substitution_processes sp on ar.rule_id=sp.rule_id and"
					+ " ar.version=sp.version where sp.status=0 and ar.approver_id='" + userId + "'";
			Query query = session.createSQLQuery(queryString);
			resultList = query.list();
			for (Object[] obj : resultList) {
				dto = new SubstitutionApprovalRequestDto();
				dto.setRuleId((String) obj[0]);
				dto.setVersion((int) obj[1]);
				dto.setProcess((String) obj[2]);
				dto.setApprovalTime(ServicesUtil.convertFromDateToString((Date) obj[3]));
				dto.setApproverId((String) obj[4]);
				dto.setApproverDisplayName((String) obj[5]);
				dtoList.add(dto);
			}
			System.err.println(dtoList);
			return dtoList;
		} catch (Exception e) {
			System.err.println("[Workbox][SubstitutionRuleDao][getApprovalRequests][Error]" + e.getLocalizedMessage());
		}
		return null;
	}

}
