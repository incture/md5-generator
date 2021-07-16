package oneapp.incture.workbox.demo.docusign.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskAuditDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.docusign.dto.SigningURLDto;
import oneapp.incture.workbox.demo.docusign.dto.SigningUrlRequestDto;
import oneapp.incture.workbox.demo.docusign.util.AccessToken;


@Component
public class DocuSignServiceImpl implements DocuSignService {
	
	@Autowired
	AccessToken accessToken;
	
	@Autowired
	DocuSignParseService docuSignParseService;
	
	@Autowired
	ProcessEventsDao processEventsDao;
	
	@Autowired
	TaskEventsDao taskEventsDao;
	
	@Autowired 
	TaskOwnersDao taskOwnersDao;
	
	@Autowired
	TaskAuditDao taskAuditDao;
	
	public ResponseMessage update() {
		ResponseMessage responseMessage = new ResponseMessage();
		long start = System.currentTimeMillis();
		try{		
			Object[] object = (Object[]) docuSignParseService.parseDocuSign().getData();
			List<ProcessEventsDto> process = (List<ProcessEventsDto>)object[0];
			System.err.println(process);
			processEventsDao.saveOrUpdateProcesses(process);
			System.err.println("[WBP-Dev][WORKBOX- DocuSign][saveOrUpdateProcesses]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			List<TaskEventsDto> task = (List<TaskEventsDto>)object[1];
			System.err.println(task);
			taskEventsDao.saveOrUpdateTasks(task);
			System.err.println("[WBP-Dev][WORKBOX- DocuSign][saveOrUpdateTasks]" + (System.currentTimeMillis() - start)+task);
			start = System.currentTimeMillis();
			List<TaskOwnersDto> owners = (List<TaskOwnersDto>)object[2];
			System.err.println(owners);
			taskOwnersDao.saveOrUpdateOwners(owners);
			System.err.println("[WBP-Dev][WORKBOX- DocuSign][saveOrUpdateOwners]" + (System.currentTimeMillis() - start)+owners);
			List<TaskAuditDto> audits = (List<TaskAuditDto>)object[3];
			System.err.println(audits);
			taskAuditDao.saveOrUpdateAudits(audits);
			System.err.println("[WBP-Dev][WORKBOX- DocuSign][saveOrUpdateAudit]" + (System.currentTimeMillis() - start)+audits);
			
			responseMessage.setMessage("running");
			responseMessage.setStatus("SUCCESS");
			responseMessage.setStatusCode("200");
			return responseMessage;
		}catch (Exception e) {
			System.err.println("[WBP-Dev][parse][DOCUSIGN]ERROR"+e.getMessage());
			responseMessage.setStatusCode("500");
			responseMessage.setStatus("FAILURE");
			responseMessage.setMessage(e.getMessage());
			return responseMessage;
			
		}
		
	}
	
	public GenericResponseDto createSigningURL(SigningUrlRequestDto signingUrlRequestDto){
		GenericResponseDto genericResponseDto = new GenericResponseDto();
		try{
			genericResponseDto.setMessage("SUCESS");
			genericResponseDto.setStatus("200");
			genericResponseDto.setStatusCode("200");
			System.err.println("inside createsigning");
			SigningURLDto urlDto = new SigningURLDto();
			urlDto = docuSignParseService.getSigningUrl(signingUrlRequestDto.getProcessId(),signingUrlRequestDto.getPuserId());
			if(urlDto.getResponsecode()==500){
				genericResponseDto.setMessage("FAILURE responsecode 500");
				genericResponseDto.setStatus("500");
				genericResponseDto.setStatusCode("500");
			}
			genericResponseDto.setData(urlDto);
			return genericResponseDto;
		}catch (Exception e) {
			e.getMessage();
			genericResponseDto.setMessage("FAILURE");
			genericResponseDto.setStatus("500");
			genericResponseDto.setStatusCode("500");
			genericResponseDto.setData(null);
			return genericResponseDto;			
		}
	}
	
//	public ResponseMessage updateTask(ActionDto actionDto){
//		long start = System.currentTimeMillis();
//		ResponseMessage responseMessage = new ResponseMessage();
//		responseMessage.setMessage("SUCCESS");
//		responseMessage.setStatus("200");
//		GenericResponseDto genericResponseDto = new GenericResponseDto();
//		genericResponseDto.setMessage("SUCESS");
//		genericResponseDto.setStatus("200");
//		genericResponseDto.setStatusCode("200");
//		System.err.println("inside createupdateTAsk");
//		try{	
//			genericResponseDto = docuSignParseService.updateTask(actionDto.getProcessId());
//			List<TaskEventsDto> tasks = (List<TaskEventsDto>)genericResponseDto.getData();
//			System.err.println(tasks);
//			taskEventsDao.saveOrUpdateTasks(tasks);
//			System.err.println("[WBP-Dev][WORKBOX- DocuSign][saveOrUpdateTasks]" + (System.currentTimeMillis() - start)+tasks);
//		}catch (Exception e) {
//			System.err.println("[WBP-Dev][parse][DOCUSIGN]ERROR"+e.getMessage());
//			responseMessage.setMessage("FAILED");
//			responseMessage.setStatus("500");
//		}
//			return responseMessage;
//	}

}
