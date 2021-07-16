package oneapp.incture.workbox.demo.inbox.sevices;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskOwnersDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDo;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskTemplateOwnerDao;
import oneapp.incture.workbox.demo.adhocTask.dao.TaskValueDao;
import oneapp.incture.workbox.demo.adhocTask.dao.UserIDPMappingDao;
import oneapp.incture.workbox.demo.inbox.dto.TaskEventsResponse;

/**
 * Session Bean implementation class TaskFacade
 */
@Service("TaskFacade")
public class TaskFacade implements TaskFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(TaskFacade.class);
	/*
	 * UserManagementFacadeWsdlConsumerLocal webService;
	 */

	@Autowired
	private TaskOwnersDao taskOwnersDao;

	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private TaskValueDao taskValueDao;

	@Autowired
	CustomAttributeDao customAttributeDao;
	
	@Autowired
	UserIDPMappingDao userIDPMappingDao;
	
	@Autowired
	private TaskTemplateOwnerDao templateOwnerDao;

	@SuppressWarnings("unused")
	@Override
	public TaskEventsResponse getTaskDetailsByProcessInstance(ProcessEventsDto processInstance, String taskId) {
		TaskEventsResponse response = new TaskEventsResponse();
		List<TaskEventsDto> taskEventsDtos = new ArrayList<TaskEventsDto>();
		List<TaskEventsDo> taskEventsDos = null;
		List<String> taskSla = null;
		TaskEventsDo futureTaskDetails = null;
		ResponseMessage responseMessage = new ResponseMessage();
		DateFormat formate = new SimpleDateFormat(PMCConstant.DETAILDATE_AMPM_FORMATE);
		formate.setTimeZone(TimeZone.getTimeZone("IST"));
		DateFormat createdFormat = new SimpleDateFormat(PMCConstant.TASK_CREATED_FORMATE);
		createdFormat.setTimeZone(TimeZone.getTimeZone("IST"));
		String totalTimeTaken = "";
		String processId = processInstance.getProcessId();
		Boolean rejectflag = false;//used for AFE
		if (!ServicesUtil.isEmpty(processId)) {

			/*
			 * Query query = sessionFactory.openSession()
			 * .createQuery("select te from TaskEventsDo te where te.taskEventsDoPK.processId =:processId"
			 * ); query.setParameter("processId", processId); taskEventsDos =
			 * (List<TaskEventsDo>) query.list();
			 */
			if (!ServicesUtil.isEmpty(taskId) && ("inventoryparentworkflow".equalsIgnoreCase(processInstance.getName())
					|| "ic_manager_approval_process".equalsIgnoreCase(processInstance.getName())
					|| "Analyst_Appproval_process".equalsIgnoreCase(processInstance.getName())))

			{
				List<String> eventIds = customAttributeDao.getCustomAttributeByTaskIdAndKey("caseId", taskId);
				taskEventsDos = taskEventsDao.getTaskDetailsByCaseId(eventIds);

			} else {

				if("AFENexus".equalsIgnoreCase(processInstance.getName())){
					taskEventsDos = taskEventsDao.getTaskDetailsByProcessIdAFE(processId);
					rejectflag = taskValueDao.getRejectStatus(processId);
				}else{
					rejectflag = taskValueDao.getRejectStatus(processId);
					taskEventsDos = taskEventsDao.getTaskDetailsByProcessId(processId);
				}
				taskSla = taskEventsDao.getTaskSla(processId);
				List<String> resubmittedProcess = taskEventsDao.getPreviousCompletedProcess(processId);
				if (!ServicesUtil.isEmpty(resubmittedProcess)) {
					for (String str : resubmittedProcess) {
						taskEventsDos.addAll(taskEventsDao.getTaskDetailsByProcessId(str));
					}
				}
			}
			System.err.println("[WBP-Dev]TaskEvents by  processID :" + processId + " taskEvents" + taskEventsDos);
			if (!ServicesUtil.isEmpty(taskEventsDos)) {
//				response = new TaskEventsResponse();
//				taskEventsDtos = new ArrayList<TaskEventsDto>();
				Integer count = 0;
				for (TaskEventsDo entity : taskEventsDos) {

					if (ServicesUtil.isEmpty(futureTaskDetails))
						futureTaskDetails = entity;

					TaskEventsDto taskEventsDto = new TaskEventsDto();

					if(rejectflag){
						if("NOT STARTED".equalsIgnoreCase(entity.getStatus())
								|| "NOTSTARTED".equalsIgnoreCase(entity.getStatus()))
							continue;
					}
					taskEventsDto.setEventId(entity.getEventId());
					taskEventsDto.setProcessId(entity.getProcessId());
					taskEventsDto.setDescription(entity.getDescription());
					taskEventsDto.setName(entity.getName());
					taskEventsDto.setSubject(entity.getSubject());
					taskEventsDto.setStatus(entity.getStatus());
					taskEventsDto.setCurrentProcessor(entity.getCurrentProcessor());
					taskEventsDto.setCurrentProcessorDisplayName(entity.getCurrentProcessorDisplayName());
					taskEventsDto.setTaskType(entity.getTaskType());
					taskEventsDto.setForwardedBy(entity.getForwardedBy());
					taskEventsDto.setForwardedAt(entity.getForwardedAt());
					if (!ServicesUtil.isEmpty(entity.getForwardedAt()))
						taskEventsDto.setForwardedAtInString(formate.format(entity.getForwardedAt()));
					taskEventsDto.setStatusFlag(entity.getStatusFlag());
					taskEventsDto.setTaskMode(entity.getTaskMode());
					try{
						if(!ServicesUtil.isEmpty(taskSla.get(count)))
							taskEventsDto.setTaskSla(taskSla.get(count));
						count++;
					}catch (Exception e) {
						System.err.println("[WBP-Dev]ERROR in Task sla"+e);
					}
					List<TaskOwnersDo> taskOwnersDos;
					List<String> owners = new ArrayList<String>();
					String ownersName = "";
					if (ServicesUtil.isEmpty(entity.getCompletedAt())) {
						if (!ServicesUtil.isEmpty(taskEventsDto.getProcessId())
								&& !ServicesUtil.isEmpty(taskEventsDto.getName())
								&& !ServicesUtil.isEmpty(entity.getCreatedAt())) {
						}
					} else {
						taskEventsDto.setCompletedAt(entity.getCompletedAt());
						taskEventsDto.setCompletedAtInString(ServicesUtil.isEmpty(entity.getCompletedAt()) ? null
								: formate.format(entity.getCompletedAt()));
					}

					if (ServicesUtil.isEmpty(taskEventsDto.getCurrentProcessor())) {
						if (!ServicesUtil.isEmpty(taskEventsDto.getEventId())) {

							taskOwnersDos = taskOwnersDao.getTaskOwnersDetailsByEventId(taskEventsDto.getEventId());
							if (!ServicesUtil.isEmpty(taskOwnersDos)) {
								for (int i = 0; i < taskOwnersDos.size(); i++) {
									owners.add(taskOwnersDos.get(i).getTaskOwnersDoPK().getTaskOwner());
									if (i == taskOwnersDos.size() - 1) {
										ownersName = ownersName + " " + taskOwnersDos.get(i).getTaskOwnerDisplayName();
									} else if (i == 0) {
										ownersName = ownersName + taskOwnersDos.get(i).getTaskOwnerDisplayName() + ",";
									} else {
										ownersName = ownersName + " " + taskOwnersDos.get(i).getTaskOwnerDisplayName()
												+ ",";
									}
								}
							}
						}
					} else {
						owners.add(taskEventsDto.getCurrentProcessor());
						if (!ServicesUtil.isEmpty(taskEventsDto.getCurrentProcessorDisplayName()))
							ownersName = taskEventsDto.getCurrentProcessorDisplayName();
						else {
							ownersName = taskOwnersDao.getOwnerName(taskEventsDto.getCurrentProcessor(),
									taskEventsDto.getEventId());
							System.err.println("ownersName"+ownersName);
						}
					}
					System.err.println("ownersName"+ownersName);
					taskEventsDto.setOwners(owners);
					if (!ServicesUtil.isEmpty(ownersName))
						taskEventsDto.setOwnersName(ownersName);
					taskEventsDto.setPriority(entity.getPriority());
					taskEventsDto.setCreatedAt(
							ServicesUtil.isEmpty(entity.getCreatedAt()) ? new Date() : entity.getCreatedAt());
					if (!ServicesUtil.isEmpty(entity.getCreatedAt()))
						taskEventsDto.setCreatedAtInString(formate.format(entity.getCreatedAt()));
					taskEventsDto.setCompletionDeadLine(entity.getCompletionDeadLine());
					totalTimeTaken = "";
					if (!ServicesUtil.isEmpty(taskEventsDto.getCompletedAt())) {
						long diff = taskEventsDto.getCompletedAt().getTime() - taskEventsDto.getCreatedAt().getTime();
						long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);

						diff = diffInSeconds/(24*3600);
						if (diff > 0)
							totalTimeTaken = String.valueOf(diff) + " Days ";

						diffInSeconds = diffInSeconds % (24 * 3600);
						diff = diffInSeconds/3600;
						if (diff > 0)
							totalTimeTaken = totalTimeTaken + String.valueOf(diff) + " Hours ";
						diffInSeconds %= 3600;

						diff = diffInSeconds/60;
						if (diff > 0)
							totalTimeTaken = totalTimeTaken + String.valueOf(diff) + " Minutes";

						diffInSeconds %= 60;
						if ("".equals(totalTimeTaken))
							totalTimeTaken = String.valueOf(diffInSeconds) + " Seconds";

						System.err.println(totalTimeTaken);
						taskEventsDto.setTotalTime(totalTimeTaken);
					}

					taskEventsDtos.add(taskEventsDto);
					logger.error("Task Event DTO : : " + taskEventsDto);
				}
			}
			System.err.println("Final Task Events DTOs : : : " + taskEventsDtos);
		}
		Comparator<TaskEventsDto> sortByStartedAt = new Comparator<TaskEventsDto>() {
			@Override
			public int compare(TaskEventsDto o1, TaskEventsDto o2) {
				return o1.getCreatedAt().compareTo(o2.getCreatedAt());
			}
		};

		if (!ServicesUtil.isEmpty(taskEventsDos)) {
			Collections.sort(taskEventsDtos, sortByStartedAt);
		}


		// logic to get the future tasks info
		try {
			List<Object[]> taskValues = null;
			boolean futureTaskFlag = false;
			Map<String,Integer> userMapAFE = userIDPMappingDao.getUsersWithBudget();
			Gson g=new Gson();
			if("AFENexus".equalsIgnoreCase(processInstance.getName())){
				if("COMPLETED".equalsIgnoreCase(processInstance.getStatus()))
					taskValues = new ArrayList<>();
				else
					taskValues = taskValueDao.getFutureTaskDetailsByProcessIdForAdhocAFE(processId);
				System.err.println("taskValues"+g.toJson(taskValues));
			}else{
				if("COMPLETED".equalsIgnoreCase(processInstance.getStatus())) {
					taskValues = new ArrayList<>();
					futureTaskFlag = true;
				}
				else {
					taskValues = taskValueDao.getFutureTaskDetailsByProcessIdForAdhoc(processId);
				}				
			}
			
			Map<Integer, TaskEventsDto> Existingmap = new TreeMap<>();
			Map<Integer, TaskEventsDto> map = new TreeMap<>();
			int j = 1;
			List<String> afeTaskName = new ArrayList<>();
			for (TaskEventsDto taskEventsDto : taskEventsDtos) {
				if("AFENexus".equalsIgnoreCase(processInstance.getName())){
					if(!taskEventsDto.getProcessId().equals(processId))
						continue;
					Existingmap.put(j , taskEventsDto);
				}else
					Existingmap.put(j , taskEventsDto);
				j++;
				afeTaskName.add(taskEventsDto.getName());
			}
			System.err.println("afeTaskName"+afeTaskName);
			int i = 0;
			
			for (Object[] taskValue : taskValues) {

				if (!futureTaskFlag)
					futureTaskFlag = true;

				System.err.println("TaskFacade.getTaskDetailsByProcessInstance() future tasks : " + g.toJson(taskValue));
				// check for the step number exist in the map
				i++;
				int stepNumber = i;
				if (Existingmap.containsKey(stepNumber)) {
					TaskEventsDto taskDto = map.get(stepNumber);
					if (!ServicesUtil.isEmpty(taskDto)) {
						List<String> ownerId = null;
						if (!ServicesUtil.isEmpty(taskDto.getOwners())) {
							ownerId = taskDto.getOwners();
						} else {
							ownerId = new ArrayList<>();
						}

						ownerId.add(ServicesUtil.isEmpty(taskValue[8]) ? null : (String) taskValue[8]);
						taskDto.setOwners(ownerId);
						taskDto.setOwnersName(ServicesUtil.isEmpty(taskDto.getOwnersName()) ? null
								: taskDto.getOwnersName() + " , "
								+ (ServicesUtil.isEmpty(taskValue[9]) ? null : (String) taskValue[9]));
					}

				} else {
					
					String ownerSeqType = ServicesUtil.isEmpty(taskValue[7]) ? "" : (String) taskValue[7];
					String ownerId = ServicesUtil.isEmpty(taskValue[5]) ? "" : (String) taskValue[5];
					TaskEventsDto taskDto = new TaskEventsDto();
					List<String> ownerIdList = new ArrayList<>();;
					if(ownerSeqType.equals("Group") && !"AFENexus".equalsIgnoreCase(processInstance.getName())) {
						List<TaskOwnersDto> taskOwnerList = templateOwnerDao.getOwners(ownerId);
						String taskOwnerName = "";
						for (TaskOwnersDto taskOwnersDto : taskOwnerList) {
							ownerIdList.add(taskOwnersDto.getTaskOwner());
							taskOwnerName += taskOwnersDto.getTaskOwnerDisplayName() + " , ";
						}
						if(!taskOwnerName.equals("")) {
							taskDto.setOwnersName(taskOwnerName.substring(0 ,taskOwnerName.length()-2));
						}
						else {
							continue;
						}
						
					}
					else {
				    	ownerIdList.add(ServicesUtil.isEmpty(taskValue[8]) ? null : (String) taskValue[8]);
						taskDto.setOwnersName(ServicesUtil.isEmpty(taskValue[9]) ? null : (String) taskValue[9]);
					}
					
					taskDto.setTaskType(ServicesUtil.isEmpty(taskValue[1]) ? null : (String) taskValue[1]);
					taskDto.setName(ServicesUtil.isEmpty(taskValue[2]) ? null : (String) taskValue[2]);
					taskDto.setDescription(ServicesUtil.isEmpty(taskValue[3]) ? null : (String) taskValue[3]);
					taskDto.setSubject(ServicesUtil.isEmpty(taskValue[4]) ? null : (String) taskValue[4]);
					taskDto.setOwners(ownerIdList);
					taskDto.setProcessId(processId);

					if (futureTaskDetails != null) {
						taskDto.setDescription(ServicesUtil.isEmpty(taskDto.getDescription())
								? futureTaskDetails.getDescription() : taskDto.getDescription());
						taskDto.setSubject(ServicesUtil.isEmpty(taskDto.getSubject()) ? futureTaskDetails.getSubject()
								: taskDto.getSubject());
					}
					taskDto.setStatus("NOTSTARTED");
					if("AFENexus".equalsIgnoreCase(processInstance.getName())){
						Integer amount = customAttributeDao.getAmount(processId);
						if(!ownerIdList.isEmpty() && userMapAFE.containsKey(ownerIdList.get(0)))
						{
							if(userMapAFE.get(ownerIdList.get(0))< amount){
								continue;
							}
						}
					}
					
					map.put(stepNumber, taskDto);
				}

			}

			System.err.println("map"+g.toJson(map));
			if (futureTaskFlag) {
				taskEventsDtos.addAll(map.values());
			}
			System.err.println("taskEventsDtos"+g.toJson(taskEventsDtos));
			// Logic to check the future tasks based on the historical data of
			// the same process instances

			if(!"AFENexus".equalsIgnoreCase(futureTaskDetails.getProcessName())){
				if (!futureTaskDetails.getProcessName().equalsIgnoreCase("ApprovalForAwardforRFQ") ) {

					if (!futureTaskFlag) {

						if (futureTaskDetails != null && !ServicesUtil.isEmpty(futureTaskDetails.getProcessName())) {

							System.err.println(
									"TaskFacade.getTaskDetailsByProcessInstance() " + futureTaskDetails.getProcessName());

							int avgStepsCount = taskEventsDao
									.getTotalTasksCountByProcessName(futureTaskDetails.getProcessName());

							int availableStepsCount = taskEventsDtos.size();

							System.err.println("TaskFacade.getTaskDetailsByProcessInstance() max tasks count : "
									+ avgStepsCount + " available tasks count : " + availableStepsCount);

							if (avgStepsCount != 0 && avgStepsCount > availableStepsCount) {

								for (int c = avgStepsCount - availableStepsCount; c >= 0; c--) {
									TaskEventsDto taskEventsDto = new TaskEventsDto();

									taskEventsDto.setProcessId(futureTaskDetails.getProcessId());
									taskEventsDto.setDescription(futureTaskDetails.getDescription());
									taskEventsDto.setName(futureTaskDetails.getName());
									taskEventsDto.setSubject(futureTaskDetails.getSubject());
									taskEventsDto.setStatus("NOTSTARTED");
									taskEventsDto.setTaskType(futureTaskDetails.getTaskType());
									taskEventsDto.setStatusFlag(futureTaskDetails.getStatusFlag());
									taskEventsDto.setTaskMode(futureTaskDetails.getTaskMode());

									taskEventsDtos.add(taskEventsDto);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println("TaskFacade.getTaskDetailsByProcessInstance() error : " + e);
			e.printStackTrace();
		}

		responseMessage.setMessage("Task Details Fetched Sucessfully");
		responseMessage.setStatus("SUCCESS");
		responseMessage.setStatusCode("1");
		response.setResponseMessage(responseMessage);
		response.setTaskEventDtos(taskEventsDtos);
		return response;
	}

}