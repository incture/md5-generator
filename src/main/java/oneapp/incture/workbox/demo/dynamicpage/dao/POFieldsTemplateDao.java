package oneapp.incture.workbox.demo.dynamicpage.dao;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dynamicpage.dto.POFieldsTemplateResponseDto;

@Repository
public class POFieldsTemplateDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@SuppressWarnings("unchecked")
	public List<POFieldsTemplateResponseDto> getFieldValues(String fieldName){
		List<POFieldsTemplateResponseDto> list = new ArrayList<POFieldsTemplateResponseDto>();
		String query = "";
		try {
			query = "SELECT VALUE FROM PO_FIELD_VALUE_MAPPING WHERE KEY = '"+fieldName+"' ";
			List<Object> objectList = this.getSession().createSQLQuery(query).list();
			System.err.println("POFieldsTemplateDao.getFieldValues() objectList : "+objectList);
			
			if(!ServicesUtil.isEmpty(objectList)) {
				for(Object obj : objectList) {
					POFieldsTemplateResponseDto dto = new POFieldsTemplateResponseDto();
					dto.setValue(obj!=null ? obj.toString() : null);
					list.add(dto);
				}
			}     
			
		}catch(Exception e) {
			System.err.println("POFieldsTemplateDao.getFieldValues() exception : "+e.getMessage());
		}
		return list;
	}
}
