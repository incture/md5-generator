package oneapp.incture.workbox.demo.document.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.document.dto.AttachmentDetail;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.document.util.DocumentServiceUtil;


@Service
public class DocumentServiceImpl implements DocumentService {
	
	@Autowired
	DocumentServiceUtil documentServiceUtil;

	@Override
	public DocumentResponseDto uploadDocument(List<AttachmentRequestDto> attachmentList) {

		DocumentResponseDto response = new DocumentResponseDto();

		 List<AttachmentDetail>  attachmentIds=new ArrayList<>();
		 AttachmentDetail attachmentDetail=null;
		String objectId = null;
		boolean virusFlag = false;
		List<AttachmentRequestDto> attachments = new ArrayList<>();

		ResponseMessage respMessage = new ResponseMessage();
		respMessage.setMessage("Document upload failed . ");
		respMessage.setStatus(PMCConstant.FAILURE);
		respMessage.setStatusCode(PMCConstant.CODE_FAILURE);

		try {
			for (AttachmentRequestDto attachment :attachmentList) {

				attachmentDetail=new AttachmentDetail();
				long start=System.currentTimeMillis();
				objectId = DocumentServiceUtil.uploadToDocumentService(attachment.getEncodedFileContent(),
						attachment.getFileName(), attachment.getFileType());
				
				System.err.println("[WBP-Dev]DocumentServiceImpl.uploadDocument() file "+attachment.getFileName()+" time : "+(System.currentTimeMillis()-start));
				
				if (!ServicesUtil.isEmpty(objectId)) {
					attachmentDetail.setAttachmentId(objectId);
					attachmentDetail.setAttachmentType(attachment.getFileType());
					attachmentDetail.setAttachmentName(attachment.getFileName());
					attachmentDetail.setAttachmentSize(attachment.getFileSize());
					System.err.println("[WBP-Dev]DocumentServiceImpl.uploadDocument() Object Id : " + objectId);
				} else {
					virusFlag = true;
					attachments.add(attachment);
				}

				attachmentIds.add(attachmentDetail);
			}
			
			if (!virusFlag) {
				respMessage.setMessage("Document upload succefully . ");
				respMessage.setStatus(PMCConstant.SUCCESS);
				respMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
				System.err.println("[WBP-Dev]DocumentServiceImpl.uploadDocument() Object Id : " + objectId);
			} else {
				respMessage.setMessage("Document upload failed . ");
				respMessage.setStatus(PMCConstant.FAILURE);
				respMessage.setStatusCode(PMCConstant.CODE_FAILURE);
			}

			response.setAttachmentIds(attachmentIds);
			response.setAttachments(attachments);
			response.setResponse(respMessage);

		} catch (Exception e) {
			System.err.println("[WBP-Dev]DocumentServiceImpl.uploadDocument() error : " + e);
			respMessage.setMessage("Document upload failed . " + e);
			respMessage.setStatus(PMCConstant.FAILURE);
			respMessage.setStatusCode(PMCConstant.CODE_FAILURE);
			response.setResponse(respMessage);
		}

		return response;
	
	}

	@Override
	public AttachmentRequestDto getOriginalAttachment(String objectId) {
		
		return documentServiceUtil.getOriginalDocument(objectId);
	}

}
