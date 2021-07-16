package oneapp.incture.workbox.demo.ecc.eccadapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dao.UserDetailsDao;
import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.UserDetailsDto;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Service
//////@Transactional
public class ECCParser {

	@Autowired
	private UserDetailsDao userDetailsDao;

	public TaskDetailsListDto converToDto(JSONArray results) {

		TaskDetailsListDto listDto = new TaskDetailsListDto();
		ProcessEventsDto processDto = null;
		TaskEventsDto taskDto = null;
		TaskOwnersDto ownersDto = null;
		CustomAttributeValueDto customAttributeValueDto = null;
		Map<String, UserDetailsDto> userDetailsMap = new HashMap<String, UserDetailsDto>();

		List<ProcessEventsDto> processEventsDto = new ArrayList<>();
		List<TaskEventsDto> taskEvensDto = new ArrayList<>();
		List<TaskOwnersDto> taskOwnersDto = new ArrayList<>();
		List<CustomAttributeValueDto> customAttributeValuesDto = new ArrayList<>();

		Map<String, ProcessEventsDto> allProcess = new HashMap<>();

		try {
			long start = System.currentTimeMillis();
			// user = UserManagementAccessor.getUserProvider().getCurrentUser();
			//
			//
			// String userId = user.getName();
			String userId = "P000006";
			System.err.println("[WBP-Dev]User Name : " + userId);

			userDetailsMap = getUserDetailsMap();
//			System.err.println("ECCParser.converToDto() user details map : " + userDetailsMap);

			// String userId = "";
			for (Object obj : results) {
				JSONObject jsonObj = (JSONObject) obj;

				processDto = new ProcessEventsDto();
				taskDto = new TaskEventsDto();
				ownersDto = new TaskOwnersDto();

				String eventId = jsonObj.getString("InstanceID");

				String taskDefinationId = jsonObj.getString("TaskDefinitionID").split("_")[0];

				String processName = "";

				if (taskDefinationId.equalsIgnoreCase("TS20000166")) {
					// processId ="PO"+new Random().nextInt(); // Generating
					// Random process Id
					processName = "PurchaseOrderApprovalProcess";
				} else if (taskDefinationId.equalsIgnoreCase("TS00007986")) {
					// processId ="PR"+new Random().nextInt();
					processName = "PurchaseRequisition";
				} else if (taskDefinationId.equalsIgnoreCase("TS00300052")) {
					processName = "AppropriationRequestApproval";
				}
				String status = jsonObj.getString("Status");

				processDto.setName(processName);

				processDto.setStartedAt(dateParser(jsonObj, "CreatedOn", processName));
				processDto.setStartedBy(jsonObj.getString("CreatedBy"));
				processDto.setStartedByDisplayName(jsonObj.getString("CreatedByName"));

				if (status.equalsIgnoreCase("READY")) {
					processDto.setStatus("RUNNING");
				} else if (taskDefinationId.equalsIgnoreCase("TS00007986")
						|| taskDefinationId.equalsIgnoreCase("TS20000166")) {
					processDto.setStatus("RUNNING");
					processDto.setCompletedAt(new Date());
				} else {
					processDto.setStatus(status);
					processDto.setCompletedAt(new Date());
				}
				processDto.setSubject(jsonObj.getString("TaskDefinitionName"));

				taskDto.setEventId(eventId);
				taskDto.setDescription(jsonObj.getString("TaskTitle"));
				taskDto.setCurrentProcessor(userId);
				taskDto.setCurrentProcessorDisplayName(ServicesUtil.isEmpty(jsonObj.getString("ProcessorName")) ? null
						: jsonObj.getString("ProcessorName"));
				taskDto.setPriority(jsonObj.getString("Priority"));
				taskDto.setProcessName(processName);
				// taskDto.setForwardedBy(jsonObj.getString("ForwardingUser"));
				// taskDto.setForwardedAt(dateParser(jsonObj,
				// "ForwardedOn",processName));
				taskDto.setCreatedAt(dateParser(jsonObj, "CreatedOn", processName));
				taskDto.setCompletionDeadLine(dateParser(jsonObj, "CompletionDeadLine", processName));
				taskDto.setStatus(status);
				taskDto.setSubject(jsonObj.getString("TaskDefinitionName"));
				taskDto.setUpdatedAt(new Date());
				taskDto.setName(jsonObj.getString("TaskDefinitionID"));
				taskDto.setOrigin("ECC");

				// // Check for SubstitutedUser
				// try {
				// String substitutedUser =
				// jsonObj.getString("SubstitutedUser");
				// if (!ServicesUtil.isEmpty(substitutedUser)) {
				// substitutedIds.add(eventId);
				// }
				// } catch (Exception x) {
				// System.err.println("[WBP-Dev]AdlsaWBECCParser.converToDto()
				// error in
				// inner try : "+x);
				//
				// }

				ownersDto.setEventId(eventId);
				try {
					ownersDto.setTaskOwnerDisplayName(userDetailsMap.get(userId).getDisplayName() != null
							? userDetailsMap.get(userId).getDisplayName() : null);
					ownersDto.setOwnerEmail(userDetailsMap.get(userId).getEmailId() != null
							? userDetailsMap.get(userId).getEmailId() : null);
				} catch (Exception e) {
					System.err.println("ECCParser.converToDto() exception : " + e.getMessage());
				}
				ownersDto.setTaskOwner(userId);
				ownersDto.setIsSubstituted(false);

				// parse custom attributes
				JSONArray attributes = jsonObj.getJSONObject("CustomAttributeData").getJSONArray("results");
				String processId = "";
				if (!ServicesUtil.isEmpty(attributes)) {

					for (Object ob : attributes) {
//						System.err.println("[WBP-Dev] ECC CustomAttributes JSON" + ob);
						JSONObject customAttributeJson = (JSONObject) ob;

						customAttributeValueDto = new CustomAttributeValueDto(eventId, processName);

						customAttributeValueDto.setKey(ServicesUtil.isEmpty(customAttributeJson.getString("Name"))
								? null : customAttributeJson.getString("Name"));
						customAttributeValueDto
								.setAttributeValue(ServicesUtil.isEmpty(customAttributeJson.getString("Value")) ? null
										: customAttributeJson.getString("Value").trim());
//						System.err.println("[WBP-Dev] ECC CustomAttributesDto" + customAttributeValueDto);
						// check for the reviewer
						if (taskDefinationId.equalsIgnoreCase("TS00007986")) {
							/*
							 * if (customAttributeValueDto.getKey().
							 * equalsIgnoreCase("Total Value")) { Long prValue =
							 * Long.valueOf(customAttributeValueDto.
							 * getAttributeValue().trim()); System.err.
							 * println("[WBP-Dev]ECCParser.converToDto() pr value : "
							 * + prValue);
							 * 
							 * if (prValue >= 10000L) {
							 * 
							 * TaskOwnersDto ownersDtoReviewer = new
							 * TaskOwnersDto();
							 * 
							 * ownersDtoReviewer.setEventId(eventId);
							 * ownersDtoReviewer.setTaskOwnerDisplayName(taskDto
							 * .getCurrentProcessorDisplayName());
							 * ownersDtoReviewer.setTaskOwner("P000100");
							 * ownersDtoReviewer.setIsSubstituted(false);
							 * ownersDtoReviewer.setOwnerEmail(null);
							 * ownersDtoReviewer.setIsReviewer(Boolean.TRUE);
							 * 
							 * taskOwnersDto.add(ownersDtoReviewer); } }
							 * 
							 */}

						if (taskDefinationId.equalsIgnoreCase("TS20000166")) {
							if (customAttributeValueDto.getKey().equalsIgnoreCase("PO_NUMBER")) {
								processId = customAttributeValueDto.getAttributeValue();
							}
							if (customAttributeValueDto.getKey().equalsIgnoreCase("Created by")) {
								processDto.setStartedBy(customAttributeValueDto.getAttributeValue());
							}
						} else if (taskDefinationId.equalsIgnoreCase("TS00007986")) {
							if (customAttributeValueDto.getKey().equalsIgnoreCase("PR_NUMBER")) {
								processId = customAttributeValueDto.getAttributeValue();
							}

							if (customAttributeValueDto.getKey().equalsIgnoreCase("CREATED_BY")) {
								processDto.setStartedBy(customAttributeValueDto.getAttributeValue());
							}
						} else if (taskDefinationId.equalsIgnoreCase("TS00300052")) {
							if (customAttributeValueDto.getKey().equalsIgnoreCase("AR_NUMBER")) {
								processId = customAttributeValueDto.getAttributeValue();
							}

							if (customAttributeValueDto.getKey().equalsIgnoreCase("CREATED_BY")) {
								processDto.setStartedBy(customAttributeValueDto.getAttributeValue());
							}
						} else {

					System.err.println("ECCParser.converToDto() No id found : " + customAttributeValueDto);
						}
					//	System.err.println("[WBP-Dev] ECC CustomAttributesDto" + customAttributeValueDto);
						customAttributeValuesDto.add(customAttributeValueDto);
					}
				}

				if (ServicesUtil.isEmpty(processId)) {
					System.err.println("ECCParser.converToDto() process id is empty : " + taskDto);

				}

//				System.err.println("ECCParser.converToDto() process Id : " + processId);

				processDto.setProcessId(processId);
				processDto.setRequestId(processId);

				if (!ServicesUtil.isEmpty(processId)) {
					allProcess.put(processId, processDto);
				}
				taskDto.setProcessId(processId);

				processEventsDto.add(processDto);

				taskEvensDto.add(taskDto);
				taskOwnersDto.add(ownersDto);

			}

			listDto.setProcessEventsDto(new ArrayList<ProcessEventsDto>(allProcess.values()));
			listDto.setTaskEvensDto(taskEvensDto);
			listDto.setTaskOwnersDto(taskOwnersDto);
			listDto.setCustomAttributeValueDto(customAttributeValuesDto);
			// listDto.setSubstitutedIds(substitutedIds);
			// System.err.println("[WBP-Dev]AdlsaWBECCParser.converToDto()
			// substituted
			// tasks : "+substitutedIds);
//			System.err.println("[WBP-Dev]ECCParser.converToDto() parsing time " + (System.currentTimeMillis() - start));
		} catch (Exception e) {
			System.err.println("[WBP-Dev]AdlsaWBECCParser.converToDto() Error while parsing : " + e);
		}

		return listDto;
	}

	private Date dateParser(JSONObject jsonObj, String key, String processName) {
		if (!JSONObject.NULL.equals(jsonObj.get(key)) && !ServicesUtil.isEmpty(jsonObj.get(key))
				&& !"null".equals(jsonObj.get(key))) {

			String date = jsonObj.get(key).toString();

			date = date.substring(date.indexOf("(") + 1, date.indexOf(")"));

			long x = Long.parseLong(date);

			return new Date(x);

		} else if (key.equalsIgnoreCase("CompletionDeadLine") && !ServicesUtil.isEmpty(processName)) {
			String createdAt = jsonObj.get("CreatedOn").toString();
			createdAt = createdAt.substring(createdAt.indexOf("(") + 1, createdAt.indexOf(")"));
			long created = Long.parseLong(createdAt);

			long sla = 0;
			int defaultSlaCount = 5;
			String defaultSlaUnits = "days";
			if (defaultSlaUnits.equalsIgnoreCase("days")) {
				sla = defaultSlaCount * 24 * 60 * 60 * 1000L;
			} else if (defaultSlaUnits.equalsIgnoreCase("hours")) {
				sla = defaultSlaCount * 60 * 60 * 1000L;
			} else if (defaultSlaUnits.equalsIgnoreCase("months")) {
				sla = defaultSlaCount * 30 * 24 * 60 * 60 * 1000L;
			}
			// System.err.println("[WBP-Dev]ECCParser.dateParser() SLA setup :
			// "+(System.currentTimeMillis()-start));

			return new Date(created + (sla));

		}

		return null;

	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * String str = "TS12000017";// _WS90000076_0000000605";
	 * 
	 * System.out.println("AdlsaWBECCParser.main(): " + str.split("_")[0]); }
	 */

	private Map<String, UserDetailsDto> getUserDetailsMap() {
		Map<String, UserDetailsDto> userDetailsMap = new HashMap<String, UserDetailsDto>();
		UserDetailsDto userDetails = null;
		List<Object[]> resultList = this.userDetailsDao.getUserDetailResponse();
		if (!ServicesUtil.isEmpty(resultList) && resultList.size() > 0) {
			for (Object[] object : resultList) {
				userDetails = new UserDetailsDto();
				userDetails.setUserId(ServicesUtil.isEmpty(object[0]) ? null : (String) object[0]);
				userDetails.setEmailId(ServicesUtil.isEmpty(object[1]) ? null : (String) object[1]);
				userDetails.setDisplayName(ServicesUtil.isEmpty(object[2]) ? null : (String) object[2]);
				userDetailsMap.put(userDetails.getUserId(), userDetails);
			}
		}
		System.err.println(userDetailsMap);
		// usersMap = userDetailsMap;
		return userDetailsMap;
	}

}
