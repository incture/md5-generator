package oneapp.incture.workbox.demo.inbox.sevices;

import java.util.List;

import org.json.JSONObject;

import com.sap.cloud.security.xsuaa.token.Token;
import com.sap.cloud.security.xsuaa.token.XsuaaToken;

import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.inbox.dto.WorkflowResponseDto;




public interface WorkFlowActionFacadeLocal {

	public ResponseMessage taskAction(ActionDto dto,XsuaaToken token);
	public ResponseMessage actionsSync(List<ActionDto> dtos,XsuaaToken token);
	
	public ResponseMessage createWorkFlowInstance(String payload);
	public ResponseMessage createWorkFlowInstanceMap();
	ResponseMessage createWorkFlowInstance(JSONObject payload);
	public WorkflowResponseDto getContextData(String taskId);
}
