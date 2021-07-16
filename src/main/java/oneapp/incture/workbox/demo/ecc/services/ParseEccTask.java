package oneapp.incture.workbox.demo.ecc.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.RestResponse;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkboxMasterDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstants;
import oneapp.incture.workbox.demo.adapter_base.util.PropertiesConstantsStatic;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.ecc.eccadapter.util.SCPRestUtil;

@Service
public class ParseEccTask {

	@Autowired
	TaskEventsDao eventsDao;

	@Autowired
	ProcessEventsDao processEventsDao;
	@Autowired

	TaskOwnersDao ownersDao;
	@Autowired
	CustomAttributeDao attributeDao;
	@Autowired
	PropertiesConstants getProperty;

	public WorkboxMasterDto parseSFTasks(String taskDefinitionID) {
		String taskCollectionUrl = prepareDetailUrl(taskDefinitionID);
		WorkboxMasterDto list = null;
		System.err.println("[WBP-Dev]ParseEccTask.parseSFTasks() TaskDefinition Url" + taskCollectionUrl);
		Long start = System.currentTimeMillis();
		try {
			RestResponse restResponse = SCPRestUtil.callRestService(taskCollectionUrl, null, null,
					PMCConstant.HTTP_METHOD_GET, PMCConstant.APPLICATION_JSON, false, null, "BOPF1", "Dec@200", null,
					null);
			System.err.println("[WBP-Dev]Rest Response: " + restResponse);

			if (restResponse.getResponseCode() == 200) {
				JSONObject jsonObj = (JSONObject) restResponse.getResponseObject();
				if (!ServicesUtil.isEmpty(jsonObj)) {
					JSONArray results = jsonObj.getJSONObject("d").getJSONArray("results");

					start = System.currentTimeMillis();
					list = converToDto(results);
				}
			}
			System.err.println(
					"[WBP-Dev]ParseEccTask.parseSFTasks() time to parse" + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis();
			System.err.println(list);
			// processEventsDao.get
			if (list != null) {
				processEventsDao.saveOrUpdateProcesses(list.getProcesses());
				eventsDao.saveOrUpdateTasks(list.getTasks());
				ownersDao.saveOrUpdateOwners(list.getOwners());
				attributeDao.addCustomAttributeValue(list.getAttrDtos());
			}
			System.err.println(
					"[WBP-Dev]ParseEccTask.parseSFTasks() time to save to DB" + (System.currentTimeMillis() - start));

		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("[WBP-Dev]ParseEccTask.parseSFTasks() error" + e.getMessage());

			
		}

		return list;

	}

	private WorkboxMasterDto converToDto(JSONArray results) {
		// TODO Auto-generated method stub

		System.err.println("[WBP-Dev]ParseEccTask.converToDto() results " + results);

		WorkboxMasterDto listDto = new WorkboxMasterDto();
		TaskEventsDto taskDto = null;
		TaskOwnersDto ownersDto = null;
		CustomAttributeValue customAttributeValueDto = null;
		ProcessEventsDto processDto = null;

		List<TaskEventsDto> taskEventsDto = new ArrayList<>();
		List<TaskOwnersDto> taskOwnersDto = new ArrayList<>();
		List<ProcessEventsDto> processEventsDto = new ArrayList<>();
		List<CustomAttributeValue> attributeValues = new ArrayList<>();
		// List<CustomAttrDtoDemo> customAttributeValuesDto = new ArrayList<>();
		String userId = "BOPF1";
		String processName = "";
		try {
			for (Object obj : results) {
				JSONObject jsonObj = (JSONObject) obj;

				taskDto = new TaskEventsDto();
				ownersDto = new TaskOwnersDto();
				processDto = new ProcessEventsDto();

				String eventId = jsonObj.getString("InstanceID");

				// jsonObj.getString("TaskDefinitionID");
				processName = jsonObj.getString("TaskDefinitionName");
				String processId = "";
				if (processName.equalsIgnoreCase("PO approval")) {
					processId = "PO" + new Random().nextInt(Integer.MAX_VALUE - 1);
				} else if (processName.equalsIgnoreCase("PR approval")) {
					processId = "PR" + new Random().nextInt(Integer.MAX_VALUE - 1);
				}

				taskDto.setEventId(eventId);
				taskDto.setProcessId(processId.split("_")[0]);
				taskDto.setDescription(jsonObj.getString("TaskTitle"));
				// taskDto.setCurrentProcessorDisplayName(userId);
				taskDto.setPriority(jsonObj.getString("Priority"));
				taskDto.setProcessName(jsonObj.getString("TaskDefinitionName"));
				taskDto.setOrigin("ECC");

				processDto.setName(jsonObj.getString("TaskDefinitionName"));
				processDto.setProcessId(processId);
				processDto.setRequestId(processId);
				/*
				 * taskDto.setForwardedBy(jsonObj.getString("ForwardingUser"));
				 * taskDto.setForwardedAt(dateParser(jsonObj, "ForwardedOn"));
				 */
				// taskDto.setCurrentProcessor(userId);
				taskDto.setCreatedAt(dateParser(jsonObj, "CreatedOn"));
				taskDto.setCompletionDeadLine(dateParser(jsonObj, "CompletionDeadLine"));

				String status = jsonObj.getString("Status");
				taskDto.setStatus(status);
				if (status.equalsIgnoreCase("READY"))
					processDto.setStatus("RUNNING");
				else
					processDto.setStatus(status);

				taskDto.setSubject(jsonObj.getString("TaskDefinitionName"));
				taskDto.setUpdatedAt(new Date());

				ownersDto.setEventId(eventId);
				ownersDto.setTaskOwnerDisplayName(userId);
				ownersDto.setTaskOwner(userId);
				ownersDto.setIsSubstituted(false);
				ownersDto.setOwnerEmail(null);

				// parse custom attributes
				JSONArray attributes = jsonObj.getJSONObject("CustomAttributeData").getJSONArray("results");

				if (!ServicesUtil.isEmpty(attributes)) {
					for (Object ob : attributes) {

						JSONObject customAttributeJson = (JSONObject) ob;
						customAttributeValueDto = new CustomAttributeValue();
						String name = customAttributeJson.getString("Name");
						String value = customAttributeJson.getString("Value");
						customAttributeValueDto.setProcessName(processName);
						customAttributeValueDto.setTaskId(eventId);
						customAttributeValueDto.setKey((name != null && !value.equals("")) ? name : null);
						customAttributeValueDto.setAttributeValue((value != null && !value.equals("")) ? value : null);

						if (customAttributeJson != null && name != null) {
							if (("PO_NUMBER").equalsIgnoreCase(name)) {
								processDto.setRequestId((value != null && !value.equals("")) ? value : null);

							}

							if (processName.equalsIgnoreCase("PO approval")) {
								if (("PO_NUMBER").equalsIgnoreCase(name)) {
									processDto.setRequestId((value != null && !value.equals("")) ? value : null);

									taskDto.setRequestId((value != null && !value.equals("")) ? value : null);

								}
							}
							if (processName.equalsIgnoreCase("PR approval")) {
								if (("PR_NUMBER").equalsIgnoreCase(name)) {
									processDto.setRequestId((value != null && !value.equals("")) ? value : null);
									taskDto.setRequestId((value != null && !value.equals("")) ? value : null);

								}
							}

							if (("Created by").equalsIgnoreCase(name)) {
								String startedBy = ((value != null && !value.equals("")) ? value : null);
								processDto.setStartedBy(startedBy);
								processDto.setStartedByDisplayName(startedBy);

							}
						}

						attributeValues.add(customAttributeValueDto);
					}
					taskEventsDto.add(taskDto);
					taskOwnersDto.add(ownersDto);
					processEventsDto.add(processDto);

				}

				listDto.setTasks(taskEventsDto);
				listDto.setOwners(taskOwnersDto);
				listDto.setProcesses(processEventsDto);
				listDto.setAttrDtos(attributeValues);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]ParseEccTask.converToDto() error" + e);
		}
		// listDto.setProcesses(customAttributeValuesDto);
		// listDto.setSubstitutedIds(substitutedIds);
		System.err.println("[WBP-Dev]ParseEccTask.converToDto() list" + listDto);

		return listDto;
	}

	private static Date dateParser(JSONObject jsonObj, String key) {
		if (!JSONObject.NULL.equals(jsonObj.get(key)) && !ServicesUtil.isEmpty(jsonObj.get(key))
				&& !"null".equals(jsonObj.get(key))) {

			String date = jsonObj.get(key).toString();

			date = date.substring(date.indexOf("(") + 1, date.indexOf(")"));

			long x = Long.parseLong(date);

			return new Date(x);

		}

		if (key.equalsIgnoreCase("CompletionDeadLine")) {
			return new Date();
		}

		return null;

	}

	private static String prepareDetailUrl(String taskDefinitionID) {

		String baseUrl = PropertiesConstantsStatic.CC_VIRTUAL_HOST + PropertiesConstantsStatic.TASK_COLLECTION_URL
				+ PMCConstant.TASK_COLLECTION_RELATIVE_URL;

		String ids[] = taskDefinitionID.split(",");
		String filter = "";
		filter = PMCConstant.FILTER + "=((";
		for (String i : ids) {
			filter = filter + PMCConstant.TASK_DEFINITION_ID + PMCConstant.SPACE + "eq" + PMCConstant.SPACE
					+ PMCConstant.SINGLE_QUOTE + i + PMCConstant.SINGLE_QUOTE + PMCConstant.SPACE + "or"
					+ PMCConstant.SPACE;
		}
		// remove the last spaces and or condition
		filter = filter.substring(0, filter.length() - 8);
		filter = filter + ")" + PMCConstant.SPACE + "and" + PMCConstant.SPACE + "Status" + PMCConstant.SPACE + "eq"
				+ PMCConstant.SPACE + PMCConstant.SINGLE_QUOTE + "READY" + PMCConstant.SINGLE_QUOTE + ")";

		// order by filter
		String orderBy = PMCConstant.CREATED_ON_DESC;

		// expand custom attributes
		String expandCustomAttributes = "$expand=CustomAttributeData";
		// skip and top
		System.err.println(
				"url with filte r" + baseUrl + "&" + filter.trim() + "&" + orderBy + "&" + expandCustomAttributes);
		return baseUrl + "&" + filter.trim() + "&" + orderBy + "&" + expandCustomAttributes;

		// return baseUrl;
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * System.out.println(prepareDetailUrl("123")); }
	 */

}
