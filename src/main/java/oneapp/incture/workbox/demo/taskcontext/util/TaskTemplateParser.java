package oneapp.incture.workbox.demo.taskcontext.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oneapp.incture.workbox.demo.adapter_base.dao.LayoutTemplateDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskTemplateDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ConfigurationResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.LayoutAttributesTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.LayoutTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskTemplateTableDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TemplateTableDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Component
public class TaskTemplateParser {
	
	@Autowired
	TaskTemplateDao taskTemplateDao;
	
	@Autowired
	LayoutTemplateDao layoutTemplateDao;

	public ConfigurationResponseDto createResposneToSave(TaskTemplateTableDto taskTemplateTableDto) {
		ConfigurationResponseDto responseDto = new ConfigurationResponseDto();
		List<TaskTemplateTableDto> taskTemplateTableDtos = new ArrayList<>();
		TaskTemplateTableDto taskTemplateTable = null;
		String templateId = "";
		try{
			if(!ServicesUtil.isEmpty(taskTemplateTableDto)){
				taskTemplateTable = new TaskTemplateTableDto();
				if(ServicesUtil.isEmpty(taskTemplateTableDto.getTemplateId()) || "".equals(taskTemplateTableDto.getTemplateId()))
				{
					templateId = taskTemplateDao.getNextTemplateId();
					if(ServicesUtil.isEmpty(templateId))
						templateId="TEMP0001";
					else
					   templateId = "TEMP"+String.format("%04d", (Integer.valueOf(templateId.substring(4))+1));
					
					responseDto.setTemplateId(templateId);
					taskTemplateTableDto.setTemplateId(templateId);
					taskTemplateTable.setTemplateId(templateId);
				}else
					taskTemplateTable.setTemplateId(taskTemplateTableDto.getTemplateId());
				taskTemplateTable.setTaskName(taskTemplateTableDto.getTaskName());
				taskTemplateTable.setProcessName(taskTemplateTableDto.getProcessName());
				taskTemplateTable.setParentTaskName(taskTemplateTableDto.getParentTaskName());
				taskTemplateTable.setOrigin(taskTemplateTableDto.getOrigin());

				if(!ServicesUtil.isEmpty(taskTemplateTableDto.getLayoutsData())){
					responseDto = fetchLayoutsData(taskTemplateTableDto.getLayoutsData(),
							taskTemplateTableDto.getTemplateId(),null);
				}
				taskTemplateTableDtos.add(taskTemplateTable);
			}
		}catch (Exception e) {
			System.err.println("error Parsing"+e);
		}
		return new ConfigurationResponseDto(taskTemplateTableDtos, responseDto.getTemplateTableDtos(),
				responseDto.getLayoutTemplateDtos(), responseDto.getLayoutAttributesTemplateDtos(),templateId,null);
	}

	private ConfigurationResponseDto fetchLayoutsData(List<LayoutTemplateDto> layoutsData,String templateId, String parentLayoutId) {
		List<TemplateTableDto> templateTableDtos = new ArrayList<>();
		TemplateTableDto templateTableDto = null;
		List<LayoutTemplateDto> layoutTemplateDtos = new ArrayList<>();
		LayoutTemplateDto layoutTemplate = null;
		List<LayoutAttributesTemplateDto> layoutAttributesTemplateDtos = new ArrayList<>();
		LayoutAttributesTemplateDto layoutAttributesTemplateDto = null;
		String layoutId = "";
		Integer attrSequence = 1;
		if(ServicesUtil.isEmpty(parentLayoutId)) 
			layoutId = layoutTemplateDao.getNextLayoutId();
		
		else
			layoutId = parentLayoutId;
		
		for (LayoutTemplateDto layoutTemplateDto : layoutsData) {
			templateTableDto = new TemplateTableDto();
			if(ServicesUtil.isEmpty(layoutTemplateDto.getLayoutId()) || "".equals(layoutTemplateDto.getLayoutId()))
			{
				if(ServicesUtil.isEmpty(layoutId))
					layoutId="LYT0001";
				else
				   layoutId = "LYT"+String.format("%04d", (Integer.valueOf(layoutId.substring(3))+1));
				templateTableDto.setLayoutId(layoutId);
			}else
				templateTableDto.setLayoutId(layoutTemplateDto.getLayoutId());
			if(("".equals(layoutTemplateDto.getParentLayoutName()) || ServicesUtil.isEmpty(layoutTemplateDto.getParentLayoutName())
					) && ServicesUtil.isEmpty(parentLayoutId))
			{
				templateTableDto.setSequence("00"+layoutTemplateDto.getSequence());
				templateTableDto.setTemplateId(templateId);
				templateTableDtos.add(templateTableDto);
			}
			layoutTemplate = new LayoutTemplateDto();
			if(ServicesUtil.isEmpty(layoutTemplateDto.getLayoutId()) || "".equals(layoutTemplateDto.getLayoutId()))
			{
				layoutTemplate.setLayoutId(layoutId);
			}else{
				layoutTemplate.setLayoutId(layoutTemplateDto.getLayoutId());	
			}
			layoutTemplate.setLabel(layoutTemplateDto.getLabel());
			layoutTemplate.setLayoutName(layoutTemplateDto.getLayoutName());
			layoutTemplate.setLayoutType(layoutTemplateDto.getLayoutType());
			layoutTemplate.setLevel(layoutTemplateDto.getLevel());
			layoutTemplate.setIsDeleted(layoutTemplateDto.getIsDeleted());
			if(ServicesUtil.isEmpty(layoutTemplateDto.getParentLayoutName()) || "".equals(layoutTemplateDto.getParentLayoutName()))
				layoutTemplate.setParentLayoutName(parentLayoutId);
			else
				layoutTemplate.setParentLayoutName(layoutTemplateDto.getParentLayoutName());
			layoutTemplate.setSequence(layoutTemplateDto.getSequence());
			layoutTemplate.setLayoutSize(layoutTemplateDto.getLayoutSize());
			layoutTemplateDtos.add(layoutTemplate);
			attrSequence = 1;
			for (LayoutAttributesTemplateDto attributeDto : layoutTemplateDto.getLayoutAttributesData()) {
				layoutAttributesTemplateDto = new LayoutAttributesTemplateDto();
				if(ServicesUtil.isEmpty(layoutTemplateDto.getLayoutId()) || "".equals(layoutTemplateDto.getLayoutId()))
					layoutAttributesTemplateDto.setLayoutId(layoutId);
				else
					layoutAttributesTemplateDto.setLayoutId(attributeDto.getLayoutId());
				
				layoutAttributesTemplateDto.setActionURL(attributeDto.getActionURL());
				layoutAttributesTemplateDto.setHasAction(ServicesUtil.isEmpty(attributeDto.getHasAction()?0:attributeDto.getHasAction()));
				layoutAttributesTemplateDto.setIndex(attributeDto.getIndex());
				layoutAttributesTemplateDto.setIsEditable(attributeDto.getIsEditable());
				layoutAttributesTemplateDto.setIsMandatory(attributeDto.getIsMandatory());
				layoutAttributesTemplateDto.setIsVisible(attributeDto.getIsVisible());
				layoutAttributesTemplateDto.setKey(attributeDto.getKey());
				layoutAttributesTemplateDto.setKeyLabel(attributeDto.getKeyLabel());
				layoutAttributesTemplateDto.setKeyType(attributeDto.getKeyType());
				layoutAttributesTemplateDto.setSequence(attrSequence.toString());
				layoutAttributesTemplateDto.setValueHelpId(attributeDto.getValueHelpId());
				layoutAttributesTemplateDto.setSourceIndex(attributeDto.getSourceIndex());
				layoutAttributesTemplateDto.setSourceKey(attributeDto.getSourceKey());
				layoutAttributesTemplateDto.setIsRunTime(attributeDto.getIsRunTime());
				layoutAttributesTemplateDto.setRunTimeType(attributeDto.getRunTimeType());
				layoutAttributesTemplateDtos.add(layoutAttributesTemplateDto);
				attrSequence++;
			}
			if(!ServicesUtil.isEmpty(layoutTemplateDto.getSubLayoutsData())){
				
				ConfigurationResponseDto dto = fetchLayoutsData(layoutTemplateDto.getSubLayoutsData(), null, layoutId);
				layoutAttributesTemplateDtos.addAll(dto.getLayoutAttributesTemplateDtos());
				layoutTemplateDtos.addAll(dto.getLayoutTemplateDtos());
				layoutId = dto.getLayoutId();
			}
		}
		return new ConfigurationResponseDto(null, templateTableDtos, layoutTemplateDtos, layoutAttributesTemplateDtos,null,layoutId);
	}

}
