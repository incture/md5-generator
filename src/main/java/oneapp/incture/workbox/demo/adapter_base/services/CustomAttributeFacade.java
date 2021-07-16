package oneapp.incture.workbox.demo.adapter_base.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dto.AttributeKeyDto;
import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeTemplateResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.KeyValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.KeyValueResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Service("CustomAttributeFacade")
public class CustomAttributeFacade implements CustomAttributeFacadeLocal {

	@Autowired
	CustomAttributeDao customAttributeDao;

	@Override
	public CustomAttributeTemplateResponse getCustomAttributeTemplates(String processName, String key,
			Boolean isActive) {
		CustomAttributeTemplateResponse customAttributeTemplateResponse = null;
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(PMCConstant.FAILURE);
		responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
		if (!ServicesUtil.isEmpty(processName)) {
			customAttributeTemplateResponse = new CustomAttributeTemplateResponse();
			try {
				List<CustomAttributeTemplate> attributeTemplates = customAttributeDao
						.getCustomAttributeTemplates(processName, key, isActive);

				List<AttributeKeyDto> attributes = new ArrayList<>();
				AttributeKeyDto attributeKeyDto = null;
				for (CustomAttributeTemplate temp : attributeTemplates) {

					attributeKeyDto = new AttributeKeyDto();
					attributeKeyDto.setAttributeType(temp.getDataType());
					attributeKeyDto.setKey(temp.getKey());
					attributeKeyDto.setProcessName(temp.getProcessName());

					attributes.add(attributeKeyDto);
				}

				customAttributeTemplateResponse.setCustomAttributeTemplates(attributeTemplates);
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
				if (!ServicesUtil.isEmpty(attributeTemplates) && attributeTemplates.size() > 0)
					responseMessage.setMessage("Fetching Custom attribute templates successful");
				else
					responseMessage.setMessage(PMCConstant.NO_RESULT);
			} catch (Exception ex) {
				responseMessage.setMessage("Exception while fetching custom attribute templates");
				System.err.println("[WBP-Dev]Exception while fetching custom attribute templates : " + ex);
			}
			customAttributeTemplateResponse.setResponseMessage(responseMessage);
		}
		return customAttributeTemplateResponse;
	}

	@Override
	public ResponseMessage saveOrUpdateCustomAttributes(List<CustomAttributeTemplate> customAttributeTemplates) {
		return customAttributeDao.saveOrUpdateCustomAttributes(customAttributeTemplates);
	}

	@Override
	public KeyValueResponseDto getAutoCompleteValues(String processName, String key, String attributeValue) {

		KeyValueResponseDto responseDto = new KeyValueResponseDto();

		try {
			List<String> attributeValues = customAttributeDao.getCustomAttributesForAutoComplete(processName, key,
					attributeValue);

			if (!ServicesUtil.isEmpty(attributeValues)) {
				List<KeyValueDto> dtos = new ArrayList<>();

				for (String val : attributeValues) {
					KeyValueDto dto = new KeyValueDto();
					dto.setKey(val);
					dto.setValue(val);
					dtos.add(dto);
				}
				responseDto.setKeyValuePairs(dtos);
				responseDto
						.setMessage(new ResponseMessage(PMCConstant.SUCCESS, PMCConstant.CODE_SUCCESS, "Values Found"));
			} else {
				responseDto.setMessage(
						new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE, "No value found"));
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev]Exception while getAutoCompleteValues " + e);
			responseDto.setMessage(new ResponseMessage(PMCConstant.FAILURE, PMCConstant.CODE_FAILURE, "Error"));

		}

		return responseDto;
	}

	@Override
	public CustomAttributeTemplateResponse getAllAttributes() {
		List<CustomAttributeTemplate> list = new ArrayList<CustomAttributeTemplate>();
		ResponseMessage responseMessage = new ResponseMessage();
		CustomAttributeTemplateResponse response = new CustomAttributeTemplateResponse();
		try {
			list = customAttributeDao.getAllAttributesHeader();
			if (!ServicesUtil.isEmpty(list)) {
				response.setCustomAttributeTemplates(list);
				responseMessage.setMessage("Custom Attributes Fetched Successfully");
				responseMessage.setStatus(PMCConstant.SUCCESS);
				responseMessage.setStatusCode(PMCConstant.CODE_SUCCESS);
				response.setResponseMessage(responseMessage);
			} else {
				responseMessage.setMessage("Error Occurred while fetching the custom attributes");
				responseMessage.setStatus(PMCConstant.FAILURE);
				responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Exception while getAllAttributes " + e);
			responseMessage.setMessage("INTERNAL SERVER ERROR");
			responseMessage.setStatus(PMCConstant.FAILURE);
			responseMessage.setStatusCode(PMCConstant.CODE_FAILURE);
		}
		return response;
	}

}
