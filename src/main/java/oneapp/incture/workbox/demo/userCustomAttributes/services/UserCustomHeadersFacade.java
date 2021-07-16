package oneapp.incture.workbox.demo.userCustomAttributes.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dto.AttributeKeyDto;
import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeTemplateResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.userCustomAttributes.dao.UserCustomHeadersDao;
import oneapp.incture.workbox.demo.userCustomAttributes.dto.UserCustomHeadersDto;

@Service("UserCustomHeadersFacade")
//////@Transactional
public class UserCustomHeadersFacade implements UserCustomHeadersFacadeLocal{
	
	@Autowired
	private UserCustomHeadersDao userCustomHeadersDao;
	
	@Autowired
	CustomAttributeDao customAttributeDao;

	@Override
	public List<CustomAttributeTemplate> getUserDefinedCustomAttributes(String userId, String processName) {
		List<CustomAttributeTemplate> attributeTemplates = null;
		attributeTemplates = userCustomHeadersDao.getUserDefinedCustomAttributes(userId, processName);
		attributeTemplates = attributeTemplates.stream().filter(a -> !a.getDataType().equalsIgnoreCase("BUTTON")).collect(Collectors.toList());
		
		System.err.println("UserCustomHeadersFacade.getUserDefinedCustomAttributes() attribute templates : "+attributeTemplates);
		return attributeTemplates;
	}

	@Override
	public ResponseMessage saveOrUpdateUserCustomization(List<UserCustomHeadersDto> userCustomHeadersDto, String userId) {
		List<CustomAttributeTemplate> list = new ArrayList<CustomAttributeTemplate>();
		ResponseMessage response = new ResponseMessage();
		if(ServicesUtil.isEmpty(userCustomHeadersDto)) {
			response.setMessage("Input cannot be null");
			response.setStatus(PMCConstant.FAILURE);
			response.setStatusCode(PMCConstant.CODE_FAILURE);
		}else {
			if(!ServicesUtil.isEmpty(userId)) {
				response = userCustomHeadersDao.saveOrUpdateUserCustomization(userCustomHeadersDto);
			}
			else {
				for(UserCustomHeadersDto dto : userCustomHeadersDto) {
					CustomAttributeTemplate entity = new CustomAttributeTemplate();
					entity.setKey(dto.getKey());
					entity.setProcessName(dto.getProcessName());
					entity.setDataType(dto.getDataType() != null ? dto.getDataType() : null);
					entity.setLabel(dto.getLabel() != null ? dto.getLabel() : null);
					entity.setIsActive(dto.getIsActive() != null ? (dto.getIsActive().equals(true) ? true : false) : null);
					entity.setIsMandatory(dto.getIsMandatory() != null ? (dto.getIsMandatory().equals(true) ? true : false) : null);
					entity.setIsEditable(dto.getIsEditable() != null ? (dto.getIsEditable().equals(true) ? true : false) : null);
					// setting 4 more variable IS_VISIBLE,ORIGIN,Description and IS_DELETED bcz while updating there are set to null
					entity.setIsVisible(dto.getIsVisible() != null ? (dto.getIsVisible().equals(true) ? true : false) : null);
					entity.setIsDeleted(dto.getIsDeleted() != null ? (dto.getIsDeleted().equals(true) ? true : false) : null);
					entity.setOrigin(dto.getOrigin() != null ? dto.getOrigin() : null);
					entity.setDescription(dto.getDescription() != null ? dto.getDescription() : null);
					list.add(entity);
				}
				response = customAttributeDao.saveOrUpdateCustomAttributes(list);
			}
		}
		return response;
	}

	@Override
	public CustomAttributeTemplateResponse getCustomAttributeTemplates(String processName, String key,
			Boolean isActive, String userId) {
		System.err.println("Custom Attribute test processname "+processName);
		System.err.println("Custom Attribute test key "+key);
		System.err.println("Custom Attribute test isActive "+isActive);
		System.err.println("Custom Attribute test userId "+userId);
		CustomAttributeTemplateResponse customAttributeTemplateResponse = null;
		List<AttributeKeyDto> attributes = new ArrayList<>();
		List<CustomAttributeTemplate> attributeTemplates = null;
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(PMCConstant.FAILURE);
		responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
		if (!ServicesUtil.isEmpty(processName)) {
			customAttributeTemplateResponse = new CustomAttributeTemplateResponse();
			try {
				
				if(!ServicesUtil.isEmpty(userId)) {
					attributeTemplates = userCustomHeadersDao.getUserDefinedCustomAttributes(userId, processName);
					System.err.println("Custom Attribute test if userDefinedCustomAttribute "+attributeTemplates);
				}
				else {
				 attributeTemplates = customAttributeDao
						.getCustomAttributeTemplates(processName, key, isActive);
				
				 	/*AttributeKeyDto attributeKeyDto = null;
					for (CustomAttributeTemplate temp : attributeTemplates) {
	
						attributeKeyDto = new AttributeKeyDto();
						attributeKeyDto.setAttributeType(temp.getDataType());
						attributeKeyDto.setKey(temp.getKey());
						attributeKeyDto.setProcessName(temp.getProcessName());
	
						attributes.add(attributeKeyDto);
					}*/
				 System.err.println("Custom Attribute test else userDefinedCustomAttribute "+attributeTemplates);
				}
				attributeTemplates = attributeTemplates.stream().filter(a -> !a.getDataType().equalsIgnoreCase("BUTTON")).collect(Collectors.toList());
				
				System.err.println("UserCustomHeadersFacade.getCustomAttributeTemplates() attribute templates : "+attributeTemplates);
				
				customAttributeTemplateResponse.setCustomAttributeTemplates(attributeTemplates);
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
				if (!ServicesUtil.isEmpty(attributeTemplates) && attributeTemplates.size() > 0)
					responseMessage.setMessage("Fetching Custom attribute templates successful");
				else
					responseMessage.setMessage(PMCConstant.NO_RESULT);
			} catch (Exception ex) {
				responseMessage.setMessage("Exception while fetching custom attribute templates");
				System.err.println("[WBP-Dev]Exception while fetching custom attribute templates : " + ex.getMessage());
			}
			customAttributeTemplateResponse.setResponseMessage(responseMessage);
		}
		return customAttributeTemplateResponse;
	}

}
