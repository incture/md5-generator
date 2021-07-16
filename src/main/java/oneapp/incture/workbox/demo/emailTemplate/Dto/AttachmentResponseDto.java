package oneapp.incture.workbox.demo.emailTemplate.Dto;

public class AttachmentResponseDto {

	String documentId;
	String documentName;
	String documentType;
	Integer documentSize;
	String encodedFile;
	Boolean isDeleted;
	@Override
	public String toString() {
		return "AttachmentResponseDto [documentId=" + documentId + ", documentName=" + documentName + ", documentType="
				+ documentType + ", documentSize=" + documentSize + ", encodedFile=" + encodedFile + ", isDeleted="
				+ isDeleted + "]";
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public Integer getDocumentSize() {
		return documentSize;
	}
	public void setDocumentSize(Integer documentSize) {
		this.documentSize = documentSize;
	}
	public String getEncodedFile() {
		return encodedFile;
	}
	public void setEncodedFile(String encodedFile) {
		this.encodedFile = encodedFile;
	}
	
	
	
}
