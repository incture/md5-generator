package oneapp.incture.workbox.demo.detailpage.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("TaskContentAuditDao")
//@Transactional
public class TaskAuditDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskAuditDto> getTaskAudit(String eventId) {
		List<TaskAuditDto> auditDtos = null;
		TaskAuditDto taskAuditDto = null;
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));
		try{

			String auditQryStr = "SELECT AUDIT_ID,ACTION_TYPE,COMMENT,UPDATED_AT,USER_ID,USER_NAME,SEND_TO_USER,SEND_TO_USER_NAME"
					+ " FROM TASK_AUDIT WHERE EVENT_ID = '"+eventId+"'";
			Query auditQry = this.getSession().createSQLQuery(auditQryStr);
			List<Object[]> audits = auditQry.list();

			if(!ServicesUtil.isEmpty(audits))
			{
				auditDtos = new ArrayList<>();
				for (Object[] obj : audits) {
					taskAuditDto = new TaskAuditDto();
					taskAuditDto.setAuditId(obj[0].toString());
					if(!ServicesUtil.isEmpty(obj[1]))
						taskAuditDto.setAction(obj[1].toString());
					if(!ServicesUtil.isEmpty(obj[2]))
						taskAuditDto.setComment(obj[2].toString());
					taskAuditDto.setUpdatedAt((Date)obj[3]);
					taskAuditDto.setUpdatedAtString(simpleDateFormat1.format(taskAuditDto.getUpdatedAt()));
					taskAuditDto.setUserId(obj[4].toString());
					taskAuditDto.setUserName(ServicesUtil.isEmpty(obj[5])?"":obj[5].toString());
					taskAuditDto.setSendToUser(ServicesUtil.isEmpty(obj[6])?null:obj[6].toString());
					taskAuditDto.setSendToUserName(ServicesUtil.isEmpty(obj[7])?null:obj[7].toString());
					auditDtos.add(taskAuditDto);
				}
			}

		}catch (Exception e) {
			System.err.println("[WBP-DEV] ERROR IN TASK AUDIT"+e.getMessage());
		}
		return auditDtos;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TaskAuditDto> getAuditLogs(String eventId,String signature) {
		List<TaskAuditDto> auditDtos = null;
		TaskAuditDto taskAuditDto = null;
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));
		System.err.println("Task Audit eventId"+eventId);
		System.err.println("Task Audit Signature"+signature);
		
		try{

			String auditQryStr = "SELECT TA.AUDIT_ID,TA.ACTION_TYPE,TA.COMMENT,TA.UPDATED_AT,TA.USER_ID,TA.USER_NAME,"
					+ "TA.SEND_TO_USER,TA.SEND_TO_USER_NAME ,"
					+ "TA.PLATFORM,TA.SIGNATURE_VERIFIED ,"
					+ " TE.COMPLETED_AT,PE.REQUEST_ID ,TA.EVENT_ID"
					+ " FROM TASK_AUDIT TA INNER JOIN TASK_EVENTS TE ON TA.EVENT_ID=TE.EVENT_ID INNER JOIN PROCESS_EVENTS PE ON TE.PROCESS_ID=PE.PROCESS_ID ";
					
			
			boolean signatureFlag=false;
			if(!ServicesUtil.isEmpty(signature) && !signature.equalsIgnoreCase("ALL")){
				
				auditQryStr=auditQryStr+" WHERE TA.SIGNATURE_VERIFIED= '"+signature+"' ";
				signatureFlag=true;
			}
			
			if(signatureFlag && !ServicesUtil.isEmpty(eventId) ){
				
				auditQryStr=auditQryStr+ " AND TA.EVENT_ID = '"+eventId+"' ";
			}else if (!ServicesUtil.isEmpty(eventId)){
				auditQryStr=auditQryStr+ " WHERE TA.EVENT_ID = '"+eventId+"' ";
			}
			
			Query auditQry = this.getSession().createSQLQuery(auditQryStr);
			List<Object[]> audits = auditQry.list();
			System.err.println("Task Audit Details"+audits);
			

			if(!ServicesUtil.isEmpty(audits))
			{
				auditDtos = new ArrayList<>();
				for (Object[] obj : audits) {
					taskAuditDto = new TaskAuditDto();
					taskAuditDto.setAuditId(obj[0].toString());
					if(!ServicesUtil.isEmpty(obj[1]))
						taskAuditDto.setAction(obj[1].toString());
					if(!ServicesUtil.isEmpty(obj[2]))
						taskAuditDto.setComment(obj[2].toString());
					taskAuditDto.setUpdatedAt((Date)obj[3]);
					taskAuditDto.setUpdatedAtString(simpleDateFormat1.format(taskAuditDto.getUpdatedAt()));
					taskAuditDto.setUserId(obj[4].toString());
					taskAuditDto.setUserName(ServicesUtil.isEmpty(obj[5])?"":obj[5].toString());
					taskAuditDto.setSendToUser(ServicesUtil.isEmpty(obj[6])?null:obj[6].toString());
					taskAuditDto.setSendToUserName(ServicesUtil.isEmpty(obj[7])?null:obj[7].toString());
					
					taskAuditDto.setPlatform(ServicesUtil.isEmpty(obj[8])?null:obj[8].toString());
					taskAuditDto.setSignatureVerified(ServicesUtil.isEmpty(obj[9])?null:obj[9].toString());
					
					taskAuditDto.setCompletedAt(ServicesUtil.isEmpty(obj[10])?null:(Date)obj[10]);
					taskAuditDto.setCompletedAtString(ServicesUtil.isEmpty(taskAuditDto.getCompletedAt())?null:simpleDateFormat1.format(taskAuditDto.getCompletedAt()));
					taskAuditDto.setRequestId(ServicesUtil.isEmpty(obj[11])?null:obj[11].toString());
					
					taskAuditDto.setEventId(ServicesUtil.isEmpty(obj[12])?null:obj[12].toString());
					
					auditDtos.add(taskAuditDto);
				}
			}

		}catch (Exception e) {
			System.err.println("[WBP-DEV] ERROR IN TASK AUDIT"+e.getMessage());
		}
		System.err.println("Task Audit full details"+auditDtos);
		
		return auditDtos;
	}
}
