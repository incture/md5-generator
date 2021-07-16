package oneapp.incture.workbox.demo.substitution.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.naming.spi.DirStateFactory.Result;
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
import oneapp.incture.workbox.demo.adapter_base.dto.SubstitutionTaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.substitution.dto.ProcessStatusDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionProcessDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRuleAuditDto;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRulesDto;
import oneapp.incture.workbox.demo.substitution.dto.UserTaskMappingDto;
import oneapp.incture.workbox.demo.substitution.entity.SubstitutionRuleDo;
import oneapp.incture.workbox.demo.substitution.util.PMCConstantSubs;
import oneapp.incture.workbox.demo.substitution.util.ServicesUtilSubs;

@Repository
//@Transactional
public class SubstitutionRuleVersionDao extends BaseDao<SubstitutionRuleDo, SubstitutionRulesDto> {

	@Autowired
	UserTaskMappingDao userTaskDao;

	@Autowired
	TaskOwnersDao taskOwnerDao;

	@Autowired
	SubstitutionProcessVersionDao subsProcessDao;

	@Autowired
	SubstitutionRuleAuditDao substitutionRuleAuditDao;

	@Autowired
	SubstitutionProcessApproverDao substitutionProcessApproverDao;

	@Autowired
	SubstitutionProcessVersionDao substitutionProcessVersionDao;

	@Autowired
	private SessionFactory sessionFactory;


	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		}catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	@Override
	protected SubstitutionRuleDo importDto(SubstitutionRulesDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		SubstitutionRuleDo entity = new SubstitutionRuleDo();
		if (!ServicesUtil.isEmpty(fromDto.getRuleId()))
			entity.setRuleId(fromDto.getRuleId());
		if (!ServicesUtil.isEmpty(fromDto.getVersion()))
			entity.setVersion(fromDto.getVersion());
		if (!ServicesUtil.isEmpty(fromDto.getSubstitutedUser()))
			entity.setSubstitutedUser(fromDto.getSubstitutedUser());
		if (!ServicesUtil.isEmpty(fromDto.getSubstitutedUserName()))
			entity.setSubstitutedUserName(fromDto.getSubstitutedUserName());
		if (!ServicesUtil.isEmpty(fromDto.getSubstitutingUser()))
			entity.setSubstitutingUser(fromDto.getSubstitutingUser());
		if (!ServicesUtil.isEmpty(fromDto.getSubstitutingUserName()))
			entity.setSubstitutingUserName(fromDto.getSubstitutingUserName());
		if (!ServicesUtil.isEmpty(fromDto.getEndDate()))
			entity.setEndDate(ServicesUtil.convertFromStringToDateSubstitution(fromDto.getEndDate()));
		if (!ServicesUtil.isEmpty(fromDto.getStartDate()))
			entity.setStartDate(ServicesUtil.convertFromStringToDateSubstitution(fromDto.getStartDate()));
		if (!ServicesUtil.isEmpty(fromDto.getUpdateAt()))
			entity.setUpdatedAt(ServicesUtil.convertFromStringToDateSubstitution(fromDto.getUpdateAt()));
		if (!ServicesUtil.isEmpty(fromDto.isActive()))
			entity.setActive(fromDto.isActive());
		if (!ServicesUtil.isEmpty(fromDto.isEnabled()))
			entity.setEnabled(fromDto.isEnabled());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedBy()))
			entity.setCreatedBy(fromDto.getCreatedBy());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedByDisp()))
			entity.setCreatedByDisp(fromDto.getCreatedByDisp());
		if (!ServicesUtil.isEmpty(fromDto.getCreatedAt()))
			entity.setCreatedAt(ServicesUtil.convertFromStringToDateSubstitution(fromDto.getCreatedAt()));
		if (!ServicesUtil.isEmpty(fromDto.getModifiedAt()))
			entity.setModifiedAt(ServicesUtil.convertFromStringToDateSubstitution(fromDto.getModifiedAt()));

		return entity;
	}

	@Override
	protected SubstitutionRulesDto exportDto(SubstitutionRuleDo entity) {

		SubstitutionRulesDto dto = new SubstitutionRulesDto();
		if (!ServicesUtil.isEmpty(entity.getRuleId()))
			dto.setRuleId(entity.getRuleId());
		if (!ServicesUtil.isEmpty(entity.getVersion()))
			dto.setVersion(entity.getVersion());
		if (!ServicesUtil.isEmpty(entity.getSubstitutedUser()))
			dto.setSubstitutedUser(entity.getSubstitutedUser());
		if (!ServicesUtil.isEmpty(entity.getSubstitutedUserName()))
			dto.setSubstitutedUserName(entity.getSubstitutedUserName());
		if (!ServicesUtil.isEmpty(entity.getSubstitutingUser()))
			dto.setSubstitutingUser(entity.getSubstitutingUser());
		if (!ServicesUtil.isEmpty(entity.getSubstitutingUserName()))
			dto.setSubstitutingUserName(entity.getSubstitutingUserName());
		if (!ServicesUtil.isEmpty(entity.getEndDate()))
			dto.setEndDate(ServicesUtil.convertFromDateToString(entity.getEndDate()));
		if (!ServicesUtil.isEmpty(entity.getStartDate()))
			dto.setStartDate(ServicesUtil.convertFromDateToString(entity.getStartDate()));
		if (!ServicesUtil.isEmpty(entity.getUpdatedAt()))
			dto.setUpdateAt(ServicesUtil.convertFromDateToString(entity.getUpdatedAt()));
		if (!ServicesUtil.isEmpty(entity.isActive()))
			dto.setActive(entity.isActive());
		if (!ServicesUtil.isEmpty(entity.isEnabled()))
			dto.setEnabled(entity.isEnabled());
		if (!ServicesUtil.isEmpty(entity.getCreatedBy()))
			dto.setCreatedBy(entity.getCreatedBy());
		if (!ServicesUtil.isEmpty(entity.getCreatedByDisp()))
			dto.setCreatedByDisp(entity.getCreatedByDisp());
		if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
			dto.setCreatedAt(ServicesUtil.convertFromDateToString(entity.getCreatedAt()));
		if (!ServicesUtil.isEmpty(entity.getModifiedAt()))
			dto.setModifiedAt(ServicesUtil.convertFromDateToString(entity.getModifiedAt()));
		/* For UI dummy disabling */
		dto.setDisableButton(false);

		return dto;
	}

	public String substitutionProcess(String substitutingUser, String startPoint,Token token) {
		String response = PMCConstant.FAILURE;
		try {
			List<SubstitutionRulesDto> substitutionList = getSubstitutedOfSubstituting(substitutingUser);
			// List<String> processList = null;
			if (!ServicesUtil.isEmpty(substitutionList)) {
				for (SubstitutionRulesDto dto : substitutionList) {
					updateProcess(dto, PMCConstant.SUBSTITUTION_INSTANT_TYPE, startPoint,token);
				}
			}
			response = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println(
					"[Workbox][Workbox][SubstitutionRuleDao][substitutionProcess][error]" + e.getLocalizedMessage());
		}
		return response;
	}

	@SuppressWarnings({ "unchecked" })
	public List<String> getNewTaskList(String substitutedUsers, String substitutingUser) {
		String processFilter = "";
		try {
			// Commented process filter as Bentley does not want (if u want to
			// enable process filter uncomment from START TO END)
			// START

			// String processNameQuery = " select distinct sp.process from
			// SUBSTITUTION_PROCESSES sp join substitution_rule sr on
			// sr.rule_id=sp.rule_id "
			// + "where sr.substituted_user='" + substitutedUsers + "' and
			// sr.substituting_user='"
			// + substitutingUser + "' and sr.is_active=1 and sr.is_enable=1 ";
			// Query processQuery =
			// this.getSession().createSQLQuery(processNameQuery);
			// List<String> process = processQuery.list();
			// if (!ServicesUtil.isEmpty(process))
			// processFilter = " AND te.proc_name in (" +
			// ServicesUtil.getStringFromList(process) + ") ";

			// END
			Session session = sessionFactory.openSession();
			String queryString = ""
					+ "SELECT DISTINCT TE.EVENT_ID FROM TASK_EVENTS TE JOIN TASK_OWNERS TO ON TE.EVENT_ID=TO.EVENT_ID "
					+ "WHERE UPPER(TO.TASK_OWNER) =UPPER('" + substitutedUsers + "') AND TE.SUBJECT='"
					+ PMCConstantSubs.MANGER_TASK + "' AND "
					+ "TE.STATUS IN ('READY','RESERVED') AND (IS_SUBSTITUTED=0 OR IS_SUBSTITUTED IS NULL) "
					+ processFilter + "AND TE.EVENT_ID NOT IN "
					+ "(SELECT DISTINCT TO1.EVENT_ID FROM TASK_OWNERS TO1 WHERE UPPER(TO1.TASK_OWNER) =UPPER('"
					+ substitutingUser + "'))";
			System.err.println(
					"[WBP-Dev][Workbox][Workbox][SubstitutionRuleDao][getNewTaskList][queryString]" + queryString);

			Query query = session.createSQLQuery(queryString);
			return query.list();
		} catch (Exception e) {
			System.err.println(
					"[Workbox][Workbox][SubstitutionRuleDao][getNewTaskList][Error]" + e.getLocalizedMessage());
		}
		return null;
	}

	@SuppressWarnings({ "unchecked" })
	public List<SubstitutionRulesDto> getSubstitutedOfSubstituting(String substitutingUser) {
		
		List<SubstitutionRulesDto> ruleDtos = null;
		SubstitutionRulesDto dto = null;
		try {
			Session session = sessionFactory.openSession();
			String queryString = " select do from SubstitutionRuleDo do where substitutingUser='" + substitutingUser
					+ "'";

			Query query = session.createQuery(queryString);
			List<SubstitutionRuleDo> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				ruleDtos = new ArrayList<SubstitutionRulesDto>();
				for (SubstitutionRuleDo entity : resultList) {
					dto = new SubstitutionRulesDto();
					dto = exportDto(entity);

					String processQuery = "select distinct process from SUBSTITUTION_PROCESSES where rule_id='"
							+ dto.getRuleId() + "' ";

					List<String> processResultList = session.createSQLQuery(processQuery).list();
					if (!ServicesUtil.isEmpty(processResultList)) {
						dto.setProcessList(processResultList);
					}
					ruleDtos.add(dto);
				}
			}
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][getSubstitution][error]"
							+ e.getLocalizedMessage());
		}
		return ruleDtos;
	}

	public static Boolean getIsEnabled(String startTime, String endTime, String type) {

		Boolean result = false;
		if (PMCConstant.SUBSTITUTION_SCHEDULER_TYPE.equals(type)) {
			result = (ServicesUtil.convertFromStringToDateSubstitution(ServicesUtil.getIstTimeTwoMinuteBefore())
					.before(ServicesUtil.convertFromStringToDateSubstitution(startTime)))
					&& (ServicesUtil.convertFromStringToDateSubstitution(ServicesUtil.getIstTimeTwoMinuteAfter())
							.after(ServicesUtil.convertFromStringToDateSubstitution(startTime)));
		} else if (PMCConstant.SUBSTITUTION_INSTANT_TYPE.equals(type)) {
			result = (ServicesUtil.convertFromStringToDateSubstitution(ServicesUtilSubs.getUTCTime())
					.after(ServicesUtil.convertFromStringToDateSubstitution(startTime))
					&& ServicesUtil.convertFromStringToDateSubstitution(ServicesUtilSubs.getUTCTime())
							.before(ServicesUtil.convertFromStringToDateSubstitution(endTime)));
		} else
			result = false;
		return result;
	}

	public Boolean getIsDisabled(String endTime) {

		if (ServicesUtil.convertFromStringToDateSubstitution(ServicesUtil.getIstTimeTwoMinuteBefore())
				.before(ServicesUtil.convertFromStringToDateSubstitution(endTime))
				&& ServicesUtil.convertFromStringToDateSubstitution(endTime).before(
						ServicesUtil.convertFromStringToDateSubstitution(ServicesUtil.getIstTimeTwoMinuteAfter()))) {
			return true;
		} else
			return false;
	}

	public String createRule(SubstitutionRulesDto dto,Token token) {
		String response = PMCConstant.FAILURE;
		try {
			if (getIsEnabled(dto.getStartDate(), dto.getEndDate(), PMCConstant.SUBSTITUTION_INSTANT_TYPE)
					&& dto.isActive()) {
				dto.setEnabled(true);
			}
			dto.setCreatedAt(ServicesUtilSubs.getUTCTime());
			dto.setUpdateAt(ServicesUtilSubs.getUTCTime());
			dto.setModifiedAt(ServicesUtilSubs.getUTCTime());
			response = createSubstitution(dto,token);
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][Workbox][SubstitutionRuleDao][createRule][error]" + e.getLocalizedMessage());
		}
		return response;

	}

	public String createProcess(String substitutedUser, String substitutedUserDisp, String substitutingUser,
			String substitutingUserDisp, String type, List<String> processList) {
		String response = PMCConstant.FAILURE;
		try {
			List<TaskOwnersDto> ownersDtos = new ArrayList<TaskOwnersDto>();
			TaskOwnersDto ownersDto = null;
			List<UserTaskMappingDto> mappingDtos = new ArrayList<UserTaskMappingDto>();
			UserTaskMappingDto mappingDto = null;
			List<String> taskIds = null;

			// changes made

			// if (!PMCConstant.NEW_TASK.equals(type)) {
			// taskIds = taskOwnerDao.getSubstitutedUserTask(substitutedUser,
			// substitutingUser, processList);
			// } else {
			taskIds = getNewTaskList(substitutedUser, substitutingUser);
			// }
			if (!ServicesUtil.isEmpty(taskIds)) {
				for (String taskId : taskIds) {
					ownersDto = new TaskOwnersDto();
					ownersDto.setEventId(taskId);
					ownersDto.setIsProcessed(false);
					ownersDto.setIsSubstituted(true);
					ownersDto.setTaskOwner(substitutingUser);
					ownersDto.setTaskOwnerDisplayName(substitutingUserDisp);
					ownersDtos.add(ownersDto);

					mappingDto = new UserTaskMappingDto();
					mappingDto.setSubstitutedUser(substitutedUser);
					mappingDto.setSubstitutingUser(substitutingUser);
					mappingDto.setTaskId(taskId);
					mappingDtos.add(mappingDto);
				}
				System.err.println("[WBP-Dev][Workbox]Creating records Started mappingDtos : " + mappingDtos);
				System.err.println("[WBP-Dev][Workbox]Creating records Started ownersDtos : " + ownersDtos);
				// userTaskDao.saveOrUpdateTasks(mappingDtos);
				// taskOwnerDao.saveOrUpdateOwners(ownersDtos);
			}
			response = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err
					.println("[Workbox][Workbox][SubstitutionRuleDao][createProcess][Error]" + e.getLocalizedMessage());
		}
		return response;
	}

	public String createSubstitution(SubstitutionRulesDto dto,Token token) {
		String response = PMCConstant.FAILURE;
		try {
			String ruleId = UUID.randomUUID().toString().replaceAll("-", "");
			dto.setUpdateAt(ServicesUtilSubs.getUTCTime());
			dto.setRuleId(ruleId);
			dto.setVersion(1);
			Map<Integer, String> statusMapping = new HashMap<>();
			statusMapping.put(0, "PENDING");
			statusMapping.put(1, "APPROVED");
			statusMapping.put(2, "REJECTED");
			statusMapping.put(3, "CANCELED");
			if (!ServicesUtil.isEmpty(dto.getProcessList())) {

				Map<String, Integer> processApprovalStatus = substitutionProcessApproverDao
						.checkForApprovalRequired(dto.getProcessList(), ruleId, 1, dto,token);
				for (Object key : processApprovalStatus.keySet()) {
					String approvedAt = null;
					if((Integer) processApprovalStatus.get(key) == 1) {
						approvedAt = ServicesUtilSubs.getUTCTime();
					}
					subsProcessDao.create(new SubstitutionProcessDto(ruleId, (String) key, 1,
							(Integer) processApprovalStatus.get(key),
							statusMapping.get((Integer) processApprovalStatus.get(key)) , approvedAt));
				}
			}
			create(dto);
			List<SubstitutionRuleAuditDto> entity = setSubstitutionRuleAuditDto(ruleId, dto, "create");
			substitutionRuleAuditDao.saveOrUpdateRuleAudit(entity);
			response = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][createSubstitution][error]"
							+ e.getLocalizedMessage());
		}
		return response;
	}

	private List<SubstitutionRuleAuditDto> setSubstitutionRuleAuditDto(String ruleId, SubstitutionRulesDto dto,
			String actionType) {
		SubstitutionRuleAuditDto entity = new SubstitutionRuleAuditDto();
		List<String> column = substitutionRuleAuditDao.getRuleAuditColumn();
		List<SubstitutionRuleAuditDto> entityList = new ArrayList<SubstitutionRuleAuditDto>();
		if (actionType.equalsIgnoreCase("create")) {
			for (String columnName : column) {
				entity = new SubstitutionRuleAuditDto();
				entity.setRuleId(ruleId);
				entity.setUserId(dto.getSubstitutedUser());
				entity.setColumnName(columnName);
				entity.setColumnValueOld(null);
				entity.setActionType(actionType);
				entity.setUpdatedAt(new Date());

				if (columnName.equalsIgnoreCase("substitutingUser")) {
					entity.setColumnValueNew(dto.getSubstitutingUser());
				} else if (columnName.equalsIgnoreCase("startDate")) {
					entity.setColumnValueNew(dto.getStartDate());
				} else if (columnName.equalsIgnoreCase("endDate")) {
					entity.setColumnValueNew(dto.getEndDate());
				} else {
					List<String> value = dto.getProcessList();
					String valueSeperatedByComa = String.join(",", value);
					entity.setColumnValueNew(valueSeperatedByComa);
				}
				entityList.add(entity);
			}
		} else if (actionType.equalsIgnoreCase("update")) {
			Map<String, String> old = substitutionRuleAuditDao.getRuleAuditDetails(ruleId, dto.getVersion());
			for (String columnName : column) {
				entity = new SubstitutionRuleAuditDto();
				entity.setRuleId(ruleId);
				entity.setUserId(dto.getSubstitutedUser());
				entity.setActionType(actionType);
				entity.setUpdatedAt(new Date());
				entity.setColumnValueOld(old.get(columnName));
				List<String> convertedOldValue = Arrays.asList(old.get(columnName).split(","));
				if (columnName.equalsIgnoreCase("substitutingUser")
						&& !dto.getSubstitutingUser().equalsIgnoreCase(old.get(columnName))) {
					entity.setColumnName(columnName);
					entity.setColumnValueNew(dto.getSubstitutingUser());
				} else if (columnName.equalsIgnoreCase("startDate")
						&& !dto.getStartDate().equalsIgnoreCase(old.get(columnName))) {
					entity.setColumnName(columnName);
					entity.setColumnValueNew(dto.getStartDate());
				} else if (columnName.equalsIgnoreCase("endDate")
						&& !dto.getEndDate().equalsIgnoreCase(old.get(columnName))) {
					entity.setColumnName(columnName);
					entity.setColumnValueNew(dto.getEndDate());
				} else if (columnName.equalsIgnoreCase("processList")) {
					System.err.println(dto.getProcessList());
					System.err.println(convertedOldValue);
					if (!dto.getProcessList().containsAll(convertedOldValue)
							|| !convertedOldValue.containsAll(dto.getProcessList())) {
						List<String> value = dto.getProcessList();
						String valueSeperatedByComa = String.join(",", value);
						entity.setColumnValueNew(valueSeperatedByComa);
						entity.setColumnName(columnName);
					}
				}
				if (!ServicesUtil.isEmpty(entity.getColumnName()))
					entityList.add(entity);
			}
		}
		return entityList;
	}

	public String deleteRule(SubstitutionRulesDto dto) {
		String response = PMCConstant.FAILURE;
		try {
			// subsProcessDao.deleteRecord(dto.getRuleId());
			deleteSubstitution(dto);
			substitutionProcessVersionDao.cancelApprovalRequest(dto.getRuleId(), dto.getVersion());
			response = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][SubstitutionRuleFacade][SubstitutionRuleDao][deleteRule][error]"
					+ e.getLocalizedMessage());
		}
		return response;
	}

	public String deleteProcess(String substitutedUser, String substitutingUser) {
		String response = PMCConstant.FAILURE;
		try {

			List<String> taskIds = userTaskDao.getTaskOfSubstitutingUser(substitutedUser, substitutingUser);
			taskOwnerDao.deleteSubstitutionOwner(substitutingUser, taskIds);
			userTaskDao.deleteDisabledSubstitution(substitutedUser, substitutingUser);
			response = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][deleteSubstitution][error]"
							+ e.getLocalizedMessage());
		}
		return response;

	}

	public String deleteSubstitution(SubstitutionRulesDto dto) {
		String response = PMCConstant.FAILURE;
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			// delete(dto);
			String query = "update substitution_rule set is_deleted = 1 where rule_id = '" + dto.getRuleId() + "'";
			session.createSQLQuery(query).executeUpdate();
			response = PMCConstant.SUCCESS;
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][deleteSubstitution][error]"
							+ e.getLocalizedMessage());
		}
		return response;
	}

	public String updateRule(SubstitutionRulesDto dto, String type,Token token) {
		System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionRuleDao]updateRule type : " + type);
		String response = PMCConstant.FAILURE;
		try {
			List<SubstitutionRuleAuditDto> entity = setSubstitutionRuleAuditDto(dto.getRuleId(), dto, "update");
			substitutionRuleAuditDao.saveOrUpdateRuleAudit(entity);
			updateSubstitution(dto);
			updateProcess(dto, type, PMCConstantSubs.SUBSTITUTION_INSTANT_TYPE_SUBS_TAB,token);
			response = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionRuleDao][updateRule][error]" + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings("unused")
	public String updateProcess(SubstitutionRulesDto dto, String type, String startPoint,Token token) {
		String response = PMCConstant.FAILURE;
		List<String> processList = null;
		try {
			if (!ServicesUtil.isEmpty(dto.getProcessList())) {
				if (!ServicesUtil.isEmpty(dto.getProcessList())
						&& !PMCConstantSubs.SUBSTITUTION_INSTANT_TYPE_INBOX.equals(startPoint)) {
					subsProcessDao.updateRecord(dto.getProcessList(), dto.getRuleId(), dto.getVersion(), dto,token);
				}
			}
			if (response.equals(PMCConstant.FAILURE))
				response = PMCConstant.NO_RESULT;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionRuleDao][updateProcess][error]" + e.getMessage());
		}
		return response;

	}

	public String updateSubstitution(SubstitutionRulesDto dto) {
		String response = PMCConstant.FAILURE;
		try {
			if (dto.isActive()
					&& getIsEnabled(dto.getStartDate(), dto.getEndDate(), PMCConstant.SUBSTITUTION_INSTANT_TYPE))
				dto.setEnabled(true);
			else
				dto.setEnabled(false);

			// saveOrUpdate(importDto(dto));
			updateCurrentRule(dto.getRuleId(), dto.getVersion());
			dto.setVersion(dto.getVersion() + 1);
			dto.setUpdateAt(ServicesUtilSubs.getUTCTime());
			dto.setModifiedAt(ServicesUtilSubs.getUTCTime());
			dto.setCreatedAt(ServicesUtilSubs.getUTCTime());
			create(dto);
			response = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleDao][updateSubstitution][error]" + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public List<SubstitutionRulesDto> getSubstitution(String userId, String userType, String origin) {
		List<SubstitutionRulesDto> ruleDtos = new ArrayList<SubstitutionRulesDto>();
		// SubstitutionRulesDto dto = null;
		String userQuery = "";
		try {
			/*
			 * UPDATING the substitution Rule Is_enable using current_time
			 */
			Session session = sessionFactory.openSession();
			String updateEnable = updateSubstitutionRule();
			if (PMCConstant.SUBSTITUTED.equals(userType)) {
				userQuery += " where upper(do1.substituted_user)=upper('" + userId + "') ";
			} else {
				userQuery += " where upper(do1.substituting_user)=upper('" + userId + "') ";
			}

			String queryString = " select do1.* from Substitution_Rule do1 inner join"
					+ " (select max(version) as version,rule_Id from Substitution_Rule group by rule_Id) do2 "
					+ "on do2.rule_id=do1.rule_id and do2.version=do1.version" + userQuery + "";

			if (ServicesUtil.isEmpty(userQuery)) {
				queryString += " where  do1.end_date > current_timestamp and do1.is_deleted=0";
			} else {
				queryString += " and  do1.end_date > current_timestamp and do1.is_deleted=0";
			}
			if (!ServicesUtil.isEmpty(origin) && PMCConstant.INBOX_TYPE_SUBSTITUTION_INBOX.equals(origin)) {
				queryString += " and do1.is_active=1 and do1.is_enabled=1 ";
			}

			queryString += " order by do1.start_date, do1.end_date";
			System.err.println("[WBP-Dev][Workbox][SubstitutedDao][queryString]" + queryString);
			Query query = session.createSQLQuery(queryString).addEntity(SubstitutionRuleDo.class);
			List<SubstitutionRuleDo> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (SubstitutionRuleDo substitutionRuleDo : resultList) {

					SubstitutionRulesDto substitutionRulesDto = exportDto(substitutionRuleDo);
					if (ServicesUtil.isEmpty((String) substitutionRulesDto.getCreatedBy())) {
						substitutionRulesDto.setCreatedBy(substitutionRulesDto.getSubstitutedUser());
					}

					String processStatusQuery = "select sp.process,sp.status,pct.process_display_name from SUBSTITUTION_PROCESSES sp join process_config_tb pct "
							+ "on sp.process=pct.process_name where rule_id='"
							+ substitutionRulesDto.getRuleId()
							+ "' and version=(SELECT MAX(version) FROM SUBSTITUTION_RULE " + "WHERE rule_id='"
							+ substitutionRulesDto.getRuleId() + "')";

					Map<Integer, String> statusMapping = new HashMap<>();
					statusMapping.put(0, "PENDING");
					statusMapping.put(1, "APPROVED");
					statusMapping.put(2, "REJECTED");
					statusMapping.put(3, "CANCELED");
					List<ProcessStatusDto> processStatusDtos = new ArrayList<>();
					List<String> process = new ArrayList<String>();
					List<Object[]> processResultList = session.createSQLQuery(processStatusQuery).list();
					if (!ServicesUtil.isEmpty(processResultList)) {

						for (Object[] obj : processResultList) {
							ProcessStatusDto processStatus = new ProcessStatusDto();
							String status = statusMapping.get((Integer) obj[1]);
							processStatus.setProcessName((String) obj[0]);
							processStatus.setStatus(status);
							processStatus.setLabelName(ServicesUtil.isEmpty((String) obj[2])==true?(String) obj[0]:(String) obj[2]);
							processStatusDtos.add(processStatus);
							if (status.equalsIgnoreCase("APPROVED"))
								process.add((String) obj[0]);
						}

					}
					substitutionRulesDto.setProcessStatus(processStatusDtos);
					substitutionRulesDto.setProcessList(process);
					ruleDtos.add(substitutionRulesDto);
				}
			}
		}

		catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][getSubstitution][error]"
							+ e.getLocalizedMessage());
		}
		return ruleDtos;
	}

	public String getSubstitutedRules(String userId) {

		List<SubstitutionRulesDto> ruleDtos = new ArrayList<SubstitutionRulesDto>();
		String ruleFilterQuery = " AND (";
		try {
			Session session = sessionFactory.openSession();
			SubstitutionRulesDto dto = null;

			String query = "select s1.rule_id , s1.version ,  s1.substituted_user , s1.created_at , s1.modified_at , s1.end_date , s1.is_enable , s1.is_active from substitution_rule s1 inner join (select max(version) as version , rule_id from substitution_rule group by rule_id) as s2 on s1.rule_id = s2.rule_id where s1.is_enable ="
					+ "case when s1.version = s2.version then 1 else 0 end and s1.is_active ="
					+ "case when s1.version = s2.version then 1 else 0 end";
			List<Object[]> resulstList = session.createSQLQuery(query).list();
			if (!ServicesUtil.isEmpty(resulstList)) {
				for (Object[] object : resulstList) {

					String ruleId = (String) object[0];
					Integer version = (Integer) object[1];

					String processQuery = "select process from SUBSTITUTION_PROCESSES where rule_id='" + ruleId
							+ "' and version = " + version + " and status = 1";
					List<String> processResultList = session.createSQLQuery(processQuery).list();

					if (!ServicesUtil.isEmpty(processResultList)) {
						ruleFilterQuery += " (TW.TASK_OWNER='" + (String) object[2] + "' AND TE.PROC_NAME IN ("
								+ ServicesUtil.getStringFromList(processResultList) + ")";

						if ((Byte) object[6] == 1 && (Byte) object[7] == 1) {
							ruleFilterQuery += " AND TE.created_at between '" + (Date) object[3] + "' and '"
									+ (Date) object[5] + "'";
						} else {
							ruleFilterQuery += " AND TE.created_at between '" + (Date) object[3] + "' and '"
									+ (Date) object[4] + "'";
						}

						ruleFilterQuery += ") OR";
					}
				}

				ruleFilterQuery = ruleFilterQuery.substring(0, ruleFilterQuery.length() - 2);
				ruleFilterQuery += ")";
			} else {
				ruleFilterQuery = " AND (te.status='NoSubstitution') ";
			}

		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][getSubstitutedRules][error]"
							+ e.getMessage());
		}
		return ruleFilterQuery;
	}

	public String updateSubstitutionRule() {
		Session session = sessionFactory.openSession();
		Transaction tx  = session.beginTransaction();
		String updateEnableQuery = "update sr set is_enable= (case when (current_timestamp between "
				+ "sr.start_Date and sr.end_date) then 1 else 0 end ) , updated_at='" + ServicesUtilSubs.getUTCTime()
				+ "'" + " from substitution_rule sr where sr.is_Active=1 and"
				+ " sr.updated_at > ADD_SECONDS (TO_TIMESTAMP (updated_at), -60*60*24*90) ";
		int resultRows = session.createSQLQuery(updateEnableQuery).executeUpdate();
		System.err.println("[WBP-Dev][Workbox][updateSubstitutionRule][query]" + updateEnableQuery);
		tx.commit();
		session.close();
		if (resultRows > 0)
			return PMCConstant.SUCCESS;

		else
			return PMCConstant.FAILURE;
	}

	@SuppressWarnings({ "unchecked" })
	public List<SubstitutionRulesDto> getChangedRule() {
		List<SubstitutionRulesDto> ruleDtos = new ArrayList<SubstitutionRulesDto>();
		try {
			Session session = sessionFactory.openSession();
			String queryString = " select do from SubstitutionRuleDo do where (do.startDate = '"
					+ ServicesUtil.getIstTimeWithoutT() + "' or do.endDate='" + ServicesUtil.getIstTimeWithoutT()
					+ "') and do.isActive=1 ";
			// System.err.println("[WBP-Dev][Workbox][PMC][SubstitutuionRuleDao][getChangedRule]"
			// + queryString);
			Query query = session.createQuery(queryString);
			List<SubstitutionRuleDo> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (SubstitutionRuleDo entity : resultList) {
					ruleDtos.add(exportDto(entity));
				}
			}
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][getEnabledRule][error]"
							+ e.getLocalizedMessage());
		}
		return ruleDtos;
	}

//	@SuppressWarnings({ "unchecked" })
//	public List<SubstitutionRulesDto> getRule(String substitutedUser, String substitutingUser, String startDate,
//			String endDate, String createdBy) {
//		List<SubstitutionRulesDto> ruleDtos = new ArrayList<SubstitutionRulesDto>();
//		// SubstitutionRulesDto dto = null;
//		try {
//
//			String queryString = " select do1.* from Substitution_Rule do1 inner join"
//					+ " (select max(version) as version,rule_Id from Substitution_Rule group by rule_Id) do2 "
//					+ "on do2.rule_id=do1.rule_id and do2.version=do1.version where do1.is_deleted=0";
//			// String queryString = " select do from SubstitutionRuleDo do ";
//
//			if (!ServicesUtil.isEmpty(createdBy)
//					|| (!ServicesUtil.isEmpty(substitutedUser) || !ServicesUtil.isEmpty(substitutingUser)
//							|| !ServicesUtil.isEmpty(endDate) || !ServicesUtil.isEmpty(startDate))) {
//
//				queryString += " and ";
//				if (!ServicesUtil.isEmpty(createdBy)) {
//					queryString += " upper(do1.created_by)=upper('" + createdBy + "') and ";
//				}
//				if (!ServicesUtil.isEmpty(substitutedUser)) {
//					queryString += " upper(do1.substituted_user)=upper('" + substitutedUser + "') and ";
//				}
//				if (!ServicesUtil.isEmpty(substitutingUser)) {
//					queryString += " upper(do1.substituting_user)=upper('" + substitutingUser + "') and ";
//				}
//
//				if (!ServicesUtil.isEmpty(startDate) || !ServicesUtil.isEmpty(endDate)) {
//					queryString += " ( ";
//					if (!ServicesUtil.isEmpty(startDate)) {
//						queryString += " ('" + startDate + "' between do1.start_date and do1.end_date ) or ";
//					}
//
//					if (!ServicesUtil.isEmpty(endDate)) {
//						queryString += " ('" + endDate + "' between do1.start_date and do1.end_date) or ";
//					}
//					if (!ServicesUtil.isEmpty(endDate) && !ServicesUtil.isEmpty(startDate)) {
//						queryString += "  do1.start_date BETWEEN '" + startDate + "' and '" + endDate + "' or"
//								+ " do1.end_date BETWEEN '" + startDate + "' and '" + endDate + "' or ";
//					}
//					queryString = queryString.substring(0, queryString.length() - 3) + ")";
//
//				} else
//					queryString = queryString.substring(0, queryString.length() - 4);
//			}
//
//			queryString += " order by do1.start_date, do1.end_date desc ";
//			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionDao][getRules][queryString]" + queryString);
//			Query query = this.getSession().createSQLQuery(queryString).addEntity(SubstitutionRuleDo.class);
//			List<SubstitutionRuleDo> resultList = query.list();
//			if (!ServicesUtil.isEmpty(resultList)) {
//				for (SubstitutionRuleDo substitutionRuleDo : resultList) {
//
//					SubstitutionRulesDto substitutionRulesDto = exportDto(substitutionRuleDo);
//					if (ServicesUtil.isEmpty((String) substitutionRulesDto.getCreatedBy())) {
//						substitutionRulesDto.setCreatedBy(substitutionRulesDto.getSubstitutedUser());
//					}
//
//					String processQuery = "select sp.process,sp.status,pct.process_display_name from SUBSTITUTION_PROCESSES sp join process_config_tb pct "
//							+ "on sp.process=pct.process_name where rule_id='"
//							+ substitutionRulesDto.getRuleId() + "' and version='" + substitutionRulesDto.getVersion()
//							+ "'";
//
//					Map<Integer, String> statusMapping = new HashMap<>();
//					statusMapping.put(0, "PENDING");
//					statusMapping.put(1, "APPROVED");
//					statusMapping.put(2, "REJECTED");
//					statusMapping.put(3, "CANCELED");
//					List<ProcessStatusDto> processStatusDtos = new ArrayList<>();
//					List<String> process = new ArrayList<String>();
//					List<Object[]> processResultList = this.getSession().createSQLQuery(processQuery).list();
//					if (!ServicesUtil.isEmpty(processResultList)) {
//
//						for (Object[] obj : processResultList) {
//							ProcessStatusDto processStatus = new ProcessStatusDto();
//							String status = statusMapping.get((Integer) obj[1]);
//							processStatus.setProcessName((String) obj[0]);
//							processStatus.setStatus(status);
//							processStatus.setLabelName(ServicesUtil.isEmpty((String) obj[2])==true?(String) obj[0]:(String) obj[2]);
//							processStatusDtos.add(processStatus);
//							process.add((String) obj[0]);
//						}
//
//					}
//					substitutionRulesDto.setProcessStatus(processStatusDtos);
//					substitutionRulesDto.setProcessList(process);
//					ruleDtos.add(substitutionRulesDto);
//				}
//			}
//		} catch (Exception e) {
//			System.err.println(
//					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][getSubstitution][error]"
//							+ e.getLocalizedMessage());
//		}
//		return ruleDtos;
//	}
	
	@SuppressWarnings({ "unchecked" })
	public List<SubstitutionRulesDto> getRule(String substitutedUser, String substitutingUser, String startDate,
			String endDate, String createdBy) {
		List<SubstitutionRulesDto> ruleDtos = new ArrayList<SubstitutionRulesDto>();
		Map<String, SubstitutionRulesDto> ruleMap = new HashMap<>();
		// SubstitutionRulesDto dto = null;
		try {
			Session session = sessionFactory.openSession();
			String queryString = "select do1.rule_id , do1.version ,  do1.substituted_user , do1.substituted_user_name , do1.substituting_user ,"
					+ " do1.substituting_user_name ,do1.start_date , do1.end_date , do1.updated_at , do1.modified_at , do1.created_at ,"
					+ " do1.created_by , do1.created_by_disp , sp.process , pct.process_display_name , sp.status ,  do1.is_active , do1.is_enable"
					+ " from Substitution_Rule do1 inner join (select max(version) as version, rule_Id from Substitution_Rule group by rule_Id) do2"
					+ " on do2.rule_id = do1.rule_id and do2.version = do1.version "
					+ " join substitution_processes sp on sp.rule_id = do1.rule_id and sp.version = do2.version join process_config_tb pct on pct.process_name = sp.process"
					+ " where do1.is_deleted = 0 ";

			if (!ServicesUtil.isEmpty(createdBy)
					|| (!ServicesUtil.isEmpty(substitutedUser) || !ServicesUtil.isEmpty(substitutingUser)
							|| !ServicesUtil.isEmpty(endDate) || !ServicesUtil.isEmpty(startDate))) {

				queryString += " and ";
				if (!ServicesUtil.isEmpty(createdBy)) {
					queryString += " upper(do1.created_by)=upper('" + createdBy + "') and ";
				}
				if (!ServicesUtil.isEmpty(substitutedUser)) {
					queryString += " upper(do1.substituted_user)=upper('" + substitutedUser + "') and ";
				}
				if (!ServicesUtil.isEmpty(substitutingUser)) {
					queryString += " upper(do1.substituting_user)=upper('" + substitutingUser + "') and ";
				}

				if (!ServicesUtil.isEmpty(startDate) || !ServicesUtil.isEmpty(endDate)) {
					queryString += " ( ";
					if (!ServicesUtil.isEmpty(startDate)) {
						queryString += " ('" + startDate + "' between do1.start_date and do1.end_date ) or ";
					}

					if (!ServicesUtil.isEmpty(endDate)) {
						queryString += " ('" + endDate + "' between do1.start_date and do1.end_date) or ";
					}
					if (!ServicesUtil.isEmpty(endDate) && !ServicesUtil.isEmpty(startDate)) {
						queryString += "  do1.start_date BETWEEN '" + startDate + "' and '" + endDate + "' or"
								+ " do1.end_date BETWEEN '" + startDate + "' and '" + endDate + "' or ";
					}
					queryString = queryString.substring(0, queryString.length() - 3) + ")";

				} else
					queryString = queryString.substring(0, queryString.length() - 4);
			}

			queryString += " order by do1.start_date, do1.end_date desc ";
			System.err.println("[WBP-Dev][Workbox][PMC][SubstitutionDao][getRules][queryString]" + queryString);
			Query query = session.createSQLQuery(queryString);
			List<Object[]> resulstList = query.list();
			
			Map<Integer, String> statusMapping = new HashMap<>();
			statusMapping.put(0, "PENDING");
			statusMapping.put(1, "APPROVED");
			statusMapping.put(2, "REJECTED");
			statusMapping.put(3, "CANCELED");
			
			for (Object[] object : resulstList) {
				String ruleId = String.valueOf(object[0]);
				if(ruleMap.keySet().contains(ruleId)) {
					SubstitutionRulesDto dto = ruleMap.get(ruleId);
					ProcessStatusDto processStatus = new ProcessStatusDto();
					String status = statusMapping.get((Integer) object[15]);
					processStatus.setProcessName((String) object[13]);
					processStatus.setStatus(status);
					processStatus.setLabelName(ServicesUtil.isEmpty((String) object[14])==true?(String) object[13]:(String) object[14]);
					dto.getProcessStatus().add(processStatus);
					dto.getProcessList().add((String) object[13]);
				}
				else{
					SubstitutionRulesDto dto = new SubstitutionRulesDto();
					dto.setRuleId(String.valueOf(object[0]));
					dto.setVersion((Integer) object[1]);
					dto.setSubstitutedUser(String.valueOf(object[2]));
					dto.setSubstitutedUserName(String.valueOf(object[3]));
					dto.setSubstitutingUser(String.valueOf(object[4]));
					dto.setSubstitutingUserName(String.valueOf(object[5]));
					dto.setStartDate(ServicesUtil.convertFromDateToString((Date) object[6]));
					dto.setEndDate(ServicesUtil.convertFromDateToString((Date) object[7]));
					dto.setUpdateAt(ServicesUtil.convertFromDateToString((Date) object[8]));
					dto.setModifiedAt(ServicesUtil.convertFromDateToString((Date) object[9]));
					dto.setCreatedAt(ServicesUtil.convertFromDateToString((Date) object[10]));
					dto.setCreatedBy((String) object[11]);
					dto.setCreatedByDisp((String) object[12]);
					dto.setActive(ServicesUtil.asBoolean(object[16]));
					dto.setEnabled(ServicesUtil.asBoolean(object[17]));
					
					
					ProcessStatusDto processStatus = new ProcessStatusDto();
					String status = statusMapping.get((Integer) object[15]);
					processStatus.setProcessName((String) object[13]);
					processStatus.setStatus(status);
					processStatus.setLabelName(ServicesUtil.isEmpty((String) object[14])==true?(String) object[13]:(String) object[14]);
					dto.setProcessStatus(new ArrayList<>());
					dto.setProcessList(new ArrayList<>());
					dto.getProcessStatus().add(processStatus);
					dto.getProcessList().add((String) object[13]);
					
					ruleMap.put(ruleId, dto);
				}
			}
			
			
			
//			List<SubstitutionRuleDo> resultList = query.list();
//			if (!ServicesUtil.isEmpty(resultList)) {
//				for (SubstitutionRuleDo substitutionRuleDo : resultList) {
//
//					SubstitutionRulesDto substitutionRulesDto = exportDto(substitutionRuleDo);
//					if (ServicesUtil.isEmpty((String) substitutionRulesDto.getCreatedBy())) {
//						substitutionRulesDto.setCreatedBy(substitutionRulesDto.getSubstitutedUser());
//					}
//
//					String processQuery = "select sp.process,sp.status,pct.process_display_name from SUBSTITUTION_PROCESSES sp join process_config_tb pct "
//							+ "on sp.process=pct.process_name where rule_id='"
//							+ substitutionRulesDto.getRuleId() + "' and version='" + substitutionRulesDto.getVersion()
//							+ "'";
//
//					Map<Integer, String> statusMapping = new HashMap<>();
//					statusMapping.put(0, "PENDING");
//					statusMapping.put(1, "APPROVED");
//					statusMapping.put(2, "REJECTED");
//					statusMapping.put(3, "CANCELED");
//					List<ProcessStatusDto> processStatusDtos = new ArrayList<>();
//					List<String> process = new ArrayList<String>();
//					List<Object[]> processResultList = this.getSession().createSQLQuery(processQuery).list();
//					if (!ServicesUtil.isEmpty(processResultList)) {
//
//						for (Object[] obj : processResultList) {
//							ProcessStatusDto processStatus = new ProcessStatusDto();
//							String status = statusMapping.get((Integer) obj[1]);
//							processStatus.setProcessName((String) obj[0]);
//							processStatus.setStatus(status);
//							processStatus.setLabelName(ServicesUtil.isEmpty((String) obj[2])==true?(String) obj[0]:(String) obj[2]);
//							processStatusDtos.add(processStatus);
//							process.add((String) obj[0]);
//						}
//
//					}
//					substitutionRulesDto.setProcessStatus(processStatusDtos);
//					substitutionRulesDto.setProcessList(process);
//					ruleDtos.add(substitutionRulesDto);
//				}
//			}
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][getSubstitution][error]"
							+ e.getLocalizedMessage());
		}
		return new ArrayList<>(ruleMap.values());
	}


	@SuppressWarnings("unchecked")
	public List<String> getSubstitutedUser(String userId) {
		Session session = sessionFactory.openSession();
		String queryString = "select distinct substituted_User from SUBSTITUTION_RULE where Substituting_user='"
				+ userId + "' ";
		return session.createSQLQuery(queryString).list();

	}

	@SuppressWarnings("unchecked")
	public List<String> getActiveSubstitutedUser(String userId) {
		Session session = sessionFactory.openSession();
		String queryString = "select distinct upper(substituted_user) from user_task_mapping where upper(substituting_user)=upper('"
				+ userId + "') union select distinct (substituted_user) from "
				+ "substitution_rule where upper(substituting_user)=upper('" + userId + "')"
				+ " and is_active=1 and is_enable=1";
		return session.createSQLQuery(queryString).list();
	}

	public String getSubstitutedTaskOrNot(String userId, String instanceId) {
		// String result = PMCConstant.FAILURE;
		try {
			Session session = sessionFactory.openSession();
			String queryString = "select Count(*) from user_task_mapping where Substituting_user='" + userId
					+ "' and task_id='" + instanceId + "' ";

			BigInteger result = (BigInteger) session.createSQLQuery(queryString).list().get(0);
			System.err.println("[WBP-Dev][Workbox][SubstitutionRuleDao][result]" + result.intValue());
			if (result.intValue() > 0)
				return PMCConstant.SUCCESS;

		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][SubstitutionRuleDao][error]" + e.getLocalizedMessage());
		}

		return PMCConstant.FAILURE;
	}

	public String validateRule(SubstitutionRulesDto dto) {
		try {
			
			Session session = sessionFactory.openSession();
			String qString = "select count(*) from Substitution_Rule do1 inner join (select max(version) as version,rule_Id from Substitution_Rule group by rule_Id) do2 on do2.rule_id=do1.rule_id and do2.version=do1.version" + 
									" where do1.is_deleted=0 and  do1.start_date = '" +   dto.getStartDate() +"' and do1.substituting_user = '" + dto.getSubstitutingUser() + "' and do1.substituted_user = '" + dto.getSubstitutedUser() +
									"'";
			
//			String queryString = "SELECT COUNT(*) FROM SUBSTITUTION_RULE" + " WHERE START_DATE = '" + dto.getStartDate()
//					+ "' AND SUBSTITUTING_USER = '" + dto.getSubstitutingUser() + "'" + " AND SUBSTITUTED_USER = '"
//					+ dto.getSubstitutedUser() + "' AND IS_DELETED = 0";

			BigInteger result = (BigInteger) session.createSQLQuery(qString).list().get(0);
			System.err.println("[WBP-Dev][Workbox][SubstitutionRuleDao][Validation result]" + result.intValue());
			if (result.intValue() > 0)
				return PMCConstant.FAILURE;

		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][SubstitutionRuleDao][error]" + e);
		}

		return PMCConstant.SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public List<String> getSubstitutedUserForTask(String userId, String eventId) {
		Session session = sessionFactory.openSession();
		String queryString = "SELECT DISTINCT SR.SUBSTITUTED_USER FROM SUBSTITUTION_PROCESSES SP "
				+ "INNER JOIN SUBSTITUTION_RULE SR ON SR.RULE_ID = SP.RULE_ID  "
				+ "WHERE SR.IS_ACTIVE = 1 AND SR.SUBSTITUTING_USER = '" + userId + "' AND (SP.PROCESS = ("
				+ " SELECT PROC_NAME FROM TASK_EVENTS WHERE EVENT_ID = '" + eventId + "') OR "
				+ "(SR.SUBSTITUTED_USER IN (SELECT TASK_OWNER FROM TASK_OWNERS WHERE EVENT_ID = '" + eventId + "')"
				+ " AND SP.PROCESS = 'ALL')) "
				+ "AND SR.SUBSTITUTING_USER NOT IN (SELECT TASK_OWNER FROM TASK_OWNERS WHERE EVENT_ID = '" + eventId
				+ "')";
		return session.createSQLQuery(queryString).list();
	}

	public void updateSubsRule(String approver, String requestor) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query updateQry = session
				.createSQLQuery("UPDATE SUBSTITUTION_RULE SET IS_ACTIVE = 1 " + "WHERE SUBSTITUTING_USER = '" + approver
						+ "' AND SUBSTITUTED_USER = '" + requestor + "' "
						+ "AND RULE_ID IN (SELECT DISTINCT SR.RULE_ID FROM SUBSTITUTION_RULE SR "
						+ "INNER JOIN SUBSTITUTION_PROCESSES SP ON SR.RULE_ID = SP.RULE_ID "
						+ "WHERE SR.SUBSTITUTING_USER = '" + approver + "' AND SR.SUBSTITUTED_USER = '" + requestor
						+ "' " + "AND (PROCESS = 'CFAApproverMatrixChangeProcess' OR PROCESS = 'ALL'))");
		updateQry.executeUpdate();
		tx.commit();
		session.close();

	}

	public List<Object[]> getTaskAuditLogs(SubstitutionTaskAuditDto dto) {
		Session session = sessionFactory.openSession();
		String query = "select event_id , substituted_user , substituting_user , action_type, updated_at , task_audit_id from substitution_task_audit";
		if (!ServicesUtil.isEmpty(dto.getTaskAuditId()) || !ServicesUtil.isEmpty(dto.getEventId())
				|| !ServicesUtil.isEmpty(dto.getSubstitutedUser()) || !ServicesUtil.isEmpty(dto.getSubstitutingUser())
				|| !ServicesUtil.isEmpty(dto.getUpdateAt()) || !ServicesUtil.isEmpty(dto.getActionType())) {

			query += " where ";
			if (!ServicesUtil.isEmpty(dto.getTaskAuditId())) {
				query += "upper(task_audit_id) = upper('" + dto.getTaskAuditId() + "') and ";
			}
			if (!ServicesUtil.isEmpty(dto.getEventId())) {
				query += "upper(event_id) = upper('" + dto.getEventId() + "') and ";
			}
			if (!ServicesUtil.isEmpty(dto.getSubstitutedUser())) {
				query += "upper(substituted_user) = upper('" + dto.getSubstitutedUser() + "') and ";
			}
			if (!ServicesUtil.isEmpty(dto.getSubstitutingUser())) {
				query += "upper(substituting_user) = upper('" + dto.getSubstitutingUser() + "') and ";
			}

			if (!ServicesUtil.isEmpty(dto.getActionType())) {
				query += "upper(action_type) = upper('" + dto.getActionType() + "') and ";
			}

			query = query.substring(0, query.length() - 4);

		}
		System.err.println("[WBP-Dev][Workbox][SubstitutionRuleDao][getTaskAuditLogs] query : " + query);
		return session.createSQLQuery(query).list();
	}

	public void updateCurrentRule(String ruleId, int version) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String query = "update substitution_rule set updated_at = '" + ServicesUtilSubs.getUTCTime()
				+ "',modified_at = '" + ServicesUtilSubs.getUTCTime()
				+ "', is_active = 0 , is_enable = 0 where rule_id = '" + ruleId + "' and version = " + version;
		session.createSQLQuery(query).executeUpdate();
		tx.commit();
		session.close();
	}

	public SubstitutionRulesDto getRuleByVersion(String ruleId, int version) {
		Session session = sessionFactory.openSession();
		String query = "select do from SubstitutionRuleDo do where ruleId = '" + ruleId + "' and version = " + version;
		SubstitutionRuleDo substitutionRuleDo = (SubstitutionRuleDo) session.createQuery(query)
				.uniqueResult();
		SubstitutionRulesDto substitutionRulesDto = exportDto(substitutionRuleDo);

		String processQuery = "select process,status from SUBSTITUTION_PROCESSES where rule_id='" + ruleId
				+ "' and version = " + version;
		Map<Integer, String> statusMapping = new HashMap<>();
		statusMapping.put(0, "PENDING");
		statusMapping.put(1, "APPROVED");
		statusMapping.put(2, "REJECTED");
		statusMapping.put(3, "CANCELED");
		List<ProcessStatusDto> processStatusDtos = new ArrayList<>();
		List<String> process = new ArrayList<String>();
		List<Object[]> processResultList = session.createSQLQuery(processQuery).list();

		if (!ServicesUtil.isEmpty(processResultList)) {
			for (Object[] obj : processResultList) {
				ProcessStatusDto processStatus = new ProcessStatusDto();
				String status = statusMapping.get((Integer) obj[1]);
				processStatus.setProcessName((String) obj[0]);
				processStatus.setStatus(status);
				processStatusDtos.add(processStatus);
				if (status.equalsIgnoreCase("APPROVED"))
					process.add((String) obj[0]);
			}
			substitutionRulesDto.setProcessList(process);
			substitutionRulesDto.setProcessStatus(processStatusDtos);
		}

		return substitutionRulesDto;

	}

	public String activateOrDeactivateRule(SubstitutionRulesDto dto) {
		
		String result = PMCConstant.FAILURE;
		try {
//			String updateString = "update substitution_rule set is_active = :isActive , updated_at = :updatedAt where rule_id = :ruleId and version = :version";
//			Query query = this.getSession().createSQLQuery(updateString).setParameter("isActive", dto.isActive())
//					.setParameter("ruleId", dto.getRuleId())
//					.setParameter("version", dto.getVersion());
//			query.executeUpdate();
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(importDto(dto));
			tx.commit();
			session.close();
			result = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleVersionDao][activateOrDeactivateRule][error]"
							+ e.getLocalizedMessage());
		}
		return result;
	}
}
