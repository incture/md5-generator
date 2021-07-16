package oneapp.incture.workbox.demo.emailTemplate.Dao;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.entity.EmailTableContentDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.emailTemplate.Dto.EmailTableContentDto;

@Repository
public class EmailTableContentDao extends BaseDao<EmailTableContentDo, EmailTableContentDto>{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	protected EmailTableContentDo importDto(EmailTableContentDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		EmailTableContentDo entity = new EmailTableContentDo();
		entity.setTableContentId(fromDto.getTableContentId());
		if (!ServicesUtil.isEmpty(fromDto.getVersion()))
			entity.setVersion(fromDto.getVersion());
		
		if (!ServicesUtil.isEmpty(fromDto.getColumnName()))
			entity.setColumnName(fromDto.getColumnName());
		if (!ServicesUtil.isEmpty(fromDto.getColumnValueField()))
			entity.setColumnValueField(fromDto.getColumnValueField());
		if (!ServicesUtil.isEmpty(fromDto.getSequenceNumber()))
			entity.setSequenceNumber(fromDto.getSequenceNumber());
		return entity;
	}

	@Override
	protected EmailTableContentDto exportDto(EmailTableContentDo entity) {
		EmailTableContentDto emailTableContentDto = new EmailTableContentDto();
		if (!ServicesUtil.isEmpty(entity.getTableContentId()))
			emailTableContentDto.setTableContentId(entity.getTableContentId());
		
		if (!ServicesUtil.isEmpty(entity.getVersion()))
			emailTableContentDto.setVersion(entity.getVersion());
		if (!ServicesUtil.isEmpty(entity.getColumnName()))
			emailTableContentDto.setColumnName(entity.getColumnName());
		if (!ServicesUtil.isEmpty(entity.getColumnValueField()))
			emailTableContentDto.setColumnValueField(entity.getColumnValueField());
		if (!ServicesUtil.isEmpty(entity.getSequenceNumber()))
			emailTableContentDto.setSequenceNumber(entity.getSequenceNumber());
		return emailTableContentDto;
	}

	public void saveOrUpdateEmailTableContent(List<EmailTableContentDto> tableContentDtos) {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			if (!ServicesUtil.isEmpty(tableContentDtos) && tableContentDtos.size() > 0) {
				session = this.getSession();
				for (int i = 0; i < tableContentDtos.size(); i++) {
					EmailTableContentDto currentTask = tableContentDtos.get(i);
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
			System.err.println("[WB-DEV]ERROR saving email table content:"+e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<EmailTableContentDto> getTableContentIfExists(String tableContentId) {
		List<EmailTableContentDto> emailTableContentDtos = null;
		EmailTableContentDto emailTableContentDto = null;
		Session session=sessionFactory.openSession();
		Query qry = session.createSQLQuery("SELECT TABLE_CONTENT_ID,COLUMN_NAME,COLUMN_VALUE_FIELD,SEQUENCE_NUMBER"
				+ " FROM EMAIL_TABLE_CONTENT WHERE TABLE_CONTENT_ID = '"+tableContentId+"' "
				+ "ORDER BY SEQUENCE_NUMBER");
		List<Object[]> result = qry.list();
		if(!ServicesUtil.isEmpty(result)){
			emailTableContentDtos = new ArrayList<EmailTableContentDto>();
			for (Object[] obj : result) {
				emailTableContentDto = new EmailTableContentDto();
				emailTableContentDto.setColumnName(obj[1].toString());
				emailTableContentDto.setColumnValueField(obj[2].toString());
				emailTableContentDto.setSequenceNumber((Integer)obj[3]);
				emailTableContentDto.setTableContentId(obj[0].toString());
				emailTableContentDto.setIsDeleted(false);
				emailTableContentDtos.add(emailTableContentDto);
			}
			
		}
		return emailTableContentDtos;
	}

	public void deleteTableContent(String templateId) {
		Session session=null;
		try{
			session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query deleteQry = session.createSQLQuery("DELETE FROM EMAIL_TABLE_CONTENT WHERE "
					+ "TABLE_CONTENT_ID IN (SELECT TABLE_CONTENT_ID FROM EMAIL_CONTENT WHERE TEMPLATE_ID = '"+templateId+"')");
			//Transaction tx = session.beginTransaction();
			deleteQry.executeUpdate();
			tx.commit();
			session.close();
			
		}catch (Exception e) {
			System.err.println("[WBP-Dev][Email Template]delete email table content ERROR:"+e);
		}
	}


}
