package oneapp.incture.workbox.demo.emailTemplate.Dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.entity.AttachmentsDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.service.DocumentServiceImpl;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttachmentResponseDto;
import oneapp.incture.workbox.demo.emailTemplate.Dto.AttachmentsDto;

@Repository
public class AttachmentDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	DocumentServiceImpl documentService;

	public Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}

	protected AttachmentsDo importDto(AttachmentsDto fromDto) {
		AttachmentsDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getAttachmentId())
				&& !ServicesUtil.isEmpty(fromDto.getDocumentId())) {
			entity = new AttachmentsDo();
			entity.setAttachmentId(fromDto.getAttachmentId());
			entity.setDocumentId(fromDto.getDocumentId());
			if (!ServicesUtil.isEmpty(fromDto.getDocumentType()))
				entity.setDocumentType(fromDto.getDocumentType());
			if (!ServicesUtil.isEmpty(fromDto.getDocumentName()))
				entity.setDocumentName(fromDto.getDocumentName());
			if (!ServicesUtil.isEmpty(fromDto.getCompressedImg()))
				entity.setCompressedImg(fromDto.getCompressedImg());
			if (!ServicesUtil.isEmpty(fromDto.getDocumentSize()))
				entity.setDocumentSize(fromDto.getDocumentSize());
		}
		return entity;
	}

	public void saveOrUpdateAttachments(List<AttachmentsDto> attachmentsDtos) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			if (!ServicesUtil.isEmpty(attachmentsDtos) && !attachmentsDtos.isEmpty()) {
				session = this.getSession();
				for (int i = 0; i < attachmentsDtos.size(); i++) {
					AttachmentsDto currentTask = attachmentsDtos.get(i);
					session.saveOrUpdate(importDto(currentTask));
					if (i % 20 == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				tx.commit();
				session.close();
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Email Template][INSERTING Attachemnts] ERROR:" + e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<AttachmentsDo> getAttachments(String attachmentId) {
		try {
			Session session=sessionFactory.openSession();
			String fetchStr = "select p from AttachmentsDo p where p.attachmentId= :val";
			Query q = session.createQuery(fetchStr);
			q.setParameter("val", attachmentId);
			List<AttachmentsDo> result = q.list();
			System.err.println(result);
			return result;
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Email Template]getAttachments ERROR:" + e);
		}
		return new ArrayList<>();
	}

	public void deleteAttachments(String documentIds) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query deleteQry = this.getSession().createSQLQuery("DELETE FROM ATTACHMENTS WHERE " + "DOCUMENT_ID = ("
					+ documentIds.substring(0, documentIds.length() - 1) + ")");
			deleteQry.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Email Template]delete attachments ERROR:" + e);
		}
	}

	public void deleteAllAttahments(String templateId) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query deleteQry = session
					.createSQLQuery("DELETE FROM ATTACHMENTS WHERE "
							+ "ATTACHMENT_ID IN (SELECT ATTACHMENT_ID FROM EMAIL_CONTENT WHERE TEMPLATE_ID = '"
							+ templateId + "')");
			
			deleteQry.executeUpdate();
			tx.commit();
			session.close();
			
		} catch (Exception e) {
			System.err.println("[WBP-Dev][Email Template]delete All attachments ERROR:" + e);
		}
	}

	public List<AttachmentResponseDto> getAttachmentDetails(List<String> attachmentIds) {
		List<AttachmentResponseDto> attachmentResponseDtos = new ArrayList<>();
		AttachmentResponseDto attachmentResponseDto = null;
		AttachmentRequestDto attachmentRequestDto = null;
		try {

			for (String attachments : attachmentIds) {
				attachmentRequestDto = documentService.getOriginalAttachment(attachments);
				attachmentResponseDto = new AttachmentResponseDto();
				attachmentResponseDto.setDocumentName(attachmentRequestDto.getFileName());
				attachmentResponseDto.setDocumentSize(attachmentRequestDto.getFileSize());
				attachmentResponseDto.setDocumentType("image/*");
				attachmentResponseDto.setEncodedFile(attachmentRequestDto.getEncodedFileContent());
				attachmentResponseDtos.add(attachmentResponseDto);
			}
		} catch (Exception e) {
			System.err.println("[WBP-DEV]EmailTemplateDao.getAttachmentDetails() error :" + e);
		}
		return attachmentResponseDtos;
	}

}
