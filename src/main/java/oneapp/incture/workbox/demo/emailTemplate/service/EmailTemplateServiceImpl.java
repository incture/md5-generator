package oneapp.incture.workbox.demo.emailTemplate.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.emailTemplate.Dao.AttachmentDao;
import oneapp.incture.workbox.demo.emailTemplate.Dao.CrossConstantsDao;
import oneapp.incture.workbox.demo.emailTemplate.Dao.EmailContentDao;
import oneapp.incture.workbox.demo.emailTemplate.Dao.EmailContentValuesDao;
import oneapp.incture.workbox.demo.emailTemplate.Dao.EmailTableContentDao;
import oneapp.incture.workbox.demo.emailTemplate.Dao.EmailTemplateDao;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttachmentResponseDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttributesDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.CustomAttributeKeys;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailContentValuesDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTableContentDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplate;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateCreation;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateNewDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.MessageContent;
import oneapp.incture.workbox.demo.emailTemplate.Dto.TaskDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.VersionDetailsDto;
import oneapp.incture.workbox.demo.emailTemplate.util.EmailTemplateParser;

@Service
//@Transactional
public class EmailTemplateServiceImpl implements EmailTemplateService{

	@Autowired
	EmailTemplateDao emailTemplateDao;

	@Autowired
	EmailTemplateParser emailTemplateParser;

	@Autowired
	EmailTableContentDao emailTableContentDao;

	@Autowired
	EmailContentDao emailContentDao;

	@Autowired
	EmailContentValuesDao emailContentValuesDao;

	@Autowired
	CrossConstantsDao crossConstantsDao;
	
	@Autowired
	AttachmentDao attachmentDao;

	@Override
	public TaskDetails getTaskDetail(String processName) {
		TaskDetails taskDetails = null;
		try{
			taskDetails = emailTemplateDao.getTaskDetails(processName);
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Error for getTaskDetail"+e);
		}
		return taskDetails;
	}

	@Override
	public AttributesDetails getAttributeKeys(String processName, String taskName) {
		AttributesDetails attributesDetails = new AttributesDetails();
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		List<CustomAttributeKeys> customAttributeKeys = null;
		try{
			customAttributeKeys = emailTemplateDao.getAttibuteKeys(processName,taskName);
			customAttributeKeys.addAll(crossConstantsDao.getUserKeys());
			attributesDetails.setCustomAttibutes(customAttributeKeys);
			if(ServicesUtil.isEmpty(customAttributeKeys))
				res.setMessage("Empty Attributes");
			else
				res.setMessage("Attributes fetched");
			res.setStatusCode(PMCConstant.CODE_SUCCESS);
			res.setStatus(PMCConstant.SUCCESS);
			attributesDetails.setResponseMessage(res);
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Error for getAttributeKeys"+e);
		}
		return attributesDetails;
	}

	@Override
	public EmailTemplateDetails getEmailTemplate() {
		EmailTemplateDetails emailTemplateDetails = null;
		try{
			emailTemplateDetails = emailTemplateDao.getEmailTemplate();
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Error in getEmailTemplate :"+e);
		}
		return emailTemplateDetails;
	}

	@Override
	public ResponseMessage saveEmailTemplate(EmailTemplate emailTemplate) {
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		try{
			EmailTemplateCreation templateCreation = emailTemplateParser.createEmailDtos(emailTemplate);
			Gson g = new Gson();
			System.err.println(g.toJson(templateCreation));
			emailTemplateDao.saveOrUpdateEmailTemaplte(templateCreation.getEmailTemplateDto());
			emailContentDao.saveOrUpdateEmailContent(templateCreation.getEmailContentDto());
			emailTableContentDao.saveOrUpdateEmailTableContent(templateCreation.getEmailTableContentDto());
			attachmentDao.saveOrUpdateAttachments(templateCreation.getAttachmentsDtos());
			if(ServicesUtil.isEmpty(emailTemplate.getEmailTemplate().getTemplateId()))
				res.setMessage("Email Template Creation Successful");
			else
				res.setMessage("Email Template Updation Successful");
			res.setStatusCode(PMCConstant.CODE_SUCCESS);
			res.setStatus(PMCConstant.SUCCESS);
		}catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateServiceImpl.saveEmailTemplate() Error :"+e);
		}
		return res;
	}

	@Override
	public ResponseMessage updateEmailTemplate(EmailTemplate emailTemplate) {
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		try{
			EmailTemplateCreation templateCreation = emailTemplateParser.createEmailDtos(emailTemplate);
			Gson g = new Gson();
			System.err.println(g.toJson(templateCreation));

		}catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateServiceImpl.saveEmailTemplate() Error :"+e);
		}
		return null;
	}

	@Override
	public EmailTemplateNewDto getTemplateDetails(String templateId) {
		List<EmailTemplate> emailTemplateDtos = null;
		List<EmailTableContentDto> emailTableContent = null;
		List<AttachmentResponseDto> attachmentresponseDto = null;
		EmailTemplateNewDto response = new EmailTemplateNewDto();
		
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		try{
			emailTemplateDtos = emailTemplateDao.getTemplateDetailsTemplateId(templateId);
			
			for (EmailTemplate emailTemplateDto : emailTemplateDtos) {
				if(!ServicesUtil.isEmpty(emailTemplateDto.getEmailContent().getTableContentId()))
				{
					emailTableContent = emailTableContentDao.getTableContentIfExists(
							emailTemplateDto.getEmailContent().getTableContentId());
					emailTemplateDto.getEmailContent().setEmailTableContent(emailTableContent);

				}
				if(!ServicesUtil.isEmpty(emailTemplateDto.getEmailContent().getAttachmentId())){
					attachmentresponseDto = 
							emailTemplateDao.getAttachmentDetail(emailTemplateDto.getEmailContent().getAttachmentId());
					emailTemplateDto.getEmailContent().setAttachmentDetail(attachmentresponseDto);
				}
				
			}
			
			res.setMessage("Email Template Fetched");
			res.setStatusCode(PMCConstant.CODE_SUCCESS);
			res.setStatus(PMCConstant.SUCCESS);
			
			response.setEmailTemplates(emailTemplateDtos);
			response.setResponseMeassage(res);
			
		}catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateServiceImpl.getTemplateDetails() error :"+e);
		}
		
		return response;
	}
	
	public EmailTemplate getTemplateDetailsByEventId(String eventId) {
		EmailTemplate emailTemplate = new EmailTemplate();
		List<EmailTableContentDto> emailTableContent = null;
		List<AttachmentResponseDto> attachmentresponseDtos = null;
		try{
			emailTemplate = emailTemplateDao.getTemplateDetails(eventId);
			if(!ServicesUtil.isEmpty(emailTemplate.getEmailContent().getTableContentId()))
			{
				emailTableContent = emailTableContentDao.getTableContentIfExists(
						emailTemplate.getEmailContent().getTableContentId());
				emailTemplate.getEmailContent().setEmailTableContent(emailTableContent);

			}
			if(!ServicesUtil.isEmpty(emailTemplate.getEmailContent().getAttachmentId())){
				attachmentresponseDtos = 
						emailTemplateDao.getAttachmentDetails(emailTemplate.getEmailContent().getAttachmentId());
				emailTemplate.getEmailContent().setAttachmentDetail(attachmentresponseDtos);
				Gson g  = new Gson();
				System.err.println("[WBP-Dev] attachmentresponseDtos "+g.toJson(attachmentresponseDtos));
			}

		}catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateServiceImpl.getTemplateDetails() error :"+e);
		}
		return emailTemplate;
	}

	@Override
	public ResponseMessage sendEmail(String eventId) {
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		try{
			EmailTemplate emailTemplate = getTemplateDetailsByEventId(eventId);
			System.err.println(emailTemplate);
			MessageContent msgContent = emailTemplateParser.setMessageBody(emailTemplate.getEmailContent().getMessageBody(),emailTemplate.getEmailContent().getEmailTableContent(),eventId);
			emailTemplate.getEmailContent().setMessageBody(msgContent.getMessageBody());
			if(!ServicesUtil.isEmpty(emailTemplate.getEmailContent().getAttachmentDetail()))
				emailTemplate.getEmailContent().getAttachmentDetail().addAll(msgContent.getAttributesDetails());
			else
				emailTemplate.getEmailContent().setAttachmentDetail(msgContent.getAttributesDetails());
			emailTemplateParser.sendEmail(emailTemplate,eventId);
			EmailContentValuesDto contentValuesDto = emailTemplateParser.archiveContent(
					emailTemplate.getEmailContent(),eventId);
			emailContentValuesDao.saveOrUpdateEmailContent(contentValuesDto);
			res.setMessage("Email sent");
			res.setStatusCode(PMCConstant.CODE_SUCCESS);
			res.setStatus(PMCConstant.SUCCESS);
		}catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateServiceImpl.sendEmail() error :"+e);
		}
		res.setMessage(PMCConstant.SUCCESS);
		res.setStatusCode(PMCConstant.CODE_SUCCESS);
		res.setStatus(PMCConstant.SUCCESS);
		return res;
	}

	@Async
	public void sendAllMails(List<TaskEventsDto> tasks) {
		try{
			Thread.sleep(5000);
			for (TaskEventsDto taskEventsDto : tasks) {
				sendEmail(taskEventsDto.getEventId());
			}
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Error mail sending :"+e);
		}

	}

	@Override
	public ResponseMessage deleteEmailTemaplte(String templateId) {
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		try{
			attachmentDao.deleteAllAttahments(templateId);
			emailTableContentDao.deleteTableContent(templateId);
			emailContentDao.deleteEmailContent(templateId);
			emailTemplateDao.deleteEmailTemplate(templateId);
			res.setMessage("Email Template Deleted");
			res.setStatusCode(PMCConstant.CODE_SUCCESS);
			res.setStatus(PMCConstant.SUCCESS);
		}catch (Exception e) {
			System.err.println("[WBP-Dev]Deleting email Template error :"+e);
		}
		return res;
	}

	@Override
	public VersionDetailsDto getAllVersion(String templateId){
		VersionDetailsDto versionDetailsDto=new VersionDetailsDto();
		List<Integer> version= new ArrayList<Integer>();
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
	    try{
	    	version=emailTemplateDao.getAllVersions(templateId);
	    	res.setMessage("Fetched versions Success");
	    	res.setStatusCode(PMCConstant.CODE_SUCCESS);
	    	res.setStatus(PMCConstant.SUCCESS);
	    	versionDetailsDto.setVersion(version);
	    	versionDetailsDto.setRespomseMessage(res);
	    }
	    catch(Exception e){
	    	System.err.println("[WBP-Dev]Fetching version error :"+e);
	    }
	    return versionDetailsDto;
		
	}
	
	@Override
	public EmailTemplate getTemplateByTemplateIdVersion(String templateId, int version){
		EmailTemplate emailTemplate=null;
		List<EmailTableContentDto> emailTableContent = null;
		List<AttachmentResponseDto> attachmentresponseDtos = null;
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		
		try{
			emailTemplate=emailTemplateDao.getEmailTemplateByTemplateIdVersion(templateId,version);
			
		   if(emailTemplate!=null){
			
			if(!ServicesUtil.isEmpty(emailTemplate.getEmailContent().getTableContentId()))
			{
				emailTableContent = emailTableContentDao.getTableContentIfExists(
						emailTemplate.getEmailContent().getTableContentId());
				emailTemplate.getEmailContent().setEmailTableContent(emailTableContent);

			}
			if(!ServicesUtil.isEmpty(emailTemplate.getEmailContent().getAttachmentId())){
				attachmentresponseDtos = 
						emailTemplateDao.getAttachmentDetail(emailTemplate.getEmailContent().getAttachmentId());
				emailTemplate.getEmailContent().setAttachmentDetail(attachmentresponseDtos);
			}
			res.setMessage("Template Details Fetched");
			res.setStatusCode(PMCConstant.CODE_SUCCESS);
			res.setStatus(PMCConstant.SUCCESS);
			
		   }
			
		   emailTemplate.setResponseMessage(res);
			
		}
		catch (Exception e) {
			System.err.println("[WBP-Dev]Fetching Template Details Error :"+e);
		}
		
	return emailTemplate;
	}






}
