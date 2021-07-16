package oneapp.incture.workbox.demo.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_salesforce.services.SalesforceService;
import oneapp.incture.workbox.demo.adapter_salesforce.util.SalesforceConstant;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/salesforce",produces="application/json")
public class SalesforceController {

	@Autowired 
	SalesforceService salesforceService;
	
	@RequestMapping(value = "/addData",method=RequestMethod.GET)
	public ResponseMessage addData()
	{
		
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(SalesforceConstant.FAILURE);
		resp.setStatus(SalesforceConstant.STATUS_CODE_FAILURE);
		resp.setStatusCode(SalesforceConstant.STATUS_CODE_FAILURE);
		
		try{
			
			String res = salesforceService.createData();
			if(res.equals(SalesforceConstant.SUCCESS)){
				resp.setMessage(SalesforceConstant.SUCCESS);
				resp.setStatus(SalesforceConstant.STATUS_CODE_SUCCESS);
				resp.setStatusCode(SalesforceConstant.STATUS_CODE_SUCCESS);
			}
		}catch (Exception e) {
			
			System.err.println("[WBP-Dev]SalesforceController.addData() error : "+e);
		}
		
		return resp;
		
	}
}
