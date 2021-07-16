package oneapp.incture.workbox.demo.inbox.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILTER_METADATA")
public class FilterMetaData {

	@Id
	@Column(name = "COLUMN_ID", length = 100)
	private int columnId;

	@Column(name = "COLUMN_NAME", length = 100)
	private String columnName;

	@Column(name = "LABEL", length = 100)
	private String label;

	@Column(name = "DATA_TYPE", length = 100)
	private String dataType;

	@Column(name = "TABLE_NAME", length = 100)
	private String tableName;

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Column(name = "TABLE_ALIAS", length = 100)
	private String tableAlias;

	@Column(name = "FILTER_TYPE", length = 100)
	private String filterType;

	@Column(name = "IS_ACTIVE", length = 100)
	private boolean isActive;

	public int getColumnId() {
		return columnId;
	}

	public void setColumnId(int columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	@Override
	public String toString() {
		return "FilterMetaData [columnId=" + columnId + ", columnName=" + columnName + ", label=" + label
				+ ", dataType=" + dataType + ", tableName=" + tableName + ", tableAlias=" + tableAlias + ", filterType="
				+ filterType + ", isActive=" + isActive + "]";
	}

}
