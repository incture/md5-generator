package oneapp.incture.workbox.demo.sharepoint.util;

public interface SharePointConstant {

//	String REQUEST_URL_SHAREPOINT = "https://graph.microsoft.com/v1.0/sites/incturet.sharepoint.com,53d96276-4382-45ff-b5f4-03608e5cf0f1,1036887a-4acf-4549-90ca-ddadac22dc5d/lists";
	String REQUEST_URL_SHAREPOINT = "https://graph.microsoft.com/v1.0/sites/incturet.sharepoint.com,53d96276-4382-45ff-b5f4-03608e5cf0f1,1036887a-4acf-4549-90ca-ddadac22dc5d/lists";
    String SHAREPOINT_JSON_Format = "?$format=json";
    String SHAREPOINT_ITEMS = "/items?expand=fields";
    String SHAREPOINT_DIRECTORY="2def74e9-29f5-4545-8fdd-78aae8499930";

 

    String SAML_HEADER_SHAREPOINT = "Authorization";

 

    // ChatBot entity names
    String PROCESS_NAME = "processName";
//    String PROCESS_NAME_DOC_APPROVAL = "Workbox_Document_Approval Tasks";
    String PROCESS_NAME_DOC_APPROVAL = "Sample repo workflow Tasks 1";
    String USER_ID = "userId";
    String CATERGORY = "category";
    String DURATION = "duration";
    String TASK_OWNER = "taskowner";
    String STATUS = "status";
    String TIME_LINE = "timeline";

 

    String NULL_STRING = "null";

 

    String HTTP_METHOD_GET = "GET";

 

    String APPLICATION_JSON = "application/json";

 

    String REQUEST_URL_INST = "https://bpmworkflowruntimea2d6007ea-kbniwmq1aj.hana.ondemand.com/workflow-service/rest/v1/";

 

    final static String TENANT_SPECIFIC_AUTHORITY = "https://login.microsoftonline.com/78d7cfc1-dedd-4464-b309-74a59265897e/";

 

    final static String AUTHORITY_COMMON = "https://login.microsoftonline.com/common/";
    final static String AUTHORITY_ORGANIZATION = "https://login.microsoftonline.com/organizations/";
    
    final static String PUBLIC_CLIENT_ID = "843d854a-cff7-41c8-bcf4-b74765b26483";
    final static String GRAPH_DEFAULT_SCOPE = "https://graph.microsoft.com/.default";
    
    final static String USER_NAME = "workbox.demo@incture.com";
    final static String USER_PASSWORD = "Happy@321";

 

    final static String CONFIDENTIAL_CLIENT_ID= "843d854a-cff7-41c8-bcf4-b74765b26483";
    final static String CONFIDENTIAL_CLIENT_SECRET = "Yx5_V9olCwVC+P@t1mWga6[D@7nRfaKo";
}
