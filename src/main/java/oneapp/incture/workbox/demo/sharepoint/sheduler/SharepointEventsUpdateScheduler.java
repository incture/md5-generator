package oneapp.incture.workbox.demo.sharepoint.sheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.sharepoint.util.AdminParseResponse;
import oneapp.incture.workbox.demo.sharepoint.util.AdminParseSharepoint;

@Component("sharepointEventsScheduler")
public class SharepointEventsUpdateScheduler {

	@Autowired
	AdminParseSharepoint adminParseSharepoint;
	@Autowired
	ProcessEventsDao processEventsDao;
	@Autowired
	TaskEventsDao taskEventsDao;
	@Autowired
	TaskOwnersDao taskOwnersDao;
	@Autowired
	CustomAttributeDao customAttrDao;

	public void updateSharepointTasks() {
		System.err.println("[WBP-Dev][WORKBOX- Sharepoint][parseDetail][Start]" + (System.currentTimeMillis()));
		createData();
		System.err.println("[WBP-Dev][WORKBOX- Sharepoint][parseDetail][End]" + (System.currentTimeMillis()));

	}

	public String createData() {
		String res = PMCConstant.FAILURE;
		long start = System.currentTimeMillis();
		try {
			AdminParseResponse response = adminParseSharepoint.parseDetail();
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][parseDetail]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			processEventsDao.saveOrUpdateProcesses(response.getProcesses());
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][saveOrUpdateProcesses]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskEventsDao.saveOrUpdateTasks(response.getTasks());
			;
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][saveOrUpdateTasks]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskOwnersDao.saveOrUpdateOwners(response.getOwners());
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][saveOrUpdateOwners]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			customAttrDao.addCustomAttributeValue(response.getCustomAttributeValues());
			System.err.println("[WBP-Dev][WORKBOX- Sharepoint][addCustomAttributeValue]" + (System.currentTimeMillis() - start));

			res = PMCConstant.SUCCESS;
		} catch (Exception e) {
			System.err.println("[WBP-Dev]SharepointServiceImpl.createData() error " + e);
			
		}
		return res;
	}
}
