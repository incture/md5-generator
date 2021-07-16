package oneapp.incture.workbox.demo.adapter.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.service.DocumentService;
import oneapp.incture.workbox.demo.document.util.DocumentServiceUtil;
import oneapp.incture.workbox.demo.inbox.dto.WorkflowResponseDto;
import oneapp.incture.workbox.demo.inbox.sevices.WorkFlowActionFacadeLocal;
import oneapp.incture.workbox.demo.scpadapter.util.AdminParse;
import oneapp.incture.workbox.demo.scpadapter.util.Workflow;


@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/workFlow", produces = "application/json")
public class WorkFlowRest {

	@Autowired
	private WorkFlowActionFacadeLocal workFlowActionFacadeLocal;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private AdminParse adminParse;
	@Autowired
	private Workflow workflow;
	@Autowired
	private TaskEventsDao taskEventsDao;
	@Autowired
	DocumentServiceUtil documentServiceUtil;

	@RequestMapping(value = "/instances", method = RequestMethod.POST)
	public ResponseMessage createInstance(@RequestBody String payload) {

		return workFlowActionFacadeLocal.createWorkFlowInstance(payload);
	}
	
	@RequestMapping(value = "/instancesDemo", method = RequestMethod.GET)
	public ResponseMessage createInstancemap() {
		System.err.println("WorkFlowRest.createInstancemap()");

		return workFlowActionFacadeLocal.createWorkFlowInstanceMap();
	}

//	@RequestMapping(value = "/createpoinstance", method = RequestMethod.POST, consumes = "application/json")
//	public ResponseMessage createInstancepo(@RequestBody PurchaseOrderApprovalRequest purchaseOrderApprovalRequest) {
//
//		System.err.println("WorkFlowRest.createInstance() input Data : "
//				+ purchaseOrderApprovalRequest.getPurchaseOrderApproval());
//		ResponseMessage response = new ResponseMessage();
//		response.setMessage(PMCConstant.FAILURE);
//		response.setStatus(PMCConstant.FAILURE);
//		response.setStatusCode(PMCConstant.CODE_FAILURE);
//		List<AttachmentDetail>  attachmentIds = null;
//		
//		try {
//
//			// check for attachemnts
//			if (!ServicesUtil.isEmpty(purchaseOrderApprovalRequest.getAttachments())) {
//				// upload documents
//				DocumentResponseDto respDto = documentService
//						.uploadDocument(purchaseOrderApprovalRequest.getAttachments());
//
//				if (respDto.getResponse().getStatus().equalsIgnoreCase(PMCConstant.FAILURE)) {
//					return respDto.getResponse();
//				} else {
//					// check for attachment ids
//					if (!ServicesUtil.isEmpty(respDto.getAttachmentIds())) {
//						attachmentIds = respDto.getAttachmentIds();
//					} else {
//						attachmentIds = new ArrayList<>();
//					}
//
//				}
//
//			}
//
//			PurchaseOrderApproval createInstance = purchaseOrderApprovalRequest.getPurchaseOrderApproval();
//
//			if (!ServicesUtil.isEmpty(attachmentIds) && !ServicesUtil.isEmpty(createInstance.getContext())) {
//				// add attachment ids to the context data
//				PurchaseOrderApprovalContextData context = createInstance.getContext();
//				context.setAttachmentIds(attachmentIds);
//
//				createInstance.setContext(context);
//			}
//
//			JSONObject payload = new JSONObject(createInstance);
//			System.err.println("WorkFlowRest.createInstance() Json formatted payload : " + payload);
//			response = workFlowActionFacadeLocal.createWorkFlowInstance(payload);
//		} catch (Exception e) {
//			System.err.println("WorkFlowRest.createInstance() error : " + e);
//			response.setMessage(e.toString());
//		}
//
//		return response;
//	}

//	@RequestMapping(value = "/createTEinstance", method = RequestMethod.POST, consumes = "application/json",produces = "application/json")
//	public ResponseMessage createInstancete(@RequestBody TravelAndExpenseApprovalRequest travelAndExpenseApprovalRequest) {
//
//		System.err.println("WorkFlowRest.createInstance() input Data : " + travelAndExpenseApprovalRequest.getTravelAndExpenseApproval());
//		ResponseMessage response = new ResponseMessage();
//		response.setMessage(PMCConstant.FAILURE);
//		response.setStatus(PMCConstant.FAILURE);
//		response.setStatusCode(PMCConstant.CODE_FAILURE);
//
//		List<AttachmentDetail>  attachmentIds = null;
//		try {
//
//			// check for attachemnts
//			if (!ServicesUtil.isEmpty(travelAndExpenseApprovalRequest.getAttachments())) {
//				// upload documents
//				DocumentResponseDto respDto = documentService
//						.uploadDocument(travelAndExpenseApprovalRequest.getAttachments());
//
//				if (respDto.getResponse().getStatus().equalsIgnoreCase(PMCConstant.FAILURE)) {
//					return respDto.getResponse();
//				} else {
//					// check for attachment ids
//					if (!ServicesUtil.isEmpty(respDto.getAttachmentIds())) {
//						attachmentIds = respDto.getAttachmentIds();
//					} else {
//						attachmentIds = new ArrayList<>();
//					}
//
//				}
//
//			}
//
//			TravelAndExpenseApproval createInstance = travelAndExpenseApprovalRequest.getTravelAndExpenseApproval();
//			if (!ServicesUtil.isEmpty(attachmentIds) && !ServicesUtil.isEmpty(createInstance.getContext())) {
//				// add attachment ids to the context data
//				TravelAndExpenseApprovalContextData context = createInstance.getContext();
//				context.setAttachmentIds(attachmentIds);
//
//				createInstance.setContext(context);
//			}
//
//			JSONObject payload = new JSONObject(createInstance);
//			System.err.println("WorkFlowRest.createInstance() Json formatted payload : " + payload);
//			response = workFlowActionFacadeLocal.createWorkFlowInstance(payload);
//		} catch (Exception e) {
//			System.err.println("WorkFlowRest.createInstance() error : " + e);
//			response.setMessage(e.toString());
//		}
//
//		return response;
//	}

	@RequestMapping(value = "/getdocuments", method = RequestMethod.POST, consumes = "application/json")
	public List<AttachmentRequestDto> getDocument(@RequestBody List<String> objectIds){
		System.err.println("WorkFlowRest.getDocument() requested ids : "+objectIds);

		List<AttachmentRequestDto> attachments=new ArrayList<>();
		try{
		for(String id:objectIds){
			attachments.add(documentServiceUtil.getOriginalDocument(id));
		//System.err.println("WorkFlowRest.getDocument() content : "+doc);
		}
		}catch(Exception e){
			System.err.println("WorkFlowRest.getDocument() error : "+e);
		}
		
		return attachments;
	}
	
	@RequestMapping(value="/getContext/{taskId}",method=RequestMethod.GET)
	public WorkflowResponseDto getContextData(@PathVariable String taskId) {
		return workFlowActionFacadeLocal.getContextData(taskId);
	}
	
//	@RequestMapping(value = "/createBusinessProcessInstance", method = RequestMethod.POST)
//	public ResponseMessage createBusinessProcessInstance(@RequestBody BusinessProcessApprovalRequest businessProcessRequest) {
//		System.err.println("WorkFlowRest.createBusinessProcessInstance() payload : "+businessProcessRequest);
//		ResponseMessage response = new ResponseMessage();
//		JSONObject payload = null;
//		List<AttachmentDetail>  attachmentIds = null;
//		try {
//			if(!ServicesUtil.isEmpty(businessProcessRequest.getAttachments())) {
//				DocumentResponseDto respDto = documentService
//						.uploadDocument(businessProcessRequest.getAttachments());
//				
//				if (respDto.getResponse().getStatus().equalsIgnoreCase(PMCConstant.FAILURE)) {
//					return respDto.getResponse();
//				} else {
//					// check for attachment ids
//					if (!ServicesUtil.isEmpty(respDto.getAttachmentIds())) {
//						attachmentIds = respDto.getAttachmentIds();
//					} else {
//						attachmentIds = new ArrayList<>();
//					}
//				}	
//			}
//			if(!ServicesUtil.isEmpty(businessProcessRequest.getAttachments()) && !ServicesUtil.isEmpty(attachmentIds)) {
//				businessProcessRequest.getBusinessProcessApproval().getContext().setAttachmentIds(attachmentIds);
//			}
//			if(ServicesUtil.isEmpty(businessProcessRequest.getBusinessProcessApproval().getContext().getLineItems())) {
//				response.setMessage("Line Items cannot be empty");
//				response.setStatus(PMCConstant.FAILURE);
//				response.setStatusCode(PMCConstant.CODE_FAILURE);
//			}else {
//				//businessProcessDto.getContext().setRequestId(("INV"+ThreadLocalRandom.current().nextInt()+"").substring(0,7));
//				payload = new JSONObject(businessProcessRequest.getBusinessProcessApproval());
//				System.err.println("WorkFlowRest.createBusinessProcessInstance() json payload : "+payload);
//				response = workFlowActionFacadeLocal.createWorkFlowInstance(payload);
//			}
//		}catch(Exception e) {
//			System.err.println("WorkFlowRest.createBusinessProcessInstance() exception : "+e.getMessage());
//			response.setMessage("Internal Server Error");
//			response.setStatus(PMCConstant.FAILURE);
//			response.setStatusCode(PMCConstant.CODE_FAILURE);
//		}
//		
//		return response;
//	}
	
//	@RequestMapping(value = "/updateBusinessProcessContext/{processId}", method = RequestMethod.POST)
//	public ResponseMessage updateBusinessProcessWorkflowContext(@RequestBody BusinessProcessApprovalContextDto businessProcessDto, @PathVariable String processId) {
//		ResponseMessage response = new ResponseMessage();
//		JSONObject payload = null;
//		try {
//			if(ServicesUtil.isEmpty(businessProcessDto)) {
//				response.setMessage("Payload cannot be empty");
//				response.setStatus(PMCConstant.FAILURE);
//				response.setStatusCode(PMCConstant.CODE_FAILURE);
//			}else if(ServicesUtil.isEmpty(processId)){
//				response.setMessage("Process id cannot be empty");
//				response.setStatus(PMCConstant.FAILURE);
//				response.setStatusCode(PMCConstant.CODE_FAILURE);
//			}else {
//				payload = new JSONObject(businessProcessDto);
//				response = workFlowActionFacadeLocal.updateWorkflowContext(payload, processId);
//			}
//		}catch(Exception e) {
//			System.err.println("WorkFlowRest.updateBusinessProcessWorkflowContext() exception e "+e.getMessage());
//			response.setMessage("Internal Server Error");
//			response.setStatus(PMCConstant.FAILURE);
//			response.setStatusCode(PMCConstant.CODE_FAILURE);
//		}
//		
//		return response;
//	}
	
	@RequestMapping(value = "/updateCustomAttributeById", method = RequestMethod.GET)
	public ResponseMessage updateCustomAttributesById(@RequestParam(value = "taskId", required = true) String taskId, 
			@RequestParam(value = "processName", required = true) String processName) {
		
		List<TaskEventsDto> tasks = new ArrayList<TaskEventsDto>();
		TaskEventsDto task = new TaskEventsDto();
		task.setEventId(taskId);
		task.setProcessName(processName);
		tasks.add(task);
		
		ResponseMessage response = new ResponseMessage();
		try {
			response = adminParse.updateCustomAttributes(tasks);
		}catch(Exception e) {
			response.setMessage("Internal Server Error");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		return response;
	}
	
	@RequestMapping(value = "/updateCustomAttribute", method = RequestMethod.POST)
	public ResponseMessage updateCustomAttributes(@RequestBody String payload, @RequestParam(value="processId", required=true) String processId){
		ResponseMessage responseMessage = new ResponseMessage();
		RestResponse response = workflow.updateCustomAttributes(payload, processId);
		
		if(response == null){
			responseMessage.setMessage("Internal Server Error");
			responseMessage.setStatus(PMCConstant.FAILURE);
			responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		else{
			if(response.getResponseCode() >= 200 && response.getResponseCode() <= 207){
				responseMessage.setMessage("Custom Attributes Updated Successfully");
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			}else{
				responseMessage.setMessage("Custom Attribute Updation Failed");
				responseMessage.setStatus(PMCConstant.FAILURE);
				responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
			}
		}
		return responseMessage;
	}
	
	@RequestMapping(value = "/updateContextData", method = RequestMethod.POST)
	public ResponseMessage updateContextData(@RequestBody String payload, @RequestParam(value = "processId", required = true) String processId, 
			@RequestParam(value = "processName", required = true) String processName){
		ResponseMessage responseMessage = new ResponseMessage();
		RestResponse response = workflow.updateContextData(payload, processId, processName);
		
		if(response == null){
			responseMessage.setMessage("Internal Server Error");
			responseMessage.setStatus(PMCConstant.FAILURE);
			responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		else{
			if(response.getResponseCode() >= 200 && response.getResponseCode() <= 207){
				responseMessage.setMessage("Context Data Updated Successfully");
				if("ic_manager_approval_process".equalsIgnoreCase(processName))
				responseMessage.setMessage("Form Updated Successfully");
				
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			}else{
				responseMessage.setMessage("Form Updation Failed");
				responseMessage.setStatus(PMCConstant.FAILURE);
				responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
			}
		}
		return responseMessage;
	}
	
	@RequestMapping(value = "/updateWorkflowContextData", method = RequestMethod.POST)
	public ResponseMessage updateWorkflowContextData(@RequestBody String payload, @RequestParam(value = "processId", required = true) String processId){
		ResponseMessage responseMessage = new ResponseMessage();
		RestResponse response = workflow.updateWorkflowContextData(payload, processId);
		
		if(response == null){
			responseMessage.setMessage("Internal Server Error");
			responseMessage.setStatus(PMCConstant.FAILURE);
			responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		else{
			if(response.getResponseCode() >= 200 && response.getResponseCode() <= 207){
				responseMessage.setMessage("Data Updated Successfully");
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			}else{
				responseMessage.setMessage("Data Updation Failed");
				responseMessage.setStatus(PMCConstant.FAILURE);
				responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
			}
		}
		return responseMessage;
	}
	
	@RequestMapping(value = "/updateContextDataLineItems", method = RequestMethod.POST)
	public ResponseMessage updateContextDataLineItems(@RequestBody String payload, @RequestParam(value = "processId", required = true) String processId){
		ResponseMessage responseMessage = new ResponseMessage();
		RestResponse response = workflow.updateContextDataLineItems(payload, processId);
		
		if(response == null){
			responseMessage.setMessage("Internal Server Error");
			responseMessage.setStatus(PMCConstant.FAILURE);
			responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		else{
			if(response.getResponseCode() >= 200 && response.getResponseCode() <= 207){
				responseMessage.setMessage("Context Data Updated Successfully");
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			}else{
				responseMessage.setMessage("Context Data Updation Failed");
				responseMessage.setStatus(PMCConstant.FAILURE);
				responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
			}
		}
		return responseMessage;
	}
	
	/*@RequestMapping(value = "/checkTasksExistence", method = RequestMethod.GET)
	public GenericResponseDto checkTasksExistence(@RequestParam(value = "taskId", required = true) String taskId,
			@RequestParam(value = "caseId", required = true) String caseId,
			@RequestParam(value = "processName", required = true) String processName){
		return taskEventsDao.checkForTasksExistence(taskId, caseId, processName);
	}*/
}
