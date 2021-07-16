package oneapp.incture.workbox.demo.sharepoint.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;

@Service
public class SharepointServiceImpl implements SharepointService {

	@Autowired
	AdminParseSharepoint adminParseSharepoint;
	@Autowired
	ProcessEventsDao processEventsDao;
	@Autowired
	TaskEventsDao taskEventsDao;
	@Autowired
	TaskOwnersDao taskOwnersDao;
	@Autowired
	CustomAttributeDao customAttrDao;
	@Autowired
	PropertiesConstants getProperty;
	
	@Autowired
	SharepointUploadFile sharepointUploadFile;

	@Override
//	////@Transactional
	public String createData() {
		String res = PMCConstant.FAILURE;
		long start = System.currentTimeMillis();
		try {
			AdminParseResponse response = adminParseSharepoint.parseDetail();
			System.err.println("Payload : " + new Gson().toJson(response));
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][parseDetail]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			processEventsDao.saveOrUpdateProcesses(response.getProcesses());
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][saveOrUpdateProcesses]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskEventsDao.saveOrUpdateTasks(response.getTasks());
			;
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][saveOrUpdateTasks]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskOwnersDao.saveOrUpdateOwners(response.getOwners());
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][saveOrUpdateOwners]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			customAttrDao.addCustomAttributeValue(response.getCustomAttributeValues());
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][addCustomAttributeValue]" + (System.currentTimeMillis() - start));

			res = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println("[WBP-Dev]SharepointServiceImpl.createData() error " + e);
			
		}
		return res;
	}

	@Override
//	////@Transactional
	public String updateData(TaskEventChangeDto taskChange) {
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

			String payload = mapper.writeValueAsString(requestPayload);

			payload = "{\"Status\":\"" + status + "\",\"PercentComplete\":" + percentComplete + "}";

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
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev]SharepointServiceImpl.updateData() error " + e);
			
		}
		return res;
	}
	
	public AttachmentRequestDto getOriginalAttachment(String fileName) {
		String fileContent = sharepointUploadFile.getFileUsingSharepoint(fileName);
		AttachmentRequestDto attachmentRequestDto = new AttachmentRequestDto();
		attachmentRequestDto.setFileName(fileName);
		attachmentRequestDto.setEncodedFileContent(fileContent);
		return attachmentRequestDto;
	}

}
