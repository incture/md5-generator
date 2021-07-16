package oneapp.incture.workbox.demo.cai.chatbot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatBotResponseDto;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatbotDataRequestDto;
import oneapp.incture.workbox.demo.cai.chatbot.services.ChatbotService;

@RestController
@ComponentScan("oneapp.incture")
@RequestMapping(value = "/workbox/chatbot", produces = "application/json")
public class ChatBotRest {

	@Autowired
	ChatbotService chatbotService;

	@RequestMapping(value = "/getData", method = RequestMethod.POST)
	public ChatBotResponseDto getData(@RequestBody ChatbotDataRequestDto ChatbotDataRequestDto) {
		System.err.println("[WBP-Dev]ChatbotApi.getData() Data : " + ChatbotDataRequestDto);
		ChatBotResponseDto response = new ChatBotResponseDto();
		try {
			response = chatbotService.getData(ChatbotDataRequestDto);
		} catch (Exception e) {

			System.err.println("[WBP-Dev]ChatbotApi.getData() error : " + e);
		}

		return response;
	}

	@RequestMapping(value = "/action", method = RequestMethod.POST)
	public ChatBotResponseDto action(@RequestBody ChatbotDataRequestDto ChatbotDataRequestDto) {
		ChatBotResponseDto response = new ChatBotResponseDto();

		try {

			response = chatbotService.action(ChatbotDataRequestDto);

		} catch (Exception e) {
			System.err.println("[WBP-Dev]ChatbotApi.action() error : " + e.getMessage());
			e.printStackTrace();
		}

		return response;
	}
	
	@RequestMapping(value = "/getInstanceId", method = RequestMethod.POST)
	public ChatBotResponseDto getInstanceId(@RequestBody ChatbotDataRequestDto ChatbotDataRequestDto) {
		ChatBotResponseDto response = new ChatBotResponseDto();

		try {
			response = chatbotService.getInstanceId(ChatbotDataRequestDto);
		} catch (Exception e) {
			System.err.println("[WBP-Dev]ChatbotApi.getInstanceId() error : " + e.getMessage());
			e.printStackTrace();
		}

		return response;
	}

	@RequestMapping(value = "/basicTest", method = RequestMethod.GET, produces = "application/json")
	public String test() {
		return "Success";
	}

}
