package oneapp.incture.workbox.demo.document.dto;

public class AttachmentDetail {


	private String attachmentType;
	private String attachmentId;
	private String attachmentName;
	private Integer attachmentSize;
	
	public Integer getAttachmentSize() {
		return attachmentSize;
	}
	public void setAttachmentSize(Integer attachmentSize) {
		this.attachmentSize = attachmentSize;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	@Override
	public String toString() {
		return "AttachmentDetail [attachmentType=" + attachmentType + ", attachmentId=" + attachmentId
				+ ", attachmentName=" + attachmentName + ", attachmentSize=" + attachmentSize + "]";
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	
	
}
