package oneapp.incture.workbox.demo.versionControl.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentDto {

	private String encodedFileContent;  // base64 encoded content
	private String fileName;
	private String fileType;           // MIME type 
	private byte[] file;
	private String documentID;
	private String commentID;
	private String versionID;
	
	public String getVersionID() {
		return versionID;
	}

	public void setVersionID(String versionID) {
		this.versionID = versionID;
	}

	public String getEncodedFileContent() {
		return encodedFileContent;
	}

	public void setEncodedFileContent(String encodedFileContent) {
		this.encodedFileContent = encodedFileContent;
	}

	public String getDocumentID() {
		return documentID;
	}

	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}

	public String getCommentID() {
		return commentID;
	}

	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	
	public String getAttachmentName() {
		return fileName;
	}
	public void setAttachmentName(String attachmentName) {
		this.fileName = attachmentName;
	}

	public String getAttachmentType() {
		return fileType;
	}
	public void setAttachmentType(String attachmentType) {
		this.fileType = attachmentType;
	}
	
	
	@Override
	public String toString() {
		return "AttachmentDto [encodedFileContent=" + encodedFileContent + ", fileName=" + fileName + ", fileType="
				+ fileType + ", file=" + Arrays.toString(file) + ", documentID=" + documentID + ", commentID="
				+ commentID + ", versionID=" + versionID + "]";
	}
}
