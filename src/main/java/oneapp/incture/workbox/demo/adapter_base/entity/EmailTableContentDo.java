package oneapp.incture.workbox.demo.adapter_base.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMAIL_TABLE_CONTENT")
public class EmailTableContentDo implements BaseDo,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TABLE_CONTENT_ID")
	private String tableContentId;
	
	@Id
	@Column(name="VERSION")
	private int version;
	
	@Id
	@Column(name = "COLUMN_NAME")
	private String columnName;
	
	@Column(name = "COLUMN_VALUE_FIELD")
	private String columnValueField;
	
	@Column(name = "SEQUENCE_NUMBER")
	private Integer sequenceNumber;

	@Override
	public String toString() {
		return "EmailTableContentDo [tableContentId=" + tableContentId + ",version=" + version +", columnName=" + columnName
				+ ", columnValueField=" + columnValueField + ", sequenceNumber=" + sequenceNumber + "]";
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
	public Object getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

