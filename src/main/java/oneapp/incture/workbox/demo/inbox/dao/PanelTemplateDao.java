package oneapp.incture.workbox.demo.inbox.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dao.UserIdpMappingDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;
import oneapp.incture.workbox.demo.inbox.dto.InboxTypeDto;
import oneapp.incture.workbox.demo.inbox.dto.PanelResponseDto;
import oneapp.incture.workbox.demo.inbox.entity.PanelTemplateDo;

@Repository("PanelTemplateDao")
//////@Transactional
public class PanelTemplateDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	InboxFilterDao inboxFilterDao;
	@Autowired
	InboxTypeDao inboxTypeDao;
	@Autowired
	private UserIdpMappingDao userIdpMappingDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}

	}

	public PanelResponseDto getInboxPanel(Token token) {
		ResponseMessage responseMessage = new ResponseMessage();
		PanelResponseDto panelResponseDto = new PanelResponseDto();
		List<PanelTemplateDo> panelTemplateDos = new ArrayList<>();
		List<InboxTypeDto> inboxTypeDtos = new ArrayList<>();
		Map<String, List<InboxTypeDto>> parentMap = new HashMap<>();
		String columnName = "PANEL_NAME";
		try {
			String taskOwner = token.getLogonName();
			Boolean isAdmin = fetchIsAdmin(taskOwner);
			System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel] isAdmin : " + isAdmin);
			String language = userIdpMappingDao.getUserLanguage(taskOwner);
			if (!ServicesUtil.isEmpty(language)) {
				switch (language) {

				case "in":
					columnName = "PANEL_NAME_IN";
					break;

				case "en":
					columnName = "PANEL_NAME";
					break;

				case "ar":
					columnName = "PANEL_NAME_AR";
					break;

				case "zh":
					columnName = "PANEL_NAME_ZH";
					break;

				default:
					columnName = "PANEL_NAME";
					break;

				}
			}
			Session session = sessionFactory.openSession();
			String selectPanelTempQuery = "SELECT PANEL_ID," + columnName
					+ ",PANEL_TYPE,PARENT_ID,ICON,SEQUENCE,ROLE_ACCESS "
					+ "FROM PANEL_TEMPLATE WHERE DEVICE_TYPE = 'WEB' ORDER BY SEQUENCE ";
			System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel] selectPanelTempQuery : " + selectPanelTempQuery);
			Query q = session.createSQLQuery(selectPanelTempQuery.trim());

			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();
			for (Object[] obj : resultList) {
				PanelTemplateDo panelTemplateDo = new PanelTemplateDo();
				panelTemplateDo.setPanelId(obj[0] == null ? null : (String) obj[0]);
				panelTemplateDo.setPanelName(obj[1] == null ? null : (String) obj[1]);
				panelTemplateDo.setPanelType(obj[2] == null ? null : (String) obj[2]);
				panelTemplateDo.setParentId(obj[3] == null ? null : (String) obj[3]);
				panelTemplateDo.setIcon(obj[4] == null ? null : (String) obj[4]);
				panelTemplateDo.setSequence(obj[5] == null ? null : (String) obj[5]);
				panelTemplateDo.setRoleAccess(obj[6] == null ? null : (String) obj[6]);

				if (panelTemplateDo.getParentId().equals(PMCConstant.PANEL_TEMPLATE_ID_PANEL)) {
					panelTemplateDos.add(panelTemplateDo);
					parentMap.put(panelTemplateDo.getPanelId(), null);

					if (panelTemplateDo.getPanelId().equals(PMCConstant.PANEL_TEMPLATE_ID_GROUPS)) {
						fetchGroups(parentMap, taskOwner);
					} else if (panelTemplateDo.getPanelId().equals(PMCConstant.PANEL_TEMPLATE_ID_VIEWS)) {
						fetchViews(parentMap, taskOwner);
					}
				} else if (!panelTemplateDo.getParentId().isEmpty()) {
					fetchTasks(parentMap, panelTemplateDo, taskOwner, isAdmin);
				}
			}

			for (PanelTemplateDo templateDo : panelTemplateDos) {
				InboxTypeDto inboxTypeDto = new InboxTypeDto();
				inboxTypeDto.setName(templateDo.getPanelName());
				inboxTypeDto.setType(templateDo.getPanelType());
				inboxTypeDto.setInboxId(templateDo.getPanelId());
				// inboxTypeDto.setParentId("");
				inboxTypeDto.setIcon(templateDo.getIcon());
				inboxTypeDto.setDtoList(parentMap.get(templateDo.getPanelId()));

				inboxTypeDtos.add(inboxTypeDto);
			}

			panelResponseDto.setInboxTypeDtos(inboxTypeDtos);

			responseMessage.setMessage(PMCConstant.READ_SUCCESS);
			responseMessage.setStatus(PMCConstant.SUCCESS);
			responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			panelResponseDto.setResponseMessage(responseMessage);

			return panelResponseDto;

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORFLOW DETAIL][PROCESS DETAIL]ERROR:" + e.getMessage());
			e.printStackTrace();
			return null;
		}

	}

	private Boolean fetchIsAdmin(String taskOwner) {
		System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel][fetchIsAdmin] taskOwner : " + taskOwner);
		String query = "SELECT USER_ROLE FROM USER_IDP_MAPPING WHERE USER_ID='" + taskOwner + "'";
		System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel][fetchIsAdmin] query : " + query);
		Query q = this.getSession().createSQLQuery(query.trim());
		String userType = String.valueOf((q.uniqueResult()));
		if (userType.equals(PMCConstant.USER_ROELS_ADMIN)) {
			return true;
		}
		return false;
	}

	private void fetchTasks(Map<String, List<InboxTypeDto>> parentMap, PanelTemplateDo panelTemplateDo,
			String taskOwner, Boolean isAdmin) {
		List<InboxTypeDto> inboxdtoList = parentMap.get(panelTemplateDo.getParentId());
		if (inboxdtoList == null) {
			inboxdtoList = new ArrayList<InboxTypeDto>();
		}
		String baseQuery = inboxFilterDao.getSelectQuery() + inboxFilterDao.getCommonJoinQuery()
				+ inboxFilterDao.getCommonConditionQuery(null, null);
		String inboxTypeQuery = "";
		switch (panelTemplateDo.getPanelId()) {
		case "MyInbox":
			inboxTypeQuery = InboxTypeDao.getMyTaskQuery(taskOwner);
			break;
		case "AdminInbox":
			if (!isAdmin)
				return;

			inboxTypeQuery = InboxTypeDao.getAdminInboxTaskQuery(new InboxFilterDto());
			break;
		case "SubstitutionInbox":
			inboxTypeQuery = inboxTypeDao.getSubstitutedTaskQuery(taskOwner);
			break;
		default:
		}
		String countQuery = "SELECT COUNT(*) FROM ( " + baseQuery + inboxTypeQuery + ")";
		System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel][fetchTasks]countQuery : " + countQuery);
		Session session = sessionFactory.openSession();
		Query countQry = session.createSQLQuery(countQuery.trim());
		BigDecimal taskCount = ServicesUtil.getBigDecimal(countQry.uniqueResult());
		InboxTypeDto typeDto = new InboxTypeDto();
		typeDto.setName(panelTemplateDo.getPanelName());
		typeDto.setCount(taskCount.toString());
		typeDto.setInboxId(panelTemplateDo.getPanelId());
		typeDto.setParentId(PMCConstant.PANEL_TEMPLATE_ID_ALL_TASK);
		typeDto.setType(PMCConstant.PANEL_TEMPLATE_LINK);

		inboxdtoList.add(typeDto);
		parentMap.put(panelTemplateDo.getParentId(), inboxdtoList);
	}

	private void fetchGroups(Map<String, List<InboxTypeDto>> parentMap, String taskOwner) {
		List<InboxTypeDto> inboxdtoList = parentMap.get(PMCConstant.PANEL_TEMPLATE_ID_GROUPS);
		if (inboxdtoList == null) {
			inboxdtoList = new ArrayList<InboxTypeDto>();
		}
		String selectGroupQuery = "SELECT DISTINCT TW.GROUP_ID,TW.GROUP_OWNER FROM TASK_OWNERS TW INNER JOIN TASK_EVENTS TE ON TW.EVENT_ID=TE.EVENT_ID"
				+ " WHERE TW.TASK_OWNER='" + taskOwner
				+ "' AND TE.STATUS IN ('READY','RESERVED') AND TW.GROUP_ID IS NOT NULL";
		System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel][fetchGroups]selectGroupQuery : " + selectGroupQuery);
		Session session = sessionFactory.openSession();
		Query q = session.createSQLQuery(selectGroupQuery.trim());

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = q.list();
		for (Object[] obj : resultList) {
			InboxTypeDto typeDto = new InboxTypeDto();
			typeDto.setInboxId(obj[0] == null ? null : (String) obj[0]);
			typeDto.setParentId(PMCConstant.PANEL_TEMPLATE_ID_GROUPS);
			typeDto.setName(obj[1] == null ? null : (String) obj[1]);
			typeDto.setType(PMCConstant.PANEL_TEMPLATE_LINK);

			inboxdtoList.add(typeDto);
		}
		parentMap.put(PMCConstant.PANEL_TEMPLATE_ID_GROUPS, inboxdtoList);
	}

	private void fetchViews(Map<String, List<InboxTypeDto>> parentMap, String taskOwner) {
		List<InboxTypeDto> inboxdtoList = parentMap.get(PMCConstant.PANEL_TEMPLATE_ID_VIEWS);
		if (inboxdtoList == null) {
			inboxdtoList = new ArrayList<InboxTypeDto>();
		}
		String selectViewQuery = "SELECT UCF.VIEW_NAME, UCF.FILTER_ID, UCF.FILTER_DATA FROM USER_CUSTOM_FILTER UCF WHERE UCF.IS_VIEW=1 AND UCF.USER_ID='"
				+ taskOwner + "'";
		System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel][fetchViews]selectViewQuery : " + selectViewQuery);
		Session session = sessionFactory.openSession();
		Query q = session.createSQLQuery(selectViewQuery.trim());

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = q.list();
		for (Object[] obj : resultList) {
			InboxTypeDto typeDto = new InboxTypeDto();
			typeDto.setName(obj[0] == null ? null : (String) obj[0]);
			typeDto.setParentId(PMCConstant.PANEL_TEMPLATE_ID_VIEWS);
			typeDto.setInboxId(obj[1] == null ? null : ((int) obj[1] + ""));
			typeDto.setType(PMCConstant.PANEL_TEMPLATE_LINK);
			typeDto.setViewPayload(obj[2] == null ? null : (String) obj[2]);

			inboxdtoList.add(typeDto);
		}
		parentMap.put(PMCConstant.PANEL_TEMPLATE_ID_VIEWS, inboxdtoList);
	}

	public PanelResponseDto getInboxPanelForMobile(Integer dropDownCount, Token token) {
		ResponseMessage responseMessage = new ResponseMessage();
		PanelResponseDto panelResponseDto = new PanelResponseDto();
		List<PanelTemplateDo> panelTemplateDos = new ArrayList<>();
		List<InboxTypeDto> inboxTypeDtos = new ArrayList<>();
		Map<String, List<InboxTypeDto>> parentMap = new HashMap<>();

		try {
//			User user = UserManagementUtil.getLoggedInUser();
			String taskOwner = token.getLogonName();
			Boolean isAdmin = fetchIsAdmin(taskOwner);
			System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel] isAdmin : " + isAdmin);
			String selectPanelTempQuery = "SELECT PANEL_ID,PANEL_NAME,PANEL_TYPE,PARENT_ID,ICON,SEQUENCE,ROLE_ACCESS FROM PANEL_TEMPLATE WHERE DEVICE_TYPE = 'MOBILE' ORDER BY SEQUENCE";
			System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel] selectPanelTempQuery : " + selectPanelTempQuery);
			Query q = this.getSession().createSQLQuery(selectPanelTempQuery.trim());

			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();
			for (Object[] obj : resultList) {
				PanelTemplateDo panelTemplateDo = new PanelTemplateDo();
				panelTemplateDo.setPanelId(obj[0] == null ? null : (String) obj[0]);
				panelTemplateDo.setPanelName(obj[1] == null ? null : (String) obj[1]);
				panelTemplateDo.setPanelType(obj[2] == null ? null : (String) obj[2]);
				panelTemplateDo.setParentId(obj[3] == null ? null : (String) obj[3]);
				panelTemplateDo.setIcon(obj[4] == null ? null : (String) obj[4]);
				panelTemplateDo.setSequence(obj[5] == null ? null : (String) obj[5]);
				panelTemplateDo.setRoleAccess(obj[6] == null ? null : (String) obj[6]);

				if (panelTemplateDo.getParentId().equals(PMCConstant.PANEL_TEMPLATE_ID_PANEL)) {
					panelTemplateDos.add(panelTemplateDo);
					fetchTasksForMobile(parentMap, panelTemplateDo, taskOwner, isAdmin, dropDownCount);
					// parentMap.put(panelTemplateDo.getPanelId(), null);

					if (panelTemplateDo.getPanelId().equals(PMCConstant.PANEL_TEMPLATE_ID_GROUPS)) {
						fetchGroups(parentMap, taskOwner);
					} else if (panelTemplateDo.getPanelId().equals(PMCConstant.PANEL_TEMPLATE_ID_VIEWS)) {
						fetchViews(parentMap, taskOwner);
					}
				} else if (!panelTemplateDo.getParentId().isEmpty()) {
					fetchTasksForMobile(parentMap, panelTemplateDo, taskOwner, isAdmin, dropDownCount);
				}
			}

			for (PanelTemplateDo templateDo : panelTemplateDos) {
				InboxTypeDto inboxTypeDto = new InboxTypeDto();
				inboxTypeDto.setName(templateDo.getPanelName());
				inboxTypeDto.setType(templateDo.getPanelType());
				inboxTypeDto.setInboxId(templateDo.getPanelId());
				// inboxTypeDto.setParentId("");
				inboxTypeDto.setIcon(templateDo.getIcon());
				inboxTypeDto.setDtoList(parentMap.get(templateDo.getPanelId()));

				inboxTypeDtos.add(inboxTypeDto);
			}

			if (!isAdmin)
				inboxTypeDtos = inboxTypeDtos.stream()
						.filter(a -> !a.getInboxId().equalsIgnoreCase(PMCConstant.PANEL_TEMPLATE_ID_ADMININBOX))
						.collect(Collectors.toList());

			panelResponseDto.setInboxTypeDtos(inboxTypeDtos);

			responseMessage.setMessage(PMCConstant.READ_SUCCESS);
			responseMessage.setStatus(PMCConstant.SUCCESS);
			responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			panelResponseDto.setResponseMessage(responseMessage);
		} catch (Exception e) {
			System.err.println("PanelTemplateDao.getInboxPanelForMobile() exception : " + e.getMessage());
		}
		return panelResponseDto;
	}

	@SuppressWarnings("unchecked")
	private void fetchTasksForMobile(Map<String, List<InboxTypeDto>> parentMap, PanelTemplateDo panelTemplateDo,
			String taskOwner, Boolean isAdmin, Integer dropDownCount) {
		List<InboxTypeDto> inboxTypeList = new ArrayList<>();
		List<InboxTypeDto> inboxdtoList = parentMap.get(panelTemplateDo.getParentId());
		if (inboxdtoList == null) {
			inboxdtoList = new ArrayList<InboxTypeDto>();
		}
		String baseQuery = "SELECT TOP " + dropDownCount + " PE.NAME, PCT.PROCESS_DISPLAY_NAME,COUNT(TE.EVENT_ID) "
				+ inboxFilterDao.getCommonJoinQuery() + inboxFilterDao.getCommonConditionQuery(null, null);
		String inboxTypeQuery = "";
		switch (panelTemplateDo.getPanelId()) {
		case "MyInbox":
			inboxTypeQuery = InboxTypeDao.getMyTaskQuery(taskOwner);
			break;
		case "AdminInbox":
			if (!isAdmin)
				return;
			inboxTypeQuery = InboxTypeDao.getAdminInboxTaskQuery(new InboxFilterDto());
			break;
		case "SubstitutionInbox":
			inboxTypeQuery = inboxTypeDao.getSubstitutedTaskQuery(taskOwner);
			break;
		default:
		}
		String groupByQuery = " GROUP BY PE.NAME,PCT.PROCESS_DISPLAY_NAME ";
		System.err.println("PanelTemplateDao.fetchTasksForMobile() process query : "
				+ (baseQuery + inboxTypeQuery + groupByQuery));
		List<Object[]> resultSet = this.getSession().createSQLQuery(baseQuery + inboxTypeQuery + groupByQuery).list();
		if (panelTemplateDo.getPanelId().equalsIgnoreCase(PMCConstant.PANEL_TEMPLATE_ID_MYINBOX)
				|| panelTemplateDo.getPanelId().equalsIgnoreCase(PMCConstant.PANEL_TEMPLATE_ID_ADMININBOX)) {
			InboxTypeDto dto = new InboxTypeDto();
			dto.setCount(null);
			dto.setName(PMCConstant.PANEL_NAME_ALL);
			dto.setType(PMCConstant.PANEL_TEMPLATE_LINK);
			dto.setInboxId(PMCConstant.PANEL_TEMPLATE_ID_ALL);
			dto.setParentId(panelTemplateDo.getPanelId());
			dto.setIcon(panelTemplateDo.getIcon());
			inboxTypeList.add(dto);
			inboxdtoList.add(dto);
		}
		for (Object[] obj : resultSet) {
			InboxTypeDto dto = new InboxTypeDto();
			dto.setCount(obj[2].toString());
			dto.setName(obj[1].toString());
			dto.setType(PMCConstant.PANEL_TEMPLATE_LINK);
			dto.setInboxId(obj[0].toString());
			dto.setParentId(panelTemplateDo.getPanelId());
			// inboxTypeDto.setParentId("");
			dto.setIcon(panelTemplateDo.getIcon());
			dto.setDtoList(parentMap.get(panelTemplateDo.getPanelId()));
			inboxTypeList.add(dto);
			inboxdtoList.add(dto);
			parentMap.put(panelTemplateDo.getParentId(), inboxdtoList);
			/*
			 * if(panelTemplateDo.getPanelId().equalsIgnoreCase(PMCConstant.
			 * PANEL_TEMPLATE_ID_MYINBOX) ||
			 * panelTemplateDo.getPanelId().equalsIgnoreCase(PMCConstant.
			 * PANEL_TEMPLATE_ID_ADMININBOX) ||
			 * panelTemplateDo.getPanelId().equalsIgnoreCase(PMCConstant.
			 * PANEL_TEMPLATE_ID_MYINBOX)) parentMap.put(panelTemplateDo.getPanelId(),
			 * inboxdtoList);
			 */
		}
		if (panelTemplateDo.getPanelId().equalsIgnoreCase(PMCConstant.PANEL_TEMPLATE_ID_MYINBOX)
				|| panelTemplateDo.getPanelId().equalsIgnoreCase(PMCConstant.PANEL_TEMPLATE_ID_ADMININBOX)) {
			/*
			 * InboxTypeDto dto = new InboxTypeDto(); dto.setCount(null);
			 * dto.setName(PMCConstant.PANEL_NAME_MORE);
			 * dto.setType(PMCConstant.PANEL_TEMPLATE_LINK);
			 * dto.setInboxId(PMCConstant.PANEL_NAME_MORE);
			 * dto.setParentId(panelTemplateDo.getPanelId()); //
			 * inboxTypeDto.setParentId(""); dto.setIcon(panelTemplateDo.getIcon());
			 * inboxTypeList.add(dto);
			 */
			parentMap.put(panelTemplateDo.getPanelId(), inboxTypeList);
		}
	}

	public PanelResponseDto getInboxTiles(Token token) {
		ResponseMessage responseMessage = new ResponseMessage();
		PanelResponseDto panelResponseDto = new PanelResponseDto();
		List<InboxTypeDto> inboxTypeDtos = new ArrayList<>();
		try {

			String taskOwner = token.getLogonName();
			Boolean isAdmin = fetchIsAdmin(taskOwner);
			System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel] isAdmin : " + isAdmin);
			String selectPanelTempQuery = "SELECT PANEL_NAME,PANEL_TYPE,PANEL_ID,ICON FROM PANEL_TEMPLATE WHERE DEVICE_TYPE = 'TILE' ";
			if (!isAdmin)
				selectPanelTempQuery = selectPanelTempQuery + " AND ROLE_ACCESS != 'Admin User' ";
			selectPanelTempQuery = selectPanelTempQuery + " ORDER BY SEQUENCE ";
			System.err.println("[WBP-Dev][WorkboxRest][getInboxPanel] selectPanelTempQuery : " + selectPanelTempQuery);
			Query q = this.getSession().createSQLQuery(selectPanelTempQuery.trim());

			@SuppressWarnings("unchecked")
			List<Object[]> resultList = q.list();

			for (Object[] obj : resultList) {
				InboxTypeDto inboxTypeDto = new InboxTypeDto();
				inboxTypeDto.setName(obj[0] == null ? null : (String) obj[0]);
				inboxTypeDto.setType(obj[1] == null ? null : (String) obj[1]);
				inboxTypeDto.setInboxId(obj[2] == null ? null : (String) obj[2]);
				// inboxTypeDto.setParentId("");
				inboxTypeDto.setIcon(obj[3] == null ? null : (String) obj[3]);

				inboxTypeDtos.add(inboxTypeDto);
			}

			panelResponseDto.setInboxTypeDtos(inboxTypeDtos);

			responseMessage.setMessage(PMCConstant.READ_SUCCESS);
			responseMessage.setStatus(PMCConstant.SUCCESS);
			responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			panelResponseDto.setResponseMessage(responseMessage);

			return panelResponseDto;

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORFLOW DETAIL][PROCESS DETAIL]ERROR:" + e.getMessage());
			return null;
		}

	}
}
