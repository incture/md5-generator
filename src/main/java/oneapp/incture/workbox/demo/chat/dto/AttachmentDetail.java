package oneapp.incture.workbox.demo.chat.dto;

public class AttachmentDetail {

	private String attachmentId;
	private String attachment;
	private String attachmentName;
	private String attachmentType;
	private Integer attachmentSize;
	private String compressedImg;
	
	public Integer getAttachmentSize() {
		return attachmentSize;
	}
	public void setAttachmentSize(Integer attachmentSize) {
		this.attachmentSize = attachmentSize;
	}
	public String getCompressedImg() {
		return compressedImg;
	}
	public void setCompressedImg(String compressedImg) {
		this.compressedImg = compressedImg;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	@Override
	public String toString() {
		return "AttachmentDetail [attachmentId=" + attachmentId + ", attachment=" + attachment + ", attachmentName="
				+ attachmentName + ", attachmentType=" + attachmentType + ", attachmentSize=" + attachmentSize
				+ ", compressedImg=" + compressedImg + "]";
	}
	
}
