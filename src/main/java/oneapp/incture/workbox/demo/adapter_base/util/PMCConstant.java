package oneapp.incture.workbox.demo.adapter_base.util;

/**
 * @author Saurabh
 *
 */
public interface PMCConstant {
	String NULL_STRING = "null";
	String ORDER_TYPE_CREATED_AT = "createdAt";
	String ORDER_TYPE_SLA_DUE_DATE = "dueDate";
	String ORDER_TYPE_COMPLETED_DATE = "completedAt";
	String ORDER_TYPE_UPDATED_DATE = "updatedAt";
	String ORDER_BY_ASC = "ASC";
	String ORDER_BY_DESC = "DESC";
	String SEARCH_ALL = "ALL";
	String SEARCH_SMALL_ALL = "All";
	String PROCESS_STATUS_IN_PROGRESS = "IN_PROGRESS";
	String GRAPH_TREND_MONTH = "month";
	String GRAPH_TREND_WEEK = "week";
	String WEEK_PREFIX = "Week";
	String STATUS_ALL = "ALL";
	String SEARCH_RESERVED = "RESERVED";
	String SEARCH_READY = "READY";
	String TASK_STATUS_RESERVED = "RESERVED";
	String TASK_STATUS_READY = "READY";
	String TASK_STATUS_ALL_COMPLETED = "ALL COMPLETED";
	
	String PANEL_TEMPLATE_ID_ALLTASKS = "AllTask";
	/* Manual Task status */
	String TASK_STATUS_RESOLVED = "RESOLVED";
	String TASK_STATUS_IN_PROGRESS = "INPROGRESS";

	String PROCESS_AGING_REPORT = "process aging";
	String TASK_AGING_REPORT = "task aging";
	String USER_NAME = "User Name";
	String PROCESS_NAME_LABEL = "Process Name";
	String PROCESS_TOTAL = "Total";
	int WEEK_RANGE = 7;
	int MONTH_RANGE = 30;
	int MONTH_INTERVAL = 6;
	int COMPLETED_RANGE = 30;

	String UESR_PROCESS_GRAPH_MONTH_FORMATE = "dd MMM";
	String PMC_DATE_FORMATE = "dd MMM yyyy";
	int WEEK_INTERVAL = 1;
	String TASK_CREATED_FORMATE = "YYYY-MM-dd hh:mm:ss.SSS";
	String DETAIL_DATE_FORMATE = "dd MMM YYYY hh:mm:ss";
	String DETAILDATE_AMPM_FORMATE = "dd MMM YYYY hh:mm:ss a";
	int PAGE_SIZE = 20;
	String SEARCH_COMPLETED = "COMPLETED";
	String USER_TASK_STATUS_GRAPH = "task Status Graph";
	String TASK_COMPLETED = "COMPLETED";
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

	// String WBuserId = "INC00695";
	// String WBpassword = "Password@3";

	/* Add for consuming odata services */

	String HTTP_METHOD_PUT = "PUT";
	String HTTP_METHOD_POST = "POST";
	String HTTP_METHOD_GET = "GET";
	String HTTP_METHOD_PATCH = "PATCH";

	String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	String HTTP_HEADER_ACCEPT = "Accept";

	String APPLICATION_JSON = "application/json";
	String APPLICATION_XML = "application/xml";
	String APPLICATION_ATOM_XML = "application/atom+xml";
	String APPLICATION_FORM = "application/x-www-form-urlencoded";
	String APPLICATION_VND = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	String METADATA = "$metadata";
	String COUNT = "$count";

	String SEPARATOR = "/";

	boolean PRINT_RAW_CONTENT = true;

	// String ECC_URL =
	// "http://sthcigwdq1.kaust.edu.sa:8005/sap/opu/odata/IWPGW/TASKPROCESSING;mo;v=2/";
	// String BPM_URL = "http://sap.bpm.host:80/bpmodata/tasks.svc/";

	// String BPM_URL ="http://10.120.28.214:50000/bpmodata/tasks.svc/";
	String WF_URL = "";

	/* Added when destinations are added */

	// String BPM_DEST = "workboxbpm";
	// String BPM_LOCATION = "houston";
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
	String SUBSTITUTING = "SUBSTITUTING";
	String SUBSTITUTED = "SUBSTITUTED";

	// BRANCH CONFIGURATION CONSTANTS

	// DEV____________________________________________________________________________

	// 1. Workflow end points

	// String REQUEST_URL_INST =
	// "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/rest/v1/";
	//
	// String REQUEST_BASE_URL_TC =
	// "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/odata/tcm/";

	// 2. Network Request Credentials
	// String NETWORK_REQUEST_URL =
	// "https://oauthasservices-kbniwmq1aj.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";
	// String NETWORK_USER_ID = "e5422b86-1f6f-33a6-a6b6-2bd5cabde2a1";
	// String NETWORK_PASSWORD = "Workbox@123";

	// 3. Mobile rest notification
	// MOBILE_RESTNOTIFICATION="https://mobile-kbniwmq1aj.hana.ondemand.com/restnotification/";

	// QA_____________________________________________________________________________

	// 1. Workflow end points

	// String REQUEST_URL_INST =
	// "https://bpmworkflowruntimea2d6007ea-a8b98c03e.hana.ondemand.com/workflow-service/rest/v1/";

	// String REQUEST_BASE_URL_TC =
	// "https://bpmworkflowruntimea2d6007ea-a8b98c03e.hana.ondemand.com/workflow-service/odata/tcm/";

	// 2. Network Request Credentials
	// String NETWORK_REQUEST_URL =
	// "https://oauthasservices-a8b98c03e.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";
	// String NETWORK_USER_ID = "00b2004f-5b09-3656-a808-19c174dc2f6d";
	// String NETWORK_PASSWORD = "123456";

	// PROD___________________________________________________________________________

	// 1. Workflow end points

	// String REQUEST_URL_INST =
	// "https://bpmworkflowruntimea2d6007ea-a8b98c03e.hana.ondemand.com/workflow-service/rest/v1/";
	//
	// String REQUEST_BASE_URL_TC =
	// "https://bpmworkflowruntimea2d6007ea-a8b98c03e.hana.ondemand.com/workflow-service/odata/tcm/";
	//
	// // 2. Network Request Credentials
	// String NETWORK_REQUEST_URL =
	// "https://oauthasservices-a8b98c03e.hana.ondemand.com/oauth2/api/v1/token?grant_type=client_credentials";
	// String NETWORK_USER_ID = "00b2004f-5b09-3656-a808-19c174dc2f6d";
	// String NETWORK_PASSWORD = "123456";

	// String MOBILE_RESTNOTIFICATION =
	// "https://mobile-a8b98c03e.hana.ondemand.com/restnotification/";
	// String MOBILE_APPLICATION_ID = "incture.workbox";
	// String MOBILE_USER = "P2000922351"; // Venkatesh.kesary@incture.com
	// String MOBILE_USER_PASS = "Workbox@123";

	// String REQUEST_URL_SHAREPOINT =
	// "https://graph.microsoft.com/v1.0/sites/incturet.sharepoint.com,53d96276-4382-45ff-b5f4-03608e5cf0f1,1036887a-4acf-4549-90ca-ddadac22dc5d/lists";
	String SHAREPOINT_JSON_Format = "?$format=json";
	String SHAREPOINT_ITEMS = "/items?expand=fields";

	String SAML_HEADER_SHAREPOINT = "Authorization";

	String TASK_COLLECTION_RELATIVE_URL = "TaskCollection";

	String SAML_HEADER_KEY_TC = "samlHeaderKeyTC";
	String SAML_HEADER_KEY_TI = "samlHeaderKeyTI";

	/* Added for generating standard header for inbox table */
	String STANDARD_HEADER = "STANDARD";
	String CUSTOM_HEADER = "CUSTOM";

	String FORWARD_TASK = "FORWARD";
	String SUBSTITUTE_TASK = "SUBSTITUTION";
	String GROUP_NAME = "workbox";

	String SEARCH_STATUS = "STATUS";
	String SEARCH_PROCESS = "PROCESS";

	String TOTAL_ACTIVE_TASK = "totalActiveTask";
	String TOTAL_ACTIVE_TASK_GRAPH = "totalActiveTasks";
	String USER_WORK_ITEM_COUNT_GRAPH = "userWorkItemCount";
	String OPEN_TASK = "openTask";
	String PENDING_TASK = "pendingTask";
	String SLA_BREACHED_TASK = "slaBreachedTask";
	String MY_TASK = "myTask";

	String COMPLETED_WITH_SLA = "Completed On Time";
	String COMPLETED_WITHOUT_SLA = "Completed After SLA";
	String RESERVED_WITH_SLA = "In Progress On Time";
	String RESERVED_WITHOUT_SLA = "In Progress After SLA";
	String READY_WITH_SLA = "New On Time";
	String READY_WITHOUT_SLA = "New After SLA";

	String INBOX_TYPE_GROUP_TASK = "groupTask";
	String INBOX_TYPE_MY_TASK = "myTask";

	String REPORT_FILTER_IN_TIME = "inTime";
	String REPORT_FILTER_CRITICAL = "critical";
	String REPORT_FILTER_SLA_BREACHED = "slaBreached";
	String REPORT_FILTER_PENDING = "pending";
	String REPORT_FILTER_WITH_SLA = "withSla";
	String TASK_COMPLETION_TREND_LIST = "taskCompletionTrendList";

	String NEW_TASK = "newTask";
	String ALL_TASK = "allTask";
	String ADD_USER = "addUser";
	String REMOVE_USER = "removeUser";

	String ACTION_TYPE_CLAIM = "claim";
	String ACTION_TYPE_RELEASE = "release";
	String ACTION_TYPE_FORWARD = "forward";

	String CLAIM_SUCCESS = "Claimed successfully";
	String RELEASE_SUCCESS = "Released successfully";
	String FORWARD_SUCCESS = "Forwarded successfully";

	String CLAIM_FAILURE = "Claim failed";
	String RELEASE_FAILURE = "Release failed";
	String FORWARD_FAILURE = "Forward failed";

	String SUBSTITUTION_SCHEDULER_TYPE = "Scheduler";
	String SUBSTITUTION_INSTANT_TYPE = "instant";

	String CRITICAL_TIME = "60*60*24*90"; // before 3 months before deadline

	String REPORT_COMPLETED = "Completed";
	String REPORT_READY = "New";
	String REPORT_RESERVED = "In Progress";
	String REPORT_SLA_BREACHED = "SLA Breached";
	String REPORT_ON_TIME = "On Time";
	String REPORT_CRITICAL = "Critical";
	String REPORT_COMPLETED_ON = "COMPLETED_ON";

	String MANUAL_TASK_ORIGIN = "WORKBOX";
	// String MANUAL_TASK_URL =
	// "https://workboxwebkbniwmq1aj.hana.ondemand.com/manual-task";

	// ChatBot entity names
	String PROCESS_NAME = "processName";
	String PROCESS_NAME_DOC_APPROVAL = "Workbox_Document_Approval Tasks";
	String USER_ID = "userId";
	String CATERGORY = "category";
	String DURATION = "duration";
	String TASK_OWNER = "taskowner";
	String STATUS = "status";
	String TIME_LINE = "timeline";
	//

	// Dev
	// String

	// QA

	// ECC PR PO Constants
	// String CC_VIRTUAL_HOST = "http://34.210.142.28:8080"; // actual url
	// String CC_VIRTUAL_HOST = "http://incture.ecc:8001";// Cloud connector url
	// String TASK_COLLECTION_URL = CC_VIRTUAL_HOST +
	// "/sap/opu/odata/IWPGW/TASKPROCESSING;v=2/";
	String TASK_DEFINITION_ID = "TaskDefinitionID";
	String SPACE = "%20";
	String SINGLE_QUOTE = "%27";
	String FILTER = "$filter";
	String JSON_FORMATTER = "$format=json";
	String CREATED_ON_DESC = "$orderby=CreatedOn%20desc";
	String PURCHASE_ORDER = "PO";
	String PURCHASE_REQUISITION = "PR";
	// String CONN_TENANT = "kbniwmq1aj";
	String CONN_LOCATION = "";
	// String PROXY = "http://ecc.virtual.address.http:8080";
	// String PRApprovalURL = "/sap/opu/odata/sap/ZPR_RELEASE_SRV/pr_appSet";
	// String POApprovalURL = "/sap/opu/odata/sap/ZPO_RELEASE_SRV/Po_appSet";
	// String PRApprovalURL =
	// "/sap/opu/odata/sap/ZGW_PR_APPROVAL_SRV/PR_APPROVE_REJECTSet";

	String MyInbox = "My Inbox";
	String GroupInbox = "Group Inbox";
	String AdminInbox = "Admin Inbox";
	String HTTP_METHOD_DELETE = "DELETE";

	// String COMPLETED_TASK_MAIL_FROM = "workboxtaskmanagement@gmail.com";
	// String COMPLETED_TASK_MAIL_FROM_PWD = "Incture@1234";
	// String COMPLETED_TASK_MAIL_TO = "sreeparna.kundu@incture.com";
	String COMPLETED_TASK_MAIL_SUBJECT = "Report Of Completed Tasks";

	// String IAS_SERVICES = "https://aiiha1kww.accounts.ondemand.com/service/";
	// String IAS_USER = "T000006";
	// String IAS_USER_PASS = "Workbox@123";
	String INBOX_TYPE_ADMIN = "adminInbox";
	String INBOX_TYPE_SUBSTITUTION_INBOX = "substitutionInbox";

	String INBOX_TYPE_MY_INBOX = "myInbox";

	String PANEL_TEMPLATE_LINK = "Link";
	String USER_ROELS_ADMIN = "Admin";
	String PANEL_TEMPLATE_ID_PANEL = "Panel";
	String PANEL_TEMPLATE_ID_ALL_TASK = "AllTask";
	String PANEL_TEMPLATE_ID_GROUPS = "Groups";
	String PANEL_TEMPLATE_ID_VIEWS = "Views";
	String PANEL_TEMPLATE_ID_MYINBOX = "MyInbox";
	String PANEL_TEMPLATE_ID_ADMININBOX = "AdminInbox";
	String PANEL_TEMPLATE_ID_SUBSTITUTIONINBOX = "SubstitutionInbox";
	String PANEL_TEMPLATE_ID_DRAFT = "Draft";
	String PANEL_TEMPLATE_ID_CREATEDTASKS = "CreatedTasks";
	String PANEL_TEMPLATE_ID_COMPLETEDTASKS = "CompletedTasks";
	String PINNED_TASKS = "PinnedTasks";
	String USER_TASKS = "UserTasks";

	String WORKBOX_ADMIN = "P000100";
	String ACTION_TYPE_APPROVE = "approve";
	String ACTION_TYPE_REJECT = "reject";
	String SAVE_SUCCESS = "saved successfully";
	String SAVE_FAILURE = "saving failed";
	String ORDER_TYPE_REQUEST_ID = "requestId";

	// Quick filters
	String FOR_ME = "ForMe";
	String ALL_FORWARDED = "AllForwarded";

	String SERVICE_NOT_AVAILABLE = "Service Not Available";
	String ADMIN = "Admin";

	int UNAUTHORIZED = 401;
	String COLUMNS_FOR_AUTOCOMPLETE = "started_by,forwarded_by,name";
	String FILTERTYPE_INPUT = "input";
	String FILTERTYPE_MULTICOMBOBOX = "multicombobox";
	String FILTERTYPE_COMBOBOX = "combobox";
	String FILYTERTYPE_COMBOINPUT = "comboinput";
	String FILTERTYPE_CHECKBOX = "checkbox";
	String FILTERTYPE_DATEFILTER = "datefilter";
	String DEFAULT = "default";
	String CUSTOM = "custom";
	String ACTION_NOT_SUPPORTED = "Action Not Supported";
	String APPROVED_SUCCESS = "Approved Successfully";
	String REJECTED_SUCCESS = "Rejected Successfully";
	String FAILED = " failed!";
	String ADHOC = "Ad-hoc";
	String PANEL_NAME_MORE = "More";
	String PANEL_NAME_ALL = "All";
	String PANEL_TEMPLATE_ID_ALL = "all";
	String TABLE = "TABLE";
	String FORMS = "FORMS";

	String VIBRATION_SETTING = "Vibration";
	int TRAY_SIZE = 4;
	String DROPDOWN = "DROPDOWN";
	String INDIVIDUAL = "individual";
	String GROUP = "group";
	String ROLE = "role";
	String PANEL_TEMPLATE_ID_REQUESTORCOMPLETED = "RequestorCompletedTasks";
	String NOTIFICATION = "notification";

}