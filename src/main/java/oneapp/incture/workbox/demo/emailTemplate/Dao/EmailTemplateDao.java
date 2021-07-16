package oneapp.incture.workbox.demo.emailTemplate.Dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.AttachmentsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.EmailTemplateDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.service.DocumentServiceImpl;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttachmentResponseDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.CustomAttributeKeys;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailContent;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplate;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.TaskDetailDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.TaskDetails;
import oneapp.incture.workbox.demo.emailTemplate.Dto.VersionStatusDto;

@Repository
public class EmailTemplateDao extends BaseDao<EmailTemplateDo, EmailTemplateDto> {

	@Autowired
	AttachmentDao attachmentDao;

	@Autowired
	DocumentServiceImpl documentService;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected EmailTemplateDo importDto(EmailTemplateDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		EmailTemplateDo entity = new EmailTemplateDo();
		entity.setTemplateId(fromDto.getTemplateId());
		entity.setProcessName(fromDto.getProcessName());
		entity.setTaskName(fromDto.getTaskName());

		if (!ServicesUtil.isEmpty(fromDto.getLatestVersion()))
			entity.setVersion(fromDto.getLatestVersion());

		if (!ServicesUtil.isEmpty(fromDto.getEmailTypeId()))
			entity.setEmailTypeId(fromDto.getEmailTypeId());
		if (!ServicesUtil.isEmpty(fromDto.getOwnerId()))
			entity.setOwnerId(fromDto.getOwnerId());
		if (!ServicesUtil.isEmpty(fromDto.getStatus()))
			entity.setStatus(fromDto.getStatus());
		if (!ServicesUtil.isEmpty(fromDto.getTemplateName()))
			entity.setTemplateName(fromDto.getTemplateName());
		if (!ServicesUtil.isEmpty(fromDto.getTemplateType()))
			entity.setTemplateType(fromDto.getTemplateType());
		return entity;
	}

	@Override
	protected EmailTemplateDto exportDto(EmailTemplateDo entity) {
		EmailTemplateDto emailTemplateDto = new EmailTemplateDto();

		if (!ServicesUtil.isEmpty(entity.getEmailTypeId()))
			emailTemplateDto.setLatestVersion(entity.getVersion());

		if (!ServicesUtil.isEmpty(entity.getEmailTypeId()))
			emailTemplateDto.setEmailTypeId(entity.getEmailTypeId());
		if (!ServicesUtil.isEmpty(entity.getOwnerId()))
			emailTemplateDto.setOwnerId(entity.getOwnerId());
		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			emailTemplateDto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getStatus()))
			emailTemplateDto.setStatus(entity.getStatus());
		if (!ServicesUtil.isEmpty(entity.getTaskName()))
			emailTemplateDto.setTaskName(entity.getTaskName());
		if (!ServicesUtil.isEmpty(entity.getTemplateId()))
			emailTemplateDto.setTemplateId(entity.getTemplateId());
		if (!ServicesUtil.isEmpty(entity.getTemplateName()))
			emailTemplateDto.setTemplateName(entity.getTemplateName());
		if (!ServicesUtil.isEmpty(entity.getTemplateType()))
			emailTemplateDto.setTemplateType(entity.getTemplateType());
		return emailTemplateDto;
	}

	public void saveOrUpdateEmailTemaplte(EmailTemplateDto emailTemplateDtos) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			if (!ServicesUtil.isEmpty(emailTemplateDtos)) {
				//session = this.getSession();

				EmailTemplateDto currentTask = emailTemplateDtos;
				session.saveOrUpdate(importDto(currentTask));

				session.flush();
				session.clear();

			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WB-DEV]ERROR saving email template:" + e);
		}
	}

	@SuppressWarnings("unchecked")
	public TaskDetails getTaskDetails(String processName) {
		TaskDetails taskDetails = new TaskDetails();
		List<TaskDetailDto> taskDetailDtos = new ArrayList<>();
		TaskDetailDto taskDetailDto = null;
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		String getTaskName = "SELECT TASK_NAME FROM PROCESS_TEMPLATE WHERE PROCESS_NAME = '" + processName + "'";
		Query getTaskNameQry = this.getSession().createSQLQuery(getTaskName);
		List<String> taskNames = getTaskNameQry.list();
		if (!ServicesUtil.isEmpty(taskNames)) {
			for (String tasks : taskNames) {
				taskDetailDto = new TaskDetailDto();
				taskDetailDto.setTaskDisplayName(tasks);
				taskDetailDto.setTaskName(tasks);
				taskDetailDtos.add(taskDetailDto);
			}
			res.setMessage("Tasks Fetched");
		}
		taskDetails.setTaskDetails(taskDetailDtos);
		res.setStatusCode(PMCConstant.CODE_SUCCESS);
		res.setStatus(PMCConstant.SUCCESS);
		taskDetails.setResponseMessage(res);
		return taskDetails;
	}

	@SuppressWarnings("unchecked")
	public List<CustomAttributeKeys> getAttibuteKeys(String processName, String taskName) {
		List<CustomAttributeKeys> customAttributeKeys = new ArrayList<>();
		CustomAttributeKeys attributeKeys = null;
		Query query = this.getSession()
				.createSQLQuery("SELECT KEY,LABEL FROM CUSTOM_ATTR_TEMPLATE "
						+ "WHERE PROCESS_NAME = (SELECT TEMPLATE_ID FROM PROCESS_TEMPLATE" + " WHERE PROCESS_NAME = '"
						+ processName + "' AND TASK_NAME = '" + taskName + "') AND is_deleted = 0 ");
		List<Object[]> result = query.list();

		if (!ServicesUtil.isEmpty(result)) {
			for (Object[] obj : result) {
				attributeKeys = new CustomAttributeKeys();
				attributeKeys.setKey(obj[0].toString());
				attributeKeys.setLabel(obj[1].toString());
				customAttributeKeys.add(attributeKeys);
			}
		}
		return customAttributeKeys;
	}

	@SuppressWarnings("unchecked")
	public EmailTemplateDetails getEmailTemplate() {
		EmailTemplateDetails emailTemplateDetails = new EmailTemplateDetails();
		EmailTemplateDto emailTemplateDto = null;
		List<EmailTemplateDto> emailTemplateDtos = new ArrayList<>();
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		try {
			Query query = this.getSession().createSQLQuery(
					"SELECT T1.TEMPLATE_ID,T1.TASK_NAME,T1.PROCESS_NAME,T1.OWNER_ID,T1.STATUS,T1.TEMPLATE_NAME,T1.VERSION "
							+ "FROM EMAIL_TEMPLATE T1 JOIN (SELECT TEMPLATE_ID , MAX(VERSION) AS VERSION FROM EMAIL_TEMPLATE GROUP BY TEMPLATE_ID) T2 "
							+ "ON T1.TEMPLATE_ID = T2.TEMPLATE_ID AND T1.VERSION  = T2.VERSION;");
			List<Object[]> result = query.list();

			if (!ServicesUtil.isEmpty(result)) {

				for (Object[] obj : result) {
					emailTemplateDto = new EmailTemplateDto();
					List<VersionStatusDto> versionList = new ArrayList<VersionStatusDto>();
					emailTemplateDto.setOwnerId(ServicesUtil.isEmpty(obj[3]) ? null : obj[3].toString());
					emailTemplateDto.setProcessName(ServicesUtil.isEmpty(obj[2]) ? null : obj[2].toString());
					emailTemplateDto.setStatus(ServicesUtil.isEmpty(obj[4]) ? null : obj[4].toString());
					emailTemplateDto.setTaskName(ServicesUtil.isEmpty(obj[1]) ? null : obj[1].toString());
					emailTemplateDto.setTemplateId(ServicesUtil.isEmpty(obj[0]) ? null : obj[0].toString());
					emailTemplateDto.setTemplateName(ServicesUtil.isEmpty(obj[5]) ? null : obj[5].toString());
					emailTemplateDto.setLatestVersion(ServicesUtil.isEmpty(obj[6]) ? 0 : (int) obj[6]);
					int mx = (int) obj[6];
					for (int i = (int) obj[6]; i >= 1; i--) {
						VersionStatusDto versionStatusDto = new VersionStatusDto();
						if (i == mx) {
							versionStatusDto.setVersion(i);
							versionStatusDto.setStatus("Activated");

						} else {
							versionStatusDto.setVersion(i);
							versionStatusDto.setStatus("Deactivated");
						}
						versionList.add(versionStatusDto);

					}
					emailTemplateDto.setVersions(versionList);
					emailTemplateDtos.add(emailTemplateDto);
				}
				res.setMessage("Email Templates Fetched");
			} else {
				res.setMessage("Email Templates Empty");
			}
			res.setStatusCode(PMCConstant.CODE_SUCCESS);
			res.setStatus(PMCConstant.SUCCESS);
		} catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateDao.getEmailTemplate() error :" + e);
		}
		emailTemplateDetails.setEmailTemplateDtos(emailTemplateDtos);
		emailTemplateDetails.setResponseMessage(res);
		return emailTemplateDetails;
	}

	public EmailTemplate getTemplateDetails(String eventId) {
		EmailTemplate emailTemplate = new EmailTemplate();
		EmailTemplateDto emailTemplateDto = null;
		EmailContent emailContent = null;
		Session session=sessionFactory.openSession();
		try {
			String getTemplateStr = "SELECT ET.TEMPLATE_ID,ET.TASK_NAME,ET.PROCESS_NAME,ET.EMAIL_TYPE_ID,ET.OWNER_ID,ET.STATUS,ET.TEMPLATE_NAME,ET.TEMPLATE_TYPE, "
					+ "EC.ATTACHMENT_ID, EC.TO,EC.BCC,EC.CC,EC.FRAME_ID,EC.IS_ATTACHMENT,EC.IS_TABLE_CONTENT,EC.MESSAGE_BODY,EC.SUBJECT,EC.TABLE_CONTENT_ID,ET.VERSION "
					+ "FROM EMAIL_TEMPLATE ET INNER JOIN EMAIL_CONTENT EC ON EC.TEMPLATE_ID = ET.TEMPLATE_ID AND EC.VERSION=ET.VERSION "
					+ "INNER JOIN TASK_EVENTS TE ON TE.NAME = ET.TASK_NAME AND TE.PROC_NAME = ET.PROCESS_NAME WHERE TE.EVENT_ID ='"
					+ eventId + "' AND ET.STATUS = 'Activated' ORDER BY ET.VERSION DESC LIMIT 1";

			Query getTempalteQry = session.createSQLQuery(getTemplateStr);
			Object[] result = (Object[]) getTempalteQry.list().get(0);

			if (!ServicesUtil.isEmpty(result)) {
				emailTemplateDto = new EmailTemplateDto();
				emailTemplateDto.setEmailTypeId(ServicesUtil.isEmpty(result[3]) ? null : result[3].toString());
				emailTemplateDto.setOwnerId(ServicesUtil.isEmpty(result[4]) ? null : result[4].toString());
				emailTemplateDto.setProcessName(ServicesUtil.isEmpty(result[2]) ? null : result[2].toString());
				emailTemplateDto.setStatus(ServicesUtil.isEmpty(result[5]) ? null : result[5].toString());
				emailTemplateDto.setTaskName(ServicesUtil.isEmpty(result[1]) ? null : result[1].toString());
				emailTemplateDto.setTemplateId(ServicesUtil.isEmpty(result[0]) ? null : result[0].toString());
				emailTemplateDto.setTemplateName(ServicesUtil.isEmpty(result[6]) ? null : result[6].toString());
				emailTemplateDto.setTemplateType(ServicesUtil.isEmpty(result[7]) ? null : result[7].toString());
				emailTemplateDto.setLatestVersion(ServicesUtil.isEmpty(result[18]) ? 0 : (int) result[18]);

				// emailTemplateDto.setVersions(Collections.reverse(range));
				emailContent = new EmailContent();
				emailContent.setBcc(ServicesUtil.isEmpty(result[10]) ? "" : result[10].toString());
				emailContent.setCc(ServicesUtil.isEmpty(result[11]) ? "" : result[11].toString());
				emailContent.setFrameId(ServicesUtil.isEmpty(result[12]) ? null : result[12].toString());
				emailContent.setIsAttachment(
						ServicesUtil.isEmpty(result[13]) ? null : (Boolean) result[13] );
				emailContent.setIsTableContent(
						ServicesUtil.isEmpty(result[14]) ? null : (Boolean) result[14]);
				emailContent.setMessageBody(ServicesUtil.isEmpty(result[15]) ? null : result[15].toString());
				emailContent.setSubject(ServicesUtil.isEmpty(result[16]) ? null : result[16].toString());
				emailContent.setTemplateId(ServicesUtil.isEmpty(result[0]) ? null : result[0].toString());
				emailContent.setTo(ServicesUtil.isEmpty(result[9]) ? "" : result[9].toString());
				emailContent.setAttachmentId(ServicesUtil.isEmpty(result[8]) ? null : result[8].toString());
				emailContent.setTableContentId(ServicesUtil.isEmpty(result[17]) ? null : result[17].toString());
				emailTemplate.setEmailContent(emailContent);
				emailTemplate.setEmailTemplate(emailTemplateDto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateDao.getTemplateDetails() error :" + e);
		}
		return emailTemplate;
	}

	public List<EmailTemplate> getTemplateDetailsTemplateId(String templateId) {
		EmailTemplate emailTemplate = null;
		List<EmailTemplate> fetchResult = new ArrayList<EmailTemplate>();
		EmailTemplateDto emailTemplateDto = null;
		EmailContent emailContent = null;
		try {
			String getTemplateStr = "SELECT ET.TEMPLATE_ID, ET.TASK_NAME,ET.PROCESS_NAME,ET.EMAIL_TYPE_ID,"
					+ "ET.OWNER_ID,ET.STATUS,ET.TEMPLATE_NAME,ET.TEMPLATE_TYPE,"
					+ "EC.ATTACHMENT_ID,EC.TO,EC.BCC,EC.CC,EC.FRAME_ID,EC.IS_ATTACHMENT,EC.IS_TABLE_CONTENT,"
					+ "EC.MESSAGE_BODY,EC.SUBJECT,EC.TABLE_CONTENT_ID ,ET.VERSION "
					+ "FROM EMAIL_TEMPLATE ET INNER JOIN EMAIL_CONTENT EC ON EC.TEMPLATE_ID = ET.TEMPLATE_ID AND EC.VERSION = ET.VERSION "
					+ "WHERE ET.TEMPLATE_ID = '" + templateId + "'";
			System.err.println("Query :" + getTemplateStr);
			Query getTempalteQry = this.getSession().createSQLQuery(getTemplateStr);
			List<Object[]> resultList = getTempalteQry.list();

			for (Object[] result : resultList) {
				if (!ServicesUtil.isEmpty(result)) {
					emailTemplate = new EmailTemplate();
					emailTemplateDto = new EmailTemplateDto();
					emailTemplateDto.setEmailTypeId(ServicesUtil.isEmpty(result[3]) ? null : result[3].toString());
					emailTemplateDto.setOwnerId(ServicesUtil.isEmpty(result[4]) ? null : result[4].toString());
					emailTemplateDto.setProcessName(ServicesUtil.isEmpty(result[2]) ? null : result[2].toString());
					emailTemplateDto.setStatus(ServicesUtil.isEmpty(result[5]) ? null : result[5].toString());
					emailTemplateDto.setTaskName(ServicesUtil.isEmpty(result[1]) ? null : result[1].toString());
					emailTemplateDto.setTemplateId(ServicesUtil.isEmpty(result[0]) ? null : result[0].toString());
					emailTemplateDto.setTemplateName(ServicesUtil.isEmpty(result[6]) ? null : result[6].toString());
					emailTemplateDto.setTemplateType(ServicesUtil.isEmpty(result[7]) ? null : result[7].toString());
					emailTemplateDto.setLatestVersion(ServicesUtil.isEmpty(result[18]) ? 0 : (int) result[18]);
					emailContent = new EmailContent();
					emailContent.setBcc(ServicesUtil.isEmpty(result[10]) ? "" : result[10].toString());
					emailContent.setCc(ServicesUtil.isEmpty(result[11]) ? "" : result[11].toString());
					emailContent.setFrameId(ServicesUtil.isEmpty(result[12]) ? null : result[12].toString());
					emailContent.setIsAttachment(
							ServicesUtil.isEmpty(result[13]) ? null : (Byte) result[13] == 1 ? true : false);
					emailContent.setIsTableContent(
							ServicesUtil.isEmpty(result[14]) ? null : (Byte) result[14] == 1 ? true : false);
					emailContent.setMessageBody(ServicesUtil.isEmpty(result[15]) ? null : result[15].toString());
					emailContent.setSubject(ServicesUtil.isEmpty(result[16]) ? null : result[16].toString());
					emailContent.setTemplateId(ServicesUtil.isEmpty(result[0]) ? null : result[0].toString());
					emailContent.setTo(ServicesUtil.isEmpty(result[9]) ? "" : result[9].toString());
					emailContent.setAttachmentId(ServicesUtil.isEmpty(result[8]) ? null : result[8].toString());
					emailContent.setTableContentId(ServicesUtil.isEmpty(result[17]) ? null : result[17].toString());
					emailContent.setVersion((ServicesUtil.isEmpty(result[18]) ? 0 : (int) result[18]));
					emailTemplate.setEmailContent(emailContent);
					emailTemplate.setEmailTemplate(emailTemplateDto);
					fetchResult.add(emailTemplate);
				}
			}

		} catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateDao.getTemplateDetails() error :" + e);
		}
		return fetchResult;
	}

	public List<AttachmentResponseDto> getAttachmentDetails(String attachmentId) {
		List<AttachmentResponseDto> attachmentResponseDtos = new ArrayList<>();
		AttachmentResponseDto attachmentResponseDto = null;
		AttachmentRequestDto attachmentRequestDto = null;
		try {
			List<AttachmentsDo> attachments = attachmentDao.getAttachments(attachmentId);

			for (AttachmentsDo attachmentsDo : attachments) {
				attachmentRequestDto = documentService.getOriginalAttachment(attachmentsDo.getDocumentId());
				attachmentResponseDto = new AttachmentResponseDto();
				attachmentResponseDto.setDocumentName(attachmentRequestDto.getFileName());
				attachmentResponseDto.setDocumentSize(attachmentRequestDto.getFileSize());
				attachmentResponseDto.setDocumentType(attachmentsDo.getDocumentType());
				attachmentResponseDto.setEncodedFile(attachmentRequestDto.getEncodedFileContent());
				attachmentResponseDtos.add(attachmentResponseDto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateDao.getAttachmentDetails() error :" + e);
		}
		return attachmentResponseDtos;
	}

	public List<AttachmentResponseDto> getAttachmentDetail(String attachmentId) {
		List<AttachmentResponseDto> attachmentResponseDtos = new ArrayList<>();
		AttachmentResponseDto attachmentResponseDto = null;
		try {
			List<AttachmentsDo> attachments = attachmentDao.getAttachments(attachmentId);

			for (AttachmentsDo attachmentsDo : attachments) {
				attachmentResponseDto = new AttachmentResponseDto();
				attachmentResponseDto.setDocumentId(attachmentsDo.getDocumentId());
				attachmentResponseDto.setDocumentName(attachmentsDo.getDocumentName());
				attachmentResponseDto.setDocumentSize(attachmentsDo.getDocumentSize());
				attachmentResponseDto.setDocumentType(attachmentsDo.getDocumentType());
				attachmentResponseDto.setIsDeleted(false);
				attachmentResponseDtos.add(attachmentResponseDto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateDao.getAttachmentDetails() error :" + e);
		}
		return attachmentResponseDtos;
	}

	public void deleteEmailTemplate(String templateId) {
		Session session=null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query deleteQry = session
					.createSQLQuery("DELETE FROM EMAIL_TEMPLATE WHERE " + "TEMPLATE_ID = '" + templateId + "'");
			//Transaction tx = session.beginTransaction();
			deleteQry.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Email Template]delete email Template table ERROR:" + e);
		}
	}

	public List<Integer> getAllVersions(String temlateId) {
		String getVersion = "SELECT VERSION FROM EMAIL_TEMPLATE WHERE " + "TEMPLATE_ID ='" + temlateId
				+ "' ORDER BY VERSION DESC";
		System.err.println("Query :" + getVersion);
		Query getVersionQry = this.getSession().createSQLQuery(getVersion);
		List<Integer> version = getVersionQry.list();
		return version;

	}

	public EmailTemplate getEmailTemplateByTemplateIdVersion(String templateId, int version) {
		EmailTemplate emailTemplate = null;
		// List<EmailTemplate>fetchResult=new ArrayList<EmailTemplate>();
		EmailTemplateDto emailTemplateDto = null;
		EmailContent emailContent = null;
		ResponseMessage res = new ResponseMessage();
		res.setMessage(PMCConstant.FAILURE);
		res.setStatusCode(PMCConstant.CODE_FAILURE);
		res.setStatus(PMCConstant.FAILURE);
		try {

			String getTemplateDetails = "SELECT ET.TEMPLATE_ID, ET.TASK_NAME,ET.PROCESS_NAME,ET.EMAIL_TYPE_ID,"
					+ "ET.OWNER_ID,ET.STATUS,ET.TEMPLATE_NAME,ET.TEMPLATE_TYPE,"
					+ "EC.ATTACHMENT_ID,EC.TO,EC.BCC,EC.CC,EC.FRAME_ID,EC.IS_ATTACHMENT,EC.IS_TABLE_CONTENT,"
					+ "EC.MESSAGE_BODY,EC.SUBJECT,EC.TABLE_CONTENT_ID ,ET.VERSION "
					+ "FROM EMAIL_TEMPLATE ET INNER JOIN EMAIL_CONTENT EC ON EC.TEMPLATE_ID = ET.TEMPLATE_ID AND EC.VERSION = ET.VERSION "
					+ "WHERE ET.TEMPLATE_ID = '" + templateId + "' AND ET.VERSION = '" + version + "'";
			System.err.println("Query :" + getTemplateDetails);
			Object[] result = (Object[]) this.getSession().createSQLQuery(getTemplateDetails).list().get(0);
			if (!ServicesUtil.isEmpty(result)) {
				emailTemplate = new EmailTemplate();
				emailTemplateDto = new EmailTemplateDto();
				emailTemplateDto.setEmailTypeId(ServicesUtil.isEmpty(result[3]) ? null : (String) result[3]);
				emailTemplateDto.setOwnerId(ServicesUtil.isEmpty(result[4]) ? null : result[4].toString());
				emailTemplateDto.setProcessName(ServicesUtil.isEmpty(result[2]) ? null : result[2].toString());
				emailTemplateDto.setStatus(ServicesUtil.isEmpty(result[5]) ? null : result[5].toString());
				emailTemplateDto.setTaskName(ServicesUtil.isEmpty(result[1]) ? null : result[1].toString());
				emailTemplateDto.setTemplateId(ServicesUtil.isEmpty(result[0]) ? null : result[0].toString());
				emailTemplateDto.setTemplateName(ServicesUtil.isEmpty(result[6]) ? null : result[6].toString());
				emailTemplateDto.setTemplateType(ServicesUtil.isEmpty(result[7]) ? null : result[7].toString());
				emailTemplateDto.setLatestVersion(ServicesUtil.isEmpty(result[18]) ? 0 : (int) result[18]);
				emailContent = new EmailContent();
				emailContent.setBcc(ServicesUtil.isEmpty(result[10]) ? "" : result[10].toString());
				emailContent.setCc(ServicesUtil.isEmpty(result[11]) ? "" : result[11].toString());
				emailContent.setFrameId(ServicesUtil.isEmpty(result[12]) ? null : result[12].toString());
				emailContent.setIsAttachment(
						ServicesUtil.isEmpty(result[13]) ? null : (boolean)result[13] );
				emailContent.setIsTableContent(
						ServicesUtil.isEmpty(result[14]) ? null : (boolean) result[14] );
				emailContent.setMessageBody(ServicesUtil.isEmpty(result[15]) ? null : result[15].toString());
				emailContent.setSubject(ServicesUtil.isEmpty(result[16]) ? null : result[16].toString());
				emailContent.setTemplateId(ServicesUtil.isEmpty(result[0]) ? null : result[0].toString());
				emailContent.setTo(ServicesUtil.isEmpty(result[9]) ? "" : result[9].toString());
				emailContent.setAttachmentId(ServicesUtil.isEmpty(result[8]) ? null : result[8].toString());
				emailContent.setTableContentId(ServicesUtil.isEmpty(result[17]) ? null : result[17].toString());
				emailContent.setVersion((ServicesUtil.isEmpty(result[18]) ? 0 : (int) result[18]));
				emailTemplate.setEmailContent(emailContent);
				emailTemplate.setEmailTemplate(emailTemplateDto);
				if (emailTemplate != null) {

					res.setMessage("Template Details Fetched");
					res.setStatusCode(PMCConstant.CODE_SUCCESS);
					res.setStatus(PMCConstant.SUCCESS);
				}
				emailTemplate.setResponseMessage(res);

			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev][No Email Template is There:" + e);
		}

		return emailTemplate;

	}

	public void updateStatus(String templateId, int version) {
		Session session = null;
		session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		version = version - 1;
		String updateStatus = "UPDATE EMAIL_TEMPLATE SET STATUS ='Deactivated' WHERE TEMPLATE_ID = '" + templateId
				+ "' AND VERSION = '" + version + "' ";
		this.getSession().createSQLQuery(updateStatus).executeUpdate();
		tx.commit();
		session.close();
	}
}
