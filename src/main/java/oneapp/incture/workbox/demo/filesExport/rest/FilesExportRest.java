package oneapp.incture.workbox.demo.filesExport.rest;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.filesExport.dto.FileExportResponseDto;
import oneapp.incture.workbox.demo.filesExport.service.FilesExportFacadeLocal;
import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture.workbox")
@RequestMapping(value = "/workbox/filesExport", produces = "application/json")
public class FilesExportRest {

	@Autowired
	private FilesExportFacadeLocal exportLocal;

	@RequestMapping(value = "/inboxDownload", method = RequestMethod.POST)

	public FileExportResponseDto generateInboxReportByte(@RequestBody InboxFilterDto filterDto
			,@AuthenticationPrincipal Token token) throws Exception {
		FileExportResponseDto dto = new FileExportResponseDto();
		ResponseMessage message = new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE,
				"Export failed");
		dto.setMessage(message);

		try {
			String encodedString = exportLocal.exportInboxAsFile(filterDto,token);
			if ("ERROR".equalsIgnoreCase(encodedString)) {
				dto.setMessage(message);
				return dto;
			}
			dto.setFileContent(encodedString);
			dto.setMessage(
					new ResponseMessage(PMCConstant.STATUS_SUCCESS, PMCConstant.CODE_SUCCESS, "Export Successful"));

			return dto;
		} catch (Exception e) {
			System.err.println("inboxDownload error" + e.getMessage());
			message = new ResponseMessage(PMCConstant.STATUS_FAILURE, PMCConstant.CODE_FAILURE, "Export failed");
			dto.setMessage(message);
			return dto;

		}
	}

	@RequestMapping(value = "/inboxDownload2", method = RequestMethod.POST)

	public ResponseEntity<Resource> generateInboxReport2(@RequestBody InboxFilterDto filterDto
			,@AuthenticationPrincipal Token token) throws Exception {
		try {
			File file = exportLocal.exportInboxAsFile2(filterDto,token);

			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders header = new HttpHeaders();
			header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
			header.setContentLength(file.length());
			System.err.println("FilesExportRest.generateInboxReport2() http headers" + header.isEmpty());

			return ResponseEntity.ok().headers(header).contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(resource);
		} catch (Exception e) {
			System.err.println("inboxDownload error" + e.getMessage());
			return null;

		}
	}

	@RequestMapping(value = "/inboxDownloadCA", method = RequestMethod.POST)

	public ResponseEntity<Resource> generateInboxReportCA(@RequestBody InboxFilterDto filterDto
			,@AuthenticationPrincipal Token token) throws Exception {
		try {
			// File file = exportLocal.exportInboxAsFileWithCA(filterDto);
			File file = exportLocal.exportInboxAsFileWithCAAsMap(filterDto,token);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders header = new HttpHeaders();
			header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
			header.setContentLength(file.length());
			System.err.println("FilesExportRest.generateInboxReport2() http headers" + header.isEmpty());

			return ResponseEntity.ok().headers(header).contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(resource);
		} catch (Exception e) {
			System.err.println("inboxDownload error" + e.getMessage());
			return null;

		}
	}

	@RequestMapping(value = "/inboxDownloadRowToColumn", method = RequestMethod.POST)

	public ResponseEntity<Resource> generateInboxReportowToColumn(@RequestBody InboxFilterDto filterDto
			,@AuthenticationPrincipal Token token)
			throws Exception {
		try {
			File file = exportLocal.exportInboxAsFileWithRowToColumn(filterDto,token);
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
			HttpHeaders header = new HttpHeaders();
			header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
			header.setContentLength(file.length());
			System.err.println("FilesExportRest.generateInboxReport2() http headers" + header.isEmpty());

			return ResponseEntity.ok().headers(header).contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
					.body(resource);
		} catch (Exception e) {
			System.err.println("inboxDownload error" + e.getMessage());
			return null;

		}
	}

}
