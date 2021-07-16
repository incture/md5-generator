package oneapp.incture.workbox.demo.adapterJira.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapterJira.dto.JiraFieldGenericDto;
import oneapp.incture.workbox.demo.adapterJira.util.ApproveTaskInJira;
import oneapp.incture.workbox.demo.adapterJira.util.JiraConstants;
import oneapp.incture.workbox.demo.adapterJira.util.RestUserJira;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskEventsDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;



@Repository
//@Transactional
public class JiraIssuesDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	RestUserJira restUserJira;
	
	@Autowired
	private TaskOwnersDao taskOwnersDao;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public String getProcessName(String eventId) {
		List<String> result = (List<String>) this.getSession().createSQLQuery("Select proc_name from task_events " + " where event_id = '" + eventId + "'").list();
		System.err.println(result);
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	public String getIssueName(String taskId) {
		String query = "Select attr_value from custom_attr_values " + " where key = 'Issue' and task_id = '" + taskId + "'";
		System.err.println(query);
		List<String> result = (List<String>) this.getSession().createSQLQuery(query).list();
		System.err.println(result);
		return result.get(0);
	}
	
	public String updateState(String stateId, String eventId, JiraFieldGenericDto jiraFieldGenericDto) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		TaskEventsDo taskEventsDo = (TaskEventsDo) this.getSession().get(TaskEventsDo.class, eventId);
		
		try {
		taskEventsDo.setStatus("COMPLETED");
		taskEventsDo.setCompletedAt(ServicesUtil.convertAdminFromStringToDate(ServicesUtil.getUtcTime()));
		this.getSession().update(taskEventsDo);
		
		String issueName = getIssueName(eventId);
		RestResponse statusResponse = restUserJira.callGetResponse(JiraConstants.baseUrl + "/issue/" + issueName + "/transitions?transitionId=" + stateId);
		JSONObject statusObject = (JSONObject) statusResponse.getResponseObject();
		JSONArray statusArr = statusObject.getJSONArray("transitions");
		String statusObj = ((JSONObject)statusArr.get(0)).getString("name");
		String updateQuery = "update custom_attr_values set attr_value = '" + statusObj + "' where key = 'Status' and task_id = '" + eventId + "'";
		System.err.println(updateQuery);
		this.getSession().createSQLQuery(updateQuery).executeUpdate();		
		
		String requestUrl = null;
		String payload = null;
		HttpResponse response = null;
		ApproveTaskInJira approveTaskInJira = new ApproveTaskInJira();
		String processName = getProcessName(eventId);
		
		requestUrl = JiraConstants.baseUrl + "/issue/" + processName + "/transitions?expand=transition.fields";
		System.err.println("[WBP-Dev]JiraIssuesDao.updateResolution()  requestUrl : " + requestUrl);
		payload =  "{\"fields\":{\"resolution\":{\"name\":\"" + jiraFieldGenericDto.getResolution() + "\"}}" + "," + "\"transition\":{\"id\":\"" + stateId + "\"}}";
		System.err.println("[WBP-Dev]JiraIssuesDao.updateResolution()  payload : " + payload);
		response = (HttpResponse) approveTaskInJira.approveTask(requestUrl, payload);
		System.err.println(response);
		
		requestUrl = JiraConstants.baseUrl + "/issue/" + processName;
		System.err.println("[WBP-Dev]JiraIssuesDao.updateDefaultRootCause()  requestUrl : " + requestUrl);
		payload = "{\"fields\":{\"customfield_10029\":{\"value\": \""+ jiraFieldGenericDto.getDefaultRootCauseName() +"\", \"id\":\"" + jiraFieldGenericDto.getDefaultRootCauseId() + "\"}, \"summary\":\"" + jiraFieldGenericDto.getSummary() + "\", \"description\":\""+ jiraFieldGenericDto.getDescription() +"\"}}";
		System.err.println("[WBP-Dev]JiraIssuesDao.updateDefaultRootCause()  payload : " + payload);
		response = (HttpResponse) approveTaskInJira.forwardTask(requestUrl, payload);
		System.err.println(response);
	
		requestUrl = JiraConstants.baseUrl + "/issue/" + processName + "/comment";
		System.err.println("[WBP-Dev]JiraIssuesDao.updateComment()  requestUrl : " + requestUrl);
		payload = "{\"body\": \""+ jiraFieldGenericDto.getComment() +"\"}";
		System.err.println("[WBP-Dev]JiraIssuesDao.updateComment()  payload : " + payload);
		response = (HttpResponse) approveTaskInJira.approveTask(requestUrl, payload);
		System.err.println(response);	
		
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][updateState]Error" + e);
		}
		session.flush();
		session.clear();
		tx.commit();
		session.close();
		return "Updated record successfully";
	}
	
	public String updateAssignee(String taskOwner, String eventId) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Map<String, UserIDPMappingDto> userList = fetchUsers(1);
		TaskOwnersDto taskOwnersDto = new TaskOwnersDto();
		TaskEventsDo taskEventsDo = (TaskEventsDo) this.getSession().get(TaskEventsDo.class, eventId);
		
		try {
		String currentProcessorDisplayName = userList.get(taskOwner).getUserFirstName() + " "
				+ userList.get(taskOwner).getUserLastName();
	
		taskEventsDo.setStatus("RESERVED");
		taskEventsDo.setCurrentProcessor(taskOwner);
		taskEventsDo.setCurrentProcessorDisplayName(currentProcessorDisplayName);
		
		taskOwnersDto.setEventId(eventId);
		taskOwnersDto.setTaskOwner(taskOwner);
		taskOwnersDto.setTaskOwnerDisplayName(currentProcessorDisplayName);
		taskOwnersDto.setOwnerEmail(userList.get(taskOwner).getUserEmail());
		
		this.getSession().update(taskEventsDo);
		taskOwnersDao.saveOrUpdateTaskOwner(taskOwnersDto);
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][updateAssignee]Error" + e);
		}
		session.flush();
		session.clear();
		tx.commit();
		session.close();
		return "Updated assignee successfully";
	}
		
		@SuppressWarnings("unchecked")
		public Map<String, UserIDPMappingDto> fetchUsers(int type) {
			System.err.println("In fetch users");
			UserIDPMappingDto userDetails = null;
			String userofSF = "Select user_login_name,user_email,user_first_name,user_id,user_last_name,jira_id"
					+ " from user_idp_mapping where jira_id is not null";
			Query sfUsersQuery = this.getSession().createSQLQuery(userofSF);
			List<Object[]> userList = sfUsersQuery.list();
			System.err.println(userList);

			Map<String, UserIDPMappingDto> userMapping = new HashMap<>();
			for (Object[] obj : userList) {
				userDetails = new UserIDPMappingDto();
				userDetails.setUserEmail((String) obj[1]);
				userDetails.setUserFirstName((String) obj[2]);
				userDetails.setUserId((String) obj[3]);
				userDetails.setUserLastName((String) obj[4]);
				userDetails.setUserLoginName((String) obj[0]);
				userDetails.setJiraId((String) obj[5]);
				if (type == 0)
					userMapping.put((String) obj[5], userDetails);
				else
					userMapping.put((String) obj[3], userDetails);
			}

			System.err.println(userMapping);
			return userMapping;
		}
}
