package oneapp.incture.workbox.demo.adapter_salesforce.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.ApproveRequestDto;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.RequestDto;
import oneapp.incture.workbox.demo.adapter_salesforce.util.ApproveTaskInSalesforce;
import oneapp.incture.workbox.demo.adapter_salesforce.util.SalesforceConstant;

@Component
public class SalesforceActionFacade {

	@Autowired
	ApproveTaskInSalesforce salesforceUtil;

	@Autowired
	TaskEventsDao taskEvents;

	@Autowired
	ProcessEventsDao processEvents;

	public ResponseMessage completeSalesforceTask(String instanceId, String comment, String actionType) {
		List<String> processList = null;
		ResponseMessage responseDto = new ResponseMessage();
		responseDto.setMessage(PMCConstant.STATUS_FAILURE);
		responseDto.setStatus(PMCConstant.STATUS_FAILURE);
		responseDto.setStatusCode("1");

		try{
			ApproveRequestDto requestDto = new ApproveRequestDto();
			List<RequestDto> requestDtoList = new ArrayList<>();
			processList = new ArrayList<>();
			RequestDto approveDto = new RequestDto();
			approveDto.setActionType(actionType);
			approveDto.setComments(comment);
			String processId = taskEvents.getProcessIdUsingTaskId(instanceId);
			System.err.println("[WBP-Dev][Salesforce][ProcessId]"+processId);
			Object workItemResponse = salesforceUtil.getWorkItem(SalesforceConstant.REST_URL_SALESFORCE+
					SalesforceConstant.GET_WORKITEM_ID+processId+"'");
			processList.add(processId);

			JSONObject workItem = (JSONObject) ((JSONObject)workItemResponse).getJSONArray("records").get(0);
			approveDto.setContextId(workItem.getString("Id"));

			requestDtoList.add(approveDto);

			requestDto.setRequests(requestDtoList);

			System.err.println("[WBP-Dev][Salesforce][ApproveRequest]"+requestDto);

			Object response = salesforceUtil.approveTask(requestDto);
			System.err.println("[WBP-Dev][Salesforce][approveResponse]"+response);

			if(((JSONObject)response).getBoolean("success")){
				taskEvents.updateTaskEventListToCompleted(instanceId);
				processEvents.updateProcessListToCompleted(processList);
			}

			responseDto.setMessage(SalesforceConstant.SUCCESS);
			responseDto.setStatus(SalesforceConstant.SUCCESS);
			responseDto.setStatusCode("0");
		}catch (Exception e) {
			System.err.println("[WBP-Dev][SalesForce][Action]Error"+e.getMessage());
		}

		return responseDto;
	}
}
