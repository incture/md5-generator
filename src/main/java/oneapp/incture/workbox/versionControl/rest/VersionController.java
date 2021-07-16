package oneapp.incture.workbox.versionControl.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.versionControl.dto.AttachmentDto;
import oneapp.incture.workbox.demo.versionControl.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionControlResponseDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionDetailDto;
import oneapp.incture.workbox.demo.versionControl.services.VersionControlService;
import oneapp.incture.workbox.demo.versionControl.util.Response;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/versionControl", produces = "application/json")
public class VersionController {

	@Autowired
	VersionControlService versionControlService;

	@RequestMapping(value = "/getAllVersion", method = RequestMethod.GET, produces = "application/json")
	public VersionControlResponseDto getAllVersion() {
		return versionControlService.getAllVersion();
	}
	
	@RequestMapping(value = "/getVersionDetail", method = RequestMethod.GET, produces = "application/json")
	public VersionControlResponseDto getVersionDetail(
			@RequestParam(value = "versionNumber", required = false) String versionNumber) {
		return versionControlService.getVersionDetail(versionNumber);
	}

	@RequestMapping(value = "/createNewVersion", method = RequestMethod.POST)
	public ResponseMessage createNewVersion(@RequestBody VersionDetailDto dto) {

		return versionControlService.createNewVersion(dto);
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<Response<?>> upload(@RequestParam("file") MultipartFile file[], 
			@RequestParam("fileName") String fileName[], @RequestParam("fileType") String fileType[],@RequestParam("versionId") String versionId)
	{
		try {
			List<AttachmentDto> attachments = new ArrayList<>();
			for(int i=0; i<fileName.length; i++)
			{
				AttachmentDto request = new AttachmentDto();
				request.setFile(file[i].getBytes());
				request.setFileName(fileName[i]);
				request.setFileType(fileType[i]);
				attachments.add(request);
			}
			return ResponseEntity.ok().body(new Response<DocumentResponseDto>(versionControlService.uploadDocument(attachments,versionId)));
		} catch (Exception e) {
			final Response<String> body = new Response<String>(e.getMessage());
			body.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			body.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}
	
    @RequestMapping(value = "/getDocument", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response<?>> getDocument(@RequestParam(value = "versionId", required = false) String versionId)
	{
		return versionControlService.getDocument(versionId);
	}
	
    @RequestMapping(value = "/getDocumentByID", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Response<?>> getDocumentByID(@RequestParam(value = "documentID", required = false) String documentID)
	{
		return versionControlService.getDocumentByID(documentID);
	}

}
