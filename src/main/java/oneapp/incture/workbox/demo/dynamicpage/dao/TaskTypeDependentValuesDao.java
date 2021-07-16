package oneapp.incture.workbox.demo.dynamicpage.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.dynamicpage.dto.POFieldsTemplateResponseDto;

@Repository
public class TaskTypeDependentValuesDao {
	
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public List<POFieldsTemplateResponseDto> getFieldValues(String fieldName, String dependencyKey, String dependencyValue, String taskType){
		List<POFieldsTemplateResponseDto> list = new ArrayList<POFieldsTemplateResponseDto>();
		String query = "";
		try {
			query = "SELECT VALUE FROM TASK_TYPE_DEPENDENT_VALUES WHERE KEY = '"+fieldName+"' "
					+ " AND DEPENDENCY_KEY = '"+dependencyKey+"' AND DEPENDENCY_VALUE = '"+dependencyValue+"' "
					+ " AND TASK_TYPE = '"+taskType+"' ";
			System.err.println("TaskTypeDependentValuesDao.getFieldValues() query : "+query);
			List<Object> resultList = sessionFactory.getCurrentSession().createSQLQuery(query).list();
			
			if(!ServicesUtil.isEmpty(resultList)) {
				for(Object obj : resultList) {
					POFieldsTemplateResponseDto dto = new POFieldsTemplateResponseDto();
					dto.setValue(obj != null ? obj.toString() : null);
					list.add(dto);
				}
			}
		}catch(Exception e) {
			System.err.println("TaskTypeDependentValuesDao.getFieldValues() exception : "+e.getMessage());
		}
		return list;
	}
}
