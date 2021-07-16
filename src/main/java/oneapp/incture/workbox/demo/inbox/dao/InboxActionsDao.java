package oneapp.incture.workbox.demo.inbox.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.inbox.entity.InboxActions;



@Repository
//////@Transactional
public class InboxActionsDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void saveOrUpdateAction(InboxActions inboxActions) {
		try {
			Session session = sessionFactory.openSession();
			Transaction tx  = session.beginTransaction();
			session.saveOrUpdate(inboxActions);
			tx.commit();
			session.close();
			}
			catch(Exception e) {
				System.out.println("oneapp.incture.workbox.demo.adapter_base.dao function:saveOrUpdateAction");
				e.printStackTrace();
			}
			
	}
}
