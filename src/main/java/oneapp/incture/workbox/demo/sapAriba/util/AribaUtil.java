package oneapp.incture.workbox.demo.sapAriba.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.sapAriba.dto.ParseResponse;

@Service
@Transactional
public class AribaUtil {
	
	@Autowired
	ParseSapAriba parseSapAriba;
	@Autowired
	ProcessEventsDao processEventsDao;
	@Autowired
	TaskEventsDao taskEventsDao;
	@Autowired
	TaskOwnersDao taskOwnersDao;
	@Autowired
	private SessionFactory sessionFactory;

	@Async
	public void updateData(String processId) {

		String res = SapAribaConstant.FAILURE;
		long start = System.currentTimeMillis();

		try{
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			ParseResponse response = parseSapAriba.parse(processId);

			if(ServicesUtil.isEmpty(response)){
				res = SapAribaConstant.SUCCESS;
			}

			System.err.println(response.getProcesses());
			processEventsDao.saveOrUpdateProcesses(response.getProcesses());
			System.err.println("[WORKBOX- SAP ARiba][saveOrUpdateProcesses]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskEventsDao.saveOrUpdateTasks(response.getTasks());

			System.err.println("[WORKBOX- SAP ARiba][saveOrUpdateTasks]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			taskOwnersDao.saveOrUpdateOwners(response.getOwners());
			System.err.println("[WORKBOX- SAP ARiba][saveOrUpdateOwners]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			tx.commit();
			session.close();
			res = SapAribaConstant.SUCCESS;
			
		}catch (Exception e) {

			System.err.println("[parse][SAP ARiba]ERROR"+e.getMessage());
		}

	}
}
