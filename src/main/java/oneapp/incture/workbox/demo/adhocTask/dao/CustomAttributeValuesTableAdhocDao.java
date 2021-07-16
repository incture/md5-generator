package oneapp.incture.workbox.demo.adhocTask.dao;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javassist.expr.NewArray;
import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValueTableAdhocDo;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeValuesTableAdhocDto;

@Repository
public class CustomAttributeValuesTableAdhocDao
		extends BaseDao<CustomAttributeValueTableAdhocDo, CustomAttributeValuesTableAdhocDto> {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	@Override
	protected CustomAttributeValueTableAdhocDo importDto(CustomAttributeValuesTableAdhocDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {

		CustomAttributeValueTableAdhocDo customAttributeValuesdo = new CustomAttributeValueTableAdhocDo();

		if (!ServicesUtil.isEmpty(fromDto.getAttributeValue()))
			customAttributeValuesdo.setAttributeValue(fromDto.getAttributeValue());
		if (!ServicesUtil.isEmpty(fromDto.getKey()))
			customAttributeValuesdo.setKey(fromDto.getKey());
		if (!ServicesUtil.isEmpty(fromDto.getProcessName()))
			customAttributeValuesdo.setProcessName(fromDto.getProcessName());
		if (!ServicesUtil.isEmpty(fromDto.getTaskId()))
			customAttributeValuesdo.setTaskId(fromDto.getTaskId());
		if (!ServicesUtil.isEmpty(fromDto.getRowNumber()))
			customAttributeValuesdo.setRowNumber(fromDto.getRowNumber());
		if (!ServicesUtil.isEmpty(fromDto.getDependantOn()))
			customAttributeValuesdo.setDependantOn(fromDto.getDependantOn());

		return customAttributeValuesdo;
	}

	@Override
	protected CustomAttributeValuesTableAdhocDto exportDto(CustomAttributeValueTableAdhocDo entity) {

		CustomAttributeValuesTableAdhocDto customAttributeValuesdto = new CustomAttributeValuesTableAdhocDto();
		if (!ServicesUtil.isEmpty(entity.getAttributeValue()))
			customAttributeValuesdto.setAttributeValue(entity.getAttributeValue());
		if (!ServicesUtil.isEmpty(entity.getKey()))
			customAttributeValuesdto.setKey(entity.getKey());
		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			customAttributeValuesdto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getTaskId()))
			customAttributeValuesdto.setTaskId(entity.getTaskId());
		if (!ServicesUtil.isEmpty(entity.getRowNumber()))
			customAttributeValuesdto.setRowNumber(entity.getRowNumber());
		if (!ServicesUtil.isEmpty(entity.getDependantOn()))
			customAttributeValuesdto.setDependantOn(entity.getDependantOn());
		;
		return customAttributeValuesdto;
	}

	public void saveOrUpdateTableValues(List<CustomAttributeValuesTableAdhocDto> customAttributeValuesTableAdhocDtos) {

		try {
			if (!ServicesUtil.isEmpty(customAttributeValuesTableAdhocDtos)) {
				Session session = sessionFactory.openSession();
				Transaction transaction = session.beginTransaction();
				for (int i = 0; i < customAttributeValuesTableAdhocDtos.size(); i++) {
					CustomAttributeValuesTableAdhocDto customAttributeValuesTableAdhocDto = customAttributeValuesTableAdhocDtos
							.get(i);
					session.saveOrUpdate(importDto(customAttributeValuesTableAdhocDto));
					if (i % 20 == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				transaction.commit();
				session.close();
			}

		} catch (Exception e) {
			System.err.println("Error saving table attributes");
			e.printStackTrace();
		}

	}

	public void saveOrUpdateTableValuesDo(List<CustomAttributeValueTableAdhocDo> customAttributeValuesTableAdhocDos) {
		try {
			if (!ServicesUtil.isEmpty(customAttributeValuesTableAdhocDos)) {
				Session session = sessionFactory.openSession();
				Transaction transaction = session.beginTransaction();
				for (int i = 0; i < customAttributeValuesTableAdhocDos.size(); i++) {
					CustomAttributeValueTableAdhocDo customAttributeValuesTableAdhocDo = customAttributeValuesTableAdhocDos
							.get(i);
					session.saveOrUpdate(customAttributeValuesTableAdhocDo);
					if (i % 20 == 0 && i > 0) {
						session.flush();
						session.clear();
					}
				}
				session.flush();
				session.clear();
				transaction.commit();
				session.close();
			}

		} catch (Exception e) {
			System.err.println("Error saving table attributes");
		}

	}
	
	public Map<Integer, List<CustomAttributeValue>> fetchTableAttributes(String taskId , String processName) {
		
		List<CustomAttributeValueTableAdhocDo> adhocDos = new ArrayList<>();
		Map<Integer, List<CustomAttributeValue>> tableValueMap = new LinkedHashMap<>();
		CustomAttributeValue customAttributeValue = null;
		List<CustomAttributeValue> list = null;
		try {
			Session session = sessionFactory.openSession();
			String query  = "select row_number  , label , attr_value from custom_attr_values_table cavt join custom_attr_template cat on cavt.key = cat.key " + 
					" where task_id = '" +  taskId +"' and cavt.process_name = '" + processName + "' order by row_number , sequence ";
			List<Object[]> result = session.createSQLQuery(query).list();
			for (Object[] object : result) {
				if(tableValueMap.containsKey((Integer)object[0])) {
					customAttributeValue = new CustomAttributeValue();
					customAttributeValue.setAttributeValue(ServicesUtil.asString(object[2]));
					customAttributeValue.setKey(ServicesUtil.asString(object[1]));
					tableValueMap.get((Integer)object[0]).add(customAttributeValue);
				}
				else {
					list = new ArrayList<>();
					customAttributeValue = new CustomAttributeValue();
					customAttributeValue.setAttributeValue(ServicesUtil.asString(object[2]));
					customAttributeValue.setKey(ServicesUtil.asString(object[1]));
					list.add(customAttributeValue);
					tableValueMap.put((Integer)object[0], list);
				}
			}
//			System.err.println(NewArray);
		} catch (Exception e) {
			System.err.println("Error fetching table attributes");
			e.printStackTrace();
		}
		return tableValueMap;
	}

}
