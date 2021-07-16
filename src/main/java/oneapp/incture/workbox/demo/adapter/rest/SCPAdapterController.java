package oneapp.incture.workbox.demo.adapter.rest;


import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigurationDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTaskDetailsResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessContextEntity;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.scpadapter.scheduler.EventsUpdateScheduler;
import oneapp.incture.workbox.demo.scpadapter.services.ProcessConfigurationService;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/SCP")
public class SCPAdapterController {

	@Autowired
	EventsUpdateScheduler scpAdapter;
	@Autowired
	ProcessConfigurationService processConfig;

	@RequestMapping(value = "/testController")
	public String check() {
		System.err.println("[WBP-Dev]SCPAdapterController.check()");
		return "Success";
	}

	@RequestMapping(value = "/testAdapter")
	public ResponseMessage testSCPAdapter() {
		System.err.println("[WBP-Dev]SCPAdapterController.testECCAdapter()");
		try {
			scpAdapter.updateWorkFlowEvents();
			return new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS, PMCConstant.UPDATE_SUCCESS);

		} catch (Exception e) {
			System.err.println("[WBP-Dev]SCPAdapterController.check()");

			return new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE, PMCConstant.UPDATE_FAILURE);
		}
	}

	@RequestMapping(value = "/updateCustomAttributes")
	public ResponseMessage updateCustomAttributes() {
		System.err.println("[WBP-Dev]SCPAdapterController.updateCustomAttributes()");
		try {
			scpAdapter.updateCustomAttributes();
			return new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS, PMCConstant.UPDATE_SUCCESS);

		} catch (Exception e) {
			System.err.println("[WBP-Dev]SCPAdapterController.check()");

			return new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE, PMCConstant.UPDATE_FAILURE);
		}
	}
	

	@RequestMapping(value = "/checkForConfiguredProcesses")
	public List<String> checkForNewProcess() {
		
		System.err.println("SCPAdapterController.checkForNewProcess()");
		
		return processConfig.getAllConfiguredProcesses();
		
	}
	

	
	@RequestMapping(value = "/getAllProcesses")
	public ProcessConfigurationDto NewProcess() {
		
		System.err.println("SCPAdapterController.checkForNewProcess()");
		
		return processConfig.newProcesses();
		
	}
	
	//critical
	@RequestMapping(value="/uploadFile",method=RequestMethod.POST,produces={"application/JSON"})
    public ResponseMessage uploadFile(@RequestParam("file") MultipartFile file,@RequestParam(name="name") String processDisplayName,
    		@RequestParam(name="sla",required=false) String sla,@RequestParam(name="origin",required=false) String origin,
    		@RequestParam(name="critical",required=false) String critical) {
	
		ResponseMessage resp=new ResponseMessage();
		try{
		String content = new String(file.getBytes(), StandardCharsets.UTF_8);
		System.err.println("SCPAdapterController.uploadFile()");
		
		 resp=processConfig.configureProcess(content,processDisplayName,sla,origin,critical);
		 System.err.println("SCPAdapterController.uploadFile() file resp : "+resp);
		
		}
		catch(Exception e){
			System.err.println("SCPAdapterController.uploadFile() file error : "+e);
			e.printStackTrace();
		
		}
//		ByteArrayInputStream stream = new   ByteArrayInputStream(file.getBytes());
//		String myString = IOUtils.toString(stream, "UTF-8");
		
		
//		try {
//		    while (it.hasNext()) {
//		        String line = it.nextLine();
//		        // do something with line
//		    }
//		} finally {
//		    LineIterator.closeQuietly(it);
//		}
		return  resp;
    	
    }
	
	@RequestMapping(value="/getProcessDetails",method=RequestMethod.GET)
    public ProcessTaskDetailsResponse getProcessTaskDetails(@RequestParam(name="processName") String processName) {
		ProcessTaskDetailsResponse result=processConfig.getProcessTaskDetails(processName);
		return  result;
    	
    }
	
	@RequestMapping(value="/getProcessContextData",method=RequestMethod.GET)
    public String getProcessContextData(@RequestParam(name="processName") String processName) {
	
		return  processConfig.getContextData(processName);
    	
    }
	
	@RequestMapping(value="/processContextData",method=RequestMethod.GET)
    public ProcessContextEntity processContextData(@RequestParam(name="processName") String processName) {
	
		return  processConfig.processContextData(processName);
    	
    }
	
	@RequestMapping(value="/updateProcessContextData",method=RequestMethod.POST)
    public String processContextData(@RequestBody ProcessContextEntity processContext) {
	
		return  processConfig.updateProcessContextData(processContext);
    	
    }
	
	

}

