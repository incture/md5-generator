package oneapp.incture.workbox.demo.emailTemplate.service;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttributesDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplate;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateNewDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.TaskDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.VersionDetailsDto;

public interface EmailTemplateService {

	TaskDetails getTaskDetail(String processName);

	AttributesDetails getAttributeKeys(String processName, String taskName);

	EmailTemplateDetails getEmailTemplate();

	ResponseMessage saveEmailTemplate(EmailTemplate emailTemplate);

	ResponseMessage updateEmailTemplate(EmailTemplate emailTemplate);

	EmailTemplateNewDto getTemplateDetails(String templateId);

	ResponseMessage sendEmail(String eventId);

	void sendAllMails(List<TaskEventsDto> tasks);

	ResponseMessage deleteEmailTemaplte(String templateId);

	VersionDetailsDto getAllVersion(String templateId);

	EmailTemplate getTemplateByTemplateIdVersion(String templateId, int version);

}
