package oneapp.incture.workbox.demo.detailpage.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import oneapp.incture.workbox.demo.adapter_base.dao.LayoutAttributesTemplateDao;
import oneapp.incture.workbox.demo.adapter_base.dao.LayoutTemplateDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskEventsDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TaskTemplateDao;
import oneapp.incture.workbox.demo.adapter_base.dao.TemplateTableDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ConfigurationResponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.DetailsPageReponseDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskAuditDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskConfigurationDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskTemplateTableDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
//import oneapp.incture.workbox.demo.chat.dto.ChatHistoryResponse;
//import oneapp.incture.workbox.demo.chat.dto.ChatRequestDto;
//import oneapp.incture.workbox.demo.chat.util.ChatParser;
import oneapp.incture.workbox.demo.detailpage.dao.DetailPageDao;
import oneapp.incture.workbox.demo.detailpage.dao.TaskAuditDao;
import oneapp.incture.workbox.demo.detailpage.dto.CustomDetailDto;
import oneapp.incture.workbox.demo.detailpage.dto.CustomDto;
import oneapp.incture.workbox.demo.detailpage.dto.DynamicButtonDto;
import oneapp.incture.workbox.demo.detailpage.dto.DynamicDetailDto;
import oneapp.incture.workbox.demo.detailpage.dto.DynamicTaskDetailsDto;
import oneapp.incture.workbox.demo.detailpage.dto.HeaderDto;
import oneapp.incture.workbox.demo.taskcontext.util.TaskContextConstant;
import oneapp.incture.workbox.demo.taskcontext.util.TaskTemplateParser;

@Service("detailPageFacade")
public class DetailPageFacade implements DetailPageFacadeLocal{

	@Autowired
	private TaskEventsDao taskEventsDao;

	@Autowired
	private ProcessEventsDao processEventsDao;

	@Autowired
	private DetailPageDao detailPageDao;

//	@Autowired
//	ChatParser chatParse;

	@Autowired
	TaskAuditDao taskAuditDao;

	@Autowired
	TaskTemplateParser taskTemplateParser;

	@Autowired
	LayoutAttributesTemplateDao layoutAttributesTemplateDao;

	@Autowired
	LayoutTemplateDao layoutTemplateDao;

	@Autowired
	TaskTemplateDao taskTemplateDao;

	@Autowired
	TemplateTableDao templateTableDao;

	public DynamicTaskDetailsDto getDynamicDetails(String taskId, String selectType) {
		List<TaskAuditDto> activityDto = null;
		DynamicTaskDetailsDto responseDto= new DynamicTaskDetailsDto();
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yy hh:mm:ss a");
		simpleDateFormat1.setTimeZone(TimeZone.getTimeZone("IST"));
		//		try {
		List<HeaderDto> headerDto = new ArrayList<HeaderDto>();
		TaskEventsDto taskEvent = taskEventsDao.getTaskInstanceIfExists(taskId);
		System.err.println("[WBP-Dev]taskEventsDto: "+taskEvent.toString());
		if (!ServicesUtil.isEmpty(taskEvent) && !ServicesUtil.isEmpty(taskEvent.getProcessName())) {
			String processName = taskEvent.getProcessName();
			String processId = taskEvent.getProcessId();
			String origin = taskEvent.getOrigin();
			if(!TaskContextConstant.TASK_STORY.equals(selectType)){
				ProcessEventsDto processEvent = processEventsDao.getProcessDetail(processId);
				System.err.println("[WBP-Dev]processEvents "+processEvent.toString());
				headerDto.add(new HeaderDto(null, null, "Subject", taskEvent.getSubject()));
				headerDto.add(new HeaderDto(null, null, "Created By", processEvent.getStartedByDisplayName()));
				headerDto.add(new HeaderDto(null, null, "Created At", simpleDateFormat1.format(taskEvent.getCreatedAt()).toString()));
				if("Ad-hoc".equalsIgnoreCase(taskEvent.getOrigin()))
					headerDto.add(new HeaderDto(null, null, "Description", processEvent.getSubject()));
				else
					headerDto.add(new HeaderDto(null, null, "Description", taskEvent.getDescription()));
			}
			CustomDetailDto customDetailDto = detailPageDao.getDetail(processName, taskId, selectType, origin);
			System.err.println("[WBP-Dev]customDetailDto: "+customDetailDto.toString());
			if (!ServicesUtil.isEmpty(customDetailDto)
					&& ("SUCCESS").equalsIgnoreCase(customDetailDto.getResponseMessage().getStatus())) {
				List<DynamicButtonDto> buttonsDto = customDetailDto.getDynamicButtons();
				List<DynamicDetailDto> dynamicDetails = customDetailDto.getDynamicDetails();
				List<CustomDto> contentDto = new ArrayList<CustomDto>();
				for (DynamicDetailDto dd : dynamicDetails) {
					contentDto.add(new CustomDto(dd.getDataType(), dd.getTitle(), dd.getCustomDetails()));
				}


				responseDto.setButtonsDto(buttonsDto);
				responseDto.setContentDto(contentDto);
				responseDto.setHeaderDto(headerDto);

			}

		}

		if(TaskContextConstant.TASK_STORY.equals(selectType)){
//			ChatHistoryResponse chatResponse = chatParse.getChatHistory(new ChatRequestDto(taskId, null));
//			if(!ServicesUtil.isEmpty(chatResponse.getChatMessages().getMessages()))
//			{
//				responseDto.setConversationDto(chatResponse.getChatMessages().getMessages());
//				responseDto.setCurrentPage(chatResponse.getCurrentPage());
//				responseDto.setPageCount(chatResponse.getPageCount());
//				responseDto.setTotalChatCount(chatResponse.getTotalChatCount());
//			}

			activityDto = new ArrayList<TaskAuditDto>();
			activityDto = taskAuditDao.getTaskAudit(taskId);

			responseDto.setActivityDto(activityDto);

		}
		//		} catch (Exception e) {
		//
		//			System.err.println("[WBP-Dev][TaskFacade][getdynamicDetails]"+e.getMessage());
		//		}
		return responseDto;
	}

	@Override
	public DetailsPageReponseDto getDynamicDetailsNew(String taskId) {
		DetailsPageReponseDto detailsPageReponseDto = null;

		try{
			detailsPageReponseDto = detailPageDao.getAttributesDetials(taskId);
		}catch (Exception e) {
			System.err.println("[WBProdcut-Dev]getDynamicDetailsNew error"+e);
		}
		return detailsPageReponseDto;
	}

	@Override
	public TaskConfigurationDto taskConfiguration(String processName, String taskName) {
		TaskConfigurationDto taskConfigurationDto = null;
		try{
			taskConfigurationDto = detailPageDao.getTaskConfiguration(processName, taskName);
		}catch (Exception e) {
			System.err.println("[WBProdcut-Dev]gettaskConfiguration error"+e);
		}
		return taskConfigurationDto;
	}

	@Override
	public ResponseMessage configurationUpdate(TaskTemplateTableDto taskTemplateTableDto) {
		ConfigurationResponseDto responseDto = null;
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessage(PMCConstant.FAILURE);
		responseMessage.setStatus(PMCConstant.FAILURE);
		responseMessage.setStatusCode("1");
		try{
			responseDto =  taskTemplateParser.createResposneToSave(taskTemplateTableDto);
			Gson g = new Gson();
			System.err.println("Response"+g.toJson(responseDto));
//			if(ServicesUtil.isEmpty(responseDto.getTemplateId()) || "".equals(responseDto.getTemplateId())){
				taskTemplateDao.saveOrUpdateTaskTemplateTable(responseDto.getTaskTemplateTableDtos());
				templateTableDao.saveOrUpdateTemplateTable(responseDto.getTemplateTableDtos());
				layoutTemplateDao.saveOrUpdateLayoutTemplate(responseDto.getLayoutTemplateDtos());
				layoutAttributesTemplateDao.saveOrUpdateLayoutAttributeTemplate(responseDto.getLayoutAttributesTemplateDtos());
//			}else{
//				taskTemplateDao.createTaskTemplateTable(responseDto.getTaskTemplateTableDtos(),responseDto.getTemplateId());
//				templateTableDao.createTemplateTable(responseDto.getTemplateTableDtos());
//				layoutTemplateDao.createLayoutTemplate(responseDto.getLayoutTemplateDtos());
//				layoutAttributesTemplateDao.createLayoutAttributeTemplate(responseDto.getLayoutAttributesTemplateDtos());
//			
//			}
			responseMessage.setMessage(PMCConstant.SUCCESS);
			responseMessage.setStatus(PMCConstant.SUCCESS);
			responseMessage.setStatusCode("0");

		}catch (Exception e) {
			System.err.println("[WBProdcut-Dev]configurationUpdate error"+e);
			e.printStackTrace();
		}
		return responseMessage;
	}

	@Override
	public DetailsPageReponseDto dynamicDetailFromContext(String taskId) {
		DetailsPageReponseDto detailsPageReponseDto = null;

		try{
			detailsPageReponseDto = detailPageDao.getAttributesDetialsFromContext(taskId);
		}catch (Exception e) {
			System.err.println("[WBProdcut-Dev]getDynamicDetailsNew error"+e);
		}
		return detailsPageReponseDto;
	}
	
	@Override
	public List<TaskAuditDto> getAuditLogDetails(String taskId, String signature) {
		// TODO Auto-generated method stub
		return taskAuditDao.getAuditLogs(taskId, signature);
	}


}
