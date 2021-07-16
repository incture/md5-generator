package oneapp.incture.workbox.demo.adapter.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.PRUpdateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.ecc.eccadapter.ECCAdapter;
import oneapp.incture.workbox.demo.ecc.services.ParseEccTask;


@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "workbox/ECC")
public class ECCAdapterController {

	@Autowired
	ECCAdapter eccAdapter;
	@Autowired
	ParseEccTask eccTask;
	
	@RequestMapping(value = "/testController")
	public String check(){
		System.err.println("[WBP-Dev]ECCAdapterTestController.check()");
		return "Success";
	}
	
	@RequestMapping(value = "/testAdapter")
	public ResponseMessage testECCAdapter(){
		System.err.println("[WBP-Dev]ECCAdapterTestController.testECCAdapter()");
		
		return eccAdapter.parseDataFromECC("TS00007986", 0, 0,null);
	}
	

	

	@RequestMapping(value = "/updateTask", method = RequestMethod.GET)
	public Object updateData() {
		return eccTask.parseSFTasks("TS20000166,TS00007986");

	}
	
	//test the update service 
		@RequestMapping(value = "/updatePRDetails", method = RequestMethod.POST, produces = "application/json")
		public ResponseMessage updateECCData(@RequestBody PRUpdateDto updateRequestDto){
			System.err.println("ECCAdapterTestController.updateECCData() request paylod ; "+updateRequestDto);
			return eccAdapter.updatePRDetails(updateRequestDto);
		}
}

