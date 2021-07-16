package oneapp.incture.workbox.demo.chat.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import oneapp.incture.workbox.demo.adapter_base.entity.BaseDo;

@Embeddable
public class AttachmentsDoPk implements BaseDo, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "ATTACHMENT_ID" , columnDefinition = "NVARCHAR(100)")
	private String attachmentId;
	
	@Column(name = "DOCUMENT_ID" , columnDefinition = "NVARCHAR(100)")
	private String documentId;
	
	public AttachmentsDoPk() {
		super();
	}

	public AttachmentsDoPk(String attachmentId, String documentId) {
		super();
		this.attachmentId = attachmentId;
		this.documentId = documentId;
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

	@Override
	public String toString() {
		return "AttachmentsDoPk [attachmentId=" + attachmentId + ", documentId=" + documentId + "]";
	}

	@Override
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
