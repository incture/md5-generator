package oneapp.incture.workbox.demo.dynamicpage.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageGroupResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.ProcessDetailsMasterDto;
import oneapp.incture.workbox.demo.dynamicpage.entity.CatalogueGroupMapping;
import oneapp.incture.workbox.demo.dynamicpage.entity.ProcessDetailsCatalogueMapping;

@Repository
public class DynamicPageDetailsDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public DynamicPageResponseDto getDynamicDetailsForAdmin(String plant, String processName) {
		DynamicPageResponseDto response = new DynamicPageResponseDto();
		String query = "";
		String countQuery = "";
		Integer count = null;
		try {
			countQuery = "SELECT COUNT(*) FROM CATALOGUE_GROUP_MAPPING WHERE "
					+ "CATALOGUE_NAME = '"+plant+"' AND PROCESS_NAME = '"+processName+"'";
			System.err.println("DynamicPageDetailsDao.getDynamicDetailsForAdmin() count query : "+countQuery);
			
			Object countObj = this.getSession().createSQLQuery(countQuery).uniqueResult();
			count = Integer.parseInt(countObj.toString());
			
			if(count > 0) {
				query = "SELECT DISTINCT GM.CATALOGUE_NAME HEADER_PLANT, GM.GROUP_ID HEADER_GROUP_ID, GM.GROUP_TYPE, GM.VISIBILITY HEADER_VISIBILITY, "
						+ "GM.EDITABILITY HEADER_EDITABILITY, GM.TITLE, PM.KEY, PM.LABEL, PM.IS_MANDATORY, "
						+ "PM.DATA_TYPE,PM.VISIBILITY PO_VISIBILITY, PM.SERVICE_URL, PM.DEPENDENCY, PM.GROUP_ID, PM.ECC_KEY, "
						+ "PM.EDITABILITY FROM CATALOGUE_GROUP_MAPPING GM INNER JOIN PROCESS_DETAILS_CATALOGUE_MAPPING PM "
						+ "ON PM.GROUP_ID = GM.GROUP_ID WHERE GM.CATALOGUE_NAME = '"+plant+"' AND "
						+ "GM.PROCESS_NAME = '"+processName+"' AND PM.PROCESS_NAME = '"+processName+"' "
						+ "AND PM.CATALOGUE_NAME = '"+plant+"' ORDER BY SUBSTRING(GM.GROUP_ID,2,1) ASC";
				System.err.println("DynamicPageDetailsDao.getDynamicDetailsForAdmin() query : "+query);
				List<Object[]> obj = this.getSession().createSQLQuery(query).list();
				List<DynamicPageGroupResponseDto> groupList = getDynamicPageDto(obj);
				response.setGroupList(groupList);
				response.setMessage("Dynamic Details Fetched Successfully");
				response.setStatus(PMCConstant.SUCCESS);
				response.setStatusCode(PMCConstant.CODE_SUCCESS);
			}else {
				query = "SELECT GT.GROUP_ID, GT.GROUP_TYPE, GT.VISIBILITY, GT.IS_EDITABLE, "
						+ "GT.TITLE, POM.KEY, POM.LABEL, POM.IS_MANDATORY, POM.DATA_TYPE, "
						+ "POM.VISIBILITY PO_VISIBILITY, POM.SERVICE_URL, POM.DEPENDENCY, POM.GROUP_ID PO_GROUP_ID, "
						+ "POM.ECC_KEY, POM.EDITABILITY FROM DYNAMIC_GROUPS_TEMPLATE GT "
						+ "INNER JOIN PROCESS_DETAILS_MASTER POM ON POM.GROUP_ID = GT.GROUP_ID "
						+ "WHERE POM.PROCESS_NAME = '"+processName+"' AND GT.PROCESS_NAME = '"+processName+"' "
						+ "ORDER BY SUBSTRING(GT.GROUP_ID,2,1) ASC";
				
				System.err.println("DynamicPageDetailsDao.getDynamicDetailsForAdmin() query : "+query);
				List<Object[]> obj = this.getSession().createSQLQuery(query).list();
				List<DynamicPageGroupResponseDto>  list = getCustomDynamicPageDto(obj);
				response.setGroupList(list);
				response.setMessage("Dynamic Details Fetched Successfully");
				response.setStatus(PMCConstant.SUCCESS);
				response.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
			
		}catch(Exception e) {
			System.err.println("DynamicPageDetailsDao.getDynamicDetailsForAdmin() exception : "+e.getMessage());
			response.setMessage("Internal Server Error");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		
		return response;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public List<DynamicPageGroupResponseDto> getDynamicPageDto(List<Object[]> objectList) {
		List<DynamicPageGroupResponseDto> groupList = new ArrayList<DynamicPageGroupResponseDto>();
		List<String> checkList = new ArrayList<String>();
		
		List<String> groupIdsList = objectList.stream().map(a -> a[1].toString()).distinct().collect(Collectors.toList());
		System.err.println("DynamicPageDetailsDao.getDynamicPageDto() groupIds : "+groupIdsList);
		
		for(String groupId : groupIdsList) {
			List<ProcessDetailsMasterDto> poFieldList = new ArrayList<ProcessDetailsMasterDto>();
			DynamicPageGroupResponseDto groupDto = new DynamicPageGroupResponseDto();
			
			List<Object[]> poGroupList = objectList.stream().filter(a -> a[1].equals(groupId)).collect(Collectors.toList());
			
			for(Object[] obj : poGroupList) {
				ProcessDetailsMasterDto poMasterDto = new ProcessDetailsMasterDto();
				
				if(ServicesUtil.isEmpty(checkList) || !checkList.contains(obj[1].toString())) {
					groupDto.setCatalogueName(obj[0]!=null ? obj[0].toString() : null);
					groupDto.setGroupId(obj[1]!=null ? obj[1].toString() : null);
					groupDto.setGroupType(obj[2]!=null ? obj[2].toString() : null);
					groupDto.setVisibility(obj[3].toString().equalsIgnoreCase("1") ? true : false);
					groupDto.setIsEditable(obj[4].toString().equalsIgnoreCase("1") ? true : false);
					groupDto.setTitle(obj[5]!=null ? obj[5].toString() : null);
					checkList.add(obj[1].toString());
				}
				
				poMasterDto.setKey(obj[6]!=null ? obj[6].toString() : null);
				poMasterDto.setLabel(obj[7]!=null ? obj[7].toString() : null);
				poMasterDto.setIsMandatory(obj[8].toString().equalsIgnoreCase("1") ? true : false);
				poMasterDto.setDataType(obj[9]!=null ? obj[9].toString() : null);
				poMasterDto.setVisibility(obj[10].toString().equalsIgnoreCase("1") ? true : false);
				poMasterDto.setServiceUrl(obj[11]!=null ? obj[11].toString() : null);
				poMasterDto.setDependency(obj[12]!=null ? obj[12].toString() : null);
				poMasterDto.setGroupId(obj[13]!=null ? obj[13].toString() : null);
				poMasterDto.setEccKey(obj[14]!=null ? obj[14].toString() : null);
				poMasterDto.setEditability(obj[15].toString().equalsIgnoreCase("1") ? true : false);
				poFieldList.add(poMasterDto);
			}
			groupDto.setPoFieldDetails(poFieldList);
			groupList.add(groupDto);
		}
		return groupList;
	}
	
	public List<DynamicPageGroupResponseDto>  getCustomDynamicPageDto(List<Object[]> objectList){
		List<DynamicPageGroupResponseDto> groupList = new ArrayList<DynamicPageGroupResponseDto>();
		List<String> checkList = new ArrayList<String>();
		
		List<String> groupIdsList = objectList.stream().map(a -> a[0].toString()).distinct().collect(Collectors.toList());
		System.err.println("DynamicPageDetailsDao.getCustomDynamicPageDto() groupIds : "+groupIdsList);
		
		for(String groupId : groupIdsList) {
			List<ProcessDetailsMasterDto> poFieldList = new ArrayList<ProcessDetailsMasterDto>();
			DynamicPageGroupResponseDto groupDto = new DynamicPageGroupResponseDto();
			
			List<Object[]> poGroupList = objectList.stream().filter(a -> a[0].equals(groupId)).collect(Collectors.toList());
			System.err.println("DynamicPageDetailsDao.getCustomDynamicPageDto() poGroupList : "+poGroupList);
			
			for(Object[] obj : poGroupList) {
				ProcessDetailsMasterDto poMasterDto = new ProcessDetailsMasterDto();
				
				if(ServicesUtil.isEmpty(checkList) || !checkList.contains(obj[0].toString())) {
					groupDto.setCatalogueName(null);
					groupDto.setGroupId(obj[0]!=null ? obj[0].toString() : null);
					groupDto.setGroupType(obj[1]!=null ? obj[1].toString() : null);
					groupDto.setVisibility(obj[2].toString().equalsIgnoreCase("1") ? true : false);
					groupDto.setIsEditable(obj[3].toString().equalsIgnoreCase("1") ? true : false);
					groupDto.setTitle(obj[4]!=null ? obj[4].toString() : null);
					checkList.add(obj[0].toString());
				}
				
				poMasterDto.setKey(obj[5]!=null ? obj[5].toString() : null);
				poMasterDto.setLabel(obj[6]!=null ? obj[6].toString() : null);
				poMasterDto.setIsMandatory(obj[7].toString().equalsIgnoreCase("1") ? true : false);
				poMasterDto.setDataType(obj[8]!=null ? obj[8].toString() : null);
				poMasterDto.setVisibility(obj[9].toString().equalsIgnoreCase("1") ? true : false);
				poMasterDto.setServiceUrl(obj[10]!=null ? obj[10].toString() : null);
				poMasterDto.setDependency(obj[11]!=null ? obj[11].toString() : null);
				poMasterDto.setGroupId(obj[12]!=null ? obj[12].toString() : null);
				poMasterDto.setEccKey(obj[13]!=null ? obj[13].toString() : null);
				poMasterDto.setEditability(obj[14].toString().equalsIgnoreCase("1") ? true : false);
				poFieldList.add(poMasterDto);
			}
			groupDto.setPoFieldDetails(poFieldList);
			groupList.add(groupDto);
		}
		return groupList;	
	}
	
	public ResponseMessage saveOrUpdateCustomization(List<DynamicPageGroupResponseDto> dynamicPageDtoList) {
		ResponseMessage response = new ResponseMessage();
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			for(DynamicPageGroupResponseDto dto : dynamicPageDtoList) {
				CatalogueGroupMapping groupEntity = new CatalogueGroupMapping();
				groupEntity.setCatalogueName(dto.getCatalogueName());
				groupEntity.setGroupId(dto.getGroupId());
				groupEntity.setGroupType(dto.getGroupType());
				groupEntity.setVisibility(dto.getVisibility()!=null ? dto.getVisibility() : null);
				groupEntity.setEditability(dto.getIsEditable()!=null ? dto.getIsEditable() : null);
				groupEntity.setTitle(dto.getTitle()!=null ? dto.getTitle() : null);
				groupEntity.setProcessName(dto.getProcessName()!=null ? dto.getProcessName() : null);
				
				session.saveOrUpdate(groupEntity);
				
				for(ProcessDetailsMasterDto poDto : dto.getPoFieldDetails()) {
					ProcessDetailsCatalogueMapping poEntity = new ProcessDetailsCatalogueMapping();
					poEntity.setCatalogueName(dto.getCatalogueName());
					poEntity.setKey(poDto.getKey());
					poEntity.setLabel(poDto.getLabel()!=null ? poDto.getLabel() : null);
					poEntity.setIsMandatory(poDto.getIsMandatory());
					poEntity.setDataType(poDto.getDataType()!=null ? poDto.getDataType() : null);
					poEntity.setVisibility(poDto.getVisibility());
					poEntity.setServiceUrl(poDto.getServiceUrl()!=null ? poDto.getServiceUrl() : null);
					poEntity.setDependency(poDto.getDependency()!=null ? poDto.getDependency() : null);
					poEntity.setGroupId(dto.getGroupId()!=null ? dto.getGroupId() : null);
					poEntity.setEccKey(poDto.getEccKey()!=null ? poDto.getEccKey() : null);
					poEntity.setEditability(poDto.getEditability()!=null ? poDto.getEditability() : null);
					poEntity.setProcessName(poDto.getProcessName()!=null ? poDto.getProcessName() : null);
					
					session.saveOrUpdate(poEntity);
					
				}
			}
			
			tx.commit();
			session.close();
			response.setMessage("Details Saved Successfully");
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
		}catch(Exception e) {
			System.err.println("DynamicPageDetailsDao.saveOrUpdateCustomization() exception : "+e.getMessage());
			response.setMessage("Internal Server Error");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		return response;
	}
	
	public ResponseMessage resetToDefault(String plant, String processName) {
		ResponseMessage response = new ResponseMessage();
		String query1 = "";
		String query2 = "";
		try {
			query1 = "DELETE FROM CATALOGUE_GROUP_MAPPING WHERE CATALOGUE_NAME = '"+plant+"' AND PROCESS_NAME = '"+processName+"' ";
			query2 = "DELETE FROM PROCESS_DETAILS_CATALOGUE_MAPPING WHERE CATALOGUE_NAME = '"+plant+"' AND PROCESS_NAME = '"+processName+"' ";
			
			int query1Count = this.getSession().createSQLQuery(query1).executeUpdate();
			int query2Count = this.getSession().createSQLQuery(query2).executeUpdate();
			
			if(query1Count > 0 && query2Count > 0) {
				response.setMessage("Default Setting Applied Successfully");
				response.setStatus(PMCConstant.SUCCESS);
				response.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
			else {
				response.setMessage("Already In Default State");
				response.setStatus(PMCConstant.SUCCESS);
				response.setStatusCode(PMCConstant.CODE_SUCCESS);
			}
		}catch(Exception e) {
			System.err.println("DynamicPageDetailsDao.resetToDefault() exception : "+e.getMessage());
			response.setMessage("Internal Server Error");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		
		return response;
	}
}
