package oneapp.incture.workbox.demo.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.workflow.dto.AdvanceCustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomProcessCreationDto;
import oneapp.incture.workbox.demo.workflow.services.WorkflowService;

@Controller
@CrossOrigin
@ComponentScan(basePackages = {"oneapp.incture.workbox"})
@RequestMapping(value = "workbox/advanceWorkflow", produces = "application/json")
public class AdvanceWorkflowController {
	
	@Autowired
	private WorkflowService workflowService;
	
	
	@RequestMapping(value = "/manageWorkflow" , method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage processWorkflowCreation(@RequestBody AdvanceCustomProcessCreationDto customProcessCreation) {
		return workflowService.manageProcessWorkflow(customProcessCreation);
	}

}
