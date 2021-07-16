package oneapp.incture.workbox.demo.versionControl.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.versionControl.dto.AttachmentDto;
import oneapp.incture.workbox.demo.versionControl.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionControlResponseDto;
import oneapp.incture.workbox.demo.versionControl.dto.VersionDetailDto;
import oneapp.incture.workbox.demo.versionControl.util.Response;

public interface VersionControlService {

	VersionControlResponseDto getAllVersion();

	ResponseMessage createNewVersion(VersionDetailDto dto);

	VersionControlResponseDto getVersionDetail(String versionNumber);
	
	DocumentResponseDto uploadDocument(List<AttachmentDto> attachmentList, String versionId);
	
	ResponseEntity<Response<?>> getDocument(String policyNumber);
	
	public ResponseEntity<Response<?>> getDocumentByID(String documentID);
	
}
