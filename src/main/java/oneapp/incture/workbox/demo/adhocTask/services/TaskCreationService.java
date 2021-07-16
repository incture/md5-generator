package oneapp.incture.workbox.demo.adhocTask.services;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adhocTask.dto.AdhocActionDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ApproverDto;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CrossConstantResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributesDto;


public interface TaskCreationService {

	AttributesResponseDto getProcessAttributes(String processName);

	ResponseMessage createTasks(AttributesResponseDto attributesResponseDto, Token token);

	ResponseMessage deleteDraft(String processId, String eventId);

	AttributesResponseDto viewDraft(String processId);

	CrossConstantResponseDto getConstants(String name); 

	ResponseMessage updateTaskAttributes(CustomAttributesDto customAttributeDto); 

	ResponseMessage actionOnAdhoc(AdhocActionDto actionDto);

	ApproverDto getApprover();

}
