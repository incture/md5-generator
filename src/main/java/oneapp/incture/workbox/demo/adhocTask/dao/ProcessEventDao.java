package oneapp.incture.workbox.demo.adhocTask.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.RequestIdDto;
import oneapp.incture.workbox.demo.adhocTask.util.ProcessDescDto;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.adhocTask.util.TimeZoneConvertion;

@Repository("adhocProcessEventDao")
public class ProcessEventDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	@Autowired
	private TimeZoneConvertion timeZoneConvertion;

	public RequestIdDto getRequestId(String actionType, String processName, String resubmitRequestId) {

		RequestIdDto requestIdDto = null;
		String requestId = null;
		Integer id = null;
		String reqIdString = null;
		Object[] requestDetail = null;

		if (actionType.equals("Submit") && ServicesUtil.isEmpty(resubmitRequestId)) {

			requestDetail = getRequestDetail(processName);

			if (requestDetail == null) {

				Query reuestIdQry = getSession().createSQLQuery(
						"select process_request_id from " + "process_config_tb where process_name = :processName ");
				reuestIdQry.setParameter("processName", processName);

				requestId = (String) (reuestIdQry.uniqueResult()) + "0001";

				id = 1;
			} else {

				reqIdString = requestDetail[0].toString();
				reqIdString = reqIdString.substring(reqIdString.length() - 4);
				id = Integer.parseInt(reqIdString);
				id = id + 1;
				requestId = requestDetail[1].toString() + String.format("%04d", id);
			}

			requestId = requestId.toString();
		} else if (actionType.equals("Save")) {
			Query qr = getSession().createSQLQuery("select top 1 request_id from process_events"
					+ " where process_id in (select process_id from task_events where status = 'DRAFT') order by request_id desc");
			reqIdString = (String) qr.uniqueResult();

			if (reqIdString == null) {
				requestId = "DR" + "0001";

				id = 1;
			} else {

				reqIdString = reqIdString.substring(reqIdString.length() - 4);
				id = Integer.parseInt(reqIdString);
				id = id + 1;
				requestId = "DR" + String.format("%04d", id);
			}

			requestId = requestId.toString();
		} else if (actionType.equals("Resubmit") && !ServicesUtil.isEmpty(resubmitRequestId)) {
			id = 1;
			requestId = resubmitRequestId.toString();
		}

		requestIdDto = new RequestIdDto();
		requestIdDto.setId(id);
		requestIdDto.setRequestId(requestId);
		requestIdDto.setRequestDetail(requestDetail);
		return requestIdDto;
	}

	private Object[] getRequestDetail(String processName) {
		String processDetail = "select top 1 pe.request_id, pct.process_request_id from process_config_tb pct "
				+ "join process_events pe on pe.name = pct.process_name "
				+ "join task_events te on te.process_id = pe.process_id " + "where " + "pct.process_name = '"
				+ processName + "' and te.status <> 'DRAFT' " + " order by request_id desc";
		Query processDetailQuery = getSession().createSQLQuery(processDetail);
		return (Object[]) processDetailQuery.uniqueResult();
	}

	public ProcessDescDto updateProcessEvents(AttributesResponseDto attributesResponseDto) {

		ProcessDescDto processDescDto = null;
		TimeZoneConvertion timeZoneConvertion = null;
		String requestId = null;
		Object[] requestDetail = null;
		Object[] newRequestDetail = null;
		String reqIdString = null;
		boolean flag = false;

		timeZoneConvertion = new TimeZoneConvertion();

		String requestInfo = "select top 1 pe.request_id, pct.process_request_id,pe.subject, "
				+ " (select event_id from tasksevent where process_id = :processId) " + " from process_config_tb pct "
				+ " join process_events pe on pe.name = pct.process_name "
				+ " where pct.process_name = :processName and pe.request_id not like 'DR%' order by request_id desc ";
		Query requestInfoQry = getSession().createSQLQuery(requestInfo);
		requestInfoQry.setParameter("processId", attributesResponseDto.getProcessId());
		requestInfoQry.setParameter("processName", attributesResponseDto.getProcessName());
		requestDetail = (Object[]) requestInfoQry.uniqueResult();

		if (requestDetail == null) {
			flag = true;
			String newRequestInfo = "select pct.process_request_id,te.event_id,( select subject from process_events "
					+ "where process_id =:processId ) from process_config_tb pct,task_events te "
					+ " where pct.process_name =:processName  and te.process_id = :processId ";
			Query newRequestInfoQry = getSession().createSQLQuery(newRequestInfo);
			newRequestInfoQry.setParameter("processId", attributesResponseDto.getProcessId());
			newRequestInfoQry.setParameter("processName", attributesResponseDto.getProcessName());
			newRequestDetail = (Object[]) (newRequestInfoQry.uniqueResult());
			requestId = newRequestDetail[0].toString() + "0001";
		} else {
			reqIdString = requestDetail[0].toString();
			reqIdString = reqIdString.substring(reqIdString.length() - 4);
			Integer id = Integer.parseInt(reqIdString);
			id = id + 1;
			requestId = requestDetail[1].toString() + String.format("%04d", id);
		}

		requestId = requestId.toString();

		String processString = "update process_events set  status = '" + TaskCreationConstant.NEW + "', "
				+ "request_id = :requestId ,started_at = :startedAt where process_id = :processId";
		Query submitQuery = getSession().createSQLQuery(processString);
		submitQuery.setParameter("requestId", requestId);
		submitQuery.setParameter("startedAt",
				ServicesUtil.convertAdminFromStringToDate(timeZoneConvertion.convertToIst()));
		submitQuery.setParameter("processId", attributesResponseDto.getProcessId());
		submitQuery.executeUpdate();

		processDescDto = new ProcessDescDto();

		if (flag == false) {
			processDescDto.setEventId(requestDetail[3].toString());
			processDescDto.setDescription(requestDetail[2].toString());
		} else {
			processDescDto.setEventId(newRequestDetail[1].toString());
			processDescDto.setDescription(requestDetail[2].toString());
		}
		processDescDto.setProcessName(attributesResponseDto.getProcessName());
		processDescDto.setRequestId(requestId);

		return processDescDto;
	}

	public ResponseMessage deleteDraftProcess(String processId) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query processQry = session.createSQLQuery("delete from process_events where process_id =:processId");
			processQry.setParameter("processId", processId);
			if (processQry.executeUpdate() < 1) {
				resp.setMessage("Process Not found");
			} else {
				resp.setMessage(TaskCreationConstant.SUCCESS);
				resp.setStatus(TaskCreationConstant.SUCCESS);
				resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][DRAFT DELETION] ERROR : " + e.getMessage());
		}

		return resp;
	}

	public void updateStatusInProcessEvents(String event, String status) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String updateQry = "UPDATE PROCESS_EVENTS SET STATUS = '" + status + "'";

		if (status.equals(TaskCreationConstant.COMPLETED.toUpperCase()))
			updateQry += ",COMPLETED_AT = '" + timeZoneConvertion.convertToUTC() + "'";

		updateQry += " WHERE PROCESS_ID = (" + "SELECT PROCESS_ID FROM TASK_EVENTS WHERE EVENT_ID = '" + event + "')";

		Query updatePE = session.createSQLQuery(updateQry);
		updatePE.executeUpdate();
		tx.commit();
		session.close();

	}

	public void updateDescription(String processId, String value) {

		Query updatePE = getSession().createSQLQuery(
				"UPDATE PROCESS_EVENTS SET SUBJECT = '" + value + "' " + "WHERE PROCESS_ID = '" + processId + "' ");
		updatePE.executeUpdate();
	}

	public String getProcessRequestId(String processId) {
		Session session = sessionFactory.openSession();
		Query requestId = session
				.createSQLQuery("SELECT REQUEST_ID FROM PROCESS_EVENTS " + "WHERE PROCESS_ID = '" + processId + "'");
		return (String) requestId.uniqueResult();
	}

	public String getProcessName(String taskId) {
		try {
			String processDetail = "select proc_name from task_events where event_id='" + taskId + "' ";
			// Query processDetailQuery =
			// getSession().createSQLQuery(processDetail);
			// String obj = (String) processDetailQuery.uniqueResult();
			Object obj = getSession().createSQLQuery(processDetail).uniqueResult();

			System.err.println("[getProcessName]" + obj.toString());
			return obj.toString();
		} catch (Exception e) {
			System.err.println("[getProcessName]error" + e.getLocalizedMessage());
		}
		return "";
	}

	public String getStartedBy(String instanceId) {
		Query startedBy = getSession()
				.createSQLQuery("SELECT STARTED_BY_DISP FROM PROCESS_EVENTS " + "WHERE PROCESS_ID"
						+ " in (select process_id from task_events where event_id = '"+instanceId+"')");
		return (String) startedBy.uniqueResult();
	}

}
