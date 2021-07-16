package oneapp.incture.workbox.demo.workflow.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.workflow.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.TeamDetailDto;

@Component
public class ValidateRequest {

	@Autowired
	ProcessConfigDao processConfigDao;

	public ResponseMessage validate(CustomProcessCreationDto customProcessCreation) {

		ResponseMessage responseMessage = new ResponseMessage(); 
		responseMessage.setStatus(WorkflowCreationConstant.FAILURE);
		responseMessage.setStatusCode(WorkflowCreationConstant.STATUS_CODE_FAILURE);
		responseMessage.setMessage(WorkflowCreationConstant.FAILURE);

		if(checkLabels(customProcessCreation))
		{
			responseMessage.setMessage("Error in custom Attribute");
			return responseMessage;
		}
		if(checkTaskName(customProcessCreation))
		{
			responseMessage.setMessage("Error in Task Template");
			return responseMessage;
		}

		if(customProcessCreation.getTeamDetailDto().isEmpty()){
			responseMessage.setMessage("Tasks are Empty");
			return responseMessage;
		}
		
		responseMessage.setStatus(WorkflowCreationConstant.SUCCESS);
		responseMessage.setStatusCode(WorkflowCreationConstant.STATUS_CODE_SUCCESS);
		responseMessage.setMessage(WorkflowCreationConstant.SUCCESS);

		return responseMessage;
	}

	private boolean checkTaskName(CustomProcessCreationDto customProcessCreation) {

		for ( TeamDetailDto obj : customProcessCreation.getTeamDetailDto()) {

			if("".equals(obj.getEventName()) || obj.getEventName().equals(null) ||
					"".equals(obj.getTaskType()) || obj.getTaskType() == null)

			{
				return true;
			}
		}
		return false;
	}

	private boolean checkLabels(CustomProcessCreationDto customProcessCreation) {

		Map<String,String> labelType = new HashMap<>();

		for ( CustomAttributeTemplateDto obj : customProcessCreation.getCustomAttribute()) {

			if(labelType.containsKey(obj.getLabel()) && (labelType.get(obj.getLabel()).equals(obj.getDataType())))
			{	
				return true;
			}

			if(obj.getIsActive().equals(true)){
				labelType.put(obj.getLabel(), obj.getDataType());
			}

			if("".equals(obj.getLabel()) || obj.getLabel() == null || "".equals(obj.getDataType()))
			{
				return true;
			}
		}
		return false;
	}
}
