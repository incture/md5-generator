package oneapp.incture.workbox.demo.docusign.service;

import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.docusign.dto.SigningURLDto;


public interface DocuSignParseService {
	
	public RestResponse getEnvelopes(String accessToken) throws Exception;
	public RestResponse getRecipients(String accessToken,String envelopeId);
	public SigningURLDto getSigningUrl(String accesToken,String envelopeid);
	public GenericResponseDto parseDocuSign();
}
