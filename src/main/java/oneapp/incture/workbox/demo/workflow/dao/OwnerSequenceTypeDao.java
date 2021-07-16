package oneapp.incture.workbox.demo.workflow.dao;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.workflow.dto.OwnerSequenceTypeDto;
import oneapp.incture.workbox.demo.workflow.entity.OwnerSequenceTypeDo;

@Repository
public class OwnerSequenceTypeDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	public void saveOrUpdateOwnerSequence(List<OwnerSequenceTypeDo> ownerSequenceTypeDos) {
		Session session = null;
		try {
			if (!ServicesUtil.isEmpty(ownerSequenceTypeDos) && !ownerSequenceTypeDos.isEmpty()) {
				session = this.getSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < ownerSequenceTypeDos.size(); i++) {
					OwnerSequenceTypeDo currentSeq = ownerSequenceTypeDos.get(i);
					session.saveOrUpdate(currentSeq);
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
		} catch (Exception e) {
			System.err.println("[WBP-Dev] Saving of owner sequence error" + e.getMessage());
		}
	}

	public OwnerSequenceTypeDto getOwnerSequence(String processName, String templateId) {
		Session session = null;
		OwnerSequenceTypeDto ownerSequenceTypeDto = null;
		try {
			if (!ServicesUtil.isEmpty(processName) && !ServicesUtil.isEmpty(templateId)) {
				session = this.getSession();
				String ownerQuery = "SELECT OWNER_SEQU_TYPE , ATTR_TYPE_ID , ATTR_TYPE_NAME , ORDER_BY " +
				 " FROM TASK_OWNER_TEMPLATE_SEQUENCE  WHERE PROCESS_NAME = :processName and TEMPLATE_ID = :templateId ";
				Object[] result  =  (Object[]) session.createSQLQuery(ownerQuery)
						.setParameter("processName", processName).setParameter("templateId", templateId).uniqueResult();
				
				if(!ServicesUtil.isEmpty(result)) {
					ownerSequenceTypeDto = new OwnerSequenceTypeDto();
					ownerSequenceTypeDto.setOwnerSequType(ServicesUtil.isEmpty(result[0]) ? "" : result[0].toString());
					ownerSequenceTypeDto.setAttrTypeId(ServicesUtil.isEmpty(result[1]) ? "" : result[1].toString());
					ownerSequenceTypeDto.setAttrTypeName(ServicesUtil.isEmpty(result[2]) ? "" : result[2].toString());
					ownerSequenceTypeDto.setOrderBy(ServicesUtil.isEmpty(result[3]) ? "" : result[3].toString());
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][OwnerSequenceTypeDao][getOwnerSequence]  Error fetching owner sequence :"
					+ e.getMessage());
		}
		return ownerSequenceTypeDto;
	}

}
