package oneapp.incture.workbox.demo.document.dto;

import java.util.Arrays;

public class AttachmentRequestDto {

	private String encodedFileContent;// base64 encoded content
	private String fileName;
	private String fileType; // MIME type 
	private Integer fileSize;
	private  byte[] file;
	

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getEncodedFileContent() {
		return encodedFileContent;
	}

	public void setEncodedFileContent(String encodedFileContent) {
		this.encodedFileContent = encodedFileContent;
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

	@Override
	public String toString() {
		return "AttachmentRequestDto [encodedFileContent=" + encodedFileContent + ", fileName=" + fileName
				+ ", fileType=" + fileType + ", fileSize=" + fileSize + ", file=" + Arrays.toString(file) + "]";
	}

}
