package oneapp.incture.workbox.demo.workflow.services;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.workflow.dto.AFEOrderDetail;
import oneapp.incture.workbox.demo.workflow.dto.AdvanceCustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.DropDownRequestDto;
import oneapp.incture.workbox.demo.workflow.dto.ProcessConfigListDto;

public interface WorkflowService {

	CustomProcessCreationDto fetchProcessDetail(String processName, String processType);
	
	ResponseMessage deleteprocess(List<String> processName);

	ResponseMessage processWorkflowCreation(CustomProcessCreationDto customProcessCreation);
	
	ResponseMessage manageProcessWorkflow(AdvanceCustomProcessCreationDto customProcessCreation);


	ResponseMessage updateProcessWorkflow(CustomProcessCreationDto updateProcessDto);

	ProcessConfigListDto getProcess(String processType);
	
	ResponseMessage insertDropDownValues(DropDownRequestDto dropDownValues);
	
	DropDownRequestDto getDropDownValues(String customKey);
	
	ResponseMessage serverStatus(String serverUrl);

	AFEOrderDetail getAFENexusOrder(String processName);

	ResponseMessage updateAfeNexusOrder(String processName,String orderType);
}
