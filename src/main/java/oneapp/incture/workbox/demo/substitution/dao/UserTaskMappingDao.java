package oneapp.incture.workbox.demo.substitution.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.substitution.dto.UserTaskMappingDto;
import oneapp.incture.workbox.demo.substitution.entity.UserTaskMappingDo;


@Repository("UserTaskMappingDao")
public class UserTaskMappingDao extends BaseDao<UserTaskMappingDo, UserTaskMappingDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(UserTaskMappingDao.class);
	private static final int _HIBERNATE_BATCH_SIZE = 300;
	
	
	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		}catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	protected UserTaskMappingDo importDto(UserTaskMappingDto fromDto) {
		UserTaskMappingDo mappingDo = null;
		if (!ServicesUtil.isEmpty(fromDto)) {
			mappingDo = new UserTaskMappingDo();
			mappingDo.setSubstitutedUser(fromDto.getSubstitutedUser());
			mappingDo.setSubstitutingUser(fromDto.getSubstitutingUser());
			mappingDo.setTaskId(fromDto.getTaskId());
		}
		return mappingDo;
	}

	protected UserTaskMappingDto exportDto(UserTaskMappingDo entity) {
		UserTaskMappingDto mappingDto = null;
		if (!ServicesUtil.isEmpty(entity)) {
			mappingDto = new UserTaskMappingDto();
			mappingDto.setSubstitutedUser(entity.getSubstitutedUser());
			mappingDto.setTaskId(entity.getTaskId());
			mappingDto.setSubstitutingUser(entity.getSubstitutingUser());
		}
		return mappingDto;
	}

	public void saveOrUpdateTasks(List<UserTaskMappingDto> list) {
		System.err.println("[WBP-Dev][Workbox][started][save or update][usermapping]");

		try {
			if (!ServicesUtil.isEmpty(list) && list.size() > 0) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < list.size(); i++) {
					session.saveOrUpdate(importDto(list.get(i)));
					if (i % _HIBERNATE_BATCH_SIZE == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				tx.commit();
				session.close();
				// session.close();
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][Batch insert][userTaskMapping]" + e.getLocalizedMessage());
		}
		System.err.println("[WBP-Dev][Workbox][Ended][save or update][userMapping]");

	}

	public String createUserTask(String taskId, String substitutedUser, String substitutingUser) {
		String response = PMCConstant.FAILURE;
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			UserTaskMappingDto dto = new UserTaskMappingDto();
			dto.setSubstitutedUser(substitutedUser);
			dto.setSubstitutingUser(substitutingUser);
			dto.setTaskId(taskId);
			session.save(importDto(dto));
			tx.commit();
			session.close();
			response = PMCConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[PMC][UserTaskMappingDao][createUserTaskMapping][error]" + e.getMessage());
		}
		return response;
	}

	@SuppressWarnings({ "unchecked" })
	public Map<String, UserTaskMappingDto> getDisabledSubstitutionResult(String taskId) {
		Map<String, UserTaskMappingDto> map = new HashMap<String, UserTaskMappingDto>();
		try {
			Session session = sessionFactory.openSession();
			UserTaskMappingDto dto = null;
			String queryString = "select ut.* from user_task_mapping ut"
					+ " join substitution_rule sr on sr.substituting_user=ut.substituting_user "
					+ "and sr.substituted_user=ut.substituted_user where ut.task_id='" + taskId
					+ "'  and not (sr.exist=1 and sr.is_active =1 and sr.is_enable=1)";

			Query query = session.createSQLQuery(queryString);
			List<Object[]> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					dto = new UserTaskMappingDto();
					dto.setSubstitutingUser((String) obj[1]);
					dto.setSubstitutedUser((String) obj[2]);
					dto.setTaskId((String) obj[3]);
					map.put((String) obj[1], dto);
				}
			}
		} catch (Exception e) {
			logger.error("[PMC][UserTaskMappingDao][getSubstitutionResult][error]" + e.getMessage());
		}
		return map;
	}

	@SuppressWarnings({ "unchecked" })
	public Map<String, UserTaskMappingDto> getSubstitutionResultNew(String taskId) {
		Map<String, UserTaskMappingDto> map = new HashMap<String, UserTaskMappingDto>();
		try {
			Session session = sessionFactory.openSession();
			UserTaskMappingDto dto = null;
			String queryString = "select ut.* from user_task_mapping ut"
					+ " join substitution_rule sr on sr.substituting_user=ut.substituting_user "
					+ "and sr.substituted_user=ut.substituted_user where ut.task_id='" + taskId
					+ "'  and not (sr.exist=1 and sr.is_active =1 and sr.is_enable=1)";

			Query query =session.createSQLQuery(queryString);
			List<Object[]> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (Object[] obj : resultList) {
					dto = new UserTaskMappingDto();
					dto.setSubstitutingUser((String) obj[1]);
					dto.setSubstitutedUser((String) obj[2]);
					dto.setTaskId((String) obj[3]);
					map.put((String) obj[3], dto);
				}
			}
		} catch (Exception e) {
			logger.error("[PMC][UserTaskMappingDao][getSubstitutionResult][error]" + e.getMessage());
		}
		return map;
	}

	@SuppressWarnings({ "unchecked" })
	public List<UserTaskMappingDto> substitutionForDeletion(String substitutedUser, String substitutingUser) {
		List<UserTaskMappingDto> dtos = new ArrayList<UserTaskMappingDto>();
		String queryString = "select ut from UserTaskMappingDo ut where (ut.substitutingUser='" + substitutingUser
				+ "' and ut.substitutedUser='" + substitutedUser + "')";
		try {
			Session session = sessionFactory.openSession();
			Query query = session.createQuery(queryString);
			List<UserTaskMappingDo> resultList = query.list();
			if (!ServicesUtil.isEmpty(resultList)) {
				for (UserTaskMappingDo entity : resultList) {
					dtos.add(exportDto(entity));
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][PMC][UserTaskMappingDao][substitutionForDeletion][error]" + e.getLocalizedMessage());
		}

		return dtos;
	}

	@SuppressWarnings({ "unchecked" })
	public List<UserTaskMappingDto> getSubstitutedUserDisabled(String taskId) {
		List<UserTaskMappingDto> list = new ArrayList<UserTaskMappingDto>();
		try {
			Session session = sessionFactory.openSession();
			String queryString = "select st from UserTaskMappingDo st where st.taskId='" + taskId + "'";
			Query query = session.createQuery(queryString);
			List<UserTaskMappingDo> listDo = query.list();
			for (UserTaskMappingDo entity : listDo) {
				list.add(exportDto(entity));
			}
		} catch (Exception e) {
			logger.error("[PMC][UserTaskMappingDao][getSubstitutedUserDisabled][error]" + e.getMessage());
		}
		return list;
	}

	/*public String deleteUserTask(UserTaskMappingDto dto) {
		try {
			String deleteQuery = "delete from user_task_mapping where id='" + dto.getId() + "'";
			Query q = this.getSession().createSQLQuery(deleteQuery);
			int result = (Integer) q.executeUpdate();
			if (result > 0)
				return PMCConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[PMC][UserTaskMappingDao][deleteUserTask][error]" + e.getMessage());
		}
		return PMCConstant.FAILURE;
	}*/

	public String recordExistsOrNot(String taskId, String substituted, String substituting) {
		try {
			Session session = sessionFactory.openSession();
			String queryString = "select count(*) from user_task_mapping where task_id='" + taskId
					+ "' and substituted_user ='" + substituted + "' and substituting_user='" + substituting + "'";
			BigInteger result = (BigInteger) session.createSQLQuery(queryString).list().get(0);
			if (result.intValue() > 0)
				return PMCConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[PMC][UserTaskMappingDao][recordExistsOrNot][error]" + e.getMessage());
		}
		return PMCConstant.FAILURE;
	}

	public String deleteDisabledSubstitution(String substitutedUser, String substitutingUser) {
		try {
			Session session = getSession();
			Transaction tx = session.beginTransaction();
			
			String deleteQuery = "delete from user_task_mapping where upper(substituted_user)=upper('" + substitutedUser
					+ "') and upper(substituting_user)=upper('" + substitutingUser + "')";
			Query q = session.createSQLQuery(deleteQuery);
			tx.commit();
			session.close();
			int result = (Integer) q.executeUpdate();
			if (result > 0)
				return PMCConstant.SUCCESS;
		} catch (Exception e) {
			logger.error("[PMC][UserTaskMappingDao][deleteUserTask][error]" + e.getMessage());
		}
		return PMCConstant.FAILURE;
	}

	@SuppressWarnings("unchecked")
	public List<String> getTaskOfSubstitutingUser(String SubstitutedUser, String SubstitutingUser) {
		List<String> taskList = null;
		try {
			String queryString = " SELECT TASK_ID  FROM USER_TASK_MAPPING WHERE upper(SUBSTITUTING_USER)=upper('"
					+ SubstitutingUser + "') GROUP BY TASK_ID HAVING COUNT(TASK_ID)<2";
			Query query = this.getSession().createSQLQuery(queryString);
			taskList = query.list();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][PMC][UserTaskMappingDao][getTaskOfSubstitutingUser][error]" + e.getMessage());
		}
		return taskList;
	}

	public String getSendBackUser(String task, String userId, String type) {

		try {
			String queryString = " SELECT top 1 substituted_User FROM USER_TASK_MAPPING WHERE upper(SUBSTITUTING_USER)=upper('"
					+ userId + "')  and task_id='" + task + "' and task_type='" + type + "' ";
			Query query = this.getSession().createSQLQuery(queryString);

			return (String) query.list().get(0);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Workbox][PMC][UserTaskMappingDao][getSendBackUser][error]" + e.getMessage());
		}
		return null;
	}

	public Boolean deletedSubstitutingTask(String userId, String eventId) {
		try {
			Session session = getSession();
			Transaction tx = session.beginTransaction();
			String queryString = "DELETE FROM USER_TASK_MAPPING "
					+ "WHERE TASK_ID = '"+eventId+"' AND SUBSTITUTING_USER = '"+userId+"'";
			Query query = session.createSQLQuery(queryString);
			Integer count = query.executeUpdate();
			
			tx.commit();
			session.close();
			return count>0?true:false;
		} catch (Exception e) {
			System.err.println("[WBP-Dev] Deleting from User task Mapping" + e.getMessage());
		}
		return false;
		
	}
}