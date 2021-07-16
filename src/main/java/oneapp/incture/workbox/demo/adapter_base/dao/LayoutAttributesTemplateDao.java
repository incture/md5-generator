package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.LayoutAttributesTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.entity.LayoutAttributesTemplate;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository
public class LayoutAttributesTemplateDao extends BaseDao<LayoutAttributesTemplate, LayoutAttributesTemplateDto>{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected LayoutAttributesTemplate importDto(LayoutAttributesTemplateDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		// TODO Auto-generated method stub
		LayoutAttributesTemplate template=new LayoutAttributesTemplate();
		if(!ServicesUtil.isEmpty(fromDto.getActionURL()))
			template.setActionURL(fromDto.getActionURL());
		if(!ServicesUtil.isEmpty(fromDto.getKey()))
			template.setKey(fromDto.getKey());
		if(!ServicesUtil.isEmpty(fromDto.getKeyLabel()))
			template.setKeyLabel(fromDto.getKeyLabel());
		if(!ServicesUtil.isEmpty(fromDto.getKeyType()))
			template.setKeyType(fromDto.getKeyType());
		if(!ServicesUtil.isEmpty(fromDto.getLayoutId()))
			template.setLayoutId(fromDto.getLayoutId());
		if(!ServicesUtil.isEmpty(fromDto.getSequence()))
			template.setSequence(fromDto.getSequence());
		if(!ServicesUtil.isEmpty(fromDto.getValueHelpId()))
			template.setValueHelpId(fromDto.getValueHelpId());
		if(!ServicesUtil.isEmpty(fromDto.getHasAction()))
			template.setHasAction(fromDto.getHasAction());
		if(!ServicesUtil.isEmpty(fromDto.getIsEditable()))
			template.setIsEditable(fromDto.getIsEditable());
		if(!ServicesUtil.isEmpty(fromDto.getIsMandatory()))
			template.setIsMandatory(fromDto.getIsMandatory());
		if(!ServicesUtil.isEmpty(fromDto.getIsVisible()))
			template.setIsVisible(fromDto.getIsVisible());
		if(!ServicesUtil.isEmpty(fromDto.getSourceKey()))
			template.setSourceKey(fromDto.getSourceKey());
		if(fromDto.getSourceIndex() != null)
			template.setSourceIndex(fromDto.getSourceIndex());
		if(!ServicesUtil.isEmpty(fromDto.getIsRunTime()))
			template.setIsRuntime(fromDto.getIsRunTime());
		if(!ServicesUtil.isEmpty(fromDto.getRunTimeType()))
			template.setRunTimeType(fromDto.getRunTimeType());
			
		return template;
	}

	@Override
	protected LayoutAttributesTemplateDto exportDto(LayoutAttributesTemplate entity) {
		
		LayoutAttributesTemplateDto template=new LayoutAttributesTemplateDto();
		if(!ServicesUtil.isEmpty(entity.getActionURL()))
			template.setActionURL(entity.getActionURL());
		if(!ServicesUtil.isEmpty(entity.getKey()))
			template.setKey(entity.getKey());
		if(!ServicesUtil.isEmpty(entity.getKeyLabel()))
			template.setKeyLabel(entity.getKeyLabel());
		if(!ServicesUtil.isEmpty(entity.getKeyType()))
			template.setKeyType(entity.getKeyType());
		if(!ServicesUtil.isEmpty(entity.getLayoutId()))
			template.setLayoutId(entity.getLayoutId());
		if(!ServicesUtil.isEmpty(entity.getSequence()))
			template.setSequence(entity.getSequence());
		if(!ServicesUtil.isEmpty(entity.getValueHelpId()))
			template.setValueHelpId(entity.getValueHelpId());
		if(!ServicesUtil.isEmpty(entity.getHasAction()))
			template.setHasAction(entity.getHasAction());
		if(!ServicesUtil.isEmpty(entity.getIsEditable()))
			template.setIsEditable(entity.getIsEditable());
		if(!ServicesUtil.isEmpty(entity.getIsMandatory()))
			template.setIsMandatory(entity.getIsMandatory());
		if(!ServicesUtil.isEmpty(entity.getIsVisible()))
			template.setIsVisible(entity.getIsVisible());
		if(!ServicesUtil.isEmpty(entity.getSourceKey()))
			template.setSourceKey(entity.getSourceKey());
		if(!ServicesUtil.isEmpty(entity.getSourceIndex()))
			template.setSourceIndex(entity.getSourceIndex());
		if(!ServicesUtil.isEmpty(entity.getIsRuntime()))
			template.setIsRunTime(entity.getIsRuntime());
		if(!ServicesUtil.isEmpty(entity.getRunTimeType()))
			template.setRunTimeType(entity.getRunTimeType());
		
		return template;
	}
	
	public void updateLayoutAttributeTemplate(List<LayoutAttributesTemplateDto> layoutAttributesTemplateDtos) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(layoutAttributesTemplateDtos) && !layoutAttributesTemplateDtos.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < layoutAttributesTemplateDtos.size(); i++) {
					LayoutAttributesTemplateDto currentTask = layoutAttributesTemplateDtos.get(i);
					session.update(importDto(currentTask));
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
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][Updating Layout Attribute Template] ERROR:"+e.getMessage());
		}
	}
	
	public void createLayoutAttributeTemplate(List<LayoutAttributesTemplateDto> layoutAttributesTemplateDtos) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(layoutAttributesTemplateDtos) && !layoutAttributesTemplateDtos.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < layoutAttributesTemplateDtos.size(); i++) {
					LayoutAttributesTemplateDto currentTask = layoutAttributesTemplateDtos.get(i);
					session.save(importDto(currentTask));
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
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING Layout Attribute Template] ERROR:"+e.getMessage());
		}
	}
	
	public void saveOrUpdateLayoutAttributeTemplate(List<LayoutAttributesTemplateDto> layoutAttributesTemplateDtos) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(layoutAttributesTemplateDtos) && !layoutAttributesTemplateDtos.isEmpty()) {
				//session = this.getSession();
				session=sessionFactory.openSession();
				Transaction tx=session.beginTransaction();
				for (int i = 0; i < layoutAttributesTemplateDtos.size(); i++) {
					LayoutAttributesTemplateDto currentTask = layoutAttributesTemplateDtos.get(i);
					session.saveOrUpdate(importDto(currentTask));
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
		}catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][Updating Layout Attribute Template] ERROR:"+e.getMessage());
			e.printStackTrace();
		}
	}

}
