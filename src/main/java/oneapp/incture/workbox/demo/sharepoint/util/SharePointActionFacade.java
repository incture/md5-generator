package oneapp.incture.workbox.demo.sharepoint.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;

@Service
public class SharePointActionFacade implements SharePointActionFacadeLocal{

	@Autowired
	PropertiesConstants getProperty;
	@Autowired
	TaskEventsDao taskEventsDao;
	
	@Override
//	////@Transactional
	public ResponseMessage approveData(TaskEventChangeDto taskChange) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(PMCConstant.FAILURE);
		responseMessage.setStatusCode("1");
		responseMessage.setMessage(PMCConstant.FAILURE);
		
		RestResponse restResponse;
		ObjectMapper mapper = new ObjectMapper();
		String res = PMCConstant.FAILURE;
		try {
			String accessToken = TokenGenerator.getAccessTokenFromUserCredentials();
			String eventId = taskChange.getEventId();
			String status = taskChange.getStatus();
			String percentComplete = taskChange.getPercentComplete();
			String processId = taskChange.getProcessId();
			ShareApprovalRequestPayload requestPayload = new ShareApprovalRequestPayload();
			requestPayload.setPercentComplete(percentComplete);
			requestPayload.setStatus(status);

			//String payload = mapper.writeValueAsString(requestPayload);

			String payload = "{\"Status\":\"" + status + "\",\"PercentComplete\":" + percentComplete + "}";

			restResponse = RestUtilSharePoint.callRestService(
					getProperty.getValue("REQUEST_URL_SHAREPOINT") + "/" + processId + "/items/" + eventId + "/fields",
					PMCConstant.SAML_HEADER_SHAREPOINT, payload, "PATCH", "application/json", false, null, null, null,
					accessToken, "Bearer");

			System.err.println("[WBP-Dev]SharepointServiceImpl.updateData() response : " + restResponse.getResponseObject());
			System.err.println("[WBP-Dev]SharepointServiceImpl.updateData() resp code : " + restResponse.getResponseCode() + "  "
					+ restResponse.getHttpResponse());

			// after getting success needs to update table with the new status
			if (restResponse.getResponseCode() == 200) {
				String st = taskEventsDao.updateTaskEventToCompleted(taskChange.getEventId());
				System.err.println("[WBP-Dev]SharepointServiceImpl.updateData() " + st);
				res = PMCConstant.SUCCESS;
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode("0");
				responseMessage.setMessage(PMCConstant.SUCCESS);
			}
			else if(restResponse.getResponseCode() == 401){
				responseMessage.setStatus(PMCConstant.FAILURE);
				responseMessage.setStatusCode("1");
				responseMessage.setMessage("Unauthorized : The user account is disabled");
			}
			
		} catch (Exception e) {
			System.err.println("[WBP-Dev]SharepointServiceImpl.updateData() error " + e);
			
		}
		return responseMessage;
	}

}
