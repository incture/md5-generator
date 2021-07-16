package oneapp.incture.workbox.demo.adapter_salesforce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adapter_salesforce.dto.ParseResponse;
import oneapp.incture.workbox.demo.adapter_salesforce.util.ApproveTaskInSalesforce;
import oneapp.incture.workbox.demo.adapter_salesforce.util.SalesforceConstant;

@Service
//////@Transactional
public class SalesforceServiceImpl implements SalesforceService{

	@Autowired
	ParseSalesforce parseSalesForce;
	@Autowired
	ProcessEventsDao processEventsDao;
	@Autowired
	TaskEventsDao taskEventsDao;
	@Autowired
	TaskOwnersDao taskOwnersDao;
	@Autowired
	CustomAttributeDao customAttrDao;
	@Autowired
	ApproveTaskInSalesforce approveInSalesforce;

	public String createData() {

		String res = SalesforceConstant.FAILURE;
		long start = System.currentTimeMillis();

		try{

			ParseResponse response = parseSalesForce.parseDetails();
			System.err.println("[WBP-Dev][WORKBOX- Salesforce][parseDetail]" + (System.currentTimeMillis() - start));

			System.err.println(response);

			if(ServicesUtil.isEmpty(response)){
				res = SalesforceConstant.SUCCESS;
				return res;
			}
			start = System.currentTimeMillis();			

			System.err.println(response.getProcesses());
			processEventsDao.saveOrUpdateProcesses(response.getProcesses());
			System.err.println("[WBP-Dev][WORKBOX- Salesforce][saveOrUpdateProcesses]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskEventsDao.saveOrUpdateTasks(response.getTasks());

			System.err.println("[WBP-Dev][WORKBOX- Salesforce][saveOrUpdateTasks]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskOwnersDao.saveOrUpdateOwners(response.getOwners());
			System.err.println("[WBP-Dev][WORKBOX- Salesforce][saveOrUpdateOwners]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			customAttrDao.addCustomAttributeValue(response.getCustomAttributeValues());
			System.err.println("[WBP-Dev][WORKBOX- Salesforce][addCustomAttributeValue]" + (System.currentTimeMillis() - start));

			res = SalesforceConstant.SUCCESS;
			

		}catch (Exception e) {

			System.err.println("[WBP-Dev][parse][SALESFORCE]ERROR"+e.getMessage());
		}

		return res;
	}


}
