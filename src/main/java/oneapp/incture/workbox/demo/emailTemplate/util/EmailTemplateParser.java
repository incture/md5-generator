package oneapp.incture.workbox.demo.emailTemplate.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeValuesTableDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dao.UserIdpMappingDao;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.document.dto.AttachmentDetail;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.document.service.DocumentServiceImpl;
import oneapp.incture.workbox.demo.emailTemplate.Dao.AttachmentDao;
import oneapp.incture.workbox.demo.emailTemplate.Dao.CustomAttrValueDao;
import oneapp.incture.workbox.demo.emailTemplate.Dao.EmailTableContentDao;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttachmentResponseDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttachmentsDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttributeValues;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailContent;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailContentDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailContentValuesDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTableContentDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplate;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTemplateCreation;
import oneapp.incture.workbox.demo.emailTemplate.Dto.MessageContent;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;



@Component
public class EmailTemplateParser {

	@Autowired
	DocumentServiceImpl documentServiceImpl;

	@Autowired
	EmailTableContentDao emailTableContentDao;

	@Autowired
	UserIdpMappingDao userIdpMappingDao;

	@Autowired
	PropertiesConstants getProperty;

	@Autowired
	CustomAttrValueDao customAttrValueDao;

	@Autowired
	TaskOwnersDao taskOwnersDao;

	@Autowired
	ProcessEventsDao processEventsDao;

	@Autowired
	CustomAttributeValuesTableDao attributeValuesTableDao;
	
	@Autowired
	SharepointUploadFile sharepointUploadFile;

	@Autowired
	AttachmentDao attachmentDao;

	public EmailTemplateCreation createEmailDtos(EmailTemplate emailTemplate) {
		EmailTemplateCreation templateCreation = new EmailTemplateCreation();
		EmailContentDto emailContentDto = null;
		List<AttachmentsDto> attachmentsDtos = null;
		AttachmentsDto attachmentsDto = null;
		String templateId = "";
		int version;
		
		List<EmailTableContentDto> emailTableContentDtos = null;
		StringBuilder deleteAttachment = new StringBuilder();
		try{
			if(ServicesUtil.isEmpty(emailTemplate.getEmailTemplate().getTemplateId())){
				templateId = UUID.randomUUID().toString().replace("-", "");
			    version=1;
			
			}	    
			else{
			templateId = emailTemplate.getEmailTemplate().getTemplateId();
			version=emailTemplate.getEmailTemplate().getLatestVersion() +1;
			
			}
			emailTemplate.getEmailTemplate().setTemplateId(templateId);
			templateCreation.setEmailTemplateDto(emailTemplate.getEmailTemplate());

			if(!ServicesUtil.isEmpty(emailTemplate.getEmailContent())){
				EmailContent emailContent = emailTemplate.getEmailContent();
				emailContentDto = new EmailContentDto();
				emailContentDto.setTemplateId(templateId);
				emailContentDto.setVersion(version);
				emailContentDto.setTo(ServicesUtil.isEmpty(emailContent.getTo())?null:String.join(",", emailContent.getTo()));
				emailContentDto.setCc(ServicesUtil.isEmpty(emailContent.getCc())?null:String.join(",", emailContent.getCc()));
				emailContentDto.setBcc(ServicesUtil.isEmpty(emailContent.getBcc())?null:String.join(",", emailContent.getBcc()));
				emailContentDto.setSubject(emailContent.getSubject());
				emailContentDto.setMessageBody(emailContent.getMessageBody());
				emailContentDto.setIsAttachment(emailContent.getIsAttachment());
				if(emailContentDto.getIsAttachment() && !ServicesUtil.isEmpty(emailContent.getAttachmentDetail())){
					
					List<AttachmentRequestDto> attachmentRequestDtos = new ArrayList<>();
					AttachmentRequestDto attachmentRequestDto = null;
					for (AttachmentResponseDto attach : emailContent.getAttachmentDetail()) {
						if(!ServicesUtil.isEmpty(attach.getEncodedFile())){
							attachmentRequestDto = new AttachmentRequestDto();
		            
							attachmentRequestDto.setEncodedFileContent(attach.getEncodedFile());
							attachmentRequestDto.setFileName(attach.getDocumentName());
							attachmentRequestDto.setFileSize(attach.getDocumentSize());
							attachmentRequestDto.setFileType(attach.getDocumentType());
							attachmentRequestDtos.add(attachmentRequestDto);
						}
						if(!ServicesUtil.isEmpty(attach.getIsDeleted())){
							if(attach.getIsDeleted()){
								deleteAttachment.append("'");
								deleteAttachment.append(attach.getDocumentId());
								deleteAttachment.append("',");
							}
						}
					}
					attachmentDao.deleteAttachments(deleteAttachment.toString());
					DocumentResponseDto responseDto = sharepointUploadFile.uploadFile(attachmentRequestDtos,attachmentRequestDto.getFileName());
					attachmentsDtos = new ArrayList<AttachmentsDto>();
					String attachmentId = "";
					if(ServicesUtil.isEmpty(emailContent.getAttachmentId()))
						attachmentId = UUID.randomUUID().toString().replace("-", "");
					else
						attachmentId = emailContent.getAttachmentId();

					for (AttachmentDetail attachment : responseDto.getAttachmentIds()) {
						attachmentsDto = new AttachmentsDto();
						attachmentsDto.setAttachmentId(attachmentId);
						attachmentsDto.setDocumentId(attachment.getAttachmentId());
						attachmentsDto.setDocumentName(attachment.getAttachmentName());
						attachmentsDto.setDocumentSize(attachment.getAttachmentSize());
						attachmentsDto.setDocumentType(attachment.getAttachmentType());
						attachmentsDtos.add(attachmentsDto);

					}
					emailContentDto.setAttachmentId(attachmentId);
				}
				emailContentDto.setIsTableContent(emailContent.getIsTableContent());
				if(emailContentDto.getIsTableContent()){
					emailTableContentDtos = new ArrayList<>();
					for (EmailTableContentDto emailTableContentDto : emailTemplate.getEmailContent().getEmailTableContent()){
						if(!emailTableContentDto.getIsDeleted()){
							emailTableContentDto.setVersion(version);
							emailTableContentDtos.add(emailTableContentDto);
						}
						else
							emailTableContentDao.delete(emailTableContentDto);
					}
					emailContentDto.setTableContentId(emailContent.getEmailTableContent().get(0).getTableContentId());
				}
                templateCreation.getEmailTemplateDto().setLatestVersion(version);
				templateCreation.setAttachmentsDtos(attachmentsDtos);
				templateCreation.setEmailContentDto(emailContentDto);
				templateCreation.setEmailTableContentDto(emailTableContentDtos);

			}

		}catch (Exception e) {
			System.err.println("[WBP-Dev] Creation of Email Template Dtos error :"+e);
		}
		return templateCreation;
	}
	
	public static void main(String args[]){
		String str = "{value1} {value2}";
		str = str.trim().replace("{", "").replace("}", ",").replace(" ", "");
		str = str.substring(0, str.length()-1);
		System.err.println(str);
	}

	public void sendEmail(EmailTemplate emailTemplate,String eventId) {
		String receiver = null;
		String cc = null;
		String bcc = null;
		String[] mailIds = null;
		StringBuilder emails = new StringBuilder();

		EmailContent content = emailTemplate.getEmailContent();
		Map<String,AttributeValues> cusAttr = customAttrValueDao.getCustAttr(eventId);
		System.err.println("custom attributes set");
		Map<String,String> userDetails = userIdpMappingDao.getUsers();
		List<String> taskOwnerEmails = taskOwnersDao.getTaskOwnersEmails(eventId);
		System.err.println("task owner set");
		String creator = processEventsDao.getCreatorEmail(eventId);
		receiver = ServicesUtil.isEmpty(content.getTo())?"":content.getTo();
		receiver = receiver.trim().replace("{", "").replace("}", ",").replace(" ", "");//String.join(",", content.getTo()).replace("${", "").replace("}", "");
		receiver = receiver.substring(0, receiver.length()-1);//receiver.replace("-", ".");

		if(receiver.contains(",")){
			mailIds = receiver.split(",");

			for (String str : mailIds) {
				System.err.println(str);
				if("taskOwner".equalsIgnoreCase(str) || "creator".equalsIgnoreCase(str)
						|| "Approver".equalsIgnoreCase(str) || "Task Creator".equalsIgnoreCase(str)){
					if("taskOwner".equalsIgnoreCase(str) || "Approver".equalsIgnoreCase(str)){
						System.err.println(str);
						str = str.replace(str, String.join(",",taskOwnerEmails));
						System.err.println(str);
					}else{
						System.err.println(str);
						str = str.replace(str, creator);
						System.err.println(str);
					}
				}
				else
					str = str.replace(str, userDetails.get(cusAttr.get(str)));

				emails.append(str);
				emails.append(",");
			}
			System.err.println("emails"+emails);
			receiver = emails.substring(0, emails.length()-1);
		}else{
			if("taskOwner".equalsIgnoreCase(receiver) || "creator".equalsIgnoreCase(receiver)
					|| "Approver".equalsIgnoreCase(receiver) || "Task Creator".equalsIgnoreCase(receiver)){
				if("taskOwner".equalsIgnoreCase(receiver) || "Approver".equalsIgnoreCase(receiver)){
					receiver = String.join(",",taskOwnerEmails);
				}else{
					receiver = creator;
				}
			}
			else
				receiver = userDetails.get(cusAttr.get(receiver));
		}

		if(!ServicesUtil.isEmpty(content.getCc()))
		{
			System.err.println(content.getCc());
			cc = ServicesUtil.isEmpty(content.getCc())?"":content.getCc();
			emails = new StringBuilder();
			cc = cc.trim().replace("{", "").replace("}", ",").replace(" ", "");
			cc = cc.substring(0, cc.length()-1);
			if(cc.contains(",")){
				mailIds = cc.split(",");
				for (String str : mailIds) {
					if("taskOwner".equalsIgnoreCase(str) || "creator".equalsIgnoreCase(str)
							|| "Approver".equalsIgnoreCase(str) || "Task Creator".equalsIgnoreCase(str)){
						if("taskOwner".equalsIgnoreCase(str) || "Approver".equalsIgnoreCase(str)){
							str = str.replace(str, String.join(",",taskOwnerEmails));
							System.err.println(str);
						}else{
							str = str.replace(str, creator);
							System.err.println(str);
						}
					}
					else
						str = str.replace(str, userDetails.get(cusAttr.get(str)));

					emails.append(str);
					emails.append(",");
				}
				cc = emails.substring(0, emails.length()-1);
				System.err.println("cc emails"+cc);
			}else{
				System.err.println();
				if("taskOwner".equalsIgnoreCase(cc) || "creator".equalsIgnoreCase(cc)
						|| "Approver".equalsIgnoreCase(cc) || "Task Creator".equalsIgnoreCase(cc)){
					if("taskOwner".equalsIgnoreCase(cc) || "Approver".equalsIgnoreCase(cc)){
						cc = String.join(",",taskOwnerEmails);
					}else{
						cc = creator;
					}
				}
				else
					cc = userDetails.get(cusAttr.get(cc));
			}
		}

		if(!ServicesUtil.isEmpty(content.getBcc()))
		{
			bcc = ServicesUtil.isEmpty(content.getBcc())?"":content.getBcc();
			emails = new StringBuilder();
			bcc = bcc.trim().replace("{", "").replace("}", ",").replace(" ", "");
			bcc = bcc.substring(0, bcc.length()-1);
			if(bcc.contains(",")){
				mailIds = bcc.split(",");
				for (String str : mailIds) {
					if("taskOwner".equalsIgnoreCase(str) || "creator".equalsIgnoreCase(str)
							|| "Approver".equalsIgnoreCase(str) || "Task Creator".equalsIgnoreCase(str)){
						if("taskOwner".equalsIgnoreCase(str) || "Approver".equalsIgnoreCase(str)){
							str = str.replace(str, String.join(",",taskOwnerEmails));
							System.err.println(str);
						}else{
							str = str.replace(str, creator);
							System.err.println(str);
						}
					}
					else
						str = str.replace(str, userDetails.get(cusAttr.get(str)));

					emails.append(str);
					emails.append(",");
				}				

				bcc = emails.substring(0, emails.length()-1);
			}else{
				if("taskOwner".equalsIgnoreCase(bcc) || "creator".equalsIgnoreCase(bcc)
						|| "Approver".equalsIgnoreCase(bcc) || "Task Creator".equalsIgnoreCase(bcc)){
					if("taskOwner".equalsIgnoreCase(bcc) || "Approver".equalsIgnoreCase(bcc)){
						bcc = String.join(",",taskOwnerEmails);
					}else{
						bcc = creator;
					}
				}
				else
					bcc = userDetails.get(cusAttr.get(bcc));
			}
		}
		System.err.println("[WBP-Dev]Sending mail to " + receiver);
		System.err.println("[WBP-Dev]Sending mail cc " + cc);
		System.err.println("[WBP-Dev]Sending mail bcc " + bcc);

		if(content.getSubject().contains("$")){
			String[] message = content.getSubject().split("[$]");
			StringBuilder messageOutput = new StringBuilder("");
			String taskOwnerName = taskOwnersDao.getTaskOwnersName(eventId);
			String creatorName = processEventsDao.getCreatorName(eventId);

			for (String str : message) {
				if (str.contains("{")) {
					String s = str.substring(str.indexOf("{")+1, str.indexOf("}"));
					if(cusAttr.containsKey(s))
						str = str.replace("{"+s+"}", cusAttr.get(s).getAttributeValue());
					else if("taskOwner".equalsIgnoreCase(s))
						str = str.replace("{"+s+"}", taskOwnerName);
					else if("creator".equalsIgnoreCase(s))
						str = str.replace("{"+s+"}", creatorName);
				}
				messageOutput.append(str);
			}

			content.setSubject(messageOutput.toString());
		}
		System.err.println(content);
		sendMail(content,receiver,cc,bcc);

	}

	private void sendMail(EmailContent content, String receiver, String cc, String bcc) {
		final String FROM_MAIL_ID = getProperty.getValue("FROM_MAIL_ID");
		final String FROM_MAIL_ID_PASSWORD = getProperty.getValue("FROM_MAIL_ID_PASSWORD");
		System.err.println(FROM_MAIL_ID+FROM_MAIL_ID_PASSWORD);
		String MAIL_HOST_NAME = "smtp.gmail.com";
		String MAIL_PORT_NUMBER = "587";

		Properties prop = new Properties();
		prop.put("mail.smtp.host", MAIL_HOST_NAME);
		prop.put("mail.smtp.port", MAIL_PORT_NUMBER);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", MAIL_PORT_NUMBER);
		prop.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM_MAIL_ID, FROM_MAIL_ID_PASSWORD);
			}
		});

		try {

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setContent(content.getMessageBody(), "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if(!ServicesUtil.isEmpty(content.getAttachmentDetail())){
				for (AttachmentResponseDto attachment : content.getAttachmentDetail()) {
					System.err.println("Test atachment getEncodedFile"+attachment.getEncodedFile());
					System.err.println("Test atachment getDocumentName"+attachment.getDocumentName());
					System.err.println("Test atachment getDocumentType"+attachment.getDocumentType());
					messageBodyPart = new MimeBodyPart();
					String filename = attachment.getDocumentName();
					String fileBase64 = attachment.getEncodedFile();
					byte[] imgBytes = Base64.getDecoder().decode(fileBase64);
					DataSource source = new ByteArrayDataSource(imgBytes, attachment.getDocumentType());
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(filename);
					multipart.addBodyPart(messageBodyPart);
				}
			}
			System.err.println("here email");
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM_MAIL_ID));
			if(!ServicesUtil.isEmpty(receiver))
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
			if(!ServicesUtil.isEmpty(cc))
				message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			if(!ServicesUtil.isEmpty(bcc))
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
			System.err.println("here email2");
			message.setContent(multipart);
			message.setSubject(content.getSubject());
			System.err.println("here email3");
			Transport.send(message);

			System.err.println("[WBP-Dev]Mail Sent Successfully to " + receiver);

		} catch (Exception e) {

			System.err.println("[WBP-Dev][WORKBOX-NEW]EMAIL NOTIFICATION ERROR" + e);
			e.printStackTrace();
		}
	}

	public String createTable(List<EmailTableContentDto> emailTableContent, Map<String, AttributeValues> cusAttr, String eventId) {
		String tableStart = "<table style=\\\"font-size: 15px; font-family: LatoWeb; width: 100%; border-collapse: collapse; border: 1px solid black\\\" border=\\\"1\\\" cellspacing=\\\"2\\\" cellpadding=\\\"2\\\" class=\\\"ze_tableView\\\"><tbody>";
		String trStart = "<tr>\r\n";
		String trEnd = "</tr>\r\n";
		String headerStart = "  <th>  ";
		String headerEnd = "</th>\r\n";
		String valueStart = "<td>";
		String valueEnd = "</td>\r\n";
		String tableEnd = "</tbody></table>";
		StringBuilder tableContent = new StringBuilder();
		Boolean rowContentFlag = false;
		Map<Integer,Map<String,String>> cusTableValues = null;
		Integer rowCount = attributeValuesTableDao.getTableRowCount(eventId);
		if(ServicesUtil.isEmpty(rowCount))
			rowCount = 0;
		if(rowCount >=1)
		{
			cusTableValues = attributeValuesTableDao.getTableDetails(eventId);
			rowContentFlag = checkTableItems(emailTableContent,cusTableValues);
		}

		if(!ServicesUtil.isEmpty(emailTableContent)){
			tableContent.append(tableStart);
			tableContent.append(trStart);
			for (EmailTableContentDto header : emailTableContent) {
				tableContent.append(headerStart);
				tableContent.append(header.getColumnName().trim());
				tableContent.append(headerEnd);
			}
			tableContent.append(trEnd);
			if(rowContentFlag == false){
				tableContent.append(trStart);
				for (EmailTableContentDto header : emailTableContent) {
					tableContent.append(valueStart);
					tableContent.append((header.getColumnValueField().contains("$")?cusAttr.get(header.getColumnValueField().replace("${", "").replace("}", "")):header.getColumnValueField()));
					tableContent.append(valueEnd);
				}
				tableContent.append(trEnd);
			}else {
				for(int i=1;i<=rowCount;i++){
					tableContent.append(trStart);
					for (EmailTableContentDto header : emailTableContent) {
						tableContent.append(valueStart);
						String value = header.getColumnValueField().contains("$")?
								(cusTableValues.get(i).containsKey(header.getColumnValueField().replace("${", "").replace("}", "")))?
										cusTableValues.get(i).get(header.getColumnValueField().replace("${", "").replace("}", ""))
										:(cusAttr.containsKey(header.getColumnValueField().replace("${", "").replace("}", "")))?
												cusAttr.get(header.getColumnValueField().replace("${", "").replace("}", "")).getAttributeValue()
												:""
													:header.getColumnValueField();
										tableContent.append(value);
										tableContent.append(valueEnd);
					}
					tableContent.append(trEnd);
				}
			}
			tableContent.append(tableEnd);
		}

		System.err.println(tableContent.toString());
		return tableContent.toString();
	}

	private Boolean checkTableItems(List<EmailTableContentDto> emailTableContent,
			Map<Integer, Map<String, String>> cusTableValues) {

		for (EmailTableContentDto contentDto : emailTableContent) {
			if(cusTableValues.get(1).containsKey(contentDto.getColumnValueField().replace("${", "").replace("}", ""))){
				return true;
			}
		}
		return false;
	}

	public MessageContent setMessageBody(String messageBody, List<EmailTableContentDto> tableContent,String eventId) {
		MessageContent msgContent = new MessageContent();
		String[] message = messageBody.split("[$]");
		Map<String,AttributeValues> cusAttr = customAttrValueDao.getCustAttr(eventId);//eventId
		StringBuilder messageOutput = new StringBuilder();
		String taskOwnerName = taskOwnersDao.getTaskOwnersName(eventId);
		String creator = processEventsDao.getCreatorName(eventId);
		List<String> attachmentIds = new ArrayList<>();
		List<AttachmentResponseDto> attachmentresponseDtos = null;
		for (String str : message) {
			if (str.contains("{")) {
				String s = str.substring(str.indexOf("{")+1, str.indexOf("}"));
				if(cusAttr.containsKey(s)){
					if("ATTACHMENT".equalsIgnoreCase(cusAttr.get(s).getDataType())){
						str = str.replace("{"+s+"}", "");
						JSONArray jsonArray=new JSONArray(cusAttr.get(s).getAttributeValue());
						for(Object obj:jsonArray){
							if(obj instanceof JSONObject){
								JSONObject jobj=(JSONObject) obj;
								attachmentIds.add(jobj.optString("attachmentId"));
							}
						}
					}
					else
						str = str.replace("{"+s+"}", cusAttr.get(s).getAttributeValue());
				}
				else if("taskOwner".equalsIgnoreCase(s) || "Approver".equalsIgnoreCase(s))
					str = str.replace("{"+s+"}", taskOwnerName);
				else if("creator".equalsIgnoreCase(s) || "Task Creator".equalsIgnoreCase(s))
					str = str.replace("{"+s+"}", creator);
			}
			messageOutput.append(str);
		}
		if(!ServicesUtil.isEmpty(attachmentIds)){
			attachmentresponseDtos = 
					attachmentDao.getAttachmentDetails(attachmentIds);
			Gson g  = new Gson();
			System.err.println("[WBP-Dev] attachmentresponseDtos "+g.toJson(attachmentresponseDtos));
		}
		messageBody = String.join(" ", messageOutput);
		messageBody = messageBody+createTable(tableContent,cusAttr,eventId);
		msgContent.setMessageBody(messageBody);
		msgContent.setAttributesDetails(attachmentresponseDtos);
		return msgContent;
	}

	public EmailContentValuesDto archiveContent(EmailContent emailContent,String eventId) {
		EmailContentValuesDto contentValuesDto = new EmailContentValuesDto();

		if (!ServicesUtil.isEmpty(emailContent.getTo()))
			contentValuesDto.setTo(String.join(",", emailContent.getTo()));
		if (!ServicesUtil.isEmpty(emailContent.getTemplateId()))
			contentValuesDto.setTemplateId(emailContent.getTemplateId());
		if (!ServicesUtil.isEmpty(emailContent.getAttachmentId()))
			contentValuesDto.setAttachmentId(emailContent.getAttachmentId());
		if (!ServicesUtil.isEmpty(emailContent.getBcc()))
			contentValuesDto.setBcc(String.join(",", emailContent.getBcc()));
		if (!ServicesUtil.isEmpty(emailContent.getCc()))
			contentValuesDto.setCc(String.join(",", emailContent.getCc()));
		if (!ServicesUtil.isEmpty(emailContent.getFrameId()))
			contentValuesDto.setFrameId(emailContent.getFrameId());
		if (!ServicesUtil.isEmpty(emailContent.getMessageBody()))
			contentValuesDto.setMessageBody(emailContent.getMessageBody());
		if (!ServicesUtil.isEmpty(emailContent.getSubject()))
			contentValuesDto.setSubject(emailContent.getSubject());
		if (!ServicesUtil.isEmpty(emailContent.getTableContentId()))
			contentValuesDto.setTableContentId(emailContent.getTableContentId());
		if (!ServicesUtil.isEmpty(emailContent.getIsAttachment()))
			contentValuesDto.setIsAttachment(emailContent.getIsAttachment());
		if (!ServicesUtil.isEmpty(emailContent.getIsTableContent()))
			contentValuesDto.setIsTableContent(emailContent.getIsTableContent());
		if (!ServicesUtil.isEmpty(eventId))
			contentValuesDto.setEventId(eventId);

		return contentValuesDto;
	}

}
