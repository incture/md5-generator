package oneapp.incture.workbox.demo.chatbot.sevices;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskOwnersDto;
import oneapp.incture.workbox.demo.adapter_base.dto.WorkBoxDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.chatbot.dao.ChatBotDao;
import oneapp.incture.workbox.demo.chatbot.dto.ChatBotActionConvo;
import oneapp.incture.workbox.demo.chatbot.dto.ChatBotActionRequest;
import oneapp.incture.workbox.demo.chatbot.dto.ChatBotConversationDto;
import oneapp.incture.workbox.demo.chatbot.dto.ChatBotReplyDto;
import oneapp.incture.workbox.demo.chatbot.dto.ChatBotRequestDto;
import oneapp.incture.workbox.demo.chatbot.dto.ChatBotResponseDto;
import oneapp.incture.workbox.demo.chatbot.dto.ChatBotUserDto;
import oneapp.incture.workbox.demo.chatbot.dto.EntityDto;
import oneapp.incture.workbox.demo.dashboard.dto.DashboardDto;
import oneapp.incture.workbox.demo.dashboard.dto.DashboardResponseDto;
import oneapp.incture.workbox.demo.dashboard.dto.TaskNameCountDto;
import oneapp.incture.workbox.demo.dashboard.sevices.DashboardFacadeLocal;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxRequestDto;
import oneapp.incture.workbox.demo.inbox.dto.WorkboxResponseDto;
import oneapp.incture.workbox.demo.inbox.sevices.WorkFlowActionFacade;
import oneapp.incture.workbox.demo.inbox.sevices.WorkboxFacade;
import oneapp.incture.workbox.demo.workload.dao.HeatMapDao;
import oneapp.incture.workbox.demo.workload.dto.UserWorkloadDto;

@Service("ChatBotFacade")
public class ChatBotFacade implements ChatBotFacadeLocal {

	@Autowired
	DashboardFacadeLocal graphFacadeLocal;
	@Autowired
	ChatBotDao botDao;
	@Autowired
	HeatMapDao heatMapDao;
	@Autowired
	WorkFlowActionFacade actionFacade;
	@Autowired
	WorkboxFacade wbFacade;

	@Override
	public ChatBotResponseDto getTaskDetail(ChatBotRequestDto botRequestDto) {
		System.err.println("[WBP-Dev][ChatBotFacade] [getTaskDetail] invoked ");
		String processList = "";
		String taskOwner = "";
		String status = "";
		String timeline = "";
		String reply = "";

		if (!ServicesUtil.isEmpty(botRequestDto.getEntities())) {
			Map<String, List<EntityDto>> map = botRequestDto.getEntities();
			System.err.println("[WBP-Dev][ChatBotFacade] [getTaskDetail]  map " + map);
			for (Entry<String, List<EntityDto>> entry : map.entrySet()) {
				System.err.println(
						"[ChatBotFacade] [getTaskDetail]  key-" + entry.getKey() + " value- " + entry.getKey());
				if (entry.getKey().equalsIgnoreCase("tasktype")) {
					for (EntityDto entityDto : entry.getValue()) {
						if ("active".equalsIgnoreCase(entityDto.getRaw())) {
							status = "'" + PMCConstant.TASK_STATUS_READY + "','" + PMCConstant.TASK_STATUS_RESERVED
									+ "'" + ",'" + PMCConstant.TASK_STATUS_IN_PROGRESS + "','"
									+ PMCConstant.TASK_STATUS_RESOLVED + "'";
						} else if ("open".equalsIgnoreCase(entityDto.getRaw())) {
							status = "'" + PMCConstant.TASK_STATUS_READY + "'";
						} else if ("pending".equalsIgnoreCase(entityDto.getRaw())) {
							status = "'" + PMCConstant.TASK_STATUS_RESERVED + "'";
						}
					}
				}
				if (entry.getKey().equalsIgnoreCase(PMCConstant.PROCESS_NAME)) {
					for (EntityDto entityDto : entry.getValue()) {
						processList = processList + "'" + entityDto.getRaw() + "',";
					}
					processList = processList.substring(0, processList.length() - 1);
				}
				if (entry.getKey().equalsIgnoreCase(PMCConstant.TASK_OWNER)) {
					for (EntityDto entityDto : entry.getValue()) {
						taskOwner = taskOwner + "'" + entityDto.getRaw() + "',";
					}
					taskOwner = taskOwner.substring(0, taskOwner.length() - 1);
				}
				if (entry.getKey().equalsIgnoreCase(PMCConstant.STATUS)) {
					for (EntityDto entityDto : entry.getValue()) {
						status = status + "'" + entityDto.getRaw().toUpperCase() + "',";
					}
					status = status.substring(0, status.length() - 1);
				}
				if (entry.getKey().equalsIgnoreCase(PMCConstant.TIME_LINE)) {
					for (EntityDto entityDto : entry.getValue()) {
						if ("sla breached".equalsIgnoreCase(entityDto.getRaw())) {
							timeline = " CURRENT_TIMESTAMP > comp_deadline ";
						}
						if ("In time".equalsIgnoreCase(entityDto.getRaw())) {
							timeline = " CURRENT_TIMESTAMP < ADD_SECONDS(comp_deadline, - (" + PMCConstant.CRITICAL_TIME
									+ ")) ";
						}
						if ("critical".equalsIgnoreCase(entityDto.getRaw())) {
							timeline = " current_timestamp BETWEEN comp_deadline AND ADD_SECONDS(comp_deadline,- ("
									+ PMCConstant.CRITICAL_TIME + ")) ";
						}
					}
				}
			}
			System.err.println("[WBP-Dev]processName" + processList + "status " + status + "taskOwner " + taskOwner + "timeline "
					+ timeline);
			reply = botDao.getDetails(processList, status, taskOwner, timeline);

		} else {
			DashboardResponseDto dto = graphFacadeLocal.getGraphDetails("All", "", "month",
					botRequestDto.getUserDto().getUserId(),1,1,null);
			DashboardDto graphDto = dto.getGraphDto();
			for (TaskNameCountDto countDto : graphDto.getTaskDonutList()) {
				reply = reply + countDto.getStrName() + " - " + countDto.getStatus() + " - " + countDto.getTaskCount()
						+ "\n";
			}
		}

		ChatBotResponseDto response = new ChatBotResponseDto();
		ChatBotReplyDto botReplyDto = new ChatBotReplyDto();
		List<ChatBotReplyDto> botReplyList = new ArrayList<ChatBotReplyDto>();
		botReplyDto.setType("text");
		botReplyDto.setContent(reply);
		botReplyList.add(botReplyDto);
		response.setReplies(botReplyList);
		ChatBotConversationDto botConversationDto = new ChatBotConversationDto();
		botConversationDto.setLanguage("en");

		botConversationDto.setMemory(botRequestDto);
		System.err.println("[WBP-Dev][ChatBot] response" + response);
		return response;

	}

	@Override
	public ChatBotResponseDto getHeatMap(ChatBotRequestDto botRequestDto) {

		String status = "";
		String process = "";
		String reply = "";
		if (!ServicesUtil.isEmpty(botRequestDto)) {
			if (!ServicesUtil.isEmpty(botRequestDto.getEntities())) {
				Map<String, List<EntityDto>> map = botRequestDto.getEntities();
				System.err.println("[WBP-Dev][ChatBotFacade] [getHeatMap]  map " + map);
				for (Entry<String, List<EntityDto>> entry : map.entrySet()) {
					if (entry.getKey().equalsIgnoreCase(PMCConstant.STATUS)) {
						for (EntityDto entityDto : entry.getValue()) {
							status = entityDto.getRaw();
						}
					}
					if (entry.getKey().equalsIgnoreCase(PMCConstant.PROCESS_NAME)) {
						for (EntityDto entityDto : entry.getValue()) {
							process = entityDto.getRaw();
						}
					}
				}
			} else {
				process = "ALL";
				status = "ALL";
			}
			List<UserWorkloadDto> dtos = heatMapDao.getUserWorkload(process, "", status);
			for (UserWorkloadDto userWorkloadDto : dtos) {
				reply = reply + userWorkloadDto.getUserName() + " (" + userWorkloadDto.getUserId() + ") -"
						+ userWorkloadDto.getNoOfTask() + "\n";
			}

		}
		ChatBotResponseDto response = new ChatBotResponseDto();
		ChatBotReplyDto botReplyDto = new ChatBotReplyDto();
		List<ChatBotReplyDto> botReplyList = new ArrayList<ChatBotReplyDto>();
		botReplyDto.setType("text");
		botReplyDto.setContent(reply);
		botReplyList.add(botReplyDto);
		response.setReplies(botReplyList);
		ChatBotConversationDto botConversationDto = new ChatBotConversationDto();
		botConversationDto.setLanguage("en");

		botConversationDto.setMemory(botRequestDto);
		System.err.println("[WBP-Dev][ChatBot] response" + response);
		return response;

	}

	public WorkboxRequestDto convertCBRequestToWBRequest(ChatBotActionRequest botRequestDto) {
		WorkboxRequestDto workboxRequestDto = new WorkboxRequestDto();
		String action="";
		if (!ServicesUtil.isEmpty(botRequestDto)) {
			Map<String, EntityDto> map = botRequestDto.getMemory();
			EntityDto dto = new EntityDto();
			System.err.println("[WBP-Dev][ChatBotFacade] [convertCBRequestToWBRequest]  map " + map);
			for (Entry<String, EntityDto> entry : map.entrySet()) {
				System.err.println("[WBP-Dev][ChatBotFacade] [convertCBRequestToWBRequest]  key-" + entry.getKey() + " value- "
						+ entry.getKey());
				dto = entry.getValue();
				if (entry.getKey().equalsIgnoreCase("action")) {
					action = dto.getRaw();
				} else if (entry.getKey().equalsIgnoreCase("processname")) {
					String processName = dto.getRaw();
					;
					ArrayList<String> processList = new ArrayList<String>();
					processList.add(processName);
					workboxRequestDto.setProcessName(processList);
				} else if (entry.getKey().equalsIgnoreCase("status")) {
					String status = dto.getRaw();
					;
					workboxRequestDto.setStatus(status);
				} else if (entry.getKey().equalsIgnoreCase("origin")) {
					String origin = dto.getRaw();
					;
					workboxRequestDto.setOrigin(origin);
				} else if (entry.getKey().equalsIgnoreCase("sendTo")) {
					@SuppressWarnings("unused")
					String sendTo = dto.getRaw();
					;

				}
			}
            if("claim".equalsIgnoreCase(action))
            {
            	workboxRequestDto.setStatus("READY");
            }
            else if("release".equalsIgnoreCase(action))
            {
            	workboxRequestDto.setStatus("RESERVED");
            }
			ChatBotUserDto userDto = botRequestDto.getUserDto();
			if (userDto.getIsAdmin().equalsIgnoreCase("true"))
				workboxRequestDto.setIsAdmin(true);
			else
				workboxRequestDto.setIsAdmin(false);
			TaskOwnersDto currentUserInfo = new TaskOwnersDto();
			currentUserInfo.setTaskOwner(botRequestDto.getUserDto().getUserId());

			workboxRequestDto.setCurrentUserInfo(currentUserInfo);
			workboxRequestDto.setInboxType("myTask");
		}
		return workboxRequestDto;

	}

	@SuppressWarnings("unused")
	@Override
	public ChatBotResponseDto takeAction(ChatBotActionRequest botRequestDto) {
		System.err.println("[WBP-Dev][ChatBotFacade] [takeAction] ChatBotActionRequest" + botRequestDto);
		WorkboxResponseDto workboxResponseDto = botRequestDto.getWorkboxResponseDto();
		List<String> instanceList = null;
		String botReply = "";
		ActionDto actionDto = new ActionDto();
		String taskkey = "";
		if (!ServicesUtil.isEmpty(botRequestDto)) {
			Map<String, EntityDto> map = botRequestDto.getMemory();
			EntityDto dto = new EntityDto();
			System.err.println("[WBP-Dev][ChatBotFacade] [takeAction]  map " + map);
			for (Entry<String, EntityDto> entry : map.entrySet()) {
				System.err
						.println("[ChatBotFacade] [takeAction]  key-" + entry.getKey() + " value- " + entry.getValue());
				dto = entry.getValue();
				if (entry.getKey().equalsIgnoreCase("action")) {
					actionDto.setAction(dto.getRaw());
					actionDto.setActionType(dto.getRaw());
					taskkey = dto.getScalar();
				} else if (entry.getKey().equalsIgnoreCase("origin")) {
					actionDto.setOrigin(dto.getRaw());
				} else if (entry.getKey().equalsIgnoreCase("sendTo")) {
					String sendTo = dto.getRaw();
					;
					if (ServicesUtil.isEmpty(sendTo)) {
						actionDto.setSendToUser(sendTo);
					}
				}
			}
		}

		List<WorkBoxDto> boxDtos = getTasksListForAction(botRequestDto);

		if (!ServicesUtil.isEmpty(boxDtos)) {
			instanceList = new ArrayList<String>();
			if ("1".equalsIgnoreCase(taskkey)) {
				for (WorkBoxDto workBoxDto : boxDtos) {
					instanceList.add(workBoxDto.getTaskId());
				}
			} else {
				instanceList.add(boxDtos.get(Integer.parseInt(taskkey) - 2).getTaskId());
			}

			actionDto.setInstanceList(instanceList);
//			if (botRequestDto.getUserDto().getIsAdmin().equalsIgnoreCase("true"))
//				actionDto.setIsAdmin(true);
//			else
//				actionDto.setIsAdmin(false);
			actionDto.setUserId(botRequestDto.getUserDto().getUserId());
			actionDto.setUserDisplay(botRequestDto.getUserDto().getUserName());
			System.err.println("[WBP-Dev][ChatBotFacade] [takeAction]  actionDto " + actionDto);
			ResponseMessage responseMessage = actionFacade.taskAction(actionDto,null);
			botReply = responseMessage.getMessage();
		} else {
			botReply = "No tasks in your Inbox";
		}

		// setting user info

		ChatBotResponseDto botResponseDto = new ChatBotResponseDto();
		ChatBotReplyDto botReplyDto = new ChatBotReplyDto();
		List<ChatBotReplyDto> botReplyList = new ArrayList<ChatBotReplyDto>();
		botReplyDto.setType("text");
		botReplyDto.setContent(botReply);
		botReplyList.add(botReplyDto);
		botResponseDto.setReplies(botReplyList);
		ChatBotActionConvo botConversationDto = new ChatBotActionConvo();
		botConversationDto.setMemory(botRequestDto);
		botConversationDto.setLanguage("en");
		System.err.println("[WBP-Dev][ChatBotFacade] [takeAction] botResponseDto" + botResponseDto);
		return botResponseDto;
	}

	@Override
	public ChatBotResponseDto getTasksList(ChatBotActionRequest botRequestDto) {
		System.err.println("[WBP-Dev][ChatBotFacade] [getTasksList] botRequestDto" + botRequestDto);
		WorkboxRequestDto workboxRequestDto = convertCBRequestToWBRequest(botRequestDto);
		WorkboxResponseDto workboxResponseDto = wbFacade.getWorkboxFilterData(workboxRequestDto, false, null);
		System.err.println("[WBP-Dev][ChatBotFacade] [getTasksList] workboxResponseDto" + workboxResponseDto);
		botRequestDto.setWorkboxResponseDto(workboxResponseDto);
		String botReply = "";
		List<WorkBoxDto> boxDtos = workboxResponseDto.getWorkBoxDtos();
		int i = 0;
		if (!ServicesUtil.isEmpty(boxDtos)) {
			if (boxDtos.size() > 1) {
				i++;
				botReply = "1. All \n_________________\n";
			}
			for (WorkBoxDto boxDto : boxDtos) {
				i++;
				botReply = botReply + i + ". " + boxDto.getSubject() + "  " + boxDto.getStatus() + "  "
						+ boxDto.getRequestId() + "\n_________________\n";
			}
		} else {
			botReply = "No tasks in your Inbox";
		}

		ChatBotResponseDto botResponseDto = new ChatBotResponseDto();
		ChatBotReplyDto botReplyDto = new ChatBotReplyDto();
		List<ChatBotReplyDto> botReplyList = new ArrayList<ChatBotReplyDto>();
		botReplyDto.setType("text");
		botReplyDto.setContent(botReply);
		botReplyList.add(botReplyDto);
		botResponseDto.setReplies(botReplyList);
		ChatBotActionConvo botConversationDto = new ChatBotActionConvo();
		System.err.println("[WBP-Dev][ChatBotFacade] [getTasksList] botResponseDto" + botResponseDto);
		botConversationDto.setMemory(botRequestDto);
		botConversationDto.setLanguage("en");
		System.err.println("[WBP-Dev][ChatBotFacade] [getTasksList] botResponseDto" + botResponseDto);
		return botResponseDto;
	}

	public List<WorkBoxDto> getTasksListForAction(ChatBotActionRequest botRequestDto) {
		System.err.println("[WBP-Dev][ChatBotFacade] [getTasksList] botRequestDto" + botRequestDto);
		WorkboxRequestDto workboxRequestDto = convertCBRequestToWBRequest(botRequestDto);
		WorkboxResponseDto workboxResponseDto = wbFacade.getWorkboxFilterData(workboxRequestDto, false, null);
		System.err.println("[WBP-Dev][ChatBotFacade] [getTasksList] workboxResponseDto" + workboxResponseDto);
		botRequestDto.setWorkboxResponseDto(workboxResponseDto);
		List<WorkBoxDto> boxDtos = workboxResponseDto.getWorkBoxDtos();
		return boxDtos;
	}

}
