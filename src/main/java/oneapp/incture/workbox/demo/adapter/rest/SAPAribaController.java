package oneapp.incture.workbox.demo.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.sapAriba.dto.AribaResposeDto;
import oneapp.incture.workbox.demo.sapAriba.service.SapAribaService;
import oneapp.incture.workbox.demo.sapAriba.util.AribaUtil;
import oneapp.incture.workbox.demo.sapAriba.util.SapAribaConstant;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/sapAriba",produces="application/json")
public class SAPAribaController {

	@Autowired
	SapAribaService sapAribaService;
	
	@Autowired
	AribaUtil util;
	
	@RequestMapping(value = "/addData",method=RequestMethod.GET)
	public ResponseMessage addData()
	{
		
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(SapAribaConstant.FAILURE);
		resp.setStatus(SapAribaConstant.STATUS_CODE_FAILURE);
		resp.setStatusCode(SapAribaConstant.STATUS_CODE_FAILURE);
		
		try{
			
			String res = sapAribaService.createData();
			if(res.equals(SapAribaConstant.SUCCESS)){
				resp.setMessage(SapAribaConstant.SUCCESS);
				resp.setStatus(SapAribaConstant.STATUS_CODE_SUCCESS);
				resp.setStatusCode(SapAribaConstant.STATUS_CODE_SUCCESS);
			}
		}catch (Exception e) {
			
			System.err.println("SapAribaController.addData() error : "+e);
		}
		
		return resp;
		
	}
	
	@RequestMapping(value = "/getDetail/{taskId}",method=RequestMethod.GET)
	public AribaResposeDto getDetail(@PathVariable String taskId)
	{
		
		return sapAribaService.getDetails(taskId);
	}
	
	@RequestMapping(value = "/update/{taskId}",method=RequestMethod.GET)
	public void update(@PathVariable String taskId)
	{
		util.updateData(taskId);
	}
}
