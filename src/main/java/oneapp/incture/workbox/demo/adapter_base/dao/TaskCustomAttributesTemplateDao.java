package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.TaskCustomAttributesTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskTemplateTableDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TemplateTableDto;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskCustomAttributesTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.TemplateTableDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class TaskCustomAttributesTemplateDao extends BaseDao<TaskCustomAttributesTemplate, TaskCustomAttributesTemplateDto> {

	@Override
	protected TaskCustomAttributesTemplate importDto(TaskCustomAttributesTemplateDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		// TODO Auto-generated method stub
		TaskCustomAttributesTemplate template=new TaskCustomAttributesTemplate();
		if(!ServicesUtil.isEmpty(fromDto.getKey()))
			template.setKey(fromDto.getKey());
		if(!ServicesUtil.isEmpty(fromDto.getLabel()))
			template.setLabel(fromDto.getLabel());
		if(!ServicesUtil.isEmpty(fromDto.getOrigin()))
			template.setOrigin(fromDto.getOrigin());
		if(!ServicesUtil.isEmpty(fromDto.getProcessName()))
			template.setProcessName(fromDto.getProcessName());
		if(!ServicesUtil.isEmpty(fromDto.getTaskName()))
			template.setTaskName(fromDto.getTaskName());
		
		return template;
	}

	@Override
	protected TaskCustomAttributesTemplateDto exportDto(TaskCustomAttributesTemplate entity) {
		// TODO Auto-generated method stub
		TaskCustomAttributesTemplateDto template=new TaskCustomAttributesTemplateDto();
				if(!ServicesUtil.isEmpty(entity.getKey()))
					template.setKey(entity.getKey());
				if(!ServicesUtil.isEmpty(entity.getLabel()))
					template.setLabel(entity.getLabel());
				if(!ServicesUtil.isEmpty(entity.getOrigin()))
					template.setOrigin(entity.getOrigin());
				if(!ServicesUtil.isEmpty(entity.getProcessName()))
					template.setProcessName(entity.getProcessName());
				if(!ServicesUtil.isEmpty(entity.getTaskName()))
					template.setTaskName(entity.getTaskName());
				
				return template;
	}
	
	public void create(List<TaskCustomAttributesTemplateDto> dtos) throws ExecutionFault, InvalidInputFault, NoResultFault {
		for(TaskCustomAttributesTemplateDto dto:dtos){
		super.create(dto);
		}
	}
	
	public List<TaskCustomAttributesTemplate> getProcessTaskDetails(String processName){
		
		List<TaskCustomAttributesTemplate> list=new ArrayList<TaskCustomAttributesTemplate>();
		
		String query="Select te from TaskCustomAttributesTemplate te where te.processName='"+processName+"'";
	List result=this.getSession().createQuery(query).list();
	
	for(Object obj:result){
		list.add((TaskCustomAttributesTemplate)obj);
	}
	
	return list;
	}
}
