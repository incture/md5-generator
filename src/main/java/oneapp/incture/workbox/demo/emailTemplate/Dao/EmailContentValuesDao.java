package oneapp.incture.workbox.demo.emailTemplate.Dao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.entity.EmailContentValuesDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailContentValuesDto;

@Repository
public class EmailContentValuesDao extends BaseDao<EmailContentValuesDo, EmailContentValuesDto>{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected EmailContentValuesDo importDto(EmailContentValuesDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		EmailContentValuesDo entity = new EmailContentValuesDo();
		entity.setTemplateId(fromDto.getTemplateId());
		entity.setEventId(fromDto.getEventId());
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
	protected EmailContentValuesDto exportDto(EmailContentValuesDo entity) {

		return null;
	}

	public void saveOrUpdateEmailContent(EmailContentValuesDto emailContentValuesDtos) {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			if (!ServicesUtil.isEmpty(emailContentValuesDtos)) {
				//session = this.getSession();

				EmailContentValuesDto currentTask = emailContentValuesDtos;
				session.saveOrUpdate(importDto(currentTask));

				session.flush();
				session.clear();

				tx.commit();
				session.close();
			}	
		}catch (Exception e) {
			System.err.println("[WB-DEV]ERROR saving email content:"+e);
		}
	}

}
