package oneapp.incture.workbox.demo.adapter_base.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dto.TemplateTableDto;
import oneapp.incture.workbox.demo.adapter_base.entity.TemplateTableDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("TemplateTableDao")
public class TemplateTableDao extends BaseDao<TemplateTableDo, TemplateTableDto> {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected TemplateTableDo importDto(TemplateTableDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		TemplateTableDo workloadRangeDo = new TemplateTableDo();

		if (!ServicesUtil.isEmpty(fromDto.getLayoutId()))
			workloadRangeDo.setLayoutId(fromDto.getLayoutId());
		if (!ServicesUtil.isEmpty(fromDto.getSequence()))
			workloadRangeDo.setSequence(fromDto.getSequence());
		if (!ServicesUtil.isEmpty(fromDto.getTemplateId()))
			workloadRangeDo.setTemplateId(fromDto.getTemplateId());
		return workloadRangeDo;

	}

	@Override
	protected TemplateTableDto exportDto(TemplateTableDo entity) {

		TemplateTableDto workloadRangeDto = new TemplateTableDto();

		if (!ServicesUtil.isEmpty(entity.getLayoutId()))
			workloadRangeDto.setLayoutId(entity.getLayoutId());
		if (!ServicesUtil.isEmpty(entity.getSequence()))
			workloadRangeDto.setSequence(entity.getSequence());
		if (!ServicesUtil.isEmpty(entity.getTemplateId()))
			workloadRangeDto.setTemplateId(entity.getTemplateId());
		return workloadRangeDto;
	}

	@Override
	public void create(TemplateTableDto dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		super.create(dto);
	}

	@Override
	public void delete(TemplateTableDto dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		super.delete(dto);
	}

	@Override
	public void update(TemplateTableDto dto) throws ExecutionFault, InvalidInputFault, NoResultFault {
		super.update(dto);
	}
	
	public void updateTemplateTable(List<TemplateTableDto> templateTable) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(templateTable) && !templateTable.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < templateTable.size(); i++) {
					TemplateTableDto currentTask = templateTable.get(i);
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][UPDATING Template Table] ERROR:"+e.getMessage());
		}
	}
	
	public void saveOrUpdateTemplateTable(List<TemplateTableDto> templateTable) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(templateTable) && !templateTable.isEmpty()) {
				//session = this.getSession();
				session=sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < templateTable.size(); i++) {
					TemplateTableDto currentTask = templateTable.get(i);
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][UPDATING Template Table] ERROR:"+e.getMessage());
		}
	}
	
	public void createTemplateTable(List<TemplateTableDto> templateTable) {
		Session session = null;
		try{
			if (!ServicesUtil.isEmpty(templateTable) && !templateTable.isEmpty()) {
				//session = this.getSession();
				 session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < templateTable.size(); i++) {
					TemplateTableDto currentTask = templateTable.get(i);
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
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING Template Table] ERROR:"+e.getMessage());
		}
	}
}
