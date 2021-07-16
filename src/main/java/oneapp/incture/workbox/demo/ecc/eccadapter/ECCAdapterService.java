package oneapp.incture.workbox.demo.ecc.eccadapter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import oneapp.incture.workbox.demo.adapter_base.dao.EventsUpdateDao;
import oneapp.incture.workbox.demo.adapter_base.dto.CustomAttributeValueDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.adapter_base.entity.ProcessEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskEventsDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDo;
import oneapp.incture.workbox.demo.adapter_base.entity.TaskOwnersDoPK;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;

@Service
public class ECCAdapterService {

	@Autowired
	EventsUpdateDao eventsUpdateDao;

//	////@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public String updateDataIntoDB(TaskDetailsListDto list, String userId)  {

		String status = PMCConstant.FAILURE;

		try {

			long start=System.currentTimeMillis();
			//			if (ServicesUtil.isEmpty(userId)) {
			//				try {
			//					User user = UserManagementAccessor.getUserProvider().getCurrentUser();
			//					userId = user.getName();
			//				} catch (Exception e) {
			//					System.err.println(
			//							"AdlsaWBAllServiceImpl.updateDataIntoDB() error while getting logged in user : " + e);
			//				}
			//			}

			List<ProcessEventsDo> processList=exportProcessEntities(list);
			List<TaskEventsDo> taskList = exportTaskEntities(list);
			List<TaskOwnersDo> taskOwnerList = exportOwnerEntity(list);
			List<CustomAttributeValue> customAttributes = exportCustomAttributes(list);


			//			Thread processesSaveThread = new Thread(new Runnable() {
			//				@Override
			//				public void run() {
			eventsUpdateDao.saveOrUpdateProcesses(processList);
			//				}
			//			});

			//			Thread taskSaveThread = new Thread(new Runnable() {
			//				@Override
			//				public void run() {
			eventsUpdateDao.saveOrUpdateTasks(taskList);

		//				}
		//			});

		//			Thread ownerSaveThread = new Thread(new Runnable() {
		//				@Override
		//				public void run() {
		eventsUpdateDao.saveOrUpdateOwners(taskOwnerList);
		//				}
		//			});

		//			Thread customAttributesThread = new Thread(new Runnable() {
		//				@Override
		//				public void run() {
		eventsUpdateDao.saveOrUpdateCustomAttributes(customAttributes);
		//				}
		//			});

		//			processesSaveThread.start();
		//			taskSaveThread.start();
		//			ownerSaveThread.start();
		//			customAttributesThread.start();

		System.err.println("[WBP-Dev]ECCAdapterService.updateDataIntoDB() db insertion time : "+(System.currentTimeMillis()-start));
		status = PMCConstant.SUCCESS;
	} catch (Exception e) {
		System.err.println("[WBP-Dev]ECCAdapterService.updateDataIntoDB() error while inserting data : " + e);
	}
	return status;
}

private List<ProcessEventsDo> exportProcessEntities(TaskDetailsListDto list) {

	List<ProcessEventsDo> listDo = new ArrayList<>();
	ProcessEventsDo entity = null;
	if (!ServicesUtil.isEmpty(list) && !ServicesUtil.isEmpty(list.getProcessEventsDto())) {

		try {
			List<ProcessEventsDto> listDto = list.getProcessEventsDto();
			for (ProcessEventsDto fromDto : listDto) {
				entity = new ProcessEventsDo();
				if (!ServicesUtil.isEmpty(fromDto.getProcessId()))
					entity.setProcessId(fromDto.getProcessId());
				if (!ServicesUtil.isEmpty(fromDto.getName()))
					entity.setName(fromDto.getName());
				if (!ServicesUtil.isEmpty(fromDto.getStartedBy()))
					entity.setStartedBy(fromDto.getStartedBy());
				if (!ServicesUtil.isEmpty(fromDto.getStatus()))
					entity.setStatus(fromDto.getStatus());
				if (!ServicesUtil.isEmpty(fromDto.getSubject()))
					entity.setSubject(fromDto.getSubject());
				if (!ServicesUtil.isEmpty(fromDto.getCompletedAt()))
					entity.setCompletedAt(fromDto.getCompletedAt());
				if (!ServicesUtil.isEmpty(fromDto.getStartedAt()))
					entity.setStartedAt(fromDto.getStartedAt());
				if (!ServicesUtil.isEmpty(fromDto.getRequestId()))
					entity.setRequestId(fromDto.getRequestId());
				if (!ServicesUtil.isEmpty(fromDto.getStartedByDisplayName()))
					entity.setStartedByDisplayName(fromDto.getStartedByDisplayName());

				listDo.add(entity);
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev]AdlsaWBAllServiceImpl.exportProcessEntities() " + e);
		}
	}
	return listDo;
}

private List<TaskEventsDo> exportTaskEntities(TaskDetailsListDto list) {
	List<TaskEventsDo> listDo = new ArrayList<>();
	TaskEventsDo taskDo = null;

	if (!ServicesUtil.isEmpty(list) && !ServicesUtil.isEmpty(list.getTaskEvensDto())) {

		try {
			List<TaskEventsDto> taskList = list.getTaskEvensDto();
			for (TaskEventsDto dto : taskList) {
				// System.err.println("[WBP-Dev]AdlsaWBAllServiceImpl.exportTaskEntities()
				// cur_proc : "+dto.getCurrentProcessor());
				taskDo = new TaskEventsDo();
				taskDo.setCompletedAt(dto.getCompletedAt());
				taskDo.setCompletionDeadLine(dto.getCompletionDeadLine());
				taskDo.setCreatedAt(dto.getCreatedAt());
				taskDo.setCurrentProcessor(dto.getCurrentProcessor());
				taskDo.setCurrentProcessorDisplayName(dto.getCurrentProcessorDisplayName());
				taskDo.setDescription(dto.getDescription());
				taskDo.setEventId(dto.getEventId());
				taskDo.setForwardedAt(dto.getForwardedAt());
				taskDo.setForwardedBy(dto.getForwardedBy());
				taskDo.setName(dto.getName());
				taskDo.setOrigin(dto.getOrigin());
				taskDo.setPriority(dto.getPriority());
				taskDo.setProcessId(dto.getProcessId());
				taskDo.setProcessName(dto.getProcessName());
				taskDo.setSlaDueDate(dto.getSlaDueDate());
				taskDo.setStatus(dto.getStatus());
				taskDo.setStatusFlag(dto.getStatusFlag());
				taskDo.setSubject(dto.getSubject());
				taskDo.setTaskMode(dto.getTaskMode());
				taskDo.setTaskType(dto.getTaskType());
				taskDo.setUpdatedAt(dto.getUpdatedAt());
				listDo.add(taskDo);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]AdlsaWBAllServiceImpl.exportTaskEntities() " + e);
		}

	}
	return listDo;
}

private List<TaskOwnersDo> exportOwnerEntity(TaskDetailsListDto list) {
	List<TaskOwnersDo> listDo = new ArrayList<>();
	TaskOwnersDo ownerDo = null;

	if (!ServicesUtil.isEmpty(list) && !ServicesUtil.isEmpty(list.getTaskOwnersDto())) {
		try {
			List<TaskOwnersDto> listDtos = list.getTaskOwnersDto();
			for (TaskOwnersDto dto : listDtos) {
				// System.err.println("[WBP-Dev]AdlsaWBAllServiceImpl.exportOwnerEntity()
				// owner : "+dto.getTaskOwner());
				ownerDo = new TaskOwnersDo();
				TaskOwnersDoPK taskownerPk = new
						TaskOwnersDoPK(dto.getEventId(), dto.getTaskOwner());

				ownerDo.setTaskOwnersDoPK(taskownerPk);
				ownerDo.setTaskOwnerDisplayName(dto.getTaskOwnerDisplayName());
				ownerDo.setOwnerEmail(dto.getOwnerEmail());
				ownerDo.setIsSubstituted(dto.getIsSubstituted());
				ownerDo.setIsProcessed(dto.getIsProcessed());
				ownerDo.setIsReviewer(dto.getIsReviewer());


				listDo.add(ownerDo);
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]AdlsaWBAllServiceImpl.exportOwnerEntity() " + e);
		}
	}
	return listDo;
}
private List<CustomAttributeValue> exportCustomAttributes(TaskDetailsListDto list) {
	List<CustomAttributeValue> customAttributes = new ArrayList<>();
	CustomAttributeValue customAttribute = null;

	if (!ServicesUtil.isEmpty(list) && !ServicesUtil.isEmpty(list.getCustomAttributeValueDto())) {

		try {
			List<CustomAttributeValueDto> customDtos = list.getCustomAttributeValueDto();
			for (CustomAttributeValueDto dto : customDtos) {
				customAttribute = new CustomAttributeValue();
				if (null != dto.getAttributeValue()) {
					customAttribute.setAttributeValue(dto.getAttributeValue().trim());
				}
				customAttribute.setKey(dto.getKey());
				customAttribute.setTaskId(dto.getTaskId());
				customAttribute.setProcessName(dto.getProcessName());

				customAttributes.add(customAttribute);
			}

		} catch (Exception e) {
			System.err.println("[WBP-Dev]AdlsaWBAllServiceImpl.exportCustomAttributes() " + e);
		}
	}

	return customAttributes;
}
}
