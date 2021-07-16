package oneapp.incture.workbox.demo.sapAriba.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.sapAriba.dto.AribaResposeDto;
import oneapp.incture.workbox.demo.sapAriba.dto.ParseResponse;
import oneapp.incture.workbox.demo.sapAriba.util.ParseSapAriba;
import oneapp.incture.workbox.demo.sapAriba.util.SapAribaConstant;

@Service
//@Transactional
public class AribaServiceImpl implements SapAribaService{

	@Autowired
	ParseSapAriba parseAriba;
	@Autowired
	ProcessEventsDao processEventsDao;
	@Autowired
	TaskEventsDao taskEventsDao;
	@Autowired
	TaskOwnersDao taskOwnersDao;
	@Autowired
	CustomAttributeDao customAttrDao;
	@Autowired
	private SessionFactory sessionFactory;


	public String createData() {

		String res = SapAribaConstant.FAILURE;
		long start = System.currentTimeMillis();

		try{
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			System.err.println("AribaServiceImpl.createData()");
			ParseResponse response = parseAriba.parseDetails();
			

			start = System.currentTimeMillis();

			if(ServicesUtil.isEmpty(response)){
				res = SapAribaConstant.SUCCESS;
				return res;
			}
			
			System.err.println("AribaServiceImpl.createData() total processes : "+response.getProcesses().size());
			System.err.println("AribaServiceImpl.createData() total tasks : "+response.getTasks().size());
			System.err.println("AribaServiceImpl.createData()total owners : "+response.getOwners().size());
			System.err.println("AribaServiceImpl.createData() total custom attributes :"+response.getCustomAttributeValues().size());
			
			start=System.currentTimeMillis();
//			processEventsDao.saveOrUpdateProcesses(response.getProcesses());
//			System.err.println("processEvents : " + new Gson().toJson(response.getProcesses()));
//			System.err.println("processes : " + response.getProcesses());
//			System.err.println("[AribaServiceImpl.createData()][saveOrUpdateProcesses]" + (System.currentTimeMillis() - start));
			
			start = System.currentTimeMillis();
//			taskEventsDao.saveOrUpdateTasks(response.getTasks());
//			System.err.println("taskEvents : " + new Gson().toJson(response.getTasks()));
//			System.err.println("tasks : " + response.getTasks());
//			System.err.println("[AribaServiceImpl.createData()][saveOrUpdateTasks]" + (System.currentTimeMillis() - start));
			
			start = System.currentTimeMillis();
//			taskOwnersDao.saveOrUpdateOwners(response.getOwners());
//			System.err.println("taskOwners : " + new Gson().toJson(response.getOwners()));
//			System.err.println("owners : " + response.getOwners());
//			System.err.println("[AribaServiceImpl.createData() [saveOrUpdateOwners]" + (System.currentTimeMillis() - start));
			
			start = System.currentTimeMillis();
//			customAttrDao.addCustomAttributeValue(response.getCustomAttributeValues());
//			System.err.println("customAttributeValues : "  + new Gson().toJson(response.getCustomAttributeValues()));
			System.err.println("cav : " + response.getCustomAttributeValues());
//			System.err.println("[AribaServiceImpl.createData()][addCustomAttributeValue]" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();

			tx.commit();
			session.close();
			res = SapAribaConstant.SUCCESS;
			

		}catch (Exception e) {

			System.err.println("[parse][SAP ARiba]ERROR"+e.getMessage());
		}

		return res;
	}

	@Override
	@Async
	public void updateData(String processId) {

		String res = SapAribaConstant.FAILURE;
		long start = System.currentTimeMillis();

		try{
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			ParseResponse response = parseAriba.parse(processId);

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

			res = SapAribaConstant.SUCCESS;
			tx.commit();
			session.close();
		}catch (Exception e) {

			System.err.println("[parse][SAP ARiba]ERROR"+e.getMessage());
		}

	}

	@Override
	public AribaResposeDto getDetails(String taskId) {
		AribaResposeDto aribaResposeDto = new AribaResposeDto();

		try{
			aribaResposeDto = parseAriba.getDetails(taskId);

		}catch (Exception e) {
			System.out.println("AribaServiceImpl.getDetails()error"+e.getMessage());
		}
		return aribaResposeDto;
	}

	public String getAribaUserId(String user) {
		return parseAriba.getAribaUserId(user);
	}


}
