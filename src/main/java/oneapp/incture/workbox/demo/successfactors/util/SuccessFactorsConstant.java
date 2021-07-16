package oneapp.incture.workbox.demo.successfactors.util;

public interface SuccessFactorsConstant {

	// SuccessFactors URl
	/*
	 * String GET_SF_BEARER_TOKEN_URL =
	 * "https://hrapp.cfapps.eu10.hana.ondemand.com/oData/getToken?userId="; String
	 * GET_SF_TASKS_URL =
	 * "https://apisalesdemo4.successfactors.com:443/odata/v2/Todo?$filter=categoryId%20eq%20%27"
	 * + 18 + "%27"; String GET_SF_CUSTATTR_URL =
	 * "https://apisalesdemo4.successfactors.com/odata/v2/EmployeeTime?$filter=workflowRequestId%20eq%20%27";
	 * String GET_SF_CUSTATTR_FILTER_URL =
	 * "&$select=endDate,timeType,createdDateTime,quantityInHours,startDate,approvalStatus,quantityInDays,userId,comment,userIdNav&$expand=userIdNav";
	 * String APPROVE_SF_TASK_URL =
	 * "https://apisalesdemo4.successfactors.com:443/odata/v2/approveWfRequest?wfRequestId=";
	 * String REJECT_SF_TASK_URL =
	 * "https:// apisalesdemo4.successfactors.com:443/odata/v2/rejectWfRequest?wfRequestId="
	 * ;
	 * 
	 * String APPROVE_SF_TRAVEL_TASK_URL =
	 * "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/TravelRequest/approveTravelRequest";
	 * String REJECT_SF_TRAVEL_TASK_URL =
	 * "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/TravelRequest/approveTravelRequest";
	 * 
	 * String APPROVE_SF_EXPENSE_TASK_URL =
	 * "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/expense-audit/approveExpense";
	 * String REJECT_SF_EXPENSE_TASK_URL =
	 * "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/expense-audit/rejectExpense";
	 * 
	 * String APPROVE_SF_TS_TASK_URL =
	 * "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/time-sheet/approveTimesheet";
	 * String REJECT_SF_TS_TASK_URL =
	 * "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/time-sheet/rejectTimesheet";
	 */
	
	String GET_SF_BEARER_TOKEN_URL = "https://hrapp.cfapps.eu10.hana.ondemand.com/oData/getToken?userId=";
	String GET_SF_TASKS_URL = "https://apisalesdemo4.successfactors.com:443/odata/v2/Todo?$format=json&$filter=categoryId%20eq%20%27"
			+ 18 + "%27";
	String GET_SF_CUSTATTR_URL = "https://apisalesdemo4.successfactors.com/odata/v2/EmployeeTime?$format=json&$filter=workflowRequestId%20eq%20%27";
	String GET_SF_CUSTATTR_FILTER_URL = "&$select=endDate,timeType,createdDateTime,quantityInHours,startDate,approvalStatus,quantityInDays,userId,comment,userIdNav&$expand=userIdNav";
	String APPROVE_SF_TASK_URL = "https://apisalesdemo4.successfactors.com:443/odata/v2/approveWfRequest?wfRequestId=";
	String REJECT_SF_TASK_URL = "https://apisalesdemo4.successfactors.com:443/odata/v2/rejectWfRequest?$format=json&wfRequestId=";
	
	String APPROVE_SF_TRAVEL_TASK_URL = "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/TravelRequest/approveTravelRequest";
	String REJECT_SF_TRAVEL_TASK_URL = "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/TravelRequest/approveTravelRequest";
	
	String APPROVE_SF_EXPENSE_TASK_URL = "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/expense-audit/approveExpense";
	String REJECT_SF_EXPENSE_TASK_URL = "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/expense-audit/rejectExpense";
	
	String APPROVE_SF_TS_TASK_URL = "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/time-sheet/approveTimesheet";
	String REJECT_SF_TS_TASK_URL = "https://hrapp.cfapps.eu10.hana.ondemand.com/hr-tech/time-sheet/rejectTimesheet";
	String COMMENT_TAG = "&comment=";
}
