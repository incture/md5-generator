package oneapp.incture.workbox.demo.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.workflow.dto.AFEOrderDetail;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.DropDownRequestDto;
import oneapp.incture.workbox.demo.workflow.dto.ProcessConfigListDto;
import oneapp.incture.workbox.demo.workflow.services.WorkflowService;

@Controller
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "workbox/customProcess", produces = "application/json")
@PropertySource(value = { "classpath:application.properties" })
public class WorkflowController {

	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private Environment environment;

	@RequestMapping(value = "/processWorkflowCreation", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage processWorkflowCreation(@RequestBody CustomProcessCreationDto customProcessCreation) {
		return workflowService.processWorkflowCreation(customProcessCreation);
	}

	@RequestMapping(value = "/getAttributes/{processName}", method = RequestMethod.GET)
	@ResponseBody
	public CustomProcessCreationDto fetchProcessDetail(@PathVariable String processName,
			@RequestParam("processType") String processType) {
		return workflowService.fetchProcessDetail(processName, processType);
	}

	@RequestMapping(value = "/updateProcess", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage updateProcessWorkflow(@RequestBody CustomProcessCreationDto updateProcessDto) {
		return workflowService.updateProcessWorkflow(updateProcessDto);
	}

	@RequestMapping(value = "/deleteProcess", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage deleteProcess(@RequestBody List<String> processName) {
		return workflowService.deleteprocess(processName);
	}

	@RequestMapping(value = "/getProcess", method = RequestMethod.GET)
	@ResponseBody
	public ProcessConfigListDto getProcess(@RequestParam(value = "processType", required = false) String processType) {
		return workflowService.getProcess(processType);
	}

	@RequestMapping(value = "/getDropDownValues", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage insertDropDownValues(@RequestBody DropDownRequestDto dropDownValues) {

		return workflowService.insertDropDownValues(dropDownValues);
	}

	@RequestMapping(value = "/getDropDownValues/{customKey}", method = RequestMethod.GET)
	@ResponseBody
	public DropDownRequestDto getDropDownValues(@PathVariable String customKey) {

		return workflowService.getDropDownValues(customKey);
	}
	
	@RequestMapping(value = "/getAFENexusOrder", method = RequestMethod.GET)
	@ResponseBody
	public AFEOrderDetail getAFENexusOrder(@RequestParam(value = "processName", required = false) String processName) {
		return workflowService.getAFENexusOrder(processName);
	}
	
	@RequestMapping(value = "/updateAfeNexusOrder", method = RequestMethod.GET)
	@ResponseBody
	public ResponseMessage updateAfeNexusOrder(@RequestParam (value = "processName", required = false) String processName,@RequestParam (value = "orderType", required = false) String orderType) {

		return workflowService.updateAfeNexusOrder(processName,orderType);
	}
	
	@RequestMapping(value = "/serverStatus", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseMessage serverStatus() {
		String serverUrl = environment.getRequiredProperty("JAVA_URL");
		return workflowService.serverStatus(serverUrl);

	}

}
