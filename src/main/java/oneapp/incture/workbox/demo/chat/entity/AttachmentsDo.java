//package oneapp.incture.workbox.demo.chat.entity;
//
//import java.io.Serializable;
//import java.util.Arrays;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;
//
//@Entity
//@Table(name = "ATTACHMENTS")
//public class AttachmentsDo implements BaseDo,Serializable{
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	
//	@Id
//	@Column(name = "ATTACHMENT_ID" , columnDefinition = "NVARCHAR(100)")
//	private String attachmentId;
//	
//	@Id
//	@Column(name = "DOCUMENT_ID" , columnDefinition = "NVARCHAR(100)")
//	private String documentId;
//	
//	@Column(name = "DOCUMENT_TYPE" , columnDefinition = "NVARCHAR(100)")
//	private String documentType;
//	
//	@Column(name = "DOCUMENT_NAME" , columnDefinition = "NVARCHAR(100)")
//	private String documentName;
//	
//	@Column(name = "COMPRESSED_IMAGE", columnDefinition = "BLOB")
//	private byte[] compressedImg;
//	
//	@Column(name = "DOCUMENT_SIZE", columnDefinition = "INTEGER")
//	private Integer documentSize;
//
//	public Integer getDocumentSize() {
//		return documentSize;
//	}
//
//	public void setDocumentSize(Integer documentSize) {
//		this.documentSize = documentSize;
//	}
//
//	public byte[] getCompressedImg() {
//		return compressedImg;
//	}
//
//	public void setCompressedImg(byte[] compressedImg) {
//		this.compressedImg = compressedImg;
//	}
//
//	public String getDocumentName() {
//		return documentName;
//	}
//
//	public void setDocumentName(String documentName) {
//		this.documentName = documentName;
//	}
//
//	@Override
//	public String toString() {
//		return "AttachmentsDo [attachmentId=" + attachmentId + ", documentId=" + documentId + ", documentType="
//				+ documentType + ", documentName=" + documentName + ", compressedImg=" + Arrays.toString(compressedImg)
//				+ ", documentSize=" + documentSize + "]";
//	}
//
//	public String getAttachmentId() {
//		return attachmentId;
//	}
//
//	public void setAttachmentId(String attachmentId) {
//		this.attachmentId = attachmentId;
//	}
//
//	public String getDocumentId() {
//		return documentId;
//	}
//
//	public void setDocumentId(String documentId) {
//		this.documentId = documentId;
//	}
//
//	public String getDocumentType() {
//		return documentType;
//	}
//
//	public void setDocumentType(String documentType) {
//		this.documentType = documentType;
//	}
//
//	@Override
//	public Object getPrimaryKey() {
//		return null;
//	}
//
//	
//	
//	
//}
