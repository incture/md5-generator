package oneapp.incture.workbox.demo.docusign.service;

import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.docusign.dto.SigningUrlRequestDto;

public interface DocuSignService {
	public ResponseMessage update();

	public GenericResponseDto createSigningURL(SigningUrlRequestDto signingUrlRequestDto);
}
