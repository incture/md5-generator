package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dto.ActionConfigDto;
import oneapp.incture.workbox.demo.adapter_base.entity.ActionConfigDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class ActionConfigDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String,Map<String,List<ActionConfigDto>>> getActionConfig(String inboxType) {
		Map<String,Map<String,List<ActionConfigDto>>> dto = new HashMap<>();
		Map<String,List<ActionConfigDto>> taskDto = null;
		List<ActionConfigDto> actionsDto = null;
		ActionConfigDto actionConfigDto = null;
		Query q = this.getSession().createSQLQuery("SELECT AC.PROCESS_NAME ||':' ||AC.TASK_NAME,AC.STATUS,AC.ACTIONS,AC.TYPE,AC.ACTION_NATURE"
				+ " FROM ACTION_CONFIG AC "
				+ "INNER JOIN INBOX_ACTION_CONFIG IAC ON IAC.ACTIONS = AC.ACTIONS "
				+ "WHERE IAC.INBOX_TYPE = '"+inboxType+"' ORDER BY TYPE");
		List<Object[]> list = q.list();
		
		for (Object[] obj : list) {
			if(dto.containsKey((String)obj[0])){
				taskDto = dto.get(obj[0].toString());
				if(taskDto.containsKey(obj[1].toString())){
					actionsDto = taskDto.get(obj[1].toString());
					actionConfigDto = new ActionConfigDto();
					actionConfigDto.setAction(obj[2].toString());
					actionConfigDto.setType(obj[3].toString());
					actionConfigDto.setActionType(ServicesUtil.isEmpty(obj[4])? null : obj[4].toString());
					actionsDto.add(actionConfigDto);
				}else{
					actionsDto = new ArrayList<>();
					actionConfigDto = new ActionConfigDto();
					actionConfigDto.setAction(obj[2].toString());
					actionConfigDto.setType(obj[3].toString());
					actionConfigDto.setActionType(ServicesUtil.isEmpty(obj[4])? null : obj[4].toString());
					actionsDto.add(actionConfigDto);
					taskDto.put(obj[1].toString(), actionsDto);
				}
			}else{
				taskDto = new HashMap<>();
				actionsDto = new ArrayList<>();
				actionConfigDto = new ActionConfigDto();
				actionConfigDto.setAction(obj[2].toString());
				actionConfigDto.setType(obj[3].toString());
				actionConfigDto.setActionType(ServicesUtil.isEmpty(obj[4])? null : obj[4].toString());
				actionsDto.add(actionConfigDto);
				taskDto.put(obj[1].toString(), actionsDto);
				dto.put(obj[0].toString(), taskDto);
			}
		}
		Gson g = new Gson();
		
		System.err.println("dto"+g.toJson(dto));
		return dto;
	}

	public void savaOrUpdateActionConfig(List<ActionConfigDo> actionConfigDos) {
		
		try {
			if (!ServicesUtil.isEmpty(actionConfigDos) && !actionConfigDos.isEmpty()) {
				Session session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < actionConfigDos.size(); i++) {
					ActionConfigDo currentTask = actionConfigDos.get(i);
					session.saveOrUpdate(currentTask);
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
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Saving of Action config ERROR" + e.getMessage());
		}
	}
}
