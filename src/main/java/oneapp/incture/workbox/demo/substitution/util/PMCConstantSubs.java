package oneapp.incture.workbox.demo.substitution.util;

import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;

/**
 * @author Neelam
 *
 */
public interface PMCConstantSubs extends PMCConstant {
	String ORDER_TYPE_CREATED_AT = "createdAt";
	String ORDER_TYPE_SLA_DUE_DATE = "dueDate";
	String ORDER_BY_ASC = "ASC";
	String ORDER_BY_DESC = "DESC";
	String SEARCH_ALL = "ALL";
	String SEARCH_SMALL_ALL = "All";
	String PROCESS_STATUS_IN_PROGRESS = "IN_PROGRESS";
	String GRAPH_TREND_MONTH = "month";
	String GRAPH_TREND_WEEK = "week";
	String WEEK_PREFIX = "Week";
	String STATUS_ALL = "ALL";
	String TASK_STATUS_RESERVED = "RESERVED";
	String TASK_STATUS_READY = "READY";
	String PROCESS_AGING_REPORT = "process aging";
	String TASK_AGING_REPORT = "task aging";
	String USER_NAME = "User Name";
	String PROCESS_NAME_LABEL = "Process Name";
	String PROCESS_TOTAL = "Total";
	int WEEK_RANGE = 7;
	int MONTH_RANGE = 30;
	int MONTH_INTERVAL = 6;
	int COMPLETED_RANGE = 30;
	String SEARCH_RESERVED = "RESERVED";
	String SEARCH_READY = "READY";
	String UESR_PROCESS_GRAPH_MONTH_FORMATE = "dd MMM";
	String PMC_DATE_FORMATE = "dd MMM yyyy";
	int WEEK_INTERVAL = 1;
	String TASK_CREATED_FORMATE = "YYYY-MM-dd hh:mm:ss.SSS";
	String DETAIL_DATE_FORMATE = "dd MMM YYYY hh:mm:ss";
	// String DETAILDATE_AMPM_FORMATE = "dd MMM YYYY hh:mm:ss a";
	String BENTLEY_DETAILDATE_AMPM_FORMATE = "MMM dd,YYYY hh:mm a";
	int PAGE_SIZE = 20;
	String SEARCH_COMPLETED = "COMPLETED";
	String USER_TASK_STATUS_GRAPH = "task Status Graph";
	String TASK_COMPLETED = "COMPLETED";
	
	String PROCESS_COMPLETED = "COMPLETED";
	String PROCESS_CANCELED = "CANCELED";
	String PROCESS_ERROR = "ERRONEOUS";

	String TASK_COMPLETED_STR = "Completed";
	String TASK_INPROGRESS = "IN_PROGRESS";
	String UPDATE = "Update";
	String CREATE = "Create";
	String DELETE = "Delete";
	String STATUS_FAILURE = "FAILURE";
	String STATUS_SUCCESS = "SUCCESS";
	String NO_RESULT = "NO RESULT FOUND";
	String INTERNAL_ERROR = "Internal Error";
	String SEND_NOTIFICATION = "Send Notification";
	String REMIND_ME = "Remind Me";
	String USER_TASK_REPORT = "User workload";
	String PROCESS_TRACKER = "Process Aeging";
	String TASK_AEGING = "Task Aeging";
	String REPORT_EXCEL = "Excel";
	String REPORT_PDF = "PDF";
	String REPORT = "PMC Report";
	String PROCESS_BY_DURATION = "Process By Duration";
	String TASK_MANAGER = "Task Manager";
	int ARCHIVE_DAY = 0;
	String DAYS = "days";
	String HOURS = "hours";
	String MINUTES = "minute";

	/* User Id And Password For Wsdl Access */

	String WBuserId = "INC00695";
	String WBpassword = "Password@3";

	/* Add for consuming odata services */

	String HTTP_METHOD_PUT = "PUT";
	String HTTP_METHOD_POST = "POST";
	String HTTP_METHOD_GET = "GET";
	String HTTP_METHOD_PATCH = "PATCH";
	String HTTP_METHOD_DELETE = "DELETE";

	String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	String HTTP_HEADER_ACCEPT = "Accept";

	String APPLICATION_JSON = "application/json";
	String APPLICATION_XML = "application/xml";
	String APPLICATION_ATOM_XML = "application/atom+xml";
	String APPLICATION_FORM = "application/x-www-form-urlencoded";
	String METADATA = "$metadata";
	String COUNT = "$count";

	String SEPARATOR = "/";

	boolean PRINT_RAW_CONTENT = true;

	String ECC_URL = "http://sthcigwdq1.kaust.edu.sa:8005/sap/opu/odata/IWPGW/TASKPROCESSING;mo;v=2/";
	String BPM_URL = "http://sap.bpm.host:80/bpmodata/tasks.svc/";
	// String BPM_URL ="http://10.120.28.214:50000/bpmodata/tasks.svc/";
	String WF_URL = "";

	/* Added when destinations are added */

	String BPM_DEST = "workboxbpm";
	String BPM_LOCATION = "houston";
	String ON_PREMISE_PROXY = "OnPremise";

	/* Added for Task Management */

	String NOT_OWNER = "Not a Owner";

	String READ = "Read";
	String SUCCESS = "SUCCESS";
	String FAILURE = "FAILURE";

	String EXISTS = "EXISTS";
	String NOTEXIST = "NOT_EXIST";

	String CREATED_SUCCESS = "created successfully";
	String UPDATE_SUCCESS = "updated successfully";
	String DELETE_SUCCESS = "deleted successfully";
	String READ_SUCCESS = "Data fetched successfully";

	String CREATE_FAILURE = "creation failed";
	String UPDATE_FAILURE = "updation failed";
	String DELETE_FAILURE = "deletion failed";
	String READ_FAILURE = "Data fetch failed";

	String CODE_FAILURE = "1";
	String CODE_SUCCESS = "0";

	Boolean isEnabled = true;
	Boolean disabled = false;
	String TRUE = "true";
	String FALSE = "false";
	String SUBSTITUTING = "SUBSTITUTING";
	String SUBSTITUTED = "SUBSTITUTED";

	/* Task/Process persisting */

	/* Workbox Dev Workflow URLs */
	// String REQUEST_URL_INST =
	// "https://bpmworkflowruntimea2d6007ea-jg2kiqgyo3.hana.ondemand.com/workflow-service/rest/v1/";
	// String REQUEST_BASE_URL_TC =
	// "https://bpmworkflowruntimea2d6007ea-jg2kiqgyo3.hana.ondemand.com/workflow-service/odata/tcm/";
	String TASK_COLLECTION_RELATIVE_URL = "TaskCollection?$format=json";

	/* Bentley Dev Workflow URLs */
	// String REQUEST_URL_INST =
	// "https://bpmworkflowruntimeworkflow-prttj4jqbl.us3.hana.ondemand.com/workflow-service/rest/v1/";
	// String REQUEST_BASE_URL_TC =
	// "https://bpmworkflowruntimeworkflow-prttj4jqbl.us3.hana.ondemand.com/workflow-service/odata/tcm/";

	/* Bentley Workflow URLs */
	// String REQUEST_URL_INST =
	// "https://bpmworkflowruntimeworkflow-vg6ksiq8rt.us3.hana.ondemand.com/workflow-service/rest/v1/";
	String REQUEST_URL_INST = "https://bpmworkflowruntimeh647e170e-vg6ksiq8rt.us3.hana.ondemand.com/workflow-service/rest/v1/";
	String REQUEST_BASE_URL_TC = "https://bpmworkflowruntimeh647e170e-vg6ksiq8rt.us3.hana.ondemand.com/workflow-service/odata/tcm/";

	String SAML_HEADER_KEY_TC = "samlHeaderKeyTC";
	String SAML_HEADER_KEY_TI = "samlHeaderKeyTI";

	/* Added for generating standard header for inbox table */
	String STANDARD_HEADER = "STANDARD";

	String FORWARD_TASK = "FORWARD";
	String SUBSTITUTE_TASK = "SUBSTITUTION";
	String GROUP_NAME = "workbox";

	String SEARCH_STATUS = "STATUS";
	String SEARCH_PROCESS = "PROCESS";
	String NO_USER_FOUND = "NO USER FOUND";
	String PLATFORM_OAUTH_TOKEN = "platformOAuthToken";
	String APPLICATION_OAUTH_TOKEN = "applicationOAuthToken";

	String GROUPS_USER_CACHE_KEY = "groupsUsersCache";
	String USER_DETAILS_CACHE_KEY = "userDetailsCache";

	String NULL_STRING = "null";
	String MAND_MISS = "Mandatory Fields Missing";

	/* Cross Configuration References */

	String ADMIN_GROUP_CONFIG_REF = "1";
	String BUSINESS_OBJ_FOR_REASON_CODE = "PCA";
	String NEW_TASK = "newTask";
	String ALL_TASK = "allTask";
	String ADD_USER = "addUser";
	String REMOVE_USER = "removeUser";

	String TASK = "TASK";
	String PROCESS = "PROCESS";
	String CUSTOM_SEARCH_supplierNumber = "ORIGINAL_PARENT_SUPPLIER_NUMBER";
	String CUSTOM_SEARCH_amount = "ANN_IMP_GC";
	String CUSTOM_SEARCH_majorReasonCode = "REASON_CODE";
	String CUSTOM_SEARCH_pcaRequestor = "PCA_REQUESTOR";

	String SUBSTITUTION_SCHEDULER_TYPE = "Scheduler";
	String SUBSTITUTION_INSTANT_TYPE = "instant";
	String SUBSTITUTION_INSTANT_TYPE_INBOX = "inbox";
	String SUBSTITUTION_INSTANT_TYPE_SUBS_TAB = "substitutionTab";
	String PCA_PROCESS_NAME = "pca_approval_swf";

	String PROCESS_ERRONEOUS_STATE = "ERRONEOUS";

	/* BENTLEY SPECIFIC */

	String ACTION_TYPE_APPROVE = "Approve";
	String ACTION_TYPE_REJECT = "Reject";
	String ACTION_TYPE_FORWARD = "Forward";
	String ACTION_TYPE_SEND_BACK = "SendBack";
	String ACTION_TYPE_CLAIM = "Claim";
	String ACTION_TYPE_RELEASE = "Release";
	String ACTION_TYPE_EDITED = "Edit";
	String ACTION_TYPE_CANCELED = "Cancel";

	String TASK_TYPE_ALL = "AllTask";
	String TASK_TYPE_MY_TASK = "MyTask";
	String TASK_TYPE_SUBSTITUTED = "SubstitutedTask";

	String ORDER_TYPE_CUSTOM_REQUEST_NO = "requestNo";
	String ORDER_TYPE_CUSTOM_STATUS = "status";
	String ORDER_TYPE_CUSTOM_TOTAL_VALUE = "totalValue";
	String ORDER_TYPE_CUSTOM_PR_TYPE = "PRType";
	String ORDER_TYPE_CUSTOM_CREATED_ON = "createdOn";

	String CUSTOM_STATUS = "status";
	String CUSTOM_PROCESS = "materialprapproval";

	String PROXY = "http://sapr3d03.bentley.com:443";
	String CONN_LOCATION = "";
	String CONN_TENANT = "vg6ksiq8rt";
	int PROXYPORT = 443;

	String EMAIL_MANAGER_TASK = "ScheduledMailToManager";
	// BENTLEY Task Type CODES:

	String TASK_AP = "AP";

	String TASK_LEGAL_APPROVED_WITH_CONTRACT = "LegalApprovedWC";

	String TASK_LEGAL_APPROVED_NO_CONTRACT = "LegalApprovedNC";

	String TASK_LEGAL_NOT_DONE = "LegalND";

	String TASK_LEGAL_REJECTED = "LegalR";

	// BENTLEY STATUS CODES:

	String APPROVED_CODE = "0005";
	String REJECTED_CODE = "0006";
	String RELEASED_CODE = "0007";
	String FORWARDED_CODE = "0008";
	String CLAIMED_CODE = "0022";
	String REVIEWED_CODE = "0018";
	String UNKNOWN_CODE = "0010";
	String CANCELLED_CODE = "0009";
	String AWAITING_APPROVAL_CODE = "0004";
	String AWAITING_AP_APPROVAL_CODE = "0021";
	String AWAITING_CONTRACT_COMPLETION_CODE = "0013";
	String AWAITING_APPROVAL_CHANGE_CODE = "0019";
	String AWAITING_CONTRACT_COMP_CHANGE_CODE = "0020";
	String LEGAL_APPROVED_WITH_CONTRACT_CODE = "0014";
	String LEGAL_APPROVED_NO_CONTRACT_CODE = "0015";
	String LEGAL_NOT_DONE_CODE = "0016";
	String LEGAL_REJECTED_CODE = "0017";

	String APPROVED_TEXT = "Approved";
	String REJECTED_TEXT = "Rejected";
	String RELEASED_TEXT = "Released";
	String FORWARDED_TEXT = "Forwarded";
	String REVIEWED_TEXT = "Reviewed";
	String CLAIMED_TEXT = "Claimed";
	String UNKNOWN_TEXT = "Unknown";
	String CANCELLED_TEXT = "Cancelled";
	String AWAITING_APPROVAL_TEXT = "Awaiting Approval";
	String AWAITING_AP_APPROVAL_TEXT = "Awaiting AP Approval";
	String AWAITING_CONTRACT_COMPLETION_TEXT = "Awaiting Contract Completion";
	String AWAITING_APPROVAL_CHANGE_TEXT = "Awaiting Approval(Chng.after approved)";
	String AWAITING_CONTRACT_COMP_CHANGE_TEXT = "Awaiting Contract Comp.(Chng.after approved)";
	String LEGAL_APPROVED_WITH_CONTRACT_TEXT = "Legal Approved - With Contract";
	String LEGAL_APPROVED_NO_CONTRACT_TEXT = "Legal Approved - No Contract";
	String LEGAL_NOT_DONE_TEXT = "Legal - Not Done";
	String LEGAL_REJECTED_TEXT = "Legal Rejected";

	String ADMIN_FORWARD = "Admin Forward";
	String AP_TASK = "AP review task";
	String MANGER_TASK = "Manager Approval";
	String LEGAL_TASK = "Legal Review";
	String PROCESS_STATUS_RUNNING = "RUNNING";

}
