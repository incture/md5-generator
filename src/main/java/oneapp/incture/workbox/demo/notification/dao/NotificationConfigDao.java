package oneapp.incture.workbox.demo.notification.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

//import com.sap.db.jdbc.SessionFactory;

import oneapp.incture.workbox.demo.notification.entity.NotificationConfigDo;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.notification.dto.NotificationActionDto;
import oneapp.incture.workbox.demo.notification.dto.NotificationConfigDto;
import oneapp.incture.workbox.demo.workflow.dao.CrossConstantDao;

@Repository
public class NotificationConfigDao extends BaseDao<NotificationConfigDo, NotificationConfigDto> {

	@Autowired
	CrossConstantDao crossConstantDao;
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected NotificationConfigDo importDto(NotificationConfigDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		NotificationConfigDo entity = null;
		if (!ServicesUtil.isEmpty(fromDto)) {
			entity = new NotificationConfigDo();
			if (!ServicesUtil.isEmpty(fromDto.getUserId()))
				entity.setUserId(fromDto.getUserId());
			if (!ServicesUtil.isEmpty(fromDto.getUserName()))
				entity.setUserName(fromDto.getUserName());
			if (!ServicesUtil.isEmpty(fromDto.getEnableChannel()))
				entity.setEnableChannel(fromDto.getEnableChannel());
			if (!ServicesUtil.isEmpty(fromDto.getEnableAction()))
				entity.setEnableAction(fromDto.getEnableAction());
			if (!ServicesUtil.isEmpty(fromDto.getEventOrigin()))
				entity.setSourceEvent(fromDto.getEventOrigin());
		}
		return entity;
	}

	@Override
	protected NotificationConfigDto exportDto(NotificationConfigDo entity) {
		// TODO Auto-generated method stub
		NotificationConfigDto notificationDto = new NotificationConfigDto();
		if (!ServicesUtil.isEmpty(entity.getUserId()))
			notificationDto.setUserId(entity.getUserId());
		if (!ServicesUtil.isEmpty(entity.getUserName()))
			notificationDto.setUserName(entity.getUserName());
		if (!ServicesUtil.isEmpty(entity.getEnableChannel()))
			notificationDto.setEnableChannel(entity.getEnableChannel());
		if (!ServicesUtil.isEmpty(entity.getEnableAction()))
			notificationDto.setEnableAction(entity.getEnableAction());
		return notificationDto;
	}

	public void saveOrUpdateNotificaionConfigs(List<NotificationConfigDto> notificationDtos) {
		Session session = null;
		if (!ServicesUtil.isEmpty(notificationDtos) && !notificationDtos.isEmpty()) {
			//session = this.getSession();
			 session = sessionFactory.openSession();
			 Transaction tx = session.beginTransaction();	 
			for (int i = 0; i < notificationDtos.size(); i++) {
				NotificationConfigDto currentTask = notificationDtos.get(i);
				try {
					session.saveOrUpdate(importDto(currentTask));
				} catch (InvalidInputFault | ExecutionFault | NoResultFault e) {
					e.printStackTrace();
				}
				if (i % 20 == 0 && i > 0) {
					session.flush();
					session.clear();
				}
			}
			session.flush();
			session.clear();
		    tx.commit();
            session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<NotificationActionDto> getConfiguration(String userId) {

		List<NotificationActionDto> actionDtos = new ArrayList<>();
		
		NotificationActionDto actionDto = null;
		try {
			String queryString = "select do from NotificationConfigDo do";

			if (!ServicesUtil.isEmpty(userId)) {
				queryString += " where do.userId='" + userId + "'";
			} else {
				queryString += " where do.userId='Admin' ";
			}
			System.err.println(" [WBP-Dev][Workbox] Query String: " + queryString);

			Query query = this.getSession().createQuery(queryString);
			List<NotificationConfigDo> resultList = query.list();
			Map<String, List<String>> channelMap = new HashMap<>();
			Map<String, String> originMap = new HashMap<>();
			
			List<Object[]> fetchResult = crossConstantDao.getActionValues("Notification Events");
			
			if(!ServicesUtil.isEmpty(fetchResult)){
				for (Object[] obj : fetchResult) {
					
					originMap.put(obj[1].toString(), obj[0].toString());
				}
			}
			
			System.err.println("MAP : " + originMap);

			if (!ServicesUtil.isEmpty(resultList)) {
				for (NotificationConfigDo notificationConfigDo : resultList) {
					List<String> channels = new ArrayList<>();
					if (channelMap.containsKey(notificationConfigDo.getEnableAction())) {
						channels = channelMap.get(notificationConfigDo.getEnableAction());
						channels.add(notificationConfigDo.getEnableChannel());
					} else {
						channels.add(notificationConfigDo.getEnableChannel());
						
					}
					
					channelMap.put(notificationConfigDo.getEnableAction(), channels);
				}
				actionDtos = new ArrayList<>();
				
				for (Map.Entry<String, List<String>> entry : channelMap.entrySet()) {
					actionDto = new NotificationActionDto();
					actionDto.setEventOrigin(originMap.get(entry.getKey()));
					actionDto.setActionType(entry.getKey());
					actionDto.setChannelLists(entry.getValue());
					actionDtos.add(actionDto);
					
					if (originMap.containsKey(entry.getKey()))
						originMap.remove(entry.getKey());
					System.err.println(
							"[WBP-Dev][Workbox] ActionType: " + entry.getKey() + ", ChannelList: " + entry.getValue());
				}
				if (!ServicesUtil.isEmpty(originMap)) {
					for (Map.Entry<String, String> entry : originMap.entrySet()) {
						actionDto = new NotificationActionDto();
						actionDto.setActionType(entry.getKey());
						actionDto.setEventOrigin(entry.getValue());
						actionDto.setChannelLists(new ArrayList<>());
						actionDtos.add(actionDto);
					}
				}

			}
			// this below logic need to get updated once all the channels are
			// being called from UI setting
			else {

				actionDtos = new ArrayList<>();

				if (!ServicesUtil.isEmpty(originMap)) {
					for (Map.Entry<String, String> entry : originMap.entrySet()) {
						actionDto = new NotificationActionDto();
						actionDto.setActionType(entry.getKey());
						actionDto.setEventOrigin(entry.getValue());
						actionDto.setChannelLists(new ArrayList<>());
						actionDtos.add(actionDto);
					}
				}
			}
		} catch (Exception e) {
			System.err.println(
					"[WBP-Dev][Workbox][PMC][SubstitutionRuleFacade][SubstitutionRuleDao][getSubstitution][error]"
							+ e.getLocalizedMessage());
		}
		return actionDtos;
	}

}
