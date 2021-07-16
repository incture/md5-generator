package oneapp.incture.workbox.demo.notification.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.hibernate.Transaction;
import com.sap.cloud.security.xsuaa.token.Token;
import oneapp.incture.workbox.demo.notification.entity.NotificationViewSettingsDo;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationEventDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationViewSettingsDto;

//@Transactional
@Repository("NotificationViewSettingDao")
public class NotificationViewSettingDao extends BaseDao<NotificationViewSettingsDo, NotificationViewSettingsDto> {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected NotificationViewSettingsDto exportDto(NotificationViewSettingsDo entity) {

		NotificationViewSettingsDto notificationViewSettingsDto = new NotificationViewSettingsDto();

		if (!ServicesUtil.isEmpty(entity.getViewType()))
			notificationViewSettingsDto.setViewType(entity.getViewType());
		if (!ServicesUtil.isEmpty(entity.getViewName()))
			notificationViewSettingsDto.setViewName(entity.getViewName());
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			notificationViewSettingsDto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getSettings()))
			notificationViewSettingsDto.setSettings(entity.getSettings());
		if (!ServicesUtil.isEmpty(entity.getViewIcon()))
			notificationViewSettingsDto.setViewIcon(entity.getViewIcon());
		if (!ServicesUtil.isEmpty(entity.getIsDefault()))
			notificationViewSettingsDto.setIsDefault(entity.getIsDefault());
		if (!ServicesUtil.isEmpty(entity.getId()))
			notificationViewSettingsDto.setId(entity.getId());
		if (!ServicesUtil.isEmpty(entity.getAdminUser()))
			notificationViewSettingsDto.setAdminUser(entity.getAdminUser());
		if (!ServicesUtil.isEmpty(entity.getCategory()))
			notificationViewSettingsDto.setCategory(entity.getCategory());
		return notificationViewSettingsDto;
	}

	@Override
	protected NotificationViewSettingsDo importDto(NotificationViewSettingsDto fromDto) {

		NotificationViewSettingsDo entity = new NotificationViewSettingsDo();

		if (!ServicesUtil.isEmpty(fromDto.getViewType()))
			entity.setViewType(fromDto.getViewType());
		if (!ServicesUtil.isEmpty(fromDto.getViewName()))
			entity.setViewName(fromDto.getViewName());
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			entity.setUserId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getSettings()))
			entity.setSettings(fromDto.getSettings());
		if (!ServicesUtil.isEmpty(fromDto.getViewIcon()))
			entity.setViewIcon(fromDto.getViewIcon());
		if (!ServicesUtil.isEmpty(fromDto.getIsDefault()))
			entity.setIsDefault(fromDto.getIsDefault());
		if (!ServicesUtil.isEmpty(fromDto.getId()))
			entity.setId(fromDto.getId());
		if (!ServicesUtil.isEmpty(fromDto.getAdminUser()))
			entity.setAdminUser(fromDto.getAdminUser());
		if (!ServicesUtil.isEmpty(fromDto.getCategory()))
			entity.setCategory(fromDto.getCategory());

		return entity;
	}

	public String saveOrUpdate(NotificationViewSettingsDto fromDto) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(importDto(fromDto));
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}

	@SuppressWarnings("unchecked")
	public List<NotificationViewSettingsDto> getViewObjects(String userId, String isDefault) {

		List<NotificationViewSettingsDto> notificationViewDtos = null;
		NotificationViewSettingsDto viewDto = null;
		String query = "select distinct vs.view_type, vs.view_name , vs.settings ,CONCAT( (SELECT COALESCE(STRING_AGG (event_name, ', '), '') "
				+ "FROM notification_events ne where vs.view_name=ne.event_group AND ne.id = '1') ,(SELECT  COALESCE(STRING_AGG ( ne.event_name, ', ') , '') "
				+ "FROM notification_events ne join event_channel_mapping ecm on ecm.event_id=ne.event_id join view_setting vst "
				+ "on ne.event_group = vst.view_name where ecm.channel= vs.view_name AND ne.id = '1' "
				+ "and (vst.default= '1' and vst.user_id='Admin' and vst.id = '2'))) 	as"
				+ "eventList, vs.default , vs.view_icon ,(SELECT vst.default from view_setting vst where vst.user_id='Admin' and vst.id = '2' "
				+ "and vst.view_name = vs.view_name) as isenable" + "  from view_setting vs ";

		if (!ServicesUtil.isEmpty(userId)) {
			query += "where vs.user_id='" + userId + "'";

			if (!ServicesUtil.isEmpty(isDefault)) {
				query += " and vs.id= '" + isDefault + "'";
			}
		}

		System.err.println("NotificationViewSettingDao.getViewObjects() query" + query);
		Query taskCreatorQry = getSession().createSQLQuery(query);
		List<Object[]> resultList = taskCreatorQry.list();

		if (resultList != null) {
			notificationViewDtos = new ArrayList<>();
			for (Object[] obj : resultList) {
				viewDto = new NotificationViewSettingsDto();
				viewDto.setViewType(obj[0] == null ? null : (String) obj[0]);
				viewDto.setViewName(obj[1] == null ? null : (String) obj[1]);
				viewDto.setSettings(obj[2] == null ? null : (String) obj[2]);
				viewDto.setEvents(obj[3] == null ? null : (String) obj[3]);

				byte flag = (obj[4] == null ? 0 : (byte) obj[4]);
				viewDto.setIsDefault(flag == 1 ? true : false);

				viewDto.setViewIcon(obj[5] == null ? null : (String) obj[5]);

				byte isEnable = (obj[6] == null ? flag : (byte) obj[6]);
				viewDto.setIsEnable(isEnable == 1 ? true : false);
				notificationViewDtos.add(viewDto);
			}
		}

		return notificationViewDtos;
	}

	public String getUserRole(Token token) {
		Session session = null;
		session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query fetchNameQry = session.createSQLQuery("select USER_ROLE from user_idp_mapping" + " where user_id='"
				+ token.getLogonName().toUpperCase() + "' ");
		String role = (String) fetchNameQry.uniqueResult();
		System.err.println("[getUserRole][Role]" + role);
		tx.commit();
		session.close();

		return role;
	}

	public List<String> getExistingUsers() {

		List<String> resultList = new ArrayList<>();
		try {
			Query query = getSession()
					.createSQLQuery("select distinct user_id from view_setting where user_id NOT IN ('Admin')");
			resultList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;

	}

	public String deleteViewList(String viewList) {

		try {
			Session session = null;
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			String queryString = "delete from VIEW_SETTING where view_name = '" + viewList + "'";

			System.err.println("[deleteViewList][Query String ]" + queryString);
			session.createSQLQuery(queryString).executeUpdate();
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
	}

}