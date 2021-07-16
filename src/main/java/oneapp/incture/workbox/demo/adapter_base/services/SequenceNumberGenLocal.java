package oneapp.incture.workbox.demo.adapter_base.services;

import org.hibernate.Session;

public interface SequenceNumberGenLocal {

	 public String getNextSeqNumber(String referenceCode, Integer noOfDigits, Session session);
}
