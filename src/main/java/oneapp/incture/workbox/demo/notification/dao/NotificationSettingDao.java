package oneapp.incture.workbox.demo.notification.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.notification.entity.UserSettingDetailsDo;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationSettingDto;

//@Transactional
@Repository("NotificationSettingDao")
public class NotificationSettingDao extends BaseDao<UserSettingDetailsDo, NotificationSettingDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected NotificationSettingDto exportDto(UserSettingDetailsDo entity) {

		NotificationSettingDto NotificationProfileSettingDto = new NotificationSettingDto();

		if (!ServicesUtil.isEmpty(entity.getProfileSettingId()))
			NotificationProfileSettingDto.setProfileSettingId(entity.getProfileSettingId());
		if (!ServicesUtil.isEmpty(entity.getAdditionalSettingId()))
			NotificationProfileSettingDto.setAdditionalSettingId(entity.getAdditionalSettingId());
		if (!ServicesUtil.isEmpty(entity.getValue()))
			NotificationProfileSettingDto.setValue(entity.getValue());
		if (!ServicesUtil.isEmpty(entity.getMore()))
			NotificationProfileSettingDto.setMore(entity.getMore());
		if (!ServicesUtil.isEmpty(entity.getIsDefault()))
			NotificationProfileSettingDto.setIsDefault(entity.getIsDefault());

		return NotificationProfileSettingDto;
	}

	@Override
	protected UserSettingDetailsDo importDto(NotificationSettingDto fromDto) {

		UserSettingDetailsDo entity = new UserSettingDetailsDo();

		if (!ServicesUtil.isEmpty(fromDto.getProfileSettingId()))
			entity.setProfileSettingId(fromDto.getProfileSettingId());
		if (!ServicesUtil.isEmpty(fromDto.getAdditionalSettingId()))
			entity.setAdditionalSettingId(fromDto.getAdditionalSettingId());
		if (!ServicesUtil.isEmpty(fromDto.getValue()))
			entity.setValue(fromDto.getValue());
		if (!ServicesUtil.isEmpty(fromDto.getMore()))
			entity.setMore(fromDto.getMore());
		if (!ServicesUtil.isEmpty(fromDto.getIsDefault()))
			entity.setIsDefault(fromDto.getIsDefault());

		return entity;
	}

	public String saveOrUpdate(List<NotificationSettingDto> dtos) {
		Session session=null;
		try {
			
			for (NotificationSettingDto notificationSettingDto : dtos) {
				session=sessionFactory.openSession();
				Transaction tx=session.beginTransaction();
				session.saveOrUpdate(importDto(notificationSettingDto));
				tx.commit();
				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
		return PMCConstant.SUCCESS;

	}

	public String deleteSettings(List<NotificationSettingDto> dtos) {
		try {
			for (NotificationSettingDto notificationSettingDto : dtos) {
				remove(importDto(notificationSettingDto));
			}
		} catch (ExecutionFault e) {
			e.printStackTrace();
			return PMCConstant.FAILURE;
		}
		return PMCConstant.SUCCESS;

	}

	public String copyAdminSettings(String settingId, String newSettingId) {
		Session session = null;
		String query = "insert into user_setting_details select '" + newSettingId
				+ "',additonal_setting_id,value, more, default  from  user_setting_details where profile_setting_id='"
				+ settingId + "'";
		
		try {
			
		  session= sessionFactory.openSession();
		  Transaction tx = session.beginTransaction();
			Query insertQuery = session.createSQLQuery(query);
			insertQuery.executeUpdate();
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println("NotificationSettingDao.copyAdminSettings() error" + e.getMessage());
			return PMCConstant.FAILURE;
		}
	}

	@SuppressWarnings("unchecked")
	public List<NotificationSettingDto> getResult(String settingId) {
		List<NotificationSettingDto> ruleDtos = null;
		NotificationSettingDto dto = null;
		String queryString = " select do from UserSettingDetailsDo do where do.profileSettingId='1'";

		System.err.println("getResult [NotificationSettingDto][QueryString]" + queryString);
		Query query = this.getSession().createQuery(queryString);
		List<UserSettingDetailsDo> resultList = query.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			ruleDtos = new ArrayList<NotificationSettingDto>();
			for (UserSettingDetailsDo entity : resultList) {
				dto = new NotificationSettingDto();
				dto = exportDto(entity);
				ruleDtos.add(dto);
			}
		}
		return ruleDtos;
	}

	public String updateSettingsByAdmin(NotificationSettingDto settingDto) {

		String query = " ";
		Session session =null;
		int enable = settingDto.getIsEnable() == true ? 1 : 0;
		if (PMCConstant.ADMIN.equalsIgnoreCase(settingDto.getProfileSettingId())) {
			query = "update user_setting_details set default = '" + enable + "' where profile_setting_id in ('Admin') "
					+ "and ADDITONAL_SETTING_ID = '" + settingDto.getAdditionalSettingId() + "' ";
		} else {
			query = "update user_setting_details set default = '" + enable + "' where profile_setting_id in ( "
					+ "select settings from VIEW_SETTING where view_name in (select view_name from VIEW_SETTING "
					+ "where settings ='" + settingDto.getProfileSettingId() + "') and user_id = 'Admin' and id='1') "
					+ "and ADDITONAL_SETTING_ID = '" + settingDto.getAdditionalSettingId() + "' ";
		}
		try {
			 session= sessionFactory.openSession();
			  Transaction tx = session.beginTransaction();
			Query insertQuery = session.createSQLQuery(query);
			insertQuery.executeUpdate();
			tx.commit();
			session.close();
			return PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println("NotificationSettingDao.copyAdminSettings() error" + e.getMessage());
			return PMCConstant.FAILURE;
		}

	}

	public String getAdminSettingId(String profileId) {

		String settingId = null;

		if (PMCConstant.ADMIN.equalsIgnoreCase(profileId)) {
			settingId = PMCConstant.ADMIN;
		} else {
			Query fetchNameQry = this.getSession().createSQLQuery(
					"select settings from VIEW_SETTING where view_name in (select view_name from VIEW_SETTING "
							+ "where settings ='" + profileId + "') and user_id = 'Admin' and id='1'");

			if (!ServicesUtil.isEmpty(fetchNameQry.uniqueResult()))
				settingId = (String) fetchNameQry.uniqueResult();
		}
		System.err.println("[getSettingId][settingId]" + settingId);

		return settingId;

	}

}