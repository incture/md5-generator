package oneapp.incture.workbox.demo.inbox.util;

public interface FilterConstants {

	// Conditions
	String EQUALS = "equals";
	String IN = "in";
	String CONTAINS = "contains";
	String STARTS_WITH = "startsWith";
	String ENDS_WITH = "endsWith";
	String GREATER_THAN = "greaterThan";
	String LESSER_THAN = "lesserThan";
	String DATE_RANGE = "DateRange";

	// Operators
	String OPTR_AND = "and";
	String OPTR_NOT = "not";
	String OPTR_OR = "OR";
	String OPTR_BETWEEN = "between";

	// DataTypes
	String STRING_TYPE = "string";
	String DATE_TYPE = "datetype";
	String BOOLEAN_TYPE = "boolean";
	String NUMBER_TYPE = "number";

	// Task State
	String CRITICAL = "critical";
	String IN_TIME = "inTime";
	String SLA_BREACHED = "slaBreached";

}
