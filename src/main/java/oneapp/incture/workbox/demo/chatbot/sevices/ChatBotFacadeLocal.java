package oneapp.incture.workbox.demo.chatbot.sevices;


import oneapp.incture.workbox.demo.chatbot.dto.ChatBotActionRequest;
import oneapp.incture.workbox.demo.chatbot.dto.ChatBotRequestDto;
import oneapp.incture.workbox.demo.chatbot.dto.ChatBotResponseDto;

/**
 * @author Sandhya.R
 *
 */
public interface ChatBotFacadeLocal {
	ChatBotResponseDto getTasksList(ChatBotActionRequest botRequestDto);

	ChatBotResponseDto getTaskDetail(ChatBotRequestDto botRequestDto);

	ChatBotResponseDto getHeatMap(ChatBotRequestDto botRequestDto);

	ChatBotResponseDto takeAction(ChatBotActionRequest botRequestDto);

}
