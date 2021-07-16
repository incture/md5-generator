package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessTemplate;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class ProcessTemplateDao extends BaseDao<ProcessTemplate, ProcessTemplateDto> {

	@Override
	protected ProcessTemplate importDto(ProcessTemplateDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		ProcessTemplate entity = new ProcessTemplate();
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getProcessName())
				&& !ServicesUtil.isEmpty(fromDto.getTemplateId())) {
			entity.setProcessName(fromDto.getProcessName());
			entity.setTemplateId(fromDto.getTemplateId());
			if (!ServicesUtil.isEmpty(fromDto.getTaskName()))
				entity.setTaskName(fromDto.getTaskName());
			if (!ServicesUtil.isEmpty(fromDto.getOwnerId()))
				entity.setOwnerId(fromDto.getOwnerId());
			if (!ServicesUtil.isEmpty(fromDto.getTaskType()))
				entity.setTaskType(fromDto.getTaskType());
			if (!ServicesUtil.isEmpty(fromDto.getRunTimeUser()))
				entity.setRunTimeUser(fromDto.getRunTimeUser());
			if (!ServicesUtil.isEmpty(fromDto.getCustomKey()))
				entity.setCustomKey(fromDto.getCustomKey());
			if (!ServicesUtil.isEmpty(fromDto.getSubject()))
				entity.setSubject(fromDto.getSubject());
			if (!ServicesUtil.isEmpty(fromDto.getDescription()))
				entity.setDescription(fromDto.getDescription());
			if (!ServicesUtil.isEmpty(fromDto.getSourceId()))
				entity.setSourceId(fromDto.getSourceId());
			if (!ServicesUtil.isEmpty(fromDto.getTargetId()))
				entity.setTargetId(fromDto.getTargetId());
			if (!ServicesUtil.isEmpty(fromDto.getTaskNature()))
				entity.setTaskNature(fromDto.getTaskNature());
		}
		return entity;
	}

	@Override
	protected ProcessTemplateDto exportDto(ProcessTemplate entity) {
		ProcessTemplateDto processTemplateDto = new ProcessTemplateDto();
		processTemplateDto.setProcessName(entity.getProcessName());
		processTemplateDto.setTemplateId(entity.getTemplateId());
		if (!ServicesUtil.isEmpty(entity.getTaskName()))
			processTemplateDto.setTaskName(entity.getTaskName());
		if (!ServicesUtil.isEmpty(entity.getOwnerId()))
			processTemplateDto.setOwnerId(entity.getOwnerId());
		if (!ServicesUtil.isEmpty(entity.getTaskType()))
			processTemplateDto.setTaskType(entity.getTaskType());
		if (!ServicesUtil.isEmpty(entity.getRunTimeUser()))
			processTemplateDto.setRunTimeUser(entity.getRunTimeUser());
		if (!ServicesUtil.isEmpty(entity.getCustomKey()))
			processTemplateDto.setCustomKey(entity.getCustomKey());
		if (!ServicesUtil.isEmpty(entity.getSubject()))
			processTemplateDto.setSubject(entity.getSubject());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			processTemplateDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getSourceId()))
			processTemplateDto.setSourceId(entity.getSourceId());
		if (!ServicesUtil.isEmpty(entity.getTargetId()))
			processTemplateDto.setTargetId(entity.getTargetId());
		if (!ServicesUtil.isEmpty(entity.getTaskNature()))
			processTemplateDto.setTaskNature(entity.getTaskNature());
		return processTemplateDto;
	}

	public void saveOrUpdateProcessTemplate(List<ProcessTemplateDto> processTemplateDtos) {
		Session session = null;
		try {
			if (!ServicesUtil.isEmpty(processTemplateDtos) && processTemplateDtos.size() > 0) {
				session = this.getSession();
				for (int i = 0; i < processTemplateDtos.size(); i++) {
					ProcessTemplateDto currentTask = processTemplateDtos.get(i);
					session.saveOrUpdate(importDto(currentTask));
					if (i % 20 == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Saving of Process Template ERROR" + e.getMessage());
		}
	}

}
