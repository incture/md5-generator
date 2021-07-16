package oneapp.incture.workbox.demo.workflowauthentication.util;

/**
 * <h1>This class is used to maintain workflow related constants</h1>
 * 
 * @author Polireddy.M
 * @since 2019-05-16
 * @version 1.0
 *
 */
public class WorkflowConstants {

	public static final String XSCRF_TOKEN_URL = "/rest/v1/xsrf-token";
	public static final String WORKFLOW_TRIGGER_URL = "/rest/v1/workflow-instances";
	public static final String APPROVE_TASK_URL = "/v1/task-instances/";
	public static final String GET_TASK_INSTANCE_ID_URL = "/rest/v1/task-instances?workflowInstanceId=";
	public static final String GET_WORKFLOW_CONTEXT = "/rest/v1/workflow-instances/";

	public static final String APPROVED = "APPROVED";
	public static final String REJECTED = "REJECTED";
	public static final String COMPLETED = "COMPLETED";
	public static final String INPROGRESS = "INPROGRESS";
	public static final String STATUS = "status";

	public static final String CONTENT_TYPE = "application/json";
	public static final String X_CSRF_TOKEN = "x-csrf-token";
	public static final String AUTHORIZATION = "Authorization";
	public static final String ACCEPT = "accept";
	public static final String FETCH = "fetch";

	public static final String DEFINITION_ID = "definitionId";
	public static final String USER_WF_DEFINITION_ID = "userprofilewf";
	public static final String CONTEXT = "context";

	public static final String ID = "id";
	public static final int SUCCESS_CODE = 204;

}
