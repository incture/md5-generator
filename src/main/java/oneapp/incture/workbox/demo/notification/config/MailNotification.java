package oneapp.incture.workbox.demo.notification.config;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.notification.util.NotificationConstant;

@Component
public class MailNotification {
	
	@Autowired
	PropertiesConstants getProperty;

	public ResponseMessage mail(String receiver, String subject, String text) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(NotificationConstant.FAILURE);
		responseMessage.setStatus(NotificationConstant.FAILURE);
		responseMessage.setStatusCode(NotificationConstant.STATUS_CODE_FAILURE);

		receiver = receiver.replace("-", ".");
		System.err.println("[WBP-Dev]Sending mail to " + receiver);

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

			String appURl = text + "<br><br>" + "Click "+"<a href=\""+getProperty.getValue("APP_URL")+"\">here</a>"+" to View Task." 
																			+ "<br><br>";
			

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			messageBodyPart.setContent(appURl, "text/html");
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM_MAIL_ID));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
			message.setContent(multipart);
			message.setSubject(subject);
			Transport.send(message);

			responseMessage.setMessage("Mail Sent");
			responseMessage.setStatus(NotificationConstant.SUCCESS);
			responseMessage.setStatusCode(NotificationConstant.STATUS_CODE_SUCCESS);
			System.err.println("[WBP-Dev]Mail Sent Successfully to " + receiver);

		} catch (Exception e) {

			System.err.println("[WBP-Dev][WORKBOX-NEW]EMAIL NOTIFICATION ERROR" + e);
		}

		return null;
	}
}
