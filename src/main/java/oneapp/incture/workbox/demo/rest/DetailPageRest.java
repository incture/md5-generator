package oneapp.incture.workbox.demo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.adapter_base.dto.DetailsPageReponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskConfigurationDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskTemplateTableDto;
import oneapp.incture.workbox.demo.detailpage.dto.DynamicTaskDetailsDto;
import oneapp.incture.workbox.demo.detailpage.service.DetailPageFacadeLocal;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.service.DocumentService;
import oneapp.incture.workbox.demo.sharepoint.util.SharepointService;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;


@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value="/workbox/detailPage", produces = "application/json")
public class DetailPageRest {
	
	@Autowired
	private DetailPageFacadeLocal detailPageFacadeLocal;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	SharepointService sharepointService;

	@RequestMapping(value = "/dynamicDetails",method = RequestMethod.GET)
	public DynamicTaskDetailsDto getDynamicTestDetails(@RequestParam(name = "taskId", required = true) String taskId,
														@RequestParam(name = "$select", required = false) String selectType) {
		DynamicTaskDetailsDto dto= detailPageFacadeLocal.getDynamicDetails(taskId,selectType);
		return dto;
	}
	
	@RequestMapping(value = "/auditLogs",method = RequestMethod.GET)
	public List<TaskAuditDto> getAuditLogDetails(@RequestParam(name = "taskId", required = false) String taskId,
														@RequestParam(name = "signature", required = false) String signature) {
		List<TaskAuditDto> dto= detailPageFacadeLocal.getAuditLogDetails(taskId,signature);
		return dto;
	}
	
	@RequestMapping(value = "/dynamicDetailsJabil",method = RequestMethod.GET)
	public DetailsPageReponseDto geDynamicDetailsJabil(@RequestParam(name = "taskId", required = true) String taskId) {
		DetailsPageReponseDto dto= detailPageFacadeLocal.getDynamicDetailsNew(taskId);
		return dto;
	}
	
	@RequestMapping(value = "/dynamicDetailFromContext",method = RequestMethod.GET)
	public DetailsPageReponseDto dynamicDetailFromContext(@RequestParam(name = "taskId", required = true) String taskId) {
		DetailsPageReponseDto dto= detailPageFacadeLocal.dynamicDetailFromContext(taskId);
		return dto;
	}
	
	@RequestMapping(value = "/taskConfiguration",method = RequestMethod.GET)
	public TaskConfigurationDto taskConfiguration(@RequestParam(name = "processName", required = false) String processName,
													@RequestParam(name = "taskName", required = false) String taskName) {
		TaskConfigurationDto dto= detailPageFacadeLocal.taskConfiguration(processName,taskName);
		return dto;
	}
	
	@RequestMapping(value = "/updateTaskConfiguration",method = RequestMethod.POST)
	public ResponseMessage taskConfigurationUpdate(@RequestBody TaskTemplateTableDto taskTemplateTableDto) {
		ResponseMessage dto = detailPageFacadeLocal.configurationUpdate(taskTemplateTableDto);
		return dto;
	}
	
	@RequestMapping(value = "/getOriginalAttachment/{fileName}",method = RequestMethod.GET)
	public AttachmentRequestDto getOriginalAttachment(@PathVariable String fileName) {
		AttachmentRequestDto dto= sharepointService.getOriginalAttachment(fileName);
		return dto;
	}
}