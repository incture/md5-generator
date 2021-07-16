package oneapp.incture.workbox.demo.inbox.dao;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.security.um.user.User;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.inbox.dto.PinnedTaskDto;
import oneapp.incture.workbox.demo.inbox.entity.PinnedTaskDo;
import oneapp.incture.workbox.demo.inbox.entity.PinnedTaskDoPK;

@Repository("PinnedTaskDao")
//////@Transactional
public class PinnedTaskDao {

	@Autowired
	SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public ResponseMessage createPinnedTask(PinnedTaskDto pinnedTaskDto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setMessage(PMCConstant.STATUS_FAILURE);
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode("1");
		try{
			
			
			PinnedTaskDo pinnedTaskDo = new PinnedTaskDo();
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction(); 
			pinnedTaskDo.setPinnedTaskDoPK(new PinnedTaskDoPK(pinnedTaskDto.getPinnedTaskId(), token.getLogonName()));
			session.saveOrUpdate(pinnedTaskDo);
			session.flush();
			session.clear();
			tx.commit();
			session.close();
			
			responseDto = new ResponseMessage();
			responseDto.setMessage(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode("0");
			
		}catch (Exception e) {
			e.getMessage();
		}
				
		return responseDto;
	}

	public ResponseMessage deletePinnedTask(PinnedTaskDto pinnedTaskdto,Token token) {
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setMessage(PMCConstant.STATUS_FAILURE);
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode("1");
		try{
//			User user = UserManagementUtil.getLoggedInUser();
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query deleteTask = session.createSQLQuery("DELETE FROM PINNED_TASK "
					+ "WHERE PINNED_TASK_ID = '"+pinnedTaskdto.getPinnedTaskId()+"' AND USER_ID = '"+token.getLogonName()+"'");
			deleteTask.executeUpdate();
			tx.commit();
			session.close();
			responseDto = new ResponseMessage();
			responseDto.setMessage(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatus(PMCConstant.STATUS_SUCCESS);
			responseDto.setStatusCode("0");
		}catch (Exception e) {
			e.getMessage();
		}
		return responseDto;
	}

}
