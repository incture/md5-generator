package oneapp.incture.workbox.demo.adapterJira.services;

import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapterJira.dao.JiraIssuesDao;
import oneapp.incture.workbox.demo.adapterJira.dto.JiraFieldGenericDto;
import oneapp.incture.workbox.demo.adapterJira.util.ApproveTaskInJira;
import oneapp.incture.workbox.demo.adapterJira.util.JiraConstants;
import oneapp.incture.workbox.demo.adapterJira.util.RestUserJira;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.UserIDPMappingDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;


@Service
//@Transactional
public class JiraActionFacade {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	JiraService jiraService;
	
	@Autowired
	JiraIssuesDao jiraIssuesDao;
	
	@Autowired
	RestUserJira restUserJira;
	
	public Session getSession() {
		System.err.println(sessionFactory);
		return sessionFactory.getCurrentSession();
	}

	public ResponseMessage completeTransitionOrForward(ActionDto actionDto, ActionDtoChild actionDtoChild) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(PMCConstant.STATUS_FAILURE);
		responseMessage.setStatus(PMCConstant.STATUS_FAILURE);
		responseMessage.setStatusCode("1");
		
		String requestUrl = null;
		HttpClient client = new DefaultHttpClient();
		String payload = null;
		HttpResponse response = null;
		ApproveTaskInJira approveTaskInJira = new ApproveTaskInJira();
		JiraFieldGenericDto jiraFieldGenericDto = new JiraFieldGenericDto();

		try {
			if ((actionDtoChild.getActionTypeId() == null && actionDtoChild.getSendToUser() != null) || "Forward".equalsIgnoreCase(actionDtoChild.getActionType())) {
				Map<String, UserIDPMappingDto> userList = jiraIssuesDao.fetchUsers(1);
				String processName = jiraIssuesDao.getProcessName(actionDtoChild.getInstanceId());
				requestUrl = JiraConstants.baseUrl + "/issue/" + processName  + "/assignee";
				System.err.println("[WBP-Dev]JiraActionFacade.completeTransitionOrForward()  requestUrl : " + requestUrl);
				payload = "{\"accountId\": \"" + userList.get(actionDtoChild.getSendToUser()).getJiraId() + "\"}";
				System.err.println("[WBP-Dev]JiraActionFacade.completeTransitionOrForward()  payload : " + payload);
				response = (HttpResponse) approveTaskInJira.forwardTask(requestUrl, payload);
				if (response.getStatusLine().getStatusCode() == 204) {
					responseMessage.setStatus("Success");
					responseMessage.setStatusCode("200");
					responseMessage.setMessage("assignee changed successfully");
					String record = jiraIssuesDao.updateAssignee(actionDtoChild.getSendToUser(), actionDtoChild.getInstanceId());
					System.err.println(record);
				} else {
					responseMessage.setStatus("Fail");
					responseMessage.setStatusCode("400");
					responseMessage.setMessage("assignee changing failed");
				}
				
			} 
			else if(actionDtoChild.getActionTypeId() != null && actionDtoChild.getSendToUser() == null) {
				String processName = jiraIssuesDao.getProcessName(actionDtoChild.getInstanceId());
				requestUrl = JiraConstants.baseUrl + "/issue/" + processName  + "/transitions";
				System.err.println("[WBP-Dev]JiraActionFacade.completeTransitionOrForward()  requestUrl : " + requestUrl);
				payload = "{\"transition\":{\"id\":\"" + actionDtoChild.getActionTypeId() + "\"}}";
				System.err.println("[WBP-Dev]JiraActionFacade.completeTransitionOrForward()  payload : " + payload);
				System.err.println(approveTaskInJira);
				response = (HttpResponse) approveTaskInJira.approveTask(requestUrl, payload);
				System.err.println(response);
				if (response.getStatusLine().getStatusCode() == 204) {
					responseMessage.setStatus("Success");
					responseMessage.setStatusCode("200");
					responseMessage.setMessage("state changed successfully");
					String record = jiraIssuesDao.updateState(actionDtoChild.getActionTypeId(), actionDtoChild.getInstanceId(), actionDtoChild.getJiraField());
					System.err.println(record);
				} else {
					responseMessage.setStatus("Fail");
					responseMessage.setStatusCode("400");
					responseMessage.setMessage("state changing failed");
				}
			}
			else if(actionDtoChild.getActionTypeId() != null && actionDtoChild.getSendToUser() != null){
				String processName = jiraIssuesDao.getProcessName(actionDtoChild.getInstanceId());
				requestUrl = JiraConstants.baseUrl + "/issue/" + processName  + "/transitions";
				System.err.println("[WBP-Dev]JiraActionFacade.completeTransitionOrForward()  requestUrl : " + requestUrl);
				payload = "{\"transition\":{\"id\":\"" + actionDtoChild.getActionTypeId() + "\"}}";
				System.err.println("[WBP-Dev]JiraActionFacade.completeTransitionOrForward()  payload : " + payload);
				System.err.println(approveTaskInJira);
				response = (HttpResponse) approveTaskInJira.approveTask(requestUrl, payload);
				System.err.println(response);
				responseMessage.setStatus("Fail");
				responseMessage.setStatusCode("400");
				responseMessage.setMessage("state and assignee changing failed");
				if (response.getStatusLine().getStatusCode() == 204) {
					Map<String, UserIDPMappingDto> userList = jiraIssuesDao.fetchUsers(1);
					requestUrl = JiraConstants.baseUrl + "/issue/" + processName  + "/assignee";
					System.err.println("[WBP-Dev]JiraActionFacade.completeTransitionOrForward()  requestUrl : " + requestUrl);
					payload = "{\"accountId\": \"" + userList.get(actionDtoChild.getSendToUser()).getJiraId() + "\"}";
					System.err.println("[WBP-Dev]JiraActionFacade.completeTransitionOrForward()  payload : " + payload);
					response = (HttpResponse) approveTaskInJira.forwardTask(requestUrl, payload);
					if (response.getStatusLine().getStatusCode() == 204) {
						responseMessage.setStatus("Success");
						responseMessage.setStatusCode("200");
						responseMessage.setMessage("state and assignee changed sucessfully");
						String record1 = jiraIssuesDao.updateAssignee(actionDtoChild.getSendToUser(), actionDtoChild.getInstanceId());
						String record = jiraIssuesDao.updateState(actionDtoChild.getActionTypeId(), actionDtoChild.getInstanceId(), actionDtoChild.getJiraField());
//						String record1 = jiraIssuesDao.updateAssignee(actionDtoChild.getSendToUser(), actionDtoChild.getInstanceId());
						System.err.println(record1);
						System.err.println(record);
						
					}
			}
			}	
			
		} catch (Exception e) {
			System.err.println("[WBP-Dev][JIRA][completeTransitionOrForward]Error" + e);
		}

		return responseMessage;
	}
}
