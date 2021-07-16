package oneapp.incture.workbox.demo.workflow.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.BaseDao;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeTemplate;
import oneapp.incture.workbox.demo.adapter_base.util.ExecutionFault;
import oneapp.incture.workbox.demo.adapter_base.util.InvalidInputFault;
import oneapp.incture.workbox.demo.adapter_base.util.NoResultFault;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.workflow.dto.CrossConstantDto;
import oneapp.incture.workbox.demo.workflow.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.workflow.dto.DropDownRequestDto;
import oneapp.incture.workbox.demo.workflow.dto.ValuesDto;
import oneapp.incture.workbox.demo.workflow.services.DropDownResposeDto;
import oneapp.incture.workbox.demo.workflow.util.WorkflowCreationConstant;

@Repository
public class CustomAttrTemplateDao extends BaseDao<CustomAttributeTemplate, CustomAttributeTemplateDto> {

	@Autowired
	TaskTemplateDao taskTemplateDao;

	@Autowired
	private ProcessDetail processDetail;

	@Autowired
	private CrossConstantDao crossConstantDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected CustomAttributeTemplate importDto(CustomAttributeTemplateDto fromDto)
			throws InvalidInputFault, ExecutionFault, NoResultFault {
		CustomAttributeTemplate entity = null;
		if (!ServicesUtil.isEmpty(fromDto) && !ServicesUtil.isEmpty(fromDto.getProcessName())
				&& !ServicesUtil.isEmpty(fromDto.getKey())) {
			entity = new CustomAttributeTemplate();
			entity.setKey(fromDto.getKey());
			entity.setProcessName(fromDto.getProcessName());
			if (!ServicesUtil.isEmpty(fromDto.getDataType()))
				entity.setDataType(fromDto.getDataType());
			if (!ServicesUtil.isEmpty(fromDto.getDescription()))
				entity.setDescription(fromDto.getDescription());
			if (!ServicesUtil.isEmpty(fromDto.getLabel()))
				entity.setLabel(fromDto.getLabel());
			if (!ServicesUtil.isEmpty(fromDto.getIsActive()))
				entity.setIsActive(fromDto.getIsActive());
			if (!ServicesUtil.isEmpty(fromDto.getIsEditable()))
				entity.setIsEditable(fromDto.getIsEditable());
			if (!ServicesUtil.isEmpty(fromDto.getIsMandatory()))
				entity.setIsMandatory(fromDto.getIsMandatory());
			if (!ServicesUtil.isEmpty(fromDto.getOrigin()))
				entity.setOrigin(fromDto.getOrigin());
			if (!ServicesUtil.isEmpty(fromDto.getAttributePath()))
				entity.setAttributePath(fromDto.getAttributePath());
			if (!ServicesUtil.isEmpty(fromDto.getDependantOn()))
				entity.setDependantOn(fromDto.getDependantOn());
			if (!ServicesUtil.isEmpty(fromDto.getIsVisible()))
				entity.setIsVisible(fromDto.getIsVisible());
			if (!ServicesUtil.isEmpty(fromDto.getIsDeleted()))
				entity.setIsDeleted(fromDto.getIsDeleted());
			if (!ServicesUtil.isEmpty(fromDto.getIsRunTime()))
				entity.setIsRunTime(fromDto.getIsRunTime());
			if (!ServicesUtil.isEmpty(fromDto.getRunTimeType()))
				entity.setRunTimeType(fromDto.getRunTimeType());
			if (!ServicesUtil.isEmpty(fromDto.getDefaultValue()))
				entity.setDefaultValue(fromDto.getDefaultValue());
			if (!ServicesUtil.isEmpty(fromDto.getSequence()))
				entity.setSequence(fromDto.getSequence());

		}
		return entity;
	}

	@Override
	protected CustomAttributeTemplateDto exportDto(CustomAttributeTemplate entity) {
		CustomAttributeTemplateDto customAttributeTemplateDto = new CustomAttributeTemplateDto();
		if (!ServicesUtil.isEmpty(entity.getProcessName()))
			customAttributeTemplateDto.setProcessName(entity.getProcessName());
		if (!ServicesUtil.isEmpty(entity.getKey()))
			customAttributeTemplateDto.setKey(entity.getKey());
		if (!ServicesUtil.isEmpty(entity.getDataType()))
			customAttributeTemplateDto.setDataType(entity.getDataType());
		if (!ServicesUtil.isEmpty(entity.getDescription()))
			customAttributeTemplateDto.setDescription(entity.getDescription());
		if (!ServicesUtil.isEmpty(entity.getLabel()))
			customAttributeTemplateDto.setLabel(entity.getLabel());
		if (!ServicesUtil.isEmpty(entity.getIsActive()))
			customAttributeTemplateDto.setIsActive(entity.getIsActive());
		if (!ServicesUtil.isEmpty(entity.getIsEditable()))
			customAttributeTemplateDto.setIsEditable(entity.getIsEditable());
		if (!ServicesUtil.isEmpty(entity.getIsMandatory()))
			customAttributeTemplateDto.setIsMandatory(entity.getIsMandatory());
		if (!ServicesUtil.isEmpty(entity.getOrigin()))
			customAttributeTemplateDto.setOrigin(entity.getOrigin());
		if (!ServicesUtil.isEmpty(entity.getAttributePath()))
			customAttributeTemplateDto.setAttributePath(entity.getAttributePath());
		if (!ServicesUtil.isEmpty(entity.getDependantOn()))
			customAttributeTemplateDto.setDependantOn(entity.getDependantOn());
		if (!ServicesUtil.isEmpty(entity.getIsVisible()))
			customAttributeTemplateDto.setIsVisible(entity.getIsVisible());
		if (!ServicesUtil.isEmpty(entity.getIsDeleted()))
			customAttributeTemplateDto.setIsDeleted(entity.getIsDeleted());
		if (!ServicesUtil.isEmpty(entity.getIsRunTime()))
			customAttributeTemplateDto.setIsRunTime(entity.getIsRunTime());
		if (!ServicesUtil.isEmpty(entity.getRunTimeType()))
			customAttributeTemplateDto.setRunTimeType(entity.getRunTimeType());
		if (!ServicesUtil.isEmpty(entity.getDefaultValue()))
			customAttributeTemplateDto.setDefaultValue(entity.getDefaultValue());
		if (!ServicesUtil.isEmpty(entity.getSequence()))
			customAttributeTemplateDto.setSequence(entity.getSequence());
		return customAttributeTemplateDto;
	}

	public void saveOrUpdateAttributes(List<CustomAttributeTemplateDto> attibutes) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		try {
			if (!ServicesUtil.isEmpty(attibutes) && !attibutes.isEmpty()) {

				for (int i = 0; i < attibutes.size(); i++) {
					CustomAttributeTemplateDto mainAttr = attibutes.get(i);
					session.saveOrUpdate(importDto(mainAttr));
					if (i % 20 == 0 && i > 0) {
						session.flush();
						session.clear();
					}
					if (!ServicesUtil.isEmpty(mainAttr.getTableAttributes())) {
						List<CustomAttributeTemplateDto> tableAttributes = mainAttr.getTableAttributes();
						if (!ServicesUtil.isEmpty(tableAttributes) && !tableAttributes.isEmpty()) {
							saveOrUpdateAttributes(tableAttributes);
						}
					}

				}
				session.flush();
				session.clear();
				tx.commit();
				session.close();
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX-NEW][INSERTING CUSTOM ATTRIBUTES] ERROR:" + e.getMessage());
		}
	}

	public static void main(String args[]) {

		String x = "${fghjajdsl}";

		System.err.println(x.substring(x.indexOf("{") + 1, x.indexOf("}")));

	}

	@SuppressWarnings({ "unchecked" })
	public List<CustomAttributeTemplateDto> getCustomAttributesIfExists(String processName,
			Map<String, List<ValuesDto>> dropDownValues, String processName2) {
		List<CustomAttributeTemplateDto> customAttributeList = null;
		CustomAttributeTemplateDto attributeTemplateDto = null;
		Query query = this.getSession().createQuery(
				"select cat from CustomAttributeTemplate cat where cat.processName= :processName and cat.isDeleted= :isDeleted ");
		query.setParameter("processName", processName);
		query.setParameter("isDeleted", false);
		List<CustomAttributeTemplate> attributeList = query.list();
		System.err.println("Custom attributes : " + new Gson().toJson(attributeList));
		List<String> customKeys = taskTemplateDao.geProcessCustomKeys(processName2);
		System.err.println("customKey " + customKeys);
		if (!attributeList.isEmpty()) {
			customAttributeList = new ArrayList<>();
			for (CustomAttributeTemplate customAttributeTemplate : attributeList) {
				attributeTemplateDto = exportDto(customAttributeTemplate);

				attributeTemplateDto.setIsRunTime(attributeTemplateDto.getIsRunTime());
				if ("DROPDOWN".equalsIgnoreCase(attributeTemplateDto.getDataType())) {
					if (!ServicesUtil.isEmpty(dropDownValues)
							&& dropDownValues.containsKey(attributeTemplateDto.getKey()))
						attributeTemplateDto.setDropDownValues(dropDownValues.get(attributeTemplateDto.getKey()));
				}
				attributeTemplateDto.setIsEdited(0);

				if ("TABLE".equalsIgnoreCase(attributeTemplateDto.getDataType())) {
					attributeTemplateDto.setTableAttributes(
							getCustomAttributesIfExists(attributeTemplateDto.getKey(), null, processName2));
				}

				customAttributeList.add(attributeTemplateDto);
			}
			return customAttributeList;
		} else {
			return new ArrayList<>();
		}
	}

	public void deleteInCAT(List<String> processName) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String deleteInCAT = "DELETE FROM CUSTOM_ATTR_TEMPLATE WHERE PROCESS_NAME IN "
				+ "(SELECT DISTINCT PROCESS_NAME FROM PROCESS_CONFIG_TB WHERE PROCESS_NAME IN (:processName)) "
				+ " OR PROCESS_NAME IN (SELECT TEMPLATE_ID FROM PROCESS_TEMPLATE WHERE PROCESS_NAME = '" + processName
				+ "')";
		Query deleteInCATQuery = session.createSQLQuery(deleteInCAT);
		deleteInCATQuery.setParameterList("processName", processName);
		deleteInCATQuery.executeUpdate();
		tx.commit();
		session.close();

	}

	public String updateAttibutes(List<CustomAttributeTemplateDto> list) {
		String result = WorkflowCreationConstant.FAILURE;
		CustomAttributeTemplateDto customAttributeTemplateDto = null;
		List<CustomAttributeTemplateDto> customAttributeTemplateDtos = null;
		List<CrossConstantDto> constantDtos = new ArrayList<>();
		List<String> valuesToRemove = new ArrayList<>();
		try {
			customAttributeTemplateDtos = new ArrayList<>();
			for (CustomAttributeTemplateDto catList : list) {
				if (WorkflowCreationConstant.ONE.equals(catList.getIsEdited()))// updating
																				// the
																				// existing
																				// custom
																				// attribute
				{
					Session session = sessionFactory.openSession();
					Transaction tx = session.beginTransaction();
					Query updateAttriibutes = session.createSQLQuery("UPDATE CUSTOM_ATTR_TEMPLATE "
							+ "SET IS_EDITABLE= :val5, IS_MAND= :val6,IS_DELETED= :val1, LABEL= :val4"
							+ ",ATTRIBUTE_PATH= :val7,IS_VISIBLE= :val8,RUN_TIME_TYPE= :val9,DEFAULT_VALUE= :val10 , SEQUENCE = :val11 "
							+ " WHERE PROCESS_NAME= :val2 AND KEY= :val3");
					updateAttriibutes.setParameter("val1", catList.getIsDeleted());
					updateAttriibutes.setParameter("val2", catList.getProcessName());
					updateAttriibutes.setParameter("val3", catList.getKey());
					updateAttriibutes.setParameter("val4", catList.getLabel());
					updateAttriibutes.setParameter("val5", catList.getIsEditable());
					updateAttriibutes.setParameter("val6", catList.getIsMandatory());
					updateAttriibutes.setParameter("val7", catList.getAttributePath());
					updateAttriibutes.setParameter("val8", catList.getIsVisible());
					updateAttriibutes.setParameter("val9", catList.getRunTimeType());
					updateAttriibutes.setParameter("val10", catList.getDefaultValue());
					updateAttriibutes.setParameter("val11", catList.getSequence());

					updateAttriibutes.executeUpdate();
					tx.commit();
					session.close();

				} else if (WorkflowCreationConstant.TWO.equals(catList.getIsEdited())) // adding
																						// new
																						// custom
																						// Attribute
				{

					customAttributeTemplateDto = new CustomAttributeTemplateDto();

					customAttributeTemplateDto.setDescription(catList.getDescription());
					customAttributeTemplateDto.setDataType(catList.getDataType());
					customAttributeTemplateDto.setIsActive(catList.getIsActive());
					customAttributeTemplateDto.setIsEditable(catList.getIsEditable());
					customAttributeTemplateDto.setIsVisible(catList.getIsVisible());
					customAttributeTemplateDto.setIsMandatory(catList.getIsMandatory());
					customAttributeTemplateDto.setIsDeleted(catList.getIsDeleted());
					customAttributeTemplateDto.setIsRunTime(catList.getIsRunTime());
					customAttributeTemplateDto.setRunTimeType(catList.getRunTimeType());
					customAttributeTemplateDto.setSequence(catList.getSequence());

					if (WorkflowCreationConstant.ATTACHMENT.equals(catList.getDataType())) {
						String key = catList.getLabel();
						key = key.trim();
						key = key.replace(" ", "_");
						customAttributeTemplateDto.setKey(key);
					} else
						customAttributeTemplateDto.setKey(catList.getKey());

					customAttributeTemplateDto.setLabel(catList.getLabel());
					customAttributeTemplateDto.setProcessName(catList.getProcessName());
					customAttributeTemplateDto.setAttributePath(catList.getAttributePath());
					customAttributeTemplateDto.setOrigin(catList.getOrigin());
					customAttributeTemplateDto.setDependantOn(catList.getDependantOn());
					customAttributeTemplateDtos.add(customAttributeTemplateDto);

				}

				if ("TABLE".equalsIgnoreCase(catList.getDataType())
						&& !ServicesUtil.isEmpty(catList.getTableAttributes())
						&& !catList.getTableAttributes().isEmpty()) {
					updateAttibutes(catList.getTableAttributes());

				}

				if ("DROPDOWN".equals(catList.getDataType()) && !ServicesUtil.isEmpty(catList.getDropDownValues())) {
					DropDownRequestDto downRequestDto = new DropDownRequestDto();
					downRequestDto.setCustomKey(catList.getKey());
					downRequestDto.setValues(catList.getDropDownValues());
					DropDownResposeDto response = processDetail.createCrossConstantDto(downRequestDto);

					if (!ServicesUtil.isEmpty(response.getCrossConstants()))
						constantDtos.addAll(response.getCrossConstants());
					if (!ServicesUtil.isEmpty(response.getValuesToRemove()))
						valuesToRemove.addAll(response.getValuesToRemove());
				}
				if (!valuesToRemove.isEmpty())
					crossConstantDao.removeValues(catList.getKey(), valuesToRemove);
			}
			if (!constantDtos.isEmpty())
				crossConstantDao.saveOrUpdateCrossConstants(constantDtos);
			saveOrUpdateAttributes(customAttributeTemplateDtos);
			result = WorkflowCreationConstant.SUCCESS;

		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][WORKFLOW UPDATE][ATTRIBUTE UPDATION]ERROR:" + e.getMessage());
		}
		return result;
	}

}
