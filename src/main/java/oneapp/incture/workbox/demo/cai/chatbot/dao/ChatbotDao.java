package oneapp.incture.workbox.demo.cai.chatbot.dao;

import java.util.List;

import oneapp.incture.workbox.demo.adapter_base.entity.CustomAttributeValue;
import oneapp.incture.workbox.demo.cai.chatbot.dto.ChatbotTasksDto;

public interface ChatbotDao {

	ChatbotTasksDto getAllTasksAdmin(String processType, Integer page);

	ChatbotTasksDto getAllTasksOfOwner(String userId, String processType, Integer page, String action);

	List<CustomAttributeValue> getCustomAttributeValuesForInstaceId(String taskId);

}
