package oneapp.incture.workbox.demo.adhocTask.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import javassist.expr.NewArray;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TableContentDto;
import oneapp.incture.workbox.demo.adhocTask.dto.ValueListDto;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.adhocTask.util.UrlsForDropdown;
import oneapp.incture.workbox.demo.document.dto.AttachmentDetail;

@Repository
public class CustomAttributeTemplateDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private TaskTemplateOwnerDao taskTemplateOwnerDao;

	@Autowired
	oneapp.incture.workbox.demo.workflow.dao.TaskTemplateDao taskTemplateDao;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	@SuppressWarnings("unchecked")

	public List<Object[]> fetchAttributes(String processName, String origin) {
		try {
			String attributeDetail = "select cat.PROCESS_NAME,cat.KEY,cat.LABEL,cat.IS_ACTIVE,cat.IS_MAND,cat.IS_EDITABLE "
					+ ",cat.DATA_TYPE,cat.ATTR_DES,cat.origin,cat.attribute_path,cat.dependant_on,cat.IS_RUN_TIME,cat.RUN_TIME_TYPE,cat.DEFAULT_VALUE"
					+ " from CUSTOM_ATTR_TEMPLATE as cat " + "where (cat.PROCESS_NAME= '" + processName
					+ "' AND ORIGIN = '" + origin + "' AND ATTRIBUTE_PATH is null) "
					+ "and (cat.IS_DELETED= 0) and (cat.IS_VISIBLE=1) ORDER BY CASE WHEN SEQUENCE IS NOT NULL THEN SEQUENCE END , CASE WHEN SEQUENCE IS NULL THEN LABEL END";
			Query attrDetailQuery = getSession().createSQLQuery(attributeDetail);

			return attrDetailQuery.list();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WOKRBOX][ATTRIBUTE FETCHING] ERROR: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<CustomAttributeTemplateDto> getDraftDetails(String eventId) {

		List<CustomAttributeTemplateDto> customAttrList = null;
		CustomAttributeTemplateDto customAttrDto = null;
		Gson g = new Gson();

		try {

			String fetchAttribute = "select cat.KEY,(select cav.attr_value from custom_attr_values cav  "
					+ "where ( task_id = " + "(select process_id from task_events where event_id = '" + eventId + "'))"
					+ " and cav.key = cat.key) as value, "
					+ "cat.LABEL,cat.IS_MAND,cat.DATA_TYPE,cat.PROCESS_NAME,cat.ATTR_DES,cat.is_editable,cat.is_active"
					+ ",cat.IS_RUN_TIME,cat.RUN_TIME_TYPE " + "from CUSTOM_ATTR_TEMPLATE  as cat where (process_name = "
					+ "(select proc_name from task_events where event_id = '" + eventId + "') "
					+ ") and is_deleted = 0 and attribute_path is null ";
			Query fetchQuery = getSession().createSQLQuery(fetchAttribute);
			List<Object[]> attributeDetailList = fetchQuery.list();

			Query description = getSession().createSQLQuery("SELECT SUBJECT FROM PROCESS_EVENTS WHERE PROCESS_ID = ("
					+ "select PROCESS_ID from task_events where event_id = '" + eventId + "')");
			String desc = (String) description.uniqueResult();
			customAttrList = new ArrayList<>();
			customAttrDto = new CustomAttributeTemplateDto();
			List<String> customKeys = new ArrayList<>();
			if (!ServicesUtil.isEmpty(attributeDetailList))
				customKeys = taskTemplateDao.geProcessCustomKeys(attributeDetailList.get(0)[5].toString());
			customAttrDto.setDataType("TEXT AREA");
			customAttrDto.setIsMandatory(true);
			customAttrDto.setKey("description");
			customAttrDto.setLabel("Description");
			customAttrDto.setProcessName("STANDARD");
			customAttrDto.setTaskId(eventId);
			customAttrDto.setAttrDes("");
			customAttrDto.setIsActive(true);
			customAttrDto.setIsDeleted(true);
			customAttrDto.setIsEditable(null);
			customAttrDto.setValue(desc);
			customAttrDto.setIsEdited(1);
			customAttrList.add(customAttrDto);
			for (Object[] obj : attributeDetailList) {

				customAttrDto = new CustomAttributeTemplateDto();

				customAttrDto.setDataType(obj[4].toString());
				customAttrDto.setIsMandatory(ServicesUtil.asBoolean(obj[3]));
				customAttrDto.setKey(obj[0].toString());
				customAttrDto.setLabel(obj[2].toString());
				customAttrDto.setProcessName(obj[5].toString());
				customAttrDto.setTaskId(eventId);
				if (!ServicesUtil.isEmpty(obj[6]))
					customAttrDto.setAttrDes(obj[6].toString());
				customAttrDto.setIsActive(ServicesUtil.asBoolean(obj[8]));
				customAttrDto.setIsDeleted(true);
				customAttrDto.setIsEditable(ServicesUtil.asBoolean(obj[7]));
				customAttrDto.setOrigin(TaskCreationConstant.PROCESS);
				if (!ServicesUtil.isEmpty(obj[9]))
					customAttrDto.setIsRunTime(ServicesUtil.asBoolean(obj[9]));
				else
					customAttrDto.setIsRunTime(false);

				customAttrDto.setDataTypeKey(obj[4].equals(TaskCreationConstant.DROPDOWN) && customAttrDto.getIsRunTime() ? 1 : 0);
				if (customAttrDto.getDataType().equals(TaskCreationConstant.ATTACHMENT)) {
					if (ServicesUtil.isEmpty(obj[1]) || "".equals(obj[1].toString())) {
						customAttrDto.setValue("");
						customAttrDto.setIsEdited(0);
					} else {
						customAttrDto.setValue(obj[1].toString());
						ObjectMapper mapper = new ObjectMapper();
						List<AttachmentDetail> attachmentIds = mapper.readValue(obj[1].toString(), List.class);
						AttachmentDetail attachment = mapper.readValue(g.toJson(attachmentIds.get(0)),
								AttachmentDetail.class);
						customAttrDto.setAttachmentName(attachment.getAttachmentId());
						customAttrDto.setAttachmentSize(attachment.getAttachmentSize());
						customAttrDto.setAttachmentType(attachment.getAttachmentType());
						customAttrDto.setAttachmentId(attachment.getAttachmentId());
						customAttrDto.setIsEdited(1);
						customAttrDto.setIsVisible(true);
					}
				} else if (customAttrDto.getDataType().equals(TaskCreationConstant.DROPDOWN)) {
					customAttrDto.setUrl(UrlsForDropdown.getDropdownUrl((String) obj[2], (String) obj[0],
							customAttrDto.getIsRunTime(),
							ServicesUtil.isEmpty(obj[10]) ? PMCConstant.INDIVIDUAL : obj[10].toString()));
					if (customKeys.contains(obj[0])) {
						customAttrDto.setDropDownType(
								ServicesUtil.isEmpty(obj[10]) ? PMCConstant.INDIVIDUAL : obj[10].toString());
						if (ServicesUtil.isEmpty(obj[1])) {
							customAttrDto.setValue("");
							customAttrDto.setValueList(new ArrayList<ValueListDto>());
							customAttrDto.setIsEdited(0);
						} else {
							List<ValueListDto> valueListDtos = new ArrayList<ValueListDto>();
							for (String value : obj[1].toString().split(",")) {
								ValueListDto valueListDto = new ValueListDto();
								valueListDto.setId(value);
								valueListDto.setType(customAttrDto.getDropDownType());
								valueListDtos.add(valueListDto);
							}
							customAttrDto.setValueList(valueListDtos);
							customAttrDto.setValue(obj[1].toString());
							customAttrDto.setIsEdited(1);
						}
					} else {
						if (ServicesUtil.isEmpty(obj[1])) {
							customAttrDto.setValue("");
							customAttrDto.setIsEdited(0);
						} else {
							customAttrDto.setValue(obj[1].toString());
							customAttrDto.setIsEdited(1);
						}
					}
				} else if (customAttrDto.getDataType().equals(TaskCreationConstant.TABLE)) {
					customAttrDto.setTableContents(fetchTableData(eventId , customAttrDto.getKey()));
				} else {
					if ("".equals(obj[1]) || obj[1] == null) {
						customAttrDto.setValue("");
						customAttrDto.setIsEdited(0);
					} else {
						customAttrDto.setValue(obj[1].toString());
						customAttrDto.setIsEdited(1);
					}
				}
				customAttrList.add(customAttrDto);
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev][DRAFT VIEW]" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return customAttrList;
	}

	// fething all the table values stored in custom_attr_values_table
	public List<TableContentDto> fetchTableData(String taskId , String processName) {

		Map<Integer, TableContentDto> tableDataMap = new LinkedHashMap<>();
		List<TableContentDto> tableContentDtos = new ArrayList<>();
		List<CustomAttributeTemplateDto> customAttributeTemplateDtos = null;
		CustomAttributeTemplateDto tableCustomAttr = null;
		List<ValueListDto> valueList = null;
		try {
			String query = "select CAT.PROCESS_NAME,CAT.KEY,CAT.LABEL,CAT.IS_ACTIVE,CAT.IS_MAND,CAT.IS_EDITABLE,"
					+ " CAT.DATA_TYPE,CAT.ATTR_DES,CAT.ORIGIN,CAT.ATTRIBUTE_PATH,CAT.DEPENDANT_ON , CAVT.ATTR_VALUE , CAVT.ROW_NUMBER , CAT.IS_RUN_TIME "
					+ " FROM CUSTOM_ATTR_TEMPLATE CAT JOIN CUSTOM_ATTR_VALUES_TABLE CAVT ON CAT.KEY = CAVT.KEY AND CAT.PROCESS_NAME = CAVT.PROCESS_NAME "
					+ " WHERE CAVT.TASK_ID = (select process_id from task_events where event_id = '" + taskId + "')" 
					+ " AND CAVT.PROCESS_NAME = '"+ processName + "' AND CAT.IS_DELETED = 0 AND CAT.IS_VISIBLE = 1";
			List<Object[]> result = this.getSession().createSQLQuery(query).list();
			if (!ServicesUtil.isEmpty(result)) {
				for (Object[] obj : result) {

					tableCustomAttr = new CustomAttributeTemplateDto();
					tableCustomAttr.setProcessName(obj[0].toString());
					tableCustomAttr.setKey(obj[1].toString());
					tableCustomAttr.setLabel(obj[2].toString());
					if (!ServicesUtil.isEmpty(obj[3]))
						tableCustomAttr.setIsActive(ServicesUtil.asBoolean(obj[3]));
					if (!ServicesUtil.isEmpty(obj[4]))
						tableCustomAttr.setIsMandatory(ServicesUtil.asBoolean(obj[4]));
					if (!ServicesUtil.isEmpty(obj[5]))
						tableCustomAttr.setIsEditable(ServicesUtil.asBoolean(obj[5]));
					tableCustomAttr.setDataType(obj[6].toString());
					if (!ServicesUtil.isEmpty(obj[7]))
						tableCustomAttr.setAttrDes(obj[7].toString());
					valueList = new ArrayList<ValueListDto>();
					tableCustomAttr.setValueList(valueList);
					

					tableCustomAttr.setOrigin(ServicesUtil.isEmpty(obj[8]) ? "" : obj[8].toString());
					tableCustomAttr.setAttributePath(ServicesUtil.isEmpty(obj[9]) ? null : obj[9].toString());
					tableCustomAttr.setDependantOn(ServicesUtil.isEmpty(obj[10]) ? null : obj[10].toString());

					tableCustomAttr.setValue(ServicesUtil.isEmpty(obj[11]) ? "" : obj[11].toString());
					
					if (!ServicesUtil.isEmpty(obj[13]))
						tableCustomAttr.setIsRunTime(ServicesUtil.asBoolean(obj[13]));
					else
						tableCustomAttr.setIsRunTime(false);

					tableCustomAttr.setDataTypeKey(obj[6].equals(TaskCreationConstant.DROPDOWN) && tableCustomAttr.getIsRunTime() ? 1 : 0);
					Integer rowNumber = (Integer) obj[12];
					tableCustomAttr.setRowNumber(rowNumber);
					
					if (tableDataMap.containsKey(rowNumber)) {
						tableDataMap.get(rowNumber).getTableAttributes().add(tableCustomAttr);
					} else {
						customAttributeTemplateDtos = new ArrayList<>();
						customAttributeTemplateDtos.add(tableCustomAttr);
						TableContentDto tableContentDto = new TableContentDto();
						tableContentDto.setTableAttributes(customAttributeTemplateDtos);
						tableDataMap.put(rowNumber, tableContentDto);
					}
				}
				for (TableContentDto tableContentDto : tableDataMap.values()) {
					tableContentDtos.add(tableContentDto);
				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WOKRBOX][FETCHING TABLE DATA] ERROR: " + e.getMessage());
		}
		return tableContentDtos;
	}

	@SuppressWarnings("unchecked")
	public List<CustomAttributeTemplateDto> getTaskCustomAttributes(String processName) {
		List<CustomAttributeTemplateDto> cusAttrList = null;
		CustomAttributeTemplateDto taskCusAttr = null;
		List<ValueListDto> valueList = null;
		List<TableContentDto> tableContentDtos = null;
		try {
			String attributeDetail = "select cat.PROCESS_NAME,cat.KEY,cat.LABEL,cat.IS_ACTIVE,cat.IS_MAND,cat.IS_EDITABLE "
					+ ",cat.DATA_TYPE,cat.ATTR_DES,cat.origin,cat.attribute_path,cat.dependant_on , cat.IS_RUN_TIME from CUSTOM_ATTR_TEMPLATE as cat "
					+ "where (cat.PROCESS_NAME in " + "(select template_id from process_template where process_name = '"
					+ processName + "' and" + " (source_id is null or source_id = '')))"
					+ " and (cat.IS_DELETED= 0) and (cat.IS_VISIBLE= 1) ";
			Query attrDetailQuery = getSession().createSQLQuery(attributeDetail);

			List<Object[]> custAttr = attrDetailQuery.list();
			if (!custAttr.isEmpty()) {
				cusAttrList = new ArrayList<>();
				for (Object[] obj : custAttr) {
					taskCusAttr = new CustomAttributeTemplateDto();
					taskCusAttr.setProcessName(obj[0].toString());
					taskCusAttr.setKey(obj[1].toString());
					taskCusAttr.setLabel(obj[2].toString());
//					System.err.println(ServicesUtil.asBoolean(obj[3]));
					if (!ServicesUtil.isEmpty(obj[3]))
						taskCusAttr.setIsActive(ServicesUtil.asBoolean(obj[3]));
					if (!ServicesUtil.isEmpty(obj[4]))
						taskCusAttr.setIsMandatory(ServicesUtil.asBoolean(obj[4]));
					if (!ServicesUtil.isEmpty(obj[5]))
						taskCusAttr.setIsEditable(ServicesUtil.asBoolean(obj[5]));
					taskCusAttr.setDataType(obj[6].toString());
					if (!ServicesUtil.isEmpty(obj[7]))
						taskCusAttr.setAttrDes(obj[7].toString());
					valueList = new ArrayList<ValueListDto>();
					taskCusAttr.setValueList(valueList);
					
					if (!ServicesUtil.isEmpty(obj[11]))
						taskCusAttr.setIsRunTime(ServicesUtil.asBoolean(obj[11]));
					else
						taskCusAttr.setIsRunTime(false);
					taskCusAttr.setDataTypeKey(obj[6].equals(TaskCreationConstant.DROPDOWN) && taskCusAttr.getIsRunTime() ? 1 : 0);

					taskCusAttr.setOrigin(ServicesUtil.isEmpty(obj[8]) ? "" : obj[8].toString());
					taskCusAttr.setAttributePath(ServicesUtil.isEmpty(obj[9]) ? null : obj[9].toString());
					taskCusAttr.setDependantOn(ServicesUtil.isEmpty(obj[10]) ? null : obj[10].toString());

					taskCusAttr.setValue("");
					
					if(taskCusAttr.getDataType().equals(TaskCreationConstant.TABLE)) {
						TableContentDto tableContentDto = new TableContentDto();
						tableContentDtos = new ArrayList<>();
						tableContentDto.setTableAttributes(getTaskCustomAttributesByTemplateId(taskCusAttr.getKey()));
						tableContentDtos.add(tableContentDto);
						taskCusAttr.setTableContents(tableContentDtos);

					}

					cusAttrList.add(taskCusAttr);
				}

				System.err.println(new Gson().toJson(cusAttrList));
				return cusAttrList;
				
			} else
				return new ArrayList<>();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WOKRBOX][ATTRIBUTE FETCHING] ERROR: " + e);
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<Object[]> fetchNotVisibleAttributes(String processName) {
		try {
			String attributeDetail = "select cat.PROCESS_NAME,cat.KEY,cat.LABEL,cat.IS_ACTIVE,cat.IS_MAND,cat.IS_EDITABLE "
					+ ",cat.DATA_TYPE,cat.ATTR_DES,cat.origin,cat.attribute_path,cat.dependant_on,cat.IS_RUN_TIME,cat.RUN_TIME_TYPE,cat.DEFAULT_VALUE"
					+ " from CUSTOM_ATTR_TEMPLATE as cat " + "where (cat.PROCESS_NAME= '" + processName
					+ "' AND ORIGIN = 'Process' AND ATTRIBUTE_PATH is not null) " + "and (cat.IS_DELETED= 0)";
			Query attrDetailQuery = getSession().createSQLQuery(attributeDetail);

			return attrDetailQuery.list();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WOKRBOX][ATTRIBUTE FETCHING] ERROR: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	public List<CustomAttributeTemplateDto> getTaskCustomAttributesByTemplateId(String templateId) {

		List<CustomAttributeTemplateDto> cusAttrList = null;
		CustomAttributeTemplateDto taskCusAttr = null;
		List<ValueListDto> valueList = null;
		List<TableContentDto> tableContentDtos = null;
		try {
			String attributeDetail = "select cat.PROCESS_NAME,cat.KEY,cat.LABEL,cat.IS_ACTIVE,cat.IS_MAND,cat.IS_EDITABLE "
					+ ",cat.DATA_TYPE,cat.ATTR_DES,cat.origin,cat.attribute_path,cat.dependant_on , cat.IS_RUN_TIME from"
					+ " CUSTOM_ATTR_TEMPLATE as cat " + "where cat.PROCESS_NAME = '" + templateId + "'"
					+ " and (cat.IS_DELETED= 0) and (cat.IS_VISIBLE=1) ";
			Query attrDetailQuery = getSession().createSQLQuery(attributeDetail);

			List<Object[]> custAttr = attrDetailQuery.list();
			if (!custAttr.isEmpty()) {
				cusAttrList = new ArrayList<>();
				for (Object[] obj : custAttr) {
					taskCusAttr = new CustomAttributeTemplateDto();
					taskCusAttr.setProcessName(obj[0].toString());
					taskCusAttr.setKey(obj[1].toString());
					taskCusAttr.setLabel(obj[2].toString());

					if (!ServicesUtil.isEmpty(obj[3]))
						taskCusAttr.setIsActive(ServicesUtil.asBoolean(obj[3]));
					if (!ServicesUtil.isEmpty(obj[4]))
						taskCusAttr.setIsMandatory(ServicesUtil.asBoolean(obj[4]));
					if (!ServicesUtil.isEmpty(obj[5]))
						taskCusAttr.setIsEditable(ServicesUtil.asBoolean(obj[5]));
					taskCusAttr.setDataType(obj[6].toString());
					if (!ServicesUtil.isEmpty(obj[7]))
						taskCusAttr.setAttrDes(obj[7].toString());
					valueList = new ArrayList<ValueListDto>();
					taskCusAttr.setValueList(valueList);
					if (!ServicesUtil.isEmpty(obj[11]))
						taskCusAttr.setIsRunTime(ServicesUtil.asBoolean(obj[11]));
					else
						taskCusAttr.setIsRunTime(false);
					taskCusAttr.setDataTypeKey(obj[6].equals(TaskCreationConstant.DROPDOWN) && taskCusAttr.getIsRunTime() ? 1 : 0);
					taskCusAttr.setOrigin(ServicesUtil.isEmpty(obj[8]) ? "" : obj[8].toString());
					taskCusAttr.setAttributePath(ServicesUtil.isEmpty(obj[9]) ? null : obj[9].toString());
					taskCusAttr.setDependantOn(ServicesUtil.isEmpty(obj[10]) ? null : obj[10].toString());

					taskCusAttr.setValue("");

					if (taskCusAttr.getDataType().equals(TaskCreationConstant.TABLE)) {
						TableContentDto tableContentDto = new TableContentDto();
						tableContentDtos = new ArrayList<>();
						tableContentDto.setTableAttributes(getTaskCustomAttributesByTemplateId(taskCusAttr.getKey()));
						tableContentDtos.add(tableContentDto);
						taskCusAttr.setTableContents(tableContentDtos);

					}

					cusAttrList.add(taskCusAttr);
				}
				System.err.println("Task level custom attributes : " + new Gson().toJson(cusAttrList));
				return cusAttrList;
			} else
				return new ArrayList<>();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WOKRBOX][ATTRIBUTE FETCHING] ERROR: " + e.getMessage());
			return new ArrayList<>();
		}

	}

}
