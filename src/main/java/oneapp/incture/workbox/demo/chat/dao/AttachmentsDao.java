package oneapp.incture.workbox.demo.chat.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.entity.AttachmentsDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class AttachmentsDao extends BaseDao<AttachmentsDo, AttachmentsDto>{

	@Override
	protected AttachmentsDo importDto(AttachmentsDto fromDto) throws InvalidInputFault, ExecutionFault, NoResultFault {
		AttachmentsDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getAttachmentId())
				&& !ServicesUtil.isEmpty(fromDto.getDocumentId())){
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

	@Override
	protected AttachmentsDto exportDto(AttachmentsDo entity) {
		AttachmentsDto attachmentsDto = new AttachmentsDto();
		attachmentsDto.setDocumentId(entity.getDocumentId());
		attachmentsDto.setAttachmentId(entity.getAttachmentId());
		attachmentsDto.setDocumentType(entity.getDocumentType());
		if (!ServicesUtil.isEmpty(entity.getDocumentName()))
			attachmentsDto.setDocumentName(entity.getDocumentName());
		if (!ServicesUtil.isEmpty(entity.getDocumentSize()))
			attachmentsDto.setDocumentSize(entity.getDocumentSize());
		return attachmentsDto;
	}
	
	public void saveOrUpdateAttachments(List<AttachmentsDto> attachmentsDtos) {
		Session session = null;
		try{
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
			}
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING Attachemnts] ERROR:"+e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public String getCompressedImage(String documentId) {
		String compressedimg = "";
		Query fetchImg = this.getSession().createQuery("select p from AttachmentsDo p where p.documentId= :id");
		fetchImg.setParameter("id", documentId);
		List<AttachmentsDo> attachmentsDos = fetchImg.list();
		if(!ServicesUtil.isEmpty(attachmentsDos.get(0).getCompressedImg()))
			compressedimg = new String(attachmentsDos.get(0).getCompressedImg());
		return compressedimg;
	}

}
