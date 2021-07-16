package oneapp.incture.workbox.demo.adapter_base.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.entity.InboxActions;





@Repository("baseInboxActionsDao")
//////@Transactional
public class InboxActionsDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void saveOrUpdateAction(InboxActions inboxActions) {
		getSession().saveOrUpdate(inboxActions);
	}
}
