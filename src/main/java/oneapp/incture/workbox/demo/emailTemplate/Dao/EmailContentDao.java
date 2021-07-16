package oneapp.incture.workbox.demo.emailTemplate.Dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.entity.EmailContentDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailContentDto;

@Repository
public class EmailContentDao extends BaseDao<EmailContentDo, EmailContentDto> {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected EmailContentDo importDto(EmailContentDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		EmailContentDo entity = new EmailContentDo();
		entity.setTemplateId(fromDto.getTemplateId());

		if (!ServicesUtil.isEmpty(fromDto.getVersion()))
			entity.setVersion(fromDto.getVersion());

		if (!ServicesUtil.isEmpty(fromDto.getAttachmentId()))
			entity.setAttachmentId(fromDto.getAttachmentId());
		if (!ServicesUtil.isEmpty(fromDto.getBcc()))
			entity.setBcc(fromDto.getBcc());
		if (!ServicesUtil.isEmpty(fromDto.getCc()))
			entity.setCc(fromDto.getCc());
		if (!ServicesUtil.isEmpty(fromDto.getFrameId()))
			entity.setFrameId(fromDto.getFrameId());
		if (!ServicesUtil.isEmpty(fromDto.getIsTableContent()))
			entity.setIsTableContent(fromDto.getIsTableContent());
		if (!ServicesUtil.isEmpty(fromDto.getMessageBody()))
			entity.setMessageBody(fromDto.getMessageBody());
		if (!ServicesUtil.isEmpty(fromDto.getSubject()))
			entity.setSubject(fromDto.getSubject());
		if (!ServicesUtil.isEmpty(fromDto.getTableContentId()))
			entity.setTableContentId(fromDto.getTableContentId());
		if (!ServicesUtil.isEmpty(fromDto.getTo()))
			entity.setTo(fromDto.getTo());
		if (!ServicesUtil.isEmpty(fromDto.getIsAttachment()))
			entity.setIsAttachment(fromDto.getIsAttachment());
		return entity;
	}

	@Override
	protected EmailContentDto exportDto(EmailContentDo entity) {

		return null;
	}

	public void saveOrUpdateEmailContent(EmailContentDto emailContentDtos) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			if (!ServicesUtil.isEmpty(emailContentDtos)) {
				//session = this.getSession();

				EmailContentDto currentTask = emailContentDtos;
				session.saveOrUpdate(importDto(currentTask));

				session.flush();
				session.clear();

				tx.commit();
				session.close();
			}
		} catch (Exception e) {
			System.err.println("[WB-DEV]ERROR saving email content:" + e);
		}
	}

	public void deleteEmailContent(String templateId) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query deleteQry = session
					.createSQLQuery("DELETE FROM EMAIL_CONTENT WHERE " + "TEMPLATE_ID = '" + templateId + "'");
			//Transaction tx = session.beginTransaction();
			deleteQry.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Email Template]delete email content ERROR:" + e);
		}
	}

}
