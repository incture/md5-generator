package oneapp.incture.workbox.demo.scpadapter.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dao.EventsUpdateDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.mapping.AdminParseResponseObject;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.scpadapter.util.AdminParse;
import oneapp.incture.workbox.demo.scpadapter.util.AdminParse.AdminParseResponse;

@Component("eventsUpdateScheduler")
public class EventsUpdateScheduler {

	@Autowired
	AdminParse parse;
	@Autowired
	EventsUpdateDao eventsUpdateDao;
	

	static boolean updateSCP = true;

	// @Autowired
	// TaskEventsDao taskEvents;

	// @Autowired
	// ProcessEventsDao processEvents;
	//
	// @Autowired
	// TaskOwnersDao ownerEvents;
	//
	// @Scheduled(fixedDelay = 6000)
	// public void updateEvents() {
	// System.err.println("[WBP-Dev][PMC]EventsUpdateScheduler started : " + new
	// Date());
	// AdminParseResponse parseResponse = parse.parseDetail();
	// processEvents.saveOrUpdateProcesses(parseResponse.getProcesses());
	// taskEvents.saveOrUpdateTasks(parseResponse.getTasks());
	// List<TaskOwnersDto> owners = parseResponse.getOwners();
	// ownerEvents.saveOrUpdateOwners(owners);
	// System.err.println("[WBP-Dev][PMC]EventsUpdateScheduler ended : " + new
	// Date());
	// }

	public ResponseMessage updateCustomAttributes() {
		AdminParseResponse parseResponse = parse.parseDetail();
		return parse.updateCustomAttributes(parseResponse.getTasks());
	}

	// @Scheduled(fixedDelay = 6000)
	// public void updateCompleteEvents() {
	// System.err.println("[WBP-Dev][PMC]EventsUpdateScheduler started : " + new
	// Date());
	// AdminParseResponse parseResponse = parse.parseCompleteDetail();
	// processEvents.saveOrUpdateProcesses(parseResponse.getProcesses());
	// taskEvents.saveOrUpdateTasks(parseResponse.getTasks());
	// List<TaskOwnersDto> owners = parseResponse.getOwners();
	// ownerEvents.saveOrUpdateOwners(owners);
	// System.err.println("[WBP-Dev][PMC]EventsUpdateScheduler ended : " + new
	// Date());
	// }
	//
	// @SuppressWarnings("unused")
	// private List<TaskOwnersDto> checkOwnersCache(List<TaskOwnersDto> owners)
	// {
	// EhCacheImplementation ownersCache = new
	// EhCacheImplementation("ownersCache");
	// if (ServicesUtil.isEmpty(ownersCache.getKeys())) {
	// // ownersCache.putOwnersInCache(123,);
	// }
	//
	// return owners;
	// }

	/*
	 * public static void main(String[] args) {
	 * 
	 * AnnotationConfigApplicationContext applicationContext = new
	 * AnnotationConfigApplicationContext(HibernateConfiguration.class);
	 * EventsUpdateScheduler eventsUpdateScheduler =
	 * applicationContext.getBean(EventsUpdateScheduler.class);
	 * eventsUpdateScheduler.updateWorkFlowEvents(); applicationContext.close();
	 * 
	 * }
	 */

//	@Scheduled(fixedDelay = 6000)
	public void updateWorkFlowEvents() {

		try {
			System.err.println("[WBP-Dev][parseSCP][SchedulerStart] : " + System.currentTimeMillis());
			System.err.println("[WBP-Dev][parseSCP][parsingStart] : " + System.currentTimeMillis());
			final AdminParseResponseObject parseResponse = parse.parseAPI(updateSCP);
			System.err.println("[WBP-Dev][parseSCP][parsingEnd] : " + System.currentTimeMillis());
			if (!ServicesUtil.isEmpty(parseResponse)) {
				System.err.println("[WBP-Dev][parseSCP][InsertingStart] : " + System.currentTimeMillis());

				Thread processSaveThread = new Thread(new Runnable() {
					@Override
					public void run() {
						eventsUpdateDao.saveOrUpdateProcesses(parseResponse.getProcesses());
					}
				});

				Thread taskSaveThread = new Thread(new Runnable() {
					@Override
					public void run() {
						eventsUpdateDao.saveOrUpdateTasks(parseResponse.getTasks());
					}
				});

				Thread ownerSaveThread = new Thread(new Runnable() {
					@Override
					public void run() {
						eventsUpdateDao.saveOrUpdateOwners(parseResponse.getOwners());
					}
				});

				Thread customAttributeSaveThread = new Thread(new Runnable() {

					@Override
					public void run() {

						// ResponseMessage resp = updateCustomAttributes();
						// System.err.println("[WBP-Dev][parse][customAttributes]
						// : "+resp);
						System.err.println("[WBP-Dev]SCP Attributes:" + parseResponse.getCustomAttributeValues());
						eventsUpdateDao.saveOrUpdateCustomAttributes(parseResponse.getCustomAttributeValues());
					}

				});
				
				Thread taskSaveThreadCustValues = new Thread(new Runnable() {
					@Override
					public void run() {
						System.err.println("EventsUpdateScheduler.updateWorkFlowEvents() cust values total :  "+parseResponse.getCustValues().size());
						eventsUpdateDao.saveOrUpdateCustValues(parseResponse.getCustValues());
					}
				});

				processSaveThread.start();
				taskSaveThread.start();
				ownerSaveThread.start();
				customAttributeSaveThread.start();
				taskSaveThreadCustValues.start();
				// processDetailSaveThread.start();
				// prjMapsSaveThread.start();

				System.err.println("[WBP-Dev][parseSCP][InsertingEnd] : " + System.currentTimeMillis());

			}
			System.err.println("[WBP-Dev][parseSCP][SchedulerEnd] : " + System.currentTimeMillis());
		}

		catch (Exception e) {

			System.err.println("[WBP-Dev][parseSCP][Exception] : " + e.getMessage());

		}
		//updateSCP = false;
	}

}
