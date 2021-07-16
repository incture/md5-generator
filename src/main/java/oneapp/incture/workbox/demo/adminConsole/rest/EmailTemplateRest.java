package oneapp.incture.workbox.demo.adminConsole.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttributesDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplate;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateNewDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.TaskDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.VersionDetailsDto;
import oneapp.incture.workbox.demo.emailTemplate.service.EmailTemplateService;

@RestController
@CrossOrigin
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/emailTemplate", produces = "application/json")
public class EmailTemplateRest {

	@Autowired
	EmailTemplateService emailTemplateService;

	@RequestMapping(value = "/getTaskDetail/{processName}", method = RequestMethod.GET, produces = "application/json")
	public TaskDetails getTaskDetail(@PathVariable String processName) {
		return emailTemplateService.getTaskDetail(processName);
	}

	@RequestMapping(value = "/getAttributeKeys/{processName}/{taskName}", method = RequestMethod.GET, produces = "application/json")
	public AttributesDetails getAttributeKeys(@PathVariable String processName, @PathVariable String taskName) {
		return emailTemplateService.getAttributeKeys(processName, taskName);
	}

	@RequestMapping(value = "/getEmailTemplate", method = RequestMethod.GET, produces = "application/json")
	public EmailTemplateDetails getEmailTemplate() {
		return emailTemplateService.getEmailTemplate();
	}

	@RequestMapping(value = "/saveEmailTemplate", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage saveEmailTemplate(@RequestBody EmailTemplate emailTemplate) {
		return emailTemplateService.saveEmailTemplate(emailTemplate);
	}

	@RequestMapping(value = "/updateEmailTemplate", method = RequestMethod.POST, produces = "application/json")
	public ResponseMessage updateEmailTemplate(@RequestBody EmailTemplate emailTemplate) {
		return emailTemplateService.updateEmailTemplate(emailTemplate);
	}

	@RequestMapping(value = "/getTemplateDetails/{templateId}", method = RequestMethod.GET, produces = "application/json")
	public EmailTemplateNewDto getTemplateDetails(@PathVariable String templateId) {
		return emailTemplateService.getTemplateDetails(templateId);
	}

	@RequestMapping(value = "/sendEmail/{eventId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseMessage sendEmail(@PathVariable String eventId) {
		return emailTemplateService.sendEmail(eventId);
	}

	@RequestMapping(value = "/deleteEmailTemaplte/{templateId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseMessage deleteEmailTemaplte(@PathVariable String templateId) {
		return emailTemplateService.deleteEmailTemaplte(templateId);
	}
	
	@RequestMapping(value = "/getAllVersionByTemplateId/{templateId}", method = RequestMethod.GET, produces = "application/json")
	public VersionDetailsDto getAllVersion(@PathVariable String templateId) {
		return emailTemplateService.getAllVersion(templateId);
	}

	@RequestMapping(value = "/getTemplateDetailsByTemplateIdVersion", method = RequestMethod.GET, produces = "application/json")
	public EmailTemplate getTemplateByTemplateIdVersion(@RequestParam String templateId, @RequestParam int version) {
		return emailTemplateService.getTemplateByTemplateIdVersion(templateId, version);
	}

}
