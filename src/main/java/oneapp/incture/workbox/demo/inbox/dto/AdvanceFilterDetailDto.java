package oneapp.incture.workbox.demo.inbox.dto;

public class AdvanceFilterDetailDto {

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	private String condition;
	private String value;
	private String upperLimit;
	private String lowerLimit;
	private String level;
	private String dataType;
	private String operator;
	private String displayValue;
	
	
	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}

	public String getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(String lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "AdvanceFilterDetailDto [condition=" + condition + ", value=" + value + ", upperLimit=" + upperLimit
				+ ", lowerLimit=" + lowerLimit + ", level=" + level + ", dataType=" + dataType + ", operator="
				+ operator + ", displayValue=" + displayValue + "]";
	}

}
