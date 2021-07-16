package oneapp.incture.workbox.demo.sharepoint.util;

import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;

public interface SharepointService {

	String createData();
	String updateData(TaskEventChangeDto dto);
	public AttachmentRequestDto getOriginalAttachment(String fileName);

}
