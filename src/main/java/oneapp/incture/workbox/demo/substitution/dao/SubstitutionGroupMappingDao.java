package oneapp.incture.workbox.demo.substitution.dao;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.substitution.dto.SubstitutionGroupMappingDto;
import oneapp.incture.workbox.demo.substitution.entity.SubstitutionGroupMappingDo;


@Repository
//@Transactional
public class SubstitutionGroupMappingDao extends BaseDao<SubstitutionGroupMappingDo, SubstitutionGroupMappingDto> {
	
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		}catch(Exception e) {
			return sessionFactory.openSession();
		}
	}

	protected SubstitutionGroupMappingDo importDto(SubstitutionGroupMappingDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		SubstitutionGroupMappingDo entity = new SubstitutionGroupMappingDo();
		if (!ServicesUtil.isEmpty(fromDto.getProcess()))
			entity.setProcess(fromDto.getProcess());
		if (!ServicesUtil.isEmpty(fromDto.getUserGroupId()))
			entity.setUserGroupId(fromDto.getUserGroupId());
		if(!ServicesUtil.isEmpty(fromDto.getGroupName()))
			entity.setGroupName(fromDto.getGroupName());

		return entity;
	}

	protected SubstitutionGroupMappingDto exportDto(SubstitutionGroupMappingDo entity) {

		SubstitutionGroupMappingDto dto = new SubstitutionGroupMappingDto();
		if (!ServicesUtil.isEmpty(entity.getProcess()))
			dto.setProcess(entity.getProcess());
		if (!ServicesUtil.isEmpty(entity.getUserGroupId()))
			dto.setUserGroupId(entity.getUserGroupId());
		if(!ServicesUtil.isEmpty(entity.getGroupName()))
			dto.setGroupName(entity.getGroupName());
		return dto;
	}
	
	
	public void saveOrUpdateSubstitutionGroupMapping(SubstitutionGroupMappingDto entity) throws Exception {
		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(importDto(entity));
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][ERROR][SubstitutionGroupMappingDao] " + e.getMessage());
		}

	}

}
