package oneapp.incture.workbox.demo.substitution.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.entity.CrossConstantDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ProcessAttributesDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ValueListDto;
import oneapp.incture.workbox.demo.adhocTask.services.TaskCreationService;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionApprovalRequestDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionProcessApproverDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRulesDto;
import oneapp.incture.workbox.demo.substitution.entity.SubstitutionProcessApproverDo;

@Repository
//@Transactional
public class SubstitutionProcessApproverDao
		extends BaseDao<SubstitutionProcessApproverDo, SubstitutionProcessApproverDto> {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	TaskCreationService taskCreationService;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		}catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	@Autowired
	ApprovalRequestDao approvalRequestDao;

	protected SubstitutionProcessApproverDo importDto(SubstitutionProcessApproverDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		SubstitutionProcessApproverDo entity = new SubstitutionProcessApproverDo();
		if (!ServicesUtil.isEmpty(fromDto.getProcess()))
			entity.setProcess(fromDto.getProcess());
		if (!ServicesUtil.isEmpty(fromDto.getProcessDisplayName()))
			entity.setProcessDisplayName(fromDto.getProcessDisplayName());
		if (!ServicesUtil.isEmpty(fromDto.getApprovingUser()))
			entity.setApprovingUser(fromDto.getApprovingUser());
		if (!ServicesUtil.isEmpty(fromDto.getApprovingUserName()))
			entity.setApprovingUserName(fromDto.getApprovingUserName());
		if (!ServicesUtil.isEmpty(fromDto.isActivated()))
			entity.setActivated(fromDto.isActivated());
		if (!ServicesUtil.isEmpty(fromDto.isApprovelRequired()))
			entity.setApprovelRequired(fromDto.isApprovelRequired());

		return entity;

	}

	protected SubstitutionProcessApproverDto exportDto(SubstitutionProcessApproverDo entity) {

		SubstitutionProcessApproverDto dto = new SubstitutionProcessApproverDto();
		if (!ServicesUtil.isEmpty(entity.getProcess()))
			dto.setProcess(entity.getProcess());
		if (!ServicesUtil.isEmpty(entity.getProcessDisplayName()))
			dto.setProcessDisplayName(entity.getProcessDisplayName());
		if (!ServicesUtil.isEmpty(entity.getApprovingUser()))
			dto.setApprovingUser(entity.getApprovingUser());
		if (!ServicesUtil.isEmpty(entity.getApprovingUserName()))
			dto.setApprovingUserName(entity.getApprovingUserName());
		if (!ServicesUtil.isEmpty(entity.isActivated()))
			dto.setActivated(entity.isActivated());
		if (!ServicesUtil.isEmpty(entity.isApprovelRequired()))
			dto.setApprovelRequired(entity.isApprovelRequired());

		return dto;
	}

	public void saveOrUpdateRuleAudit(SubstitutionProcessApproverDto entity) throws Exception {
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(importDto(entity));
			tx.commit();
			session.close();

		} catch (Exception e) {
			System.err.println("[WBP-Dev][ApprovalRequestDao] ERROR ENTERING APPROVAL REQUEST" + e.getMessage());
		}

	}

	public Map<String, Integer> checkForApprovalRequired(List<String> processList, String ruleId, int version,
			SubstitutionRulesDto dto,Token token) {
		Session session = sessionFactory.openSession();
		Map<String, Integer> prcocessApprovalStatus = new HashMap<String, Integer>();
		int approvalRequiredProcess = 1;
		List<String> approvedProcesses = new ArrayList<>();
//		String processString = "";
//
//		for (String process : processList) {
//			processString += process + "','";
//		}
		
		String processString = String.join("','", processList);
		
		String qString = "select sp.* from substitution_process_approver sp where sp.is_activated = 1 and  sp.is_deleted = 0 and sp.process in('"
				+ processString + "')";

		List<SubstitutionProcessApproverDo> substitutionProcessApproverDos = session.createSQLQuery(qString).addEntity(SubstitutionProcessApproverDo.class).list();

		AttributesResponseDto processAttributes = taskCreationService
				.getProcessAttributes("SubstitutionProcessApproval");
		System.err.println("[Workbox][SubstitutionRuleDao][setApprovalTask] processAttributes : "
				+ new Gson().toJson(processAttributes));
		ProcessAttributesDto processAttributesDto = processAttributes.getListOfProcesssAttributes().get(0);
		List<ProcessAttributesDto> processAttributesDtos = new ArrayList<>();

		String query = "select do from CrossConstantDo do where do.constantId = 'cat.substitution'";
		List<CrossConstantDo> constantDos = session.createQuery(query).list();

		// setting the task for approval required processes
		for (SubstitutionProcessApproverDo entity : substitutionProcessApproverDos) {
			approvedProcesses.add(entity.getProcess());
			prcocessApprovalStatus.put(entity.getProcess(), 0);
			approvalRequestDao.saveOrUpdateApprovalRequest(new SubstitutionApprovalRequestDto(ruleId, version,
					entity.getProcess(), null, entity.getApprovingUser(), entity.getApprovingUserName()));
			//prcocessApprovalStatus.put(entity.getProcess(), 0);
			String processAttributeJson = new Gson().toJson(processAttributesDto);
			processAttributesDtos.add(setApprovalTask(processAttributeJson, entity.getProcess() ,entity.getProcessDisplayName(), dto,
					entity.getApprovingUser(), constantDos));
			approvalRequiredProcess++;
		}

		// adding the remaining process the map
		for (String process : processList) {
			if (!approvedProcesses.contains(process)) {
				prcocessApprovalStatus.put(process, 1);
			}
		}

		if (approvalRequiredProcess > 1) {
			try {
				processAttributes.setListOfProcesssAttributes(processAttributesDtos);
				processAttributes.setActionType("Submit");
				processAttributes.setIsEdited("2");
				System.err.println("[Workbox][SubstitutionRuleDao][setApprovalTask] processAttributes : "
						+ new Gson().toJson(processAttributes));
				taskCreationService.createTasks(processAttributes,token);
			} catch (Exception e) {
				System.err.println(
						"[WBP-Dev][SubstitutionProcessApproverDao][checkForApprovalRequired][task creation] error : "
								+ e.getMessage());
			}
		}

		return prcocessApprovalStatus;

	}

	// public Map<String, Integer> checkForApprovalRequired(List<String>
	// processList, String ruleId, int version,
	// SubstitutionRulesDto dto) {
	// String processString = "";
	// processList = sortProcessList(processList);
	// for (String process : processList) {
	// processString += process + "','";
	// }
	// String qString = "select sp.* from substitution_process_approver sp right
	// outer join (select process_name as process from process_config_tb where
	// process_name in('"
	// + processString.substring(0, processString.length() - 2)
	// + ")) pc on sp.process = pc.process where sp.is_deleted IS NULL or
	// sp.is_deleted = 0 order by lower(pc.process)";
	// List<SubstitutionProcessApproverDo> substitutionProcessApproverDos =
	// this.getSession().createSQLQuery(qString)
	// .addEntity(SubstitutionProcessApproverDo.class).list();
	// Map<String, Integer> prcocessApprovalStatus = new HashMap<String,
	// Integer>();
	// int index = 0;
	// int approvalRequiredProcess = 1;
	//
	// AttributesResponseDto processAttributes = taskCreationService
	// .getProcessAttributes("SubstitutionProcessApproval");
	// System.err.println("[Workbox][SubstitutionRuleDao][setApprovalTask]
	// processAttributes : "
	// + new Gson().toJson(processAttributes));
	// ProcessAttributesDto processAttributesDto =
	// processAttributes.getListOfProcesssAttributes().get(0);
	// List<ProcessAttributesDto> processAttributesDtos = new ArrayList<>();
	//
	// String query = "select do from CrossConstantDo do where do.constantId =
	// 'cat.substitution'";
	// List<CrossConstantDo> constantDos =
	// this.getSession().createQuery(query).list();
	//
	// for (SubstitutionProcessApproverDo entity :
	// substitutionProcessApproverDos) {
	//
	// prcocessApprovalStatus.put(processList.get(index), 1);
	// if (!ServicesUtil.isEmpty(entity)) {
	// System.err.println(new Gson().toJson(entity));
	// if (entity.isActivated() && entity.isApprovelRequired()) {
	// approvalRequestDao.saveOrUpdateApprovalRequest(new
	// SubstitutionApprovalRequestDto(ruleId, version,
	// entity.getProcess(), null, entity.getApprovingUser(),
	// entity.getApprovingUserName()));
	// prcocessApprovalStatus.put(entity.getProcess(), 0);
	// String processAttributeJson = new Gson().toJson(processAttributesDto);
	// processAttributesDtos.add(setApprovalTask(processAttributeJson,
	// entity.getProcess(), dto,
	// entity.getApprovingUser(), constantDos));
	// approvalRequiredProcess++;
	//
	// }
	// }
	// index++;
	//
	// }
	//
	// if (approvalRequiredProcess > 1) {
	// try {
	// processAttributes.setListOfProcesssAttributes(processAttributesDtos);
	// processAttributes.setActionType("Submit");
	// processAttributes.setIsEdited("2");
	// System.err.println("[Workbox][SubstitutionRuleDao][setApprovalTask]
	// processAttributes : "
	// + new Gson().toJson(processAttributes));
	// taskCreationService.createTasks(processAttributes);
	// } catch (Exception e) {
	// System.err.println(
	// "[WBP-Dev][SubstitutionProcessApproverDao][checkForApprovalRequired][task
	// creation] error : "
	// + e.getMessage());
	// }
	// }
	// return prcocessApprovalStatus;
	// }

	private List<String> sortProcessList(List<String> processList) {
		Collections.sort(processList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		return processList;
	}

	public ProcessAttributesDto setApprovalTask(String processAttributeJson, String processName, String processDisplayName,
			SubstitutionRulesDto dto, String approvingUser, List<CrossConstantDo> constantDos) {

		ProcessAttributesDto processAttributesDto = null;

		try {

			HashMap<String, String> hashMap = new HashMap<String, String>();
			JSONObject jsonDto = new JSONObject(dto);
			List<ValueListDto> valueListDtos = new ArrayList<ValueListDto>();

			for (CrossConstantDo crossConstantDo : constantDos) {
				if (crossConstantDo.getConstantValue().equals("processName")) {
					hashMap.put(crossConstantDo.getConstantName(), processName);
				}
				else if(crossConstantDo.getConstantValue().equals("processDisplayName")) {
					hashMap.put(crossConstantDo.getConstantName(), processDisplayName);
				}
				else if (crossConstantDo.getConstantValue().equals("approver")) {
					ValueListDto valueListDto = new ValueListDto(approvingUser, "individual");
					valueListDtos.add(valueListDto);
				} else {
					hashMap.put(crossConstantDo.getConstantName(),
							String.valueOf(jsonDto.get(crossConstantDo.getConstantValue())));
				}
			}
			hashMap.put("description", "Substitution process approval for " + processName);

			System.err.println(processAttributeJson);
			processAttributesDto = new Gson().fromJson(processAttributeJson, ProcessAttributesDto.class);

			for (CustomAttributeTemplateDto customAttributeTemplateDto : processAttributesDto
					.getCustomAttributeTemplateDto()) {

				String value = hashMap.get(customAttributeTemplateDto.getKey());

				if (customAttributeTemplateDto.getLabel().equalsIgnoreCase("Approver")) {
					customAttributeTemplateDto.setValueList(valueListDtos);
				} else if (customAttributeTemplateDto.getLabel().equalsIgnoreCase("Start Date")
						|| customAttributeTemplateDto.getLabel().equalsIgnoreCase("End Date")) {
					value = hashMap.get(customAttributeTemplateDto.getKey()).split("T")[0];

				}

				customAttributeTemplateDto.setValue(value);

			}
		} catch (Exception e) {
			System.err.println("[Workbox][SubstitutionRuleDao][setApprovalTask][Error]" + e.getMessage());
		}
		return processAttributesDto;

	}

	public List<SubstitutionProcessApproverDto> getAllSubtitutionProcessApprovers() {
		List<SubstitutionProcessApproverDo> resultList = null;
		List<SubstitutionProcessApproverDto> dto = null;
		try {
			Session session = sessionFactory.openSession();
			String queryString = " select do from SubstitutionProcessApproverDo do where do.isDeleted = 0 order by do.processDisplayName";
			Query query = session.createQuery(queryString);
			resultList = query.list();
			dto = exportDtoList(resultList);
			return dto;
		} catch (Exception e) {
			System.err
					.println("[Workbox][SubstitutionRuleDao][getSubstitutedUserTask][Error]" + e.getLocalizedMessage());
		}
		return dto;
	}

	public void updateQuery(String process) {
		try {
			String query = "update substitution_process_approver set is_deleted=1 where process = '" + process + "'";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.createSQLQuery(query).executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ApprovalRequestDao] ERROR (UpdateQuery)" + e.getMessage());
		}
	}

	public void saveOrUpdateSubstitutionProcessApprover(SubstitutionProcessApproverDto entity) throws Exception {
		try {
			updateQuery(entity.getProcess());
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(importDto(entity));
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ApprovalRequestDao] ERROR ENTERING APPROVAL REQUEST" + e.getMessage());
		}

	}

	public void deleteProcessApprover(String process) {
		try {

			String query = "update substitution_process_approver set is_deleted = 1 where process = '" + process + "'";
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.createSQLQuery(query).executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ApprovalRequestDao] ERROR updating process approver" + e.getMessage());
		}
	}

	public void updateSubtitutionProcessApprover(SubstitutionProcessApproverDto dto) {
		try {
			updateQuery(dto.getProcess());
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(importDto(dto));
			tx.commit();
			session.close();

		} catch (Exception e) {
			System.err.println("[WBP-Dev][ApprovalRequestDao] ERROR updating APPROVAL REQUEST" + e.getMessage());
		}
	}

}
