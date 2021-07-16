package oneapp.incture.workbox.demo.inbox.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;
import oneapp.incture.workbox.demo.substitution.dao.SubstitutionRuleDao;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionRulesDto;

@Repository("InboxTypeDao")
//////@Transactional
public class InboxTypeDao {

	@Autowired
	SubstitutionRuleDao subsDao;

	public static String getTaskByGroupQuery(String inboxName) {
		String groupTaskQuery = "";
		try {
			groupTaskQuery = groupTaskQuery
					+ " AND TW.TASK_OWNER = :taskOwner AND TE.EVENT_ID IN(SELECT DISTINCT TES.EVENT_ID "
					+ " FROM TASK_EVENTS TES INNER JOIN TASK_OWNERS TWS ON TES.EVENT_ID = TWS.EVENT_ID ";

			if (inboxName != null) {
				groupTaskQuery = groupTaskQuery + " where TWS.GROUP_ID = :groupId) ";
			} else {
				groupTaskQuery = groupTaskQuery + " where TWS.GROUP_ID IS NOT NULL) ";
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return groupTaskQuery;
	}

	public static String getMyTaskQuery(String taskOwner) {
		String myTaskQuery = "AND ( TW.TASK_OWNER = '" + taskOwner + "'  AND ( TE.STATUS = 'READY' OR TE.CUR_PROC = '"
				+ taskOwner + "' ))";
		return myTaskQuery;

	}

	public static String getAdminInboxTaskQuery(InboxFilterDto inboxFilterDto) {
		String result = "";
		if (PMCConstant.PANEL_TEMPLATE_ID_ADMININBOX.equalsIgnoreCase(inboxFilterDto.getInboxType())
				&& PMCConstant.TASK_STATUS_ALL_COMPLETED.equalsIgnoreCase(
						(inboxFilterDto.getQuickFilter() == null) ? null : inboxFilterDto.getQuickFilter().getStatus()))
			return " AND TE.STATUS IN('COMPLETED','RESOLVED','APPROVE','REJECT','DONE') ";
		else {
			if (!ServicesUtil.isEmpty(inboxFilterDto) && !ServicesUtil.isEmpty(inboxFilterDto.getQuickFilter())
					&& !ServicesUtil.isEmpty(inboxFilterDto.getQuickFilter().getStatus())) {
				if (inboxFilterDto.getQuickFilter().getStatus().equalsIgnoreCase(PMCConstant.TASK_STATUS_READY))
					result = " AND TE.STATUS IN ('READY') ";
				else if (inboxFilterDto.getQuickFilter().getStatus().equalsIgnoreCase(PMCConstant.TASK_STATUS_RESERVED))
					result = " AND TE.STATUS IN ('RESERVED') ";
				else
					result = " AND TE.STATUS IN('READY','RESERVED') ";
			} else
				result = " AND TE.STATUS IN('READY','RESERVED') ";
			return result;
		}
	}

	public String getSubstitutedTaskQuery(String user) {
		String substitutionQuery = " AND (";
		try {

			substitutionQuery = subsDao.getSubstitutedRules(user);
			/*
			 * List<SubstitutionRulesDto> substitedUser = subsDao.getSubstitution(user,
			 * PMCConstant.SUBSTITUTING, PMCConstant.INBOX_TYPE_SUBSTITUTION_INBOX);
			 */
			/*
			 * if (!ServicesUtil.isEmpty(substitedUser)) { for (SubstitutionRulesDto dto :
			 * substitedUser) { if (!ServicesUtil.isEmpty(dto.getProcessList()) &&
			 * !dto.getProcessList().contains(PMCConstant.SEARCH_ALL)) { substitutionQuery
			 * += " (TW.TASK_OWNER='" + dto.getSubstitutedUser() + "' AND TE.PROC_NAME IN ("
			 * + ServicesUtil.getStringFromList(dto.getProcessList()) + ")) OR"; } else
			 * substitutionQuery += " TW.TASK_OWNER='" + dto.getSubstitutedUser() + "' OR";
			 * } substitutionQuery = substitutionQuery.substring(0,
			 * substitutionQuery.length() - 2); substitutionQuery += ")"; } else
			 * substitutionQuery = " AND (te.status='NoSubstitution') ";
			 */
		} catch (Exception e) {
			System.err.println("[WBP-Dev][PMC][InboxTypeDto][getSubstitutedTaskQuery]error" + e.getLocalizedMessage());
		}
		System.err.println("[WBP-Dev][PMC][InboxTypeDto][getSubstitutedTaskQuery]" + substitutionQuery);
		return substitutionQuery;
	}

	public static String getDraftTaskQuery() {
		String draftTaskQuery = "AND TE.CREATED_BY = :taskOwner AND TE.STATUS = 'DRAFT'";
		return draftTaskQuery;
	}

	public static String getViewTaskQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getCreatedTaskQuery() {
		String createdTaskQuery = "AND TE.CREATED_BY = :taskOwner AND TE.STATUS IN('READY','RESERVED','RESOLVED')";
		return createdTaskQuery;
	}

	public static String getCompletedTaskQuery() {
		String completedTaskQuery = " AND TE.STATUS NOT IN('READY','RESERVED','DRAFT') AND (TE.CUR_PROC=:taskOwner "
				+ "OR TE.created_by= :taskOwner) ";
		return completedTaskQuery;
	}

	public static String getPinnedTaskQuery() {
		String pinnedTaskQuery = "AND TE.EVENT_ID IN(SELECT PINNED_TASK_ID FROM PINNED_TASK"
				+ " WHERE USER_ID = :taskOwner AND (TE.STATUS = 'READY' OR (TE.CUR_PROC = "
				+ "	:taskOwner AND TE.STATUS = 'RESERVED') ";
		// for substitution task condition
		pinnedTaskQuery += "OR (TE.CUR_PROC = TW.TASK_OWNER AND TE.STATUS = 'RESERVED' AND TE.CUR_PROC IN "
				+ "(SELECT SR.SUBSTITUTED_USER FROM SUBSTITUtION_RULE SR "
				+ "INNER JOIN SUBSTITUTION_PROCESSES SP ON SP.RULE_ID = SR.RULE_ID "
				+ "WHERE SR.SUBSTITUTING_USER = :taskOwner AND (SP.PROCESS='ALL' OR SP.PROCESS=TE.PROC_NAME)))))";
		return pinnedTaskQuery;
	}

	public static String getUserTaskQuery(List<String> userId) {

		String userTaskQuery = "AND TW.TASK_OWNER IN (";
		if (!ServicesUtil.isEmpty(userId)) {
			for (String user : userId) {
				userTaskQuery = userTaskQuery + "'" + user + "',";
			}
			userTaskQuery = userTaskQuery.substring(0, userTaskQuery.length() - 1) + ") ";
		} else {
			userTaskQuery = " ";
		}
		System.err.println("[WBP-Dev]InboxTypeDao.getAdminUserTaskQuery() " + userTaskQuery);
		return userTaskQuery;
	}

	public static String getAllTaskQuery() {

		return " ";
	}

	public static String getRequestorCompletedQuery(String upperCase) {
		String createdTaskQuery = "AND TE.CREATED_BY = :taskOwner AND TE.STATUS NOT IN('READY','RESERVED','DRAFT') "
				+ "and te.event_id in (select event_id from ("
				+ "select peq.request_id,teq.event_id as event_id,row_number() over (partition by peq.request_id  "
				+ "order by peq.completed_at desc,teq.created_at desc) as rownumber " + "from Process_events peq "
				+ "join Task_events teq on teq.process_id= peq.process_id "
				+ "where teq.completed_at is not null and peq.request_id=pe.request_id) where rownumber=1)";

		return createdTaskQuery;
	}

}
