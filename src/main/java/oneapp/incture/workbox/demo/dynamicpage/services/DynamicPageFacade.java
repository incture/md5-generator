package oneapp.incture.workbox.demo.dynamicpage.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.GenericResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dynamicpage.dao.DynamicPageDetailsDao;
import oneapp.incture.workbox.demo.dynamicpage.dao.POFieldsTemplateDao;
import oneapp.incture.workbox.demo.dynamicpage.dao.TaskTypeDependentValuesDao;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageGroupResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.DynamicPageResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.POFieldsTemplateResponseDto;
import oneapp.incture.workbox.demo.dynamicpage.dto.ProcessDetailsMasterDto;


@Service("dynamicPageFacade")
//////@Transactional
public class DynamicPageFacade implements DynamicPageFacadeLocal{
	
	@Autowired
	private DynamicPageDetailsDao dynamicPageDao;
	
	@Autowired
	private POFieldsTemplateDao poFieldsTemplateDao;
	
	@Autowired
	private TaskTypeDependentValuesDao taskTypeDependentValueDao;

	@Override
	public DynamicPageResponseDto getDynamicDetailsForAdmin(String plant, String processName) {
		return dynamicPageDao.getDynamicDetailsForAdmin(plant,processName);
	}


	@Override
	public ResponseMessage saveOrUpdateCustomization(List<DynamicPageGroupResponseDto> dynamicPageDtoList) {
		ResponseMessage response = new ResponseMessage();
		
		if(ServicesUtil.isEmpty(dynamicPageDtoList)) {
			response.setMessage("Request Body cannot be empty");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}else {
			response = dynamicPageDao.saveOrUpdateCustomization(dynamicPageDtoList);
		}
		return response;
	}


	@Override
	public GenericResponseDto getPOFieldValues(String fieldName, String dependencyKey, String dependencyValue, String taskType) {
		GenericResponseDto response = new GenericResponseDto();
		List<POFieldsTemplateResponseDto> result = new ArrayList<POFieldsTemplateResponseDto>();
		
		if(ServicesUtil.isEmpty(dependencyKey) && ServicesUtil.isEmpty(dependencyValue) && ServicesUtil.isEmpty(taskType)) {
			result = poFieldsTemplateDao.getFieldValues(fieldName);
		}
		else {
			result = taskTypeDependentValueDao.getFieldValues(fieldName, dependencyKey, dependencyValue, taskType);
		}
		if(ServicesUtil.isEmpty(result)) {
			response.setMessage("Data Not Found");
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
		}
		else {
			response.setData(result);
			response.setMessage("Data Fetched Successfully");
			response.setStatus(PMCConstant.SUCCESS);
			response.setStatusCode(PMCConstant.CODE_SUCCESS);
		}
		return response;
	}


	@Override
	public DynamicPageResponseDto getDynamicDetailsForUser(String plant, String processName) {
		DynamicPageResponseDto dynamicDto = new DynamicPageResponseDto();
		List<DynamicPageGroupResponseDto> newList = new ArrayList<DynamicPageGroupResponseDto>();
		try {
			dynamicDto = dynamicPageDao.getDynamicDetailsForAdmin(plant,processName);
			
			List<DynamicPageGroupResponseDto> dynamicGroupDto = dynamicDto.getGroupList().stream().filter(a -> a.getVisibility().equals(true)).collect(Collectors.toList());
			
			for(DynamicPageGroupResponseDto dto : dynamicGroupDto) {
				List<ProcessDetailsMasterDto> poMasterDtoList = dto.getPoFieldDetails().stream().filter(a -> a.getVisibility().equals(true)).collect(Collectors.toList());
				dto.setPoFieldDetails(poMasterDtoList);
				newList.add(dto);
			}
			dynamicDto.setGroupList(newList);
			dynamicDto.setMessage("Dynamic Details Fetched Successfully");
			dynamicDto.setStatus(PMCConstant.SUCCESS);
			dynamicDto.setStatusCode(PMCConstant.CODE_SUCCESS);
		}catch(Exception e) {
			System.err.println("DynamicPageFacade.getDynamicDetailsForUser() exception : "+e.getMessage());
			dynamicDto.setMessage("Internal Server Error");
			dynamicDto.setStatus(PMCConstant.FAILURE);
			dynamicDto.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		return dynamicDto;
	}


	@Override
	public ResponseMessage resetToDefault(String plant, String processName) {
		return dynamicPageDao.resetToDefault(plant,processName);
	}

}
