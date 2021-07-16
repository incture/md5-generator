package oneapp.incture.workbox.demo.adapter.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.docusign.dto.KeyDto;
import oneapp.incture.workbox.demo.docusign.dto.SigningUrlRequestDto;
import oneapp.incture.workbox.demo.docusign.service.DocuSignParseService;
import oneapp.incture.workbox.demo.docusign.service.DocuSignService;
import oneapp.incture.workbox.demo.docusign.util.AccessToken;
import oneapp.incture.workbox.demo.docusign.util.AddKeys;




@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/docusign")
public class DocusignAdapterController {
	
	@Autowired
	AccessToken accessToken;
	
	@Autowired
	DocuSignParseService docuSignParseService;
	
	@Autowired
	DocuSignService docuSignService;
	
	@Autowired
	AddKeys addKeys;
	
	
	@RequestMapping(value="/trial",method=RequestMethod.GET)
	public ResponseMessage trial() throws Exception{
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(accessToken.getJwtAccessToken());
		responseMessage.setStatus("200");
		return responseMessage;
	}
	
	@RequestMapping(value="/accesstoken",method=RequestMethod.GET)
	public String trialUserId() throws Exception{
		return accessToken.getJwtAccessTokenUserId("a3d02c80-ca72-4b7a-99e3-bb3aaf4aab3f");
	}
	
	@RequestMapping(value = "/addData",method = RequestMethod.GET)
	public ResponseMessage addData(){
		return docuSignService.update();
	}
	

	@RequestMapping(value = "/sign",method = RequestMethod.POST)
	public GenericResponseDto docusignActionFacade(@RequestBody SigningUrlRequestDto actionDto){
		return docuSignService.createSigningURL(actionDto);
	}
	
//	@RequestMapping(value="/update",method = RequestMethod.POST)
//	public ResponseMessage updateTask(@RequestBody ActionDto actionDto){
//		return docuSignService.updateTask(actionDto);	
//	}
	
	@RequestMapping(value="/addkey",method = RequestMethod.POST)
	public DocumentResponseDto addPublicKey(@RequestBody KeyDto key){
		try {
			return addKeys.addpkey(key);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping(value="/getkey",method = RequestMethod.GET)
	public AttachmentRequestDto getKeyByte(@RequestParam("objectid") String objectid){
		return addKeys.getFile(objectid);		
	}
	
	
	
}
