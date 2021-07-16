package oneapp.incture.workbox.demo.emailTemplate.Dto;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class AttachmentsDto extends BaseDto{

	private String attachmentId;
	private String documentId;
	private String documentType;
	private String documentName;
	private Integer documentSize;
	private byte[] compressedImg;
	
	public Integer getDocumentSize() {
		return documentSize;
	}
	public void setDocumentSize(Integer documentSize) {
		this.documentSize = documentSize;
	}
	public byte[] getCompressedImg() {
		return compressedImg;
	}
	public void setCompressedImg(byte[] compressedImg) {
		this.compressedImg = compressedImg;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	@Override
	public String toString() {
		return "AttachmentsDto [attachmentId=" + attachmentId + ", documentId=" + documentId + ", documentType="
				+ documentType + ", documentName=" + documentName + ", documentSize=" + documentSize
				+ ", compressedImg=" + Arrays.toString(compressedImg) + "]";
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	@Override
	public Boolean getValidForUsage() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void validate(EnOperation enOperation) throws InvalidInputFault {
		// TODO Auto-generated method stub
		
	}
	
	
}
