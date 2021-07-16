package oneapp.incture.workbox.demo.cai.chatbot.services;

import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatBotResponseDto;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatbotDataRequestDto;

public interface ChatbotService {

	// ResponseMessage createRequest(LeaveRequestDto dto, String headerName,
	// String headerValue);

	// ChatBotResponseDto createRequest(ChatBotRequestDto chatBotRequestDto);

	ChatBotResponseDto getData(ChatbotDataRequestDto chatbotDataRequestDto);

	ChatBotResponseDto action(ChatbotDataRequestDto chatbotDataRequestDto);

	ChatBotResponseDto getInstanceId(ChatbotDataRequestDto chatbotDataRequestDto);

}
