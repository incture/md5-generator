package oneapp.incture.workbox.demo.cai.chatbot.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import oneapp.incture.workbox.demo.adapter_base.dao.CustomAttributeDao;
import oneapp.incture.workbox.demo.adapter_base.dao.ProcessConfigDao;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ActionDtoChild;
import oneapp.incture.workbox.demo.adapter_base.dto.ProcessConfigDto;
import oneapp.incture.workbox.demo.adapter_base.dto.ResponseMessage;
import oneapp.incture.workbox.demo.adapter_base.dto.TaskEventsDto;
import oneapp.incture.workbox.demo.adapter_base.util.PMCConstant;
import oneapp.incture.workbox.demo.adapter_base.util.ServicesUtil;
import oneapp.incture.workbox.demo.cai.chatbot.dao.ChatbotDao;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatBotReplyDto;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatBotRequestCoversation;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatBotResponseDto;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatbotDataMemory;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatbotDataRequestDto;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatbotTasksDto;
import oneapp.incture.workbox.demo.inbox.sevices.WorkFlowActionFacadeLocal;

@Service
public class ChatbotServiceImpl implements ChatbotService {

	@Autowired
	ProcessConfigDao processTypeService;
	@Autowired
	ChatbotDao chatbotDao;
	@Autowired
	CustomAttributeDao attrDao;
	/*
	 * @Autowired AdlsaWbTaskDao adlsaWbTaskDao;
	 */
	@Autowired
	WorkFlowActionFacadeLocal actionsService;

//	////@Transactional
	@Override
	public ChatBotResponseDto getData(ChatbotDataRequestDto chatbotDataRequestDto) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(chatbotDataRequestDto);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.err.println("ChatbotServiceImpl.getData() chatbotDataRequestDto "
				+ chatbotDataRequestDto.getRequestType() + " : " + jsonString);

		ChatBotResponseDto response = new ChatBotResponseDto();
		ChatbotTasksDto tasksDto = null;
		String action = "";
		ChatbotDataMemory memory = null;

		if (!ServicesUtil.isEmpty(chatbotDataRequestDto.getMemory())) {
			memory = chatbotDataRequestDto.getMemory();
			action = chatbotDataRequestDto.getMemory().getAction().getValue();

		} else {
			memory = new ChatbotDataMemory();

		}

		List<ChatBotReplyDto> replies = new ArrayList<>();
		ChatBotReplyDto reply = new ChatBotReplyDto();

		List<ProcessConfigDto> processes = new ArrayList<>();
		List<TaskEventsDto> tasks = new ArrayList<>();

		int i = 0;
		// setting default Reply
		reply.setType("text");
		reply.setContent("Error While Getting " + chatbotDataRequestDto.getRequestType() + " Information.");

		try {
			String content = "";
			if (chatbotDataRequestDto.getRequestType().equalsIgnoreCase("Process")) {

				processes = processTypeService.getAllProcessConfigEntry();

				if (!ServicesUtil.isEmpty(processes)) {
					memory.setProcesses(processes);

					for (ProcessConfigDto process : processes) {

						if (!process.getProcessName().equals("ALL")) {
							i++;
							content = content + i + ". " + process.getProcessDisplayName() + "\n";
						}
					}

				} else {
					content = "No Process available .";
				}

				reply.setContent(content);

				System.err.println("[WBP-Dev]ChatbotServiceImpl.getData() inside try reply" + reply);

			} else if (chatbotDataRequestDto.getRequestType().equalsIgnoreCase("Task")) {

				if (!ServicesUtil.isEmpty(chatbotDataRequestDto.getUserInfo())
						&& !ServicesUtil.isEmpty(chatbotDataRequestDto.getUserInfo().getUserId())) {

					// if (chatbotDataRequestDto.getUserInfo().getIsAdmin()) {
					int choice = Integer.parseInt(chatbotDataRequestDto.getMemory().getProcessKey().getScalar());
					System.err.println("ChatbotServiceImpl.getData() user process choice : " + choice);

					if (choice <= chatbotDataRequestDto.getMemory().getProcesses().size() && choice > 0) {
						choice = choice - 1;
						String processType = chatbotDataRequestDto.getMemory().getProcesses().get(choice)
								.getProcessName();
						System.err.println("[WBP-Dev]ChatbotServiceImpl.getData() processType: " + processType);

						// tasksDto = chatbotDao.getAllTasksAdmin(processType,
						// chatbotDataRequestDto.getPage());
						if (!chatbotDataRequestDto.getUserInfo().getIsAdmin()) {
							tasksDto = chatbotDao.getAllTasksOfOwner(chatbotDataRequestDto.getUserInfo().getUserId(),
									processType, chatbotDataRequestDto.getPage(), action);

						} else {
							tasksDto = chatbotDao.getAllTasksAdmin(processType, chatbotDataRequestDto.getPage());
						}
						tasks = tasksDto.getTasks();
						memory.setTotalTasks(tasksDto.getCount());
						if (!ServicesUtil.isEmpty(tasks)) {
							memory.setTasks(tasks);

							if (tasks.size() >= 1) {
								i++;
								content = "1. ALL \n_________________\n";
							}

							for (TaskEventsDto task : tasks) {
								i++;
								content = content + i + ". " + task.getDescription() + "  " + task.getStatus()
										+ "\n______\n";
							}

						} else {
							content = "No tasks available .";
						}

					} else {
						content = "Enter approriate choice .";

					}

				}
			}
			reply.setContent(content);
			response.setConversation(new ChatBotRequestCoversation("en", memory, true));

		} /*
			 * catch (
			 * 
			 * //<<<<<<< Updated upstream // Exception e) { //======= } }
			 */catch (Exception e) {
			// >>>>>>> Stashed changes
			// System.err.println("[WBP-Dev]ChatbotServiceImpl.action()ProcessTypeController.gellAllProcessTypes()
			// Error : " + e);
		}
		replies.add(reply);
		response.setReplies(replies);
		response.setConversation(new ChatBotRequestCoversation("en", memory, true));

		return response;
	}

	@Override
//	////@Transactional
	public ChatBotResponseDto action(ChatbotDataRequestDto chatbotDataRequestDto) {

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(chatbotDataRequestDto);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.err.println("ChatbotServiceImpl.action() chatbotDataRequestDto " + chatbotDataRequestDto.getRequestType()
				+ " : " + jsonString);

		List<String> eventIdList = new ArrayList<>();
		ChatBotResponseDto response = new ChatBotResponseDto();
		List<TaskEventsDto> eventDtos = null;
		List<ChatBotReplyDto> replies = new ArrayList<>();

		List<ActionDtoChild> actionDtoChilds = new ArrayList<>();

		ActionDto actionDto = new ActionDto();
		// List<String> inboxType = new ArrayList<>();
		ChatBotReplyDto reply = new ChatBotReplyDto();
		try {
			eventDtos = chatbotDataRequestDto.getMemory().getTasks();
			// setting default Reply
			reply.setType("text");
			reply.setContent("Error occured while taking action . Please Try Again.");
			TaskEventsDto taskDto = null;
			int choice = Integer.parseInt(chatbotDataRequestDto.getMemory().getTaskKey().getScalar());
			System.err.println("ChatbotServiceImpl.ACTION() user TASK choice : " + choice);

			if (!ServicesUtil.isEmpty(eventDtos) && choice <= chatbotDataRequestDto.getMemory().getTasks().size() + 1
					&& choice > 0) {
				// mass approval
				if (choice == 1) {

					for (TaskEventsDto eventDto : eventDtos) {

						ActionDtoChild childDto = new ActionDtoChild();
						childDto.setActionType(chatbotDataRequestDto.getMemory().getAction().getRaw());
						childDto.setInstanceId(eventDto.getEventId());
						childDto.setProcessType(eventDto.getProcessName());
						childDto.setIsAdmin(chatbotDataRequestDto.getUserInfo().getIsAdmin());

						if ("ECC".equalsIgnoreCase(eventDto.getOrigin())) {

							if ("PurchaseRequisition".equalsIgnoreCase(eventDto.getProcessName())) {

								childDto.setProcessType("PR");
							}

							else if ("PurchaseOrder".equalsIgnoreCase(eventDto.getProcessName())) {

								childDto.setProcessType("PO");
							}

							else if ("PurchaseOrderApprovalProcess".equalsIgnoreCase(eventDto.getProcessName())) {

								childDto.setProcessType("PR");
								childDto.setRelCode("21");
							}

						} else if ("Ariba".equalsIgnoreCase(eventDto.getOrigin())) {
							eventDto.setOrigin("SCP");
						}

						else if ("Salesforce".equalsIgnoreCase(eventDto.getOrigin())) {

							if ("campaignmanagementworkflow".equalsIgnoreCase(eventDto.getProcessName())) {
								eventDto.setOrigin("SCP");
							}

						}

						childDto.setOrigin(eventDto.getOrigin());
						childDto.setSubject(eventDto.getSubject());
						childDto.setProcessLabel(eventDto.getProcessName());
						childDto.setProcessId(eventDto.getProcessId());
						childDto.setComment("");
						actionDtoChilds.add(childDto);

						// eventIdList.add(eventDto.getEventId());
						// processTypes.add(eventDto.getProcessName());
						// approvalKeys.add(eventDto.getProcessId());
						// comments.add("");
						// inboxType.add(chatbotDataRequestDto.getInboxType());
					}
				}

				else {
					taskDto = chatbotDataRequestDto.getMemory().getTasks().get(choice - 2);
					System.err.println("ChatbotServiceImpl.action() task selected" + taskDto);

					ActionDtoChild childDto = new ActionDtoChild();
					childDto.setActionType(chatbotDataRequestDto.getMemory().getAction().getRaw());
					childDto.setInstanceId(taskDto.getEventId());
					childDto.setProcessType(taskDto.getProcessName());

					if ("ECC".equalsIgnoreCase(taskDto.getOrigin())) {

						if ("PurchaseRequisition".equalsIgnoreCase(taskDto.getProcessName())) {

							childDto.setProcessType("PR");
						}

						else if ("PurchaseOrder".equalsIgnoreCase(taskDto.getProcessName())) {

							childDto.setProcessType("PO");
						}

						else if ("PurchaseOrderApprovalProcess".equalsIgnoreCase(taskDto.getProcessName())) {

							childDto.setProcessType("PR");
							childDto.setRelCode("21");
						}

					} else if ("Ariba".equalsIgnoreCase(taskDto.getOrigin())) {
						taskDto.setOrigin("SCP");
					}

					else if ("Salesforce".equalsIgnoreCase(taskDto.getOrigin())) {

						if ("campaignmanagementworkflow".equalsIgnoreCase(taskDto.getProcessName())) {
							taskDto.setOrigin("SCP");
						}

					}

					childDto.setOrigin(taskDto.getOrigin());
					childDto.setSubject(taskDto.getSubject());
					childDto.setProcessLabel(taskDto.getProcessName());
					childDto.setProcessId(taskDto.getProcessId());
					childDto.setIsAdmin(chatbotDataRequestDto.getUserInfo().getIsAdmin());
					childDto.setComment("");
					actionDtoChilds.add(childDto);

					// processTypes.add(taskDto.getProcessName());
					// eventIdList.add(taskDto.getEventId());
					// approvalKeys.add(taskDto.getProcessId());
					// comments.add("");
					// inboxType.add(chatbotDataRequestDto.getInboxType());
				}

				actionDto.setUserId(chatbotDataRequestDto.getUserInfo().getUserId());
				actionDto.setUserDisplay(chatbotDataRequestDto.getUserInfo().getUserDispName());
				actionDto.setInstanceList(eventIdList);
				actionDto.setTask(actionDtoChilds);
				actionDto.setComment("");

				actionDto.setActionType(chatbotDataRequestDto.getMemory().getAction().getRaw());

				actionDto.setHeaderName(chatbotDataRequestDto.getHeadername());
				actionDto.setHeaderValue(chatbotDataRequestDto.getHeaderValue());
				actionDto.setIsChatbot(true);

				ResponseMessage restResponse = new ResponseMessage();
				restResponse.setMessage(PMCConstant.FAILURE);
				restResponse.setStatusCode(PMCConstant.CODE_FAILURE);
				restResponse.setStatus(PMCConstant.FAILURE);

				System.err.println("ChatbotServiceImpl.action() actiondto" + actionDto);
				restResponse = actionsService.taskAction(actionDto,null);
				System.err.println("ChatbotServiceImpl.action() restResponse" + restResponse);

				String responseText = "";
				if (actionDto.getActionType().equalsIgnoreCase(PMCConstant.ACTION_TYPE_APPROVE)) {
					responseText = "approved";
				} else if (actionDto.getActionType().equalsIgnoreCase(PMCConstant.ACTION_TYPE_REJECT)) {
					responseText = "rejected";
				} else if (actionDto.getActionType().equalsIgnoreCase(PMCConstant.ACTION_TYPE_CLAIM)) {
					responseText = "claimed";
				} else if (actionDto.getActionType().equalsIgnoreCase(PMCConstant.ACTION_TYPE_RELEASE)) {
					responseText = "released";
				}
				if (restResponse.getStatus().equalsIgnoreCase(PMCConstant.FAILURE)) {

					reply.setContent("Task " + actionDto.getActionType() + " failed . because " + restResponse);
				} else {
					reply.setContent("Task " + responseText + " successfully .");
				}

			} else {
				reply.setContent("No tasks to selected . Please try again.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("[WBP-Dev]ChatbotServiceImpl.action()  Exception " + e.getMessage());
			reply.setContent(e.getMessage());
		}

		replies.add(reply);
		response.setReplies(replies);
		response.setConversation(new ChatBotRequestCoversation("en", null, true));

		System.err.println("[WBP-Dev] ChatbotServiceImpl.getData() response " + response);
		return response;
	}

	@Override
//	////@Transactional
	public ChatBotResponseDto getInstanceId(ChatbotDataRequestDto chatbotDataRequestDto) {

		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(chatbotDataRequestDto);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		System.err.println("ChatbotServiceImpl.action() chatbotDataRequestDto " + chatbotDataRequestDto.getRequestType()
				+ " : " + jsonString);

		ChatbotDataMemory memory = null;

		if (!ServicesUtil.isEmpty(chatbotDataRequestDto.getMemory())) {
			memory = chatbotDataRequestDto.getMemory();

		} else {
			memory = new ChatbotDataMemory();

		}

		ChatBotResponseDto response = new ChatBotResponseDto();
		List<TaskEventsDto> eventDtos = null;
		List<ChatBotReplyDto> replies = new ArrayList<>();

		ChatBotReplyDto reply = new ChatBotReplyDto();
		try {
			eventDtos = chatbotDataRequestDto.getMemory().getTasks();
			// setting default Reply
			reply.setType("text");
			reply.setContent("Error occured while taking action . Please Try Again.");
			TaskEventsDto taskDto = null;
			int choice = Integer.parseInt(chatbotDataRequestDto.getMemory().getTaskKey().getScalar());
			System.err.println("ChatbotServiceImpl.ACTION() user TASK choice : " + choice);

			if (!ServicesUtil.isEmpty(eventDtos) && choice <= chatbotDataRequestDto.getMemory().getTasks().size() + 1
					&& choice > 1) {

				taskDto = chatbotDataRequestDto.getMemory().getTasks().get(choice - 2);
				System.err.println("ChatbotServiceImpl.action() task selected" + taskDto);
				memory.setInstanceId(taskDto.getEventId());
				reply.setContent("Task Selected");

			}

			else if (choice == 1) {
				reply.setContent("All Task Selected for Mass Approval");
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			System.err.println("[WBP-Dev]ChatbotServiceImpl.action()  Exception " + e.getMessage());
			reply.setContent(e.getMessage());
		}

		replies.add(reply);
		response.setReplies(replies);
		response.setConversation(new ChatBotRequestCoversation("en", memory, true));

		System.err.println("[WBP-Dev] ChatbotServiceImpl.getData() response " + response);
		return response;
	}

}
