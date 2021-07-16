package oneapp.incture.workbox.demo.inbox.dto;

public class FilterLayoutDto {

	private int columnId;
	private String columnName;
	private String label;
	private String datatype;
	private String tableName;
	private String tableAlias;
	private String filterType;
	private String selectionList;
	private String conditionList;
	private String autoCompleteType;

	public String getAutoCompleteType() {
		return autoCompleteType;
	}

	public void setAutoCompleteType(String autoCompleteType) {
		this.autoCompleteType = autoCompleteType;
	}

	public String getConditionList() {
		return conditionList;
	}

	public void setConditionList(String conditionList) {
		this.conditionList = conditionList;
	}

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

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
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

	public String getSelectionList() {
		return selectionList;
	}

	public void setSelectionList(String selectionList) {
		this.selectionList = selectionList;
	}

	@Override
	public String toString() {
		return "FilterLayoutDto [columnId=" + columnId + ", columnName=" + columnName + ", label=" + label
				+ ", datatype=" + datatype + ", tableName=" + tableName + ", tableAlias=" + tableAlias + ", filterType="
				+ filterType + ", selectionList=" + selectionList + ", conditionList=" + conditionList
				+ ", autoCompleteType=" + autoCompleteType + "]";
	}

}
