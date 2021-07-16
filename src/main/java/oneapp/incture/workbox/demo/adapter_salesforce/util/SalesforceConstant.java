package oneapp.incture.workbox.demo.adapter_salesforce.util;

public interface SalesforceConstant {

	String SUCCESS = "SUCCESS";
	String FAILURE = "FAILURE";
	
	String STATUS_CODE_FAILURE = "1";
	String STATUS_CODE_SUCCESS = "0";
	String REST_URL_SALESFORCE = "https://ap16.salesforce.com/services/data/v48.0";
	String FETCH_USER_QUERY = "/query/?q=SELECT+Lastname,username,Id,Email,IsActive,Address+from+user";
	String SALESFORCE_TOKEN_URL = "https://login.salesforce.com/services/oauth2/token";
	String GRANT_TYPE = "password";
	String USERNAME = "preetham.r@incture.com";
	String PASSWORD = "Incture@1234szqyIThjvuRo33zrgWlvtgO1R";
	String CLIENT_ID = "3MVG9n_HvETGhr3DFAEbg3wl4ctpkMGfNSGozchQsyFWNbsKs0z.X7Phe8gDyOiFk9McR1VAJ7.tlL8I7ug1l";
	String CLIENT_SECRET = "6A23308B6164889056D24DA2FC3B92E983163A68093009C41B1FDBCDFE8FCBDF";
	String FETCH_WHOLE_PROCESS_INSTANCE = "/query/?q=SELECT+Id,ProcessDefinitionId,TargetObjectId,CompletedDate,CreatedById,CreatedDate,status,"
			+ "(SELECT+Id,NodeStatus,LastActorId,CreatedDate,CompletedDate,ProcessNodeName,ProcessNodeId+from+Nodes)"
			+ ",(SELECT+Id,TargetObjectId,StepStatus,Actorid,IsPending,OriginalActorId,ProcessNodeId,Comments,CreatedDate,CreatedById+FROM+StepsAndWorkitems)"
			+ ",(SELECT+Id,StepStatus,ActorId,CreatedDate+from+Steps)"
			+ ",(SELECT+Id,ActorId,CreatedDate+from+Workitems)"
			+ "+FROM+ProcessInstance+where+ProcessDefinitionId+in(SELECT+id+from+ProcessDefinition+where+TableEnumOrId+in+('Campaign'))"
			+ "+and+status+not+in+('Approved','Rejected')+and+id+not+in+(";
	String PENDING = "Pending";
	String APPROVED = "Approved";
	String REJECTED = "Rejected";
	String IN_PROGRESS = "In Progress";
	String COMPLETED = "Commpleted";
	String FETCH_CUSTOM_ATTRIBUTES = "/sobjects/Campaign/";
	String GET_WORKITEM_ID ="/query/?q=SELECT+Id,ProcessInstanceId+from+ProcessInstanceWorkitem+where+ProcessInstanceId+='";
	String REST_URL_FOR_APPROVAL = "/process/approvals/";
	String PASSWORD2 = "Incture@1234nUsuisuyrBpvH1kBpjXNgZGt";
	String USERNAME2 = "rajn27002-vrf8@force.com.test";
}
