package oneapp.incture.workbox.demo.adhocTask.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import net.objecthunter.exp4j.ExpressionBuilder;
import oneapp.incture.workbox.demo.adhocTask.dto.AttributesResponseDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeTemplateDto;
import oneapp.incture.workbox.demo.adhocTask.dto.CustomAttributeValues;
import oneapp.incture.workbox.demo.adhocTask.dto.ProcessAttributesDto;
import oneapp.incture.workbox.demo.adhocTask.dto.TableContentDto;
import oneapp.incture.workbox.demo.adhocTask.util.TaskCreationConstant;
import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.document.dto.DocumentResponseDto;
import oneapp.incture.workbox.demo.document.service.DocumentService;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessTemplateValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValueTableAdhocDo;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Repository("adhocCustomAttributeValueDao")
public class CustomAttributeValueDao {

	@Autowired
	DocumentService documentService;

	@Autowired
	ProcessEventDao processEventDao;

	@Autowired
	TaskEventDao taskEventDao;
	
	@Autowired
	SharepointUploadFile sharepointUploadFile;

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	@Autowired
	TaskTemplateOwnerDao taskTemplateOwnerDao;

	public void saveCustomAttributeValue(List<ProcessAttributesDto> listOfProcesssAttributes,
			List<String> eventIdList) {

		CustomAttributeValue attributeValue = null;
		Integer count = 0;
		Integer eventIdCount = 0;

		Session session = sessionFactory.openSession();

		Transaction tx = session.beginTransaction();

		for (ProcessAttributesDto attrValues : listOfProcesssAttributes) {

			for (CustomAttributeTemplateDto obj : attrValues.getCustomAttributeTemplateDto()) {

				count++;
				attributeValue = new CustomAttributeValue();

				attributeValue.setKey(obj.getKey());
				if (obj.getDataType().equals(TaskCreationConstant.ATTACHMENT)) {
					if ("".equals(obj.getValue())) {
						attributeValue.setKey("NULL");
					}
				} else {
					attributeValue.setAttributeValue(obj.getValue());
				}
				attributeValue.setProcessName(obj.getProcessName());
				attributeValue.setTaskId(eventIdList.get(eventIdCount));

				session.saveOrUpdate(attributeValue);

				if (count % 50 == 0) {
					session.flush();
					session.clear();
				}

			}

			eventIdCount++;
		}

		tx.commit();
		session.close();

	}

	public ResponseMessage updateCustomAttributeValues(AttributesResponseDto attributesResponseDto) {
		CustomAttributeValue attributeValue = null;
		CustomAttributeValueTableAdhocDo attributeValuetable = null;
		ResponseMessage responseMessage = null;
		Integer count = 0;
		String keyValue = "";
		List<AttachmentRequestDto> attachmentList = null;
		AttachmentRequestDto attachmentRequestDto = null;

		Session session = sessionFactory.openSession();

		Transaction tx = session.beginTransaction();
		try {
			for (CustomAttributeTemplateDto obj : attributesResponseDto.getListOfProcesssAttributes().get(0)
					.getCustomAttributeTemplateDto()) {
				if (TaskCreationConstant.DESCRIPTION.equals(obj.getKey())) {
					//continue;
					String updateDesc="update process_events set subject='"+obj.getValue()+"' where process_id='"+
							attributesResponseDto.getProcessId()+"'";
					Query update=session.createSQLQuery(updateDesc);
					update.executeUpdate();
				}

				count++;
				if (obj.getIsEdited() == 2) {
					if (TaskCreationConstant.DROPDOWN.equals(obj.getDataType())
							&& !ServicesUtil.isEmpty(obj.getValueList())
							&& TaskCreationConstant.RESOURCE.equals(obj.getDropDownType())) {
						keyValue = taskTemplateOwnerDao.createOwnerId(obj.getValueList());
						System.err.println("[WBP-Dev][WORKBOX][TASKCREATION]New Owner Id" + keyValue);
					} else if (obj.getDataType().equals(TaskCreationConstant.ATTACHMENT)) {
						if ("".equals(obj.getValue())) {
							keyValue = "";
						} else {
							attachmentRequestDto = new AttachmentRequestDto();
							attachmentRequestDto.setEncodedFileContent(obj.getValue());
							if (!ServicesUtil.isEmpty(obj.getAttachmentName()))
								attachmentRequestDto.setFileName(obj.getAttachmentName());
							else
								attachmentRequestDto.setFileName(obj.getLabel());
							attachmentRequestDto.setFileType(obj.getAttachmentType());
							attachmentList = new ArrayList<>();
							attachmentList.add(attachmentRequestDto);
							DocumentResponseDto responseDto = sharepointUploadFile.uploadFile(attachmentList,attachmentRequestDto.getFileName());
							Gson g = new Gson();
							keyValue = g.toJson(responseDto.getAttachmentIds());
						}
					} else if(TaskCreationConstant.TABLE.equalsIgnoreCase(obj.getDataType())) {
						
						for(TableContentDto tableContentDto : obj.getTableContents()) {
							for(CustomAttributeTemplateDto customAttributeTemplateDto : tableContentDto.getTableAttributes()) {
								if(customAttributeTemplateDto.getIsEdited() == 2) {
									attributeValuetable = new CustomAttributeValueTableAdhocDo();
									if("Process".equals(obj.getOrigin())) {
										attributeValuetable.setTaskId(attributesResponseDto.getProcessId());
									} else {
										attributeValuetable.setTaskId(obj.getTaskId());
									}
									attributeValuetable.setAttributeValue(customAttributeTemplateDto.getValue());
									attributeValuetable.setKey(customAttributeTemplateDto.getKey());
									attributeValuetable.setProcessName(customAttributeTemplateDto.getProcessName());
									attributeValuetable.setRowNumber(customAttributeTemplateDto.getRowNumber());
									attributeValuetable.setDependantOn(customAttributeTemplateDto.getDependantOn());
									session.saveOrUpdate(attributeValuetable);
								}
							}
						}
						
					} else {
						keyValue = obj.getValue();
					}

					attributeValue = new CustomAttributeValue();
					if ("Process".equals(obj.getOrigin()))
						attributeValue.setTaskId(attributesResponseDto.getProcessId());
					else
						attributeValue.setTaskId(obj.getTaskId());
					attributeValue.setAttributeValue(keyValue);
					attributeValue.setKey(obj.getKey());
					attributeValue.setProcessName(obj.getProcessName());
					session.saveOrUpdate(attributeValue);

//					if (!ServicesUtil.isEmpty(obj.getTableAttributes())) {
//						for (CustomAttributeTemplateDto tableObj : obj.getTableAttributes()) {
//							attributeValuetable = new CustomAttributeValueTableAdhocDo();
//							attributeValuetable.setTaskId(obj.getTaskId());
//							attributeValuetable.setAttributeValue(keyValue);
//							attributeValuetable.setKey(obj.getKey());
//							attributeValuetable.setProcessName(obj.getProcessName());
//							attributeValuetable.setRowNumber(obj.getRowNumber());
//							attributeValuetable.setDependantOn(obj.getDependantOn());
//							session.saveOrUpdate(attributeValuetable);
//						}
//
//					}

					if (count % 50 == 0) {
						session.flush();
						session.clear();
					}

				}
			}
			tx.commit();
			session.close();
			responseMessage = new ResponseMessage();
			responseMessage.setMessage("Saved Successfully ");
			responseMessage.setStatus(TaskCreationConstant.SUCCESS);
			responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);

		} catch (Exception e) {
			responseMessage = new ResponseMessage();
			responseMessage.setMessage("Not Saved in CAV ");
			responseMessage.setStatus(TaskCreationConstant.FAILURE);
			responseMessage.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);
			e.printStackTrace();
		}
		return responseMessage;
	}

	public ResponseMessage deleteDraftAttributes(String eventId) {
		ResponseMessage resp = new ResponseMessage();
		resp.setMessage(TaskCreationConstant.FAILURE);
		resp.setStatus(TaskCreationConstant.FAILURE);
		resp.setStatusCode(TaskCreationConstant.STATUS_CODE_FAILURE);

		try {
			Session session = sessionFactory.openSession();
			Transaction tx = session.beginTransaction();
			Query customAttrQry = session.createSQLQuery("delete from custom_attr_values where task_id =:eventId");
			customAttrQry.setParameter("eventId", eventId);
			resp.setMessage(TaskCreationConstant.SUCCESS);
			resp.setStatus(TaskCreationConstant.SUCCESS);
			resp.setStatusCode(TaskCreationConstant.STATUS_CODE_SUCCESS);
			customAttrQry.executeUpdate();
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev][WORKBOX][DRAFT DELETION] ERROR : " + e.getMessage());
		}

		return resp;
	}

	@SuppressWarnings("unchecked")
	public List<CustomAttributeValue> setCustomAttributes(String newEventId, String eventId) {

		List<CustomAttributeValue> attributeValuesList = null;
		CustomAttributeValue attributeValue = null;

		Query attrQry = getSession().createSQLQuery(
				"SELECT PROCESS_NAME,KEY,ATTR_VALUE FROM CUSTOM_ATTR_VALUES" + " WHERE TASK_ID='" + eventId + "'");
		List<Object[]> attrList = attrQry.list();

		attributeValuesList = new ArrayList<>();
		for (Object[] obj : attrList) {
			attributeValue = new CustomAttributeValue();
			attributeValue.setAttributeValue(obj[2].toString());
			attributeValue.setKey(obj[1].toString());
			attributeValue.setProcessName(obj[0].toString());
			attributeValue.setTaskId(newEventId);
			;

			attributeValuesList.add(attributeValue);
		}
		return attributeValuesList;
	}

	@SuppressWarnings("unchecked")
	public CustomAttributeValues setCustomAttributesNew(ProcessTemplateValueDto processTemplateValueDto , List<CustomAttributeValueTableAdhocDo> attributeValueTables) {
		List<CustomAttributeValue> attributeValuesList = null;
		CustomAttributeValue attributeValue = null;
		Map<String, String> attrKeyValue = new HashMap<>();
		CustomAttributeValues customAttributeValues = new CustomAttributeValues();
		List<String> dataTypeList = new ArrayList<>();
		try {
			Query attrQry = getSession()
					.createSQLQuery("SELECT PROCESS_NAME,KEY,ATTR_VALUE FROM CUSTOM_ATTR_VALUES WHERE" + " TASK_ID = '"
							+ processTemplateValueDto.getProcessId() + "' OR TASK_ID IN ("
							+ "SELECT EVENT_ID FROM TASK_EVENTS " + "WHERE PROCESS_ID = '"
							+ processTemplateValueDto.getProcessId() + "')");

			System.err.println("[WBP-Dev] existing CAV  of process query " + attrQry.getQueryString());

			List<Object[]> attrList = attrQry.list();

			for (Object[] obj : attrList) {
				if (!ServicesUtil.isEmpty(obj[2]))
					attrKeyValue.put(obj[1].toString(), obj[2].toString());
				else
					attrKeyValue.put(obj[1].toString(), "");
			}
			System.err.println("[WBP-Dev] existing CAV of process " + attrKeyValue);

			attributeValuesList = new ArrayList<>();

			Query newAttrQry = getSession()
					.createSQLQuery("SELECT PROCESS_NAME,KEY,ATTRIBUTE_PATH,DATA_TYPE FROM CUSTOM_ATTR_TEMPLATE"
							+ " WHERE PROCESS_NAME = '" + processTemplateValueDto.getTemplateId()
							+ "' and IS_ACTIVE = 1 and IS_VISIBLE = 1 and IS_DELETED = 0");

			System.err.println("[WBP-Dev] existing CAV of process query " + attrQry.getQueryString());

			List<Object[]> newAttrList = newAttrQry.list();

			for (Object[] obj : newAttrList) {
				attributeValue = new CustomAttributeValue();
				if (ServicesUtil.isEmpty(obj[2]))
					attributeValue.setAttributeValue("");
				else {
					String[] attr = obj[2].toString().split("[$]");
					String value = "";

					for (String str : attr) {
						if (str.contains("{")) {
							String s = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
							if (attrKeyValue.containsKey(s))
								str = str.replace("{" + s + "}", attrKeyValue.get(s));
							else
								str = "";
						}
						value += str;
					}
					attributeValue.setAttributeValue(value);
					
					if(!ServicesUtil.isEmpty(obj[3]) && !ServicesUtil.isEmpty(obj[2])) {
						String type = (String) obj[3];
						dataTypeList.add(type);
						if (type.contains(TaskCreationConstant.CALCULATION)) {
							String dataType = type.split("-")[0];
							switch (dataType) {
							case "INPUT NUMERIC":
								try {
									value = EvaluateExpression(attrKeyValue, obj[2].toString());
									attributeValue.setAttributeValue(value);
								} catch (Exception e) {
									System.err.println("[WBP-Dev]Calculating Expression Failed" + e.getMessage());
									attributeValue.setAttributeValue("");
								}
								break;

							default:
								attributeValue.setAttributeValue("");
								break;
							}
						}
					}
				}
				attributeValue.setKey(obj[1].toString());
				attributeValue.setProcessName(obj[0].toString());
				attributeValue.setTaskId(processTemplateValueDto.getEventId());
				attrKeyValue.put(attributeValue.getKey(), attributeValue.getAttributeValue());
				attributeValuesList.add(attributeValue);
			}
			customAttributeValues.setAttrKeyValues(attrKeyValue);
			customAttributeValues.setCustomAttributeValues(attributeValuesList);
			
			//if at least one TABLE attributes are there in task level
			if(dataTypeList.contains(TaskCreationConstant.TABLE)) {
				attributeValueTables.addAll(setCustomAttributesTableNew(processTemplateValueDto));
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev]Getting New set of Custom Attributes error" + e.getMessage());
		}
		return customAttributeValues;
	}

	
	String EvaluateExpression(Map<String, String> attrValue, String exp) {
		
		/* 
			 Function to evaluate the input numeric type expression 
			 expression will be in the form ${key1} '+' ${key2} + '*' ${key3} ...
		*/
		
		int j = 0;
		String key = "";
		String finalExp = "";
		System.err.println("[WBP-Dev] Attribute path : " + exp);
		while (j < exp.length() - 1) {
			char ch = exp.charAt(j);
			char next = exp.charAt(j + 1);
			if (ch == '{') {
				while (ch != '}') {
					key += ch;
					j++;
					ch = exp.charAt(j);
				}
				String value = attrValue.get(key.substring(1, key.length()));
				if(value.equals("") && !finalExp.equals("")) {
					finalExp = finalExp.substring(0 ,finalExp.length() - 1);
				}
				else {
					finalExp += value;
				}
				key = "";
			}
			if (ch == '\'' && next != ' ' && !finalExp.equals("")) {
				finalExp += next;
			}
			j++;
		}
		System.err.println("[WBP-Dev] Final exp : " + finalExp);
		String value = String.valueOf(new ExpressionBuilder(finalExp).build().evaluate());
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public List<CustomAttributeValueTableAdhocDo> setCustomAttributesTableNew(
			ProcessTemplateValueDto processTemplateValueDto) {
		List<CustomAttributeValueTableAdhocDo> attributeValuesList = null;
		CustomAttributeValueTableAdhocDo attributeValue = null;
		// Map<String, String> attrKeyValue = new HashMap<>();
		Map<String, List<String>> attrKeyValue2 = new HashMap<>();
		List<String> values = null;
		try {
			Query attrQry = getSession()
					.createSQLQuery("SELECT PROCESS_NAME,KEY,ATTR_VALUE,ROW_NUMBER FROM CUSTOM_ATTR_VALUES_TABLE WHERE"
							+ " TASK_ID = '" + processTemplateValueDto.getProcessId() + "' OR TASK_ID IN ("
							+ "SELECT EVENT_ID FROM TASK_EVENTS " + "WHERE PROCESS_ID = '"
							+ processTemplateValueDto.getProcessId() + "')  ORDER BY ROW_NUMBER");

			System.err.println("[WBP-Dev] existing CAV table  of process query " + attrQry.getQueryString());

			List<Object[]> attrList = attrQry.list();

			// for (Object[] obj : attrList) {
			// if (!ServicesUtil.isEmpty(obj[2]))
			// attrKeyValue.put(obj[1].toString(), obj[2].toString());
			// else
			// attrKeyValue.put(obj[1].toString(), "");
			// }

			for (Object[] obj : attrList) {
				if (attrKeyValue2.containsKey(obj[1].toString())) {
					if (!ServicesUtil.isEmpty(obj[2]))
						attrKeyValue2.get(obj[1].toString()).add(obj[2].toString());
					else
						attrKeyValue2.get(obj[1].toString()).add("");
				} else {
					values = new ArrayList<>();
					if (!ServicesUtil.isEmpty(obj[2]))
						values.add(obj[2].toString());
					else
						values.add("");
					attrKeyValue2.put(obj[1].toString(), values);
				}
			}

			System.err.println("[WBP-Dev] existing CAV table  of process " + attrKeyValue2);

			attributeValuesList = new ArrayList<>();
			int i = 0;
//			for (ProcessTemplateValueDto tasks : processTemplateValueDtos) {

				Query newAttrQry = getSession()
						.createSQLQuery("SELECT PROCESS_NAME,KEY,ATTRIBUTE_PATH,DATA_TYPE FROM CUSTOM_ATTR_TEMPLATE"
								+ " WHERE PROCESS_NAME in (select KEY from CUSTOM_ATTR_TEMPLATE where PROCESS_NAME = '"
								+ processTemplateValueDto.getTemplateId() + "' and data_type='TABLE')");
				System.err.println("[WBP-Dev] existing CAV table  of process query " + newAttrQry.getQueryString());

				List<Object[]> newAttrList = newAttrQry.list();

				for (Object[] obj : newAttrList) {
					attributeValue = new CustomAttributeValueTableAdhocDo();
					if (ServicesUtil.isEmpty(obj[2])) {
						attributeValue.setAttributeValue("");
						attributeValue.setKey(obj[1].toString());
						attributeValue.setProcessName(obj[0].toString());
						attributeValue.setTaskId(processTemplateValueDto.getEventId());
						attributeValue.setDependantOn(processTemplateValueDto.getTaskName());
						attributeValue.setRowNumber(1);
						// attrKeyValue.put(attributeValue.getKey(),
						// attributeValue.getAttributeValue());
						attrKeyValue2.put(attributeValue.getKey(), new ArrayList<>());
						attributeValuesList.add(attributeValue);
					} else {
						String str = obj[2].toString();
						String key = "";

						if (str.contains("{")) {
							key = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
							Integer index = 1;

							values = new ArrayList<>();
							if (!ServicesUtil.isEmpty(attrKeyValue2.get(key)) || !attrKeyValue2.get(key).isEmpty()) {
								for (String value : attrKeyValue2.get(key)) {
									attributeValue = new CustomAttributeValueTableAdhocDo();
									attributeValue.setKey(obj[1].toString());
									attributeValue.setProcessName(obj[0].toString());
									attributeValue.setTaskId(processTemplateValueDto.getEventId());
									attributeValue.setDependantOn(processTemplateValueDto.getTaskName());
									attributeValue.setRowNumber(index);
									attributeValue.setAttributeValue(value);
									attributeValuesList.add(attributeValue);
									values.add(value);
									index++;
								}
							}
							attrKeyValue2.put(attributeValue.getKey(), values);

						}

					}
				}
//			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev]Getting New set of Custom Attributes Table error" + e);
			e.printStackTrace();
		}
		return attributeValuesList;
	}

	public String getBudgetFromCustomAttributes(String instanceId) {
		String result = "";
		try {
			String attrQry = "select attr_value from custom_attr_values where " + "task_id='" + instanceId
					+ "' and key='bgiiabe5610id'";
			Object obj = getSession().createSQLQuery(attrQry).uniqueResult();
			result = obj.toString();
			System.err.println("[getBudgetFromCustomAttributes]" + result);

		} catch (Exception e) {
			System.err.println("[getBudgetFromCustomAttributes][error]" + e.getLocalizedMessage());
		}
		return result;
	}

	public String getCostCenterFromCustomAttributes(String instanceId) {
		String result = "";
		try {
			String attrQry = "select attr_value from custom_attr_values where " + "task_id='" + instanceId
					+ "' and key='bede8c13d82d'";
			Object obj = getSession().createSQLQuery(attrQry).uniqueResult();
			result = obj.toString();
			System.err.println("[getBudgetFromCustomAttributes]" + result);

		} catch (Exception e) {
			System.err.println("[getBudgetFromCustomAttributes][error]" + e.getLocalizedMessage());
		}
		return result;
	}

	public List<Object[]> getAttributesForInstance(String instanceId) {
		Query custAttrQry = getSession().createSQLQuery(
				"select key, attr_value from custom_attr_values where task_id in (select process_id from task_events where event_id = '"
						+ instanceId + "')");

		return custAttrQry.list();
	}

	public void saveAnCustomAttribute(CustomAttributeValue customAttributeValue) {
		try {
			Session session = sessionFactory.openSession();

			Transaction tx = session.beginTransaction();

			session.saveOrUpdate(customAttributeValue);

			session.flush();
			session.clear();
			tx.commit();
			session.close();
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Error adding to CAV" + e);
		}
	}
}
