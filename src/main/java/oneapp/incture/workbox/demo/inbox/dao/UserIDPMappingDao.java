package oneapp.incture.workbox.demo.inbox.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.inbox.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.inbox.entity.UserIDPMappingDo;

/**
 * @author Neelam Raj
 *
 */
@Repository("userIDPMappingDao")
public class UserIDPMappingDao extends BaseDao<UserIDPMappingDo, UserIDPMappingDto> {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private CompressImageDao compressImageDao;

	private static final Logger logger = LoggerFactory.getLogger(UserIDPMappingDao.class);

	protected UserIDPMappingDo importDto(UserIDPMappingDto fromDto) {
		UserIDPMappingDo mappingDo = null;
		if (!ServicesUtil.isEmpty(fromDto)) {
			mappingDo = new UserIDPMappingDo();
			mappingDo.setSerialId(fromDto.getSerialId());
			mappingDo.setUserEmail(fromDto.getUserEmail());
			mappingDo.setUserFirstName(fromDto.getUserFirstName());
			mappingDo.setUserLastName(fromDto.getUserLastName());
			mappingDo.setUserLoginName(fromDto.getUserLoginName());
			mappingDo.setUserId(fromDto.getUserId());
			mappingDo.setUserRole(fromDto.getUserRole());
			if (!ServicesUtil.isEmpty(fromDto.getSalesForceId()))
				mappingDo.setSalesForceId(fromDto.getSalesForceId());
			if (!ServicesUtil.isEmpty(fromDto.getSharepointId()))
				mappingDo.setSharepointId(fromDto.getSharepointId());
			if (!ServicesUtil.isEmpty(fromDto.getSuccessFactorsId()))
				mappingDo.setSuccessFactorsId(fromDto.getSuccessFactorsId());
			if (!ServicesUtil.isEmpty(fromDto.getTaskAssignable()))
				mappingDo.setTaskAssignable(fromDto.getTaskAssignable());
			if (!ServicesUtil.isEmpty(fromDto.getTheme()))
				mappingDo.setTheme(fromDto.getTheme());
			if (!ServicesUtil.isEmpty(fromDto.getLanguage()))
				mappingDo.setLanguage(fromDto.getLanguage());
			if (!ServicesUtil.isEmpty(fromDto.getProfilePic()))
				mappingDo.setProfilePic(fromDto.getProfilePic().getBytes());
			if (!ServicesUtil.isEmpty(fromDto.getCompressedImage()))
				mappingDo.setCompressedImage(fromDto.getCompressedImage().getBytes());
			if (!ServicesUtil.isEmpty(fromDto.getBudget()))
				mappingDo.setAfeNexusBudget(fromDto.getBudget());
		}
		return mappingDo;
	}

	protected UserIDPMappingDto exportDto(UserIDPMappingDo entity) {
		UserIDPMappingDto mappingDto = null;
		if (!ServicesUtil.isEmpty(entity)) {
			mappingDto = new UserIDPMappingDto();
			mappingDto.setSerialId(entity.getSerialId());
			mappingDto.setUserEmail(entity.getUserEmail());
			mappingDto.setUserFirstName(entity.getUserFirstName());
			mappingDto.setUserLastName(entity.getUserLastName());
			mappingDto.setUserLoginName(entity.getUserLoginName());
			mappingDto.setUserId(entity.getUserId());
			mappingDto.setUserRole(entity.getUserRole());
			if (!ServicesUtil.isEmpty(entity.getSalesForceId()))
				mappingDto.setSalesForceId(entity.getSalesForceId());
			if (!ServicesUtil.isEmpty(entity.getSharepointId()))
				mappingDto.setSharepointId(entity.getSharepointId());
			if (!ServicesUtil.isEmpty(entity.getSuccessFactorsId()))
				mappingDto.setSuccessFactorsId(entity.getSuccessFactorsId());
			if (!ServicesUtil.isEmpty(entity.getTaskAssignable()))
				mappingDto.setTaskAssignable(entity.getTaskAssignable());
			if (!ServicesUtil.isEmpty(entity.getTheme()))
				mappingDto.setTheme(entity.getTheme());
			if (!ServicesUtil.isEmpty(entity.getLanguage()))
				mappingDto.setLanguage(entity.getLanguage());
			if (!ServicesUtil.isEmpty(entity.getProfilePic()))
				mappingDto.setProfilePic(new String(entity.getProfilePic()));
			if (!ServicesUtil.isEmpty(entity.getCompressedImage()))
				mappingDto.setCompressedImage(new String(entity.getCompressedImage()));
			if (!ServicesUtil.isEmpty(entity.getAfeNexusBudget()))
				mappingDto.setBudget(entity.getAfeNexusBudget());
		}
		return mappingDto;
	}

	@SuppressWarnings("unchecked")
	public List<UserIDPMappingDto> getAllUser() {
		List<UserIDPMappingDto> dtos = new ArrayList<UserIDPMappingDto>();

		String queryString = " select do from UserIDPMappingDo do";
		try {
			logger.error("[PMC][UserIDPMappingDao][getAllUser]" + queryString);
			Query query = this.getSession().createQuery(queryString);
			List<UserIDPMappingDo> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (UserIDPMappingDo entity : resultList) {
					entity.setCompressedImage(null);
					dtos.add(exportDto(entity));
				}
			}
		} catch (Exception e) {
			System.err.println(
					"[PMC][UserIDPMappingDao][UserIDPMappingDao][getIDPUser][error]" + e.getLocalizedMessage());
		}
		return dtos;
	}

	public String createIDPUser(UserIDPMappingDto dto) {
		String response = PMCConstant.FAILURE;
		try {

			this.getSession().save(importDto(dto));
			response = PMCConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[PMC][UserIDPMappingDao][UserIDPMappingDao][createIDPUser][error]" + e.getMessage());
		}
		return response;
	}

	public UserIDPMappingDto getUserDetail(String userId) {
		UserIDPMappingDto userIDPMappingDto = null;
		String fetchDetailStr = "select p from UserIDPMappingDo p where p.userId= :userId";
		Query fetchDetailQry = this.getSession().createQuery(fetchDetailStr);
		fetchDetailQry.setParameter("userId", userId);
		UserIDPMappingDo userIDPMappingDos = (UserIDPMappingDo) fetchDetailQry.uniqueResult();

		userIDPMappingDto = new UserIDPMappingDto();

		userIDPMappingDos.setProfilePic(null);
		if (!ServicesUtil.isEmpty(userIDPMappingDos))
			userIDPMappingDto = exportDto(userIDPMappingDos);

		return userIDPMappingDto;
	}

	public Integer updateUserDetail(UserIDPMappingDto userIDPMappingDto) {
		System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER2");
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String updateDetailStr = "UPDATE USER_IDP_MAPPING SET ";
		if (!ServicesUtil.isEmpty(userIDPMappingDto.getTheme()))
			updateDetailStr = updateDetailStr + "THEME= '" + userIDPMappingDto.getTheme() + "',LANGUAGE='"
					+ userIDPMappingDto.getLanguage() + "',";

		// if(!ServicesUtil.isEmpty(userIDPMappingDto.getProfilePic()))
		// {
		// updateDetailStr = updateDetailStr+"COMPRESSED_IMAGE=
		// :compressedImg,";
		// userIDPMappingDto.setCompressedImage(compressImageDao.compressImage
		// (userIDPMappingDto.getProfilePic(),
		// userIDPMappingDto.getImageType()));
		// }

		System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER3");
		Query updateDetailQry = session
				.createSQLQuery(updateDetailStr.substring(0, updateDetailStr.length() - 1) + " WHERE USER_ID = '"
						+ userIDPMappingDto.getUserId() + "'");

		// if(!ServicesUtil.isEmpty(userIDPMappingDto.getProfilePic()))
		// updateDetailQry.setParameter("compressedImg",
		// userIDPMappingDto.getCompressedImage().getBytes());
		System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER4");
		int count = updateDetailQry.executeUpdate();
		tx.commit();
		session.close();
		return count;
	}

	@Async
	public void updateoriginalImage(UserIDPMappingDto userIDPMappingDto) {

		String updateDetailStr = "UPDATE USER_IDP_MAPPING SET PROFILE_PIC = :originalImg, COMPRESSED_IMAGE= :compressedImg";

		System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER3");
		Query updateDetailQry = this.getSession()
				.createSQLQuery(updateDetailStr + " WHERE USER_ID = '" + userIDPMappingDto.getUserId() + "'");

		updateDetailQry.setParameter("originalImg", userIDPMappingDto.getProfilePic().getBytes());
		updateDetailQry.setParameter("compressedImg", compressImageDao
				.compressImage(userIDPMappingDto.getProfilePic(), userIDPMappingDto.getImageType()).getBytes());
		System.err.println("[WBP-Dev][WORKBOX_PRODUCT]UPDATE USER4");
		updateDetailQry.executeUpdate();
	}

	public String getUserName(String userId) {
		Query fetchNameQry = this.getSession().createSQLQuery("SELECT USER_FIRST_NAME || ' ' || USER_LAST_NAME "
				+ "FROM USER_IDP_MAPPING WHERE USER_ID = '" + userId + "'");
		String name = (String) fetchNameQry.uniqueResult();
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserIDPMappingDto> getAllUserBudget() {
		List<UserIDPMappingDto> dtos = new ArrayList<UserIDPMappingDto>();
		String queryString = " select do from UserIDPMappingDo do where do.afeNexusBudget is not NULL";
		try {
			logger.error("[PMC][UserIDPMappingDao][getAllUser]" + queryString);
			Query query = this.getSession().createQuery(queryString);
			List<UserIDPMappingDo> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (UserIDPMappingDo entity : resultList) {
					entity.setCompressedImage(null);
					dtos.add(exportDto(entity));
				}
			}
		} catch (Exception e) {
			System.err.println(
					"[PMC][UserIDPMappingDao][UserIDPMappingDao][getIDPUser][error]" + e.getLocalizedMessage());
		}
		return dtos;
	}
	
	public void updateIDPUserBudget(UserIDPMappingDto userIDPMappingDto) {
		String queryString = "update USER_IDP_MAPPING set AFE_NEXUS_BUDGET=" + userIDPMappingDto.getBudget() + " where "
				+ " user_login_name='"+userIDPMappingDto.getUserLoginName()+"'";
		System.err.println(queryString);
		try {
			Session session = this.getSession();
			Transaction tx = session.beginTransaction();
			logger.error("[PMC][UserIDPMappingDao][updateIDPUserBudget]" + queryString);
			this.getSession().createSQLQuery(queryString).executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println(
					"[PMC][UserIDPMappingDao][UserIDPMappingDao][updateIDPUserBudget][error]" + e.getLocalizedMessage());
		}
	}
	
	public String saveOrUpdateUsers(List<UserIDPMappingDto> users) {
        String message = PMCConstant.FAILURE;
        try {
            if (!ServicesUtil.isEmpty(users) && users.size() > 0) {
                Session session = sessionFactory.openSession();
                Transaction tx = session.beginTransaction();
                for (int i = 0; i < users.size(); i++) {
                    UserIDPMappingDto currentUser = users.get(i);
                    if(ServicesUtil.isEmpty(currentUser.getLanguage()))
                        currentUser.setLanguage("en");
                    session.saveOrUpdate(importDto(currentUser));
                    if (i % 20 == 0 && i > 0) {
                        session.flush();
                        session.clear();
                    }
                }
                session.flush();
                session.clear();
                tx.commit();
                session.close();
            }
            message = PMCConstant.SUCCESS;
        }catch (Exception e) {
            System.err.println("saveOrUpdateUsers error"+e);
        }
        return message;
    }
}