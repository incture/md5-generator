package oneapp.incture.workbox.demo.notification.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.notification.entity.NotificationProfileSettingDo;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationProfileSettingDto;


@Repository("NotificationProfileSettingDao")
public class NotificationProfileSettingDao
		extends BaseDao<NotificationProfileSettingDo, NotificationProfileSettingDto> {
	
	@Autowired
	private SessionFactory sessionFactory;
	

	@Override
	protected NotificationProfileSettingDto exportDto(NotificationProfileSettingDo entity) {

		NotificationProfileSettingDto NotificationProfileSettingDto = new NotificationProfileSettingDto();

		if (!ServicesUtil.isEmpty(entity.getProfileName()))
			NotificationProfileSettingDto.setProfileName(entity.getProfileName());
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			NotificationProfileSettingDto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getProfileId()))
			NotificationProfileSettingDto.setProfileId(entity.getProfileId());
		if (!ServicesUtil.isEmpty(entity.getSettingId()))
			NotificationProfileSettingDto.setSettingId(entity.getSettingId());
		if (!ServicesUtil.isEmpty(entity.getIsActive()))
			NotificationProfileSettingDto.setIsActive(entity.getIsActive());
		if (!ServicesUtil.isEmpty(entity.getScheduledFrom()))
			NotificationProfileSettingDto
					.setScheduledFrom(ServicesUtil.convertFromDateToString(entity.getScheduledFrom()));
		if (!ServicesUtil.isEmpty(entity.getScheduledTo()))
			NotificationProfileSettingDto.setScheduledTo(ServicesUtil.convertFromDateToString(entity.getScheduledTo()));

		return NotificationProfileSettingDto;
	}

	@Override
	protected NotificationProfileSettingDo importDto(NotificationProfileSettingDto fromDto) {

		NotificationProfileSettingDo entity = new NotificationProfileSettingDo();

		if (!ServicesUtil.isEmpty(fromDto.getProfileName()))
			entity.setProfileName(fromDto.getProfileName());
		if (!ServicesUtil.isEmpty(fromDto.getUserId()))
			entity.setUserId(fromDto.getUserId());
		if (!ServicesUtil.isEmpty(fromDto.getProfileId()))
			entity.setProfileId(fromDto.getProfileId());
		if (!ServicesUtil.isEmpty(fromDto.getSettingId()))
			entity.setSettingId(fromDto.getSettingId());
		if (!ServicesUtil.isEmpty(fromDto.getIsActive()))
			entity.setIsActive(fromDto.getIsActive());
		if (!ServicesUtil.isEmpty(fromDto.getScheduledFrom()))
			entity.setScheduledFrom(ServicesUtil.convertFromStringToDateSubstitution(fromDto.getScheduledFrom()));
		if (!ServicesUtil.isEmpty(fromDto.getScheduledTo()))
			entity.setScheduledTo(ServicesUtil.convertFromStringToDateSubstitution(fromDto.getScheduledTo()));

		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<NotificationProfileSettingDto> getResult(String userId, String isAdmin) {
		List<NotificationProfileSettingDto> ruleDtos = null;
		NotificationProfileSettingDto dto = null;
		String queryString = " ";
		List<String> adminProfiles = getAllAdminProfile();
		int count = 0;
		if (ServicesUtil.isEmpty(isAdmin)) {
			queryString = "select do from NotificationProfileSettingDo do where ";

			for (String adminProfile : adminProfiles) {

				if (count == 0) {
					queryString = queryString + " (do.profileName='" + adminProfile + "' and "
							+ "userId =(case when ((select count(do) from NotificationProfileSettingDo do where do.profileName='"
							+ adminProfile + "' and " + "do.userId='" + userId + "')=0) then 'Admin' else '" + userId
							+ "' end)) ";
				} else {
					queryString = queryString + "or (do.profileName='" + adminProfile + "' and "
							+ "userId =(case when ((select count(do) from NotificationProfileSettingDo do where do.profileName='"
							+ adminProfile + "' and " + "do.userId='" + userId + "')=0) then 'Admin' else '" + userId
							+ "' end)) ";
				}
				count = ++count;

			}

			queryString = queryString + "or ( "
					+ "do.profileName not in (select profileName from NotificationProfileSettingDo"
					+ " where userId = 'Admin' and is_active = 1) " + " and do.userId='" + userId
					+ "') or (user_id = 'Admin' and is_active = 0)";
		}

		else {
			queryString = "select do from NotificationProfileSettingDo do where userId = 'Admin'";
		}

		System.err.println("getResult [QueryString]" + queryString);
		Query query = this.getSession().createQuery(queryString);
		List<NotificationProfileSettingDo> resultList = query.list();
		if (!ServicesUtil.isEmpty(resultList)) {
			ruleDtos = new ArrayList<NotificationProfileSettingDto>();
			for (NotificationProfileSettingDo entity : resultList) {
				dto = new NotificationProfileSettingDto();
				dto = exportDto(entity);
				if (entity.getUserId().equalsIgnoreCase("admin") && entity.getIsActive().equals(false))
					dto.setIsEnable(false);
				else {
					dto.setIsEnable(true);
					dto.setIsActive(false);
				}
				ruleDtos.add(dto);

			}
		}
		return ruleDtos;
	}

	public String updateOldProfile(String name) {
		Session session=null;
		  session = sessionFactory.openSession();
         Transaction tx = session.beginTransaction();
		String updateEnableQuery = "update notification_profile_setting set is_active=0 where  user_id='" + name + "'";
		int resultRows = session.createSQLQuery(updateEnableQuery).executeUpdate();
		tx.commit();
        session.close();
		System.err.println("[WBP-Dev][Workbox][updateSubstitutionRule][query]" + updateEnableQuery);
		if (resultRows > 0)
			return "SUCCESS";

		else
			return "FAILURE";
	}

	public String updateAdminProfile(String name, String profileName, Boolean isActive) {
		int enable = isActive == true ? 1 : 0;
		 Session session = sessionFactory.openSession();
         Transaction tx = session.beginTransaction();
		String updateEnableQuery = "update notification_profile_setting set is_active= '" + enable
				+ "' where  user_id='" + name + "' and " + "profile_name ='" + profileName + "'";
		int resultRows = session.createSQLQuery(updateEnableQuery).executeUpdate();
		tx.commit();
        session.close();
		System.err.println("[WBP-Dev][Workbox][updateSubstitutionRule][query]" + updateEnableQuery);
		if (resultRows > 0)
			return "SUCCESS";

		else
			return "FAILURE";
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllAdminProfile() {

		List<String> resultList = new ArrayList<>();
		try {
			Query query = getSession().createSQLQuery(
					"select distinct profile_name from NOTIFICATION_PROFILE_SETTING where user_id = 'Admin' and is_active = 1");
			resultList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;

	}
	
	
	public String deleteProfilesByAdmin(NotificationProfileSettingDto dto) {
		Session session=null;
		String profileId=dto.getProfileId();
	      session = sessionFactory.openSession();
         Transaction tx = session.beginTransaction();
		String updateEnableQuery = "delete from notification_profile_setting where  profile_id='" +profileId + "'";
		int resultRows = session.createSQLQuery(updateEnableQuery).executeUpdate();
		tx.commit();
        session.close();
		System.err.println("[WBP-Dev][Workbox][updateSubstitutionRule][query]" + updateEnableQuery);
		if (resultRows > 0)
			return "SUCCESS";

		else
			return "FAILURE";
		
	}
	
	public String deleteProfiles(NotificationProfileSettingDto dto) {
		Session session=null;
		String profileId=dto.getProfileId();
	      session = sessionFactory.openSession();
         Transaction tx = session.beginTransaction();
		String updateEnableQuery = "delete from notification_profile_setting where  profile_id='" +profileId + "'";
		int resultRows = session.createSQLQuery(updateEnableQuery).executeUpdate();
		tx.commit();
        session.close();
		System.err.println("[WBP-Dev][Workbox][updateSubstitutionRule][query]" + updateEnableQuery);
		if (resultRows > 0)
			return "SUCCESS";

		else
			return "FAILURE";
		
	}
	

}