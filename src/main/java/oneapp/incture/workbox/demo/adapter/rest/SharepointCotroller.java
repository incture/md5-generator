package oneapp.incture.workbox.demo.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.sharepoint.util.SharepointService;
import oneapp.incture.workbox.demo.sharepoint.util.TaskEventChangeDto;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/sharepoint",produces="application/json")
public class SharepointCotroller {

	@Autowired
	SharepointService sharepointService;

	@RequestMapping(value = "/test")
	public ResponseMessage addData() {
		System.err.println("[WBP-Dev]SharepointCotroller.addData() started : ");
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.CODE_FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);

		try {
			String res = sharepointService.createData();
			if (res.equalsIgnoreCase(PMCConstant.SUCCESS)) {
				resp.setMessage(PMCConstant.SUCCESS);
				resp.setStatus(PMCConstant.CODE_SUCCESS);
				resp.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		} catch (Exception e) {

			System.err.println("[WBP-Dev]SharepointCotroller.addData() error "+e);
		}
		System.err.println("[WBP-Dev]SharepointCotroller.addData() ended : ");
		return resp;
	}
	
	
	@RequestMapping(value="/updateTask",method=RequestMethod.POST)
	public ResponseMessage updateData(@RequestBody TaskEventChangeDto taskChangeDto )
	{
		System.err.println("[WBP-Dev]SharepointCotroller.upDateStatusStarted() started : "+taskChangeDto);
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(PMCConstant.FAILURE);
		resp.setStatus(PMCConstant.CODE_FAILURE);
		resp.setStatusCode(PMCConstant.CODE_FAILURE);
		try
		{
			String res = sharepointService.updateData(taskChangeDto);
			if (res.equalsIgnoreCase(PMCConstant.SUCCESS)) {
				resp.setMessage(PMCConstant.SUCCESS);
				resp.setStatus(PMCConstant.CODE_SUCCESS);
				resp.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		}
		catch (Exception e) {

			System.err.println("[WBP-Dev]SharepointCotroller.addData() error "+e);
		}
		System.err.println("[WBP-Dev]SharepointCotroller.addData() ended : ");
		return resp;
			
		}
		
		
		
	}

