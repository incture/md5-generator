	package oneapp.incture.workbox.demo.ecc.eccadapter.util;

public interface EccAdapterConstant {

	
	String DESTINATION_URL = "URL";
	String DESTINATION_USER = "User";
	String DESTINATION_PWD = "Password";

	String ECC_DESTINATION = "ECC_DESTINATION";
	
	String ACTION_APPROVE = "A";
	String ACTION_REJECT = "R";
	
	String ACTION_TYPE_APPROVE = "Approve";
	String ACTION_TYPE_REJECT = "Reject";
	
	
	String CC_VIRTUAL_HOST = "http://incture.ecc:8001";// Cloud connector url
	String TASK_COLLECTION_URL = CC_VIRTUAL_HOST + "/sap/opu/odata/IWPGW/TASKPROCESSING;v=2/";
	String PR_UPDATE_URL = CC_VIRTUAL_HOST + "/sap/opu/odata/sap/ZGW_PR_APPROVAL_SRV/PR_DETAILSSet";

}
