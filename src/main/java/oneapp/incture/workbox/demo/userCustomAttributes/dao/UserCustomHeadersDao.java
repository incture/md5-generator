package oneapp.incture.workbox.demo.userCustomAttributes.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.userCustomAttributes.dto.UserCustomHeadersDto;
import oneapp.incture.workbox.demo.userCustomAttributes.entity.UserCustomHeaders;

@Repository
public class UserCustomHeadersDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	public UserCustomHeaders importDto(UserCustomHeadersDto dto) {
		UserCustomHeaders entity = new UserCustomHeaders();
		if(!ServicesUtil.isEmpty(dto)) {
			if(!ServicesUtil.isEmpty(dto.getKey()))
				entity.setKey(dto.getKey());
			if(!ServicesUtil.isEmpty(dto.getLabel()))
				entity.setLabel(dto.getLabel());
			if(!ServicesUtil.isEmpty(dto.getProcessName()))
				entity.setProcessName(dto.getProcessName());
			if(!ServicesUtil.isEmpty(dto.getUserId()))
				entity.setUserId(dto.getUserId());
			if(!ServicesUtil.isEmpty(dto.getIsActive()))
				entity.setIsActive(dto.getIsActive());
			if(!ServicesUtil.isEmpty(dto.getSequence()))
				entity.setSequence(dto.getSequence());
			if(!ServicesUtil.isEmpty(dto.getDataType()))
				entity.setDataType(dto.getDataType());
		}
		return entity;
	}
	
	public UserCustomHeadersDto exportDto(UserCustomHeaders entity) {
		UserCustomHeadersDto dto = new UserCustomHeadersDto();
		if(!ServicesUtil.isEmpty(entity)) {
			if(!ServicesUtil.isEmpty(entity.getKey()))
				dto.setKey(entity.getKey());
			if(!ServicesUtil.isEmpty(entity.getLabel()))
				dto.setLabel(entity.getLabel());
			if(!ServicesUtil.isEmpty(entity.getProcessName()))
				dto.setProcessName(entity.getProcessName());
			if(!ServicesUtil.isEmpty(entity.getUserId()))
				dto.setUserId(entity.getUserId());
			if(!ServicesUtil.isEmpty(entity.getIsActive()))
				dto.setIsActive(entity.getIsActive());
			if(!ServicesUtil.isEmpty(entity.getSequence()))
				dto.setSequence(entity.getSequence());
			if(!ServicesUtil.isEmpty(entity.getDataType()))
				dto.setDataType(entity.getDataType());
		}
		return dto;
	}
	
	public ResponseMessage saveOrUpdateUserCustomization(List<UserCustomHeadersDto> userCustomHeadersDtoList) {
		ResponseMessage response = new ResponseMessage();
		Session session=null;
		try {
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for(UserCustomHeadersDto dto : userCustomHeadersDtoList) {
				session.saveOrUpdate(importDto(dto));
			}
			session.flush();
			session.clear();
			tx.commit();
			session.close();
			response.setMessage("Customization Saved Successfully");
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
		}catch(Exception e) {
			System.err.println("[WBP-Dev]UserCustomHeadersDao.saveOrUpdateUserCustomization() exception : "+e.getMessage());
			response.setMessage("Internal Server Error");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		return response;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CustomAttributeTemplate> getUserDefinedCustomAttributes(String userId, String processName){
		List<CustomAttributeTemplate> list = new ArrayList<>();
		String countQuery = "";
		String mainQuery = "";
		try {
			countQuery = "SELECT COUNT(USER_ID) FROM USER_CUSTOM_HEADERS WHERE UPPER(USER_ID) = UPPER('"+userId+"') AND PROCESS_NAME = '"+processName+"'";
			String countInString = sessionFactory.getCurrentSession().createSQLQuery(countQuery).uniqueResult().toString();
			System.err.println("[WBP-Dev]UserCustomHeadersDao.getUserDefinedCustomAttributes() countInString : "+countInString);
			int count = Integer.parseInt(countInString);
			if(count > 0) {
				System.err.println("[WBP-Dev]UserCustomHeadersDao.getUserDefinedCustomAttributes() check if : "+count);
				mainQuery = "SELECT PROCESS_NAME, KEY, LABEL, "
						+ "IS_ACTIVE, DATA_TYPE FROM USER_CUSTOM_HEADERS WHERE UPPER(USER_ID) = UPPER('"+userId+"') AND "
						+ "PROCESS_NAME IN ('"+processName+"') ORDER BY SEQUENCE ASC";
			}else {
				System.err.println("[WBP-Dev]UserCustomHeadersDao.getUserDefinedCustomAttributes() check else : "+count);
				mainQuery = "SELECT PROCESS_NAME, KEY, LABEL, IS_ACTIVE, DATA_TYPE FROM "
						+ "CUSTOM_ATTR_TEMPLATE WHERE PROCESS_NAME IN ('"+processName+"')";
				System.err.println("[WBP-Dev]UserCustomHeadersDao.getUserDefinedCustomAttributes() check queryresult : "+mainQuery);
			}
			System.err.println("[WBP-Dev]UserCustomHeadersDao.getUserDefinedCustomAttributes() main query : "+mainQuery);
			List<Object[]> resultList = sessionFactory.getCurrentSession().createSQLQuery(mainQuery).list();
			
			for(Object[] obj : resultList) {
				CustomAttributeTemplate template = new CustomAttributeTemplate();
				template.setProcessName(obj[0] != null ? obj[0].toString() : null);
				template.setKey(obj[1] != null ? obj[1].toString() : null);
				template.setLabel(obj[2] != null ? obj[2].toString() : null);
				//template.setIsActive(obj[3].toString().equalsIgnoreCase("1") ? true : false);
				template.setIsActive(ServicesUtil.asBoolean(obj[3]));
				template.setDataType(obj[4] != null ? obj[4].toString() : null);
				list.add(template);
			}
		}catch(Exception e) {
			System.err.println("[WBP-Dev]UserCustomHeadersDao.getUserDefinedCustomAttributes() exception : "+e.getMessage());
		}
		System.err.println("[WBP-Dev]UserCustomHeadersDao.getUserDefinedCustomAttributes() resultList: "+list);
		return list;
	}
	
}
