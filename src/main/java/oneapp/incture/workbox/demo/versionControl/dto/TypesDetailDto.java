package oneapp.incture.workbox.demo.versionControl.dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class TypesDetailDto extends BaseDto {
	
	private String detailType;
	private String labelDesc;
	private String description;
	private String linkLabel;
	private String link;
	private String documentId;
	private String versionId;
	private String validForUsage;
	
	
	
	public void setValidForUsage(String validForUsage) {
		this.validForUsage = validForUsage;
	}
	public String getDetailType() {
		return detailType;
	}
	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}
	public String getLabelDesc() {
		return labelDesc;
	}
	public void setLabelDesc(String labelDesc) {
		this.labelDesc = labelDesc;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLinkLabel() {
		return linkLabel;
	}
	public void setLinkLabel(String linkLabel) {
		this.linkLabel = linkLabel;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
	
	public String getVersionId() {
		return versionId;
	}
	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}
	@Override
	public String toString() {
		return "TypesDetailDto [detailType=" + detailType + ", labelDesc=" + labelDesc + ", description=" + description
				+ ", linkLabel=" + linkLabel + ", link=" + link + ", documentId=" + documentId + ", versionId="
				+ versionId + ", validForUsage=" + validForUsage + "]";
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
