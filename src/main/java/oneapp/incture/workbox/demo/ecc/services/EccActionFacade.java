package oneapp.incture.workbox.demo.ecc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_base.util.UserManagementUtil;
import oneapp.incture.workbox.demo.ecc.dto.POActionDto;
import oneapp.incture.workbox.demo.ecc.dto.PRActionDto;
import oneapp.incture.workbox.demo.ecc.eccadapter.util.EccAdapterConstant;
import oneapp.incture.workbox.demo.ecc.eccadapter.util.SCPRestUtilNew;
import com.sap.security.um.user.User;

@Component
public class EccActionFacade {

	@Autowired
	TaskEventsDao taskEvents;
	
	public ResponseMessage approveECCTask(ActionDto actionDto,ActionDtoChild
			childDto, String url, String processType) {
		/*
		 * String reqPayload = "";
		 *
		 * Gson gson = new
		 * GsonBuilder().excludeFieldsWithoutExposeAnnotation().create(); if
		 * (processType.equalsIgnoreCase(PMCConstant.PURCHASE_ORDER)) {
		 * ECCActionDto eccActionDto = new ECCActionDto();
		 * eccActionDto.setAction(actionDto.getAction());
		 * eccActionDto.setPurchaseOrder(actionDto.getPurchaseOrder());
		 * eccActionDto.setPoRelCode(actionDto.getPoRelCode()); reqPayload =
		 * gson.toJson(eccActionDto); } else if
		 * (processType.equalsIgnoreCase(PMCConstant.PURCHASE_REQUISITION)) {
		 * PRActionDto prActionDto = new PRActionDto();
		 * prActionDto.setAction(actionDto.getAction());
		 * prActionDto.setPurchaseReq(actionDto.getPurchaseReq());
		 * prActionDto.setprRelCode(actionDto.getPrRelCode()); reqPayload =
		 * gson.toJson(prActionDto); }
		 *
		 *
		 *
		 * url = PMCConstant.CC_VIRTUAL_HOST + url;
		 */

		/*
		 * RestResponse restResponse = SCPRestUtil.callRestServiceECC(url, null,
		 * reqPayload, PMCConstant.HTTP_METHOD_POST,
		 * PMCConstant.APPLICATION_JSON, false, "fetch", PMCConstant.USERID,
		 * PMCConstant.PASSWORD, null, null, null, "localhost", 20003, "", "");
		 */

		// RestResponse restResponse = SCPRestUtil.callRestService(url, null,
		// reqPayload, PMCConstant.HTTP_METHOD_POST,
		// PMCConstant.APPLICATION_JSON, false, null, "BOPF1", "Dec@200", null,
		// null);

		// System.err.println("[WBP-Dev][Workbox] [approveECCTask] restResponse : " +
		// restResponse);
		// System.err.println("[WBP-Dev][Workbox] [approveECCTask] restResponse : " +
		// restResponse);

		/*
		 * if (!ServicesUtil.isEmpty(restResponse) &&
		 * restResponse.getResponseCode() >= 200 &&
		 * restResponse.getResponseCode() <= 207) {
		 */
		//Commented By Sourav
		/*ResponseMessage message = new ResponseMessage();
 message.setMessage("task " + actionDto.getAction() + " " +
 PMCConstant.FAILURE);
 message.setStatus(PMCConstant.FAILURE);
 message.setStatusCode(PMCConstant.CODE_FAILURE);

 taskEvents.updateTaskEventListToCompleted(actionDto.getInstanceList());

 ParseEccTask eccTask = new ParseEccTask();
 eccTask.parseSFTasks("TS20000166,TS00007986");

 message.setMessage("task " + actionDto.getAction() + " " +
 PMCConstant.SUCCESS);
 message.setStatus(PMCConstant.SUCCESS);
 message.setStatusCode(PMCConstant.CODE_SUCCESS);
 // }
// <<<<<<< HEAD
// =======
 return message;*/
		ResponseMessage responseMessage = new ResponseMessage();
		if(!ServicesUtil.isEmpty(childDto.getInstanceId())) {
			String payload = "";
			String response = "";
			Gson gson = new Gson();
			try {
				String instance =childDto.getInstanceId();

				if(childDto.getProcessType().equalsIgnoreCase(PMCConstant.PURCHASE_REQUISITION))
				{

					if(childDto.getActionType()!=null && childDto.getActionType().equalsIgnoreCase(EccAdapterConstant.ACTION_TYPE_APPROVE)){
						childDto.setAction(EccAdapterConstant.ACTION_APPROVE);
					}else if(childDto.getActionType()!=null && childDto.getActionType().equalsIgnoreCase(EccAdapterConstant.ACTION_TYPE_REJECT)){
						childDto.setAction(EccAdapterConstant.ACTION_REJECT);
					}
					
					PRActionDto prActionDto = new
							PRActionDto(childDto.getProcessId(),childDto.getBnfPo(),
									childDto.getRelCode(),childDto.getAction());
					payload = gson.toJson(prActionDto);
					String actionUrl = url + "(Banfn='"+childDto.getProcessId()+"')";

					response = completeECCAction(payload,actionUrl);
					
					if (!actionDto.getIsChatbot()) {
						User user = UserManagementUtil.getLoggedInUser();
						System.err.println("[WBP-Dev]user : " + user.toString());
						actionDto.setUserId(user.getName());
					}
					
					System.err.println("[WBP-Dev][Final]user : " + actionDto.getUserId());
					
					if(response.equalsIgnoreCase(PMCConstant.SUCCESS)) {

						String result = taskEvents.updateToCompleted(instance,
								actionDto.getUserId()!=null?actionDto.getUserId():null,
										actionDto.getUserDisplay()!=null?actionDto.getUserDisplay():null);

						taskEvents.updateTaskComment(instance, actionDto.getUserId(),
								childDto.getComment(), childDto.getActionType());

						if(result.equalsIgnoreCase(PMCConstant.SUCCESS)) {
							responseMessage.setMessage(PMCConstant.APPROVED_SUCCESS);
							responseMessage.setStatus(PMCConstant.SUCCESS);
							responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
						}
						else {
							responseMessage.setMessage(result);
							responseMessage.setStatus(PMCConstant.FAILURE);
							responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
						}
					}
					else {
						responseMessage.setMessage("Task Approval Failed");
						responseMessage.setStatus(PMCConstant.FAILURE);
						responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
					}
				}else if(childDto.getProcessType().equalsIgnoreCase(PMCConstant.PURCHASE_ORDER)) {
					
					if(childDto.getActionType()!=null && childDto.getActionType().equalsIgnoreCase(EccAdapterConstant.ACTION_TYPE_APPROVE)){
						childDto.setAction(EccAdapterConstant.ACTION_APPROVE);
					}else if(childDto.getActionType()!=null && childDto.getActionType().equalsIgnoreCase(EccAdapterConstant.ACTION_TYPE_REJECT)){
						childDto.setAction(EccAdapterConstant.ACTION_REJECT);
					}
					
					POActionDto poActionDto=new POActionDto(childDto.getProcessId(),childDto.getRelCode(),childDto.getAction());
					payload = gson.toJson(poActionDto);
					String actionUrl = url + "('"+childDto.getProcessId()+"')";
					
					response = completeECCAction(payload,actionUrl);
					
					if (!actionDto.getIsChatbot()) {
						User user = UserManagementUtil.getLoggedInUser();
						System.err.println("[WBP-Dev]user : " + user.toString());
						actionDto.setUserId(user.getName());
					}
					
					System.err.println("[WBP-Dev][Final]user : " + actionDto.getUserId());
					if(response.equalsIgnoreCase(PMCConstant.SUCCESS)) {
						
						String result = taskEvents.updateToCompleted(instance,
								actionDto.getUserId()!=null?actionDto.getUserId():null,
										actionDto.getUserDisplay()!=null?actionDto.getUserDisplay():null);
						
						taskEvents.updateTaskComment(instance, actionDto.getUserId(), childDto.getComment(), childDto.getActionType());
						
						if(result.equalsIgnoreCase(PMCConstant.SUCCESS)) {
							responseMessage.setMessage(PMCConstant.APPROVED_SUCCESS);
							responseMessage.setStatus(PMCConstant.SUCCESS);
							responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
						}
						else {
							responseMessage.setMessage(result);
							responseMessage.setStatus(PMCConstant.FAILURE);
							responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
						}
					}
					else {
						responseMessage.setMessage("Task Approval Failed");
						responseMessage.setStatus(PMCConstant.FAILURE);
						responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
					}
				}

			}catch(Exception e) {
				System.err.println("[WBP-Dev]WorkFlowActionFacade.approveECCTask() exception :"+e.getMessage());
				responseMessage.setMessage("FAILURE DUE TO EXCEPTION");
				responseMessage.setStatus(PMCConstant.FAILURE);
				responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
			}

		}

		return responseMessage;

	}

	public String completeECCAction(String payload, String url) {
		String message = "";
		String user = "bopf2";
		String pass = "Welcome@1234";
		try {
			System.err.println("[WBP-Dev]WorkFlowActionFacade.completeECCAction() payload : " +
					payload);
			System.err.println("[WBP-Dev]WorkFlowActionFacade.completeECCAction() URL : " + url);

			RestResponse restResponse = SCPRestUtilNew.callECCRestService(url, null,
					payload,
					PMCConstant.HTTP_METHOD_PUT, PMCConstant.APPLICATION_JSON, true, "fetch",
					user, pass, null, null,
					null, "localhost", 20003);

			System.err.println("[WBP-Dev]WorkFlowActionFacade.completeECCAction() rest response :" + restResponse);

			if (restResponse.getResponseCode() >= 200 && restResponse.getResponseCode()
					<= 207) {
				message = PMCConstant.SUCCESS;
			} else {
				message = PMCConstant.FAILURE;
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]WorkFlowActionFacade.completeECCAction() exception : " +
					e.getMessage());
		}
		// >>>>>>> branch 'workbox-spring-demo2' of
		// https://github.com/incture/cw-workbox.git
		return message;

	}
	
	public String updateCustomAttributes(String payload, String url) {
		String message = "";
		String user = "bopf2";
		String pass = "Welcome@1234";
		try {
			System.err.println("[WBP-Dev]WorkFlowActionFacade.updateCustomAttributes() payload : " +
					payload);
			System.err.println("[WBP-Dev]WorkFlowActionFacade.updateCustomAttributes() URL : " + url);

			RestResponse restResponse = SCPRestUtilNew.callECCRestService(url, null,
					payload,
					PMCConstant.HTTP_METHOD_POST, PMCConstant.APPLICATION_JSON, true, "fetch",
					user, pass, null, null,
					null, "localhost", 20003);

			System.err.println("[WBP-Dev]WorkFlowActionFacade.completeECCAction() rest response :" + restResponse);

			if (restResponse.getResponseCode() >= 200 && restResponse.getResponseCode()
					<= 207) {
				message = PMCConstant.SUCCESS;
			} else {
				message = PMCConstant.FAILURE;
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]WorkFlowActionFacade.completeECCAction() exception : " +
					e.getMessage());
		}
		// >>>>>>> branch 'workbox-spring-demo2' of
		// https://github.com/incture/cw-workbox.git
		return message;

	}

	public String uploadAttachment(String payload, String url) {
		
		String message = "";
		String user = "bopf2";
		String pass = "Welcome@1234";
		try {
			System.err.println("[WBP-Dev]WorkFlowActionFacade.uploadAttachment() payload : " +
					payload);
			System.err.println("[WBP-Dev]WorkFlowActionFacade.uploadAttachment() URL : " + url);

			RestResponse restResponse = SCPRestUtilNew.callECCRestService(url, null,
					payload,
					PMCConstant.HTTP_METHOD_PUT, PMCConstant.APPLICATION_VND, true, "fetch",
					user, pass, null, null,
					null, "localhost", 20003);

			System.err.println("[WBP-Dev]WorkFlowActionFacade.uploadAttachment() rest response :" + restResponse);

			if (restResponse.getResponseCode() >= 200 && restResponse.getResponseCode()
					<= 207) {
				message = PMCConstant.SUCCESS;
			} else {
				message = PMCConstant.FAILURE;
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]WorkFlowActionFacade.uploadAttachment() exception : " +
					e.getMessage());
		}
		// >>>>>>> branch 'workbox-spring-demo2' of
		// https://github.com/incture/cw-workbox.git
		return message;
	}

}
