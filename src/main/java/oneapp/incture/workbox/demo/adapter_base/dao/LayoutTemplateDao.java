package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.LayoutTemplateDto;
import oneapp.incture.workbox.demo.adapter_base.entity.LayoutAttributesTemplate;
import oneapp.incture.workbox.demo.adapter_base.entity.LayoutTemplateDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("LayoutTemplateDao")
public class LayoutTemplateDao extends BaseDao<LayoutTemplateDo, LayoutTemplateDto> {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected LayoutTemplateDo importDto(LayoutTemplateDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		LayoutTemplateDo workloadRangeDo = new LayoutTemplateDo();

		if (!ServicesUtil.isEmpty(fromDto.getLayoutId()))
			workloadRangeDo.setLayoutId(fromDto.getLayoutId());
		if (!ServicesUtil.isEmpty(fromDto.getSequence()))
			workloadRangeDo.setSequence(fromDto.getSequence());
		if (!ServicesUtil.isEmpty(fromDto.getLabel()))
			workloadRangeDo.setLabel(fromDto.getLabel());
		if (!ServicesUtil.isEmpty(fromDto.getLayoutName()))
			workloadRangeDo.setLayoutName(fromDto.getLayoutName());
		if (!ServicesUtil.isEmpty(fromDto.getLevel()))
			workloadRangeDo.setLevel(fromDto.getLevel());
		if (!ServicesUtil.isEmpty(fromDto.getParentLayoutName()))
			workloadRangeDo.setParentLayoutName(fromDto.getParentLayoutName());
		if(!ServicesUtil.isEmpty(fromDto.getLayoutType()))
			workloadRangeDo.setLayoutType(fromDto.getLayoutType());
		if(!ServicesUtil.isEmpty(fromDto.getLayoutSize()))
			workloadRangeDo.setLayoutSize(fromDto.getLayoutSize());
		if(!ServicesUtil.isEmpty(fromDto.getIsDeleted()))
			workloadRangeDo.setIsDeleted(fromDto.getIsDeleted());
			

		return workloadRangeDo;

	}

	@Override
	protected LayoutTemplateDto exportDto(LayoutTemplateDo entity) {

		LayoutTemplateDto workloadRangeDto = new LayoutTemplateDto();

		if (!ServicesUtil.isEmpty(entity.getLayoutId()))
			workloadRangeDto.setLayoutId(entity.getLayoutId());
		if (!ServicesUtil.isEmpty(entity.getSequence()))
			workloadRangeDto.setSequence(entity.getSequence());
		if (!ServicesUtil.isEmpty(entity.getLabel()))
			workloadRangeDto.setLabel(entity.getLabel());
		if (!ServicesUtil.isEmpty(entity.getLayoutName()))
			workloadRangeDto.setLayoutName(entity.getLayoutName());
		if (!ServicesUtil.isEmpty(entity.getLevel()))
			workloadRangeDto.setLevel(entity.getLevel());
		if (!ServicesUtil.isEmpty(entity.getParentLayoutName()))
			workloadRangeDto.setParentLayoutName(entity.getParentLayoutName());
		if(!ServicesUtil.isEmpty(entity.getLayoutType()))
			workloadRangeDto.setLayoutType(entity.getLayoutType());
		if(!ServicesUtil.isEmpty(entity.getLayoutSize()))
			workloadRangeDto.setLayoutSize(entity.getLayoutSize());
		if(!ServicesUtil.isEmpty(entity.getIsDeleted()))
			workloadRangeDto.setIsDeleted(entity.getIsDeleted());
		return workloadRangeDto;
	}

	@Override
	public void create(LayoutTemplateDto dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		super.create(dto);
	}

	@Override
	public void update(LayoutTemplateDto dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		super.update(dto);
	}

	@Override
	public void delete(LayoutTemplateDto dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		super.delete(dto);
	}
	
	public void updateLayoutTemplate(List<LayoutTemplateDto> layoutTemplateDtos) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(layoutTemplateDtos) && !layoutTemplateDtos.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < layoutTemplateDtos.size(); i++) {
					LayoutTemplateDto currentTask = layoutTemplateDtos.get(i);
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][UPDATING Layout Template] ERROR:"+e.getMessage());
		}
	}
	
	public void saveOrUpdateLayoutTemplate(List<LayoutTemplateDto> layoutTemplateDtos) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(layoutTemplateDtos) && !layoutTemplateDtos.isEmpty()) {
				//session = this.getSession();
				session=sessionFactory.openSession();
				Transaction tx=session.beginTransaction();
				
				for (int i = 0; i < layoutTemplateDtos.size(); i++) {
					LayoutTemplateDto currentTask = layoutTemplateDtos.get(i);
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][UPDATING Layout Template] ERROR:"+e.getMessage());
		}
	}
	
	public void createLayoutTemplate(List<LayoutTemplateDto> layoutTemplateDtos) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(layoutTemplateDtos) && !layoutTemplateDtos.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < layoutTemplateDtos.size(); i++) {
					LayoutTemplateDto currentTask = layoutTemplateDtos.get(i);
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING Layout Template] ERROR:"+e.getMessage());
		}
	}
	
	public void createLayoutTemplateDos(List<LayoutTemplateDo> layoutTemplateDo) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(layoutTemplateDo) && !layoutTemplateDo.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < layoutTemplateDo.size(); i++) {
					session.save(layoutTemplateDo.get(i));
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][UPDATING Layout Template] ERROR:"+e.getMessage());
		}
	}
	
	//method to create layout attributes
	public void createLayoutAttrDos(List<LayoutAttributesTemplate> layoutTemplateDo) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(layoutTemplateDo) && !layoutTemplateDo.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				 Transaction tx = session.beginTransaction();
				for (int i = 0; i < layoutTemplateDo.size(); i++) {
					session.save(layoutTemplateDo.get(i));
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][create Layout Attrs Template] ERROR:"+e.getMessage());
		}
	}

	public String getNextLayoutId() {
		
		String layoutId = 
		 (String) this.getSession().createSQLQuery("SELECT TOP 1 LAYOUT_ID FROM LAYOUT_TEMPLATE_TABLE "
																+ " ORDER BY LAYOUT_ID DESC").uniqueResult();
		 
		 return layoutId;
	}
}
