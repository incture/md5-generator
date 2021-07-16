package oneapp.incture.workbox.demo.ecc.eccadapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.ecc.eccadapter.util.EccAdapterConstant;
import oneapp.incture.workbox.demo.ecc.eccadapter.util.SCPRestUtil;
//import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

import oneapp.incture.workbox.demo.adapter_base.dto.PRUpdateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Service
public class ECCAdapter {

	@Autowired
	ECCAdapterService eccAdapterService;

	@Autowired
	ECCParser eccParser;
	@Autowired
	PropertiesConstants getProperty;

	static String user = "bopf2";
	static String pass = "Welcome@1234";

	public void eccScheduler() {
		System.err.println("[WBP-Dev]ECCAdapter.eccScheduler() ecc scheduler start ;" + System.currentTimeMillis());
		System.err.println("[WBP-Dev]ECCAdapter.eccScheduler() response  : "
				+ parseDataFromECC("TS00007986,TS20000166", 0, 0, null));
		System.err.println("[WBP-Dev]ECCAdapter.eccScheduler() ecc Scheduler end : " + System.currentTimeMillis());

	}

	public ResponseMessage updatePRDetails(PRUpdateDto updateRequest) {

		ResponseMessage responseMessage = new ResponseMessage();
		// taskDefinitionID="TS00007986";
		responseMessage.setMessage(PMCConstant.CREATE_FAILURE);
		responseMessage.setStatus(PMCConstant.FAILURE);
		responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
		try {
			String taskCollectionUrl = EccAdapterConstant.PR_UPDATE_URL + "(Banfn='" + updateRequest.getBanfn()
					+ "',Bnfpo='" + updateRequest.getBnfpo() + "')";

			// get the ECC url and user credentials from the destination
//		DestinationConfiguration destConfiguration = ServicesUtil.getDest(EccAdapterConstant.ECC_DESTINATION);

//        String url = destConfiguration.getProperty(EccAdapterConstant.DESTINATION_URL);
//        String userName = destConfiguration.getProperty(EccAdapterConstant.DESTINATION_USER);
//        String password = destConfiguration.getProperty(EccAdapterConstant.DESTINATION_PWD);

//        System.err.println("ECCAdapter.updatePRDetails() base URL : "+url);
//        System.err.println("ECCAdapter.updatePRDetails() user Id : "+userName);
//        System.err.println("ECCAdapter.updatePRDetails() password : "+password);

			String payload = "{" + "\"Banfn\" : \"" + updateRequest.getBanfn() + "\", " + "\"Bnfpo\" : \""
					+ updateRequest.getBnfpo() + "\"," + "\"Menge\" : \"" + updateRequest.getMenge() + "\","
					+ "\"Maktx\" : \"" + updateRequest.getMaktx() + "\"" + "}";
			System.err.println("ECCAdapter.updatePRDetails() payload : " + payload);

			RestResponse restResponse = SCPRestUtil.callECCRestService(taskCollectionUrl, null, payload,
					PMCConstant.HTTP_METHOD_PUT, PMCConstant.APPLICATION_JSON, false, "Fetch", user, pass, null, null,
					null, "localhost", 20003);

			System.err.println("ECCAdapter.updatePRDetails() " + restResponse);
			if (restResponse.getResponseCode() >= 200 && restResponse.getResponseCode() <= 208) {
				responseMessage.setMessage(PMCConstant.UPDATE_SUCCESS);
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
			} else {
				responseMessage.setMessage(restResponse.toString());
			}

		} catch (Exception e) {
			System.err.println("ECCAdapter.updatePRDetails() error : " + e);
			responseMessage.setMessage(e.toString());
		}
		return responseMessage;
	}

//	////@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseMessage parseDataFromECC(String taskDefinitionID, int top, int skip, String userId) {

		ResponseMessage responseMessage = new ResponseMessage();

		responseMessage.setMessage(PMCConstant.CREATE_FAILURE);
		responseMessage.setStatus(PMCConstant.FAILURE);
		responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);

		// prepare TASKCOLLECTION url
		String taskCollectionUrl = prepareDetailUrl(taskDefinitionID, top, skip);
		System.err.println("[WBP-Dev]Task Processing url " + taskCollectionUrl);
		System.err.println("[WBP-Dev]AdlsaWbECCAdapter.parseDataFromECC() top : " + top);
		System.err.println("[WBP-Dev]AdlsaWbECCAdapter.parseDataFromECC() Sync URL : " + taskCollectionUrl);
		try {
			if (!ServicesUtil.isEmpty(taskCollectionUrl)) {

//				DestinationConfiguration destConfiguration = ServicesUtil.getDest(EccAdapterConstant.ECC_DESTINATION);

//				String url = destConfiguration.getProperty(getProperty.getValue("DESTINATION_URL"));
//				String userName = destConfiguration.getProperty(getProperty.getValue("DESTINATION_USER"));
//				String password = destConfiguration.getProperty(getProperty.getValue("DESTINATION_PWD"));

//				String url = destConfiguration.getProperty(EccAdapterConstant.DESTINATION_URL);
//				String userName = destConfiguration.getProperty(EccAdapterConstant.DESTINATION_USER);
//				String password = destConfiguration.getProperty(EccAdapterConstant.DESTINATION_PWD);

//				System.err.println("[WBP-Dev]ECCAdapter.parseDataFromECC() base URL : " + url);
//				System.err.println("[WBP-Dev]ECCAdapter.parseDataFromECC() user Id : " + userName);
//				System.err.println("[WBP-Dev]ECCAdapter.parseDataFromECC() password : " + password);

				Long start = System.currentTimeMillis();

				RestResponse restResponse = SCPRestUtil.callECCRestService(taskCollectionUrl, null, null,
						PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, true, null, user, pass, null, null,
						null, "localhost", 20003);

				System.err.println("[WBP-Dev][New]AdlsaWbECCAdapter.parseDataFromECC() ECC TASK FETCH TIME: ----> "
						+ (System.currentTimeMillis() - start) + " user : " + userId);
				System.err.println("[WBP-Dev][New]PersistODataService.getTasksInfo() response code  : "
						+ restResponse.getResponseCode());
				System.err.println("[WBP-Dev][New]PersistODataService.getTasksInfo() response object : "
						+ restResponse.getResponseObject());
				System.err.println("[WBP-Dev]new AdlsaWbECCAdapter.parseDataFromECC() http  response : "
						+ restResponse.getHttpResponse());
				if (restResponse.getResponseCode() == 200) {
					JSONObject jsonObj = (JSONObject) restResponse.getResponseObject();
					if (!ServicesUtil.isEmpty(jsonObj)) {
						JSONArray results = jsonObj.getJSONObject("d").getJSONArray("results");
						start = System.currentTimeMillis();
						TaskDetailsListDto list = eccParser.converToDto(results);
						System.err.println("[WBP-Dev]AdlsaWbECCAdapter.parseDataFromECC() PARSING TIME: ---->"
								+ (System.currentTimeMillis() - start) + " user : " + userId);

						System.err.println("[WBP-Dev]AdlsaWbECCAdapter.parseDataFromECC() tasks length : "
								+ (ServicesUtil.isEmpty(list.getTaskEvensDto()) ? 0 : list.getTaskEvensDto().size()));
						System.err.println("[WBP-Dev]AdlsaWbECCAdapter.parseDataFromECC() process length : "
								+ (ServicesUtil.isEmpty(list.getProcessEventsDto()) ? 0
										: list.getProcessEventsDto().size()));
						System.err.println("[WBP-Dev]AdlsaWbECCAdapter.parseDataFromECC() owners length : "
								+ (ServicesUtil.isEmpty(list.getTaskOwnersDto()) ? 0 : list.getTaskOwnersDto().size()));
						System.err.println("[WBP-Dev]AdlsaWbECCAdapter.parseDataFromECC() length : "
								+ (ServicesUtil.isEmpty(list.getCustomAttributeValueDto()) ? 0
										: list.getCustomAttributeValueDto().size()));

						System.err.println("[WBP-Dev]Owners Data from ecc " + list.getTaskOwnersDto());

						String s = eccAdapterService.updateDataIntoDB(list, userId);

						if (PMCConstant.SUCCESS.equalsIgnoreCase(s)) {
							responseMessage.setMessage(PMCConstant.CREATED_SUCCESS);
							responseMessage.setStatus(PMCConstant.SUCCESS);
							responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
						} else {
							responseMessage.setMessage("Error while storing data in DB ");
							responseMessage.setStatus(s);
						}
					}

				} else {
					responseMessage.setMessage(restResponse.getHttpResponse().toString());

				}

			} else {
				responseMessage.setMessage("No URL found to call ECC service");
				System.err.println("[WBP-Dev]AdlsaWbECCAdapter.parseDataFromECC() url found :" + taskCollectionUrl);
			}

		} catch (Exception e) {
			responseMessage.setMessage("Error while syncing data  : " + e);

			System.err.println("[WBP-Dev]AdlsaWbECCAdapter.parseDataFromECC() " + e);
		}

		return responseMessage;

	}

	/*
	 * public static void main( String[] args) {
	 * 
	 * String url=
	 * "http://34.210.142.28:8080/sap/opu/odata/IWPGW/TASKPROCESSING;v=2/TaskCollection/?$filter"
	 * +
	 * "=(TaskDefinitionID%20eq%20%27TS00007986%27)&$expand=CustomAttributeData&$format=json";
	 * 
	 * // String user="S542077"; // String pass="Incture@2019"; long
	 * start=System.currentTimeMillis(); RestResponse restResponse =
	 * SCPRestUtil.callECCRestService(url, null, null, PMCConstant.HTTP_METHOD_GET,
	 * PMCConstant.APPLICATION_JSON, true, null, user, pass, null, null, null, null,
	 * 0);
	 * 
	 * System.err. println("AdlsaWbECCAdapter.parseDataFromECC() response " +
	 * restResponse.getHttpResponse()); System.err.
	 * println("AdlsaWbECCAdapter.parseDataFromECC() ECC TASK FETCH TIME: ----> "
	 * +(System.currentTimeMillis()-start)); if (restResponse.getResponseCode() ==
	 * 200) { JSONObject jsonObj = (JSONObject) restResponse.getResponseObject(); if
	 * (!ServicesUtil.isEmpty(jsonObj)) { JSONArray results =
	 * jsonObj.getJSONObject("d").getJSONArray("results" );
	 * 
	 * start = System.currentTimeMillis(); TaskDetailsListDto list = new
	 * ECCParser().converToDto(results); System.err.
	 * println("AdlsaWbECCAdapter.parseDataFromECC() PARSING TIME: ---->"
	 * +(System.currentTimeMillis()-start)); System.out.
	 * println("ECCAdapter.main() tasks length : "+list. getTaskEvensDto().size());
	 * System.out. println("ECCAdapter.main() process length length : "
	 * +list.getProcessEventsDto().size()); System.out.
	 * println("ECCAdapter.main() custom attributes length : "
	 * +list.getCustomAttributeValueDto().size());
	 * 
	 * 
	 * } }
	 * 
	 * // parseDataFromECC("TS00007986", 0,0,null); }
	 */

	private String prepareDetailUrl(String taskDefinitionID, int top, int skip) {

		if (ServicesUtil.isEmpty(taskDefinitionID)) {
			/*
			 * 
			 * List<ProcessTypeEntity> processEntity = new ArrayList<>(); long start =
			 * System.currentTimeMillis(); try { // Fetch all active Processes processEntity
			 * = processTypeDao.getAllProcessTypes(true); taskDefinitionID=""; if
			 * (!ServicesUtil.isEmpty(processEntity)) { for (ProcessTypeEntity entity :
			 * processEntity) {
			 * taskDefinitionID=taskDefinitionID+entity.getDefinationId()+","; }
			 * 
			 * //remove last comma
			 * taskDefinitionID=taskDefinitionID.substring(0,taskDefinitionID. length()-1);
			 * } else{ System.err.
			 * println("ECCAdapter.prepareDetailUrl() No Active defination Ids found : size = "
			 * +processEntity.size()); return null; } System.err.
			 * println("ECCAdapter.prepareDetailUrl() defination time  : " +
			 * (System.currentTimeMillis() - start)); } catch (Exception e) {
			 * System.err.println("[WBP-Dev]ECCAdapter.prepareDetailUrl() " + e); }
			 * 
			 */}

		// Base url
		String baseUrl = getProperty.getValue("CC_VIRTUAL_HOST") + getProperty.getValue("TASK_COLLECTION_URL")
				+ PMCConstant.TASK_COLLECTION_RELATIVE_URL + "?" + PMCConstant.JSON_FORMATTER;

		String filter = "";
		if (!ServicesUtil.isEmpty(taskDefinitionID) && taskDefinitionID.contains(",")) {

			String ids[] = taskDefinitionID.split(",");

			filter = PMCConstant.FILTER + "=((";
			for (String i : ids) {
				filter = filter + PMCConstant.TASK_DEFINITION_ID + PMCConstant.SPACE + "eq" + PMCConstant.SPACE
						+ PMCConstant.SINGLE_QUOTE + i + PMCConstant.SINGLE_QUOTE + PMCConstant.SPACE + "or"
						+ PMCConstant.SPACE;
			}
			// remove the last spaces and or condition
			filter = filter.substring(0, filter.length() - 8);
			filter = filter + ")" + PMCConstant.SPACE + "and" + PMCConstant.SPACE + "(Status" + PMCConstant.SPACE + "eq"
					+ PMCConstant.SPACE + PMCConstant.SINGLE_QUOTE + "READY" + PMCConstant.SINGLE_QUOTE
					+ PMCConstant.SPACE + "or" + PMCConstant.SPACE + "Status" + PMCConstant.SPACE + "eq"
					+ PMCConstant.SPACE + PMCConstant.SINGLE_QUOTE + "COMPLETED" + PMCConstant.SINGLE_QUOTE + "))";

		} else if (!ServicesUtil.isEmpty(taskDefinitionID)) {
			filter = PMCConstant.FILTER + "=(" + PMCConstant.TASK_DEFINITION_ID + PMCConstant.SPACE + "eq"
					+ PMCConstant.SPACE + PMCConstant.SINGLE_QUOTE + taskDefinitionID + PMCConstant.SINGLE_QUOTE
					+ PMCConstant.SPACE + "and" + PMCConstant.SPACE + "(Status" + PMCConstant.SPACE + "eq"
					+ PMCConstant.SPACE + PMCConstant.SINGLE_QUOTE + "READY" + PMCConstant.SINGLE_QUOTE
					+ PMCConstant.SPACE + "or" + PMCConstant.SPACE + "Status" + PMCConstant.SPACE + "eq"
					+ PMCConstant.SPACE + PMCConstant.SINGLE_QUOTE + "COMPLETED" + PMCConstant.SINGLE_QUOTE + "))";
		} else {

			filter = PMCConstant.FILTER + "=(Status" + PMCConstant.SPACE + "eq" + PMCConstant.SPACE
					+ PMCConstant.SINGLE_QUOTE + "READY" + PMCConstant.SINGLE_QUOTE + PMCConstant.SPACE + "or"
					+ PMCConstant.SPACE + "Status" + PMCConstant.SPACE + "eq" + PMCConstant.SPACE
					+ PMCConstant.SINGLE_QUOTE + "COMPLETED" + PMCConstant.SINGLE_QUOTE + ")";
		}

		// order by filter
		String orderBy = PMCConstant.CREATED_ON_DESC;

		// expand custom attributes
		String expandCustomAttributes = "$expand=CustomAttributeData";

		return baseUrl + "&" + filter.trim() + "&" + orderBy + "&" + expandCustomAttributes; // +

	}

}
