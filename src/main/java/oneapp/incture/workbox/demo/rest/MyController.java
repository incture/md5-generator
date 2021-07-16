package oneapp.incture.workbox.demo.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.inbox.sevices.WorkFlowActionFacade;
import oneapp.incture.workbox.demo.scpadapter.scheduler.EventsUpdateScheduler;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;


@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/mycontroller", produces = "application/json")
public class MyController {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	WorkFlowActionFacade actions;
	
	@Autowired
	SharepointUploadFile sharepointUploadFile;
	
	/*@Autowired
	LogoutFacadeLocal logoutFacade;*/
	
	@Autowired
	private EventsUpdateScheduler eventsUpdateScheduler;

	@RequestMapping(value = "/call", method = RequestMethod.GET)
	public String callURL() {
//		JSONObject jsonObject = RestUtil.callRestService(
//				"https://flpnwc-kbniwmq1aj.dispatcher.hana.ondemand.com/sap/fiori/bpmmyinbox/bpmworkflowruntime/odata/tcm'",
//				null, "GET", "application/json", true, "Fetch");
//		if(jsonObject != null)
//			return jsonObject.toString();
//		return null;
//		Context ctx = null;
//		AuthenticationHeaderProvider authHeaderProvider = null;
//		AuthenticationHeader appToAppSSOHeader = null;
//		try {
//			ctx = new InitialContext();
//			authHeaderProvider = (AuthenticationHeaderProvider) ctx.lookup("java:comp/env/AuthHeaderProvider");
//			appToAppSSOHeader = authHeaderProvider.getAppToAppSSOHeader("https://flpnwc-kbniwmq1aj.dispatcher.hana.ondemand.com/sap/fiori/bpmmyinbox/bpmworkflowruntime/odata/tcm");
//		} catch (Exception ex) {
//			System.err.println("Exception while fetching auth Header Provider : " + ex.getMessage());
//		}
//		WorkFlowActionFasade wfFacade = new WorkFlowActionFasade();
//		return actions.("hello");
		return null;
	}
	
//	@RequestMapping(value = "/saml", method = RequestMethod.GET)
//	public String[] callSAML(@RequestParam("url") String url) {
//		String[] header = new String[2];
//		Context ctx = null;
//		AuthenticationHeaderProvider authHeaderProvider = null;
//		AuthenticationHeader appToAppSSOHeader = null;
//		if(!ServicesUtil.isEmpty(url)) {
//			try {
//				ctx = new InitialContext();
//				authHeaderProvider = (AuthenticationHeaderProvider) ctx.lookup("java:comp/env/AuthHeaderProvider");
//				appToAppSSOHeader = authHeaderProvider.getAppToAppSSOHeader(url);
//			} catch (Exception ex) {
//				System.err.println("Exception while fetching auth Header Provider : " + ex.getMessage());
//			}
//			header[0] = appToAppSSOHeader.getName();
//			header[1] = appToAppSSOHeader.getValue();
//		}
//		return header;
//	}
	
	@RequestMapping(value = "/updateCustomAttributes", method = RequestMethod.GET)
	public String customAttributes() {
		ResponseMessage message=eventsUpdateScheduler.updateCustomAttributes();
		return message.getMessage();
	}
	
//	@RequestMapping(value = "/getUserDetails", method = RequestMethod.GET)
//	public Map<String,String> getUserDetails() {
//	User user = UserManagementUtil.getLoggedInUser();
//	Map<String , String> map=new HashMap<String, String>();
//	map.put("groups", user.getGroups().toString());
//	map.put("", user.getName());
//	map.put("", user.getRoles().toString());
//	map.put("", user.toString());
//	return map;
//	
//	}
	/*@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ResponseMessage logout() {
		return logoutFacade.logout(request);
		
	}*/
	
//	@RequestMapping(value = "/callSaveEvents", method = RequestMethod.GET)
//	public String saveEvents() {
//		eventsUpdateScheduler.updateEvents();
//		return "Success";
//	}
//	
//	@RequestMapping(value = "/callSaveCompletedEvents", method = RequestMethod.GET)
//	public String saveCompletedEvents() {
//		eventsUpdateScheduler.updateCompleteEvents();
//		return "Success";
//	}
	
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
	public DocumentResponseDto uploadFile(@RequestParam String file) throws Exception {
		List<AttachmentRequestDto> attachmentRequestDtos = new ArrayList<AttachmentRequestDto>();
		AttachmentRequestDto attachmentRequestDto = new AttachmentRequestDto();
		
		attachmentRequestDto.setEncodedFileContent(file);
		attachmentRequestDto.setFileName(UUID.randomUUID().toString().replace("-", "") + ".csv");
		attachmentRequestDtos.add(attachmentRequestDto);
		return sharepointUploadFile.uploadFile(attachmentRequestDtos,"fileName");
//		return "Success";
	}
	
	@RequestMapping(value = "/getFile", method = RequestMethod.GET)
	public String getFile(@RequestParam String file) throws Exception {
		return sharepointUploadFile.getFileUsingSharepoint(file);
//		return "Success";
	}
	
}
