package oneapp.incture.workbox.demo.document.service;

import java.util.List;

import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;

public interface DocumentService {

	DocumentResponseDto uploadDocument(List<AttachmentRequestDto> attachmentList);

	AttachmentRequestDto getOriginalAttachment(String objectId);
	
}
