package oneapp.incture.workbox.demo.sapAriba.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.sapAriba.dto.AribaActionDto;
import oneapp.incture.workbox.demo.sapAriba.util.AribaOAuthTokenGenerator;
import oneapp.incture.workbox.demo.sapAriba.util.AribaUtil;
import oneapp.incture.workbox.demo.sapAriba.util.ParseSapAriba;
import oneapp.incture.workbox.demo.sapAriba.util.SCPRestUtil;

@Service
//@Transactional
public class AribaActionFacade implements AribaActionFacadeLocal {

	@Autowired
	TaskEventsDao taskEvents;

	@Autowired
	ParseSapAriba parseSapAriba;
	
	@Autowired
	ProcessEventsDao processEventsDao;
	@Autowired
	TaskOwnersDao taskOwnersDao;
	@Autowired
	AribaUtil aribaUtil;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/*@Autowired
	AribaServiceImpl aribaService; */

	public ResponseMessage performAction(AribaActionDto actionDto) {
		System.out
				.println("AribaActionService.performAction() action Payload: " + new JSONObject(actionDto).toString());
		// add logic to perform ariba actions
		ResponseMessage message = new ResponseMessage();
		message.setMessage(PMCConstant.FAILURE);
		message.setStatus(PMCConstant.FAILURE);
		message.setStatusCode(PMCConstant.CODE_FAILURE);
		if (!ServicesUtil.isEmpty(actionDto) && !ServicesUtil.isEmpty(actionDto.getUser())) {
			try{
			String user=parseSapAriba.getAribaUserId(actionDto.getUser());
			if(user.equalsIgnoreCase("Ariba Id not Found")){
				System.err.println("AribaActionService.performAction() "+user);
			}else{
				System.err.println("AribaActionService.performAction() ariba user Id :"+user);
				actionDto.setUser(user);
			}
			}catch(Exception e){
				System.err.println("AribaActionService.performAction() Error while getting the ariba Id "+e);
				
			}
			if (!ServicesUtil.isEmpty(actionDto) && !ServicesUtil.isEmpty(actionDto.getActionBody())) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				String token[] = AribaOAuthTokenGenerator.getToken();
				JSONObject payload = new JSONObject(actionDto.getActionBody());
				System.out.println("AribaActionService.performAction() body payload : " + payload.toString());
			
				String actionUrl = "https://mn1.openapi.ariba.com/api/sourcing-approval/v2/prod" + "/action?realm="
						+ actionDto.getRealm() + "&user=" + actionDto.getUser() + "&passwordadapter="
						+ actionDto.getPasswordAdapter();
				//
				// Add Api Key to the Headers
				String header = "apiKey " + actionDto.getApiKey();
				System.out.println("AribaActionService.performAction() URL : " + actionUrl);
				System.out.println("AribaActionService.performAction() token : " + token[0]);
				System.out.println("AribaActionService.performAction() token type " + token[1]);
				RestResponse restResponse = SCPRestUtil.callRestService(actionUrl, header, payload.toString(), "POST",
						"application/json", false, null, null, null, token[0], "Bearer");

				System.err.println("WorkFlowActionFacade.approveTask() Object :" + restResponse);
				if (!ServicesUtil.isEmpty(restResponse) && (restResponse.getResponseCode() >= 200)
						&& (restResponse.getResponseCode() <= 207)) {
					try {
						taskEvents.updateTaskEventToCompleted(actionDto.getId());
//						taskEvents.updateTaskComment(actionDto.getId(), actionDto.getUser(),
//								actionDto.getActionBody().getOptions().getComment(),
//								actionDto.getActionBody().getActionName());
						
					} catch (Exception e) {
						System.out.println("AribaActionService.performAction() error while updating tables : " + e);
					}
					aribaUtil.updateData(actionDto.getActionBody().getUniqueName());
					tx.commit();
					session.close();
					message.setMessage("Approved Succefully by : " + actionDto.getUser());
					message.setStatus(PMCConstant.SUCCESS);
					message.setStatusCode(PMCConstant.CODE_SUCCESS);
				} else {
					message.setMessage(restResponse.getResponseObject().toString());
				}

			}
		}else{
			message.setMessage("User Id Not found : ");
		}
		
		return message;
	}
	

	// public static void main(String[] args) {
	// AribaActionDto actionDto=new AribaActionDto();
	// AribaActionBodyDto body=new AribaActionBodyDto();
	// actionDto.setRealm("aldahra-T");
	// actionDto.setUser("allwyn.augustine@incture.com");
	// actionDto.setPasswordAdapter("ThirdPartyUser");
	// actionDto.setApiKey("P2eYXWSNblfd2ag4wCvrN8nc7GkedC0b");
	//
	//
	// body.setActionableType("Task");
	// body.setUniqueName("TSK157049337");
	// body.setActionName("Approve");
	// body.setOptions(new ActionComment("Approved"));
	//
	// actionDto.setActionBody(body);
	//
	// System.out.println("AribaActionService.main()"+performAction(actionDto));
	//
	//
	// }
}
