package oneapp.incture.workbox.demo.adapter_base.services;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.entity.SeqNumberDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Service("SequenceNumberGen")
//////@Transactional
public class SequenceNumberGen implements SequenceNumberGenLocal{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public String getNextSeqNumber(String referenceCode, Integer noOfDigits, Session session) {

		 try {
			 session = sessionFactory.getCurrentSession();
		 }catch(Exception e) {
			 session = sessionFactory.openSession();
		 }

        SeqNumberDo sequenceNumbersDo = null;
        
        /*
         * Criteria criteria = session.createCriteria(SeqNumberDo.class);
         * criteria.add(Restrictions.eq("referenceCode", referenceCode));
         * 
         * sequenceNumbersDo = (SeqNumberDo) criteria.uniqueResult();
         */
        sequenceNumbersDo = getSeqNumberDoByReferenceCode(referenceCode, session);

 

        int runningNumber=1;
        if (!ServicesUtil.isEmpty(sequenceNumbersDo)) 
            runningNumber = sequenceNumbersDo.getRunningNumber()+1;
        else
            sequenceNumbersDo =  new SeqNumberDo(referenceCode, runningNumber);
        
        sequenceNumbersDo.setRunningNumber(runningNumber);        
        session.saveOrUpdate(sequenceNumbersDo);
        //session.flush();
        
        return buildSeqNumber(referenceCode, noOfDigits, runningNumber);
    }

 

    private String buildSeqNumber(String referenceCode, Integer noOfDigits, int runningNumber) {
        StringBuilder sb = new StringBuilder(noOfDigits);
        sb.append(runningNumber);
        int noOfPads = noOfDigits - sb.length();
        // TODO: decide what to do if noOfPads is negative
        while (noOfPads-- > 0) {
            sb.insert(0, '0');
        }
        sb.insert(0, referenceCode);
        return sb.toString();
    }


    private SeqNumberDo getSeqNumberDoByReferenceCode(String referenceCode, Session session){
        String hql = "SELECT s FROM SeqNumberDo s WHERE s.referenceCode = :referenceCode";
        Query query = session.createQuery(hql);
        query.setParameter("referenceCode", referenceCode);
        return(SeqNumberDo) query.uniqueResult();

 

    }

}
