package oneapp.incture.workbox.demo.emailTemplate.Dto;

import javax.xml.bind.annotation.XmlRootElement;

import oneapp.incture.workbox.demo.adapter_base.dto.BaseDto;
import oneapp.incture.workbox.demo.adapter_base.util.EnOperation;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;

@XmlRootElement
public class EmailTableContentDto extends BaseDto{

	private String tableContentId;
	private int version;
	private String columnName;
	private String columnValueField;
	private Integer sequenceNumber;
	private Boolean isDeleted = false;
	
	@Override
	public String toString() {
		return "EmailTableContentDto [tableContentId=" + tableContentId + ", version=" + version + ",columnName=" + columnName
				+ ", columnValueField=" + columnValueField + ", sequenceNumber=" + sequenceNumber + ", isDeleted="
				+ isDeleted + "]";
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getTableContentId() {
		return tableContentId;
	}
	public void setTableContentId(String tableContentId) {
		this.tableContentId = tableContentId;
	}
	
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnValueField() {
		return columnValueField;
	}
	public void setColumnValueField(String columnValueField) {
		this.columnValueField = columnValueField;
	}
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
